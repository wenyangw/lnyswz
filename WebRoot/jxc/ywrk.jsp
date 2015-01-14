<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>


<script type="text/javascript">
var ywrk_did;
var ywrk_lx;
var ywrk_menuId;
var ywrk_spdg;
var ywrk_dg;
var ywrk_kfrkDg;
var ywrk_cgjhDg;
var ywrk_xskpDg;
var editIndex = undefined;
var ywrk_tabs;
var countYwrk = 0;
var countKfrkInYwrk = 0;
var countCgjhInYwrk = 0;
var countXskpInYwrk = 0;

var jxc_ywrk_ckCombo;
var jxc_ywrk_rklxCombo;
var jxc_ywrk_depCombo;

//编辑行字段
var spbhEditor;   
var spmcEditor;
var spcdEditor;
var spppEditor;
var spbzEditor;
var zjldwEditor;
var zjhslEditor;
var zyrslEditor;
var zslEditor;
var zdjEditor;
var cjldwEditor;
var cjhslEditor;
var cyrslEditor;
var cslEditor;
var cdjEditor;   
var spjeEditor;
var zhxsEditor;
var zjldwIdEditor;
var cjldwIdEditor;

$(function(){
	ywrk_did = lnyw.tab_options().did;
	ywrk_lx = lnyw.tab_options().lx;
	ywrk_menuId = lnyw.tab_options().id;
	
	$('#jxc_ywrk_layout').layout({
		fit : true,
		border : false,
	});
	
	ywrk_dg = $('#jxc_ywrk_dg').datagrid({
		fit : true,
	    border : false,
// 	    singleSelect : true,
	    remoteSort: false,
 	    fitColumns: true,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
		columns:[[
			{field:'ywrklsh',title:'流水号',align:'center',
				styler: function(value, rowData){
					if(rowData.isCj == '1'){
						return 'color:red;';
					}
				}},
	        {field:'createTime',title:'时间',align:'center'},
	        {field:'gysbh',title:'*供应商编号',align:'center',sortable:true,
	        	sorter: function(a, b){
	        		return  a > b;
	        	}},
	        {field:'gysmc',title:'供应商名称',align:'center'},
	        {field:'ckId',title:'仓库id',align:'center',hidden:true},
	        {field:'ckmc',title:'仓库名称',align:'center'},
	        {field:'rklxId',title:'入库类型id',align:'center',hidden:true},
	        {field:'rklxmc',title:'*入库方式',align:'center',sortable:true,
	        	sorter: function(a, b){
	        		return  a > b;
	        	}},
	        {field:'hjje',title:'金额',align:'center',
	        	formatter: function(value){
	        		return lnyw.formatNumberRgx(value);
	        	}},
	        {field:'bz',title:'备注',align:'center',
        		formatter: function(value){
        			return lnyw.memo(value, 15);
        		}},
        	{field:'kfrklshs',title:'库房入库',align:'center',
           		formatter: function(value){
           			return lnyw.memo(value, 15);
           		}},
        	{field:'cgjhlshs',title:'采购计划',align:'center',
           		formatter: function(value){
           			return lnyw.memo(value, 15);
           		}},
           	{field:'ywbtlsh',title:'业务补调',align:'center',},
        	{field:'isZs',title:'*直送',align:'center',sortable:true,
        		formatter : function(value) {
					if (value == '1') {
						return '是';
					} else {
						return '否';
					}
				},
        		sorter: function(a,b){
        			a = a == undefined ? 0 : a;
        			b = b == undefined ? 0 : b;
					return (a-b);  
				}},
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
				},
				styler: function(value){
					if(value == '1'){
						return 'color:red;';
					}
				}},
			{field:'cjTime',title:'冲减时间',align:'center'},
        	{field:'cjYwrklsh',title:'原业务入库流水号',align:'center'},
        	{field:'beYwrklsh',title:'暂估入库流水号',align:'center'},
	    ]],
	    toolbar:'#jxc_ywrk_tb',
	});
	lnyw.toolbar(1, ywrk_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', ywrk_did);
	
	
	ywrk_dg.datagrid({
        view: detailview,
        detailFormatter:function(index,row){
            return '<div style="padding:2px"><table id="ywrk-ddv-' + index + '"></table></div>';
        },
        onExpandRow: function(index,row){
            $('#ywrk-ddv-'+index).datagrid({
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
                    {field:'zdwdj',title:'单价1',width:100,align:'center'},
                    {field:'cjldwmc',title:'单位2',width:100,align:'center'},
                    {field:'cdwsl',title:'数量2',width:100,align:'center'},
                    {field:'cdwdj',title:'单价2',width:100,align:'center'},
                    {field:'spje',title:'金额',width:100,align:'center',
        	        	formatter: function(value){
        	        		return lnyw.formatNumberRgx(value);
        	        	}},
                ]],
                onResize:function(){
                	ywrk_dg.datagrid('fixDetailRowHeight',index);
                },
                onLoadSuccess:function(){
                    setTimeout(function(){
                    	ywrk_dg.datagrid('fixDetailRowHeight',index);
                    },0);
                }
            });
            ywrk_dg.datagrid('fixDetailRowHeight',index);
        }
    });
	
	
	ywrk_kfrkDg = $('#jxc_ywrk_kfrkDg').datagrid({
		fit : true,
	    border : false,
	    //remoteSort: false,
 	    fitColumns: true,
// 	    singleSelect: true, 
	    pagination : true,
		pageSize : pageSize,
		pageList : pageList,
		columns:[[
			{field:'kfrklsh',title:'流水号',align:'center', width:70},
	        {field:'createTime',title:'时间',align:'center',width:120},
	        {field:'gysbh',title:'*供应商编号',align:'center',width:70},
	        {field:'gysmc',title:'供应商名称',align:'center',width:150},
	        {field:'ckId',title:'仓库id',align:'center',hidden:true},
	        {field:'ckmc',title:'仓库名称',align:'center',width:80},
        	{field:'bz',title:'备注',align:'center',width:300,
        		formatter: function(value){
        			return lnyw.memo(value, 15);
        		}},
//         	{field:'isCj',title:'状态',align:'center',sortable:true,
//         		formatter : function(value) {
// 					if (value == '1') {
// 						return '已冲减';
// 					} else {
// 						return '正常';
// 					}
// 				},
//         		sorter: function(a,b){
//         			a = a == undefined ? 0 : a;
//         			b = b == undefined ? 0 : b;
// 					return (a-b);  
// 				}},
	    ]],
	    toolbar:'#jxc_ywrk_kfrkTb',
	});
	lnyw.toolbar(2, ywrk_kfrkDg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', ywrk_did);
	
	ywrk_kfrkDg.datagrid({
        view: detailview,
        detailFormatter:function(index,row){
            return '<div style="padding:2px"><table id="ywrk-kfrk-ddv-' + index + '"></table></div>';
        },
        onExpandRow: function(index,row){
            $('#ywrk-kfrk-ddv-'+index).datagrid({
                url:'${pageContext.request.contextPath}/jxc/kfrkAction!detDatagrid.action',
                fitColumns:false,
                singleSelect:true,
                rownumbers:true,
                height:'auto',
                queryParams: {
        			kfrklsh: row.kfrklsh,
        		},
                columns:[[
                    {field:'spbh',title:'商品编号',width:60,align:'center'},
                    {field:'spmc',title:'名称',width:200,align:'center'},
                    {field:'spcd',title:'产地',width:100,align:'center'},
                    {field:'sppp',title:'品牌',width:100,align:'center'},
                    {field:'spbz',title:'包装',width:100,align:'center'},
                    {field:'sppc',title:'批次',width:100,align:'center'},
                    {field:'zjldwmc',title:'单位1',width:100,align:'center'},
                    {field:'zdwsl',title:'数量1',width:100,align:'center'},
                    {field:'cjldwmc',title:'单位2',width:100,align:'center'},
                    {field:'cdwsl',title:'数量2',width:100,align:'center'},
                    {field:'bzsl',title:'包装数量',width:100,align:'center'},
                ]],
                onResize:function(){
                	ywrk_kfrkDg.datagrid('fixDetailRowHeight',index);
                },
                onLoadSuccess:function(){
                    setTimeout(function(){
                    	ywrk_kfrkDg.datagrid('fixDetailRowHeight',index);
                    },0);
                }
            });
            ywrk_kfrkDg.datagrid('fixDetailRowHeight',index);
        }
    });
	
	
	ywrk_cgjhDg = $('#jxc_ywrk_cgjhDg').datagrid({
		fit : true,
	    border : false,
	    //remoteSort: false,
	    //fitColumns: true,
// 	    singleSelect: true, 
	    pagination : true,
		pageSize : pageSize,
		pageList : pageList,
		columns:[[
// 			{field:'cgjhlsh',title:'流水号',align:'center'},
// 	        {field:'createTime',title:'时间',align:'center'},
// 	        {field:'gysbh',title:'供应商编号',align:'center'},
// 	        {field:'gysmc',title:'供应商名称',align:'center'},
// 	        {field:'ckId',title:'仓库id',align:'center',hidden:true},
// 	        {field:'ckmc',title:'仓库名称',align:'center'},
//         	{field:'bz',title:'备注',align:'center',
//         		formatter: function(value){
//         			return lnyw.memo(value, 15);
//         		}},
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
			{field:'zdwyrsl',title:'入库数量',align:'center',
			   	formatter: function(value){
			   		return value == 0 ? '' : value;
					},
					styler: function(value,row){
						return 'color:red;';
				}},
			{field:'zdwdj',title:'单价1',align:'center',
			   	formatter: function(value){
			   		return value == 0 ? '' : value;
				}},	
			{field:'cjldwmc',title:'单位2',align:'center'},
			{field:'cdwsl',title:'数量2',align:'center',
			   	formatter: function(value){
			   		return value == 0 ? '' : value;
				}},
			{field:'cdwdj',title:'单价2',align:'center',
			   	formatter: function(value){
			   		return value == 0 ? '' : value;
				}},
			{field:'spje',title:'金额',align:'center',
			   	formatter: function(value){
			   		return value == 0 ? '' : lnyw.formatNumberRgx(value);
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
			{field:'ywrklshs',title:'业务入库流水号',align:'center',
				formatter: function(value){
					return lnyw.memo(value, 15);
				}},
	    ]],
	    toolbar:'#jxc_ywrk_cgjhTb',
	});
	lnyw.toolbar(3, ywrk_cgjhDg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', ywrk_did);
	
	ywrk_xskpDg = $('#jxc_ywrk_xskpDg').datagrid({
		fit : true,
	    border : false,
	    singleSelect : true,
	    remoteSort: false,
	    fitColumns: false,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
		columns:[[
			{field:'xskplsh',title:'流水号',align:'center'},
	        {field:'createTime',title:'*时间',align:'center',sortable:true,
				sorter: function(a,b){
					return moment(a).diff(moment(b), 'days');
				}},
	        {field:'khbh',title:'客户编号',align:'center'},
	        {field:'khmc',title:'客户名称',align:'center'},
	        {field:'ckId',title:'仓库id',align:'center',hidden:true},
	        {field:'ckmc',title:'仓库',align:'center'},
	        {field:'fhId',title:'分户 id',align:'center',hidden:true},
	        {field:'fhmc',title:'分户',align:'center'},
	        {field:'jsfsmc',title:'结算方式',align:'center'},
	        {field:'ywymc',title:'业务员',align:'center'},
	        {field:'fplxmc',title:'发票类型',align:'center'},
	        {field:'bookmc',title:'书名',align:'center'},
	        {field:'kpr',title:'制单人',align:'center'},
	        {field:'hjje',title:'金额',align:'center',
	        	formatter: function(value){
	        		return lnyw.formatNumberRgx(value);
	        	}},
	        {field:'hjse',title:'税额',align:'center',
		        	formatter: function(value){
		        		return lnyw.formatNumberRgx(value);
		        	}},
	        {field:'bz',title:'备注',align:'center',
        		formatter: function(value){
        			return lnyw.memo(value, 15);
        		}},
        	{field:'xsthlshs',title:'销售提货流水号',align:'center',
           		formatter: function(value){
           			return lnyw.memo(value, 15);
           		}},
        	{field:'isZs',title:'*直送',align:'center',sortable:true,
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
        	{field:'isCj',title:'*状态',align:'center',sortable:true,
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
        	{field:'cjXskplsh',title:'原销售流水号',align:'center'},
	    ]],
	    toolbar:'#jxc_ywrk_xskpTb',
	});
	lnyw.toolbar(4, ywrk_xskpDg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', ywrk_did);
	
	ywrk_xskpDg.datagrid({
        view: detailview,
        detailFormatter:function(index,row){
            return '<div style="padding:2px"><table id="ywrk-xskp-ddv-' + index + '"></table></div>';
        },
        onExpandRow: function(index,row){
            $('#ywrk-xskp-ddv-'+index).datagrid({
                url:'${pageContext.request.contextPath}/jxc/xskpAction!detDatagrid.action',
                fitColumns:true,
                singleSelect:true,
                rownumbers:true,
                loadMsg:'',
                height:'auto',
                queryParams: {
        			xskplsh: row.xskplsh,
        		},
                columns:[[
                    {field:'spbh',title:'商品编号',width:200,align:'center'},
                    {field:'spmc',title:'名称',width:100,align:'center'},
                    {field:'spcd',title:'产地',width:100,align:'center'},
                    {field:'sppp',title:'品牌',width:100,align:'center'},
                    {field:'spbz',title:'包装',width:100,align:'center'},
                    {field:'zjldwmc',title:'单位1',width:100,align:'center'},
                    {field:'zdwsl',title:'数量1',width:100,align:'center'},
                    {field:'zdwdj',title:'单价1',width:100,align:'center'},
                    {field:'cjldwmc',title:'单位2',width:100,align:'center'},
                    {field:'cdwsl',title:'数量2',width:100,align:'center'},
                    {field:'cdwdj',title:'单价2',width:100,align:'center'},
                    {field:'spje',title:'金额',width:100,align:'center',
        	        	formatter: function(value){
        	        		return lnyw.formatNumberRgx(value);
        	        	}},
                    {field:'spse',title:'税额',width:100,align:'center',
        		        	formatter: function(value){
        		        		return lnyw.formatNumberRgx(value);
        		        	}},
                ]],
                onResize:function(){
                	xskp_dg.datagrid('fixDetailRowHeight',index);
                },
                onLoadSuccess:function(){
                    setTimeout(function(){
                    	xskp_dg.datagrid('fixDetailRowHeight',index);
                    },0);
                }
            });
            xskp_dg.datagrid('fixDetailRowHeight',index);
        }
    });
	
	//选中列表标签后，装载数据
	ywrk_tabs = $('#jxc_ywrk_tabs').tabs({
		onSelect: function(title, index){
			if(index == 1){
				ywrk_dg.datagrid({
					url: '${pageContext.request.contextPath}/jxc/ywrkAction!datagrid.action',
					queryParams:{
						bmbh: ywrk_did,
						createTime: countYwrk == 0 ? undefined : $('input[name=createTimeYwrk]').val(),
						search: countYwrk == 0 ? undefined : $('input[name=searchYwrk]').val(),
					}
				});
				countYwrk++;
			}
			if(index == 2){
				ywrk_kfrkDg.datagrid({
					url: '${pageContext.request.contextPath}/jxc/kfrkAction!datagrid.action',
					queryParams: {
						bmbh: ywrk_did,
						fromOther: 'fromYwrk',
						createTime: countKfrkInYwrk == 0 ? undefined : $('input[name=createTimeKfrkInYwrk]').val(),
						search: countKfrkInYwrk == 0 ? undefined : $('input[name=searchKfrkInYwrk]').val(),
						},
				});
				countKfrkInYwrk++;
			}
			if(index == 3){
				ywrk_cgjhDg.datagrid({
					url: '${pageContext.request.contextPath}/jxc/cgjhAction!datagridDet.action',
					queryParams: {
						bmbh: ywrk_did,
						fromOther: 'fromYwrk',
						createTime: countCgjhInYwrk == 0 ? undefined : $('input[name=createTimeCgjhInYwrk]').val(),
						search: countCgjhInYwrk == 0 ? undefined : $('input[name=searchCgjhInYwrk]').val(),
						isZs : '1',
						},
				});
				countCgjhInYwrk++;
			}
			if(index == 4){
				ywrk_xskpDg.datagrid({
					url: '${pageContext.request.contextPath}/jxc/xskpAction!datagrid.action',
					queryParams: {
						bmbh: ywrk_did,
						fromOther: 'fromYwrk',
						createTime: countXskpInYwrk == 0 ? undefined : $('input[name=createTimeXskpInYwrk]').val(),
						search: countXskpInYwrk == 0 ? undefined : $('input[name=searchXskpInYwrk]').val(),
						otherBm: jxc.otherBm(ywrk_did)['bmbh']
						//isZs : '1',
						},
				});
				countXskpInYwrk++;
			}
			
		},
	});
	
	ywrk_spdg = $('#jxc_ywrk_spdg').datagrid({
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
	        {field:'sppp',title:'商品品牌',width:25,align:'center',editor:'textRead',hidden:true},
	        {field:'spbz',title:'商品包装',width:25,align:'center',editor:'textRead',hidden:true},
	        {field:'zjldwmc',title:'单位1',width:25,align:'center',editor:'textRead'},
	        {field:'zdwjhsl',title:'计划数量1',width:25,align:'center',editor:'textRead'},
	        {field:'zdwyrsl',title:'已入数量1',width:25,align:'center',editor:'textRead'},
	        {field:'zdwsl',title:'数量1',width:25,align:'center',
	        	editor:{
	        		type:'numberbox',
	        		options:{
	        			//精度
	        			precision:LENGTH_SL,
	        		}}},
       		{field:'zdwdj',title:'单价1',width:25,align:'center',
    	        editor:{
    	        	type:'numberbox',
    	        	options:{
    	        		precision: LENGTH_JE
    	        }}},		
	        {field:'cjldwmc',title:'单位2',width:25,align:'center',editor:'textRead'},
	        {field:'cdwjhsl',title:'计划数量2',width:25,align:'center',editor:'textRead'},
	        {field:'cdwyrsl',title:'已入数量2',width:25,align:'center',editor:'textRead'},
	        {field:'cdwsl',title:'数量2',width:25,align:'center',
	        		editor:{
        				type:'numberbox',
        				options:{
        					//精度
        					precision:LENGTH_SL,
        			}}},
   			{field:'cdwdj',title:'单价2',width:25,align:'center',
    	        editor:{
    	        	type:'numberbox',
    	        	options:{
    	        		precision: LENGTH_JE
    	        }}},
   	        {field:'spje',title:'金额',width:25,align:'center',
   	        	editor:{
           			type:'numberbox',
           			options:{
           				precision: LENGTH_JE
           			}}},
        	{field:'zhxs',title:'转换系数',width:25,align:'center',editor:'text', hidden:true},
        	{field:'zjldwId',title:'主单位id',width:25,align:'center',editor:'text', hidden:true},
        	{field:'cjldwId',title:'次单位id',width:25,align:'center',editor:'text', hidden:true},
        	{field:'thsl',title:'提货数量',width:25,align:'center',editor:'text', hidden:true},
	    ]],
        onClickRow: clickRow,
        onAfterEdit: function (rowIndex, rowData, changes) {
            //endEdit该方法触发此事件
            editIndex = undefined;
        },
        
         
	});
	
	//初始化创建时间
	$('#createDate').html(moment().format('YYYY年MM月DD日'));
	
	$('input[name=isDep]').click(function(){
		depChange();
	});
	
	$('input[name=isZs]').click(function(){
		if($(this).is(':checked')){
			$('.isZs').css('display','table-cell');
		}else{
			$('.isZs').css('display','none');
			$('input[name=shdz]').val('');
		}
	});
	
	//初始化入库类型列表
	//jsfsCombo = lnyw.initCombo($("input[name=jsfsId]"), 'id', 'jsmc', '${pageContext.request.contextPath}/jxc/jsfsAction!listJsfs.action');
	jxc_ywrk_rklxCombo = lnyw.initCombo($("#jxc_ywrk_rklxId"), 'id', 'rklxmc', '${pageContext.request.contextPath}/jxc/rklxAction!listRklx.action');
	
	//初始化仓库列表
	jxc_ywrk_ckCombo = lnyw.initCombo($("#jxc_ywrk_ckId"), 'id', 'ckmc', '${pageContext.request.contextPath}/jxc/ckAction!listCk.action?depId=' + ywrk_did);
	

	//初始化信息
	init();
});

//以下为商品列表处理代码
function init(){
	//清空列表，添加一个空行
	ywrk_spdg.datagrid({
		data: [{}],
	});
	
	//清空全部字段
	$('input').val('');
	//$('input:checkbox').removeAttr('checked');
	//$('input:checkbox').removeProp('checked');
	$('input:checkbox').prop('checked', false);
	//收回商品库存信息
	jxc.hideKc('#jxc_ywrk_layout');
	jxc.spInfo($('#jxc_ywrk_layout'), '');

	jxc_ywrk_ckCombo.combobox('selectedIndex', 0);
	jxc_ywrk_rklxCombo.combobox('selectedIndex', 0);
	
	//初始化流水号
	$.ajax({
		type: "POST",
		url: '${pageContext.request.contextPath}/jxc/lshAction!getLsh.action',
		data: {
			bmbh: ywrk_did,
			lxbh: ywrk_lx,
		},
		dataType: 'json',
		success: function(d){
			if(d.success){
				$('#ywrkLsh').html(d.obj);
			}  
		},
	});
	
	//根据权限，动态加载功能按钮
	lnyw.toolbar(0, ywrk_spdg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', ywrk_did);
	
	//清空合计内容
	ywrk_spdg.datagrid('reloadFooter',[{}]);
}

function depChange(bmbh){
	if($('input[name=isDep]').is(':checked')){
		$('.isDep').css('display','table-cell');
		//初始化部门列表
		if(jxc_ywrk_depCombo == undefined){
			jxc_ywrk_depCombo = lnyw.initCombo($("#jxc_ywrk_depId"), 'id', 'depName', '${pageContext.request.contextPath}/admin/departmentAction!listDeps.action');
		}
	}else{
		$('.isDep').css('display','none');
	}
	if(bmbh == undefined){
		jxc_ywrk_depCombo.combobox('selectedIndex', 0);
	}else{
		jxc_ywrk_depCombo.combobox('setValue', bmbh);
	}
}

//判断行是否编辑完成
function rowOk(){
	if(editIndex == undefined){
		return true;
	}
	if(keyOk()){
		if(zslEditor.target.val() >0 ){
			return true;
		}
	}
	return false;
}

//判断商品信息是否完成
function keyOk(){
	if(editIndex != undefined){
		if(spmcEditor.target.val().length > 0){
			return true;
		}
	}
	return false;
}

//根据指定行，进入编辑状态
function enterEdit(rowIndex, isClick){
	//如果选中行为最后一行，先增加一个空行
	if(rowIndex == ywrk_spdg.datagrid('getRows').length - 1){
		ywrk_spdg.datagrid('appendRow', {});
	}else{
		if(!isClick){
			return false;
		}
	}
	//将选中行标识为编辑行
	editIndex = rowIndex;
	//选择编辑行，并进行入编辑状态
	ywrk_spdg.datagrid('selectRow', editIndex)
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
				ywrk_spdg.datagrid('endEdit', editIndex);
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
    ywrk_spdg.datagrid('cancelEdit', editIndex)
            .datagrid('deleteRow', editIndex);
    editIndex = undefined;
    updateFooter();
    jxc.hideKc('#jxc_ywrk_layout');
}

//保存行
function accept(){
    if (rowOk()){
    	ywrk_spdg.datagrid('acceptChanges');
    	jxc.hideKc('#jxc_ywrk_layout');
    }
}

//取消编辑行
function cancelAll(){
	ywrk_spdg.datagrid('rejectChanges');
    editIndex = undefined;
    updateFooter();
    jxc.hideKc('#jxc_ywrk_layout');
}

//提交数据到后台
function saveAll(){
	var msg = formValid();
	if(msg == ''){
		//编辑行是否完成
		if(rowOk()){
			ywrk_spdg.datagrid('endEdit', editIndex);
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
	
	var rows = ywrk_spdg.datagrid('getRows');
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
	var footerRows = ywrk_spdg.datagrid('getFooterRows');
	var effectRow = new Object();
	//将表头内容传入后台
	if($('input[name=isZs]').is(':checked')){
		effectRow['isZs'] =  '1';
		effectRow['shdz'] =  $('input[name=shdz]').val();
	}else{
		effectRow['isZs'] =  '0';
	}
	if($('input[name=isDep]').is(':checked')){
		effectRow['isDep'] =  '1';
		effectRow['depId'] =  jxc_ywrk_depCombo.combobox('getValue');
		effectRow['depName'] =  jxc_ywrk_depCombo.combobox('getText');
	}else{
		effectRow['isDep'] =  '0';
	}
	
	effectRow['gysbh'] = $('input[name=gysbh]').val();
	effectRow['gysmc'] = $('input[name=gysmc]').val();
	effectRow['ckId'] = jxc_ywrk_ckCombo.combobox('getValue');
	effectRow['ckmc'] = jxc_ywrk_ckCombo.combobox('getText');
	effectRow['rklxId'] = jxc_ywrk_rklxCombo.combobox('getValue');
	effectRow['rklxmc'] = jxc_ywrk_rklxCombo.combobox('getText');
	effectRow['bz'] = $('input[name=jxc_ywrk_bz]').val();
	effectRow['hjje'] = lnyw.delcommafy(footerRows[0]['spje']); 
	effectRow['xskplsh'] = $('input[name=xskplsh]').val();
	effectRow['kfrklshs'] = $('input[name=kfrklshs]').val();
	effectRow['ywrklshs'] = $('input[name=ywrklshs]').val();
	effectRow['cgjhDetIds'] = $('input[name=cgjhDetIds]').val();
	effectRow['bmbh'] = ywrk_did;
	effectRow['lxbh'] = ywrk_lx;
	effectRow['menuId'] = ywrk_menuId;
	
	
	//将表格中的数据去掉最后一个空行后，转换为json格式
	effectRow['datagrid'] = JSON.stringify(rows.slice(0, rows.length - 1));
	//提交到action
	//$.ajaxSettings.traditional=true;
	$.ajax({
		type: "POST",
		url: '${pageContext.request.contextPath}/jxc/ywrkAction!save.action',
		data: effectRow,
		dataType: 'json',
		success: function(rsp){
			if(rsp.success){
		    	$.messager.show({
					title : '提示',
					msg : '提交成功！'
				});
		    	init();
		    	$.messager.confirm('请确认', '是否打印业务入库单？', function(r) {
					if (r) {
						var url = lnyw.bp() + '/jxc/ywrkAction!printYwrk.action?ywrklsh=' + rsp.obj.ywrklsh + '&bmbh=' + ywrk_did;
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
	var editors = ywrk_spdg.datagrid('getEditors', editIndex);
    spbhEditor = editors[0];
    spmcEditor = editors[1];
    spcdEditor = editors[2];
    spppEditor = editors[3];
    spbzEditor = editors[4];
    zjldwEditor = editors[5];
    zjhslEditor = editors[6];
    zyrslEditor = editors[7];
    zslEditor = editors[8];
    zdjEditor = editors[9];
    cjldwEditor = editors[10];
    cjhslEditor = editors[11];
    cyrslEditor = editors[12];
    cslEditor = editors[13];
    cdjEditor = editors[14];
    spjeEditor = editors[15];
    zhxsEditor = editors[16];
    zjldwIdEditor = editors[17];
    cjldwIdEditor = editors[18];
    
    
    if($(spbhEditor.target).val() != ''){
    	jxc.spInfo($('#jxc_ywrk_layout'), '1', $(spppEditor.target).val(), $(spbzEditor.target).val());
    	jxc.showKc('#jxc_ywrk_layout', 
    			'${pageContext.request.contextPath}/jxc/ywrkAction!getSpkc.action', 
    			{
    				bmbh : ywrk_did, 
    				ckId : jxc_ywrk_ckCombo.combobox('getValue'),
    				spbh : $(spbhEditor.target).val(),
    			});
    }else{
    	jxc.spInfo($('#jxc_ywrk_layout'), '');
    	jxc.hideKc('#jxc_ywrk_layout');
    }
    
	//loadEditor();
	//处理编辑行的换行事件
	$('.datagrid-row-editing').on('keydown','input', function(event){
		//监听按下向下箭头
		if(event.keyCode == 40){
			var rowIndex = editIndex;
			if(rowOk()){
				ywrk_spdg.datagrid('endEdit', editIndex);
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
    						depId: ywrk_did,
    					},
    					dataType:'json',
    					success:function(data){
    						if(data.success){
    							//设置信息字段值
    							setValueBySpbh(data.obj);
    							zslEditor.target.focus();
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
    		jxc.spQuery($(spbhEditor.target).val(),
    				ywrk_did,
    				undefined,
    				'${pageContext.request.contextPath}/jxc/spQuery.jsp',
    				'${pageContext.request.contextPath}/jxc/spAction!spDg.action',
    				zslEditor
    				);
    		return false;
    	}
    });
    
    //输入主单位数量后，计算次单位数量
    zslEditor.target.bind('keyup', function(event){
    	if($(zhxsEditor.target).val() != 0){
    		$(cslEditor.target).numberbox('setValue', $(zslEditor.target).val() / $(zhxsEditor.target).val());
    	}
    	calculate();
    }).bind('keydown', function(event){
     	if(event.keyCode == 9){
     		zdjEditor.target.focus();
     		return false;
     	}
    });
    
    zdjEditor.target.bind('keyup', function(event){
    	if($(zhxsEditor.target).val() != 0){
    		$(cdjEditor.target).numberbox('setValue', $(zdjEditor.target).val() * $(zhxsEditor.target).val() * (1 + SL));
    	}
    	calculate();
    }).bind('keydown', function(event){
     	if(event.keyCode == 9){
     		cslEditor.target.focus();
     		return false;
     	}
    });
    
    cslEditor.target.bind('keyup', function(event){
    	if($(zhxsEditor.target).val() != 0){
    		$(zslEditor.target).numberbox('setValue', $(cslEditor.target).val() * $(zhxsEditor.target).val());
    	}
    	calculate();
    }).bind('keydown', function(event){
     	if(event.keyCode == 9){
     		cdjEditor.target.focus();
     		return false;
     	}
    });
    
  	//输入次单位单价后，计算金额
    cdjEditor.target.bind('keyup', function(event){
    	if($(zhxsEditor.target).val() != 0){
    		$(zdjEditor.target).numberbox('setValue', $(cdjEditor.target).val() / $(zhxsEditor.target).val() / (1 + SL));
    	}
    	calculate();
    }).bind('keydown', function(event){
    	if(event.keyCode == 40){
     		spjeEditor.target.focus();
     	}
    });
  	
  	//输入每行总额后,重新计算单价
    spjeEditor.target.bind('keyup', function(event){
    	$(zdjEditor.target).numberbox('setValue', $(this).val() / $(zslEditor.target).val());
    	if($(zhxsEditor.target).val() != 0 && $(cslEditor.target).val() != 0){
    		$(cdjEditor.target).numberbox('setValue', $(this).val() * (1 + SL) / $(zslEditor.target).val() * $(zhxsEditor.target).val());
    	}else{
    		$(cdjEditor.target).numberbox('setValue', 0.000);
    	}
    	updateFooter();
    }).bind('keydown', function(event){
    	if(event.keyCode == 40){
//      		//spjeEditor.target.focus();
     	}
    });
    
  	//计算金额
    function calculate(){
    	var spje = zslEditor.target.val() * zdjEditor.target.val();   
        $(spjeEditor.target).numberbox('setValue',spje);
        //更新汇总列
        updateFooter();
    }
  	
    //初始进入编辑状态时，使用商品编号获得焦点
    spbhEditor.target.focus();
}
//求和
function updateFooter(){
	var rows = ywrk_spdg.datagrid('getRows');
	var spmc_footer = '合计';
	var hjje = 0.0000;
	$.each(rows, function(){
		var index = ywrk_spdg.datagrid('getRowIndex', this);
		if(index < rows.length - 1){
			if(editIndex == index){
				hjje += Number(spjeEditor.target.val());
			}else{
				hjje += Number(this.spje == undefined ? 0 : this.spje);
			}
		}
 	});
	ywrk_spdg.datagrid('reloadFooter', [{spmc : spmc_footer, spje : lnyw.formatNumberRgx(hjje.toFixed(LENGTH_JE))}]);
}

function expandKc(bmbh, spbh){
	$.ajax({
		url:'${pageContext.request.contextPath}/jxc/spAction!getSpKc.action',
// 		async: false,
		cache: false,
		data:{
			bmbh : ywrk_did,
			spbh : spbh
		},
		dataType:'json',
		success:function(data){
// 			if(data.success){
				$('#spkc').propertygrid({
				    data: data.rows,
				    showGroup: true,
				    scrollbarSize: 0,
				    showHeader:false
				});
// 			}else{
// 				$.messager.alert('提示', '供应商信息不存在！', 'error');
// 			}
		}
	});
	
}

//验证记录是否已存在
function existKey(value, rowIndex){
	var keys = ywrk_spdg.datagrid('getRows');
	var exist = false;
	if(keys.length > 2){
		$.each(keys, function(){
			var index = ywrk_spdg.datagrid('getRowIndex', this);
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
	zhxsEditor.target.val(rowData.zhxs);
	zjldwIdEditor.target.val(rowData.zjldwId);
	cjldwIdEditor.target.val(rowData.cjldwId);
	
	jxc.spInfo($('#jxc_ywrk_layout'), '1', rowData.sppp, rowData.spbz);
	jxc.showKc('#jxc_ywrk_layout', 
			'${pageContext.request.contextPath}/jxc/ywrkAction!getSpkc.action', 
			{
				bmbh : ywrk_did, 
				ckId : jxc_ywrk_ckCombo.combobox('getValue'),
				spbh : $(spbhEditor.target).val(),
			});
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

//////////////////////////////////////////////以下为业务入库划列表处理代码

function cjYwrk(){
	var row = ywrk_dg.datagrid('getSelected');
	if (row != undefined) {
		if(row.isCj != '1'){
			if(row.kfrklsh == undefined){
				$.messager.prompt('请确认', '是否要冲减选中的业务入库单？请填写备注', function(bz){
					if (bz != undefined){
						$.ajax({
							url : '${pageContext.request.contextPath}/jxc/ywrkAction!cjYwrk.action',
							data : {
								ywrklsh : row.ywrklsh,
								bmbh: ywrk_did,
								lxbh: ywrk_lx,
								menuId : ywrk_menuId,
								bz : bz
							},
							method: 'post',
							dataType : 'json',
							success : function(d) {
								ywrk_dg.datagrid('load');
								ywrk_dg.datagrid('unselectAll');
								$.messager.show({
									title : '提示',
									msg : d.msg
								});
								$.messager.confirm('请确认', '是否打印业务入库单？', function(r) {
									if (r) {
										var url = lnyw.bp() + '/jxc/ywrkAction!printYwrk.action?ywrklsh=' + d.obj.ywrklsh + '&bmbh=' + ywrk_did;
										jxc.print(url, PREVIEW_REPORT, HIDE_PRINT_WINDOW);
									}
								});
							}
						});
					}
				});
			}else{
				$.messager.alert('警告', '选中的业务入库记录已由库房入库，请重新选择！',  'warning');
			}
		}else{
			$.messager.alert('警告', '选中的业务入库记录已被冲减，请重新选择！',  'warning');
		}
	}else{
		$.messager.alert('警告', '请选择一条记录进行操作！',  'warning');
	}
}

function printYwrk(){
	var row = ywrk_dg.datagrid('getSelected');
	if (row != undefined) {
		$.messager.confirm('请确认', '是否打印业务入库单？', function(r) {
			if (r) {
				var url = lnyw.bp() + '/jxc/ywrkAction!printYwrk.action?ywrklsh=' + row.ywrklsh + '&bmbh=' + row.bmbh;
				jxc.print(url, PREVIEW_REPORT, HIDE_PRINT_WINDOW);
			}
		});
	}else{
		$.messager.alert('警告', '请选择一条记录进行操作！',  'warning');
	}
}

/*
 * 将暂估入库转换为正式入库
 */
function changeYwrk(){
	var selected = ywrk_dg.datagrid('getSelections');
	var flag = true;
	var lsh = [];
	if (selected.length) {
		var preRow = undefined;
		$.each(selected, function(index){
			if(this.isCj == '1'){
	    		$.messager.alert('提示', '选中的入库单已经冲减，请重新选择！', 'error');
				flag = false;
				return false;
	    	}
			if(this.rklxId == '01'){
	    		$.messager.alert('提示', '选中的入库单已经是正式入库，请重新选择！', 'error');
				flag = false;
				return false;
	    	}
		    if(index != 0){
		    	if(this.gysbh != preRow.gysbh){
		    		$.messager.alert('提示', '请选择相同供应商的入库单进行操作！', 'error');
					flag = false;
					return false;
		    	}
		    	if(this.ckId != preRow.ckId){
		    		$.messager.alert('提示', '请选择相同仓库的入库单进行操作！', 'error');
					flag = false;
					return false;
		    	}
		    }else{
		    	preRow = this;
		    }
		    lsh.push(this.ywrklsh);
		});
		
		if(flag){
			$.messager.confirm('请确认', '是否要将选中的业务入库单进行类型转换？', function(r){
				if (r){
					$.ajax({
						url : '${pageContext.request.contextPath}/jxc/ywrkAction!changeYwrk.action',
						data : {
							ywrklshs : lsh.join(','),
						},
						method: 'post',
						dataType : 'json',
						success : function(d) {
							$('input[name=gysbh]').val(selected[0].gysbh);
							$('input[name=gysmc]').val(selected[0].gysmc);
							console.info(selected[0].isZs);
							if (selected[0].isZs == '1') {
								$('input[name=isZs]').prop('checked', true);
							}
							if (selected[0].isDep == '1') {
								$('input[name=isDep]').prop('checked', true);
							}
							jxc_ywrk_ckCombo.combobox('setValue', selected[0].ckId);
							jxc_ywrk_rklxCombo.combobox('setValue', '01');
							
							
							ywrk_spdg.datagrid('loadData', d.rows);
	 						updateFooter();
							$('input[name=ywrklshs]').val(lsh.join(','));
							ywrk_tabs.tabs('select', 0);
						}
					});
				}
			});
		}
	}else{
		$.messager.alert('警告', '请选择一条记录进行操作！',  'warning');
	}
}


function searchYwrk(){
	ywrk_dg.datagrid('load',{
		bmbh: ywrk_did,
		createTime: $('input[name=createTimeYwrk]').val(),
		search: $('input[name=searchYwrk]').val(),
	});
}

//////////////////////////////////////////////以上为业务入库列表处理代码


//////////////////////////////////////////////以下为库房入库列表处理代码

function generateYwrk(){
	var rows = ywrk_kfrkDg.datagrid('getSelections');
	var kfrklshs = [];
	if(rows.length > 0){
		$.messager.confirm('请确认', '是否要将选中记录进行业务入库？', function(r) {
			if (r) {
				for ( var i = 0; i < rows.length; i++) {
					kfrklshs.push(rows[i].kfrklsh);
				}
				var kfrklshsStr = kfrklshs.join(',');
				$.ajax({
					url : '${pageContext.request.contextPath}/jxc/kfrkAction!toYwrk.action',
					data : {
						kfrklshs : kfrklshsStr
					},
					dataType : 'json',
					success : function(d) {
						$.each(d.rows, function(index){
							if(index != d.rows.length - 1){
								d.rows[index].spje = d.rows[index].zdwsl * d.rows[index].zdwdj;
							}
						});
						ywrk_spdg.datagrid('loadData', d.rows);
						updateFooter();
						$('input[name=gysbh]').val(rows[0].gysbh);
						$('input[name=gysmc]').val(rows[0].gysmc);
						$('input[name=kfrklshs]').val(kfrklshsStr);
						ywrk_tabs.tabs('select', 0);
					}
				});
			}
		});
	}else{
		$.messager.alert('警告', '请选择最少一条记录进行操作！',  'warning');
	}
}

function searchKfrkInYwrk(){
	ywrk_kfrkDg.datagrid('load',{
		bmbh: ywrk_did,
		createTime: $('input[name=createTimeKfrkInYwrk]').val(),
		search: $('input[name=searchKfrkInYwrk]').val(),
		fromOther: 'fromYwrk'
	});
}

//////////////////////////////////////////////以上为库房入库列表处理代码

//////////////////////////////////////////////以下为采购计划(直送)列表处理代码

function createYwrkFromCgjh(){
	var rows = ywrk_cgjhDg.datagrid('getSelections');
	//var cgjhlshs = [];
	var cgjhDetIds = [];
	if(rows.length > 0){
		$.messager.confirm('请确认', '是否要将选中记录进行业务入库？', function(r) {
			if (r) {
				for ( var i = 0; i < rows.length; i++) {
					cgjhDetIds.push(rows[i].id);
				}
				var cgjhDetStr = cgjhDetIds.join(',');
// 				for ( var i = 0; i < rows.length; i++) {
// 					cgjhlshs.push(rows[i].cgjhlsh);
// 				}
				//var cgjhlshsStr = cgjhlshs.join(',');
				$.ajax({
					url : '${pageContext.request.contextPath}/jxc/cgjhAction!toYwrk.action',
					data : {
						//cgjhlshs : cgjhlshsStr
						cgjhDetIds : cgjhDetStr 
					},
					dataType : 'json',
					success : function(d) {
						$('input[name=gysbh]').val(rows[0].gysbh);
						$('input[name=gysmc]').val(rows[0].gysmc);
						$('input[name=isZs]').prop('checked', true);
						jxc_ywrk_ckCombo.combobox('setValue', rows[0].ckId);
						ywrk_spdg.datagrid('loadData', d.rows);
						updateFooter();
						$('input[name=cgjhDetIds]').val(cgjhDetStr);
						ywrk_tabs.tabs('select', 0);
					}
				});
			}
		});
	}else{
		$.messager.alert('警告', '请选择最少一条记录进行操作！',  'warning');
	}
}

function searchCgjhInYwrk(){
	ywrk_cgjhDg.datagrid('load',{
		bmbh: ywrk_did,
		createTime: $('input[name=createTimeCgjhInYwrk]').val(),
		search: $('input[name=searchCgjhInYwrk]').val(),
		fromOther: 'fromYwrk',
		isZs: '1'
	});
}

//////////////////////////////////////////////以上为库房入库列表处理代码

//////////////////////////////////////////////以上为内部销售列表处理代码
function toYwrk(){
	var row = ywrk_xskpDg.datagrid('getSelected');
	if(row != undefined){
		$.messager.confirm('请确认', '是否要将选中记录进行业务入库？', function(r) {
			if (r) {
				$.ajax({
					url : '${pageContext.request.contextPath}/jxc/xskpAction!toYwrk.action',
					data : {
						xskplsh : row.xskplsh
					},
					dataType : 'json',
					success : function(d) {
						$('input[name=isDep]').prop('checked', 'checked');
						$('input[name=isDep]').attr('checked', 'checked');
						depChange(row.bmbh);
						$('input[name=gysbh]').val(jxc.otherBm(ywrk_did)['gysbh']);
						$('input[name=gysmc]').val(jxc.otherBm(ywrk_did)['gysmc']);
						var bz = row.bz.trim().length == 0 ? '' : '/' + row.bz.trim();
						$('input[name=jxc_ywrk_bz]').val(row.xskplsh + bz);
						$('input[name=xskplsh]').val(row.xskplsh);
						ywrk_tabs.tabs('select', 0);
						
						ywrk_spdg.datagrid('loadData', d.rows);
						updateFooter();
					}
				});
			}
		});
	}else{
		$.messager.alert('警告', '请选择最少一条记录进行操作！',  'warning');
	}
}

function searchXskpInYwrk(){
	ywrk_xskpDg.datagrid('load',{
		bmbh: ywrk_did,
		createTime: $('input[name=createTimeXskpInYwrk]').val(),
		search: $('input[name=searchXskpInYwrk]').val(),
		fromOther: 'fromYwrk',
		otherBm: jxc.otherBm(ywrk_did)['bmbh']
// 		isZs: '1'
	});
}
//////////////////////////////////////////////以上为内部销售列表处理代码

</script>

<!-- tabPosition:'left', headerWidth:'35' -->
<div id="jxc_ywrk_tabs" class="easyui-tabs" data-options="fit:true, border:false," style="width:100%;height:100%;">
	
    <div title="新增记录" data-options="closable:false">
        <div id='jxc_ywrk_layout' style="height:100%;width=100%">
			<div data-options="region:'north',title:'单据信息',border:false,collapsible:false" style="width:100%;height:150px">		
				<table class="tinfo">
					<tr>
						<td colspan="2">直送<input type="checkbox" name="isZs">&nbsp;&nbsp;&nbsp;&nbsp;内部<input type="checkbox" name="isDep"></td>
						<th>入库类型</th><td><input id="jxc_ywrk_rklxId" name="rklxId" type="text" size="8"></td>
						<th class="read">时间</th><td><div id="createDate" class="read"></div></td>
						<th class="read">单据号</th><td><div id="ywrkLsh" class="read"></div></td>
					</tr>
					<tr>
						<th>供应商编码</th><td><input name="gysbh" class="easyui-validatebox"
							data-options="validType:['mustLength[8]','integer']" onkeyup="gysLoad()" size="8"></td>
						<th class="read">供应商名称</th><td colspan="3"><input name="gysmc" readonly="readonly" size="50"></td>
					</tr>
					<tr>
						<th>仓库</th><td><input id="jxc_ywrk_ckId" name="ckId" type="text" size="8"></td>
						<th class="isZs" style="display:none">送货地址</th><td class="isZs" style="display:none"><input name="shdz" type="text" size="8"></td>
						<th class="isDep" style="display:none">部门</th><td class="isDep" style="display:none"><input id="jxc_ywrk_depId" name="depId" type="text" size="8"></td>
					</tr>
					<tr>
						<th>备注</th><td colspan="7"><input name="jxc_ywrk_bz" style="width:90%"></td>
					</tr>
				</table>
				<input name="xskplsh" type="hidden">
				<input name="ywrklshs" type="hidden">
				<input name="kfrklshs" type="hidden">
				<input name="cgjhDetIds" type="hidden">
			</div>
			<div data-options="region:'center',title:'商品信息',split:true" style="width:150px">		
				<table id='jxc_ywrk_spdg'></table>
			</div>
			<div data-options="region:'east',title:'商品库存信息',split:true,collapsed:true," style="width:150px">
				<table id="show_spkc"></table>
			</div>
		</div>
    </div>
    <div title="业务入库列表" data-options="closable:false" >
    	<table id='jxc_ywrk_dg'></table>
    </div>
	<div title="库房入库列表" data-options="closable:false" >
		<table id='jxc_ywrk_kfrkDg'></table>
	</div>
	<div title="采购计划(直送)列表" data-options="closable:false" >
		<table id='jxc_ywrk_cgjhDg'></table>
	</div>
	<div title="内部销售列表" data-options="closable:false" >
		<div id='jxc_ywrk_xskpDg'></div>
	</div>
</div>

<div id="jxc_ywrk_tb" style="padding:3px;height:auto">
	请输入查询起始日期:<input type="text" name="createTimeYwrk" class="easyui-datebox" data-options="value: moment().date(1).format('YYYY-MM-DD')" style="width:100px">
	输入流水号、供应商、备注：<input type="text" name="searchYwrk" style="width:100px">
	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchYwrk();">查询</a>
</div>
<div id="jxc_ywrk_kfrkTb" style="padding:3px;height:auto">
	请输入查询起始日期:<input type="text" name="createTimeKfrkInYwrk" class="easyui-datebox" data-options="value: moment().date(1).format('YYYY-MM-DD')" style="width:100px">
	输入流水号、供应商、备注：<input type="text" name="searchKfrkInYwrk" style="width:100px">
	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchKfrkInYwrk();">查询</a>
</div>
<div id="jxc_ywrk_cgjhTb" style="padding:3px;height:auto">
	请输入查询起始日期:<input type="text" name="createTimeCgjhInYwrk" class="easyui-datebox" data-options="value: moment().date(1).format('YYYY-MM-DD')" style="width:100px">
	输入流水号、供应商、备注：<input type="text" name="searchCgjhInYwrk" style="width:100px">
	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchCgjhInYwrk();">查询</a>
</div>
<div id="jxc_ywrk_xskpTb" style="padding:3px;height:auto">
	请输入查询起始日期:<input type="text" name="createTimeXskpInYwrk" class="easyui-datebox" data-options="value: moment().date(1).format('YYYY-MM-DD')" style="width:100px">
	输入流水号、供应商、备注：<input type="text" name="searchXskpInYwrk" style="width:100px">
	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchXskpInYwrk();">查询</a>
</div>



