<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<script type="text/javascript">

$(function(){
	
	$.ajax({
		url: '${pageContext.request.contextPath}/jxc/chartAction!chartXsje.action',
		data: {
		},
		cache: false,
		async: false,
		dataType: 'json',
		success: function(data){
			createChart(data);
		}
	});
	 
});

function createChart(data){
	$('#container').highcharts({                   //图表展示容器，与div的id保持一致
        chart: {
            type: 'line'                         //指定图表的类型，默认是折线图（line）
        },
        title: {
            text: data.title      //指定图表标题
        },
        xAxis: {
        	categories: data.categories   //指定x轴分组
        },
        yAxis: {
            title: {
                text: '金额(元)',                  //指定y轴的标题
            }
        },
        series: data.series,
    });
}
</script>
<div id="container" style="min-width:800px;height:400px"></div>

