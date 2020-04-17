<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="main.css">
	<script type="text/javascript" src="admin.js"></script>
	
	<style>
		#slabs {
			margin-left: auto;
			margin-right: auto;
		}
	</style>
	
	<script>
		function validateForm(){
		    var form = document.getElementById("slabForm"), inputs = form.getElementsByTagName("input"), input = null, flag = true;
		    for(var i = 0, len = inputs.length; i < len; i++) {
		        input = inputs[i];
		        if(!input.value) {
		            flag = false;
		            input.focus();
		            alert("Please fill all the inputs");
		            break;
		        }
		    }
		    return(flag);
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
	
		<h3>Update Tax Slabs</h3>
		
		<form id="slabForm" action="PushSlabs" method="POST" onsubmit="return validateForm();">
			<table id="slabs">
				<tr>
					<th>Sr No</th>
					<th>Lower Boundary</th>
					<th>Upper Boundary</th>
					<th>Percent</th>
				</tr>
				<c:forEach items="${slabs}" var="slab" varStatus="counter">
					<tr>
						<td>${counter.count}</td>
						<td><input type="number" name="lower" value="${slab.lower}" min="0" required></td>
						<td><input type="number" name="upper" value="${slab.upper}" min="0" required></td>
						<td><input type="number" name="percent" value="${slab.percent}" min="0" required></td>
					</tr>
				</c:forEach>
				<tr>
					<td>${slabs_cnt+1}</td>
					<td><input type="number" name="lower" value="0" min="0" required></td>
					<td><input type="number" name="upper" value="0" min="0" required></td>
					<td><input type="number" name="percent" value="0" min="0" required></td>
				</tr>
				<tr>
					<td>${slabs_cnt+2}</td>
					<td><input type="number" name="lower" value="0" min="0" required></td>
					<td><input type="number" name="upper" value="0" min="0" required></td>
					<td><input type="number" name="percent" value="0" min="0" required></td>
				</tr>
				<tr>
					<td colspan="4" style="text-align: center; padding: 40px"><input type="submit" value="Submit"></td>
				</tr>
			</table>
		</form>
		
	</div>

</body>
</html>