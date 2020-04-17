	function gotoHome() {
		window.location.href = "admin_menu.html";
	}
	
	function gotoLimits() {
		window.location.href = "GetLimits";
	}
	
	function gotoSlabs() {
		window.location.href = "GetSlabs";
	}
	
	function gotoAddField() {
		window.location.href = "GetYFields";
	}
	
	function gotoRemoveField() {
		window.location.href = "GetFields";
	}
	
	function gotoSalary() {
		window.location.href = "GetSalary";
	}
	
	function gotoAddEmployee() {
		window.location.href = "add_emp.html";
	}
	
	function gotoRemoveEmployee() {
		window.location.href = "GetEmployees";
	}
	
	function gotoReset() {
		window.location.href = "reset.html";
	}
	
	function logOut() {
		var logForm = document.createElement("form");
		logForm.method = "POST";
		logForm.action = "Logout";
		document.body.appendChild(logForm);
		logForm.submit();
		document.body.removeChild(logForm);
	}