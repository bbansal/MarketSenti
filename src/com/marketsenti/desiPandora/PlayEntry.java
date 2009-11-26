/**
 * 
 */
package com.marketsenti.desiPandora;


/**
 * @author abhishek
 *
 */
public class PlayEntry<T> {
	private String Title_;
	private String Artist_;
	private String Movie_;
	private String Summary_;
	T object_;
	/**
	 * @return the artist
	 */
	public String getArtist() {
		return Artist_;
	}
	/**
	 * @param artist the artist to set
	 */
	public void setArtist(String artist) {
		Artist_ = artist;
	}
	/**
	 * @return the movie
	 */
	public String getMovie() {
		return Movie_;
	}
	/**
	 * @param movie the movie to set
	 */
	public void setMovie(String movie) {
		Movie_ = movie;
	}
	/**
	 * @return the object
	 */
	public T getObject() {
		return object_;
	}
	/**
	 * @param object the object to set
	 */
	public void setObject(T object) {
		this.object_ = object;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return Title_;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		Title_ = title;
	}

}
