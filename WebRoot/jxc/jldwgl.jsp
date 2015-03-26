<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<script type="text/javascript">
var jldw_dg;
var did;
var mid;
$(function(){
	jldw_dg = $('#jldw_gl_datagrid');
	did = lnyw.tab_options().did;
	mid = lnyw.tab_options().id;
	jldw_dg.datagrid({
		url:'${pageContext.request.contextPath}/jxc/jldwAction!datagrid.action',
		fit : true,
		singleSelect:true,
	    border : false,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
	    columns:[[
	        {field:'id',title:'编号',width:100},
	        {field: 'jldwmc',title:'计量单位名称',width:100},	        
	    ]],
	});
	//根据权限，动态加载功能按钮
	lnyw.toolbar(0, jldw_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', did);
});
function appendJldw() {
	var p = $('#jxc_jldw_addDate');
	p.dialog({
		title : '增加计量单位',
		href : '${pageContext.request.contextPath}/jxc/jldwAdd.jsp',
		width : 340,
		height : 150,
		modal : true,
		buttons: [{
            text:'增加',
            iconCls:'icon-ok',
            handler:function(){
            	$('#jxc_jldwAdd_form').form('submit', {
					url : '${pageContext.request.contextPath}/jxc/jldwAction!add.action',
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
							jldw_dg.datagrid('appendRow', json.obj);
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

function editJldw(){
	var rows = jldw_dg.datagrid('getSelections');
	if(rows.length > 0){	
		var p = $('#jxc_jldw_addDate');
		p.dialog({
			title : '编辑计量单位',
			href : '${pageContext.request.contextPath}/jxc/jldwEdit.jsp',
			width : 350,
			height : 150,
			buttons : [ {
				text : '编辑',
				handler : function() {
					var f = p.find('form');
					f.form('submit', {
						url : '${pageContext.request.contextPath}/jxc/jldwAction!edit.action',
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
								jldw_dg.datagrid('reload');
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
					jldwmc: rows[0].jldwmc,
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
function removeJldw(){
	var rows = jldw_dg.datagrid('getSelections');
	if (rows.length > 0) {
		$.messager.confirm('请确认', '您要删除当前所选项目？', function(r) {
			if (r) {
				$.ajax({
					url : '${pageContext.request.contextPath}/jxc/jldwAction!delete.action',
					data : {
						id : rows[0].id,
						depId:did,
						menuId:mid,	
					},
					dataType : 'json',
					success : function(d) {
						jldw_dg.datagrid('load');
						jldw_dg.datagrid('unselectAll');
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
<div id="jxc_jldw_selet" class="easyui-layout" data-options="fit:true">			
	<div data-options="region:'center'">
		<div id="jldw_gl_datagrid"></div>
	</div>
</div>
<div id='jxc_jldw_addDate'></div>

