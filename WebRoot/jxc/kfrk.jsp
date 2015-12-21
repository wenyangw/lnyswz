<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>


<script type="text/javascript">
var did;
var lx;
var menuId;
var kfrk_spdg;
var kfrk_dg;
var kfrk_cgjhDg;
var kfrk_tabs;
var jxc_kfrk_ckCombo;

var spRow;

var editIndex = undefined;
//编辑行字段
var spbhEditor;   
var spmcEditor;
var spcdEditor;
var spppEditor;
var spbzEditor;
var hwIdEditor;
var sppcEditor;
var zjldwEditor;
var zjhslEditor;
var zyrslEditor;
var zslEditor;
var cjldwEditor;
var cjhslEditor;
var cyrslEditor;
var cslEditor;
var bzslEditor;
var zhxsEditor;
var zjldwIdEditor;
var cjldwIdEditor;

$(function(){
	did = lnyw.tab_options().did;
	lx = lnyw.tab_options().lx;
	menuId = lnyw.tab_options().id;
	
	$('#jxc_kfrk_layout').layout({
		fit : true,
		border : false,
	});
	
	kfrk_dg = $('#jxc_kfrk_dg').datagrid({
		fit : true,
	    border : false,
	    singleSelect : true,
	    remoteSort: false,
	    fitColumns: true,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
		frozenColumns:[[
		]],
		columns:[[
			{field:'kfrklsh',title:'流水号',align:'center'},
	        {field:'createTime',title:'时间',align:'center'},
	        {field:'ckId',title:'仓库id',align:'center',hidden:true},
	        {field:'ckmc',title:'仓库名称',align:'center'},
	        {field:'gysbh',title:'供应商编号',align:'center'},
	        {field:'gysmc',title:'供应商名称',align:'center'},
	        {field:'shry',title:'送货人',align:'center'},
	        {field:'ch',title:'车号',align:'center'},
	        {field:'zjh',title:'证件号',align:'center'},
        	{field:'bz',title:'备注',align:'center',
        		formatter: function(value){
        			return lnyw.memo(value, 15);
        		}},
        	{field:'cgjhlshs',title:'采购计划流水号',align:'center',
           		formatter: function(value){
           			return lnyw.memo(value, 15);
           		}},
           	{field:'ywrklsh',title:'业务入库流水号',align:'center',
               		formatter: function(value){
               			return lnyw.memo(value, 15);
               		}},
        	{field:'isCj',title:'状态',align:'center',sortable:true,
        		formatter : function(value) {
					if (value == '1') {
						return '已冲减';
					} else {
						return '正常';
					}
				},
        		sorter: function(a,b){
        			a = a == undefined ? 0 : a;
        			b = b == undefined ? 0 : b;
					return (a-b);  
				}},
			{field:'cjTime',title:'冲减时间',align:'center'},
        	{field:'cjKfrklsh',title:'原库房入库流水号',align:'center'},
	    ]],
	    toolbar:'#jxc_kfrk_tb',
	});
	lnyw.toolbar(1, kfrk_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', did);
	
	
	kfrk_dg.datagrid({
        view: detailview,
        detailFormatter:function(index,row){
            return '<div style="padding:2px"><table id="kfrk-ddv-' + index + '"></table></div>';
        },
        onExpandRow: function(index,row){
            $('#kfrk-ddv-'+index).datagrid({
                url:'${pageContext.request.contextPath}/jxc/kfrkAction!detDatagrid.action',
                fitColumns:true,
                singleSelect:true,
                rownumbers:true,
                loadMsg:'',
                height:'auto',
                queryParams: {
        			kfrklsh: row.kfrklsh,
        		},
                columns:[[
                    {field:'spbh',title:'商品编号',width:200,align:'center'},
                    {field:'spmc',title:'名称',width:100,align:'center'},
                    {field:'spcd',title:'产地',width:100,align:'center'},
                    {field:'sppp',title:'品牌',width:100,align:'center'},
                    {field:'spbz',title:'包装',width:100,align:'center'},
                    {field:'hwId',title:'货位id',width:100,align:'center', hidden: true},
                    {field:'hwmc',title:'货位',width:100,align:'center'},
                    {field:'sppc',title:'批次',width:100,align:'center'},
                    {field:'zjldwmc',title:'单位1',width:100,align:'center'},
                    {field:'zdwsl',title:'数量1',width:100,align:'center',
                       	formatter: function(value){
                       		return value == 0 ? '' : value;
                   		}},
                    {field:'cjldwmc',title:'单位2',width:100,align:'center'},
                    {field:'cdwsl',title:'数量2',width:100,align:'center',
                       	formatter: function(value){
                       		return value == 0 ? '' : value;
                   		}},
                    {field:'bzsl',title:'包装数量',width:100,align:'center',
                       	formatter: function(value){
                       		return value == 0 ? '' : value;
                   		}},
                ]],
                onResize:function(){
                	kfrk_dg.datagrid('fixDetailRowHeight',index);
                },
                onLoadSuccess:function(){
                    setTimeout(function(){
                    	kfrk_dg.datagrid('fixDetailRowHeight',index);
                    },0);
                }
            });
            kfrk_dg.datagrid('fixDetailRowHeight',index);
        }
    });
	
	
	kfrk_cgjhDg = $('#jxc_kfrk_cgjhDg').datagrid({
		fit : true,
	    border : false,
	    remoteSort: false,
// 	    fitColumns: true,
// 	    singleSelect: true, 
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
		frozenColumns:[[
		]],
		columns:[[
			{field:'id',title:'detId',align:'center', checkbox:true},
			{field:'cgjhlsh',title:'流水号',align:'center'},
	        {field:'createTime',title:'时间',align:'center'},
	        {field:'createName',title:'计划员',align:'center'},
	        {field:'spbh',title:'商品编号',align:'center'},
            {field:'spmc',title:'名称',align:'center'},
            {field:'spcd',title:'产地',align:'center'},
            {field:'sppp',title:'品牌',align:'center'},
            {field:'spbz',title:'包装',align:'center'},
            {field:'zjldwmc',title:'单位1',align:'center'},
            {field:'zdwsl',title:'数量1',align:'center',
               	formatter: function(value){
               		return value == 0 ? '' : value;
           		}},
       		{field:'zdwyrsl',title:'到货数量',align:'center',
               	formatter: function(value){
               		return value == 0 ? '' : value;
           		},
           		styler: function(value,row){
           			return 'color:red;';
    			}},
            {field:'cjldwmc',title:'单位2',align:'center'},
            {field:'cdwsl',title:'数量2',align:'center',
               	formatter: function(value){
               		return value == 0 ? '' : value;
           		}},
         	{field:'cdwyrsl',title:'到货数量2',align:'center',
               	formatter: function(value){
               		return value == 0 ? '' : value;
            	},
            	styler: function(value,row){
            		return 'color:red;';
      		}},
	        {field:'gysbh',title:'供应商编号',align:'center'},
	        {field:'gysmc',title:'供应商名称',align:'center'},
	        {field:'ckId',title:'仓库id',align:'center',hidden:true},
	        {field:'ckmc',title:'仓库',align:'center'},
	        {field:'jsfsmc',title:'付款方式',align:'center'},
	        {field:'shdz',title:'送货地址',align:'center'},
	        {field:'lxr',title:'联系人',align:'center'},
	        {field:'dhsj',title:'到货时间',align:'center'},
        	{field:'bz',title:'备注',align:'center',
        		formatter: function(value){
        			return lnyw.memo(value, 15);
        		}},
        	{field:'kfrklshs',title:'库房入库流水号',align:'center',
            		formatter: function(value){
            			return lnyw.memo(value, 15);
            		}},
	    ]],
	    toolbar:'#jxc_kfrk_cgjhTb',
	});
	lnyw.toolbar(2, kfrk_cgjhDg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', did);
	
	
// 	kfrk_cgjhDg.datagrid({
//         view: detailview,
//         detailFormatter:function(index,row){
//             return '<div style="padding:2px"><table id="ddv-' + index + '"></table></div>';
//         },
//         onExpandRow: function(index,row){
//             $('#ddv-'+index).datagrid({
//                 url:'${pageContext.request.contextPath}/jxc/cgjhAction!detDatagrid.action',
//                 fitColumns:true,
//                 singleSelect:true,
//                 rownumbers:true,
//                 loadMsg:'',
//                 height:'auto',
//                 queryParams: {
//         			cgjhlsh: row.cgjhlsh,
//         		},
//                 columns:[[
//                     {field:'spbh',title:'商品编号',width:200,align:'center'},
//                     {field:'spmc',title:'名称',width:100,align:'center'},
//                     {field:'spcd',title:'产地',width:100,align:'center'},
//                     {field:'sppp',title:'品牌',width:100,align:'center'},
//                     {field:'spbz',title:'包装',width:100,align:'center'},
//                     {field:'zjldwmc',title:'单位1',width:100,align:'center'},
//                     {field:'zdwsl',title:'数量1',width:100,align:'center'},
//                     {field:'cjldwmc',title:'单位2',width:100,align:'center'},
//                     {field:'cdwsl',title:'数量2',width:100,align:'center'},
//                 ]],
//                 onResize:function(){
//                 	kfrk_cgjhDg.datagrid('fixDetailRowHeight',index);
//                 },
//                 onLoadSuccess:function(){
//                     setTimeout(function(){
//                     	kfrk_cgjhDg.datagrid('fixDetailRowHeight',index);
//                     },0);
//                 }
//             });
//             kfrk_cgjhDg.datagrid('fixDetailRowHeight',index);
//         }
//     });
	
	kfrk_ywrkDg = $('#jxc_kfrk_ywrkDg').datagrid({
		fit : true,
	    border : false,
	    singleSelect : true,
	    remoteSort: false,
	    fitColumns: true,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
		frozenColumns:[[
		]],
		columns:[[
			{field:'ywrklsh',title:'流水号',align:'center'},
	        {field:'createTime',title:'时间',align:'center'},
	        {field:'gysbh',title:'供应商编号',align:'center'},
	        {field:'gysmc',title:'供应商名称',align:'center'},
	        {field:'rklxmc',title:'入库类型',align:'center'},
	        {field:'bz',title:'备注',align:'center',
        		formatter: function(value){
        			return lnyw.memo(value, 15);
        		}},
	    ]],
	    toolbar:'#jxc_kfrk_ywrkTb',
	});
	lnyw.toolbar(3, kfrk_ywrkDg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', did);
	
	
	kfrk_ywrkDg.datagrid({
        view: detailview,
        detailFormatter:function(index,row){
            return '<div style="padding:2px"><table id="kfrk-ywrk-ddv-' + index + '"></table></div>';
        },
        onExpandRow: function(index,row){
            $('#kfrk-ywrk-ddv-'+index).datagrid({
                url:'${pageContext.request.contextPath}/jxc/ywrkAction!detDatagrid.action',
                fitColumns:true,
                singleSelect:true,
                rownumbers:true,
                loadMsg:'',
                height:'auto',
                queryParams: {
        			ywrklsh: row.ywrklsh,
        		},
                columns:[[
                    {field:'spbh',title:'商品编号',width:200,align:'center'},
                    {field:'spmc',title:'名称',width:100,align:'center'},
                    {field:'spcd',title:'产地',width:100,align:'center'},
                    {field:'sppp',title:'品牌',width:100,align:'center'},
                    {field:'spbz',title:'包装',width:100,align:'center'},
                    {field:'zjldwmc',title:'单位1',width:100,align:'center'},
                    {field:'zdwsl',title:'数量1',width:100,align:'center'},
                    {field:'cjldwmc',title:'单位2',width:100,align:'center'},
                    {field:'cdwsl',title:'数量2',width:100,align:'center'},
                ]],
                onResize:function(){
                	kfrk_ywrkDg.datagrid('fixDetailRowHeight',index);
                },
                onLoadSuccess:function(){
                    setTimeout(function(){
                    	kfrk_ywrkDg.datagrid('fixDetailRowHeight',index);
                    },0);
                }
            });
            kfrk_ywrkDg.datagrid('fixDetailRowHeight',index);
        }
    });
	
	//选中列表标签后，装载数据
	kfrk_tabs = $('#jxc_kfrk_tabs').tabs({
		onSelect: function(title, index){
			if(index == 1){
				kfrk_dg.datagrid({
					url: '${pageContext.request.contextPath}/jxc/kfrkAction!datagrid.action',
					queryParams:{
						bmbh: did,
					}
				});
			}
			if(index == 2){
				kfrk_cgjhDg.datagrid({
					url: '${pageContext.request.contextPath}/jxc/cgjhAction!datagridDet.action',
					queryParams: {
						bmbh: did,
						fromOther: 'fromKfrk'
						},
				});
			}
			if(index == 3){
				kfrk_ywrkDg.datagrid({
					url: '${pageContext.request.contextPath}/jxc/ywrkAction!datagrid.action',
					queryParams: {
						bmbh: did,
						fromOther: 'fromKfrk'
						},
				});
			}
		},
	});
	
	kfrk_spdg = $('#jxc_kfrk_spdg').datagrid({
		fit : true,
	    border : false,
	    singleSelect : true,
	    fitColumns: true,
	    autoRowHeight: false,
	    showFooter: true,
		columns:[[
	        {field:'spbh',title:'商品编号',width:25,align:'center',editor:'text'},
	        {field:'spmc',title:'商品名称',width:100,align:'center',editor:'textRead'},
	        {field:'spcd',title:'商品产地',width:25,align:'center',editor:'textRead'},
	        {field:'sppp',title:'商品品牌',width:25,align:'center',editor:'text',hidden:true},
	        {field:'spbz',title:'商品包装',width:25,align:'center',editor:'text',hidden:true},
	        {field:'hwId',title:'货位',width:25,align:'center',
	        	editor:{
	        		type: 'combobox',
	        		options:{
	        			required: true,
	        			valueField : 'id',
	        			textField : 'hwmc',
	        			panelHeight: 'auto',
	        			//url: '${pageContext.request.contextPath}/jxc/hwAction!listHw.action?depId=' + did,
	        		}
	        	}},
	        {field:'sppc',title:'商品批次',width:25,align:'center',editor:'datebox'},
	        {field:'zjldwmc',title:'单位1',width:25,align:'center',editor:'textRead'},
	        {field:'zdwjhsl',title:'计划数量1',width:25,align:'center',editor:'textRead'},
	        {field:'zdwyrsl',title:'已入数量1',width:25,align:'center',editor:'textRead'},
// 	        	editor:{
// 	        		type:'numberbox',
// 	        		options:{
// 	        			//精度
// 	        			precision:3,
// 	        		}}},
	        {field:'zdwsl',title:'到货数量1',width:25,align:'center',
	        	editor:{
	        		type:'numberbox',
	        		options:{
	        			//精度
	        			precision:3,
	        		}}},
	        {field:'cjldwmc',title:'单位2',width:25,align:'center',editor:'textRead'},
	        {field:'cdwjhsl',title:'计划数量2',width:25,align:'center',editor:'textRead'},
	        {field:'cdwyrsl',title:'已入数量2',width:25,align:'center',editor:'textRead'},
	        {field:'cdwsl',title:'到货数量2',width:25,align:'center',
	        		editor:{
        				type:'numberbox',
        				options:{
        					//精度
        					precision:3,
        			}}},
// 	        {field:'cdwrksl',title:'入库数量2',width:25,align:'center',editor:'textRead'},
// 	        		editor:{
//         				type:'numberbox',
//         				options:{
//         					//精度
//         					precision:3,
//         			}}},
        	{field:'bzsl',title:'包装数',width:25,align:'center',
				editor:{
        			type:'numberbox',
        			options:{
        				//精度
        				precision:0,
        			}}},
        	{field:'zhxs',title:'转换系数',width:25,align:'center',editor:'text', hidden:true},
        	{field:'zjldwId',title:'主计量单位id',align:'center',editor:'text', hidden:true},
        	{field:'cjldwId',title:'次计量单位id',align:'center',editor:'text', hidden:true},
	    ]],
        onClickRow: clickRow,
        onAfterEdit: function (rowIndex, rowData, changes) {
            //endEdit该方法触发此事件
            editIndex = undefined;
        },
        
         
	});
	
	//初始化创建时间
	$('#createDate').html(moment().format('YYYY年MM月DD日'));
	
	//$('#jxc_kfrk_tabs a.tabs-inner').css('height','100px');
	//$('#jxc_kfrk_tabs span.tabs-title').css('white-space','normal');
	
	//初始化仓库列表
	jxc_kfrk_ckCombo = lnyw.initCombo($("#jxc_kfrk_ckId"), 'id', 'ckmc', '${pageContext.request.contextPath}/jxc/ckAction!listCk.action?depId=' + did);
	
	//初始化信息
	init();
});

//以下为商品列表处理代码
function init(){
	//清空列表，添加一个空行
	kfrk_spdg.datagrid({
		data: [{}],
	});
	
	//清空全部字段
	$('input').val('');
	//收回商品库存信息
	jxc.hideKc('#jxc_kfrk_layout');
	jxc.spInfo($('#jxc_kfrk_layout'), '');
	
	 
	jxc_kfrk_ckCombo.combobox('selectedIndex', 0);
	
	
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
				$('#kfrkLsh').html(d.obj);
			}  
		},
	});
	
	//根据权限，动态加载功能按钮
	lnyw.toolbar(0, kfrk_spdg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', did);
	//清空合计内容
	kfrk_spdg.datagrid('reloadFooter',[{}]);
}

