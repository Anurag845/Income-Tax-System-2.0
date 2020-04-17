<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import ="class_package.Taxable"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="main.css">
	<script src="jquery.min.js"></script>
	<link rel="stylesheet" type="text/css" href="DataTables/datatables.min.css">
  	<script type="text/javascript" charset="utf8" src="DataTables/datatables.min.js"></script>
	<style>
		table,td,th {
			border-collapse: collapse;
			border: 1px solid black;
		}
		
		table.dataTable {
		    width: auto;
		}
		
		.table_div {
			display: inline-block;
			margin-left: 40px;
			margin-right: 40px;
		}
	</style>
	<script type="text/javascript" src="user.js"></script>
	<script>
		$(document).ready(function () {
		    $('#taxable').DataTable();
		});
	</script>
</head>
<body>

	<div class="heading">
		
		<h1>Income Tax System</h1>
		
		<div class="topnav">
			<a onclick="gotoHome();">Home</a>
  			<a onclick="gotoForm();">Declaration Form</a>
  			<a onclick="gotoValidate();">Declaration Validation</a>
  			<a onclick="gotoTaxable();">Taxable Amount</a>
  			<a onclick="gotoTax();">Income Tax</a>
  			<a onclick="logOut();" style="float:right;">Logout</a>
		</div>
				
	</div>
	
	<div class="contents">
		
		<h3>Taxable Amount</h3>
		
		<div class="table_div">
			<c:if test="${taxable_cnt > 0}">
				<table id="taxable">
					<thead>
						<tr>
							<th>Employee Id</th>
							<th>Employee Name</th>
							<th>April</th>
							<th>May</th>
							<th>June</th>
							<th>July</th>
							<th>August</th>
							<th>September</th>
							<th>October</th>
							<th>November</th>
							<th>December</th>
							<th>January</th>
							<th>February</th>
							<th>March</th>
							<th>Annual</th>
							<th>Adjusted</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${taxables}" var="taxable">
							<tr>
								<td>${taxable.emp_id}</td>
								<td>${taxable.emp_name}</td>
								<td>${taxable.april}</td>
								<td>${taxable.may}</td>
								<td>${taxable.june}</td>
								<td>${taxable.july}</td>
								<td>${taxable.august}</td>
								<td>${taxable.september}</td>
								<td>${taxable.october}</td>
								<td>${taxable.november}</td>
								<td>${taxable.december}</td>
								<td>${taxable.january}</td>
								<td>${taxable.february}</td>
								<td>${taxable.march}</td>
								<td>${taxable.annual}</td>
								<td>${taxable.adjusted}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</c:if>
		</div>
	</div>
	
</body>
</html>