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
import class_package.Slab;

/**
 * Servlet implementation class GetSlabs
 */
@WebServlet("/GetSlabs")
public class GetSlabs extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetSlabs() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		ArrayList<Slab> slabs = new ArrayList<Slab>();
		
		try {
			Connection cn = Computation.connect();
			PreparedStatement ps;
			ResultSet rs;
			
			ps = cn.prepareStatement("SELECT * FROM tax_slabs");
			rs = ps.executeQuery();
			while(rs.next()) {
				Slab s = new Slab(rs.getInt(1),rs.getInt(2),rs.getInt(3));
				slabs.add(s);
			}
			
			Computation.compute_all();
			
			rs.close();
			ps.close();
			cn.close();
			
			request.setAttribute("slabs",slabs);
			request.setAttribute("slabs_cnt",slabs.size());
			request.getRequestDispatcher("slabs.jsp").forward(request,response);
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
