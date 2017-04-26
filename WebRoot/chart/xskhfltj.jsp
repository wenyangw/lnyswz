<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<script type="text/javascript">
var xskhfltj_did;
var xskhfltj_bmbh;
var xskhfltj_chart;
var jxc_xskhfltj_ywyCombo;
var xskhfltj_kh_dg;


$(function(){
	xskhfltj_did = lnyw.tab_options().did;
	
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
	}];
	
	var years = [];
	for(var i = 0; i < 3; i++){
		var v = moment().year() - i;
		years.push({"id": v, "text": v});
	}
	
	if(xskhfltj_did >= '10'){
		//$('.bm').css('display','table-cell');
		$('.bm').css('display','inline');
		$('#jxc_xskhfltj_dep').combobox({
			data: ywbms,
		    width:100,
		    valueField: 'id',
		    textField: 'depName',
		    panelHeight: 'auto',
		    onSelect: function(rec){
		    	xskhfltj_bmbh = $(this).combobox('getValue');
		    	xskhfltj_kh_dg.datagrid('load',{
		    		bmbh: xskhfltj_bmbh,
					year: $('#jxc_xskhfltj_year').combobox('getValue'),
		    	});
		    }
		}).combobox('selectedIndex', 0);
		xskhfltj_bmbh = $('#jxc_xskhfltj_dep').combobox('getValue');
	}else{
		xskhfltj_bmbh = xskhfltj_did;
	}
	
/* 	jxc_xskhfltj_ywyCombo = lnyw.initCombo($("#jxc_xskhfltj_ywy"), 'id', 'realName', '${pageContext.request.contextPath}/admin/userAction!listYwys.action?did=' + xskhfltj_bmbh);
	
	$('input[name=jxc_xskhfltj_nb]').prop('checked', true);
	jxc_xskhfltj_ywyCombo.combobox('disable');
	
	$('input[name=jxc_xskhfltj_nb]').click(function(){
		if($(this).is(':checked')){
			jxc_xskhfltj_ywyCombo.combobox('disable');
		}else{
			jxc_xskhfltj_ywyCombo.combobox('enable');
			jxc_xskhfltj_ywyCombo.combobox('selectedIndex', 0);
		}
	}); */
	
	$('#jxc_xskhfltj_tblx').combobox({
	    data: types,
	    width:100,
	    valueField: 'id',
	    textField: 'text',
	    panelHeight: 'auto',
	    onSelect: function(rec){
	    	xskhfltj_options.chart.type = $(this).combobox('getValue');
	    	xskhfltj_setColumnLabel();
	    	xskhfltj_chart = new Highcharts.Chart(xskhfltj_options);
	    }
	}).combobox('selectedIndex', 0);
	
	
	
	$('#jxc_xskhfltj_tjlx').combobox({
	    data: fields,
	    width:100,
	    valueField: 'id',
	    textField: 'text',
	    panelHeight: 'auto',
	    onSelect: function(){
	    	xskhfltj_options.title.text = $(this).combobox('getText') + '对比分析';
	    }
	}).combobox('selectedIndex', 0);
	
	$('#jxc_xskhfltj_year').combobox({
	    data: years,
	    width:100,
	    valueField: 'id',
	    textField: 'text',
	    panelHeight: 'auto',
	    onSelect: function(rec){
	    	//xskhfltj_bmbh = $(this).combobox('getValue');
	    	xskhfltj_kh_dg.datagrid('load',{
	    		bmbh: xskhfltj_bmbh,
				year: $('#jxc_xskhfltj_year').combobox('getValue'),
	    	});
	    }
	}).combobox('selectedIndex', 0);
	
	
	listKh();
	
	$('#export').click(function() {
	    xskhfltj_chart.exportChart();
	});
	
	
	
});

