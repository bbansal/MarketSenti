/**
 * 
 */
package com.marketsenti.desiPandora;

import java.util.ArrayList;
import java.util.List;

import com.marketsenti.desiPandora.recommendationEngine.RecommendationEngine;
import com.marketsenti.desiPandora.PlayEntry;

/**
 * @author abhishek
 *
 */
public class BackEndImplement <T> implements BackEndInterface<T> {

	List<PlayEntry<T>> playList_;
	List<PlayEntry<T>> liked_;
	List<PlayEntry<T>> disliked_;
	RecommendationEngine<T> recommendationEngine_;
	
	/**
	 * 
	 */
	public BackEndImplement(RecommendationEngine<T> recommendationEngine) {
		recommendationEngine_ = recommendationEngine;
		liked_    = new ArrayList<PlayEntry<T>>();
		disliked_ = new ArrayList<PlayEntry<T>>();

	}

	/* (non-Javadoc)
	 * @see com.marketsenti.desiPandora.BackEndInterface#StopPlaylist()
	 */
	public void StopPlaylist() {
		throw new RuntimeException("Unimplemented method");
	}

	/* (non-Javadoc)
	 * @see com.marketsenti.desiPandora.BackEndInterface#ThumbsDown(java.lang.Object)
	 */
	public void ThumbsDown(PlayEntry<T> playEntry) {
		disliked_.add(playEntry);
	}

	/* (non-Javadoc)
	 * @see com.marketsenti.desiPandora.BackEndInterface#ThumbsUp(java.lang.Object)
	 */
	public void ThumbsUp(PlayEntry<T> playEntry) {
		liked_.add(playEntry);
	}

	/* (non-Javadoc)
	 * @see com.marketsenti.desiPandora.BackEndInterface#getPlayList(java.lang.Object)
	 *  TODO :  Convert all Videoentry to PlayListEntry
	 * 
	 */
	public List<PlayEntry<T>> getPlayList(PlayEntry<T> playEntry) {
		    if(playEntry != null) ThumbsUp(playEntry);
			List<PlayEntry<T>> playList = recommendationEngine_.getRecommendations(liked_, disliked_);
			return playList;
	}

	
}
