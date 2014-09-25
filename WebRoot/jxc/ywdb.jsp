<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>


<script type="text/javascript">
var ywdb_did;
var ywdb_lx;
var ywdb_menuId;
var ywdb_spdg;
var ywdb_dg;
var ywdb_tabs;
var jxc_ywdb_ckComboF;
var jxc_ywdb_ckComboT;

var spRow;

var editIndex = undefined;
//编辑行字段
var spbhEditor;   
var spmcEditor;
var spcdEditor;
var spppEditor;
var spbzEditor;
var zjldwEditor;
var zslEditor;
var cjldwEditor;
var cslEditor;
var zhxsEditor;
var zjldwIdEditor;
var cjldwIdEditor;

$(function(){
	ywdb_did = lnyw.tab_options().did;
	ywdb_lx = lnyw.tab_options().lx;
	ywdb_menuId = lnyw.tab_options().id;
	
	$('#jxc_ywdb_layout').layout({
		fit : true,
		border : false,
	});
	
	ywdb_dg = $('#jxc_ywdb_dg').datagrid({
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
			{field:'ywdblsh',title:'流水号',align:'center'},
	        {field:'createTime',title:'时间',align:'center'},
	        {field:'ckIdF',title:'原仓库id',align:'center',hidden:true},
	        {field:'ckmcF',title:'原仓库名称',align:'center'},
	        {field:'ckIdT',title:'目的仓库id',align:'center',hidden:true},
	        {field:'ckmcT',title:'目的仓库名称',align:'center'},
        	{field:'bz',title:'备注',align:'center',
        		formatter: function(value){
        			return lnyw.memo(value, 15);
        		}},
        	{field:'kfdblsh',title:'库房调拨流水号',align:'center',
               		formatter: function(value){
               			return lnyw.memo(value, 15);
               		}},
        	{field:'isCj',title:'状态',align:'center',sortable:true,
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
        	{field:'cjYwdblsh',title:'原调拨流水号',align:'center'},
	    ]],
	    toolbar:'#jxc_ywdb_tb',
	});
	lnyw.toolbar(1, ywdb_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', ywdb_did);
	
	
	ywdb_dg.datagrid({
        view: detailview,
        detailFormatter:function(index,row){
            return '<div style="padding:2px"><table id="ywdb-ddv-' + index + '"></table></div>';
        },
        onExpandRow: function(index,row){
            $('#ywdb-ddv-'+index).datagrid({
                url:'${pageContext.request.contextPath}/jxc/ywdbAction!detDatagrid.action',
                fitColumns:true,
                singleSelect:true,
                rownumbers:true,
                loadMsg:'',
                height:'auto',
                queryParams: {
        			ywdblsh: row.ywdblsh,
        		},
                columns:[[
                    {field:'spbh',title:'商品编号',width:200,align:'center'},
                    {field:'spmc',title:'名称',width:100,align:'center'},
                    {field:'spcd',title:'产地',width:100,align:'center'},
                    {field:'sppp',title:'品牌',width:100,align:'center'},
                    {field:'spbz',title:'包装',width:100,align:'center'},
                    {field:'zjldwmc',title:'单位1',width:100,align:'center'},
                    {field:'zdwsl',title:'数量1',width:100,align:'center',
                       	formatter: function(value){
                       		return value == 0 ? '' : value;
                   		}},
                    {field:'cjldwmc',title:'单位2',width:100,align:'center'},
                    {field:'cdwsl',title:'数量2',width:100,align:'center',
                       	formatter: function(value){
                       		return value == 0 ? '' : value;
                   		}},
                ]],
                onResize:function(){
                	ywdb_dg.datagrid('fixDetailRowHeight',index);
                },
                onLoadSuccess:function(){
                    setTimeout(function(){
                    	ywdb_dg.datagrid('fixDetailRowHeight',index);
                    },0);
                }
            });
            ywdb_dg.datagrid('fixDetailRowHeight',index);
        }
    });
	
	
	//选中列表标签后，装载数据
	ywdb_tabs = $('#jxc_ywdb_tabs').tabs({
		onSelect: function(title, index){
			if(index == 1){
				ywdb_dg.datagrid({
					url: '${pageContext.request.contextPath}/jxc/ywdbAction!datagrid.action',
					queryParams:{
						bmbh: ywdb_did,
					}
				});
			}
		},
	});
	
	ywdb_spdg = $('#jxc_ywdb_spdg').datagrid({
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
	        {field:'zdwsl',title:'数量1',width:25,align:'center',
	        	editor:{
	        		type:'numberbox',
	        		options:{
	        			//精度
	        			precision:3,
	        		}}},
	        {field:'cjldwmc',title:'单位2',width:25,align:'center',editor:'textRead'},
	        {field:'cdwsl',title:'数量2',width:25,align:'center',
	        		editor:{
        				type:'numberbox',
        				options:{
        					//精度
        					precision:3,
        			}}},
        	{field:'zhxs',title:'转换系数',width:25,align:'center',editor:'text', hidden:true},
        	{field:'zjldwId',title:'主计量单位id',align:'center',editor:'text', hidden:true},
        	{field:'cjldwId',title:'次计量单位id',align:'center',editor:'text', hidden:true},
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
	jxc_ywdb_ckComboF = lnyw.initCombo($("#jxc_ywdb_ckIdF"), 'id', 'ckmc', '${pageContext.request.contextPath}/jxc/ckAction!listCk.action?depId=' + ywdb_did);
	jxc_ywdb_ckComboT = lnyw.initCombo($("#jxc_ywdb_ckIdT"), 'id', 'ckmc', '${pageContext.request.contextPath}/jxc/ckAction!listCk.action?depId=' + ywdb_did);
	
	//初始化信息
	init();
});

//以下为商品列表处理代码
function init(){
	//清空列表，添加一个空行
	ywdb_spdg.datagrid({
		data: [{}],
	});
	
	//清空全部字段
	$('input').val('');
	//收回商品库存信息
	jxc.hideKc('#jxc_ywdb_layout');
	jxc.spInfo($('#jxc_ywdb_layout'), '');
	
	 
	jxc_ywdb_ckComboF.combobox('selectedIndex', 0);
	jxc_ywdb_ckComboT.combobox('selectedIndex', 0);
	
	//初始化流水号
	$.ajax({
		type: "POST",
		url: '${pageContext.request.contextPath}/jxc/lshAction!getLsh.action',
		data: {
			bmbh: ywdb_did,
			lxbh: ywdb_lx,
		},
		dataType: 'json',
		success: function(d){
			if(d.success){
				$('#ywdbLsh').html(d.obj);
			}  
		},
	});
	
	//根据权限，动态加载功能按钮
	lnyw.toolbar(0, ywdb_spdg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', ywdb_did);
	//清空合计内容
	ywdb_spdg.datagrid('reloadFooter',[{}]);
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
	if(rowIndex == ywdb_spdg.datagrid('getRows').length - 1){
		ywdb_spdg.datagrid('appendRow', {});
	}else{
		if(!isClick){
			return false;
		}
	}
	//将选中行标识为编辑行
	editIndex = rowIndex;
	//选择编辑行，并进行入编辑状态
	ywdb_spdg.datagrid('selectRow', editIndex)
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
				ywdb_spdg.datagrid('endEdit', editIndex);
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
    ywdb_spdg.datagrid('cancelEdit', editIndex)
            .datagrid('deleteRow', editIndex);
    editIndex = undefined;
    updateFooter();
    jxc.hideKc('#jxc_ywdb_layout');
}

//保存行
function accept(){
    if (rowOk()){
    	ywdb_spdg.datagrid('acceptChanges');
    	jxc.hideKc('#jxc_ywdb_layout');
    }
}

//取消编辑行
function cancelAll(){
	ywdb_spdg.datagrid('rejectChanges');
    editIndex = undefined;
    updateFooter();
    jxc.hideKc('#jxc_ywdb_layout');
}

//提交数据到后台
function saveYwdb(){
	var msg = formValid();
	if(msg == ''){
		//编辑行是否完成
		if(rowOk()){
			ywdb_spdg.datagrid('endEdit', editIndex);
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
	
	var rows = ywdb_spdg.datagrid('getRows');
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
	var footerRows = ywdb_spdg.datagrid('getFooterRows');
	var effectRow = new Object();
	//将表头内容传入后台
	effectRow['ckIdF'] = jxc_ywdb_ckComboF.combobox('getValue');
	effectRow['ckmcF'] = jxc_ywdb_ckComboF.combobox('getText');
	effectRow['ckIdT'] = jxc_ywdb_ckComboT.combobox('getValue');
	effectRow['ckmcT'] = jxc_ywdb_ckComboT.combobox('getText');
	effectRow['bz'] = $('input[name=jxc_ywdb_bz]').val();
	effectRow['bmbh'] = ywdb_did;
	effectRow['lxbh'] = ywdb_lx;
	effectRow['menuId'] = ywdb_menuId;
	
	//将表格中的数据去掉最后一个空行后，转换为json格式
	effectRow['datagrid'] = JSON.stringify(rows.slice(0, rows.length - 1));
	//提交到action
	//$.ajaxSettings.traditional=true;
	$.ajax({
		type: "POST",
		url: '${pageContext.request.contextPath}/jxc/ywdbAction!save.action',
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
		}
	});
}

//处理编辑行
function setEditing(){
	//初始化编辑行
	var spRow = undefined;
	
	//加载字段
	var editors = ywdb_spdg.datagrid('getEditors', editIndex);
    spbhEditor = editors[0];
    spmcEditor = editors[1];
    spcdEditor = editors[2];
    spppEditor = editors[3];
    spbzEditor = editors[4];
    zjldwEditor = editors[5];
    zslEditor = editors[6];
    cjldwEditor = editors[7];
    cslEditor = editors[8];
    zhxsEditor = editors[9];
    zjldwIdEditor = editors[10];
    cjldwIdEditor = editors[11];
    
    if($(spbhEditor.target).val() != ''){
    	jxc.spInfo($('#jxc_ywdb_layout'), '1', $(spppEditor.target).val(), $(spbzEditor.target).val());
    	
    	jxc.showKc('#jxc_ywdb_layout', 
    			'${pageContext.request.contextPath}/jxc/ywdbAction!getSpkc.action', 
    			{
    				bmbh : ywdb_did, 
    				ckId : jxc_ywdb_ckComboF.combobox('getValue'),
//     				fhId : fhValue,
    				spbh : $(spbhEditor.target).val(),
    			});
    }else{
    	jxc.spInfo($('#jxc_ywdb_layout'), '');
    	jxc.hideKc('#jxc_ywdb_layout');
    }
    
  	//loadEditor();
	//处理编辑行的换行事件
	$('.datagrid-row-editing').on('keydown','input', function(event){
		//监听按下向下箭头
		if(event.keyCode == 40){
			var rowIndex = editIndex;
			if(rowOk()){
				ywdb_spdg.datagrid('endEdit', editIndex);
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
    						depId : ywdb_did,
    					},
    					dataType:'json',
    					success:function(data){
    						if(data.success){
    							//设置信息字段值
    							setValueBySpbh(data.obj);
    							//spOk = true;
    							hwIdEditor.target.focus();
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
    		spRow = jxc.spQuery($(spbhEditor.target).val(),
   				ywdb_did,
   				jxc_ywdb_ckComboF.combobox('getValue'),
   				'${pageContext.request.contextPath}/jxc/spQuery.jsp',
   				'${pageContext.request.contextPath}/jxc/spAction!spDg.action',
   				zslEditor  				
   				);
    		return false;
    	}
    });
    
    //输入主单位数量后，计算次单位数量
    zslEditor.target.bind('keyup', function(event){
		var kcRow = $('#show_spkc').propertygrid("getRows");
	    
	    //判断输入数量是否小于可调拨数量
    	var kxssl = undefined;
    	if(kcRow == undefined){
    		kxssl = Number(0);
    	}else{
    		kxssl = Number(kcRow[0].value);
    	}
    	var zsl = Number($(zslEditor.target).val());
    	if(zsl > kxssl){
    		$.messager.alert("提示", "调拨数量不能大于可调拨数量，请重新输入！");
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
     		bzslEditor.target.focus();
     		return false;
     	}
    });
    
    //汇总计算
    function calculate(){
        //更新汇总列
        updateFooter();
    }
  	
    //初始进入编辑状态时，使用商品编号获得焦点
    spbhEditor.target.focus();
}
//求和
function updateFooter(){
 	var rows = ywdb_spdg.datagrid('getRows');
	var spmc_footer = '合计';
	var hjsl = 0.000000;
	$.each(rows, function(){
		var index = ywdb_spdg.datagrid('getRowIndex', this);
		if(index < rows.length - 1){
		if(editIndex == index){
			hjsl += Number(cslEditor.target.val());
		}else{
			hjsl += Number(this.cdwsl);
		}
	}
 		
 	});
	
	ywdb_spdg.datagrid('reloadFooter', [{spmc : spmc_footer, cdwsl : hjsl !== hjsl ? '' : hjsl.toFixed(LENGTH_SL)}]);
}

//验证记录是否已存在
function existKey(value, rowIndex){
	var keys = ywdb_spdg.datagrid('getRows');
	var exist = false;
	if(keys.length > 2){
		$.each(keys, function(){
			var index = ywdb_spdg.datagrid('getRowIndex', this);
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
	if(jxc_ywdb_ckComboF.combobox('getValue') == jxc_ywdb_ckComboT.combobox('getValue')){
		message += '请选择不同的仓库<br>';
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
	
	jxc.spInfo($('#jxc_ywdb_layout'), '1', rowData.sppp, rowData.spbz);
	//jxc.showKc('#jxc_ywdb_layout', '${pageContext.request.contextPath}', ywdb_did, $(spbhEditor.target).val());
	jxc.showKc('#jxc_ywdb_layout', 
			'${pageContext.request.contextPath}/jxc/ywdbAction!getSpkc.action', 
			{
				bmbh : ywdb_did, 
				ckId : jxc_ywdb_ckComboF.combobox('getValue'),
// 				fhId : fhValue,
				spbh : $(spbhEditor.target).val(),
			});
}

//////////////////////////////////////////////以上为商品列表处理代码

//////////////////////////////////////////////以下为库房入库划列表处理代码
function cjYwdb(){
	var row = ywdb_dg.datagrid('getSelected');
	if (row != undefined) {
		if(!row.cjYwdblsh || row.isCj != '1'){
			if(row.kfdblsh == null){
				$.messager.prompt('请确认', '是否要冲减选中的业务调拨？请填写备注', function(bz){
					if (bz != undefined){
						$.ajax({
							url : '${pageContext.request.contextPath}/jxc/ywdbAction!cjYwdb.action',
							data : {
								ywdblsh : row.ywdblsh,
								bmbh: ywdb_did,
								lxbh: ywdb_lx,
								menuId : ywdb_menuId,
								bz : bz,
							},
							method: 'post',
							dataType : 'json',
							success : function(d) {
								ywdb_dg.datagrid('load');
								ywdb_dg.datagrid('unselectAll');
								$.messager.show({
									title : '提示',
									msg : d.msg
								});
							}
						});
					}
				});
			}else{
				$.messager.alert('警告', '选中的调拨单已进行库房调拨，不能被冲减，请重新选择！',  'warning');
			}
		}else{
			$.messager.alert('警告', '选中的业务调拨已冲减，请重新选择！',  'warning');
		}
	}else{
		$.messager.alert('警告', '请选择一条记录进行操作！',  'warning');
	}
}

function searchYwdb(){
	ywdb_dg.datagrid('load',{
		bmbh: ywdb_did,
		createTime: $('input[name=createTimeYwdb]').val(),
	});
}

//////////////////////////////////////////////以上为业务调拨列表处理代码


</script>

<div id="jxc_ywdb_tabs" class="easyui-tabs" data-options="fit:true, border:false," style="width:100%;height:100%;">
	
    <div title="新增记录" data-options="closable:false">
        <div id='jxc_ywdb_layout' style="height:100%;width=100%">
			<div data-options="region:'north',title:'单据信息',border:false,collapsible:false" style="width:100%;height:150px">		
				<table class="tinfo">
					<tr>
						<td colspan="4"></td>
						<th class="read">时间</th><td><div id="createDate" class="read"></div></td>
						<th class="read">单据号</th><td><div id="ywdbLsh" class="read"></div></td>
					</tr>
					<tr>
						<th>源仓库</th><td><input id="jxc_ywdb_ckIdF" name="ckIdF" size="8"></td>
						<th>目的仓库</th><td><input id="jxc_ywdb_ckIdT" name="ckIdT" size="8"></td>
					</tr>
					<tr>
						<th>备注</th><td colspan="7"><input name="jxc_ywdb_bz" style="width:90%"></td>
					</tr>
				</table>
			</div>
			<div data-options="region:'center',title:'商品信息',split:true" style="width:150px">		
				<table id='jxc_ywdb_spdg'></table>
			</div>
			<div data-options="region:'east',title:'商品库存信息',split:true,collapsed:true," style="width:150px">
				<table id="show_spkc"></table>
			</div>
		</div>
    </div>
    <div title="业务调拨列表" data-options="closable:false" >
    	<table id='jxc_ywdb_dg'></table>
    </div>
</div>

<div id="jxc_ywdb_tb" style="padding:3px;height:auto">
	请输入查询起始日期:<input type="text" name="createTimeYwdb" class="easyui-datebox" data-options="value: moment().date(1).format('YYYY-MM-DD')" style="width:100px">
	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchYwdb();">查询</a>
</div>
