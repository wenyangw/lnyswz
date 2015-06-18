<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%-- <%@ include file="/common/page.jsp"%> --%>
<script type="text/javascript">
var fplx_dg;
var did;
var mid;
$(function(){
	fplx_dg = $('#fplx_gl_datagrid');
	did = lnyw.tab_options().did;
	mid = lnyw.tab_options().id;
	fplx_dg.datagrid({
		url:'${pageContext.request.contextPath}/jxc/fplxAction!datagrid.action',
		fit : true,
		singleSelect:true,
	    border : false,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
	    columns:[[
	        {field:'id',title:'编号',width:100},
	        {field: 'fplxmc',title:'发票类型名称',width:100},	        
        ]],
	});
	//根据权限，动态加载功能按钮
	lnyw.toolbar(0, fplx_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', did);
});
function appendFplx() {
	var p = $('#jxc_fplx_addDate');
	p.dialog({
		title : '增加发票类型',
		href : '${pageContext.request.contextPath}/jxc/fplxAdd.jsp',
		width : 340,
		height : 150,
		modal : true,
		buttons: [{
            text:'增加',
            iconCls:'icon-ok',
            handler:function(){
            	$('#jxc_fplxAdd_form').form('submit', {
					url : '${pageContext.request.contextPath}/jxc/fplxAction!add.action',
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
							fplx_dg.datagrid('appendRow', json.obj);
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
function editFplx(){
	var rows = fplx_dg.datagrid('getSelections');
	if(rows.length > 0){	
		var p = $('#jxc_fplx_addDate');
		p.dialog({
			title : '编辑发票类型',
			href : '${pageContext.request.contextPath}/jxc/fplxEdit.jsp',
			width : 350,
			height : 150,
			buttons : [ {
				text : '编辑',
				handler : function() {
					var f = p.find('form');
					f.form('submit', {
						url : '${pageContext.request.contextPath}/jxc/fplxAction!edit.action',
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
								fplx_dg.datagrid('reload');
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
					fplxmc: rows[0].fplxmc,
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
function removeFplx(){
	var rows = fplx_dg.datagrid('getSelections');
	if (rows.length > 0) {
		$.messager.confirm('请确认', '您要删除当前所选项目？', function(r) {
			if (r) {	
				$.ajax({
					url : '${pageContext.request.contextPath}/jxc/fplxAction!delete.action',
					data : {
						id : rows[0].id,
						depId:did,
						menuId:mid,		
					},
					dataType : 'json',
					success : function(d) {
						fplx_dg.datagrid('load');
						fplx_dg.datagrid('unselectAll');
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
		<div id="jxc_fplx_selet" class="easyui-layout" data-options="fit:true">			
			<div data-options="region:'center'">
				<div id="fplx_gl_datagrid"></div>
			</div>
		</div>
		<div id='jxc_fplx_addDate'></div>

