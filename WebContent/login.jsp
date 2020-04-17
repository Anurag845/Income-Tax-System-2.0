<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>

	<link rel="stylesheet" type="text/css" href="login.css">

	<body>

		<h2 id = "title">
			Income Tax System
		</h2>

		<div class = "login-form">
			<form action = "Login" method = "POST" onsubmit="return validate();">
				<fieldset>
					<legend>Login</legend>
					<br>
					Username: <br>
					<div class="usr_div">
						<input name = "username" type = "text" id="log_username">
						<button type="button" id="question">
    						<img title="Username is the id given during joining. Example C2K..." class="img_usr" src="images/question.png" alt="?" />
						</button>
					</div>
					<br>
					Password: <br>
					<div class="pass_div">
						<input name = "password" type = "password" id="log_password">
						<button type="button" id="eye">
    						<img id="show_hide_pass" class="img_pass" src="images/show_password.png" alt="eye" />
						</button>
					</div>
					<br>
					<span><%=request.getAttribute("message")%></span>
					<br>
					<br>
					<div class="sub_btn">
						<input id="login_btn" type="submit" value="Login">
					</div>
					<br>
				</fieldset>
			</form>
		</div>
		
		<div class = "text-logo">

			<img src = "images/pict.jpeg" alt = "PICT">

		</div>

	</body>

	<script>

		function validate() {
			var usrname = document.getElementById('log_username').value;
			var passwd = document.getElementById('log_password').value;
			if(usrname == "" || passwd == "") {
				alert("Username and Password fields are mandatory");
				return false;			
			}
			else if(passwd.length < 8) {
				alert("Password should be minimum 8 characters");
				return false;			
			}
			else {
				return true;
			}
		}
		
		function showPassword() {
			var password = document.getElementById('log_password').value;
		}

		function show() {
    		var p = document.getElementById('log_password');
    		p.setAttribute('type', 'text');
		}

		function hide() {
		    var p = document.getElementById('log_password');
		    p.setAttribute('type', 'password');
		}
		
		function gotoForgot() {
			window.location.href = "forgot.html";
		}

		var pwShown = 0;

		document.getElementById("eye").addEventListener("click", function () {
    		if (pwShown == 0) {
        		pwShown = 1;
				document.getElementById("show_hide_pass").src = "images/hide_password.png";
        		show();
    		} else {
        		pwShown = 0;
				document.getElementById("show_hide_pass").src = "images/show_password.png";
        		hide();
    		}
		}, false);

	</script>

</html>
