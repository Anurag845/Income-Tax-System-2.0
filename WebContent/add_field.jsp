<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="main.css">
	
	<style>
		#decide {
			margin-left: auto;
			margin-right: auto;
		}
		
		input[type="submit"] {
			margin-top: 35px;
			margin-bottom: 35px;
		}
		
		#new_field,#new_std,#new_subfield {
			text-align: center;
			display: none;
		}
		
		#field,#std {
			margin-left: auto;
			margin-right: auto;
		}
		
		fieldset {
			margin-top: 40px;
			margin-bottom: 40px;
		}
	</style>
	
	<script type="text/javascript" src="admin.js"></script>
	<script>
		function show_field() {
			var std = document.getElementById("new_std");
			std.style.display = "none";
			var subfield = document.getElementById("new_subfield");
			subfield.style.display = "none";
			var field = document.getElementById("new_field");
			field.style.display = "inline-block";
		}
		
		function show_subfield() {
			var std = document.getElementById("new_std");
			std.style.display = "none";
			var field = document.getElementById("new_field");
			field.style.display = "none";
			var subfield = document.getElementById("new_subfield");
			subfield.style.display = "inline-block";
		}

		function show_std() {
			var field = document.getElementById("new_field");
			field.style.display = "none";
			var subfield = document.getElementById("new_subfield");
			subfield.style.display = "none";
			var std = document.getElementById("new_std");
			std.style.display = "inline-block";
		}
		
		function show_num() {
			var table = document.getElementById("field");
			var len = table.rows.length;
			if(len == 4) {
				var num_row = table.insertRow();
				var num_label = num_row.insertCell();
				num_label.innerHTML = "<td><label for='sub_num'>Enter number of sub-fields</label></td>";
				var num_input = num_row.insertCell();
				num_input.innerHTML = "<td><input type='number' id='sub_num' name='sub_num' onkeydown='delete_sub();' onkeyup='insert_sub();'></td>";
			}
		}
		
		function hide_num() {
			var table = document.getElementById("field");
			var len = table.rows.length;
			for(var cnt=len-1;cnt>3;cnt--) {
				table.deleteRow(cnt);
			}
		}
		
		function insert_sub() {
			var no = document.getElementById("sub_num").value;
			for(var cnt=0;cnt<no;cnt++) {
				var table = document.getElementById("field");
				var name_row = table.insertRow();
				var name_label = name_row.insertCell();
				name_label.innerHTML = "<td><label for='sub_field_"+cnt+"'>Enter name of sub-field "+cnt+"</label></td>";
				var name_input = name_row.insertCell();
				name_input.innerHTML = "<td><input type='text' name='sub_field'></td>";
			}
		}
	
		function delete_sub() {
			var table = document.getElementById("field");
			var len = table.rows.length;
			for(var cnt=len-1;cnt>4;cnt--) {
				table.deleteRow(cnt);
			}
		}
		
		function validate() {
			if(document.getElementById("fid").checked) {
				var fname = document.getElementById("fname").value;
				var fdesc = document.getElementById("fdesc").value;
				var limit = document.getElementById("limit").value;
				if(fname.length == 0 || limit == "" || document.querySelector('input[name="sub_field_present"]:checked') == null) {
					alert("All fields are compulsory!");
					return false;
				}
				return true;
			}
			else if(document.getElementById("subfid").checked) {
				var grp_fname = document.getElementById("grp_fname").value;
				var sub_fname = document.getElementById("sub_fname").value;
				if(grp_fname == "" || sub_fname == "") {
					alert("All fields are compulsory!");
					return false;
				}
				return true;
			}
			else if(document.getElementById("ded").checked) {
				var dname = document.getElementById("dname").value;
				var ddesc = document.getElementById("ddesc").value;
				var dvalue = document.getElementById("dvalue").value;
				if(dname.length = 0 || dvalue == "") {
					alert("All fields are compulsory!");
					return false;
				}
				return true;
			}
		}
	</script>
