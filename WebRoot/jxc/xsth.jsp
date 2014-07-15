<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<script type="text/javascript">
var xsth_dg;
var xsth_spdg;
var xsth_kfckDg;
var xsth_xskpDg;
var editIndex = undefined;
var xsth_did;
var xsth_lx;
var xsth_menuId;
var countXsth = 0;
var countXskpInXsth = 0;

var jxc_xsth_ckCombo;
var jxc_xsth_fhCombo;
var jxc_xsth_ywyCombo;
var jxc_xsth_jsfsCombo;

//编辑行字段
var spbhEditor;   
var spmcEditor;
var spcdEditor;
var spppEditor;
var spbzEditor;
var zjldwEditor;
var kpslEditor;
var thslEditor;
var zslEditor;
var zdjEditor;
var cancelldwEditor;
var cslEditor;
var cdjEditor;   
var spjeEditor;
var zhxsEditor;
var zjldwIdEditor;
var cjldwIdEditor;

$(function(){
	xsth_did = lnyw.tab_options().did;
	xsth_lx = lnyw.tab_options().lx;
	xsth_menuId = lnyw.tab_options().id;

	$('#jxc_xsth_layout').layout({
		fit : true,
		border : false,
	});
	
	xsth_dg = $('#jxc_xsth_dg').datagrid({
		fit : true,
	    border : false,
	    singleSelect : true,
// 	    fitColumns : true, 
	    remoteSort: false,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
		columns:[[
			{field:'xsthlsh',title:'流水号',align:'center',
				styler: function(value, rowData){
					if(rowData.isCancel == '1'){
						return 'color:red;';
					}
				}},
	        {field:'createTime',title:'*时间',align:'center',sortable:true,
				sorter: function(a,b){
					return moment(a).diff(moment(b), 'days');
				}},
			{field:'createName',title:'*创建人',align:'center',sortable:true,
				sorter: function(a,b){
					return a > b;
				}},	
	        {field:'khbh',title:'客户编号',align:'center',hidden:true},
	        {field:'khmc',title:'客户名称',align:'center'},
	        {field:'ckId',title:'仓库id',align:'center',hidden:true},
	        {field:'ckmc',title:'仓库',align:'center'},
	        {field:'fhId',title:'分户 id',align:'center',hidden:true},
	        {field:'fhmc',title:'分户名称',align:'center'},
	        {field:'ywyId',title:'业务员 id',align:'center',hidden:true},
	        {field:'ywymc',title:'业务员',align:'center'},
	        {field:'jsfsId',title:'结算方式id',align:'center',hidden:true},
	        {field:'jsfsmc',title:'结算方式',align:'center'},
	        {field:'thfs',title:'提货方式',align:'center',
	        	formatter: function(value){
	        		if(value == '1'){
	        			return '自提';
	        		}else{
	        			return '送货';
	        		}
    			}},
	        {field:'shdz',title:'送货地址',align:'center'},
	        {field:'ch',title:'车号',align:'center'},
	        {field:'thr',title:'提货人',align:'center'},
	        {field:'hjje',title:'金额',align:'center',
	        	formatter: function(value){
	        		return lnyw.formatNumberRgx(value);
	        	}},
	        {field:'hjsl',title:'数量',align:'center'},
	        {field:'bookmc',title:'书名',align:'center',
        		formatter: function(value){
        			return lnyw.memo(value, 15);
        		}},
	        {field:'bz',title:'备注',align:'center',
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
        	{field:'isTh',title:'已提货',align:'center',
           		formatter: function(value){
           			if(value == '1'){
           				return '是';
           			}
           			return '';
           		}},
        	{field:'isCancel',title:'*取消',align:'center',sortable:true,
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
				},
				styler: function(value){
					if(value == '1'){
						return 'color:red;';
					}
				}},
			{field:'cancelTime',title:'取消时间',align:'center'},
			{field:'cjXsthlsh',title:'冲减销售提货',align:'center'},
        	{field:'isHk',title:'*回款',align:'center',sortable:true,
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
			{field:'hkTime',title:'回款时间',align:'center'},
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
        	{field:'toFp',title:'*开票',align:'center',sortable:true,
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
			{field:'kpTime',title:'开票时间',align:'center'},
        	{field:'xskplsh',title:'销售流水号',align:'center',
        		formatter: function(value){
        			return lnyw.memo(value, 15);
        		}},
	    ]],
	    toolbar:'#jxc_xsth_tb',
	});
	lnyw.toolbar(1, xsth_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', xsth_did);
	//$("#dg").datagrid({....}).datagrid('tooltip'); 所有列
	//$("#dg").datagrid({....}).datagrid('tooltip',['productid','listprice']); 指定列
	
	
	xsth_dg.datagrid({
        view: detailview,
        detailFormatter:function(index,row){
            return '<div style="padding:2px"><table id="xsth-ddv-' + index + '"></table></div>';
        },
        onExpandRow: function(index,row){
            $('#xsth-ddv-'+index).datagrid({
                url:'${pageContext.request.contextPath}/jxc/xsthAction!detDatagrid.action',
                fitColumns:false,
                singleSelect:true,
                rownumbers:true,
                loadMsg:'',
                height:'auto',
                queryParams: {
        			xsthlsh: row.xsthlsh,
        		},
                columns:[[
                    {field:'spbh',title:'商品编号',width:200,align:'center'},
                    {field:'spmc',title:'名称',width:100,align:'center'},
                    {field:'spcd',title:'产地',width:100,align:'center'},
                    {field:'sppp',title:'品牌',width:100,align:'center'},
                    {field:'spbz',title:'包装',width:100,align:'center'},
                    {field:'zjldwmc',title:'单位1',width:100,align:'center'},
                    {field:'zdwsl',title:'数量1',width:100,align:'center'},
                    {field:'cksl',title:'出库数量',width:100,align:'center',
                    	formatter : function(value) {
        					return value == 0 ? '' : value;
        				},
                    	styler: function(value){
	    					if(value != 0){
	    						return 'color:red;';
	    					}
    				}},
                    {field:'kpsl',title:'开票数量',width:100,align:'center',
                    	formatter : function(value) {
        					return value == 0 ? '' : value;
        				},

    					styler: function(value){
	    					if(value != 0){
	    						return 'color:blue;';
	    					}
                    	}},
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
                	xsth_dg.datagrid('fixDetailRowHeight',index);
                },
                onLoadSuccess:function(){
                    setTimeout(function(){
                    	xsth_dg.datagrid('fixDetailRowHeight',index);
                    },0);
                }
            });
            xsth_dg.datagrid('fixDetailRowHeight',index);
        }
    });
	
// 	xsth_kfckDg = $('#jxc_xsth_kfckDg').datagrid({
// 		fit : true,
// 	    border : false,
// 	    singleSelect : true,
// 	    remoteSort: false,
// 	    fitColumns: true,
// 	    pagination : true,
// 		pagePosition : 'bottom',
// 		pageSize : 10,
// 		pageList : [ 10, 15, 20, 25, 30 ],
// 		frozenColumns:[[
// 		]],
// 		columns:[[
// 			{field:'kfcklsh',title:'流水号',align:'center'},
// 	        {field:'createTime',title:'*时间',align:'center',sortable:true,
// 				sorter: function(a,b){
// 					return moment(a).diff(moment(b), 'days');
// 				}},
// 	        {field:'gysbh',title:'供应商编号',align:'center'},
// 	        {field:'gysmc',title:'供应商名称',align:'center'},
// 	        {field:'rklxId',title:'入库类型id',align:'center',hidden:true},
// 	        {field:'rklxmc',title:'入库类型',align:'center'},
// 	        {field:'fkId',title:'分库 id',align:'center',hidden:true},
// 	        {field:'fkmc',title:'分库名称',align:'center'},
// 	        {field:'hjje',title:'金额',align:'center'},
// 	        {field:'bz',title:'备注',align:'center',
//         		formatter: function(value){
//         			return lnyw.memo(value, 15);
//         		}},
//         	{field:'xsthlsh',title:'库房入库流水号',align:'center',
//            		formatter: function(value){
//            			return lnyw.memo(value, 15);
//            		}},
//         	{field:'isCancel',title:'*状态',align:'center',sortable:true,
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
// 			{field:'cancelTime',title:'冲减时间',align:'center'},
//         	{field:'cancelkfcklsh',title:'原业务入库流水号',align:'center'},
// 	    ]],
// 	    toolbar:'#jxc_xsth_kfckTb',
// 	});
// 	lnyw.toolbar(2, xsth_kfckDg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', xsth_did);
	
	
// 	xsth_kfckDg.datagrid({
//         view: detailview,
//         detailFormatter:function(index,row){
//             return '<div style="padding:2px"><table id="xsth-kfck-ddv-' + index + '"></table></div>';
//         },
//         onExpandRow: function(index,row){
//             $('#xsth-kfck-ddv-'+index).datagrid({
//                 url:'${pageContext.request.contextPath}/jxc/kfckAction!detDatagrid.action',
//                 fitColumns:true,
//                 singleSelect:true,
//                 rownumbers:true,
//                 loadMsg:'',
//                 height:'auto',
//                 queryParams: {
//         			kfcklsh: row.kfcklsh,
//         		},
//                 columns:[[
//                     {field:'spbh',title:'商品编号',width:200,align:'center'},
//                     {field:'spmc',title:'名称',width:100,align:'center'},
//                     {field:'spcd',title:'产地',width:100,align:'center'},
//                     {field:'sppp',title:'品牌',width:100,align:'center'},
//                     {field:'spbz',title:'包装',width:100,align:'center'},
//                     {field:'zjldwmc',title:'单位1',width:100,align:'center'},
//                     {field:'zdwsl',title:'数量1',width:100,align:'center'},
//                     {field:'zdwdj',title:'销售单价1',width:100,align:'center'},
//                     {field:'cjldwmc',title:'单位2',width:100,align:'center'},
//                     {field:'cdwsl',title:'数量2',width:100,align:'center'},
//                     {field:'cdwdj',title:'销售单价2',width:100,align:'center'},
//                     {field:'spje',title:'金额',width:100,align:'center'},
//                 ]],
//                 onResize:function(){
//                 	xsth_kfckDg.datagrid('fixDetailRowHeight',index);
//                 },
//                 onLoadSuccess:function(){
//                     setTimeout(function(){
//                     	xsth_kfckDg.datagrid('fixDetailRowHeight',index);
//                     },0);
//                 }
//             });
//             xsth_kfckDg.datagrid('fixDetailRowHeight',index);
//         }
//     });
	
	xsth_xskpDg = $('#jxc_xsth_xskpDg').datagrid({
		fit : true,
	    border : false,
// 	    singleSelect : true,
	    remoteSort: false,
	    fitColumns: true,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
		frozenColumns:[[
		]],
		columns:[[
			{field:'id',title:'销售开票DetId',align:'center',checkbox:true},
			{field:'xskplsh',title:'流水号',align:'center'},
			{field:'createTime',title:'时间',align:'center'},
			{field:'createName',title:'创建人',align:'center'},
			{field:'khbh',title:'供应商编号',align:'center',hidden:true},
			{field:'khmc',title:'供应商名称',align:'center'},
			{field:'ywyId',title:'业务员id',align:'center',hidden:true},
			{field:'ywymc',title:'业务员',align:'center'},
			{field:'ckId',title:'仓库id',align:'center',hidden:true},
			{field:'ckmc',title:'仓库',align:'center'},
// 			{field:'fhId',title:'分户id',align:'center',hidden:true},
// 			{field:'fhmc',title:'分户',align:'center'},
			{field:'toFp',title:'*开票',align:'center',sortable:true,
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
			{field:'spbh',title:'商品编号',align:'center'},
			{field:'spmc',title:'名称',align:'center'},
			{field:'spcd',title:'产地',align:'center'},
			{field:'sppp',title:'品牌',align:'center'},
			{field:'spbz',title:'包装',align:'center'},
			{field:'zjldwmc',title:'单位1',align:'center'},
			{field:'zdwsl',title:'数量1',align:'center'},
			{field:'thsl',title:'已提数量',align:'center',
				formatter: function(value){
					return value == 0 ? '' : value;
				},
				styler:function(){
					return 'color:red;';
				}},
			{field:'cjldwmc',title:'单位2',align:'center'},
			{field:'cdwsl',title:'数量2',align:'center'},
			{field:'bookmc',title:'书名',align:'center',
				formatter: function(value){
					return lnyw.memo(value, 15);
				}},
			{field:'bz',title:'备注',align:'center',
				formatter: function(value){
					return lnyw.memo(value, 15);
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
	    ]],
	    toolbar:'#jxc_xsth_xskpTb',
	});
	lnyw.toolbar(2, xsth_xskpDg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', xsth_did);
	
	
// 	xsth_xskpDg.datagrid({
//         view: detailview,
//         detailFormatter:function(index,row){
//             return '<div style="padding:2px"><table id="xsth-xskp-ddv-' + index + '"></table></div>';
//         },
//         onExpandRow: function(index,row){
//             $('#xsth-xskp-ddv-'+index).datagrid({
//                 url:'${pageContext.request.contextPath}/jxc/xskpAction!detDatagrid.action',
//                 fitColumns:true,
//                 singleSelect:true,
//                 rownumbers:true,
//                 loadMsg:'',
//                 height:'auto',
//                 queryParams: {
//         			xskplsh: row.xskplsh,
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
//                     {field:'spje',title:'金额',width:100,align:'center',
//         	        	formatter: function(value){
//         	        		return lnyw.formatNumberRgx(value);
//         	        	}},
//                 ]],
//                 onResize:function(){
//                 	xsth_xskpDg.datagrid('fixDetailRowHeight',index);
//                 },
//                 onLoadSuccess:function(){
//                     setTimeout(function(){
//                     	xsth_xskpDg.datagrid('fixDetailRowHeight',index);
//                     },0);
//                 }
//             });
//             xsth_xskpDg.datagrid('fixDetailRowHeight',index);
//         }
//     });
	//选中列表标签后，装载数据
	xsth_tabs = $('#jxc_xsth_tabs').tabs({
		onSelect: function(title, index){
			if(index == 1){
				xsth_dg.datagrid({
					url: '${pageContext.request.contextPath}/jxc/xsthAction!datagrid.action',
					queryParams:{
						bmbh: xsth_did,
						createTime: countXsth == 0 ? undefined : $('input[name=createTimeXsth]').val(),
						search: countXsth == 0 ? undefined : $('input[name=searchXsth]').val(),
					}
				});
				countXsth++;
			}
// 			if(index == 2){
// 				xsth_kfckDg.datagrid({
// 					url: '${pageContext.request.contextPath}/jxc/kfckAction!datagrid.action',
// 					queryParams: {
// 						bmbh: xsth_did,
// 						fromOther: 'fromXsth'
// 						},
// 				});
// 			}
			if(index == 2){
				xsth_xskpDg.datagrid({
					url: '${pageContext.request.contextPath}/jxc/xskpAction!datagridDet.action',
					queryParams: {
						bmbh: xsth_did,
						fromOther: 'fromXsth',
						createTime: countXskpInXsth == 0 ? undefined : $('input[name=createTimeXskpInXsth]').val(),
						search: countXskpInXsth == 0 ? undefined : $('input[name=searchXskpInXsth]').val(),
						},
				});
			}
		},
	});
	
	//初始化商品编辑表格
	xsth_spdg = $('#jxc_xsth_spdg').datagrid({
		fit : true,
	    border : false,
	    singleSelect : true,
	    fitColumns: true,
	    autoRowHeight: false,
	    showFooter: true,
		columns:[[
	        {field:'spbh',title:'商品编号',width:25,align:'center',editor:'text'},
	        {field:'spmc',title:'商品名称',width:100,align:'center',editor:'textRead'},
	        {field:'spcd',title:'商品产地',width:20,align:'center',editor:'textRead'},
	        {field:'sppp',title:'商品品牌',width:20,align:'center',editor:'text',hidden:true},
	        {field:'spbz',title:'商品包装',width:20,align:'center',editor:'text',hidden:true},
	        {field:'zjldwmc',title:'单位1',width:25,align:'center',editor:'textRead'},
	        {field:'kpsl',title:'发票数量',width:25,align:'center',editor:'textRead'},
	        {field:'thsl',title:'已提货数量',width:25,align:'center',editor:'textRead'},
	        {field:'zdwsl',title:'数量1',width:25,align:'center',
	        	editor:{
	        		type:'numberbox',
	        		options:{
	        			//精度
	        			precision: LENGTH_SL,
	        		}}},
	        {field:'zdwdj',title:'单价1(含税)',width:25,align:'center',
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
	        {field:'cdwdj',title:'单价2(含税)',width:25,align:'center',
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
	
	//$('#jxc_xsth_tabs a.tabs-inner').css('height','100px');
	//$('#jxc_xsth_tabs span.tabs-title').css('white-space','normal');
	
	
	//初始化创建时间
	$('#createDate').html(moment().format('YYYY年MM月DD日'));
	
	//初始化仓库列表
	jxc_xsth_ckCombo = lnyw.initCombo($("#jxc_xsth_ckId"), 'id', 'ckmc', '${pageContext.request.contextPath}/jxc/ckAction!listCk.action?depId=' + xsth_did);
	
	//初始化业务员列表
	jxc_xsth_ywyCombo = lnyw.initCombo($("#jxc_xsth_ywyId"), 'id', 'realName', '${pageContext.request.contextPath}/admin/userAction!listYwys.action?did=' + xsth_did);
	
	//初始化付款方式列表
	jxc_xsth_jsfsCombo = lnyw.initCombo($("#jxc_xsth_jsfsId"), 'id', 'jsmc', '${pageContext.request.contextPath}/jxc/jsfsAction!listJsfs.action');
	
	//初始化分户列表
	$('input[name=isFh]').click(function(){
		if($(this).is(':checked')){
			$('.isFh').css('display','table-cell');
			$('.isFhth').css('display','inline');
			//初始化分户列表
			if(jxc_xsth_fhCombo == undefined){
				jxc_xsth_fhCombo = lnyw.initCombo($("#jxc_xsth_fhId"), 'id', 'fhmc', '${pageContext.request.contextPath}/jxc/fhAction!listFhs.action?depId=' + xsth_did);
			}
			jxc_xsth_fhCombo.combobox('selectedIndex', 0);
		}else{
			$('.isFh').css('display','none');
			$('.isFhth').css('display','none');
		}
	});
	
	$('input[name=thfs]').click(function(){
 		if($('input#thfs_sh').is(':checked')){
			$('.isSh').css('display','table-cell');
			$('.isZt').css('display','none');
		}else{
			$('.isSh').css('display','none');
			$('.isZt').css('display','table-cell');
		}
	});
	
 	$('input[name=khmc]').change(function(){
// 		if($('input[name=isSx]').is(':checked')){
// 			checkKh();
// 		}
 		loadKh($('input[name=khbh]').val().trim());
 	});
	
	//初始化信息
	init();
});

//以下为商品列表处理代码
function init(){
	//清空列表，添加一个空行
	xsth_spdg.datagrid({
		data: [{}],
	});
	
	//清空全部字段
	$('input').val('');
	$('input:checkbox').removeAttr('checked');
	$('input:checkbox').removeProp('checked');
	$('input:checkbox[name=toFp]').prop('checked', 'checked');
	
	$('input#thfs_zt').attr('ckecked', 'checked');
	$('.isSh').css('display','none');
	$('.isFh').css('display','none');
	$('.isFhth').css('display','none');
	
	if(jxc.showFh(xsth_did)){
		$('.fh').css('display', 'inline');
	}else{
		$('.fh').css('display', 'none');
	}
	
	if(jxc.showBookmc(xsth_did)){
		$('.bookmc').css('display', 'table-row');
	}else{
		$('.bookmc').css('display', 'none');
	}
	
	//收回商品库存信息
	$('#jxc_xsth_layout').layout('collapse', 'east');
	jxc.spInfo($('#jxc_xsth_layout'), '');
	
	jxc_xsth_ckCombo.combobox('selectedIndex', 0);
	jxc_xsth_ywyCombo.combobox('selectedIndex', 0);
	jxc_xsth_jsfsCombo.combobox('setValue', JSFS_QK);
	
	//初始化流水号
	$.ajax({
		type: "POST",
		url: '${pageContext.request.contextPath}/jxc/lshAction!getLsh.action',
		data: {
			bmbh: xsth_did,
			lxbh: xsth_lx,
		},
		dataType: 'json',
		success: function(d){
			if(d.success){
				$('#xsthLsh').html(d.obj);
			}  
		},
	});
	
	//根据权限，动态加载功能按钮
	lnyw.toolbar(0, xsth_spdg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', xsth_did);
	
	//清空合计内容
	xsth_spdg.datagrid('reloadFooter',[{}]);
}

//判断行是否编辑完成
function rowOk(){
	if(editIndex == undefined){
		return true;
	}
	if(keyOk()){
// 		if($('input[name=isSx]').is(':checked')){
// 		if(!$('input[name=isFhth]').is(':checked') && jxc_xsth_jsfsCombo.combobox('getValue') == JSFS_QK){
// 			if(zslEditor.target.val() > 0 && zdjEditor.target.val() > 0 && spjeEditor.target.val() >0){
// 				return true;
// 			}
// 		}else{
			if(zslEditor.target.val() != 0){
				return true;
			}
// 		}
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
	if(rowIndex == xsth_spdg.datagrid('getRows').length - 1){
		xsth_spdg.datagrid('appendRow', {});
	}else{
		if(!isClick){
			return false;
		}
	}
	//将选中行标识为编辑行
	editIndex = rowIndex;
	//选择编辑行，并进行入编辑状态
	xsth_spdg.datagrid('selectRow', editIndex)
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
				xsth_spdg.datagrid('endEdit', editIndex);
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
    xsth_spdg.datagrid('cancelEdit', editIndex)
            .datagrid('deleteRow', editIndex);
    editIndex = undefined;
    updateFooter();
    jxc.hideKc('#jxc_xsth_layout');
}

//保存行
function accept(){
    if (rowOk()){
    	xsth_spdg.datagrid('acceptChanges');
    	jxc.hideKc('#jxc_xsth_layout');
    }
}

//取消编辑行
function cancelAll(){
	xsth_spdg.datagrid('rejectChanges');
    editIndex = undefined;
    updateFooter();
    jxc.hideKc('#jxc_xsth_layout');
}

//提交数据到后台
function saveAll(){
	
	var msg = formValid();
	if(msg == ''){
		//编辑行是否完成
		if(rowOk()){
			xsth_spdg.datagrid('endEdit', editIndex);
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
	
	var rows = xsth_spdg.datagrid('getRows');
	if(rows.length == 1){
		$.messager.alert('提示', '未添加商品数据,请继续操作！', 'error');
		return false;
	}
	var footerRows = xsth_spdg.datagrid('getFooterRows');
	var effectRow = new Object();
	if(NEED_AUDIT == "1" && jxc_xsth_jsfsCombo.combobox('getValue') == JSFS_QK){
// 			var payTime = undefined;
// 			$.ajax({
// 				url: '${pageContext.request.contextPath}/jxc/xskpAction!getXskpNoHkFirst.action',
// 				data: {
// 					bmbh: xsth_did,
// 					khbh: $('input[name=khbh]').val(),
// 					ywyId: jxc_xsth_ywyCombo.combobox('getValue'),
// 					jsfsId: JSFS_QK,
// 				},
// 				cache: false,
// 				async: false,
// 				dataType: 'json',
// 				success: function(data){
// 					payTime = data.obj.payTime;
// 				}
// 			});
			
// 			if(moment().diff(payTime) > 0){
// 				effectRow['needAudit'] = jxc.auditLevel(xsth_did)['second'];
// 			}else{
// 				effectRow['needAudit'] = jxc.auditLevel(xsth_did)['first'];
// 			}
		effectRow['needAudit'] = jxc.getAuditLevel(
				xsth_did, 
				$('input[name=khbh]').val(),
				jxc_xsth_ywyCombo.combobox('getValue'),
				JSFS_QK);
	}else{
		effectRow['needAudit'] = "0";
	}
	//将表头内容传入后台
// 	effectRow['isSx'] = $('input[name=isSx]').is(':checked') ? '1' : '0';
	effectRow['isSx'] = '0';
	effectRow['isZs'] = $('input[name=isZs]').is(':checked') ? '1' : '0';
	effectRow['toFp'] = $('input[name=toFp]').is(':checked') ? '1' : '0';
	if($('input[name=isFh]').is(':checked')){
		effectRow['isFh'] = '1';
		effectRow['fhId'] = jxc_xsth_fhCombo.combobox('getValue');
		effectRow['fhmc'] = jxc_xsth_fhCombo.combobox('getText');
	}else{
		effectRow['isFh'] = '0';
	}
	effectRow['isFhth'] = $('input[name=isFhth]').is(':checked') ? '1' : '0';
	
	if($('input[name=xskpDetIds]').val().trim().length > 0){
		effectRow['isLs'] = '0';
	}else{
		effectRow['isLs'] = $('input[name=isFhth]').is(':checked') ? '0' : '1';
	}
	
	effectRow['khbh'] = $('input[name=khbh]').val();
	effectRow['khmc'] = $('input[name=khmc]').val();
	effectRow['ckId'] = jxc_xsth_ckCombo.combobox('getValue');
	effectRow['ckmc'] = jxc_xsth_ckCombo.combobox('getText');
	effectRow['ywyId'] = jxc_xsth_ywyCombo.combobox('getValue');
	effectRow['ywymc'] = jxc_xsth_ywyCombo.combobox('getText');
	effectRow['jsfsId'] = jxc_xsth_jsfsCombo.combobox('getValue');
	effectRow['jsfsmc'] = jxc_xsth_jsfsCombo.combobox('getText');
	
	if($('input#thfs_sh').is(':checked')){
		effectRow['thfs'] = '0';
		effectRow['shdz'] = $('input[name=shdz]').val();
	}else{
		effectRow['thfs'] = '1';
		effectRow['ch'] = $('input[name=ch]').val();
		effectRow['thr'] = $('input[name=thr]').val();
	}
	effectRow['hjje'] = lnyw.delcommafy(footerRows[0]['spje']);
	effectRow['hjsl'] = lnyw.delcommafy(footerRows[0]['cdwsl']);
	
	effectRow['bookmc'] = $('input[name=bookmc]').val();
	effectRow['bz'] = $('input[name=bz]').val();
	effectRow['xskpDetIds'] = $('input[name=xskpDetIds]').val();
	
	effectRow['bmbh'] = xsth_did;
	effectRow['lxbh'] = xsth_lx;
	effectRow['menuId'] = xsth_menuId;
	
	//将表格中的数据去掉最后一个空行后，转换为json格式
	effectRow['datagrid'] = JSON.stringify(rows.slice(0, rows.length - 1));
	//提交到action
	$.ajax({
		type: "POST",
		url: '${pageContext.request.contextPath}/jxc/xsthAction!save.action',
		data: effectRow,
		dataType: 'json',
		success: function(rsp){
			if(rsp.success){
		    	$.messager.show({
					title : '提示',
					msg : '提交成功！'
				});
		    	init();
		    	$.messager.confirm('请确认', '是否打印销售提货单？', function(r) {
					if (r) {
						var url = lnyw.bp() + '/jxc/xsthAction!printXsth.action?xsthlsh=' + rsp.obj.xsthlsh + "&bmbh=" + xsth_did;
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
	
	//加载字段
	var editors = xsth_spdg.datagrid('getEditors', editIndex);
    spbhEditor = editors[0];
    spmcEditor = editors[1];
    spcdEditor = editors[2];
    spppEditor = editors[3];
    spbzEditor = editors[4];
    zjldwEditor = editors[5];
    kpslEditor = editors[6];
    thslEditor = editors[7];
    zslEditor = editors[8];
    zdjEditor = editors[9];
    cjldwEditor = editors[10];
    cslEditor = editors[11];
    cdjEditor = editors[12];
    spjeEditor = editors[13];
    zhxsEditor = editors[14];
    zjldwIdEditor = editors[15];
    cjldwIdEditor = editors[16];
    
    if($(spbhEditor.target).val() != ''){
    	var fhValue = '';
    	if($('input[name=isFh]').is(':checked')){
    		fhValue = jxc_xsth_fhCombo.combobox('getValue');
    	}
    	jxc.spInfo($('#jxc_xsth_layout'), '1', $(spppEditor.target).val(), $(spbzEditor.target).val());
    	jxc.showKc('#jxc_xsth_layout', 
    			'${pageContext.request.contextPath}/jxc/xsthAction!getSpkc.action', 
    			{
    				bmbh : xsth_did, 
    				ckId : jxc_xsth_ckCombo.combobox('getValue'),
    				fhId : fhValue,
    				spbh : $(spbhEditor.target).val(),
    			});
    }else{
    	jxc.spInfo($('#jxc_xsth_layout'), '');
    	jxc.hideKc('#jxc_xsth_layout');
   	}
    
	//loadEditor();
	//处理编辑行的换行事件
	$('.datagrid-row-editing').on('keydown','input', function(event){
		//监听按下向下箭头
		if(event.keyCode == 40){
			var rowIndex = editIndex;
			if(rowOk()){
				xsth_spdg.datagrid('endEdit', editIndex);
				enterEdit(rowIndex + 1, false);
			}else{
				if(!keyOk()){
					removeit();
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
    						depId: xsth_did
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
    				xsth_did,
    				'${pageContext.request.contextPath}/jxc/spQuery.jsp',
    				'${pageContext.request.contextPath}/jxc/spAction!spDg.action',
    				zslEditor,
    				true);
    		return false;
    	}
    });
    
    //输入主单位数量后，计算金额
    zslEditor.target.bind('keyup', function(event){
    	if(event.keyCode == 9){
     		return false;
     	}
    	var wtsl = 0;
    	if(Number($(kpslEditor.target).val()) > 0){
    		wtsl = (Number($(kpslEditor.target).val()) - Number($(thslEditor.target).val())).toFixed(LENGTH_SL);
    		if(Number($(zslEditor.target).val()) > wtsl){
    			$.messager.alert("提示", "提货数量不能大于未提货数量，请重新输入！");
        		$(zslEditor.target).numberbox('setValue', 0);
        		zslEditor.target.focus();
        		return false;
    		}
    	}
    	
    	if($(zhxsEditor.target).val() != 0){
    		$(cslEditor.target).numberbox('setValue', $(zslEditor.target).val() / $(zhxsEditor.target).val());
    	}
    	calForZ();
    }).bind('keydown', function(event){
     	if(event.keyCode == 9){
     		zdjEditor.target.focus();
     		return false;
     	}
    });
    
        
    zdjEditor.target.bind('keyup', function(event){
    	if(event.keyCode == 9){
     		return false;
     	}
    	if($(zhxsEditor.target).val() != 0){
    		$(cdjEditor.target).numberbox('setValue', $(zdjEditor.target).val() * $(zhxsEditor.target).val());
    	}
    	calForZ();
    }).bind('keydown', function(event){
     	if(event.keyCode == 9){
     		cslEditor.target.focus();
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
    
    cslEditor.target.bind('keyup', function(event){
    	if(event.keyCode == 9){
    		return false;
    	}
    	//if((event.keyCode >= 48 && event.keyCode <= 57) || event.keyCode == 190 ){
    		if($(zhxsEditor.target).val() != 0){
    			$(zslEditor.target).numberbox('setValue', $(cslEditor.target).val() * $(zhxsEditor.target).val());
    		}
    		calForC();
     	//}else{
    		//return false;
    	//}
    }).bind('keydown', function(event){
     	if(event.keyCode == 9){
     		cdjEditor.target.focus();
     		return false;
     	}
    });
    
    
    
        
    //输入次单位单价后，计算金额
    cdjEditor.target.bind('keyup', function(event){
    	if(event.keyCode == 9){
    		return false;
    	}
    	if($(zhxsEditor.target).val() != 0){
    		$(zdjEditor.target).numberbox('setValue', $(cdjEditor.target).val() / $(zhxsEditor.target).val());
    	}
    	calForC();
    }).bind('keydown', function(event){
    	if(event.keyCode == 40){
     		spjeEditor.target.focus();
     	}
    });
    
  	//输入金额计算单价
    spjeEditor.target.bind('keyup', function(event){
    	if(event.keyCode == 9){
     		return false;
     	}
     	if($(zhxsEditor.target).val() != 0){
     		$(cdjEditor.target).numberbox('setValue', $(spjeEditor.target).val() / $(cslEditor.target).val());
     	}
     	$(zdjEditor.target).numberbox('setValue', $(spjeEditor.target).val() / $(zslEditor.target).val());
     	
//      	calculate();
    	updateFooter();
    }).bind('keydown', function(event){
    	if(event.keyCode == 40){
     		spjeEditor.target.focus();
     	}
    });
    
      	
    //计算金额
    function calForZ(){
    	var spje = 0.0000;
//     	if(cslEditor.target.val() != 0 && cdjEditor.target.val() != 0){
//     		spje = cslEditor.target.val() * cdjEditor.target.val();
//     	}else{
        	spje = zslEditor.target.val() * zdjEditor.target.val();
//     	}
        $(spjeEditor.target).numberbox('setValue',spje);
        //更新汇总列
        updateFooter();
    }
    
    function calForC(){
    	var spje = 0.0000;
//     	if(cslEditor.target.val() != 0 && cdjEditor.target.val() != 0){
    		spje = cslEditor.target.val() * cdjEditor.target.val();
//     	}else{
//         	spje = zslEditor.target.val() * zdjEditor.target.val();
//     	}
        $(spjeEditor.target).numberbox('setValue',spje);
        //更新汇总列
        updateFooter();
    }
  	
    //初始进入编辑状态时，使用商品编号获得焦点
    spbhEditor.target.focus();
}
//求和
function updateFooter(){
 	var rows = xsth_spdg.datagrid('getRows');
	var spmc_footer = '合计';
	var hjje = 0.000000;
	var hjsl = 0.000;
	$.each(rows, function(){
		var index = xsth_spdg.datagrid('getRowIndex', this);
		if(index < rows.length - 1){
			if(editIndex == index){
				hjje += Number(spjeEditor.target.val());
				hjsl += Number(cslEditor.target.val());
			}else{
				hjje += Number(this.spje);
				hjsl += Number(this.cdwsl);
			}
		}
 		
 	});
	xsth_spdg.datagrid('reloadFooter', [{
		spmc : spmc_footer,
		spje : lnyw.formatNumberRgx(hjje.toFixed(LENGTH_JE)),
		cdwsl : hjsl.toFixed(LENGTH_SL),
		}]
	);
}

//验证记录是否已存在
function existKey(value, rowIndex){
	var keys = xsth_spdg.datagrid('getRows');
	var exist = false;
	if(keys.length > 2){
		$.each(keys, function(){
			var index = xsth_spdg.datagrid('getRowIndex', this);
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
// 		message += '客户信息<br>';
// 	}
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
	
	
   	var fhValue = '';
   	if($('input[name=isFh]').is(':checked')){
   		fhValue = jxc_xsth_fhCombo.combobox('getValue');
   	}
   	jxc.spInfo($('#jxc_xsth_layout'), '1', rowData.sppp, rowData.spbz);
   	jxc.showKc('#jxc_xsth_layout', 
   			'${pageContext.request.contextPath}/jxc/xsthAction!getSpkc.action', 
   			{
   				bmbh : xsth_did, 
   				ckId : jxc_xsth_ckCombo.combobox('getValue'),
   				fhId : fhValue,
   				spbh : $(spbhEditor.target).val(),
   			});
}

function checkKh(){
	$.ajax({
		url:'${pageContext.request.contextPath}/jxc/khAction!checkKh.action',
		async: false,
		data:{
			khbh: $('input[name=khbh]').val(),
			depId: xsth_did,
		},
		dataType:'json',
		success:function(data){
			if(!data.success){
				$.messager.alert('提示', data.msg, 'error');
				$('input[name=khbh]').val('');
				$('input[name=khmc]').val('');
				$('input[name=khbh]').focus();
			}
		}
	});
}

//根据客户编码获取客户详细信息
function loadKh(khbh){
	$.ajax({
		url:'${pageContext.request.contextPath}/jxc/khAction!loadKh.action',
		async: false,
		cache: false,
		context:this,
		data:{
			khbh: khbh,
			depId: xsth_did,
		},
		dataType:'json',
		success:function(data){
			if(data.success){
				//设置信息字段值
				$('input[name=khmc]').val(data.obj.khmc);
// 				$('input[name=sh]').val(data.obj.sh);
// 				$('input[name=khh]').val(data.obj.khh);
// 				$('input[name=dzdh]').val(data.obj.dzdh);
				jxc_xsth_ywyCombo.combobox('setValue', data.obj.ywyId);
// 				if(data.obj.isSx == '1'){
// 					$('input[name=isSx]').prop('checked', 'ckecked');
// 				}
			}else{
				$.messager.alert('提示', '供应商信息不存在！', 'error');
			}
		}
	});
}

//在前台页面响应输入事件，按ESC键弹出客户列表或直接输入客户编码
function khLoad(){
	switch(event.keyCode){
	case 27:
		//是否授信客户，不再进行判断处理，2014-06-22
// 		var isSx = '0';
// 		if($('input[name=isSx]').is(':checked')){
// 			isSx='1';
// 		}
		jxc.query('客户检索', $('input[name=khbh]'), $('input[name=khmc]'), 
				'${pageContext.request.contextPath}/jxc/query.jsp',
				'${pageContext.request.contextPath}/jxc/khAction!khDg.action?depId=' + xsth_did);
// 				'${pageContext.request.contextPath}/jxc/khAction!khDg.action?isSx=' + isSx + "&depId=" + xsth_did);
		break;
	case 9:
		break;
	default:
		if($('input[name=khbh]').val().trim().length == 0){
			$('input[name=khmc]').val('');
		}
		if($('input[name=khbh]').val().trim().length == 8){
			loadKh($('input[name=khbh]').val().trim());
// 			$.ajax({
// 				url:'${pageContext.request.contextPath}/jxc/khAction!loadKh.action',
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
// 						$('input[name=khmc]').change();
// 						console.info("ywyId:" + data.obj.ywyId);
// 						jxc_xsth_ywyCombo.combobox('setValue', data.obj.ywyId);
// 						if(data.obj.isSx == '1'){
// 							$('input[name=isSx]').prop('checked', 'ckecked');
// 						}
// 						//$('#jxc_xsth_ywyId').focus();
// 						//rowKh = data;
// 					}else{
// 						$.messager.alert('提示', '客户信息不存在！', 'error');
// 					}
// 				}
// 			});
		}
		break;
	}
}
//////////////////////////////////////////////以上为商品列表处理代码

//////////////////////////////////////////////以下为销售提货列表处理代码
function cancelXsth(){
	var row = xsth_dg.datagrid('getSelected');
	if (row != undefined) {
		if(row.isCancel != '1'){
			if(row.locked == '0'){
				if(row.fromFp == '1' || row.xskplsh == undefined || row.xskplsh == ''){
					//if(row.isHk != '1'){
						if(row.isTh != '1'){
							$.messager.confirm('请确认', '您要取消选中的销售提货单？', function(r) {
								if (r) {
									$.ajax({
										url : '${pageContext.request.contextPath}/jxc/xsthAction!cancelXsth.action',
										data : {
											xsthlsh : row.xsthlsh,
											bmbh : xsth_did,
											menuId : xsth_menuId,
											lxbh: xsth_lx,
										},
										dataType : 'json',
										success : function(d) {
											xsth_dg.datagrid('load');
											xsth_dg.datagrid('unselectAll');
											$.messager.show({
												title : '提示',
												msg : d.msg
											});
										}
									});
								}
							});
						}else{
							$.messager.alert('警告', '选中的销售提货记录已经出库，不能取消！',  'warning');
						}
	// 				}else{
	// 					$.messager.alert('警告', '选中的销售提货记录已经还款，不能取消！',  'warning');
	// 				}
				}else{
					$.messager.alert('警告', '选中的销售提货记录已开票，不能取消！',  'warning');
				}
			}else{
				$.messager.alert('警告', '选中的销售提货记录已被库房锁定，请重新选择！',  'warning');
			}
		}else{
			$.messager.alert('警告', '选中的销售提货记录已被取消，请重新选择！',  'warning');
		}
	}else{
		$.messager.alert('警告', '请选择最少一条记录进行操作！',  'warning');
	}
}

function printXsth(){
	var row = xsth_dg.datagrid('getSelected');
	if (row != undefined) {
		$.messager.confirm('请确认', '是否打印销售提货单？', function(r) {
			if (r) {
				var url = lnyw.bp() + '/jxc/xsthAction!printXsth.action?xsthlsh=' + row.xsthlsh + "&bmbh=" + xsth_did;
				jxc.print(url, PREVIEW_REPORT, HIDE_PRINT_WINDOW);
			}
		});
	}else{
		$.messager.alert('警告', '请选择一条记录进行操作！',  'warning');
	}
}


// function completeXsth(){
// 	var row = xsth_dg.datagrid('getSelected');
// 	if (row != undefined) {
// 		if(row.cgjhlsh != undefined){
// 			if(row.isCancel != '1'){
// 				if(row.isCompleted != '1'){
// 					$.messager.confirm('请确认', '您要完成选中的采购需求单？', function(r) {
// 						if (r) {
// 							$.ajax({
// 								url : '${pageContext.request.contextPath}/jxc/xsthAction!complete.action',
// 								data : {
// 									xsthlsh : row.xsthlsh
// 								},
// 								dataType : 'json',
// 								success : function(d) {
// 									xsth_dg.datagrid('load');
// 									xsth_dg.datagrid('unselectAll');
// 									$.messager.show({
// 										title : '提示',
// 										msg : d.msg
// 									});
// 								}
// 							});
// 						}
// 					});
// 				}else{
// 					$.messager.alert('警告', '选中的采购需求记录已完成，请重新选择！',  'warning');
// 				}
// 			}else{
// 				$.messager.alert('警告', '选中的采购需求记录已被取消，请重新选择！',  'warning');
// 			}
// 		}else{
// 			$.messager.alert('警告', '选中的采购需求未进行计划，不能进行完成操作，请重新选择！',  'warning');
// 		}
// 	}else{
// 		$.messager.alert('警告', '请选择最少一条记录进行操作！',  'warning');
// 	}
// }




function searchXsth(){
	xsth_dg.datagrid('load',{
		bmbh: xsth_did,
		createTime: $('input[name=createTimeXsth]').val(),
		search: $('input[name=searchXsth]').val(),
	});
}


//////////////////////////////////////////////以上为销售提货列表处理代码

//////////////////////////////////////////////以下为库房出库列表处理代码


// function searchKfckInXsth(){
// 	xsth_kfckDg.datagrid('load',{
// 		bmbh: xsth_did,
// 		createTime: $('input[name=createTimeKfckInXsth]').val(),
// 		fromOther: 'fromXsth'
// 	});
// }

//////////////////////////////////////////////以上为库房出库列表处理代码

////////////////////////////////////////////以下为销售开票列表处理代码
function toXsth(){
	var rows = xsth_xskpDg.datagrid('getSelections');
	var xskpDetIds = [];
	if(rows.length > 0){
		var preRow = undefined;
		var flag = true;
	    $.each(rows, function(index){
	    	xskpDetIds.push(rows[index].id);
	    	if(index != 0){
	    		if(this.khbh != preRow.khbh){
	    			$.messager.alert('提示', '请选择同一客户的销售发票进行提货！', 'error');
					flag = false;
					//return false;
	    		}else{
	    			preRow = this;
	    		}
	    	}
	    	preRow = this;
	    });
	    if(flag){
			$.messager.confirm('请确认', '是否要将选中记录进行销售提货？', function(r) {
				if (r) {
					var xskpDetStr = xskpDetIds.join(',');
					$.ajax({
						url : '${pageContext.request.contextPath}/jxc/xskpAction!toXsth.action',
						data : {
							xskpDetIds: xskpDetStr 
						},
						dataType : 'json',
						success : function(d) {
							xsth_spdg.datagrid('loadData', d.rows);
							$('input[name=khbh]').val(rows[0].khbh);
							$('input[name=khmc]').val(rows[0].khmc);
							jxc_xsth_ckCombo.combobox("setValue", rows[0].ckId);
							jxc_xsth_ywyCombo.combobox("setValue", rows[0].ywyId);
							jxc_xsth_jsfsCombo.combobox("setValue", rows[0].jsfsId);
							updateFooter();
// 							$('input[name=xskplsh]').val(row.xskplsh);
							$('input[name=xskpDetIds]').val(xskpDetStr);
							xsth_tabs.tabs('select', 0);
						}
					});
				}
			});
	    }
	}else{
		$.messager.alert('警告', '请选择最少一条记录进行操作！',  'warning');
	}
}

function searchXskpInXsth(){
	xsth_xskpDg.datagrid('load',{
		bmbh: xsth_did,
		createTime: $('input[name=createTimeXskpInXsth]').val(),
		fromOther: 'fromXsth'
	});
}

////////////////////////////////////////////以上为销售开票列表处理代码

</script>

<!-- tabPosition:'left', headerWidth:'35' -->
<div id="jxc_xsth_tabs" class="easyui-tabs" data-options="fit:true, border:false," style="width:100%;height:100%;">
    <div title="新增记录" data-options="closable:false">
        <div id='jxc_xsth_layout' style="height:100%;width=100%">
			<div data-options="region:'north',title:'单据信息',border:false,collapsible:false" style="width:100%;height:173px">		
				<table class="tinfo">
					<tr>
						<td colspan="5">
<!-- 							授信客户<input type="checkbox" name="isSx">&nbsp;&nbsp;&nbsp; -->
							直送<input id="zsCheck" type="checkbox" name="isZs">&nbsp;&nbsp;&nbsp;
							发票<input id="fpCheck" type="checkbox" name="toFp">&nbsp;&nbsp;&nbsp;
							<span class="fh" style="display:none">
							分户<input type="checkbox" name="isFh">&nbsp;&nbsp;&nbsp;
							<span class="isFhth" style="display:none">分户提货<input type="checkbox" name="isFhth"></span>
							</span>
						</td>
						<th class="read">时间</th><td><div id="createDate" class="read"></div></td>
						<th class="read">单据号</th><td colspan="4"><div id="xsthLsh" class="read"></div></td>
					</tr>
					<tr>
						<th>客户编号</th><td><input name="khbh" class="easyui-validatebox"
							data-options="validType:['mustLength[8]','integer']" onkeyup="khLoad()" size="8"></td>
						<th>客户名称</th><td colspan="3"><input name="khmc" readonly="readonly" size="50"></td>
						<th>仓库</th><td><input id="jxc_xsth_ckId" name="ckId" size="10"></td>
						<th class="isFh" style="display:none">分户</th><td class="isFh" style="display:none"><input id="jxc_xsth_fhId" name="fhId" size="10"></td>
					</tr>
					<tr>
						<th>业务员</th><td><input id="jxc_xsth_ywyId" name="ywyId" size="8"></td>
						<th>结算方式</th><td><input id="jxc_xsth_jsfsId" name="jsfsId" size="8"></td>
						<td colspan="2" align="right">自提<input type="radio" name="thfs" id='thfs_zt' checked="checked" value="1">送货<input type="radio" name="thfs" id="thfs_sh" value="0"></td>
						<th class="isZt">车号</th><td class="isZt"><input name="ch" size="10"><th class="isZt">提货人</th><td class="isZt"><input name="thr" size="10"></td>
						<td class="isSh" style="display:none" colspan="2">送货地址<input name="shdz" size="20"></td>
					</tr>
					<tr class='bookmc'>
						<th>书名</th><td colspan="10"><input name="bookmc" type="text" style="width:71%"></td>
					</tr>
					<tr>
						<th>备注</th><td colspan="10"><input name="bz" style="width:90%"></td>
					</tr>
				</table>
				<input name="xskpDetIds" type="hidden">
			</div>
			<div data-options="region:'center',title:'商品信息',split:true" style="width:150px">		
				<table id='jxc_xsth_spdg'></table>
			</div>
			<div data-options="region:'east',title:'商品库存信息',split:true,collapsed:true" style="width:150px">
				<table id="show_spkc"></table>
			</div>
		</div>
    </div>
    <div title="销售提货列表" data-options="closable:false" >
    	<div id='jxc_xsth_dg'></div>
    </div>
<!--     <div title="库房出库列表" data-options="closable:false" > -->
<!--     	<div id='jxc_xsth_kfckDg'></div> -->
<!--     </div> -->
	<div title="销售开票列表" data-options="closable:false" >
		<div id='jxc_xsth_xskpDg'></div>
	</div>
</div>

<div id="jxc_xsth_tb" style="padding:3px;height:auto">
	请输入查询起始日期:<input type="text" name="createTimeXsth" class="easyui-datebox" data-options="value: moment().date(1).format('YYYY-MM-DD')" style="width:100px">
	输入流水号、客户、业务员、备注：<input type="text" name="searchXsth" style="width:100px">
	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchXsth();">查询</a>
</div>
<!-- <div id="jxc_xsth_kfckTb" style="padding:3px;height:auto"> -->
<!-- 	请输入查询起始日期:<input type="text" name="createTimeKfckInXsth" class="easyui-datebox" data-options="value: moment().date(1).format('YYYY-MM-DD')" style="width:100px"> -->
<!-- 	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchKfckInXsth();">查询</a> -->
<!-- </div> -->
<div id="jxc_xsth_xskpTb" style="padding:3px;height:auto">
	请输入查询起始日期:<input type="text" name="createTimeXskpInXsth" class="easyui-datebox" data-options="value: moment().date(1).format('YYYY-MM-DD')" style="width:100px">
	输入流水号、客户、业务员、备注：<input type="text" name="searchXskpInXsth" style="width:100px">
	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchXskpInXsth();">查询</a>
</div>


