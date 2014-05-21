<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<script type="text/javascript">
var fh_dg;
var depCombo;
var did;
$(function(){
	did = lnyw.tab_options().did;
	
	fh_dg = $('#jxc_fh_dg');
	fh_dg.datagrid({
		url:'${pageContext.request.contextPath}/jxc/fhAction!datagrid.action',
		fit : true,
		singleSelect:true,
	    border : false,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
	    columns:[[    	
	        {field:'id',title:'编号',width:100},	       
	        {field: 'fhmc',title:'分户名称',width:100},
	        {field:'depId',title:'部门id',width:100, hidden:true},
	        {field:'depName',title:'部门',width:100},
	        ]],
	});
	//根据权限，动态加载功能按钮
	lnyw.toolbar(0, fh_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', did);
});

function appendFh() {
	var p = $('#jxc_fh_addDialog');
	p.dialog({
		title : '增加分户管理',
		href : '${pageContext.request.contextPath}/jxc/fhAdd.jsp',
		width : 340,
		height : 200,
		modal : true,
		buttons: [{
            text:'增加',
            iconCls:'icon-ok',
            handler:function(){
            	$('#jxc_fhAdd_form').form('submit', {
					url : '${pageContext.request.contextPath}/jxc/fhAction!add.action',
					success : function(d) {
						var json = $.parseJSON(d);
						if (json.success) {
							fh_dg.datagrid('appendRow', json.obj);
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
				var depId = f.find('input[name=depId]');
        	    var depCombo = depId.combobox({
			    	url:'${pageContext.request.contextPath}/admin/departmentAction!listDeps.action',
			    	valueField:'id',
			    	textField:'depName'
				});
				f.find('input[name=id]').focus();
			}
	});
}

function editFh(){
	var rows = fh_dg.datagrid('getSelections');
	if(rows.length > 0){	
		var p = $('#jxc_fh_addDialog');
		p.dialog({
			title : '编辑分户管理',
			href : '${pageContext.request.contextPath}/jxc/fhAdd.jsp',
			width : 350,
			height : 200,
			buttons : [ {
				text : '编辑',
				handler : function() {
					var f = p.find('form');
					f.form('submit', {
						url : '${pageContext.request.contextPath}/jxc/fhAction!edit.action',
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
								fh_dg.datagrid('reload');
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
				var depId = f.find('input[name=depId]');
	        	var depCombo = depId.combobox({
				    url:'${pageContext.request.contextPath}/admin/departmentAction!listDeps.action',
				    valueField:'id',
				    textField:'depName'
				});
				f.form('load', rows[0]);
				f.find('input[name=fhmc]').focus();
			}
		});	
	}else{
		$.messager.alert('警告', '请选择一条记录进行修改！',  'warning');
	}
}
function removeFh(){
	var rows = fh_dg.datagrid('getSelections');

	if (rows.length > 0) {
		$.messager.confirm('请确认', '您要删除当前所选项目？', function(r) {
			if (r) {
				$.ajax({
					url : '${pageContext.request.contextPath}/jxc/fhAction!delete.action',
					data : {
						id : rows[0].id,
					},
					dataType : 'json',
					success : function(d) {
						fh_dg.datagrid('load');
						fh_dg.datagrid('unselectAll');
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
<div id="jxc_fh_layout" class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center'">
		<div id="jxc_fh_dg"></div>
	</div>
</div>

<div id='jxc_fh_addDialog'></div>

