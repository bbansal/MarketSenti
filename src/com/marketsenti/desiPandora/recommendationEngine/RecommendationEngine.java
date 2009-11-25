/**
 * 
 */
package com.marketsenti.desiPandora.recommendationEngine;

import java.util.List;

import com.marketsenti.desiPandora.PlayEntry;

/**
 * @author abhishek
 *
 */
public interface RecommendationEngine<T>  {
	public List<PlayEntry<T>> getRecommendations(List<PlayEntry<T>> liked, List<PlayEntry<T>> disliked);
}