//判断行是否编辑完成
function rowOk(){
	if(editIndex == undefined){
		return true;
	}
	if(keyOk()){
		if(zslEditor.target.val() != 0 ){
			return true;
		}
	}
	return false;
}

//判断商品信息是否完成
function keyOk(){
	if(spmcEditor.target.val().length > 0){
		return true;
	}
	return false;
}

//根据指定行，进入编辑状态
function enterEdit(rowIndex, isClick){
	//如果选中行为最后一行，先增加一个空行
	if(rowIndex == kfrk_spdg.datagrid('getRows').length - 1){
		kfrk_spdg.datagrid('appendRow', {});
	}else{
		if(!isClick){
			return false;
		}
	}
	//将选中行标识为编辑行
	editIndex = rowIndex;
	//选择编辑行，并进行入编辑状态
	kfrk_spdg.datagrid('selectRow', editIndex)
			.datagrid('beginEdit', editIndex);
	setEditing();
}

//单击行操作
function clickRow(rowIndex, rowData){
	//是否有编辑行
	if(editIndex != undefined){ //有编辑行
		//选中行与编辑行是否为同一行
		//同一行则无任何操作
		if(editIndex != rowIndex){
			//编辑行内容验证是否通过
			if(rowOk()){ //行验证通过
				//结束编辑行
				kfrk_spdg.datagrid('endEdit', editIndex);
				//将选中行转为编辑状态
				enterEdit(rowIndex, true);
			}else{ //行验证未通过
				//验证关键字段信息
				//通过，不做任何操作，将该行继续完成或删除该行
				if(!keyOk()){ //未通过，将该行删除
					if(rowIndex > editIndex){
						rowIndex = rowIndex - 1;
					}
					removeRow();
					enterEdit(rowIndex, true);
				}
			}
		}
	}else{ //无编辑行
		enterEdit(rowIndex, true);
	}
}

