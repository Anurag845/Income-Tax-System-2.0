<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import ="class_package.Employee"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="main.css">
<style>
	#empform,#empbasics,#empval {
		margin-left: auto;
		margin-right: auto;
		margin-top: 40px;
		margin-bottom: 40px;
	}
	
	#empbasics * {
		border-collapse: collapse;
		border: 1px solid black;
	}
</style>

<script type="text/javascript" src="user.js"></script>
<script src="jquery.min.js"></script>
<link href="dist/css/select2.min.css" rel="stylesheet"/>
<script src="dist/js/select2.min.js"></script>
<script>
$(document).ready(function() {
    $('.sel_emp').select2();
});

$('.sel_emp').select2({
	placeholder: 'Select employee name'
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
		
		<h3>Employee Declaration Validation</h3>
		
		<form action="EmpDetailsVal" method="POST">
			<table id="empform">
				<tr>
					<td>Select Employee Name</td>
					<td>
						<select class="sel_emp" name="emp_id" required>
							<option value="">Select Employee Name...</option>
    						<c:forEach items="${employees}" var="employee">
        						<option value="${employee.emp_id}">${employee.emp_name}</option>
    						</c:forEach>
						</select>
					</td>
					<td>
						<input type="submit" value="Enter">
					</td>
				</tr>
			</table>
		</form>
		
		<c:if test="${gotDet}">
			<table id="empbasics">
				<tr>
					<th>Employee Id</th>
					<th>Employee Name</th>
					<th>Gross Salary</th>
				</tr>
				<tr>
					<td>${emp_id}</td>
					<td>${emp_name}</td>
					<td>${gross_sal}</td>
				</tr>
			</table>
			
			<c:if test="${absent}">
				<p>Declaration records for ${emp_id} do not exist.</p>
			</c:if>
			
			<c:if test="${fdecsCount+sdecsCount > 0}">
				<c:set var="count" value="1" scope="page"/>
				<form action="Validate" method="POST">
					<table id="empval">
						<tr>
							<th>Sr No</th>
							<th>Field/Subfield</th>
							<th>Amount Declared</th>
							<th>Amount Proved</th>
							<th>Upload Proof</th>
						</tr>
						<c:forEach items="${fdecs}" var="dec">
							<tr>
								<td>${count}</td>
								<td>${dec.field_name}</td>
								<td>${dec.amount_declared}</td>
								<c:if test="${dec.status eq 'pending'}">
									<td><input name="f${dec.id}" type="number" value="${dec.amount_proved}" min="0"></td>
								</c:if>
								<c:if test="${dec.status eq 'proved'}">
									<td>${dec.amount_proved}</td>
								</c:if>
							</tr>
							<c:set var="count" value="${count + 1}" scope="page"/>
						</c:forEach>
						<c:forEach items="${sdecs}" var="dec">
							<tr>
								<td>${count}</td>
								<td>${dec.field_name}</td>
								<td>${dec.amount_declared}</td>
								<c:if test="${dec.status eq 'pending'}">
									<td><input name="s${dec.id}" type="number" value="${dec.amount_proved}" min="0"></td>
								</c:if>
								<c:if test="${dec.status eq 'proved'}">
									<td>${dec.amount_proved}</td>
								</c:if>
								<c:set var="count" value="${count + 1}" scope="page"/>
							</tr>
						</c:forEach>
						<tr>
							<td><input name="emp_id" type="hidden" value="${emp_id}"></td>
						</tr>
						<tr>
							<td colspan="5" style="text-align:center;"><input type="submit" value="Submit"></td>
						</tr>
					</table>
				</form>			
			</c:if>

		</c:if>
	</div>
</body>
</html>