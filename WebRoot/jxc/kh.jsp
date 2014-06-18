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
	    toolbar:'#jxc_kh_tb',
	    onClickRow:function(rowIndex, rowData){
	    	khDet_dg.datagrid('load', {
	    		depId: kh_did,
	    		khbh: rowData.khbh
	    	});
	    },
	});
	
	khDet_dg = $('#jxc_khdet_dg').datagrid({
		url : '${pageContext.request.contextPath}/jxc/khAction!datagridDet.action',
		fit : true,
	    border : false,
	    singleSelect : true,
	    fitColumns: false,
	    queryParams:{
	    	depId: kh_did
	    },
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
		columns:[[
	        {field:'khbh',title:'客户编号'},
	        {field:'khmc',title:'客户名称'},
	        {field:'ywyId',title:'业务员id', hidden:true},
	        {field:'ywyName',title:'业务员'},
	        {field:'khlxId',title:'客户类型id', hidden:true},
	        {field:'khlxmc',title:'客户类型'},
	        {field:'sxzq',title:'授信账期(天)',
	        	formatter : function(value, rowData, rowIndex) {
		        	if(value==0){
		        		return '';
		        	}else{
		        		return value;
		        	}				
				}},
	        {field:'sxje',title:'授信金额(元)',width:100,align:'right',
				formatter : function(value, rowData, rowIndex) {
		        	if(value==0){
		        		return '';
		        	}else{
		        		return value;
		        	}				
				}},
	        {field:'yfje',title:'历史金额',width:100,align:'right',
				formatter : function(value, rowData, rowIndex) {
		        	if(value==0){
		        		return '';
		        	}else{
		        		return value;
		        	}				
				}},
	    ]],
	});
	//根据权限，动态加载功能按钮
	lnyw.toolbar(0, khDet_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', kh_did);
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

function editKhDet(){
	var row = khDet_dg.datagrid('getSelected');
	if (row != undefined) {
		var detDialog = $('#jxc_kh_addDialog');
		detDialog.dialog({
			title : '修改客户授信信息',
			href : '${pageContext.request.contextPath}/jxc/khDet.jsp',
			width : 340,
			height : 280,
			buttons : [ {
				text : '确定',
				handler : function() {
					var f = detDialog.find('form');
					f.form('submit', {
						url : '${pageContext.request.contextPath}/jxc/khAction!editKhDet.action',
						onSubmit:function(){
						},
						success : function(d) {
							var json = $.parseJSON(d);
							if (json.success) {
								khDet_dg.datagrid('reload', {
									depId: did,
									khbh: kh_dg.datagrid('getSelected').khbh 
									});
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
				var ywyId = $("input[name=ywyId]");
				var ywyCombo = ywyId.combobox({
				    url:'${pageContext.request.contextPath}/admin/userAction!listYwys.action?did=' + kh_did,
				    valueField:'id',
				    textField:'realName',
				    panelHeight: 'auto',
				});
				var khlxId = $("input[name=khlxId]");
				var khlxCombo = khlxId.combobox({
				    url:'${pageContext.request.contextPath}/jxc/khlxAction!listKhlxs.action',
				    valueField:'id',
				    textField:'khlxmc',
				    panelHeight: 'auto',
				});
				if(row['khlxId'] == undefined){
					row['khlxId'] = '01';
				}
				row["depId"] = kh_did;
				row["menuId"] = kh_menuId;
				f.form('load', row);
				initForm(khlxId.combobox('getValue'));
				khlxId.combobox({
					onSelect: function(){
						initForm(khlxId.combobox('getValue'));
					}
				});
			}
		});
	}else{
		$.messager.alert('提示', '请选择一条要编辑的记录！', 'error');
	}
	
	var initForm = function(value){
		if(value != '01'){
			$('form input[name=sxzq]').removeAttr('disabled');
			$('form input[name=sxje]').removeAttr('disabled');
			$('form input[name=yfje]').removeAttr('disabled');
		}else{
			$('form input[name=sxzq]').attr('disabled','disabled');
			$('form input[name=sxje]').attr('disabled','disabled');
			$('form input[name=yfje]').attr('disabled','disabled');
		}
	};
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

function searchKh(){
	kh_dg.datagrid('load',{
		khcs: $('input[name=search]').val(),
	});
}
</script>
<div id='jxc_kh_layout' style="height:100%;width=100%">
	<div data-options="region:'west',split:true,collapsible:false" style="width:320px">
		<div id='jxc_kh_west' class="easyui-layout" data-options="fit:true, split:false" style="height:100%; width=100%">
			请输入查询内容
			<div data-options="region:'center',title:'客户列表',split:true" style="height:100px;width:250px">		
				<div id='jxc_kh_dg'></div>
			</div>
		</div>
	</div>
    <div data-options="region:'center',title:'详细信息',split:true, fit:true" style="height:100px;">
    	<div id='jxc_khdet_dg'></div>
    </div>
</div>
<div id="jxc_kh_tb" style="padding:3px;height:auto">
	输入编号、名称：<input type="text" name="search" style="width:100px">
	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchKh();">查询</a>
</div>
<div id='jxc_kh_addDialog'></div>


	
