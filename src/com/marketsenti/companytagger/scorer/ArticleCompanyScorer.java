/**
 * 
 */
package com.marketsenti.companytagger.scorer;
import com.marketsenti.Article.Article;
import com.marketsenti.domain.CompanyInfo;
/**
 * @author abhishek
 *
 */
public interface ArticleCompanyScorer {
	public double score(Article article, CompanyInfo company);
}
