package servlet_package;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import class_package.Computation;

/**
 * Servlet implementation class PushSalary
 */
@WebServlet("/PushSalary")
public class PushSalary extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PushSalary() {
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
		HashMap<String,Integer> id_oldsal = new HashMap<String,Integer>();
		HashMap<String,Integer> id_newsal = new HashMap<String,Integer>();
		HashSet<String> id_dec = new HashSet<String>();
		
		try {
			Connection cn = Computation.connect();
			PreparedStatement ps;
			ResultSet rs;
			
			ps = cn.prepareStatement("SELECT emp_id,gross_sal FROM employee");
			rs = ps.executeQuery();
			while(rs.next()) {
				id_oldsal.put(rs.getString(1),rs.getInt(2));
			}
			
			ps = cn.prepareStatement("SELECT emp_id FROM taxable");
			rs = ps.executeQuery();
			while(rs.next()) {
				id_dec.add(rs.getString(1));
			}
			
			int cnt = 0;
			for(String emp_id : id_oldsal.keySet()) {
				if(Computation.isInteger(request.getParameter(emp_id))) {
					int old_sal = id_oldsal.get(emp_id);
					int new_sal = Integer.parseInt(request.getParameter(emp_id));
					if(old_sal != new_sal) {
						ps = cn.prepareStatement("UPDATE employee SET gross_sal = ? WHERE emp_id = ?");
						ps.setInt(1,new_sal);
						ps.setString(2,emp_id);
						ps.executeUpdate();
						cnt++;
						if(id_dec.contains(emp_id)) {
							id_newsal.put(emp_id,new_sal);
						}
					}
				}
			}
			
			Computation.compute_all(id_newsal);
			
			request.setAttribute("status_message","Gross salary of " + cnt + " employees updated successfully.");
			request.getRequestDispatcher("admin_status.jsp").forward(request,response);
		}
		catch(ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

}
