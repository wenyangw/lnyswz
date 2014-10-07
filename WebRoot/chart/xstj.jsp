<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<script type="text/javascript">

$(function(){
	var chart;
	
	var types = [{
	    "id": 'line',
	    "text": "折线图"
	},{
	    "id": 'column',
	    "text": "柱形图"
	},];
	
	var fields = [{
	    "id": 'xsje',
	    "text": "销售金额"
	},{
	    "id": 'xsml',
	    "text": "销售毛利"
	},];
	
	var options = {
	    chart: {
	        renderTo: 'container',
	    },
	    title:{
    		text: '销售分析'
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
        series: [],
        credits:{
        	enabled: false
        },
        exporting:{
        	enabled: false
        }
	};
	
	$('#cType').combobox({
	    data: types,
	    width:100,
	    valueField: 'id',
	    textField: 'text',
	    panelHeight: 'auto',
	    onSelect: function(rec){
	    	options.chart.type = $(this).combobox('getValue');
	    	chart = new Highcharts.Chart(options);
	    }
	}).combobox('selectedIndex', 0);
	
	$('#cField').combobox({
	    data: fields,
	    width:100,
	    valueField: 'id',
	    textField: 'text',
	    panelHeight: 'auto',
	    onSelect: function(rec){
	    	$.ajax({
	    		url: '${pageContext.request.contextPath}/jxc/chartAction!getXstj.action',
	    		data: {
	    			bmbh: '05',
	    			field: $(this).combobox('getValue')
	    		},
	    		cache: false,
	    		async: false,
	    		dataType: 'json',
	    		success: function(data){
	    			drawChart(data);
	    		}
	    	});
	    }
	}).combobox('selectedIndex', 0);
	
	$.ajax({
		url: '${pageContext.request.contextPath}/jxc/chartAction!getXstj.action',
		data: {
			bmbh: '05',
			field: $('#cField').combobox('getValue')
		},
		cache: false,
		async: false,
		dataType: 'json',
		success: function(data){
			drawChart(data);
		}
	});
	
	$('#export').click(function() {
	    chart.exportChart();
	});
	
	function drawChart(data){
		options.chart.type = $('#cType').combobox('getValue');
		options.xAxis.categories = data.categories;
		options.series = data.series;
		chart = new Highcharts.Chart(options);
	};
	 
});


</script>
<table width=100%><tr>
<td>统计类型：<input id="cField" name="cField"></td>
<td>图表类型：<input id="cType" name="cType"></td>
<td align="right"><button id="export">导出</button></td>
</tr></table>
<br>
<div id="container" style="min-width:800px;height:400px"></div>

