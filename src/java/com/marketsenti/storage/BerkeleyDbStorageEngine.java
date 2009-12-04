package com.marketsenti.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Lists;
import com.marketsenti.storage.serializer.BytesSerializer;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.EnvironmentLockedException;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.Transaction;

public class BerkeleyDbStorageEngine implements StorageEngine
{
  private final Logger                          logger                =
                                                                          Logger.getLogger(BerkeleyDbStorageEngine.class);
  private final String                          dataDir;
  private final Properties                      props                 = new Properties();
  private final EnvironmentConfig               environmentConfig;
  private final DatabaseConfig                  databaseConfig;
  private final Map<String, Environment>        environments          =
                                                                          Collections.synchronizedMap(new HashMap<String, Environment>());
  private final Map<String, Database>           stores                =
                                                                          Collections.synchronizedMap(new HashMap<String, Database>());

  private final Map<String, StoreSchema>        storeSchemas          =
                                                                          Collections.synchronizedMap(new HashMap<String, StoreSchema>());

  private final Map<String, BytesSerializer<?>> keySerializers        =
                                                                          Collections.synchronizedMap(new HashMap<String, BytesSerializer<?>>());
  private final Map<String, BytesSerializer<?>> valueSerializers      =
                                                                          Collections.synchronizedMap(new HashMap<String, BytesSerializer<?>>());

  private final long                            bdbCacheSize          = 5 * 1024 * 1024;
  private final int                             bdbMinFileUtilization = 80;

  public BerkeleyDbStorageEngine(String dataDir, String propsFile) throws FileNotFoundException,
      IOException
  {
    if (null != propsFile && new File(propsFile).exists())
      props.load(new FileInputStream(propsFile));

    environmentConfig = createEnvironmentConfig();
    databaseConfig = createDatabaseConfig();
    this.dataDir = dataDir;
  }

  public BerkeleyDbStorageEngine(String dataDir, Map<String,String> properties) throws FileNotFoundException,
      IOException
  {
    for (Entry<String, String> entry: properties.entrySet())
    {
      props.setProperty(entry.getKey(), entry.getValue());
    }
    environmentConfig = createEnvironmentConfig();
    databaseConfig = createDatabaseConfig();
    this.dataDir = dataDir;
  }

  private EnvironmentConfig createEnvironmentConfig()
  {
    EnvironmentConfig environmentConfig = new EnvironmentConfig();

    environmentConfig.setTransactional(true);
    environmentConfig.setCacheSize(Long.parseLong(props.getProperty("je.maxMemory", ""
        + bdbCacheSize)));
    environmentConfig.setTxnNoSync(true);
    environmentConfig.setAllowCreate(true);
    environmentConfig.setConfigParam(EnvironmentConfig.CLEANER_MIN_UTILIZATION,
                                     props.getProperty("bdb.min.file.utilization", ""
                                         + bdbMinFileUtilization));
    environmentConfig.setSharedCache(true);

    return environmentConfig;
  }

  private DatabaseConfig createDatabaseConfig()
  {
    DatabaseConfig databaseConfig = new DatabaseConfig();
    databaseConfig.setAllowCreate(true);
    databaseConfig.setTransactional(true);
    return databaseConfig;
  }

  private void closeCursor(Cursor cursor)
  {
    try
    {
      if (null != cursor)
        cursor.close();
    }
    catch (DatabaseException e)
    {
      throw new RuntimeException("Error while closing cursor" + cursor);
    }
  }

  private void attemptCommit(Transaction t)
  {
    try
    {
      t.commit();
    }
    catch (DatabaseException e)
    {
      throw new RuntimeException("Failed while transaction commit.", e);
    }
  }

  private void attemptAbort(Transaction t)
  {
    try
    {
      t.abort();
    }
    catch (DatabaseException e)
    {
      throw new RuntimeException("Failed while transaction abort.", e);
    }
  }

