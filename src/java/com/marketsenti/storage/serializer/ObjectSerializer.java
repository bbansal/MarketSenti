package com.marketsenti.storage.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ObjectSerializer implements BytesSerializer<Serializable>
{
  public Serializable fromBytes(byte[] bytes)
  {
    try
    {
      ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes));
      Object obj = in.readObject();
      in.close();
      return (Serializable) obj;
    }
    catch (Exception e)
    {
      throw new RuntimeException("Failed to serialize bytes to Object.", e);
    }
  }

  public byte[] getBytes(Serializable object)
  {
    try
    {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      ObjectOutputStream os = new ObjectOutputStream(bos);
      os.writeObject(object);
      os.flush();
      bos.flush();
      
      byte[] bytes = bos.toByteArray();
      
      bos.close();
      os.close();
      
      return bytes;
    }
    catch (Exception e)
    {
      throw new RuntimeException("Failed to serialize Object to Bytes.", e);
    }
  }
}
