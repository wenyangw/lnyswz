<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<script type="text/javascript">
var dict_dg;
var did;
var mid;
$(function(){
	did = lnyw.tab_options().did;
	mid = lnyw.tab_options().id;
	$('#admin_dict_layout').layout({
		fit : true,
		border : false,
	});
	$('#admin_dict_tree').tree({
		url:'${pageContext.request.contextPath}/admin/dictAction!listDict.action',
		onClick : function(node) {
			dict_dg.datagrid('load', {
				id : node.id,
				ename :node.attributes.ename,
			});
		}	
	});
	dict_dg = $('#admin_dict_dg').datagrid({
		url : '${pageContext.request.contextPath}/admin/dictAction!datagrid.action',
		fit : true,
	    border : false,
	    singleSelect: true,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
		columns:[[
	        {field:'id',title:'编号',width:100, checkbox : true},
	        {field:'ename',title:'英文名',width:80},
	        {field:'cname',title:'中文名',width:100},
	       	{field:'genre',title:'类型',width:40,
	        	formatter : function(value) {	
					for(var i=0;i<dictType.length;i++){
						if(value==dictType[i].value){
							return dictType[i].text;
						}
					}
				}
			},
			{field:'specials',title:'特殊种类',width:70},
	        {field:'tname',title:'所属表名',width:80},
	        {field:'show',title:'提示信息',width:140},
	        {field:'specialValues',title:'特殊值',width:160},
	        {field:'orderNum',title:'排序',width:50},
	        {field:'tree',title:'分类',width:50,
	        	formatter : function(value) {	
		       	 	 if(value=='1'){
		       	 		 return "是";
		       	 	 }else if(value=='0'){
		       	 		 return "否";
		       	 	 }
	       	 	}
	       	 },
	        {field:'frozen',title:'是否冻结',width:50,
	        	formatter : function(value) {	
		       	 	 if(value=='1'){
		       	 		 return "是";
		       	 	 }else if(value=='0'){
		       	 		 return "否";
		       	 	 }
	       	 	}
	       	 },
	        {field:'display',title:'是否查询 ',width:80,
	       	 	formatter : function(value) {	
	       	 	 if(value=='1'){
	       	 		 return "可查询";
	       	 	 }else if(value=='0'){
	       	 		 return "不可查询";
	       	 	 }
	       	 }
	        },
	        {field:'orderBy',title:'排序条件',width:80},
	    ]],
		 toolbar:'#admin_dict_tb',
	});
	//根据权限，动态加载功能按钮
	lnyw.toolbar(0, dict_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', did);
});

function appendDict() {
	var p = $('#admin_dict_addDialog');
	p.dialog({
		title : '增加字典',
		href : '${pageContext.request.contextPath}/admin/dictAdd.jsp',
		width : 340,
		height : 410,
		modal : true,
		buttons: [{
            text:'确定',
            iconCls:'icon-ok',
            handler:function(){
            	$('#admin_dictAdd_form').form('submit', {
					url : '${pageContext.request.contextPath}/admin/dictAction!add.action',
					success : function(d) {
						var json = $.parseJSON(d);
						if (json.success) {
							dict_dg.datagrid('appendRow', json.obj);
							if(json.obj.genre == '03'){
								$('#admin_dict_tree').tree('reload');
							}
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
			var genre=f.find('input[name=genre]');
			var genreCom=genre.combobox({
				data : dictType,
			});
			 f.form('load', {
					depId:did,
					menuId:mid,			
			});				
		}
	});
}

function editDict(){
	var rows = dict_dg.datagrid('getSelections');
	if (rows.length == 1) {
		var p = $('#admin_dict_addDialog');
		p.dialog({
			title : '修改字典类别',
			href : '${pageContext.request.contextPath}/admin/dictEdit.jsp',
			width : 350,
			height : 410,
			buttons : [ {
				text : '确定',
				handler : function() {
					var f = p.find('form');
					f.form('submit', {
						url : '${pageContext.request.contextPath}/admin/dictAction!edit.action',
						onSubmit:function(){						
						},
						success : function(d) {
							var json = $.parseJSON(d);
							if (json.success) {
								dict_dg.datagrid('reload');
								if(rows[0].genre == '03'){
									$('#admin_dict_tree').tree('reload');
								}
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
				var genre=f.find('input[name=genre]');
				var genreCom=genre.combobox({
				data : dictType,
			});
				f.form('load', {
					id : rows[0].id,
					ename:rows[0].ename,
					cname:rows[0].cname,
					specials:rows[0].specials,
					tname:rows[0].tname,
					orderNum:rows[0].orderNum,
					display:rows[0].display,
					specialValues:rows[0].specialValues,
					show:rows[0].show,
					genre:rows[0].genre,
					frozen:rows[0].frozen,
					tree:rows[0].tree,
					depId:did,
					menuId:mid,	
					orderBy:rows[0].orderBy,
				});
			
			}
		});
	} else if (rows.length > 1) {
		$.messager.alert('提示', '同一时间只能编辑一条记录！', 'error');
	} else {
		$.messager.alert('提示', '请选择一条要编辑的记录！', 'error');
	}
}
function removeDict(){
	var rows = dict_dg.datagrid('getSelections');
	var ids = [];
	if (rows.length > 0) {
		$.messager.confirm('请确认', '您要删除当前所选项目？', function(r) {
			if(r){
				for ( var i = 0; i < rows.length; i++) {
					ids.push(rows[i].id);
				}
				$.ajax({
					url : '${pageContext.request.contextPath}/admin/dictAction!delete.action',
					data : {
						ids : ids.join(','),
						depId:did,
						menuId:mid,	
					},
					dataType : 'json',
					success : function(d) {
						dict_dg.datagrid('reload');
						dict_dg.datagrid('unselectAll');
						if(rows[0].genre == '03'){
							$('#admin_dict_tree').tree('reload');
						}
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
//筛选信息
function searchDict(){
	dict_dg.datagrid('load',{
		//筛选条件
		tjsx: $('input[name=tjsx]').val(),
		
	});
}

</script>
<div id='admin_dict_layout' style="height: 100%;">
	<div data-options="region:'west',title:'视图',split:true" style="height: 100px; width: 150px">
		<ul id="admin_dict_tree"></ul>
	</div>
	<div data-options="region:'center',title:'详细内容',split:true, fit:true" style="height: 100px;">
		<div id='admin_dict_dg'></div>
		<div id="admin_dict_tb" style="padding:3px;height:auto">
			条件筛选：<input type="text" name="tjsx" 
				class="easyui-validatebox" style="width:100px">
			<a href="#" class="easyui-linkbutton" 
				data-options="iconCls:'icon-search',plain:true" onclick="searchDict();">查询</a>
		</div>
	</div>
</div>
<div id='admin_dict_addDialog'></div>