</head>
<body>
	<div class='heading'>
		
		<h1>Income Tax System</h1>
		
		<div class="topnav">
			<a onclick="gotoHome();">Home</a>
  			<a onclick="gotoLimits();">Exemption Limits</a>
  			<a onclick="gotoSlabs();">Tax Slabs</a>
  			<a onclick="gotoSalary();">Update Salary</a>
  			<a onclick="gotoAddField();">Add Field</a>
  			<a onclick="gotoRemoveField();">Remove Field</a>
  			<a onclick="gotoAddEmployee();">Add Employee</a>
  			<a onclick="gotoRemoveEmployee();">Remove Employee</a>
  			<a onclick="gotoReset();">Reset</a>
  			<a onclick="logOut();" style="float:right;">Logout</a>
		</div>
				
	</div>
	
	<div class="contents">
	
		<h3>Add New Entry</h3>
			
		<form action="AddField" method="POST" onsubmit="return validate();" autocomplete="off">	
			<table id="decide">
				<tr>
					<td><input type="radio" id="fid" name="type" value="field" onclick="show_field();"></td>
					<td>Field</td>
					<td><input type="radio" id="subfid" name="type" value="subfield" onclick="show_subfield();"></td>
					<td>Sub-Field</td>
					<td><input type="radio" id="ded" name="type" value="deduc" onclick="show_std();"></td>
					<td>Deduction</td>
				</tr>
			</table>
			
			<div id="new_field">
				<fieldset>
					<legend>New Field</legend>
				<table id="field">
					<tr>
						<td><label for="dec_name">Enter declaration field name</label></td>
						<td><input id="fname" type="text" name="dec_name"></td>
					</tr>
					<tr>
						<td><label for="dec_desc">Enter field description</label></td>
						<td><textarea id="fdesc" rows="3" name="dec_desc"></textarea></td>
					</tr>
					<tr>
						<td><label for="dec_limit">Enter exemption limit</label></td>
						<td><input id="limit" type="number" name="dec_limit"></td>
					</tr>
					<tr>
						<td><label for="sub_field_present">Sub-fields present?</label></td>
						<td>
							<input type="radio" id="yes" name="sub_field_present" value="yes" onclick="show_num();">Yes
							<input type="radio" id="no" name="sub_field_present" value="no" onclick="hide_num();">No
						</td>
					</tr>			
				</table>
				<input type="submit" name="submit" value="Submit">
				</fieldset>
			</div>			
			
			<div id="new_subfield">
				<fieldset>
					<legend>New Sub-Field</legend>
				<c:if test="${fmap_size == 0}">
					<p>No field currently has subfields.</p>
				</c:if>
				<c:if test="${fmap_size > 0}">
					<table id="subfield">
						<tr>
							<td><label>Select declaration field</label></td>
							<td>
								<select id="grp_fname" name="field_name">
									<option value="">Select field name ...</option>
									<c:forEach items="${fmap}" var="element">
								        <option value="${element.key}">${element.value}</option>
								    </c:forEach>
								</select>
							</td>
						</tr>
						<tr>
							<td><label for="sf_name">Enter sub-field name</label></td>
							<td><input id="sub_fname" type="text" name="sf_name"></td>
						</tr>			
					</table>
					<input type="submit" name="submit" value="Submit">
				</c:if>
				</fieldset>
			</div>
			
			<div id="new_std">
				<fieldset>
					<legend>New Deduction</legend>
					<table id="std">
						<tr>
							<td><label for="ded_name">Enter deduction name</label></td>
							<td><input id="dname" type="text" name="ded_name"></td>
						</tr>
						<tr>
							<td><label for="ded_desc">Enter deduction description</label></td>
							<td><textarea id="ddesc" rows="3" name="ded_desc"></textarea></td>
						</tr>
						<tr>
							<td><label for="ded_value">Enter deduction value</label></td>
							<td><input id="dvalue" type="number" name="ded_value"></td>
						</tr>
					</table>
					<input type="submit" name="submit" value="Submit">
				</fieldset>
			</div>
		</form>
	</div>
</body>
</html>