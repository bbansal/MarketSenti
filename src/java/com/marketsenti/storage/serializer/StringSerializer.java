package com.marketsenti.storage.serializer;


public class StringSerializer implements BytesSerializer<String>
{

  @Override
  public String fromBytes(byte[] bytes)
  {
    return new String(bytes);
  }

  @Override
  public byte[] getBytes(String object)
  {
    return object.getBytes();
  }
  
}