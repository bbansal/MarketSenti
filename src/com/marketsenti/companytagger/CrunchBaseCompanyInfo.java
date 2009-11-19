package com.marketsenti.companytagger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.collect.ImmutableSet;
import com.marketsenti.domain.CompanyInfo;
import com.marketsenti.domain.Employee;
import com.marketsenti.webutils.HTTPUtils;

/**
 * Simple class to return {@link CompanyInfo} from crunchbase data.
 * 
 * @author bbansal
 * 
 */
public class CrunchBaseCompanyInfo
{
  private static String      searchRequest      =
                                                    "http://api.crunchbase.com/v/1/search.js?query=";
  private static String      companyInfoRequest =
                                                    "http://api.crunchbase.com/v/1/company/";

  private static Set<String> usefulTags         =
                                                    ImmutableSet.of("number_of_employees",
                                                                    "competitions",
                                                                    "relationships",
                                                                    "tag_list",
                                                                    "overview",
                                                                    "acquisitions",
                                                                    "ipo",
                                                                    "external_links",
                                                                    "funding_rounds");

  /**
   * queries crunch base and returns Company object filled with info.
   * 
   * @param crunchbaseCompanyName
   *          : name of the company at crunchbase (normally the company name)
   * @return
   * @throws IOException
   * @throws ClientProtocolException
   * @throws JSONException
   */
  public static CompanyInfo getCompanyInfo(String crunchbaseCompanyName) throws ClientProtocolException,
      IOException,
      JSONException
  {
    String requestURL = companyInfoRequest + crunchbaseCompanyName.toLowerCase() + ".js";
    String jsonText =
        HTTPUtils.HttpGetResponseAsString(requestURL, null).getResponseString();
    JSONObject jsonObject = new JSONObject(jsonText);

    return createCompany(crunchbaseCompanyName, jsonObject);
  }

  private static CompanyInfo createCompany(String companyName, JSONObject jsonObject) throws JSONException
  {
    CompanyInfo companyInfo = new CompanyInfo(companyName);

    // fill companyInfo
    companyInfo.setSize(jsonObject.get("number_of_employees").toString());
    companyInfo.setOverview(jsonObject.get("overview").toString());
    companyInfo.setKeywords(getKeywordList(jsonObject));
    companyInfo.setCompetitiors(getCompetitiors(jsonObject));
    companyInfo.setExecutives(getExecutives(jsonObject));

    System.out.println(companyInfo);
    return companyInfo;
  }

  private static List<Employee> getExecutives(JSONObject jsonObject) throws JSONException
  {
    List<Employee> list = new ArrayList<Employee>();
    JSONArray employees = jsonObject.getJSONArray("relationships");

    for (int i = 0; i < employees.length(); i++)
    {
      JSONObject employee = employees.getJSONObject(i);
      JSONObject person = employee.getJSONObject("person");
      list.add(new Employee(person.getString("first_name") + " "
          + person.getString("last_name"), employee.getString("title")));
    }

    return list;
  }

  private static List<String> getCompetitiors(JSONObject jsonObject) throws JSONException
  {
    List<String> list = new ArrayList<String>();
    JSONArray competitors = jsonObject.getJSONArray("competitions");

    for (int i = 0; i < competitors.length(); i++)
    {
      list.add(competitors.getJSONObject(i).getJSONObject("competitor").getString("name"));
    }

    return list;
  }

  private static List<String> getKeywordList(JSONObject jsonObject) throws JSONException
  {
    return Arrays.asList(jsonObject.get("tag_list").toString().split(","));
  }
}
