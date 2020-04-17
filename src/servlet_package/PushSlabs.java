package servlet_package;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
 * Servlet implementation class PushSlabs
 */
@WebServlet("/PushSlabs")
public class PushSlabs extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PushSlabs() {
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
		ArrayList<Slab> slabs = new ArrayList<Slab>();
		
		String low_par[] = request.getParameterValues("lower");
		String up_par[] = request.getParameterValues("upper");
		String per_par[] = request.getParameterValues("percent");
		
		for(int i = 0; i < low_par.length; i++) {
			int lower = Integer.parseInt(low_par[i]);
			int upper = Integer.parseInt(up_par[i]);
			int percent = Integer.parseInt(per_par[i]);
			
			if(lower != 0 || upper != 0) {
				Slab s = new Slab(lower,upper,percent);
				slabs.add(s);
			}
		}
		
		try {
			Connection cn = Computation.connect();
			PreparedStatement ps;
			
			ps = cn.prepareStatement("DELETE FROM tax_slabs");
			ps.executeUpdate();
			
			for(int k = 0; k < slabs.size(); k++) {
				Slab s = slabs.get(k);
				ps = cn.prepareStatement("INSERT INTO tax_slabs VALUES(?,?,?)");
				ps.setInt(1,s.getLower());
				ps.setInt(2,s.getUpper());
				ps.setInt(3,s.getPercent());
				ps.executeUpdate();
			}
			
			ps.close();
			cn.close();
			
			Computation.compute_all();
			
			request.setAttribute("status_message","Tax slabs updated successfully.");
			request.getRequestDispatcher("admin_status.jsp").forward(request,response);
		}
		catch(ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

}
