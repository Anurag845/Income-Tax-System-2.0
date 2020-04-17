<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="main.css">
	<script type="text/javascript" src="admin.js"></script>
	<style>
		table.dataTable {
		    width: auto;
		}
		
		.table_div {
			display: inline-block;
			margin: 20px;
		}
	</style>
	<script src="jquery.min.js"></script>
	<link rel="stylesheet" type="text/css" href="DataTables/datatables.min.css">
  	<script type="text/javascript" charset="utf8" src="DataTables/datatables.min.js"></script>
	<script>
		$(document).ready(function () {
		    $('#emp_sal').DataTable({info: false});
		});
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
		
		<h3>Update Employee Salary</h3>
		
		<div class="table_div">		
			<c:if test="${emp_cnt > 0}">
				<form action="PushSalary" method="POST">
					<table id="emp_sal">
						<thead>
							<tr>
								<th>Sr No</th>
								<th>Employee Id</th>
								<th>Employee Name</th>
								<th>Gross Salary</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${employees}" var="emp" varStatus="counter">
								<tr>
									<td>${counter.count}</td>
									<td>${emp.emp_id}</td>
									<td>${emp.emp_name}</td>
									<td><input name="${emp.emp_id}" type="number" value="${emp.gross_sal}" min="0"></td>
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
			</c:if>
		</div>
	</div>
</body>
</html>