  private <K, V> void putValueList(String storename, K key, Iterator<V> values)
  {
    DatabaseEntry keyEntry =
        new DatabaseEntry(((BytesSerializer<K>) keySerializers.get(storename)).getBytes(key));
    boolean succeeded = false;
    Transaction transaction = null;
    Cursor cursor = null;
    try
    {
      transaction = environments.get(storename).beginTransaction(null, null);
      cursor = stores.get(storename).openCursor(transaction, null);
      while (values.hasNext())
      {
        DatabaseEntry valueEntry =
            new DatabaseEntry(((BytesSerializer<V>) valueSerializers.get(storename)).getBytes(values.next()));
        OperationStatus status = cursor.put(keyEntry, valueEntry);
        if (status != OperationStatus.SUCCESS)
          throw new RuntimeException("Put operation failed with status: " + status);
      }
      succeeded = true;
    }
    catch (DatabaseException e)
    {
      logger.error(e);
      throw new RuntimeException("Failed while putting values", e);
    }
    finally
    {
      closeCursor(cursor);
      if (succeeded)
        attemptCommit(transaction);
      else
        attemptAbort(transaction);
    }
  }

  synchronized public Environment getEnvironment(String storeName) throws EnvironmentLockedException,
      DatabaseException
  {
    if (environments.containsKey(storeName))
      return environments.get(storeName);

    // otherwise create a new environment
    File storeDir = new File(this.dataDir, storeName);
    if (!storeDir.exists() || !storeDir.isDirectory())
    {
      storeDir.delete();
      storeDir.mkdirs();
    }

    Environment environment = new Environment(storeDir, environmentConfig);
    logger.info("Creating environment for " + storeName + ": ");
    environments.put(storeName, environment);
    return environment;
  }

  @Override
  public <K, V> StoreSchema createStore(String storeName,
                                        Class keyClass,
                                        Class valueClass,
                                        BytesSerializer<K> keyserializer,
                                        BytesSerializer<V> valueSerializer)
  {
    if (stores.containsKey(storeName))
      throw new RuntimeException("store " + storeName + " already exists !");

    if (!(keyClass.getName().equals(String.class.getName()) && valueClass.getName()
                                                                         .equals(String.class.getName())))
      throw new RuntimeException("bdbStore supports <String,String> for now keyClass:"
          + keyClass.getName() + " valueClass:" + valueClass.getName());

    try
    {
      Environment environment = getEnvironment(storeName);
      Database db = environment.openDatabase(null, storeName, databaseConfig);
      stores.put(storeName, db);
      StoreSchema schema = new StoreSchema(keyClass, valueClass);
      storeSchemas.put(storeName, schema);
      keySerializers.put(storeName, keyserializer);
      valueSerializers.put(storeName, valueSerializer);
      return schema;
    }
    catch (Exception e)
    {
      throw new RuntimeException("Failed to open Berkeley Db Store.", e);
    }
  }

  @Override
  public boolean dropStore(String storename)
  {
    if (!stores.containsKey(storename))
      throw new RuntimeException("Store " + storename + " does not exist !");
    try
    {
      // close database/environment
      stores.get(storename).close();
      environments.get(storename).close();
    }
    catch (Exception e)
    {
      throw new RuntimeException("Error while closing berkeley db database/environments",
                                 e);
    }

    storeSchemas.remove(storename);
    stores.remove(storename);
    environments.remove(storename);
    keySerializers.remove(storename);
    valueSerializers.remove(storename);

    return true;
  }

  @Override
  public StoreSchema getSchema(String storename)
  {
    return storeSchemas.get(storename);
  }

  @Override
  public <K, V> Iterator<StoreEntry<K, V>> getUpdatesSince(String storename,
                                                           long timestamp)
  {
    throw new RuntimeException("Not implemented yet !!");
  }