//删除行
function removeRow(){
    if (editIndex == undefined){
    	return; 
    }
    kfrk_spdg.datagrid('cancelEdit', editIndex)
            .datagrid('deleteRow', editIndex);
    editIndex = undefined;
    updateFooter();
    jxc.hideKc('#jxc_kfrk_layout');
}

//保存行
function accept(){
    if (rowOk()){
    	kfrk_spdg.datagrid('acceptChanges');
    	jxc.hideKc('#jxc_kfrk_layout');
    }
}

//取消编辑行
function cancelAll(){
	kfrk_spdg.datagrid('rejectChanges');
    editIndex = undefined;
    updateFooter();
    jxc.hideKc('#jxc_kfrk_layout');
}

//提交数据到后台
function saveAll(){
	var msg = formValid();
	if(msg == ''){
		//编辑行是否完成
		if(rowOk()){
			kfrk_spdg.datagrid('endEdit', editIndex);
		}else{ //编辑行未完成
			if(keyOk()){
				$.messager.alert('提示', '商品数据编辑未完成,请继续操作！', 'error');
				return false;
			}else{
				removeRow();
			}
		}
	}else{
		$.messager.alert('提示', msg + '填写不完整,请继续操作！', 'error');
		return false;
	}
	
	var rows = kfrk_spdg.datagrid('getRows');
	if(rows.length == 1){
		$.messager.alert('提示', '未添加商品数据,请继续操作！', 'error');
		return false;
	}
	$.each(rows.slice(0, rows.length - 1), function(){
		if(this.zdwsl == undefined){
			$.messager.alert('提示', '商品数据未完成,请继续操作！', 'error');
			return false;
		}
	});
	var footerRows = kfrk_spdg.datagrid('getFooterRows');
	var effectRow = new Object();
	//将表头内容传入后台
	effectRow['gysbh'] = $('input[name=gysbh]').val();
	effectRow['gysmc'] = $('input[name=gysmc]').val();
	effectRow['ckId'] = jxc_kfrk_ckCombo.combobox('getValue');
	effectRow['ckmc'] = jxc_kfrk_ckCombo.combobox('getText');
	effectRow['ch'] = $('input[name=ch]').val();
	effectRow['zjh'] = $('input[name=zjh]').val();
	effectRow['shry'] = $('input[name=shry]').val();
	effectRow['bz'] = $('input[name=jxc_kfrk_bz]').val();
	effectRow['cgjhDetIds'] = $('input[name=cgjhDetIds]').val();
	effectRow['ywrklsh'] = $('input[name=ywrklsh]').val();
	effectRow['bmbh'] = did;
	effectRow['lxbh'] = lx;
	effectRow['menuId'] = menuId;
	
	//将表格中的数据去掉最后一个空行后，转换为json格式
	effectRow['datagrid'] = JSON.stringify(rows.slice(0, rows.length - 1));
	//提交到action
	//$.ajaxSettings.traditional=true;
	$.ajax({
		type: "POST",
		url: '${pageContext.request.contextPath}/jxc/kfrkAction!save.action',
		data: effectRow,
		dataType: 'json',
		success: function(rsp){
			if(rsp.success){
		    	$.messager.show({
					title : '提示',
					msg : '提交成功！'
				});
		    	init();
		    	$.messager.confirm('请确认', '是否打印库房入库单？', function(r) {
					if (r) {
						var url = lnyw.bp() + '/jxc/kfrkAction!printKfrk.action?kfrklsh=' + rsp.obj.kfrklsh + '&bmbh=' + did;
						jxc.print(url, PREVIEW_REPORT, HIDE_PRINT_WINDOW);
					}
				});
			}  
		},
		error: function(){
			$.messager.alert("提示", "提交错误了！");
		}
	});
}

