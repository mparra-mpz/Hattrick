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
	
	public List<String> getMatches(String query) {
		logger.debug("getMatches(String query)");
		String urlQuery = this.url + query;
		logger.debug("URL: " + urlQuery);
		logger.debug("User agent: " + this.userAgent);
		List<String> matches = new ArrayList<String>();
		try {
			logger.debug("Start processing hattrick matches.");
			Document document = Jsoup.connect(urlQuery)
					.header("Accept-Encoding", "gzip, deflate")
					.userAgent(this.userAgent)
					.maxBodySize(0)
					.timeout(10000)
					.get();
			logger.debug("Get matches table.");
			Elements rows = document.select("table[class=indent]").select("tr");
			for (Element row : rows) {
				logger.debug("Get match row.");
				Elements cols = row.select("td");
				String matchClass = cols.get(1).select("img").get(0).attr("class");
				logger.debug("Got match class: " + matchClass);
				if (matchClass.equals("matchLeague") || matchClass.equals("matchCupA")
						|| matchClass.equals("matchQualification")) {
					String aux = cols.get(2).select("a").get(0).attr("href");
					String matchID = aux.substring(aux.indexOf("=")+1, aux.indexOf("&"));
					logger.debug("Got match id: " + matchID);
					matches.add(matchID);
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
