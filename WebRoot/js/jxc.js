/**
 * 进销存业务中处理方法
 * 
 * @author 王文阳
 * 
 * @version 20131130
 */

var jxc = $.extend({}, jxc);/* 定义全局对象，类似于命名空间或包的作用 */

//系统设定，是否需要进行审核(Constant.java同步)
var NEED_AUDIT = '1';
var AUDIT_REFUSE = '9';

jxc.auditLevel = function(bmbh){
	var level = Object.create(Object.prototype);
	switch (bmbh) {
	case '01':
		level['first'] = '1';
		level['second'] = '3';
		return level;
		break;
	case '04':
		level['first'] = '1';
		level['second'] = '2';
		return level;
		break;
	case '05':
		level['first'] = '1';
		level['second'] = '2';
		return level;
		break;
	case '07':
		level['first'] = '1';
		level['second'] = '3';
		return level;
		break;
	case '08':
		level['first'] = '1';
		level['second'] = '3';
		return level;
		break;
	default:
		break;
	}
};

jxc.getAuditLevel = function(url, bmbh, khbh, ywyId, jsfsId){
	var payTime = undefined;
	var isUp = undefined;
	var postponeDay = undefined;
	$.ajax({
		url: url,
		data: {
			bmbh: bmbh,
			khbh: khbh,
			ywyId: ywyId,
		},
		cache: false,
		async: false,
		dataType: 'json',
		success: function(data){
			payTime = data.obj.payTime;
			isUp = data.obj.isUp;
			postponeDay = Number(data.obj.postponeDay);
		}
	});
	
	//超期时间大于延期日期，禁止提货
	if(postponeDay > 0 && moment().diff(payTime, 'days') > postponeDay){
		return undefined;
	}else{
		//延期天数为0(非授信客户)或超期天数大于0的非优质客户，进行二级审批
		if(postponeDay == 0 || (moment().diff(payTime, 'days') > 0 && isUp == '1')){
			return jxc.auditLevel(bmbh)['second'];
		}else{
			//优质客户超期30天进行二级审批，否则进行一级审批
			if(moment().diff(payTime, 'days') > 30){
				return jxc.auditLevel(bmbh)['second'];
			}else{
				return jxc.auditLevel(bmbh)['first'];
			}
		}
	}
};

jxc.notInExcludeKhs = function(bmbh, khbh){
	switch (bmbh) {
	case '04':
		//教材公司，股份公司和辽海公司不参与审批流程	
		var kh04 = ['21010017', '21010798', '21010103'];
		if(kh04.indexOf(khbh) >= 0){
			return false;
		}else{
			return true;
		}
		break;
	default:
		return true;
		break;
	}
};


var ywbms = [{
    "id": '01',
    "depName": "文达印刷 "
},{
    "id": '04',
    "depName": "教材公司"
},{
    "id": '05',
    "depName": "文达纸业"
},{
    "id": '07',
    "depName": "经营拓展部"
},{
    "id": '08',
    "depName": "大连分公司"
},];

//销售欠款值
var JSFS_QK = '06';
var LENGTH_JE = 4;
var LENGTH_SL = 3;
var SL = 0.17;
var PRINT_REPORT = '1';
var PREVIEW_REPORT = '0';
var SHOW_PRINT_WINDOW = '1';
var HIDE_PRINT_WINDOW = '0';


jxc.otherBm = function(bmbh){
	var bm = Object.create(Object.prototype);
	switch (bmbh) {
	case '04':
		bm['bmbh'] = '05';
		bm['gysbh'] = '21010074';
		bm['gysmc'] = '辽宁文达纸业有限公司';
		return bm;
		break;
	case '05':
		bm['bmbh'] = '04';
		bm['gysbh'] = '21010004';
		bm['gysmc'] = '辽宁印刷物资有限责任公司';
		return bm;
		break;
	default:
		break;
	}
};

