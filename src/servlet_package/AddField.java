package servlet_package;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import class_package.Computation;

/**
 * Servlet implementation class AddField
 */
@WebServlet("/AddField")
public class AddField extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddField() {
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
		String type = request.getParameter("type");
		
		try {
			
			Connection cn = Computation.connect();
			PreparedStatement ps = null;
			ResultSet rs = null;
			
			if(type.equals("field")) {
				String fname = request.getParameter("dec_name");
				int limit = Integer.parseInt(request.getParameter("dec_limit"));
				String sub_field = request.getParameter("sub_field_present");
				
				ps = cn.prepareStatement("INSERT INTO fields(field,tax_limit,sub_field) VALUES(?,?,?) RETURNING field_id");
				ps.setString(1,fname);
				ps.setInt(2,limit);
				ps.setString(3,sub_field);
				rs = ps.executeQuery();
				
				if(sub_field.equals("yes")) {
					if(rs.next()) {
						int fid = rs.getInt(1);
						String subfields[] = request.getParameterValues("sub_field");
						for(int i = 0; i < subfields.length; i++) {
							ps = cn.prepareStatement("INSERT INTO subfields(field_id,subfield) VALUES(?,?)");
							ps.setInt(1,fid);
							ps.setString(2,subfields[i]);
							ps.executeUpdate();
						}
					}
				}
			}
			else if(type.equals("subfield")) {
				int fid = Integer.parseInt(request.getParameter("field_name"));
				String subfield = request.getParameter("sf_name");
				
				ps = cn.prepareStatement("INSERT INTO subfields(field_id,subfield) VALUES(?,?)");
				ps.setInt(1,fid);
				ps.setString(2,subfield);
				ps.executeUpdate();
			}
			else if(type.equals("deduc")) {
				String ded_name = request.getParameter("ded_name");
				int ded_value = Integer.parseInt(request.getParameter("ded_value"));
				ps = cn.prepareStatement("INSERT INTO deductions(deduction,amount) VALUES(?,?)");
				ps.setString(1,ded_name);
				ps.setInt(2,ded_value);
				ps.executeUpdate();
			}
			
			ps.close();
			cn.close();
			
			Computation.compute_all();
			
			request.setAttribute("status_message","Field added successfully.");
			request.getRequestDispatcher("admin_status.jsp").forward(request,response);
		}
		catch(ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

}
