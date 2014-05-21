<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<script type="text/javascript">
var spbgy_dg;
var did;
var menuId;

$(function(){
	
	did = lnyw.tab_options().did;
	menuId = lnyw.tab_options().id;
	
	$.ajax({
		type: "POST",
		async: false,
		url: '${pageContext.request.contextPath}/jxc/ckAction!listCk.action',
		data: {
			depId: did,
		},
		dataType: 'json',
		success: function(d){
			$('#jxc_spbgy_ckId').combobox({
				data : d,
			    valueField: 'id',
				textField: 'ckmc',
				panelHeight: 'auto',
			}).combobox('selectedIndex' , 0);
		},
	});

	spbgy_dg = $('#jxc_spbgy_dg').datagrid({
		url : '${pageContext.request.contextPath}/jxc/spBgyAction!datagridBgy.action',		
		queryParams: {
			depId : did,
 			ckId : $('#jxc_spbgy_ckId').combobox('getValue'),
		},
		fit : true,
	    border : false,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
		columns:[[
		    {field:'bgyId',title:"保管员",width:100,checkbox:true},
	        {field:'spbh',title:'商品编号',width:100},
	        {field:'spmc',title:'商品名称',width:100},
	        {field:'spcd',title:'商品产地',width:100},
	    ]],
		toolbar : '#jxc_spbgy_tb',
		onBeforeLoad:function(param){
// 			param.jxc_spbgy_ckId = $('#jxc_spbgy_ckId').combobox('getValue');
		},
		onLoadSuccess: function(data){
			var rowData = data.rows;  
            $.each(rowData,function(idx,val){//遍历JSON  
                  if(val.bgyId > 0){  
                    spbgy_dg.datagrid("selectRow", idx);//如果数据行为已选中则选中改行  
                  }  
            }); 
		},
	});
	//根据权限，动态加载功能按钮
 	lnyw.toolbar(0, spbgy_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', did);
	
});


// function appendSplb() {
// 	var p = $('#jxc_splb_addDialog');
// 	p.dialog({
// 		title : '增加商品类别',
// 		href : '${pageContext.request.contextPath}/jxc/splbAdd.jsp',
// 		width : 340,
// 		height : 220,
// 		modal : true,
// 		buttons: [{
//             text:'确定',
//             iconCls:'icon-ok',
//             handler:function(){
//             	var addForm = $('#jxc_splbAdd_form');
//             	addForm.form('submit',{
//             		url:'${pageContext.request.contextPath}/jxc/splbAction!add.action',
//             		onSubmit:function(){
//             			var from = parseInt(addForm.find('input[name=idFrom]').val());
//             			var to = parseInt(addForm.find('input[name=idTo]').val());
// 						if(from > to){
// 							addForm.find('input[name=idFrom]').focus();
// 							$.messager.alert('提示', '编号范围要从小到大！', 'error');
// 							return false;
// 						}
// 						return true;
//             		},
//             		success:function(d){
//             			var json = $.parseJSON(d);
//             			if(json.success){
//             				splb_dg.datagrid('appendRow', json.obj);
//             				p.dialog('close');
//             			}
//             			$.messager.show({
//             				title : '提示',
//             				msg : json.msg
//             			});
//             		},
//             	});
//             }
//         }],
//         onLoad : function() {
// 			var f = p.find('form');
// 			var spdlId = f.find('input[name=spdlId]');
// 			var spdlCombo = spdlId.combobox({
// 				url : '${pageContext.request.contextPath}/jxc/spdlAction!listSpdls.action',
// 				valueField:'id',
// 			    textField:'spdlmc'
// 			});
// 			$('input[name=splbmc]').focus();
// 		}
// 	});
// }

// function editSplb(){
// 	var rows = splb_dg.datagrid('getSelections');
// 	if (rows.length == 1) {
// 		var p = $('#jxc_splb_addDialog');
// 		p.dialog({
// 			title : '修改商品类别',
// 			href : '${pageContext.request.contextPath}/jxc/splbEdit.jsp',
// 			width : 350,
// 			height : 220,
// 			buttons : [ {
// 				text : '确定',
// 				handler : function() {
// 					var f = p.find('form');
// 					f.form('submit', {
// 						url : '${pageContext.request.contextPath}/jxc/splbAction!edit.action',
// 						onSubmit:function(){
// 							var from = parseInt(f.find('input[name=idFrom]').val());
// 	            			var to = parseInt(f.find('input[name=idTo]').val());
// 							if(from > to){
// 								f.find('input[name=idFrom]').focus();
// 								$.messager.alert('提示', '编号范围要从小到大！', 'error');
// 								return false;
// 							}
// 							return true;
// 						},
// 						success : function(d) {
// 							var json = $.parseJSON(d);
// 							if (json.success) {
// 								splb_dg.datagrid('reload');
// 								p.dialog('close');
// 							}
// 							$.messager.show({
// 								msg : json.msg,
// 								title : '提示'
// 							});
// 						}
// 					});
// 				}
// 			} ],
// 			onLoad : function() {
// 				var f = p.find('form');
// 				var spdlId = f.find('input[name=spdlId]');
// 				var spdlCombo = spdlId.combobox({
// 					url : '${pageContext.request.contextPath}/jxc/spdlAction!listSpdls.action',
// 					valueField:'id',
// 				    textField:'spdlmc'
// 				});
// 				f.form('load', rows[0]);
// 				f.find('input[name=splbmc]').focus();
// 			}
// 		});
// 	} else if (rows.length > 1) {
// 		$.messager.alert('提示', '同一时间只能编辑一条记录！', 'error');
// 	} else {
// 		$.messager.alert('提示', '请选择一条要编辑的记录！', 'error');
// 	}
// }
function saveSpBgy(){
	var rows = spbgy_dg.datagrid('getSelections');
	var spbhs = [];
	if (rows.length > 0) {
		$.messager.confirm('请确认', '您要关联选中的商品条目？', function(r) {
			if (r) {
				for (var i = 0; i < rows.length; i++) {
					spbhs.push(rows[i].spbh);
				}
				$.ajax({
					url : '${pageContext.request.contextPath}/jxc/spBgyAction!saveSpBgy.action',
					data : {
						depId : did,
						ckId : $('#jxc_spbgy_ckId').combobox('getValue'),
						spbhs : spbhs.join(",")
					},
					dataType : 'json',
					success : function(d) {
						spbgy_dg.datagrid('load',{
							depId : did,
							ckId : $('#jxc_spbgy_ckId').combobox('getValue')
							
						});
						spbgy_dg.datagrid('unselectAll');
						$.messager.show({
							title : '提示',
							msg : d.msg
						});
					}
				});
			}
		});
	} else {
		$.messager.alert('提示', '请选择至少一条商品！', 'error');
	}
}

function deleteSpBgy(){
	$.messager.confirm('请确认', '您是否要取消所有关联的商品条目？', function(r) {
		if (r) {
			$.ajax({
				url : '${pageContext.request.contextPath}/jxc/spBgyAction!deleteSpBgy.action',
				data : {
					depId : did,
					ckId : $('#jxc_spbgy_ckId').combobox('getValue'),
				},
				dataType : 'json',
				success : function(d) {
					spbgy_dg.datagrid('load',{
						depId : did,
						ckId : $('#jxc_spbgy_ckId').combobox('getValue')
					});
					spbgy_dg.datagrid('unselectAll');
					$.messager.show({
						title : '提示',
						msg : d.msg
					});
				}
			});
		}
	});
}
</script>
<!-- <div id='jxc_spbgy_layout' style="height:100%;width=100%"> -->
<!-- 	<div data-options="region:'west',title:'保管员',split:true" style="height:100px;width:150px"> -->
<!-- 		<ul id="jxc_spbgy_tree"></ul> -->
<!-- 	</div> -->
<!--     <div data-options="region:'center',title:'商品列表',split:true, fit:true" style="height:100px;"> -->
<!--     </div> -->
<!-- </div> -->
<div id="jxc_spbgy_tb" style="padding:3px;height:auto">
请选择仓库:<input id="jxc_spbgy_ckId" name="ckId" type='text' size="8">
</div>
<div id='jxc_spbgy_dg'></div>

	
