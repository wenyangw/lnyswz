<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<script type="text/javascript">
var shdz_dg;
var did;
var mid;
$(function(){
	shdz_dg = $('#shdz_gl_datagrid');
	did = lnyw.tab_options().did;
	mid = lnyw.tab_options().id;
	shdz_dg.datagrid({
		url:'${pageContext.request.contextPath}/jxc/shdzAction!datagrid.action',
		fit : true,
		singleSelect:true,
	    border : false,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
	    columns:[[	    	
	        {field:'id',title:'编号',width:100, checkbox:true},	       
	        {field: 'khmc',title:'名称',width:100},	        
	        {field: 'khdz',title:'地址',width:100},	        
	        {field: 'lxr',title:'联系人',width:100},	        
	        {field: 'phone',title:'电话',width:100},	                
	     ]],
	});
	//根据权限，动态加载功能按钮
	lnyw.toolbar(0, shdz_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', did);
});
function appendShdz() {
	var p = $('#jxc_shdz_addDate');
	p.dialog({
		title : '增加送货地址',
		href : '${pageContext.request.contextPath}/jxc/shdzAdd.jsp',
		width : 340,
		height : 210,
		modal : true,
		buttons: [{
            text:'增加',
            iconCls:'icon-ok',
            handler:function(){
            	$('#jxc_shdzAdd_form').form('submit', {
					url : '${pageContext.request.contextPath}/jxc/shdzAction!add.action',
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
							shdz_dg.datagrid('appendRow', json.obj);
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

function editShdz(){
	var rows = shdz_dg.datagrid('getSelections');
	if(rows.length > 0){	
		var p = $('#jxc_shdz_addDate');
		p.dialog({
			title : '编辑地址',
			href : '${pageContext.request.contextPath}/jxc/shdzEdit.jsp',
			width : 350,
			height : 210,
			buttons : [ {
				text : '编辑',
				handler : function() {
					var f = p.find('form');
					f.form('submit', {
						url : '${pageContext.request.contextPath}/jxc/shdzAction!edit.action',
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
								shdz_dg.datagrid('reload');
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
					khmc: rows[0].khmc,
					khdz: rows[0].khdz,
					lxr: rows[0].lxr,
					phone: rows[0].phone,
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
function removeShdz(){
	var rows = shdz_dg.datagrid('getSelections');
	if (rows.length > 0) {
		$.messager.confirm('请确认', '您要删除当前所选项目？', function(r) {
			if (r) {
				$.ajax({
					url : '${pageContext.request.contextPath}/jxc/shdzAction!delete.action',
					data : {
						id : rows[0].id,
						depId:did,
						menuId:mid,	
					},
					dataType : 'json',
					success : function(d) {
						shdz_dg.datagrid('load');
						shdz_dg.datagrid('unselectAll');
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
<div id="jxc_shdz_selet" class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center'">
		<div id="shdz_gl_datagrid"></div>
	</div>
</div>
<div id='jxc_shdz_addDate'></div>
