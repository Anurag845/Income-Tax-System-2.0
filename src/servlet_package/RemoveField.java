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
 * Servlet implementation class RemoveField
 */
@WebServlet("/RemoveField")
public class RemoveField extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RemoveField() {
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
		try {
			ArrayList<Integer> fids = new ArrayList<Integer>();
			ArrayList<Integer> sfids = new ArrayList<Integer>();
			ArrayList<Integer> dids = new ArrayList<Integer>();
			
			Connection cn = Computation.connect();
			PreparedStatement ps;
			ResultSet rs;
			
			ps = cn.prepareStatement("SELECT field_id FROM fields");
			rs = ps.executeQuery();
			while(rs.next()) {
				fids.add(rs.getInt(1));
			}
			
			for(int i = 0; i < fids.size(); i++) {
				int fid = fids.get(i);
				if(request.getParameter("f"+Integer.toString(fid)) != null) {
					ps = cn.prepareStatement("DELETE FROM fields WHERE field_id=?");
					ps.setInt(1,fid);
					ps.executeUpdate();
					ps = cn.prepareStatement("DELETE FROM subfields WHERE field_id=?");
					ps.setInt(1,fid);
					ps.executeUpdate();
				}
			}
			
			ps = cn.prepareStatement("SELECT subfield_id FROM subfields");
			rs = ps.executeQuery();
			while(rs.next()) {
				sfids.add(rs.getInt(1));
			}
			
			for(int i = 0; i < sfids.size(); i++) {
				int sfid = sfids.get(i);
				if(request.getParameter("s"+Integer.toString(sfid)) != null) {
					ps = cn.prepareStatement("DELETE FROM subfields WHERE subfield_id=?");
					ps.setInt(1,sfid);
					ps.executeUpdate();
				}
			}
			
			ps = cn.prepareStatement("SELECT ded_id FROM deductions");
			rs = ps.executeQuery();
			while(rs.next()) {
				dids.add(rs.getInt(1));
			}
			
			for(int j = 0; j < dids.size(); j++) {
				int did = dids.get(j);
				if(request.getParameter("d"+Integer.toString(did)) != null) {
					ps = cn.prepareStatement("DELETE FROM deductions WHERE ded_id=?");
					ps.setInt(1,did);
					ps.executeUpdate();
				}
			}
			
			rs.close();
			ps.close();
			cn.close();
			
			Computation.compute_all();
			
			request.setAttribute("status_message","Field deleted successfully.");
			request.getRequestDispatcher("admin_status.jsp").forward(request,response);
		}
		catch(ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

}
