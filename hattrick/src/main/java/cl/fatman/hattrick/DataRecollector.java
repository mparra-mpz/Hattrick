package cl.fatman.hattrick;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

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
		return null;
	}

}
