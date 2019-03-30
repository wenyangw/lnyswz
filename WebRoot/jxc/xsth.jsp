<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<!-- 
2015.08.12
	增加直送打印销售合同
-->

<script type="text/javascript">
var xsth_dg;
var xsth_spdg;
var xsth_kfckDg;
var xsth_xskpDg;
var xsth_ywrkDg;
var xsth_fydDg;
var editIndex = undefined;
var xsth_did;
var xsth_lx;
var xsth_menuId;
var countXsth = 0;
var countXskpInXsth = 0;
var countYwrkInXsth = 0;
var countFydInXsth = 0;

var jxc_xsth_ckCombo;
var jxc_xsth_fhCombo;
var jxc_xsth_ywyCombo;
var jxc_xsth_jsfsCombo;

var xsthRow;
var fydRow;
var detDg;
var fydDetDg;

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
var cjldwEditor;
var cslEditor;
var cdjEditor;   
var spjeEditor;
var zhxsEditor;
var zjldwIdEditor;
var cjldwIdEditor;
var dwcbEditor;

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
	    idField: 'xsthlsh',
	    singleSelect : true,
// 	    fitColumns : true, 
	    remoteSort: false,
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
            {field:'needAuditXsjj',title:'销价审批',align:'center',hidden: (NEED_AUDIT_XSJJ === '0' || xsth_did === '01'),
                styler: function(value, rowData){
                    if(rowData.needAuditXsjj == rowData.isAuditXsjj){
                        return 'color:blue;';
                    }
                    if(rowData.isAuditXsjj == '9'){
                        return 'color:red;';
                    }
                }},
            {field:'isAuditXsjj',title:'进度',align:'center',hidden: (NEED_AUDIT_XSJJ === '0' || xsth_did === '01'),
                styler: function(value, rowData){
                    if(rowData.needAuditXsjj == rowData.isAuditXsjj){
                        return 'color:blue;';
                    }
                    if(rowData.isAuditXsjj == '9'){
                        return 'color:red;';
                    }
                }},
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
	        {field:'fhmc',title:'分户名称',align:'center',hidden: xsth_did != '04'},
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
	        {field:'ysfy',title:'运费',align:'center'},
	        {field:'yysfy',title:'原运费',align:'center'},
	        {field:'bookmc',title:'书名',align:'center',hidden: xsth_did != '04'
//         		formatter: function(value){
//         			return lnyw.memo(value, 25);
//         		}
	        },
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
        	{field:'shbz',title:'审批说明',align:'center'},
        	{field:'payDays',title:'付款天数',align:'center'},
	    ]],
	    toolbar:'#jxc_xsth_tb',
	});
	lnyw.toolbar(1, xsth_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', xsth_did);
	
	xsth_dg.datagrid({
        view: detailview,
        detailFormatter:function(index,row){
            return '<div style="padding:2px"><table id="xsth-ddv-' + index + '"></table></div>';
        },
        onExpandRow: function(index,row){
        	xsthRow = xsth_dg.datagrid('selectRow', index).datagrid('getSelected');
            detDg = $('#xsth-ddv-'+index).datagrid({
                url:'${pageContext.request.contextPath}/jxc/xsthAction!detDatagrid.action',
                fitColumns:false,
                //singleSelect:true,
                rownumbers:true,
                loadMsg:'',
                height:'auto',
                queryParams: {
        			xsthlsh: row.xsthlsh,
        		},
                columns:[[
					{field:'id',title:'id',width:20,align:'center',hidden:true},      
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
                    {field:'thsl',title:'原数量',width:100,align:'center',
                    	formatter : function(value) {
        					return value == 0 ? '' : value;
        				},

    					styler: function(value){
	    					if(value != 0){
	    						return 'color:purple;';
	    					}
                    	}},
                   	{field:'qrsl',title:'本次确认数量',width:100,align:'center',
                       	formatter : function(value) {
           					return value == 0 ? '' : value;
           				},

       					styler: function(value){
   	    					if(value != 0){
   	    						return 'color:green;';
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
       	        	{field:'completed',title:'完成',align:'center',
        	        		formatter : function(value) {
        						if (value == '1') {
        							return '是';
        						} else {
        							return '';
        						}
        					},},
       	        	{field:'cgjhlsh',title:'采购计划流水号',align:'center',
       	           		formatter: function(value){
       	           			return lnyw.memo(value, 15);
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
	
	
	xsth_xskpDg = $('#jxc_xsth_xskpDg').datagrid({
		fit : true,
	    border : false,
// 	    singleSelect : true,
	    remoteSort: false,
	    //fitColumns: true,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
		columns:[[
			{field:'id',title:'销售开票DetId',align:'center',checkbox:true},
			{field:'xskplsh',title:'流水号',align:'center'},
			{field:'createTime',title:'时间',align:'center'},
			{field:'createName',title:'创建人',align:'center'},
			{field:'khbh',title:'客户编号',align:'center',hidden:true},
			{field:'khmc',title:'客户名称',align:'center'},
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
	
	xsth_ywrkDg = $('#jxc_xsth_ywrkDg').datagrid({
		fit : true,
	    border : false,
	    remoteSort: false,
	    fitColumns: true,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
		columns:[[
			{field:'id',title:'业务入库DetId',align:'center',checkbox:true},
			{field:'ywrklsh',title:'流水号',align:'center'},
			{field:'createTime',title:'时间',align:'center'},
			{field:'createName',title:'创建人',align:'center'},
			{field:'gysbh',title:'供应商编号',align:'center',hidden:true},
			{field:'gysmc',title:'供应商名称',align:'center'},
			{field:'ckId',title:'仓库id',align:'center',hidden:true},
			{field:'ckmc',title:'仓库',align:'center'},
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
			{field:'bz',title:'备注',align:'center',
				formatter: function(value){
					return lnyw.memo(value, 15);
				}},
			{field:'rklxmc',title:'类型',align:'center'},
	    ]],
	    toolbar:'#jxc_xsth_ywrkTb',
	});
	lnyw.toolbar(3, xsth_ywrkDg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', xsth_did);
	
	xsth_fydDg = $('#jxc_xsth_fydDg').datagrid({
		fit : true,
	    border : false,
	    idField: 'fydlsh',
	    singleSelect : true,
// 	    fitColumns : true, 
	    remoteSort: false,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
		columns:[[
			{field:'status',title:'状态',align:'center',
				formatter : function(value) {
					if (value == '0') {
						return '新单';
					} else if (value == '1'){
						return '修改';
					} else {
						return '原始';
					}
				},
			},
			{field:'fydlsh',title:'流水号',align:'center',
				styler: function(value, rowData){
					if(rowData.isCancel == '1'){
						return 'color:red;';
					}
				}},
	        {field:'createTime',title:'*时间',align:'center',sortable:true,
				sorter: function(a,b){
					return moment(a).diff(moment(b), 'days');
				}},
			{field:'publisher',title:'出版社编号',align:'center',hidden:true},
	        {field:'publishercn',title:'出版社名称',align:'center'},
	        {field:'tzdbh',title:'印单编号',align:'center'},
	        {field:'bname',title:'书名',align:'center'},
	        {field:'tzrq',title:'制单日期',align:'center'},
	        {field:'zdr',title:'制单人',align:'center'},
	        {field:'zdfs',title:'装订方式',align:'center'},
	        {field:'zzfs',title:'装帧方式',align:'center'},
	        {field:'edited',title:'确认单价',align:'center',
	        	formatter : function(value) {
					if (value == '0') {
						return '否';
					} else if (value == '1'){
						return '是';
					}
				},
	        },
	        {field:'sended',title:'上传',align:'center',
	        	formatter : function(value) {
					if (value == '0') {
						return '否';
					} else if (value == '1'){
						return '是';
					}
				},
	        },
	        {field:'sendId',title:'上传Id',align:'center',hidden:true},
	        {field:'sendName',title:'上传人',align:'center'},
	        {field:'sendTime',title:'上传时间',align:'center'},
	        
	    ]],
	    toolbar:'#jxc_xsth_fydTb',
	});
	lnyw.toolbar(4, xsth_fydDg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', xsth_did);
	
	xsth_fydDg.datagrid({
        view: detailview,
        detailFormatter:function(index,row){
            return '<div style="padding:2px"><table id="xsth-fyd-ddv-' + index + '"></table></div>';
        },
        onExpandRow: function(index,row){
        	fydRow = xsth_fydDg.datagrid('selectRow', index).datagrid('getSelected');
            fydDetDg = $('#xsth-fyd-ddv-'+index).datagrid({
                url:'${pageContext.request.contextPath}/jxc/fydAction!detDatagrid.action',
                fitColumns:true,
                //singleSelect:true,
                rownumbers:true,
                loadMsg:'',
                height:'auto',
                queryParams: {
        			fydlsh: row.fydlsh,
        		},
                columns:[[
					{field:'id',title:'id',width:2,align:'center',hidden:true},      
                    {field:'sno',title:'序号',width:50,align:'center'},
                    {field:'zzmc',title:'纸张名称',width:200,align:'center'},
                    {field:'zzgg',title:'规格',width:100,align:'center'},
                    {field:'danjiadw',title:'单位',width:50,align:'center'},
                    {field:'sjyzl',title:'正用纸数',width:100,align:'center'},
                    {field:'jfl',title:'加放率',width:100,align:'center'},
                    {field:'jfyzl',title:'加放数',width:100,align:'center'},
                    {field:'zzhjl',title:'合计用纸',width:100,align:'center'},
                    {field:'danjia',title:'单价',width:100,align:'center'},
                    {field:'gongj',title:'金额',width:100,align:'center'},
                    {field:'cton',title:'吨数',width:100,align:'center'},
                ]],
                onResize:function(){
                	xsth_fydDg.datagrid('fixDetailRowHeight',index);
                },
                onLoadSuccess:function(){
                    setTimeout(function(){
                    	xsth_fydDg.datagrid('fixDetailRowHeight',index);
                    },0);
                }
            });
            xsth_fydDg.datagrid('fixDetailRowHeight',index);
        }
    });
	

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
				countXskpInXsth++;
			}
			if(index == 3){
				xsth_ywrkDg.datagrid({
					url: '${pageContext.request.contextPath}/jxc/ywrkAction!datagridDet.action',
					queryParams: {
						bmbh: xsth_did,
						fromOther: 'fromXsth',
						createTime: countYwrkInXsth == 0 ? undefined : $('input[name=createTimeYwrkInXsth]').val(),
						search: countYwrkInXsth == 0 ? undefined : $('input[name=searchYwrkInXsth]').val(),
						},
				});
				countYwrkInXsth++;
			}
			if(index == 4){
				xsth_fydDg.datagrid({
					url: '${pageContext.request.contextPath}/jxc/fydAction!datagrid.action',
					queryParams: {
						bmbh: xsth_did,
						fromOther: 'fromXsth',
						createTime: countFydInXsth == 0 ? undefined : $('input[name=createTimeFydInXsth]').val(),
						search: countFydInXsth == 0 ? undefined : $('input[name=searchFydInXsth]').val(),
						},
				});
				countFydInXsth++;
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
        				precision: 2
        			}}},
        	{field:'zhxs',title:'转换系数',width:25,align:'center',editor:'text', hidden:true},
        	{field:'zjldwId',title:'主单位id',width:25,align:'center',editor:'text', hidden:true},
        	{field:'cjldwId',title:'次单位id',width:25,align:'center',editor:'text', hidden:true},
        	{field:'dwcb',title:'成本',width:25,align:'center',editor:'text', hidden:true},
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
	jxc_xsth_ywyCombo.combobox({
		onSelect: function(rec){
			updateJsfs();
		}
	});
	
	//初始化付款方式列表
	jxc_xsth_jsfsCombo = lnyw.initCombo($("#jxc_xsth_jsfsId"), 'id', 'jsmc', '${pageContext.request.contextPath}/jxc/jsfsAction!listJsfs.action');
	
	//初始化分户列表
	$('input[name=isFh]').click(function(){
		if($(this).is(':checked')){
			$('.isFh').css('display','inline');
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
			$('.isSh').css('display','inline');
			if((xsth_did == '05' || xsth_did == '08') && !$('input#zsCheck').is(':checked')){
				$('.isShYf').css('display','inline');
			}
			$('.isZt').css('display','none');
		}else{
			$('.isSh').css('display','none');
			$('.isShYf').css('display','none');
			$('.isZt').css('display','inline');
			$('.isSh input').val('');
		}
	});
	
	$('input[name=jxc_xsth_isZs]').click(function(){
 		if($('input#zsCheck').is(':checked')){
 			jxc_xsth_ckCombo.combobox('setValue', jxc.getZfCk(xsth_did));
		}else{
			jxc_xsth_ckCombo.combobox('selectedIndex', 0);
		}
	});
	
 	$('input[name=xsth_khmc]').change(function(){
 		loadKh($('input[name=jxc_xsth_khbh]').val().trim());
 		jxc_xsth_ckCombo.combobox('setValue', ($('input#zsCheck').is(':checked') && jxc.cbs(xsth_did).indexOf($('input[name=jxc_xsth_khbh]').val()) < 0) ? jxc.getZfCk(xsth_did) : jxc.getCkByKhbh(xsth_did, $('input[name=jxc_xsth_khbh]').val()));
 		updateJsfs();
 		if($('input#thfs_sh').is(':checked')){
 			if(editIndex == 0){
 	 			updateYf(spbhEditor.target.val());
 	 		}else{
 				var rows = xsth_spdg.datagrid('getRows');
 				updateYf(rows[0].spbh);
 	 		}
 		}
 	});
 	
 	
 	
 	$('input[name=jxc_xsth_shdz]').keyup(function(event){
 		if(event.which == 27){
 			khShLoad();
 		}
 	});
 	
 	$('input[name=jxc_xsth_shdz]').change(function(){
 		if(editIndex == 0){
 			updateYf(spbhEditor.target.val());
 		}else{
			var rows = xsth_spdg.datagrid('getRows');
			updateYf(rows[0].spbh);
 		}
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
	//$('input').val('');
	$('#jxc_xsth_info input').val('');
	//$('input:checkbox').removeProp('checked');
	//$('input:checkbox').removeAttr('checked');
	$('input:checkbox').prop('checked', false);
	$('input:checkbox[name=toFp]').prop('checked', 'checked');
	
	$('input#thfs_zt').attr('checked', 'checked');
	$('.isSh').css('display','none');
	$('.isFh').css('display','none');
 	$('.isFhth').css('display','none');
	
	if(jxc.showFh(xsth_did)){
		$('.fh').css('display', 'inline');
	}else{
		$('.fh').css('display', 'none');
	}
	
	if(jxc.showBookmc(xsth_did)){
		$('.jxc_xsth_bookmc').css('display', 'block');
	}else{
		$('.jxc_xsth_bookmc').css('display', 'none');
	}
	
	//收回商品库存信息
	if(xsth_did != '04'){
		jxc.hideKc('#jxc_xsth_layout');
		//$('#jxc_xsth_layout').layout('collapse', 'east');
	}
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
                $('input#jxc_xsth_code').val(d.obj + moment().format('YYYYMMDDHHmmssSSS'));
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
//		if($('input[name=isSx]').is(':checked')){
//		if(!$('input[name=isFhth]').is(':checked') && jxc_xsth_jsfsCombo.combobox('getValue') == JSFS_QK){
		if(!$('input[name=isFhth]').is(':checked')){
	 		if(zslEditor.target.val() > 0 && zdjEditor.target.val() > 0 && spjeEditor.target.val() >0){
	 			return true;
	 		}
		}else{
			if(zslEditor.target.val() != 0){
				return true;
			}
		}
//		}else{
			//if(zslEditor.target.val() != 0){
			//	return true;
			//}
//		}
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
    if(xsth_did != '04'){
    	jxc.hideKc('#jxc_xsth_layout');
    }

}


//保存行
function accept(){
    if (rowOk()){
    	xsth_spdg.datagrid('acceptChanges');
    	if(xsth_did != '04'){
    		jxc.hideKc('#jxc_xsth_layout');
    	}
    }
}

//取消编辑行
function cancelAll(){
	xsth_spdg.datagrid('rejectChanges');
    editIndex = undefined;
    updateFooter();
    if(xsth_did != '04'){
    	jxc.hideKc('#jxc_xsth_layout');
    }
}


//提交数据到后台
function saveXsth(){
	
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
	
	//销售是否超过限额
	<%--var sxkh = jxc.isExcess('${pageContext.request.contextPath}', xsth_did, $('input[name=jxc_xsth_khbh]').val(), jxc_xsth_ywyCombo.combobox('getValue'));--%>
	<%--if(sxkh.isLocked == '1'){--%>
		<%--$.messager.alert('提示', '该客户已经被限制销售，请联系管理人员！', 'error');--%>
		<%--return false;--%>
	<%--}--%>

    var sxkh = undefined;

	if(jxc.notInExcludeKhs(xsth_did, $('input[name=jxc_xsth_khbh]').val()) && jxc_xsth_jsfsCombo.combobox('getValue') == JSFS_QK){
		if(!$('input[name=isFhth]').is(':checked')){
            sxkh = jxc.isExcess('${pageContext.request.contextPath}', xsth_did, $('input[name=jxc_xsth_khbh]').val(), jxc_xsth_ywyCombo.combobox('getValue'));
            if(sxkh.isLocked == '1'){
                $.messager.alert('提示', '该客户已经被限制销售，请联系管理人员！', 'error');
                return false;
            }
			if(sxkh.khlxId == '02'){
				if((Number(sxkh.qkje) + Number(lnyw.delcommafy(footerRows[0].spje))) > Number(sxkh.sxje) * Number(sxkh.limitPer)){
					$.messager.alert('提示', '客户欠款已超出限制比例，请回款后销售！', 'error');
					return false;
				}else{
					if((Number(sxkh.qkje) + Number(footerRows[0].spje)) > Number(sxkh.limitJe)){
						$.messager.alert('提示', '客户欠款已超出限制金额，请回款后销售！', 'error');
						return false;
					}
				}
			}
		}
	}

	var needAuditXsjj = 0;
	var spbhXsjj = new Array();
	var dwcb = 0;
    if(!$('input[name=isFhth]').is(':checked')) {
        $.each(rows, function () {
            dwcb = this.dwcb * (1 + SL);
            if (NEED_AUDIT_XSJJ == '1' && ('' + this.spbh).substring(0, 1) === '4' && ('' + this.spbh).substring(0, 3) !== '471') {
                if (this.zdwdj <= (1 + 0.01) * dwcb) {
                    needAuditXsjj = 3;
                    spbhXsjj.push('商品' + this.spbh + '的销售加价率低于1%');
                } else if (this.zdwdj <= (1 + 0.02) * dwcb) {
                    needAuditXsjj = needAuditXsjj <= 2 ? 2 : needAuditXsjj;
                    spbhXsjj.push('商品' + this.spbh + '的销售加价率低于2%');
                } else if (this.zdwdj <= (1 + 0.03) * dwcb) {
                    needAuditXsjj = needAuditXsjj <= 1 ? 1 : needAuditXsjj;
                    spbhXsjj.push('商品' + this.spbh + '的销售加价率低于3%');
                }
            } else {
                if (this.zdwdj <= dwcb) {
                    spbhXsjj.push(this.spbh);
                }
            }
        });
    }

	if(spbhXsjj.length > 0) {
		if(needAuditXsjj > 0){
			spbhXsjj.push('需要' + (needAuditXsjj == 3 ? '总经理' : (needAuditXsjj == 2 ? '副总经理' : '部门经理')) + '进行审批！');
		}else{
            spbhXsjj.push('销售价格低于成本，请确定后继续！');
        }
		$.messager.confirm('请确认', spbhXsjj.join(needAuditXsjj > 0 ? '<br>' : '，'), function (r) {
			if (r) {
				effectRow['needAuditXsjj'] = needAuditXsjj;
				save();
			}
		});
	}else {
		effectRow['needAuditXsjj'] = needAuditXsjj;
		save();
	}

	function save(){
		if(NEED_AUDIT == "1"
				&& jxc.notInExcludeKhs(xsth_did, $('input[name=jxc_xsth_khbh]').val()) 
				&& $('input[name=xskpDetIds]').val().trim().length == 0
				&& !$('input[name=isFhth]').is(':checked')){
			if(jxc_xsth_jsfsCombo.combobox('getValue') == JSFS_QK){
				var needA = jxc.getAuditLevel(
						'${pageContext.request.contextPath}/jxc/xskpAction!getLatestXs.action',
						xsth_did, 
						$('input[name=jxc_xsth_khbh]').val(),
						jxc_xsth_ywyCombo.combobox('getValue'),
						JSFS_QK);
				if(needA != undefined){
					effectRow['needAudit'] = needA;
					$.messager.alert('提示', '本次提货需进入' + needA + '级审批流程！', 'warning');
				}else{
					$.messager.alert('提示', '该客户授信已超期,禁止继续销售！', 'error');
					return false;
				}
			}else{
				effectRow['needAudit'] = "1";
				$.messager.alert('提示', '本次提货需进入1级审批流程！', 'warning');
			}
		}else{
			effectRow['needAudit'] = "0";
		}
		//将表头内容传入后台
	// 	effectRow['isSx'] = $('input[name=isSx]').is(':checked') ? '1' : '0';
		effectRow['isSx'] = '0';
		effectRow['isZs'] = $('input[name=jxc_xsth_isZs]').is(':checked') ? '1' : '0';
		effectRow['toFp'] = $('input[name=toFp]').is(':checked') ? '1' : '0';
		if($('input[name=isFh]').is(':checked')){
			effectRow['isFh'] = '1';
			effectRow['fhId'] = jxc_xsth_fhCombo.combobox('getValue');
			effectRow['fhmc'] = jxc_xsth_fhCombo.combobox('getText');
		}else{
			effectRow['isFh'] = '0';
		}
		//传入直送
		effectRow['fromOther'] = jxc.notInExcludeKhs(xsth_did, $('input[name=jxc_xsth_khbh]').val()) ? '' : 'cbs';
		effectRow['isFhth'] = $('input[name=isFhth]').is(':checked') ? '1' : '0';
		//effectRow['isFhth'] = '0';
		
		if($('input[name=xskpDetIds]').val().trim().length > 0){
			effectRow['isLs'] = '0';
		}else{
			effectRow['isLs'] = $('input[name=isFhth]').is(':checked') ? '0' : '1';
		}
		
		effectRow['khbh'] = $('input[name=jxc_xsth_khbh]').val();
		effectRow['khmc'] = $('input[name=xsth_khmc]').val();
		effectRow['ckId'] = jxc_xsth_ckCombo.combobox('getValue');
		effectRow['ckmc'] = jxc_xsth_ckCombo.combobox('getText');
		effectRow['ywyId'] = jxc_xsth_ywyCombo.combobox('getValue');
		effectRow['ywymc'] = jxc_xsth_ywyCombo.combobox('getText');
		effectRow['jsfsId'] = jxc_xsth_jsfsCombo.combobox('getValue');
		effectRow['jsfsmc'] = jxc_xsth_jsfsCombo.combobox('getText');
		
		effectRow['thr'] = $('input[name=thr]').val();
		effectRow['payDays'] = $('input[name=payDays]').val();
		if($('input#thfs_sh').is(':checked')){
			effectRow['thfs'] = '0';
			effectRow['shdz'] = $('input[name=jxc_xsth_shdz]').val();
		}else{
			effectRow['thfs'] = '1';
			effectRow['ch'] = $('input[name=ch]').val();
		}
		effectRow['hjje'] = lnyw.delcommafy(footerRows[0]['spje']);
		effectRow['hjsl'] = lnyw.delcommafy(footerRows[0]['cdwsl']);
		effectRow['ysfy'] = ($('input#thfs_sh').is(':checked') && !$('input#zsCheck').is(':checked')) ? $('input[name=jxc_xsth_ysfy]').val() : 0;
		
		effectRow['bookmc'] = $('input[name=jxc_xsth_bookmc]').val();
		effectRow['bz'] = $('input[name=jxc_xsth_bz]').val();
		effectRow['xskpDetIds'] = $('input[name=xskpDetIds]').val();
		effectRow['ywrkDetIds'] = $('input[name=ywrkDetIds]').val();

		effectRow['verifyCode'] = $('input#jxc_xsth_code').val();

		effectRow['bmbh'] = xsth_did;
		effectRow['lxbh'] = xsth_lx;
		effectRow['menuId'] = xsth_menuId;
		
		//将表格中的数据去掉最后一个空行后，转换为json格式
		effectRow['datagrid'] = JSON.stringify(rows.slice(0, rows.length - 1));
		//提交到action
		//MaskUtil.mask('正在保存，请等待……');
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
			    	//保存后不再直接打印，审批通过后在列表内打印
			    	//$.messager.confirm('请确认', '是否打印销售提货单？', function(r) {
					//	if (r) {
					//		var url = lnyw.bp() + '/jxc/xsthAction!printXsth.action?xsthlsh=' + rsp.obj.xsthlsh + "&bmbh=" + xsth_did;
					//		jxc.print(url, PREVIEW_REPORT, HIDE_PRINT_WINDOW);
					//	}
					//});
			    	
			    	//直送提货单打印销售合同，是否有金额限定未定
			    	if($('input[name=jxc_xsth_isZs]').is(':checked')){
				    	$.messager.confirm('请确认', '是否打印销售合同？', function(r) {
							if (r) {
								var url = lnyw.bp() + '/jxc/xsthAction!printXsht.action?xsthlsh=' + rsp.obj.xsthlsh + "&bmbh=" + xsth_did;
								jxc.print(url, PREVIEW_REPORT, HIDE_PRINT_WINDOW);
							}
						});
			    	}
			    	
				}else{
                    $.messager.alert('提示', rsp.msg, 'error');
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
    dwcbEditor = editors[17];
    
    if($(spbhEditor.target).val() != ''){
    	var fhValue = '';
    	if($('input[name=isFhth]').is(':checked')){
    	//if($('input[name=isFh]').is(':checked')){
    		fhValue = jxc_xsth_fhCombo.combobox('getValue');
    	}
    	jxc.spInfo($('#jxc_xsth_layout'), '1', $(spppEditor.target).val(), $(spbzEditor.target).val());
    	jxc.showKc('#jxc_xsth_layout', 
    			'${pageContext.request.contextPath}/jxc/xsthAction!getSpkc.action', 
    			{
    				bmbh : xsth_did, 
    				ckId : jxc_xsth_ckCombo.combobox('getValue'),
    				fhId : fhValue,
    				isFhth: $('input[name=isFhth]').is(':checked') ? '1' : '0',
    				spbh : $(spbhEditor.target).val(),
    			});
    }else{
    	jxc.spInfo($('#jxc_xsth_layout'), '');
    	if(xsth_did != '04'){
    		jxc.hideKc('#jxc_xsth_layout');
    	}
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
    				jxc_xsth_ckCombo.combobox('getValue'),
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
    	
    	//checkSl(zslEditor.target);
    	checkKc(zslEditor.target);
    	
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
//	.bind('focusout', function(event){
//   		//验证成本
//		if(!$('input[name=xskpDetIds]').val()) {
//            checkCb();
//        }
//     	return false;
//    });

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
    			checkKc(cslEditor.target);
    		}
    		calForZ();
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
    	calForZ();
    }).bind('keydown', function(event){
     	if(event.keyCode == 9){
      		spjeEditor.target.focus();
     	}
    });
//    .bind('focusout', function(event){
// 		//验证成本
//        if(!$('input[name=xskpDetIds]').val()) {
//            checkCb();
//        }
// 		return false;
//	});
    
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
    	var spje = 0.00;
//     	if(cslEditor.target.val() != 0 && cdjEditor.target.val() != 0){
//     		spje = cslEditor.target.val() * cdjEditor.target.val();
//     	}else{
        	spje = zslEditor.target.val() * zdjEditor.target.val();
//     	}
        $(spjeEditor.target).numberbox('setValue',spje.toFixed(2));
        //更新汇总列
        updateFooter();
    }
    
    function calForC(){
    	var spje = 0.00;
//     	if(cslEditor.target.val() != 0 && cdjEditor.target.val() != 0){
    		spje = cslEditor.target.val() * cdjEditor.target.val();
//     	}else{
//         	spje = zslEditor.target.val() * zdjEditor.target.val();
//     	}
        $(spjeEditor.target).numberbox('setValue',spje.toFixed(2));
        //更新汇总列
        updateFooter();
    }
  	
    //初始进入编辑状态时，使商品编号获得焦点
    if(spbhEditor.target.val() == ''){
	    spbhEditor.target.focus();
    }else{
	    zslEditor.target.focus();
    }
    
    if(zslEditor.target.val() != ''){
    	checkKc();
    }
    
    function checkKc(target){
    	//非直送业务要判断库存
    	if(!$('input[name=jxc_xsth_isZs]').is(':checked') || !jxc.notInExcludeZsKhs(xsth_did, $('input[name=jxc_xsth_khbh]').val())){
    	//if(!$('input[name=isZs]').is(':checked')){
    	
	    	//判断提货数量是否大于业务数量-临时数量
	    	//从已开票生成提货单不做判断
	    	if($('input[name=xskpDetIds]').val().trim().length == 0 && !$('input[name=isFhth]').is(':checked')){
				var kcRow = $('#show_spkc').propertygrid("getRows");
			    
		    	var kxssl = undefined;
		    	if(kcRow == undefined){
		    		kxssl = Number(0);
		    	}else{
		    		kxssl = Number(kcRow[0].value);
		    	}
		    	var zsl = Number($(zslEditor.target).val());
		    	if(zsl > kxssl){
		    		$.messager.alert("提示", "提货数量不能大于可提货数量，请重新输入！");
		    		$(zslEditor.target).numberbox('setValue', 0);
		    		$(cslEditor.target).numberbox('setValue', 0);
		    		$(target).focus();
		    		return false;
		    	}
	    	}
    	}
    }
}

function checkCb(){
    if($(zdjEditor.target).val() > 0){
        var kcRow = $('#show_spkc').propertygrid("getRows");
        if(kcRow != undefined){
//            if(NEED_AUDIT_XSJJ == "1" &&
//                $(spbhEditor.target).val().substring(0, 1) == '4' &&
//                $(spbhEditor.target).val().substring(0, 3) != '471'
//            ){
//                if ($(zdjEditor.target).val() < (1 + 0.03) * Number(kcRow[0].xscb)){
//                    $.messager.alert("提示", "销售加价率低于3%，将进行审批！");
//                }
//            }else {
                if ($(zdjEditor.target).val() <= Number(kcRow[0].xscb)) {
                    $.messager.alert("提示", "销售单价小于成本，请确认后继续操作！");
                }
//            }
        }
    }
}


//求和
function updateFooter(){
 	var rows = xsth_spdg.datagrid('getRows');
	var spmc_footer = '合计';
	var hjje = 0.00;
	var hjsl = 0.000;
	var spbh;
	$.each(rows, function(){
		var index = xsth_spdg.datagrid('getRowIndex', this);
		if(index < rows.length - 1){
			if(editIndex == index){
				hjje += Number(spjeEditor.target.val());
				hjsl += Number(cslEditor.target.val());
				spbh = spbhEditor.target.val();
			}else{
				hjje += Number(this.spje);
				hjsl += Number(this.cdwsl);
				spbh = this.spbh;
			}
		}
 		
 	});
	xsth_spdg.datagrid('reloadFooter', [{
		spmc : spmc_footer + '(共' + (rows.length - 1) + '条)',
		spje : lnyw.formatNumberRgx(hjje.toFixed(2)),
		cdwsl : isNaN(hjsl) ? '' : hjsl.toFixed(LENGTH_SL),
		}]
	);
	if($('input#thfs_sh').is(':checked') && (xsth_did == '05' || xsth_did == '08')){
		updateYf(spbh);
	}
}

function updateYf(spbh){
	if($('input[name=jxc_xsth_dist]').val() == ''){
		$('input[name=jxc_xsth_ysfy]').val('');
	}else{
		var footerRows = xsth_spdg.datagrid('getFooterRows');
		var hjsl = footerRows[0]['cdwsl'];
		if(hjsl != undefined){
			$('input[name=jxc_xsth_ysfy]').val(jxc.getYf(xsth_did, spbh, $('input[name=jxc_xsth_dist]').val(), hjsl));
// 			if(hjsl == 0){
// 				$('input[name=jxc_xsth_ysfy]').val('');
// 			}else{
// 				$('input[name=jxc_xsth_ysfy]').val(jxc.getYf(xsth_did, spbh, $('input[name=jxc_xsth_dist]').val(), hjsl));
// 			}
		}
	}
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
 	if(jxc_xsth_ywyCombo.combobox('getValue') == 0){
 		message += '未选择业务员<br>';
 	}
	return message;
}

//根据获得的行数据对各字段赋值
function setValueBySpbh(rowData){
	
	var rows = xsth_spdg.datagrid('getRows');
	var exist = false;
	$.each(rows, function(){
		var index = xsth_spdg.datagrid('getRowIndex', this);
		if(index != editIndex){
			if(this.spbh == rowData.spbh){
				exist = true;
				return;
			}
		}
 	});
	
	if(exist){
		$.messager.alert('警告', '商品编号只能出现一次，请重新输入！',  'warning');
		spbhEditor.target.focus();
		return;
	}else{
	
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
	dwcbEditor.target.val(rowData.dwcb);
	//文达印刷业务员为公司(天女)、公司(天狮)时取销售单价
	if(xsth_did == '01' && (jxc_xsth_ywyCombo.combobox('getValue') == 115 || jxc_xsth_ywyCombo.combobox('getValue') == 193)){
		zdjEditor.target.val(rowData.specXsdj);
	}
	
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
   				isFhth: $('input[name=isFhth]').is(':checked') ? '1' : '0',
   				spbh : $(spbhEditor.target).val(),
   			});
	}
}


function checkKh(){
	$.ajax({
		url:'${pageContext.request.contextPath}/jxc/khAction!checkKh.action',
		async: false,
		data:{
			khbh: $('input[name=jxc_xsth_khbh]').val(),
			depId: xsth_did,
		},
		dataType:'json',
		success:function(data){
			if(!data.success){
				$.messager.alert('提示', data.msg, 'error');
				$('input[name=jxc_xsth_khbh]').val('');
				$('input[name=xsth_khmc]').val('');
				$('input[name=jxc_xsth_khbh]').focus();
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
				$('input[name=xsth_khmc]').val(data.obj.khmc);
//				$('input[name=sh]').val(data.obj.sh);
//				$('input[name=khh]').val(data.obj.khh);
//				$('input[name=dzdh]').val(data.obj.dzdh);
				jxc_xsth_ywyCombo.combobox('setValue', data.obj.ywyId);
//				if(data.obj.isSx == '1'){
//					$('input[name=isSx]').prop('checked', 'ckecked');
//				}
			}else{
				$.messager.alert('提示', '客户信息不存在！', 'error');
			}
		}
	});
}

//在前台页面响应输入事件，按ESC键弹出客户列表或直接输入客户编码
function khLoad(){
	switch(event.keyCode){
	case 27:
		jxc.query('客户检索', $('input[name=jxc_xsth_khbh]'), $('input[name=xsth_khmc]'), $('input[name=jxc_xsth_dist]'),
				'${pageContext.request.contextPath}/jxc/query.jsp',
				'${pageContext.request.contextPath}/jxc/khAction!khDg.action?depId=' + xsth_did);
// 				'${pageContext.request.contextPath}/jxc/khAction!khDg.action?isSx=' + isSx + "&depId=" + xsth_did);
		break;
	case 9:
		break;
	default:
		if($('input[name=jxc_xsth_khbh]').val().trim().length == 0){
			$('input[name=xsth_khmc]').val('');
		}
		if($('input[name=jxc_xsth_khbh]').val().trim().length == 8){
			loadKh($('input[name=jxc_xsth_khbh]').val().trim());
			if(!$('input[name=jxc_xsth_isZs]').is(':checked')){
				jxc_xsth_ckCombo.combobox('setValue', jxc.getCkByKhbh(xsth_did, $('input[name=jxc_xsth_khbh]').val()));
			}
			
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

//在前台页面响应输入事件，按ESC键弹出客户列表或直接输入客户编码
function khShLoad(){
	switch(event.keyCode){
	case 27:
		jxc.query('客户检索', $('input[name=jxc_xsth_shkhbh]'), $('input[name=jxc_xsth_shdz]'), $('input[name=jxc_xsth_dist]'),  
				'${pageContext.request.contextPath}/jxc/query.jsp',
				'${pageContext.request.contextPath}/jxc/khAction!khDg.action?depId=' + xsth_did);
// 				'${pageContext.request.contextPath}/jxc/khAction!khDg.action?isSx=' + isSx + "&depId=" + xsth_did);
		break;
	case 9:
		break;
	default:
		if($('input[name=jxc_xsth_khbh]').val().trim().length == 0){
			$('input[name=xsth_khmc]').val('');
		}
		if($('input[name=khbh]').val().trim().length == 8){
			loadKh($('input[name=jxc_xsth_khbh]').val().trim());
			if(!$('input[name=jxc_xsth_isZs]').is(':checked')){
				jxc_xsth_ckCombo.combobox('setValue', jxc.getCkByKhbh(xsth_did, $('input[name=jxc_xsth_khbh]').val()));
			}
			
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

function updateJsfs(){
	$.ajax({
		type: "POST",
		async: false,
		url: '${pageContext.request.contextPath}/jxc/xsthAction!getYsje.action',
		data: {
			bmbh: xsth_did,
			khbh: $('input[name=jxc_xsth_khbh]').val().trim(),
			ywyId: jxc_xsth_ywyCombo.combobox('getValue'),
		},
		dataType: 'json',
		success: function(d){
			if(Number(d.obj) > 0){
				jxc_xsth_jsfsCombo.combobox('setValue', JSFS_QK);
				jxc_xsth_jsfsCombo.combobox('readonly', true);
			}else{
				jxc_xsth_jsfsCombo.combobox('readonly', false);
			}
		},
	});
}
//////////////////////////////////////////////以上为商品列表处理代码

//////////////////////////////////////////////以下为销售提货列表处理代码
function cancelXsth(){
 	var selected = xsth_dg.datagrid('getSelected');
 	if (selected != undefined) {
	 	$.ajax({
			type: "POST",
			async: false,
			url: '${pageContext.request.contextPath}/jxc/xsthAction!refreshXsth.action',
			data: {
				xsthlsh: selected.xsthlsh,
			},
			dataType: 'json',
			success: function(d){
				var row = d.obj;
				if(row.isCancel != '1'){
		 			if(row.locked == '0'){
		 			    if(row.cgjhlsh == undefined){
							if(row.fromFp == '1' || row.xskplsh == undefined || row.xskplsh == ''){
								if(row.isTh != '1'){
									$.messager.confirm('请确认', '您要取消选中的销售提货单？', function(r) {
										if (r) {
											//MaskUtil.mask('正在取消，请稍等……');
											$.ajax({
												url : '${pageContext.request.contextPath}/jxc/xsthAction!cancelXsth.action',
												data : {
													xsthlsh : row.xsthlsh,
													fromOther: jxc.notInExcludeKhs(xsth_did, selected.khbh) ? '' : 'cbs',
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
												},
												complete: function(){
													//MaskUtil.unmask();
												}
											});
										}
									});
								}else{
									$.messager.alert('警告', '选中的销售提货记录已经出库，不能取消！',  'warning');
								}
							}else{
								$.messager.alert('警告', '选中的销售提货记录已开票，不能取消！',  'warning');
							}
                        }else{
                            $.messager.alert('警告', '选中的销售提货已由计划员进行处理，不能取消！',  'warning');
                        }
		 			}else{
		 				$.messager.alert('警告', '选中的销售提货记录已被库房锁定，请重新选择！',  'warning');
		 			}
		 		}else{
		 			$.messager.alert('警告', '选中的销售提货记录已被取消，请重新选择！',  'warning');
		 		}
			},
		});
 	}else{
 		$.messager.alert('警告', '请选择最少一条记录进行操作！',  'warning');
 	}
}

function printXsth(){
	var selected = xsth_dg.datagrid('getSelected');
 	if (selected != undefined) {
	 	$.ajax({
			type: "POST",
			async: false,
			url: '${pageContext.request.contextPath}/jxc/xsthAction!refreshXsth.action',
			data: {
				xsthlsh: selected.xsthlsh,
			},
			dataType: 'json',
			success: function(d){
				var row = d.obj;
				if(row.needAudit == row.isAudit){
					$.messager.confirm('请确认', '是否打印销售提货单？', function(r) {
						if (r) {
							var url = lnyw.bp() + '/jxc/xsthAction!printXsth.action?xsthlsh=' + row.xsthlsh + "&bmbh=" + xsth_did + '&type=' + PRINT_TYPE_XSTH_YW;
							jxc.print(url, PREVIEW_REPORT, HIDE_PRINT_WINDOW);
						}
					});
				}else{
					$.messager.alert('警告', '选择打印的销售提货未经过审批！',  'warning');
				}
			}
		});
	}else{
		$.messager.alert('警告', '请选择一条记录进行操作！',  'warning');
	}
}

function exportXsth(){
	var selected = xsth_dg.datagrid('getSelected');
 	if (selected != undefined) {
 		var data = {
 				xsthlsh : selected.xsthlsh,
				bmbh: xsth_did,
				type: 'rtf'
			};
		jxc.export('${pageContext.request.contextPath}', '/jxc/xsthAction!exportXsth.action', data);

// 		$.ajax({	
// 			url:'${pageContext.request.contextPath}/jxc/xsthAction!exportXsth.action',
// 			async: false,
// 			cache: false,
// 			context:this,	
// 			data : {
// 				xsthlsh : selected.xsthlsh,
// 				bmbh: xsth_did,
// 			},
// 			success:function(data){
// 				var json = $.parseJSON(data);
				
// 				window.open("${pageContext.request.contextPath}/"+json.obj);
				
// 				$.messager.show({
// 					title : "提示",
// 					msg : json.msg
// 				});
// 			},
// 			complete: function(){
// 				//lnyw.MaskUtil.unmask();
// 			}
// 		});
	}else{
		$.messager.alert('警告', '请选择一条记录进行操作！',  'warning');
	}
}


function printXsht(){
	var selected = xsth_dg.datagrid('getSelected');
 	if (selected != undefined) {
 		if(selected.isCancel == '0'){
 			if(selected.isKp == '0'){
 				//if(selected.isZs == '1'){
 					//if(selected.needAudit == selected.isAudit){
					 	$.messager.confirm('请确认', '是否打印销售合同？', function(r) {
							if (r) {
								var url = lnyw.bp() + '/jxc/xsthAction!printXsht.action?xsthlsh=' + selected.xsthlsh + "&bmbh=" + xsth_did;
								jxc.print(url, PREVIEW_REPORT, HIDE_PRINT_WINDOW);
							}
						});
 					//}else{
 					//	$.messager.alert('警告', '选择的记录还未审批 ，请重新选择！',  'warning');
 					//}
//  	 			}else{
//  	 	 			$.messager.alert('警告', '选择的记录不是直送业务 ，请重新选择！',  'warning');
//  	 	 		}

 			}else{
 	 			$.messager.alert('警告', '选择的记录已开票，请重新选择！',  'warning');
 	 		}
 		}else{
 			$.messager.alert('警告', '选择的记录已取消，请重新选择！',  'warning');
 		}
	}else{
		$.messager.alert('警告', '请选择一条记录进行操作！',  'warning');
	}
}

function exportXsht(){
	var selected = xsth_dg.datagrid('getSelected');
 	if (selected != undefined) {
 		if(selected.isCancel == '0'){
 			if(selected.isKp == '0'){
 				//if(selected.isZs == '1'){
 					//if(selected.needAudit == selected.isAudit){
					 	$.messager.confirm('请确认', '是否导出销售合同？', function(r) {
							if (r) {
								var data = {
										xsthlsh : selected.xsthlsh,
										bmbh: xsth_did,
										type: 'rtf'
									};
								jxc.export('${pageContext.request.contextPath}', '/jxc/xsthAction!exportXsht.action', data);
							}
						});
 					//}else{
 					//	$.messager.alert('警告', '选择的记录还未审批 ，请重新选择！',  'warning');
 					//}
//  	 			}else{
//  	 	 			$.messager.alert('警告', '选择的记录不是直送业务 ，请重新选择！',  'warning');
//  	 	 		}

 			}else{
 	 			$.messager.alert('警告', '选择的记录已开票，请重新选择！',  'warning');
 	 		}
 		}else{
 			$.messager.alert('警告', '选择的记录已取消，请重新选择！',  'warning');
 		}
	}else{
		$.messager.alert('警告', '请选择一条记录进行操作！',  'warning');
	}
}

function updateShdz(){
    var row = xsth_dg.datagrid('getSelected');
    if(row != undefined){
		$.messager.prompt('请确认', '是否要修改送货地址？请输入新的送货地址', function(shdz){
			if (shdz != undefined){
				$.ajax({
					url : '${pageContext.request.contextPath}/jxc/xsthAction!updateShdz.action',
					data : {
						xsthlsh : row.xsthlsh,
						shdz: shdz,
						bmbh : xsth_did,
						menuId : xsth_menuId,
					},
					dataType : 'json',
					method: 'post',
					success : function(d) {
						xsth_dg.datagrid('updateRow', {
						    index: xsth_dg.datagrid('getRowIndex', row),
							row: {
						        shdz: shdz
							}
						});
						//xsth_dg.datagrid('unselectAll');
						$.messager.show({
							title : '提示',
							msg : d.msg
						});
					}
				});
			}
		});
    }else{
        $.messager.alert('警告', '请选择一条记录进行操作！',  'warning');
    }
}

function updateBz(){
    var row = xsth_dg.datagrid('getSelected');
    if(row != undefined){
        $.messager.prompt('请确认', '是否要修改备注？请输入新的备注', function(bz){
            if (bz != undefined){
                $.ajax({
                    url : '${pageContext.request.contextPath}/jxc/xsthAction!updateBz.action',
                    data : {
                        xsthlsh : row.xsthlsh,
                        bz: bz,
                        bmbh : xsth_did,
                        menuId : xsth_menuId,
                    },
                    dataType : 'json',
                    method: 'post',
                    success : function(d) {
                        xsth_dg.datagrid('updateRow', {
                            index: xsth_dg.datagrid('getRowIndex', row),
                            row: {
                                bz: bz
                            }
                        });
                        $.messager.show({
                            title : '提示',
                            msg : d.msg
                        });
                    }
                });
            }
        });
    }else{
        $.messager.alert('警告', '请选择一条记录进行操作！',  'warning');
    }
}

function printShd(){
	if(detDg != undefined){
		var detRows = detDg.datagrid('getSelections');
		if(detRows != null){
			if(xsthRow.isZs == '1'){
				if(xsthRow.isCancel == '0'){
					if(xsthRow.needAudit == xsthRow.isAudit){
						var xsthDetIds = [];
						var flag = true;
						$.each(detRows, function(index){
						  	if(detRows[index].cgjhlsh == undefined){
						  		$.messager.alert('提示', '选择的销售提货记录未实施计划，请重新选择！', 'error');
								flag = false;
						  	}
						  	if(detRows[index].thsl == 0){
						  		$.messager.alert('提示', '选择的销售提货记录未确认数量，请重新选择！', 'error');
								flag = false;	
						  	}
						 	xsthDetIds.push(detRows[index].id);
						});
						if(flag){
							$.messager.confirm('请确认', '是否打印收货确认单？', function(r) {
								if (r) {
								//var url = lnyw.bp() + '/jxc/xsthAction!printShd.action?xsthlsh=' + xsthRow.xsthlsh + "&cgjhlsh=" + detRow.cgjhlsh + "&bmbh=" + xsth_did;
									var url = lnyw.bp() + '/jxc/xsthAction!printShd.action?xsthDetIds=' + xsthDetIds.join(',') + "&cgjhlsh=" + detRows[0].cgjhlsh + "&bmbh=" + xsth_did;
									jxc.print(url, PREVIEW_REPORT, HIDE_PRINT_WINDOW);
								}
							});
						}
					}else{
						$.messager.alert('警告', '选择的销售提货记录还未审批，请重新选择！',  'warning');
					}
				}else{
					$.messager.alert('警告', '选择的销售提货记录已经取消，请重新选择！',  'warning');
				}
			}else{
				$.messager.alert('警告', '选择的销售提货记录不是直送业务，请重新选择！',  'warning');
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


function exportShd(){
	if(detDg != undefined){
		var detRows = detDg.datagrid('getSelections');
		if(detRows != null){
			if(xsthRow.isZs == '1'){
				if(xsthRow.isCancel == '0'){
					if(xsthRow.needAudit == xsthRow.isAudit){
						var xsthDetIds = [];
						var flag = true;
						$.each(detRows, function(index){
						  	if(detRows[index].cgjhlsh == undefined){
						  		$.messager.alert('提示', '选择的销售提货记录未实施计划，请重新选择！', 'error');
								flag = false;
						  	}
						  	if(detRows[index].thsl == 0){
						  		$.messager.alert('提示', '选择的销售提货记录未确认数量，请重新选择！', 'error');
								flag = false;	
						  	}
						 	xsthDetIds.push(detRows[index].id);
						});
						if(flag){
							$.messager.confirm('请确认', '是否导出收货确认单？', function(r) {
								if (r) {
								//var url = lnyw.bp() + '/jxc/xsthAction!printShd.action?xsthlsh=' + xsthRow.xsthlsh + "&cgjhlsh=" + detRow.cgjhlsh + "&bmbh=" + xsth_did;
									//var url = lnyw.bp() + '/jxc/xsthAction!printShd.action?xsthDetIds=' + xsthDetIds.join(',') + "&cgjhlsh=" + detRows[0].cgjhlsh + "&bmbh=" + xsth_did;
									//jxc.print(url, PREVIEW_REPORT, HIDE_PRINT_WINDOW);
									
									var data = {
										xsthDetIds : xsthDetIds.join(','),
										cgjhlsh: detRows[0].cgjhlsh,
										bmbh: xsth_did,
										xsthlsh: xsthRow.xsthlsh,
										type: 'rtf'
									};
									jxc.export('${pageContext.request.contextPath}', '/jxc/xsthAction!exportShd.action', data);
								
								
// 									$.ajax({	
// 										url:'${pageContext.request.contextPath}/jxc/xsthAction!exportShd.action',
// 										async: false,
// 										cache: false,
// 										context:this,	
// 										data : {
// 											xsthDetIds : xsthDetIds.join(','),
// 											cgjhlsh: detRows[0].cgjhlsh,
// 											bmbh: xsth_did,
// 										},
// 										success:function(data){
// 											var json = $.parseJSON(data);
											
// 											window.open("${pageContext.request.contextPath}/" + json.obj);
											
// 											$.messager.show({
// 												title : "提示",
// 												msg : json.msg
// 											});
// 										},
// 										complete: function(){
// 											//lnyw.MaskUtil.unmask();
// 										}
// 									});
								}
							});
						}
					}else{
						$.messager.alert('警告', '选择的销售提货记录还未审批，请重新选择！',  'warning');
					}
				}else{
					$.messager.alert('警告', '选择的销售提货记录已经取消，请重新选择！',  'warning');
				}
			}else{
				$.messager.alert('警告', '选择的销售提货记录不是直送业务，请重新选择！',  'warning');
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


function changeYf(){
	var selected = xsth_dg.datagrid('getSelected');
 	if (selected != undefined) {
	 	if(selected.isCancel != '1'){
			$.messager.prompt('请确认', '请输入重新核算的运费：', function(yfje){
				if (yfje != undefined){
					$.ajax({
						url : '${pageContext.request.contextPath}/jxc/xsthAction!changeYf.action',
						data : {
							xsthlsh : selected.xsthlsh,
							ysfy: yfje,
							bmbh : xsth_did,
							menuId : xsth_menuId,
						},
						dataType : 'json',
						success : function(d) {
                            xsth_dg.datagrid('updateRow', {
                                index: xsth_dg.datagrid('getRowIndex', selected),
                                row: {
                                    ysfy: yfje
                                }
                            });
							//xsth_dg.datagrid('reload');
							//xsth_dg.datagrid('unselectAll');
							$.messager.show({
								title : '提示',
								msg : d.msg
							});
						}
					});
				}
			});
		}else{
			$.messager.alert('警告', '选择的销售提货已取消！',  'warning');
		}
	}else{
		$.messager.alert('警告', '请选择一条记录进行操作！',  'warning');
	}
}


//要判断处理的单据有效性（冲减、开票、直送）
function confirmThsl(){
	if(detDg != undefined){
		var detRow = detDg.datagrid('getSelected');
		if(detRow != null){
			if(xsthRow.isZs == '1'){
				if(xsthRow.isCancel == '0'){
					if(xsthRow.needAudit == xsthRow.isAudit){
						if(xsthRow.isKp == '0'){
							//if(detRow.cgjhlsh != undefined){
								if(detRow.completed == '0'){
									$.messager.prompt('请确认', '是否要确认提货数量？请输入', function(thsl){
										if (thsl != undefined){
											$.ajax({
												url : '${pageContext.request.contextPath}/jxc/xsthAction!updateThsl.action',
												data : {
													id : detRow.id,
													thsl: thsl,
													fromOther: 'xsth',
													bmbh : xsth_did,
													menuId : xsth_menuId,
												},
												dataType : 'json',
												success : function(d) {
                                                    detDg.datagrid('updateRow', {
                                                        index: detDg.datagrid('getRowIndex', detRow),
                                                        row: {
                                                            thsl: thsl
                                                        }
                                                    });
													//detDg.datagrid('reload');
													//detDg.datagrid('unselectAll');
													$.messager.show({
														title : '提示',
														msg : d.msg
													});
												}
											});
										}
									});
								}else{
									$.messager.alert('警告', '选择的销售提货已完成，请重新选择！',  'warning');
								}
// 							}else{
// 								$.messager.alert('警告', '选择的销售提货记录还未实施计划，请重新选择！',  'warning');
// 							}
							//detDg = undefined;
						}else{
							$.messager.alert('警告', '选择的销售提货记录已经开票，请重新选择！',  'warning');
						}
					}else{
						$.messager.alert('警告', '选择的销售提货记录还未审批，请重新选择！',  'warning');
					}
				}else{
					$.messager.alert('警告', '选择的销售提货记录已经取消，请重新选择！',  'warning');
				}
			}else{
				$.messager.alert('警告', '选择的销售提货记录不是直送业务，请重新选择！',  'warning');
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

function completeXsth(){
	if(detDg != undefined){
		var detRow = detDg.datagrid('getSelected');
		if(detRow != null){
			//if(detRow.completed == '0'){
				if(xsthRow.isZs == '1'){
					if(xsthRow.isCancel == '0'){
						if(xsthRow.needAudit == xsthRow.isAudit){
							if(xsthRow.isKp == '0'){
								//if(detRow.cgjhlsh != undefined){
									if(detRow.thsl != undefined){
										$.messager.confirm('请确认', detRow.completed == '0' ? '是否要确认直送完成？' : '是否要取消直送完成？', function(r) {
											if (r) {
												$.ajax({
													url : '${pageContext.request.contextPath}/jxc/xsthAction!updateZsComplete.action',
													data : {
														id : detRow.id,
														bmbh : xsth_did,
														menuId : xsth_menuId
													},
													dataType : 'json',
													success : function(d) {
                                                        detDg.datagrid('updateRow', {
                                                            index: detDg.datagrid('getRowIndex', detRow),
                                                            row: {
                                                                completed: detRow.completed == '0' ? '1' : '0'
                                                            }
                                                        });
														//detDg.datagrid('reload');
														//detDg.datagrid('unselectAll');
														$.messager.show({
															title : '提示',
															msg : d.msg
														});
													}
												});
											}
										});
									}else{
										$.messager.alert('警告', '选择的销售提货记录还未确认数量，请重新选择！',  'warning');
									}
// 								}else{
// 									$.messager.alert('警告', '选择的销售提货记录还未实施计划，请重新选择！',  'warning');
// 								}
								//detDg = undefined;
							}else{
								$.messager.alert('警告', '选择的销售提货记录已经开票，请重新选择！',  'warning');
							}
						}else{
							$.messager.alert('警告', '选择的销售提货记录还未审批，请重新选择！',  'warning');
						}
					}else{
						$.messager.alert('警告', '选择的销售提货记录已经取消，请重新选择！',  'warning');
					}
				}else{
					$.messager.alert('警告', '选择的销售提货记录不是直送业务，请重新选择！',  'warning');
				}
			//}else{
			//	$.messager.alert('警告', '选择的销售提货记录已完成，请重新选择！',  'warning');
			//}
		}else{
			$.messager.alert('警告', '请选择商品明细记录进行操作！',  'warning');
			return false;
		}
		
	}else{
		$.messager.alert('警告', '请选择商品明细记录进行操作！',  'warning');
		return false;
	}
	
}

function searchXsth(){
	xsth_dg.datagrid('load',{
		bmbh: xsth_did,
		createTime: $('input[name=createTimeXsth]').val(),
		search: $('input[name=searchXsth]').val(),
	});
}


//////////////////////////////////////////////以上为销售提货列表处理代码

//////////////////////////////////////////////以下为库房出库列表处理代码


//function searchKfckInXsth(){
//	xsth_kfckDg.datagrid('load',{
//		bmbh: xsth_did,
//		createTime: $('input[name=createTimeKfckInXsth]').val(),
//		fromOther: 'fromXsth'
//	});
//}

//////////////////////////////////////////////以上为库房出库列表处理代码

////////////////////////////////////////////以下为销售开票列表处理代码
function toXsth(){
	var rows = xsth_xskpDg.datagrid('getSelections');
	var xskpDetIds = [];
	if(rows.length > 0){
		var preRow = undefined;
		var flag = true;
		var spbhs = []
	    $.each(rows, function(index){
	    	xskpDetIds.push(rows[index].id);
	    	if(index != 0){
	    		if(this.khbh != preRow.khbh){
	    			$.messager.alert('提示', '请选择同一客户的销售发票进行提货！', 'error');
					flag = false;
					//return false;
	    		}
	    	}
	    	if(spbhs.indexOf(this.spbh) >= 0){
                $.messager.alert('提示', '同一商品不能出现2次，请重新选择！', 'error');
                flag = false;
			}else {
                spbhs.push(this.spbh);
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
							$('input[name=jxc_xsth_khbh]').val(rows[0].khbh);
							$('input[name=xsth_khmc]').val(rows[0].khmc);
							jxc_xsth_ckCombo.combobox("setValue", rows[0].ckId);
							jxc_xsth_ywyCombo.combobox("setValue", rows[0].ywyId);
							jxc_xsth_jsfsCombo.combobox("setValue", rows[0].jsfsId);
							updateFooter();
//							$('input[name=xskplsh]').val(row.xskplsh);
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
		search: countXskpInXsth = $('input[name=searchXskpInXsth]').val(),
		fromOther: 'fromXsth'
	});
}

////////////////////////////////////////////以上为销售开票列表处理代码
////////////////////////////////////////////以下为业务入库列表处理代码
function createXsthFromYwrk(){
	var rows = xsth_ywrkDg.datagrid('getSelections');
	var ywrkDetIds = [];
	if(rows.length > 0){
		var preRow = undefined;
		var flag = true;
	    $.each(rows, function(index){
	    	ywrkDetIds.push(rows[index].id);
	    	if(index != 0){
	    		if(this.ywrklsh != preRow.ywrklsh){
	    			$.messager.alert('提示', '请选择同一入库单的商品进行提货！', 'error');
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
					var ywrkDetStr = ywrkDetIds.join(',');
					$.ajax({
						url : '${pageContext.request.contextPath}/jxc/ywrkAction!toXsth.action',
						data : {
							ywrkDetIds: ywrkDetStr
						},
						dataType : 'json',
						success : function(d) {
							xsth_spdg.datagrid('loadData', d.rows);
							jxc_xsth_ckCombo.combobox("setValue", rows[0].ckId);
							$('input:checkbox#zsCheck').prop('checked', true);
							updateFooter();
							$('input[name=ywrkDetIds]').val(ywrkDetStr);
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


function searchYwrkInXsth(){
	xsth_ywrkDg.datagrid('load',{
		bmbh: xsth_did,
		createTime: $('input[name=createTimeYwrkInXsth]').val(),
		fromOther: 'fromXsth'
	});
}

////////////////////////////////////////////以上为业务入库列表处理代码

////////////////////////////////////////////以下为付印单列表处理代码
function confirmXsdj(){
	if(fydDetDg != undefined){
		var detRow = fydDetDg.datagrid('getSelected');
		var detIndex = fydDetDg.datagrid('getRowIndex', detRow);
		if(detRow != null){
			if(fydRow.status == '1' || fydRow.status == '0'){
				$.messager.prompt('请确认', '是否要确认销售单价？请输入', function(danjia){
					if (danjia != undefined){
						$.ajax({
							url : '${pageContext.request.contextPath}/jxc/fydAction!updateXsdj.action',
							data : {
								id : detRow.id,
								danjia: danjia,
								fromOther: 'xsth',
								bmbh : xsth_did,
								menuId : xsth_menuId,
							},
							dataType : 'json',
							success : function(d) {
								fydDetDg.datagrid('updateRow', {
									index: detIndex,
									row: {
										danjia: danjia,
										gongj: d.obj.gongj
									}
								});
								fydDetDg.datagrid('unselectRow', detIndex);
								
								var edited = '1';
								var detRows = fydDetDg.datagrid('getRows'); 
								$.each(detRows, function(index){
									if(detRows[index].danjia == 0){
										edited = '0';
									}
								});
								if(fydRow.edited != edited){
									xsth_fydDg.datagrid('updateRow', {
										index: xsth_fydDg.datagrid('getRowIndex', fydRow),
										row: {
											edited: edited
										}
									});
								}
								
								$.messager.show({
									title : '提示',
									msg : d.msg
								});
							}
						});
					}
				});
			}else{
				$.messager.alert('警告', '选择的付印单记录已进行了更新，请重新选择！',  'warning');
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

function sendFyd(){
	var selected = xsth_fydDg.datagrid('getSelected');
 	if (selected != undefined) {
 		if (selected.edited != '0') {
	 		$.messager.confirm('请确认', '是否上传付印单数据？', function(r){
				if (r != undefined){
					$.ajax({
						url : '${pageContext.request.contextPath}/jxc/fydAction!sendFyd.action',
						data : {
							fydlsh : selected.fydlsh,
							bmbh : xsth_did
						},
						dataType : 'json',
						success : function(d) {
							xsth_fydDg.datagrid('reload');
							xsth_fydDg.datagrid('unselectAll');
							$.messager.show({
								title : '提示',
								msg : d.msg
							});
						}
					});
				}
			});
 		}else{
 			$.messager.alert('警告', '选择的记录商品单价未确认，请操作！',  'warning');
 		}
	}else{
		$.messager.alert('警告', '请选择一条记录进行操作！',  'warning');
	}
}

function searchFydInXsth(){
	xsth_fydDg.datagrid('load',{
		bmbh: xsth_did,
		createTime: $('input[name=createTimeFydInXsth]').val(),
		fromOther: 'fromXsth'
	});
}

////////////////////////////////////////////以上为付印单列表处理代码



</script>


<!-- tabPosition:'left', headerWidth:'35' -->
<div id="jxc_xsth_tabs" class="easyui-tabs" data-options="fit:true, border:false," style="width:100%;height:100%;">
    <div title="新增记录" data-options="closable:false">
        <div id='jxc_xsth_layout' style="height:100%;width:100%">
			<div id="jxc_xsth_info" data-options="region:'north',title:'单据信息',border:false,collapsible:false" style="width:100%;height:180px">
				<div class="form_line">
					<span class="form_label">直发<input id="zsCheck" type="checkbox" name="jxc_xsth_isZs"></span>
					<span class="form_label">发票<input id="fpCheck" type="checkbox" name="toFp"></span>
					<span class="fh form_label" style="display:none">分户<input type="checkbox" name="isFh"></span>
	 				<span class="isFhth form_label" style="display:none">分户提货<input type="checkbox" name="isFhth"></span>
					<span class="read form_label">时间</span>
					<span id="createDate" class="read"></span>
					<span class="read form_label">单据号</span>
					<span id="xsthLsh" class="read"></span>
				</div>
				<div class="form_line">
					<span class="form_label">客户编号</span>
					<span><input name="jxc_xsth_khbh" class="easyui-validatebox" data-options="validType:['mustLength[8]','integer']" onkeyup="khLoad()" size="6"></span>
					<span class="form_label">客户名称</span>
					<span><input name="xsth_khmc" readonly="readonly" size="30"></span>
					<span class="form_label">仓库</span>
					<span><input id="jxc_xsth_ckId" name="jxc_xsth_ckId" size="10"></span>
					<span class="isFh form_label" style="display:none">分户</span>
					<span class="isFh" style="display:none"><input id="jxc_xsth_fhId" name="fhId" size="10"></span>
					<span class="isShYf form_label" style="display:none">运费</span>
					<span class="isShYf" style="display:none"><input name="jxc_xsth_ysfy" size="4">元</span>
				</div>
				<div class="form_line">
					<span class="form_label">业务员</span>
					<span><input id="jxc_xsth_ywyId" name="ywyId" size="4"></span>
					<span class="form_label">结算方式</span>
					<span><input id="jxc_xsth_jsfsId" name="jsfsId" size="4"></span>
					<span class="form_label">自提<input type="radio" name="thfs" id='thfs_zt' checked="checked" value="1"></span><span class="form_label">送货<input type="radio" name="thfs" id="thfs_sh" value="0"></span>
					<span class="isZt form_label">车号</span>
					<span class="isZt"><input name="ch" size="10"></span>
					<span class="form_label">提货人</span>
					<span><input name="thr" size="10"></span>
					<span class="isSh form_label" style="display:none">送货地址</span>
					<span class="isSh" style="display:none"><input name="jxc_xsth_shdz" size="12"></span>
					<span class="form_label">付款天数</span>
					<span><input name="payDays" size="10"></span>
				</div>
				<div class='jxc_xsth_bookmc form_line'>
						<span class="form_label">书名</span>
						<span><input name="jxc_xsth_bookmc" type="text" style="width:71%"></span>
					</div>
				<div class="form_line">
					<span class="form_label">备注</span>
					<span><input name="jxc_xsth_bz" style="width:90%"></span>
				</div>
				<input name="xskpDetIds" type="hidden">
				<input name="ywrkDetIds" type="hidden">
				<input name="jxc_xsth_shkhbh" type="hidden">
				<input name="jxc_xsth_dist" type="hidden">
				<input name="verifyCode" id="jxc_xsth_code" type="hidden">
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
	<div title="销售开票列表" data-options="closable:false" >
		<div id='jxc_xsth_xskpDg'></div>
	</div>
	<div title="业务入库列表" data-options="closable:false" >
		<div id='jxc_xsth_ywrkDg'></div>
	</div>
	<div title="付印单列表" data-options="closable:false" >
		<div id='jxc_xsth_fydDg'></div>
	</div>
</div>

<div id="jxc_xsth_tb" style="padding:3px;height:auto">
	请输入查询起始日期:<input type="text" name="createTimeXsth" class="easyui-datebox" data-options="value: moment().date(1).format('YYYY-MM-DD')" style="width:100px">
	输入流水号、客户编号、名称、业务员、备注、书名、分户：<input type="text" name="searchXsth" style="width:100px">
	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchXsth();">查询</a>
</div>
<div id="jxc_xsth_xskpTb" style="padding:3px;height:auto">
	请输入查询起始日期:<input type="text" name="createTimeXskpInXsth" class="easyui-datebox" data-options="value: moment().date(1).format('YYYY-MM-DD')" style="width:100px">
	输入流水号、客户、业务员、备注：<input type="text" name="searchXskpInXsth" style="width:100px">
	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchXskpInXsth();">查询</a>
</div>
<div id="jxc_xsth_ywrkTb" style="padding:3px;height:auto">
	请输入查询起始日期:<input type="text" name="createTimeYwrkInXsth" class="easyui-datebox" data-options="value: moment().date(1).format('YYYY-MM-DD')" style="width:100px">
	输入流水号、供应商、备注：<input type="text" name="searchYwrkInXsth" style="width:100px">
	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchYwrkInXsth();">查询</a>
</div>
<div id="jxc_xsth_fydTb" style="padding:3px;height:auto">
	请输入查询起始日期:<input type="text" name="createTimeFydInXsth" class="easyui-datebox" data-options="value: moment().date(1).format('YYYY-MM-DD')" style="width:100px">
	输入流水号、供应商、备注：<input type="text" name="searchFydInXsth" style="width:100px">
	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchFydInXsth();">查询</a>
</div>


