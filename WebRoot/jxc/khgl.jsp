<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<script type="text/javascript">
var khgl_dg;
var did;
var mid;

$(function(){
	did = lnyw.tab_options().did;
	mid = lnyw.tab_options().id;
	khgl_dg = $('#jxc_khgl_dg');
	khgl_dg.datagrid({
		width: 600,
		url:'${pageContext.request.contextPath}/jxc/khAction!datagrid.action',
		fit : true,
		singleSelect:true,
	    queryParams: {
	    	depId: did
	    	},
		border : false,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
	    columns:[[
// 	    	{title:'通用信息',colspan:7},
// 			{title:'专属信息',colspan:7},
// 			],[
	        {field:'khbh',title:'客户编号'},
	        {field:'khmc',title:'客户名称',width:200},
	        {field:'dzdh',title:'地址/电话',width:200},
	        {field:'khh',title:'开户行',width:100},
	        {field:'sh',title:'税号',	width:100},
	        {field:'fr',title:'法人代表',width:100},
	        {field:'address',title:'送货地址',width:100},
	        {field:'dist',title:'距离',width:100},
	        {field:'isNsr',title:'一般纳税人',
	        	formatter : function(value) {
		        	if(value == '1'){
		        		return '是';
		        	}else{
		        		return '';
		        	}				
				}},
// 	        {field:'detId',title:'detId',width:100,hidden:true},
// 	        {field:'ywyId',title:'业务员id',width:100,hidden:true},
// 	        {field:'ywyName',title:'业务员',width:100},
// 	        {field:'lxr',title:'联系人'},
// 	        {field:'did',title:'部门id',width:100, hidden:true},
// 	        {field:'khlxId',title:'客户类型id',width:100,hidden:true},
// 	        {field:'khlxmc',title:'客户类型',width:100},
// 	        {field:'isSx',title:'授信客户',
// 	        	formatter : function(value, rowData, rowIndex) {
// 		        	if(value == '1'){
// 		        		return '是';
// 		        	}else{
// 		        		return '';
// 		        	}				
// 				}},
// 	        {field:'sxzq',title:'授信账期(天)',
// 	        	formatter : function(value, rowData, rowIndex) {
// 	        	if(value==0){
// 	        		return '';
// 	        	}else{
// 	        		return value;
// 	        	}				
// 				}},
// 	        {field:'sxje',title:'授信金额(元)',width:100,align:'right',
// 					formatter : function(value, rowData, rowIndex) {
// 	        	if(value==0){
// 	        		return '';
// 	        	}else{
// 	        		return value;
// 	        	}				
// 				}},
// 	        {field:'yfje',title:'历史金额',width:100,align:'right',
// 					formatter : function(value, rowData, rowIndex) {
// 	        	if(value==0){
// 	        		return '';
// 	        	}else{
// 	        		return value;
// 	        	}				
// 				}},
	        ]],
	});
	//根据权限，动态加载功能按钮
	lnyw.toolbar(0, khgl_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', did);
	
});

function appendKh() {
	var p = $('#jxc_kh_addDialog');
	p.dialog({
		title : '增加客户',
		href : '${pageContext.request.contextPath}/jxc/khAdd.jsp',
		width : 340,
		height : 310,
		modal : true,
		buttons: [{
            text:'增加',
            iconCls:'icon-ok',
            handler:function(){
            	$('#jxc_khAdd_form').form('submit', {
					url : '${pageContext.request.contextPath}/jxc/khAction!add.action',
					onSubmit: function(){	
						if($(this).form('validate')){
							var flag=true;
							$.ajax({
								url : '${pageContext.request.contextPath}/jxc/khAction!existKh.action',
								async: false,
								data : {
									khbh :$('input[name=khbh]').val()
								},
								dataType : 'json',
								success : function(d) {
									var j = $.parseJSON(jxc.toJson(d));
									if(!j.success){
										flag=false;
									}

								}
							});	
							if(!flag){
	            				$.messager.alert('提示', '客户编号已存在！', 'error');
	            			}
	            			return flag;
	            		}else{
	            			return false;
	            		}
					},
					success : function(d) {
						var json = $.parseJSON(jxc.toJson(d));
						if (json.success) {
							khgl_dg.datagrid('appendRow', json.obj);
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
				//sxzq :0,
				//sxje :0,
				depId:did,
				menuId:mid,		
			});			
			f.find('input[name=khbh]').focus();

// 			initNsr($('form input[name=isNsr]'));
// 			$('form input[name=isNsr]').click(function(){
// 				initNsr(this);
// 			});
		}
	});
}

function editKh(){
	var row = khgl_dg.datagrid('getSelected');
	if(row != undefined){	
		var p = $('#jxc_kh_addDialog');
		p.dialog({
			title : '编辑客户',
			href : '${pageContext.request.contextPath}/jxc/khAdd.jsp',
			width : 350,
			height : 310,
			buttons : [ {
				text : '编辑',
				handler : function() {
					var f = p.find('form');
					f.form('submit', {
						url : '${pageContext.request.contextPath}/jxc/khAction!edit.action',
						onSubmit: function(){	
							if($(this).form('validate')){
								return true;
							}else{
	            				return false;
	            			}
						},
						success : function(d) {
							var json = $.parseJSON(jxc.toJson(d));
							if (json.success) {
								khgl_dg.datagrid('reload');
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
				row["depId"] = did;
				row["menuId"] = mid;
				f.form('load', row);
// 				f.form('load', {
// 					khbh : rows[0].khbh,
// 					khmc: rows[0].khmc,
// 					dzdh: rows[0].dzdh,
// 					khh:rows[0].khh,
// 					sh:rows[0].sh,
// 					fr:rows[0].fr,
// 					isNsr:rows[0].isNsr,
// 					depId:did,
// 					menuId:mid,		
// 				});
// 				initNsr($('form input[name=isNsr]'));
								
				f.find('input[name=khbh]').focus();
// 				$('form input[name=isNsr]').click(function(){
// 					initNsr(this);
// 				});
			}
		});
	}else{
		$.messager.alert('警告', '请选择一条记录进行修改！',  'warning');
	}
}

function initNsr(target){
	if($(target).is(':checked')){
		$('form input[name=sh]').removeAttr('disabled');
		$('form input[name=khh]').removeAttr('disabled');
	}else{
		$('form input[name=sh]').attr('disabled','disabled');
		$('form input[name=khh]').attr('disabled','disabled');
	}
};
function removeKh(){
	var row = khgl_dg.datagrid('getSelected');
	if (row != undefined) {
		$.messager.confirm('请确认', '您要删除当前所选项目？', function(r) {
			if (r) {
				$.ajax({
					url : '${pageContext.request.contextPath}/jxc/khAction!delete.action',
					async: false,
					data : {
						khbh : row.khbh,
						depId : did,
						menuId : mid,		
					},
					dataType : 'json',
					success : function(d) {
						if(!d.success){
							$.messager.alert('警告', '对不起！此客户被其他部门占用！',  'warning');
						}
						khgl_dg.datagrid('load');
						khgl_dg.datagrid('unselectAll');
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


function editKhDet(){
	var row = khgl_dg.datagrid('getSelected');
	if(row != undefined){	
		var p = $('#jxc_kh_addDialog');
		p.dialog({
			title : '编辑客户专属信息',
			href : '${pageContext.request.contextPath}/jxc/khDet.jsp',
			width : 350,
			height : 360,
			buttons : [ {
				text : '编辑',
				handler : function() {
					var f = p.find('form');
					f.form('submit', {
						url : '${pageContext.request.contextPath}/jxc/khAction!editDet.action',
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
								khgl_dg.datagrid('reload');
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
				var ywyId = $("input[name=ywyId]");
				var ywyCombo = ywyId.combobox({
				    url:'${pageContext.request.contextPath}/admin/userAction!listYwys.action?did=' + did,
				    valueField:'id',
				    textField:'realName',
				    panelHeight: 'auto',
				});
				
// 				row["id"] = row.detId;
				row["depId"] = did;
				row["menuId"] = mid;
				f.form('load', row);
// 				f.form('load', {
// 					khbh : rows[0].khbh,
// 					khmc: rows[0].khmc,
// 					ywyId: rows[0].ywyId,
// 					isSx: rows[0].isSx,
// 					lxr :rows[0].lxr,
// 					sxzq :rows[0].sxzq,
// 					sxje :rows[0].sxje,
// 					yfje :rows[0].yfje,
// 					//sxje :sxje,
// 					detId: rows[0].detId,
// 					depId:did,
// 					menuId:mid,		
// 				});
				initForm($('form input[name=isSx]'));
				
				f.find('input[name=lxr]').focus();
				$('form input[name=isSx]').click(function(){
					initForm(this);
				});
				
			}
		});
	}else{
		$.messager.alert('警告', '请选择一条记录进行修改！',  'warning');
	}
	var initForm = function(target){
		if($(target).is(':checked')){
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



function removeKhDet(){
	var row = khgl_dg.datagrid('getSelected');
	if (row != undefined) {
		$.messager.confirm('请确认', '您要删除当前所选项目？', function(r) {
			if (r) {
				$.ajax({
					url : '${pageContext.request.contextPath}/jxc/khAction!deleteDet.action',
					data : {
						khbh : row.khbh,
						detId: row.detId,
						depId:did,
						menuId:mid,		
					},
					dataType : 'json',
					success : function(d) {
						khgl_dg.datagrid('load');
						khgl_dg.datagrid('unselectAll');
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

function searchFunKh(){
	$('#jxc_khgl_dg').datagrid('load',{
		khcs:$('#jxc_kh_selet input[name=khcs]').val(),
		depId:did,
	});	
}

function clearFunKh(){
	$('#jxc_kh_selet input[name=khcs]').val("");
	$('#jxc_khgl_dg').datagrid('load',{depId:did,});
}
</script>
<div id="jxc_kh_selet" class="easyui-layout" data-options="fit:true">
	<div data-options="region:'north',collapsible:true,title:'查询条件'"
		style="height: 60px;">
		检索条件（模糊）：
		<input name="khcs" />
		<a href="#" class="easyui-linkbutton"
			data-options="iconCls:'icon-search',plain:true"
			onclick="searchFunKh();">查询</a>
		<a href="#" class="easyui-linkbutton"
			data-options="iconCls:'icon-search',plain:true"
			onclick="clearFunKh();">清空</a>
	</div>
	<div data-options="region:'center'">
		<div id="jxc_khgl_dg"></div>
	</div>
</div>

<div id='jxc_kh_addDialog'></div>
