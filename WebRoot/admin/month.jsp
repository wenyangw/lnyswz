<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<script type="text/javascript">
$(function(){
	$('#admin_month_button').linkbutton({
		text: '月末结账',
		plain: true,
	    iconCls: 'icon-reload'
	}).bind('click', function(){
		$.messager.confirm('请确认', '是否要进行月末结账？(请先进行数据备份！！)', function(one) {
			if (one) {
				$.messager.confirm('再次确认', '是否要进行月末结账？结账后数据不能恢复，请再次确认！！！', function(fin) {
					if (fin) {
						$.ajax({
							url : '${pageContext.request.contextPath}/admin/monthAction!update.action',
							method: 'post',
							dataType : 'json',
							success : function(d) {
								$.messager.alert('信息', d.msg, 'info');
								// $.messager.show({
								// 	title : '提示',
								// 	msg : d.msg

								// });
							}
						});
					}
				});
			}
		});
    })
    $('a#admin_month_button').css({
    	'padding-left': '50px',
    	'padding-top': '50px',
    });

	
});

</script>
<a id="admin_month_button" href="#"></a>



	
