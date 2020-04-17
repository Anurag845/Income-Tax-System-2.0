package class_package;

public class Deduction {
	private int ded_id;
	private String deduction;
	private int amount;

	public Deduction(int ded_id, String deduction, int amount) {
		this.ded_id = ded_id;
		this.deduction = deduction;
		this.amount = amount;
	}

	public int getDed_id() {
		return ded_id;
	}

	public void setDed_id(int ded_id) {
		this.ded_id = ded_id;
	}

	public String getDeduction() {
		return deduction;
	}

	public void setDeduction(String deduction) {
		this.deduction = deduction;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	
}
