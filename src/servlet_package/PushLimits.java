package servlet_package;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import class_package.Computation;

/**
 * Servlet implementation class PushLimits
 */
@WebServlet("/PushLimits")
public class PushLimits extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PushLimits() {
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
		
		HashMap<Integer,Integer> f_lim = new HashMap<Integer,Integer>();
		HashMap<Integer,Integer> d_amt = new HashMap<Integer,Integer>();
		
		try {
			Connection cn = Computation.connect();
			PreparedStatement ps;
			ResultSet rs;
			
			ps = cn.prepareStatement("SELECT field_id,tax_limit FROM fields");
			rs = ps.executeQuery();
			while(rs.next()) {
				f_lim.put(rs.getInt(1),rs.getInt(2));
			}
			
			for(int fid : f_lim.keySet()) {
				if(Computation.isInteger(request.getParameter("f"+Integer.toString(fid)))) {
					int new_lim = Integer.parseInt(request.getParameter("f"+Integer.toString(fid)));
					int old_lim = f_lim.get(fid);
					if(new_lim != old_lim) {
						ps = cn.prepareStatement("UPDATE fields SET tax_limit = ? WHERE field_id = ?");
						ps.setInt(1,new_lim);
						ps.setInt(2,fid);
						ps.executeUpdate();
					}
				}
			}
			
			ps = cn.prepareStatement("SELECT ded_id,amount FROM deductions");
			rs = ps.executeQuery();
			while(rs.next()) {
				d_amt.put(rs.getInt(1),rs.getInt(2));
			}
			
			for(Integer did : d_amt.keySet()) {
				if(Computation.isInteger(request.getParameter("d"+Integer.toString(did)))) {
					int new_val = Integer.parseInt(request.getParameter("d"+Integer.toString(did)));
					int old_val = d_amt.get(did);
					if(new_val != old_val) {
						ps = cn.prepareStatement("UPDATE deductions SET amount = ? WHERE ded_id = ?");
						ps.setInt(1,new_val);
						ps.setInt(2,did);
						ps.executeUpdate();
					}
				}
			}
			
			rs.close();
			ps.close();
			cn.close();
			
			Computation.compute_all();
			
			request.setAttribute("status_message","Limits updated successfully.");
			request.getRequestDispatcher("admin_status.jsp").forward(request,response);
		}
		catch(ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

}
