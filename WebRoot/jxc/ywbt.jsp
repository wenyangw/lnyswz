<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>


<script type="text/javascript">
var did;
var lx;
var menuId;

var ywbt_spdg;
var ywbt_dg;
var ywbt_ywrkDg;
var editIndex = undefined;
var ywbt_tabs;

//编辑行字段
var spbhEditor;   
var spmcEditor;
var spcdEditor;
var spppEditor;
var spbzEditor;
var zjldwEditor;
var spjeEditor;
var btjeEditor;
var zjldwIdEditor;

$(function(){
	did = lnyw.tab_options().did;
	lx = lnyw.tab_options().lx;
	menuId = lnyw.tab_options().id;
	
	$('#jxc_ywbt_layout').layout({
		fit : true,
		border : false,
	});
	
	ywbt_dg = $('#jxc_ywbt_dg').datagrid({
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
			{field:'ywbtlsh',title:'流水号',align:'center'},
	        {field:'createTime',title:'时间',align:'center'},
	        {field:'createName',title:'创建人',align:'center'},
	        {field:'hjje',title:'金额',align:'center',
	        	formatter: function(value){
	        		return lnyw.formatNumberRgx(value);
	        	}},
	        {field:'bz',title:'备注',align:'center',
        		formatter: function(value){
        			return lnyw.memo(value, 15);
        		}},
        	{field:'gysmc',title:'供应商',align:'center'},	
        	{field:'ywrklsh',title:'业务入库',align:'center'},
	    ]],
	    toolbar:'#jxc_ywbt_tb',
	});
	lnyw.toolbar(1, ywbt_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', did);
	
	
	ywbt_dg.datagrid({
        view: detailview,
        detailFormatter:function(index,row){
            return '<div style="padding:2px"><table id="ywbt-ddv-' + index + '"></table></div>';
        },
        onExpandRow: function(index,row){
            $('#ywbt-ddv-'+index).datagrid({
                url:'${pageContext.request.contextPath}/jxc/ywbtAction!detDatagrid.action',
                fitColumns:true,
                singleSelect:true,
                rownumbers:true,
                loadMsg:'',
                height:'auto',
                queryParams: {
        			ywbtlsh: row.ywbtlsh,
        		},
                columns:[[
                    {field:'spbh',title:'商品编号',width:200,align:'center'},
                    {field:'spmc',title:'名称',width:100,align:'center'},
                    {field:'spcd',title:'产地',width:100,align:'center'},
                    {field:'sppp',title:'品牌',width:100,align:'center'},
                    {field:'spbz',title:'包装',width:100,align:'center'},
                    {field:'zjldwmc',title:'单位1',width:100,align:'center'},
                    {field:'spje',title:'金额',width:100,align:'center',
        	        	formatter: function(value){
        	        		return lnyw.formatNumberRgx(value);
        	        	}},
                ]],
                onResize:function(){
                	ywbt_dg.datagrid('fixDetailRowHeight',index);
                },
                onLoadSuccess:function(){
                    setTimeout(function(){
                    	ywbt_dg.datagrid('fixDetailRowHeight',index);
                    },0);
                }
            });
            ywbt_dg.datagrid('fixDetailRowHeight',index);
        }
    });
	
	
	ywbt_ywrkDg = $('#jxc_ywbt_ywrkDg').datagrid({
		fit : true,
	    border : false,
	    //remoteSort: false,
	    fitColumns: true,
	    singleSelect: true, 
	    pagination : true,
		pageSize : pageSize,
		pageList : pageList,
		columns:[[
			{field:'ywrklsh',title:'流水号',align:'center'},
	        {field:'createTime',title:'时间',align:'center'},
	        {field:'gysbh',title:'*供应商编号',align:'center',sortable:true,
	        	sorter: function(a,b){
					return (a - b); 
				}},
	        {field:'gysmc',title:'供应商名称',align:'center'},
	        {field:'ckId',title:'仓库id',align:'center',hidden:true},
	        {field:'ckmc',title:'仓库名称',align:'center'},
	        {field:'hjje',title:'合计金额',align:'center',
	        	formatter: function(value){
	        		return lnyw.formatNumberRgx(value);
	        	}},
        	{field:'bz',title:'备注',align:'center',
        		formatter: function(value){
        			return lnyw.memo(value, 15);
        		}},
	    ]],
	    toolbar:'#jxc_ywbt_ywrkTb',
	});
	lnyw.toolbar(2, ywbt_ywrkDg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', did);
	
	ywbt_ywrkDg.datagrid({
        view: detailview,
        detailFormatter:function(index,row){
            return '<div style="padding:2px"><table id="ywbt-ywrk-ddv-' + index + '"></table></div>';
        },
        onExpandRow: function(index,row){
            $('#ywbt-ywrk-ddv-'+index).datagrid({
                url:'${pageContext.request.contextPath}/jxc/ywrkAction!detDatagrid.action',
                fitColumns:true,
                singleSelect:true,
                rownumbers:true,
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
                    {field:'sppc',title:'批次',width:100,align:'center'},
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
                    {field:'bzsl',title:'包装数量',width:100,align:'center'},
                ]],
                onResize:function(){
                	ywbt_ywrkDg.datagrid('fixDetailRowHeight',index);
                },
                onLoadSuccess:function(){
                    setTimeout(function(){
                    	ywbt_ywrkDg.datagrid('fixDetailRowHeight',index);
                    },0);
                }
            });
            ywbt_ywrkDg.datagrid('fixDetailRowHeight',index);
        }
    });
	
	//选中列表标签后，装载数据
	ywbt_tabs = $('#jxc_ywbt_tabs').tabs({
		onSelect: function(title, index){
			if(index == 1){
				ywbt_dg.datagrid({
					url: '${pageContext.request.contextPath}/jxc/ywbtAction!datagrid.action',
					queryParams:{
						bmbh: did,
					}
				});
			}
			if(index == 2){
				ywbt_ywrkDg.datagrid({
					url: '${pageContext.request.contextPath}/jxc/ywrkAction!datagrid.action',
					queryParams: {
						bmbh: did,
						fromOther: 'fromYwbt'
						},
				});
			}
		},
	});
	
	ywbt_spdg = $('#jxc_ywbt_spdg').datagrid({
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
	        {field:'sppp',title:'商品品牌',width:25,align:'center',editor:'textRead',hidden:true},
	        {field:'spbz',title:'商品包装',width:25,align:'center',editor:'textRead',hidden:true},
	        {field:'zjldwmc',title:'单位1',width:25,align:'center',editor:'textRead'},
	        {field:'spje',title:'入库金额',width:25,align:'center',editor:'textRead'},
   	        {field:'btje',title:'补调金额',width:25,align:'center',
   	        	editor:{
           			type:'numberbox',
           			options:{
           				precision: LENGTH_JE
           			}}},
        	{field:'zjldwId',title:'主单位id',width:25,align:'center',editor:'text', hidden:true},
	    ]],
        onClickRow: clickRow,
        onAfterEdit: function (rowIndex, rowData, changes) {
            //endEdit该方法触发此事件
            editIndex = undefined;
        },
        
         
	});
	
	//初始化创建时间
	$('#createDate').html(moment().format('YYYY年MM月DD日'));
	
	$('input').focusin(function(){
		if(editIndex != undefined){
			ywbt_spdg.datagrid('endEdit', editIndex);
		}
	});
	
	//初始化信息
	init();
});

