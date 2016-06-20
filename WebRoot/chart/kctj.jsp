<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<script type="text/javascript">
var kctj_did;
var kctj_bmbh;
var kctj_chart;

$(function(){
	kctj_did = lnyw.tab_options().did;
	
	var types = [{
	    "id": 'column',
	    "text": "柱形图"
	},{
	    "id": 'line',
	    "text": "折线图"
	},];

	var fields = [{
	    "id": 'kcje',
	    "text": "库存金额"
	},
// 	{
// 	    "id": 'xscb',
// 	    "text": "销售成本"
// 	},
	];
	
	if(kctj_did >= '10'){
		$('.bm').css('display','inline');
		$('#jxc_kctj_dep').combobox({
			data: ywbms,
		    width:100,
		    valueField: 'id',
		    textField: 'depName',
		    panelHeight: 'auto',
		    onSelect: function(rec){
		    	kctj_bmbh = $(this).combobox('getValue');
		    }
		}).combobox('selectedIndex', 0);
		kctj_bmbh = $('#jxc_kctj_dep').combobox('getValue');
	}else{
		kctj_bmbh = kctj_did;
	}
	
	$('#jxc_kctj_tblx').combobox({
	    data: types,
	    width:100,
	    valueField: 'id',
	    textField: 'text',
	    panelHeight: 'auto',
	    onSelect: function(rec){
	    	kctj_options.chart.type = $(this).combobox('getValue');
	    	kctj_setColumnLabel();
	    	kctj_chart = new Highcharts.Chart(kctj_options);
	    }
	}).combobox('selectedIndex', 0);
	
	
	
	$('#jxc_kctj_tjlx').combobox({
	    data: fields,
	    width:100,
	    valueField: 'id',
	    textField: 'text',
	    panelHeight: 'auto',
	    onSelect: function(rec){
	    	kctj_options.title.text = $(this).combobox('getText') + '对比分析';
	    }
	}).combobox('selectedIndex', 0);
	
	$('#export').click(function() {
	    kctj_chart.exportChart();
	});
	
});

var kctj_column_dataLabels = {
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

var kctj_line_dataLabels = {
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

var kctj_options = {
    chart: {
        renderTo: 'kctj_container',
    },
    title:{
   		text: '库存金额分析',
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
	
function kctj_drawChart(data){
	kctj_options.chart.type = $('#jxc_kctj_tblx').combobox('getValue');
	kctj_options.xAxis.categories = data.categories;
	kctj_options.series = data.series;
	kctj_setColumnLabel();
	kctj_chart = new Highcharts.Chart(kctj_options);
};
	
function kctj_setColumnLabel(){
	for(var i = 0; i < kctj_options.series.length; i++){
		if($('#jxc_kctj_tblx').combobox('getValue') == 'column'){
			kctj_options.series[i].dataLabels = kctj_column_dataLabels;
		}else{
			kctj_options.series[i].dataLabels = kctj_line_dataLabels;
		}
	}
}

function kctj_getData(){
	lnyw.MaskUtil.mask('正在刷新，请等待……');
	$.ajax({
		url: '${pageContext.request.contextPath}/jxc/chartAction!getKctj.action',
		data: {
			bmbh: kctj_bmbh,
			field: $('#jxc_kctj_tjlx').combobox('getValue'),
		},
		cache: false,
		async: false,
		dataType: 'json',
		success: function(data){
			kctj_drawChart(data);
		},
		complete: function(){
			lnyw.MaskUtil.unmask();
		}
	});
}

</script>
<table width=100% style="margin:5px;"><tr>
<td align="left"><span class="bm" style="display:none">部门：<input id="jxc_kctj_dep" name="jxc_kctj_dep"></span>
&nbsp;&nbsp;&nbsp;&nbsp;统计类型：<input id="jxc_kctj_tjlx" name="jxc_kctj_tjlx">
&nbsp;&nbsp;&nbsp;&nbsp;图表类型：<input id="jxc_kctj_tblx" name="jxc_kctj_tblx">
&nbsp;&nbsp;&nbsp;&nbsp;<button id="refresh" onclick="kctj_getData()">刷新</button>
&nbsp;&nbsp;&nbsp;&nbsp;<button id="export">导出</button></td>
</tr></table>
<br>
<div id="kctj_container" style="min-width:800px;height:400px"></div>
<div style="margin:10px;">注：因系统切换、并行等原因，2014年1-4月的库存统计不十分准确。</div>

