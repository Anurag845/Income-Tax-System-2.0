package servlet_package;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import class_package.Computation;
import class_package.Declaration;
import class_package.Employee;

/**
 * Servlet implementation class EmpDetailsVal
 */
@WebServlet("/EmpDetailsVal")
public class EmpDetailsVal extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmpDetailsVal() {
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String emp_id = request.getParameter("emp_id");
		String emp_name = null;
		int gross_sal = 0;
		ArrayList<Employee> employees = new ArrayList<Employee>();
		ArrayList<Declaration> fdecs = new ArrayList<Declaration>();
		ArrayList<Declaration> sdecs = new ArrayList<Declaration>();
		HashMap<Integer,String> id_name = new HashMap<Integer,String>();
		try {
			Connection cn = Computation.connect();
			PreparedStatement ps;
			ResultSet rs;
			
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
			request.setAttribute("employees",employees);
			
			int present = 0;
			ps = cn.prepareStatement("SELECT COUNT(*) FROM Taxable WHERE emp_id=?");
			ps.setString(1,emp_id);
			rs = ps.executeQuery();
			if(rs.next()) {
				present = rs.getInt(1);
			}
			
			if(present == 0) {
				request.setAttribute("absent",true);
				request.getRequestDispatcher("dec_validate.jsp").forward(request,response);
			}
			else {			
				ps = cn.prepareStatement("SELECT field_id,field FROM fields");
				rs = ps.executeQuery();
				while(rs.next()) {
					id_name.put(rs.getInt(1),rs.getString(2));
				}
				
				ps = cn.prepareStatement("SELECT subfield_id,subfield FROM subfields");
				rs = ps.executeQuery();
				while(rs.next()) {
					id_name.put(rs.getInt(1),rs.getString(2));
				}
				
				ps = cn.prepareStatement("SELECT * FROM declarations WHERE emp_id = ?");
				ps.setString(1,emp_id);
				rs = ps.executeQuery();
				while(rs.next()) {
					String name = "";
					int id = 0;
					int field_id = rs.getInt(3);
					int subfield_id = rs.getInt(7);
					if(subfield_id == 0) {
						name = id_name.get(field_id);
						id = field_id;
						Declaration dec = new Declaration(rs.getString(2),id,name,rs.getInt(4),rs.getInt(5),rs.getString(6));
						fdecs.add(dec);
					}
					else {
						name = id_name.get(subfield_id);
						id = subfield_id;
						Declaration dec = new Declaration(rs.getString(2),id,name,rs.getInt(4),rs.getInt(5),rs.getString(6));
						sdecs.add(dec);
					}
				}
				
				request.setAttribute("fdecs",fdecs);
				request.setAttribute("sdecs",sdecs);
				request.setAttribute("fdecsCount",fdecs.size());
				request.setAttribute("sdecsCount",sdecs.size());
				request.getRequestDispatcher("dec_validate.jsp").forward(request,response);
			}
			rs.close();
			ps.close();
			cn.close();
		}
		catch(ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
}
