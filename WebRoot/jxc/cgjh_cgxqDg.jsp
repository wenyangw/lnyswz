<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<script type="text/javascript">
var cgjh_cgxqDg;

$(function(){
	cgjh_cgxqDg = $('#jxc_cgjh_cgxqDg').datagrid({
		//url: '${pageContext.request.contextPath}/jxc/cgxqAction!datagrid.action',
		fit : true,
	    border : false,
	    singleSelect : true,
	    remoteSort: false,
	    fitColumns: true,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
		queryParams: {
			bmbh: lnyw.tab_options().did,
		},
		frozenColumns:[[
		    
		]],
		columns:[[
			{field:'cgxqlsh',title:'流水号',width:100,align:'center'},
	        {field:'createTime',title:'时间',width:150,align:'center'},
	        {field:'gysbh',title:'供应商编号',width:200,align:'center'},
	        {field:'gysmc',title:'商品品牌',width:200,align:'center'},
	        {field:'ywyId',title:'业务员id',width:200,align:'center',hidden:true},
	        {field:'ywymc',title:'业务员',width:200,align:'center'},
	        {field:'dhfs',title:'到货方式',width:200,align:'center'},
	        {field:'shdz',title:'送货地址',width:200,align:'center'},
	        {field:'fkfs',title:'付款方式',width:200,align:'center'},
	        {field:'dhsj',title:'到货时间',width:200,align:'center'},
	        {field:'xqsj',title:'需求时间',width:200,align:'center'},
	        {field:'hjje',title:'金额',width:200,align:'center'},
        	{field:'bz',title:'备注',width:200,align:'center'},
        	{field:'isCancel',title:'状态',width:200,align:'center',sortable:true,
        		formatter : function(value) {
					if (value == '1') {
						return '取消';
					} else {
						return '正常';
					}
				},
        		sorter: function(a,b){
        			a = a == undefined ? 0 : a;
        			b = b == undefined ? 0 : b;
					return (a-b);  
				}},
        	{field:'isCompleted',title:'完成',width:200,align:'center',sortable:true,
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
	    ]],
	});
	
	
	
	cgjh_cgxqDg.datagrid({
        view: detailview,
        detailFormatter:function(index,row){
            return '<div style="padding:2px"><table id="ddv-' + index + '"></table></div>';
        },
        onExpandRow: function(index,row){
            $('#ddv-'+index).datagrid({
                url:'${pageContext.request.contextPath}/jxc/cgxqAction!detDatagrid.action',
                fitColumns:true,
                singleSelect:true,
                rownumbers:true,
                loadMsg:'',
                height:'auto',
                queryParams: {
        			cgxqlsh: row.cgxqlsh,
        		},
                columns:[[
                    {field:'spbh',title:'商品编号',width:200,align:'center'},
                    {field:'spmc',title:'名称',width:100,align:'center'},
                    {field:'spcd',title:'产地',width:100,align:'center'},
                    {field:'sppp',title:'品牌',width:100,align:'center'},
                    {field:'spbz',title:'包装',width:100,align:'center'},
                    {field:'zjldwmc',title:'单位1',width:100,align:'center'},
                    {field:'zdwsl',title:'数量1',width:100,align:'center'},
                    {field:'zdwdj',title:'单价1',width:100,align:'center'},
                    {field:'cjldwmc',title:'单位2',width:100,align:'center'},
                    {field:'cdwsl',title:'数量2',width:100,align:'center'},
                    {field:'cdwdj',title:'单价2',width:100,align:'center'},
                    {field:'hjje',title:'金额',width:100,align:'center'},
                ]],
                onResize:function(){
                	cgjh_cgxqDg.datagrid('fixDetailRowHeight',index);
                },
                onLoadSuccess:function(){
                    setTimeout(function(){
                    	cgjh_cgxqDg.datagrid('fixDetailRowHeight',index);
                    },0);
                }
            });
            cgjh_cgxqDg.datagrid('fixDetailRowHeight',index);
        }
    });
	
	//选中列表标签后，装载数据
	$('#jxc_cgjh_tabs').tabs({
		onSelect: function(title, index){
			if(index == 2){
				
				cgjh_cgxqDg.datagrid({
					url: '${pageContext.request.contextPath}/jxc/cgxqAction!datagrid.action',
				});
				lnyw.toolbar(index, cgjh_cgxqDg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', lnyw.tab_options().did);
			}
		}
	});
	
});



//////////////////////////////////////////////以下为采购需求列表处理代码
function cancelCgxq(){
	var row = cgjh_cgxqDg.datagrid('getSelected');
	if (row != undefined) {
		if(row.isCancel != '1'){
			if(row.isCompleted != '1'){
				$.messager.confirm('请确认', '您要取消选中的采购需求单？', function(r) {
					if (r) {
						$.ajax({
							url : '${pageContext.request.contextPath}/jxc/cgxqAction!cancel.action',
							data : {
								cgxqlsh : row.cgxqlsh
							},
							dataType : 'json',
							success : function(d) {
								cgjh_cgxqDg.datagrid('load');
								cgjh_cgxqDg.datagrid('unselectAll');
								$.messager.show({
									title : '提示',
									msg : d.msg
								});
							}
						});
					}
				});
			}else{
				$.messager.alert('警告', '选中的采购需求记录已完成，不能取消！',  'warning');
			}
		}else{
			$.messager.alert('警告', '选中的采购需求记录已被取消，请重新选择！',  'warning');
		}
	}else{
		$.messager.alert('警告', '请选择最少一条记录进行操作！',  'warning');
	}
}

function completeCgxq(){
	var row = cgjh_cgxqDg.datagrid('getSelected');
	if (row != undefined) {
		if(row.isCancel != '1'){
			if(row.isCompleted != '1'){
				$.messager.confirm('请确认', '您要完成选中的采购需求单？', function(r) {
					if (r) {
						$.ajax({
							url : '${pageContext.request.contextPath}/jxc/cgxqAction!complete.action',
							data : {
								cgxqlsh : row.cgxqlsh
							},
							dataType : 'json',
							success : function(d) {
								cgjh_cgxqDg.datagrid('load');
								cgjh_cgxqDg.datagrid('unselectAll');
								$.messager.show({
									title : '提示',
									msg : d.msg
								});
							}
						});
					}
				});
			}else{
				$.messager.alert('警告', '选中的采购需求记录已完成，请重新选择！',  'warning');
			}
		}else{
			$.messager.alert('警告', '选中的采购需求记录已被取消，请重新选择！',  'warning');
		}
	}else{
		$.messager.alert('警告', '请选择最少一条记录进行操作！',  'warning');
	}
}


//////////////////////////////////////////////以上为采购需求列表处理代码

</script>
<div title="采购需求列表" data-options="closable:false" >
		<table id='jxc_cgjh_cgxqDg'></table>
</div>


