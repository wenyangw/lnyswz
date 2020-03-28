<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<script type="text/javascript">
var did;
var spdw_dg;
$(function(){
	did = lnyw.tab_options().did;
	
	$('#jxc_spdw_layout').layout({
		fit : true,
		border : false,
	});
	$('#jxc_spfl_tree').tree({
		url:'${pageContext.request.contextPath}/jxc/spdwAction!listSpfl.action?depId=' + did,
		onClick : function(node) {
			if(node.attributes){
				spdw_dg.datagrid('load', {
					splbId : node.id,
					depId : did,
				});
			}
		}
		
	});
	spdw_dg = $('#jxc_spdw_dg').datagrid({
		url : '${pageContext.request.contextPath}/jxc/spdwAction!datagrid.action',
		queryParams :{
			depId : did,
		},
		fit : true,
	    border : false,
	    singleSelect : true,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
		columns:[[
	        {field:'id',title:'编号',width:100},
	        {field:'spdwmc',title:'商品段位名称',width:100},
	        {field:'splbId',title:'商品类别Id',width:100,hidden:true},
	        {field:'splbmc',title:'商品类别名称',width:100},
	    ]],
		
	});
	//根据权限，动态加载功能按钮
	lnyw.toolbar(0, spdw_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', did);
});

function appendSpdw() {
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
            			var json = $.parseJSON(jxc.toJson(d));
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
							var json = $.parseJSON(jxc.toJson(d));
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
<div id='jxc_spdw_layout' style="height:100%;width=100%">
	<div data-options="region:'west',title:'商品分类',split:true" style="height:100px;width:150px">
		<ul id="jxc_spfl_tree"></ul>
	</div>
    <div data-options="region:'center',title:'商品段位列表',split:true, fit:true" style="height:100px;">
    	<div id='jxc_spdw_dg'></div>
    </div>
</div>
<div id='jxc_spdw_addDialog'></div>


	
