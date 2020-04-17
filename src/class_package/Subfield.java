package class_package;

public class Subfield {
	private int field_id;
	private int subfield_id;
	private String subfield;
	private int amount;
	
	public	Subfield(int field_id,int subfield_id,String subfield) {
		this.setField_id(field_id);
		this.setSubfield_id(subfield_id);
		this.setSubfield(subfield);
	}

	public int getField_id() {
		return field_id;
	}

	public void setField_id(int field_id) {
		this.field_id = field_id;
	}

	public int getSubfield_id() {
		return subfield_id;
	}

	public void setSubfield_id(int subfield_id) {
		this.subfield_id = subfield_id;
	}

	public String getSubfield() {
		return subfield;
	}

	public void setSubfield(String subfield) {
		this.subfield = subfield;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

}
