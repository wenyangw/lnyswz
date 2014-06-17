<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<script type="text/javascript">
var kh_did;
var kh_menuId;

var kh_dg;
var khDet_dg;
$(function(){
	kh_did = lnyw.tab_options().did;
	kh_menuId = lnyw.tab_options().id;
	
	$('#jxc_kh_layout').layout({
		fit : true,
		border : false,
	});
	
	kh_dg = $('#jxc_kh_dg').datagrid({
		url : '${pageContext.request.contextPath}/jxc/khAction!datagrid.action',
		fit : true,
	    border : false,
	    singleSelect : true,
	    fitColumns: true,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
		columns:[[
	        {field:'khbh',title:'客户编号'},
	        {field:'khmc',title:'客户名称'},
	    ]],
	});
	//根据权限，动态加载功能按钮
	lnyw.toolbar(0, kh_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', kh_did);
});

function appendKhDet() {
	var kh_row = $('#jxc_khdet_khdwDg').datagrid('getSelections');
	if(khdw_row.length == 1){
		var addDialog = $('#jxc_kh_addDialog');
		addDialog.dialog({
			title : '增加商品段位',
			href : '${pageContext.request.contextPath}/jxc/khAdd.jkh',
			width : 340,
			height : 420,
			modal : true,
			buttons: [{
	            text:'确定',
	            iconCls:'icon-ok',
	            handler:function(){
	            	var addForm = $('#jxc_khAdd_form');
	            	addForm.form('submit',{
	            		url:'${pageContext.request.contextPath}/jxc/khAction!add.action',
	            		onSubmit:function(){
	            			if($(this).form('validate')){
	            				var flag = true;
	            				$.ajax({
	            					url: '${pageContext.request.contextPath}/jxc/khAction!existSp.action',
	            					async: false,
	            					data : {
	            						khbh : $('#khbh').val(),
	            					},
	            					dataType : 'json',
	    							success : function(d) {
	    								if(!d.success){
	    									flag = false;
	    								}
	    							},
	            				});
	            				if(!flag){
	            					$.messager.alert('提示', '商品编号已存在！', 'error');
	            				}
	            				return flag;
	            			}else{
	            				return false;
	            			}
	            		},
	            		success:function(d){
	            			var json = $.parseJSON(d);
	            			if(json.success){
	            				kh_dg.datagrid('appendRow', json.obj);
	            				addDialog.dialog('close');
	            			}
	            			$.messager.show({
	            				title : '提示',
	            				msg : json.msg
	            			});
	            		},
	            	});
	            }
	        }],
	        onLoad : function() {
				var f = addDialog.find('form');
				var zjldwId = f.find('input[name=zjldwId]');
				var cjldwId = f.find('input[name=cjldwId]');
 				var zjldwCombo = zjldwId.combobox({
					url: '${pageContext.request.contextPath}/jxc/jldwAction!listJldw.action',
					valueField:'id',
				    textField:'jldwmc'
				});
 				var cjldwCombo = cjldwId.combobox({
					url: '${pageContext.request.contextPath}/jxc/jldwAction!listJldw.action',
					valueField:'id',
				    textField:'jldwmc'
				});
				f.form('load', {
					khdwId : khdw_row[0].id,
					khdwmc : khdw_row[0].khdwmc,
					khmc : khdw_row[0].khdwmc,
					khbh : khdw_row[0].id,
					zhxs : 0.00,
					yxq : 0,
					depId : did,
					menuId : menuId,
				});
				$('input[name=khbh]').focus();
			}
		});	
	}else{
		$.messager.alert('提示', '增加商品前请选择商品段位！', 'error');
	}
}

function editSp(){
	var rows = kh_dg.datagrid('getSelections');
	if (rows.length == 1) {
		var editDialog = $('#jxc_kh_addDialog');
		editDialog.dialog({
			title : '修改商品',
			href : '${pageContext.request.contextPath}/jxc/khAdd.jkh',
			width : 340,
			height : 420,
			buttons : [ {
				text : '确定',
				handler : function() {
					var f = editDialog.find('form');
					f.form('submit', {
						url : '${pageContext.request.contextPath}/jxc/khAction!edit.action',
						onSubmit:function(){
						},
						success : function(d) {
							var json = $.parseJSON(d);
							if (json.success) {
								kh_dg.datagrid('reload');
								editDialog.dialog('close');
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
				var f = editDialog.find('form');
				var zjldwId = f.find('input[name=zjldwId]');
				var cjldwId = f.find('input[name=cjldwId]');
 				var zjldwCombo = zjldwId.combobox({
					url: '${pageContext.request.contextPath}/jxc/jldwAction!listJldw.action',
					valueField:'id',
				    textField:'jldwmc'
				});
 				var cjldwCombo = cjldwId.combobox({
					url: '${pageContext.request.contextPath}/jxc/jldwAction!listJldw.action',
					valueField:'id',
				    textField:'jldwmc'
				});
 				var row = rows[0];
 				row["menuId"] = menuId;
 				row["depId"] = did;
				f.form('load', row);
				$('input[name=khbh]').focus();
			}
		});
	} else if (rows.length > 1) {
		$.messager.alert('提示', '同一时间只能编辑一条记录！', 'error');
	} else {
		$.messager.alert('提示', '请选择一条要编辑的记录！', 'error');
	}
}
function removeSp(){
	var rows = kh_dg.datagrid('getSelections');
	if (rows.length == 1) {
		$.messager.confirm('请确认', '您要删除当前所选项目？', function(r) {
			if (r) {
				$.ajax({
					url : '${pageContext.request.contextPath}/jxc/khAction!delete.action',
					data : {
						khbh : rows[0].khbh,
						depId : did,
						menuId : menuId,
					},
					dataType : 'json',
					success : function(d) {
						kh_dg.datagrid('load');
						kh_dg.datagrid('unselectAll');
						$.messager.show({
							title : '提示',
							msg : d.msg
						});
					}
				});
			}
		});
	} else if (rows.length > 1) {
		$.messager.alert('提示', '同一时间只能删除一条记录！', 'error');
	} else {
		$.messager.alert('提示', '请选择一条要删除的记录！', 'error');
	}
}
function editSpDet(){
	var rows = kh_dg.datagrid('getSelections');
	if (rows.length == 1) {
		var detDialog = $('#jxc_kh_addDialog');
		detDialog.dialog({
			title : '修改商品专属信息',
			href : '${pageContext.request.contextPath}/jxc/khDet.jkh',
			width : 340,
			height : 240,
			buttons : [ {
				text : '确定',
				handler : function() {
					var f = detDialog.find('form');
					f.form('submit', {
						url : '${pageContext.request.contextPath}/jxc/khAction!editSpDet.action',
						onSubmit:function(){
						},
						success : function(d) {
							var json = $.parseJSON(d);
							if (json.success) {
								kh_dg.datagrid('reload',{depId: did});
								detDialog.dialog('close');
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
				var f = detDialog.find('form');
				f.form('load', {
					khbh: rows[0].khbh,
					khmc: rows[0].khmc,
					detId: rows[0].detId,
					maxKc: rows[0].maxKc,
					minKc: rows[0].minKc,
					xsdj: rows[0].xsdj,
					limitXsdj: rows[0].limitXsdj,
					depId: did,
					menuId : menuId,
				});
				$('input[name=xsdj]').focus();
			}
		});
	} else if (rows.length > 1) {
		$.messager.alert('提示', '同一时间只能编辑一条记录！', 'error');
	} else {
		$.messager.alert('提示', '请选择一条要编辑的记录！', 'error');
	}
}
function removeSpDet(){
	var rows = kh_dg.datagrid('getSelections');
	if (rows.length == 1) {
		$.messager.confirm('请确认', '您要删除当前所选商品的专属信息？', function(r) {
			if (r) {
				$.ajax({
					url : '${pageContext.request.contextPath}/jxc/khAction!deleteSpDet.action',
					data : {
						khbh : rows[0].khbh,
						detId: rows[0].detId,
						menuId: menuId,
						depId: did,
					},
					dataType : 'json',
					success : function(d) {
						kh_dg.datagrid('load');
						kh_dg.datagrid('unselectAll');
						$.messager.show({
							title : '提示',
							msg : d.msg
						});
					}
				});
			}
		});
	} else if (rows.length > 1) {
		$.messager.alert('提示', '同一时间只能删除一条记录！', 'error');
	} else {
		$.messager.alert('提示', '请选择一条要删除的记录！', 'error');
	}
}
</script>
<div id='jxc_kh_layout' style="height:100%;width=100%">
	<div data-options="region:'west',split:true,collapsible:false" style="width:250px">
		<div id='jxc_kh_west' class="easyui-layout" data-options="fit:true, split:false" style="height:100%; width=100%">
			<div data-options="region:'center',title:'客户列表',split:true" style="height:100px;width:150px">		
				<div id='jxc_kh_Dg'></div>
			</div>
		</div>
	</div>
    <div data-options="region:'center',title:'详细信息',split:true, fit:true" style="height:100px;">
    	<div id='jxc_khdet_dg'></div>
    </div>
</div>
<div id='jxc_kh_addDialog'></div>


	
