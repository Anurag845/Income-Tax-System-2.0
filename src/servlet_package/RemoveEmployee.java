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

/**
 * Servlet implementation class RemoveEmployee
 */
@WebServlet("/RemoveEmployee")
public class RemoveEmployee extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RemoveEmployee() {
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
		
		ArrayList<String> empids = new ArrayList<String>();
		
		try {
			Connection cn = Computation.connect();
			PreparedStatement ps;
			ResultSet rs;
			
			ps = cn.prepareStatement("SELECT emp_id FROM employee ORDER BY emp_id");
			rs = ps.executeQuery();
			while(rs.next()) {
				String emp_id = rs.getString(1);
				empids.add(emp_id);				
			}
			
			int cnt = 0;
			for(String emp_id : empids) {
				if(request.getParameter(emp_id) != null) {
					ps = cn.prepareStatement("DELETE FROM employee WHERE emp_id = ?");
					ps.setString(1,emp_id);
					ps.executeUpdate();
					ps = cn.prepareStatement("DELETE FROM taxable WHERE emp_id = ?");
					ps.setString(1,emp_id);
					ps.executeUpdate();
					ps = cn.prepareStatement("DELETE FROM tax WHERE emp_id = ?");
					ps.setString(1,emp_id);
					ps.executeUpdate();
					ps = cn.prepareStatement("DELETE FROM declarations WHERE emp_id = ?");
					ps.setString(1,emp_id);
					ps.executeUpdate();
					cnt++;
				}
			}
			
			rs.close();
			ps.close();
			cn.close();
			
			String message = "Number of employees deleted is " + cnt;
			
			request.setAttribute("status_message",message);
			request.getRequestDispatcher("admin_status.jsp").forward(request,response);
		}
		catch(ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

}
