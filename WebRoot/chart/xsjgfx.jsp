<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<script type="text/javascript">
var xsjgfx_did;
var xsjgfx_bmbh;
var xsjgfx_chart;

$(function(){
	xsjgfx_did = lnyw.tab_options().did;
	
	
	var fields = [{
	    "id": 'xsje',
	    "text": "销售金额(不含税)"
	},{
	    "id": 'xsml',
	    "text": "销售毛利"
	},];
	
	//部门
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
	
	//统计类型
	$('#jxc_xsjgfx_tjlx').combobox({
	    data: fields,
	    width:100,
	    valueField: 'id',
	    textField: 'text',
	    panelHeight: 'auto',
	}).combobox('selectedIndex', 0);
	
	//导出	
	$('#export').click(function() {
	    xsjgfx_chart.exportChart();
	});
	
});

var xsjgfx_options = {
    chart: {
        //renderTo: 'xsjgfx_container',
        plotBackgroundColor: null,
        plotBorderWidth: null,
        plotShadow: false
    },
    title:{
   		style: {
   			fontSize: '26px',
   		}
   	},
   	tooltip: {
        pointFormat: '<b>{point.y:.1f} 万元</b>'
    },
    plotOptions: {
        pie: {
            allowPointSelect: true,
            cursor: 'pointer',
            dataLabels: {
                enabled: true,
                format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                style: {
                    color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                }
            }
        }
    },
    series: [{
    	type: 'pie',
    }],
    credits:{
    	enabled: false
    },
    exporting:{
    	enabled: false
    }
};
	
function xsjgfx_drawPie(data){
	
	//xsjgfx_options.series[0].data = data[0];
	
	for(var i = 0; i < data.length; i++){
		xsjgfx_options.title.text = '' + data[i].name + $('#jxc_xsjgfx_tjlx').combobox('getText') + '对比分析';
		xsjgfx_options.chart.renderTo = 'xsjgfx_container' + i;
		
		xsjgfx_options.series[0].data = [];
		//var browserData = [];
	    for (var j = 0; j < data[i].cate.length; j++) {
	    	xsjgfx_options.series[0].data.push({
	            name: data[i].cate[j],
	            y: data[i].data[j],
	        });
	    }
		
		//xsjgfx_options.series[0].data = browserData;
		xsjgfx_chart = new Highcharts.Chart(xsjgfx_options);
	}
};
	
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
			xsjgfx_drawPie(data.series);
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
&nbsp;&nbsp;&nbsp;&nbsp;包含内部<input type="checkbox" id="jxc_xsjgfx_nb" name="jxc_xsjgfx_nb">
&nbsp;&nbsp;&nbsp;&nbsp;<button id="refresh" onclick="xsjgfx_getData()">刷新</button>
&nbsp;&nbsp;&nbsp;&nbsp;<button id="export">导出</button></td>
</tr></table>
<br>
<div>
<span id="xsjgfx_container0" style="min-width:800px;height:400px;float:left;"></span>
<span id="xsjgfx_container1" style="min-width:800px;height:400px;float:left;"></span>
</div>

