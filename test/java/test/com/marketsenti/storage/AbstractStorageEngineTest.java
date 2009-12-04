package test.com.marketsenti.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import com.marketsenti.storage.StorageEngine;
import com.marketsenti.storage.StoreSchema;
import com.marketsenti.storage.serializer.StringSerializer;

public abstract class AbstractStorageEngineTest extends TestCase
{
  
  protected abstract StorageEngine getStorageEngine();
  
  private  Map<String, List<String>> generateTestData(int numKeys)
  {
    Map<String, List<String>> testData = new HashMap<String, List<String>>();
    for (int i = 0; i < numKeys; i++)
    {   
      int listSize = (int) (Math.random() * 10);
      String key = generateRandomString();
      List<String> list = new ArrayList<String>(listSize);
      for (int j = 0 ; j < listSize ; j++)
      {
        list.add(generateRandomString());
      }
      
      testData.put(key, list);
    }
    
    return testData;
  }

  private String generateRandomString()
  {
    return "random" + (int)(Math.random() * Integer.MAX_VALUE);
  }
  
  public void testCreateStore()
  {
     String storeName = "test-store";
     StorageEngine engine = getStorageEngine();
     engine.createStore(storeName, String.class, String.class, new StringSerializer(), new StringSerializer());
     assertEquals("store must be created",engine.getSchema(storeName).getKeySchema(), String.class);
     assertEquals("store must be created",engine.getSchema(storeName).getValueSchema(), String.class);
  }
}
