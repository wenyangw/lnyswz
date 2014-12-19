<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>


<script type="text/javascript">
var did;
var lx;
var menuId;

var ywpd_spdg;
var ywpd_dg;
// var ywpd_kfpdDg;
var editIndex = undefined;
var ywpd_tabs;

var jxc_ywpd_ckCombo;
var jxc_ywpd_pdlxCombo;

//编辑行字段
var spbhEditor;   
var spmcEditor;
var spcdEditor;
var spppEditor;
var spbzEditor;
var zjldwEditor;
var zslEditor;
var zdjEditor;
var cjldwEditor;
var spjeEditor;
var zhxsEditor;
var zjldwIdEditor;
var cjldwIdEditor;

$(function(){
	did = lnyw.tab_options().did;
	lx = lnyw.tab_options().lx;
	menuId = lnyw.tab_options().id;
	
	$('#jxc_ywpd_layout').layout({
		fit : true,
		border : false,
	});
	
	ywpd_dg = $('#jxc_ywpd_dg').datagrid({
		fit : true,
	    border : false,
	    singleSelect : true,
	    remoteSort: false,
// 	    fitColumns: true,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
		columns:[[
			{field:'ywpdlsh',title:'流水号',align:'center'},
	        {field:'createTime',title:'时间',align:'center'},
	        {field:'ckId',title:'仓库id',align:'center',hidden:true},
	        {field:'ckmc',title:'仓库名称',align:'center'},
	        {field:'pdlxId',title:'类型id',align:'center',hidden:true},
	        {field:'pdlxmc',title:'方式',align:'center'},
	        {field:'mc',title:'名称',align:'center'},
	        {field:'hjje',title:'金额',align:'center',
	        	formatter: function(value){
	        		return lnyw.formatNumberRgx(value);
	        	}},
	        {field:'bz',title:'备注',align:'center',
        		formatter: function(value){
        			return lnyw.memo(value, 15);
        		}},
        	{field:'kfpdlsh',title:'库房盘点',align:'center',
           		formatter: function(value){
           			return lnyw.memo(value, 15);
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
        	{field:'cjYwpdlsh',title:'原业务入库流水号',align:'center'},
	    ]],
	    toolbar:'#jxc_ywpd_tb',
	});
	lnyw.toolbar(1, ywpd_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', did);
	
	
	ywpd_dg.datagrid({
        view: detailview,
        detailFormatter:function(index,row){
            return '<div style="padding:2px"><table id="ywpd-ddv-' + index + '"></table></div>';
        },
        onExpandRow: function(index,row){
            $('#ywpd-ddv-'+index).datagrid({
                url:'${pageContext.request.contextPath}/jxc/ywpdAction!detDatagrid.action',
                fitColumns:true,
                singleSelect:true,
                rownumbers:true,
                loadMsg:'',
                height:'auto',
                queryParams: {
        			ywpdlsh: row.ywpdlsh,
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
                	ywpd_dg.datagrid('fixDetailRowHeight',index);
                },
                onLoadSuccess:function(){
                    setTimeout(function(){
                    	ywpd_dg.datagrid('fixDetailRowHeight',index);
                    },0);
                }
            });
            ywpd_dg.datagrid('fixDetailRowHeight',index);
        }
    });
	
	
// 	ywpd_kfpdDg = $('#jxc_ywpd_kfpdDg').datagrid({
// 		fit : true,
// 	    border : false,
// // 	    remoteSort: false,
// 	    fitColumns: true,
// // 	    singleSelect: true, 
// 	    pagination : true,
// 		pageSize : pageSize,
// 		pageList : pageList,
// 		columns:[[
// 			{field:'kfpdlsh',title:'流水号',align:'center'},
// 	        {field:'createTime',title:'时间',align:'center'},
// 	        {field:'ckId',title:'仓库id',align:'center',hidden:true},
// 	        {field:'ckmc',title:'仓库名称',align:'center'},
// 	        {field:'lxId',title:'类型id',align:'center',hidden:true},
// 	        {field:'lxmc',title:'方式',align:'center'},
//         	{field:'bz',title:'备注',align:'center',
//         		formatter: function(value){
//         			return lnyw.memo(value, 15);
//         		}},
// 	    ]],
// 	    toolbar:'#jxc_ywpd_kfpdTb',
// 	});
// 	lnyw.toolbar(2, ywpd_kfpdDg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', did);
	
// 	ywpd_kfpdDg.datagrid({
//         view: detailview,
//         detailFormatter:function(index,row){
//             return '<div style="padding:2px"><table id="ywpd-kfpd-ddv-' + index + '"></table></div>';
//         },
//         onExpandRow: function(index,row){
//             $('#ywpd-kfpd-ddv-'+index).datagrid({
//                 url:'${pageContext.request.contextPath}/jxc/kfpdAction!detDatagrid.action',
//                 fitColumns:true,
//                 singleSelect:true,
//                 rownumbers:true,
//                 height:'auto',
//                 queryParams: {
//         			kfpdlsh: row.kfpdlsh,
//         		},
//                 columns:[[
//                     {field:'spbh',title:'商品编号',width:200,align:'center'},
//                     {field:'spmc',title:'名称',width:100,align:'center'},
//                     {field:'spcd',title:'产地',width:100,align:'center'},
//                     {field:'sppp',title:'品牌',width:100,align:'center'},
//                     {field:'spbz',title:'包装',width:100,align:'center'},
//                     {field:'hwId',title:'货位id',width:100,align:'center',hidden:true},
//                     {field:'hwmc',title:'货位',width:100,align:'center'},
//                     {field:'sppc',title:'批次',width:100,align:'center'},
//                     {field:'zjldwmc',title:'单位1',width:100,align:'center'},
//                     {field:'zdwsl',title:'数量1',width:100,align:'center'},
//                     {field:'cjldwmc',title:'单位2',width:100,align:'center'},
//                     {field:'cdwsl',title:'数量2',width:100,align:'center'},
//                     {field:'bzsl',title:'包装数量',width:100,align:'center'},
//                 ]],
//                 onResize:function(){
//                 	ywpd_kfpdDg.datagrid('fixDetailRowHeight',index);
//                 },
//                 onLoadSuccess:function(){
//                     setTimeout(function(){
//                     	ywpd_kfpdDg.datagrid('fixDetailRowHeight',index);
//                     },0);
//                 }
//             });
//             ywpd_kfpdDg.datagrid('fixDetailRowHeight',index);
//         }
//     });
	
	//选中列表标签后，装载数据
	ywpd_tabs = $('#jxc_ywpd_tabs').tabs({
		onSelect: function(title, index){
			if(index == 1){
				ywpd_dg.datagrid({
					url: '${pageContext.request.contextPath}/jxc/ywpdAction!datagrid.action',
					queryParams:{
						bmbh: did,
					}
				});
			}
// 			if(index == 2){
// 				ywpd_kfpdDg.datagrid({
// 					url: '${pageContext.request.contextPath}/jxc/kfpdAction!datagrid.action',
// 					queryParams: {
// 						bmbh: did,
// 						fromOther: 'fromYwpd'
// 						},
// 				});
// 			}
		},
	});
	
	ywpd_spdg = $('#jxc_ywpd_spdg').datagrid({
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
  	        {field:'cjldwmc',title:'单位2',width:25,align:'center',editor:'textRead',hidden:true},
// 	        {field:'cdwsl',title:'数量2',width:25,align:'center',
// 	        		editor:{
//         				type:'numberbox',
//         				options:{
//         					//精度
//         					precision:LENGTH_SL,
//         			}}},
//    			{field:'cdwdj',title:'单价2',width:25,align:'center',
//     	        editor:{
//     	        	type:'numberbox',
//     	        	options:{
//     	        		precision: LENGTH_JE
//     	        }}},
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
	
	//初始化创建时间
	$('#createDate').html(moment().format('YYYY年MM月DD日'));
	
	
	//初始化类型列表
	jxc_ywpd_pdlxCombo = lnyw.initCombo($("#jxc_ywpd_pdlxId"), 'id', 'pdlxmc', '${pageContext.request.contextPath}/jxc/pdlxAction!listPdlx.action');
	
	//初始化仓库列表
	jxc_ywpd_ckCombo = lnyw.initCombo($("#jxc_ywpd_ckId"), 'id', 'ckmc', '${pageContext.request.contextPath}/jxc/ckAction!listCk.action?depId=' + did);
	

	//初始化信息
	init();
});

//以下为商品列表处理代码
function init(){
	//清空列表，添加一个空行
	ywpd_spdg.datagrid({
		data: [{}],
	});
	
	//清空全部字段
	$('input').val('');
	//收回商品库存信息
	jxc.hideKc('#jxc_ywpd_layout');
	jxc.spInfo($('#jxc_ywpd_layout'), '');

	jxc_ywpd_ckCombo.combobox('selectedIndex', 0);
	jxc_ywpd_pdlxCombo.combobox('selectedIndex', 0);
	
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
				$('#ywpdLsh').html(d.obj);
			}  
		},
	});
	
	//根据权限，动态加载功能按钮
	lnyw.toolbar(0, ywpd_spdg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', did);
	
	//清空合计内容
	ywpd_spdg.datagrid('reloadFooter',[{}]);
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
	if(rowIndex == ywpd_spdg.datagrid('getRows').length - 1){
		ywpd_spdg.datagrid('appendRow', {});
	}else{
		if(!isClick){
			return false;
		}
	}
	//将选中行标识为编辑行
	editIndex = rowIndex;
	//选择编辑行，并进行入编辑状态
	ywpd_spdg.datagrid('selectRow', editIndex)
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
				ywpd_spdg.datagrid('endEdit', editIndex);
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
    ywpd_spdg.datagrid('cancelEdit', editIndex)
            .datagrid('deleteRow', editIndex);
    editIndex = undefined;
    updateFooter();
    jxc.hideKc('#jxc_ywpd_layout');
}

//保存行
function accept(){
    if (rowOk()){
    	ywpd_spdg.datagrid('acceptChanges');
    	jxc.hideKc('#jxc_ywpd_layout');
    }
}

//取消编辑行
function cancelAll(){
	ywpd_spdg.datagrid('rejectChanges');
    editIndex = undefined;
    updateFooter();
    jxc.hideKc('#jxc_ywpd_layout');
}

//提交数据到后台
function saveAll(){
//	var msg = formValid();
	var msg = '';
	if(msg == ''){
		//编辑行是否完成
		if(rowOk()){
			ywpd_spdg.datagrid('endEdit', editIndex);
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
	
	var rows = ywpd_spdg.datagrid('getRows');
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
	var footerRows = ywpd_spdg.datagrid('getFooterRows');
	var effectRow = new Object();
	//将表头内容传入后台
		
	effectRow['mc'] = $('input[name=mc]').val();
	effectRow['ckId'] = jxc_ywpd_ckCombo.combobox('getValue');
	effectRow['ckmc'] = jxc_ywpd_ckCombo.combobox('getText');
	effectRow['pdlxId'] = jxc_ywpd_pdlxCombo.combobox('getValue');
	effectRow['pdlxmc'] = jxc_ywpd_pdlxCombo.combobox('getText');
	effectRow['bz'] = $('input[name=jxc_ywpd_bz]').val();
	effectRow['hjje'] = lnyw.delcommafy(footerRows[0]['spje']); 

	effectRow['bmbh'] = did;
	effectRow['lxbh'] = lx;
	effectRow['menuId'] = menuId;
	
	
	//将表格中的数据去掉最后一个空行后，转换为json格式
	effectRow['datagrid'] = JSON.stringify(rows.slice(0, rows.length - 1));
	//提交到action
	//$.ajaxSettings.traditional=true;
	$.ajax({
		type: "POST",
		url: '${pageContext.request.contextPath}/jxc/ywpdAction!save.action',
		data: effectRow,
		dataType: 'json',
		success: function(rsp){
			if(rsp.success){
		    	$.messager.show({
					title : '提示',
					msg : '提交成功！'
				});
		    	init();
		    	$.messager.confirm('请确认', '是否打印业务盘点单？', function(r) {
					if (r) {
						var url = lnyw.bp() + '/jxc/ywpdAction!printYwpd.action?ywpdlsh=' + rsp.obj.ywpdlsh + '&bmbh=' + did;
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
	var editors = ywpd_spdg.datagrid('getEditors', editIndex);
    spbhEditor = editors[0];
    spmcEditor = editors[1];
    spcdEditor = editors[2];
    spppEditor = editors[3];
    spbzEditor = editors[4];
    zjldwEditor = editors[5];
    zslEditor = editors[6];
    zdjEditor = editors[7];
    cjldwEditor = editors[8];
    spjeEditor = editors[9];
    zhxsEditor = editors[10];
    zjldwIdEditor = editors[11];
    cjldwIdEditor = editors[12];
    
    
    if($(spbhEditor.target).val() != ''){
    	jxc.spInfo($('#jxc_ywpd_layout'), '1', $(spppEditor.target).val(), $(spbzEditor.target).val());
    	jxc.showKc('#jxc_ywpd_layout', 
    			'${pageContext.request.contextPath}/jxc/ywpdAction!getSpkc.action', 
    			{
    				bmbh : did, 
    				ckId : jxc_ywpd_ckCombo.combobox('getValue'),
    				spbh : $(spbhEditor.target).val(),
    			});
    }else{
    	jxc.spInfo($('#jxc_ywpd_layout'), '');
    	jxc.hideKc('#jxc_ywpd_layout');
    }
    
	//loadEditor();
	//处理编辑行的换行事件
	$('.datagrid-row-editing').on('keydown','input', function(event){
		//监听按下向下箭头
		if(event.keyCode == 40){
			var rowIndex = editIndex;
			if(rowOk()){
				ywpd_spdg.datagrid('endEdit', editIndex);
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
    						depId: did,
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
    				did,
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
//     	if($(zhxsEditor.target).val() != 0){
//     		$(cslEditor.target).numberbox('setValue', $(zslEditor.target).val() / $(zhxsEditor.target).val());
//     	}
    	calculate();
    }).bind('keydown', function(event){
     	if(event.keyCode == 9){
     		zdjEditor.target.focus();
//      		return false;
     	}
    });
    
    zdjEditor.target.bind('keyup', function(event){
//     	if($(zhxsEditor.target).val() != 0){
//     		$(cdjEditor.target).numberbox('setValue', $(zdjEditor.target).val() * $(zhxsEditor.target).val() * (1 + SL));
//     	}
    	calculate();
    }).bind('keydown', function(event){
     	if(event.keyCode == 9){
     		spjeEditor.target.focus();
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
    
  	//输入次单位单价后，计算金额
//     cdjEditor.target.bind('keyup', function(event){
//     	if($(zhxsEditor.target).val() != 0){
//     		$(zdjEditor.target).numberbox('setValue', $(cdjEditor.target).val() / $(zhxsEditor.target).val() / (1 + SL));
//     	}
//     	calculate();
//     }).bind('keydown', function(event){
//     	if(event.keyCode == 40){
//      		spjeEditor.target.focus();
//      	}
//     });
  	
  	//输入每行总额后,重新计算单价
    spjeEditor.target.bind('keyup', function(event){
    	$(zdjEditor.target).numberbox('setValue', $(this).val() / $(zslEditor.target).val());
//     	if($(zhxsEditor.target).val() != 0 && $(cslEditor.target).val() != 0){
//     		$(cdjEditor.target).numberbox('setValue', $(this).val() * (1 + SL) / $(zslEditor.target).val() * $(zhxsEditor.target).val());
//     	}else{
//     		$(cdjEditor.target).numberbox('setValue', 0.000);
//     	}
    	updateFooter();
    }).bind('keydown', function(event){
    	if(event.keyCode == 40){
//      	//spjeEditor.target.focus();
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
	var rows = ywpd_spdg.datagrid('getRows');
	var spmc_footer = '合计';
	var hjje = 0.0000;
	$.each(rows, function(){
		var index = ywpd_spdg.datagrid('getRowIndex', this);
		if(index < rows.length - 1){
			if(editIndex == index){
				hjje += Number(spjeEditor.target.val());
			}else{
				
				hjje += Number(this.spje == undefined ? 0 : this.spje);
			}
		}
 	});
	ywpd_spdg.datagrid('reloadFooter', [{spmc : spmc_footer, spje : lnyw.formatNumberRgx(hjje.toFixed(LENGTH_JE))}]);
}

function expandKc(bmbh, spbh){
	$.ajax({
		url:'${pageContext.request.contextPath}/jxc/spAction!getSpKc.action',
// 		async: false,
		data:{
			bmbh : did,
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
	var keys = ywpd_spdg.datagrid('getRows');
	var exist = false;
	if(keys.length > 2){
		$.each(keys, function(){
			var index = ywpd_spdg.datagrid('getRowIndex', this);
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
	zdjEditor.target.val(getDwcb(rowData.spbh));

    
	jxc.spInfo($('#jxc_ywpd_layout'), '1', rowData.sppp, rowData.spbz);
	jxc.showKc('#jxc_ywpd_layout', 
			'${pageContext.request.contextPath}/jxc/ywpdAction!getSpkc.action', 
			{
				bmbh : did, 
				ckId : jxc_ywpd_ckCombo.combobox('getValue'),
				spbh : $(spbhEditor.target).val(),
			});
}

function getDwcb(spbh){
	var dwcb = 0;
	$.ajax({
		async:false,
		type: "POST",
		url: '${pageContext.request.contextPath}/jxc/ywzzAction!getDwcb.action',
		data: {
			bmbh: did,
			spbh: spbh
			},
		dataType: 'json',
		success: function(rsp){
			dwcb = rsp.obj;
		},
		error: function(){
			$.messager.alert("提示", "提交错误了！");
		}
	});
	return dwcb;
}

//////////////////////////////////////////////以上为商品列表处理代码

//////////////////////////////////////////////以下为业务盘点列表处理代码

function cjYwpd(){
	var row = ywpd_dg.datagrid('getSelected');
	if (row != undefined) {
		if(row.isCj != '1'){
			if(row.kfpdlsh == undefined){
				$.messager.prompt('请确认', '是否要冲减选中的业务盘点？请填写备注', function(bz){
					if (bz != undefined){
						$.ajax({
							url : '${pageContext.request.contextPath}/jxc/ywpdAction!cjYwpd.action',
							data : {
								ywpdlsh : row.ywpdlsh,
								bmbh: did,
								lxbh: lx,
								menuId : menuId,
								bz : bz
							},
							method: 'post',
							dataType : 'json',
							success : function(d) {
						 		ywpd_dg.datagrid('load');
								ywpd_dg.datagrid('unselectAll');
								$.messager.show({
									title : '提示',
									msg : d.msg
								});
								$.messager.confirm('请确认', '是否打印业务盘点单？', function(r) {
									if (r) {
										var url = lnyw.bp() + '/jxc/ywpdAction!printYwpd.action?ywpdlsh=' + d.obj.ywpdlsh + '&bmbh=' + did;
										jxc.print(url, PREVIEW_REPORT, HIDE_PRINT_WINDOW);
									}
								});
							}
						});
					}
				});
			}else{
				$.messager.alert('警告', '选中的业务盘点已由库房处理，请重新选择！',  'warning');
			}
		}else{
			$.messager.alert('警告', '选中的业务盘点记录已被冲减，请重新选择！',  'warning');
		}
	}else{
		$.messager.alert('警告', '请选择一条记录进行操作！',  'warning');
	}
}


function printYwpd(){
	var row = ywpd_dg.datagrid('getSelected');
	if (row != undefined) {
		$.messager.confirm('请确认', '是否打印业务盘点单？', function(r) {
			if (r) {
				var url = lnyw.bp() + '/jxc/ywpdAction!printYwpd.action?ywpdlsh=' + row.ywpdlsh + '&bmbh=' + did;
				jxc.print(url, PREVIEW_REPORT, HIDE_PRINT_WINDOW);
			}
		});
	}else{
		$.messager.alert('警告', '请选择一条记录进行操作！',  'warning');
	}
}

function searchYwpd(){
	ywpd_dg.datagrid('load',{
		bmbh: did,
		createTime: $('input[name=createTimeYwpd]').val(),
	});
}

//////////////////////////////////////////////以上为业务盘点列表处理代码


//////////////////////////////////////////////以下为库房盘点列表处理代码

// function generateYwpd(){
// 	var row = ywpd_kfpdDg.datagrid('getSelected');
// 	if(row != undefined){
// 		$.messager.confirm('请确认', '是否要将选中记录进行业务入库？', function(r) {
// 			if (r) {
// 				$.ajax({
// 					url : '${pageContext.request.contextPath}/jxc/kfpdAction!toYwpd.action',
// 					data : {
// 						kfpdlsh : row.kfpdlsh
// 					},
// 					dataType : 'json',
// 					success : function(d) {
// 						ywpd_spdg.datagrid('loadData', d.rows);
// 						updateFooter();
// 						$('input[name=kfpdlsh]').val(row.kfpdlsh);
// 						ywpd_tabs.tabs('select', 0);
// 					}
// 				});
// 			}
// 		});
// 	}else{
// 		$.messager.alert('警告', '请选择最少一条记录进行操作！',  'warning');
// 	}
// }

// function searchKfpdInYwpd(){
// 	ywpd_kfpdDg.datagrid('load',{
// 		bmbh: did,
// 		createTime: $('input[name=createTimeKfpdInYwpd]').val(),
// 		fromOther: 'fromYwpd'
// 	});
// }

//////////////////////////////////////////////以上为库房盘点列表处理代码

</script>

<!-- tabPosition:'left', headerWidth:'35' -->
<div id="jxc_ywpd_tabs" class="easyui-tabs" data-options="fit:true, border:false," style="width:100%;height:100%;">
    <div title="新增记录" data-options="closable:false">
        <div id='jxc_ywpd_layout' style="height:100%;width=100%">
			<div data-options="region:'north',title:'单据信息',border:false,collapsible:false" style="width:100%;height:150px">		
				<table class="tinfo">
					<tr>
						<td colspan="2"></td>
						<th class="read">时间</th><td><div id="createDate" class="read"></div></td>
						<th class="read">单据号</th><td><div id="ywpdLsh" class="read"></div></td>
					</tr>
					<tr>
						<th>名称</th><td><input name="mc" size="50"></td>
						<th>类型</th><td><input id="jxc_ywpd_pdlxId" name="pdlxId" type="text" size="8"></td>
						<th>仓库</th><td><input id="jxc_ywpd_ckId" name="ckId" type="text" size="8"></td>
					</tr>
					<tr>
						<th>备注</th><td colspan="5"><input name="jxc_ywpd_bz" style="width:90%"></td>
					</tr>
				</table>
<!-- 				<input name="kfpdlsh" type="hidden"> -->
			</div>
			<div data-options="region:'center',title:'商品信息',split:true" style="width:150px">		
				<table id='jxc_ywpd_spdg'></table>
			</div>
			<div data-options="region:'east',title:'商品库存信息',split:true,collapsed:true," style="width:150px">
				<table id="show_spkc"></table>
			</div>
		</div>
    </div>
    <div title="业务盘点列表" data-options="closable:false" >
    	<table id='jxc_ywpd_dg'></table>
    </div>
<!-- 	<div title="库房盘点列表" data-options="closable:false" > -->
<!-- 		<table id='jxc_ywpd_kfpdDg'></table> -->
<!-- 	</div> -->
</div>

<div id="jxc_ywpd_tb" style="padding:3px;height:auto">
	请输入查询起始日期:<input type="text" name="createTimeYwpd" class="easyui-datebox" data-options="value: moment().date(1).format('YYYY-MM-DD')" style="width:100px">
	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchYwpd();">查询</a>
</div>
<!-- <div id="jxc_ywpd_kfpdTb" style="padding:3px;height:auto"> -->
<!-- 	请输入查询起始日期:<input type="text" name="createTimeKfpdInYwpd" class="easyui-datebox" data-options="value: moment().date(1).format('YYYY-MM-DD')" style="width:100px"> -->
<!-- 	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchKfpdInYwpd();">查询</a> -->
<!-- </div> -->
