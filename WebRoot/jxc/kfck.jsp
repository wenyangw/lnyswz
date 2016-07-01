<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>


<script type="text/javascript">
var jxc_kfck_did;
var jxc_kfck_lx;
var jxc_kfck_menuId;
var kfck_spdg;
var kfck_dg;
var kfck_xsthDg;
var editIndex = undefined;
var kfck_tabs;

var jxc_kfck_ckCombo;
var jxc_kfck_fhCombo;

//编辑行字段
var spbhEditor;   
var spmcEditor;
var spcdEditor;
var spppEditor;
var spbzEditor;
var hwIdEditor;
var sppcEditor;
var zjldwEditor;
var zthslEditor;
var zytslEditor;
var zslEditor;
var cjldwEditor;
var cthslEditor;
var cytslEditor;
var cslEditor;
var zhxsEditor;
var zjldwIdEditor;
var cjldwIdEditor;

$(function(){
	jxc_kfck_did = lnyw.tab_options().did;
	jxc_kfck_lx = lnyw.tab_options().lx;
	jxc_kfck_menuId = lnyw.tab_options().id;

	$('#jxc_kfck_layout').layout({
		fit : true,
		border : false,
	});
	
	kfck_dg = $('#jxc_kfck_dg').datagrid({
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
			{field:'kfcklsh',title:'流水号',align:'center'},
	        {field:'createTime',title:'时间',align:'center'},
	        {field:'khbh',title:'供应商编号',align:'center'},
	        {field:'khmc',title:'供应商名称',align:'center'},
	        {field:'ckId',title:'仓库id',align:'center',hidden:true},
	        {field:'ckmc',title:'仓库',align:'center'},
	        {field:'fhId',title:'分户id',align:'center',hidden:true},
	        {field:'fhmc',title:'分户',align:'center'},
	        {field:'thfs',title:'提货方式',align:'center',
	        	formatter : function(value) {
					if (value == '1') {
						return '自提';
					} else {
						return '送货';
					}
				}},
	        {field:'thr',title:'送货人',align:'center'},
	        {field:'ch',title:'车号',align:'center'},
	        {field:'shdz',title:'送货地址',align:'center'},
        	{field:'bz',title:'备注',align:'center'},
//         	{field:'xsthlsh',title:'销售提货流水号',align:'center'},
        	{field:'xsthlshs',title:'销售提货',align:'center',
           		formatter: function(value){
           			return lnyw.memo(value, 15);
           		}},
//            	{field:'xskplsh',title:'业务入库流水号',align:'center',
//                		formatter: function(value){
//                			return lnyw.memo(value, 15);
//                		}},
        	{field:'isCj',title:'*状态',align:'center',sortable:true,
        		formatter : function(value) {
					if (value == '1') {
						return '已冲减';
					} else {
						return '';
					}
				},
        		sorter: function(a,b){
        			a = a == undefined ? 0 : a;
        			b = b == undefined ? 0 : b;
					return (a-b);  
				}},
			{field:'cjTime',title:'冲减时间',align:'center'},
        	{field:'cjKfcklsh',title:'原库房出库流水号',align:'center'},
	    ]],
	    toolbar:'#jxc_kfck_tb',
	});
	lnyw.toolbar(1, kfck_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', jxc_kfck_did);
	
	
	kfck_dg.datagrid({
        view: detailview,
        detailFormatter:function(index,row){
            return '<div style="padding:2px"><table id="kfck-ddv-' + index + '"></table></div>';
        },
        onExpandRow: function(index,row){
            $('#kfck-ddv-'+index).datagrid({
                url:'${pageContext.request.contextPath}/jxc/kfckAction!detDatagrid.action',
                fitColumns:true,
                singleSelect:true,
                rownumbers:true,
                loadMsg:'',
                height:'auto',
                queryParams: {
        			kfcklsh: row.kfcklsh,
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
                    {field:'zdwsl',title:'数量1',width:100,align:'center'},
                    {field:'cjldwmc',title:'单位2',width:100,align:'center'},
                    {field:'cdwsl',title:'数量2',width:100,align:'center'},
                ]],
                onResize:function(){
                	kfck_dg.datagrid('fixDetailRowHeight',index);
                },
                onLoadSuccess:function(){
                    setTimeout(function(){
                    	kfck_dg.datagrid('fixDetailRowHeight',index);
                    },0);
                }
            });
            kfck_dg.datagrid('fixDetailRowHeight',index);
        }
    });
	
	
	kfck_xsthDg = $('#jxc_kfck_xsthDg').datagrid({
		fit : true,
	    border : false,
	    remoteSort: false,
// 	    fitColumns: true,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
		columns:[[
			{field:'id',title:'销售提货DetId',align:'center',checkbox:true},
			{field:'locked',title:'锁定',align:'center',
				formatter : function(value) {
					if (value == '1') {
						return '是';
					} else {
						return '';
					}
				},
        		sorter: function(a,b){
        			a = a == undefined ? 0 : a;
        			b = b == undefined ? 0 : b;
					return (a-b);  
				}},
			{field:'xsthlsh',title:'流水号',align:'center'},
	        {field:'createTime',title:'时间',align:'center'},
	        {field:'createName',title:'创建人',align:'center'},
	        {field:'khbh',title:'供应商编号',align:'center',hidden:true},
	        {field:'khmc',title:'供应商名称',align:'center'},
	        {field:'ywyId',title:'业务员id',align:'center',hidden:true},
	        {field:'ywymc',title:'业务员',align:'center'},
	        {field:'ckId',title:'仓库id',align:'center',hidden:true},
	        {field:'ckmc',title:'仓库',align:'center'},
	        {field:'fhId',title:'分户id',align:'center',hidden:true},
	        {field:'fhmc',title:'分户',align:'center'},
    		{field:'spbh',title:'商品编号',align:'center'},
            {field:'spmc',title:'名称',align:'center'},
            {field:'spcd',title:'产地',align:'center'},
            {field:'sppp',title:'品牌',align:'center'},
            {field:'spbz',title:'包装',align:'center'},
            {field:'zjldwmc',title:'单位1',align:'center'},
            {field:'zdwsl',title:'数量1',align:'center'},
            {field:'cksl',title:'已提数量',align:'center',
            	formatter: function(value){
            		return value == 0 ? '' : value;
            	},
            	styler:function(){
            		return 'color:red;';
            	}},
//             {field:'zdwytsl',title:'已提数量',align:'center',
//             	formatter: function(value){
//             		return value == 0 ? '' : value;
//             	},
//             	styler:function(){
//             		return 'color:red;';
//             	}},
            {field:'thsl',title:'原提货数量',align:'center',
            	formatter: function(value){
            		return value == 0 ? '' : value;
            	},
            	styler:function(){
            		return 'color:blue;';
            	}},
            {field:'cjldwmc',title:'单位2',align:'center'},
            {field:'cdwsl',title:'数量2',align:'center'},
            {field:'ccksl',title:'已提数量2',align:'center',
            	formatter: function(value){
            		return value == 0 ? '' : value;
            	},
            	styler:function(){
            		return 'color:red;';
            	}},
	        {field:'thfs',title:'到货方式',align:'center',
	        	formatter : function(value) {
					if (value == '1') {
						return '自提';
					} else {
						return '送货';
					}
				}},
	        {field:'thr',title:'提货人',align:'center'},
	        {field:'ch',title:'车号',align:'center'},
	        {field:'shdz',title:'送货地址',align:'center'},
        	{field:'bz',title:'备注',align:'center',
        		formatter: function(value){
        			return lnyw.memo(value, 15);
        		}},
        	{field:'isKp',title:'已开票',align:'center',
        		formatter: function(value){
            		return value == '1' ? '是' : '';
            	}},
        	{field:'kfcklshs',title:'库房出库流水号',align:'center',
           		formatter: function(value){
           			return lnyw.memo(value, 15);
           		}},
	    ]],
	    toolbar:'#jxc_kfck_xsthTb',
	});
	lnyw.toolbar(2, kfck_xsthDg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', jxc_kfck_did);
	
	
// 	kfck_xsthDg.datagrid({
//         view: detailview,
//         detailFormatter:function(index,row){
//             return '<div style="padding:2px"><table id="ddv-' + index + '"></table></div>';
//         },
//         onExpandRow: function(index,row){
//             $('#ddv-'+index).datagrid({
//                 url:'${pageContext.request.contextPath}/jxc/xsthAction!detDatagrid.action',
//                 fitColumns:true,
//                 singleSelect:true,
//                 rownumbers:true,
//                 loadMsg:'',
//                 height:'auto',
//                 queryParams: {
//         			xsthlsh: row.xsthlsh,
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
//                 	kfck_xsthDg.datagrid('fixDetailRowHeight',index);
//                 },
//                 onLoadSuccess:function(){
//                     setTimeout(function(){
//                     	kfck_xsthDg.datagrid('fixDetailRowHeight',index);
//                     },0);
//                 }
//             });
//             kfck_xsthDg.datagrid('fixDetailRowHeight',index);
//         }
//     });
	

	//选中列表标签后，装载数据
	kfck_tabs = $('#jxc_kfck_tabs').tabs({
		onSelect: function(title, index){
			if(index == 1){
				kfck_dg.datagrid({
					url: '${pageContext.request.contextPath}/jxc/kfckAction!datagrid.action',
					queryParams:{
						bmbh: jxc_kfck_did,
					}
				});
			}
			if(index == 2){
				kfck_xsthDg.datagrid({
					url: '${pageContext.request.contextPath}/jxc/xsthAction!datagridDet.action',
					queryParams: {
						bmbh: jxc_kfck_did,
						fromOther: 'fromKfck',
						ckId: jxc_kfck_ckCombo.combobox('getValue'),
						},
				});
			}
		},
	});
	
	kfck_spdg = $('#jxc_kfck_spdg').datagrid({
		fit : true,
	    border : false,
	    singleSelect : true,
	    fitColumns: true,
	    autoRowHeight: false,
	    showFooter: true,
		columns:[[
	        {field:'spbh',title:'商品编号',width:25,align:'center',editor:'textRead'},
	        {field:'spmc',title:'商品名称',width:100,align:'center',editor:'textRead'},
	        {field:'spcd',title:'产地',width:25,align:'center',editor:'textRead'},
	        {field:'sppp',title:'品牌',width:25,align:'center',editor:'text',hidden:true},
	        {field:'spbz',title:'包装',width:25,align:'center',editor:'text',hidden:true},
	        {field:'hwId',title:'货位',width:25,align:'center',
	        	editor:{
	        		type: 'combobox',
	        		options:{
	        			required: true,
	        			valueField : 'id',
	        			textField : 'hwmc',
	        			panelHeight: 'auto',
	        			//url: '${pageContext.request.contextPath}/jxc/hwAction!listHw.action?depId=' + jxc_kfck_did,
	        			
	        		}
	        	}},
	        {field:'sppc',title:'批次',width:25,align:'center',editor:'datebox'},
	        {field:'zjldwmc',title:'单位1',width:15,align:'center',editor:'textRead'},
	        {field:'zdwthsl',title:'开单数量1',width:20,align:'center',editor:'textRead'},
	        {field:'zdwytsl',title:'已提数量1',width:20,align:'center',editor:'textRead'},
	        {field:'zdwsl',title:'提货数量1',width:25,align:'center',
	        	editor:{
	        		type:'numberbox',
	        		options:{
	        			//精度
	        			precision: LENGTH_SL,
	        		}}},
	        {field:'cjldwmc',title:'单位2',width:15,align:'center',editor:'textRead'},
	        {field:'cdwthsl',title:'开单数量2',width:25,align:'center',editor:'textRead'},
	        {field:'cdwytsl',title:'已提数量2',width:25,align:'center',editor:'textRead'},
	        {field:'cdwsl',title:'提货数量2',width:25,align:'center',
	        		editor:{
        				type:'numberbox',
        				options:{
        					//精度
        					precision: LENGTH_SL,
        			}}},
        	{field:'zhxs',title:'转换系数',width:25,align:'center',editor:'text', hidden:true},
        	{field:'zjldwId',title:'主单位id',width:25,align:'center',editor:'text', hidden:true},
        	{field:'cjldwId',title:'次单位id',width:25,align:'center',editor:'text', hidden:true},
	    ]],
        onClickRow: clickRow,
        onAfterEdit: function (rowIndex, rowData, changes) {
            //endEdit该方法触发此事件
            editIndex = undefined;
        },
        
         
	});
	
	//初始化仓库列表
	jxc_kfck_ckCombo = lnyw.initCombo($("#jxc_kfck_ckId"), 'id', 'ckmc', '${pageContext.request.contextPath}/jxc/ckAction!listCk.action?depId=' + jxc_kfck_did);
	//初始化分户列表
	jxc_kfck_fhCombo = lnyw.initCombo($("#jxc_kfck_fhId"), 'id', 'fhmc', '${pageContext.request.contextPath}/jxc/fhAction!listFhs.action?depId=' + jxc_kfck_did);
	
	//初始化创建时间
	$('#createDate').html(moment().format('YYYY年MM月DD日'));
	

	//初始化信息
	init();
});

