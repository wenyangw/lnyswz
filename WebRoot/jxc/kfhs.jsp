<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>


<script type="text/javascript">
var kfhs_dg;
var kfhs_spdg;
var kfhs_ywhsDg;
var did;
var lx;
var menuId;

var kfhs_tabs;

var jxc_kfhs_ckCombo;
var jxc_kfhs_fhCombo;

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
var zslEditor;
var cjldwEditor;
var cslEditor;
var zjldwIdEditor;
var cjldwIdEditor;
var zhxsEditor;


$(function(){
	did = lnyw.tab_options().did;
	lx = lnyw.tab_options().lx;
	menuId = lnyw.tab_options().id;
	
	//tabOptions = lnyw.tab_options();
	$('#jxc_kfhs_layout').layout({
		fit : true,
		border : false,
	});
	
	kfhs_dg = $('#jxc_kfhs_dg').datagrid({
		fit : true,
	    border : false,
	    singleSelect : true,
	    remoteSort: false,
 	    fitColumns: true,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
		columns:[[
			{field:'kfhslsh',title:'流水号',align:'center'},
	        {field:'createTime',title:'时间',align:'center'},
	        {field:'ckId',title:'仓库id',align:'center',hidden:true},
	        {field:'ckmc',title:'仓库名称',align:'center'},
// 	        {field:'fhId',title:'分户id',align:'center',hidden:true},
// 	        {field:'fhmc',title:'分户名称',align:'center'},
	        {field:'bz',title:'备注',align:'center',
        		formatter: function(value){
        			return lnyw.memo(value, 15);
        		}},
        	{field:'ywhslsh',title:'业务调号流水号',align:'center'},
        	{field:'isCj',title:'*冲减',align:'center', sortable:true,
        		formatter: function(value) {
					if (value == '1') {
						return '已冲减';
					}
				}},
        	{field:'cjTime',title:'冲减时间',align:'center'},
        	{field:'cjName',title:'冲减人',align:'center'},
        	{field:'cjKfhslsh',title:'冲减流水号',align:'center'},
        	
	    ]],
	    toolbar:'#jxc_kfhs_tb',
	});
	lnyw.toolbar(1, kfhs_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', did);
	
	
	kfhs_dg.datagrid({
        view: detailview,
        detailFormatter:function(index,row){
            return '<div style="padding:2px"><table id="kfhs-ddv-' + index + '"></table></div>';
        },
        onExpandRow: function(index,row){
            $('#kfhs-ddv-'+index).datagrid({
                url:'${pageContext.request.contextPath}/jxc/kfhsAction!detDatagrid.action',
                fitColumns:true,
                singleSelect:true,
                rownumbers:true,
                loadMsg:'',
                height:'auto',
                queryParams: {
        			kfhslsh: row.kfhslsh,
        		},
                columns:[[
                    {field:'spbh',title:'商品编号',width:200,align:'center',
                    	styler:function(value, rowData,	rowIndex){
	    	        		if(rowData.isZj == '1'){
	    						return 'color:blue;';
	    	        		}else{
	    						return 'color:red;';
	    	        		}
    					}
                    },
                    {field:'spmc',title:'名称',width:100,align:'center',
                    	styler:function(value, rowData,	rowIndex){
	    	        		if(rowData.isZj == '1'){
	    						return 'color:blue;';
	    	        		}else{
	    						return 'color:red;';
	    	        		}
	    	        	}
                    },
                    {field:'spcd',title:'产地',width:100,align:'center',
                       	styler:function(value, rowData,	rowIndex){
   	    	        		if(rowData.isZj == '1'){
   	    						return 'color:blue;';
   	    	        		}else{
   	    						return 'color:red;';
   	    	        		}
   	    	        	}
                    },
                    {field:'sppp',title:'品牌',width:100,align:'center',
                       	styler:function(value, rowData,	rowIndex){
   	    	        		if(rowData.isZj == '1'){
   	    						return 'color:blue;';
   	    	        		}else{
   	    						return 'color:red;';
   	    	        		}
   	    	        	}
                    },
                    {field:'spbz',title:'包装',width:100,align:'center',
                       	styler:function(value, rowData,	rowIndex){
   	    	        		if(rowData.isZj == '1'){
   	    						return 'color:blue;';
   	    	        		}else{
   	    						return 'color:red;';
   	    	        		}
   	    	        	}
                    },
                    {field:'zjldwmc',title:'单位1',width:100,align:'center',
                       	styler:function(value, rowData,	rowIndex){
   	    	        		if(rowData.isZj == '1'){
   	    						return 'color:blue;';
   	    	        		}else{
   	    						return 'color:red;';
   	    	        		}
   	    	        	}
                    },
                    {field:'zdwsl',title:'数量1',width:100,align:'center',
                       	styler:function(value, rowData,	rowIndex){
   	    	        		if(rowData.isZj == '1'){
   	    						return 'color:blue;';
   	    	        		}else{
   	    						return 'color:red;';
   	    	        		}
   	    	        	}
                    },
                    {field:'cjldwmc',title:'单位2',width:100,align:'center',
                       	styler:function(value, rowData,	rowIndex){
   	    	        		if(rowData.isZj == '1'){
   	    						return 'color:blue;';
   	    	        		}else{
   	    						return 'color:red;';
   	    	        		}
   	    	        	}
                    },
                    {field:'cdwsl',title:'数量2',width:100,align:'center',
                       	styler:function(value, rowData,	rowIndex){
   	    	        		if(rowData.isZj == '1'){
   	    						return 'color:blue;';
   	    	        		}else{
   	    						return 'color:red;';
   	    	        		}
   	    	        	}
                    },
                ]],
                onResize:function(){
                	kfhs_dg.datagrid('fixDetailRowHeight',index);
                },
                onLoadSuccess:function(){
                    setTimeout(function(){
                    	kfhs_dg.datagrid('fixDetailRowHeight',index);
                    },0);
                }
            });
            kfhs_dg.datagrid('fixDetailRowHeight',index);
        }
    });
	
	
	kfhs_ywhsDg = $('#jxc_kfhs_ywhsDg').datagrid({
		fit : true,
	    border : false,
	    singleSelect : true,
	    remoteSort: false,
 	    fitColumns: true,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
		columns:[[
			{field:'ywhslsh',title:'流水号',align:'center',width:80},
	        {field:'createTime',title:'时间',align:'center',width:100},
	        {field:'ckId',title:'仓库id',align:'center',hidden:true},
	        {field:'ckmc',title:'仓库名称',align:'center',width:80},
// 	        {field:'fhId',title:'分户id',align:'center',hidden:true},
// 	        {field:'fhmc',title:'分户名称',align:'center'},
	        {field:'bz',title:'备注',align:'center',width:200,
        		formatter: function(value){
        			return lnyw.memo(value, 15);
        		}},
	    ]],
	    toolbar:'#jxc_kfhs_ywhsTb',
	});
	lnyw.toolbar(2, kfhs_ywhsDg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', did);
	
	
	kfhs_ywhsDg.datagrid({
        view: detailview,
        detailFormatter:function(index,row){
            return '<div style="padding:2px"><table id="kfhs-ywhs-ddv-' + index + '"></table></div>';
        },
        onExpandRow: function(index,row){
            $('#kfhs-ywhs-ddv-'+index).datagrid({
                url:'${pageContext.request.contextPath}/jxc/ywhsAction!detDatagrid.action',
                fitColumns:true,
                singleSelect:true,
                rownumbers:true,
                loadMsg:'',
                height:'auto',
                queryParams: {
        			ywhslsh: row.ywhslsh,
 					fromOther: 'fromKfhs'
        		},
                columns:[[
                    {field:'spbh',title:'商品编号',width:200,align:'center',
                    	styler:function(value, rowData,	rowIndex){
	    	        		if(rowData.isZj == '1'){
	    						return 'color:blue;';
	    	        		}else{
	    						return 'color:red;';
	    	        		}
    					}
                    },
                    {field:'spmc',title:'名称',width:100,align:'center',
                    	styler:function(value, rowData,	rowIndex){
	    	        		if(rowData.isZj == '1'){
	    						return 'color:blue;';
	    	        		}else{
	    						return 'color:red;';
	    	        		}
	    	        	}
                    },
                    {field:'spcd',title:'产地',width:100,align:'center',
                       	styler:function(value, rowData,	rowIndex){
   	    	        		if(rowData.isZj == '1'){
   	    						return 'color:blue;';
   	    	        		}else{
   	    						return 'color:red;';
   	    	        		}
   	    	        	}
                    },
                    {field:'sppp',title:'品牌',width:100,align:'center',
                       	styler:function(value, rowData,	rowIndex){
   	    	        		if(rowData.isZj == '1'){
   	    						return 'color:blue;';
   	    	        		}else{
   	    						return 'color:red;';
   	    	        		}
   	    	        	}
                    },
                    {field:'spbz',title:'包装',width:100,align:'center',
                       	styler:function(value, rowData,	rowIndex){
   	    	        		if(rowData.isZj == '1'){
   	    						return 'color:blue;';
   	    	        		}else{
   	    						return 'color:red;';
   	    	        		}
   	    	        	}
                    },
                    {field:'zjldwmc',title:'单位1',width:100,align:'center',
                       	styler:function(value, rowData,	rowIndex){
   	    	        		if(rowData.isZj == '1'){
   	    						return 'color:blue;';
   	    	        		}else{
   	    						return 'color:red;';
   	    	        		}
   	    	        	}
                    },
                    {field:'zdwsl',title:'数量1',width:100,align:'center',
                       	styler:function(value, rowData,	rowIndex){
   	    	        		if(rowData.isZj == '1'){
   	    						return 'color:blue;';
   	    	        		}else{
   	    						return 'color:red;';
   	    	        		}
   	    	        	}
                    },
                    {field:'cjldwmc',title:'单位2',width:100,align:'center',
                       	styler:function(value, rowData,	rowIndex){
   	    	        		if(rowData.isZj == '1'){
   	    						return 'color:blue;';
   	    	        		}else{
   	    						return 'color:red;';
   	    	        		}
   	    	        	}
                    },
                    {field:'cdwsl',title:'数量2',width:100,align:'center',
                       	styler:function(value, rowData,	rowIndex){
   	    	        		if(rowData.isZj == '1'){
   	    						return 'color:blue;';
   	    	        		}else{
   	    						return 'color:red;';
   	    	        		}
   	    	        	}
                    },
	    	    	{field:'isZs',title:'直送',width:100,align:'center',
	                       	styler:function(value, rowData,	rowIndex){
	   	    	        		if(rowData.isZj == '1'){
	   	    						return 'color:blue;';
	   	    	        		}else{
	   	    						return 'color:red;';
	   	    	        		}
	   	    	        	}
		    	    	 },
                ]],
                onResize:function(){
                	kfhs_ywhsDg.datagrid('fixDetailRowHeight',index);
                },
                onLoadSuccess:function(){
                    setTimeout(function(){
                    	kfhs_ywhsDg.datagrid('fixDetailRowHeight',index);
                    },0);
                }
            });
            kfhs_ywhsDg.datagrid('fixDetailRowHeight',index);
        }
    });
	
	//选中列表标签后，装载数据
	kfhs_tabs = $('#jxc_kfhs_tabs').tabs({
		onSelect: function(title, index){
			if(index == 1){
				kfhs_dg.datagrid({
					url: '${pageContext.request.contextPath}/jxc/kfhsAction!datagrid.action',
					queryParams:{
						bmbh: did,
					}
				});
			}
			if(index == 2){
				kfhs_ywhsDg.datagrid({
					url: '${pageContext.request.contextPath}/jxc/ywhsAction!datagrid.action',
					queryParams:{
						bmbh: did,
						fromOther: 'fromKfhs'
					}
				});
			}
		},
	});
	
	//初始化商品编辑表格
	kfhs_spdg = $('#jxc_kfhs_spdg').datagrid({
		fit : true,
	    border : false,
	    singleSelect : true,
	    fitColumns: true,
	    autoRowHeight: false,
	    showFooter: true,
		columns:[[
	        {field:'spbh',title:'商品编号',width:25,align:'center',editor:'textRead',
	        	styler:function(value, rowData){
	        		if(rowData.isZj == 1){
						return 'color:blue;';
	        		}else{
						return 'color:red;';
	        		}
				}},
	        {field:'spmc',title:'商品名称',width:100,align:'center',editor:'textRead',
	        	styler:function(value, rowData){
	        		if(rowData.isZj == 1){
						return 'color:blue;';
	        		}else{
						return 'color:red;';
	        		}
				}},
	        {field:'spcd',title:'商品产地',width:25,align:'center',editor:'textRead',
	        	styler:function(value, rowData){
	        		if(rowData.isZj == 1){
						return 'color:blue;';
	        		}else{
						return 'color:red;';
	        		}
				}},
	        {field:'sppp',title:'商品品牌',width:25,align:'center',editor:'textRead',
	        	styler:function(value, rowData){
	        		if(rowData.isZj == 1){
						return 'color:blue;';
	        		}else{
						return 'color:red;';
	        		}
				}},
	        {field:'spbz',title:'商品包装',width:25,align:'center',editor:'textRead',
	        	styler:function(value, rowData){
	        		if(rowData.isZj == 1){
						return 'color:blue;';
	        		}else{
						return 'color:red;';
	        		}
				}},
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
	        {field:'zjldwmc',title:'单位1',width:25,align:'center',editor:'textRead',
	        	styler:function(value, rowData){
	        		if(rowData.isZj == 1){
						return 'color:blue;';
	        		}else{
						return 'color:red;';
	        		}
				}},
	        {field:'zdwsl',title:'数量1',width:25,align:'center',editor:'textRead',
	        	styler:function(value, rowData){
	        		if(rowData.isZj == 1){
						return 'color:blue;';
	        		}else{
						return 'color:red;';
	        		}
				}},
			{field:'cjldwmc',title:'单位2',width:25,align:'center',editor:'textRead',
	        	styler:function(value, rowData){
	        		if(rowData.isZj == 1){
						return 'color:blue;';
	        		}else{
						return 'color:red;';
	        		}
				}},
	        {field:'cdwsl',title:'数量2',width:25,align:'center',editor:'textRead',
	        	styler:function(value, rowData){
	        		if(rowData.isZj == 1){
						return 'color:blue;';
	        		}else{
						return 'color:red;';
	        		}
				}},
   	        {field:'zjldwId',title:'主单位id',width:25,align:'center',editor:'textRead', hidden:true},
   	        {field:'cjldwId',title:'次单位id',width:25,align:'center',editor:'textRead', hidden:true},
   	        {field:'zhxs',title:'转换系数',width:25,align:'center',editor:'textRead', hidden:true},
	    ]],
	   	onClickRow: clickRow,
	   	onAfterEdit: function (rowIndex, rowData, changes) {
            //endEdit该方法触发此事件
            editIndex = undefined;
        },
	});
	
	//初始化创建时间
	$('#createDate').html(moment().format('YYYY年MM月DD日'));
	
	//初始化仓库列表
 	jxc_kfhs_ckCombo = lnyw.initCombo($("#jxc_kfhs_ckId"), 'id', 'ckmc', '${pageContext.request.contextPath}/jxc/ckAction!listCk.action?depId=' + did);
 	//初始化分户列表
	jxc_kfhs_fhCombo = lnyw.initCombo($("#jxc_kfhs_fhId"), 'id', 'fhmc', '${pageContext.request.contextPath}/jxc/fhAction!listFhs.action?depId=' + did);
	
 	//初始化信息
	init();
});

//以下为商品列表处理代码
function init(){
	//清空列表，添加一个空行
	kfhs_spdg.datagrid({data:[],});
	
	//清空全部字段
	$('input').val('');
	//$('input:checkbox').removeAttr('checked');
	$('input:checkbox').prop('checked', false);
	
	//收回商品库存信息
	$('#jxc_kfhs_layout').layout('collapse', 'east');
	jxc.spInfo($('#jxc_kfhs_layout'), '');
	
	//隐藏分户选择列表
	$('.jxc_kfhs_isFh').css('display', 'none');
	
	jxc_kfhs_ckCombo.combobox("selectedIndex", 0);
	jxc_kfhs_fhCombo.combobox("selectedIndex", 0);
	
	
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
				$('#kfhsLsh').html(d.obj);
			}  
		},
	});
	
	//根据权限，动态加载功能按钮
	lnyw.toolbar(0, kfhs_spdg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', did);
}


