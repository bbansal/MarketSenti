/**
 * 
 */
package com.desipandora.sessionInfo;


import java.util.ArrayList;
import java.util.List;

import com.desipandora.api.SongEntry;
/**
 * @author abhishek
 *
 */
public class PlayList {
	private List<SongEntry> playList;
	private int indexRelatedFeedSeed;
	private int indexNextRecommendation;
	
	public PlayList(){
		this.playList = new ArrayList<SongEntry>();
		this.indexRelatedFeedSeed = -1;
		this.indexNextRecommendation = 0;
	}
	
	public List<SongEntry> getPlayList() {
		return playList;
	}
	
	public PlayList(List<SongEntry> playList, int indexRelatedFeedSeed, int indexNextRecommendation) {
		this.playList = new ArrayList<SongEntry>();
		this.playList.addAll(playList);
		this.indexRelatedFeedSeed = indexRelatedFeedSeed;
		this.indexNextRecommendation = indexNextRecommendation;
	}
	public void setPlayList(List<SongEntry> playList) {
		this.playList.addAll(playList);
	}
	public int getIndexRelatedFeedSeed() {
		return indexRelatedFeedSeed;
	}
	public void setIndexRelatedFeedSeed(int indexRelatedFeedSeed) {
		this.indexRelatedFeedSeed = indexRelatedFeedSeed;
	}

	/**
	 * @return the indexNextRecommendation
	 */
	public int getIndexNextRecommendation() {
		return indexNextRecommendation;
	}

	/**
	 * @param indexNextRecommendation the indexNextRecommendation to set
	 */
	public void setIndexNextRecommendation(int indexNextRecommendation) {
		this.indexNextRecommendation = indexNextRecommendation;
	}
	
}
