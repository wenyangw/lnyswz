<%@ page language="java" contentType="text/html; charset=utf-8"
		 pageEncoding="utf-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/plugins/jquery-easyui-1.3.3/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/plugins/jquery-easyui-1.3.3/themes/icon.css">

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/common.css">

<script type="text/javascript" src="${pageContext.request.contextPath}/plugins/jquery-2.0.0/jquery.min.js" charset="utf-8"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/plugins/jquery-easyui-1.3.3/jquery.easyui.min.js" charset="utf-8"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/plugins/jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/plugins/momentjs-2.15.1/moment.min.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/js/utils.js?v=20160831_7"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jxc.js?v=20161027"></script>

	<%
		String name = "" ;
		if(request.getParameter("name") != null){
			name = request.getParameter("name");
		}

		String bmbh = "" ;
		if(request.getParameter("bmbh") != null){
			bmbh = request.getParameter("bmbh");
		}
	%>

	<style>
 	.showTopRight{
		float: right;
	}

	.showTopLeft{
		float: left;
		font-size: 22px;
	}
	</style>

<script type="text/javascript">

var intervalId;
var field;
var width;
var changeTime = "";
var showName;

$(function(){
	getShowName();
});

function getShowName(){
	var name = $('input[name="name"]').val().split(",");
	var t = moment(new Date()).format('HH-mm-ss');
	
	for(var i = 0; i < name.length; i++){
		if((i + 1) < name.length){
			var timeStar = checkedShow(name[i]).time;
			var timeEnd = checkedShow(name[i+1]).time;
			
			if((t >= timeStar) && (t < timeEnd)){
					showName = name[i];
					changeTime = timeEnd;
					showData(name[i]);
					break;
			}else{	
				continue;
			}
		}else{
			showData(name[i]);
		}		
	}
}
function showData(name){
 	var show_dg;	
	var columns = [];
	var datas = checkedShow(name);
	$('#title').html(datas.title);

	var height = getHeight();
	for(var i = 0; i < datas.field.length; i++){
		var data = Object.create(Object.prototype);
        data["field"] = datas.field[i];
        data["title"] = datas.fieldTitle[i];
        data["width"] = window.screen.availWidth * datas.width[i];
        if(datas.align[i] != ''){
             data["align"] = datas.align[i]; 
       
		}  
        
        data["formatter"] = function(value, row, index){
			if(value){
                //改变表格中内容字体的大小
				 return '<span style = "font-size:' + fontHeaderHeight(height) + 'px; line-height:' + lineDataHeight(height) + 'px; ">' + value + '</span>';
			}          
        } ;
        data["styler"] = function(value, row){
		    return getStyler(row.statusId);
        } ;

		columns.push(data);
	}

	show_dg = $('#show_datagrid');
	show_dg.datagrid({
		url: '${pageContext.request.contextPath}' + datas.actionUrl,
	    fitColumns: true,
	    fit: true,
		singleSelect: true,
	    border: false,
	    pagination: true,
		pagePosition: 'bottom',
// 		nowrap:false,
		pageSize: 12,
		pageList: [12],
		queryParams: {
		    bmbh: $('input[name="bmbh"]').val()
        },
		rowStyler: function(index, record) {
            return 'height:' + height + 'px;';
        },
	    columns: [columns],
	    onLoadSuccess:function(data){   
	    	$('.datagrid-htable').css('text-align', 'center');//标题居中
	    }
	});
	
	setDatagridHeaderStyle(height);
	setTitleStyle(height);//标题设置
	setPageStyle(height);//分页栏设置		

	timeShow(show_dg, 30000, 'y');

	showTime();

	$("select#selectTime").click(function(){
		timeShow(show_dg, $(this).val());
	});
 	selectTime(height);//选择显示时间间隔
	//检测分辨率变化
	window.onresize = function(){
		setTimeout(domresize, 300);
	};

    function getStyler(statusId){
        switch(statusId){
            case '01':
                //return 'background-color:#ffeeff;';
                break;
            case '02':
                return 'background-color:#ffffff;';
                break;
            case '03':
                return 'background-color:#ff66cc;';
                //return 'background-color:#ffc000;';
                break;
            case '04':
                return 'background-color:#ff0000;color:white;';
                break;
            case '05':
                return 'background-color:#bf8f00;';
                break;
            case '06':
                return 'background-color:#ffff00;';
                break;
            case '31':
                return 'background-color:#ffff00;';
                //return 'background-color:#f4b084;';
                break;
            case '32':
                return 'background-color:#92D050;';
                break;
            case '33':
                return 'background-color:#00B0F0;color:white;';
                break;
            case '38':
                return 'background-color:#00B050;color:white;';
                break;
            case '39':
                return 'background-color:#2F75B5;color:white;';
                break;
        }
    }
}

