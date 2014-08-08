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
	    	khDet_dg.datagrid({
	    		url: '${pageContext.request.contextPath}/jxc/khAction!datagridDet.action',
	 	    	queryParams:{
	 	    		depId: kh_did,
	 	    		khbh: rowData.khbh
	 	    	},
	    	});
	    	//根据权限，动态加载功能按钮
	    	lnyw.toolbar(0, khDet_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', kh_did);
	    	
// 	    	khDet_dg.datagrid('load', {
// 	    		depId: kh_did,
// 	    		khbh: rowData.khbh
// 	    	});
	    },
	});
	
	khDet_dg = $('#jxc_khdet_dg').datagrid({
// 		url : '${pageContext.request.contextPath}/jxc/khAction!datagridDet.action',
// 	    queryParams:{
// 	    	depId: kh_did
// 	    },
		fit : true,
	    border : false,
	    singleSelect : true,
	    fitColumns: false,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
		columns:[[
	        {field:'khbh',width:100,title:'客户编号'},
	        {field:'khmc',width:250,title:'客户名称'},
	        {field:'ywyId',title:'业务员id', hidden:true},
	        {field:'ywyName',width:100,title:'业务员'},
	        {field:'khlxId',title:'客户类型id', hidden:true},
	        {field:'khlxmc',width:100,title:'客户类型'},
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
	        {field:'lsje',title:'历史金额',width:100,align:'right',
				formatter : function(value, rowData, rowIndex) {
		        	if(value==0){
		        		return '';
		        	}else{
		        		return value;
		        	}				
				}},
			{field:'isUp',title:'二级审核', width:50,},
			{field:'postponeDay',title:'限制期', width:50},
	    ]],
	});
	
});

