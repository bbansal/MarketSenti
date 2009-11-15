package com.marketsenti.storage;

import java.util.Iterator;

public class StoreEntry<K, V>
{
  private final K       key;
  private final Iterator<V> values;

  public StoreEntry(K key, Iterator<V> values)
  {
    this.key = key;
    this.values = values;
  }

  public K getKey()
  {
    return key;
  }

  public Iterator<V> getValues()
  {
    return values;
  }
}
