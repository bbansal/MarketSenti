package test.com.marketsenti.companytagger;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;

import junit.framework.TestCase;

import com.marketsenti.companytagger.CrunchBaseCompanyInfo;
import com.marketsenti.textextractor.ArticleTextExtractor;

public class TestCrunchBaseApi extends TestCase
{
  
   public void testCrunchBaseAPI() throws MalformedURLException, IOException, JSONException
   {
     CrunchBaseCompanyInfo.getCompanyInfo("google");
   }
}