jxc.earliestXskp = function(area, url, params){
	$(area).layout('expand', 'east');
	
	$.ajax({
		url: url,
		data: params,
		cache: false,
		dataType: 'json',
		success: function(data){
			$('#show_spkc').propertygrid({
				fit : true,
				data: data.rows,
				showGroup: true,
				scrollbarSize: 0,
				showHeader:false,
				groupFormatter: groupFormatter,
			});
		}
	});
};


jxc.showFh = function(bmbh){
	switch(bmbh){
	case '04':
		return true;
	default:
		return false;
	}
};

jxc.showBookmc = function(bmbh){
	switch(bmbh){
	case '04':
		return true;
	default:
		return false;
	}
};

jxc.checkKh = function(needAudit, khbh, depId){
	if(needAudit == '1'){
		return '需要审核';
	}else{
		return '不需要审核';
	}
};


jxc.toJson = function(str) {
	 return str.substr(0, str.indexOf('<') > 0 ? str.indexOf('<') : str.length);
};

//var dictType = [ {
//	value : '00',
//	text : '变量'
//},  {
//	value : '01',
//	text : '字段'
//}, {
//	value : '02',
//	text : '表'
//},{
//	value : '03',
//	text : '视图'
//} ];
//
//var dictDisplay = [ {
//	value :'00',
//	text :'无',
//},{
//	value:'01',
//	text:'可查'
//},{
//	value:'02',
//	text:'不可查'
//}];
// 
//var dictOpe = [ {
//	value :'=',
//	text :'等于',
//},{
//	value:'>',
//	text:'大于'
//},{
//	value:'<',
//	text:'小于'
//},{
//	value:'>=',
//	text:' 大于等于'
//},{
//	value:'<=',
//	text:'小于等于'
//},{
//	value:'!=',
//	text:'不等于'
//},{
//	value:'1',
//	text:'类似'
//},{
//	value:'2',
//	text:'左类似'
//},{
//	value:'3',
//	text:'右类似'
//}];
//
////初始化combobox
//lnyw.initCombo = function(target, key, value, url){
//	return target.combobox({
//	    url: url,
//	    valueField: key,
//	    textField: value,
//	    panelHeight: 'auto',
//	    onLoadSuccess:function(){
//	    	target.combobox('selectedIndex', 0);
//	    }
//	});
//},
//
////动态加载功能按钮
//lnyw.toolbar = function(tabId, panel, url, did) {
//	var mid = $('#layout_center_tabs').tabs('getSelected').panel('options').id;
//	$.ajax({
//		url : url,
//		data : {
//			mid : mid,
//			tabId: tabId,
//			did : did,
//		},
//		dataType : 'json',
//		success : function(d) {
//			if(d != null){
//				panel.datagrid("addToolbarItem",d);
//			}
//		}
//	});
//	return;
//};

jxc.spInfo = function(target, type, sppp, spbz){
	if(type != ''){
		var info='';
		if(sppp != '' && sppp != undefined && $.trim(sppp).length > 0){
			info += '品牌(' + sppp + ')' + ((spbz != '' && spbz != undefined && $.trim(spbz).length > 0) ? ',' : '');
		}
		info += (spbz != '' && spbz != undefined && $.trim(spbz).length > 0) ? '包装(' + spbz + ')' : '';
		target.layout('panel', 'center').panel({title : '商品信息' + (info != '' ? '：' + info : '') });
	}else{
		target.layout('panel', 'center').panel({title : '商品信息'});
	}
};

