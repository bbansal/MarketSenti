package com.marketsenti.storage;

import java.util.Iterator;


/**
 * Base interface for all storage requirements
 * 
 * @author bbansal
 * 
 */
public interface StorageEngine
{
  public StoreSchema createStore(String storename, Class keyClass, Class valueClass);

  public StoreSchema getSchema(String storename);

  public boolean dropStore(String storename);

  public <K, V> Iterator<V> getValues(String storename, K key);

  public <K, V> void putValue(String storename, K key, Iterator<V> values, long timestamp);

  public <K, V> void appendValue(String storename, K key, V value, long timestamp);

  public <K, V> void removeEntry(String storename, K key);
  
  public <K,V> Iterator<StoreEntry<K, V>> scanStore(String storename);
  
  public <K,V> Iterator<StoreEntry<K, V>> getUpdatesSince(String storename, long timestamp);
}
