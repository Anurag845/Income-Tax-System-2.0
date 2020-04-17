package servlet_package;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import class_package.Computation;

/**
 * Servlet implementation class Validate
 */
@WebServlet("/Validate")
public class Validate extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Validate() {
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
		
		HashSet<Integer> fids = new HashSet<Integer>();
		HashMap<Integer,Integer> sfids = new HashMap<Integer,Integer>();
		
		try {
			Connection cn = Computation.connect();
			PreparedStatement ps = null;
			ResultSet rs = null;
			
			ps = cn.prepareStatement("SELECT field_id FROM declarations WHERE emp_id = ? and subfield_id = ?");
			ps.setString(1,emp_id);
			ps.setInt(2,0);
			rs = ps.executeQuery();
			while(rs.next()) {
				fids.add(rs.getInt(1));
			}
			
			ps = cn.prepareStatement("SELECT subfield_id,field_id FROM declarations WHERE emp_id = ? and subfield_id != ?");
			ps.setString(1,emp_id);
			ps.setInt(2,0);
			rs = ps.executeQuery();
			while(rs.next()) {
				sfids.put(rs.getInt(1),rs.getInt(2));
			}
			
			Iterator<Integer> it = fids.iterator();
			while(it.hasNext()) {
				int field = it.next();
				if(Computation.isInteger(request.getParameter("f"+Integer.toString(field)))) {
					int value = Integer.parseInt(request.getParameter("f"+Integer.toString(field)));
					if(value > 0) {
						ps = cn.prepareStatement("UPDATE declarations SET amount_proved = ?, status = ? WHERE emp_id = ? and field_id = ?");
						ps.setInt(1,value);
						ps.setString(2,"proved");
						ps.setString(3,emp_id);
						ps.setInt(4,field);
						ps.executeUpdate();
					}
				}
			}
			
			for(int subfield_id : sfids.keySet()) {
				if(Computation.isInteger(request.getParameter("s"+Integer.toString(subfield_id)))) {
					int value = Integer.parseInt(request.getParameter("s"+Integer.toString(subfield_id)));
					if(value > 0) {
						//String field_id = sfids.get(subfield_id);
						ps = cn.prepareStatement("UPDATE declarations SET amount_proved = ?, status = ? WHERE emp_id = ? and subfield_id = ?");
						ps.setInt(1,value);
						ps.setString(2,"proved");
						ps.setString(3,emp_id);
						ps.setInt(4,subfield_id); //possibly subfield_id sufficient
						ps.executeUpdate();
					}
				}
			}
			
			Computation.compute_update(emp_id);
			
			rs.close();
			ps.close();
			cn.close();
			
			request.setAttribute("status_message","Validation performed successfully.");
			request.getRequestDispatcher("user_status.jsp").forward(request,response);
		}
		catch(ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

}
