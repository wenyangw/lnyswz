<%@ page import="java.io.UnsupportedEncodingException" %>
<%@ page language="java" contentType="text/html; charset=utf-8"
		 pageEncoding="utf-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/plugins/jquery-easyui-1.3.3/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/plugins/jquery-easyui-1.3.3/themes/icon.css">

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/common.css">

<script type="text/javascript" src="${pageContext.request.contextPath}/plugins/jquery-2.2.3/jquery-2.2.3.min.js" charset="utf-8"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/plugins/jquery-easyui-1.3.3/jquery.easyui.min.js" charset="utf-8"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/plugins/jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/plugins/momentjs-2.15.1/moment.min.js"></script>

<%-- <script type="text/javascript" src="${pageContext.request.contextPath}/plugins/json/json.js"></script> --%>

<script type="text/javascript" src="${pageContext.request.contextPath}/js/utils.js?v=20160831_7"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jxc.js?v=20161027"></script>
	<%!
		public String convert(String param) throws UnsupportedEncodingException{
			return new String(param.getBytes("iso-8859-1"),"utf-8");
		}
	%>
	<%
		String actionUrl = "" ;
		String field = "" ;
		String fieldTitle = "" ;
		String title = "" ;
		String width = "" ;

		if(request.getParameter("actionUrl") != null){
			actionUrl = request.getParameter("actionUrl");
		}
		if(request.getParameter("field") != null){
			field = request.getParameter("field");
		}
		if(request.getParameter("fieldTitle") != null){
			fieldTitle = convert(request.getParameter("fieldTitle"));
		}
		if(request.getParameter("title")!=null){
			title = convert(request.getParameter("title"));
		}
		if(request.getParameter("width")!=null){
			width = request.getParameter("width");
		}
	%>

	<style>
	.show_all{
		font-size: 22px;
	}
	.showTopRight{
		float:right;

	}
	.showTopLeft{
		float:left;
		font-size: 22px;
		line-height: 40px;
	}



	</style>

<script type="text/javascript">
var intervalId;
$(function(){

	var show_dg;
	var columns=[];

	var actionUrl = $('input[name="actionUrl"]').val();
	var field = $('input[name="field"]').val().split(",");
	var fieldTitle = $('input[name="fieldTitle"]').val().split(",");
	var width = $('input[name="width"]').val().split(",");



	for(var i = 0; i < field.length; i++){
		var onlyTitle = Object.create(Object.prototype);
		onlyTitle["field"] = field[i];
		onlyTitle["title"] = fieldTitle[i];
		onlyTitle["width"] = window.screen.availWidth * width[i];
		columns.push(onlyTitle);
	}

	show_dg = $('#show_datagrid');
	show_dg.datagrid({
		url:'${pageContext.request.contextPath}' + actionUrl,
	    fitColumns:true,
	    fit : true,
		singleSelect:true,
	    border : false,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
	    columns: [columns],
	});

	console.info($('div[class^=datagrid-cell-]'));
	$('.datagrid-cell').css('font-size', '22px');
//	show_dg.datagrid("getPager").pagination({
//
//		onChangePageSize : function(){
//
//			var size = show_dg.datagrid("getPager" ).data("pagination").options.pageSize;
//			var height = Math.ceil( ( window.screen.availHeight * 0.80 ) / size );
//
			show_dg.datagrid( {
				rowStyler : function(index, record) {
                    $('.datagrid-header-row').css('height', '40px');
                    $('.datagrid-pager').css('height', '40px');
                    $('.datagrid-pager table').css('line-height', '40px');
                    $('.pagination-info').css('line-height', '40px');

			        return 'height:40px;';
				}
			});
//		},
//	});
	
	
	timerShow(show_dg, 5000, "y");

	showTime();

	$("select#selectTime").click(function(){
		timerShow(show_dg, $(this).val());
	});
});

function timerShow(dg, timeNum, init){
	
    clearInterval(intervalId) ;

	var i=0;
	if(init == "y"){
		i = 1;
	}
	
	 intervalId = setInterval(function() {
	    i++;
	    
		var max = Math.ceil( dg.datagrid('getData').total / dg.datagrid("getPager" ).data("pagination" ).options.pageSize ); 
		
		if(i > max){
			i = 0;
		}else{
			dg.datagrid('getPager').pagination('select', i);
		}
		
    }, timeNum);
}

function showTime(){	
    setInterval(
	    "$('#dateShow').html(new Date().toLocaleString() + ' 星期'+'日一二三四五六'.charAt(new Date().getDay()));",
     	1000);
}
</script>
</head>
<body>
<div id="jxc_show" class="easyui-layout" data-options="fit:true">
	<div data-options="region:'north'" style="width:100%;height:50px;">
		<div >
			<span id="title" class="showTopLeft"><%=title%></span>
			<span id="dateShow" class="showTopRight"></span>
			<br>
			<div  class="showTopRight">刷新时间
				<select id="selectTime">
					<option value=1000>1s</option>
					<option value=2000>2s</option>
					<option value=5000 selected = "selected">5s</option>
					<option value=15000>15s</option>
					<option value=20000>20s</option>
					<option value=30000>30s</option>
					<option value=50000>50s</option>
				</select>
			</div>
		</div>
	</div>
	<div class="show_all" data-options="region:'center'">
		<div id="show_datagrid"></div>
	</div>
</div>

<input type="hidden" id='actionUrl' name="actionUrl" value=<%=actionUrl%>>
<input type="hidden" id='field' name="field" value=<%=field%>>
<input type="hidden" id='fieldTitle' name="fieldTitle" value=<%=fieldTitle%>>
<input type="hidden" id='width' name="width" value=<%=width%>>

</body>
</html>