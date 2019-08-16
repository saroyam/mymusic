<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/MyCustomTag.tld" prefix="mine" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="css/creative.css" type="text/css">
<title>My Music</title>
</head>
<body>
	<div class="mypanel">

		<br>
		

		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		
		<img src="images/logo.jpg"
			alt="My music Photo" width="70%" height="20%"> <br>
			
			 <mine:myTag msg="${requestScope.msg }" />
			
			<form method="POST" role="form" action="LoginRegister">
				<input type="hidden" name="action" value="login" />
				<input name="email" id="email" type="email" maxlength="50" placeholder="Email" required> <br>
				<input name="password" id="password" type="password" maxlength="25" placeholder="Password" required> <br>
				<input id="signInSubmit" name="signInSubmit" class="myBtn" type="submit"  value="Login" />

			</form>
			
		<br>
			<form method="POST"  action="LoginRegister">
				<input type="hidden" name="action" value="redirectRegister"/>
				<input  name="registerRedirect"  type="submit" class="myBtn" value="Not registered? Click here" />
			</form>
			
		<br>
	</div>
</body>
</html>