//商品信息快速查询
jxc.spQuery = function(value, depId, ckId, urlJsp, urlAction, focusTarget, xsdjWithS){
	$('#jxc_spQuery').dialog({
		href: urlJsp,
		title:'商品查询',
		width:600,
		height:420,
		modal : true,
		onLoad: function(){
			$('#jxc_spQuery_dg').datagrid({
				url : urlAction,
				fit : true,
			    border : false,
			    singleSelect : true,
			    fitColumns: true,
			    pagination : true,
				pagePosition : 'bottom',
				pageSize : 10,
				pageList : [ 10, 15, 20, 25, 30 ],
				//将编辑行输入的商品编号传入对话框
				queryParams:{
					query : value,
					depId : depId,
					ckId : ckId,
				},
				columns:[[
			        {field:'spbh',title:'编号'},
			        {field:'spmc',title:'名称'},
			        {field:'spcd',title:'产地'},
			        {field:'sppp',title:'品牌'},
			        {field:'spbz',title:'包装'},
			        {field:'zjldwId',title:'主计量单位id',hidden:true},
			        {field:'zjldwmc',title:'主计量单位'},
			        {field:'xsdj',title:'销售单价(无税)', hidden: xsdjWithS ? true : false},
			        {field:'xsdjs',title:'销售单价(含税)', hidden: xsdjWithS ? false : true,
		        		formatter: function(value){
		        			return value == undefined ? '' : value.toFixed(LENGTH_JE) ;
		        		}	
			        },
			        {field:'limitXsdj',title:'最低销价',hidden:true},
			        {field:'kcsl',title:'库存数量',
			        	formatter: function(value){
		        			return value == '0.000' ? '' : value.toFixed(LENGTH_SL) ;
		        		}},
		        	{field:'dwcb',title:'成本',hidden:true},
			        
			    ]],
			    toolbar:'#jxc_spQuery_tb',
			    //双击商品行，返回商品信息并关闭对话框
			    onDblClickRow: function(rowIndex, rowData){
			    	//设置编辑行的值
			    	setValueBySpbh(rowData);
			    	focusTarget.target.focus();
					$('#jxc_spQuery').dialog('close');
			    },    					
			});
			var query = $('#jxc_spQuery_tb input');
	    	query.val(value);
	    	query.focus();
	    	//录入查询内容时，即时查询，刷新表格
	    	query.keyup(function(){
	    		$('#jxc_spQuery_dg').datagrid('load', 
	    				{
	    					query: query.val(),
	    					depId: depId,
	    					ckId: ckId
	    				});
	    	});
		},
	});
};

//商品信息快速查询
jxc.spHsQuery = function(value, depId, urlJsp, urlAction, setMethod, focusTarget){
	$('#jxc_spQuery').dialog({
		href: urlJsp,
		title:'商品查询',
		width:480,
		height:420,
		modal : true,
		onLoad: function(){
			$('#jxc_spQuery_dg').datagrid({
				url : urlAction,
				fit : true,
			    border : false,
			    singleSelect : true,
			    fitColumns: true,
			    pagination : true,
				pagePosition : 'bottom',
				pageSize : 10,
				pageList : [ 10, 15, 20, 25, 30 ],
				//将编辑行输入的商品编号传入对话框
				queryParams:{
					query : value,
					depId : depId,
				},
				columns:[[
			        {field:'spbh',title:'编号'},
			        {field:'spmc',title:'名称'},
			        {field:'spcd',title:'产地'},
			        {field:'sppp',title:'品牌'},
			        {field:'spbz',title:'包装'},
			        {field:'zjldwId',title:'主计量单位id',hidden:true},
			        {field:'zjldwmc',title:'主计量单位'},
//			        {field:'xsdj',title:'销售单价'},
//			        {field:'limitXsdj',title:'最低销价',hidden:true},
			        
			    ]],
			    toolbar:'#jxc_spQuery_tb',
			    //双击商品行，返回商品信息并关闭对话框
			    onDblClickRow: function(rowIndex, rowData){
			    	//设置编辑行的值
			    	eval(setMethod + "(rowData)");
			    	focusTarget.focus();
					$('#jxc_spQuery').dialog('close');
			    },    					
			});
			var query = $('#jxc_spQuery_tb input');
	    	query.val(value);
	    	query.focus();
	    	//录入查询内容时，即时查询，刷新表格
	    	query.keyup(function(){
	    		$('#jxc_spQuery_dg').datagrid('load', 
	    				{
	    					query: query.val(),
	    					depId: depId});
	    	});
		},
	});
};

