package com.marketsenti.textextractor;

import java.io.IOException;
import java.net.URL;
import java.util.Set;

import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.SourceCompactor;
import net.htmlparser.jericho.StartTag;
import net.htmlparser.jericho.TextExtractor;

import com.google.common.collect.Sets;

public class ArticleTextExtractor
{
  private final static Set<String> IGNORE_TAGS = Sets.newHashSet("iframe",
                                                                   "head",
                                                                   "script",
                                                                   "link",
                                                                   "noscript",
                                                                   "form",
                                                                   "style",
                                                                   "meta",
                                                                   "!--");

  public static String extractText(URL sourceURL, Set<String> ignoreTags) throws IOException
  {
    Source source = new Source(sourceURL);
    TextExtractor scraper = new TextExtractor(source);
    scraper.setExcludeNonHTMLElements(true);

    if (null != ignoreTags)
    {
      for (String ignoreTag : ignoreTags)
        for (StartTag startTag : source.getAllStartTags(ignoreTag))
          scraper.excludeElement(startTag);
    }

    return scraper.toString();
  }
}
