<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<script type="text/javascript">
var did;
var lx;
var menuId;
var xshk_khDg;
var xshk_xskpDg;
var jxc_xshk_ywyCombo;

//本次回款对应发票笔数
var countHk;
//每行回款后剩余金额
var je;
var lastHkje;

$(function(){
	did = lnyw.tab_options().did;
	lx = lnyw.tab_options().lx;
	menuId = lnyw.tab_options().id;
	
	$('#jxc_xshk_layout').layout({
		fit : true,
		border : false,
	});
	
	xshk_khDg = $('#jxc_xshk_khDg').datagrid({
		url : '${pageContext.request.contextPath}/jxc/khAction!listKhByYwy.action',
		queryParams :{
			depId : did,
		},
		fit : true,
	    border : false,
	    singleSelect : true,
// 	    pagination : true,
// 		pagePosition : 'bottom',
// 		pageSize : pageSize,
// 		pageList : pageList,
		columns:[[
	        {field:'khbh',title:'客户编号',width:60},
	        {field:'khmc',title:'客户名称',width:200},
	    ]],
	    onSelect: function(rowIndex, rowData){
	    	xshk_xskpDg.datagrid('load', {
    			bmbh:did,
    			khbh:rowData.khbh,
    			onBeforeLoad: function(){
	   	 			$('#khbh').html(rowData.khbh);
	   	 			$('#khmc').html(rowData.khmc);
    				$('#khlx').html('');
    				$('#sxzq').html('');
    				$('#sxje').html('');
    				$('#yfje').html('');
    			},
    			
	    	});
	    }
	});
	
	xshk_xskpDg = $('#jxc_xshk_xskpDg').datagrid({
		url:'${pageContext.request.contextPath}/jxc/xskpAction!getXskpNoHk.action',
		queryParams:{
			bmbh:did,
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
			$('#khlx').html(data.obj.khlxmc);
			$('#sxzq').html(data.obj.sxzq + '天');
			$('#sxje').html(data.obj.sxje + '元');
			$('#yfje').html(data.obj.yfje == 0 ? '' : data.obj.yfje + '元');
		}
	});

	//初始化业务员列表
	jxc_xshk_ywyCombo = lnyw.initCombo($("#jxc_xshk_ywyId"), 'id', 'realName', '${pageContext.request.contextPath}/admin/userAction!listYwys.action?did=' + did);
	
	jxc_xshk_ywyCombo.combobox({
		onSelect: function(){
			xshk_khDg.datagrid('load', {
				depId: did,
				ywyId: jxc_xshk_ywyCombo.combobox('getValue')
			});
		}
	});
	
	$('#hkje').keyup(function() {
		countHk = 0;
		var rows = xshk_xskpDg.datagrid('getRows');
		//本次回款金额
		je = Number($('#hkje').val());
		if(rows != undefined){
			$.each(rows, function(index){
				if(je != 0){
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
					countHk++;
				if(je == 0){
					console.info('countHk:' + countHk);
					console.info('lastHkje:' + lastHkje);
					return false;
				}
			});
		}else{
			$.messager.alert('提示', '回款的客户没有销售记录！', 'error');
		}
	});
	
	//初始化信息
	init();
	
	
});

//以下为商品列表处理代码
function init(){
	//清空全部字段
	$('input').val('');
	
	//jxc_xshk_ywyCombo.combobox('selectedIndex', 0);
	
	//初始化创建时间
	$('#createDate').html(moment().format('YYYY年MM月DD日'));
	
	//初始化流水号
	$.ajax({
		type: "POST",
		url: '${pageContext.request.contextPath}/jxc/lshAction!getLsh.action',
		data: {
			bmbh: did,
			lxbh: lx,
		},
		dataType: 'json',
		success: function(d){
			if(d.success){
				$('#xshkLsh').html(d.obj);
			}  
		},
	});
	//根据权限，动态加载功能按钮
 	lnyw.toolbar(0, xshk_xskpDg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', did);
}

function selectKh(rowData){
	//xshk_xskpDg.datagrid('clear');
	//var row = xshk_khDg.datagrid('getSelected');
	xshk_xskpDg.datagrid({
		url:'${pageContext.request.contextPath}/jxc/xskpAction!getXskpNoHk.action',
		queryParams:{
			bmbh:did,
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
			$('#yfje').html(data.obj.yfje == 0 ? '' : data.obj.yfje + '元');
		}
	});
}

//提交数据到后台
function saveAll(){
	var khbh = $('#khbh').html();
	var hkje = $('#hkje').val();
	
	if(khbh == ''){
		$.messager.alert('提示', '没有选中客户进行回款,请重新操作！', 'error');
		return false;
	}
	if(hkje == ''){
		$.messager.alert('提示', '没有输入回款金额,请重新操作！', 'error');
		return false;
	}
	
	var effectRow = new Object();
	
	//将表头内容传入后台
	effectRow['khbh'] = khbh;
	effectRow['khmc'] = $('#khmc').html();
	effectRow['hkje'] = hkje;
	effectRow['lastHkje'] = lastHkje;
	effectRow['isYf'] = je > 0 ? '1' : '0';
	
	effectRow['bmbh'] = did;
	effectRow['lxbh'] = lx;
	effectRow['menuId'] = menuId;
	
	//将表格中的数据去掉最后一个空行后，转换为json格式
	
	effectRow['datagrid'] = JSON.stringify(xshk_xskpDg.datagrid('getRows').slice(0, countHk));
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
	    			bmbh:did,
	    			khbh:$('#khbh').html(),
		    	});
			}  
		},
		error: function(){
			$.messager.alert("提示", "提交错误了！");
		}
	});
}
</script>
<div id='jxc_xshk_layout' style="height:100%;width=100%">
	<div data-options="region:'west',title:'业务员-客户',split:true" style="height:100%;width:300px">
		<div id='jxc_xshk_khLayout' style="height:100%;width=100%">
			<div data-options="region:'north',title:'商品分类',split:true" style="height:50px;width:100%">
				请选择业务员：<input id="jxc_xshk_ywyId" name="ywyId" size="16">
			</div>
			<div data-options="region:'center',title:'商品分类',split:true" style="height:100%;width:100%">
				<div id="jxc_xshk_khDg"></div>
			</div>
		</div>
	</div>
    <div data-options="region:'center',title:'明细',split:true, fit:true" style="height:100%;width:100%">
    	<div id='jxc_xshk_xskpLayout' style="height:100%;width=100%">
			<div data-options="region:'north',title:'商品分类',split:true" style="height:120px;width:100%">
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
						<th>客户编号</th><td><div id="khbh"></div></td>
						<th>客户名称</th><td colspan="3"><div id="khmc"></div></td>
						<th>客户类型</th><td><div id="khlx"></div></td>
					</tr>
					<tr class="read">
						<th>授信期</th><td><div id="sxzq"></div></td>
						<th>授信金额</th><td style="width:100px"><div id="sxje"></div></td>
						<th>历史陈欠</th><td style="width:100px"><div id="yfje">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div></td>
						<th>目前欠款</th><td><div id="ysje"></div></td>
					</tr>
					<tr>
						<th>本次还款金额</th><td><input id="hkje" name="hkje" type="text" size="8">元</td>
					</tr>
				</table>
			</div>
			<div data-options="region:'center',title:'商品分类',split:true" style="height:100%;width:100%">
    			<div id='jxc_xshk_xskpDg'></div>
			</div>
		</div>
    </div>
</div>


	
