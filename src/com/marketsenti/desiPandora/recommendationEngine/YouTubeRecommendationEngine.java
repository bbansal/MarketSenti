/**    
 * 
 */
package com.marketsenti.desiPandora.recommendationEngine;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.google.gdata.client.youtube.YouTubeQuery;
import com.google.gdata.client.youtube.YouTubeService;
import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.VideoFeed;
import com.google.gdata.util.ServiceException;
import com.marketsenti.desiPandora.PlayEntry;

/**
 * @author abhishek
 * @param <T>
 *
 */
public class YouTubeRecommendationEngine implements RecommendationEngine <VideoEntry>{
        
	private YouTubeService service_;
	
	public YouTubeRecommendationEngine(String applicationName) {
		super();
		this.service_ = new YouTubeService(applicationName);
	}

	/* (non-Javadoc)
	 * @see com.marketsenti.desiPandora.recommendationEngine.RecommendationEngine#getRecommendations(java.util.List, java.util.List)
	 */
	public List<PlayEntry<VideoEntry>> getRecommendations(List<PlayEntry<VideoEntry>> liked, List<PlayEntry<VideoEntry>> disliked) {
		// TODO Auto-generated method stub
		String queryString = liked.get(0).getTitle();
		 YouTubeQuery query = null;
		try{
			query = new YouTubeQuery(new URL("http://gdata.youtube.com/feeds/api/videos"));
			query.setOrderBy(YouTubeQuery.OrderBy.VIEW_COUNT);
			query.setFullTextQuery(queryString);
			query.setSafeSearch(YouTubeQuery.SafeSearch.NONE);
			try{
				VideoFeed videoFeed = service_.query(query, VideoFeed.class);
				printVideoFeed(videoFeed, false);
			}
			catch(IOException e){
				e.printStackTrace();
			}
			catch (ServiceException e){
				e.printStackTrace();
			}
			
		}
		catch(MalformedURLException e){
			System.out.println("Malformed URL");
			e.printStackTrace();
		}
		
		
		return null;
	}

	public static void printVideoFeed(VideoFeed videoFeed, boolean detailed){
		for (VideoEntry videoEntry : videoFeed.getEntries()){
			printVideoEntry(videoEntry, false);
		}
	}
	
	public static void printVideoEntry(VideoEntry videoEntry, boolean detailed){
		System.out.println("Title : " + videoEntry.getTitle().getPlainText());
	}
	
}
