<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="main.css">
	<script type="text/javascript" src="admin.js"></script>
	<script src="jquery.min.js"></script>
	<link rel="stylesheet" type="text/css" href="DataTables/datatables.min.css">
  	<script type="text/javascript" charset="utf8" src="DataTables/datatables.min.js"></script>
	<script>
		$(document).ready(function () {
		    $('#emps').DataTable({info: false});
		});
	</script>
	<style>
		table.dataTable {
		    width: auto;
		}
		
		.table_div {
			display: inline-block;
			margin: 20px;
		}
	</style>
	<script>
		function confirmation() {
			if(confirm("CAUTION! SELECTED EMPLOYEE'S RECORDS WILL BE DELETED PERMANENTLY!")) {
				if(confirm("ARE YOU SURE TO PROCEED?")) {
					return true;
				}
			}
			return false;
		}
	</script>
</head>
<body>
	<div class="heading">
		
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
	
		<h3>Remove Employee</h3>
		
		<div class="table_div">
			<form action="RemoveEmployee" method="POST" onsubmit="return confirmation();">
				<table id="emps">
					<thead>
						<tr>
							<th>Sr No</th>
							<th>Employee Id</th>
							<th>Employee Name</th>
							<th>Remove</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${employees}" var="employee" varStatus="counter">
							<tr>
								<td>${counter.count}</td>
								<td>${employee.emp_id}</td>
								<td>${employee.emp_name}</td>
								<td><input type="checkbox" name="${employee.emp_id}"></td>
							</tr>
						</c:forEach>
					</tbody>
					<tfoot>
						<tr>
							<td colspan="4" style="text-align: center; padding: 20px;"><input type="submit" value="Submit"></td>
						</tr>
					</tfoot>
				</table>
			</form>
		</div>
	</div>
</body>
</html>