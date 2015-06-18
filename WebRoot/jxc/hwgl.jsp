<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<script type="text/javascript">
var hw_dg;
var ckCombo;
var did;
var mid;
$(function(){
	did = lnyw.tab_options().did;
	mid = lnyw.tab_options().id;
	hw_dg = $('#hw_gl_datagrid');
	hw_dg.datagrid({
		url:'${pageContext.request.contextPath}/jxc/hwAction!datagrid.action',
		fit : true,
		singleSelect:true,
	    border : false,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
	    columns:[[    	
	        {field:'id',title:'编号',width:100},	       
	        {field: 'hwmc',title:'货位名称',width:100},
	        {field:'ckId',title:'仓库id',width:100, hidden:true},
	        {field:'ckmc',title:'所属仓库',width:100},
	    ]],
	});
	//根据权限，动态加载功能按钮
	lnyw.toolbar(0, hw_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', did);
});
function appendHw() {
	var p = $('#jxc_hw_addDate');
	p.dialog({
		title : '增加货位管理',
		href : '${pageContext.request.contextPath}/jxc/hwAdd.jsp',
		width : 340,
		height : 200,
		modal : true,
		buttons: [{
            text:'增加',
            iconCls:'icon-ok',
            handler:function(){
            	$('#jxc_hwAdd_form').form('submit', {
					url : '${pageContext.request.contextPath}/jxc/hwAction!add.action',
					success : function(d) {				
						var json = $.parseJSON(d);
						if (json.success) {
							hw_dg.datagrid('appendRow', json.obj);
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
				var ckid = f.find('input[name=ckId]');
        	    var ckhwCombo = ckid.combobox({
			    	url:'${pageContext.request.contextPath}/jxc/ckAction!listCk.action?depId=' + did,
			    	valueField:'id',
			   		textField:'ckmc'
				});
        	    f.form('load', {
					depId:did,
					menuId:mid,			
				});				
				f.find('input[name=id]').focus();
		}
	});
}

function editHw(){
	var rows = hw_dg.datagrid('getSelections');
	if(rows.length > 0){	
		var p = $('#jxc_hw_addDate');
		p.dialog({
			title : '编辑货位管理',
			href : '${pageContext.request.contextPath}/jxc/hwEdit.jsp',
			width : 350,
			height : 200,
			buttons : [ {
				text : '编辑',
				handler : function() {
					var f = p.find('form');
					f.form('submit', {
						url : '${pageContext.request.contextPath}/jxc/hwAction!edit.action',
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
								hw_dg.datagrid('reload');
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
				var ckid = f.find('input[name=ckId]');
	        	var ckhwCombo = ckid.combobox({
				    url:'${pageContext.request.contextPath}/jxc/ckAction!listCk.action',
				    valueField:'id',
				    textField:'ckmc'
				});
	        	f.form('load', {
					id : rows[0].id,
					hwmc:rows[0].hwmc,
					ckId:rows[0].ckId,
					ckmc:rows[0].ckmc,
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
function removeHw(){
	var rows = hw_dg.datagrid('getSelections');
	if (rows.length > 0) {
		$.messager.confirm('请确认', '您要删除当前所选项目？', function(r) {
			if (r) {				
				$.ajax({
					url : '${pageContext.request.contextPath}/jxc/hwAction!delete.action',
					data : {
						id : rows[0].id,
						depId:did,
						menuId:mid,	
					},
					dataType : 'json',
					success : function(d) {
						hw_dg.datagrid('load');
						hw_dg.datagrid('unselectAll');
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
<div id="jxc_hw_selet" class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center'">
		<div id="hw_gl_datagrid"></div>
	</div>
</div>
<div id='jxc_hw_addDate'></div>
