/**
 * 
 */
package test.com.desipandora;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import com.desipandora.api.SongEntry;
import com.desipandora.youTube.DesiPandoraServiceYouTube;
import com.marketsenti.storage.InMemoryStorageEngine;
import com.marketsenti.storage.StorageEngine;

/**
 * @author abhishek
 *
 */
public class TestDesiPandoraServiceyouTube extends TestCase{

	/**
	 * @param args
	 * Frontend has to be aware that even after requesting a 
	 * number of songs, desiPandora can return more songs.
	 */
	public void testDesiPandora() {
		// TODO Auto-generated method stub
		//System.setProperty("http.proxyHost", "svlproxy.amd.com");
		//System.setProperty("http.proxyPort", "74");
		StorageEngine storage  = new InMemoryStorageEngine(); 
		String applicationName = "DesiPandora-01";
		DesiPandoraServiceYouTube desiPandoraService = new DesiPandoraServiceYouTube(storage, applicationName);
		String sessionId = desiPandoraService.createSessionId("absk82");
		List<String> keyWordList = new ArrayList<String>();
		//keyWordList.add("metallica");
		keyWordList.add("tu hi meri sab hai");
		List<SongEntry> songEntryList =  new ArrayList<SongEntry>();
		songEntryList = desiPandoraService.getFirstFewSongs(keyWordList, sessionId, 50);
		songEntryList.addAll(desiPandoraService.getNextSongs(sessionId, 10));
		songEntryList.addAll(desiPandoraService.getNextSongs(sessionId, 10));
		songEntryList.addAll(desiPandoraService.getNextSongs(sessionId, 10));
		songEntryList.addAll(desiPandoraService.getNextSongs(sessionId, 10));
		Iterator<SongEntry> iterSongEntry = songEntryList.iterator();
		while(iterSongEntry.hasNext()){
			System.out.println("Song retrieved "+iterSongEntry.next().getTitle());
		}
	}

}
