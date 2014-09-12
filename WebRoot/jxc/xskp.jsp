<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>


<script type="text/javascript">
var xskp_did;
var xskp_lx;
var xskp_menuId;
var xskp_spdg;
var xskp_dg;
var xskp_xsthDg;
var editIndex = undefined;
var xskp_tabs;

var countXskp = 0;
var countXsthInXskp = 0;

var jxc_xskp_ckCombo;
var jxc_xskp_ywyCombo;
var jxc_xskp_jsfsCombo;
var jxc_xskp_fhCombo;
var jxc_xskp_fyrCombo;


//编辑行字段
var spbhEditor;   
var spmcEditor;
var spcdEditor;
var spppEditor;
var spbzEditor;
var zjldwEditor;
var zthslEditor;
var zytslEditor;
var zslEditor;
var zdjEditor;
var cjldwEditor;
var cthslEditor;
var cytslEditor;
var cslEditor;
var cdjEditor;   
var spjeEditor;
var spseEditor;
var sphjEditor;
var zhxsEditor;
var zjldwIdEditor;
var cjldwIdEditor;

$(function(){
	xskp_did = lnyw.tab_options().did;
	xskp_lx = lnyw.tab_options().lx;
	xskp_menuId = lnyw.tab_options().id;
	
	$('#jxc_xskp_layout').layout({
		fit : true,
		border : false,
	});
	
	xskp_dg = $('#jxc_xskp_dg').datagrid({
		fit : true,
	    border : false,
	    singleSelect : false,
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
	        {field:'khbh',title:'客户编号',align:'center',hidden:true},
	        {field:'khmc',title:'客户名称',align:'center'},
	        {field:'ckId',title:'仓库id',align:'center',hidden:true},
	        {field:'ckmc',title:'仓库',align:'center'},
	        {field:'fhId',title:'分户 id',align:'center',hidden:true},
	        {field:'fhmc',title:'分户',align:'center'},
	        {field:'ywymc',title:'业务员',align:'center'},
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
	        {field:'bookmc',title:'书名',align:'center'},
	        {field:'kpr',title:'制单人',align:'center'},
        	{field:'ywrklsh',title:'内部入库',align:'center'},
	        {field:'jsfsmc',title:'结算方式',align:'center'},
	        {field:'fplxmc',title:'发票类型',align:'center'},
	    ]],
	    toolbar:'#jxc_xskp_tb',
	});
	lnyw.toolbar(1, xskp_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', xskp_did);
	
	xskp_dg.datagrid({
        view: detailview,
        detailFormatter:function(index,row){
            return '<div style="padding:2px"><table id="xskp-ddv-' + index + '"></table></div>';
        },
        onExpandRow: function(index,row){
            $('#xskp-ddv-'+index).datagrid({
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
	
	
	xskp_xsthDg = $('#jxc_xskp_xsthDg').datagrid({
		fit : true,
	    border : false,
	    //remoteSort: false,
// 	    fitColumns: true,
// 	    singleSelect: true, 
	    pagination : true,
		pageSize : pageSize,
		pageList : pageList,
		columns:[[
			{field:'id',title:'销售提货DetId',align:'center',checkbox:true},
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
			{field:'kpsl',title:'开票数量',align:'center',
				formatter: function(value){
					return value == 0 ? '' : value;
				},
				styler:function(){
					return 'color:red;';
				}},
			{field:'thsl',title:'原提货数量',align:'center',
				formatter: function(value){
					return value == 0 ? '' : value;
				},
				styler:function(){
					return 'color:blue;';
				}},
			{field:'zdwdj',title:'单价1',align:'center'},
// 			{field:'zdwytsl',title:'开票数量',align:'center',
// 				formatter: function(value){
// 					return value == 0 ? '' : value;
// 				},
// 				styler:function(){
// 					return 'color:red;';
// 				}},
			{field:'cjldwmc',title:'单位2',align:'center'},
			{field:'cdwsl',title:'数量2',align:'center'},
			{field:'cdwdj',title:'单价2',align:'center'},
			{field:'bookmc',title:'书名',align:'center',
				//formatter: function(value){
				//	return lnyw.memo(value, 15);
				//}
			},
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
			{field:'xskplshs',title:'销售开票',align:'center',
				formatter: function(value){
					return lnyw.memo(value, 15);
				}},
	    ]],
	    toolbar:'#jxc_xskp_xsthTb',
	});
	lnyw.toolbar(2, xskp_xsthDg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', xskp_did);
	
// 	xskp_xsthDg.datagrid({
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
//                     {field:'sppc',title:'批次',width:100,align:'center'},
//                     {field:'zjldwmc',title:'单位1',width:100,align:'center'},
//                     {field:'zdwsl',title:'数量1',width:100,align:'center'},
//                     {field:'cjldwmc',title:'单位2',width:100,align:'center'},
//                     {field:'cdwsl',title:'数量2',width:100,align:'center'},
//                 ]],
//                 onResize:function(){
//                 	xskp_xsthDg.datagrid('fixDetailRowHeight',index);
//                 },
//                 onLoadSuccess:function(){
//                     setTimeout(function(){
//                     	xskp_xsthDg.datagrid('fixDetailRowHeight',index);
//                     },0);
//                 }
//             });
//             xskp_xsthDg.datagrid('fixDetailRowHeight',index);
//         }
//     });
	
	//选中列表标签后，装载数据
	xskp_tabs = $('#jxc_xskp_tabs').tabs({
		onSelect: function(title, index){
			if(index == 1){
				xskp_dg.datagrid({
					url: '${pageContext.request.contextPath}/jxc/xskpAction!datagrid.action',
					queryParams:{
						bmbh: xskp_did,
						createTime: countXskp == 0 ? undefined : $('input[name=createTimeXskp]').val(),
						search: countXskp == 0 ? undefined : $('input[name=searchXskp]').val(),
					}
				});
				countXskp++;
			}
			if(index == 2){
				xskp_xsthDg.datagrid({
					url: '${pageContext.request.contextPath}/jxc/xsthAction!datagridDet.action',
					queryParams: {
						bmbh: xskp_did,
						fromOther: 'fromXskp',
// 						createTime: countXsthInXskp == 0 ? undefined : $('input[name=createTimeXsthInXskp]').val(),
						createTime: $('input[name=createTimeXsthInXskp]').val(),
// 						search: countXsthInXskp == 0 ? undefined : $('input[name=searchXsthInXskp]').val(),
						search: $('input[name=searchXsthInXskp]').val(),
						isKp : '1'
						},
				});
				countXsthInXskp++;
			}
			
		},
	});
	
	xskp_spdg = $('#jxc_xskp_spdg').datagrid({
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
	        {field:'zjldwmc',title:'单位1',width:25,align:'center',editor:'textRead'},
	        {field:'zdwthsl',title:'提货数量1',width:25,align:'center',editor:'textRead'},
	        {field:'zdwytsl',title:'已开票数量1',width:25,align:'center',editor:'textRead'},
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
	        {field:'cdwthsl',title:'提货数量2',width:25,align:'center',editor:'textRead'},
	        {field:'cdwytsl',title:'已开票数量2',width:25,align:'center',editor:'textRead'},
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
   			{field:'spse',title:'税额',width:25,align:'center',
   	        	editor:{
           			type:'numberbox',
           			options:{
           				precision: 2
           			}}},
   			{field:'sphj',title:'合计',width:25,align:'center',
   	        	editor:{
           			type:'numberbox',
           			options:{
           				precision: 2
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
	
	//$('#jxc_xskp_tabs a.tabs-inner').css('height','100px');
	//$('#jxc_xskp_tabs span.tabs-title').css('white-space','normal');
	
	
	//初始化创建时间
	$('#createDate').html(moment().format('YYYY年MM月DD日'));
	
	
	$('input[name=khmc]').change(function(){
// 		var isCheck = true;
// 		if($('input#sxCheck').is(':checked')){
// 			isCheck = checkKh();
// 		}
// 		if(isCheck){
// 			loadKh($('input[name=khbh]').val().trim());
// 		}
		loadKh($('input[name=khbh]').val().trim());
	});
	

	$('input[name=isFh]').click(function(){
		if($(this).is(':checked')){
			$('.fh').css('display','table-cell');
			//初始化分户列表
			if(jxc_xskp_fhCombo == undefined){
				jxc_xskp_fhCombo = lnyw.initCombo($("#jxc_xskp_fhId"), 'id', 'fhmc', '${pageContext.request.contextPath}/jxc/fhAction!listFhs.action?depId=' + xskp_did);
			}else{
				jxc_xskp_fhCombo.combobox('selectedIndex', 0);
			}
		}else{
			$('.fh').css('display','none');
		}
	});
	
	//初始化仓库列表
	jxc_xskp_ckCombo = lnyw.initCombo($("#jxc_xskp_ckId"), 'id', 'ckmc', '${pageContext.request.contextPath}/jxc/ckAction!listCk.action?depId=' + xskp_did);
	
	//初始化业务员列表
	jxc_xskp_ywyCombo = lnyw.initCombo($("#jxc_xskp_ywyId"), 'id', 'realName', '${pageContext.request.contextPath}/admin/userAction!listYwys.action?did=' + xskp_did);
	
	//初始化付款方式列表
	jxc_xskp_jsfsCombo = lnyw.initCombo($("#jxc_xskp_jsfsId"), 'id', 'jsmc', '${pageContext.request.contextPath}/jxc/jsfsAction!listJsfs.action');	
	
	$('input').focusin(function(){
		if(editIndex != undefined){
			xskp_spdg.datagrid('endEdit', editIndex);
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
	
	
	//初始化信息
	init();
});

//以下为商品列表处理代码
function init(){
	//清空列表，添加一个空行
	xskp_spdg.datagrid({
		data: [{}],
	});
	
	//清空全部字段
	$('input#thfs_zt').attr('ckecked', 'checked');
	$('.isSh').css('display','none');
	$('#info input').val('');
// 	$('input').val('');
	//$('input:checkbox').removeAttr('checked');
	//$('input:checkbox').removeProp('checked');
	$('input:checkbox').prop('checked', false);
	$('.fh').css('display','none');
	
	jxc_xskp_ckCombo.combobox('clear');
	jxc_xskp_ckCombo.combobox('selectedIndex', 0);
	
	//jxc_xskp_ywyCombo.combobox('selectedIndex', 0);
	//jxc_xskp_jsfsCombo.combobox('selectedIndex', 0);
	jxc_xskp_jsfsCombo.combobox('setValue', JSFS_QK);
	
	//收回商品库存信息
	jxc.hideKc('#jxc_xskp_layout');
	//xskp_spdg.datagrid('fitColumns');
	jxc.spInfo($('#jxc_xskp_layout'), '');
	
	//初始化流水号
	$.ajax({
		type: "POST",
		url: '${pageContext.request.contextPath}/jxc/lshAction!getLsh.action',
		data: {
			bmbh: xskp_did,
			lxbh: xskp_lx,
		},
		dataType: 'json',
		success: function(d){
			if(d.success){
				$('#xskpLsh').html(d.obj);
			}  
		},
	});
	
	//根据权限，动态加载功能按钮
	lnyw.toolbar(0, xskp_spdg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', xskp_did);
	
	//清空合计内容
	xskp_spdg.datagrid('reloadFooter',[{}]);
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
	if(spmcEditor.target.val().length > 0){
		return true;
	}
	return false;
}

//根据指定行，进入编辑状态
function enterEdit(rowIndex, isClick){
	//如果选中行为最后一行，先增加一个空行
	if(rowIndex == xskp_spdg.datagrid('getRows').length - 1){
		xskp_spdg.datagrid('appendRow', {});
	}else{
		if(!isClick){
			return false;
		}
	}
	//将选中行标识为编辑行
	editIndex = rowIndex;
	//选择编辑行，并进行入编辑状态
	xskp_spdg.datagrid('selectRow', editIndex)
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
				xskp_spdg.datagrid('endEdit', editIndex);
				//将选中行转为编辑状态
				enterEdit(rowIndex, true);
			}else{ //行验证未通过
				//验证关键字段信息
				//通过，不做任何操作，将该行继续完成或删除该行
				if(!keyOk()){ //未通过，将该行删除
					if(rowIndex > editIndex){
						rowIndex = rowIndex - 1;
					}
					removeit();
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
    xskp_spdg.datagrid('cancelEdit', editIndex)
            .datagrid('deleteRow', editIndex);
    editIndex = undefined;
    updateFooter();
    jxc.hideKc('#jxc_xskp_layout');
    //xskp_spdg.datagrid('fitColumns');
}

//保存行
function accept(){
    if (rowOk()){
    	xskp_spdg.datagrid('acceptChanges');
    	jxc.hideKc('#jxc_xskp_layout');
    	//xskp_spdg.datagrid('fitColumns');
    }
}

//取消编辑行
function cancelAll(){
	xskp_spdg.datagrid('rejectChanges');
    editIndex = undefined;
    updateFooter();
    jxc.hideKc('#jxc_xskp_layout');
    //xskp_spdg.datagrid('fitColumns');
}

//提交数据到后台
function saveAll(){
	var msg = formValid();
	if(msg == ''){
		//编辑行是否完成
		if(rowOk()){
			xskp_spdg.datagrid('endEdit', editIndex);
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
	
	var rows = xskp_spdg.datagrid('getRows');
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
	
	var effectRow = new Object();
	//if(!$('input[name=isZs]').is(':checked') && !$('input[name=isFh]').is(':checked') && $('input[name=xsthDetIds]').val() == ''){
	if(!$('input[name=isZs]').is(':checked') && $('input[name=xsthDetIds]').val() == ''){
		$.messager.defaults.ok = '是';
		$.messager.defaults.cancel = '否';
		$.messager.confirm('确认', '是否同步生成提货单?', function(r){
			if (r){
				effectRow['needXsth'] = '1';
			}
			doSave();
		});
	}else{
		doSave();
	}
	
	function doSave(){
		var footerRows = xskp_spdg.datagrid('getFooterRows');
		//将表头内容传入后台
		if($('input#isNsr').is(':checked')){
			effectRow['fplxId'] = '1';
			effectRow['fplxmc'] = '增值税';
		}else{
			effectRow['fplxId'] = '0';
			effectRow['fplxmc'] = '普通';
		}
		
// 		effectRow['isSx'] = $('input[name=isSx]').is(':checked') ? '1' : '0';
		effectRow['isSx'] = '0';
		effectRow['isZs'] = $('input[name=isZs]').is(':checked') ? '1' : '0';
		if($('input[name=isFh]').is(':checked')){
			effectRow['isFh'] =  '1';
			effectRow['fhId'] = jxc_xskp_fhCombo.combobox('getValue');
			effectRow['fhmc'] = jxc_xskp_fhCombo.combobox('getText');
		}
		if($('input#thfs_sh').is(':checked')){
			effectRow['thfs'] = '0';
			effectRow['shdz'] = $('input[name=shdz]').val();
		}else{
			effectRow['thfs'] = '1';
			effectRow['ch'] = $('input[name=ch]').val();
			effectRow['thr'] = $('input[name=thr]').val();
		}
		
		if(jxc_xskp_fyrCombo == undefined || jxc_xskp_fyrCombo.combobox('getText') == ''){
			effectRow['fyr'] = $('input[name=fyr]').val();
		}else{
			effectRow['fyr'] = jxc_xskp_fyrCombo.combobox('getText');
		}
		effectRow['bookmc'] = $('input[name=bookmc]').val();
		effectRow['khbh'] = $('input[name=khbh]').val();
		effectRow['khmc'] = $('input[name=khmc]').val();
		effectRow['sh'] = $('input[name=sh]').val();
		effectRow['khh'] = $('input[name=khh]').val();
		effectRow['dzdh'] = $('input[name=dzdh]').val();
		effectRow['ckId'] = jxc_xskp_ckCombo.combobox('getValue');
		effectRow['ckmc'] = jxc_xskp_ckCombo.combobox('getText');
		effectRow['ywyId'] = jxc_xskp_ywyCombo.combobox('getValue');
		effectRow['ywymc'] = jxc_xskp_ywyCombo.combobox('getText');
		effectRow['jsfsId'] = jxc_xskp_jsfsCombo.combobox('getValue');
		effectRow['jsfsmc'] = jxc_xskp_jsfsCombo.combobox('getText');
		effectRow['bz'] = $('input[name=jxc_xskp_bz]').val();
		effectRow['hjje'] = lnyw.delcommafy(footerRows[0]['spje']); 
		effectRow['hjse'] = lnyw.delcommafy(footerRows[0]['spse']); 
		effectRow['xsthDetIds'] = $('input[name=xsthDetIds]').val();
		effectRow['bmbh'] = xskp_did;
		effectRow['lxbh'] = xskp_lx;
		effectRow['menuId'] = xskp_menuId;
		
		//将表格中的数据去掉最后一个空行后，转换为json格式
		effectRow['datagrid'] = JSON.stringify(rows.slice(0, rows.length - 1));
		//提交到action
		//$.ajaxSettings.traditional=true;
		$.ajax({
			type: "POST",
			url: '${pageContext.request.contextPath}/jxc/xskpAction!save.action',
			data: effectRow,
			dataType: 'json',
			success: function(rsp){
				if(rsp.success){
			    	$.messager.show({
						title : '提示',
						msg : '提交成功！',
					});
			    	init();
				}
				$.messager.confirm('请确认', '是否导出销售单据到金穗？', function(r) {
					if (r) {
						var url = lnyw.bp() + '/jxc/xskpAction!toJs.action?xskplsh=' + rsp.obj.xskplsh;
						jxc.toJs(url, rsp.obj.fplxId);
					}
				});
			},
			error: function(){
				$.messager.alert("提示", "提交错误了！");
			}
		});
	}
}

//处理编辑行
function setEditing(){
		
	//加载字段
	var editors = xskp_spdg.datagrid('getEditors', editIndex);
    spbhEditor = editors[0];
    spmcEditor = editors[1];
    spcdEditor = editors[2];
    spppEditor = editors[3];
    spbzEditor = editors[4];
    zjldwEditor = editors[5];
    zthslEditor = editors[6];
    zytslEditor = editors[7];
    zslEditor = editors[8];
    zdjEditor = editors[9];
    cjldwEditor = editors[10];
    cthslEditor = editors[11];
    cytslEditor = editors[12];
    cslEditor = editors[13];
    cdjEditor = editors[14];
    spjeEditor = editors[15];
    spseEditor = editors[16];
    sphjEditor = editors[17];
    zhxsEditor = editors[18];
    zjldwIdEditor = editors[19];
    cjldwIdEditor = editors[20];
    
    if($(spbhEditor.target).val() != ''){
    	jxc.spInfo($('#jxc_xskp_layout'), '1', $(spppEditor.target).val(), $(spbzEditor.target).val());
//     	var fhValue = '';
//     	if($('input[name=isFh]').is(':checked')){
//     		fhValue = jxc_xskp_fhCombo.combobox('getValue');
//     	}
    	jxc.showKc('#jxc_xskp_layout', 
    			'${pageContext.request.contextPath}/jxc/xskpAction!getSpkc.action', 
    			{
    				bmbh : xskp_did, 
    				ckId : jxc_xskp_ckCombo.combobox('getValue'),
//     				fhId : fhValue,
    				spbh : $(spbhEditor.target).val(),
    			});
    }else{
    	jxc.spInfo($('#jxc_xskp_layout'), '');
    	jxc.hideKc('#jxc_xskp_layout');
    }
    //xskp_spdg.datagrid('fitColumns');
    
	//loadEditor();
	//处理编辑行的换行事件
	$('.datagrid-row-editing').on('keydown','input', function(event){
		//监听按下向下箭头
		if(event.keyCode == 40){
			var rowIndex = editIndex;
			if(rowOk()){
				xskp_spdg.datagrid('endEdit', editIndex);
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
    	if(event.which == 9){
    		if($(this).val().trim().length == 7){
    			if(!existKey($(this).val(), editIndex)){
    				$.ajax({
    					url:'${pageContext.request.contextPath}/jxc/spAction!loadSp.action',
    					async: false,
    					context:this,
    					data:{
    						spbh: $(this).val(),
    						depId: xskp_did
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
    				//将商品库存信息展开
    				$('#jxc_xskp_layout').layout('expand', 'east');
    			}else{
    				$.messager.alert('提示', '商品编号只能出现一次，请重新输入！', 'error');
    			}
    		}else{
    			$.messager.alert('提示', '商品编号必须是7位，请重新输入！', 'error');
    		}
    		return false;
    	}
    	//按ESC键，弹出对话框，可以按商品编号或名称查询，双击商品行返回信息
    	if(event.which == 27){
    		jxc.spQuery($(spbhEditor.target).val(),
    				xskp_did,
    				'${pageContext.request.contextPath}/jxc/spQuery.jsp',
    				'${pageContext.request.contextPath}/jxc/spAction!spDg.action',
    				zslEditor);
// 			zslEditor.target.focus();
    		return false;
    	}
    });
    
    
    
    //输入主单位数量后，计算次单位数量
    zslEditor.target.bind('keyup', function(event){
	    var kcRow = $('#show_spkc').propertygrid("getRows");
	    
    	var kxssl = undefined;
    	if(kcRow == undefined){
    		kxssl = Number(0);
    	}else{
    		kxssl = Number(kcRow[0].value);
    	}
    	var zsl = Number($(zslEditor.target).val());
    	if(zsl > kxssl){
    		$.messager.alert("提示", "开票数量不能大于可销售数量，请重新输入！");
    		$(zslEditor.target).numberbox('setValue', 0);
    		zslEditor.target.focus();
    		return false;
    	}
    	var wksl = (Number($(zthslEditor.target).val()) - Number($(zytslEditor.target).val())).toFixed(LENGTH_SL);
    	if(Number($(zslEditor.target).val()) > wksl && wksl > 0){
    		$.messager.alert("提示", "开票数量不能大于未开票数量，请重新输入！");
    		$(zslEditor.target).numberbox('setValue', 0);
    		zslEditor.target.focus();
    		return false;
    	}
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
    	if((event.keyCode >= 48 && event.keyCode <= 57) || event.keyCode == 190 ){
    		if($(zhxsEditor.target).val() != 0){
    			$(zslEditor.target).numberbox('setValue', $(cslEditor.target).val() * $(zhxsEditor.target).val());
    		}
    		calculate();
    	}else{
    		return false;
    	}
    }).bind('keydown', function(event){
     	if(event.keyCode == 9){
     		cdjEditor.target.focus();
     		return false;
     	}
    });
    
    cdjEditor.target.bind('focus', function(event){
    	if($(cdjEditor.target).val() == 0 || $(cdjEditor.target).val() == NaN){
    		$(cdjEditor.target).val('');
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
     		sphjEditor.target.focus();
     	}
    });
  	
  	//输入合计金额后，计算单价
    sphjEditor.target.bind('keyup', function(event){
    	if(event.keyCode == 9){
     		return false;
     	}
    	changeSpje();
    }).bind('keydown', function(event){
    	changeSpje();
    	if(event.keyCode == 40){
     		//spjeEditor.target.focus();
     	}
    });
  	
  	function changeSpje(){
  		$(spjeEditor.target).numberbox('setValue', $(sphjEditor.target).val() / (1 + SL));
    	$(spseEditor.target).numberbox('setValue', $(sphjEditor.target).val() - $(spjeEditor.target).val());
    	$(zdjEditor.target).numberbox('setValue', $(spjeEditor.target).val() / $(zslEditor.target).val());
    	if($(zhxsEditor.target).val() != 0){
	    	$(cdjEditor.target).numberbox('setValue', $(sphjEditor.target).val() / $(cslEditor.target).val());
    	}
    	updateFooter();
  	}
    
    //计算金额
    function calculate(){
    	var spje = (zslEditor.target.val() * zdjEditor.target.val()).toFixed(2);
    	var spse = (spje * SL).toFixed(2);
        $(spjeEditor.target).numberbox('setValue', spje);
        $(spseEditor.target).numberbox('setValue', spse);
        $(sphjEditor.target).numberbox('setValue', Number(spje) + Number(spse));
        //更新汇总列
        updateFooter();
    }
  	
    //初始进入编辑状态时，使用商品编号获得焦点
    spbhEditor.target.focus();
}
//求和
function updateFooter(){
	var rows = xskp_spdg.datagrid('getRows');
	//var spmc_footer = '合计';
	var hjje = 0;
	var hjse = 0;
	$.each(rows, function(){
		var index = xskp_spdg.datagrid('getRowIndex', this);
		if(index < rows.length - 1){
			if(editIndex == index){
				hjje += Number(spjeEditor.target.val());
				hjse += Number(spseEditor.target.val());
			}else{
				hjje += Number(this.spje == undefined ? 0 : this.spje);
				hjse += Number(this.spse == undefined ? 0 : this.spse);
			}
		}
 	});
	xskp_spdg.datagrid('reloadFooter',
			[{
				spmc : '合计',
				spje : lnyw.formatNumberRgx(hjje.toFixed(2)),
				spse : lnyw.formatNumberRgx(hjse.toFixed(2)),
				sphj : lnyw.formatNumberRgx((hjje + hjse).toFixed(2))
			}
// 			,{
// 				spmc: '总计',
// 				sphj: (hjje + hjse).toFixed(LENGTH_JE)
// 			}
			]);
}

//验证记录是否已存在
function existKey(value, rowIndex){
	var keys = xskp_spdg.datagrid('getRows');
	var exist = false;
	if(keys.length > 2){
		$.each(keys, function(){
			var index = xskp_spdg.datagrid('getRowIndex', this);
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
	if($('input[name=khmc]').val() == ''){
		message += '客户信息<br>';
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
	zdjEditor.target.val(rowData.xsdj);
	if(rowData.zhxs != 0){
		cdjEditor.target.val(rowData.xsdj * rowData.zhxs * (1 + SL));
	}
	
	jxc.spInfo($('#jxc_xskp_layout'), '1', $(spppEditor.target).val(), $(spbzEditor.target).val());
// 	var fhValue = '';
// 	if($('input[name=isFh]').is(':checked')){
// 		fhValue = jxc_xskp_fhCombo.combobox('getValue');
// 	}
	jxc.showKc('#jxc_xskp_layout', 
			'${pageContext.request.contextPath}/jxc/xskpAction!getSpkc.action', 
			{
				bmbh : xskp_did, 
				ckId : jxc_xskp_ckCombo.combobox('getValue'),
// 				fhId : fhValue,
				spbh : $(spbhEditor.target).val(),
			});
	//xskp_spdg.datagrid('fitColumns');
}

function checkKh(){
	$.ajax({
		url:'${pageContext.request.contextPath}/jxc/khAction!checkKh.action',
		async: false,
		data:{
			khbh: $('input[name=khbh]').val(),
			depId: xskp_did,
		},
		dataType:'json',
		success:function(data){
			if(!data.success){
				$.messager.alert('提示', data.msg, 'error');
				$('input[name=khbh]').val('');
				$('input[name=khmc]').val('');
				$('input[name=sh]').val('');
				$('input[name=khh]').val('');
				$('input[name=dzdh]').val('');
				$('input[name=khbh]').focus();
				return false;
			}
		}
	});
	return true;
	
}

function loadKh(khbh){
	$.ajax({
		url:'${pageContext.request.contextPath}/jxc/khAction!loadKh.action',
		async: false,
		cache: false,
		context:this,
		data:{
			khbh: khbh,
			depId: xskp_did,
		},
		dataType:'json',
		success:function(data){
			if(data.success){
				//设置信息字段值
				$('input[name=khmc]').val(data.obj.khmc);
				$('input[name=sh]').val(data.obj.sh);
				$('input[name=khh]').val(data.obj.khh);
				$('input[name=dzdh]').val(data.obj.dzdh);
				jxc_xskp_ywyCombo.combobox('setValue', data.obj.ywyId);
// 				if(data.obj.isSx == '1'){
// 					$('input[name=isSx]').prop('checked', 'ckecked');
// 				}
				if(data.obj.isNsr == '1'){
					$('input#isNsr').attr('checked', 'checked');
					$('input#isNsr').prop('checked', 'checked');
				}else{
					$('input#isNoNsr').attr('checked', 'checked');
					$('input#isNoNsr').prop('checked', 'checked');
				}
				if(xskp_did == '04'){
					//初始化发印人列表
					jxc_xskp_fyrCombo = lnyw.initCombo($("#jxc_xskp_fyr"), 'fyr', 'fyr', '${pageContext.request.contextPath}/jxc/xskpAction!listFyrs.action?bmbh=' + xskp_did + '&khbh=' + khbh);
				}
			}else{
				$.messager.alert('提示', '客户信息不存在！', 'error');
			}
		}
	});
}

function khLoad(){
	switch(event.keyCode){
	case 27:
		//不再进行判断，2014-06-22
		//是否授信客户
// 		var params = '';
// 		if($('input#sxCheck').is(':checked') ){
// 			params += '?isSx=1&depId=' + xskp_did;
// 			if($('input#isNsr').is(':checked')){
// 				params += '&isNsr=1';
// 			}
// 		}else{
// 			if($('input#isNsr').is(':checked')){
// 				params += '?isNsr=1';
// 			}
// 		}
		jxc.query('客户检索', $('input[name=khbh]'), $('input[name=khmc]'), 
				'${pageContext.request.contextPath}/jxc/query.jsp',
				'${pageContext.request.contextPath}/jxc/khAction!khDg.action');
// 				'${pageContext.request.contextPath}/jxc/khAction!khDg.action' + params);
		break;
	case 9:
		break;
	default:
		if($('input[name=khbh]').val().trim().length == 0){
			$('input[name=khmc]').val('');
			$('input[name=sh]').val('');
			$('input[name=khh]').val('');
			$('input[name=dzdh]').val('');
		}
		if($('input[name=khbh]').val().trim().length == 8){
			loadKh($('input[name=khbh]').val().trim());
		}
		break;
	}
}
//////////////////////////////////////////////以上为商品列表处理代码

//////////////////////////////////////////////以下为销售开票列表处理代码
function cjXskp(){
	var row = xskp_dg.datagrid('getSelected');
	if (row != undefined) {
		if(row.isCj != '1'){
			if(row.isHk == '0'){
				if(row.ywrklsh == undefined){
					if(row.fromTh == '1' || (row.xsthlshs == undefined || row.xsthlshs.trim == '')){
						$.messager.prompt('请确认', '是否要冲减选中的销售开票单？请填写备注', function(bz){
							if (bz != undefined) {
								$.ajax({
									url : '${pageContext.request.contextPath}/jxc/xskpAction!cjXskp.action',
									data : {
										xskplsh : row.xskplsh,
										bmbh: xskp_did,
										lxbh: xskp_lx,
										bz: bz,
									},
									method: 'post',
									dataType : 'json',
									success : function(d) {
										xskp_dg.datagrid('load');
										xskp_dg.datagrid('unselectAll');
										$.messager.show({
											title : '提示',
											msg : d.msg
										});
									}
								});
							}
						});
					}else{
						$.messager.alert('警告', '选中的销售记录已进行提货，请重新选择！',  'warning');
					}
				}else{
					$.messager.alert('警告', '选中的销售记录已进行内部入库，请重新选择！',  'warning');
				}
			}else{
				$.messager.alert('警告', '选中的销售记录已进行还款，请重新选择！',  'warning');
			}
		}else{
			$.messager.alert('警告', '选中的销售开票记录已被冲减，请重新选择！',  'warning');
		}
	}else{
		$.messager.alert('警告', '请选择一条记录进行操作！',  'warning');
	}
}

function toJs(){
	var rows = xskp_dg.datagrid('getSelections');
	var xskplshs = [];
	if(rows.length > 0){
		var preRow = undefined;
		var flag = true;
	    $.each(rows, function(index){
	    	if(rows[index].isCj == '1'){
	    		$.messager.alert('提示', '选择的销售发票已冲减！', 'error');
	    		return false;
	    	}
	    	xskplshs.push(rows[index].xskplsh);
	    	if(index != 0){
	    		if(this.khbh != preRow.khbh || this.fhId != preRow.fhId){
	    			$.messager.alert('提示', '请选择同一客户或分户的销售发票进行操作！', 'error');
					flag = false;
					//return false;
	    		}else{
	    			preRow = this;
	    		}
	    	}
	    	preRow = this;
	    });
	    if(flag){
	    	$.messager.confirm('请确认', '是否导出数据到金穗接口？', function(r) {
				if (r) {
					var xskplshStr = xskplshs.join(',');
					var url = lnyw.bp() + '/jxc/xskpAction!toJs.action?xskplsh=' + xskplshStr;
					jxc.toJs(url, rows[0].fplxId);
				}
			});
	    }
	}else{
		$.messager.alert('警告', '请选择一条记录进行操作！',  'warning');
	}
	
	
// 	var row = xskp_dg.datagrid('getSelected');
// 	if (row != undefined) {
// 		$.messager.confirm('请确认', '是否导出数据到金穗接口？', function(r) {
// 			if (r) {
// 				var url = lnyw.bp() + '/jxc/xskpAction!toJs.action?xskplsh=' + row.xskplsh;
// 				jxc.toJs(url, row.fplxId);
// 			}
// 		});
// 	}else{
// 		$.messager.alert('警告', '请选择一条记录进行操作！',  'warning');
// 	}
}

function printXsqk(){
	var rows = xskp_dg.datagrid('getSelections');
	var xskplshs = [];
	if (rows != '') {
		var preRow = undefined;
		var flag = true;
	    $.each(rows, function(index){
	    	if(rows[index].isCj == '1'){
	    		$.messager.alert('提示', '选择的销售发票已冲减！', 'error');
	    		flag = false;
	    		return false;
	    	}
	    	if(index != 0){
	    		if(this.khbh != preRow.khbh){
	    			$.messager.alert('提示', '请选择同一客户的销售发票进行操作！', 'error');
					flag = false;
					return false;
	    		}
	    	}
	    	preRow = this;
	    });
	
		if(flag){
			$.messager.confirm('请确认', '已选择' + rows.length + '张发票，是否合并打印销售欠款单？', function(r) {
				if (r) {
					for ( var i = 0; i < rows.length; i++) {
						xskplshs.push(rows[i].xskplsh);
					}
					var xskplsh = xskplshs.join(',');
				
					var url = lnyw.bp() + '/jxc/xskpAction!printXsqk.action?xskplsh=' + xskplsh + "&bmbh=" + xskp_did;
					jxc.print(url, PREVIEW_REPORT, HIDE_PRINT_WINDOW);
				}
			});
		}
	}else{
		$.messager.alert('警告', '请选择一条记录进行操作！',  'warning');
	}
}

function createXsth(){
	var row = xskp_dg.datagrid('getSelected');
	if (row != undefined) {
		if(row.isCj != '1'){
			if(row.xsthlshs == undefined){
				//if(row.fhId == undefined){
					$.messager.prompt('请确认', '是否要生成销售提货单？', function(){
		 				//if (bz != undefined) {
							$.ajax({
								url : '${pageContext.request.contextPath}/jxc/xskpAction!createXsth.action',
								data : {
									xskplsh : row.xskplsh,
									bmbh: xskp_did,
									lxbh: xskp_lx,
									menuId: xskp_menuId,
									//bz: bz,
								},
								method: 'post',
								dataType : 'json',
								success : function(d) {
									xskp_dg.datagrid('reload');
									xskp_dg.datagrid('unselectAll');
									$.messager.show({
										title : '提示',
										msg : d.msg
									});
								}
							});
						//}
					});
// 				}else{
// 					$.messager.alert('警告', '选中的销售开票记录为分户销售，不能生成销售提货单,请重新选择！',  'warning');
// 				}
			}else{
				$.messager.alert('警告', '选中的销售开票记录已关联销售提货,请重新选择！',  'warning');
			}
		}else{
			$.messager.alert('警告', '选中的销售开票记录已被冲减，请重新选择！',  'warning');
		}
	}else{
		$.messager.alert('警告', '请选择一条记录进行操作！',  'warning');
	}
}

function searchXskp(){
	xskp_dg.datagrid('load',{
		bmbh: xskp_did,
		createTime: $('input[name=createTimeXskp]').val(),
		search: $('input[name=searchXskp]').val(),
	});
}

//////////////////////////////////////////////以上为销售开票列表处理代码


//////////////////////////////////////////////以下为销售提货列表处理代码

function generateXskp(){
	var rows = xskp_xsthDg.datagrid('getSelections');
	var xsthDetIds = [];
	var flag = true;
	if(rows.length > 0){
// 		if(rows.length > 1){
//     		var preRow = undefined;
// 		    $.each(rows, function(index){
// 		    	if(index != 0){
// 		    		if(this.khbh != preRow.khbh){
// 		    			$.messager.alert('提示', '请选择同一客户的销售提货单的进行开票！', 'error');
// 						flag = false;
// 						return false;
// 		    		}else{
// 		    			preRow = this;
// 		    		}
// 		    	}
// 		    	preRow = this;
// 		    });
//     	}
		if(flag){
			$.messager.confirm('请确认', '是否要将选中记录进行销售开票？', function(r) {
				if (r) {
					for ( var i = 0; i < rows.length; i++) {
						xsthDetIds.push(rows[i].id);
					}
					var xsthDetStr = xsthDetIds.join(',');
					$.ajax({
						url : '${pageContext.request.contextPath}/jxc/xsthAction!toXskp.action',
						data : {
							xsthDetIds : xsthDetStr
						},
						dataType : 'json',
						success : function(d) {
							$('input[name=khbh]').val(rows[0].khbh);
							$('input[name=khmc]').val(rows[0].khmc);
							$('input[name=bookmc]').val(rows[0].bookmc);
							$('input[name=jxc_xskp_bz]').val(rows[0].bz);
							jxc_xskp_ckCombo.combobox('setValue', rows[0].ckId);
							jxc_xskp_ywyCombo.combobox('setValue', rows[0].ywyId);
							
// 							if(rows[0].isSx == '1'){
// 								$('input[name=isSx]').attr('checked', 'ckecked');
// 							}
							
							xskp_spdg.datagrid('loadData', d.rows);
	// 						updateFooter();
							$('input[name=xsthDetIds]').val(xsthDetStr);
							xskp_tabs.tabs('select', 0);
						}
					});
				}
			});
		}
	}else{
		$.messager.alert('警告', '请选择最少一条记录进行操作！',  'warning');
	}
}

function searchXsthInXskp(){
	xskp_xsthDg.datagrid('load',{
		bmbh: xskp_did,
		createTime: $('input[name=createTimeXsthInXskp]').val(),
		search: $('input[name=searchXsthInXskp]').val(),
		fromOther: 'fromXskp',
		isKp: '1'
	});
}

//////////////////////////////////////////////以上为销售提货列表处理代码

</script>

<!-- tabPosition:'left', headerWidth:'35' -->
<div id="jxc_xskp_tabs" class="easyui-tabs" data-options="fit:true, border:false," style="width:100%;height:100%;">
	
    <div title="新增记录" data-options="closable:false">
        <div id='jxc_xskp_layout' style="height:100%;width=100%">
			<div  class="tinfo" id="info" data-options="region:'north',title:'单据信息',border:false,collapsible:false" style="width:100%;height:200px">		
				<table>
					<tr>
						<td colspan="2">
							增值税发票<input type="radio" name="fplxId" value="1" id="isNsr" checked="checked">
							&nbsp;&nbsp;
							普通发票<input type="radio" name="fplxId" value="0" id="isNoNsr"></td>
						<td colspan="2">
<!-- 							授信<input id="sxCheck" type="checkbox" name="isSx">&nbsp;&nbsp;&nbsp; -->
							直送<input id="zsCheck" type="checkbox" name="isZs">&nbsp;&nbsp;&nbsp;
							分户<input id="fhCheck" type="checkbox" name="isFh">
						</td>
						<th class="read">时间</th><td><div id="createDate" class="read"></div></td>
						<th class="read">单据号</th><td><div id="xskpLsh" class="read"></div></td>
					</tr>
					<tr>
						<th>客户编码</th><td><input name="khbh" class="easyui-validatebox"
							data-options="validType:['mustLength[8]','integer']" onkeyup="khLoad()"></td>
						<th class="read">客户名称</th><td colspan="3"  class="read"><input name="khmc" readonly="readonly" style="width:100%"></td>
					</tr>
					<tr>
						<th class="read">税号</th><td class="read"><input name="sh" readonly="readonly"></td>
						<th class="read">开户行账号</th><td class="read"><input name="khh" readonly="readonly"></td>
						<th class="read">地址电话</th><td class="read"><input name="dzdh" readonly="readonly"></td>
					</tr>
					<tr>
						<th>结算方式</th><td><input id="jxc_xskp_jsfsId" name="jsfsId" type="text"></td>
						<th>业务员</th><td><input id="jxc_xskp_ywyId" name="ywyId" type="text"></td>
						<th>仓库</th><td><input id="jxc_xskp_ckId" name="ckId" type="text"></td>
						<th class="fh" style="display:none">分户</th><td class="fh" style="display:none"><input id="jxc_xskp_fhId" name="fhId"></td>
					</tr>
					<tr>
						<th>发印人</th><td><input id="jxc_xskp_fyr" name="fyr" type="text"></td>
						<th>书名</th><td colspan="3"><input name="bookmc" type="text" style="width:71%"></td>
						<td colspan="2" align="right">自提<input type="radio" name="thfs" id='thfs_zt' checked="checked" value="1">送货<input type="radio" name="thfs" id="thfs_sh" value="0"></td>
						<th class="isZt">车号</th><td class="isZt"><input name="ch" size="10"><th class="isZt">提货人</th><td class="isZt"><input name="thr" size="10"></td>
						<td class="isSh" style="display:none" colspan="2">送货地址<input name="shdz" size="20"></td>
					</tr>
					<tr>
						<th>备注</th><td colspan="7"><input name="jxc_xskp_bz" style="width:90%"></td>
					</tr>
				</table>
				<input name="xsthDetIds" type="hidden">
			</div>
			<div data-options="region:'center',title:'商品信息',split:true" style="width:150px">		
				<table id='jxc_xskp_spdg'></table>
			</div>
			<div data-options="region:'east',title:'商品库存信息',split:true,collapsed:true," style="width:150px">
				<table id="show_spkc"></table>
			</div>
		</div>
    </div>
    <div title="销售列表" data-options="closable:false" >
    	<div id='jxc_xskp_dg'></div>
    </div>
	<div title="销售提货列表" data-options="closable:false" >
		<div id='jxc_xskp_xsthDg'></div>
	</div>
</div>

<div id="jxc_xskp_tb" style="padding:3px;height:auto">
	请输入查询起始日期:<input type="text" name="createTimeXskp" class="easyui-datebox" data-options="value: moment().date(1).format('YYYY-MM-DD')" style="width:100px">
	输入流水号、客户编号、名称、业务员、备注：<input type="text" name="searchXskp" style="width:100px">
	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchXskp();">查询</a>
</div>
<div id="jxc_xskp_xsthTb" style="padding:3px;height:auto">
	请输入查询起始日期:<input type="text" name="createTimeXsthInXskp" class="easyui-datebox" data-options="value: moment().date(1).format('YYYY-MM-DD')" style="width:100px">
	输入流水号、客户编号、名称、业务员、备注：<input type="text" name="searchXsthInXskp" style="width:100px">
	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchXsthInXskp();">查询</a>
</div>
