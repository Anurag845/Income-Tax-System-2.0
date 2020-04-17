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
import class_package.Taxable;


/**
 * Servlet implementation class Taxable
 */
@WebServlet("/GetTaxable")
public class GetTaxable extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetTaxable() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			ArrayList<Taxable> taxables = new ArrayList<Taxable>();
			
			Connection cn = Computation.connect();
			PreparedStatement ps = null;
			ResultSet rs = null;
			
			ps = cn.prepareStatement("SELECT employee.emp_id,emp_name,April,May,June,July,August,September,October,November,December,January,February,March,Annual,Adjusted FROM taxable NATURAL JOIN employee ORDER BY emp_id");
			rs = ps.executeQuery();
			while(rs.next()) {
				Taxable t = new Taxable(rs.getString(1),rs.getString(2),rs.getInt(3),rs.getInt(4),rs.getInt(5),rs.getInt(6),rs.getInt(7),rs.getInt(8),rs.getInt(9),rs.getInt(10),rs.getInt(11),rs.getInt(12),rs.getInt(13),rs.getInt(14),rs.getInt(15),rs.getInt(16));
				taxables.add(t);
			}
			
			rs.close();
			ps.close();
			cn.close();
			
			request.setAttribute("taxables",taxables);
			request.setAttribute("taxable_cnt",taxables.size());
			request.getRequestDispatcher("taxable.jsp").forward(request,response);
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
