package cl.fatman.hattrick;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;

import cl.fatman.hattrick.DataRecollector;
import cl.fatman.hattrick.Match;

public class DataRecollectorTest {
	
	@Test
	public void getMatchesTest() {
		String url = "http://www.hattrick.org";
		String userAgent = "Mozilla/5.0 (X11; Fedora; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.108 Safari/537.36";
		String query = "/Club/Matches/Archive.aspx?season=63&TeamID=973229";
		List<Match> matches;
		DataRecollector recollector = new DataRecollector(url, userAgent);
		matches = recollector.getMatches(query);
		assertThat("Matches should be greater than 0.", matches.size(), greaterThan(0));
	}

}
