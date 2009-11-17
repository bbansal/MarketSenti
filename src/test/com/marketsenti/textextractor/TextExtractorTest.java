package test.com.marketsenti.textextractor;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;

import junit.framework.TestCase;

import com.marketsenti.textextractor.ArticleTextExtractor;

public class TextExtractorTest extends TestCase
{
  private String articleURL = "http://rss.cnn.com/~r/rss/money_latest/~3/UCZ3fgRf6i0/index.htm";
  
   public void testTextExtraction() throws MalformedURLException, IOException
   {
     String text = ArticleTextExtractor.extractText(new URL(articleURL), null);
     System.out.println(text);
     FileUtils.writeStringToFile(new File("test.txt"), text);
   }
}