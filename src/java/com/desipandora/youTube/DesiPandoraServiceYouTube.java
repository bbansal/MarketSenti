/**
 * 
 */
package com.desipandora.youTube;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.desipandora.api.DesiPandoraService;
import com.desipandora.api.SongEntry;
import com.desipandora.api.SongEntry.SongEntryType;
import com.desipandora.sessionInfo.PlayList;
import com.desipandora.sessionInfo.SessionEntry;
import com.google.gdata.client.youtube.YouTubeQuery;
import com.google.gdata.client.youtube.YouTubeService;
import com.google.gdata.data.extensions.Rating;
import com.google.gdata.data.geo.impl.GeoRssWhere;
import com.google.gdata.data.media.mediarss.MediaKeywords;
import com.google.gdata.data.media.mediarss.MediaPlayer;
import com.google.gdata.data.media.mediarss.MediaThumbnail;
import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.VideoFeed;
import com.google.gdata.data.youtube.YouTubeMediaContent;
import com.google.gdata.data.youtube.YouTubeMediaGroup;
import com.google.gdata.data.youtube.YouTubeMediaRating;
import com.google.gdata.data.youtube.YtPublicationState;
import com.google.gdata.data.youtube.YtStatistics;
import com.google.gdata.util.ServiceException;
import com.marketsenti.storage.InMemoryStorageEngine;
import com.marketsenti.storage.StorageEngine;

/**
 * @author abhishek
 * 
 */
public class DesiPandoraServiceYouTube implements DesiPandoraService {

	private StorageEngine storage;
	private String storeName;
	private YouTubeService service;
	private String serviceName;

	// Play Session specific info
	// private List<VideoEntry> playList;
	// int counterNextSeed ;

	public DesiPandoraServiceYouTube() {
		super();
		this.storage = new InMemoryStorageEngine();
		this.storeName = "DesiPandoraDb"; // temporary. see where to put this.
		storage.createStore(storeName, String.class, SessionEntry.class, null,
				null);
		this.serviceName = "DesiPandora-01";
	}

