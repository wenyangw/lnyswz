<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<script type="text/javascript">
var cgjh_did;
var cgjh_lx;
var cgjh_menuId;
var cgjh_spdg;
var cgjh_dg;
var cgjh_cgxqDg;
var cgjh_xsthDg;
var cgjh_cgjhDg;
var cgjh_spkcDg;
var editIndex = undefined;
var cgjh_tabs;

var jxc_cgjh_jsfsCombo;
var jxc_cgjh_ckCombo;

var cgjh_detDg;
var cgjhRow;

//编辑行字段
var spbhEditor;   
var spmcEditor;
var spcdEditor;
var spppEditor;
var spbzEditor;
var spdjEditor;
var shdzEditor;
var lxrEditor;
var dhsjEditor;
var zjldwEditor;
var zslEditor;
var zdjEditor;
var cjldwEditor;
var cslEditor;
var cdjEditor;   
var spjeEditor;
var zhxsEditor;
var zjldwIdEditor;
var cjldwIdEditor;

$(function(){
	cgjh_did = lnyw.tab_options().did;
	cgjh_lx = lnyw.tab_options().lx;
	cgjh_menuId = lnyw.tab_options().id;
	
	$('#jxc_cgjh_layout').layout({
		fit : true,
		border : false,
	});

	cgjh_dg = $('#jxc_cgjh_dg').datagrid({
		//url: '${pageContext.request.contextPath}/jxc/cgjhAction!datagrid.action',
		fit : true,
	    border : false,
	    singleSelect : true,
	    remoteSort: false,
	    //fitColumns: true,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
		columns:[[
			{field:'needAudit',title:'等级',align:'center',
				styler: function(value, rowData){
					if(rowData.needAudit == rowData.isAudit){
						return 'color:blue;';
					}
					if(rowData.isAudit == '9'){
						return 'color:red;';
					}
				}},
			{field:'isAudit',title:'进度',align:'center',
				styler: function(value, rowData){
					if(rowData.needAudit == rowData.isAudit){
						return 'color:blue;';
					}
					if(rowData.isAudit == '9'){
						return 'color:red;';
					}
				}},
			{field:'cgjhlsh',title:'流水号',align:'center',
				styler: function(value, rowData){
					if(rowData.isCompleted == '1'){
						return 'color:red;';
					}
       			}},
	        {field:'createTime',title:'时间',align:'center'},
	        {field:'gysbh',title:'供应商编号',align:'center'},
	        {field:'gysmc',title:'*供应商名称',align:'center', sortable:true,
	        	sorter: function(a, b){
	        		return a.localeCompare(b);
	        	}},
	        {field:'ywyId',title:'仓库id',align:'center',hidden:true},
	        {field:'ckmc',title:'仓库',align:'center'},
// 	        {field:'shdz',title:'送货地址',align:'center',
//         		formatter: function(value){
//         			return lnyw.memo(value, 15);
//         		}},
	        {field:'jsfsmc',title:'结算方式',align:'center'},
// 	        {field:'dhsj',title:'到货时间',align:'center'},
	        {field:'hjje',title:'金额',align:'center',
	        	formatter: function(value){
	        		return lnyw.formatNumberRgx(value);
	        	}},
        	{field:'hjsl',title:'数量',align:'center',
	        	formatter: function(value){
	        		return lnyw.formatNumberRgx(value);
	        	}},	
        	{field:'bz',title:'备注',align:'center',
        		formatter: function(value){
        			return lnyw.memo(value, 15);
        		}},
        	{field:'cgxqlshs',title:'采购需求流水号',align:'center',
        		formatter: function(value){
        			return lnyw.memo(value, 15);
        		}},
        	{field:'isKfrk',title:'*到货',align:'center',sortable:true,
       			formatter : function(value) {
   					if (value == '1') {
   						return '是';
   					}
   					if(value == '2'){
   						return '正';
   					}
   					if(value == '3'){
   						return '暂';
   					}
   					return '';
   				},
           		sorter: function(a,b){
           			a = a == undefined ? 0 : a;
           			b = b == undefined ? 0 : b;
   					return (a-b);  
   				},
   				styler: function(value){
                  	return 'color:blue;';
       			}},	
//         	{field:'kfrklshs',title:'库房入库流水号',align:'center',
//             	formatter: function(value){
//         			return lnyw.memo(value, 15);
//         		}},
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
			{field:'xsthlsh',title:'销售提货流水号',align:'center',},
        	{field:'isHt',title:'*合同',align:'center',sortable:true,
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
			{field:'returnHt',title:'*合同收回',align:'center',sortable:true,
        		formatter : function(value, rowData) {
        			if(rowData.isHt == '1'){
						if (value == '0') {
							return '否';
						}else if(value == "1"){
							return '是';
						}
					}else{
						return '-';
					}
				},
        		sorter: function(a,b){
        			a = a == undefined ? 0 : a;
        			b = b == undefined ? 0 : b;
					return (a-b);  
				}},	
        	{field:'isAudit',title:'*已审核',align:'center',sortable:true,
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
        	{field:'isCancel',title:'*状态',align:'center',sortable:true,
        		formatter : function(value) {
					if (value == '1') {
						return '取消';
					} else {
						return '正常';
					}
				},
        		sorter: function(a,b){
        			a = a == undefined ? 0 : a;
        			b = b == undefined ? 0 : b;
					return (a-b);  
				}},
        	{field:'isCompleted',title:'*完成',align:'center',sortable:true,
        		formatter : function(value, rowData) {
        			if(rowData.isCancel == '0'){
						if (value == '1') {
							return '是';
						} else {
							return '否';
						}
        			}else{
        				return '-';
        			}
				},
				sorter: function(a,b){
	        			a = a == undefined ? 0 : a;
	        			b = b == undefined ? 0 : b;
						return (a-b);  
				},
				styler: function(value){
           			if(value == '1'){
               			return 'color:blue;';
               		}
    			}},
            {field:'nbjhlsh',title:'内部计划流水号'}
	    ]],
	    toolbar:'#jxc_cgjh_tb',
	});
	lnyw.toolbar(1, cgjh_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', cgjh_did);
	
	
	cgjh_dg.datagrid({
        view: detailview,
        detailFormatter:function(index,row){
            return '<div style="padding:2px"><table id="cgjh-ddv-' + index + '"></table></div>';
        },
        onExpandRow: function(index,row){
        	cgjhRow = cgjh_dg.datagrid('selectRow', index).datagrid('getSelected');
        	cgjh_detDg = $('#cgjh-ddv-'+index).datagrid({
                url:'${pageContext.request.contextPath}/jxc/cgjhAction!detDatagrid.action',
                fitColumns:true,
                singleSelect:true,
                rownumbers:true,
                loadMsg:'',
                height:'auto',
                queryParams: {
        			cgjhlsh: row.cgjhlsh,
        		},
                columns:[[
					{field:'isBack',title:'取消',align:'center',
						formatter : function(value) {
							if (value == '1') {
								return '是';
							} else {
								return '';
							}
						}},
                    {field:'spbh',title:'商品编号',width:80,align:'center'},
                    {field:'spmc',title:'名称',width:250,align:'center'},
                    {field:'spcd',title:'产地',width:80,align:'center'},
                    {field:'sppp',title:'品牌',width:80,align:'center'},
                    {field:'spbz',title:'包装',width:100,align:'center'},
                    {field:'spdj',title:'等级',width:50,align:'center'},
                    {field:'zjldwmc',title:'单位1',width:60,align:'center'},
                    {field:'zdwsl',title:'数量1',width:100,align:'center',
                       	formatter: function(value){
                       		return value == 0 ? '' : value;
                   		}},
                   	{field:'zdwyrsl',title:'到货数量',width:100,align:'center',
                        formatter: function(value){
                        	return value == 0 ? '' : value;
                    	},
                    	styler: function(value,row){
                   			return 'color:red;';
            			}},
           			{field:'zdwrksl',title:'直送入库数量',width:100,align:'center',
                           formatter: function(value){
                           	return value == 0 ? '' : value;
                       	},
                       	styler: function(value,row){
                      			return 'color:blue;';
               			}},
                    {field:'zdwdj',title:'单价1',width:100,align:'center',
                        formatter: function(value){
                        	return value == 0 ? '' : value;
                    	}},
                    {field:'cjldwmc',title:'单位2',width:100,align:'center'},
                    {field:'cdwsl',title:'数量2',width:100,align:'center',
                       	formatter: function(value){
                       		return value == 0 ? '' : value;
                   		}},
                  	{field:'cdwyrsl',title:'到货数量2',width:100,align:'center',
                        formatter: function(value){
                          	return value == 0 ? '' : value;
                       	},
                       	styler: function(value,row){
                      			return 'color:red;';
               			}},
                    {field:'cdwdj',title:'单价2',width:100,align:'center',
                       	formatter: function(value){
                       		return value == 0 ? '' : value;
                   		}},
                    {field:'spje',title:'金额',width:100,align:'center',
                       	formatter: function(value){
                       		return value == 0 ? '' : value;
                   		}},
                	{field:'shdz',title:'送货地址',width:100,align:'center',
                 		formatter: function(value){
                 			return lnyw.memo(value, 15);
                 		}},
               		{field:'lxr',title:'联系人',width:100,align:'center',
                   		formatter: function(value){
                   			return lnyw.memo(value, 15);
                   		}},
                	{field:'dhsj',title:'到货时间',width:100,align:'center',},
                    {field:'kfrklshs',title:'库房入库流水号',width:100,align:'center',
                		formatter: function(value){
                			return lnyw.memo(value, 15);
                		}},
                	{field:'ywrklshs',title:'业务入库流水号',width:100,align:'center',},
               		{field:'isLock',title:'锁定',align:'center',
                   		formatter : function(value) {
           					if (value == '1') {
           						return '是';
           					} else {
           						return '';
           					}
           				}},

                ]],
                onResize:function(){
                	cgjh_dg.datagrid('fixDetailRowHeight',index);
                },
                onLoadSuccess:function(){
                    setTimeout(function(){
                    	cgjh_dg.datagrid('fixDetailRowHeight',index);
                    },0);
                }
            });
            cgjh_dg.datagrid('fixDetailRowHeight',index);
        }
    });
	
	
	cgjh_cgxqDg = $('#jxc_cgjh_cgxqDg').datagrid({
		fit : true,
	    border : false,
	    remoteSort: false,
// 	    fitColumns: true,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
		columns:[[
			{field:'id',title:'记录号',align:'center',checkbox:true},
			{field:'cgxqlsh',title:'流水号',align:'center'},
			{field:'createTime',title:'时间',align:'center'},
			{field:'spbh',title:'*商品编号',align:'center', sortable:true,
	        	sorter: function(a, b){
	        		return a.localeCompare(b);
	        	}},
			{field:'spmc',title:'名称',align:'center'},
			{field:'spcd',title:'*产地',align:'center',sortable:true,
	        	sorter: function(a, b){
	        		if(typeof(a) == "string"){ 
	        			return a.localeCompare(b); 
	        		}
	        	}},
			{field:'sppp',title:'品牌',align:'center'},
			{field:'spbz',title:'包装',align:'center'},
			{field:'zjldwmc',title:'单位1',align:'center',
				formatter: function(value){
					return value == 0 ? '' : value;
				}},
			{field:'zdwsl',title:'数量1',align:'center',
			   	formatter: function(value){
			   		return value == 0 ? '' : value;
					}},
			{field:'zdwdj',title:'单价1',align:'center',
			   	formatter: function(value){
			   		return value == 0 ? '' : value;
					}},
			{field:'zdwxsdj',title:'销价1',align:'center',
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
			{field:'cdwxsdj',title:'销价2',align:'center',
			   	formatter: function(value){
			   		return value == 0 ? '' : value;
					}},
			{field:'spje',title:'金额',align:'center',
			    formatter: function(value){
			    	return value == 0 ? '' : lnyw.formatNumberRgx(value);
					}},      
	        {field:'gysbh',title:'供应商编号',align:'center', hidden:true},
	        {field:'gysmc',title:'供应商名称',align:'center'},
	        {field:'khbh',title:'客户编号',align:'center', hidden:true},
	        {field:'khmc',title:'客户名称',align:'center'},
	        {field:'createId',title:'创建人id',align:'center',hidden:true},
	        {field:'createName',title:'业务员',align:'center'},
// 	        {field:'dhfs',title:'到货方式',align:'center'},
	        {field:'lxr',title:'联系人',align:'center'},
	        {field:'shdz',title:'送货地址',align:'center'},
	        {field:'jsfsmc',title:'结算方式',align:'center'},
	        {field:'dhsj',title:'到货时间',align:'center'},
// 	        {field:'xqsj',title:'需求时间',align:'center'},
	        {field:'hjje',title:'金额',align:'center'},
        	{field:'bz',title:'备注',align:'center',
        		formatter: function(value){
        			return lnyw.memo(value, 15);
        		}},
        	{field:'cgjhlsh',title:'采购计划流水号',align:'center',
           		formatter: function(value){
           			return lnyw.memo(value, 15);
           		}},
        	{field:'isLs',title:'*临时',align:'center',sortable:true,
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
//         	{field:'isCancel',title:'状态',align:'center',sortable:true,
//         		formatter : function(value) {
// 					if (value == '1') {
// 						return '取消';
// 					} else {
// 						return '正常';
// 					}
// 				},
//         		sorter: function(a,b){
//         			a = a == undefined ? 0 : a;
//         			b = b == undefined ? 0 : b;
// 					return (a-b);  
// 				}},
//         	{field:'isCompleted',title:'完成',align:'center',sortable:true,
//         		formatter : function(value) {
// 					if (value == '1') {
// 						return '是';
// 					} else {
// 						return '否';
// 					}
// 				},
// 				sorter: function(a,b){
// 	        			a = a == undefined ? 0 : a;
// 	        			b = b == undefined ? 0 : b;
// 						return (a-b);  
// 				}},
	    ]],
	    toolbar:'#jxc_cgjh_cgxqTb',
	});
	lnyw.toolbar(2, cgjh_cgxqDg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', cgjh_did);

	cgjh_spkcDg = $('#jxc_cgjh_spkcDg').datagrid({
		fit : true,
	    border : false,
	    remoteSort: false,
// 	    fitColumns: true,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
		columns:[[
			{field:'spbh',title:'商品编号',align:'center'},
			{field:'spmc',title:'名称',align:'center'},
			{field:'spcd',title:'产地',align:'center'},
			{field:'sppp',title:'品牌',align:'center'},
			{field:'spbz',title:'包装',align:'center'},
			{field:'zjldwId',title:'主单位id',align:'center',hidden:true},
			{field:'zjldwmc',title:'单位',align:'center',},
			{field:'minKc',title:'最低库存',align:'center',
			   	formatter: function(value){
			   		return value == 0 ? '' : value.toFixed(LENGTH_SL);
					}},
			{field:'kcsl',title:'库存数量',align:'center',
			   	formatter: function(value){
			   		return value == 0 ? '' : value.toFixed(LENGTH_SL);
					}},
			
	    ]],
	    toolbar:'#jxc_cgjh_spkcTb',
	});
	lnyw.toolbar(3, cgjh_spkcDg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', cgjh_did);
	
	cgjh_xsthDg = $('#jxc_cgjh_xsthDg').datagrid({
		fit : true,
	    border : false,
	    remoteSort: false,
// 	    fitColumns: true,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
		columns:[[
			{field:'id',title:'记录号',align:'center',checkbox:true},
			{field:'needAudit',title:'等级',align:'center'},
			{field:'xsthlsh',title:'流水号',align:'center'},
			{field:'createTime',title:'时间',align:'center'},
			{field:'spbh',title:'商品编号',align:'center'},
			{field:'spmc',title:'名称',align:'center'},
			{field:'spcd',title:'产地',align:'center'},
			{field:'sppp',title:'品牌',align:'center'},
			{field:'spbz',title:'包装',align:'center'},
			{field:'zjldwmc',title:'单位1',align:'center',
				formatter: function(value){
					return value == 0 ? '' : value;
				}},
			{field:'zdwsl',title:'数量1',align:'center',
			   	formatter: function(value){
			   		return value == 0 ? '' : value;
					}},
			{field:'zdwdj',title:'单价1',align:'center',
			   	formatter: function(value){
			   		return value == 0 ? '' : value;
					}},
			{field:'zdwxsdj',title:'销价1',align:'center',
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
			{field:'cdwxsdj',title:'销价2',align:'center',
			   	formatter: function(value){
			   		return value == 0 ? '' : value;
					}},
			{field:'spje',title:'销售金额',align:'center',
			    formatter: function(value){
			    	return value === 0 ? '' : lnyw.formatNumberRgx(value);
					}},      
	        {field:'khbh',title:'客户编号',align:'center', hidden:true},
	        {field:'khmc',title:'客户名称',align:'center'},
	        {field:'ywyId',title:'业务员id',align:'center',hidden:true},
	        {field:'ywymc',title:'业务员',align:'center'},
// 	        {field:'dhfs',title:'到货方式',align:'center'},
	        {field:'thr',title:'联系人',align:'center'},
	        {field:'shdz',title:'送货地址',align:'center'},
	        {field:'ckId',title:'仓库id',align:'center',hidden:true},
	        {field:'ckmc',title:'仓库',align:'center'},
	        {field:'jsfsmc',title:'结算方式',align:'center'},
	        //{field:'dhsj',title:'到货时间',align:'center'},
// 	        {field:'xqsj',title:'需求时间',align:'center'},
	        //{field:'hjje',title:'金额',align:'center'},
        	{field:'bz',title:'备注',align:'center',
        		formatter: function(value){
        			return lnyw.memo(value, 15);
        		}}
//         	{field:'isLs',title:'*临时',align:'center',sortable:true,
//         		formatter : function(value) {
// 					if (value == '1') {
// 						return '是';
// 					} else {
// 						return '否';
// 					}
// 				},
//         		sorter: function(a,b){
//         			a = a == undefined ? 0 : a;
//         			b = b == undefined ? 0 : b;
// 					return (a-b);  
// 				}},
//         	{field:'isCancel',title:'状态',align:'center',sortable:true,
//         		formatter : function(value) {
// 					if (value == '1') {
// 						return '取消';
// 					} else {
// 						return '正常';
// 					}
// 				},
//         		sorter: function(a,b){
//         			a = a == undefined ? 0 : a;
//         			b = b == undefined ? 0 : b;
// 					return (a-b);  
// 				}},
//         	{field:'isCompleted',title:'完成',align:'center',sortable:true,
//         		formatter : function(value) {
// 					if (value == '1') {
// 						return '是';
// 					} else {
// 						return '否';
// 					}
// 				},
// 				sorter: function(a,b){
// 	        			a = a == undefined ? 0 : a;
// 	        			b = b == undefined ? 0 : b;
// 						return (a-b);  
// 				}},
	    ]],
	    toolbar:'#jxc_cgjh_xsthTb'
	});
	lnyw.toolbar(4, cgjh_xsthDg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', cgjh_did);
	
	
	cgjh_cgjhDg = $('#jxc_cgjh_cgjhDg').datagrid({
		fit : true,
	    border : false,
	    remoteSort: false,
// 	    fitColumns: true,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
		columns:[[
			{field:'id',title:'detId',align:'center', checkbox:true},
			{field:'bmmc',title:'部门',align:'center'},
			{field:'cgjhlsh',title:'流水号',align:'center'},
			{field:'createTime',title:'时间',align:'center'},
			{field:'createName',title:'计划员',align:'center'},
			{field:'spbh',title:'商品编号',align:'center'},
			{field:'spmc',title:'名称',align:'center'},
			{field:'spcd',title:'*产地',align:'center', sortable:true,
	        	sorter: function(a, b){
	        		return a.localeCompare(b);
	        	}},
			{field:'sppp',title:'品牌',align:'center'},
			{field:'spbz',title:'包装',align:'center'},
			{field:'zjldwmc',title:'单位1',align:'center'},
			{field:'zdwsl',title:'数量1',align:'center',
			   	formatter: function(value){
			   		return value == 0 ? '' : value;
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
			{field:'gysbh',title:'供应商编号',align:'center',hidden:true},
			{field:'gysmc',title:'供应商名称',align:'center',hidden:true},
			{field:'ckId',title:'仓库id',align:'center',hidden:true},
			{field:'ckmc',title:'仓库',align:'center',hidden:true},
			{field:'jsfsmc',title:'付款方式',align:'center'},
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
				},
			},
			{field:'shdz',title:'送货地址',align:'center'},
			{field:'lxr',title:'联系人',align:'center'},
			{field:'dhsj',title:'到货时间',align:'center'},
			{field:'bz',title:'备注',align:'center',
				formatter: function(value){
					return lnyw.memo(value, 15);
				}},
	    ]],
	    toolbar:'#jxc_cgjh_cgjhTb',
	});
	lnyw.toolbar(5, cgjh_cgjhDg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', cgjh_did);
	

	
// 	cgjh_cgxqDg.datagrid({
//         view: detailview,
//         detailFormatter:function(index,row){
//             return '<div style="padding:2px"><table id="ddv-' + index + '"></table></div>';
//         },
//         onExpandRow: function(index,row){
//             $('#ddv-'+index).datagrid({
//                 url:'${pageContext.request.contextPath}/jxc/cgxqAction!detDatagrid.action',
//                 fitColumns:true,
//                 singleSelect:true,
//                 rownumbers:true,
//                 loadMsg:'',
//                 height:'auto',
//                 queryParams: {
//         			cgxqlsh: row.cgxqlsh,
//         		},
//                 columns:[[
//                     {field:'spbh',title:'商品编号',width:200,align:'center'},
//                     {field:'spmc',title:'名称',width:100,align:'center'},
//                     {field:'spcd',title:'产地',width:100,align:'center'},
//                     {field:'sppp',title:'品牌',width:100,align:'center'},
//                     {field:'spbz',title:'包装',width:100,align:'center'},
//                     {field:'zjldwmc',title:'单位1',width:100,align:'center'},
//                     {field:'zdwsl',title:'数量1',width:100,align:'center'},
//                     {field:'zdwdj',title:'单价1',width:100,align:'center'},
//                     {field:'cjldwmc',title:'单位2',width:100,align:'center'},
//                     {field:'cdwsl',title:'数量2',width:100,align:'center'},
//                     {field:'cdwdj',title:'单价2',width:100,align:'center'},
//                     {field:'spje',title:'金额',width:100,align:'center'},
//                 ]],
//                 onResize:function(){
//                 	cgjh_cgxqDg.datagrid('fixDetailRowHeight',index);
//                 },
//                 onLoadSuccess:function(){
//                     setTimeout(function(){
//                     	cgjh_cgxqDg.datagrid('fixDetailRowHeight',index);
//                     },0);
//                 }
//             });
//             cgjh_cgxqDg.datagrid('fixDetailRowHeight',index);
//         }
//     });
	
	//选中列表标签后，装载数据
	cgjh_tabs = $('#jxc_cgjh_tabs').tabs({
		onSelect: function(title, index){
			if(index == 1){
				cgjh_dg.datagrid({
					url: '${pageContext.request.contextPath}/jxc/cgjhAction!datagrid.action',
					queryParams: {
						bmbh: cgjh_did,
						isZs: $('input#jxc_cgjhDg_isZs').is(':checked') ? '1' : '0',
						isNotZs: $('input#jxc_cgjhDg_isNotZs').is(':checked') ? '1' : '0',
					},
				});
			}
			if(index == 2){
				
				cgjh_cgxqDg.datagrid({
					url: '${pageContext.request.contextPath}/jxc/cgxqAction!datagrid.action',
					queryParams:{
						bmbh: cgjh_did,
						fromOther: 'fromCgjh',
					}
				});
			}
			if(index == 3){
				cgjh_spkcDg.datagrid({
					url: '${pageContext.request.contextPath}/jxc/ywzzAction!listLowSps.action',
					queryParams:{
						bmbh: cgjh_did,
					}
				});
			}
			if(index == 4){
				cgjh_xsthDg.datagrid({
					url: '${pageContext.request.contextPath}/jxc/xsthAction!datagridDet.action',
					queryParams:{
						bmbh: cgjh_did,
						fromOther: 'fromCgjh'
					}
				});
			}
			if(index == 5){
				cgjh_cgjhDg.datagrid({
					url: '${pageContext.request.contextPath}/jxc/cgjhAction!detDg.action',
					queryParams:{
						bmbh: cgjh_did,
						gysbh:  jxc.getNbjhGys(cgjh_did),
						fromOther: 'fromCgjh',
						
					}
				});
			}
			
		}
	});
	
	cgjh_spdg = $('#jxc_cgjh_spdg').datagrid({
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
	        {field:'spdj',title:'商品等级',width:25,align:'center',editor:'text'},
	        {field:'shdz',title:'送货地址',width:25,align:'center',editor:'text'},
	        {field:'lxr',title:'联系人',width:25,align:'center',editor:'text'},
	        {field:'dhsj',title:'到货时间',width:25,align:'center',editor:'datebox'},
	        {field:'zjldwmc',title:'单位1',width:25,align:'center',editor:'textRead'},
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
	    ]],
        onClickRow: clickRow,
        onAfterEdit: function (rowIndex, rowData, changes) {
            //endEdit该方法触发此事件
            editIndex = undefined;
        },
         
	});
	
	//$('#jxc_cgjh_tabs a.tabs-inner').css('height','100px');
	//$('#jxc_cgjh_tabs span.tabs-title').css('white-space','normal');
	
	
	//初始化创建时间
	$('#createDate').html(moment().format('YYYY年MM月DD日'));
	
// 	$('input[name=isLs]').click(function(){
// 		if($(this).is(':checked')){
// 			$('.ls').css('display','table-row');
// 			//初始化分户列表
// 			if(jxc_cgjh_jsfsCombo == undefined){
// 				jxc_cgjh_jsfsCombo = lnyw.initCombo($("input[name=jsfsId]"), 'id', 'jsfsmc', '${pageContext.request.contextPath}/jxc/jsfsAction!listJsfs.action');
// 			}else{
// 				jxc_cgjh_jsfsCombo.combobox('selectedIndex', 0);
// 			}
// 		}else{
// 			$('.ls').css('display','none');
// 		}
// 	});
	
	//初始化业务员列表
// 	var ywyId = $("input[name=ywyId]");
// 	var ywyCombo = ywyId.combobox({
// 	    url:'${pageContext.request.contextPath}/admin/userAction!listYwys.action?did=' + did,
// 	    valueField:'id',
// 	    textField:'realName',
// 	    panelHeight: 'auto',
// 	});
	
	//初始化付款方式列表
	jxc_cgjh_jsfsCombo = lnyw.initCombo($("#jxc_cgjh_jsfsId"), 'id', 'jsmc', '${pageContext.request.contextPath}/jxc/jsfsAction!listJsfs.action');
	
	//初始化仓库列表
	jxc_cgjh_ckCombo = lnyw.initCombo($("#jxc_cgjh_ckId"), 'id', 'ckmc', '${pageContext.request.contextPath}/jxc/ckAction!listCk.action?depId=' + cgjh_did);
	

	//初始化信息
	init();
});

//以下为商品列表处理代码
function init(){
	//清空列表，添加一个空行
	cgjh_spdg.datagrid({
		data: [{}],
	});
	
	//清空全部字段
	$('input').val('');
	//$('input:checkbox').removeAttr('checked');
	$('input:checkbox').prop('checked', false);
	$('input:checkbox#jxc_cgjhDg_isNotZs').prop('checked', true);
	$('input:checkbox#jxc_cgjhDg_isZs').prop('checked', true);
	
	
	//收回商品库存信息
	jxc.hideKc('#jxc_cgjh_layout');
	jxc.spInfo($('#jxc_cgjh_layout'), '');
	
 	jxc_cgjh_ckCombo.combobox('clear');
 	jxc_cgjh_ckCombo.combobox('selectedIndex', 0);
 	jxc_cgjh_jsfsCombo.combobox('clear');
	
	//初始化流水号
	$.ajax({
		type: "POST",
		url: '${pageContext.request.contextPath}/jxc/lshAction!getLsh.action',
		cache: false,
		data: {
			bmbh: cgjh_did,
			lxbh: cgjh_lx,
		},
		dataType: 'json',
		success: function(d){
			if(d.success){
				$('#cgjhLsh').html(d.obj);
			}  
		},
	});
	
	//根据权限，动态加载功能按钮
	lnyw.toolbar(0, cgjh_spdg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', cgjh_did);
	
	//清空合计内容
	cgjh_spdg.datagrid('reloadFooter',[{}]);
}

//判断行是否编辑完成
function rowOk(){
	if(editIndex == undefined){
		return true;
	}
	if(keyOk()){
		if(zslEditor.target.val() > 0 && zdjEditor.target.val() > 0){
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
	if(rowIndex == cgjh_spdg.datagrid('getRows').length - 1){
		cgjh_spdg.datagrid('appendRow', {});
	}else{
		if(!isClick){
			return false;
		}
	}
	//将选中行标识为编辑行
	editIndex = rowIndex;
	//选择编辑行，并进行入编辑状态
	cgjh_spdg.datagrid('selectRow', editIndex)
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
				cgjh_spdg.datagrid('endEdit', editIndex);
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
    cgjh_spdg.datagrid('cancelEdit', editIndex)
            .datagrid('deleteRow', editIndex);
    editIndex = undefined;
    updateFooter();
    jxc.hideKc('#jxc_cgjh_layout');
}

//保存行
function accept(){
    if (rowOk()){
    	cgjh_spdg.datagrid('acceptChanges');
    	jxc.hideKc('#jxc_cgjh_layout');
    }
}

//取消编辑行
function cancelAll(){
	if(editIndex != undefined){
		cgjh_spdg.datagrid('rejectChanges');
	    editIndex = undefined;
	}
    //updateFooter();
    var rows = cgjh_spdg.datagrid('getRows');
    if(rows.length != 1){
    	for(var i = 0; i < rows.length - 1; i++){
    		cgjh_spdg.datagrid('deleteRow', i);
    	}
    }
    jxc.hideKc('#jxc_cgjh_layout');
}

//提交数据到后台
function saveAll(){
	
	var msg = formValid();
	if(msg == ''){
		//编辑行是否完成
		if(rowOk()){
			cgjh_spdg.datagrid('endEdit', editIndex);
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
	
	var rows = cgjh_spdg.datagrid('getRows');
	if(rows.length == 1){
		$.messager.alert('提示', '未添加商品数据,请继续操作！', 'error');
		return false;
	}
	$.each(rows.slice(0, rows.length - 1), function(){
		if(this.spje == undefined){
			$.messager.alert('提示', '商品数据未完成,请继续操作！', 'error');
			return false;
		}
	});
	var footerRows = cgjh_spdg.datagrid('getFooterRows');
	var effectRow = new Object();
	//将表头内容传入后台
	
	effectRow['isZs'] = $('input[name=isZs]').is(':checked') ? '1' : '0';
	effectRow['isHt'] = $('input[name=isHt]').is(':checked') ? '1' : '0';
	effectRow['isNb'] = $('input[name=isNb]').is(':checked') ? '1' : '0';
	effectRow['gysbh'] = $('input[name=jxc_cgjh_gysbh]').val();
	effectRow['gysmc'] = $('input[name=jxc_cgjh_gysmc]').val();
	effectRow['ckId'] = jxc_cgjh_ckCombo.combobox('getValue');
	effectRow['ckmc'] = jxc_cgjh_ckCombo.combobox('getText');
	effectRow['jsfsId'] = jxc_cgjh_jsfsCombo.combobox('getValue');
	effectRow['jsfsmc'] = jxc_cgjh_jsfsCombo.combobox('getText');
// 	effectRow['shdz'] = $('input[name=shdz]').val();
// 	effectRow['lxr'] = $('input[name=lxr]').val();
// 	effectRow['dhsj'] = $('input[name=dhsj]').val();
	effectRow['hjje'] = lnyw.delcommafy(footerRows[0]['spje']); 
	effectRow['bz'] = $('input[name=jxc_cgjh_bz]').val();
	effectRow['cgxqDetIds'] = $('input[name=cgxqDetIds]').val();
	effectRow['xsthDetIds'] = $('input[name=cgjh_xsthDetIds]').val();
	effectRow['nbjhlsh'] = $('input[name=cgjh_nbjhlsh]').val();
	effectRow['bmbh'] = cgjh_did;
	effectRow['lxbh'] = cgjh_lx;
	effectRow['menuId'] = cgjh_menuId;
	//获取采购计划审批权限，无设置即为不需要审批
	var needA = jxc.getAuditLevelCgjh(cgjh_did, lnyw.delcommafy(footerRows[0]['spje']));
	if(needA != '0'){
		effectRow['needAudit'] = needA;
		$.messager.alert('提示', '本次采购需进入' + needA + '级审批流程！', 'warning');
	}else{
		effectRow['needAudit'] = '0';
	}

	
	//将表格中的数据去掉最后一个空行后，转换为json格式
	effectRow['datagrid'] = JSON.stringify(rows.slice(0, rows.length - 1));
	//提交到action
	//MaskUtil.mask('正在保存，请等待……');
	$.ajax({
		type: "POST",
		url: '${pageContext.request.contextPath}/jxc/cgjhAction!save.action',
		data: effectRow,
		dataType: 'json',
		success: function(rsp){
			if(rsp.success){
		    	$.messager.show({
					title : '提示',
					msg : '提交成功！'
				});
		    	init();
		    	//原流程中，保存后即打印计划单，新流程中如需审批不打印计划单
		    	if(needA == undefined || needA == '0'){
			    	$.messager.confirm('请确认', '是否打印采购计划单？', function(r) {
						if (r) {
							var url = lnyw.bp() + '/jxc/cgjhAction!printCgjh.action?cgjhlsh=' + rsp.obj.cgjhlsh + "&bmbh=" + cgjh_did;
							jxc.print(url, PREVIEW_REPORT, HIDE_PRINT_WINDOW, {createId: ${user.id}, createName: "${user.realName}"});
						}
					});
		    	}
			}  
		},
		error: function(){
			$.messager.alert("提示", "提交错误了！");
		},
		complete: function(){
			//MaskUtil.unmask();
		}
	});
}

//处理编辑行
function setEditing(){
	//初始化编辑行
	var spRow = undefined;
	
	//加载字段
	var editors = cgjh_spdg.datagrid('getEditors', editIndex);
    spbhEditor = editors[0];
    spmcEditor = editors[1];
    spcdEditor = editors[2];
    spppEditor = editors[3];
    spbzEditor = editors[4];
    spdjEditor = editors[5];
    shdzEditor = editors[6];
    lxrEditor = editors[7];
    dhsjEditor = editors[8];
    zjldwEditor = editors[9];
    zslEditor = editors[10];
    zdjEditor = editors[11];
    cjldwEditor = editors[12];
    cslEditor = editors[13];
    cdjEditor = editors[14];
    spjeEditor = editors[15];
    zhxsEditor = editors[16];
    zjldwIdEditor = editors[17];
    cjldwIdEditor = editors[18];
    
    if($(spbhEditor.target).val() != ''){
    	jxc.spInfo($('#jxc_cgjh_layout'), '1', $(spppEditor.target).val(), $(spbzEditor.target).val());
		jxc.showKc('#jxc_cgjh_layout', 
				'${pageContext.request.contextPath}/jxc/cgjhAction!getSpkc.action', 
				{
					bmbh : cgjh_did, 
					ckId : jxc_cgjh_ckCombo.combobox('getValue'),
					spbh : $(spbhEditor.target).val(),
				});
    }else{
    	jxc.spInfo($('#jxc_cgjh_layout'), '');
    	jxc.hideKc('#jxc_cgjh_layout');
   	}
    
	//loadEditor();
	//处理编辑行的换行事件
	$('.datagrid-row-editing').on('keydown','input', function(event){
		//监听按下向下箭头
		if(event.keyCode == 40){
			var rowIndex = editIndex;
			if(rowOk()){
				cgjh_spdg.datagrid('endEdit', editIndex);
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
    						depId : cgjh_did,
    					},
    					dataType:'json',
    					success:function(data){
    						if(data.success){
    							//设置信息字段值
    							setValueBySpbh(data.obj);
    							//spOk = true;
    							spdjEditor.target.focus();
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
    				cgjh_did,
    				jxc_cgjh_ckCombo.combobox('getValue'), //查看指定仓库的库存，业务账-临时
    				'${pageContext.request.contextPath}/jxc/spQuery.jsp',
    				'${pageContext.request.contextPath}/jxc/spAction!spDg.action',
    				zslEditor		
    			);
    		return false;
    	}
    });
    
    shdzEditor.target.bind('keydown', function(event){
    	//按ESC键,弹出送货地址列表
    	if(event.keyCode == 27){
    		jxc.queryAddr('地址检索', shdzEditor.target, lxrEditor.target,
    				'${pageContext.request.contextPath}/jxc/queryAddr.jsp',
    				'${pageContext.request.contextPath}/jxc/shdzAction!shdzDg.action');
    	}
    });
//     function addressLoad(){
//     	switch(event.keyCode){
//     	case 27:
//     		jxc.query('地址检索', "", shdzEditor.target, 
//     				'${pageContext.request.contextPath}/jxc/query.jsp',
//     				'${pageContext.request.contextPath}/jxc/khAction!khDg.action');
//     		break;
//     	case 9:
//     		break;
//     	}
//     }
    
    //输入主单位数量后，计算金额
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
    	if(event.keyCode == 9){
    		return false;
    	}
    	if($(zhxsEditor.target).val() != 0){
    		$(zslEditor.target).numberbox('setValue', $(cslEditor.target).val() * $(zhxsEditor.target).val());
    	}
    	calculateCdw();
    }).bind('keydown', function(event){
     	if(event.keyCode == 9){
     		cdjEditor.target.focus();
     		return false;
     	}
    });
    
    //输入次单位单价后，计算金额
    cdjEditor.target.bind('keyup', function(event){
    	if($(zhxsEditor.target).val() != 0){
    		$(zdjEditor.target).numberbox('setValue', $(cdjEditor.target).val() / (1 + SL) / $(zhxsEditor.target).val());
    	}
    	calculateCdw();
    }).bind('keydown', function(event){
    	if(event.keyCode == 40){
     		spjeEditor.target.focus();
     	}
    }).bind('focus', function(event){
    	if($(cdjEditor.target).val() == 0){
    		$(cdjEditor.target).val('');
    	}
    });
    
  	//输入商品金额后，计算单价
    spjeEditor.target.bind('keyup', function(event){
    	if($(zhxsEditor.target).val() != 0 && $(cslEditor.target).val() != 0){
    		$(cdjEditor.target).numberbox('setValue', $(spjeEditor.target).val() / $(cslEditor.target).val());
    	}
    	if($(zslEditor.target).val() != 0){
    		$(zdjEditor.target).numberbox('setValue', $(spjeEditor.target).val() / (1 + SL) / $(zslEditor.target).val());
    	}
    	//更新汇总列
        updateFooter();
    }).bind('keydown', function(event){
    	if(event.keyCode == 40){
     		spjeEditor.target.focus();
     	}
    });

    
    //计算金额
    function calculate(){
        var spje = zslEditor.target.val() * zdjEditor.target.val() * (1 + SL);   
        $(spjeEditor.target).numberbox('setValue',spje);
        //更新汇总列
        updateFooter();
    }
    
    function calculateCdw(){
        var spje = cslEditor.target.val() * cdjEditor.target.val();   
        $(spjeEditor.target).numberbox('setValue',spje);
        //更新汇总列
        updateFooter();
    }
  	
    //初始进入编辑状态时，使用商品编号获得焦点
    spbhEditor.target.focus();
    updateFooter();
}
//求和
function updateFooter(){
 	var rows = cgjh_spdg.datagrid('getRows');
	var spmc_footer = '合计';
	var hjje = 0.000000;
	var hjsl = 0.000000;
	$.each(rows, function(){
		var index = cgjh_spdg.datagrid('getRowIndex', this);
		if(index < rows.length - 1){
			if(editIndex == index){
				hjje += Number(spjeEditor.target.val());
				hjsl += Number(cslEditor.target.val());
			}else{
				hjje += Number(this.spje == undefined ? 0 : this.spje);
				hjsl += Number(this.cdwsl == undefined ? 0 : this.cdwsl);
			}
		}
 	});
	cgjh_spdg.datagrid('reloadFooter', [{spmc : spmc_footer, cdwsl:hjsl.toFixed(LENGTH_SL), spje : lnyw.formatNumberRgx(hjje.toFixed(LENGTH_JE))}]);
}

//验证记录是否已存在
function existKey(value, rowIndex){
	var keys = cgjh_spdg.datagrid('getRows');
	var exist = false;
	if(keys.length > 2){
		$.each(keys, function(){
			var index = cgjh_spdg.datagrid('getRowIndex', this);
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
	if($('input[name=jxc_cgjh_gysmc]').val() == ''){
		message += '供应商信息<br>';
	}
// 	if($('input[name=ywyId]').val() == ''){
// 		message += '业务员信息<br>';
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
	
	spdjEditor.target.val('一等');
	
	jxc.spInfo($('#jxc_cgjh_layout'), '1', rowData.sppp, rowData.spbz);
	jxc.showKc('#jxc_cgjh_layout', 
			'${pageContext.request.contextPath}/jxc/cgjhAction!getSpkc.action', 
			{
				bmbh : cgjh_did, 
				ckId : jxc_cgjh_ckCombo.combobox('getValue'),
				spbh : $(spbhEditor.target).val(),
			});
}

<%--function gysLoad(){--%>
<%--	switch(event.keyCode){--%>
<%--	case 27:--%>
<%--		jxc.query('供应商检索', $('input[name=jxc_cgjh_gysbh]'), $('input[name=jxc_cgjh_gysmc]'), '',--%>
<%--				'${pageContext.request.contextPath}/jxc/query.jsp',--%>
<%--				'${pageContext.request.contextPath}/jxc/gysAction!gysDg.action');--%>
<%--		break;--%>
<%--	case 9:--%>
<%--		break;--%>
<%--	default:--%>
<%--		if($('input[name=jxc_cgjh_gysbh]').val().trim().length == 0){--%>
<%--			$('input[name=jxc_cgjh_gysmc]').val('');--%>
<%--		}--%>
<%--		if($('input[name=jxc_cgjh_gysbh]').val().trim().length == 8){--%>
<%--			$.ajax({--%>
<%--				url:'${pageContext.request.contextPath}/jxc/gysAction!loadGys.action',--%>
<%--				async: false,--%>
<%--				context:this,--%>
<%--				data:{--%>
<%--					gysbh: $('input[name=jxc_cgjh_gysbh]').val().trim(),--%>
<%--				},--%>
<%--				dataType:'json',--%>
<%--				success:function(data){--%>
<%--					if(data.success){--%>
<%--						//设置信息字段值--%>
<%--						$('input[name=jxc_cgjh_gysmc]').val(data.obj.gysmc);--%>
<%--						$('input[name=ywyId]').focus();--%>
<%--					}else{--%>
<%--						$.messager.alert('提示', '供应商信息不存在！', 'error');--%>
<%--					}--%>
<%--				}--%>
<%--			});--%>
<%--		}--%>
<%--		break;--%>
<%--	}--%>
<%--}--%>


//////////////////////////////////////////////以上为商品列表处理代码

//////////////////////////////////////////////以下为采购计划列表处理代码
function cancelCgjh(){
	var row = cgjh_dg.datagrid('getSelected');
	if (row != undefined) {
		if(row.isKfrk != '1'){
			if(row.isCancel != '1'){
				if(!(row.nbjhlsh != undefined && row.isNb == '1')){
					if(row.isCompleted != '1'){
						$.messager.confirm('请确认', '您要取消选中的采购计划单？', function(r) {
							if (r) {
								//MaskUtil.mask('正在取消，请等待……');
								$.ajax({
									url : '${pageContext.request.contextPath}/jxc/cgjhAction!cancel.action',
									data : {
										cgjhlsh : row.cgjhlsh,
										bmbh : cgjh_did,
										menuId : cgjh_menuId,
									},
									dataType : 'json',
									success : function(d) {
										cgjh_dg.datagrid('reload');
										cgjh_dg.datagrid('unselectAll');
										$.messager.show({
											title : '提示',
											msg : d.msg
										});
									},
									complete: function(){
										//MaskUtil.unmask();
									}
								});
							}
						});
					}else{
						$.messager.alert('警告', '选中的采购计划已完成，不能取消！',  'warning');
					}
                }else{
                    $.messager.alert('警告', '选中的采购计划已由' + row.gysmc + '进行处理，不能取消！',  'warning');
                }
			}else{
				$.messager.alert('警告', '选中的采购计划已被取消，请重新选择！',  'warning');
			}
		}else{
			$.messager.alert('警告', '选中的采购计划已进行库房入库，不能取消，请重新选择！',  'warning');
		}
	}else{
		$.messager.alert('警告', '请选择最少一条记录进行操作！',  'warning');
	}
}

function printCgjh(){
	var row = cgjh_dg.datagrid('getSelected');
	if (row != undefined) {
		if(row.needAudit == row.isAudit){
			$.messager.confirm('请确认', '是否打印采购计划单？', function(r) {
				if (r) {
					var url = lnyw.bp() + '/jxc/cgjhAction!printCgjh.action?cgjhlsh=' + row.cgjhlsh + "&bmbh=" + cgjh_did;
					jxc.print(url, PREVIEW_REPORT, HIDE_PRINT_WINDOW, {createId: ${user.id}, createName: "${user.realName}"});
				}
			});
		}else{
			$.messager.alert('警告', '选中的计划单还未进行审批，请重新选择择一条记录进行操作！',  'warning');
		}
	}else{
		$.messager.alert('警告', '请选择一条记录进行操作！',  'warning');
	}
}

function exportCgjh(){
	var row = cgjh_dg.datagrid('getSelected');
	if (row != undefined) {
		if(row.needAudit == row.isAudit){
			$.messager.confirm('请确认', '是否导出采购计划单？', function(r) {
				if (r) {
					var data = {
							cgjhlsh : row.cgjhlsh,
							bmbh: cgjh_did,
							type: 'rtf'
						};
					jxc.export('${pageContext.request.contextPath}', '/jxc/cgjhAction!export.action', data);
				}
			});
		}else{
			$.messager.alert('警告', '选中的计划单还未进行审批，请重新选择择一条记录进行操作！',  'warning');
		}
	}else{
		$.messager.alert('警告', '请选择一条记录进行操作！',  'warning');
	}
}

function completeCgjh(){
	var row = cgjh_dg.datagrid('getSelected');
	if (row != undefined) {
		if(row.isCancel != '1'){
			//if(row.kfrklshs != null){
				if(row.isCompleted != '1'){
					$.messager.confirm('请确认', '您是否要完成选中的采购需求单？', function(r) {
						if (r) {
							$.ajax({
								url : '${pageContext.request.contextPath}/jxc/cgjhAction!complete.action',
								data : {
									cgjhlsh : row.cgjhlsh,
									bmbh : cgjh_did,
									menuId : cgjh_menuId,
								},
								dataType : 'json',
								success : function(d) {
                                    cgjh_dg.datagrid('updateRow', {
                                        index: cgjh_dg.datagrid('getRowIndex', row),
                                        row: {
                                            isCompleted: '1'
                                        }
                                    });
									//cgjh_dg.datagrid('reload');
									//cgjh_dg.datagrid('unselectAll');
									$.messager.show({
										title : '提示',
										msg : d.msg
									});
								}
							});
						}
					});
				}else{
					$.messager.alert('警告', '选中的采购计划已完成，请重新选择！',  'warning');
				}
			//}else{
			//	$.messager.alert('警告', '选中的采购计划还未进行入库，不能进行完成操作，请重新选择！',  'warning');
			//}
		}else{
			$.messager.alert('警告', '选中的采购计划已被取消，请重新选择！',  'warning');
		}
	}else{
		$.messager.alert('警告', '请选择最少一条记录进行操作！',  'warning');
	}
}

function unComplete(){
	var row = cgjh_dg.datagrid('getSelected');
	if (row != undefined) {
		if(row.isCompleted == '1'){
			$.messager.confirm('请确认', '您是否要取消完成选中的采购需求单？', function(r) {
				if (r) {
					$.ajax({
						url : '${pageContext.request.contextPath}/jxc/cgjhAction!unComplete.action',
						data : {
							cgjhlsh : row.cgjhlsh,
							bmbh : cgjh_did,
							menuId : cgjh_menuId,
						},
						dataType : 'json',
						success : function(d) {
                            cgjh_dg.datagrid('updateRow', {
                                index: cgjh_dg.datagrid('getRowIndex', row),
                                row: {
                                    isCompleted: '0'
                                }
                            });
							//cgjh_dg.datagrid('reload');
							//cgjh_dg.datagrid('unselectAll');
							$.messager.show({
								title : '提示',
								msg : d.msg
							});
						}
					});
				}
			});
		}else{
			$.messager.alert('警告', '选中的采购计划还未完成，请重新选择！',  'warning');
		}
		
	}else{
		$.messager.alert('警告', '请选择最少一条记录进行操作！',  'warning');
	}	
}

function changeHt(){
	var row = cgjh_dg.datagrid('getSelected');
	if (row != undefined) {
		if(row.isCancel != '1'){
			//if(row.isHt == '1'){
				if(row.returnHt == '0'){
					$.messager.confirm('请确认', '您是否要转换选中的采购计划合同标志？', function(r) {
						if (r) {
							$.ajax({
								url : '${pageContext.request.contextPath}/jxc/cgjhAction!changeHt.action',
								data : {
									cgjhlsh : row.cgjhlsh,
									bmbh : cgjh_did,
									menuId : cgjh_menuId,
								},
								dataType : 'json',
								success : function(d) {
                                    cgjh_dg.datagrid('updateRow', {
                                        index: cgjh_dg.datagrid('getRowIndex', row),
                                        row: {
                                            isHt: row.isHt == '1' ? '0' : '1'
                                        }
                                    });
									//cgjh_dg.datagrid('reload');
									//cgjh_dg.datagrid('unselectAll');
									$.messager.show({
										title : '提示',
										msg : d.msg
									});
								}
							});
						}
					});
				}else{
					$.messager.alert('警告', '选中的采购计划已标注合同收回，请重新选择！',  'warning');
				}
			//}else{
			//	$.messager.alert('警告', '选中的采购计划不是合同计划，请重新选择！',  'warning');
			//}
		}else{
			$.messager.alert('警告', '选中的采购计划已被取消，请重新选择！',  'warning');
		}
	}else{
		$.messager.alert('警告', '请选择最少一条记录进行操作！',  'warning');
	}
}

function htCgjh(){
	var row = cgjh_dg.datagrid('getSelected');
	if (row != undefined) {
		if(row.isCancel != '1'){
			if(row.isHt == '1'){
				if(row.returnHt == '0'){
					$.messager.confirm('请确认', '您是否要标记选中的采购计划合同标志？', function(r) {
						if (r) {
							$.ajax({
								url : '${pageContext.request.contextPath}/jxc/cgjhAction!ht.action',
								data : {
									cgjhlsh : row.cgjhlsh,
									bmbh : cgjh_did,
									menuId : cgjh_menuId,
								},
								dataType : 'json',
								success : function(d) {
                                    cgjh_dg.datagrid('updateRow', {
                                        index: cgjh_dg.datagrid('getRowIndex', row),
                                        row: {
                                            returnHt: '1'
                                        }
                                    });
								    //cgjh_dg.datagrid('reload');
									//cgjh_dg.datagrid('unselectAll');
									$.messager.show({
										title : '提示',
										msg : d.msg
									});
								}
							});
						}
					});
				}else{
					$.messager.alert('警告', '选中的采购计划已标注合同收回，请重新选择！',  'warning');
				}
			}else{
				$.messager.alert('警告', '选中的采购计划不是合同计划，请重新选择！',  'warning');
			}
		}else{
			$.messager.alert('警告', '选中的采购计划已被取消，请重新选择！',  'warning');
		}
	}else{
		$.messager.alert('警告', '请选择最少一条记录进行操作！',  'warning');
	}
}

function updateGys(){
    var selected = cgjh_dg.datagrid('getSelected');
	if(selected != null){
		if(selected.isCancel == '0'){
			$.messager.prompt('请确认', '是否要修改供应商？请输入供应商编号：', function(gysbh){
				if (gysbh != undefined){
                    $.ajax({
                        url : '${pageContext.request.contextPath}/jxc/gysAction!loadGys.action',
                        data : {
                            gysbh: gysbh,
                        },
                        dataType : 'json',
                        method: 'post',
                        success : function(gys) {
                            if(gys.success){
								$.ajax({
									url : '${pageContext.request.contextPath}/jxc/cgjhAction!updateGys.action',
									data : {
										cgjhlsh : selected.cgjhlsh,
										gysbh: gysbh,
										gysmc: gys.obj.gysmc,
										bmbh : cgjh_did,
										menuId : cgjh_menuId
									},
									dataType : 'json',
									method: 'post',
									success : function(d) {
                                        cgjh_dg.datagrid('updateRow', {
											index: cgjh_dg.datagrid('getRowIndex', selected),
											row: {
												gysbh: gysbh,
												gysmc: gys.obj.gysmc
											}
										});
										$.messager.show({
											title : '提示',
											msg : d.msg
										});
									}
								});
							}else{
                                $.messager.alert('警告', '没有找到对应的供应商名称，请重新录入！',  'warning');
                                return;
                            }
						}
					});
				}
			});
		}else{
			$.messager.alert('警告', '选择的采购计划已经取消，请重新选择！',  'warning');
		}
	}else{
		$.messager.alert('警告', '请选择一条采购计划进行操作！',  'warning');
		return false;
	}
}


//要判断处理的单据有效性（冲减、开票、直送）
function updateShdz(){
    if(cgjh_detDg != undefined){
        var detRow = cgjh_detDg.datagrid('getSelected');
        if(detRow != null){
            if(cgjhRow.isCancel == '0'){
				$.messager.prompt('请确认', '是否要修改送货地址？请输入', function(shdz){
					if (shdz != undefined){
						$.ajax({
							url : '${pageContext.request.contextPath}/jxc/cgjhAction!updateShdz.action',
							data : {
								id : detRow.id,
								shdz: shdz,
								bmbh : cgjh_did,
								menuId : cgjh_menuId
							},
							dataType : 'json',
							method: 'post',
							success : function(d) {
								cgjh_detDg.datagrid('updateRow', {
                                    index: cgjh_detDg.datagrid('getRowIndex', detRow),
                                    row: {
                                        shdz: shdz
                                    }
								});
								//cgjh_detDg.datagrid('unselectAll');
								$.messager.show({
									title : '提示',
									msg : d.msg
								});
							}
						});
					}
                });
			}else{
				$.messager.alert('警告', '选择的采购计划已经取消，请重新选择！',  'warning');
			}
        }else{
            $.messager.alert('警告', '请选择商品明细记录进行操作！',  'warning');
            return false;
        }
    }else{
        $.messager.alert('警告', '请选择商品明细记录进行操作！',  'warning');
        return false;
    }

}

function lockSpInCgjh(){
	if(cgjh_detDg != undefined){
		var cgjh_detRow = cgjh_detDg.datagrid('getSelected');
		if(cgjh_detRow != null){
			if(cgjh_detRow.isLock == '0'){
				if(cgjh_detRow.isBack == '0'){
					// if(cgjh_detRow.zdwyrsl != undefined){
						$.messager.confirm('请确认', '您是否要锁定选中的采购计划商品品种？', function(r) {
							if (r) {
								$.ajax({
									url : '${pageContext.request.contextPath}/jxc/cgjhAction!lockSpInCgjh.action',
									data : {
										//cgjhlsh : row.cgjhlsh,
										id : cgjh_detRow.id,
										bmbh : cgjh_did,
										menuId : cgjh_menuId,
									},
									dataType : 'json',
									success : function(d) {
                                        cgjh_detDg.datagrid('updateRow', {
                                            index: cgjh_detDg.datagrid('getRowIndex', cgjh_detRow),
                                            row: {
                                                isLock: '1'
                                            }
                                        });
										//cgjh_detDg.datagrid('reload');
										//cgjh_detDg.datagrid('unselectAll');
										$.messager.show({
											title : '提示',
											msg : d.msg
										});
									}
								});
							}
						});
					// }else{
					// 	$.messager.alert('警告', '选择的商品记录还未到货，只能做取消操作，请重新选择！',  'warning');
					// }
				}else{
					$.messager.alert('警告', '选择的商品记录已被取消，请重新选择！',  'warning');
				}
			}else{
				$.messager.alert('警告', '选择的商品记录已被锁定，请重新选择！',  'warning');
			}
		}else{
			$.messager.alert('警告', '请选择商品明细记录进行操作！',  'warning');
			return false;
		}
		
	}else{
		$.messager.alert('警告', '请选择商品明细记录进行操作！',  'warning');
		return false;
	}
}

function backSpInCgjh(){
	if(cgjh_detDg != undefined){
		var cgjh_detRow = cgjh_detDg.datagrid('getSelected');
		if(cgjh_detRow != null){
			if(cgjh_detRow.isLock == '0'){
				if(cgjh_detRow.isBack == '0'){
					if(cgjh_detRow.zdwyrsl == undefined){
						$.messager.confirm('请确认', '您是否要取消选中的采购计划商品品种？', function(r) {
							if (r) {
								$.ajax({
									url : '${pageContext.request.contextPath}/jxc/cgjhAction!backSpInCgjh.action',
									data : {
										id : cgjh_detRow.id,
										bmbh : cgjh_did,
										menuId : cgjh_menuId,
									},
									dataType : 'json',
									success : function(d) {
                                        cgjh_detDg.datagrid('updateRow', {
                                            index: cgjh_detDg.datagrid('getRowIndex', cgjh_detRow),
                                            row: {
                                                isBack: '0'
                                            }
                                        });
										//cgjh_detDg.datagrid('reload');
										//cgjh_detDg.datagrid('unselectAll');
										$.messager.show({
											title : '提示',
											msg : d.msg
										});
									}
								});
							}
						});
					}else{
						$.messager.alert('警告', '选择的商品记录已到货，不能取消，请重新选择！',  'warning');
					}
				}else{
					$.messager.alert('警告', '选择的商品记录已被取消，请重新选择！',  'warning');
				}
			}else{
				$.messager.alert('警告', '选择的商品记录已被锁定，请重新选择！',  'warning');
			}
		}else{
			$.messager.alert('警告', '请选择商品明细记录进行操作！',  'warning');
			return false;
		}
	}else{
		$.messager.alert('警告', '请选择商品明细记录进行操作！',  'warning');
		return false;
	}
}

function searchCgjh(){
	cgjh_dg.datagrid('load',{
		bmbh: cgjh_did,
		createTime: $('input[name=createTimeCgjh]').val(),
		search: $('input[name=searchCgjh]').val(),
		isZs: $('input#jxc_cgjhDg_isZs').is(':checked') ? '1' : '0',
		isNotZs: $('input#jxc_cgjhDg_isNotZs').is(':checked') ? '1' : '0',
	});
}

function expandRows(){
	$.each(cgjh_dg.datagrid('getRows'), function(index){
		cgjh_dg.datagrid('expandRow', index);
	});
}


//////////////////////////////////////////////以上为采购计划列表处理代码


//////////////////////////////////////////////以下为采购需求列表处理代码

function createCgjh(){
	var rows = cgjh_cgxqDg.datagrid('getSelections');
	var cgxqDetIds = [];
	var cgxqlshs = [];
	var cgxqBzs = [];
	if(rows.length > 0){
		$.messager.confirm('请确认', '是否要将选中记录生成采购计划？', function(r) {
			if (r) {
				for ( var i = 0; i < rows.length; i++) {
					cgxqDetIds.push(rows[i].id);
					//检查每条需求的备注，并合并
					if(cgxqlshs.indexOf(rows[i].cgxqlsh) < 0){
						cgxqlshs.push(rows[i].cgxqlsh);						
						cgxqBzs.push(rows[i].bz);
					}
				}
				var cgxqDetStr = cgxqDetIds.join(',');
				$.ajax({
					url : '${pageContext.request.contextPath}/jxc/cgxqAction!toCgjh.action',
					data : {
						cgxqDetIds : cgxqDetStr
					},
					dataType : 'json',
					success : function(d) {
						$.each(d.rows, function(index){
							if(index == d.rows.length - 1){
								return false;
							}
							d.rows[index].lxr = rows[0].lxr;
							d.rows[index].shdz = rows[0].shdz;
							d.rows[index].dhsj = rows[0].dhsj;
							d.rows[index].spdj = '一等';
						});
						cgjh_spdg.datagrid('loadData', d.rows);
						
						$('input[name=cgxqDetIds]').val(cgxqDetStr);
						$('input[name=jxc_cgjh_bz]').val(cgxqBzs.join(','));
						cgjh_tabs.tabs('select', 0);
					}
				});
			}
		});
	}else{
		$.messager.alert('警告', '请选择最少一条记录进行操作！',  'warning');
	}
}

function refuseCgxq(){
	var row = cgjh_cgxqDg.datagrid('getSelected');
	if (row != undefined) {
		$.messager.confirm('请确认', '您要退回选中的采购需求单？', function(r) {
			if (r) {
				$.ajax({
					url : '${pageContext.request.contextPath}/jxc/cgxqAction!refuse.action',
					data : {
						id : row.id,
						bmbh : cgjh_did,
						menuId : cgjh_menuId,
					},
					dataType : 'json',
					success : function(d) {
                        cgjh_cgxqDg.datagrid('updateRow', {
                            index: cgjh_cgxqDg.datagrid('getRowIndex', row),
                            row: {
                                isRefuse: '1'
                            }
                        });
						//cgjh_cgxqDg.datagrid('reload');
						//cgjh_cgxqDg.datagrid('unselectAll');
						$.messager.show({
							title : '提示',
							msg : d.msg
						});
					}
				});
			}
		});
	}else{
		$.messager.alert('警告', '请选择最少一条记录进行操作！',  'warning');
	}
}

function completeCgxq(){
	var row = cgjh_cgxqDg.datagrid('getSelected');
	if (row != undefined) {
		$.messager.confirm('请确认', '您要完成选中的采购需求单？', function(r) {
			if (r) {
				$.ajax({
					url : '${pageContext.request.contextPath}/jxc/cgxqAction!complete.action',
					data : {
						id : row.id,
						bmbh : cgjh_did,
						menuId : cgjh_menuId,
					},
					dataType : 'json',
					success : function(d) {
                        cgjh_cgxqDg.datagrid('updateRow', {
                            index: cgjh_cgxqDg.datagrid('getRowIndex', row),
                            row: {
                                isComplete: '1'
                            }
                        });
						//cgjh_cgxqDg.datagrid('reload');
						//cgjh_cgxqDg.datagrid('unselectAll');
						$.messager.show({
							title : '提示',
							msg : d.msg
						});
					}
				});
			}
		});
	}else{
		$.messager.alert('警告', '请选择最少一条记录进行操作！',  'warning');
	}
}


// function searchCgxqInCgjh(){
// 	cgjh_cgxqDg.datagrid('load',{
// 		bmbh: did,
// 		createTime: $('input[name=createTimeCgxqInCgjh]').val(),
// 		fromOther: 'fromCgjh'
// 	});
// }

//////////////////////////////////////////////以上为采购需求列表处理代码

function createCgjhFromSpkc(){
	var rows = cgjh_spkcDg.datagrid('getSelections');
	//var spbhs = [];
	if(rows.length > 0){
		$.messager.confirm('请确认', '是否要将选中记录生成采购计划？', function(r) {
			if (r) {
// 				for ( var i = 0; i < rows.length; i++) {
// 					spbhs.push(rows[i].spbh);
// 				}
// 				var spbhStr = spbhs.join(',');
// 				$.ajax({
// 					url : '${pageContext.request.contextPath}/jxc/ywzzAction!toCgjh.action',
// 					data : {
// 						spbhs : spbhStr,
// 						bmbh : did
// 					},
// 					dataType : 'json',
// 					success : function(d) {
// 						cgjh_spdg.datagrid('loadData', d.rows);
// // 						$('input[name=cgxqDetIds]').val(cgxqDetStr);
// 						cgjh_tabs.tabs('select', 0);
// 					}
// 				});
				cgjh_spdg.datagrid('loadData', rows);
// // 						$('input[name=cgxqDetIds]').val(cgxqDetStr);
						cgjh_tabs.tabs('select', 0);
			}
		});
	}else{
		$.messager.alert('警告', '请选择最少一条记录进行操作！',  'warning');
	}
}

////////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////以下为销售提货(直送)列表处理内容

function createCgjhFromXsth(){
	var rows = cgjh_xsthDg.datagrid('getSelections');
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
			$.messager.confirm('请确认', '是否要将选中记录进行采购？', function(r) {
				if (r) {
					for ( var i = 0; i < rows.length; i++) {
						xsthDetIds.push(rows[i].id);
					}
					var xsthDetIdsStr = xsthDetIds.join(',');
					$.ajax({
						url : '${pageContext.request.contextPath}/jxc/xsthAction!toCgjh.action',
						data : {
							xsthDetIds : xsthDetIdsStr,
							shdz: rows[0].shdz,
							thr: rows[0].thr
						},
						dataType : 'json',
						type: 'post',
						success : function(d) {
							cgjh_spdg.datagrid('loadData', d.rows);
							updateFooter();
							$('input[name=cgjh_xsthDetIds]').val(xsthDetIdsStr);
//							$('input[name=xsthlsh]').val(rows[0].xsthlsh);
							jxc_cgjh_ckCombo.combobox('setValue', rows[0].ckId);
							$('#jxc_cgjh_isZs').prop('checked', 'checked');
							$('input[name=jxc_cgjh_bz]').val(rows[0].bz);
							
							cgjh_tabs.tabs('select', 0);
						}
					});
				}
			});
    	}
	}else{
		$.messager.alert('警告', '请选择最少一条记录进行操作！',  'warning');
	}
}

///////////////////////////////////以上为销售提货(直送)列表处理内容

///////////////////////////////////以下为采购计划(内部)列表处理内容
function createCgjhFromCgjh(){
	var rows = cgjh_cgjhDg.datagrid('getSelections');
	var flag = true;
	if(rows.length > 0){
    	if(rows.length > 1){
    		var preRow = undefined;
		    $.each(rows, function(index){
		    	if(index != 0){
		    		if(this.cgjhlsh != preRow.cgjhlsh){
		    			$.messager.alert('提示', '请选择同一张采购计划单的商品进行出库！', 'error');
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
			$.messager.confirm('请确认', '是否要将选中记录进行采购？', function(r) {
				if (r) {
					$.ajax({
						url : '${pageContext.request.contextPath}/jxc/cgjhAction!toCgjhFromCgjh.action',
						data : {
							cgjhlsh : rows[0].cgjhlsh,
						},
						dataType : 'json',
						type: 'post',
						success : function(d) {
							cgjh_spdg.datagrid('loadData', d.rows);
							updateFooter();
							$('input[name=cgjh_nbjhlsh]').val(rows[0].cgjhlsh);
							if(rows[0].isZs == '1'){
								jxc_cgjh_ckCombo.combobox('setValue', jxc.getZfCk(cgjh_did));
							}
							$('#jxc_cgjh_isZs').prop('checked', rows[0].isZs == '1' ? 'checked' : undefined);
							$('input[name=jxc_cgjh_bz]').val(rows[0].bz);
							cgjh_tabs.tabs('select', 0);
						}
					});
				}
			});
    	}
	}else{
		$.messager.alert('警告', '请选择最少一条记录进行操作！',  'warning');
	}
}
///////////////////////////////////以上为采购计划(内部)列表处理内容

</script>

<!-- tabPosition:'left', headerWidth:'35' -->
<div id="jxc_cgjh_tabs" class="easyui-tabs" data-options="fit:true, border:false," style="width:100%;height:100%;">
	
    <div title="新增记录" data-options="closable:false">
        <div id='jxc_cgjh_layout' style="height:100%;width:100%">
			<div data-options="region:'north',title:'单据信息',border:false,collapsible:false" style="width:100%;height:145px">		
				<table class="tinfo">
					<tr>
						<td colspan="4">
							合同<input type="checkbox" name="isHt" id="isHt">&nbsp;&nbsp;&nbsp;&nbsp;
							直送<input type="checkbox" id='jxc_cgjh_isZs' name="isZs">&nbsp;&nbsp;&nbsp;&nbsp;
							内部<input type="checkbox" id='jxc_cgjh_isNb' name="isNb">
							</td>
<!-- 						<th>临时采购</th><td colspan="3"><input type="checkbox" name="isLs" value="1" /> -->
						<th class="read"></th><td><div id="createDate" class="read"></div></td>
						<th class="read">单据号</th><td><div id="cgjhLsh" class="read"></div></td>
					</tr>
					<tr>
						<th>供应商编码</th><td><input name="jxc_cgjh_gysbh" class="easyui-validatebox"
							data-options="validType:['mustLength[8]','integer']" onkeyup="jxc.gysLoad('jxc_cgjh_gysbh', 'jxc_cgjh_gysmc')" size="8"></td>
						<th class="read">供应商名称</th><td class="read"><input name="jxc_cgjh_gysmc" readonly="readonly" size="50"></td>
<!-- 						<th>业务员</th><td><input name="ywyId"></td> -->
						<th>结算方式</th><td><input id="jxc_cgjh_jsfsId" name="jsfsId" size="8"></td>
						<th>仓库</th><td><input id="jxc_cgjh_ckId" name="ckId" size="8"></td>
					</tr>
<!-- 					<tr> -->
<!-- 						<th>到货时间</th><td><input name="dhsj" type="text" class="easyui-my97" size="8"></td> -->
<!-- 						<th>送货地址</th><td><input name="shdz" onkeyup="addressLoad()" size="50"></td> -->
<!-- 						<th>联系人</th><td><input name="lxr" size="20"></td> -->
<!-- 						----<th>需求时间</th><td><input name="xqsj"></td> -->
<!-- 					</tr> -->
					<tr>
						<th>备注</th><td colspan="7"><input name="jxc_cgjh_bz" style="width:90%"></td>
					</tr>
				</table>
				<input name="cgxqDetIds" type="hidden">
				<input name="cgjh_xsthDetIds" type="hidden">
				<input name="cgjh_nbjhlsh" type="hidden">
			</div>
			<div data-options="region:'center',title:'商品信息',split:true" style="width:150px">		
				<table id='jxc_cgjh_spdg'></table>
			</div>
			<div data-options="region:'east',title:'商品库存信息',split:true,collapsed:true," style="width:150px">
				<table id="show_spkc"></table>
			</div>
		</div>
    </div>
    <div title="采购计划列表" data-options="closable:false" >
    	<table id='jxc_cgjh_dg'></table>
    </div>
	<div title="采购需求列表" data-options="closable:false" >
		<table id='jxc_cgjh_cgxqDg'></table>
	</div>
	<div title="商品库存列表" data-options="closable:false" >
		<table id='jxc_cgjh_spkcDg'></table>
	</div>
	<div title="销售提货(直送)列表" data-options="closable:false" >
		<table id='jxc_cgjh_xsthDg'></table>
	</div>
	<div title="采购计划(内部)列表" data-options="closable:false" >
		<table id='jxc_cgjh_cgjhDg'></table>
	</div>
</div>

<div id="jxc_cgjh_tb" style="padding:3px;height:auto">
	请输入查询起始日期:<input type="text" name="createTimeCgjh" class="easyui-datebox" data-options="value: moment().date(1).format('YYYY-MM-DD')" style="width:100px">
	输入流水号、供应商编号、名称、备注：<input type="text" name="searchCgjh" style="width:100px">
	直送<input type="checkbox" id='jxc_cgjhDg_isZs'>
	非直送<input type="checkbox" id='jxc_cgjhDg_isNotZs'>
	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchCgjh();">查询</a>
	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="expandRows();">展开</a>
	
</div>
<!-- <div id="jxc_cgjh_cgxqTb" style="padding:3px;height:auto"> -->
<!-- 	请输入查询起始日期:<input type="text" name="createTimeCgxqInCgjh" class="easyui-datebox" data-options="value: moment().date(1).format('YYYY-MM-DD')" style="width:100px"> -->
<!-- 	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchCgxqInCgjh();">查询</a> -->
<!-- </div> -->
<div id="jxc_xsth_tb" style="padding:3px;height:auto">
	请输入查询起始日期:<input type="text" name="createTimeXsthInCgjh" class="easyui-datebox" data-options="value: moment().date(1).format('YYYY-MM-DD')" style="width:100px">
	输入流水号、客户、名称、备注：<input type="text" name="searchXsthInCgjh" style="width:100px">
	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchXsthInCgjh();">查询</a>
</div>
<div id="jxc_cgjh_spkcTb" style="padding:3px;height:auto">
</div>