function getHeight(){
	return Math.ceil(($(window).height()) / (12 + 3));
}

function fontPageHeight(height){
	return Math.ceil(height / 3);
}

function fontHeaderHeight(height){
	return Math.ceil(height * 0.48);
}

function headerHeight(height){
	return Math.ceil(height * 0.72);
}

function fontTitleHeight(height){
	return Math.ceil(height * 0.75);
}

function lineDataHeight(height){
	return Math.ceil(height * 0.72);
}

//改变表格宽高
function domresize(){
	var height = getHeight();	 ;

	//对数据行进行设置
	$('#show_datagrid').datagrid({
		rowStyler : function(index, record) {
			return 'height:' + height + 'px';
		},	          	   
	});
	 
	setTitleStyle(height);//标题设置
	setDatagridHeaderStyle(height);
	setPageStyle(height);//分页栏设置				
	selectTime(height);//选择显示时间间隔

}
//标题设置
function setTitleStyle(height){	
	/******************标题高度**************begin**************/
	$('.show_top').css('height', height + 'px');
	/******************标题高度**************end**************/

    $('.show_top').css('background-color', '#e0ecff');

	/******************标题字体**************begin**************/
	$('.showTopLeft').css('font-size', fontTitleHeight(height) + 'px');
	$('.showTopRight').css('font-size', fontPageHeight(height) + 'px');
	$('.showTopRight').css('line-height', height + 'px');
	/******************标题字体**************end**************/
}

//表头设置
function setDatagridHeaderStyle(height){
	/******************表头高度**************begin**************/
	$('.datagrid-header-row').css('height', height + 'px');
	$('.panel-body-noborder').css('padding-top', headerHeight(height) + 'px');
	$('.panel-body-noborder').css('margin-top', '0px');
	/******************表头高度**************end**************/
	
	/******************表头字体**************begin**************/
	$('.datagrid-cell span ').css('line-height', height + 'px');
	$('.datagrid-cell span ').css('font-size', fontHeaderHeight(height) + 'px');
	/******************表头字体**************end**************/  
}

//分页栏设置
function setPageStyle(height){
	/******************分页栏高度**************begin**************/    
	$('.datagrid-pager').css('height', height + 'px');
	$('.datagrid-pager table').css('line-height', height + 'px');
	$('.pagination-info').css('line-height', height + 'px');
	/******************分页栏高度**************end**************/    
	
	/******************分页字体**************begin**************/    
	$('.pagination-num').css('font-size', fontPageHeight(height) + 'px');
	$('.pagination-info').css('font-size', fontPageHeight(height) + 'px');
	$('.pagination-page-list').css('font-size', fontPageHeight(height) + 'px');
	$('.pagination span').css('font-size', fontPageHeight(height) + 'px');
	/******************分页字体**************end**************/  
}

