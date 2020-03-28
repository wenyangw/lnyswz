<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
	
<html>
<head>
	<meta charset="UTF-8">
	<title>信息管理移动平台</title>
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/jquery.mobile-1.4.5.min.css" />
<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.mobile-1.4.5.min.js"></script>
</head>
<body>
<div data-role="page" id="pageone">
	<div data-role="header">
		<h1>信息管理移动平台</h1>
	</div>

  	<div data-role="content">
  		<form method="post" action="${pageContext.request.contextPath}/login.action">
  			<div data-role="fieldcontain">
    			<label for="userName">用户名:</label>
				<input type="text" name="userName" id="userName" value="">
				<label for="password">密码:</label>
				<input type="password" name="password" id="password" value="" autocomplete="off">
		
				<input type="hidden" name="source" value="mobile">
				<input type="submit" data-inline="true" value="登录">
			</div>
			${msg}
		</form>
	</div>
	
  	<div data-role="footer">
    	<h1>@2015 辽宁印刷物资有限责任公司</h1>
  	</div>
</div> 
</body>
</html>