function appendKhDet() {
	var kh_row = $('#jxc_kh_dg').datagrid('getSelected');
	if(kh_row != undefined){
		var ywyId;
		var khlxId;
		var addDialog = $('#jxc_kh_addDialog');
		
		addDialog.dialog({
			title : '增加客户授信信息',
			href : '${pageContext.request.contextPath}/jxc/khDet.jsp',
			width : 340,
			height : 360,
			modal : true,
			buttons: [{
	            text:'确定',
	            iconCls:'icon-ok',
	            handler:function(){
	            	var addForm = addDialog.find('form');
	            	addForm.form('submit',{
	            		url:'${pageContext.request.contextPath}/jxc/khAction!addDet.action',
	            		onSubmit:function(){
	            			if($(this).form('validate')){
	            				var flag = true;
	            				$.ajax({
	            					url: '${pageContext.request.contextPath}/jxc/khAction!existKhDet.action',
	            					async: false,
	            					data : {
	            						khbh : $('#khbh').val(),
	            						ywyId: ywyId.combobox('getValue'),
	            					},
	            					dataType : 'json',
	    							success : function(d) {
	    								if(!d.success){
	    									flag = false;
	    								}
	    							},
	            				});
	            				if(!flag){
	            					$.messager.alert('提示', '客户-业务员信息已存在！', 'error');
	            				}
	            				return flag;
	            			}else{
	            				return false;
	            			}
	            		},
	            		success:function(d){
	            			var json = $.parseJSON(d);
	            			if(json.success){
	            				khDet_dg.datagrid('appendRow', json.obj);
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
				ywyId = $("input[name=ywyId]");
				ywyId.combobox({
				    url:'${pageContext.request.contextPath}/admin/userAction!listYwys.action?did=' + kh_did,
				    valueField:'id',
				    textField:'realName',
				    panelHeight: 'auto',
				});
				khlxId = $("input[name=khlxId]");
				khlxId.combobox({
				    url:'${pageContext.request.contextPath}/jxc/khlxAction!listKhlxs.action',
				    valueField:'id',
				    textField:'khlxmc',
				    panelHeight: 'auto',
				    onSelect: function(){
				    	initForm(khlxId);
					},
				});
				khlxId.combobox('selectedIndex', 0);
				
				f.form('load', {
					khbh: kh_row.khbh,
					khmc: kh_row.khmc,
					depId: kh_did,
					menuId: kh_menuId
				});
				$('input[name=lxr]').focus();
			}
		});
		
		
	}else{
		$.messager.alert('提示', '请选择客户！', 'error');
	}
}

function editKhDet(){
	var row = khDet_dg.datagrid('getSelected');
	if (row != undefined) {
		var ywyId;
		var khlxId;
		var detDialog = $('#jxc_kh_addDialog');
		detDialog.dialog({
			title : '修改客户授信信息',
			href : '${pageContext.request.contextPath}/jxc/khDet.jsp',
			width : 340,
			height : 360,
			buttons : [ {
				text : '确定',
				handler : function() {
					var f = detDialog.find('form');
					f.form('submit', {
						url : '${pageContext.request.contextPath}/jxc/khAction!editDet.action',
						onSubmit:function(){
							if(khlxId.combobox('getValue') != '01' && ywyId.combobox('getValue') == 0){
								$.messager.alert('提示', '请选择业务员！', 'error');
								return false;
							}
						},
						success : function(d) {
							var json = $.parseJSON(d);
							if (json.success) {
								khDet_dg.datagrid('reload', {
									depId: kh_did,
									khbh: kh_dg.datagrid('getSelected') == undefined ? undefined : kh_dg.datagrid('getSelected').khbh 
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
				ywyId = $("input[name=ywyId]");
				ywyId.combobox({
				    url:'${pageContext.request.contextPath}/admin/userAction!listYwys.action?did=' + kh_did,
				    valueField:'id',
				    textField:'realName',
				    panelHeight: 'auto',
				});
				khlxId = $("input[name=khlxId]");
				khlxId.combobox({
				    url:'${pageContext.request.contextPath}/jxc/khlxAction!listKhlxs.action',
				    valueField:'id',
				    textField:'khlxmc',
				    panelHeight: 'auto',
				    onSelect: function(){
						initForm(khlxId);
					}
				});
				if(row['khlxId'] == undefined){
					row['khlxId'] = '01';
				}
				row["depId"] = kh_did;
				row["menuId"] = kh_menuId;
				f.form('load', row);
				initForm(khlxId);
				
			}
		});
		
		
	}else{
		$.messager.alert('提示', '请选择一条要编辑的记录！', 'error');
	}
	
	
}

function removeKhDet(){
	var row = khDet_dg.datagrid('getSelected');
	if (row != undefined) {
		$.messager.confirm('请确认', '您要删除当前所选客户授信信息？', function(r) {
			if (r) {
				$.ajax({
					url : '${pageContext.request.contextPath}/jxc/khAction!deleteDet.action',
					data : {
						//khbh : row.khbh,
						detId: row.detId,
						menuId: kh_menuId,
						depId: kh_did,
					},
					dataType : 'json',
					success : function(d) {
						khDet_dg.datagrid('load');
						khDet_dg.datagrid('unselectAll');
						$.messager.show({
							title : '提示',
							msg : d.msg
						});
					}
				});
			}
		});
	} else {
		$.messager.alert('提示', '请选择一条要删除的记录！', 'error');
	}
}

function initForm(target){
	var value = $(target).combobox('getValue');
	if(value != '01'){
		$('input[name=sxzq]').removeAttr('disabled');
		$('input[name=sxje]').removeAttr('disabled');
		$('input[name=lsje]').removeAttr('disabled');
		$('input[name=isUp]').removeAttr('disabled');
		$('input[name=postponeDay]').removeAttr('disabled');
		if($('input[name=postponeDay]').val() == ''){
			$('input[name=postponeDay]').val('60');
		}
	}else{
		$('input#sxzq').attr('disabled','disabled');
		$('input[name=sxje]').attr('disabled','disabled');
		$('input[name=lsje]').attr('disabled','disabled');
		$('input[name=isUp]').attr('disabled','disabled');
		$('input[name=postponeDay]').attr('disabled','disabled');
	}
};



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


	
