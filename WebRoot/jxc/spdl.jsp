<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<script type="text/javascript">
var spdl_dg;
var did;
$(function(){
	did = lnyw.tab_options().did;
	spdl_dg = $('#jxc_spdl_dg').datagrid({
	    url:'${pageContext.request.contextPath}/jxc/spdlAction!datagrid.action',
	    fit : true,
	    border : false,
	    singleSelect : true,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
		idField : 'id',
	    columns:[[
	        {field:'id',title:'id',width:100},
	        {field:'spdlmc',title:'商品大类名称',width:100},
	        {field:'depIds',title:'部门id',width:100,hidden:true},
	        {field:'depNames',title:'部门名称'},
	        
	    ]],
	});
	lnyw.toolbar(0, spdl_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', did);
});


function appendSpdl() {
	var p = $('#jxc_spdl_addDialog');
	p.dialog({
		title : '增加商品大类',
		href : '${pageContext.request.contextPath}/jxc/spdlAdd.jsp',
		width : 340,
		height : 200,
		modal : true,
		buttons: [{
            text:'确定',
            iconCls:'icon-ok',
            handler:function(){
            	$('#jxc_spdlAdd_form').form('submit', {
					url : '${pageContext.request.contextPath}/jxc/spdlAction!add.action',
					success : function(d) {
						var json = $.parseJSON(d);
						if (json.success) {
							spdl_dg.datagrid('appendRow', json.obj);
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
			$('input[name=id]').focus();
		}
	});
}

function editSpdl(){
	var rows = spdl_dg.datagrid('getSelections');
	if (rows.length == 1) {
		var p = $('#jxc_spdl_addDialog');
		p.dialog({
			title : '修改商品大类',
			href : '${pageContext.request.contextPath}/jxc/spdlEdit.jsp',
			width : 350,
			height : 210,
			buttons : [ {
				text : '确定',
				handler : function() {
					var f = p.find('form');
					f.form('submit', {
						url : '${pageContext.request.contextPath}/jxc/spdlAction!edit.action',
						success : function(d) {
							var json = $.parseJSON(d);
							if (json.success) {
								spdl_dg.datagrid('reload');
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
				var depIds = f.find('input[name=depIds]');
				var depCombo = depIds.combobox({
				    url:'${pageContext.request.contextPath}/admin/departmentAction!listDeps.action',
				    valueField : 'id',
					textField : 'depName',
				    multiple: true,				
				});
				f.form('load', {
					id : rows[0].id,
					spdlmc : rows[0].spdlmc,
					depIds : lnyw.getList(rows[0].depIds), 
				});
				f.find('input[name=id]').attr('readonly', true);
				f.find('input[name=spdlmc]').focus();
			}
		});
	} else if (rows.length > 1) {
		$.messager.alert('提示', '同一时间只能编辑一条记录！', 'error');
	} else {
		$.messager.alert('提示', '请选择一条要编辑的记录！', 'error');
	}
}
function removeSpdl(){
	var rows = spdl_dg.datagrid('getSelections');
	if (rows.length == 1) {
		$.messager.confirm('请确认', '您要删除当前所选项目？', function(r) {
			if (r) {
				$.ajax({
					url : '${pageContext.request.contextPath}/jxc/spdlAction!delete.action',
					data : {
						id : rows[0].id
					},
					dataType : 'json',
					success : function(d) {
						spdl_dg.datagrid('load');
						spdl_dg.datagrid('unselectAll');
						$.messager.show({
							title : '提示',
							msg : d.msg
						});
					}
				});
			}
		});
	} else if (rows.length > 1) {
		$.messager.alert('提示', '同一时间只能编辑一条记录！', 'error');
	} else {
		$.messager.alert('提示', '请选择一条要编辑的记录！', 'error');
	}
}
</script>
<div id='jxc_spdl_dg'></div>
<div id='jxc_spdl_addDialog'></div>

	
