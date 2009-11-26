package com.marketsenti.domain;

import java.lang.reflect.Field;

import com.marketsenti.utils.Utils;

public class Employee
{
  private final String name;
  private final String position;

  public Employee(String name, String position)
  {
    this.name = name;
    this.position = position;
  }

  public String getName()
  {
    return name;
  }

  public String getPosition()
  {
    return position;
  }

  @Override
  public String toString()
  {
    try
    {
      return Utils.toStringDeep(this);
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }
}
