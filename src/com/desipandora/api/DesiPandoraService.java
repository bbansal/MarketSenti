package com.desipandora.api;

import java.util.List;

public interface DesiPandoraService
{

  /**
   * Create a unique session-id, associate it with the given uID §
   * @param uId
   * @return session-id
   */
   String createSessionId(String uId);
   
   /**
    * 
    * @param Keywords
    * @param sessionId
    * @param numSongs
    * @return
    */
   List<SongEntry> getFirstFewSongs(List<String> Keywords, String sessionId, int numSongs); 
   
   List<SongEntry> getNextSongs(String sessionId, int numSongs);
   
   void feedback(String sessionId, String songId, double feedback);
   
}
