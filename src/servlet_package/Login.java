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
import javax.servlet.http.HttpSession;

import class_package.Computation;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
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
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		try {
			Connection cn = Computation.connect();
			PreparedStatement ps;
			ResultSet rs;
			String message="";
			ps = cn.prepareStatement("SELECT * FROM users WHERE username=?");
			ps.setString(1,username);
			rs = ps.executeQuery();
			if(rs.next()) {
				if(rs.getString("password").equals(password)) {
					HttpSession session = request.getSession();
					session.setAttribute("designation",username);
					String destination = null;
					if(username.equals("user")) {
						destination = "user_menu.html";
					}
					else if(username.equals("admin")) {
						destination = "admin_menu.html";
					}
					response.sendRedirect(destination);
				}
				else {
					message = "Incorrect password!";
					request.setAttribute("message",message);
					request.getRequestDispatcher("login.jsp").forward(request,response);
				}
			}
			else {
				message = "Incorrect username!";
				request.setAttribute("message",message);
				request.getRequestDispatcher("login.jsp").forward(request,response);
			}
			rs.close();
			ps.close();
			cn.close();
		}
		catch(SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
