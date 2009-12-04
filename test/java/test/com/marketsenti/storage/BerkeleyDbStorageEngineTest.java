package test.com.marketsenti.storage;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.ImmutableMap;
import com.marketsenti.storage.BerkeleyDbStorageEngine;
import com.marketsenti.storage.StorageEngine;

public class BerkeleyDbStorageEngineTest extends AbstractStorageEngineTest{

  private String testDataDir;
  @Override
  public void setUp()
  {
    testDataDir = "/tmp/" +  "test-" + Math.random() * Integer.MAX_VALUE;
    File file = new File(testDataDir);
    file.mkdirs();
    
  }
  @Override 
  public void tearDown()
  {
    File file = new File(testDataDir);
    try
    {
      FileUtils.deleteDirectory(file);
    }
    catch (IOException e)
    {
      // ignore this here
    }
  }
  
  @Override
  protected StorageEngine getStorageEngine()
  {
    Map<String,String> properties =  ImmutableMap.of("je.maxMemory", "1024000");
    try
    {
      return new BerkeleyDbStorageEngine(testDataDir, properties);
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }
  
}