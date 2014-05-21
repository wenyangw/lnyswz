<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<script type="text/javascript">
var did;
var menuId;
var lx;

var sxkh_dg;
var xsmx_dg;

var jxc_khhk_ywyCombo;

$(function(){
	did = lnyw.tab_options().did;
	menuId = lnyw.tab_options().id;
	lx = lnyw.tab_options().lx;
	
	$('#jxc_khhk_layout').layout({
		fit : true,
		border : false,
	});
	
	
	sxkh_dg = $('#jxc_khhk_sxkhDg').datagrid({
		url : '${pageContext.request.contextPath}/jxc/khAction!sxkhDg.action',
// 		queryParams :{
// 			depId : did,
// 		},
		fit : true,
	    border : false,
	    singleSelect : true,
// 	    pagination : true,
// 		pagePosition : 'bottom',
// 		pageSize : pageSize,
// 		pageList : pageList,
		columns:[[
	        {field:'khbh',title:'编号',width:100},
	        {field:'khmc',title:'名称',width:100},
	        {field:'sxzq',title:'授信期',width:100},
	        {field:'sxje',title:'授信金额',width:100},
	    ]],
	    toolbar: '#jxc_khhk_sxkhTb',
	});
	
	xsmx_dg = $('#jxc_khhk_xsmxDg').datagrid({
		//url : '${pageContext.request.contextPath}/jxc/Action!datagrid.action',
// 		queryParams :{
// 			depId : did,
// 		},
		fit : true,
	    border : false,
	    singleSelect : true,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
		columns:[[
	        {field:'',title:'单据类型',width:100},
	        {field:'',title:'单据流水号',width:100},
	        {field:'',title:'开票时间',width:100},
	        {field:'',title:'应付时间',width:100},
	        {field:'',title:'销售金额',width:100},
	        {field:'',title:'还款金额',width:100,},
	    ]],
		
	});
	//根据权限，动态加载功能按钮
// 	lnyw.toolbar(0, spdw_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', did);
	
	//初始化业务员列表
	jxc_khhk_ywyCombo = lnyw.initCombo($("#jxc_khhk_ywyId"), 'id', 'realName', '${pageContext.request.contextPath}/admin/userAction!listYwys.action?did=' + did);
	jxc_khhk_ywyCombo.combobox({
		onSelect:function(record){
			sxkh_dg.datagrid('load', {
				depId: did,
				ywyId: record.id,
			});
		},	
	});
	
	
	
});

function hk() {
	var p = $('#jxc_spdw_addDialog');
	p.dialog({
		title : '增加商品段位',
		href : '${pageContext.request.contextPath}/jxc/spdwAdd.jsp',
		width : 340,
		height : 220,
		modal : true,
		buttons: [{
            text:'确定',
            iconCls:'icon-ok',
            handler:function(){
            	var addForm = $('#jxc_spdwAdd_form');
            	addForm.form('submit',{
            		url:'${pageContext.request.contextPath}/jxc/spdwAction!add.action',
            		onSubmit:function(){
            		},
            		success:function(d){
            			var json = $.parseJSON(d);
            			if(json.success){
            				spdw_dg.datagrid('appendRow', json.obj);
            				p.dialog('close');
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
			var f = p.find('form');
			var splbId = f.find('input[name=splbId]');
			var splbCombotree = splbId.combotree({
				url: '${pageContext.request.contextPath}/jxc/spdwAction!listSpfl.action?depId=' + did,
				onClick:function(node){
					var parent = splbCombotree.combotree('tree').tree('getParent', node.target);
					if(parent == null){
						$.messager.alert('提示', '请选择商品分类，不要选商品大类！', 'error');
						
					}else{
						$('tr#fromTo_tr').css('display', 'table-row');
						$('div#fromTo')
							.html(node.attributes.idFrom + ' - ' + node.attributes.idTo)
							.css('color', 'red');
					}
				}
			});
			$('input[name=spdwmc]').focus();
		}
	});
}

function editSpdw(){
	var rows = spdw_dg.datagrid('getSelections');
	if (rows.length == 1) {
		var p = $('#jxc_spdw_addDialog');
		p.dialog({
			title : '修改商品段位',
			href : '${pageContext.request.contextPath}/jxc/spdwAdd.jsp',
			width : 350,
			height : 220,
			buttons : [ {
				text : '确定',
				handler : function() {
					var f = p.find('form');
					f.form('submit', {
						url : '${pageContext.request.contextPath}/jxc/spdwAction!edit.action',
						onSubmit:function(){
						},
						success : function(d) {
							var json = $.parseJSON(d);
							if (json.success) {
								spdw_dg.datagrid('reload');
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
				var splbId = f.find('input[name=splbId]');
				var splbComboTree = splbId.combotree({
					url: '${pageContext.request.contextPath}/jxc/spdwAction!listSpfl.action?depId=' + did,
// 					url : '${pageContext.request.contextPath}/jxc/splbAction!getSplbs.action?depId=' + did,
					onClick:function(node){
						var parent = splbCombotree.combotree('tree').tree('getParent', node.target);
						if(parent == null){
							$.messager.alert('提示', '请选择商品分类，不要选商品大类！', 'error');
						}
					}
				});
				f.form('load', rows[0]);
				f.find('input[name=spdwmc]').focus();
			}
		});
	} else if (rows.length > 1) {
		$.messager.alert('提示', '同一时间只能编辑一条记录！', 'error');
	} else {
		$.messager.alert('提示', '请选择一条要编辑的记录！', 'error');
	}
}
function removeSpdw(){
	var rows = spdw_dg.datagrid('getSelections');
	if (rows.length == 1) {
		$.messager.confirm('请确认', '您要删除当前所选项目？', function(r) {
			if (r) {
				$.ajax({
					url : '${pageContext.request.contextPath}/jxc/spdwAction!delete.action',
					data : {
						id : rows[0].id
					},
					dataType : 'json',
					success : function(d) {
						spdw_dg.datagrid('load');
						spdw_dg.datagrid('unselectAll');
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
<div id='jxc_khhk_layout' style="height:100%;width=100%">
	<div data-options="region:'west',title:'授信客户',split:true" style="height:100px;width:200px">
		<table id="jxc_khhk_sxkhDg"></table>
	</div>
    <div data-options="region:'center',split:true, fit:true" style="height:100%;">
    	<div id='jxc_khhk_rlayout' style="height:100%;width=100%">
			<div data-options="region:'north'" style="height:100px;">
				<table>
					<tr>
						<th>客户名称</th><td><div id="khmc"></div></td><th>授信期</th><td><div id="sxzq"></div></td><th>授信金额</th><td><div id="sxje"></div></td>
					</tr>
					<tr>
						<th>销售金额</th><td><div id="ysje"></div></td>
					</tr>
					<tr>
						<th>预付款</th><td><input type="checkbox" id="isYf" name="isYf"></td><th>金额</th><td><input name="hkje"></td>
					</tr>
				</table>				
			</div>
		    <div data-options="region:'center',title:'销售明细',split:true, fit:true" style="height:100%;">
		    	<div id='jxc_khhk_xsmxDg'></div>
		    </div>
		</div>
    </div>
</div>
<div id="jxc_khhk_sxkhTb" style="padding:3px;height:auto">
	业务员:<input id="jxc_khhk_ywyId" name="ywyId" type="text" size="8">
</div>



	
