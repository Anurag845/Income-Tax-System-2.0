package class_package;

import java.util.ArrayList;

public class Field {
	private int field_id;
	private String field;
	private int tax_limit;
	private String sub_field;
	private ArrayList<Subfield> subfields;
	private int amount;
	
	public Field(int field_id,String field,int tax_limit,String sub_field,ArrayList<Subfield> subfields) {
		this.setField_id(field_id);
		this.setField(field);
		this.setTax_limit(tax_limit);
		this.setSub_field(sub_field);
		this.setSubfields(subfields);
	}

	public int getField_id() {
		return field_id;
	}

	public void setField_id(int field_id) {
		this.field_id = field_id;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public int getTax_limit() {
		return tax_limit;
	}

	public void setTax_limit(int tax_limit) {
		this.tax_limit = tax_limit;
	}

	public String getSub_field() {
		return sub_field;
	}

	public void setSub_field(String sub_field) {
		this.sub_field = sub_field;
	}

	public ArrayList<Subfield> getSubfields() {
		return subfields;
	}

	public void setSubfields(ArrayList<Subfield> subfields) {
		this.subfields = subfields;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
}
