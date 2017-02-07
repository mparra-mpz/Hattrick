package cl.fatman.hattrick;

import java.util.List;

import cl.fatman.hattrick.DataRecollector;
import cl.fatman.hattrick.Match;

public class App 
{
    public static void main( String[] args )
    {
    	String teamId = args[0];
    	String season = args[1];
    	String url = "http://www.hattrick.org";
		String userAgent = "Mozilla/5.0 (X11; Fedora; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.108 Safari/537.36";
		String baseMatches = "/Club/Matches/Archive.aspx?season={0}&TeamID={1}";
		
		DataRecollector dataRec = new DataRecollector(url, userAgent);
		String queryMatches;
		queryMatches = baseMatches.replace("{0}", season).replace("{1}", teamId);
		
		List<Match> auxMatches = dataRec.getMatches(queryMatches);
		for (Match aux : auxMatches) {
			String baseMatch = "/Club/Matches/Match.aspx?matchID={2}";
			String queryMatch = baseMatch.replace("{2}", aux.getId());
			Match match = dataRec.getMatch(queryMatch, aux, teamId);
			String msg = match.getDate() + "\t" + match.getCondition() + "\t"
					+ match.getFormation() + "\t" + match.getTactics() + "\t" + match.getStyle() + "\t"
					+ match.getDefensiveIFK() + "\t" + match.getOffensiveIFK() + "\t"
					+ match.getMidfield() + "\t"
					+ match.getRightDefense() + "\t" + match.getCentralDefense() + "\t" + match.getLeftDefense() + "\t"
					+ match.getRightOffensive() + "\t" + match.getCentralOffensive() + "\t" + match.getLeftOffensive();
			System.out.println(msg);
		}
    }
}
