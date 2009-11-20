package com.marketsenti.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Utils
{
  public static String toStringDeep(Object value) throws IllegalArgumentException,
      IllegalAccessException,
      InvocationTargetException
  {
    StringBuilder builder = new StringBuilder(value.getClass() + "(");
    Method[] methods = value.getClass().getDeclaredMethods();
    for (Method method : methods)
    {
      if (method.getName().contains("get"))
      {
        builder.append(method.getName());
        builder.append("(");
        builder.append(method.invoke(value));
        builder.append(")");
      }
    }
    builder.append(")");
    return builder.toString();
  }
}
