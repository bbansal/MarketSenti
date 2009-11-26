package com.marketsenti.storage;

public class StoreSchema
{
  private final Class keySchema;
  private final Class valueSchema;

  public StoreSchema(Class keySchema, Class valueSchema)
  {
    this.keySchema = keySchema;
    this.valueSchema = valueSchema;
  }

  public Class getKeySchema()
  {
    return keySchema;
  }

  public Class getValueSchema()
  {
    return valueSchema;
  }

}
