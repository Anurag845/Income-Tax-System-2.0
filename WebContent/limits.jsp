<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="main.css">
	
	<style>
		#fields,#deducs {
			margin-top: 25px;
			margin-bottom: 25px;
			margin-left: auto;
			margin-right: auto;
		}
		
		#deducs {
			margin-bottom: 150px;
		}
	</style>
	
	<script type="text/javascript" src="admin.js"></script>
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
	
		<h3>Update Exemption Limits</h3>
		
		<form action="PushLimits" method="POST">
			<table id="fields">
				<tr>
					<th>Sr No</th>
					<th>Fields</th>
					<th>Tax Limits</th>
				</tr>
				<c:forEach items="${fields}" var="field" varStatus="counter">
					<tr>
						<td>${counter.count}</td>
						<td>${field.field}</td>
						<td><input name="f${field.field_id}" type="number" value="${field.tax_limit}" min="0"></td>
					</tr>
				
					<c:if test="${field.sub_field eq 'yes'}">
						<c:forEach items="${field.subfields}" var="subfield">
							<tr>
								<td></td>
								<td>${subfield.subfield}</td>
								<td></td>
							</tr>
						</c:forEach>
					</c:if>
				</c:forEach>
			</table>
			
			<h3>Update Deductions</h3>
			
			<table id="deducs">
				<tr>
					<th>Sr No</th>
					<th>Deduction</th>
					<th>Amount</th>
				</tr>
				<c:forEach items="${deductions}" var="ded" varStatus="counter">
					<tr>
						<td>${counter.count}</td>
						<td>${ded.deduction}</td>
						<td><input name="d${ded.ded_id}" type="number" value="${ded.amount}" min="0"></td>
					</tr>
				</c:forEach>
				<tr>
					<td colspan="3" style="text-align: center;padding: 30px;"><input type="submit" value="Submit"></td>
				</tr>
			</table>
			
		</form>
		
	</div>

</body>
</html>