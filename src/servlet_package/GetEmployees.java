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
 * Servlet implementation class GetEmployees
 */
@WebServlet("/GetEmployees")
public class GetEmployees extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetEmployees() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ArrayList<Employee> employees = new ArrayList<Employee>();
		try {
			Connection cn = Computation.connect();
			PreparedStatement ps;
			ResultSet rs;
			
			ps = cn.prepareStatement("SELECT * FROM employee ORDER BY emp_id::bytea");
			rs = ps.executeQuery();
			while(rs.next()) {
				Employee emp = new Employee(rs.getString(1),rs.getString(2),rs.getInt(3));
				employees.add(emp);
			}
			
			request.setAttribute("employees",employees);
			request.getRequestDispatcher("remove_emp.jsp").forward(request,response);
		}
		catch(ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