var xskhfltj_column_dataLabels = {
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

var xskhfltj_line_dataLabels = {
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

var xskhfltj_options = {
    chart: {
        renderTo: 'xskhfltj_container',
    },
    title:{
   		text: '销售金额排名',
   		style: {
   			fontSize: '26px',
   		}
   	},
	xAxis: {
       	categories: [],
       	labels: {
       		rotation: -45
        }
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
	
function xskhfltj_drawChart(data){
	xskhfltj_options.chart.type = $('#jxc_xskhfltj_tblx').combobox('getValue');
	xskhfltj_options.xAxis.categories = data.categories;
	xskhfltj_options.series = data.series;
	xskhfltj_setColumnLabel();
	xskhfltj_chart = new Highcharts.Chart(xskhfltj_options);
};
	
function xskhfltj_setColumnLabel(){
	for(var i = 0; i < xskhfltj_options.series.length; i++){
		if($('#jxc_xskhfltj_tblx').combobox('getValue') == 'column'){
			xskhfltj_options.series[i].dataLabels = xskhfltj_column_dataLabels;
		}else{
			xskhfltj_options.series[i].dataLabels = xskhfltj_line_dataLabels;
		}
	}
}

function xskhfltj_getData(){
	lnyw.MaskUtil.mask('正在刷新，请等待……');
	$.ajax({
		url: '${pageContext.request.contextPath}/jxc/chartAction!getXskhfltj.action',
		data: {
			bmbh: xskhfltj_bmbh,
			//ywyId: jxc_xskhfltj_ywyCombo.combobox('getValue'),
			field: $('#jxc_xskhfltj_tjlx').combobox('getValue'),
			year: $('#jxc_xskhfltj_year').combobox('getValue'),
			includeNb: $('input#jxc_xskhfltj_nb').is(':checked') ? '1' : '0',
		},
		cache: false,
		async: false,
		dataType: 'json',
		success: function(data){
			xskhfltj_drawChart(data);
		},
		complete: function(){
			lnyw.MaskUtil.unmask();
		}
	});
}

function listKh(){
	console.info('listKh');
	xskhfltj_kh_dg = $('#xskhfltj_kh_dg').datagrid({
		url : '${pageContext.request.contextPath}/jxc/chartAction!listKh.action',
		fit : true,
	    border : false,
	    singleSelect : true,
	    fitColumns: true,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
		queryParams: {
			bmbh: xskhfltj_bmbh,
			year: $('#jxc_xskhfltj_year').combobox('getValue'),
		},
		columns:[[
	        {field:'khbh',title:'客户编号'},
	        {field:'khmc',title:'客户名称'},
	    ]],
	    toolbar:'#xskhfltj_kh_tb',
	    onClickRow:function(rowIndex, rowData){
	    	khDet_dg.datagrid({
	    		url: '${pageContext.request.contextPath}/jxc/khAction!datagridDet.action',
	 	    	queryParams:{
	 	    		depId: kh_did,
	 	    		khbh: rowData.khbh
	 	    	},
	    	});
	    	//根据权限，动态加载功能按钮
	    	lnyw.toolbar(0, khDet_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', kh_did);
	    	
// 	    	khDet_dg.datagrid('load', {
// 	    		depId: kh_did,
// 	    		khbh: rowData.khbh
// 	    	});
	    },
	});
}

</script>
<div id='xskhfltj_layout' class="easyui-layout" style="height:100%;width=100%">
	<div data-options="region:'west',split:true,collapsible:false" style="width:320px">
		<div id='xskhfltj_kh_west' class="easyui-layout" data-options="fit:true, split:false" style="height:100%; width=100%">
			<div data-options="region:'north',title:'',split:true" style="height:100px;width:250px">		
				<div class="bm" style="display:none">部门：<input id="jxc_xskhfltj_dep" name="jxc_xskhfltj_dep"></div>
				<div>统计年度：<input id="jxc_xskhfltj_year" name="jxc_xskhfltj_year"></div>
			</div>
			<div data-options="region:'center',title:'客户列表',split:true" style="height:100px;width:250px">		
				<div id='xskhfltj_kh_dg'></div>
			</div>
		</div>
	</div>
	<div data-options="region:'center',title:'详细信息',split:true, fit:true" style="height:100px;">
		<table width=100% style="margin:5px;">
		<tr>
			<td align="left">
			&nbsp;&nbsp;&nbsp;&nbsp;统计类型：<input id="jxc_xskhfltj_tjlx" name="jxc_xskhfltj_tjlx">
			&nbsp;&nbsp;&nbsp;&nbsp;图表类型：<input id="jxc_xskhfltj_tblx" name="jxc_xskhfltj_tblx">
			&nbsp;&nbsp;&nbsp;&nbsp;<button id="refresh" onclick="xskhfltj_getData()">刷新</button>
			&nbsp;&nbsp;&nbsp;&nbsp;<button id="export">导出</button>
			</td>
		</tr>
		</table>
		<div id="xskhfltj_container" style="min-width:800px;height:400px;float:right"></div>
		<div style="margin:20px;">1：在统计年度中销售金额前20名的客户，与上一年的对比</div>
		<div style="margin:20px;">2：统计数据的分类依据为（客户+业务员）</div>
		<div style="margin:20px;">3：统计年度为当年，数据截止到当月;统计年度为往年，数据截止到年底。</div>
    </div>
</div>
<div id="xskhfltj_kh_tb" style="padding:3px;height:auto">
	输入编号、名称：<input type="text" name="search" style="width:100px">
	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchKh();">查询</a>
</div>