//供应商、客户快速查询
jxc.query = function(title, input_bh, input_mc, urlJsp, urlAction){
	$('#jxc_query_dialog').dialog({
		href: urlJsp,
		title:title,
		width:450,
		height:420,
		modal : true,
		onLoad: function(){
			$('#jxc_query_dg').datagrid({
				url : urlAction,
				fit : true,
			    border : false,
			    singleSelect : true,
			    fitColumns: true,
			    pagination : true,
				pagePosition : 'bottom',
				pageSize : 10,
				pageList : [ 10, 15, 20, 25, 30 ],
				//将编辑行输入的编号传入对话框
				queryParams:{
					query : $(input_bh).val(),
				},
				columns:[[
			        {field:'bh',title:'编号',width:50,align:'center'},
			        {field:'mc',title:'名称',width:150,align:'center'},
			    ]],
			    toolbar:'#jxc_query_tb',
			    //双击数据行，返回信息并关闭对话框
			    onDblClickRow: function(rowIndex, rowData){
			    	//设置编辑行的值
			    	if(input_bh != ''){
			    		$(input_bh).val(rowData.bh);
			    		$(input_mc).val(rowData.mc);
			    		$(input_mc).change();
			    	}else{
			    		if(rowData.address == undefined){
			    			$(input_mc).val(rowData.bh + (rowData.mc==undefined ? '' : '(' + rowData.mc + ')'));
			    		}else{
			    			$(input_mc).val(rowData.mc + (rowData.address==undefined ? '' : '(' + rowData.address + ')'));
			    		}
			    	}
			    	$('#jxc_query_dialog').dialog('close');
			    },
			});
			var query = $('#jxc_query_tb input');
	    	query.val($(input_bh).val());
	    	query.focus();
	    	//录入查询内容时，即时查询，刷新表格
	    	query.keyup(function(){
	    		$('#jxc_query_dg').datagrid('load', {query: query.val()});
	    	});
		},
	});
};

//送货地址快速查询
jxc.queryAddr = function(title, target1, target2, urlJsp, urlAction){
	$('#jxc_queryAddr_dialog').dialog({
		href: urlJsp,
		title:title,
		width:450,
		height:420,
		modal : true,
		onLoad: function(){
			$('#jxc_queryAddr_dg').datagrid({
				url : urlAction,
				fit : true,
			    border : false,
			    singleSelect : true,
			    fitColumns: true,
			    pagination : true,
				pagePosition : 'bottom',
				pageSize : 10,
				pageList : [ 10, 15, 20, 25, 30 ],
				//将编辑行输入的编号传入对话框
				//queryParams:{
					//query : $(input_bh).val(),
				//},
				columns:[[
			        {field:'khmc',title:'名称',width:150,align:'center'},
			        {field:'khdz',title:'地址',width:50,align:'center'},
			        {field:'lxr',title:'联系人',width:50,align:'center'},
			        {field:'phone',title:'电话',width:50,align:'center'},
			    ]],
			    toolbar:'#jxc_queryAddr_tb',
			    //双击数据行，返回信息并关闭对话框
			    onDblClickRow: function(rowIndex, rowData){
			    	//设置编辑行的值
		    		$(target1).val(rowData.khmc + (rowData.khdz==undefined ? '' : '(' + rowData.khdz + ')'));
		    		$(target2).val(rowData.lxr + (rowData.phone==undefined ? '' : '(' + rowData.phone + ')'));
			    	
			    	$('#jxc_queryAddr_dialog').dialog('close');
			    },
			});
			var query = $('#jxc_queryAddr_tb input');
	    	//query.val($(input_bh).val());
	    	//query.focus();
	    	//录入查询内容时，即时查询，刷新表格
	    	query.keyup(function(){
	    		$('#jxc_queryAddr_dg').datagrid('load', {query: query.val()});
	    	});
		},
	});
};

