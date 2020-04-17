package class_package;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;

public class Computation {
	
	public static Connection connect() throws ClassNotFoundException, SQLException {
		Class.forName("org.postgresql.Driver");
		Connection cn = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/Income_Tax", "postgres", "dell");
		return cn;
	}
	
	public static boolean isInteger(String str) {
	    if (str == null) {
	        return false;
	    }
	    int length = str.length();
	    if (length == 0) {
	        return false;
	    }
	    int i = 0;
	    if (str.charAt(0) == '-') {
	        if (length == 1) {
	            return false;
	        }
	        i = 1;
	    }
	    for (; i < length; i++) {
	        char c = str.charAt(i);
	        if (c < '0' || c > '9') {
	            return false;
	        }
	    }
	    return true;
	}

	public static void compute_new(String emp_id) throws ClassNotFoundException,SQLException {
		Class.forName("org.postgresql.Driver");
		Connection cn = connect();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		ArrayList<Declaration> fdecs = new ArrayList<Declaration>();
		ArrayList<Declaration> sfdecs = new ArrayList<Declaration>();
		HashMap<Integer,Integer> fid_lim = new HashMap<Integer,Integer>();
		HashMap<Integer,Integer> sfid_fid = new HashMap<Integer,Integer>();
		HashSet<Integer> visited = new HashSet<Integer>();
		ArrayList<Slab> slabs = new ArrayList<Slab>();
		
		int gross_sal = 0;
		
		ps = cn.prepareStatement("SELECT gross_sal from employee WHERE emp_id = ?");
		ps.setString(1,emp_id);
		rs = ps.executeQuery();
		if(rs.next()) {
			gross_sal = rs.getInt(1);
		}
		
		ps = cn.prepareStatement("SELECT * FROM declarations WHERE emp_id = ?");
		ps.setString(1,emp_id);
		rs = ps.executeQuery();
		while(rs.next()) {
			int id = 0;
			int fid = rs.getInt(3);
			int sfid = rs.getInt(7);
			id = (sfid == 0) ? fid : sfid;
			Declaration dec = new Declaration(rs.getString(2),id,"",rs.getInt(4),rs.getInt(5),rs.getString(6));
			if(sfid == 0) {
				fdecs.add(dec);
			}
			else {
				sfdecs.add(dec);
			}
		}
		
		ps = cn.prepareStatement("SELECT field_id,tax_limit FROM fields");
		rs = ps.executeQuery();
		while(rs.next()) {
			fid_lim.put(rs.getInt(1),rs.getInt(2));
		}
		
		ps = cn.prepareStatement("SELECT subfield_id,field_id FROM subfields");
		rs = ps.executeQuery();
		while(rs.next()) {
			sfid_fid.put(rs.getInt(1),rs.getInt(2));
		}
		
		int taxable = gross_sal;
		
		ps = cn.prepareStatement("SELECT amount FROM deductions");
		rs = ps.executeQuery();
		while(rs.next()) {
			taxable -= rs.getInt(1);
		}
		
		for(int i = 0; i < fdecs.size(); i++) {
			Declaration dec = fdecs.get(i);
			int id = dec.getId();
			int amount = dec.getAmount_declared();
			int limit = fid_lim.get(id);
			taxable = (amount < limit) ? taxable - amount : taxable - limit;			
		}
		
		for(int j = 0; j < sfdecs.size(); j++) {
			Declaration dec = sfdecs.get(j);
			int sfid = dec.getId();
			int fid = sfid_fid.get(sfid);
			if(!visited.contains(fid)) {
				visited.add(fid);
				int limit = fid_lim.get(fid);
				int val = 0;
				for(Declaration d : sfdecs) {
					if(sfid_fid.get(d.getId()).equals(fid)) {
						val += d.getAmount_declared();
					}
				}
				taxable = (val < limit) ? taxable - val : taxable - limit;
			}
		}
		
		int month = Calendar.getInstance().get(Calendar.MONTH);
		int no_months = (month < 3) ? 3-month : 12-month+3;
		
		int taxable_monthly = (int) Math.ceil(taxable/(double)no_months);
		
		int adjusted = taxable_monthly*no_months;
		
		ps = cn.prepareStatement("SELECT * FROM tax_slabs");
		rs = ps.executeQuery();
		while(rs.next()) {
			Slab s = new Slab(rs.getInt(1),rs.getInt(2),rs.getInt(3));
			slabs.add(s);
		}
		
		int tax = 0;
		for(int k = 0; k < slabs.size(); k++) {
			Slab s = slabs.get(k);
			if(adjusted > s.getUpper()) {
				tax += (s.getUpper()-s.getLower())*s.getPercent();
			}
			else if(adjusted > s.getLower()) {
				tax += (adjusted-s.getLower())*s.getPercent();
				break;
			}
		}
		
		tax = (int) Math.ceil(tax/(double)100);
		int tax_monthly = (int) Math.ceil(tax/(double)no_months);
		
		int taxable_arr[] = new int[12];
		int tax_arr[] = new int[12];
		for(int i = 0; i < 12-no_months; i++) {
			taxable_arr[i] = 0;
			tax_arr[i] = 0;
		}
		for(int i = 12-no_months; i < 12; i++) {
			taxable_arr[i] = taxable_monthly;
			tax_arr[i] = tax_monthly;
		}
		
		int tax_adjusted = tax_monthly*no_months;
		
		ps = cn.prepareStatement("INSERT INTO taxable VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		ps.setString(1,emp_id);
		ps.setInt(2,taxable_arr[0]);
		ps.setInt(3,taxable_arr[1]);
		ps.setInt(4,taxable_arr[2]);
		ps.setInt(5,taxable_arr[3]);
		ps.setInt(6,taxable_arr[4]);
		ps.setInt(7,taxable_arr[5]);
		ps.setInt(8,taxable_arr[6]);
		ps.setInt(9,taxable_arr[7]);
		ps.setInt(10,taxable_arr[8]);
		ps.setInt(11,taxable_arr[9]);
		ps.setInt(12,taxable_arr[10]);
		ps.setInt(13,taxable_arr[11]);
		ps.setInt(14,taxable);
		ps.setInt(15,adjusted);
		ps.executeUpdate();
		
		ps = cn.prepareStatement("INSERT INTO tax VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		ps.setString(1,emp_id);
		ps.setInt(2,tax_arr[0]);
		ps.setInt(3,tax_arr[1]);
		ps.setInt(4,tax_arr[2]);
		ps.setInt(5,tax_arr[3]);
		ps.setInt(6,tax_arr[4]);
		ps.setInt(7,tax_arr[5]);
		ps.setInt(8,tax_arr[6]);
		ps.setInt(9,tax_arr[7]);
		ps.setInt(10,tax_arr[8]);
		ps.setInt(11,tax_arr[9]);
		ps.setInt(12,tax_arr[10]);
		ps.setInt(13,tax_arr[11]);
		ps.setInt(14,tax);
		ps.setInt(15,tax_adjusted);
		ps.executeUpdate();
		
		rs.close();
		ps.close();
		cn.close();
 	}
	
	
	public static void compute_update(String emp_id) throws ClassNotFoundException,SQLException {
		Class.forName("org.postgresql.Driver");
		Connection cn = connect();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		ArrayList<Declaration> fdecs = new ArrayList<Declaration>();
		ArrayList<Declaration> sfdecs = new ArrayList<Declaration>();
		HashMap<Integer,Integer> fid_lim = new HashMap<Integer,Integer>();
		HashMap<Integer,Integer> sfid_fid = new HashMap<Integer,Integer>();
		HashSet<Integer> visited = new HashSet<Integer>();
		ArrayList<Slab> slabs = new ArrayList<Slab>();
		
		int gross_sal = 0;
		
		ps = cn.prepareStatement("SELECT gross_sal from employee WHERE emp_id = ?");
		ps.setString(1,emp_id);
		rs = ps.executeQuery();
		if(rs.next()) {
			gross_sal = rs.getInt(1);
		}
		
		ps = cn.prepareStatement("SELECT * FROM declarations WHERE emp_id = ?");
		ps.setString(1,emp_id);
		rs = ps.executeQuery();
		while(rs.next()) {
			int id = 0;
			int fid = rs.getInt(3);
			int sfid = rs.getInt(7);
			id = (sfid == 0) ? fid : sfid;
			Declaration dec = new Declaration(rs.getString(2),id,"",rs.getInt(4),rs.getInt(5),rs.getString(6));
			if(sfid == 0) {
				fdecs.add(dec);
			}
			else {
				sfdecs.add(dec);
			}
		}
		
		ps = cn.prepareStatement("SELECT field_id,tax_limit FROM fields");
		rs = ps.executeQuery();
		while(rs.next()) {
			fid_lim.put(rs.getInt(1),rs.getInt(2));
		}
		
		ps = cn.prepareStatement("SELECT subfield_id,field_id FROM subfields");
		rs = ps.executeQuery();
		while(rs.next()) {
			sfid_fid.put(rs.getInt(1),rs.getInt(2));
		}
		
		int taxable = gross_sal;
		
		ps = cn.prepareStatement("SELECT amount FROM deductions");
		rs = ps.executeQuery();
		while(rs.next()) {
			taxable -= rs.getInt(1);
		}
		
		for(int i = 0; i < fdecs.size(); i++) {
			Declaration dec = fdecs.get(i);
			int id = dec.getId();
			int amount = (dec.getStatus().equals("pending")) ? dec.getAmount_declared() : dec.getAmount_proved();
			int limit = fid_lim.get(id);
			taxable = (amount < limit) ? taxable - amount : taxable - limit;			
		}
		
		for(int j = 0; j < sfdecs.size(); j++) {
			Declaration dec = sfdecs.get(j);
			int sfid = dec.getId();
			int fid = sfid_fid.get(sfid);
			if(!visited.contains(fid)) {
				visited.add(fid);
				int limit = fid_lim.get(fid);
				int val = 0;
				for(Declaration d : sfdecs) {
					if(sfid_fid.get(d.getId()).equals(fid)) {
						val = (d.getStatus().equals("pending")) ? val + d.getAmount_declared() : val + d.getAmount_proved();
					}
				}
				taxable = (val < limit) ? taxable - val : taxable - limit;
			}
		}
		
		int month = Calendar.getInstance().get(Calendar.MONTH);
		int no_months = (month < 3) ? 3-month : 12-month+3;
		
		int taxable_arr[] = new int[12];
		int tax_arr[] = new int[12];
		
		int past_taxable = 0;
		int past_tax = 0;
		
		ps = cn.prepareStatement("SELECT * FROM taxable WHERE emp_id = ?");
		ps.setString(1,emp_id);
		if(rs.next()) {
			for(int i = 0; i < 12-no_months; i++) {
				taxable_arr[i] = rs.getInt(i+2);
				past_taxable += taxable_arr[i];
			}
		}
		
		ps = cn.prepareStatement("SELECT * FROM tax WHERE emp_id = ?");
		ps.setString(1,emp_id);
		if(rs.next()) {
			for(int i = 0; i < 12-no_months; i++) {
				tax_arr[i] = rs.getInt(i+2);
				past_tax += tax_arr[i];
			}
		}
		
		int adjust = taxable-past_taxable;
		
		int adjusted_month = (int) Math.ceil(adjust/(double)no_months);
		
		for(int i = 12-no_months; i < 12; i++) {
			taxable_arr[i] = adjusted_month;
		}
		
		int adjusted = 0;
		for(int i = 0; i < 12; i++) {
			adjusted += taxable_arr[i];
		}
		
		ps = cn.prepareStatement("SELECT * FROM tax_slabs");
		rs = ps.executeQuery();
		while(rs.next()) {
			Slab s = new Slab(rs.getInt(1),rs.getInt(2),rs.getInt(3));
			slabs.add(s);
		}
		
		int tax = 0;
		for(int k = 0; k < slabs.size(); k++) {
			Slab s = slabs.get(k);
			if(adjusted > s.getUpper()) {
				tax += (s.getUpper()-s.getLower())*s.getPercent();
			}
			else if(adjusted > s.getLower()) {
				tax += (adjusted-s.getLower())*s.getPercent();
				break;
			}
		}
		
		tax = (int) Math.ceil(tax/(double)100);
		
		int adjust_tax = tax-past_tax;
		int tax_month = (int) Math.ceil(adjust_tax/(double)no_months);
		
		for(int i = 12-no_months; i < 12; i++) {
			tax_arr[i] = tax_month;
		}
		
		int rounded_tax = 0;
		for(int i = 0; i < 12; i++) {
			rounded_tax += tax_arr[i];
		}
		
		ps = cn.prepareStatement("UPDATE taxable SET april=?,may=?,june=?,july=?,august=?,september=?,october=?,november=?,december=?,january=?,february=?,march=?,annual=?,adjusted=? WHERE emp_id=?");
		ps.setInt(1,taxable_arr[0]);
		ps.setInt(2,taxable_arr[1]);
		ps.setInt(3,taxable_arr[2]);
		ps.setInt(4,taxable_arr[3]);
		ps.setInt(5,taxable_arr[4]);
		ps.setInt(6,taxable_arr[5]);
		ps.setInt(7,taxable_arr[6]);
		ps.setInt(8,taxable_arr[7]);
		ps.setInt(9,taxable_arr[8]);
		ps.setInt(10,taxable_arr[9]);
		ps.setInt(11,taxable_arr[10]);
		ps.setInt(12,taxable_arr[11]);
		ps.setInt(13,taxable);
		ps.setInt(14,adjusted);
		ps.setString(15,emp_id);
		ps.executeUpdate();
		
		ps = cn.prepareStatement("UPDATE tax SET april=?,may=?,june=?,july=?,august=?,september=?,october=?,november=?,december=?,january=?,february=?,march=?,annual=?,adjusted=? WHERE emp_id=?");
		ps.setInt(1,tax_arr[0]);
		ps.setInt(2,tax_arr[1]);
		ps.setInt(3,tax_arr[2]);
		ps.setInt(4,tax_arr[3]);
		ps.setInt(5,tax_arr[4]);
		ps.setInt(6,tax_arr[5]);
		ps.setInt(7,tax_arr[6]);
		ps.setInt(8,tax_arr[7]);
		ps.setInt(9,tax_arr[8]);
		ps.setInt(10,tax_arr[9]);
		ps.setInt(11,tax_arr[10]);
		ps.setInt(12,tax_arr[11]);
		ps.setInt(13,tax);
		ps.setInt(14,rounded_tax);
		ps.setString(15,emp_id);
		ps.executeUpdate();

		rs.close();
		ps.close();
		cn.close();
	}
	
	public static void compute_all() throws ClassNotFoundException, SQLException {
		Class.forName("org.postgresql.Driver");
		Connection cn = connect();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		ArrayList<Integer> deducs = new ArrayList<Integer>();
		ArrayList<Employee> emps = new ArrayList<Employee>();
		HashMap<Integer,Integer> fid_lim = new HashMap<Integer,Integer>();
		HashMap<Integer,Integer> sfid_fid = new HashMap<Integer,Integer>();
		ArrayList<Slab> slabs = new ArrayList<Slab>();
		
		ps = cn.prepareStatement("SELECT field_id,tax_limit FROM fields");
		rs = ps.executeQuery();
		while(rs.next()) {
			fid_lim.put(rs.getInt(1),rs.getInt(2));
		}
		
		ps = cn.prepareStatement("SELECT subfield_id,field_id FROM subfields");
		rs = ps.executeQuery();
		while(rs.next()) {
			sfid_fid.put(rs.getInt(1),rs.getInt(2));
		}
		
		ps = cn.prepareStatement("SELECT amount FROM deductions");
		rs = ps.executeQuery();
		while(rs.next()) {
			deducs.add(rs.getInt(1));
		}
		
		ps = cn.prepareStatement("SELECT * FROM tax_slabs");
		rs = ps.executeQuery();
		while(rs.next()) {
			Slab s = new Slab(rs.getInt(1),rs.getInt(2),rs.getInt(3));
			slabs.add(s);
		}
		
		ps = cn.prepareStatement("SELECT emp_id,emp_name,gross_sal FROM employee NATURAL JOIN taxable;");
		rs = ps.executeQuery();
		while(rs.next()) {
			emps.add(new Employee(rs.getString(1),rs.getString(2),rs.getInt(3)));
		}
		
		for(int cnt = 0; cnt < emps.size(); cnt++) {
			Employee emp = emps.get(cnt);
			String emp_id = emp.getEmp_id();
			int gross_sal = emp.getGross_sal();
			
			ArrayList<Declaration> fdecs = new ArrayList<Declaration>();
			ArrayList<Declaration> sfdecs = new ArrayList<Declaration>();
						
			ps = cn.prepareStatement("SELECT * FROM declarations WHERE emp_id = ?");
			ps.setString(1,emp_id);
			rs = ps.executeQuery();
			while(rs.next()) {
				int id = 0;
				int fid = rs.getInt(3);
				int sfid = rs.getInt(7);
				id = (sfid == 0) ? fid : sfid;
				Declaration dec = new Declaration(rs.getString(2),id,"",rs.getInt(4),rs.getInt(5),rs.getString(6));
				if(sfid == 0) {
					fdecs.add(dec);
				}
				else {
					sfdecs.add(dec);
				}
			}
			
			int taxable = gross_sal;
			
			for(int i = 0; i < fdecs.size(); i++) {
				Declaration dec = fdecs.get(i);
				int id = dec.getId();
				int amount = (dec.getStatus().equals("pending")) ? dec.getAmount_declared() : dec.getAmount_proved();
				if(fid_lim.containsKey(id)) {
					int limit = fid_lim.get(id);
					taxable = (amount < limit) ? taxable - amount : taxable - limit;
				}			
			}
			
			HashSet<Integer> visited = new HashSet<Integer>();
			for(int j = 0; j < sfdecs.size(); j++) {
				Declaration dec = sfdecs.get(j);
				int sfid = dec.getId();
				int fid = sfid_fid.get(sfid);
				if(!visited.contains(fid) && fid_lim.containsKey(fid)) {
					visited.add(fid);
					int limit = fid_lim.get(fid);
					int val = 0;
					for(Declaration d : sfdecs) {
						if(sfid_fid.get(d.getId()).equals(fid)) {
							val = (d.getStatus().equals("pending")) ? val + d.getAmount_declared() : val + d.getAmount_proved();
						}
					}
					taxable = (val < limit) ? taxable - val : taxable - limit;
				}
			}
			
			for(int k = 0; k < deducs.size(); k++) {
				taxable -= deducs.get(k);
			}
			
			int month = Calendar.getInstance().get(Calendar.MONTH);
			int no_months = (month < 3) ? 3-month : 12-month+3;
			
			int taxable_arr[] = new int[12];
			int tax_arr[] = new int[12];
			
			int past_taxable = 0;
			int past_tax = 0;
			
			ps = cn.prepareStatement("SELECT * FROM taxable WHERE emp_id = ?");
			ps.setString(1,emp_id);
			if(rs.next()) {
				for(int i = 0; i < 12-no_months; i++) {
					taxable_arr[i] = rs.getInt(i+2);
					past_taxable += taxable_arr[i];
				}
			}
			
			ps = cn.prepareStatement("SELECT * FROM tax WHERE emp_id = ?");
			ps.setString(1,emp_id);
			if(rs.next()) {
				for(int i = 0; i < 12-no_months; i++) {
					tax_arr[i] = rs.getInt(i+2);
					past_tax += tax_arr[i];
				}
			}
			
			int adjust = taxable-past_taxable;
			
			int adjusted_month = (int) Math.ceil(adjust/(double)no_months);
			
			for(int i = 12-no_months; i < 12; i++) {
				taxable_arr[i] = adjusted_month;
			}
			
			int adjusted = 0;
			for(int i = 0; i < 12; i++) {
				adjusted += taxable_arr[i];
			}
			
			int tax = 0;
			
			for(int k = 0; k < slabs.size(); k++) {
				Slab s = slabs.get(k);
				if(adjusted > s.getUpper()) {
					tax += (s.getUpper()-s.getLower())*s.getPercent();
				}
				else if(adjusted > s.getLower()) {
					tax += (adjusted-s.getLower())*s.getPercent();
					break;
				}
			}
			
			tax = (int) Math.ceil(tax/(double)100);
			
			int adjust_tax = tax-past_tax;
			int tax_month = (int) Math.ceil(adjust_tax/(double)no_months);
			
			for(int i = 12-no_months; i < 12; i++) {
				tax_arr[i] = tax_month;
			}
			
			int rounded_tax = 0;
			for(int i = 0; i < 12; i++) {
				rounded_tax += tax_arr[i];
			}
			
			ps = cn.prepareStatement("UPDATE taxable SET april=?,may=?,june=?,july=?,august=?,september=?,october=?,november=?,december=?,january=?,february=?,march=?,annual=?,adjusted=? WHERE emp_id=?");
			ps.setInt(1,taxable_arr[0]);
			ps.setInt(2,taxable_arr[1]);
			ps.setInt(3,taxable_arr[2]);
			ps.setInt(4,taxable_arr[3]);
			ps.setInt(5,taxable_arr[4]);
			ps.setInt(6,taxable_arr[5]);
			ps.setInt(7,taxable_arr[6]);
			ps.setInt(8,taxable_arr[7]);
			ps.setInt(9,taxable_arr[8]);
			ps.setInt(10,taxable_arr[9]);
			ps.setInt(11,taxable_arr[10]);
			ps.setInt(12,taxable_arr[11]);
			ps.setInt(13,taxable);
			ps.setInt(14,adjusted);
			ps.setString(15,emp_id);
			ps.executeUpdate();
			
			ps = cn.prepareStatement("UPDATE tax SET april=?,may=?,june=?,july=?,august=?,september=?,october=?,november=?,december=?,january=?,february=?,march=?,annual=?,adjusted=? WHERE emp_id=?");
			ps.setInt(1,tax_arr[0]);
			ps.setInt(2,tax_arr[1]);
			ps.setInt(3,tax_arr[2]);
			ps.setInt(4,tax_arr[3]);
			ps.setInt(5,tax_arr[4]);
			ps.setInt(6,tax_arr[5]);
			ps.setInt(7,tax_arr[6]);
			ps.setInt(8,tax_arr[7]);
			ps.setInt(9,tax_arr[8]);
			ps.setInt(10,tax_arr[9]);
			ps.setInt(11,tax_arr[10]);
			ps.setInt(12,tax_arr[11]);
			ps.setInt(13,tax);
			ps.setInt(14,rounded_tax);
			ps.setString(15,emp_id);
			ps.executeUpdate();
		}
		rs.close();
		ps.close();
		cn.close();
	}
	
	public static void compute_all(HashMap<String,Integer> id_sal) throws ClassNotFoundException, SQLException {
		Class.forName("org.postgresql.Driver");
		Connection cn = connect();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		ArrayList<Integer> deducs = new ArrayList<Integer>();
		HashMap<Integer,Integer> fid_lim = new HashMap<Integer,Integer>();
		HashMap<Integer,Integer> sfid_fid = new HashMap<Integer,Integer>();
		ArrayList<Slab> slabs = new ArrayList<Slab>();
		
		ps = cn.prepareStatement("SELECT field_id,tax_limit FROM fields");
		rs = ps.executeQuery();
		while(rs.next()) {
			fid_lim.put(rs.getInt(1),rs.getInt(2));
		}
		
		ps = cn.prepareStatement("SELECT subfield_id,field_id FROM subfields");
		rs = ps.executeQuery();
		while(rs.next()) {
			sfid_fid.put(rs.getInt(1),rs.getInt(2));
		}
		
		ps = cn.prepareStatement("SELECT amount FROM deductions");
		rs = ps.executeQuery();
		while(rs.next()) {
			deducs.add(rs.getInt(1));
		}
		
		ps = cn.prepareStatement("SELECT * FROM tax_slabs");
		rs = ps.executeQuery();
		while(rs.next()) {
			Slab s = new Slab(rs.getInt(1),rs.getInt(2),rs.getInt(3));
			slabs.add(s);
		}
		
		for(String emp_id : id_sal.keySet()) {
			
			int gross_sal = id_sal.get(emp_id);
			
			ArrayList<Declaration> fdecs = new ArrayList<Declaration>();
			ArrayList<Declaration> sfdecs = new ArrayList<Declaration>();
						
			ps = cn.prepareStatement("SELECT * FROM declarations WHERE emp_id = ?");
			ps.setString(1,emp_id);
			rs = ps.executeQuery();
			while(rs.next()) {
				int id = 0;
				int fid = rs.getInt(3);
				int sfid = rs.getInt(7);
				id = (sfid == 0) ? fid : sfid;
				Declaration dec = new Declaration(rs.getString(2),id,"",rs.getInt(4),rs.getInt(5),rs.getString(6));
				if(sfid == 0) {
					fdecs.add(dec);
				}
				else {
					sfdecs.add(dec);
				}
			}
			
			int taxable = gross_sal;
			
			for(int i = 0; i < fdecs.size(); i++) {
				Declaration dec = fdecs.get(i);
				int id = dec.getId();
				int amount = (dec.getStatus().equals("pending")) ? dec.getAmount_declared() : dec.getAmount_proved();
				if(fid_lim.containsKey(id)) {
					int limit = fid_lim.get(id);
					taxable = (amount < limit) ? taxable - amount : taxable - limit;
				}			
			}
			
			HashSet<Integer> visited = new HashSet<Integer>();
			for(int j = 0; j < sfdecs.size(); j++) {
				Declaration dec = sfdecs.get(j);
				int sfid = dec.getId();
				int fid = sfid_fid.get(sfid);
				if(!visited.contains(fid) && fid_lim.containsKey(fid)) {
					visited.add(fid);
					int limit = fid_lim.get(fid);
					int val = 0;
					for(Declaration d : sfdecs) {
						if(sfid_fid.get(d.getId()).equals(fid)) {
							val = (d.getStatus().equals("pending")) ? val + d.getAmount_declared() : val + d.getAmount_proved();
						}
					}
					taxable = (val < limit) ? taxable - val : taxable - limit;
				}
			}
			
			for(int k = 0; k < deducs.size(); k++) {
				taxable -= deducs.get(k);
			}
			
			int month = Calendar.getInstance().get(Calendar.MONTH);
			int no_months = (month < 3) ? 3-month : 12-month+3;
			
			int taxable_arr[] = new int[12];
			int tax_arr[] = new int[12];
			
			int past_taxable = 0;
			int past_tax = 0;
			
			ps = cn.prepareStatement("SELECT * FROM taxable WHERE emp_id = ?");
			ps.setString(1,emp_id);
			if(rs.next()) {
				for(int i = 0; i < 12-no_months; i++) {
					taxable_arr[i] = rs.getInt(i+2);
					past_taxable += taxable_arr[i];
				}
			}
			
			ps = cn.prepareStatement("SELECT * FROM tax WHERE emp_id = ?");
			ps.setString(1,emp_id);
			if(rs.next()) {
				for(int i = 0; i < 12-no_months; i++) {
					tax_arr[i] = rs.getInt(i+2);
					past_tax += tax_arr[i];
				}
			}
			
			int adjust = taxable-past_taxable;
			
			int adjusted_month = (int) Math.ceil(adjust/(double)no_months);
			
			for(int i = 12-no_months; i < 12; i++) {
				taxable_arr[i] = adjusted_month;
			}
			
			int adjusted = 0;
			for(int i = 0; i < 12; i++) {
				adjusted += taxable_arr[i];
			}
			
			int tax = 0;
			
			for(int k = 0; k < slabs.size(); k++) {
				Slab s = slabs.get(k);
				if(adjusted > s.getUpper()) {
					tax += (s.getUpper()-s.getLower())*s.getPercent();
				}
				else if(adjusted > s.getLower()) {
					tax += (adjusted-s.getLower())*s.getPercent();
					break;
				}
			}
			
			tax = (int) Math.ceil(tax/(double)100);
			
			int adjust_tax = tax-past_tax;
			int tax_month = (int) Math.ceil(adjust_tax/(double)no_months);
			
			for(int i = 12-no_months; i < 12; i++) {
				tax_arr[i] = tax_month;
			}
			
			int rounded_tax = 0;
			for(int i = 0; i < 12; i++) {
				rounded_tax += tax_arr[i];
			}
			
			ps = cn.prepareStatement("UPDATE taxable SET april=?,may=?,june=?,july=?,august=?,september=?,october=?,november=?,december=?,january=?,february=?,march=?,annual=?,adjusted=? WHERE emp_id=?");
			ps.setInt(1,taxable_arr[0]);
			ps.setInt(2,taxable_arr[1]);
			ps.setInt(3,taxable_arr[2]);
			ps.setInt(4,taxable_arr[3]);
			ps.setInt(5,taxable_arr[4]);
			ps.setInt(6,taxable_arr[5]);
			ps.setInt(7,taxable_arr[6]);
			ps.setInt(8,taxable_arr[7]);
			ps.setInt(9,taxable_arr[8]);
			ps.setInt(10,taxable_arr[9]);
			ps.setInt(11,taxable_arr[10]);
			ps.setInt(12,taxable_arr[11]);
			ps.setInt(13,taxable);
			ps.setInt(14,adjusted);
			ps.setString(15,emp_id);
			ps.executeUpdate();
			
			ps = cn.prepareStatement("UPDATE tax SET april=?,may=?,june=?,july=?,august=?,september=?,october=?,november=?,december=?,january=?,february=?,march=?,annual=?,adjusted=? WHERE emp_id=?");
			ps.setInt(1,tax_arr[0]);
			ps.setInt(2,tax_arr[1]);
			ps.setInt(3,tax_arr[2]);
			ps.setInt(4,tax_arr[3]);
			ps.setInt(5,tax_arr[4]);
			ps.setInt(6,tax_arr[5]);
			ps.setInt(7,tax_arr[6]);
			ps.setInt(8,tax_arr[7]);
			ps.setInt(9,tax_arr[8]);
			ps.setInt(10,tax_arr[9]);
			ps.setInt(11,tax_arr[10]);
			ps.setInt(12,tax_arr[11]);
			ps.setInt(13,tax);
			ps.setInt(14,rounded_tax);
			ps.setString(15,emp_id);
			ps.executeUpdate();
		}
		rs.close();
		ps.close();
		cn.close();
	}
	
	public static ArrayList<Field> getFields() throws ClassNotFoundException, SQLException {
		ArrayList<Field> dbfields = new ArrayList<Field>();
		ArrayList<Subfield> dbsubfields = new ArrayList<Subfield>();
		ArrayList<Field> fields = new ArrayList<Field>();
		HashSet<Integer> visited = new HashSet<Integer>();
		
		Class.forName("org.postgresql.Driver");
		Connection cn = connect();
		PreparedStatement ps = null;
		ResultSet rs = null;

		ps = cn.prepareStatement("SELECT fields.field_id,field,tax_limit,sub_field,subfields.subfield_id,subfields.subfield FROM fields LEFT OUTER JOIN subfields ON fields.field_id = subfields.field_id;");
		rs = ps.executeQuery();
		
		while(rs.next()) {
			Field field = new Field(rs.getInt(1),rs.getString(2),rs.getInt(3),rs.getString(4),null);
			dbfields.add(field);
			if(rs.getString(4).equals("yes")) {
				Subfield subfield = new Subfield(rs.getInt(1),rs.getInt(5),rs.getString(6));
				dbsubfields.add(subfield);
			}
		}
		
		for(int i = 0; i < dbfields.size(); i++) {
			Field field = dbfields.get(i);
			if(field.getSub_field().equals("no")) {
				fields.add(field);
			}
			else if(field.getSub_field().equals("yes") && !visited.contains(field.getField_id())) {
				ArrayList<Subfield> subfields = new ArrayList<Subfield>();
				int field_id = field.getField_id();
				visited.add(field_id);
				for(int j = 0; j < dbsubfields.size(); j++) {
					Subfield subfield = dbsubfields.get(j);
					if(subfield.getField_id() == field_id) {
						subfields.add(subfield);
					}
				}
				field.setSubfields(subfields);
				fields.add(field);
			}
		}
		rs.close();
		ps.close();
		cn.close();
		return fields;
	}
	
	public static ArrayList<Deduction> getDeducs() throws ClassNotFoundException, SQLException {
		ArrayList<Deduction> deds = new ArrayList<Deduction>();
		Class.forName("org.postgresql.Driver");
		Connection cn = connect();
		PreparedStatement ps;
		ResultSet rs;
		ps = cn.prepareStatement("SELECT * FROM deductions");
		rs = ps.executeQuery();
		while(rs.next()) {
			Deduction d = new Deduction(rs.getInt(1),rs.getString(2),rs.getInt(3));
			deds.add(d);
		}
		rs.close();
		ps.close();
		cn.close();
		return deds;
	}
	
}