//处理编辑行
function setEditing(){
	//初始化编辑行
	var spRow = undefined;
	
	//加载字段
	var editors = kfrk_spdg.datagrid('getEditors', editIndex);
    spbhEditor = editors[0];
    spmcEditor = editors[1];
    spcdEditor = editors[2];
    spppEditor = editors[3];
    spbzEditor = editors[4];
    hwIdEditor = editors[5];
    sppcEditor = editors[6];
    zjldwEditor = editors[7];
    zjhslEditor = editors[8];
    zyrslEditor = editors[9];
    zslEditor = editors[10];
    cjldwEditor = editors[11];
    cjhslEditor = editors[12];
    cyrslEditor = editors[13];
    cslEditor = editors[14];
    bzslEditor = editors[15];
    zhxsEditor = editors[16];
    zjldwIdEditor = editors[17];
    cjldwIdEditor = editors[18];
    
    if($(spbhEditor.target).val() != ''){
    	jxc.spInfo($('#jxc_kfrk_layout'), '1', $(spppEditor.target).val(), $(spbzEditor.target).val());
    	jxc.showKc('#jxc_kfrk_layout', '${pageContext.request.contextPath}', did, $(spbhEditor.target).val());
    }else{
    	jxc.spInfo($('#jxc_kfrk_layout'), '');
    	jxc.hideKc('#jxc_kfrk_layout');
    }
    
  	//初始化当前商品货位
    $.ajax({
		type: "POST",
		url: '${pageContext.request.contextPath}/jxc/kfzzAction!findHwId.action',
		data: {
			bmbh: did,
			spbh: $(spbhEditor.target).val(),
		},
		dataType: 'json',
		success: function(d){
			$(hwIdEditor.target).combobox('reload', '${pageContext.request.contextPath}/jxc/hwAction!listHw.action?depId=' + did + '&ckId=' + jxc_kfrk_ckCombo.combobox('getValue'));
			$(hwIdEditor.target).combobox('selectedIndex', 0);
		},
	});
    
  	//初始化商品批次
	$(sppcEditor.target).datebox('setValue', moment().date(1).format('YYYY-MM-DD'));
    
	//loadEditor();
	//处理编辑行的换行事件
	$('.datagrid-row-editing').on('keydown','input', function(event){
		//监听按下向下箭头
		if(event.keyCode == 40){
			var rowIndex = editIndex;
			if(rowOk()){
				kfrk_spdg.datagrid('endEdit', editIndex);
				enterEdit(rowIndex + 1, false);
			}else{
				if(!keyOk()){
					removeRow();
				}
			}
		}
	});
	
    //处理商品编号按键事件
    spbhEditor.target.bind('keydown', function(event){
    	//按Tab键,根据商品编号获取商品信息
    	if(event.keyCode == 9){
    		if($(this).val().trim().length == 7){
    			if(!existKey($(this).val(), editIndex)){
    				$.ajax({
    					url:'${pageContext.request.contextPath}/jxc/spAction!loadSp.action',
    					async: false,
    					context:this,
    					data:{
    						spbh: $(this).val(),
    						depId : did,
    					},
    					dataType:'json',
    					success:function(data){
    						if(data.success){
    							//设置信息字段值
    							setValueBySpbh(data.obj);
    							//spOk = true;
    							hwIdEditor.target.focus();
    						}else{
    							$.messager.alert('提示', '商品编号不存在！', 'error');
    						}
    					}
    				});
    			}else{
    				$.messager.alert('提示', '商品编号只能出现一次，请重新输入！', 'error');
    			}
    		}else{
    			$.messager.alert('提示', '商品编号必须是7位，请重新输入！', 'error');
    		}
    		return false;
    	}
    	//按ESC键，弹出对话框，可以按商品编号或名称查询，双击商品行返回信息
    	if(event.keyCode == 27){
    		spRow = jxc.spQuery($(spbhEditor.target).val(),
   				did,
   				undefined,
   				'${pageContext.request.contextPath}/jxc/spQuery.jsp',
   				'${pageContext.request.contextPath}/jxc/spAction!spDg.action',
   				hwIdEditor    				
   				);
    		return false;
    	}
    });
    
    //输入主单位数量后，计算次单位数量
    zslEditor.target.bind('keyup', function(event){
    	if($(spbhEditor.target).val().substring(0, 3) < '513' || $(spbhEditor.target).val().substring(0, 3) > '518'){
    		if($(zhxsEditor.target).val() != 0){
    			$(cslEditor.target).numberbox('setValue', $(zslEditor.target).val() / $(zhxsEditor.target).val());
    		}
    	}
    	calculate();
    }).bind('keydown', function(event){
     	if(event.keyCode == 9){
     		bzslEditor.target.focus();
     		return false;
     	}
    });
    
  	
//     cslEditor.target.bind('keyup', function(event){
//     	if($(zhxsEditor.target).val() != 0){
//     		$(zslEditor.target).numberbox('setValue', $(cslEditor.target).val() * $(zhxsEditor.target).val());
//     	}
//     	calculate();
//     }).bind('keydown', function(event){
//      	if(event.keyCode == 9){
//      		cdjEditor.target.focus();
//      		return false;
//      	}
//     });
        
    
    //汇总计算
    function calculate(){
        //更新汇总列
        updateFooter();
    }
  	
    //初始进入编辑状态时，使用商品编号获得焦点
    spbhEditor.target.focus();
}
//求和
function updateFooter(){
 	var rows = kfrk_spdg.datagrid('getRows');
	var spmc_footer = '合计';
	var hjsl = 0.000000;
	$.each(rows, function(){
		var index = kfrk_spdg.datagrid('getRowIndex', this);
		if(index < rows.length - 1){
		if(editIndex == index){
			hjsl += Number(cslEditor.target.val());
		}else{
			hjsl += Number(this.cdwsl);
		}
	}
 		
 	});
	
	kfrk_spdg.datagrid('reloadFooter', [{spmc : spmc_footer, cdwsl : hjsl !== hjsl ? '' : hjsl.toFixed(LENGTH_SL)}]);
}