jxc.print = function(url, isPrint, showPrint){
	var appletStr = '<APPLET ID="JrPrt" NAME="JrPrt" CODE="lnyswz/common/applet/JRPrinterApplet.class" CODEBASE="applets" ARCHIVE="reportprint.jar,commons-logging-1.1.1.jar,commons-collections-3.2.1.jar" WIDTH="0" HEIGHT="0" MAYSCRIPT> ' +    
		' <PARAM NAME="type" VALUE="application/x-java-applet;version=1.2.2">' +   
		' <PARAM NAME="scriptable" VALUE="false">' +   
		' <PARAM NAME="REPORT_URL" VALUE="'+url+'">' +
		' <PARAM NAME="REPORT_PRINT" VALUE="'+isPrint+'">' +
		' <PARAM NAME="SHOW_PRINT" VALUE="'+showPrint+'">' +
		' </APPLET>';
	
	$('#printDialog').html(appletStr);
	$.messager.show({
		title:'提示',
		msg:'准备打印，请稍候……',
		timeout:2000,
		showType:'slide',
		style:{
			right:'',
			//top:document.body.scrollTop+document.documentElement.scrollTop,
			bottom:'',
			height:50,
		}
	});
	
	
	
//	document.write('<APPLET ID="JrPrt" NAME="JrPrt" CODE="lnyswz/common/applet/JRPrinterApplet.class" CODEBASE="applets" ARCHIVE="reportprint.jar,commons-logging-1.1.1.jar,commons-collections-3.2.1.jar" WIDTH="0" HEIGHT="0" MAYSCRIPT>');   
//	document.write('<PARAM NAME="type" VALUE="application/x-java-applet;version=1.2.2">');   
//	document.write('<PARAM NAME="scriptable" VALUE="false">');   
//	document.write('<PARAM NAME="REPORT_URL" VALUE="'+url+'">');   
//	document.write('</APPLET>');
};

jxc.toJs = function(url, isZzs){
	var appletStr = '<APPLET ID="JrFile" NAME="JrFile" CODE="lnyswz/common/applet/FileApplet.class" CODEBASE="applets" ARCHIVE="file.jar" WIDTH="0" HEIGHT="0" MAYSCRIPT> ' +    
		' <PARAM NAME="type" VALUE="application/x-java-applet;version=1.2.2">' +   
		' <PARAM NAME="scriptable" VALUE="false">' +   
		' <PARAM NAME="DATA_URL" VALUE="'+url+'">' +
		' <PARAM NAME="FP_ZZS" VALUE="'+isZzs+'">' +
		' </APPLET>';

	$('#fileDialog').html(appletStr);
	$.messager.show({
		title:'提示',
		msg:'导出数据到金穗接口，请稍候……',
		timeout:2000,
		showType:'slide',
		style:{
			right:'',
			//top:document.body.scrollTop+document.documentElement.scrollTop,
			bottom:'',
			height:50,
		}
	});
	
//	document.write('<APPLET ID="JrPrt" NAME="JrPrt" CODE="lnyswz/common/applet/JRPrinterApplet.class" CODEBASE="applets" ARCHIVE="reportprint.jar,commons-logging-1.1.1.jar,commons-collections-3.2.1.jar" WIDTH="0" HEIGHT="0" MAYSCRIPT>');   
//	document.write('<PARAM NAME="type" VALUE="application/x-java-applet;version=1.2.2">');   
//	document.write('<PARAM NAME="scriptable" VALUE="false">');   
//	document.write('<PARAM NAME="REPORT_URL" VALUE="'+url+'">');   
//	document.write('</APPLET>');
};

jxc.checkNum = function(val){
	return val == undefined ? 0 : val;
};

jxc.showKc = function(area, url, params){
	$(area).layout('expand', 'east');
	
	$.ajax({
		url: url,
		data: params,
		cache: false,
		async: false,
		dataType: 'json',
		success: function(data){
			$('#show_spkc').propertygrid({
				fit : true,
				data: data.rows,
				showGroup: true,
				scrollbarSize: 0,
				showHeader:false,
				groupFormatter: groupFormatter,
			});
		}
	});
};

function groupFormatter(fvalue, rows){
	var total = 0;
	for(var i = 0; i < rows.length; i++){
		total += Number(rows[i]['value']);
	}
	if(total > 0){
		return fvalue + ' - <span style="color:blue">' + total + '</span>';
	}else{
		return fvalue + ' - <span style="color:red">' + total + '</span>';
	}
}

jxc.hideKc = function(area){
	$(area).layout('collapse', 'east');
};