//选择显示时间间隔
function selectTime(height){
	var selectTime =  new Array('&nbsp;&nbsp;&nbsp;&nbsp;<span style="font-size:' + fontPageHeight(height) + 'px;"	>刷新时间</span><select id="selectTime" style="font-size:' + fontPageHeight(height) + 'px;">');

	selectTime.push('<option value=1000> 1s </option>');
	selectTime.push('<option value=2000> 2s </option>');
	selectTime.push('<option value=5000> 5s </option>');
	selectTime.push('<option value=15000> 15s </option>');
	selectTime.push('<option value=20000> 20s </option>');
	selectTime.push('<option value=30000 selected = "selected"> 30s </option>');
	selectTime.push('<option value=50000> 50s </option>');
	
	selectTime.push('</select>');
	$(".pagination table>tbody>tr").append(selectTime.join(""));
	
	$("select#selectTime").click(function(){
		timeShow($('#show_datagrid'), $(this).val());
	});
}


function timeShow(dg, timeNum, init){
    clearInterval(intervalId) ;

	var i=0;
	if(init == "y"){
		i = 1;
	}
	
	 intervalId = setInterval(function() {
	    i++;
	    
		var max = Math.ceil((dg.datagrid('getData').total / dg.datagrid("getPager").data("pagination").options.pageSize));
			
		dg.datagrid('getPager').pagination('select', i);
		if(i == max){
			i = 0;
		}
		
    }, timeNum);
}

function showTime(){	
    setInterval(
	    "$('#dateShow').html(new Date().toLocaleString() + ' 星期'+'日一二三四五六'.charAt(new Date().getDay()));reloadJsp(new Date());",
     	1000);
}

function reloadJsp(time){

	var t = moment(time).format('HH-mm-ss');
	if(t == changeTime){
		var name = $('input[name="name"]').val().split(",");
		
		for(var i = 0; i < name.length; i++){
			if(name[i] == showName){
				if((i + 1) < name.length){
					++i;
				}				
				showName = name[i];
				showData(name[i]);
				if((i + 1) < name.length){
					changeTime = checkedShow(name[i + 1]).time;
				}else{
					changeTime='00';
				}

				break;				
			}
		}
	}
}

function checkedShow(name){
	var data = Object.create(Object.prototype);

	/**
    data["show"]={
        'title' : "标题",
        'field' : ['id', 'ckmc', 'dname' ],  //datagrid列属性
        'fieldTitle' : ['编号', '仓库名称', '所属部门'],  //datagrid列属性名称
        'align' : ['left', 'center', ''],  //datagrid每列属性对齐方式，''为默认
        'width' : [0.1, 0.5, 0.1],  //datagrid每列宽度
        'actionUrl' : '/jxc/ckAction!datagrid.action',//datagrid数据地址
        'time' : '13-10-07' //数据开始更换时间，时-分-秒。
    }
	 **/

    data["showXsth"]= {
        'title': "销售跟踪",
        'field': ['ywymc', 'khmc', 'xsthlsh', 'createTime', 'thfs', 'status', 'delayTime'],
        'fieldTitle': ['业务员', '客户', '流水号', '创建时间', '方式', '状态', '持续时间'],
        //'halign': ['center', 'center', 'center', 'center', 'center', 'center'],
        'align': ['', 'center', 'center', '', 'center', 'center', ''],
        'width': [0.1, 0.3, 0.15, 0.15, 0.05, 0.1, 0.10],
        'actionUrl': '/jxc/showAction!showXsth.action'
    }

	return data[name];
}
</script>
</head>
<body>
<div id="jxc_show" class="easyui-layout" data-options="fit:true">
	<div data-options="region:'north'" class="show_top"  >
		<div  >
			<div id="title" class="showTopLeft"></div>
			
			<div class="showTopRight">
				<span id="dateShow" ></span>			
			</div>	
		</div>                        
	</div>
	<div class="show_all" data-options="region:'center'">
		<div id="show_datagrid"></div>
	</div>
</div>

<input type="hidden" name="name" value=<%=name%>>
<input type="hidden" name="bmbh" value=<%=bmbh%>>

</body>
</html>