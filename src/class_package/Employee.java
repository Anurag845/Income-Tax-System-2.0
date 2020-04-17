package class_package;

public class Employee {
	
	private String emp_id;
	private String emp_name;
	private int gross_sal;
	
	public Employee(String emp_id, String emp_name, int gross_sal) {
		this.setEmp_id(emp_id);
		this.setEmp_name(emp_name);
		this.setGross_sal(gross_sal);
	}

	public String getEmp_id() {
		return emp_id;
	}

	public void setEmp_id(String emp_id) {
		this.emp_id = emp_id;
	}

	public String getEmp_name() {
		return emp_name;
	}

	public void setEmp_name(String emp_name) {
		this.emp_name = emp_name;
	}

	public int getGross_sal() {
		return gross_sal;
	}

	public void setGross_sal(int gross_sal) {
		this.gross_sal = gross_sal;
	}

}