	/**
	 * @param storage
	 */
	public DesiPandoraServiceYouTube(StorageEngine storage, String serviceName) {
		super();
		this.storage = storage;
		this.storeName = "DesiPandoraDb"; // temporary. see where to put this.
		storage.createStore(storeName, String.class, SessionEntry.class, null,
				null);
		this.serviceName = serviceName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.desipandora.api.DesiPandoraService#createSessionId(java.lang.String)
	 */
	public String createSessionId(String uId) {
		// generate a session Id
		Date date = new Date();
		String sessionId = uId + "_" + date.toString();
		List<SessionEntry> arrayList = new ArrayList<SessionEntry>();
		arrayList.add(new SessionEntry(uId));
		System.out.println("Session id is " + sessionId);
		storage.<String, SessionEntry> putValue(storeName, sessionId, arrayList
				.iterator(), date.getTime());
		service = new YouTubeService(serviceName);
		return sessionId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.desipandora.api.DesiPandoraService#feedback(java.lang.String,
	 * java.lang.String, double)
	 */
	public void feedback(String sessionId, String songId, double feedback) {
		SessionEntry sessionEntry = (SessionEntry) (storage.getValues(
				storeName, sessionId)).next();
		sessionEntry.addFeedbackToSong(songId, feedback);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.desipandora.api.DesiPandoraService#getFirstFewSongs(java.util.List,
	 * java.lang.String, int)
	 */
	public List<SongEntry> getFirstFewSongs(List<String> Keywords,
			String sessionId, int numSongs) {
		String queryString = "";
		Iterator<String> iterKeywords = Keywords.iterator();
		List<SongEntry> songEntryList = new ArrayList<SongEntry>();
		while (iterKeywords.hasNext()) {
			queryString = queryString + " " + iterKeywords.next();
		}
		YouTubeQuery query = null;
		try {
			query = new YouTubeQuery(new URL(
					"http://gdata.youtube.com/feeds/api/videos"));
			query.setOrderBy(YouTubeQuery.OrderBy.RELEVANCE);
			query.setFullTextQuery(queryString);
			query.setSafeSearch(YouTubeQuery.SafeSearch.NONE);
			try {
				VideoFeed videoFeed = service.query(query, VideoFeed.class);
				Iterator<SessionEntry> iterStorage = storage.getValues(
						storeName, sessionId);
				SessionEntry sessionEntry = null;
				if (iterStorage.hasNext()) {
					sessionEntry = (SessionEntry) (storage.getValues(storeName,
							sessionId).next());
				} else {
					throw new RuntimeException("No Entries exist in storeName "
							+ storeName);
				}
				List<VideoEntry> videoEntryList = videoFeed.getEntries();
				Iterator<VideoEntry> iterVideoEntryList = videoEntryList
						.iterator();
				if (iterVideoEntryList.hasNext()) {
					VideoEntry videoEntry = iterVideoEntryList.next();
					if (isValidYoutubeVideo(videoEntry)) {
						// printVideoEntry(videoEntry, true);
						SongEntry songEntry = new SongEntry(videoEntry.getId(),
								SongEntryType.YOUTUBE);
						CopyVideoEntryToSongEntry(videoEntry, songEntry);
						System.out.println("First Title : "
								+ songEntry.getTitle() + " link : "
								+ songEntry.getRelatedFeedString());
						songEntryList.add(songEntry);
						sessionEntry.addPlayListToSession(new PlayList(
								songEntryList, 0, 1));
						List<SongEntry> tempSongEntryList = getNextSongs(
								sessionId, numSongs - 1);
						songEntryList.addAll(tempSongEntryList);
					}

				} else {
					System.out.println("No Videos found");
				}
			} catch (Exception e) {
				StringWriter writer = new StringWriter();
				e.printStackTrace(new PrintWriter(writer));

				throw new RuntimeException(
						"Failed to getFirstFewSongs() with exception:"
								+ writer.toString(), e);
			}

		} catch (MalformedURLException e) {
			System.out.println("Malformed URL");
			e.printStackTrace();
		}

		return songEntryList;
	}

	private boolean isValidYoutubeVideo(VideoEntry videoEntry) {
		return videoEntry.isEmbeddable();
	}

	public static void printVideoEntry(VideoEntry videoEntry, boolean detailed) {
		System.out.println("Title: " + videoEntry.getTitle().getPlainText());

		if (videoEntry.isDraft()) {
			System.out.println("Video is not live");
			YtPublicationState pubState = videoEntry.getPublicationState();
			if (pubState.getState() == YtPublicationState.State.PROCESSING) {
				System.out.println("Video is still being processed.");
			} else if (pubState.getState() == YtPublicationState.State.REJECTED) {
				System.out.print("Video has been rejected because: ");
				System.out.println(pubState.getDescription());
				System.out.print("For help visit: ");
				System.out.println(pubState.getHelpUrl());
			} else if (pubState.getState() == YtPublicationState.State.FAILED) {
				System.out.print("Video failed uploading because: ");
				System.out.println(pubState.getDescription());
				System.out.print("For help visit: ");
				System.out.println(pubState.getHelpUrl());
			}
		}

		if (videoEntry.getEditLink() != null) {
			System.out.println("Video is editable by current user.");
		}

		if (detailed) {

			YouTubeMediaGroup mediaGroup = videoEntry.getMediaGroup();

			System.out.println("Uploaded by: " + mediaGroup.getUploader());

			System.out.println("Video ID: " + mediaGroup.getVideoId());
			System.out.println("Description: "
					+ mediaGroup.getDescription().getPlainTextContent());

			MediaPlayer mediaPlayer = mediaGroup.getPlayer();
			System.out.println("Web Player URL: " + mediaPlayer.getUrl());
			MediaKeywords keywords = mediaGroup.getKeywords();
			System.out.print("Keywords: ");
			for (String keyword : keywords.getKeywords()) {
				System.out.print(keyword + ",");
			}

			GeoRssWhere location = videoEntry.getGeoCoordinates();
			if (location != null) {
				System.out.println("Latitude: " + location.getLatitude());
				System.out.println("Longitude: " + location.getLongitude());
			}

			Rating rating = videoEntry.getRating();
			if (rating != null) {
				System.out.println("Average rating: " + rating.getAverage());
			}

			YtStatistics stats = videoEntry.getStatistics();
			if (stats != null) {
				System.out.println("View count: " + stats.getViewCount());
			}
			System.out.println();

			System.out.println("\tThumbnails:");
			for (MediaThumbnail mediaThumbnail : mediaGroup.getThumbnails()) {
				System.out.println("\t\tThumbnail URL: "
						+ mediaThumbnail.getUrl());
				System.out.println("\t\tThumbnail Time Index: "
						+ mediaThumbnail.getTime());
				System.out.println();
			}

			System.out.println("\tMedia:");
			for (YouTubeMediaContent mediaContent : mediaGroup
					.getYouTubeContents()) {
				System.out.println("\t\tMedia Location: "
						+ mediaContent.getUrl());
				System.out.println("\t\tMedia Type: " + mediaContent.getType());
				System.out.println("\t\tDuration: "
						+ mediaContent.getDuration());
				System.out.println();
			}

			for (YouTubeMediaRating mediaRating : mediaGroup
					.getYouTubeRatings()) {
				System.out
						.println("Video restricted in the following countries: "
								+ mediaRating.getCountries().toString());
			}
		}
	}

	private void CopyVideoEntryToSongEntry(VideoEntry videoEntry,
			SongEntry songEntry) {
		try {
			songEntry.setTitle(videoEntry.getTitle().getPlainText()
					.toLowerCase());
			songEntry.setRelatedFeedString(videoEntry.getRelatedVideosLink()
					.getHref());
			YouTubeMediaGroup mediaGroup = videoEntry.getMediaGroup();
			songEntry.setSongId(mediaGroup.getVideoId());
			songEntry.setDescription(mediaGroup.getDescription()
					.getPlainTextContent());
			MediaKeywords keywords = mediaGroup.getKeywords();
			songEntry.setKeyWords(keywords.getKeywords());
			songEntry.setYouTubeRating(videoEntry.getRating().getAverage());
		} catch (Exception e) {
			// TODO : fix me ignore
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.desipandora.api.DesiPandoraService#getNextSongs(java.lang.String,
	 * int)
	 */
	public List<SongEntry> getNextSongs(String sessionId, int numSongs) {
		Iterator<SessionEntry> iterStorage = storage.getValues(storeName,
				sessionId);
		SessionEntry sessionEntry = null;
		PlayList sessionPlayList = new PlayList();
		if (iterStorage.hasNext()) {
			sessionEntry = (SessionEntry) (storage.getValues(storeName,
					sessionId).next());
			sessionPlayList = sessionEntry.getPlayList();
		} else {
			throw new RuntimeException("No Entries exist in storemane "
					+ storeName);
		}

		/*
		 * Maintain a list of songIds which will keep track of what order the
		 * seongs have been sent. nextSeed is a pointer to the next song on this
		 * list which has been used as a seed for getRelatedVideos. - Label A :
		 * get song at next seed and find related videos. - add these videos to
		 * a list(to be returned) as well as the map while discarding duplicates
		 * - if (number retrieved == number requested) Update nextSeed pointer
		 * return this songlist. else go to A with nextSeed.
		 */

		List<SongEntry> songEntryList = new ArrayList<SongEntry>();
		List<SongEntry> seedPlayList = new ArrayList<SongEntry>();
		seedPlayList.addAll(sessionPlayList.getPlayList());
		int counterNextSeed = sessionPlayList.getIndexRelatedFeedSeed();
		int counterNextRecommendation = sessionPlayList
				.getIndexNextRecommendation();
		if ((sessionPlayList.getPlayList().size() - counterNextRecommendation) >= numSongs) {
			sessionEntry.addPlayListToSession(new PlayList(songEntryList,
					counterNextSeed, counterNextRecommendation + numSongs));
			System.out.println("Songs Retrieved " + songEntryList.size()
					+ ", Songs Requested " + numSongs
					+ ", CounterNextRecommendation "
					+ (counterNextRecommendation + numSongs));
			return seedPlayList.subList(counterNextRecommendation,
					counterNextRecommendation + numSongs);
		}
		while ((songEntryList.size() < numSongs)
				&& (seedPlayList.size() > counterNextSeed)) {

			SongEntry seedSongEntry = seedPlayList.get(counterNextSeed);
			System.out.println("SEED : " + seedSongEntry.getTitle());
			if (seedSongEntry.getRelatedFeedString() != "") {
				String feedString = seedSongEntry.getRelatedFeedString();
				try {
					VideoFeed relatedVideoFeed = service.getFeed(new URL(
							feedString), VideoFeed.class);

					for (VideoEntry loopVideoEntry : relatedVideoFeed
							.getEntries()) {

						if (isValidYoutubeVideo(loopVideoEntry)) {

							SongEntry songEntry = new SongEntry(loopVideoEntry
									.getId(), SongEntryType.YOUTUBE);
							CopyVideoEntryToSongEntry(loopVideoEntry, songEntry);
							boolean isDuplicate = FindSimilar(songEntry,
									seedPlayList);
							// boolean isDuplicate = false;
							if (!isDuplicate) {
								songEntryList.add(songEntry);
								seedPlayList.add(songEntry);
							}
							// System.out.println("Song Info : Title "+loopVideoEntry.getTitle().getPlainText()+" relatedVideoLink "+loopVideoEntry.getRelatedVideosLink().getHref());
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ServiceException e) {
					e.printStackTrace();
				}
			}
			counterNextSeed++;
		}
		sessionEntry.addPlayListToSession(new PlayList(songEntryList,
				counterNextSeed, counterNextRecommendation + numSongs));
		System.out.println("Songs Retrieved " + songEntryList.size()
				+ ", Songs Requested " + numSongs
				+ ", CounterNextRecommendation "
				+ (counterNextRecommendation + numSongs));
		List<SongEntry> temp = seedPlayList.subList(counterNextRecommendation,
				counterNextRecommendation + numSongs);
		return temp;
	}

	private boolean FindSimilar(SongEntry songEntry,
			List<SongEntry> seedPlayList) {
		// TODO Auto-generated method stub
		Iterator<SongEntry> iterSeedPlayList = seedPlayList.iterator();
		Set<String> titleWordsSet = new HashSet<String>(songEntry
				.getTitleWordsSet());

		while (iterSeedPlayList.hasNext()) {
			SongEntry seedSongEntry = iterSeedPlayList.next();
			Set<String> seedSongSet = seedSongEntry.getTitleWordsSet();
			Set<String> intersection = new HashSet<String>(seedSongSet);
			intersection.retainAll(titleWordsSet);
			double score = intersection.size()
					/ (Math.min(seedSongEntry.getTitleWordsSet().size(),
							titleWordsSet.size()));
			if (score > 0.5) {
				System.out.println("DISCARD : '" + songEntry.getTitle()
						+ "' MATCHES '" + seedSongEntry.getTitle() + "'");
				return true;
			}
		}
		System.out.println("ADD : '" + songEntry.getTitle() + "'");
		return false;
	}
}