//以下为商品列表处理代码
function init(){
	//清空列表，添加一个空行
	ywbt_spdg.datagrid({data:[],});
	
	//清空全部字段
	$('input').val('');
	
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
				$('#ywbtLsh').html(d.obj);
			}  
		},
	});
	
	//根据权限，动态加载功能按钮
	lnyw.toolbar(0, ywbt_spdg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', did);
	
	//清空合计内容
	ywbt_spdg.datagrid('reloadFooter',[{}]);
}

//判断行是否编辑完成
function rowOk(){
	if(editIndex == undefined){
		return true;
	}
	if(keyOk()){
		if(spjeEditor.target.val() != 0 ){
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
// 	if(rowIndex == ywbt_spdg.datagrid('getRows').length - 1){
// 		ywbt_spdg.datagrid('appendRow', {});
// 	}else{
// 		if(!isClick){
// 			return false;
// 		}
// 	}
	//将选中行标识为编辑行
	editIndex = rowIndex;
	//选择编辑行，并进行入编辑状态
	ywbt_spdg.datagrid('selectRow', editIndex)
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
// 			if(rowOk()){ //行验证通过
				//结束编辑行
				ywbt_spdg.datagrid('endEdit', editIndex);
				//将选中行转为编辑状态
				enterEdit(rowIndex, true);
// 			}else{ //行验证未通过
				//验证关键字段信息
				//通过，不做任何操作，将该行继续完成或删除该行
// 				if(!keyOk()){ //未通过，将该行删除
// 					if(rowIndex > editIndex){
// 						rowIndex = rowIndex - 1;
// 					}
// 					removeRow();
// 					enterEdit(rowIndex, true);
// 				}
// 			}
		}
	}else{ //无编辑行
		enterEdit(rowIndex, true);
	}
}


