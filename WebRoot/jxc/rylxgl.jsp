<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<script type="text/javascript">
var rylx_dg;
var did;
var mid;
$(function(){
	rylx_dg = $('#rylx_gl_datagrid');
	did = lnyw.tab_options().did;
	mid = lnyw.tab_options().id;
	rylx_dg.datagrid({
		url:'${pageContext.request.contextPath}/jxc/rylxAction!datagrid.action',
		fit : true,
		singleSelect:true,
	    border : false,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
	    columns:[[	    	
	        {field:'id',title:'编号',width:100},	       
	        {field: 'rylxmc',title:'人员类型名称',width:100},	        
	    ]],
	});
	//根据权限，动态加载功能按钮
	lnyw.toolbar(rylx_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', did);
});
function appendRylx() {
	var p = $('#jxc_rylx_addDate');
	p.dialog({
		title : '增加人员类型',
		href : '${pageContext.request.contextPath}/jxc/rylxAdd.jsp',
		width : 340,
		height : 150,
		modal : true,
		buttons: [{
            text:'增加',
            iconCls:'icon-ok',
            handler:function(){
            	$('#jxc_rylxAdd_form').form('submit', {
					url : '${pageContext.request.contextPath}/jxc/rylxAction!add.action',
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
							rylx_dg.datagrid('appendRow', json.obj);
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

function editRylx(){
	var rows = rylx_dg.datagrid('getSelections');
	if(rows.length > 0){	
		var p = $('#jxc_rylx_addDate');
		p.dialog({
			title : '编辑人员类型',
			href : '${pageContext.request.contextPath}/jxc/rylxEdit.jsp',
			width : 350,
			height : 150,
			buttons : [ {
				text : '编辑',
				handler : function() {
					var f = p.find('form');
					f.form('submit', {
						url : '${pageContext.request.contextPath}/jxc/rylxAction!edit.action',
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
								rylx_dg.datagrid('reload');
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
					rylxmc: rows[0].rylxmc,
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
function removeRylx(){
	var rows = rylx_dg.datagrid('getSelections');
	if (rows.length > 0) {
		$.messager.confirm('请确认', '您要删除当前所选项目？', function(r) {
			if (r) {				
				$.ajax({
					url : '${pageContext.request.contextPath}/jxc/rylxAction!delete.action',
					data : {
						id : rows[0].id,
						depId:did,
						menuId:mid,	
					},
					dataType : 'json',
					success : function(d) {
						rylx_dg.datagrid('load');
						rylx_dg.datagrid('unselectAll');
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
<div id="jxc_rylx_selet" class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center'">
		<div id="rylx_gl_datagrid"></div>
	</div>
</div>
<div id='jxc_rylx_addDate'></div>

