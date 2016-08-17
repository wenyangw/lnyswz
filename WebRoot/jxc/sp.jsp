<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<script type="text/javascript">
var sp_dg;
var did;
var menuId;
var spdwId;
$(function(){
	did = lnyw.tab_options().did;
	menuId = lnyw.tab_options().id;
	
	$('#jxc_sp_layout').layout({
		fit : true,
		border : false,
	});
	
	$('#jxc_sp_spfl').tree({
		url:'${pageContext.request.contextPath}/jxc/spdwAction!listSpfl.action?depId=' + did,
		onClick : function(node) {
			if(node.attributes){
				$('#jxc_sp_spdwDg').datagrid('load', {
					splbId : node.id,
					depId : did,
				});
			}
		}
		
	});
	
	$('#jxc_sp_spdwDg').datagrid({
		url : '${pageContext.request.contextPath}/jxc/spdwAction!datagrid.action',
		fit : true,
	    border : false,
	    singleSelect : true,
	    fitColumns: true,
	    queryParams: {
	    	depId : did,
	    },
		columns:[[
	        {field:'id',title:'编号',width:100},
	        {field:'spdwmc',title:'名称',width:100},
	    ]],
	    onSelect:function(rowIndex, rowData){
	    	spdwId = rowData.id;
	    	sp_dg.datagrid('load', {
	    		spdwId : spdwId,
	    		depId: did,
	    	});
	    },
	});
	
	
	sp_dg = $('#jxc_sp_dg').datagrid({
		url : '${pageContext.request.contextPath}/jxc/spAction!datagrid.action',
		fit : true,
	    border : false,
	    singleSelect : true,
	    fitColumns: true,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
		queryParams: {
			depId: did,
		},
		columns:[[
			{title:'通用信息',colspan:9},
			{title:'专属信息',colspan:4},
			],[
	        {field:'spbh',title:'商品编号'},
	        {field:'spdwId',title:'商品段位Id',hidden:true},
	        {field:'spdwmc',title:'商品段位名称',hidden:true},
	        {field:'spmc',title:'商品名称'},
	        {field:'spcd',title:'商品产地'},
	        {field:'sppp',title:'商品品牌'},
	        {field:'spbz',title:'商品包装'},
	        {field:'zjldwId',title:'主计量单位id',hidden:true},
	        {field:'zjldwmc',title:'主计量单位'},
	        {field:'cjldwId',title:'次计量单位id',hidden:true},
	        {field:'cjldwmc',title:'次计量单位'},
	        {field:'zhxs',title:'系数'},
	        {field:'yxq',title:'有效期'},
	        {field:'xsdj',title:'销售单价'},
	        {field:'specXsdj',title:'特定销价'},
			{field:'limitXsdj',title:'最低销价'},
			{field:'maxKc',title:'最大库存'},
			{field:'minKc',title:'最小库存'},
			{field:'depId',title:'部门id',hidden:true},
	        
	    ]],
		
	});
	//根据权限，动态加载功能按钮
	lnyw.toolbar(0, sp_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', did);
	
	
});

function appendSp() {
	var spdw_row = $('#jxc_sp_spdwDg').datagrid('getSelections');
	if(spdw_row.length == 1){
		var addDialog = $('#jxc_sp_addDialog');
		addDialog.dialog({
			title : '增加商品段位',
			href : '${pageContext.request.contextPath}/jxc/spAdd.jsp',
			width : 340,
			height : 420,
			modal : true,
			buttons: [{
	            text:'确定',
	            iconCls:'icon-ok',
	            handler:function(){
	            	var addForm = $('#jxc_spAdd_form');
	            	addForm.form('submit',{
	            		url:'${pageContext.request.contextPath}/jxc/spAction!add.action',
	            		onSubmit:function(){
	            			if($(this).form('validate')){
	            				var flag = true;
	            				$.ajax({
	            					url: '${pageContext.request.contextPath}/jxc/spAction!existSp.action',
	            					async: false,
	            					data : {
	            						spbh : $('#spbh').val(),
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
	            			var json = $.parseJSON(jxc.toJson(d));
	            			if(json.success){
	            				sp_dg.datagrid('appendRow', json.obj);
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
					spdwId : spdw_row[0].id,
					spdwmc : spdw_row[0].spdwmc,
					spmc : spdw_row[0].spdwmc,
					spbh : spdw_row[0].id,
					zhxs : 0.00,
					yxq : 0,
					depId : did,
					menuId : menuId,
				});
				$('input[name=spbh]').focus();
			}
		});	
	}else{
		$.messager.alert('提示', '增加商品前请选择商品段位！', 'error');
	}
}

function editSp(){
	var rows = sp_dg.datagrid('getSelections');
	if (rows.length == 1) {
		var editDialog = $('#jxc_sp_addDialog');
		editDialog.dialog({
			title : '修改商品',
			href : '${pageContext.request.contextPath}/jxc/spAdd.jsp',
			width : 340,
			height : 420,
			buttons : [ {
				text : '确定',
				handler : function() {
					var f = editDialog.find('form');
					f.form('submit', {
						url : '${pageContext.request.contextPath}/jxc/spAction!edit.action',
						onSubmit:function(){
						},
						success : function(d) {
							var json = $.parseJSON(jxc.toJson(d));
							if (json.success) {
								sp_dg.datagrid('reload');
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
				$('input[name=spbh]').focus();
			}
		});
	} else if (rows.length > 1) {
		$.messager.alert('提示', '同一时间只能编辑一条记录！', 'error');
	} else {
		$.messager.alert('提示', '请选择一条要编辑的记录！', 'error');
	}
}
function removeSp(){
	var rows = sp_dg.datagrid('getSelections');
	if (rows.length == 1) {
		$.messager.confirm('请确认', '您要删除当前所选项目？', function(r) {
			if (r) {
				$.ajax({
					url : '${pageContext.request.contextPath}/jxc/spAction!delete.action',
					data : {
						spbh : rows[0].spbh,
						depId : did,
						menuId : menuId,
					},
					dataType : 'json',
					success : function(d) {
						sp_dg.datagrid('load');
						sp_dg.datagrid('unselectAll');
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
	var rows = sp_dg.datagrid('getSelections');
	if (rows.length == 1) {
		var detDialog = $('#jxc_sp_addDialog');
		detDialog.dialog({
			title : '修改商品专属信息',
			href : '${pageContext.request.contextPath}/jxc/spDet.jsp',
			width : 340,
			height : 300,
			buttons : [ {
				text : '确定',
				handler : function() {
					var f = detDialog.find('form');
					f.form('submit', {
						url : '${pageContext.request.contextPath}/jxc/spAction!editSpDet.action',
						onSubmit:function(){
						},
						success : function(d) {
							var json = $.parseJSON(jxc.toJson(d));
							if (json.success) {
								sp_dg.datagrid('reload');
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
					spbh: rows[0].spbh,
					spmc: rows[0].spmc,
					detId: rows[0].detId,
					maxKc: rows[0].maxKc,
					minKc: rows[0].minKc,
					xsdj: rows[0].xsdj,
					specXsdj: rows[0].specXsdj,
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
	var rows = sp_dg.datagrid('getSelections');
	if (rows.length == 1) {
		$.messager.confirm('请确认', '您要删除当前所选商品的专属信息？', function(r) {
			if (r) {
				$.ajax({
					url : '${pageContext.request.contextPath}/jxc/spAction!deleteSpDet.action',
					data : {
						spbh : rows[0].spbh,
						detId: rows[0].detId,
						menuId: menuId,
						depId: did,
					},
					dataType : 'json',
					success : function(d) {
						sp_dg.datagrid('load');
						sp_dg.datagrid('unselectAll');
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
<div id='jxc_sp_layout' style="height:100%;width=100%">
	<div data-options="region:'west',split:true,collapsible:false" style="width:250px">
		<div id='jxc_sp_west' class="easyui-layout" data-options="fit:true, split:false" style="height:100%;width=100%">
			<div data-options="region:'north',title:'商品分类',split:true,collapsible:false" style="height:180px">		
				<ul id="jxc_sp_spfl"></ul>
			</div>
			<div data-options="region:'center',title:'商品段位',split:true" style="height:100px;width:150px">		
				<div id='jxc_sp_spdwDg'></div>
			</div>
		</div>
	</div>
    <div data-options="region:'center',title:'商品列表',split:true, fit:true" style="height:100px;">
    	<div id='jxc_sp_dg'></div>
    </div>
</div>
<div id='jxc_sp_addDialog'></div>


	
