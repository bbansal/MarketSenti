/**
 * 
 */
package test.com.marketsenti.desiPandora;

import junit.framework.TestCase;

import com.google.gdata.data.youtube.VideoEntry;
import com.marketsenti.desiPandora.*;
import com.marketsenti.desiPandora.recommendationEngine.YouTubeRecommendationEngine;
import com.marketsenti.desiPandora.recommendationEngine.RecommendationEngine;

/**
 * @author abhishek
 *
 */
public class TestYouTubeRecommendationEngine extends TestCase{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RecommendationEngine<VideoEntry> recommendationEngine =  new YouTubeRecommendationEngine("MarketSenti-DesiPandora-01");
		BackEndImplement<VideoEntry> youTubeEngine =  new BackEndImplement<VideoEntry>(recommendationEngine);
		PlayEntry<VideoEntry> playEntry = new PlayEntry<VideoEntry>();
		playEntry.setTitle("nothing else matters metallica");
		youTubeEngine.getPlayList(playEntry);
	}

}
