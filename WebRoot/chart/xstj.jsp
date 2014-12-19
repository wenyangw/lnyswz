<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<script type="text/javascript">
var xstj_did;
var bmbh;
var chart;

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
	    "text": "销售金额"
	},{
	    "id": 'xsml',
	    "text": "销售毛利"
	},];
	
	var column_dataLabels = {
        enabled: true,
        rotation: -90,
//         color: '#FFFFFF',
//         align: 'right',
         x: 4,
         y: -30,
//         style: {
//             fontSize: '13px',
//             fontFamily: 'Verdana, sans-serif',
//             textShadow: '0 0 3px black'
//         }
    };
	
	var line_dataLabels = {
	        enabled: true,
	        //rotation: -90,
//	         color: '#FFFFFF',
//	         align: 'right',
	         x: 4,
	         y: 0,
//	         style: {
//	             fontSize: '13px',
//	             fontFamily: 'Verdana, sans-serif',
//	             textShadow: '0 0 3px black'
//	         }
	    };
	
	var options = {
	    chart: {
	        renderTo: 'container',
	    },
	    title:{
    		text: '销售分析',
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

	if(xstj_did >= '10'){
		$('.bm').css('display','table-cell');
		$('#jxc_xstj_dep').combobox({
			data: ywbms,
		    width:100,
		    valueField: 'id',
		    textField: 'depName',
		    panelHeight: 'auto',
		    onSelect: function(rec){
		    	bmbh = $(this).combobox('getValue');
		    	getData();
		    }
		}).combobox('selectedIndex', 0);
		bmbh = $('#jxc_xstj_dep').combobox('getValue');
	}else{
		bmbh = xstj_did;
	}
	
	$('#jxc_xstj_tblx').combobox({
	    data: types,
	    width:100,
	    valueField: 'id',
	    textField: 'text',
	    panelHeight: 'auto',
	    onSelect: function(rec){
	    	options.chart.type = $(this).combobox('getValue');
	    	setColumnLabel();
	    	chart = new Highcharts.Chart(options);
	    }
	}).combobox('selectedIndex', 0);
	
	$('#jxc_xstj_tjlx').combobox({
	    data: fields,
	    width:100,
	    valueField: 'id',
	    textField: 'text',
	    panelHeight: 'auto',
	    onSelect: function(rec){
	    	getData();
	    }
	}).combobox('selectedIndex', 0);

	getData();
	
	$('#export').click(function() {
	    chart.exportChart();
	});
	
	function getData(){
		$.ajax({
			url: '${pageContext.request.contextPath}/jxc/chartAction!getXstj.action',
			data: {
				bmbh: bmbh,
				field: $('#jxc_xstj_tjlx').combobox('getValue')
			},
			cache: false,
			async: false,
			dataType: 'json',
			success: function(data){
				drawChart(data);
			}
		});
	}
	
	function drawChart(data){
		options.chart.type = $('#jxc_xstj_tblx').combobox('getValue');
		options.xAxis.categories = data.categories;
		options.series = data.series;
		setColumnLabel();
		chart = new Highcharts.Chart(options);
	};
	
	function setColumnLabel(){
    		for(var i = 0; i < options.series.length; i++){
				if($('#jxc_xstj_tblx').combobox('getValue') == 'column'){
					options.series[i].dataLabels = column_dataLabels;
				}else{
					options.series[i].dataLabels = line_dataLabels;
				}
    		}
	}
	
});

</script>
<table width=100% style="margin:5px;"><tr>
<td class="bm" style="display:none">部门：<input id="jxc_xstj_dep" name="jxc_xstj_dep"></td>
<td>统计类型：<input id="jxc_xstj_tjlx" name="jxc_xstj_tjlx"></td>
<td>图表类型：<input id="jxc_xstj_tblx" name="jxc_xstj_tblx"></td>
<td align="right"><button id="export">导出</button></td>
</tr></table>
<br>
<div id="container" style="min-width:800px;height:400px"></div>
<div style="margin:10px;">注：因系统切换、并行等原因，2014年1月的销售金额合并在2月，2014年1-4月的毛利统计不十分准确。</div>

