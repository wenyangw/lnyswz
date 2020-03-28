<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
	
<script type="text/javascript">
var did;
var mid;
$(function(){
	did = lnyw.tab_options().did;
	mid = lnyw.tab_options().id;	
	gys_dg = $('#jxc_gys_datagrid');
	gys_dg.datagrid({
		url:'${pageContext.request.contextPath}/jxc/gysAction!datagrid.action',
		fit : true,
		singleSelect:true,
	    queryParams: {depId: did},
		border : false,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
	    columns:[[
	    	{title:'通用信息',colspan:6},
			{title:'专属信息',colspan:3},
			],[
	        {field: 'gysbh',title:'供应商编号',width:100},
	        {field:'gysmc',title:'供应商名称',width:100,
	        	formatter: function(value){
        			return lnyw.memo(value, 15);
        		}},	      	
	        {field:'dzdh',title:'地址/电话',width:100},
	        {field:'khh',title:'开户行',width:100},
	        {field:'sh',title:'税号',	width:100},
	        {field:'fr',title:'法人代表',width:100},
	        {field:'lxr',title:'业务联系人',width:100},
	        {field:'did',title:'部门id',width:100, hidden:true},
	        {field:'sxzq',title:'授信账期（天）',width:100,
	        	formatter : function(value, rowData, rowIndex) {
	        		if(value==0){
	        			return '';
	        		}else{
		        		return value;
		        	}				
				}},
	        {field:'sxje',title:'授信金额（元）',width:100,align:'right',
				formatter : function(value, rowData, rowIndex) {
	        		if(value==0){
	        			return '';
	        		}else{
	        			return value;
	        		}				
				}}
	    ]],
	});
	//根据权限，动态加载功能按钮
	lnyw.toolbar(0, gys_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', did);
});
function appendGys() {
	var p = $('#jxc_gys_addDialog');
	p.dialog({
		title : '增加供应商',
		href : '${pageContext.request.contextPath}/jxc/gysAdd.jsp',
		width : 340,
		height : 350,
		modal : true,
		buttons: [{
            text:'增加',
            iconCls:'icon-ok',
            handler:function(){
            	$('#jxc_gysAdd_form').form('submit', {
					url : '${pageContext.request.contextPath}/jxc/gysAction!add.action',
					onSubmit: function(){	
						if($(this).form('validate')){
							var flag=true;
							$.ajax({
								url : '${pageContext.request.contextPath}/jxc/gysAction!existGys.action',
								async: false,
								data : {
									gysbh: $('input[name=gysbh]').val()
								},
								dataType : 'json',
								success : function(d) {
									if(!d.success){								
										flag=false;
									}								
								}								
							});	
							if(!flag){
	            					$.messager.alert('提示', '供应商编号已存在！', 'error');
	           				}
            				return flag;
            			}else{
            				return false;
            			}
					},
					success : function(d) {
						var json = $.parseJSON(d);
						if (json.success) {
							gys_dg.datagrid('appendRow', json.obj);
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
				sxzq :0,
				sxje :0,
				depId:did,
				menuId:mid,			
			});			
			f.find('input[name=id]').focus();
		}
	});
}

function editGys(){
	var rows = gys_dg.datagrid('getSelections');
	if(rows.length > 0){	
		var p = $('#jxc_gys_addDialog');
		p.dialog({
			title : '编辑供应商',
			href : '${pageContext.request.contextPath}/jxc/gysEdit.jsp',
			width : 350,
			height : 350,
			buttons : [ {
				text : '编辑',
				handler : function() {
					var f = p.find('form');
					f.form('submit', {
						url : '${pageContext.request.contextPath}/jxc/gysAction!edit.action',
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
								gys_dg.datagrid('reload');
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
					gysmc: rows[0].gysmc,
					gysbh : rows[0].gysbh,
					dzdh: rows[0].dzdh,
					khh:rows[0].khh,
					sh:rows[0].sh,
					fr:rows[0].fr,
					depId:did,
					menuId:mid,
				});
				f.find('input[name=gysbh]').focus();
			}
		});
	}else{
		$.messager.alert('警告', '请选择一条记录进行修改！',  'warning');
	}
}
function removeGys(){
	var row = gys_dg.datagrid('getSelected');
	if (row != undefined) {
		$.messager.confirm('请确认', '您要删除当前所选项目？', function(r) {
			if (r) {
				$.ajax({
					url : '${pageContext.request.contextPath}/jxc/gysAction!delete.action',
					async: false,
					data : {
						gysbh: row.gysbh,
						depId: did,
						menuId:mid,
					},
					dataType :'json',
					success : function(d) {
						if(!d.success){
							$.messager.alert('警告', '对不起！此供应商被其他部门占用！',  'warning');
						}
						gys_dg.datagrid('load');
						gys_dg.datagrid('unselectAll');
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
function editGysDet(){
	if(did != undefined){
		var rows = gys_dg.datagrid('getSelections');
		if(rows.length > 0){	
			var p = $('#jxc_gys_addDialog');
			p.dialog({
				title : '编辑供应商专属信息',
				href : '${pageContext.request.contextPath}/jxc/gysDet.jsp',
				width : 350,
				height : 220,
				buttons : [ {
					text : '编辑',
					handler : function() {
						var f = p.find('form');
						f.form('submit', {
							url : '${pageContext.request.contextPath}/jxc/gysAction!editDet.action',
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
									gys_dg.datagrid('reload');
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
					var sxje=rows[0].sxje;
					if(sxje===undefined){
						sxje=0;
					}
					f.form('load', {
						gysmc: rows[0].gysmc,
						gysbh : rows[0].gysbh,
						lxr : rows[0].lxr,
						sxzq : rows[0].sxzq,
						sxje : sxje,
						detId: rows[0].detId,
						depId: did,
						menuId:mid,
					});
					f.find('input[name=lxr]').focus();
				}
			});
		}else{
			$.messager.alert('警告', '请选择一条记录进行修改！',  'warning');
		}
	}else{
		$.messager.alert('警告', '操作者必须归属于某个部门！',  'warning');
	}
}

function removeGysDet(){
	if(did != undefined){
		var row = gys_dg.datagrid('getSelected');
		if (row != undefined) {
			$.messager.confirm('请确认', '您要删除当前所选项目？', function(r) {
				if (r) {
					
					$.ajax({
						url : '${pageContext.request.contextPath}/jxc/gysAction!deleteDet.action',
						data : {
							gysbh : row.gysbh,
							detId: row.detId,
							depId: did,
							menuId:mid,
						},
						dataType : 'json',
						success : function(d) {
							gys_dg.datagrid('load');
							gys_dg.datagrid('unselectAll');
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
	}else{
		$.messager.alert('警告', '操作者必须归属于某个部门！',  'warning');
	}
}

function searchFunGys(){
	$('#jxc_gys_datagrid').datagrid('load',{
		gyscs:$('#jxc_gys_selet input[name=gyscs]').val(),
		depId:did,
	});	
}
function clearFunGys(){
	$('#jxc_gys_selet input[name=gyscs]').val("");
	$('#jxc_gys_datagrid').datagrid('load',{depId:did,});
}

</script>

<div id="jxc_gys_selet" class="easyui-layout" data-options="fit:true">
	<div data-options="region:'north',collapsible:true,title:'查询条件'"
		style="height: 60px;">
		请输入供应商编辑或名称进行检索：
		<input name="gyscs" />
		<a href="#" class="easyui-linkbutton"
			data-options="iconCls:'icon-search',plain:true"
			onclick="searchFunGys();">查询</a>
		<a href="#" class="easyui-linkbutton"
			data-options="iconCls:'icon-reload',plain:true"
			onclick="clearFunGys();">清空</a>
	</div>
	<div data-options="region:'center'">
		<div id="jxc_gys_datagrid"></div>
	</div>
</div>

<div id='jxc_gys_addDialog'></div>