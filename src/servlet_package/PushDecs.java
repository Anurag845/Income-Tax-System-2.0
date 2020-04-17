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
 * Servlet implementation class PushDecs
 */
@WebServlet("/PushDecs")
public class PushDecs extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PushDecs() {
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
		String type = request.getParameter("type");
		String emp_id = request.getParameter("emp_id");
		
		HashSet<Integer> fids = new HashSet<Integer>();
		HashMap<Integer,Integer> sfids = new HashMap<Integer,Integer>();
		
		try {
			Connection cn = Computation.connect();
			PreparedStatement ps = null;
			ResultSet rs = null;
			
			ps = cn.prepareStatement("SELECT field_id FROM fields WHERE sub_field = ?");
			ps.setString(1,"no");
			rs = ps.executeQuery();
			while(rs.next()) {
				fids.add(rs.getInt(1));
			}
			
			ps = cn.prepareStatement("SELECT subfield_id,field_id FROM subfields");
			rs = ps.executeQuery();
			while(rs.next()) {
				sfids.put(rs.getInt(1),rs.getInt(2));
			}
			
			
			if(type.equals("New")) {
				Iterator<Integer> it = fids.iterator();
				while(it.hasNext()) {
					int field = it.next();
					if(Computation.isInteger(request.getParameter("f"+Integer.toString(field)))) {
						int value = Integer.parseInt(request.getParameter("f"+Integer.toString(field)));
						if(value > 0) {
							ps = cn.prepareStatement("INSERT INTO declarations (emp_id,field_id,amount_declared,amount_proved,status,subfield_id) VALUES(?,?,?,?,?,?)");
							ps.setString(1,emp_id);
							ps.setInt(2,field);
							ps.setInt(3,value);
							ps.setInt(4,0);
							ps.setString(5,"pending");
							ps.setInt(6,0);
							ps.executeUpdate();
						}
					}
				}
				
				for(int subfield_id : sfids.keySet()) {
					if(Computation.isInteger(request.getParameter("s"+Integer.toString(subfield_id)))) {
						int value = Integer.parseInt(request.getParameter("s"+Integer.toString(subfield_id)));
						if(value > 0) {
							int field_id = sfids.get(subfield_id);
							ps = cn.prepareStatement("INSERT INTO declarations (emp_id,field_id,amount_declared,amount_proved,status,subfield_id) VALUES(?,?,?,?,?,?)");
							ps.setString(1,emp_id);
							ps.setInt(2,field_id);
							ps.setInt(3,value);
							ps.setInt(4,0);
							ps.setString(5,"pending");
							ps.setInt(6,subfield_id);
							ps.executeUpdate();
						}
					}
				}
				Computation.compute_new(emp_id);
				request.setAttribute("status_message","New declaration form submitted successfully.");
				request.getRequestDispatcher("user_status.jsp").forward(request,response);
			}
			else if(type.equals("Update")) {
				
				HashSet<Integer> dec_fids = new HashSet<Integer>();
				HashSet<Integer> dec_sfids = new HashSet<Integer>();
				
				ps = cn.prepareStatement("SELECT field_id FROM declarations WHERE emp_id = ? and subfield_id = ?");
				ps.setString(1,emp_id);
				ps.setInt(2,0);
				rs = ps.executeQuery();
				while(rs.next()) {
					dec_fids.add(rs.getInt(1));
				}
				
				ps = cn.prepareStatement("SELECT subfield_id FROM declarations WHERE emp_id = ? and subfield_id != ?");
				ps.setString(1,emp_id);
				ps.setInt(2,0);
				rs = ps.executeQuery();
				while(rs.next()) {
					dec_sfids.add(rs.getInt(1));
				}				
				
				Iterator<Integer> it = fids.iterator();
				while(it.hasNext()) {
					int field = it.next();
					if(Computation.isInteger(request.getParameter("f"+Integer.toString(field)))) {
						int value = Integer.parseInt(request.getParameter("f"+Integer.toString(field)));
						if(value > 0 && !dec_fids.contains(field)) {
							ps = cn.prepareStatement("INSERT INTO declarations (emp_id,field_id,amount_declared,amount_proved,status,subfield_id) VALUES(?,?,?,?,?,?)");
							ps.setString(1,emp_id);
							ps.setInt(2,field);
							ps.setInt(3,value);
							ps.setInt(4,0);
							ps.setString(5,"pending");
							ps.setInt(6,0);
							ps.executeUpdate();
						}
						else if(value > 0 && dec_fids.contains(field)) {
							ps = cn.prepareStatement("UPDATE declarations SET amount_declared = ? WHERE emp_id = ? and field_id = ?");
							ps.setInt(1,value);
							ps.setString(2,emp_id);
							ps.setInt(3,field);
							ps.executeUpdate();
						}
						else if(value == 0 && dec_fids.contains(field)) {
							ps = cn.prepareStatement("DELETE FROM declarations WHERE emp_id = ? and field_id = ?");
							ps.setString(1,emp_id);
							ps.setInt(2,field);
							ps.executeUpdate();
						}
					}
				}
				
				for(int subfield_id : sfids.keySet()) {
					if(Computation.isInteger(request.getParameter("s"+Integer.toString(subfield_id)))) {
						int value = Integer.parseInt(request.getParameter("s"+Integer.toString(subfield_id)));
						if(value > 0 && !dec_sfids.contains(subfield_id)) {
							int field_id = sfids.get(subfield_id);
							ps = cn.prepareStatement("INSERT INTO declarations (emp_id,field_id,amount_declared,amount_proved,status,subfield_id) VALUES(?,?,?,?,?,?)");
							ps.setString(1,emp_id);
							ps.setInt(2,field_id);
							ps.setInt(3,value);
							ps.setInt(4,0);
							ps.setString(5,"pending");
							ps.setInt(6,subfield_id);
							ps.executeUpdate();
						}
						else if(value > 0 && dec_sfids.contains(subfield_id)) {
							ps = cn.prepareStatement("UPDATE declarations SET amount_declared = ? WHERE emp_id = ? and subfield_id = ?");
							ps.setInt(1,value);
							ps.setString(2,emp_id);
							ps.setInt(3,subfield_id);
							ps.executeUpdate();
						}
						else if(value == 0 && dec_sfids.contains(subfield_id)) {
							ps = cn.prepareStatement("DELETE FROM declarations WHERE emp_id = ? and subfield_id = ?");
							ps.setString(1,emp_id);
							ps.setInt(2,subfield_id);
							ps.executeUpdate();
						}
					}
				}
				rs.close();
				ps.close();
				cn.close();
				
				Computation.compute_update(emp_id);
				request.setAttribute("status_message","Updated declaration form submitted successfully.");
				request.getRequestDispatcher("user_status.jsp").forward(request,response);
			}		
		}
		catch(ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

}
