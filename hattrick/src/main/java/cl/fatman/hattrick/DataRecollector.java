package cl.fatman.hattrick;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cl.fatman.hattrick.Match;

public class DataRecollector {
	
	private String url;
	private String userAgent;
	private static final Logger logger = LogManager.getLogger(DataRecollector.class);
	
	public DataRecollector(String url, String userAgent) {
		super();
		this.url = url;
		this.userAgent = userAgent;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUserAgent() {
		return userAgent;
	}
	
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	
	public List<Match> getMatches(String query) {
		logger.debug("getMatches(String query)");
		String urlQuery = this.url + query;
		logger.debug("URL: " + urlQuery);
		logger.debug("User agent: " + this.userAgent);
		List<Match> matches = new ArrayList<Match>();
		try {
			logger.debug("Start processing hattrick matches.");
			Document document = Jsoup.connect(urlQuery)
					.header("Accept-Encoding", "gzip, deflate")
					.userAgent(this.userAgent)
					.maxBodySize(0)
					.timeout(10000)
					.get();
			Elements rows = document.select("table[class=indent]").select("tr");
			for (Element row : rows) {
				Elements cols = row.select("td");
				String matchDate = cols.get(0).select("span").get(0).text();
				String matchType = cols.get(1).select("img").get(0).attr("class");
				
				if (matchType.equals("matchLeague") || matchType.equals("matchCupA")
						|| matchType.equals("matchQualification")) {
					String aux = cols.get(2).select("a").get(0).attr("href");
					String matchID = aux.substring(aux.indexOf("=")+1, aux.indexOf("&"));
					logger.debug("Got match id: " + matchID);
					logger.debug("Got match date: " + matchDate);
					logger.debug("Got match type: " + matchType);
					Match match  = new  Match(matchID, matchDate, matchType);
					matches.add(match);
				}
			}
			logger.debug("Return " + matches.size() + " matches.");
		} catch (IOException e) {
			matches = null;
			logger.error("Can not retrieve URL:" + urlQuery, e);
		} catch (IllegalArgumentException e) {
			matches = null;
			logger.error("Provided URL is not valid.", e);
		}
		return matches;
	}

}