//验证记录是否已存在
function existKey(value, rowIndex){
	var keys = kfrk_spdg.datagrid('getRows');
	var exist = false;
	if(keys.length > 2){
		$.each(keys, function(){
			var index = kfrk_spdg.datagrid('getRowIndex', this);
			if(index != rowIndex){
				if(this.spbh == value){
					exist = true;
					return;
				}
			}
		});
	}
	return exist;
}

function formValid(){
	var message = '';
	if($('input[name=gysmc]').val() == ''){
		message += '供应商信息<br>';
	}
	
	return message;
}

//根据获得的行数据对各字段赋值
function setValueBySpbh(rowData){
	spbhEditor.target.val(rowData.spbh);
	spmcEditor.target.val(rowData.spmc);
	spcdEditor.target.val(rowData.spcd);
	spppEditor.target.val(rowData.sppp);
	spbzEditor.target.val(rowData.spbz);
	zjldwEditor.target.val(rowData.zjldwmc);
	cjldwEditor.target.val(rowData.cjldwmc);
	if(rowData.spbh.substring(0, 3) >= '513' && rowData.spbh.substring(0, 3) <= '518'){
		zhxsEditor.target.val(0);
	}else{
		zhxsEditor.target.val(rowData.zhxs);
	}
	zjldwIdEditor.target.val(rowData.zjldwId);
	cjldwIdEditor.target.val(rowData.cjldwId);
	
	jxc.spInfo($('#jxc_kfrk_layout'), '1', rowData.sppp, rowData.spbz);
	jxc.showKc('#jxc_kfrk_layout', '${pageContext.request.contextPath}', did, $(spbhEditor.target).val());
	
	//初始化货位，将返回商品的货位设为默认值
	$.ajax({
		type: "POST",
		url: '${pageContext.request.contextPath}/jxc/kfzzAction!findHwId.action',
		data: {
			bmbh: did,
			spbh: rowData.spbh,
		},
		dataType: 'json',
		success: function(d){
			$(hwIdEditor.target).combobox('reload', '${pageContext.request.contextPath}/jxc/hwAction!listHw.action?depId=' + did + '&ckId=' + jxc_kfrk_ckCombo.combobox('getValue'));
			$(hwIdEditor.target).combobox('selectedIndex', 0);
		},
	});
	//初始化商品批次
	$(sppcEditor.target).datebox('setValue', moment().format('YYYY-MM-DD'));
}

