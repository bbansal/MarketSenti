package com.desipandora.impl.web;

import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.desipandora.api.DesiPandoraService;
import com.desipandora.api.SongEntry;
import com.desipandora.youTube.DesiPandoraServiceYouTube;

public class DesiPandoraServlet extends HttpServlet {
	private static final Logger logger = Logger
			.getLogger(DesiPandoraServlet.class);
	private static final long serialVersionUID = 1L;

	private DesiPandoraService _service;

	private final static String OPERATION = "operation";
	private final static String GET_NEW_SESSION_ID = "getNewSessionId";
	private final static String SEARCH = "search";
	private final static String FEEDBACK = "feedback";
	private final static String GET_NEXT_SONGS = "getNextSongs";
	private final static String KEYWORDS = "keywords";
	private final static String NUM_SONGS = "numSongs";
	private final static String USER_ID = "uid";
	private final static String USER_SESSION_ID = "userSessionId";
	private final static String SONG_LIST = "songList";

	@Override
	public void init(ServletConfig config) throws ServletException {
		_service = new DesiPandoraServiceYouTube();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Cookie[] cookies = req.getCookies();
		Enumeration headers = req.getHeaderNames();
		String contextPath = req.getContextPath();
		String queryString = req.getQueryString();
		String sessionId = req.getRequestedSessionId();
		String pathInfo = req.getPathInfo();

		String reqString = getRequestAsString(cookies, headers, contextPath,
				queryString, sessionId, pathInfo);

		int operationCode = getOperation(req.getParameter(OPERATION));
		if (checkValidOperatonCode(operationCode, reqString, resp)) {
			logger.debug("handling request:" + reqString);
			handleOperation(operationCode, req, resp);
		}
	}

	private void handleOperation(int operationCode, HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		try {
			switch (operationCode) {
			case 1:
				handleGetNewSessionId(req, resp);
				break;
			case 2:
				handleSearch(req, resp);
				break;
			case 3:
				handleGetNextSongs(req, resp);
				break;
			case 4:
				handleFeedback(req, resp);
				break;
			default:
				resp.sendError(resp.SC_NOT_IMPLEMENTED,
						"No recognized operation paramter found.");
			}
		} catch (Exception e) {
			logger.error(e);
			resp.sendError(resp.SC_NOT_MODIFIED, " failed with exception:" + e);
		}

		resp.getWriter().flush();
		resp.getWriter().close();
	}

	private void handleFeedback(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub

	}

	private void handleGetNextSongs(HttpServletRequest req,
			HttpServletResponse resp) {
		// TODO Auto-generated method stub

	}

	private void handleSearch(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, JSONException {
		String keywordString = req.getParameter(KEYWORDS);
		String sessionId = req.getParameter(USER_SESSION_ID);
		String numSongString = (null != req.getParameter(NUM_SONGS)) ? req
				.getParameter(NUM_SONGS) : "10";
		int numSongs = Integer.parseInt(numSongString);

		if (null == keywordString || null == sessionId)
			throw new RuntimeException("empty keyword string passed in search.");

		List<SongEntry> songs = _service.getFirstFewSongs(Arrays
				.asList(keywordString), sessionId, numSongs);
		logger.debug("search results received for userSession:" + sessionId
				+ " keywords:" + keywordString + ":" + songs);
		JSONArray songArray = new JSONArray();
		for (SongEntry song : songs) {
			songArray.put(song.getSongId());
		}

		JSONObject object = new JSONObject();
		object.put(SONG_LIST, songArray);
		resp.getWriter().print(object.toString());
	}

	private void handleGetNewSessionId(HttpServletRequest req,
			HttpServletResponse resp) throws JSONException, IOException {
		String uId = (null != req.getParameter(USER_ID)) ? req
				.getParameter(USER_ID) : "defaultUser";
		String sessionId = _service.createSessionId(uId);

		JSONObject object = new JSONObject();
		object.put(USER_SESSION_ID, sessionId);
		resp.getWriter().print(object.toString());
	}

	private boolean checkValidOperatonCode(int operationCode, String reqString,
			HttpServletResponse resp) throws IOException {
		if (-1 == operationCode) {
			logger.error("Request recieved w/o any valid operation parameter:"
					+ reqString);
			resp.sendError(resp.SC_NOT_IMPLEMENTED,
					"No recognized operation paramter found.");
			return false;

		}

		return true;
	}

	private int getOperation(String parameter) {
		if (null == parameter) {
			return -1;
		} else if (GET_NEW_SESSION_ID.equals(parameter)) {
			return 1;
		} else if (SEARCH.equals(parameter)) {
			return 2;
		} else if (GET_NEXT_SONGS.equals(parameter)) {
			return 3;
		} else if (FEEDBACK.equals(parameter)) {
			return 4;
		}

		return -1;
	}

	private String getRequestAsString(Cookie[] cookies, Enumeration headers,
			String contextPath, String queryString, String sessionId,
			String pathInfo) {
		StringBuilder builder = new StringBuilder();

		builder.append("(" + "cookies:" + cookies + ")");
		builder.append("(" + "headers:" + headers + ")");
		builder.append("(" + "contextPath:" + contextPath + ")");
		builder.append("(" + "queryString:" + queryString + ")");
		builder.append("(" + "sessionId:" + sessionId + ")");
		builder.append("(" + "pathInfo:" + pathInfo + ")");

		return builder.toString();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	}

}
