package servlet_package;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import class_package.Computation;
import class_package.Employee;
import class_package.Field;
import class_package.Subfield;

/**
 * Servlet implementation class FormPref
 */
@WebServlet("/FormPref")
public class FormPref extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FormPref() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("resource")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pref = request.getParameter("pref");
		String emp_id = request.getParameter("emp_id");
		String emp_name = null;
		ArrayList<Employee> employees = new ArrayList<Employee>();
		ArrayList<Field> dbfields = new ArrayList<Field>();
		ArrayList<Subfield> dbsubfields = new ArrayList<Subfield>();
		ArrayList<Field> fields = new ArrayList<Field>();
		HashSet<Integer> visited = new HashSet<Integer>();
		HashMap<Integer,Integer> f_amounts = new HashMap<Integer,Integer>();
		HashMap<Integer,Integer> s_amounts = new HashMap<Integer,Integer>();
		HashMap<Integer,String> f_status = new HashMap<Integer,String>();
		HashMap<Integer,String> s_status = new HashMap<Integer,String>();
		ArrayList<String> all_status = new ArrayList<String>();
		
		int gross_sal = 0;
		
		try {
			Connection cn = Computation.connect();
			PreparedStatement ps = null;
			ResultSet rs = null;
			
			ps = cn.prepareStatement("SELECT * FROM employee ORDER BY emp_id");
			rs = ps.executeQuery();
			while(rs.next()) {
				Employee emp = new Employee(rs.getString(1),rs.getString(2),rs.getInt(3));
				employees.add(emp);
			}
			
			ps = cn.prepareStatement("SELECT * FROM employee WHERE emp_id = ?");
			ps.setString(1,emp_id);
			rs = ps.executeQuery();
			if(rs.next()) {
				emp_name = rs.getString(2);
				gross_sal = rs.getInt(3);
			}
			
			request.setAttribute("emp_id",emp_id);
			request.setAttribute("emp_name",emp_name);
			request.setAttribute("gross_sal",gross_sal);
			request.setAttribute("gotDet",true);
			request.setAttribute("formPref",true);
			request.setAttribute("employees",employees);
			request.setAttribute("pref",pref);
			
			if(pref.equals("New")) {
				String message = null;
				ps = cn.prepareStatement("DELETE FROM declarations WHERE emp_id=?");
				ps.setString(1,emp_id);
				ps.executeUpdate();
				ps = cn.prepareStatement("DELETE FROM taxable WHERE emp_id = ?");
				ps.setString(1,emp_id);
				ps.executeUpdate();
				ps = cn.prepareStatement("DELETE FROM tax WHERE emp_id = ?");
				ps.setString(1,emp_id);
				ps.executeUpdate();
				
				message = "New";
				request.setAttribute("prefSet",message);
				
				fields = Computation.getFields();
				request.setAttribute("fields",fields);
			}
			else {
				int present = 0;
				ps = cn.prepareStatement("SELECT COUNT(*) FROM Taxable WHERE emp_id=?");
				ps.setString(1,emp_id);
				rs = ps.executeQuery();
				if(rs.next()) {
					present = rs.getInt(1);
				}
				
				if(present == 0) {
					request.setAttribute("absent",true);
					request.getRequestDispatcher("dec_form.jsp").forward(request,response);
					return;
				}
				else if(pref.equals("Delete")) {
					String message = null;
					ps = cn.prepareStatement("DELETE FROM declarations WHERE emp_id=?");
					ps.setString(1,emp_id);
					ps.executeUpdate();
					ps = cn.prepareStatement("DELETE FROM taxable WHERE emp_id = ?");
					ps.setString(1,emp_id);
					ps.executeUpdate();
					ps = cn.prepareStatement("DELETE FROM tax WHERE emp_id = ?");
					ps.setString(1,emp_id);
					ps.executeUpdate();
					
					message = "Delete";
					request.setAttribute("prefSet",message);
				}
				else if(pref.equals("Update")) {
					int amount;
					String status;
					int id;
					ps = cn.prepareStatement("SELECT * FROM declarations WHERE emp_id=?");
					ps.setString(1,emp_id);
					rs = ps.executeQuery();
					
					while(rs.next()) {
						status = rs.getString(6);
						if(status.equals("pending")) {
							amount = rs.getInt(4);
						}
						else {
							amount = rs.getInt(5);
						}
						if(rs.getInt(7) == 0) {
							id = rs.getInt(3);
							f_amounts.put(id,amount);
							f_status.put(id,status);
						}
						else {
							id = rs.getInt(7);
							s_amounts.put(id,amount);
							s_status.put(id,status);
						}
					}
				
					ps = cn.prepareStatement("SELECT fields.field_id,field,tax_limit,sub_field,subfields.subfield_id,subfields.subfield FROM fields LEFT OUTER JOIN subfields ON fields.field_id = subfields.field_id;");
					rs = ps.executeQuery();
					while(rs.next()) {
						Field field = new Field(rs.getInt(1),rs.getString(2),rs.getInt(3),rs.getString(4),null);
						dbfields.add(field);
						if(rs.getString(4).equals("yes")) {
							Subfield subfield = new Subfield(rs.getInt(1),rs.getInt(5),rs.getString(6));
							dbsubfields.add(subfield);
						}
					}
					
					for(int i = 0; i < dbfields.size(); i++) {
						Field field = dbfields.get(i);
						int amt;
						String st;
						if(field.getSub_field().equals("no")) {
							if(f_amounts.containsKey(field.getField_id())) {
								amt = f_amounts.get(field.getField_id());
							}
							else {
								amt = 0;
							}
							field.setAmount(amt);
							fields.add(field);
							if(f_status.containsKey(field.getField_id())) {
								st = f_status.get(field.getField_id());
							}
							else {
								st = "pending";
							}
							all_status.add(st);
						}
						else if(field.getSub_field().equals("yes") && !visited.contains(field.getField_id())) {
							ArrayList<Subfield> subfields = new ArrayList<Subfield>();
							int field_id = field.getField_id();
							visited.add(field_id);
							for(int j = 0; j < dbsubfields.size(); j++) {
								Subfield subfield = dbsubfields.get(j);
								if(s_amounts.containsKey(subfield.getSubfield_id())) {
									amt = s_amounts.get(subfield.getSubfield_id());
								}
								else {
									amt = 0;
								}
								if(subfield.getField_id() == field_id) {
									subfield.setAmount(amt);
									subfields.add(subfield);
									if(s_status.containsKey(subfield.getSubfield_id())) {
										st = s_status.get(subfield.getSubfield_id());
									}
									else {
										st = "pending";
									}
									all_status.add(st);
								}
							}
							field.setSubfields(subfields);
							fields.add(field);
						}
					}
					request.setAttribute("all_status",all_status);
					request.setAttribute("fields",fields);
					String message = "Update";
					request.setAttribute("prefSet",message);
				}
			}

			rs.close();
			ps.close();
			cn.close();
			request.getRequestDispatcher("dec_form.jsp").forward(request,response);
		} 
		catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

}
