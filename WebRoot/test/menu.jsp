<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    
<!DOCTYPE html>
<html>
  <head>
    <jsp:include page="/common/inc.jsp"></jsp:include>
    <script type="text/javascript">
    $(function(){
    	$('#menu').tree();
    	
    	$('#btn').bind('click',function(){
    		console.info($('#menu'));
    		$('#menu').tree('append',{
    			data:[{
    				text : 'Four',
    			}]
    		});
    	});
    });
    
    </script>
</head>
<body>
	<ul id="menu">
	
	</ul>
	<button id="btn" ></button>
</body>
</html>