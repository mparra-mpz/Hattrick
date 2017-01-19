package cl.fatman.hattrick;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;

import cl.fatman.hattrick.DataRecollector;

public class DataRecollectorTest {

	@Test
	public void test() {
		fail("Not yet implemented");
	}
	
	@Test
	public void getMatchesTest() {
		String url = "";
		String userAgent = "";
		String query = "";
		List<String> matches;
		DataRecollector recollector = new DataRecollector(url, userAgent);
		matches = recollector.getMatches(query);
		assertThat("Matches should be greater than 0.", matches.size(), greaterThan(0));
	}

}