function gysLoad(){
	switch(event.keyCode){
	case 27:
		jxc.query('供应商检索', $('input[name=gysbh]'), $('input[name=gysmc]'), 
				'${pageContext.request.contextPath}/jxc/query.jsp',
				'${pageContext.request.contextPath}/jxc/gysAction!gysDg.action');
		break;
	case 9:
		break;
	default:
		if($('input[name=gysbh]').val().trim().length == 0){
			$('input[name=gysmc]').val('');
		}
		if($('input[name=gysbh]').val().trim().length == 8){
			$.ajax({
				url:'${pageContext.request.contextPath}/jxc/gysAction!loadGys.action',
				async: false,
				context:this,
				data:{
					gysbh: $('input[name=gysbh]').val().trim(),
				},
				dataType:'json',
				success:function(data){
					if(data.success){
						//设置信息字段值
						$('input[name=gysmc]').val(data.obj.gysmc);
						$('input[name=shry]').focus();
					}else{
						$.messager.alert('提示', '供应商信息不存在！', 'error');
					}
				}
			});
		}
		break;
	}
}
//////////////////////////////////////////////以上为商品列表处理代码

//////////////////////////////////////////////以下为库房入库划列表处理代码
function cjKfrk(){
	var row = kfrk_dg.datagrid('getSelected');
	if (row != undefined) {
		if(!row.cjKfrklsh || row.isCj != '1'){
			if(row.ywrklsh == null){
// 				if(row.isCj != '1'){
					$.messager.prompt('请确认', '是否要冲减选中的库房入库单？请填写备注', function(bz){
						if (bz != undefined){
							$.ajax({
								url : '${pageContext.request.contextPath}/jxc/kfrkAction!cjKfrk.action',
								data : {
									kfrklsh : row.kfrklsh,
									bmbh: did,
									lxbh: lx,
									menuId : menuId,
									bz : bz,
								},
								method: 'post',
								dataType : 'json',
								success : function(d) {
									kfrk_dg.datagrid('load');
									kfrk_dg.datagrid('unselectAll');
									$.messager.show({
										title : '提示',
										msg : d.msg
									});
								}
							});
						}
						});
// 				}else{
// 					$.messager.alert('警告', '选中的库房入库记录已被冲减，请重新选择！',  'warning');
// 				}
			}else{
				$.messager.alert('警告', '选中的库房入库已进行业务入库，不能被冲减，请重新选择！',  'warning');
			}
		}else{
			$.messager.alert('警告', '选中的库房入库单已冲减，请重新选择！',  'warning');
		}
	}else{
		$.messager.alert('警告', '请选择一条记录进行操作！',  'warning');
	}
}

