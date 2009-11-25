/**
 * 
 */
package com.marketsenti.desiPandora;



import java.net.URL;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

//import com.google.gdata.client.Query;
import com.google.gdata.client.youtube.YouTubeQuery;
import com.google.gdata.client.youtube.YouTubeService;
import com.google.gdata.data.youtube.*;
import com.google.gdata.util.ServiceException;

/**
 * @author abhishek
 *
 */
public class YoutubeUtils {
	private YouTubeService service;
	
	public void initializeConnection(String serviceName){
			service = new YouTubeService(serviceName);
	}
	
	public void searchVideo(String queryString){
		try {
			YouTubeService service = new YouTubeService("");
			YouTubeQuery query = new YouTubeQuery(new URL("http://gdata.youtube.com/feeds/api/videos"));
			query.setOrderBy(YouTubeQuery.OrderBy.VIEW_COUNT);
			query.setFullTextQuery(queryString);
			query.setSafeSearch(YouTubeQuery.SafeSearch.NONE);

			try{
				VideoFeed videoFeed = service.query(query, VideoFeed.class);
				printVideoFeed(videoFeed, false);

			}
			catch(IOException io){
				System.out.println("GData cannot be accessed");
				io.printStackTrace();
			}
			catch(ServiceException e){
				System.out.println("Service Exception");
				e.printStackTrace();
			}

			//eoFeed 
		}
		catch(MalformedURLException e){
			System.out.println("MalFormed URL");
		}
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
