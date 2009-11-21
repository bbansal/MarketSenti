/**
 * 
 */
package com.marketsenti.companytagger.scorer;
import com.marketsenti.Article.*;
import com.marketsenti.domain.*;

import java.util.Iterator;

/**
 * @author abhishek
 *
 */
public class SimpleFrequencyScorer implements ArticleCompanyScorer{
	public double score(Article article, CompanyInfo company){
		int count = 0;
		Iterator<String> iterKeyWords = company.getKeywords().iterator();
		while (iterKeyWords.hasNext()){
			String nextKeyWord = iterKeyWords.next();
			if(article.getText().matches(".*"+nextKeyWord+".*")){
				System.out.println(nextKeyWord);
				count++;
			}
		}
		System.out.println("Count for "+company.getName()+" is "+count);
		return (double) count;
	}
}
