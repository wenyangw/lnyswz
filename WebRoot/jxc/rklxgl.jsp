<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<script type="text/javascript">
var rklx_dg;
var did;
var mid;
$(function(){
	rklx_dg = $('#rklx_gl_datagrid');
	did = lnyw.tab_options().did;
	mid = lnyw.tab_options().id;
	rklx_dg.datagrid({
		url:'${pageContext.request.contextPath}/jxc/rklxAction!datagrid.action',
		fit : true,
		singleSelect:true,
	    border : false,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
	    columns:[[	    	
	        {field:'id',title:'编号',width:100},	       
	        {field: 'rklxmc',title:'入库类型名称',width:100},	        
	     ]],
	});
	//根据权限，动态加载功能按钮
	lnyw.toolbar(0, rklx_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', did);
});
function appendRklx() {
	var p = $('#jxc_rklx_addDate');
	p.dialog({
		title : '增加入库类型',
		href : '${pageContext.request.contextPath}/jxc/rklxAdd.jsp',
		width : 340,
		height : 150,
		modal : true,
		buttons: [{
            text:'增加',
            iconCls:'icon-ok',
            handler:function(){
            	$('#jxc_rklxAdd_form').form('submit', {
					url : '${pageContext.request.contextPath}/jxc/rklxAction!add.action',
					onSubmit: function(){						
							if($(this).form('validate')){
								return true;
							}else{
								return false;
							}
						
					},
					success : function(d) {
						var json = $.parseJSON(d);
						if (json.success) {
							rklx_dg.datagrid('appendRow', json.obj);
							p.dialog('close');
						}
						$.messager.show({
							title : "提示",
							msg : json.msg
						});
					}
				});
            }
        }],
        onLoad : function() {
				var f = p.find('form');	
				f.form('load', {
					depId:did,
					menuId:mid,			
				});			
				f.find('input[name=id]').focus();
			}
	});
}

function editRklx(){
	var rows = rklx_dg.datagrid('getSelections');
	if(rows.length > 0){	
		var p = $('#jxc_rklx_addDate');
		p.dialog({
			title : '编辑入库类型',
			href : '${pageContext.request.contextPath}/jxc/rklxEdit.jsp',
			width : 350,
			height : 150,
			buttons : [ {
				text : '编辑',
				handler : function() {
					var f = p.find('form');
					f.form('submit', {
						url : '${pageContext.request.contextPath}/jxc/rklxAction!edit.action',
						onSubmit: function(){
							if($(this).form('validate')){
								return true;
							}else{
								return false;
							}
						},
						success : function(d) {
							var json = $.parseJSON(d);
							if (json.success) {
								rklx_dg.datagrid('reload');
								p.dialog('close');
							}
							$.messager.show({
								msg : json.msg,
								title : '提示'
							});
						}
					});
				}
			} ],
			onLoad : function() {
				var f = p.find('form');
				f.form('load', {
					id : rows[0].id,
					rklxmc: rows[0].rklxmc,
					depId:did,
					menuId:mid,			
				});
				f.find('input[name=id]').focus();
			}
		});	
	}else{
		$.messager.alert('警告', '请选择一条记录进行修改！',  'warning');
	}
}
function removeRklx(){
	var rows = rklx_dg.datagrid('getSelections');
	if (rows.length > 0) {
		$.messager.confirm('请确认', '您要删除当前所选项目？', function(r) {
			if (r) {
				$.ajax({
					url : '${pageContext.request.contextPath}/jxc/rklxAction!delete.action',
					data : {
						id : rows[0].id,
						depId:did,
						menuId:mid,	
					},
					dataType : 'json',
					success : function(d) {
						rklx_dg.datagrid('load');
						rklx_dg.datagrid('unselectAll');
						$.messager.show({
							title : '提示',
							msg : d.msg
						});
					}
				});
			}
		});
	} else {
		$.messager.alert('警告', '请选择一条记录进行删除！',  'warning');
	}
}

</script>
<div id="jxc_rklx_selet" class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center'">
		<div id="rklx_gl_datagrid"></div>
	</div>
</div>
<div id='jxc_rklx_addDate'></div>