//提交数据到后台
function saveAll(){
	if(editIndex != undefined){
		ywbt_spdg.datagrid('endEdit', editIndex);
	}	
	var rows = ywbt_spdg.datagrid('getRows');
	if(rows.length == 0){
		$.messager.alert('提示', '未添加商品数据,请继续操作！', 'error');
		return false;
	}
	
	var footerRows = ywbt_spdg.datagrid('getFooterRows');
	var effectRow = new Object();
	//将表头内容传入后台
	
	effectRow['ywrklsh'] = $('input[name=ywrklsh]').val();
	effectRow['bz'] = $('input[name=jxc_ywbt_bz]').val();
	effectRow['hjje'] = lnyw.delcommafy(footerRows[0]['btje']);

	effectRow['bmbh'] = did;
	effectRow['lxbh'] = lx;
	effectRow['menuId'] = menuId;
	
	
	//将表格中的数据去掉最后一个空行后，转换为json格式
	effectRow['datagrid'] = JSON.stringify(rows);
	//提交到action
	//$.ajaxSettings.traditional=true;
	//MaskUtil.mask('正在保存，请等待……');
	$.ajax({
		type: "POST",
		url: '${pageContext.request.contextPath}/jxc/ywbtAction!save.action',
		data: effectRow,
		dataType: 'json',
		success: function(rsp){
			if(rsp.success){
		    	$.messager.show({
					title : '提示',
					msg : '提交成功！'
				});
		    	init();
		    	$.messager.confirm('请确认', '是否打印商品补调单？', function(r) {
					if (r) {
						var url = lnyw.bp() + '/jxc/ywbtAction!printYwbt.action?ywbtlsh=' + rsp.obj.ywbtlsh + '&bmbh=' + did;
						jxc.print(url, PREVIEW_REPORT, HIDE_PRINT_WINDOW, {createId: ${user.id}, createName: "${user.realName}"});
					}
				});
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
	
	//加载字段
	var editors = ywbt_spdg.datagrid('getEditors', editIndex);
    spbhEditor = editors[0];
    spmcEditor = editors[1];
    spcdEditor = editors[2];
    spppEditor = editors[3];
    spbzEditor = editors[4];
    zjldwEditor = editors[5];
    spjeEditor = editors[6];
    btjeEditor = editors[7];
    zjldwIdEditor = editors[8];
    
    btjeEditor.target.focus();
     
	//loadEditor();
	//处理编辑行的换行事件
	$('.datagrid-row-editing').on('keydown','input', function(event){
		//监听按下向下箭头
		if(event.keyCode == 40){
			var rowIndex = editIndex;
			if(rowOk()){
				ywbt_spdg.datagrid('endEdit', editIndex);
				enterEdit(rowIndex + 1, false);
			}else{
				if(!keyOk()){
					removeRow();
				}
			}
		}
	});
    	
  	//输入每行总额后,重新计算单价
    btjeEditor.target.bind('keyup', function(event){
    	updateFooter();
    }).bind('keydown', function(event){
    	if(event.keyCode == 40){
//      		//spjeEditor.target.focus();
     	}
    });
}

//求和
function updateFooter(){
	var rows = ywbt_spdg.datagrid('getRows');
	var spmc_footer = '合计';
	var hjje = 0.0000;
	$.each(rows, function(){
		var index = ywbt_spdg.datagrid('getRowIndex', this);
		if(index < rows.length){
			if(editIndex == index){
				hjje += Number(btjeEditor.target.val());
			}else{
				
				hjje += Number(this.btje == undefined ? 0 : this.btje);
			}
		}
 	});
	ywbt_spdg.datagrid('reloadFooter', [{spmc : spmc_footer, btje : lnyw.formatNumberRgx(hjje.toFixed(LENGTH_JE))}]);
}

//////////////////////////////////////////////以上为商品列表处理代码

//////////////////////////////////////////////以下为业务补调列表处理代码
function printYwbt(){
	var row = ywbt_dg.datagrid('getSelected');
	if (row != undefined) {
		$.messager.confirm('请确认', '是否打印商品补调单？', function(r) {
			if (r) {
				var url = lnyw.bp() + '/jxc/ywbtAction!printYwbt.action?ywbtlsh=' + row.ywbtlsh + '&bmbh=' + did;
				jxc.print(url, PREVIEW_REPORT, HIDE_PRINT_WINDOW, {createId: ${user.id}, createName: "${user.realName}"});
			}
		});
	}else{
		$.messager.alert('警告', '请选择一条记录进行操作！',  'warning');
	}
}

function searchYwbt(){
	ywbt_dg.datagrid('load',{
		bmbh: did,
		createTime: $('input[name=createTimeYwbt]').val(),
	});
}
//////////////////////////////////////////////以上为业务入库列表处理代码


//////////////////////////////////////////////以下为业务入库列表处理代码
function generateYwbt(){
	var row = ywbt_ywrkDg.datagrid('getSelected');
	if(row != undefined){
		$.messager.confirm('请确认', '是否要将选中记录进行业务补调？', function(r) {
			if (r) {
				$.ajax({
					url : '${pageContext.request.contextPath}/jxc/ywrkAction!toYwbt.action',
					data : {
						ywrklsh: row.ywrklsh
					},
					dataType : 'json',
					success : function(d) {
						ywbt_spdg.datagrid('loadData', d.rows);
						$('input[name=ywrklsh]').val(row.ywrklsh);
						updateFooter();
						ywbt_tabs.tabs('select', 0);
					}
				});
			}
		});
	}else{
		$.messager.alert('警告', '请选择最少一条记录进行操作！',  'warning');
	}
}

function searchYwrkInYwbt(){
	ywbt_ywrkDg.datagrid('load',{
		bmbh: did,
		createTime: $('input[name=createTimeYwrkInYwbt]').val(),
		fromOther: 'fromYwbt'
	});
}

//////////////////////////////////////////////以上为业务入库列表处理代码

</script>

<!-- tabPosition:'left', headerWidth:'35' -->
<div id="jxc_ywbt_tabs" class="easyui-tabs" data-options="fit:true, border:false," style="width:100%;height:100%;">
    <div title="新增记录" data-options="closable:false">
        <div id='jxc_ywbt_layout' style="height:100%;width=100%">
			<div data-options="region:'north',title:'单据信息',border:false,collapsible:false" style="width:100%;height:105px">		
				<table class="tinfo">
					<tr>
						<th class="read">时间</th><td><div id="createDate" class="read"></div></td>
						<th class="read">单据号</th><td><div id="ywbtLsh" class="read"></div></td>
					</tr>
					<tr>
						<th>业务入库流水号</th><td><input name="ywrklsh" disabled="disabled" size="12"></td>
					</tr>
					<tr>
						<th>备注</th><td><input name="jxc_ywbt_bz" size="100" ></td>
					</tr>
				</table>
			</div>
			<div data-options="region:'center',title:'商品信息',split:true" style="width:150px">		
				<table id='jxc_ywbt_spdg'></table>
			</div>
<!-- 			<div data-options="region:'east',title:'商品库存信息',split:true,collapsed:true," style="width:150px"> -->
<!-- 				<table id="show_spkc"></table> -->
<!-- 			</div> -->
		</div>
    </div>
    <div title="业务补调列表" data-options="closable:false" >
    	<table id='jxc_ywbt_dg'></table>
    </div>
	<div title="业务入库列表" data-options="closable:false" >
		<table id='jxc_ywbt_ywrkDg'></table>
	</div>
</div>

<div id="jxc_ywbt_tb" style="padding:3px;height:auto">
	请输入查询起始日期:<input type="text" name="createTimeYwbt" class="easyui-datebox" data-options="value: moment().date(1).format('YYYY-MM-DD')" style="width:100px">
	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchYwbt();">查询</a>
</div>
<div id="jxc_ywbt_ywrkTb" style="padding:3px;height:auto">
	请输入查询起始日期:<input type="text" name="createTimeYwrkInYwbt" class="easyui-datebox" data-options="value: moment().date(1).format('YYYY-MM-DD')" style="width:100px">
	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchYwrkInYwbt();">查询</a>
</div>