//以下为商品列表处理代码
function init(){
	//清空列表，添加一个空行
	kfck_spdg.datagrid({
		data: [{}],
	});
	
	//清空全部字段
	$('input').val('');
	//$('input:checkbox').removeAttr('checked');
	//$('input:checkbox').removeProp('checked');
	$('input:checkbox').prop('checked', false);
	//收回商品库存信息
	$('#jxc_kfck_layout').layout('collapse', 'east');
	jxc.spInfo($('#jxc_kfck_layout'), '');
	
	//隐藏分户选择列表
	$('.jxc_kfck_isFh').css('display', 'none');
	
	//默认自提选中
	$('input#thfs_zt').attr('checked', 'checked');
	//自提显示
	$('.thfs_zt').css('display', 'inline');
	//送货隐藏
	$('.thfs_sh').css('display', 'none');
	
	jxc_kfck_ckCombo.combobox("selectedIndex", 0);
	jxc_kfck_fhCombo.combobox("selectedIndex", 0);
	

	//初始化流水号
	$.ajax({
		type: "POST",
		url: '${pageContext.request.contextPath}/jxc/lshAction!getLsh.action',
		data: {
			bmbh: jxc_kfck_did,
			lxbh: jxc_kfck_lx,
		},
		dataType: 'json',
		success: function(d){
			if(d.success){
				$('#kfckLsh').html(d.obj);
			}  
		},
	});
	
	//根据权限，动态加载功能按钮
	lnyw.toolbar(0, kfck_spdg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', jxc_kfck_did);
	
	//清空合计内容
	kfck_spdg.datagrid('reloadFooter',[{}]);
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
	if(rowIndex == kfck_spdg.datagrid('getRows').length - 1){
		kfck_spdg.datagrid('appendRow', {});
	}else{
		if(!isClick){
			return false;
		}
	}
	//将选中行标识为编辑行
	editIndex = rowIndex;
	//选择编辑行，并进行入编辑状态
	kfck_spdg.datagrid('selectRow', editIndex)
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
				kfck_spdg.datagrid('endEdit', editIndex);
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
    kfck_spdg.datagrid('cancelEdit', editIndex)
            .datagrid('deleteRow', editIndex);
    editIndex = undefined;
    updateFooter();
    $('#jxc_kfck_layout').layout('collapse', 'east');
}

//保存行
function accept(){
    if (rowOk()){
    	kfck_spdg.datagrid('acceptChanges');
    	$('#jxc_kfck_layout').layout('collapse', 'east');
    }
}

//取消编辑行
function cancelAll(){
	if(editIndex != undefined){
		kfck_spdg.datagrid('rejectChanges');
	}else{
    	editIndex = undefined;
	}
    updateFooter();
    $('#jxc_kfck_layout').layout('collapse', 'east');
}

//提交数据到后台
function saveAll(){
	var msg = formValid();
	if(msg == ''){
		//编辑行是否完成
		if(rowOk()){
			kfck_spdg.datagrid('endEdit', editIndex);
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
	
	var rows = kfck_spdg.datagrid('getRows');
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
	var footerRows = kfck_spdg.datagrid('getFooterRows');
	var effectRow = new Object();
	//将表头内容传入后台
	effectRow['khbh'] = $('input[name=khbh]').val();
	effectRow['khmc'] = $('input[name=khmc]').val();
	effectRow['ckId'] = jxc_kfck_ckCombo.combobox('getValue');
	effectRow['ckmc'] = jxc_kfck_ckCombo.combobox('getText');
	
	if($('#jxc_kfck_isFh').is(':checked')){
		effectRow['isFh'] = '1';
		effectRow['fhId'] = jxc_kfck_fhCombo.combobox('getValue');
		effectRow['fhmc'] = jxc_kfck_fhCombo.combobox('getText');
	}else{
		effectRow['isFh'] = '0';
	}
	if($('#thfs_zt').is(':checked')){
		effectRow['thfs'] = '1';
		effectRow['ch'] = $('input[name=ch]').val();
		effectRow['thr'] = $('input[name=thr]').val();
	}else{
		effectRow['thfs'] = '0';
		effectRow['shdz'] = $('input[name=shdz]').val();
	}
	effectRow['bz'] = $('input[name=jxc_kfck_bz]').val();
	effectRow['xsthDetIds'] = $('input[name=xsthDetIds]').val();
	effectRow['xsthlsh'] = $('input[name=xsthlsh]').val();
// 	effectRow['xskplsh'] = $('input[name=xskplsh]').val();
	
	effectRow['bmbh'] = jxc_kfck_did;
	effectRow['lxbh'] = jxc_kfck_lx;
	effectRow['menuId'] = jxc_kfck_menuId;
	
	
	//将表格中的数据去掉最后一个空行后，转换为json格式
	effectRow['datagrid'] = JSON.stringify(rows.slice(0, rows.length - 1));
	//提交到action
	//$.ajaxSettings.traditional=true;
	////MaskUtil.mask('正在保存，请等待……');
	$.ajax({
		type: "POST",
		url: '${pageContext.request.contextPath}/jxc/kfckAction!save.action',
		data: effectRow,
		dataType: 'json',
		success: function(rsp){
			if(rsp.success){
		    	$.messager.show({
					title : '提示',
					msg : '提交成功！'
				});
		    	init();
		    	$.messager.confirm('请确认', '是否打印库房出库单？', function(r) {
					if (r) {
						var url = lnyw.bp() + '/jxc/kfckAction!printKfck.action?kfcklsh=' + rsp.obj.kfcklsh + '&bmbh=' + jxc_kfck_did;
						jxc.print(url, PREVIEW_REPORT, HIDE_PRINT_WINDOW);
					}
				});
			}  
		},
		error: function(){
			$.messager.alert("提示", "提交错误了！");
		},
		complete: function(){
			////MaskUtil.unmask();
		}
	});
}

//处理编辑行
function setEditing(){
	//初始化编辑行
	var spRow = undefined;
	
	//加载字段
	var editors = kfck_spdg.datagrid('getEditors', editIndex);
    spbhEditor = editors[0];
    spmcEditor = editors[1];
    spcdEditor = editors[2];
    spppEditor = editors[3];
    spbzEditor = editors[4];
    hwIdEditor = editors[5];
    sppcEditor = editors[6];
    zjldwEditor = editors[7];
    zthslEditor = editors[8];
    zytslEditor = editors[9];
    zslEditor = editors[10];
    cjldwEditor = editors[11];
    cthslEditor = editors[12];
    cytslEditor = editors[13];
    cslEditor = editors[14];
    zhxsEditor = editors[15];
    zjldwIdEditor = editors[16];
    cjldwIdEditor = editors[17];
    
    if($(spbhEditor.target).val() != ''){
    	jxc.spInfo($('#jxc_kfck_layout'), '1', $(spppEditor.target).val(), $(spbzEditor.target).val());
    	jxc.showKc('#jxc_kfck_layout', 
    			'${pageContext.request.contextPath}/jxc/kfckAction!getSpkc.action', 
    			{
    				bmbh : jxc_kfck_did, 
    				ckId : jxc_kfck_ckCombo.combobox('getValue'),
    				fhId : jxc_kfck_fhCombo.combobox('getValue'),
    				spbh : $(spbhEditor.target).val(),
//     				hwId : $(hwIdEditor.target).val(), 
//     				sppc : $(sppcEditor.target).val(), 
    			});
    }else{
    	jxc.spInfo($('#jxc_kfck_layout'), '');
    	jxc.hideKc('#jxc_kfck_layout');
    }
    
    //初始化当前商品货位
    $.ajax({
		type: "POST",
		url: '${pageContext.request.contextPath}/jxc/kfzzAction!findHwId.action',
		data: {
			bmbh: jxc_kfck_did,
			spbh: $(spbhEditor.target).val(),
		},
		dataType: 'json',
		success: function(d){
			$(hwIdEditor.target).combobox('reload', '${pageContext.request.contextPath}/jxc/hwAction!listHw.action?depId=' + jxc_kfck_did + '&ckId=' + jxc_kfck_ckCombo.combobox('getValue'));
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
				kfck_spdg.datagrid('endEdit', editIndex);
				enterEdit(rowIndex + 1, false);
			}else{
				if(!keyOk()){
					removeRow();
				}
			}
		}
	});
	
    //处理商品编号按键事件
//     spbhEditor.target.bind('keydown', function(event){
//     	//按Tab键,根据商品编号获取商品信息
//     	if(event.keyCode == 9){
//     		if($(this).val().trim().length == 7){
//     			if(!existKey($(this).val(), editIndex)){
//     				$.ajax({
//     					url:'${pageContext.request.contextPath}/jxc/spAction!loadSp.action',
//     					async: false,
//     					context:this,
//     					data:{
//     						spbh: $(this).val(),
//     						depId : jxc_kfck_did,
//     					},
//     					dataType:'json',
//     					success:function(data){
//     						if(data.success){
//     							//设置信息字段值
//     							setValueBySpbh(data.obj);
//     							sppcEditor.target.focus();
//     						}else{
//     							$.messager.alert('提示', '商品编号不存在！', 'error');
//     						}
//     					}
//     				});
//     			}else{
//     				$.messager.alert('提示', '商品编号只能出现一次，请重新输入！', 'error');
//     			}
//     		}else{
//     			$.messager.alert('提示', '商品编号必须是7位，请重新输入！', 'error');
//     		}
//     		return false;
//     	}
//     	//按ESC键，弹出对话框，可以按商品编号或名称查询，双击商品行返回信息
//     	if(event.keyCode == 27){
//     		jxc.spQuery($(spbhEditor.target).val(),
//     				jxc_kfck_did,
//     				'${pageContext.request.contextPath}/jxc/spQuery.jsp',
//     				'${pageContext.request.contextPath}/jxc/spAction!spDg.action',
//     				hwIdEditor);
// 			sppcEditor.target.focus();
//     		return false;
//     	}
//     });
    
    //输入主单位数量后，计算次单位数量
    zslEditor.target.bind('keyup', function(event){
    	if($(zthslEditor.target).val() > 0){
    		if((Number($(zslEditor.target).val()) - (Number($(zthslEditor.target).val()) - Number($(zytslEditor.target).val()))).toFixed(3) > 0){
    			$.messager.alert("提示", "提货数量大于未提货数量，请重新输入！");
        		$(zslEditor.target).numberbox('setValue', 0);
        		zslEditor.target.focus();
        		return false;
    		}
    	}else{
    		if($(zslEditor.target).val() < ($(zthslEditor.target).val() - $(zytslEditor.target).val())){
        		$.messager.alert("提示", "输入的提货数量不在未提货数量范围内，请重新输入！");
        		$(zslEditor.target).numberbox('setValue', 0);
        		zslEditor.target.focus();
        		return false;
        	}	
    	}
    	
    	if(($(spbhEditor.target).val().substring(0, 3) < '513'
    			|| $(spbhEditor.target).val().substring(0, 3) > '518')
    			&& $(zhxsEditor.target).val() != 0){
    		$(cslEditor.target).numberbox('setValue', $(zslEditor.target).val() / $(zhxsEditor.target).val());
    	}
    	calculate();
    }).bind('keydown', function(event){
     	if(event.keyCode == 9){
     		zdjEditor.target.focus();
     		return false;
     	}
    });
    
  	
    cslEditor.target.bind('keyup', function(event){
    	if(($(spbhEditor.target).val().substring(0, 3) < '513'
    			|| $(spbhEditor.target).val().substring(0, 3) > '518')
    			&& $(zhxsEditor.target).val() != 0){
    		$(zslEditor.target).numberbox('setValue', $(cslEditor.target).val() * $(zhxsEditor.target).val());
    	}
    	calculate();
    }).bind('keydown', function(event){
     	if(event.keyCode == 9){
     		cdjEditor.target.focus();
     		return false;
     	}
    });
        
    
    //计算金额
    function calculate(){
        
        //更新汇总列
        updateFooter();
    }
  	
    //初始进入编辑状态时，使用商品编号获得焦点
    spbhEditor.target.focus();
}
//求和
function updateFooter(){
 	var rows = kfck_spdg.datagrid('getRows');
	var spmc_footer = '合计';
	var hjsl = 0.000000;
	$.each(rows, function(){
		var index = kfck_spdg.datagrid('getRowIndex', this);
		if(index < rows.length - 1){
		if(editIndex == index){
			hjsl += Number(cslEditor.target.val());
		}else{
			hjsl += Number(this.cdwsl);
		}
	}
 		
 	});
	
	kfck_spdg.datagrid('reloadFooter', [{spmc : spmc_footer, cdwsl : hjsl !== hjsl ? '' : hjsl.toFixed(LENGTH_SL)}]);
}

//验证记录是否已存在
function existKey(value, rowIndex){
	var keys = kfck_spdg.datagrid('getRows');
	var exist = false;
	if(keys.length > 2){
		$.each(keys, function(){
			var index = kfck_spdg.datagrid('getRowIndex', this);
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
// 	if($('input[name=khmc]').val() == ''){
// 		message += '供应商信息<br>';
// 	}
	
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
	zhxsEditor.target.val(rowData.zhxs);
	zjldwIdEditor.target.val(rowData.zjldwId);
	cjldwIdEditor.target.val(rowData.cjldwId);
	
	jxc.spInfo($('#jxc_kfck_layout'), '1', rowData.sppp, rowData.spbz);
	//初始化货位，将返回商品的货位设为默认值
	$.ajax({
		type: "POST",
		url: '${pageContext.request.contextPath}/jxc/kfzzAction!findHwId.action',
		data: {
			bmbh: jxc_kfck_did,
			spbh: rowData.spbh,
		},
		dataType: 'json',
		success: function(d){
			$(hwIdEditor.target).combobox('reload', '${pageContext.request.contextPath}/jxc/hwAction!listHw.action?depId=' + jxc_kfck_did + '&ckId=' + jxc_kfck_ckCombo.combobox('getValue'));
			$(hwIdEditor.target).combobox('selectedIndex', 0);
		},
	});
	
	//初始化商品批次
	$(sppcEditor.target).datebox('setValue', moment().format('YYYY-MM-DD'));
}

// function khLoad(){
// 	switch(event.keyCode){
// 	case 27:
// 		jxc.query('供应商检索', $('input[name=khbh]'), $('input[name=khmc]'), 
// 				'${pageContext.request.contextPath}/jxc/query.jsp',
// 				'${pageContext.request.contextPath}/jxc/khAction!khDg.action');
// 		break;
// 	case 9:
// 		break;
// 	default:
// 		if($('input[name=khbh]').val().trim().length == 8){
// 			$.ajax({
// 				url:'${pageContext.request.contextPath}/jxc/khAction!loadGys.action',
// 				async: false,
// 				context:this,
// 				data:{
// 					khbh: $('input[name=khbh]').val().trim(),
// 				},
// 				dataType:'json',
// 				success:function(data){
// 					if(data.success){
// 						//设置信息字段值
// 						$('input[name=khmc]').val(data.obj.khmc);
// 					}else{
// 						$.messager.alert('提示', '供应商信息不存在！', 'error');
// 					}
// 				}
// 			});
// 		}
// 		break;
// 	}
// }
//////////////////////////////////////////////以上为商品列表处理代码

//////////////////////////////////////////////以下为库房出库列表处理代码
function cjKfck(){
	var row = kfck_dg.datagrid('getSelected');
	if (row != undefined) {
		if(row.xskplsh == null){
			if(row.isCj != '1'){
				$.messager.confirm('请确认', '是否要冲减选中的库房出库单？', function(r) {
					if (r) {
						////MaskUtil.mask('正在冲减，请等待……');
						$.ajax({
							url : '${pageContext.request.contextPath}/jxc/kfckAction!cjKfck.action',
							data : {
								kfcklsh : row.kfcklsh,
								bmbh: jxc_kfck_did,
								lxbh: jxc_kfck_lx,
								menuId: jxc_kfck_menuId,
							},
							dataType : 'json',
							success : function(d) {
								kfck_dg.datagrid('load');
								kfck_dg.datagrid('unselectAll');
								$.messager.show({
									title : '提示',
									msg : d.msg
								});
							},
							complete: function(){
								////MaskUtil.unmask();
							}
						});
					}
					});
			}else{
				$.messager.alert('警告', '选中的库房入库记录已被冲减，请重新选择！',  'warning');
			}
		}else{
			$.messager.alert('警告', '选中的库房入库已进行业务入库，不能被冲减，请重新选择！',  'warning');
		}
	}else{
		$.messager.alert('警告', '请选择一条记录进行操作！',  'warning');
	}
}

function searchKfck(){
	kfck_dg.datagrid('load',{
		bmbh: jxc_kfck_did,
		createTime: $('input[name=createTimeKfck]').val(),
		search: $('input[name=searchKfck]').val(),
	});
}

//////////////////////////////////////////////以上为库房出库列表处理代码


//////////////////////////////////////////////以下为销售提货列表处理代码

function generateKfck(){
	var rows = kfck_xsthDg.datagrid('getSelections');
	var xsthDetIds = [];
	var flag = true;
	if(rows.length > 0){
    	if(rows.length > 1){
    		var preRow = undefined;
		    $.each(rows, function(index){
		    	if(index != 0){
		    		if(this.xsthlsh != preRow.xsthlsh){
		    			$.messager.alert('提示', '请选择同一张销售提货单的商品进行出库！', 'error');
						flag = false;
						return false;
		    		}else{
		    			preRow = this;
		    		}
		    	}
		    	preRow = this;
		    });
    	}
    	if(flag){
			$.messager.confirm('请确认', '是否要将选中记录进行出库？', function(r) {
				if (r) {
					for ( var i = 0; i < rows.length; i++) {
						xsthDetIds.push(rows[i].id);
					}
					var xsthDetIdsStr = xsthDetIds.join(',');
					$.ajax({
						url : '${pageContext.request.contextPath}/jxc/xsthAction!toKfck.action',
						data : {
							xsthDetIds : xsthDetIdsStr
						},
						dataType : 'json',
						success : function(d) {
							kfck_spdg.datagrid('loadData', d.rows);
							updateFooter();
							$('input[name=xsthDetIds]').val(xsthDetIdsStr);
//							$('input[name=xsthlsh]').val(rows[0].xsthlsh);
							$('input[name=khbh]').val(rows[0].khbh);
							$('input[name=khmc]').val(rows[0].khmc);
							jxc_kfck_ckCombo.combobox('setValue', (jxc_kfck_did == '04') ? jxc.getCkByKhbh(jxc_kfck_did, '00000000') : rows[0].ckId);
							if(rows[0].isFh == '1'){
								
								$('#jxc_kfck_isFh').prop('checked', 'checked');
								$('.jxc_kfck_isFh').css('display', 'inline');
								jxc_kfck_fhCombo.combobox('setValue', rows[0].fhId);
							}
							if(rows[0].thfs == '1'){
								$('input#thfs_zt').attr('checked', 'checked');
								$('.thfs_zt').css('display', 'inline');
								$('.thfs_sh').css('display', 'none');
								$('input[name=thr]').val(rows[0].thr);
								$('input[name=ch]').val(rows[0].ch);
							}else{
								$('input#thfs_sh').attr('checked', 'checked');
								$('input[name=shdz]').val(rows[0].shdz);
								$('.thfs_zt').css('display', 'none');
								$('.thfs_sh').css('display', 'inline');
							}
							$('input[name=jxc_kfck_bz]').val(rows[0].bz);
						
							
							kfck_tabs.tabs('select', 0);
						}
					});
				}
			});
    	}
	}else{
		$.messager.alert('警告', '请选择最少一条记录进行操作！',  'warning');
	}
}

function printKfck(){
	var row = kfck_dg.datagrid('getSelected');
	if (row != undefined) {
		$.messager.confirm('请确认', '是否打印库房出库单？', function(r) {
			if (r) {
				var url = lnyw.bp() + '/jxc/kfckAction!printKfck.action?kfcklsh=' + row.kfcklsh + "&bmbh=" + jxc_kfck_did;
				jxc.print(url, PREVIEW_REPORT, HIDE_PRINT_WINDOW);
			}
		});
	}else{
		$.messager.alert('警告', '请选择一条记录进行操作！',  'warning');
	}
}

function printThd(){
	var row = kfck_xsthDg.datagrid('getSelected');
	if (row != undefined) {
		$.messager.confirm('请确认', '是否打印销售提货单？', function(r) {
			if (r) {
				var url = lnyw.bp() + '/jxc/xsthAction!printThd.action?xsthlsh=' + row.xsthlsh + "&bmbh=" + jxc_kfck_did;
				jxc.print(url, PREVIEW_REPORT, HIDE_PRINT_WINDOW);
			}
		});
	}else{
		$.messager.alert('警告', '请选择一条记录进行操作！',  'warning');
	}
}

function printXsthByBgy(){
	var row = kfck_xsthDg.datagrid('getSelected');
	if (row != undefined) {
		$.messager.confirm('请确认', '是否按不同保管员打印销售提货单？', function(r) {
			if (r) {
				var bgyIds = undefined;
				$.ajax({
					url:'${pageContext.request.contextPath}/jxc/xsthAction!getSpBgys.action',
					async: false,
					data:{
						xsthlsh: row.xsthlsh,
					},
					dataType:'json',
					success:function(data){
						if(data.rows.length != 0){
							//设置信息字段值
							bgyIds = data.rows;
						}else{
							$.messager.alert('提示', '该单据商品未按保管员分类！', 'error');
						}
					}
				});
			
				$.each(bgyIds, function(index){
					$.messager.confirm('请确认', '是否打印销售提货单(<font color="red">'+ this.bgyName + '</font>)？', function(r) {
						if (r) {
							var url = lnyw.bp() + '/jxc/xsthAction!printXsthByBgy.action?xsthlsh=' + row.xsthlsh + "&bmbh=" + jxc_kfck_did + "&bgyId=" + bgyIds[index].bgyId;
							jxc.print(url, PREVIEW_REPORT, HIDE_PRINT_WINDOW);
						}
					});
				});
			}
		});
	}else{
		$.messager.alert('警告', '请选择一条记录进行操作！',  'warning');
	}
}


function updateThsl(){
	var row = kfck_xsthDg.datagrid('getSelected');
	if(row != undefined){
		//if(row.thsl == 0){
			if(row.isKp != '1'){
				$.messager.prompt('请确认', '是否要修改提货数量？请输入', function(thsl){
					if (thsl != undefined){
						$.ajax({
							url : '${pageContext.request.contextPath}/jxc/xsthAction!updateThsl.action',
							data : {
								id : row.id,
								thsl: thsl,
								fromOther: '',
								bmbh : jxc_kfck_did,
								menuId : jxc_kfck_menuId,
							},
							dataType : 'json',
							success : function(d) {
								kfck_xsthDg.datagrid('reload');
								kfck_xsthDg.datagrid('unselectAll');
								$.messager.show({
									title : '提示',
									msg : d.msg
								});
							}
						});
					}
				});
			}else{
				$.messager.alert('警告', '选中的销售提货已开发票，不允许修改数量，请重新选择！',  'warning');
			}
		//}else{
		//	$.messager.alert('警告', '选中的销售提货已修改数量，请重新选择！',  'warning');
		//}
	}else{
		$.messager.alert('警告', '请选择一条记录进行操作！',  'warning');
	}
}

function lockXsth(){
	var rows = kfck_xsthDg.datagrid('getSelections');
	if(rows.length == 1){
		if(rows[0].locked != '1'){
			$.messager.confirm('请确认', '您是否要锁定选中的销售提货单？', function(r) {
				if (r) {
					$.ajax({
						url : '${pageContext.request.contextPath}/jxc/xsthAction!updateLock.action',
						data : {
							xsthlsh : rows[0].xsthlsh,
							bmbh : jxc_kfck_did,
							menuId : jxc_kfck_menuId,
						},
						dataType : 'json',
						success : function(d) {
							kfck_xsthDg.datagrid('reload');
							kfck_xsthDg.datagrid('unselectAll');
							$.messager.show({
								title : '提示',
								msg : d.msg
							});
						}
					});
				}
			});
		}else{
			$.messager.alert('警告', '选中的销售提货已被锁定，请重新选择！',  'warning');
		}
	}else{
		$.messager.alert('警告', '请选择一条记录进行操作！',  'warning');
	}
}

function unlockXsth(){
	var rows = kfck_xsthDg.datagrid('getSelections');
	if (rows.length == 1) {
		if(rows[0].locked != '0'){
			$.messager.confirm('请确认', '您是否要解锁选中的销售提货单？', function(r) {
				if (r) {
					$.ajax({
						url : '${pageContext.request.contextPath}/jxc/xsthAction!updateUnlock.action',
						data : {
							xsthlsh : rows[0].xsthlsh,
							bmbh : jxc_kfck_did,
							menuId : jxc_kfck_menuId,
						},
						dataType : 'json',
						success : function(d) {
							kfck_xsthDg.datagrid('reload');
							kfck_xsthDg.datagrid('unselectAll');
							$.messager.show({
								title : '提示',
								msg : d.msg
							});
						}
					});
				}
			});
		}else{
			$.messager.alert('警告', '选中的销售提货还未锁定，请重新选择！',  'warning');
		}
	}else{
		$.messager.alert('警告', '请选择一条记录进行操作！',  'warning');
	}
}

function searchXsthInKfck(){
	kfck_xsthDg.datagrid('load',{
		bmbh: jxc_kfck_did,
		createTime: $('input[name=createTimeXsthInKfck]').val(),
		search: $('input[name=searchXsthInKfck]').val(),
		fromOther: 'fromKfck'
	});
}

//////////////////////////////////////////////以上为采购计划列表处理代码

</script>

<!-- tabPosition:'left', headerWidth:'35' -->
<div id="jxc_kfck_tabs" class="easyui-tabs" data-options="fit:true, border:false," style="width:100%;height:100%;">
	
    <div title="新增记录" data-options="closable:false">
        <div id='jxc_kfck_layout' style="height:100%;width=100%">
			<div class="tinfo" data-options="region:'north',title:'单据信息',border:false,collapsible:false" style="width:100%;height:150px">		
				<div class="form_line">
					<span class="read form_label">分户</span>
					<span class="read"><input type="checkbox" id="jxc_kfck_isFh" name="isFh" disabled="disabled"></span>
					<span class="read form_label">时间</span>
					<span id="createDate" class="read"></span>
					<span class="read form_label">单据号</span>
					<span id="kfckLsh" class="read"></span>
				</div>
				<div class="form_line">
					<span class="read form_label">客户编号</span>
					<span><input name="khbh" class="easyui-validatebox"
						data-options="validType:['mustLength[8]','integer']" class="read" size="8" disabled="disabled"></span>
					<span class="read form_label">供应商名称</span>
					<span class="read"><input name="khmc" disabled="disabled" size="20"></span>
					<span class="read jxc_kfck_isFh form_label" style="display:none">分户</span>
					<span class="read jxc_kfck_isFh" style="display:none"><input id="jxc_kfck_fhId" name="fhId" disabled="disabled" size="8"></span>
				</div>
				<div class="form_line">
					<span class="read form_label">仓库</span>
					<span class="read"><input id="jxc_kfck_ckId" name="ckId" disabled="disabled" size="8"></span>
					<span class="read form_label">自提<input type="radio" name="thfs" id='thfs_zt' disabled="disabled"></span>
					<span class="read form_label">送货<input type="radio" name="thfs" id="thfs_sh" disabled="disabled"></span>
					<span class="read thfs_zt form_label">提货人</span>
					<span class="read thfs_zt"><input name="thr" type="text" disabled="disabled" size="8"></span>
					<span class="read thfs_zt form_label">车号</span>
					<span class="read thfs_zt"><input name="ch" type="text" disabled="disabled" size="8"></span>
					<span class="read thfs_sh form_label" style="display:none">送货地址</span>
					<span class="read thfs_sh" style="display:none"><input name="shdz" type="text" disabled="disabled" size="20"></span>
				</div>
				<div class="form_line">
					<span class="form_label">备注</span>
					<span><input name="jxc_kfck_bz" style="width:90%"></span>
				</div>
				<input name="xsthDetIds" type="hidden">
				<input name="xskplsh" type="hidden">
			</div>
			<div data-options="region:'center',title:'商品信息',split:true" style="width:150px">		
				<table id='jxc_kfck_spdg'></table>
			</div>
			<div data-options="region:'east',title:'商品库存信息',split:true,collapsed:true," style="width:150px">
				<table id="show_spkc"></table>
			</div>
		</div>
    </div>
    <div title="库房出库列表" data-options="closable:false" >
    	<table id='jxc_kfck_dg'></table>
    </div>
	<div title="销售提货列表" data-options="closable:false" >
		<table id='jxc_kfck_xsthDg'></table>
	</div>
</div>

<div id="jxc_kfck_tb" style="padding:3px;height:auto">
	请输入查询起始日期:<input type="text" name="createTimeKfck" class="easyui-datebox" data-options="value: moment().date(1).format('YYYY-MM-DD')" style="width:100px">
	输入流水号、客户编号、名称、备注：<input type="text" name="searchKfck" style="width:100px">
	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchKfck();">查询</a>
</div>
<div id="jxc_kfck_xsthTb" style="padding:3px;height:auto">
	请输入查询起始日期:<input type="text" name="createTimeXsthInKfck" class="easyui-datebox" data-options="value: moment().date(1).format('YYYY-MM-DD')" style="width:100px">
	输入流水号、客户编号、名称、业务员、备注：<input type="text" name="searchXsthInKfck" style="width:100px">
	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchXsthInKfck();">查询</a>
</div>
