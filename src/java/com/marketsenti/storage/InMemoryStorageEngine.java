package com.marketsenti.storage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.AbstractIterator;
import com.marketsenti.storage.serializer.BytesSerializer;

/**
 * Basic InMemory storage Engine implements {@link StorageEngine}<br>
 * Does not persist data keep only in RAM and will get cleaned up at the end of JVM<br>
 * useful for prototyping and testing.
 * 
 * @author bbansal
 * 
 */
public class InMemoryStorageEngine implements StorageEngine
{

  private final Map<String, Map<Object, List<Object>>> storageEngineMap;
  private final Map<String, StoreSchema>               storeSchemaMap;

  public InMemoryStorageEngine()
  {
    // thread safe
    storageEngineMap =
        Collections.synchronizedMap(new HashMap<String, Map<Object, List<Object>>>());
    storeSchemaMap = Collections.synchronizedMap(new HashMap<String, StoreSchema>());
  }

  public <K, V> StoreSchema createStore(String storename,
                                        Class keyClass,
                                        Class valueClass,
                                        BytesSerializer<K> Keyserializer,
                                        BytesSerializer<V> valueSerializer)
  {
    if (storageEngineMap.containsKey(storename))
    {
      throw new RuntimeException("store already exists:" + storename);
    }

    // create new thread-safe map for this store.
    StoreSchema schema = new StoreSchema(keyClass, valueClass);
    Map<Object, List<Object>> storeMap =
        Collections.synchronizedMap(new HashMap<Object, List<Object>>());

    storeSchemaMap.put(storename, schema);
    storageEngineMap.put(storename, storeMap);

    return schema;
  }

  synchronized public boolean dropStore(String storename)
  {
    if (!storageEngineMap.containsKey(storename))
    {
      throw new RuntimeException("store donot exists:" + storename);
    }

    storeSchemaMap.remove(storename);
    storageEngineMap.remove(storename);

    return true;
  }

  public StoreSchema getSchema(String storename)
  {
    return storeSchemaMap.get(storename);
  }

  public <K, V> void appendValue(String storename, K key, V value, long timestamp)
  {
    StoreSchema schema = this.getSchema(storename);
    checkSchema(key, value, schema);

    List<V> values = (List<V>) getStore(storename).get(key);
    values.add(value);
  }

  private <K, V> void checkSchema(K key, V value, StoreSchema schema)
  {
    if (!schema.getKeySchema().equals(key.getClass()))
    {
      throw new RuntimeException("Mismatched key type expected:" + schema.getKeySchema()
          + " got:" + key.getClass());
    }

    if (value != null && !schema.getValueSchema().equals(value.getClass()))
    {
      throw new RuntimeException("Mismatched value type expected:"
          + schema.getValueSchema() + " got:" + value.getClass());
    }
  }

  public <K, V> Iterator<StoreEntry<K, V>> getUpdatesSince(String storename,
                                                           long timestamp)
  {
    throw new RuntimeException("Not implemented yet.");
  }

  public <K, V> Iterator<V> getValues(String storename, K key)
  {
    return (Iterator<V>) getStore(storename).get(key).iterator();
  }

  public <K, V> void putValue(String storename, K key, Iterator<V> values, long timestamp)
  {
    StoreSchema schema = this.getSchema(storename);
    V firstValue = (values.hasNext()) ? values.next() : null;
    checkSchema(key, firstValue, schema);
    
    List<Object> valuesList = new ArrayList<Object>();
    valuesList.add(firstValue); // FIXME :  any checks required
    while (values.hasNext())
    {
      valuesList.add(values.next());
    }

    getStore(storename).put(key, valuesList);
  }

  public <K, V> void removeEntry(String storename, K key)
  {
    getStore(storename).remove(key);
  }

  public <K, V> Iterator<StoreEntry<K, V>> scanStore(final String storename)
  {
    return new AbstractIterator<StoreEntry<K, V>>()
    {

      final Iterator<Entry<Object, List<Object>>> storeEntriesIterator =
                                                                           getStore(storename).entrySet()
                                                                                              .iterator();

      K                                           currentKey           = null;
      Iterator<V>                                 currentValueIterator = null;

      @SuppressWarnings("unchecked")
      @Override
      protected StoreEntry<K, V> computeNext()
      {
        if (!storeEntriesIterator.hasNext())
        {
          return endOfData();
        }

        // loop while we get a valid value
        while (null == currentValueIterator || !currentValueIterator.hasNext())
        {
          Entry<Object, List<Object>> entry = storeEntriesIterator.next();
          currentKey = (K) entry.getKey();
          currentValueIterator = (Iterator<V>) entry.getValue().iterator();
        }

        return new StoreEntry<K, V>(currentKey, currentValueIterator.next());
      }
    };
  }

  private Map<Object, List<Object>> getStore(String storename)
  {
    return storageEngineMap.get(storename);
  }
}
