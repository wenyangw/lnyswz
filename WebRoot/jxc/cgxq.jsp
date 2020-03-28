<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>


<script type="text/javascript">
var cgxq_spdg;
var editIndex = undefined;
var did;
var lx;
var menuId;

var jxc_cgxq_jsfsCombo;

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
var cslEditor;
var cdjEditor;
var zxsdjEditor;
var cxsdjEditor;
var spjeEditor;
var zhxsEditor;
var zdwIdEditor;
var cdwIdEditor;

$(function(){
	did = lnyw.tab_options().did;
	lx = lnyw.tab_options().lx;
	menuId = lnyw.tab_options().id;
	
	//tabOptions = lnyw.tab_options();
	$('#jxc_cgxq_layout').layout({
		fit : true,
		border : false,
	});
	
	//初始化商品编辑表格
	cgxq_spdg = $('#jxc_cgxq_spdg').datagrid({
		fit : true,
	    border : false,
	    singleSelect : true,
	    fitColumns: true,
	    autoRowHeight: false,
	    showFooter: true,
		//初始化后加一空行
		//data:[{}],
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
	        			precision: LENGTH_SL,
	        		}}},
	        {field:'zdwdj',title:'单价1',width:25,align:'center',
	    	        editor:{
	    	        	type:'numberbox',
	    	        	options:{
	    	        		precision: LENGTH_JE
	    	        }}},
	        {field:'zdwxsdj',title:'销价1',width:25,align:'center',
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
	        {field:'cdwxsdj',title:'销价2',width:25,align:'center',
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
            $('#jxc_cgxq_layout').layout('panel', 'center').panel({title : '商品信息'});
        },
         
	});
	
	//$('#jxc_cgxq_tabs a.tabs-inner').css('height','100px');
	//$('#jxc_cgxq_tabs span.tabs-title').css('white-space','normal');
	
	//初始化信息
	init();
	
	//初始化创建时间
	$('#createDate').html(moment().format('YYYY年MM月DD日'));
	
	//初始化业务员列表
// 	var ywyId = $("input[name=ywyId]");
// 	var ywyCombo = ywyId.combobox({
// 	    url:'${pageContext.request.contextPath}/admin/userAction!listYwys.action?did=' + did,
// 	    valueField:'id',
// 	    textField:'realName',
// 	    panelHeight: 'auto',
// 	});
	
	//2015-12-01集团单位订单要统计全部需求，并能区分统一需求还是临时需求，因此客户等信息长期有效
// 	$('input[name=isLs]').click(function(){
// 		if($(this).is(':checked')){
// 			$('.ls').css('display','table-row');
// 			//初始化分户列表
// 			if(jxc_cgxq_jsfsCombo == undefined){
// 				jxc_cgxq_jsfsCombo = lnyw.initCombo($("#jxc_cgxq_jsfsId"), 'id', 'jsmc', '${pageContext.request.contextPath}/jxc/jsfsAction!listJsfs.action');
// 			}else{
// 				//jxc_cgxq_jsfsCombo.combobox('selectedIndex', 0);
// 			}
// 		}else{
// 			$('.ls').css('display','none');
// 		}
// 	});
	
 	//初始化付款方式列表
 	jxc_cgxq_jsfsCombo = lnyw.initCombo($("input[name=jsfsId]"), 'id', 'jsmc', '${pageContext.request.contextPath}/jxc/jsfsAction!listJsfs.action');
});

//以下为商品列表处理代码
function init(){
	//清空列表，添加一个空行
	cgxq_spdg.datagrid({
		data: [{}],
	});
	
	//清空全部字段
	$('input').val('');
	$('#cgxq_xqsj').datebox().datebox('setValue', moment().format('YYYY-MM-DD'));
	
	
	//$('input:checkbox').removeAttr('checked');
	$('input:checkbox').prop('checked', false);
	//$('.ls').css('display','none');
	$('.ls').css('display','block');
	
	//收回商品库存信息
	jxc.hideKc('#jxc_cgxq_layout');
	jxc.spInfo($('#jxc_cgxq_layout'), '');
	
// 	jxc_cgxq_jsfsCombo.combobox('selectedIndex', 0);
	
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
				$('#cgxqLsh').html(d.obj);
			}  
		},
	});
	
	//根据权限，动态加载功能按钮
	lnyw.toolbar(0, cgxq_spdg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', did);
	//清空合计内容
	cgxq_spdg.datagrid('reloadFooter',[{}]);
}

