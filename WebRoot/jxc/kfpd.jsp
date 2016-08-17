<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>


<script type="text/javascript">
var kfpd_spdg;
var kfpd_dg;
var kfpd_ywpdDg;
var did;
var lx;
var menuId;

var kfpd_tabs;

var jxc_kfpd_ckCombo;
var jxc_kfpd_pdlxCombo;

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


$(function(){
	did = lnyw.tab_options().did;
	lx = lnyw.tab_options().lx;
	menuId = lnyw.tab_options().id;
	
	//tabOptions = lnyw.tab_options();
	$('#jxc_kfpd_layout').layout({
		fit : true,
		border : false,
	});
	
	kfpd_dg = $('#jxc_kfpd_dg').datagrid({
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
			{field:'kfpdlsh',title:'流水号',align:'center'},
	        {field:'createTime',title:'时间',align:'center'},
	        {field:'ckId',title:'仓库id',align:'center',hidden:true},
	        {field:'ckmc',title:'仓库名称',align:'center'},
	        {field:'pdlxId',title:'类型id',align:'center',hidden:true},
	        {field:'pdlxmc',title:'类型',align:'center'},
	        {field:'mc',title:'名称',align:'center'},
	        {field:'bz',title:'备注',align:'center',
        		formatter: function(value){
        			return lnyw.memo(value, 15);
        		}},
        	{field:'ywpdlsh',title:'业务调号流水号',align:'center'},
        	{field:'isCj',title:'*冲减',align:'center', sortable:true,
        		formatter: function(value) {
					if (value == '1') {
						return '已冲减';
					}
				}},
        	{field:'cjTime',title:'冲减时间',align:'center'},
        	{field:'cjName',title:'冲减人',align:'center'},
        	{field:'cjKfpdlsh',title:'冲减流水号',align:'center'},
        	
	    ]],
	    toolbar:'#jxc_kfpd_tb',
	});
	lnyw.toolbar(1, kfpd_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', did);
	
	
	kfpd_dg.datagrid({
        view: detailview,
        detailFormatter:function(index,row){
            return '<div style="padding:2px"><table id="kfpd-ddv-' + index + '"></table></div>';
        },
        onExpandRow: function(index,row){
            $('#kfpd-ddv-'+index).datagrid({
                url:'${pageContext.request.contextPath}/jxc/kfpdAction!detDatagrid.action',
                fitColumns:true,
                singleSelect:true,
                rownumbers:true,
                loadMsg:'',
                height:'auto',
                queryParams: {
        			kfpdlsh: row.kfpdlsh,
        		},
                columns:[[
                    {field:'spbh',title:'商品编号',width:200,align:'center'},
                    {field:'spmc',title:'名称',width:100,align:'center'},
                    {field:'spcd',title:'产地',width:100,align:'center'},
                    {field:'sppp',title:'品牌',width:100,align:'center'},
                    {field:'spbz',title:'包装',width:100,align:'center'},
                    {field:'hwId',title:'货位id',width:100,align:'center', hidden:true},
                    {field:'hwmc',title:'货位',width:100,align:'center'},
                    {field:'sppc',title:'批次',width:100,align:'center'},
                    {field:'zjldwmc',title:'单位1',width:100,align:'center'},
                    {field:'zdwsl',title:'数量1',width:100,align:'center'},
                    {field:'cjldwmc',title:'单位2',width:100,align:'center'},
                    {field:'cdwsl',title:'数量2',width:100,align:'center'},
                ]],
                onResize:function(){
                	kfpd_dg.datagrid('fixDetailRowHeight',index);
                },
                onLoadSuccess:function(){
                    setTimeout(function(){
                    	kfpd_dg.datagrid('fixDetailRowHeight',index);
                    },0);
                }
            });
            kfpd_dg.datagrid('fixDetailRowHeight',index);
        }
    });
	
	
	kfpd_ywpdDg = $('#jxc_kfpd_ywpdDg').datagrid({
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
			{field:'ywpdlsh',title:'流水号',width:200,align:'center'},
	        {field:'createTime',title:'时间',width:200,align:'center'},
	        {field:'ckId',title:'仓库id',width:200,align:'center',hidden:true},
	        {field:'ckmc',title:'仓库名称',width:200,align:'center'},
	        {field:'pdlxId',title:'类型id',width:200,align:'center',hidden:true},
	        {field:'pdlxmc',title:'类型',width:200,align:'center'},
	        {field:'mc',title:'名称',width:200,align:'center'},
	        {field:'bz',title:'备注',width:200,align:'center',
        		formatter: function(value){
        			return lnyw.memo(value, 15);
        		}},
	    ]],
	    toolbar:'#jxc_kfpd_ywpdTb',
	});
	lnyw.toolbar(2, kfpd_ywpdDg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', did);
	
	
	kfpd_ywpdDg.datagrid({
        view: detailview,
        detailFormatter:function(index,row){
            return '<div style="padding:2px"><table id="kfpd-ywpd-ddv-' + index + '"></table></div>';
        },
        onExpandRow: function(index,row){
            $('#kfpd-ywpd-ddv-'+index).datagrid({
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
                    {field:'cjldwmc',title:'单位2',width:100,align:'center'},
                    {field:'cdwsl',title:'数量2',width:100,align:'center'},
                ]],
                onResize:function(){
                	kfpd_ywpdDg.datagrid('fixDetailRowHeight',index);
                },
                onLoadSuccess:function(){
                    setTimeout(function(){
                    	kfpd_ywpdDg.datagrid('fixDetailRowHeight',index);
                    },0);
                }
            });
            kfpd_ywpdDg.datagrid('fixDetailRowHeight',index);
        }
    });
	
	//选中列表标签后，装载数据
	kfpd_tabs = $('#jxc_kfpd_tabs').tabs({
		onSelect: function(title, index){
			if(index == 1){
				kfpd_dg.datagrid({
					url: '${pageContext.request.contextPath}/jxc/kfpdAction!datagrid.action',
					queryParams:{
						bmbh: did,
					}
				});
			}
			if(index == 2){
				kfpd_ywpdDg.datagrid({
					url: '${pageContext.request.contextPath}/jxc/ywpdAction!datagrid.action',
					queryParams:{
						bmbh: did,
						fromOther: 'fromKfpd'
					}
				});
			}
		},
	});
	
	//初始化商品编辑表格
	kfpd_spdg = $('#jxc_kfpd_spdg').datagrid({
		fit : true,
	    border : false,
	    singleSelect : true,
	    fitColumns: true,
	    autoRowHeight: false,
	    showFooter: true,
		columns:[[
	        {field:'spbh',title:'商品编号',width:25,align:'center',editor:'textRead'},
	        {field:'spmc',title:'商品名称',width:100,align:'center',editor:'textRead'},
	        {field:'spcd',title:'商品产地',width:25,align:'center',editor:'textRead'},
	        {field:'sppp',title:'商品品牌',width:25,align:'center',editor:'textRead'},
	        {field:'spbz',title:'商品包装',width:25,align:'center',editor:'textRead'},
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
	        {field:'zdwsl',title:'数量1',width:25,align:'center',editor:'textRead'},
	        {field:'cjldwmc',title:'单位2',width:25,align:'center',editor:'textRead'},
	        {field:'cdwsl',title:'数量2',width:25,align:'center',editor:'textRead'},
   	        {field:'zjldwId',title:'主单位id',width:25,align:'center',editor:'textRead', hidden:true},
   	        {field:'cjldwId',title:'次单位id',width:25,align:'center',editor:'textRead', hidden:true},
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
 	jxc_kfpd_ckCombo = lnyw.initCombo($("#jxc_kfpd_ckId"), 'id', 'ckmc', '${pageContext.request.contextPath}/jxc/ckAction!listCk.action?depId=' + did);
 	//初始化分户列表
	jxc_kfpd_pdlxCombo = lnyw.initCombo($("#jxc_kfpd_pdlxId"), 'id', 'pdlxmc', '${pageContext.request.contextPath}/jxc/pdlxAction!listPdlx.action');
	
 	//初始化信息
	init();
});

//以下为商品列表处理代码
function init(){
	//清空列表，添加一个空行
	kfpd_spdg.datagrid({data:[],});
	
	//清空全部字段
	$('input').val('');
	
	//收回商品库存信息
	$('#jxc_kfpd_layout').layout('collapse', 'east');
	jxc.spInfo($('#jxc_kfpd_layout'), '');
	
	jxc_kfpd_ckCombo.combobox("selectedIndex", 0);
	jxc_kfpd_pdlxCombo.combobox("selectedIndex", 0);
	
	
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
				$('#kfpdLsh').html(d.obj);
			}  
		},
	});
	
	//根据权限，动态加载功能按钮
	lnyw.toolbar(0, kfpd_spdg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', did);
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
				kfpd_spdg.datagrid('endEdit', editIndex);
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
// 	if(rowIndex == kfpd_spdg.datagrid('getRows').length - 1){
// 		kfpd_spdg.datagrid('appendRow', {});
// 	}else{
// 		if(!isClick){
// 			return false;
// 		}
// 	}
	//将选中行标识为编辑行
	editIndex = rowIndex;
	//选择编辑行，并进行入编辑状态
	kfpd_spdg.datagrid('selectRow', editIndex)
			.datagrid('beginEdit', editIndex);
	setEditing();
}

