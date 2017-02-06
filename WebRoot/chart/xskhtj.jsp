<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<script type="text/javascript">
var xskhtj_did;
var xskhtj_bmbh;
var xskhtj_chart;
var jxc_xskhtj_ywyCombo;


$(function(){
	xskhtj_did = lnyw.tab_options().did;
	
	var types = [{
	    "id": 'column',
	    "text": "柱形图"
	},{
	    "id": 'line',
	    "text": "折线图"
	},];

	var fields = [{
	    "id": 'xsje',
	    "text": "销售金额(不含税)"
	},{
	    "id": 'xsml',
	    "text": "销售毛利"
	},];
	
	if(xskhtj_did >= '10'){
		//$('.bm').css('display','table-cell');
		$('.bm').css('display','inline');
		$('#jxc_xskhtj_dep').combobox({
			data: ywbms,
		    width:100,
		    valueField: 'id',
		    textField: 'depName',
		    panelHeight: 'auto',
		    onSelect: function(rec){
		    	xskhtj_bmbh = $(this).combobox('getValue');
		    	//jxc_xskhtj_ywyCombo = lnyw.initCombo($("#jxc_xskhtj_ywy"), 'id', 'realName', '${pageContext.request.contextPath}/admin/userAction!listYwys.action?did=' + xskhtj_bmbh);
		    }
		}).combobox('selectedIndex', 0);
		xskhtj_bmbh = $('#jxc_xskhtj_dep').combobox('getValue');
	}else{
		xskhtj_bmbh = xskhtj_did;
	}
	
	jxc_xskhtj_ywyCombo = lnyw.initCombo($("#jxc_xskhtj_ywy"), 'id', 'realName', '${pageContext.request.contextPath}/admin/userAction!listYwys.action?did=' + xskhtj_bmbh);
	
	$('input[name=jxc_xskhtj_nb]').prop('checked', true);
	jxc_xskhtj_ywyCombo.combobox('disable');
	
	$('input[name=jxc_xskhtj_nb]').click(function(){
		if($(this).is(':checked')){
			jxc_xskhtj_ywyCombo.combobox('disable');
		}else{
			jxc_xskhtj_ywyCombo.combobox('enable');
			//初始化业务员列表
// 			if(jxc_xskhtj_ywyCombo == undefined){
// 				jxc_xskhtj_ywyCombo = lnyw.initCombo($("#jxc_xskhtj_ywy"), 'id', 'realName', '${pageContext.request.contextPath}/admin/userAction!listYwys.action?did=' + xskhtj_bmbh);
// 			}
			jxc_xskhtj_ywyCombo.combobox('selectedIndex', 0);
		}
	});
	
	$('#jxc_xskhtj_tblx').combobox({
	    data: types,
	    width:100,
	    valueField: 'id',
	    textField: 'text',
	    panelHeight: 'auto',
	    onSelect: function(rec){
	    	xskhtj_options.chart.type = $(this).combobox('getValue');
	    	xskhtj_setColumnLabel();
	    	xskhtj_chart = new Highcharts.Chart(xskhtj_options);
	    }
	}).combobox('selectedIndex', 0);
	
	
	
	$('#jxc_xskhtj_tjlx').combobox({
	    data: fields,
	    width:100,
	    valueField: 'id',
	    textField: 'text',
	    panelHeight: 'auto',
	    onSelect: function(){
	    	xskhtj_options.title.text = $(this).combobox('getText') + '对比分析';
	    }
	}).combobox('selectedIndex', 0);
	
	$('#export').click(function() {
	    xskhtj_chart.exportChart();
	});
	
});

var xskhtj_column_dataLabels = {
    enabled: true,
    rotation: -90,
//     color: '#FFFFFF',
//     align: 'right',
     x: 4,
     y: -30,
//     style: {
//         fontSize: '13px',
//         fontFamily: 'Verdana, sans-serif',
//         textShadow: '0 0 3px black'
//     }
};

