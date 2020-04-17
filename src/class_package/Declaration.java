package class_package;

public class Declaration {
	private String emp_id;
	private int id;
	private String field_name;
	private int amount_declared;
	private int amount_proved;
	private String status;

	public Declaration(String emp_id,int id,String field_name,int amount_declared,int amount_proved,String status) {
		this.setEmp_id(emp_id);
		this.setId(id);
		this.setField_name(field_name);
		this.setAmount_declared(amount_declared);
		this.setAmount_proved(amount_proved);
		this.setStatus(status);
	}

	public String getEmp_id() {
		return emp_id;
	}

	public void setEmp_id(String emp_id) {
		this.emp_id = emp_id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getAmount_declared() {
		return amount_declared;
	}

	public void setAmount_declared(int amount_declared) {
		this.amount_declared = amount_declared;
	}

	public int getAmount_proved() {
		return amount_proved;
	}

	public void setAmount_proved(int amount_proved) {
		this.amount_proved = amount_proved;
	}

	public String getField_name() {
		return field_name;
	}

	public void setField_name(String field_name) {
		this.field_name = field_name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
