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
import class_package.Tax;

/**
 * Servlet implementation class GetTax
 */
@WebServlet("/GetTax")
public class GetTax extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetTax() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			ArrayList<Tax> taxs = new ArrayList<Tax>();
			
			Connection cn = Computation.connect();
			PreparedStatement ps = null;
			ResultSet rs = null;
			
			ps = cn.prepareStatement("SELECT employee.emp_id,emp_name,April,May,June,July,August,September,October,November,December,January,February,March,Annual,Adjusted FROM tax NATURAL JOIN employee ORDER BY emp_id");
			rs = ps.executeQuery();
			while(rs.next()) {
				Tax t = new Tax(rs.getString(1),rs.getString(2),rs.getInt(3),rs.getInt(4),rs.getInt(5),rs.getInt(6),rs.getInt(7),rs.getInt(8),rs.getInt(9),rs.getInt(10),rs.getInt(11),rs.getInt(12),rs.getInt(13),rs.getInt(14),rs.getInt(15),rs.getInt(16));
				taxs.add(t);
			}
			
			rs.close();
			ps.close();
			cn.close();
			
			request.setAttribute("taxs",taxs);
			request.setAttribute("tax_cnt",taxs.size());
			request.getRequestDispatcher("tax.jsp").forward(request,response);
		}
		catch(ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
