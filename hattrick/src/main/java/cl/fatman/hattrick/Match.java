package cl.fatman.hattrick;

public class Match {
	
	private String id;
	private String date;
	private String type;
	private String condition;
	private int defensiveIFK;
	private int offensiveIFK;
	private int midfield;
	private int rightDefense;
	private int centralDefense;
	private int leftDefense;
	private int rightOffensive;
	private int centralOffensive;
	private int leftOffensive;
	private int hatStats;
	
	public Match(String id, String date, String type) {
		super();
		this.id = id;
		this.date = date;
		this.type = type;
		this.hatStats = 0;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public int getDefensiveIFK() {
		return defensiveIFK;
	}

	public void setDefensiveIFK(int defensiveIFK) {
		this.defensiveIFK = defensiveIFK;
	}

	public int getOffensiveIFK() {
		return offensiveIFK;
	}

	public void setOffensiveIFK(int offensiveIFK) {
		this.offensiveIFK = offensiveIFK;
	}

	public int getMidfield() {
		return midfield;
	}

	public void setMidfield(int midfield) {
		this.midfield = midfield;
	}

	public int getRightDefense() {
		return rightDefense;
	}

	public void setRightDefense(int rightDefense) {
		this.rightDefense = rightDefense;
	}

	public int getCentralDefense() {
		return centralDefense;
	}

	public void setCentralDefense(int centralDefense) {
		this.centralDefense = centralDefense;
	}

	public int getLeftDefense() {
		return leftDefense;
	}

	public void setLeftDefense(int leftDefense) {
		this.leftDefense = leftDefense;
	}

	public int getRightOffensive() {
		return rightOffensive;
	}

	public void setRightOffensive(int rightOffensive) {
		this.rightOffensive = rightOffensive;
	}

	public int getCentralOffensive() {
		return centralOffensive;
	}

	public void setCentralOffensive(int centralOffensive) {
		this.centralOffensive = centralOffensive;
	}

	public int getLeftOffensive() {
		return leftOffensive;
	}

	public void setLeftOffensive(int leftOffensive) {
		this.leftOffensive = leftOffensive;
	}
	
	public int getHatStats() {
		if (this.hatStats == 0)
			this.hatStats = (3 * this.midfield)
			+ this.rightDefense + this.centralDefense + this.leftDefense
			+ this.rightOffensive + this.centralOffensive + this.leftOffensive;
		return this.hatStats;
	}
}
