<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<script type="text/javascript">
var xsjgfx_did;
var xsjgfx_bmbh;
var xsjgfx_chart;

$(function(){
	xsjgfx_did = lnyw.tab_options().did;
	
	var types = [{
	    "id": 'pie',
	    "text": "饼图"
	},];

	var fields = [{
	    "id": 'xsje',
	    "text": "销售金额(不含税)"
	},{
	    "id": 'xsml',
	    "text": "销售毛利"
	},];
	
	if(xsjgfx_did >= '10'){
		//$('.bm').css('display','table-cell');
		$('.bm').css('display','inline');
		$('#jxc_xsjgfx_dep').combobox({
			data: ywbms,
		    width:100,
		    valueField: 'id',
		    textField: 'depName',
		    panelHeight: 'auto',
		    onSelect: function(rec){
		    	xsjgfx_bmbh = $(this).combobox('getValue');
		    }
		}).combobox('selectedIndex', 0);
		xsjgfx_bmbh = $('#jxc_xsjgfx_dep').combobox('getValue');
	}else{
		xsjgfx_bmbh = xsjgfx_did;
	}
	
	$('#jxc_xsjgfx_tblx').combobox({
	    data: types,
	    width:100,
	    valueField: 'id',
	    textField: 'text',
	    panelHeight: 'auto',
	    onSelect: function(rec){
	    	xsjgfx_options.chart.type = $(this).combobox('getValue');
	    	xsjgfx_setColumnLabel();
	    	xsjgfx_chart = new Highcharts.Chart(xsjgfx_options);
	    }
	}).combobox('selectedIndex', 0);
	
	
	
	$('#jxc_xsjgfx_tjlx').combobox({
	    data: fields,
	    width:100,
	    valueField: 'id',
	    textField: 'text',
	    panelHeight: 'auto',
	    onSelect: function(){
	    	xsjgfx_options.title.text = $(this).combobox('getText') + '对比分析';
	    }
	}).combobox('selectedIndex', 0);
	
	$('#export').click(function() {
	    xsjgfx_chart.exportChart();
	});
	
});

var xsjgfx_column_dataLabels = {
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

var xsjgfx_line_dataLabels = {
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

var xsjgfx_options = {
    chart: {
        renderTo: 'xsjgfx_container',
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
	
function xsjgfx_drawChart(data){
	xsjgfx_options.chart.type = $('#jxc_xsjgfx_tblx').combobox('getValue');
	xsjgfx_options.xAxis.categories = data.categories;
	xsjgfx_options.series = data.series;
	xsjgfx_setColumnLabel();
	xsjgfx_chart = new Highcharts.Chart(xsjgfx_options);
};
	
function xsjgfx_setColumnLabel(){
	for(var i = 0; i < xsjgfx_options.series.length; i++){
		if($('#jxc_xsjgfx_tblx').combobox('getValue') == 'column'){
			xsjgfx_options.series[i].dataLabels = xsjgfx_column_dataLabels;
		}else{
			xsjgfx_options.series[i].dataLabels = xsjgfx_line_dataLabels;
		}
	}
}

function xsjgfx_getData(){
	lnyw.MaskUtil.mask('正在刷新，请等待……');
	$.ajax({
		url: '${pageContext.request.contextPath}/jxc/chartAction!getXsjgfx.action',
		data: {
			bmbh: xsjgfx_bmbh,
			field: $('#jxc_xsjgfx_tjlx').combobox('getValue'),
			includeNb: $('input#jxc_xsjgfx_nb').is(':checked') ? '1' : '0',
		},
		cache: false,
		async: false,
		dataType: 'json',
		success: function(data){
			xsjgfx_drawChart(data);
		},
		complete: function(){
			lnyw.MaskUtil.unmask();
		}
	});
}

</script>
<table width=100% style="margin:5px;"><tr>
<td align="left"><span class="bm" style="display:none">部门：<input id="jxc_xsjgfx_dep" name="jxc_xsjgfx_dep"></span>
&nbsp;&nbsp;&nbsp;&nbsp;统计类型：<input id="jxc_xsjgfx_tjlx" name="jxc_xsjgfx_tjlx">
&nbsp;&nbsp;&nbsp;&nbsp;图表类型：<input id="jxc_xsjgfx_tblx" name="jxc_xsjgfx_tblx">
&nbsp;&nbsp;&nbsp;&nbsp;包含内部<input type="checkbox" id="jxc_xsjgfx_nb" name="jxc_xsjgfx_nb">
&nbsp;&nbsp;&nbsp;&nbsp;<button id="refresh" onclick="xsjgfx_getData()">刷新</button>
&nbsp;&nbsp;&nbsp;&nbsp;<button id="export">导出</button></td>
</tr></table>
<br>
<div id="xsjgfx_container" style="min-width:800px;height:400px"></div>
<div style="margin:10px;">注：因系统切换、并行等原因，2014年1月的销售金额合并在2月，2014年1-4月的毛利统计不十分准确。</div>

