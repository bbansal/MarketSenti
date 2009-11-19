package com.marketsenti.utils;

import java.lang.reflect.Field;

public class Utils
{
  public static String toStringDeep(Object value) throws IllegalArgumentException,
      IllegalAccessException
  {
    StringBuilder builder = new StringBuilder(value.getClass() + "(");
    Field[] fields = value.getClass().getDeclaredFields();
    for (Field field : fields)
    {
      builder.append(field.getName());
      builder.append("(");
      builder.append(toStringDeep(field.get(value)));
      builder.append(")");
    }
    builder.append(")");
    return builder.toString();
  }
}
