/**
 * 
 */
package com.marketsenti.companytagger.scorer;
import com.marketsenti.Article.*;
import com.marketsenti.companyTagger.*;
import com.marketsenti.domain.*;
/**
 * @author abhishek
 *
 */
public interface ArticleCompanyScorer {
	public double score(Article article, CompanyInfo company);
}
