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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
				if (matchType.equals("matchLeague")
						|| matchType.equals("matchCupA")
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
	
	public Match getMatch(String query, Match auxMatch, String teamId) {
		logger.debug("getMatch(String query, Match auxMatch, String teamId)");
		String urlQuery = this.url + query;
		logger.debug("URL: " + urlQuery);
		logger.debug("User agent: " + this.userAgent);
		Match match = new Match(auxMatch.getId(),
				auxMatch.getDate(),
				auxMatch.getType());
		try {
			logger.debug("Start processing hattrick match " + match.getId());
			Document document = Jsoup.connect(urlQuery)
					.header("Accept-Encoding", "gzip, deflate")
					.userAgent(this.userAgent)
					.maxBodySize(0)
					.timeout(10000)
					.get();
			String home = this.getHomeTeamId(document);
			logger.debug("Home team ID: " + home);
			int index = -1;
			if (teamId.equals(home)) {
				match.setCondition("Local");
				index = 0;
			} else {
				match.setCondition("Visita");
				index = 1;
			}
			logger.debug("Team " + teamId + " played like: " + match.getCondition());
			Elements ratingsTable = document.select("div[class=teamMatchRatingsTable]")
					.select("table");
			match.setFormation(this.getFormation(document, index));
			match.setMidfield(this.getRating(ratingsTable, 1, index ));
			match.setRightDefense(this.getRating(ratingsTable, 2, index ));
			match.setCentralDefense(this.getRating(ratingsTable, 3, index ));
			match.setLeftDefense(this.getRating(ratingsTable, 4, index ));
			match.setRightOffensive(this.getRating(ratingsTable, 5, index ));
			match.setCentralOffensive(this.getRating(ratingsTable, 6, index ));
			match.setLeftOffensive(this.getRating(ratingsTable, 7, index ));
			match.setDefensiveIFK(this.getRating(ratingsTable, 10, index ));
			match.setOffensiveIFK(this.getRating(ratingsTable, 11, index ));
			match.setTactics(this.getTacticStyle(ratingsTable, 14, index ));
			match.setStyle(this.getTacticStyle(ratingsTable, 16, index ));
			logger.info("========== MATCH INFO ==========");
			logger.info("ID: " + match.getId());
			logger.info("Date: " + match.getDate());
			logger.info("Type: " + match.getType());
			logger.info("Condition: " + match.getCondition());
			logger.info("Formation: " + match.getFormation());
			logger.info("Tactics: " + match.getTactics());
			logger.info("Style: " + match.getStyle());
			logger.info("Defensive IFK: " + match.getDefensiveIFK()
			+ " -- Ofensive IFK: " + match.getOffensiveIFK());
			logger.info("Right Defense: " + match.getRightDefense()
			+ " -- Central Defense: " + match.getCentralDefense()
			+ " -- Left Defense: " + match.getLeftDefense());
			logger.info("Midfield: " + match.getMidfield());
			logger.info("Right Ofensive: " + match.getRightOffensive()
			+ " -- Central Ofensive: " + match.getCentralOffensive()
			+ " -- Left Ofensive: " + match.getLeftOffensive());
			logger.info("HatStats: " + match.getHatStats());
		} catch (IOException e) {
			match = null;
			logger.error("Can not retrieve URL:" + urlQuery, e);
		} catch (IllegalArgumentException e) {
			match = null;
			logger.error("Provided URL is not valid.", e);
		}
		return match;
	}
	
	private String getHomeTeamId(Document document) {
		logger.debug("getHomeTeamId(Document document)");
		Elements aElement = document.select("a[class=hometeam notByLine]");
		String aux = aElement.get(0).attr("href");
		String home = aux.substring(aux.indexOf("=")+1, aux.indexOf("&"));
		return home;
	}
	
	private String getFormation(Document document, int index) {
		logger.debug("getFormation(Document document)");
		Elements spanElement = document.select("span[class=matchevent]");
		String aux = spanElement.text();
		logger.debug(aux);
		Pattern pattern = Pattern.compile("\\d-\\d-\\d");
		Matcher matcher = pattern.matcher(aux);
		List<String> formations = new ArrayList<String>();
		while (matcher.find())
			formations.add(matcher.group().replace("-", " - "));
		if (formations.size() < 2)
			index = 0;
		logger.debug("Formation: " + formations.get(index));
		return formations.get(index);
	}
	
	private String getTacticStyle(Elements ratingsTable, int position, int index ) {
		logger.debug("getTacticStyle(Elements ratingsTable, int position, int index )");
		Element tacticStyleElement = ratingsTable.select("tr").get(position);
		String tacticStyle = tacticStyleElement.select("td:not([class])")
				.get(index)
				.text();
		logger.debug("Value: "+ tacticStyle);
		return tacticStyle;
	}
	
	private int getRating(Elements ratingsTable, int position, int index ) {
		logger.debug("getRating(Elements ratingTable, int position, int index )");
		Element ratingElement = ratingsTable.select("tr").get(position);
		int value = Integer.parseInt(ratingElement.select("td[class=teamNumberRatings]")
				.get(index)
				.text());
		logger.debug("Value: "+ value);
		return value;
	}

}