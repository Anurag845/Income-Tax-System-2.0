package servlet_package;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import class_package.Computation;
import class_package.Employee;

/**
 * Servlet implementation class EmpDetails
 */
@WebServlet("/EmpDetails")
public class EmpDetails extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmpDetails() {
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
			
			rs.close();
			ps.close();
			cn.close();
			
			request.setAttribute("emp_id",emp_id);
			request.setAttribute("emp_name",emp_name);
			request.setAttribute("gross_sal",gross_sal);
			request.setAttribute("gotDet",true);
			request.setAttribute("employees",employees);

			request.getRequestDispatcher("dec_form.jsp").forward(request,response);
		}
		catch(ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

}
