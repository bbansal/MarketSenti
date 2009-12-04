/**
 * 
 */
package test.com.desipandora;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.desipandora.api.SongEntry;
import com.desipandora.youTube.DesiPandoraServiceYouTube;
import com.marketsenti.storage.InMemoryStorageEngine;
import com.marketsenti.storage.StorageEngine;

/**
 * @author abhishek
 *
 */
public class TestDesiPandoraServiceyouTube {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StorageEngine storage  = new InMemoryStorageEngine(); 
		String applicationName = "DesiPandora-01";
		DesiPandoraServiceYouTube desiPandoraService = new DesiPandoraServiceYouTube(storage, applicationName);
		String sessionId = desiPandoraService.createSessionId("absk82");
		List<String> keyWordList = new ArrayList<String>();
		keyWordList.add("metallica");
		keyWordList.add(" nothing else matters");
		List<SongEntry> songEntryList = desiPandoraService.getFirstFewSongs(keyWordList, sessionId, 10);
		Iterator<SongEntry> iterSongEntry = songEntryList.iterator();
		while(iterSongEntry.hasNext()){
			System.out.println("Song retrieved "+iterSongEntry.next().getTitle());
		}
	}

}