function printKfrk(){
	var row = kfrk_dg.datagrid('getSelected');
	if (row != undefined) {
		$.messager.confirm('请确认', '是否打印库房入库单？', function(r) {
			if (r) {
				var url = lnyw.bp() + '/jxc/kfrkAction!printKfrk.action?kfrklsh=' + row.kfrklsh + '&bmbh=' + did;
				jxc.print(url, PREVIEW_REPORT, SHOW_PRINT_WINDOW);
			}
		});
	}else{
		$.messager.alert('警告', '请选择一条记录进行操作！',  'warning');
	}
}

function searchKfrk(){
	kfrk_dg.datagrid('load',{
		bmbh: did,
		createTime: $('input[name=createTimeKfrk]').val(),
	});
}

//////////////////////////////////////////////以上为库房入库列表处理代码


//////////////////////////////////////////////以下为采购计划列表处理代码

function generateKfrk(){
	var rows = kfrk_cgjhDg.datagrid('getSelections');
	var cgjhDetIds = [];
	if(rows.length > 0){
		$.messager.confirm('请确认', '是否要将选中记录进行入库？', function(r) {
			if(r){
				for ( var i = 0; i < rows.length; i++) {
					cgjhDetIds.push(rows[i].id);
				}
				var cgjhDetStr = cgjhDetIds.join(',');
				$.ajax({
					url : '${pageContext.request.contextPath}/jxc/cgjhAction!toKfrk.action',
					data : {
						cgjhDetIds : cgjhDetStr
					},
					dataType : 'json',
					success : function(d) {
						$('input[name=gysbh]').val(rows[0].gysbh);
						$('input[name=gysmc]').val(rows[0].gysmc);
						jxc_kfrk_ckCombo.combobox('setValue', rows[0].ckId);
						kfrk_spdg.datagrid('loadData', d.rows);
						updateFooter();
						$('input[name=cgjhDetIds]').val(cgjhDetStr);
						kfrk_tabs.tabs('select', 0);
					}
				});
			}
		});
	}else{
		$.messager.alert('警告', '请选择最少一条记录进行操作！',  'warning');
	}
}

