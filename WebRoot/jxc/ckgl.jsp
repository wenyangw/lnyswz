<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<script type="text/javascript">
var ck_dg;
var depCombo;
var depId;
var mid;
var p;
$(function(){
 	p = $('#jxc_ck_addDate');
	depId = lnyw.tab_options().did;
	mid = lnyw.tab_options().id;
	ck_dg = $('#ck_gl_datagrid');
	ck_dg.datagrid({
		url:'${pageContext.request.contextPath}/jxc/ckAction!datagrid.action',
		fit : true,
		singleSelect:true,
	    border : false,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
	    columns:[[    	
	        {field:'id',title:'编号',width:100},	       
	        {field: 'ckmc',title:'仓库名称',width:100},
	        {field:'did',title:'部门',width:100, hidden:true},
	        {field:'dname',title:'所属部门',width:100},
	    ]],
	});
	//根据权限，动态加载功能按钮
	lnyw.toolbar(0, ck_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', depId);
});
function appendCkgl() {
	p.dialog({
		title : '增加仓库管理',
		href : '${pageContext.request.contextPath}/jxc/ckAdd.jsp',
		width : 340,
		height : 200,
		modal : true,
		buttons: [{
            text:'增加',
            iconCls:'icon-ok',
            handler:function(){
            	$('#jxc_ckAdd_form').form('submit', {
					url : '${pageContext.request.contextPath}/jxc/ckAction!add.action',
					success : function(d) {
						var json = $.parseJSON(d);
						if (json.success) {
							ck_dg.datagrid('appendRow', json.obj);
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
					depId:depId,
					menuId:mid,			
				});
				var did = f.find('input[name=did]');
        	    var dnamedepCombo = did.combobox({
				    url:'${pageContext.request.contextPath}/admin/departmentAction!listDeps.action',
				    valueField:'id',
				    textField:'depName'
				});
				f.find('input[name=id]').focus();
		}
	});
}

function editCkgl(){
	var rows = ck_dg.datagrid('getSelections');
	if(rows.length > 0){	
		p.dialog({
			title : '编辑仓库管理',
			href : '${pageContext.request.contextPath}/jxc/ckEdit.jsp',
			width : 350,
			height : 200,
			buttons : [ {
				text : '编辑',
				handler : function() {
					var f = p.find('form');
					f.form('submit', {
						url : '${pageContext.request.contextPath}/jxc/ckAction!edit.action',
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
								ck_dg.datagrid('reload');
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
				var did = f.find('input[name=did]');
	        	var dnamedepCombo = did.combobox({
				    url:'${pageContext.request.contextPath}/admin/departmentAction!listDeps.action',
				    valueField:'id',
				    textField:'depName'
				});
				f.form('load', {
					id : rows[0].id,
					ckmc:rows[0].ckmc,
					did:rows[0].did,
					dname:rows[0].dname,
					depId:depId,
					menuId:mid,
					});
				f.find('input[name=id]').focus();
			
			}
		});	
		}else{
			$.messager.alert('警告', '请选择一条记录进行修改！',  'warning');
		}
}
function removeCkgl(){
	var rows = ck_dg.datagrid('getSelections');
	if (rows.length > 0) {
		$.messager.confirm('请确认', '您要删除当前所选项目？', function(r) {
			if (r) {
				
				$.ajax({
					url : '${pageContext.request.contextPath}/jxc/ckAction!delete.action',
					data : {
						id : rows[0].id,
						depId:depId,
						menuId:mid,
					},
					dataType : 'json',
					success : function(d) {
						ck_dg.datagrid('load');
						ck_dg.datagrid('unselectAll');
						if(!d.success){
							$.messager.alert('提示', '对不起，该信息被使用中!', 'error');
						}
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
		<div id="jxc_ck_selet" class="easyui-layout" data-options="fit:true">
			
			<div data-options="region:'center'">
				<div id="ck_gl_datagrid"></div>
			</div>
		</div>


		<div id='jxc_ck_addDate'></div>

	</body>
</html>