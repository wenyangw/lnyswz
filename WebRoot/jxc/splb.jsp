<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<script type="text/javascript">
var splb_dg;
var did;
$(function(){
	did = lnyw.tab_options().did;
	$('#jxc_splb_layout').layout({
		fit : true,
		border : false,
	});
	$('#jxc_spdl_tree').tree({
		url:'${pageContext.request.contextPath}/jxc/spdlAction!listSpdls.action',
		onClick : function(node) {
			splb_dg.datagrid('load', {
				spdlId : node.id,
			});
		}
		
	});
	splb_dg = $('#jxc_splb_dg').datagrid({
		url : '${pageContext.request.contextPath}/jxc/splbAction!datagrid.action',
		fit : true,
	    border : false,
	    singleSelect : true,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
		columns:[[
	        {field:'id',title:'编号',width:100, checkbox : true},
	        {field:'splbmc',title:'商品类别名称',width:100},
	        {field:'idFrom',title:'起始编码',width:100},
	        {field:'idTo',title:'结束编码',width:100},
	        {field:'spdlId',title:'商品大类Id',width:100,hidden:true},
	        {field:'spdlmc',title:'商品大类名称',width:100},
	    ]],
		
	});
	//根据权限，动态加载功能按钮
	lnyw.toolbar(0, splb_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', did);
});

function appendSplb() {
	var p = $('#jxc_splb_addDialog');
	p.dialog({
		title : '增加商品类别',
		href : '${pageContext.request.contextPath}/jxc/splbAdd.jsp',
		width : 340,
		height : 220,
		modal : true,
		buttons: [{
            text:'确定',
            iconCls:'icon-ok',
            handler:function(){
            	var addForm = $('#jxc_splbAdd_form');
            	addForm.form('submit',{
            		url:'${pageContext.request.contextPath}/jxc/splbAction!add.action',
            		onSubmit:function(){
            			var from = parseInt(addForm.find('input[name=idFrom]').val());
            			var to = parseInt(addForm.find('input[name=idTo]').val());
						if(from > to){
							addForm.find('input[name=idFrom]').focus();
							$.messager.alert('提示', '编号范围要从小到大！', 'error');
							return false;
						}
						return true;
            		},
            		success:function(d){
            			var json = $.parseJSON(jxc.toJson(d));
            			if(json.success){
            				splb_dg.datagrid('appendRow', json.obj);
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
			var spdlId = f.find('input[name=spdlId]');
			var spdlCombo = spdlId.combobox({
				url : '${pageContext.request.contextPath}/jxc/spdlAction!listSpdls.action',
				valueField:'id',
			    textField:'spdlmc'
			});
			$('input[name=splbmc]').focus();
		}
	});
}

function editSplb(){
	var rows = splb_dg.datagrid('getSelections');
	if (rows.length == 1) {
		var p = $('#jxc_splb_addDialog');
		p.dialog({
			title : '修改商品类别',
			href : '${pageContext.request.contextPath}/jxc/splbEdit.jsp',
			width : 350,
			height : 220,
			buttons : [ {
				text : '确定',
				handler : function() {
					var f = p.find('form');
					f.form('submit', {
						url : '${pageContext.request.contextPath}/jxc/splbAction!edit.action',
						onSubmit:function(){
							var from = parseInt(f.find('input[name=idFrom]').val());
	            			var to = parseInt(f.find('input[name=idTo]').val());
							if(from > to){
								f.find('input[name=idFrom]').focus();
								$.messager.alert('提示', '编号范围要从小到大！', 'error');
								return false;
							}
							return true;
						},
						success : function(d) {
							var json = $.parseJSON(jxc.toJson(d));
							if (json.success) {
								splb_dg.datagrid('reload');
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
				var spdlId = f.find('input[name=spdlId]');
				var spdlCombo = spdlId.combobox({
					url : '${pageContext.request.contextPath}/jxc/spdlAction!listSpdls.action',
					valueField:'id',
				    textField:'spdlmc'
				});
				f.form('load', rows[0]);
				f.find('input[name=splbmc]').focus();
			}
		});
	} else if (rows.length > 1) {
		$.messager.alert('提示', '同一时间只能编辑一条记录！', 'error');
	} else {
		$.messager.alert('提示', '请选择一条要编辑的记录！', 'error');
	}
}
function removeSplb(){
	var rows = splb_dg.datagrid('getSelections');
	if (rows.length == 1) {
		$.messager.confirm('请确认', '您要删除当前所选项目？', function(r) {
			if (r) {
				$.ajax({
					url : '${pageContext.request.contextPath}/jxc/splbAction!delete.action',
					data : {
						id : rows[0].id
					},
					dataType : 'json',
					success : function(d) {
						splb_dg.datagrid('load');
						splb_dg.datagrid('unselectAll');
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
<div id='jxc_splb_layout' style="height:100%;width=100%">
	<div data-options="region:'west',title:'菜单',split:true" style="height:100px;width:150px">
		<ul id="jxc_spdl_tree"></ul>
	</div>
    <div data-options="region:'center',title:'功能管理',split:true, fit:true" style="height:100px;">
    	<div id='jxc_splb_dg'></div>
    </div>
</div>
<div id='jxc_splb_addDialog'></div>


	
