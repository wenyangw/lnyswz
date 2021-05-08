<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<script type="text/javascript" src="${pageContext.request.contextPath}/js/datagrid-filter.js"></script>
<script type="text/javascript">
var rkfk_did;
var rkfk_lx;
var rkfk_menuId;
var rkfk_gysDg;
var rkfk_dg;
var rkfk_ywrkDg;
// var jxc_rkfk_ywyCombo;

//入库列表所有行
var rows;

//本次付款对应入库笔数
var countFk;
var countRkfk;
var countYwrkInRkfk;
//每行付款后剩余金额
var je;
var lastFkje;

$(function(){
	rkfk_did = lnyw.tab_options().did;
	rkfk_lx = lnyw.tab_options().lx;
	rkfk_menuId = lnyw.tab_options().id;
	
	$('#jxc_rkfk_layout').layout({
		fit : true,
		border : false,
	});
	
	// $('#jxc_rkfk_gysLayout').layout({
	// 	fit : true,
	// 	border : false,
	// });
	
	//初始化业务员列表
	<%--jxc_rkfk_ywyCombo = lnyw.initCombo($("#jxc_rkfk_ywyId"), 'id', 'realName', '${pageContext.request.contextPath}/admin/userAction!listYwys.action?did=' + rkfk_did);--%>
	
	rkfk_gysDg = $('#jxc_rkfk_gysDg').datagrid({
		url : '${pageContext.request.contextPath}/jxc/ywrkAction!listGysYf.action',
		queryParams :{
			bmbh : rkfk_did,
		},
		fit : true,
	    border : false,
	    singleSelect : true,
		columns:[[
	        {field:'gysbh',title:'编号',width:60,
	        	hfilter:{type:'textbox',},
	        },
	        {field:'gysmc',title:'名称',width:200},
	    ]],
	    toolbar:'#jxc_rkfk_gysTb',
	    onSelect: function(rowIndex, rowData){
	    	$('#gysbh').html(rowData.gysbh);
	 		$('#gysmc').html(rowData.gysmc);
			// $('#khlx').html('');
			$('#sxzq').html(rowData.sxzq);
			$('#sxje').html(rowData.sxje);
			$('#yfje').html(rowData.yfje);
			// $('#lsje').html('');

			rkfk_ywrkDg.datagrid('load', {
    			bmbh: rkfk_did,
    			gysbh: rowData.gysbh
	    	});
	    }
	});
	rkfk_gysDg.datagrid('enableFilter');
	
	rkfk_dg = $('#jxc_rkfk_dg').datagrid({
		fit : true,
	    border : false,
	    singleSelect : true,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
		columns:[[
	        {field:'rkfklsh',title:'流水号',width:100,align:'center'},
	        {field:'createTime',title:'创建时间',width:100,align:'center',
	        	formatter:function(value){
	        		return moment(value).format('YYYY-MM-DD');
	        	}},
	        {field:'createName',title:'创建人',width:100,align:'center'},
	        {field:'payTime',title:'付款时间',width:100,align:'center',
	        	formatter:function(value){
	        		return moment(value).format('YYYY-MM-DD');
	        	}},	
	        {field:'gysbh',title:'客户编号',width:100,align:'center',hidden:true},
	        {field:'gysmc',title:'客户名称',width:300,align:'center'},
	        // {field:'ywyId',title:'业务员id',width:100,align:'center',hidden:true},
	        // {field:'ywymc',title:'业务员',width:100,align:'center'},
	        {field:'fkje',title:'付款金额',width:100,align:'center'},
	        {field:'isYf',title:'预付',align:'center',
	        	formatter : function(value) {
					if (value == '1') {
						return '是';
					} else {
						return '';
					}
				}},
	        // {field:'isLs',title:'历史',align:'center',
	        // 	formatter : function(value) {
			// 		if (value == '1') {
			// 			return '是';
			// 		} else {
			// 			return '';
			// 		}
			// 	}},
	        {field:'isCancel',title:'*状态',align:'center',sortable:true,
        		formatter : function(value) {
					if (value == '1') {
						return '已取消';
					} else {
						return '';
					}
				},
        		sorter: function(a,b){
        			a = a == undefined ? 0 : a;
        			b = b == undefined ? 0 : b;
					return (a-b);  
				}},
			{field:'cancelTime',title:'取消时间',align:'center'},
        	{field:'cancelRkfklsh',title:'原付款流水号',align:'center'},
	    ]],
	    toolbar:'#jxc_rkfk_tb',
	});
	lnyw.toolbar(1, rkfk_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', rkfk_did);
	
	rkfk_dg.datagrid({
        view: detailview,
        detailFormatter:function(index,row){
            return '<div style="padding:2px"><table id="rkfk-ddv-' + index + '"></table></div>';
        },
        onExpandRow: function(index,row){
            $('#rkfk-ddv-'+index).datagrid({
                url:'${pageContext.request.contextPath}/jxc/rkfkAction!detDatagrid.action',
                fitColumns:true,
                singleSelect:true,
                rownumbers:true,
                loadMsg:'',
                height:'auto',
                queryParams: {
        			rkfklsh: row.rkfklsh,
        		},
                columns:[[
                    {field:'ywrklsh',title:'入库流水号',width:200,align:'center'},
                    {field:'createTime',title:'入库时间',width:200,align:'center'},
                    {field:'hjje',title:'入库金额',width:100,align:'center'},
                    {field:'fkje',title:'付款金额',width:100,align:'center'},
                ]],
                onResize:function(){
                	rkfk_dg.datagrid('fixDetailRowHeight',index);
                },
                onLoadSuccess:function(){
                    setTimeout(function(){
                    	rkfk_dg.datagrid('fixDetailRowHeight',index);
                    },0);
                }
            });
            rkfk_dg.datagrid('fixDetailRowHeight',index);
        }
    });
		
	rkfk_ywrkDg = $('#jxc_rkfk_ywrkDg').datagrid({
		url: '${pageContext.request.contextPath}/jxc/ywrkAction!listYwrkNoFk.action',
		queryParams: {
			bmbh: rkfk_did,
		},
		fit: true,
	    border: false,
	    singleSelect: true,
		columns: [[
	        {field: 'ywrklsh', title: '流水号', width: 100, align: 'center'},
	        {field:'createTime',title:'发票时间',width:100,align:'center',
	        	formatter:function(value){
	        		return moment(value).format('YYYY-MM-DD');
	        	}},
	        {field:'payTime',title:'应付款时间',width:100,align:'center',
	        	formatter:function(value){
	        		return moment(value).format('YYYY-MM-DD');
	        	},
	        	styler:function(value){
	        		if(moment().subtract('days', 1).isAfter(value)){
	        			return 'color:red;';
	        		}
	        	}},
	        {field:'hjje',title:'发票金额',width:100,align:'center'},
	        {field:'fkedje',title:'已付金额',width:100,align:'center',
	        	formatter:function(value){
	        		if(value == 0){
	        			return '';
	        		}else{
	        			return value;
	        		}
	        	},
	        	styler:function(value){
	        		if(value != 0){
	        			return 'color:blue;';
	        		}
	        	}
	        },
	        {field:'fkje',title:'本次付款金额',width:100,align:'center',
	        	formatter:function(value){
	        		if(value == 0){
	        			return '';
	        		}else{
	        			return value;
	        		}
	        	},
	        	styler:function(value){
	        		if(value != 0){
	        			return 'color:red;';
	        		}
	        	}},
	    ]],
	    onLoadSuccess:function(data){
	    	// if($('#gysbh').html() != ''){
			// 	// $('#khlx').html(data.obj.khlxmc);
			// 	$('#sxzq').html(data.obj.sxzq + '天');
			// 	$('#sxje').html(data.obj.sxje + '元');
			// 	$('#yfzje').html(data.obj.yfzje == 0 ? '' : data.obj.yfzje + '元');
			// 	$('#yfje').html(data.obj.ysje == 0 ? '' : data.obj.yfje + '元');
			// 	// $('#lsje').html(data.obj.lsje == 0 ? '' : data.obj.lsje + '元');
	    	// }
	    	rows = rkfk_ywrkDg.datagrid('getRows');
		}
	});
	//根据权限，动态加载功能按钮
 	lnyw.toolbar(0, rkfk_ywrkDg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', rkfk_did);

 	//选中列表标签后，装载数据
	ywrk_tabs = $('#jxc_rkfk_tabs').tabs({
		onSelect: function(title, index){
			if(index == 0){
				rkfk_ywrkDg.datagrid('load', {
    				bmbh:rkfk_did,
    				gysbh:$('#gysbh').html(),
    				// ywyId: jxc_rkfk_ywyCombo.combobox('getValue'),
	    		});
			}
			if(index == 1){
				rkfk_dg.datagrid({
					url: '${pageContext.request.contextPath}/jxc/rkfkAction!datagrid.action',
					queryParams:{
						bmbh: rkfk_did,
						createTime: countRkfk == 0 ? undefined : $('input[name=createTimeRkfk]').val(),
						search: countRkfk == 0 ? undefined : $('input[name=searchRkfk]').val(),
					}
				});
				countRkfk++;
			}
		},
	});
	
	<%--jxc_rkfk_ywyCombo.combobox({--%>
	<%--	onSelect: function(){--%>
	<%--		rkfk_khDg.datagrid({--%>
	<%--			url : '${pageContext.request.contextPath}/jxc/khAction!listKhByYwy.action',--%>
	<%--	 		queryParams :{--%>
	<%--	 			depId : rkfk_did,--%>
	<%--	 			ywyId: jxc_rkfk_ywyCombo.combobox('getValue')--%>
	<%--	 		},--%>
	<%--		});--%>
	<%--		rkfk_khDg.datagrid('enableFilter');--%>
	<%--	}--%>
	<%--});--%>
	
	$('#fkje').keyup(function(event) {
		var lastTime = event.timeStamp;
		setTimeout(function () {
			if (lastTime - event.timeStamp == 0) {

				//如付款为历史陈欠，不处理下面的代码
				// if($('input[name=isLs]').is(':checked')){
				// 	return false;
				// }

				countFk = 0;
				//本次付款金额
				je = Number($('#fkje').val());
				if(rows != undefined){
					$.each(rows, function(index){
						if(je != 0){
							countFk++;
						} else {
							return false;
						}
						//每行付款金额
						lastFkje = 0;
						//每行未付款金额
						var wfkje = rows[index].hjje - rows[index].fkedje;
						if(je > wfkje){
							lastFkje = wfkje;
						}else{
							lastFkje = je;
						}
						je -= lastFkje;
						rkfk_ywrkDg.datagrid('updateRow', {
							index:index,
							row: {
								fkje: lastFkje.toFixed(4),
							}
						});
					});
				}else{
					$.messager.alert('提示', '付款的客户没有入库记录！', 'error');
				}
			}
		}, 1000);
	});
	
	// jxc_rkfk_ywyCombo.combobox('selectedIndex', 0);
	//初始化信息
	init();
	
	// $('input[name=isLs]').click(function(){
 	// 	if($('input[name=isLs]').is(':checked')){
	// 		if($('#lsje').html() == ''){
	// 			$.messager.alert('警告', '该客户无历史陈欠！',  'warning');
	// 			$('input[name=isLs]').removeAttr('checked');
	// 			$('input[name=isLs]').removeProp('checked');
	// 			return false;
	// 		}
	// 		//清除已填写的销售发票明细数据
	// 		var rows = rkfk_ywrkDg.datagrid('getRows');
	// 		if(rows != undefined){
	// 			$.each(rows, function(index){
	// 				rkfk_ywrkDg.datagrid('updateRow', {
	// 					index:index,
	// 					row: {
	// 						hkje: 0,
	// 					}
	// 				});
	// 			});
	// 		}
	// 	}
	// });
	
	
});

//以下为商品列表处理代码
function init(){
	//清空全部字段
	$('input[name=fkje]').val('');
	
	$('input:checkbox').prop('checked', false);

	rows = undefined;
	
	//初始化创建时间
	$('#createDate').html(moment().format('YYYY年MM月DD日'));
	
	//初始化流水号
	$.ajax({
		type: "POST",
		url: '${pageContext.request.contextPath}/jxc/lshAction!getLsh.action',
		data: {
			bmbh: rkfk_did,
			lxbh: rkfk_lx,
		},
		dataType: 'json',
		success: function(d){
			if(d.success){
				$('#rkfkLsh').html(d.obj);
			}  
		},
	});
	
}

function selectGys(rowData){
	rkfk_ywrkDg.datagrid({
		url:'${pageContext.request.contextPath}/jxc/ywrkAction!getYwrkNoFk.action',
		queryParams:{
			bmbh:rkfk_did,
			gysbh:rowData.gysbh
		},
		onBeforeLoad: function(){
			$('#gysbh').html(row.gysbh);
			$('#gysmc').html(row.gysmc);
		},
		onLoadSuccess:function(data){
			// $('#khlx').html(data.obj.khlxmc);
			$('#sxzq').html(data.obj.sxzq + '天');
			$('#sxje').html(data.obj.sxje + '元');
			$('#yfje').html(data.obj.ysje == 0 ? '' : data.obj.ysje + '元');
			// $('#lsje').html(data.obj.yfje == 0 ? '' : data.obj.yfje + '元');
		}
	});
}

//提交数据到后台
function saveAll(){
	var gysbh = $('#gysbh').html();
	var fkje = $('#fkje').val();
	// var lsje = $('#lsje').html();
	
	if(gysbh == ''){
		$.messager.alert('提示', '没有选中供应商进行付款,请重新操作！', 'error');
		return false;
	}
	if(fkje == ''){
		$.messager.alert('提示', '没有输入付款金额,请重新操作！', 'error');
		return false;
	}
	// if($('input[name=isLs]').is(':checked')){
	// 	if(Number(lsje) < Number(hkje)){
	// 		$.messager.alert('警告', '付款金额不能大于历史陈欠！',  'warning');
	// 		$('#hkje').html('');
	// 		$('#hkje').focus();
	// 		return false;
	// 	}
	// }
	
	var effectRow = new Object();
	var rows = rkfk_ywrkDg.datagrid('getRows');
	//将表头内容传入后台
	effectRow['gysbh'] = gysbh;
	effectRow['gysmc'] = $('#gysmc').html();
	// effectRow['ywyId'] = jxc_rkfk_ywyCombo.combobox('getValue');
	// effectRow['ywymc'] = jxc_rkfk_ywyCombo.combobox('getText');
	
	effectRow['fkje'] = fkje;
	effectRow['payTime'] = $('input[name=payTime]').val();
	effectRow['lastFkje'] = rows.size > 0 ? rows[countFk - 1].fkje : 0;
	effectRow['isYf'] = je > 0 ? '1' : '0';
	effectRow['yfje'] = je.toFixed(2);
	// effectRow['isLs'] = $('input[name=isLs]').is(':checked') ? '1' : '0';
	
	effectRow['bmbh'] = rkfk_did;
	effectRow['lxbh'] = rkfk_lx;
	effectRow['menuId'] = rkfk_menuId;
		
	//将表格中的数据去掉最后一个空行后，转换为json格式
	if(countHk > 0){
		effectRow['datagrid'] = JSON.stringify(rkfk_ywrkDg.datagrid('getRows').slice(0, countHk));
	}
	//提交到action
	$.ajax({
		type: "POST",
		url: '${pageContext.request.contextPath}/jxc/rkfkAction!save.action',
		data: effectRow,
		dataType: 'json',
		success: function(rsp){
			if(rsp.success){
		    	$.messager.show({
					title : '提示',
					msg : '提交成功！'
				});
		    	init();
		    	rkfk_ywrkDg.datagrid('load', {
	    			bmbh: rkfk_did,
	    			gysbh: $('#gysbh').html(),
	    			// ywyId: jxc_rkfk_ywyCombo.combobox('getValue'),
		    	});
			}  
		},
		error: function(){
			$.messager.alert("提示", "提交错误了！");
		}
	});
}

function printRkfk(){
	var gysbh = $('#gysbh').html();
	if(gysbh != ''){
		// var ywyId = jxc_rkfk_ywyCombo.combobox('getValue');
		
		var dialog = $('#jxc_rkfk_dateDialog');
		dialog.dialog({
			title : '请选择统计时间',
			//href : '${pageContext.request.contextPath}/jxc/khDet.jsp',
			width : 240,
			height : 120,
			buttons : [{
				text : '确定',
				handler : function() {
					var selectTime = $('input#selectTime').val();
					if(selectTime != ''){
						var url = lnyw.bp() + '/jxc/rkfkAction!printRkfk.action?bmbh=' + rkfk_did + '&gysbh=' + gysbh + "&selectTime=" + selectTime;
						jxc.print(url, PREVIEW_REPORT, HIDE_PRINT_WINDOW, {createId: ${user.id}, createName: "${user.realName}"});
						dialog.dialog('close');
					}else{
						$.messager.alert('提示', '请选择打印时间！', 'error');
						return false;
					}
				},
			},{
				text : '取消',
				handler : function() {
					dialog.dialog('close');
				},
			}],
			onLoad : function() {
				
			}
		});
	}else{
		$.messager.alert('提示', '没有选中供应商进行打印,请重新操作！', 'error');
		return false;
	}
}

function exportRkfk(){
	var gysbh = $('#gysbh').html();
	if(gysbh != ''){
		// var ywyId = jxc_rkfk_ywyCombo.combobox('getValue');
		
		var dialog = $('#jxc_rkfk_dateDialog');
		dialog.dialog({
			title : '请选择统计时间',
			//href : '${pageContext.request.contextPath}/jxc/khDet.jsp',
			width : 240,
			height : 120,
			buttons : [{
				text : '确定',
				handler : function() {
					var selectTime = $('input#selectTime').val();
					if(selectTime != ''){
						var data = {
								bmbh: rkfk_did,
								gysbh: gysbh,
								// ywyId: ywyId,
								selectTime: selectTime
								//type: 'rtf'
							};
						jxc.export('${pageContext.request.contextPath}', '/jxc/rkfkAction!exportRkfk.action', data);
						dialog.dialog('close');
					}else{
						$.messager.alert('提示', '请选择打印时间！', 'error');
						return false;
					}
				},
			},{
				text : '取消',
				handler : function() {
					dialog.dialog('close');
				},
			}],
			onLoad : function() {
				
			}
		});
	}else{
		$.messager.alert('提示', '没有选中供应商进行打印,请重新操作！', 'error');
		return false;
	}
}

//////////////////////////////////////////////以下为销售付款列表处理代码
function cancelRkfk(){
	var row = rkfk_dg.datagrid('getSelected');
	if (row != undefined) {
		if(row.isCancel != '1'){
			$.messager.confirm('请确认', '是否要取消选中的入库付款？', function(r){
				if(r) {
					$.ajax({
						url : '${pageContext.request.contextPath}/jxc/rkfkAction!cancelRkfk.action',
						data : {
							rkfklsh : row.rkfklsh,
							bmbh: rkfk_did,
							lxbh: rkfk_lx,
							menuId: rkfk_menuId,
						},
						method: 'post',
						dataType : 'json',
						success : function(d) {
							rkfk_dg.datagrid('load');
							rkfk_dg.datagrid('unselectAll');
							$.messager.show({
								title : '提示',
								msg : d.msg
							});
						}
					});
				}
			});
		}else{
			$.messager.alert('警告', '选中的入库付款记录已被冲减，请重新选择！',  'warning');
		}
	}else{
		$.messager.alert('警告', '请选择一条记录进行操作！',  'warning');
	}
}

function searchRkfk(){
	rkfk_dg.datagrid('load',{
		bmbh: rkfk_did,
		createTime: $('input[name=createTimeRkfk]').val(),
		search: $('input[name=searchRkfk]').val(),
	});
}

//////////////////////////////////////////////以上为入库付款列表处理代码

</script>
<div id="jxc_rkfk_tabs" class="easyui-tabs" data-options="fit:true, border:false," style="width:100%;height:100%;">
    <div title="新增记录" data-options="closable:false">
		<div id='jxc_rkfk_layout' style="height:100%;width:100%;">
			<div data-options="region:'west',title:'供应商',split:true" style="height:100%;width:300px">
<%--				<div id='jxc_rkfk_gysLayout' style="height:100%;width:100%;">--%>
<%--					<div data-options="region:'north',title:'业务员',split:true" style="height:80px;width:100%">--%>
<%--						请选择业务员：<input id="jxc_rkfk_ywyId" name="ywyId" size="16">--%>
<%--					</div>--%>
					<div data-options="region:'center',title:'供应商',split:true" style="height:100%;width:100%">
						<div id="jxc_rkfk_gysDg"></div>
					</div>
<%--				</div>--%>
			</div>
	    	<div data-options="region:'center',title:'明细',split:true, fit:true" style="height:100%;width:100%">
		    	<div id='jxc_rkfk_ywrkLayout' style="height:100%;width:100%;">
					<div data-options="region:'north',title:'商品分类',split:true" style="height:140px;width:100%">
						<table class="tinfo">
							<tr>
								<td></td>
								<td></td>
								<th class="read">时间</th><td colspan="3"><div id="createDate" class="read"></div></td>
								<th class="read">单据号</th><td colspan="4"><div id="rkfkLsh" class="read"></div></td>
							</tr>
							<tr>
							<td colspan="10"><hr/></td>
							</tr>
							<tr class="read">
								<th>供应商编号:</th><td><div id="gysbh"></div></td>
								<th>供应商名称:</th><td colspan="3"><div id="gysmc"></div></td>
							</tr>
							<tr class="read">
<%--								<th>客户类型:</th><td style="width:150px"><div id="khlx"></div></td>--%>
								<th>授信期:</th><td style="width:150px"><div id="sxzq"></div></td>
								<th>授信金额:</th><td style="width:150px"><div id="sxje"></div></td>
							</tr>
							<tr class="read">
								<th>应付总额:</th><td><div id="yfje"></div></td>
<!-- 								<th>销售应收:</th><td><div id="ysje"></div></td> -->
<%--								<th>历史应收:</th><td style="width:100px"><div id="lsje"></div></td>--%>
							</tr>
							<tr>
								<th>付款金额</th><td><input id="fkje" name="fkje" type="text" size="8">元</td>
								<th>付款日期</th><td><input type="text" name="payTime" id="payTime" class="easyui-datebox" data-options="value: moment().format('YYYY-MM-DD')" style="width:100px"></td>
<%--								<th>历史付款</th><td><input name="isLs" type="checkbox"></td>--%>
							</tr>
						</table>
					</div>
					<div data-options="region:'center',title:'商品分类',split:true" style="height:100%;width:100%">
		    			<div id='jxc_rkfk_ywrkDg'></div>
					</div>
				</div>
			</div>
		</div>
	</div>
    <div title="入库付款列表" data-options="closable:false" >
    	<div id='jxc_rkfk_dg'></div>
    </div>
</div>
<div id="jxc_rkfk_tb" style="padding:3px;height:auto">
	请输入查询起始日期:<input type="text" name="createTimeRkfk" class="easyui-datebox" data-options="value: moment().date(1).format('YYYY-MM-DD')" style="width:100px">
	输入流水号、供应商：<input type="text" name="searchRkfk" style="width:100px">
	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchRkfk();">查询</a>
</div>
<div id='jxc_rkfk_dateDialog'>
<br>
<center>
<input type="text" name="selectTime" class="easyui-my97" data-options="dateFmt:'yyyy年MM月',minDate:'{%y-1}-%M-%d',maxDate:'%y-%M-%d',vel:'selectTime'" style="width:100px">
</center>
<input id="selectTime" type="hidden"/>
</div>


	