//处理编辑行
function setEditing(){
	
	
	//加载字段
	var editors = kfpd_spdg.datagrid('getEditors', editIndex);
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
    
    if($(spbhEditor.target).val() != ''){
    	jxc.spInfo($('#jxc_kfpd_layout'), '1', $(spppEditor.target).val(), $(spbzEditor.target).val());
    	jxc.showKc('#jxc_kfpd_layout', '${pageContext.request.contextPath}', did, $(spbhEditor.target).val());
    }else{
    	jxc.spInfo($('#jxc_kfpd_layout'), '');
    	jxc.hideKc('#jxc_kfpd_layout');
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
			$(hwIdEditor.target).combobox('reload', '${pageContext.request.contextPath}/jxc/hwAction!listHw.action?depId=' + did + '&ckId=' + jxc_kfpd_ckCombo.combobox('getValue'));
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
				kfpd_spdg.datagrid('endEdit', editIndex);
				enterEdit(rowIndex + 1, false);
			}else{
				if(!keyOk()){
					removeRow();
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
		kfpd_spdg.datagrid('endEdit', editIndex);
	}
	var rows = kfpd_spdg.datagrid('getRows');
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
	
	var effectRow = new Object();
	//将表头内容传入后台
	effectRow['mc'] = $('input[name=mc]').val();
	effectRow['ckId'] = jxc_kfpd_ckCombo.combobox('getValue');
	effectRow['ckmc'] = jxc_kfpd_ckCombo.combobox('getText');
	effectRow['pdlxId'] = jxc_kfpd_pdlxCombo.combobox('getValue');
	effectRow['pdlxmc'] = jxc_kfpd_pdlxCombo.combobox('getText');
	effectRow['bz'] = $('input[name=jxc_kfpd_bz]').val();

	effectRow['ywpdlsh'] = $('input[name=ywpdlsh]').val();
	
	effectRow['bmbh'] = did;
	effectRow['lxbh'] = lx;
	effectRow['menuId'] = menuId;
	
	//将表格中的数据转换为json格式
	effectRow['datagrid'] = JSON.stringify(rows);
	//提交到action
	//MaskUtil.mask('正在保存，请等待……');
	$.ajax({
		type: "POST",
		url: '${pageContext.request.contextPath}/jxc/kfpdAction!save.action',
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

//////////////////////////////////////////////以下为库房盘点列表处理代码

function cjKfpd(){
	var row = kfpd_dg.datagrid('getSelected');
	if (row != undefined) {
		if(row.isCj != '1'){
			$.messager.prompt('请确认', '是否要冲减选中的库房盘点单？请填写备注', function(bz){
				if(bz != undefined){
					//MaskUtil.mask('正在冲减，请等待……');
					$.ajax({
						url : '${pageContext.request.contextPath}/jxc/kfpdAction!cjKfpd.action',
						data : {
							kfpdlsh : row.kfpdlsh,
							bmbh: did,
							lxbh: lx,
							menuId : menuId,
							bz : bz
						},
						method: 'post',
						dataType : 'json',
						success : function(d) {
							kfpd_dg.datagrid('reload');
							kfpd_dg.datagrid('unselectAll');
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
			$.messager.alert('警告', '选中的库房盘点单已被冲减，请重新选择！',  'warning');
		}
	}else{
		$.messager.alert('警告', '请选择一条记录进行操作！',  'warning');
	}
}

function searchKfpd(){
	kfpd_dg.datagrid('load',{
		bmbh: did,
		createTime: $('input[name=createTimeKfpd]').val(),
	});
}

//////////////////////////////////////////////以上为库房盘点列表处理代码

//////////////////////////////////////////////以下为业务盘点列表处理代码
function createKfpdFromYwpd(){
	var row = kfpd_ywpdDg.datagrid('getSelected');
	if(row != undefined){
		$.messager.confirm('请确认', '是否要将选中业务盘点进行库房盘点？', function(r) {
			if (r) {
				$.ajax({
					url : '${pageContext.request.contextPath}/jxc/ywpdAction!toKfpd.action',
					data : {
						ywpdlsh : row.ywpdlsh
					},
					dataType : 'json',
					success : function(d) {
						kfpd_spdg.datagrid('loadData', d.rows);
						$('input[name=mc]').val(row.mc);
						jxc_kfpd_ckCombo.combobox('setValue', row.ckId);
						jxc_kfpd_pdlxCombo.combobox('setValue', row.pdlxId);
						$('input[name=ywpdlsh]').val(row.ywpdlsh);
						kfpd_tabs.tabs('select', 0);
					}
				});
			}
		});
	}else{
		$.messager.alert('警告', '请选择一条记录进行操作！',  'warning');
	}
}

function searchYwpdInKfpd(){
	kfpd_ywpdDg.datagrid('load',{
		bmbh: did,
		createTime: $('input[name=createTimeYwpdInKfpd]').val(),
		fromOther: 'fromKfpd'
	});
}

//////////////////////////////////////////////以上为业务盘点列表处理代码
</script>

<!-- tabPosition:'left', headerWidth:'35' -->
<div id="jxc_kfpd_tabs" class="easyui-tabs" data-options="fit:true, border:false," style="width:100%;height:100%;">
    <div title="新增记录" data-options="closable:false">
        <div id='jxc_kfpd_layout' style="height:100%;width=100%">
			<div data-options="region:'north',title:'单据信息',border:false,collapsible:false" style="width:100%;height:105px">		
				<table class="tinfo">
					<tr>
						<td colspan="2"></td>
						<th class="read">时间</th><td><div id="createDate" class="read"></div></td>
						<th class="read">单据号</th><td><div id="kfpdLsh" class="read"></div></td>
					</tr>
					<tr>
						<th>名称</th><td><input name="mc" size="50"></td>
						<th>类型</th><td><input id="jxc_kfpd_pdlxId" name="pdlxId" type="text" size="8"></td>
						<th>仓库</th><td><input id="jxc_kfpd_ckId" name="ckId" type="text" size="8"></td>
					</tr>
					<tr>
						<th>备注</th><td colspan="5"><input name="jxc_kfpd_bz" style="width:90%"></td>
					</tr>
			  </table>
			  <input name="ywpdlsh" type="text" hidden="hidden">
			</div>
			<div data-options="region:'center',title:'商品信息',split:true" style="width:150px">		
				<table id='jxc_kfpd_spdg'></table>
			</div>
			<div data-options="region:'east',title:'商品库存信息',split:true,collapsed:true" style="width:150px">
				<table id="show_spkc"></table>
			</div>
		</div>
    </div>
    <div title="库房盘点列表" data-options="closable:false" >
    	<table id='jxc_kfpd_dg'></table>
    </div>
    <div title="业务盘点列表" data-options="closable:false" >
    	<table id='jxc_kfpd_ywpdDg'></table>
    </div>
</div>

<div id="jxc_kfpd_tb" style="padding:3px;height:auto">
	请输入查询起始日期:<input type="text" name="createTimeKfpd" class="easyui-datebox" data-options="value: moment().date(1).format('YYYY-MM-DD')" style="width:100px">
	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchKfpd();">查询</a>
</div>
<div id="jxc_kfpd_ywpdTb" style="padding:3px;height:auto">
	请输入查询起始日期:<input type="text" name="createTimeYwpdInKfpd" class="easyui-datebox" data-options="value: moment().date(1).format('YYYY-MM-DD')" style="width:100px">
	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchYwpdInKfpd();">查询</a>
</div>



