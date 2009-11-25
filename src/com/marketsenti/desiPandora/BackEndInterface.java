/**
 * 
 */
package com.marketsenti.desiPandora;

import java.util.List;

import com.marketsenti.desiPandora.PlayEntry;
/**
 * Interface between the frontend User interface and the backend recommendation engine.
 * @author abhishek
 * @param <T>
 *
 */
public interface BackEndInterface<T> {
List<PlayEntry<T>> getPlayList(PlayEntry<T> songId);
void ThumbsUp(PlayEntry<T> songId);
void ThumbsDown(PlayEntry<T> songId);
void StopPlaylist();
}
