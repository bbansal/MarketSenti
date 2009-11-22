package com.marketsenti.storage.serializer;

public interface BytesSerializer<K>
{
  public byte[] getBytes(K object);
  public K fromBytes(byte[] bytes);
}
