<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>


<script type="text/javascript">
var jxc_bgrk_did;
var jxc_bgrk_lx;
var jxc_bgrk_menuId;
var bgrk_bmbh;
var bgrk_spdg;
var bgrk_dg;
var bgrk_tabs;

var spRow;

var editIndex = undefined;
//编辑行字段
var spbhEditor;   
var spmcEditor;
var spcdEditor;
var spppEditor;
var spbzEditor;
var zjldwEditor;
var zrkslEditor;
var zslEditor;
var cjldwEditor;
var zhxsEditor;
var zjldwIdEditor;
var cjldwIdEditor;

$(function(){
    jxc_bgrk_did = lnyw.tab_options().did;
    jxc_bgrk_lx = lnyw.tab_options().lx;
    jxc_bgrk_menuId = lnyw.tab_options().id;
    bgrk_bmbh = '04';
	
	$('#jxc_bgrk_layout').layout({
		fit : true,
		border : false,
	});
	
	bgrk_dg = $('#jxc_bgrk_dg').datagrid({
		fit : true,
	    border : false,
	    singleSelect : true,
	    remoteSort: false,
	    fitColumns: true,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
		frozenColumns:[[
		]],
		columns:[[
            {field:'bmmc',title:'部门',align:'center'},
            {field:'lsh',title:'单据流水号',align:'center'},
	        {field:'createTime',title:'时间',align:'center'},
	        {field:'gysbh',title:'供应商编号',align:'center'},
	        {field:'gysmc',title:'供应商名称',align:'center'},
        	{field:'bz',title:'备注',align:'center'},
            {field:'bgrklsh',title:'流水号',align:'center',
                styler: function(value, rowData){
                    if(rowData.isCj == '1'){
                        return 'color:red;';
                    }
                }},
			{field:'cjTime',title:'冲减时间',align:'center'},
        	{field:'cjBgrklsh',title:'原保管入库流水号',align:'center'},
	    ]],
	    toolbar:'#jxc_bgrk_tb',
	});
	lnyw.toolbar(1, bgrk_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', jxc_bgrk_did);
	
	
	bgrk_dg.datagrid({
        view: detailview,
        detailFormatter:function(index,row){
            return '<div style="padding:2px"><table id="bgrk-ddv-' + index + '"></table></div>';
        },
        onExpandRow: function(index,row){
            $('#bgrk-ddv-'+index).datagrid({
                url:'${pageContext.request.contextPath}/jxc/bgrkAction!detDatagrid.action',
                fitColumns:true,
                singleSelect:true,
                rownumbers:true,
                loadMsg:'',
                height:'auto',
                queryParams: {
        			bgrklsh: row.bgrklsh,
        		},
                columns:[[
                    {field:'spbh',title:'商品编号',width:200,align:'center'},
                    {field:'spmc',title:'名称',width:100,align:'center'},
                    {field:'spcd',title:'产地',width:100,align:'center'},
                    {field:'sppp',title:'品牌',width:100,align:'center'},
                    {field:'spbz',title:'包装',width:100,align:'center'},
                    {field:'zjldwmc',title:'单位',width:100,align:'center'},
                    {field:'zdwsl',title:'数量',width:100,align:'center',
                       	formatter: function(value){
                       		return value == 0 ? '' : value;
                   		}},
                    // {field:'bzsl',title:'包装数量',width:100,align:'center',
                    //    	formatter: function(value){
                    //    		return value == 0 ? '' : value;
                   	// 	}},
                ]],
                onResize:function(){
                	bgrk_dg.datagrid('fixDetailRowHeight',index);
                },
                onLoadSuccess:function(){
                    setTimeout(function(){
                    	bgrk_dg.datagrid('fixDetailRowHeight',index);
                    },0);
                }
            });
            bgrk_dg.datagrid('fixDetailRowHeight',index);
        }
    });
	
	//选中列表标签后，装载数据
	bgrk_tabs = $('#jxc_bgrk_tabs').tabs({
		onSelect: function(title, index){
			if(index == 1){
				bgrk_dg.datagrid({
					url: '${pageContext.request.contextPath}/jxc/bgrkAction!datagrid.action',
					queryParams:{
						bmbh: jxc_bgrk_did,
					}
				});
			}
		},
	});
	
	bgrk_spdg = $('#jxc_bgrk_spdg').datagrid({
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
	        {field:'zjldwmc',title:'单位',width:25,align:'center',editor:'textRead'},
	        {field:'zdwrksl',title:'单据数量',width:25,align:'center',editor:'textRead'},
	        {field:'zdwsl',title:'到货数量',width:25,align:'center',
	        	editor:{
	        		type:'numberbox',
	        		options:{
	        			//精度
	        			precision:3,
	        		}}},
            {field:'cjldwmc',title:'单位',width:25,align:'center',editor:'textRead', hidden:true},
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

    $('input[name=bmbh]').click(function(){
        if($('input#bgrk_bmbh_cb').is(':checked')){
            bgrk_bmbh = '04';
        }else{
            bgrk_bmbh = '05';
        }
    });

	//初始化创建时间
	$('#createDate').html(moment().format('YYYY年MM月DD日'));
	
	//初始化信息
	init();
});

//以下为商品列表处理代码
function init(){
	//清空列表，添加一个空行
	bgrk_spdg.datagrid({
		data: [{}],
	});
	
	//清空全部字段
	$('input').val('');
	//收回商品库存信息
	jxc.hideKc('#jxc_bgrk_layout');
	jxc.spInfo($('#jxc_bgrk_layout'), '');

	//初始化流水号
	<%--$.ajax({--%>
		<%--type: "POST",--%>
		<%--url: '${pageContext.request.contextPath}/jxc/lshAction!getLsh.action',--%>
		<%--data: {--%>
			<%--bmbh: bgrk_bmbh,--%>
			<%--lxbh: jxc_bgrk_lx,--%>
		<%--},--%>
		<%--dataType: 'json',--%>
		<%--success: function(d){--%>
			<%--if(d.success){--%>
				<%--$('#bgrkLsh').html(d.obj);--%>
			<%--}  --%>
		<%--},--%>
	<%--});--%>
	
	//根据权限，动态加载功能按钮
	lnyw.toolbar(0, bgrk_spdg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', jxc_bgrk_did);
	//清空合计内容
	// bgrk_spdg.datagrid('reloadFooter',[{}]);
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
	if(rowIndex == bgrk_spdg.datagrid('getRows').length - 1){
		bgrk_spdg.datagrid('appendRow', {});
	}else{
		if(!isClick){
			return false;
		}
	}
	//将选中行标识为编辑行
	editIndex = rowIndex;
	//选择编辑行，并进行入编辑状态
	bgrk_spdg.datagrid('selectRow', editIndex)
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
				bgrk_spdg.datagrid('endEdit', editIndex);
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
    bgrk_spdg.datagrid('cancelEdit', editIndex)
            .datagrid('deleteRow', editIndex);
    editIndex = undefined;
    jxc.hideKc('#jxc_bgrk_layout');
}

//保存行
function accept(){
    if (rowOk()){
    	bgrk_spdg.datagrid('acceptChanges');
    	jxc.hideKc('#jxc_bgrk_layout');
    }
}

//取消编辑行
function cancelAll(){
	bgrk_spdg.datagrid('rejectChanges');
    editIndex = undefined;
    updateFooter();
    jxc.hideKc('#jxc_bgrk_layout');
}

//提交数据到后台
function saveBgrk(){
	var msg = formValid();
	if(msg == ''){
		//编辑行是否完成
		if(rowOk()){
			bgrk_spdg.datagrid('endEdit', editIndex);
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
	
	var rows = bgrk_spdg.datagrid('getRows');
	if(rows.length == 0){
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
	//将表头内容传入后台
	effectRow['gysbh'] = $('input[name=gysbh]').val();
	effectRow['gysmc'] = $('input[name=gysmc]').val();
	effectRow['lsh'] = $('input[name=bgrk_lsh]').val();
	effectRow['bz'] = $('input[name=jxc_bgrk_bz]').val();
	effectRow['bmbh'] = bgrk_bmbh;
	effectRow['lxbh'] = jxc_bgrk_lx;
	effectRow['menuId'] = jxc_bgrk_menuId;
	
	//将表格中的数据去掉最后一个空行后，转换为json格式
	//effectRow['datagrid'] = JSON.stringify(rows.slice(0, rows.length - 1));
	effectRow['datagrid'] = JSON.stringify(rows.slice(0, rows.length));
	//提交到action
	//$.ajaxSettings.traditional=true;
	//MaskUtil.mask('正在保存，请等待……');
	$.ajax({
		type: "POST",
		url: '${pageContext.request.contextPath}/jxc/bgrkAction!save.action',
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

//处理编辑行
function setEditing(){
	//初始化编辑行
	var spRow = undefined;
	
	//加载字段
	var editors = bgrk_spdg.datagrid('getEditors', editIndex);
    spbhEditor = editors[0];
    spmcEditor = editors[1];
    spcdEditor = editors[2];
    spppEditor = editors[3];
    spbzEditor = editors[4];
    zjldwEditor = editors[5];
    zrkslEditor = editors[6];
    zslEditor = editors[7];
    cjldwEditor = editors[8];
    zhxsEditor = editors[9];
    zjldwIdEditor = editors[10];
    cjldwIdEditor = editors[11];

    if($(spbhEditor.target).val() != ''){
    	jxc.spInfo($('#jxc_bgrk_layout'), '1', $(spppEditor.target).val(), $(spbzEditor.target).val());
        jxc.showKc('#jxc_bgrk_layout',
            '${pageContext.request.contextPath}/jxc/bgrkAction!getSpkc.action',
            {
                bmbh : bgrk_bmbh,
                spbh : $(spbhEditor.target).val(),
            });
    }else{
    	jxc.spInfo($('#jxc_bgrk_layout'), '');
    	jxc.hideKc('#jxc_bgrk_layout');
    }
    
	//loadEditor();
	//处理编辑行的换行事件
	$('.datagrid-row-editing').on('keydown','input', function(event){
		//监听按下向下箭头
		if(event.keyCode == 40){
			var rowIndex = editIndex;
			if(rowOk()){
				bgrk_spdg.datagrid('endEdit', editIndex);
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
    						depId : bgrk_bmbh,
    					},
    					dataType:'json',
    					success:function(data){
    						if(data.success){
    							//设置信息字段值
    							setValueBySpbh(data.obj);
    							//spOk = true;
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
    		spRow = jxc.spQuery($(spbhEditor.target).val(),
                bgrk_bmbh,
   				undefined,
   				'${pageContext.request.contextPath}/jxc/spQuery.jsp',
   				'${pageContext.request.contextPath}/jxc/spAction!spDg.action',
   				zslEditor
   				);
    		return false;
    	}
    });

    //初始进入编辑状态时，使用商品编号获得焦点
    spbhEditor.target.focus();
}

//验证记录是否已存在
function existKey(value, rowIndex){
	var keys = bgrk_spdg.datagrid('getRows');
	var exist = false;
	if(keys.length > 2){
		$.each(keys, function(){
			var index = bgrk_spdg.datagrid('getRowIndex', this);
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
	if(rowData.spbh.substring(0, 3) >= '513' && rowData.spbh.substring(0, 3) <= '518'){
		zhxsEditor.target.val(0);
	}else{
		zhxsEditor.target.val(rowData.zhxs);
	}
	zjldwIdEditor.target.val(rowData.zjldwId);
    cjldwIdEditor.target.val(rowData.cjldwId);

	jxc.spInfo($('#jxc_bgrk_layout'), '1', rowData.sppp, rowData.spbz);
	jxc.showKc('#jxc_bgrk_layout',
			'${pageContext.request.contextPath}/jxc/bgrkAction!getSpkc.action',
			{
				bmbh : bgrk_bmbh,
				spbh : $(spbhEditor.target).val(),
			});
}

<%--function gysLoad(){--%>
<%--	switch(event.keyCode){--%>
<%--	case 27:--%>
<%--		jxc.query('供应商检索', $('input[name=gysbh]'), $('input[name=gysmc]'), '',--%>
<%--				'${pageContext.request.contextPath}/jxc/query.jsp',--%>
<%--				'${pageContext.request.contextPath}/jxc/gysAction!gysDg.action');--%>
<%--		break;--%>
<%--	case 9:--%>
<%--		break;--%>
<%--	default:--%>
<%--		if($('input[name=gysbh]').val().trim().length == 0){--%>
<%--			$('input[name=gysmc]').val('');--%>
<%--		}--%>
<%--		if($('input[name=gysbh]').val().trim().length == 8){--%>
<%--			$.ajax({--%>
<%--				url:'${pageContext.request.contextPath}/jxc/gysAction!loadGys.action',--%>
<%--				async: false,--%>
<%--				context:this,--%>
<%--				data:{--%>
<%--					gysbh: $('input[name=gysbh]').val().trim(),--%>
<%--				},--%>
<%--				dataType:'json',--%>
<%--				success:function(data){--%>
<%--					if(data.success){--%>
<%--						//设置信息字段值--%>
<%--						$('input[name=gysmc]').val(data.obj.gysmc);--%>
<%--						$('input[name=shry]').focus();--%>
<%--					}else{--%>
<%--						$.messager.alert('提示', '供应商信息不存在！', 'error');--%>
<%--					}--%>
<%--				}--%>
<%--			});--%>
<%--		}--%>
<%--		break;--%>
<%--	}--%>
<%--}--%>

function loadKfrk() {
    if ($('input[name=bgrk_lsh]').val().trim().length == 12){
        if ($('input[name=bgrk_lsh]').val().trim().substr(4, 2) !== bgrk_bmbh) {
            bgrk_bmbh = $('input[name=bgrk_lsh]').val().trim().substr(4, 2);
            if (bgrk_bmbh === '04') {
                $('input#bgrk_bmbh_cb').prop('checked', 'checked');
            } else {
                $('input#bgrk_bmbh_zy').prop('checked', 'checked');
            }
        }
        $.ajax({
            url: '${pageContext.request.contextPath}/jxc/kfrkAction!loadKfrk.action',
            async: false,
            context: this,
            data:{
                kfrklsh: $('input[name=bgrk_lsh]').val().trim(),
            },
            dataType: 'json',
            success:function(data){
                if(data.obj){
                    //设置信息字段值
                    $('input[name=gysbh]').val(data.obj.gysbh);
                    $('input[name=gysmc]').val(data.obj.gysmc);
                    $('input[name=bz]').val(data.obj.bz);
                    bgrk_spdg.datagrid('loadData', data.rows);
                }else{
                    $.messager.alert('提示', data.msg, 'error');
                }
            }
        });
    } else {
        $.messager.alert('提示', '库房入库单流水号为12位，请检查确认！', 'error');
    }
}
//////////////////////////////////////////////以上为商品列表处理代码

//////////////////////////////////////////////以下为库房入库划列表处理代码
function cjBgrk(){
	var row = bgrk_dg.datagrid('getSelected');
	if (row != undefined) {
		if(!row.cjBgrklsh || row.isCj != '1'){
            $.messager.prompt('请确认', '是否要冲减选中的保管入库？请填写备注', function(bz){
                if (bz != undefined){
                    //MaskUtil.mask('正在冲减，请等待……');
                    $.ajax({
                        url : '${pageContext.request.contextPath}/jxc/bgrkAction!cjBgrk.action',
                        data : {
                            bgrklsh : row.bgrklsh,
                            lxbh: jxc_bgrk_lx,
                            menuId : jxc_bgrk_menuId,
                            bz : bz,
                        },
                        method: 'post',
                        dataType : 'json',
                        success : function(d) {
                            bgrk_dg.datagrid('load');
                            bgrk_dg.datagrid('unselectAll');
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
			$.messager.alert('警告', '选中的保管入库单已冲减，请重新选择！',  'warning');
		}
	}else{
		$.messager.alert('警告', '请选择一条记录进行操作！',  'warning');
	}
}

function searchBgrk(){
	bgrk_dg.datagrid('load',{
		bmbh: jxc_bgrk_did,
		createTime: $('input[name=createTimeBgrk]').val(),
        search: $('input[name=searchBgrk]').val()
	});
}

//////////////////////////////////////////////以上为保管入库列表处理代码

</script>

<!-- tabPosition:'left', headerWidth:'35' -->
<div id="jxc_bgrk_tabs" class="easyui-tabs" data-options="fit:true, border:false," style="width:100%;height:100%;">
	
    <div title="新增记录" data-options="closable:false">
        <div id='jxc_bgrk_layout' style="height:100%; width:100%">
			<div data-options="region:'north',title:'单据信息',border:false,collapsible:false" style="width:100%;height:150px">		
				<table class="tinfo">
                    <tr>
                        <th>关联号</th>
                        <td colspan="7">
                            <input name="bgrk_lsh" style="width:20%;">
                            <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="loadKfrk()">查询</a>
                        </td>
                    </tr>
					<tr>
                        <th></th>
						<td colspan="4"><span class="form_label"><input type="radio" name="bmbh" id='bgrk_bmbh_cb' checked="checked" value="1">出版教材</span><span class="form_label"><input type="radio" name="bmbh" id="bgrk_bmbh_zy" value="0">文达纸业</span></td>
						<th class="read">时间</th><td><div id="createDate" class="read"></div></td>
						<%--<th class="read">单据号</th><td><div id="bgrkLsh" class="read"></div></td>--%>
					</tr>
					<tr>
						<th>供应商编码</th><td><input name="gysbh" class="easyui-validatebox"
							data-options="validType:['mustLength[8]','integer']" onkeyup="jxc.gysLoad('gysbh', 'gysmc')" size="8"></td>
						<th class="read">供应商名称</th><td><input name="gysmc" readonly="readonly" size="50"></td>
					</tr>
					<tr>
						<th>备注</th><td colspan="7"><input name="jxc_bgrk_bz" style="width:90%"></td>
					</tr>
				</table>
			</div>
			<div data-options="region:'center',title:'商品信息',split:true" style="width:150px">		
				<table id='jxc_bgrk_spdg'></table>
			</div>
			<div data-options="region:'east',title:'商品库存信息',split:true,collapsed:true," style="width:150px">
				<table id="show_spkc"></table>
			</div>
		</div>
    </div>
    <div title="保管入库列表" data-options="closable:false" >
    	<table id='jxc_bgrk_dg'></table>
    </div>
</div>

<div id="jxc_bgrk_tb" style="padding:3px;height:auto">
	请输入查询起始日期:<input type="text" name="createTimeBgrk" class="easyui-datebox" data-options="value: moment().date(1).format('YYYY-MM-DD')" style="width:100px">
	输入流水号、供应商名称、备注、关联流水号：<input type="text" name="searchBgrk" style="width:100px">
	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchBgrk();">查询</a>
</div>
