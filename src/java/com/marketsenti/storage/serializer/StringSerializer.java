package com.marketsenti.storage.serializer;


public class StringSerializer implements BytesSerializer<String>
{

  public String fromBytes(byte[] bytes)
  {
    return new String(bytes);
  }

  public byte[] getBytes(String object)
  {
    return object.getBytes();
  }
  
}