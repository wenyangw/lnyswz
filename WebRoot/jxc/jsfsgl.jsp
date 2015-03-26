<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<script type="text/javascript">
var jsfs_dg;
var did;
var mid;
$(function(){
	did = lnyw.tab_options().did;
	mid = lnyw.tab_options().id;
	jsfs_dg = $('#jsfs_gl_datagrid');
	jsfs_dg.datagrid({
		url:'${pageContext.request.contextPath}/jxc/jsfsAction!datagrid.action',
		fit : true,
		singleSelect:true,
	    border : false,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
	    columns:[[
	        {field:'id',title:'编号',width:100},
	        {field: 'jsmc',title:'结算方式名称',width:100},	        
	    ]],
	});
	//根据权限，动态加载功能按钮
	lnyw.toolbar(0, jsfs_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', did);
});
function appendJsfs() {
	var p = $('#jxc_jsfs_addDate');
	p.dialog({
		title : '增加结算方式',
		href : '${pageContext.request.contextPath}/jxc/jsfsAdd.jsp',
		width : 340,
		height : 150,
		modal : true,
		buttons: [{
            text:'增加',
            iconCls:'icon-ok',
            handler:function(){
            	$('#jxc_jsfsAdd_form').form('submit', {
					url : '${pageContext.request.contextPath}/jxc/jsfsAction!add.action',
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
							jsfs_dg.datagrid('appendRow', json.obj);
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
				f.find('input[name=jsmc]').focus();
		}
	});
}

function editJsfs(){
	var rows = jsfs_dg.datagrid('getSelections');
	if(rows.length > 0){		
		var p = $('#jxc_jsfs_addDate');
		p.dialog({
			title : '编辑结算方式',
			href : '${pageContext.request.contextPath}/jxc/jsfsEdit.jsp',
			width : 350,
			height : 150,
			buttons : [ {
				text : '编辑',
				handler : function() {
					var f = p.find('form');
					f.form('submit', {
						url : '${pageContext.request.contextPath}/jxc/jsfsAction!edit.action',
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
								jsfs_dg.datagrid('reload');
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
					jsmc: rows[0].jsmc,
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
function removeJsfs(){
	var rows = jsfs_dg.datagrid('getSelections');
	if (rows.length > 0) {
		$.messager.confirm('请确认', '您要删除当前所选项目？', function(r) {
			if (r) {
				$.ajax({
					url : '${pageContext.request.contextPath}/jxc/jsfsAction!delete.action',
					data : {
						id : rows[0].id,
						depId:did,
						menuId:mid,	
					},
					dataType : 'json',
					success : function(d) {
						jsfs_dg.datagrid('load');
						jsfs_dg.datagrid('unselectAll');
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
<div id="jxc_jsfs_selet" class="easyui-layout" data-options="fit:true">
	<div id="jsfs_gl_datagrid"></div>
</div>
<div id='jxc_jsfs_addDate'></div>