function searchCgjhInKfrk(){
	kfrk_cgjhDg.datagrid('load',{
		bmbh: did,
		createTime: $('input[name=createTimeCgjhInKfrk]').val(),
		search: $('input[name=searchCgjhInKfrk]').val(),
		fromOther: 'fromKfrk'
	});
}

//////////////////////////////////////////////以上为采购计划列表处理代码

//////////////////////////////////////////////以下为业务入库列表处理代码

function createKfrkFromYwrk(){
	var row = kfrk_ywrkDg.datagrid('getSelected');
	//var ywrklshs = [];
	if(row != undefined){
		$.messager.confirm('请确认', '是否要将选中记录进行入库？', function(r) {
			if (r) {
				$.ajax({
					url : '${pageContext.request.contextPath}/jxc/ywrkAction!toKfrk.action',
					data : {
						ywrklsh : row.ywrklsh
					},
					dataType : 'json',
					success : function(d) {
						$('input[name=gysbh]').val(d.obj.gysbh);
						$('input[name=gysmc]').val(d.obj.gysmc);
						kfrk_spdg.datagrid('loadData', d.rows);
						updateFooter();
						$('input[name=ywrklsh]').val(row.ywrklsh);
						$('input[name=jxc_kfrk_bz]').val(row.bz);
						kfrk_tabs.tabs('select', 0);
					}
				});
			}
		});
	}else{
		$.messager.alert('警告', '请选择一条记录进行操作！',  'warning');
	}
}

function searchYwrkInKfrk(){
	kfrk_ywrkDg.datagrid('load',{
		bmbh: did,
		createTime: $('input[name=createTimeYwrkInKfrk]').val(),
		fromOther: 'fromKfrk'
	});
}

//////////////////////////////////////////////以上为业务入库列表处理代码



</script>

<!-- tabPosition:'left', headerWidth:'35' -->
<div id="jxc_kfrk_tabs" class="easyui-tabs" data-options="fit:true, border:false," style="width:100%;height:100%;">
	
    <div title="新增记录" data-options="closable:false">
        <div id='jxc_kfrk_layout' style="height:100%;width=100%">
			<div data-options="region:'north',title:'单据信息',border:false,collapsible:false" style="width:100%;height:150px">		
				<table class="tinfo">
					<tr>
						<td colspan="4"></td>
						<th class="read">时间</th><td><div id="createDate" class="read"></div></td>
						<th class="read">单据号</th><td><div id="kfrkLsh" class="read"></div></td>
					</tr>
					<tr>
						<th>供应商编码</th><td><input name="gysbh" class="easyui-validatebox"
							data-options="validType:['mustLength[8]','integer']" onkeyup="gysLoad()" size="8"></td>
						<th class="read">供应商名称</th><td><input name="gysmc" readonly="readonly" size="50"></td>
						<th>仓库</th><td><input id="jxc_kfrk_ckId" name="ckId" size="8"></td>
					</tr>
					<tr>
						<th>送货人</th><td><input name="shry" type="text" size="8"></td>
						<th>证件号</th><td><input name="zjh" type="text" size="40"></td>
						<th>车号</th><td><input name="ch" type="text" size="8"></td>
					</tr>
					<tr>
						<th>备注</th><td colspan="7"><input name="jxc_kfrk_bz" style="width:90%"></td>
					</tr>
				</table>
				<input name="cgjhDetIds" type="hidden">
				<input name="ywrklsh" type="hidden">
			</div>
			<div data-options="region:'center',title:'商品信息',split:true" style="width:150px">		
				<table id='jxc_kfrk_spdg'></table>
			</div>
			<div data-options="region:'east',title:'商品库存信息',split:true,collapsed:true," style="width:150px">
				<table id="show_spkc"></table>
			</div>
		</div>
    </div>
    <div title="库房入库列表" data-options="closable:false" >
    	<table id='jxc_kfrk_dg'></table>
    </div>
	<div title="采购计划列表" data-options="closable:false" >
		<table id='jxc_kfrk_cgjhDg'></table>
	</div>
	<div title="业务入库列表" data-options="closable:false" >
		<table id='jxc_kfrk_ywrkDg'></table>
	</div>
</div>

<div id="jxc_kfrk_tb" style="padding:3px;height:auto">
	请输入查询起始日期:<input type="text" name="createTimeKfrk" class="easyui-datebox" data-options="value: moment().date(1).format('YYYY-MM-DD')" style="width:100px">
	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchKfrk();">查询</a>
</div>
<div id="jxc_kfrk_cgjhTb" style="padding:3px;height:auto">
<!-- 	请输入查询起始日期:<input type="text" name="createTimeCgjhInKfrk" class="easyui-datebox" data-options="value: moment().date(1).format('YYYY-MM-DD')" style="width:100px"> -->
	输入流水号、供应商、商品编号、商品名称、备注：<input type="text" name="searchCgjhInKfrk" style="width:100px">
	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchCgjhInKfrk();">查询</a>
</div>
<div id="jxc_kfrk_ywrkTb" style="padding:3px;height:auto">
	请输入查询起始日期:<input type="text" name="createTimeYwrkInKfrk" class="easyui-datebox" data-options="value: moment().date(1).format('YYYY-MM-DD')" style="width:100px">
	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchYwrkInKfrk();">查询</a>
</div>

