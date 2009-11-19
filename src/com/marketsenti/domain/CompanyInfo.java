package com.marketsenti.domain;

import java.lang.reflect.Field;
import java.util.List;

import com.marketsenti.utils.Utils;

public class CompanyInfo
{
  private final String   name;
  private String         industry;
  private String         size;
  private String         revenue;
  private List<String>   competitiors;
  private List<Employee> executives;
  private List<String>   keywords;
  private String         overview;

  public CompanyInfo(String name)
  {
    this.name = name;
  }

  public String getIndustry()
  {
    return industry;
  }

  public void setIndustry(String industry)
  {
    this.industry = industry;
  }

  public String getSize()
  {
    return size;
  }

  public void setSize(String size)
  {
    this.size = size;
  }

  public String getRevenue()
  {
    return revenue;
  }

  public void setRevenue(String revenue)
  {
    this.revenue = revenue;
  }

  public List<String> getCompetitiors()
  {
    return competitiors;
  }

  public void setCompetitiors(List<String> competitiors)
  {
    this.competitiors = competitiors;
  }

  public List<Employee> getExecutives()
  {
    return executives;
  }

  public void setExecutives(List<Employee> executives)
  {
    this.executives = executives;
  }

  public List<String> getKeywords()
  {
    return keywords;
  }

  public void setKeywords(List<String> keywords)
  {
    this.keywords = keywords;
  }

  public String getName()
  {
    return name;
  }

  public void setOverview(String overview)
  {
    this.overview = overview;
  }

  public String getOverview()
  {
    return overview;
  }

  @Override
  public String toString()
  {
    try
    {
      return toStringDeep(this);
    }
    catch (Exception e)
    {
      throw new RuntimeException();
    }
  }

  private String toStringDeep(Object value) throws IllegalArgumentException,
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
