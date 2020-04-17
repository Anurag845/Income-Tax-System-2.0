<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="main.css">
	
	<script type="text/javascript" src="admin.js">
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
		<p>${status_message}</p>
	</div>

</body>
</html>