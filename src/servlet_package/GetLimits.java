package servlet_package;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import class_package.Computation;
import class_package.Deduction;
import class_package.Field;

/**
 * Servlet implementation class GetLimits
 */
@WebServlet("/GetLimits")
public class GetLimits extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetLimits() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ArrayList<Field> fields;
		ArrayList<Deduction> deds = new ArrayList<Deduction>();
		
		try {
			
			fields = Computation.getFields();
			deds = Computation.getDeducs();
			
			request.setAttribute("fields",fields);
			request.setAttribute("deductions",deds);
			
			request.getRequestDispatcher("limits.jsp").forward(request,response);
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
