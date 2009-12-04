/**
 * 
 */
package com.desipandora.sessionInfo;

import com.desipandora.api.SongEntry;

/**
 * @author abhishek
 *
 */
public class SongFeedbackEntry {
	SongEntry songEntry;
	double feedBack;
	
	/**
	 * @param songEntry
	 * @param feedBack
	 */
	public SongFeedbackEntry(SongEntry songEntry, double feedBack) {
		super();
		this.songEntry = songEntry;
		this.feedBack = feedBack;
	}
	public double getFeedBack() {
		return feedBack;
	}
	public void setFeedBack(double feedBack) {
		this.feedBack = feedBack;
	}
	public SongEntry getSongEntry() {
		return songEntry;
	}
	public void setSongEntry(SongEntry songEntry) {
		this.songEntry = songEntry;
	}
	
}
