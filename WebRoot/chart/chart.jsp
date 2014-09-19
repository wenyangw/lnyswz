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
            categories: ['一', '二', '三', '四', '五', '六', '七', '八', '九', '十', '十一', '十二']   //指定x轴分组
        },
        yAxis: {
            title: {
                text: '元'                  //指定y轴的标题
            }
        },
        series: [{                                 //指定数据列
            name: '2014年',                          //数据列名
            data: [1, 0, 4, 3, 4, 1, 3, 6, 2, 0, 3, 1]                        //数据
        }, {
            name: '2013年',
            data: [3, 1, 4, 2, 5, 1, 2, 4, 1, 4, 2, 4]
        }]
    });
}
</script>
<div id="container" style="min-width:800px;height:400px"></div>