//判断行是否编辑完成
function rowOk(){
	if(editIndex == undefined){
		return true;
	}
	if(keyOk()){
		if(zslEditor.target.val() > 0){
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
	if(rowIndex == cgxq_spdg.datagrid('getRows').length - 1){
		cgxq_spdg.datagrid('appendRow', {});
	}else{
		if(!isClick){
			return false;
		}
	}
	//将选中行标识为编辑行
	editIndex = rowIndex;
	//选择编辑行，并进行入编辑状态
	cgxq_spdg.datagrid('selectRow', editIndex)
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
				cgxq_spdg.datagrid('endEdit', editIndex);
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
    cgxq_spdg.datagrid('cancelEdit', editIndex)
            .datagrid('deleteRow', editIndex);
    editIndex = undefined;
    updateFooter();
    jxc.hideKc('#jxc_cgxq_layout');
}

//保存行
function accept(){
    if (rowOk()){
    	cgxq_spdg.datagrid('acceptChanges');
    	jxc.hideKc('#jxc_cgxq_layout');
    }
}

//取消编辑行
function cancelAll(){
	cgxq_spdg.datagrid('rejectChanges');
    editIndex = undefined;
    updateFooter();
    jxc.hideKc('#jxc_cgxq_layout');
}

//提交数据到后台
function saveAll(){
	
	var msg = formValid();
	if(msg == ''){
		//编辑行是否完成
		if(rowOk()){
			cgxq_spdg.datagrid('endEdit', editIndex);
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
	
	var rows = cgxq_spdg.datagrid('getRows');
	if(rows.length == 1){
		$.messager.alert('提示', '未添加商品数据,请继续操作！', 'error');
		return false;
	}
	
	var footerRows = cgxq_spdg.datagrid('getFooterRows');
	var effectRow = new Object();
	//将表头内容传入后台
	effectRow['gysbh'] = $('input[name=gysbh]').val();
	effectRow['gysmc'] = $('input[name=gysmc]').val();
	effectRow['khbh'] = $('input[name=khbh]').val();
	effectRow['khmc'] = $('input[name=khmc]').val();
	effectRow['jsfsId'] = jxc_cgxq_jsfsCombo.combobox('getValue');
	effectRow['jsfsmc'] = jxc_cgxq_jsfsCombo.combobox('getText');
	effectRow['lxr'] = $('input[name=jxc_cgxq_lxr]').val();
	effectRow['shdz'] = $('input[name=jxc_cgxq_shdz]').val();
	effectRow['dhsj'] = $('input[name=dhsj]').val();
	effectRow['xqsj'] = $('input[name=xqsj]').val();
		
	if($('input[name=isLs]').is(':checked')){
		effectRow['isLs'] =  '1';
		var needA = jxc.getAuditLevelCgxq(did);
		effectRow['needAudit'] = needA;
		if(needA != '0'){
			$.messager.alert('提示', '本次采购需进入' + needA + '级审批流程！', 'warning');
		}
	}else{
		effectRow['isLs'] =  '0';
		effectRow['needAudit'] = '0';
	}
	if($('input[name=cgxq_isZs]').is(':checked')){
		effectRow['isZs'] =  '1';
	}else{
		effectRow['isZs'] =  '0';
	}
	effectRow['hjje'] = lnyw.delcommafy(footerRows[0]['spje']); 
	effectRow['bz'] = $('input[name=jxc_cgxq_bz]').val();
	effectRow['bmbh'] = did;
	effectRow['lxbh'] = lx;
	effectRow['menuId'] = menuId;
	
	//将表格中的数据去掉最后一个空行后，转换为json格式
	effectRow['datagrid'] = JSON.stringify(rows.slice(0, rows.length - 1));
	//提交到action
	//MaskUtil.mask('正在保存，请等待……');
	$.ajax({
		type: "POST",
		url: '${pageContext.request.contextPath}/jxc/cgxqAction!save.action',
		data: effectRow,
		dataType: 'json',
		success: function(rsp){
			if(rsp.success){
		    	$.messager.show({
					title : '提示',
					msg : '提交成功！'
				});
		    	init();
		    	if(needA == undefined || needA == '0'){
			    	$.messager.confirm('请确认', '是否打印采购需求单？', function(r) {
						if (r) {
							var url = lnyw.bp() + '/jxc/cgxqAction!printCgxq.action?cgxqlsh=' + rsp.obj.cgxqlsh + "&bmbh=" + did;
							jxc.print(url, PREVIEW_REPORT, HIDE_PRINT_WINDOW);
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
	var editors = cgxq_spdg.datagrid('getEditors', editIndex);
    spbhEditor = editors[0];
    spmcEditor = editors[1];
    spcdEditor = editors[2];
    spppEditor = editors[3];
    spbzEditor = editors[4];
    zjldwEditor = editors[5];
    zslEditor = editors[6];
    zdjEditor = editors[7];
    zxsdjEditor = editors[8];
    cjldwEditor = editors[9];
    cslEditor = editors[10];
    cdjEditor = editors[11];
    cxsdjEditor = editors[12];
    spjeEditor = editors[13];
    zhxsEditor = editors[14];
    zdwIdEditor = editors[15];
	cdwIdEditor = editors[16];
	
	
	
	if($(spbhEditor.target).val() != ''){
		jxc.spInfo($('#jxc_cgxq_layout'), '1', $(spppEditor.target).val(), $(spbzEditor.target).val());
		jxc.showKc('#jxc_cgxq_layout', 
				'${pageContext.request.contextPath}/jxc/cgxqAction!getSpkc.action', 
				{
					bmbh : did, 
					spbh : $(spbhEditor.target).val(),
				});
    }else{
    	jxc.spInfo($('#jxc_cgxq_layout'), '');
    	jxc.hideKc('#jxc_cgxq_layout');
   	}
	 
	//loadEditor();
	//处理编辑行的换行事件
	$('.datagrid-row-editing').on('keydown','input', function(event){
		//监听按下向下箭头
		if(event.keyCode == 40){
			var rowIndex = editIndex;
			if(rowOk()){
				cgxq_spdg.datagrid('endEdit', editIndex);
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
    						depId : did,
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
    				zslEditor);
    		return false;
    	}
    });
    
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
    		$(cdjEditor.target).numberbox('setValue', $(zdjEditor.target).val() * $(zhxsEditor.target).val() * (1 + SL) );
    	}
    	calculate();
    }).bind('keydown', function(event){
     	if(event.keyCode == 9){
     		cslEditor.target.focus();
     		return false;
     	}
    });
  	
    cslEditor.target.bind('keyup', function(event){
    	if((event.keyCode >= 48 && event.keyCode <= 57) || event.keyCode == 190 || (event.keyCode >= 96 && event.keyCode <= 105) || event.keyCode == 110){
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
        
    //输入次单位单价后，计算金额
    cdjEditor.target.bind('keyup', function(event){
	   	if($(zhxsEditor.target).val() != 0){
	   		$(zdjEditor.target).numberbox('setValue', $(cdjEditor.target).val() / (1 + SL) / $(zhxsEditor.target).val());
	   	}
	   	calculate();
    }).bind('keydown', function(event){
    	if(event.keyCode == 40){
     		spjeEditor.target.focus();
     	}
    });
    
  	//输入主销售单价后，计算金额(含税)
    zxsdjEditor.target.bind('keyup', function(event){
    	if($(zhxsEditor.target).val() != 0){
    		$(cxsdjEditor.target).numberbox('setValue', $(zxsdjEditor.target).val() * $(zhxsEditor.target).val());
    	}
    	calculate();
    }).bind('keydown', function(event){
    	if(event.keyCode == 40){
     		spjeEditor.target.focus();
     	}
    });
    
  	//输入次销售单价后，计算金额(含税)
    cxsdjEditor.target.bind('keyup', function(event){
    	if($(zhxsEditor.target).val() != 0){
    		$(zxsdjEditor.target).numberbox('setValue', $(cxsdjEditor.target).val() / $(zhxsEditor.target).val());
    	}
    	calculate();
    }).bind('keydown', function(event){
    	if(event.keyCode == 40){
     		spjeEditor.target.focus();
     	}
    });
  	
  	//输入商品金额后，计算销售单价
    spjeEditor.target.bind('keyup', function(event){
    	if($(zslEditor.target).val() != 0){
    		$(zxsdjEditor.target).numberbox('setValue', $(spjeEditor.target).val() / $(zslEditor.target).val());
    	}
   	   	if($(zhxsEditor.target).val() != 0 && $(cslEditor.target).val() != 0){
    		$(cxsdjEditor.target).numberbox('setValue', $(spjeEditor.target).val() / $(cslEditor.target).val());
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
    	var spje = 0.0000;
    	if($(cslEditor.target).val() != 0 && $(cxsdjEditor.target).val() != 0){
    		spje = cslEditor.target.val() * cxsdjEditor.target.val();
    	}else{
        	spje = zslEditor.target.val() * zxsdjEditor.target.val();   
    	}
    		
        $(spjeEditor.target).numberbox('setValue',spje);
        //更新汇总列
        updateFooter();
    }
  	
    //初始进入编辑状态时，使用商品编号获得焦点
    spbhEditor.target.focus();
}
//求和
function updateFooter(){
 	var rows = cgxq_spdg.datagrid('getRows');
	var spmc_footer = '合计';
	var hjje = 0.000000;
	$.each(rows, function(){
		var index = cgxq_spdg.datagrid('getRowIndex', this);
		if(index < rows.length - 1){
		if(editIndex == index){
			hjje += Number(spjeEditor.target.val());
		}else{
			hjje += Number(this.spje);
		}
	}
 		
 	});
	cgxq_spdg.datagrid('reloadFooter', [{spmc : spmc_footer, spje : lnyw.formatNumberRgx(hjje.toFixed(LENGTH_JE))}]);
}

//验证记录是否已存在
function existKey(value, rowIndex){
	var keys = cgxq_spdg.datagrid('getRows');
	var exist = false;
	if(keys.length > 2){
		$.each(keys, function(){
			var index = cgxq_spdg.datagrid('getRowIndex', this);
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
// 	if($('input[name=gysmc]').val() == ''){
// 		message += '供应商信息<br>';
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
	zdwIdEditor.target.val(rowData.zjldwId);
	cdwIdEditor.target.val(rowData.cjldwId);
	
	
	
	if($(spbhEditor.target).val() != ''){
		jxc.spInfo($('#jxc_cgxq_layout'), '1', rowData.sppp, rowData.spbz);
		jxc.showKc('#jxc_cgxq_layout', 
				'${pageContext.request.contextPath}/jxc/cgxqAction!getSpkc.action', 
				{
					bmbh : did, 
					spbh : $(spbhEditor.target).val(),
				});
    }else{
		jxc.spInfo($('#jxc_cgxq_layout'), '');
    	jxc.hideKc('#jxc_cgxq_layout');
   	}
	
}

function gysLoad(){
	switch(event.keyCode){
	case 27:
		jxc.query('供应商检索', $('input[name=gysbh]'), $('input[name=gysmc]'), '',
				'${pageContext.request.contextPath}/jxc/query.jsp',
				'${pageContext.request.contextPath}/jxc/gysAction!gysDg.action');
		break;
	case 9:
		break;
	default:
		if($('input[name=gysbh]').val().trim().length == 0){
			$('input[name=gysmc]').val('');
		}
		if($('input[name=gysbh]').val().trim().length == 8){
			$.ajax({
				url:'${pageContext.request.contextPath}/jxc/gysAction!loadGys.action',
				async: false,
				context:this,
				data:{
					gysbh: $('input[name=gysbh]').val().trim(),
				},
				dataType:'json',
				success:function(data){
					if(data.success){
						//设置信息字段值
						$('input[name=gysmc]').val(data.obj.gysmc);
						$('input[name=ywyId]').focus();
					}else{
						$.messager.alert('提示', '供应商信息不存在！', 'error');
					}
				}
			});
		}
		break;
	}
}

function khLoad(){
	switch(event.keyCode){
	case 27:
		jxc.query('客户检索', $('input[name=khbh]'), $('input[name=khmc]'), '',
				'${pageContext.request.contextPath}/jxc/query.jsp',
				'${pageContext.request.contextPath}/jxc/khAction!khDg.action');
		break;
	case 9:
		break;
	default:
		if($('input[name=khbh]').val().trim().length == 8){
			
			$.ajax({
				url:'${pageContext.request.contextPath}/jxc/khAction!loadKh.action',
				async: false,
				context:this,
				data:{
					khbh: $('input[name=khbh]').val().trim(),
				},
				dataType:'json',
				success:function(data){
					if(data.success){
						//设置信息字段值
						$('input[name=khmc]').val(data.obj.khmc);
// 						$('input[name=ywyId]').focus();
						
					}else{
						$.messager.alert('提示', '客户信息不存在！', 'error');
					}
				}
			});
		}
		break;
	}
}

function addressLoad(){
	switch(event.keyCode){
	case 27:
		jxc.query('客户检索', "", $('input[name=jxc_cgxq_shdz]'), '',
				'${pageContext.request.contextPath}/jxc/query.jsp',
				'${pageContext.request.contextPath}/jxc/khAction!khDg.action');
		break;
	case 9:
		break;
	}
}
//////////////////////////////////////////////以上为商品列表处理代码

//////////////////////////////////////////////以下为采购需求列表处理代码
// function cancelCgxq(){
// 	console.info('From cgxq.jsp');
// 	var row = cgxq_dg.datagrid('getSelected');
// 	if (row != undefined) {
// 		if(row.isCancel != '1'){
// 			if(row.isCompleted != '1'){
// 				if(row.cgjhlsh == undefined){
// 					$.messager.confirm('请确认', '您要取消选中的采购需求单？', function(r) {
// 						if (r) {
// 							$.ajax({
// 								url : '${pageContext.request.contextPath}/jxc/cgxqAction!cancel.action',
// 								data : {
// 									cgxqlsh : row.cgxqlsh
// 								},
// 								dataType : 'json',
// 								success : function(d) {
// 									cgxq_dg.datagrid('load');
// 									cgxq_dg.datagrid('unselectAll');
// 									$.messager.show({
// 										title : '提示',
// 										msg : d.msg
// 									});
// 								}
// 							});
// 						}
// 					});
// 				}else{
// 					$.messager.alert('警告', '选中的采购需求已经进入采购计划环节，不能取消！',  'warning');
// 				}
// 			}else{
// 				$.messager.alert('警告', '选中的采购需求记录已完成，不能取消！',  'warning');
// 			}
// 		}else{
// 			$.messager.alert('警告', '选中的采购需求记录已被取消，请重新选择！',  'warning');
// 		}
// 	}else{
// 		$.messager.alert('警告', '请选择最少一条记录进行操作！',  'warning');
// 	}
// }

// function completeCgxq(){
// 	var row = cgxq_dg.datagrid('getSelected');
// 	if (row != undefined) {
// 		if(row.cgjhlsh != undefined){
// 			if(row.isCancel != '1'){
// 				if(row.isCompleted != '1'){
// 					$.messager.confirm('请确认', '您要完成选中的采购需求单？', function(r) {
// 						if (r) {
// 							$.ajax({
// 								url : '${pageContext.request.contextPath}/jxc/cgxqAction!complete.action',
// 								data : {
// 									cgxqlsh : row.cgxqlsh
// 								},
// 								dataType : 'json',
// 								success : function(d) {
// 									cgxq_dg.datagrid('load');
// 									cgxq_dg.datagrid('unselectAll');
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

function printCgxq(){
	var row = cgxq_dg.datagrid('getSelected');
	if (row != undefined) {
		if(row.needAudit == row.isAudit){
			$.messager.confirm('请确认', '是否打印采购需求单？', function(r) {
				if (r) {
					var url = lnyw.bp() + '/jxc/cgxqAction!printCgxq.action?cgxqlsh=' + row.cgxqlsh + "&bmbh=" + did;
					jxc.print(url, PREVIEW_REPORT, HIDE_PRINT_WINDOW);
				}
			});
		}else{
			$.messager.alert('警告', '选中的需求单还未进行审批，请重新选择择一条记录进行操作！',  'warning');
		}
	}else{
		$.messager.alert('警告', '请选择一条记录进行操作！',  'warning');
	}
}



//////////////////////////////////////////////以上为采购需求列表处理代码

</script>

<!-- tabPosition:'left', headerWidth:'35' -->
<div id="jxc_cgxq_tabs" class="easyui-tabs" data-options="fit:true, border:false," style="width:100%;height:100%;">
    <div title="新增记录" data-options="closable:false">
        <div id='jxc_cgxq_layout' style="height:100%;width=100%">
			<div data-options="region:'north',title:'单据信息',border:false,collapsible:false" style="width:100%;height:180px">		
				<div class="form_line">
					<span class="form_label">临时采购<input type="checkbox" name="isLs" value="1" /></span>
					<span class="form_label">直送<input type="checkbox" name="cgxq_isZs"></span>
					<span class="read form_label">时间</span>
					<span id="createDate" class="read"></span>
					<span class="read form_label">单据号</span>
					<span id="cgxqLsh" class="read"></span>
				</div>
				<div class="form_line">
					<span class="form_label">供应商编码</span>
					<span><input name="gysbh" class="easyui-validatebox"
						data-options="validType:['mustLength[8]','integer']" onkeyup="gysLoad()" size="6"></span>
					<span class="read form_label">供应商名称</span>
					<span><input name="gysmc" readonly="readonly" size="20"></span>
<!-- 						<th>业务员</th><td><input name="ywyId"></td> -->
				</div>
				<div class="ls form_line" style="display:none">
					<span class="form_label">客户编码</span>
					<span><input name="khbh" class="easyui-validatebox"	data-options="validType:['mustLength[8]','integer']" onkeyup="khLoad()"  size="6"></span>
					<span class="form_label">客户名称</span>
					<span><input name="khmc" readonly="readonly" size="20"></span>
<!-- 						<th>到货方式</th><td><input name="dhfs"></td> -->
					<span class="form_label">结算方式</span>
					<span><input id="jxc_cgxq_jsfsId" name="jsfsId"></span>
					<span class="form_label">到货时间</span>
					<span><input name="dhsj" type="text" class="easyui-my97"  size="8"></span>
				</div>
				<div class="ls form_line" style="display:none">
					<span class="form_label">联系人及电话</span>
					<span><input name="jxc_cgxq_lxr" size="15"></span>
					<span class="form_label">送货地址</span>
					<span><input name="jxc_cgxq_shdz" onkeyup="addressLoad()" size="20"></span>
					<span class="form_label">需求时间</span>
					<span><input name="xqsj" id="cgxq_xqsj" size="8"></span>
				</div>
				<div class="form_line">
					<span class="form_label">备注</span>
					<span><input name="jxc_cgxq_bz" style="width:90%"></span>
				</div>
			</div>
			<div data-options="region:'center',title:'商品信息',split:true" style="width:150px">		
				<table id='jxc_cgxq_spdg'></table>
			</div>
			<div data-options="region:'east',title:'商品库存信息',split:true,collapsed:true" style="width:150px">
				<table id="show_spkc"></table>
			</div>
		</div>
    </div>
	<%@ include file="cgxqDg.jsp"%>
</div>