//单击行操作
function clickRow(rowIndex, rowData){
	//是否有编辑行
	if(editIndex != undefined){ //有编辑行
		//选中行与编辑行是否为同一行
		//同一行则无任何操作
		if(editIndex != rowIndex){
			//编辑行内容验证是否通过
			//if(rowOk()){ //行验证通过
				//结束编辑行
				kfhs_spdg.datagrid('endEdit', editIndex);
				//将选中行转为编辑状态
			//}
			enterEdit(rowIndex, true);
		}
	}else{ //无编辑行
		enterEdit(rowIndex, true);
	}
}

//根据指定行，进入编辑状态
function enterEdit(rowIndex, isClick){
	//如果选中行为最后一行，先增加一个空行
// 	if(rowIndex == kfhs_spdg.datagrid('getRows').length - 1){
// 		kfhs_spdg.datagrid('appendRow', {});
// 	}else{
// 		if(!isClick){
// 			return false;
// 		}
// 	}
	//将选中行标识为编辑行
	editIndex = rowIndex;
	//选择编辑行，并进行入编辑状态
	kfhs_spdg.datagrid('selectRow', editIndex)
			.datagrid('beginEdit', editIndex);
	setEditing();
}

//处理编辑行
function setEditing(){
	
	
	//加载字段
	var editors = kfhs_spdg.datagrid('getEditors', editIndex);
    spbhEditor = editors[0];
    spmcEditor = editors[1];
    spcdEditor = editors[2];
    spppEditor = editors[3];
    spbzEditor = editors[4];
    hwIdEditor = editors[5];
    sppcEditor = editors[6];
    zjldwEditor = editors[7];
    zslEditor = editors[8];
    cjldwEditor = editors[9];
    cslEditor = editors[10];
    zjldwIdEditor = editors[11];
    cjldwIdEditor = editors[12];
    zhxsEditor = editors[13];
    
    if($(spbhEditor.target).val() != ''){
    	jxc.spInfo($('#jxc_kfhs_layout'), '1', $(spppEditor.target).val(), $(spbzEditor.target).val());
    	jxc.showKc('#jxc_kfhs_layout', '${pageContext.request.contextPath}', did, $(spbhEditor.target).val());
    }else{
    	jxc.spInfo($('#jxc_kfhs_layout'), '');
    	jxc.hideKc('#jxc_kfhs_layout');
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
			$(hwIdEditor.target).combobox('reload', '${pageContext.request.contextPath}/jxc/hwAction!listHw.action?depId=' + did + '&ckId=' + jxc_kfhs_ckCombo.combobox('getValue'));
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
				kfhs_spdg.datagrid('endEdit', editIndex);
				enterEdit(rowIndex + 1, false);
			}else{
				if(!keyOk()){
					removeit();
				}
			}
		}
	});
	    
    //初始进入编辑状态时，使用商品编号获得焦点
    hwIdEditor.target.focus();
}

