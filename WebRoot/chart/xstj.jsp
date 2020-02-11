<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<script type="text/javascript">
var xstj_did;
var xstj_bmbh;
var xstj_chart;

$(function(){
	xstj_did = lnyw.tab_options().did;
	
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
	},{
	    "id": 'xssl',
	    "text": "销售数量"
	},];
	
	if(xstj_did >= '10'){
		//$('.bm').css('display','table-cell');
		$('.bm').css('display','inline');
		$('#jxc_xstj_dep').combobox({
			data: ywbms,
		    width:100,
		    valueField: 'id',
		    textField: 'depName',
		    panelHeight: 'auto',
		    onSelect: function(rec){
		    	xstj_bmbh = $(this).combobox('getValue');
		    }
		}).combobox('selectedIndex', 0);
		xstj_bmbh = $('#jxc_xstj_dep').combobox('getValue');
	}else{
		xstj_bmbh = xstj_did;
	}
	
	$('#jxc_xstj_tblx').combobox({
	    data: types,
	    width:100,
	    valueField: 'id',
	    textField: 'text',
	    panelHeight: 'auto',
	    onSelect: function(rec){
	    	xstj_options.chart.type = $(this).combobox('getValue');
	    	xstj_setColumnLabel();
	    	xstj_chart = new Highcharts.Chart(xstj_options);
	    }
	}).combobox('selectedIndex', 0);
	
	
	
	$('#jxc_xstj_tjlx').combobox({
	    data: fields,
	    width:100,
	    valueField: 'id',
	    textField: 'text',
	    panelHeight: 'auto',
	    onSelect: function(rec){
	    	xstj_options.title.text = $(this).combobox('getText') + '对比分析';
	    	if(rec.id == 'xssl'){
	    		xstj_options.yAxis.title.text = '数量(吨)';
	    	}else{
	    		xstj_options.yAxis.title.text = '金额(万元)';
	    	}
	    }
	}).combobox('selectedIndex', 0);
	
	$('#export').click(function() {
	    xstj_chart.exportChart();
	});
	
});

var xstj_column_dataLabels = {
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

var xstj_line_dataLabels = {
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

var xstj_options = {
    chart: {
        renderTo: 'xstj_container',
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
	
function xstj_drawChart(data){
	xstj_options.chart.type = $('#jxc_xstj_tblx').combobox('getValue');
	xstj_options.xAxis.categories = data.categories;
	xstj_options.series = data.series;
	xstj_setColumnLabel();
	xstj_chart = new Highcharts.Chart(xstj_options);
};
	
function xstj_setColumnLabel(){
	for(var i = 0; i < xstj_options.series.length; i++){
		if($('#jxc_xstj_tblx').combobox('getValue') == 'column'){
			xstj_options.series[i].dataLabels = xstj_column_dataLabels;
		}else{
			xstj_options.series[i].dataLabels = xstj_line_dataLabels;
		}
	}
}

function xstj_getData(){
	if($('#jxc_xstj_tjlx').combobox('getValue') === 'xssl' && (xstj_bmbh == '01')){
		$.messager.alert('警告', '选择的部门无法进行销售数量的统计，请重新选择！',  'info');
	}else{
		lnyw.MaskUtil.mask('正在刷新，请等待……');
		$.ajax({
			url: '${pageContext.request.contextPath}/jxc/chartAction!getXstj.action',
			data: {
				bmbh: xstj_bmbh,
				field: $('#jxc_xstj_tjlx').combobox('getValue'),
				includeNb: $('input#jxc_xstj_nb').is(':checked') ? '1' : '0',
			},
			cache: false,
			async: false,
			dataType: 'json',
			success: function(data){
				xstj_drawChart(data);
			},
			complete: function(){
				lnyw.MaskUtil.unmask();
			}
		});
	}
}

</script>
<table width=100% style="margin:5px;"><tr>
<td align="left"><span class="bm" style="display:none">部门：<input id="jxc_xstj_dep" name="jxc_xstj_dep"></span>
&nbsp;&nbsp;&nbsp;&nbsp;统计类型：<input id="jxc_xstj_tjlx" name="jxc_xstj_tjlx">
&nbsp;&nbsp;&nbsp;&nbsp;图表类型：<input id="jxc_xstj_tblx" name="jxc_xstj_tblx">
&nbsp;&nbsp;&nbsp;&nbsp;包含内部<input type="checkbox" id="jxc_xstj_nb" name="jxc_xstj_nb">
&nbsp;&nbsp;&nbsp;&nbsp;<button id="refresh" onclick="xstj_getData()">刷新</button>
&nbsp;&nbsp;&nbsp;&nbsp;<button id="export">导出</button></td>
</tr></table>
<br>
<div id="xstj_container" style="min-width:800px;height:400px"></div>
<div style="margin:10px;">注：因商品计量单位不一致的因素，销售数量的统计仅包括教材、文达纸业、大连公公司的纸张。</div>

