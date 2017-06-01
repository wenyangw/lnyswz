<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<script type="text/javascript" src="${pageContext.request.contextPath}/js/datagrid-filter.js"></script>
<script type="text/javascript">
var xshk_did;
var xshk_lx;
var xshk_menuId;
var xshk_khDg;
var xshk_dg;
var xshk_xskpDg;
var jxc_xshk_ywyCombo;

//销售列表所有行
var rows;

//本次回款对应发票笔数
var countHk;
var countXshk;
var countXskpInXshk;
//每行回款后剩余金额
var je;
var lastHkje;

$(function(){
	xshk_did = lnyw.tab_options().did;
	xshk_lx = lnyw.tab_options().lx;
	xshk_menuId = lnyw.tab_options().id;
	
	$('#jxc_xshk_layout').layout({
		fit : true,
		border : false,
	});
	
	$('#jxc_xshk_khLayout').layout({
		fit : true,
		border : false,
	});
	
	//初始化业务员列表
	jxc_xshk_ywyCombo = lnyw.initCombo($("#jxc_xshk_ywyId"), 'id', 'realName', '${pageContext.request.contextPath}/admin/userAction!listYwys.action?did=' + xshk_did);
	
	xshk_khDg = $('#jxc_xshk_khDg').datagrid({
// 		url : '${pageContext.request.contextPath}/jxc/khAction!listKhByYwy.action',
// 		queryParams :{
// 			depId : xshk_did,
// 		},
		fit : true,
	    border : false,
	    singleSelect : true,
		columns:[[
	        {field:'khbh',title:'客户编号',width:60,
	        	hfilter:{type:'textbox',},
	        },
	        {field:'khmc',title:'客户名称',width:200},
	    ]],
	    toolbar:'#jxc_xshk_khTb',
	    onSelect: function(rowIndex, rowData){
	    	$('#khbh').html(rowData.khbh);
	 		$('#khmc').html(rowData.khmc);
			$('#khlx').html('');
			$('#sxzq').html('');
			$('#sxje').html('');
			$('#ysje').html('');
			$('#lsje').html('');

			xshk_xskpDg.datagrid('load', {
    			bmbh:xshk_did,
    			khbh:rowData.khbh,
    			ywyId: jxc_xshk_ywyCombo.combobox('getValue'),
	    	});
	    }
	});
	
	xshk_dg = $('#jxc_xshk_dg').datagrid({
// 		url:'${pageContext.request.contextPath}/jxc/xshkAction!datagrid.action',
// 		queryParams:{
// 			bmbh:xshk_did,
// 		},
		fit : true,
	    border : false,
	    singleSelect : true,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
		columns:[[
	        {field:'xshklsh',title:'流水号',width:100,align:'center'},
	        {field:'createTime',title:'创建时间',width:100,align:'center',
	        	formatter:function(value){
	        		return moment(value).format('YYYY-MM-DD');
	        	}},
	        {field:'createName',title:'创建人',width:100,align:'center'},
	        {field:'payTime',title:'回款时间',width:100,align:'center',
	        	formatter:function(value){
	        		return moment(value).format('YYYY-MM-DD');
	        	}},	
	        {field:'khbh',title:'客户编号',width:100,align:'center',hidden:true},
	        {field:'khmc',title:'客户名称',width:300,align:'center'},
	        {field:'ywyId',title:'业务员id',width:100,align:'center',hidden:true},
	        {field:'ywymc',title:'业务员',width:100,align:'center'},
	        {field:'hkje',title:'还款金额',width:100,align:'center'},
	        {field:'isYf',title:'预付',align:'center',
	        	formatter : function(value) {
					if (value == '1') {
						return '是';
					} else {
						return '';
					}
				}},
	        {field:'isLs',title:'历史',align:'center',
	        	formatter : function(value) {
					if (value == '1') {
						return '是';
					} else {
						return '';
					}
				}},
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
        	{field:'cancelXshklsh',title:'原回款流水号',align:'center'},
	    ]],
	    toolbar:'#jxc_xshk_tb',
	});
	lnyw.toolbar(1, xshk_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', xshk_did);
	
	xshk_dg.datagrid({
        view: detailview,
        detailFormatter:function(index,row){
            return '<div style="padding:2px"><table id="xshk-ddv-' + index + '"></table></div>';
        },
        onExpandRow: function(index,row){
            $('#xshk-ddv-'+index).datagrid({
                url:'${pageContext.request.contextPath}/jxc/xshkAction!detDatagrid.action',
                fitColumns:true,
                singleSelect:true,
                rownumbers:true,
                loadMsg:'',
                height:'auto',
                queryParams: {
        			xshklsh: row.xshklsh,
        		},
                columns:[[
                    {field:'xskplsh',title:'销售流水号',width:200,align:'center'},
                    {field:'createTime',title:'开票时间',width:200,align:'center'},
                    {field:'hjje',title:'发票金额',width:100,align:'center'},
                    {field:'hkje',title:'回款金额',width:100,align:'center'},
                ]],
                onResize:function(){
                	xshk_dg.datagrid('fixDetailRowHeight',index);
                },
                onLoadSuccess:function(){
                    setTimeout(function(){
                    	xshk_dg.datagrid('fixDetailRowHeight',index);
                    },0);
                }
            });
            xshk_dg.datagrid('fixDetailRowHeight',index);
        }
    });
		
	xshk_xskpDg = $('#jxc_xshk_xskpDg').datagrid({
		url:'${pageContext.request.contextPath}/jxc/xskpAction!getXskpNoHk.action',
		queryParams:{
			bmbh:xshk_did,
		},
		fit : true,
	    border : false,
	    singleSelect : true,
// 	    pagination : true,
// 		pagePosition : 'bottom',
// 		pageSize : pageSize,
// 		pageList : pageList,
		columns:[[
	        {field:'xskplsh',title:'流水号',width:100,align:'center'},
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
	        {field:'hkedje',title:'已还金额',width:100,align:'center',
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
	        {field:'hkje',title:'本次还款金额',width:100,align:'center',
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
	    	if($('#khbh').html() != ''){
				$('#khlx').html(data.obj.khlxmc);
				$('#sxzq').html(data.obj.sxzq + '天');
				$('#sxje').html(data.obj.sxje + '元');
				$('#yszje').html(data.obj.yszje == 0 ? '' : data.obj.yszje + '元');
				$('#ysje').html(data.obj.ysje == 0 ? '' : data.obj.ysje + '元');
				$('#lsje').html(data.obj.lsje == 0 ? '' : data.obj.lsje + '元');
	    	}
	    	rows = xshk_xskpDg.datagrid('getRows');
		}
	});
	//根据权限，动态加载功能按钮
 	lnyw.toolbar(0, xshk_xskpDg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', xshk_did);

 	//选中列表标签后，装载数据
	xskp_tabs = $('#jxc_xshk_tabs').tabs({
		onSelect: function(title, index){
			if(index == 0){
				xshk_xskpDg.datagrid('load', {
    				bmbh:xshk_did,
    				khbh:$('#khbh').html(),
    				ywyId: jxc_xshk_ywyCombo.combobox('getValue'),
	    		});
			}
			if(index == 1){
				xshk_dg.datagrid({
					url: '${pageContext.request.contextPath}/jxc/xshkAction!datagrid.action',
					queryParams:{
						bmbh: xshk_did,
						createTime: countXshk == 0 ? undefined : $('input[name=createTimeXshk]').val(),
						search: countXshk == 0 ? undefined : $('input[name=searchXshk]').val(),
					}
				});
				countXshk++;
			}
		},
	});
	
	jxc_xshk_ywyCombo.combobox({
		onSelect: function(){
			xshk_khDg.datagrid({
				url : '${pageContext.request.contextPath}/jxc/khAction!listKhByYwy.action',
		 		queryParams :{
		 			depId : xshk_did,
		 			ywyId: jxc_xshk_ywyCombo.combobox('getValue')
		 		},

// 				'load', {
// 				depId: xshk_did,
// 				ywyId: jxc_xshk_ywyCombo.combobox('getValue')
			});
			xshk_khDg.datagrid('enableFilter');
		}
	});
	
	$('#hkje').keyup(function() {
		
		//如回款为历史陈欠，不处理下面的代码
		if($('input[name=isLs]').is(':checked')){
			return false;
		}
		
		countHk = 0;
// 		if(rows == undefined){
// 			rows = xshk_xskpDg.datagrid('getRows');
// 		}
		//本次回款金额
		je = Number($('#hkje').val());
		if(rows != undefined){
			$.each(rows, function(index){
				if(je != 0){
					countHk++;
				}
				//每行回款金额
				lastHkje = 0;
				//每行未回款金额
				var whkje = rows[index].hjje - rows[index].hkedje;
				if(je > whkje){
					lastHkje = whkje;
				}else{
					lastHkje = je;
				}
				je -= lastHkje;
				xshk_xskpDg.datagrid('updateRow', {
					index:index,
					row: {
						hkje: lastHkje.toFixed(4),
					}
				});
				
// 				if(je == 0){
// 					return false;
// 				}
			});
		}else{
			$.messager.alert('提示', '回款的客户没有销售记录！', 'error');
		}
	});
	
	jxc_xshk_ywyCombo.combobox('selectedIndex', 0);
	//初始化信息
	init();
	
	$('input[name=isLs]').click(function(){
 		if($('input[name=isLs]').is(':checked')){
			if($('#lsje').html() == ''){
				$.messager.alert('警告', '该客户无历史陈欠！',  'warning');
				$('input[name=isLs]').removeAttr('checked');
				$('input[name=isLs]').removeProp('checked');
				return false;
			}
			//清除已填写的销售发票明细数据
			var rows = xshk_xskpDg.datagrid('getRows');
			if(rows != undefined){
				$.each(rows, function(index){
					xshk_xskpDg.datagrid('updateRow', {
						index:index,
						row: {
							hkje: 0,
						}
					});
				});
			}
		}
	});
	
	
});

//以下为商品列表处理代码
function init(){
	//清空全部字段
	$('input[name=hkje]').val('');
	
	//$('input:checkbox').removeAttr('checked');
	//$('input:checkbox').removeProp('checked');
	$('input:checkbox').prop('checked', false);
	
	//jxc_xshk_ywyCombo.combobox('selectedIndex', 0);
	
	rows = undefined;
	
	//初始化创建时间
	$('#createDate').html(moment().format('YYYY年MM月DD日'));
	
	//初始化流水号
	$.ajax({
		type: "POST",
		url: '${pageContext.request.contextPath}/jxc/lshAction!getLsh.action',
		data: {
			bmbh: xshk_did,
			lxbh: xshk_lx,
		},
		dataType: 'json',
		success: function(d){
			if(d.success){
				$('#xshkLsh').html(d.obj);
			}  
		},
	});
	
}

function selectKh(rowData){
	//xshk_xskpDg.datagrid('clear');
	//var row = xshk_khDg.datagrid('getSelected');
	xshk_xskpDg.datagrid({
		url:'${pageContext.request.contextPath}/jxc/xskpAction!getXskpNoHk.action',
		queryParams:{
			bmbh:xshk_did,
			khbh:rowData.khbh
		},
		onBeforeLoad: function(){
			$('#khbh').html(row.khbh);
			$('#khmc').html(row.khmc);
		},
		onLoadSuccess:function(data){
			$('#khlx').html(data.obj.khlxmc);
			$('#sxzq').html(data.obj.sxzq + '天');
			$('#sxje').html(data.obj.sxje + '元');
			$('#ysje').html(data.obj.ysje == 0 ? '' : data.obj.ysje + '元');
			$('#lsje').html(data.obj.yfje == 0 ? '' : data.obj.yfje + '元');
		}
	});
}

//提交数据到后台
function saveAll(){
	var khbh = $('#khbh').html();
	var hkje = $('#hkje').val();
	var lsje = $('#lsje').html();
	
	if(khbh == ''){
		$.messager.alert('提示', '没有选中客户进行回款,请重新操作！', 'error');
		return false;
	}
	if(hkje == ''){
		$.messager.alert('提示', '没有输入回款金额,请重新操作！', 'error');
		return false;
	}
	if($('input[name=isLs]').is(':checked')){
		if(Number(lsje) < Number(hkje)){
			$.messager.alert('警告', '回款金额不能大于历史陈欠！',  'warning');
			$('#hkje').html('');
			$('#hkje').focus();
			return false;
		}
	}
	
	var effectRow = new Object();
	var rows = xshk_xskpDg.datagrid('getRows');
	//将表头内容传入后台
	effectRow['khbh'] = khbh;
	effectRow['khmc'] = $('#khmc').html();
	effectRow['ywyId'] = jxc_xshk_ywyCombo.combobox('getValue');
	effectRow['ywymc'] = jxc_xshk_ywyCombo.combobox('getText');
	
	effectRow['hkje'] = hkje;
	effectRow['payTime'] = $('input[name=payTime]').val();
	effectRow['lastHkje'] = rows.size > 0 ? rows[countHk - 1].hkje : 0;
	effectRow['isYf'] = je > 0 ? '1' : '0';
	effectRow['isLs'] = $('input[name=isLs]').is(':checked') ? '1' : '0';
	
	effectRow['bmbh'] = xshk_did;
	effectRow['lxbh'] = xshk_lx;
	effectRow['menuId'] = xshk_menuId;
		
	//将表格中的数据去掉最后一个空行后，转换为json格式
	if(countHk > 0){
		effectRow['datagrid'] = JSON.stringify(xshk_xskpDg.datagrid('getRows').slice(0, countHk));
	}
	//提交到action
	$.ajax({
		type: "POST",
		url: '${pageContext.request.contextPath}/jxc/xshkAction!save.action',
		data: effectRow,
		dataType: 'json',
		success: function(rsp){
			if(rsp.success){
		    	$.messager.show({
					title : '提示',
					msg : '提交成功！'
				});
		    	init();
		    	xshk_xskpDg.datagrid('load', {
	    			bmbh: xshk_did,
	    			khbh: $('#khbh').html(),
	    			ywyId: jxc_xshk_ywyCombo.combobox('getValue'),
		    	});
			}  
		},
		error: function(){
			$.messager.alert("提示", "提交错误了！");
		}
	});
}

function printXshk(){
	var khbh = $('#khbh').html();
	if(khbh != ''){
		var ywyId = jxc_xshk_ywyCombo.combobox('getValue');
		
		var dialog = $('#jxc_xshk_dateDialog');
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
						var url = lnyw.bp() + '/jxc/xshkAction!printXshk.action?bmbh=' + xshk_did + '&khbh=' + khbh + "&ywyId=" + ywyId + "&selectTime=" + selectTime;
						jxc.print(url, PREVIEW_REPORT, HIDE_PRINT_WINDOW);
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
		$.messager.alert('提示', '没有选中客户进行打印,请重新操作！', 'error');
		return false;
	}
}

function exportXshk(){
	var khbh = $('#khbh').html();
	if(khbh != ''){
		var ywyId = jxc_xshk_ywyCombo.combobox('getValue');
		
		var dialog = $('#jxc_xshk_dateDialog');
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
						//var url = lnyw.bp() + '/jxc/xshkAction!printXshk.action?bmbh=' + xshk_did + '&khbh=' + khbh + "&ywyId=" + ywyId + "&selectTime=" + selectTime;
						//jxc.print(url, PREVIEW_REPORT, HIDE_PRINT_WINDOW);
						var data = {
								bmbh: xshk_did,
								khbh: khbh,
								ywyId: ywyId,
								selectTime: selectTime,
								//type: 'rtf'
							};
						jxc.export('${pageContext.request.contextPath}', '/jxc/xshkAction!exportXshk.action', data);
						
// 						$.ajax({	
// 							url:'${pageContext.request.contextPath}/jxc/xshkAction!exportXshk.action',
// 							async: false,
// 							cache: false,
// 							context:this,	
// 							data : {
// 								bmbh: xshk_did,
// 								khbh: khbh,
// 								ywyId: ywyId,
// 								selectTime: selectTime
// 							},
// 							success:function(data){
// 								var json = $.parseJSON(data);
								
// 								window.open("${pageContext.request.contextPath}/"+json.obj);
								
// 								$.messager.show({
// 									title : "提示",
// 									msg : json.msg
// 								});
// 							},
// 							complete: function(){
// 								//lnyw.MaskUtil.unmask();
// 							}
// 						});
						
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
		$.messager.alert('提示', '没有选中客户进行打印,请重新操作！', 'error');
		return false;
	}
}

//////////////////////////////////////////////以下为销售回款列表处理代码
function cancelXshk(){
	var row = xshk_dg.datagrid('getSelected');
	if (row != undefined) {
		if(row.isCancel != '1'){
			$.messager.confirm('请确认', '是否要取消选中的销售回款？', function(r){
				if(r) {
					$.ajax({
						url : '${pageContext.request.contextPath}/jxc/xshkAction!cancelXshk.action',
						data : {
							xshklsh : row.xshklsh,
							bmbh: xshk_did,
							lxbh: xshk_lx,
							menuId: xshk_menuId,
						},
						method: 'post',
						dataType : 'json',
						success : function(d) {
							xshk_dg.datagrid('load');
							xshk_dg.datagrid('unselectAll');
							$.messager.show({
								title : '提示',
								msg : d.msg
							});
						}
					});
				}
			});
		}else{
			$.messager.alert('警告', '选中的销售回款记录已被冲减，请重新选择！',  'warning');
		}
	}else{
		$.messager.alert('警告', '请选择一条记录进行操作！',  'warning');
	}
}

function searchXshk(){
	xshk_dg.datagrid('load',{
		bmbh: xshk_did,
		createTime: $('input[name=createTimeXshk]').val(),
		search: $('input[name=searchXshk]').val(),
	});
}

//////////////////////////////////////////////以上为销售回款列表处理代码

</script>
<div id="jxc_xshk_tabs" class="easyui-tabs" data-options="fit:true, border:false," style="width:100%;height:100%;">
    <div title="新增记录" data-options="closable:false">
		<div id='jxc_xshk_layout' style="height:100%;width=100%">
			<div data-options="region:'west',title:'业务员-客户',split:true" style="height:100%;width:300px">
				<div id='jxc_xshk_khLayout' style="height:100%;width=100%">
					<div data-options="region:'north',title:'业务员',split:true" style="height:80px;width:100%">
						请选择业务员：<input id="jxc_xshk_ywyId" name="ywyId" size="16">
					</div>
					<div data-options="region:'center',title:'客户',split:true" style="height:100%;width:100%">
						<div id="jxc_xshk_khDg"></div>
					</div>
				</div>
			</div>
	    	<div data-options="region:'center',title:'明细',split:true, fit:true" style="height:100%;width:100%">
		    	<div id='jxc_xshk_xskpLayout' style="height:100%;width=100%">
					<div data-options="region:'north',title:'商品分类',split:true" style="height:140px;width:100%">
						<table class="tinfo">
							<tr>
								<td></td>
								<td></td>
								<th class="read">时间</th><td colspan="3"><div id="createDate" class="read"></div></td>
								<th class="read">单据号</th><td colspan="4"><div id="xshkLsh" class="read"></div></td>
							</tr>
							<tr>
							<td colspan="10"><hr/></td>
							</tr>
							<tr class="read">
								<th>客户编号:</th><td><div id="khbh"></div></td>
								<th>客户名称:</th><td colspan="3"><div id="khmc"></div></td>
							</tr>
							<tr class="read">
								<th>客户类型:</th><td style="width:150px"><div id="khlx"></div></td>
								<th>授信期:</th><td style="width:150px"><div id="sxzq"></div></td>
								<th>授信金额:</th><td style="width:150px"><div id="sxje"></div></td>
							</tr>
							<tr class="read">
								<th>应收总额:</th><td><div id="ysje"></div></td>
<!-- 								<th>销售应收:</th><td><div id="ysje"></div></td> -->
								<th>历史应收:</th><td style="width:100px"><div id="lsje"></div></td>
							</tr>
							<tr>
								<th>还款金额</th><td><input id="hkje" name="hkje" type="text" size="8">元</td>
								<th>还款日期</th><td><input type="text" name="payTime" id="payTime" class="easyui-datebox" data-options="value: moment().format('YYYY-MM-DD')" style="width:100px"></td>
								<th>历史回款</th><td><input name="isLs" type="checkbox"></td>
							</tr>
						</table>
					</div>
					<div data-options="region:'center',title:'商品分类',split:true" style="height:100%;width:100%">
		    			<div id='jxc_xshk_xskpDg'></div>
					</div>
				</div>
			</div>
		</div>
	</div>
    <div title="销售回款列表" data-options="closable:false" >
    	<div id='jxc_xshk_dg'></div>
    </div>
</div>
<div id="jxc_xshk_tb" style="padding:3px;height:auto">
	请输入查询起始日期:<input type="text" name="createTimeXshk" class="easyui-datebox" data-options="value: moment().date(1).format('YYYY-MM-DD')" style="width:100px">
	输入流水号、客户：<input type="text" name="searchXshk" style="width:100px">
	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchXshk();">查询</a>
</div>
<div id='jxc_xshk_dateDialog'>
<br>
<center>
<input type="text" name="selectTime" class="easyui-my97" data-options="dateFmt:'yyyy年MM月',minDate:'{%y-1}-%M-%d',maxDate:'%y-%M-%d',vel:'selectTime'" style="width:100px">
</center>
<input id="selectTime" type="hidden"/>
</div>


	
