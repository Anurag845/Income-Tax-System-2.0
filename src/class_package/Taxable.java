package class_package;

public class Taxable {
	private String emp_id;
	private String emp_name;
	private int april;
	private int may;
	private int june;
	private int july;
	private int august;
	private int september;
	private int october;
	private int november;
	private int december;
	private int january;
	private int february;
	private int march;
	private int annual;
	private int adjusted;
	
	public Taxable(String emp_id, String emp_name, int april, int may, int june, int july, int august, int september, int october,
			int november, int december, int january, int february, int march, int annual, int adjusted) {
		super();
		this.emp_id = emp_id;
		this.emp_name = emp_name;
		this.april = april;
		this.may = may;
		this.june = june;
		this.july = july;
		this.august = august;
		this.september = september;
		this.october = october;
		this.november = november;
		this.december = december;
		this.january = january;
		this.february = february;
		this.march = march;
		this.annual = annual;
		this.adjusted = adjusted;
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
	public int getApril() {
		return april;
	}
	public void setApril(int april) {
		this.april = april;
	}
	public int getMay() {
		return may;
	}
	public void setMay(int may) {
		this.may = may;
	}
	public int getJune() {
		return june;
	}
	public void setJune(int june) {
		this.june = june;
	}
	public int getJuly() {
		return july;
	}
	public void setJuly(int july) {
		this.july = july;
	}
	public int getAugust() {
		return august;
	}
	public void setAugust(int august) {
		this.august = august;
	}
	public int getSeptember() {
		return september;
	}
	public void setSeptember(int september) {
		this.september = september;
	}
	public int getOctober() {
		return october;
	}
	public void setOctober(int october) {
		this.october = october;
	}
	public int getNovember() {
		return november;
	}
	public void setNovember(int november) {
		this.november = november;
	}
	public int getDecember() {
		return december;
	}
	public void setDecember(int december) {
		this.december = december;
	}
	public int getJanuary() {
		return january;
	}
	public void setJanuary(int january) {
		this.january = january;
	}
	public int getFebruary() {
		return february;
	}
	public void setFebruary(int february) {
		this.february = february;
	}
	public int getMarch() {
		return march;
	}
	public void setMarch(int march) {
		this.march = march;
	}
	public int getAnnual() {
		return annual;
	}
	public void setAnnual(int annual) {
		this.annual = annual;
	}
	public int getAdjusted() {
		return adjusted;
	}
	public void setAdjusted(int adjusted) {
		this.adjusted = adjusted;
	}
}