var xskhtj_line_dataLabels = {
        enabled: true,
        //rotation: -90,
//         color: '#FFFFFF',
//         align: 'right',
         x: 4,
         y: 0,
//         style: {
//             fontSize: '13px',
//             fontFamily: 'Verdana, sans-serif',
//             textShadow: '0 0 3px black'
//         }
    };

var xskhtj_options = {
    chart: {
        renderTo: 'xskhtj_container',
    },
    title:{
   		text: '销售金额(不含税)分析',
   		style: {
   			fontSize: '26px',
   		}
   	},
	xAxis: {
       	categories: []
   	},
       yAxis: {
       	labels: {
               formatter: function() {
                   return this.value ;
               }
           },
           title: {
               text: '金额(万元)',                  //指定y轴的标题
           },
       },
//         plotOptions: {
//             line: {
//                 dataLabels: {
//                     enabled: true
//                 },
//                 //enableMouseTracking: false
//             }
//         },
    series: [],
    credits:{
    	enabled: false
    },
    exporting:{
    	enabled: false
    }
};
	
function xskhtj_drawChart(data){
	xskhtj_options.chart.type = $('#jxc_xskhtj_tblx').combobox('getValue');
	xskhtj_options.xAxis.categories = data.categories;
	xskhtj_options.series = data.series;
	xskhtj_setColumnLabel();
	xskhtj_chart = new Highcharts.Chart(xskhtj_options);
};
	
function xskhtj_setColumnLabel(){
	for(var i = 0; i < xskhtj_options.series.length; i++){
		if($('#jxc_xskhtj_tblx').combobox('getValue') == 'column'){
			xskhtj_options.series[i].dataLabels = xskhtj_column_dataLabels;
		}else{
			xskhtj_options.series[i].dataLabels = xskhtj_line_dataLabels;
		}
	}
}

function xskhtj_getData(){
	lnyw.MaskUtil.mask('正在刷新，请等待……');
	$.ajax({
		url: '${pageContext.request.contextPath}/jxc/chartAction!getXskhtj.action',
		data: {
			bmbh: xskhtj_bmbh,
			ywyId: jxc_xskhtj_ywyCombo.combobox('getValue'),
			field: $('#jxc_xskhtj_tjlx').combobox('getValue'),
			includeNb: $('input#jxc_xskhtj_nb').is(':checked') ? '1' : '0',
		},
		cache: false,
		async: false,
		dataType: 'json',
		success: function(data){
			xskhtj_drawChart(data);
		},
		complete: function(){
			lnyw.MaskUtil.unmask();
		}
	});
}

</script>
<table width=100% style="margin:5px;"><tr>
<td align="left"><span class="bm" style="display:none">部门：<input id="jxc_xskhtj_dep" name="jxc_xskhtj_dep"></span>
&nbsp;&nbsp;&nbsp;&nbsp;全部<input type="checkbox" id="jxc_xskhtj_nb" name="jxc_xskhtj_nb">
业务员：<input id="jxc_xskhtj_ywy" name="jxc_xskhtj_ywy">
&nbsp;&nbsp;&nbsp;&nbsp;统计类型：<input id="jxc_xskhtj_tjlx" name="jxc_xskhtj_tjlx">
&nbsp;&nbsp;&nbsp;&nbsp;图表类型：<input id="jxc_xskhtj_tblx" name="jxc_xskhtj_tblx">
&nbsp;&nbsp;&nbsp;&nbsp;<button id="refresh" onclick="xskhtj_getData()">刷新</button>
&nbsp;&nbsp;&nbsp;&nbsp;<button id="export">导出</button></td>
</tr></table>
<br>
<div id="xskhtj_container" style="min-width:800px;height:400px"></div>
<!-- <div style="margin:10px;">注：因系统切换、并行等原因，2014年1月的销售金额合并在2月，2014年1-4月的毛利统计不十分准确。</div> -->

