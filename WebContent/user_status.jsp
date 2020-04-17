<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="main.css">
	
	<script type="text/javascript" src="user.js">
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
		<p>${status_message}</p>
	</div>

</body>
</html>