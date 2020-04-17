<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import ="class_package.Employee"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="main.css">
<style>
		#emp_form,#dec_form,#empbasics,#form_pref {
			margin-left: auto;
			margin-right: auto;
			margin-top: 40px;
			margin-bottom: 40px;
		}
		
		#dec_form {
			margin-top: 50px;
			margin-bottom: 150px;
		}
		
		#empbasics * {
			border-collapse: collapse;
			border: 1px solid black;
		}
		
		.decform td, .decform th {
			padding-left: 40px;
			padding-right: 40px;
		}
</style>
<script type="text/javascript" src="user.js"></script>
<script src="jquery.min.js"></script>
<link href="dist/css/select2.min.css" rel="stylesheet"/>
<script src="dist/js/select2.min.js"></script>
<script>

	function newAlert() {
		if(confirm("Are you sure to create a new declaration form? Previous entries for this employee will be deleted.")) {
			postPref("New");
		}
		else {
			
		}
	}
	
	function updateAlert() {
		if(confirm("Previous records will remain valid.")) {
			postPref("Update");
		}
		else {
			
		}
	}
	
	function deleteAlert() {
		if(confirm("Are you sure to delete all declaration records for this employee? Once deleted, cannot be restored.")) {
			postPref("Delete");
		}
		else {
			
		}
	}
	
	function postPref(prefValue) {
		var prefForm = document.createElement("form");
		prefForm.method = "POST";
		prefForm.action = "FormPref";
		var empid = document.createElement("input");
		empid.name = "emp_id";
		empid.value = '${emp_id}';
		prefForm.appendChild(empid);
		var prefinput = document.createElement("input");
		prefinput.name = "pref";
		prefinput.value = prefValue;
		prefForm.appendChild(prefinput);
		document.body.appendChild(prefForm);
		prefForm.submit();
		document.body.removeChild(prefForm);
	}
	
</script>
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
	
		<h3>
			Employee Declaration Form
		</h3>
		
		<form action="EmpDetails" method="POST">
			<table id="emp_form">
				<tr>
					<td>Select Employee Name</td>
					<td>
						<select class="sel_emp" name="emp_id" required>
							<option value="">Select employee name ...</option>
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
			
			
			<table id="form_pref">
				<tr>
					<td><button id="new_form" name="pref" type="submit" value="New" onclick="newAlert();">New</button></td>
					<td><button id="update_form" name="pref" type="submit" value="Update" onclick="updateAlert();">Update</button></td>
					<td><button id="delete_form" name="pref" type="submit" value="Delete" onclick="deleteAlert();">Delete</button></td>
				</tr>
			</table>
			
			<c:if test="${formPref}">
				<c:if test="${prefSet eq 'New'}">
					<form action="PushDecs" method="POST">
						<table id="dec_form" class="decform">
							<tr>
								<th>Sr No</th>
								<th>Field Name</th>
								<th>Amount</th>
							</tr>
							<c:forEach items="${fields}" var="field" varStatus="counter">
								<tr>
									<td>${counter.count}</td>
									<td>${field.field}</td>
									<c:if test="${field.sub_field eq 'no'}">
										<td><input type="number" name="f${field.field_id}" value="0" min="0"></td>
									</c:if>
								</tr>
								<c:if test="${field.sub_field eq 'yes'}">
									<c:forEach items="${field.subfields}" var="subfield">
										<tr>
											<td></td>
											<td>${subfield.subfield}</td>
											<td><input type="number" name="s${subfield.subfield_id}" value="0" min="0"></td>
										</tr>
									</c:forEach>
								</c:if>
							</c:forEach>
							<tr>
								<td><input type="hidden" name="type" value="New"></td>
								<td><input type="hidden" name="emp_id" value="${emp_id}"></td>
							</tr>
							<tr>
								<td colspan="3" style="text-align: center;padding: 30px;"><input type="submit" value="Submit"></td>
							</tr>
						</table>
					</form>
				</c:if>
				<c:if test="${absent}">
					<p>Declaration records for ${emp_id} do not exist.</p>
				</c:if>
				<c:if test="${prefSet eq 'Delete'}">
					<p>Records deleted successfully.</p>
				</c:if>
				<c:if test="${prefSet eq 'Update'}">
					<form action="PushDecs" method="POST" autocomplete="off">
						<table id="dec_form" class="decform">
							<tr>
								<th>Sr No</th>
								<th>Field Name</th>
								<th>Amount</th>
							</tr>
							<c:set var="cnt" scope="page" value="0"></c:set>
							<c:forEach items="${fields}" var="field" varStatus="counter">
								<tr>
									<td>${counter.count}</td>
									<td>${field.field}</td>
									<c:if test="${field.sub_field eq 'no'}">
										<c:if test="${all_status[cnt] eq 'pending'}">
											<td><input type="number" name="f${field.field_id}" value="${field.amount}" min="0"></td>
										</c:if>
										<c:if test="${all_status[cnt] eq 'proved'}">
											<td>${field.amount}</td>
										</c:if>
										<c:set var="cnt" scope="page" value="${cnt+1}"/>
									</c:if>
								</tr>
								<c:if test="${field.sub_field eq 'yes'}">
									<c:forEach items="${field.subfields}" var="subfield">
										<tr>
											<td></td>
											<td>${subfield.subfield}</td>
											<c:if test="${all_status[cnt] eq 'pending'}">
												<td><input type="number" name="s${subfield.subfield_id}" value="${subfield.amount}" min="0"></td>
											</c:if>
											<c:if test="${all_status[cnt] eq 'proved'}">
												<td>${subfield.amount}</td>
											</c:if>
											<c:set var="cnt" scope="page" value="${cnt+1}"/>
										</tr>
									</c:forEach>
								</c:if>
							</c:forEach>
							<tr>
								<td><input type="hidden" name="type" value="Update"></td>
								<td><input type="hidden" name="emp_id" value="${emp_id}"></td>
							</tr>
							<tr>
								<td colspan="3" style="text-align: center;padding: 30px;"><input type="submit" value="Submit"></td>
							</tr>
						</table>
					</form>		
				</c:if>
			</c:if>
		</c:if>
	</div>
	
</body>

</html>