package servlet_package;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import class_package.Computation;

/**
 * Servlet implementation class AddEmployee
 */
@WebServlet("/AddEmployee")
@MultipartConfig
public class AddEmployee extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddEmployee() {
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
		String no_emp = request.getParameter("no_emp");
		String message = "";
		try {
			Connection cn = Computation.connect();
			PreparedStatement ps = null;
			ResultSet rs = null;
			if(no_emp.equals("one")) {
				String emp_id = request.getParameter("emp_id");
				String emp_name = request.getParameter("emp_name");
				int gross_sal = Integer.parseInt(request.getParameter("gross_sal"));
				
				int cnt = 0;
				ps = cn.prepareStatement("SELECT COUNT(*) FROM employee WHERE emp_id = ?");
				ps.setString(1,emp_id);
				rs = ps.executeQuery();
				if(rs.next()) {
					cnt = rs.getInt(1);
				}
				
				if(cnt == 1) {
					message = "Employee with id " + emp_id + " already exists.";
				}
				else if(emp_id.length() > 6 || emp_name.length() > 50) {
					message = "Invalid input record!";
				}
				else {
					ps = cn.prepareStatement("INSERT INTO employee VALUES(?,?,?)");
					ps.setString(1,emp_id);
					ps.setString(2,emp_name);
					ps.setInt(3,gross_sal);
					ps.executeUpdate();
					message = "Employee record inserted successfully.";
				}
			}
			else if(no_emp.equals("many")) {
				ArrayList<String> duplicates = new ArrayList<String>();
				HashSet<String> emp_ids = new HashSet<String>();
				
				int valid_cnt = 0;
				int invalid_cnt = 0;
				
				ps = cn.prepareStatement("SELECT emp_id FROM employee");
				rs = ps.executeQuery();
				while(rs.next()) {
					emp_ids.add(rs.getString(1));
				}
				
				Part filePart = request.getPart("csv");
			    InputStream fileContent = filePart.getInputStream();
			    BufferedReader reader = new BufferedReader(new InputStreamReader(fileContent));
			    while(reader.ready()) {
			    	String line = reader.readLine();
			    	String elements[] = line.split(",");
			    	if(emp_ids.contains(elements[0])) {
			    		duplicates.add(elements[0]);
			    	}
			    	else if(elements.length != 3) {
			    		invalid_cnt++;
			    	}
			    	else if(elements[0].length() > 6 || elements[1].length() > 50 || !Computation.isInteger(elements[2])) {
			    		invalid_cnt++;
			    	}
			    	else {
			    		ps = cn.prepareStatement("INSERT INTO employee VALUES(?,?,?)");
				    	ps.setString(1,elements[0]);
				    	ps.setString(2,elements[1]);
				    	ps.setInt(3,Integer.parseInt(elements[2]));
				    	ps.executeUpdate();
				    	valid_cnt++;
			    	}
			    }
			    
			    message = "Number of invalid entries = " + Integer.toString(invalid_cnt) + ". Number of valid entries = " + Integer.toString(valid_cnt) + ". Valid entries (if any) have been successfully added. <br><br>";
			    if(duplicates.size() > 0) {
			    	String list = "";
			    	for(int i = 0; i < duplicates.size()-1; i++) {
			    		list += duplicates.get(i) + ", ";
			    	}
			    	list += duplicates.get(duplicates.size()-1) + ". ";
			    	message += "Following ids are already present in the database " + list + "Rest (if any) have been successfully added.";
			    }
			}
			rs.close();
			ps.close();
			cn.close();
			request.setAttribute("status_message",message);
			request.getRequestDispatcher("admin_status.jsp").forward(request,response);
		}
		catch(ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

}