  /**
   * We save valus as K, V inside berkeley db.
   */
  @SuppressWarnings("unchecked")
  @Override
  public <K, V> Iterator<V> getValues(String storename, K key)
  {
    Cursor cursor = null;
    try
    {
      DatabaseEntry keyEntry =
          new DatabaseEntry(((BytesSerializer<K>) keySerializers.get(storename)).getBytes(key));
      DatabaseEntry valueEntry = new DatabaseEntry();
      List<V> valueList = new ArrayList<V>();
      cursor = stores.get(storename).openCursor(null, null);

      for (OperationStatus status =
          cursor.getSearchKey(keyEntry, valueEntry, LockMode.READ_UNCOMMITTED); status == OperationStatus.SUCCESS; status =
          cursor.getNextDup(keyEntry, valueEntry, LockMode.READ_UNCOMMITTED))
      {
        valueList.add(((BytesSerializer<V>) valueSerializers).fromBytes(valueEntry.getData()));
      }

      return valueList.iterator();
    }
    catch (Exception e)
    {
      throw new RuntimeException("Error while getting values for storename:" + storename
          + " key:" + key);
    }
    finally
    {
      closeCursor(cursor);
    }
  }

  @Override
  public <K, V> void appendValue(String storename, K key, V value, long timestamp)
  {
    putValueList(storename, key, Arrays.asList(value).iterator());
  }

  @Override
  public <K, V> void putValue(String storename, K key, Iterator<V> values, long timestamp)
  {
    putValueList(storename, key, values);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <K, V> void removeEntry(String storename, K key)
  {
    Cursor cursor = null;
    Transaction transaction = null;
    try
    {
      transaction = environments.get(storename).beginTransaction(null, null);
      DatabaseEntry keyEntry =
          new DatabaseEntry(((BytesSerializer<K>) keySerializers.get(storename)).getBytes(key));
      DatabaseEntry valueEntry = new DatabaseEntry();
      cursor = stores.get(storename).openCursor(transaction, null);
      OperationStatus status =
          cursor.getSearchKey(keyEntry, valueEntry, LockMode.READ_UNCOMMITTED);
      while (status == OperationStatus.SUCCESS)
      {
        cursor.delete();
        status = cursor.getNextDup(keyEntry, valueEntry, LockMode.READ_UNCOMMITTED);
      }
    }
    catch (DatabaseException e)
    {
      logger.error(e);
      throw new RuntimeException("Failed while deleting stuff !!", e);
    }
    finally
    {
      try
      {
        closeCursor(cursor);
      }
      finally
      {
        attemptCommit(transaction);
      }
    }
  }

  @Override
  public <K, V> Iterator<StoreEntry<K, V>> scanStore(final String storename)
  {
    return new AbstractIterator<StoreEntry<K, V>>()
    {

      Cursor cursor = null;

      @Override
      protected StoreEntry<K, V> computeNext()
      {
        DatabaseEntry keyEntry = new DatabaseEntry();
        DatabaseEntry valueEntry = new DatabaseEntry();

        if (null == cursor)
        {
          cursor = initializeCursor(storename);
          return getNextEntry(cursor, keyEntry, valueEntry, true);
        }

        return null;
      }

      private StoreEntry<K, V> getNextEntry(Cursor cursor,
                                            DatabaseEntry keyEntry,
                                            DatabaseEntry valueEntry,
                                            boolean firstEntry)
      {
        try
        {
          if (firstEntry)
            cursor.getFirst(keyEntry, valueEntry, LockMode.READ_UNCOMMITTED);
          else
            cursor.getNext(keyEntry, valueEntry, LockMode.READ_UNCOMMITTED);
        }
        catch (DatabaseException e)
        {
          logger.error(e);
          throw new RuntimeException("Failed to move cursor.", e);
        }

        if (keyEntry.getData() == null)
          return endOfData();

        BytesSerializer<K> keySerializer =
            (BytesSerializer<K>) keySerializers.get(storename);
        BytesSerializer<V> valueSerializer =
            (BytesSerializer<V>) valueSerializers.get(storename);

        return new StoreEntry<K, V>(keySerializer.fromBytes(keyEntry.getData()),
                                    valueSerializer.fromBytes(valueEntry.getData()));
      }

      private Cursor initializeCursor(String storename)
      {
        try
        {
          return stores.get(storename).openCursor(null, null);
        }
        catch (Exception e)
        {
          throw new RuntimeException("Failed to open curson in scanStore.", e);
        }
      }
    };

  }
}
