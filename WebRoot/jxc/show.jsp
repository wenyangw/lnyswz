<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<html>
<script type="text/javascript">
	var jxc_show_did = lnyw.tab_options().did;
	function openPage(name){
		window.open('jxc/showDetail.jsp?name=' + name + "&bmbh=" + jxc_show_did,
			'',
			'width=' + (window.screen.availWidth - 10) + ',height=' + (window.screen.availHeight - 60) + ',fullscreen=yes,location=no'
		);
	}

</script>
<p>
	<a href="#" onclick="openPage('showXsth')">提货单跟踪</a>
</p>
</html>