//提交数据到后台
function saveAll(){
	if(editIndex != undefined){
		kfhs_spdg.datagrid('endEdit', editIndex);
	}
	var rows = kfhs_spdg.datagrid('getRows');
	if(rows.length == 0){
		$.messager.alert('提示', '未添加商品数据,请继续操作！', 'error');
		return false;
	}
 	for(var i = 0; i < rows.length; i++){
 		if(rows[i].hwId == undefined || rows[i].sppc == undefined){
 			$.messager.alert('提示', '请确认商品货位及批次！', 'error');
 			return false;
 		}
 	}
	
	//var footerRows = kfhs_ywhsDg.datagrid('getFooterRows');
	var effectRow = new Object();
	//将表头内容传入后台
	effectRow['ckId'] = jxc_kfhs_ckCombo.combobox('getValue');
	effectRow['ckmc'] = jxc_kfhs_ckCombo.combobox('getText');
	
	if($('input[name=isFh]').is(':checked')){
		effectRow['fhId'] = jxc_kfhs_fhCombo.combobox('getValue');
		effectRow['fhmc'] = jxc_kfhs_fhCombo.combobox('getText');
	}
	//effectRow['hjje'] = footerRows[0]['spje']; 
	effectRow['bz'] = $('input[name=jxc_kfhs_bz]').val();
	effectRow['ywhslsh'] = $('input[name=ywhslsh]').val();
	
	effectRow['bmbh'] = did;
	effectRow['lxbh'] = lx;
	effectRow['menuId'] = menuId;
	
	//将表格中的数据转换为json格式
	effectRow['datagrid'] = JSON.stringify(rows);
	//提交到action
	//MaskUtil.mask('正在保存，请等待……');
	$.ajax({
		type: "POST",
		url: '${pageContext.request.contextPath}/jxc/kfhsAction!save.action',
		data: effectRow,
		dataType: 'json',
		success: function(rsp){
			if(rsp.success){
		    	$.messager.show({
					title : '提示',
					msg : '提交成功！'
				});
		    	init();
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


//////////////////////////////////////////////以下为库房调号列表处理代码

function cjKfhs(){
	var row = kfhs_dg.datagrid('getSelected');
	if (row != undefined) {
		if(row.isCj != '1'){
			$.messager.prompt('请确认', '是否要冲减选中的库房调号单？请填写备注', function(bz){
				if(bz != undefined){
					//MaskUtil.mask('正在冲减，请等待……');
					$.ajax({
						url : '${pageContext.request.contextPath}/jxc/kfhsAction!cjKfhs.action',
						data : {
							kfhslsh : row.kfhslsh,
							bmbh: did,
							lxbh: lx,
							menuId : menuId,
							bz : bz
						},
						method: 'post',
						dataType : 'json',
						success : function(d) {
							kfhs_dg.datagrid('reload');
							kfhs_dg.datagrid('unselectAll');
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
			$.messager.alert('警告', '选中的库房调号单已被冲减，请重新选择！',  'warning');
		}
	}else{
		$.messager.alert('警告', '请选择一条记录进行操作！',  'warning');
	}
}

function printKfhs(){
	var selected = kfhs_dg.datagrid('getSelected');
 	//if (selected != undefined) {
 	if (selected) {
	 	$.messager.confirm('请确认', '是否打印销售提货单？', function(r) {
			if (r) {
				var url = lnyw.bp() + '/jxc/kfhsAction!printKfhs.action?kfhslsh=' + selected.kfhslsh + "&bmbh=" + did;
				jxc.print(url, PREVIEW_REPORT, HIDE_PRINT_WINDOW);
			}
		});
	}else{
		$.messager.alert('警告', '请选择一条记录进行操作！',  'warning');
	}
}

function searchKfhs(){
	kfhs_dg.datagrid('load',{
		bmbh: did,
		createTime: $('input[name=createTimeKfhs]').val(),
	});
}

//////////////////////////////////////////////以上为库房调号列表处理代码

//////////////////////////////////////////////以下为业务调号列表处理代码
function createKfhsFromYwhs(){
	var row = kfhs_ywhsDg.datagrid('getSelected');
	if(row != undefined){
		$.messager.confirm('请确认', '是否要将选中业务调号进行库房调号？', function(r) {
			if (r) {
				$.ajax({
					url : '${pageContext.request.contextPath}/jxc/ywhsAction!toKfhs.action',
					data : {
						ywhslsh : row.ywhslsh
					},
					dataType : 'json',
					success : function(d) {
						kfhs_spdg.datagrid('loadData', d.rows);
						jxc_kfhs_ckCombo.combobox('setValue', row.ckId);
						if(row.fhId != undefined){
							$('#jxc_kfhs_isFh').attr('checked', 'checked');
							$('.jxc_kfhs_isFh').css('display', 'table-cell');
							jxc_kfhs_fhCombo.combobox('setValue', row.fhId);
						}
						$('input[name=ywhslsh]').val(row.ywhslsh);
						kfhs_tabs.tabs('select', 0);
					}
				});
			}
		});
	}else{
		$.messager.alert('警告', '请选择一条记录进行操作！',  'warning');
	}
}

function searchYwhsInKfhs(){
	kfhs_ywhsDg.datagrid('load',{
		bmbh: did,
		createTime: $('input[name=createTimeYwhsInKfhs]').val(),
		fromOther: 'fromKfhs'
	});
}

//////////////////////////////////////////////以上为业务入库列表处理代码
</script>

<!-- tabPosition:'left', headerWidth:'35' -->
<div id="jxc_kfhs_tabs" class="easyui-tabs" data-options="fit:true, border:false," style="width:100%;height:100%;">
    <div title="新增记录" data-options="closable:false">
        <div id='jxc_kfhs_layout' style="height:100%;width=100%">
			<div data-options="region:'north',title:'单据信息',border:false,collapsible:false" style="width:100%;height:85px">		
				<table class="tinfo">
					<tr>
						<th>仓库</th>
						<td><input id="jxc_kfhs_ckId" name="ckId" type="text" disabled="disabled" size="8"></td>
				  		<th>分户<input type="checkbox" id="jxc_kfck_isFh" name="isFh" disabled="disabled"></th>
				  		<td class="read jxc_kfck_isFh" style="display:none"><input id="jxc_kfhs_fhId" name="fhId" disabled="disabled" size="8"></td>
				  		<td colspan="5"></td>
				  		<th class="read">时间</th><td><div id="createDate" class="read"></div></td>
				  		<th class="read">单据号</th><td><div id="kfhsLsh" class="read"></div></td>
					</tr>
					<tr>
						<th>备注</th><td colspan="15"><input type="text" name="jxc_kfhs_bz" size="150"></td>
					</tr>
			  </table>
			  <input name="ywhslsh" type="text" hidden="hidden">
			</div>
			<div data-options="region:'center',title:'商品信息',split:true" style="width:150px">		
				<table id='jxc_kfhs_spdg'></table>
			</div>
			<div data-options="region:'east',title:'商品库存信息',split:true,collapsed:true" style="width:150px">
				<table id="show_spkc"></table>
			</div>
		</div>
    </div>
    <div title="库房调号列表" data-options="closable:false" >
    	<table id='jxc_kfhs_dg'></table>
    </div>
    <div title="业务调号列表" data-options="closable:false" >
    	<table id='jxc_kfhs_ywhsDg'></table>
    </div>
</div>

<div id="jxc_kfhs_tb" style="padding:3px;height:auto">
	请输入查询起始日期:<input type="text" name="createTimeKfhs" class="easyui-datebox" data-options="value: moment().date(1).format('YYYY-MM-DD')" style="width:100px">
	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchKfhs();">查询</a>
</div>
<div id="jxc_kfhs_ywhsTb" style="padding:3px;height:auto">
	请输入查询起始日期:<input type="text" name="createTimeYwhsInKfhs" class="easyui-datebox" data-options="value: moment().date(1).format('YYYY-MM-DD')" style="width:100px">
	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchYwhsInKfhs();">查询</a>
</div>



