<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<script type="text/javascript">
var cgxq_dg;

$(function(){
	cgxq_dg = $('#jxc_cgxq_dg').datagrid({
		//url: '${pageContext.request.contextPath}/jxc/cgxqAction!datagrid.action',
		fit : true,
	    border : false,
	    remoteSort: false,
	    singleSelect: true,
	    //fitColumns: true,
	    striped: true,
	    autoRowHeight: false,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
		columns:[[
			{field:'id',title:'记录号',align:'center',checkbox:true},
			{field:'cgxqlsh',title:'流水号',align:'center'},
	        {field:'createTime',title:'时间',align:'center'},
          	{field:'spbh',title:'商品编号',align:'center'},
            {field:'spmc',title:'名称',align:'center'},
            {field:'spcd',title:'产地',align:'center'},
            {field:'sppp',title:'品牌',align:'center'},
            {field:'spbz',title:'包装',align:'center'},
            {field:'zjldwmc',title:'单位1',align:'center',
            	formatter: function(value){
            		return value == 0 ? '' : value;
        		}},
            {field:'zdwsl',title:'数量1',align:'center',
               	formatter: function(value, row){
               		return value == 0 ? '' : value;
           		},
           		styler: function(value,row){
           			if(row.isRefuse == '1'){
               			return 'color:red;';
               		}else if(row.cgjhlsh){
               			return 'color:blue;';
               		}else if(row.isCancel == '1'){
               			return 'color:orange;';
               		}
    			}},
            {field:'zdwdj',title:'单价1',align:'center',
               	formatter: function(value){
               		return value == 0 ? '' : value;
           		}},
            {field:'zdwxsdj',title:'销价1',align:'center',
               	formatter: function(value){
               		return value == 0 ? '' : value;
           		}},
            {field:'cjldwmc',title:'单位2',align:'center'},
            {field:'cdwsl',title:'数量2',align:'center',
            	formatter: function(value){
            		return value == 0 ? '' : value;
        		}},
            {field:'cdwdj',title:'单价2',align:'center',
               	formatter: function(value){
               		return value == 0 ? '' : value;
           		}},
            {field:'cdwxsdj',title:'销价2',align:'center',
               	formatter: function(value){
               		return value == 0 ? '' : value;
           		}},
            {field:'spje',title:'金额',align:'center',
                formatter: function(value){
                	return value == 0 ? '' : lnyw.formatNumberRgx(value);
           		}},      
	        {field:'gysbh',title:'供应商编号',align:'center', hidden:true},
	        {field:'gysmc',title:'供应商名称',align:'center'},
// 	        {field:'ywyId',title:'业务员id',align:'center',hidden:true},
// 	        {field:'ywymc',title:'业务员',align:'center'},
// 	        {field:'dhfs',title:'到货方式',align:'center'},
	        {field:'lxr',title:'联系人',align:'center',},
	        {field:'shdz',title:'送货地址',align:'center',
				formatter: function(value){
    				return lnyw.memo(value, 15);
    			}},
	        {field:'jsfsmc',title:'付款方式',align:'center'},
	        {field:'dhsj',title:'到货时间',align:'center',
            	formatter: function(value){
            		return value == undefined ? '' : moment(value).format('YYYY-MM-DD');
        		}},
// 	        {field:'xqsj',title:'需求时间',align:'center'},
	        {field:'hjje',title:'金额',align:'center',
            	formatter: function(value){
            		return value == 0 ? '' : value;
        		}},
        	{field:'bz',title:'备注',align:'center',
        		formatter: function(value){
        			return lnyw.memo(value, 15);
        		}},
        	{field:'cgjhlsh',title:'*采购计划流水号',align:'center',sortable:true,
        		sorter: function(a,b){
        			a = a == undefined ? 0 : a;
        			b = b == undefined ? 0 : b;
					return (a-b);  
				},
        		formatter: function(value){
        			return lnyw.memo(value, 15);
        		}},
        	{field:'isLs',title:'*临时',align:'center',sortable:true,
        		formatter : function(value) {
					if (value == '1') {
						return '是';
					} else {
						return '否';
					}
				},
        		sorter: function(a,b){
        			a = a == undefined ? 0 : a;
        			b = b == undefined ? 0 : b;
					return (a-b);  
				}},
        	{field:'isCancel',title:'*状态',align:'center',sortable:true,
        		formatter : function(value) {
					if (value == '1') {
						return '取消';
					} else {
						return '';
					}
				},
        		sorter: function(a,b){
        			a = a == undefined ? 0 : a;
        			b = b == undefined ? 0 : b;
					return (a-b);  
				}},
        	{field:'isRefuse',title:'*退回',align:'center',sortable:true,
        		formatter : function(value) {
					if (value == '1') {
						return '是';
					} else {
						return '';
					}
				},
				sorter: function(a,b){
	        			a = a == undefined ? 0 : a;
	        			b = b == undefined ? 0 : b;
						return (a-b);  
				}},
	    ]],
	    toolbar:'#jxc_cgxq_tb',
	});
	lnyw.toolbar(1, cgxq_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', did);
	
	
// 	cgxq_dg.datagrid({
//         view: detailview,
//         detailFormatter:function(index,row){
//             return '<div style="padding:2px"><table id="ddv-' + index + '"></table></div>';
//         },
//         onExpandRow: function(index,row){
//             $('#ddv-'+index).datagrid({
//                 url:'${pageContext.request.contextPath}/jxc/cgxqAction!detDatagrid.action',
//                 fitColumns:true,
//                 singleSelect:true,
//                 rownumbers:true,
//                 loadMsg:'',
//                 height:'auto',
//                 queryParams: {
//         			cgxqlsh: row.cgxqlsh,
//         		},
//                 columns:[[
//                     {field:'spbh',title:'商品编号',width:200,align:'center'},
//                     {field:'spmc',title:'名称',width:100,align:'center'},
//                     {field:'spcd',title:'产地',width:100,align:'center'},
//                     {field:'sppp',title:'品牌',width:100,align:'center'},
//                     {field:'spbz',title:'包装',width:100,align:'center'},
//                     {field:'zjldwmc',title:'单位1',width:100,align:'center'},
//                     {field:'zdwsl',title:'数量1',width:100,align:'center'},
//                     {field:'zdwdj',title:'单价1',width:100,align:'center'},
//                     {field:'zdwxsdj',title:'销价1',width:100,align:'center'},
//                     {field:'cjldwmc',title:'单位2',width:100,align:'center'},
//                     {field:'cdwsl',title:'数量2',width:100,align:'center'},
//                     {field:'cdwdj',title:'单价2',width:100,align:'center'},
//                     {field:'cdwxsdj',title:'销价2',width:100,align:'center'},
//                     {field:'spje',title:'金额',width:100,align:'center'},
//                 ]],
//                 onResize:function(){
//                 	cgxq_dg.datagrid('fixDetailRowHeight',index);
//                 },
//                 onLoadSuccess:function(){
//                     setTimeout(function(){
//                     	cgxq_dg.datagrid('fixDetailRowHeight',index);
//                     },0);
//                 }
//             });
//             cgxq_dg.datagrid('fixDetailRowHeight',index);
//         }
//     });
	
	//选中列表标签后，装载数据
	$('#jxc_cgxq_tabs').tabs({
		onSelect: function(title, index){
			if(index == 1){
				cgxq_dg.datagrid({
					url: '${pageContext.request.contextPath}/jxc/cgxqAction!datagrid.action',
					queryParams: {
						bmbh: did,
					},
				});
			}
		}
	});
	
});



//////////////////////////////////////////////以下为采购需求列表处理代码
function cancelCgxq(){
	var row = cgxq_dg.datagrid('getSelected');
	if (row != undefined) {
		if(row.cgjhlsh == undefined){
			if(row.isCancel != '1'){
				$.messager.confirm('请确认', '您要取消选中的采购需求单？', function(r) {
					if (r) {
						$.ajax({
							url : '${pageContext.request.contextPath}/jxc/cgxqAction!cancel.action',
							data : {
								id : row.id,
								menuId : menuId,
								bmbh : did,
							},
							dataType : 'json',
							success : function(d) {
								cgxq_dg.datagrid('load');
								cgxq_dg.datagrid('unselectAll');
								$.messager.show({
									title : '提示',
									msg : d.msg
								});
							}
						});
					}
				});
			}else{
				$.messager.alert('警告', '选中的采购需求记录已被取消，请重新选择！',  'warning');
			}
		}else{
			$.messager.alert('警告', '选中的采购需求记录已进行计划，不能取消！',  'warning');
		}
	}else{
		$.messager.alert('警告', '请选择最少一条记录进行操作！',  'warning');
	}
}

// function completeCgxq(){
// 	var row = cgxq_dg.datagrid('getSelected');
// 	if (row != undefined) {
// 		if(row.isCompleted != '1'){
// 			if(row.isCancel != '1'){
// 				if(row.cgjhlsh != undefined){
// 					$.messager.confirm('请确认', '您要完成选中的采购需求单？', function(r) {
// 						if (r) {
// 							$.ajax({
// 								url : '${pageContext.request.contextPath}/jxc/cgxqAction!complete.action',
// 								data : {
// 									cgxqlsh : row.cgxqlsh,
// 									menuId : menuId,
// 									bmbh : did,
// 								},
// 								dataType : 'json',
// 								success : function(d) {
// 									cgxq_dg.datagrid('load');
// 									cgxq_dg.datagrid('unselectAll');
// 									$.messager.show({
// 										title : '提示',
// 										msg : d.msg
// 									});
// 								}
// 							});
// 						}
// 					});
// 				}else{
// 					$.messager.alert('警告', '选中的采购需求还未进行计划，不能进行完成操作！',  'warning');
// 				}
// 			}else{
// 				$.messager.alert('警告', '选中的采购需求记录已被取消，请重新选择！',  'warning');
// 			}
// 		}else{
// 			$.messager.alert('警告', '选中的采购需求记录已完成，请重新选择！',  'warning');
// 		}
// 	}else{
// 		$.messager.alert('警告', '请选择最少一条记录进行操作！',  'warning');
// 	}
// }

function searchCgxq(){
	cgxq_dg.datagrid('load',{
		bmbh: did,
		createTime: $('input[name=createTimeCgxq]').val(),
	});
}

//////////////////////////////////////////////以上为采购需求列表处理代码

</script>
<div title="采购需求列表" data-options="closable:false" >
		<table id='jxc_cgxq_dg'></table>
</div>
<div id="jxc_cgxq_tb" style="padding:3px;height:auto">
	请输入查询起始日期:<input type="text" name="createTimeCgxq" class="easyui-datebox" data-options="value: moment().date(1).format('YYYY-MM-DD')" style="width:100px">
	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchCgxq();">查询</a>
</div>


