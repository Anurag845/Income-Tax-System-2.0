package class_package;

public class Slab {
	private int lower;
	private int upper;
	private int percent;
	
	public Slab(int l,int u,int p) {
		lower = l;
		upper = u;
		percent = p;
	}

	public int getLower() {
		return lower;
	}

	public void setLower(int lower) {
		this.lower = lower;
	}

	public int getUpper() {
		return upper;
	}

	public void setUpper(int upper) {
		this.upper = upper;
	}

	public int getPercent() {
		return percent;
	}

	public void setPercent(int percent) {
		this.percent = percent;
	}
}