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
	private int relatedFeedSeed;
	
	public PlayList(){
		this.playList = new ArrayList<SongEntry>();
		this.relatedFeedSeed = -1;
	}
	
	public List<SongEntry> getPlayList() {
		return playList;
	}
	
	public PlayList(List<SongEntry> playList, int relatedFeedSeed) {
		this.playList = new ArrayList<SongEntry>();
		this.playList.addAll(playList);
		this.relatedFeedSeed = relatedFeedSeed;
	}
	public void setPlayList(List<SongEntry> playList) {
		this.playList.addAll(playList);
	}
	public int getRelatedFeedSeed() {
		return relatedFeedSeed;
	}
	public void setRelatedFeedSeed(int relatedFeedSeed) {
		this.relatedFeedSeed = relatedFeedSeed;
	}
}
