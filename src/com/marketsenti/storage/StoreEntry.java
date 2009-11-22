package com.marketsenti.storage;


public class StoreEntry<K, V>
{
  private final K key;
  private final V value;

  public StoreEntry(K key, V value)
  {
    this.key = key;
    this.value = value;
  }

  public K getKey()
  {
    return key;
  }

  public V getValue()
  {
    return value;
  }
}
