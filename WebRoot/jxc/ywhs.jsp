<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>


<script type="text/javascript">
var ywhs_dg;
var ywhs_spdg;
var did;
var lx;
var menuId;
var editIndex = undefined;

var jxc_ywhs_ckCombo;
var jxc_ywhs_fhCombo;

var spbhEditor;
var spmcEditor;
var spcdEditor;
var spppEditor;
var spbzEditor;
var zsEditor;
var zjldwEditor;
var zslEditor;
var zdjEditor;
var cjldwEditor;
var cslEditor;
var spjeEditor;

$(function(){
	did = lnyw.tab_options().did;
	lx = lnyw.tab_options().lx;
	menuId = lnyw.tab_options().id;
	
	//tabOptions = lnyw.tab_options();
	$('#jxc_ywhs_layout').layout({
		fit : true,
		border : false,
	});
	
	ywhs_dg = $('#jxc_ywhs_dg').datagrid({
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
			{field:'ywhslsh',title:'流水号',align:'center'},
	        {field:'createTime',title:'时间',align:'center'},
	        {field:'ckId',title:'仓库id',align:'center',hidden:true},
	        {field:'ckmc',title:'仓库名称',align:'center'},
	        {field:'fhId',title:'分户id',align:'center',hidden:true},
	        {field:'fhmc',title:'分户名称',align:'center'},
	        {field:'hjje',title:'金额',align:'center',
	        	formatter: function(value){
	        		return lnyw.formatNumberRgx(value);
	        	}},
	        {field:'bz',title:'备注',align:'center',
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
        	{field:'cjYwhslsh',title:'原业务调号流水号',align:'center'},
        	{field:'kfhslsh',title:'库房调号流水号',align:'center'},
	    ]],
	    toolbar:'#jxc_ywhs_tb',
	});
	lnyw.toolbar(1, ywhs_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', did);
	
	
	ywhs_dg.datagrid({
        view: detailview,
        detailFormatter:function(index,row){
            return '<div style="padding:2px"><table id="ywhs-ddv-' + index + '"></table></div>';
        },
        onExpandRow: function(index,row){
            $('#ywhs-ddv-'+index).datagrid({
                url:'${pageContext.request.contextPath}/jxc/ywhsAction!detDatagrid.action',
                fitColumns:true,
                singleSelect:true,
                rownumbers:true,
                loadMsg:'',
                height:'auto',
                queryParams: {
        			ywhslsh: row.ywhslsh,
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
                    {field:'zdwdj',title:'单价1',width:100,align:'center',
                       	styler:function(value, rowData,	rowIndex){
   	    	        		if(rowData.isZj == '1'){
   	    						return 'color:blue;';
   	    	        		}else{
   	    						return 'color:red;';
   	    	        		}
   	    	        	}
                    },
                    {field:'spje',title:'金额',width:100,align:'center',
                       	styler:function(value, rowData,	rowIndex){
   	    	        		if(rowData.isZj == '1'){
   	    						return 'color:blue;';
   	    	        		}else{
   	    						return 'color:red;';
   	    	        		}
   	    	        	},
	   	 	        	formatter: function(value){
	   		        		return lnyw.formatNumberRgx(value);
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
                	ywhs_dg.datagrid('fixDetailRowHeight',index);
                },
                onLoadSuccess:function(){
                    setTimeout(function(){
                    	ywhs_dg.datagrid('fixDetailRowHeight',index);
                    },0);
                }
            });
            ywhs_dg.datagrid('fixDetailRowHeight',index);
        }
    });
	
	//选中列表标签后，装载数据
	ywhs_tabs = $('#jxc_ywhs_tabs').tabs({
		onSelect: function(title, index){
			if(index == 1){
				ywhs_dg.datagrid({
					url: '${pageContext.request.contextPath}/jxc/ywhsAction!datagrid.action',
					queryParams:{
						bmbh: did,
					}
				});
			}
		},
	});
	
	//初始化商品编辑表格
	ywhs_spdg = $('#jxc_ywhs_spdg').datagrid({
		fit : true,
	    border : false,
	    singleSelect : true,
	    fitColumns: true,
	    autoRowHeight: false,
	    showFooter: true,
		columns:[[
	        {field:'spbh',title:'商品编号',width:25,align:'center',editor:'textRead',
	        	styler:function(value, rowData,	rowIndex){
	        		if(rowIndex % 2 == 0){
						return 'color:blue;';
	        		}else{
						return 'color:red;';
	        		}
				}},
	        {field:'spmc',title:'商品名称',width:100,align:'center',editor:'textRead',
	        	styler:function(value, rowData,	rowIndex){
	        		if(rowIndex % 2 == 0){
						return 'color:blue;';
	        		}else{
						return 'color:red;';
	        		}
				}},
	        {field:'spcd',title:'商品产地',width:25,align:'center',editor:'textRead',
	        	styler:function(value, rowData,	rowIndex){
	        		if(rowIndex % 2 == 0){
						return 'color:blue;';
	        		}else{
						return 'color:red;';
	        		}
				}},
	        {field:'sppp',title:'商品品牌',width:25,align:'center',editor:'textRead',
	        	styler:function(value, rowData,	rowIndex){
	        		if(rowIndex % 2 == 0){
						return 'color:blue;';
	        		}else{
						return 'color:red;';
	        		}
				}},
	        {field:'spbz',title:'商品包装',width:25,align:'center',editor:'textRead',
	        	styler:function(value, rowData,	rowIndex){
	        		if(rowIndex % 2 == 0){
						return 'color:blue;';
	        		}else{
						return 'color:red;';
	        		}
				}},
	        {field:'isZs',title:'直送',width:25,align:'center',editor:'text',
				
				styler:function(value, rowData,	rowIndex){
	        		if(rowIndex % 2 == 0){
						return 'color:blue;';
	        		}else{
						return 'color:red;';
	        		}
				}
                },
	        {field:'zjldwmc',title:'单位1',width:25,align:'center',editor:'textRead',
	        	styler:function(value, rowData,	rowIndex){
	        		if(rowIndex % 2 == 0){
						return 'color:blue;';
	        		}else{
						return 'color:red;';
	        		}
				}},
	        {field:'zdwsl',title:'数量1',width:25,align:'center',
	        	styler:function(value, rowData,	rowIndex){
	        		if(rowIndex % 2 == 0){
						return 'color:blue;';
	        		}else{
						return 'color:red;';
	        		}
				},
				editor:{
    	        	type:'numberbox',
    	        	options:{
    	        		precision: LENGTH_SL
    	        }}},
	        {field:'zdwdj',title:'单价1',width:25,align:'center',
	    	   	styler:function(value, rowData,	rowIndex){
	        		if(rowIndex % 2 == 0){
						return 'color:blue;';
	        		}else{
						return 'color:red;';
	        		}
				},
				editor:{
    	        	type:'numberbox',
    	        	options:{
    	        		precision: LENGTH_JE
    	        }}},
   	        {field:'cjldwmc',title:'单位2',width:25,align:'center',editor:'textRead',
   	        	styler:function(value, rowData,	rowIndex){
   	        		if(rowIndex % 2 == 0){
   						return 'color:blue;';
   	        		}else{
   						return 'color:red;';
   	        		}
   				}},
   	        {field:'cdwsl',title:'数量2',width:25,align:'center',
   	        	styler:function(value, rowData,	rowIndex){
   	        		if(rowIndex % 2 == 0){
   						return 'color:blue;';
   	        		}else{
   						return 'color:red;';
   	        		}
   				},
   				editor:{
       	        	type:'numberbox',
       	        	options:{
       	        		precision: LENGTH_SL
       	        }}},
    	     {field:'spje',title:'金额',width:25,align:'center',
   	        	styler:function(value, rowData,	rowIndex){
   	        		if(rowIndex % 2 == 0){
   						return 'color:blue;';
   	        		}else{
   						return 'color:red;';
   	        		}
    			},
    			editor:{
    	        	type:'numberbox',
    	        	options:{
    	        		precision: LENGTH_JE
    	        }}},
   	        {field:'zjldwId',title:'主单位id',width:25,align:'center',editor:'text', hidden:true},
   	        {field:'cjldwId',title:'次单位id',width:25,align:'center',editor:'text', hidden:true},
   	        {field:'zhxs',title:'转换系数',width:25,align:'center',editor:'text', hidden:true},
	    ]],
	   	toolbar: '#jxc_ywhs_spTb',
	   	onClickRow: clickRow,
	});
	
	//$('#jxc_ywhs_tabs a.tabs-inner').css('height','100px');
	//$('#jxc_ywhs_tabs span.tabs-title').css('white-space','normal');
	
	
	//初始化创建时间
	$('#createDate').html(moment().format('YYYY年MM月DD日'));
	
	$('input[name=isFh]').click(function(){
		if($(this).is(':checked')){
			$('.isFh').css('display','table-cell');
			//初始化分户列表
			if(jxc_ywhs_fhCombo == undefined){
				jxc_ywhs_fhCombo = lnyw.initCombo($("#jxc_ywhs_fhId"), 'id', 'fhmc', '${pageContext.request.contextPath}/jxc/fhAction!listFhs.action?depId=' + did);
			}
			jxc_ywhs_fhCombo.combobox('selectedIndex', 0);
		}else{
			$('.isFh').css('display','none');
		}
	});
	
 	//初始化仓库列表
 	jxc_ywhs_ckCombo = lnyw.initCombo($("#jxc_ywhs_ckId"), 'id', 'ckmc', '${pageContext.request.contextPath}/jxc/ckAction!listCk.action?depId=' + did);

	$('input[name=zspbh]').bind('keydown', function(event){
		if(event.which == 27){
    		jxc.spHsQuery($('input[name=zspbh]').val(),
    				did,
    				'${pageContext.request.contextPath}/jxc/spQuery.jsp',
    				'${pageContext.request.contextPath}/jxc/spAction!spDg.action',
    				'setValueByZSpbh',
    				$('input[name=zspsl]')
    				);
    		return false;
    	}
	});
	
	$('input[name=jspbh]').bind('keydown', function(event){
		if(event.which == 27){
    		jxc.spHsQuery($('input[name=jspbh]').val(),
    				did,
    				'${pageContext.request.contextPath}/jxc/spQuery.jsp',
    				'${pageContext.request.contextPath}/jxc/spAction!spDg.action',
    				'setValueByJSpbh',
    				$('input[name=jspsl]')
    				);
    		return false;
    	}
	});
	
	$('input[name=zspsl]').bind('keyup', function(event){
		if($('input[name=zspbh]').val() != ''){
			calJe('1');
		}else{
			$.messager.alert('提示', '增加商品信息未填写！', 'error', function(){
				$('input[name=zspsl]').val('');
				$('input[name=zspbh]').focus();
			});
		}
	});
	
	$('input[name=zspdj]').bind('keyup', function(event){
		if($('input[name=zspbh]').val() != ''){
			calJe('1');
		}else{
			$.messager.alert('提示', '增加商品信息未填写！', 'error', function(){
				$('input[name=zspdj]').val('');
				$('input[name=zspbh]').focus();
			});
		}
	});
	
	$('input[name=zspje]').bind('keyup', function(event){
		if($('input[name=zspbh]').val() != ''){
			calDj('1');
		}else{
			$.messager.alert('提示', '增加商品信息未填写！', 'error', function(){
				$('input[name=zspje]').val('');
				$('input[name=zspbh]').focus();
			});
		}
	});

	$('input[name=jspsl]').bind('keyup', function(event){
		if($('input[name=jspbh]').val() != ''){
			calJe('0');
		}else{
			$.messager.alert('提示', '减少商品信息未填写！', 'error', function(){
				$('input[name=jspsl]').val('');
				$('input[name=jspbh]').focus();
			});
		}
	});
	
	$('input[name=jspdj]').bind('keyup', function(event){
		if($('input[name=jspbh]').val() != ''){
			calJe('0');
		}else{
			$.messager.alert('提示', '减少商品信息未填写！', 'error', function(){
				$('input[name=jspdj]').val('');
				$('input[name=jspbh]').focus();
			});
		}
	});
	
	$('input[name=jspje]').bind('keyup', function(event){
		if($('input[name=jspbh]').val() != ''){
			calDj('0');
		}else{
			$.messager.alert('提示', '减少商品信息未填写！', 'error', function(){
				$('input[name=jspje]').val('');
				$('input[name=jspbh]').focus();
			});
		}
	});
	
	$('input').focusin(function(){
		if(editIndex != undefined){
			ywhs_spdg.datagrid('endEdit', editIndex);
		}
	});
 	
	//根据权限，动态加载功能按钮
	lnyw.toolbar(0, ywhs_spdg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', did);
	
 	//初始化信息
	init();
});

//以下为商品列表处理代码
function init(){
	
	ywhs_spdg.datagrid({data:[]});
	
	//清空全部字段
	$('input').val('');
	//$('input:checkbox').removeAttr('checked');
	$('input:checkbox').prop('checked', false);
	$('.isFh').css('display','none');
	$('.isMoZ').css('display','none');
	$('.isMoJ').css('display','none');
	
	//收回商品库存信息
	jxc.hideKc('#jxc_ywhs_layout');
	
 	jxc_ywhs_ckCombo.combobox('selectedIndex', 0);
	
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
				$('#ywhsLsh').html(d.obj);
			}  
		},
	});
	
	
	
	//清空合计内容
	ywhs_spdg.datagrid('reloadFooter',[{}]);
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

function setValueByZSpbh(rowData){
	$('input[name=zspbh]').val(rowData.spbh);
	$('input[name=zspmc]').val(rowData.spmc);
	$('input[name=zspcd]').val(rowData.spcd);
	$('input[name=zsppp]').val(rowData.sppp);
	$('input[name=zspbz]').val(rowData.spbz);
	$('input[name=zspzdw]').val(rowData.zjldwmc);
	$('input[name=zspzdwId]').val(rowData.zjldwId);
	if(rowData.spbh.substring(0, 3) >= "513" && rowData.spbh.substring(0, 3) <= "518"){
		$('.isMoZ').css('display','table-cell');
		$('input[name=zspcdw]').val(rowData.cjldwmc);
		$('input[name=zspcdwId]').val(rowData.cjldwId);
		$('input[name=zzhxs]').val(rowData.zhxs);
	}else{
		$('.isMoZ').css('display','none');
	}
	$('input[name=zspdj]').val(getDwcb(rowData.spbh));
}

function setValueByJSpbh(rowData){
	$('input[name=jspbh]').val(rowData.spbh);
	$('input[name=jspmc]').val(rowData.spmc);
	$('input[name=jspcd]').val(rowData.spcd);
	$('input[name=jsppp]').val(rowData.sppp);
	$('input[name=jspbz]').val(rowData.spbz);
	$('input[name=jspzdw]').val(rowData.zjldwmc);
	$('input[name=jspzdwId]').val(rowData.zjldwId);
	if(rowData.spbh.substring(0, 3) >= "513" && rowData.spbh.substring(0, 3) <= "518"){
		$('.isMoJ').css('display','table-cell');
		$('input[name=jspcdw]').val(rowData.cjldwmc);
		$('input[name=jspcdwId]').val(rowData.cjldwId);
		$('input[name=jzhxs]').val(rowData.zhxs);
	}else{
		$('.isMoJ').css('display','none');
	}
	$('input[name=jspdj]').val(getDwcb(rowData.spbh));
}

function calJe(type){
	if(type == '1'){
		$('input[name=zspje]').val(($('input[name=zspsl]').val() * $('input[name=zspdj]').val()).toFixed(LENGTH_JE));
	}else{
		$('input[name=jspje]').val(($('input[name=jspsl]').val() * $('input[name=jspdj]').val()).toFixed(LENGTH_JE));
	}
}

function calDj(type){
	if(type == '1'){
		if($('input[name=zspsl]').val() != 0){
			$('input[name=zspdj]').val(($('input[name=zspje]').val() / $('input[name=zspsl]').val()).toFixed(LENGTH_JE));
		}
	}else{
		if($('input[name=jspsl]').val() != 0){
			$('input[name=jspdj]').val(($('input[name=jspje]').val() / $('input[name=jspsl]').val()).toFixed(LENGTH_JE));
		}
		
	}
}

//求和
function updateFooter(){
 	var rows = ywhs_spdg.datagrid('getRows');
	var spmc_footer = '合计';
	var hjje = 0.000000;
	$.each(rows, function(){
		var index = ywhs_spdg.datagrid('getRowIndex', this);
		if(index < rows.length){
			if(index % 2 == 0){
				if(editIndex == index){
					hjje += Number(spjeEditor.target.val());
				}else{
					hjje += Number(this.spje);
				}
			}else{
				if(editIndex == index){
					hjje -= Number(spjeEditor.target.val());
				}else{
					hjje -= Number(this.spje);
				}
			}
		}
 	});
	ywhs_spdg.datagrid('reloadFooter', [{spmc : spmc_footer, spje : lnyw.formatNumberRgx(hjje.toFixed(LENGTH_JE))}]);
}

function addRow(){
	if($('input[name=zspbh]').val() != '' && 
			$('input[name=jspbh]').val() != '' && 
			$('input[name=zspje]').val() != 0 && 
			$('input[name=jspje]').val() != 0){
		//calJe('1');
		ywhs_spdg.datagrid('appendRow', {
			spbh:$('input[name=zspbh]').val(),
			spmc:$('input[name=zspmc]').val(),
			spcd:$('input[name=zspcd]').val(),
			sppp:$('input[name=zsppp]').val(),
			spbz:$('input[name=zspbz]').val(),
			isZs:$('input[name=zIsZs]').is(':checked') ? '1' : '0',
			zjldwId:$('input[name=zspzdwId]').val(),
			zjldwmc:$('input[name=zspzdw]').val(),
			zdwsl:$('input[name=zspsl]').val(),
			zdwdj:$('input[name=zspdj]').val(),
			cjldwId:$('input[name=zspcdwId]').val(),
			cjldwmc:$('input[name=zspcdw]').val(),
			cdwsl:$('input[name=zspcsl]').val(),
			spje:$('input[name=zspje]').val(),
			zhxs:$('input[name=zzhxs]').val(), 
		});
		ywhs_spdg.datagrid('appendRow', {
			spbh:$('input[name=jspbh]').val(),
			spmc:$('input[name=jspmc]').val(),
			spcd:$('input[name=jspcd]').val(),
			sppp:$('input[name=jsppp]').val(),
			spbz:$('input[name=jspbz]').val(),
			isZs:$('input[name=jIsZs]').is(':checked') ? '1' : '0',
			zjldwId:$('input[name=jspzdwId]').val(),
			zjldwmc:$('input[name=jspzdw]').val(),
			zdwsl:$('input[name=jspsl]').val(),
			zdwdj:$('input[name=jspdj]').val(),
			cjldwId:$('input[name=jspcdwId]').val(),
			cjldwmc:$('input[name=jspcdw]').val(),
			cdwsl:$('input[name=jspcsl]').val(),
			spje:$('input[name=jspje]').val(),
			zhxs:$('input[name=jzhxs]').val(),
		});
		$('input.inputText').val('');
		updateFooter();
	}else{
		$.messager.alert('提示', '信息填写不完整！', 'error', function(){
			$('input[name=zspbh]').focus();
		});
	}
}

function removeRow(){
	var row = ywhs_spdg.datagrid('getSelected');
	if (row != undefined) {
		$.messager.confirm('请确认', '您是否要删除选中的条目？', function(r) {
			if (r) {
				var index = ywhs_spdg.datagrid('getRowIndex', row);
				if(index % 2 == 0){
					ywhs_spdg.datagrid('deleteRow', index + 1);
					ywhs_spdg.datagrid('deleteRow', index);
				}else{
					ywhs_spdg.datagrid('deleteRow', index);
					ywhs_spdg.datagrid('deleteRow', index - 1);
				}
			}
			updateFooter();
		});
	}else{
		$.messager.alert('警告', '请选择最少一条记录进行操作！',  'warning');
	}
}

function editRow(){
	var row = ywhs_spdg.datagrid('getSelected');
	if (row != undefined) {
		$.messager.confirm('请确认', '您是否要编辑选中的条目？', function(r) {
			if (r) {
				var zRow = undefined;
				var jRow = undefined;
				
				var index = ywhs_spdg.datagrid('getRowIndex', row);
				if(index % 2 == 0){
					zRow = row;
					jRow = ywhs_spdg.datagrid('selectRow', index + 1).datagrid('getSelected');
				}else{
					zRow = ywhs_spdg.datagrid('selectRow', index - 1).datagrid('getSelected');
					jRow = row;
				}
				
				$('input[name=zspbh]').val(zRow.spbh);
				$('input[name=zspmc]').val(zRow.spmc);
				$('input[name=zspcd]').val(zRow.spcd);
				$('input[name=zsppp]').val(zRow.sppp);
				$('input[name=zspbz]').val(zRow.spbz);
				$('input[name=zspdw]').val(zRow.zjldwmc);
				$('input[name=zspsl]').val(zRow.zdwsl);
				$('input[name=zspdj]').val(zRow.zdwdj);
				$('input[name=zspje]').val(zRow.spje);
				if(zRow.spbh.substring(0, 3) >= '513' && zRow.spbh.substring(0, 3) <= '518'){
					$('input[name=zspcdw]').val(zRow.cjldwmc);
					$('input[name=zspcsl]').val(zRow.cdwsl);
				}

				$('input[name=jspbh]').val(jRow.spbh);
				$('input[name=jspmc]').val(jRow.spmc);
				$('input[name=jspcd]').val(jRow.spcd);
				$('input[name=jsppp]').val(jRow.sppp);
				$('input[name=jspbz]').val(jRow.spbz);
				$('input[name=jspdw]').val(jRow.zjldwmc);
				$('input[name=jspsl]').val(jRow.zdwsl);
				$('input[name=jspdj]').val(jRow.zdwdj);
				$('input[name=jspje]').val(jRow.spje);
				if(jRow.spbh.substring(0, 3) >= '513' && jRow.spbh.substring(0, 3) <= '518'){
					$('input[name=jspcdw]').val(jRow.cjldwmc);
					$('input[name=jspcsl]').val(jRow.cdwsl);
				}
			}
		});
	}else{
		$.messager.alert('警告', '请选择最少一条记录进行操作！',  'warning');
	}
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
				ywhs_spdg.datagrid('endEdit', editIndex);
				//将选中行转为编辑状态
			}
			enterEdit(rowIndex, true);
		}
	}else{ //无编辑行
		enterEdit(rowIndex, true);
	}
}

//判断行是否编辑完成
function rowOk(){
	if(editIndex == undefined){
		return true;
	}
	if(zslEditor.target.val() > 0 && spjeEditor.target.val() > 0){
		return true;
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
	if(rowIndex == ywhs_spdg.datagrid('getRows').length - 1){
		ywhs_spdg.datagrid('appendRow', {});
	}else{
		if(!isClick){
			return false;
		}
	}
	//将选中行标识为编辑行
	editIndex = rowIndex;
	//选择编辑行，并进行入编辑状态
	ywhs_spdg.datagrid('selectRow', editIndex)
			.datagrid('beginEdit', editIndex);
	setEditing();
}



//保存行
function accept(){
    if (rowOk()){
    	ywhs_spdg.datagrid('acceptChanges');
    	jxc.hideKc('#jxc_ywhs_layout');
    }
}

//取消编辑行
function cancelAll(){
	ywhs_spdg.datagrid('rejectChanges');
    editIndex = undefined;
    updateFooter();
    jxc.hideKc('#jxc_ywhs_layout');
}

//提交数据到后台
function saveAll(){
	
// 	var msg = formValid();
// 	if(msg == ''){
// 		//编辑行是否完成
// 		if(rowOk()){
// 			ywhs_spdg.datagrid('endEdit', editIndex);
// 		}else{ //编辑行未完成
// 			if(keyOk()){
// 				$.messager.alert('提示', '商品数据编辑未完成,请继续操作！', 'error');
// 				return false;
// 			}else{
// 				removeRow();
// 			}
// 		}
// 	}else{
// 		$.messager.alert('提示', msg + '填写不完整,请继续操作！', 'error');
// 		return false;
// 	}
	
	var rows = ywhs_spdg.datagrid('getRows');
	if(rows.length == 1){
		$.messager.alert('提示', '未添加商品数据,请继续操作！', 'error');
		return false;
	}
	var footerRows = ywhs_spdg.datagrid('getFooterRows');
	var effectRow = new Object();
	//将表头内容传入后台
	effectRow['ckId'] = jxc_ywhs_ckCombo.combobox('getValue');
	effectRow['ckmc'] = jxc_ywhs_ckCombo.combobox('getText');
	if($('input[name=isFh]').is(':checked')){
		effectRow['fhId'] = jxc_ywhs_fhCombo.combobox('getValue');
		effectRow['fhmc'] = jxc_ywhs_fhCombo.combobox('getText');
	}
	effectRow['hjje'] = lnyw.delcommafy(footerRows[0]['spje']); 
	effectRow['bz'] = $('input[name=jxc_ywhs_bz]').val();
	
	effectRow['bmbh'] = did;
	effectRow['lxbh'] = lx;
	effectRow['menuId'] = menuId;
	
	//将表格中的数据去掉最后一个空行后，转换为json格式
	effectRow['datagrid'] = JSON.stringify(rows);
	//提交到action
	$.ajax({
		type: "POST",
		url: '${pageContext.request.contextPath}/jxc/ywhsAction!save.action',
		data: effectRow,
		dataType: 'json',
		success: function(rsp){
			if(rsp.success){
		    	$.messager.show({
					title : '提示',
					msg : '提交成功！'
				});
		    	init();
		    	$.messager.confirm('请确认', '是否打印业务调号单？', function(r) {
					if (r) {
						var url = lnyw.bp() + '/jxc/ywhsAction!printYwhs.action?ywhslsh=' + rsp.obj.ywhslsh + '&bmbh=' + did;
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
	//初始化编辑行
	var spRow = undefined;
	
	//加载字段
	var editors = ywhs_spdg.datagrid('getEditors', editIndex);
    spbhEditor = editors[0];
    spmcEditor = editors[1];
    spcdEditor = editors[2];
    spppEditor = editors[3];
    spbzEditor = editors[4];
    zjldwEditor = editors[5];
    zslEditor = editors[6];
    zdjEditor = editors[7];
    spjeEditor = editors[8];
	
	if($(spbhEditor.target).val() != ''){
		jxc.showKc('#jxc_ywhs_layout', 
				'${pageContext.request.contextPath}/jxc/ywhsAction!getSpkc.action', 
				{
					bmbh : did, 
					//ckId : jxc_xskp_ckCombo.combobox('getValue'),
//	 				fhId : fhValue,
					spbh : $(spbhEditor.target).val(),
				});
    }else{
    	jxc.hideKc('#jxc_ywhs_layout');
   	}
	 
	//loadEditor();
	//处理编辑行的换行事件
	$('.datagrid-row-editing').on('keydown','input', function(event){
		//监听按下向下箭头
		if(event.keyCode == 40){
			var rowIndex = editIndex;
			if(rowOk()){
				ywhs_spdg.datagrid('endEdit', editIndex);
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
    	calculate();
    }).bind('keydown', function(event){
     	if(event.keyCode == 9){
     		zdjEditor.target.focus();
     		return false;
     	}
    });
    
    zdjEditor.target.bind('keyup', function(event){
    	calculate();
    }).bind('keydown', function(event){
     	if(event.keyCode == 9){
     		cslEditor.target.focus();
     		return false;
     	}
    });
  	
  	//输入金额计算单价
    spjeEditor.target.bind('keyup', function(event){
     	$(zdjEditor.target).numberbox('setValue', $(spjeEditor.target).val() / (1 + SL) / $(zslEditor.target).val());
    	updateFooter();
    }).bind('keydown', function(event){
    	if(event.keyCode == 40){
     		spjeEditor.target.focus();
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
    zslEditor.target.focus();
}


//验证记录是否已存在
function existKey(value, rowIndex){
	var keys = ywhs_spdg.datagrid('getRows');
	var exist = false;
	if(keys.length > 2){
		$.each(keys, function(){
			var index = ywhs_spdg.datagrid('getRowIndex', this);
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

//根据获得的行数据对各字段赋值
function setValueBySpbh(rowDate){
	spbhEditor.target.val(rowDate.spbh);
	spmcEditor.target.val(rowDate.spmc);
	spcdEditor.target.val(rowDate.spcd);
	spppEditor.target.val(rowDate.sppp);
	spbzEditor.target.val(rowDate.spbz);
	zjldwEditor.target.val(rowDate.zjldwmc);
	cjldwEditor.target.val(rowDate.cjldwmc);
	zhxsEditor.target.val(rowDate.zhxs);
	zdwIdEditor.target.val(rowDate.zjldwId);
	cdwIdEditor.target.val(rowDate.cjldwId);
	
	
	if($(spbhEditor.target).val() != ''){
		jxc.showKc('#jxc_ywhs_layout', 
				'${pageContext.request.contextPath}/jxc/ywhsAction!getSpkc.action', 
				{
					bmbh : did, 
					spbh : $(spbhEditor.target).val(),
				});
    }else{
    	jxc.hideKc('#jxc_ywhs_layout');
   	}
	
}




//////////////////////////////////////////////以上为商品列表处理代码

//////////////////////////////////////////////以下为业务调号列表处理代码

function cjYwhs(){
	var row = ywhs_dg.datagrid('getSelected');
	if (row != undefined) {
		if(row.kfhslsh == undefined){
			if(row.isCj != '1'){
				$.messager.prompt('请确认', '是否要冲减选中的业务调号单？请填写备注', function(bz){
					//if (bz){
						$.ajax({
							url : '${pageContext.request.contextPath}/jxc/ywhsAction!cjYwhs.action',
							data : {
								ywhslsh : row.ywhslsh,
								bmbh: did,
								lxbh: lx,
								menuId : menuId,
								bz : bz
							},
							method: 'post',
							dataType : 'json',
							success : function(d) {
								ywhs_dg.datagrid('load');
								ywhs_dg.datagrid('unselectAll');
								$.messager.show({
									title : '提示',
									msg : d.msg
								});
								$.messager.confirm('请确认', '是否打印业务调号单？', function(r) {
									if (r) {
										var url = lnyw.bp() + '/jxc/ywhsAction!printYwhs.action?ywhslsh=' + d.obj.ywhslsh  + '&bmbh=' + did;
										jxc.print(url, PREVIEW_REPORT, HIDE_PRINT_WINDOW);
									}
								});
							}
						});
					//}
				});
			}else{
				$.messager.alert('警告', '选中的业务调号单已被冲减，请重新选择！',  'warning');
			}
		}else{
			$.messager.alert('警告', '选中的业务调号单已被库房入库，请重新选择！',  'warning');
		}
	}else{
		$.messager.alert('警告', '请选择一条记录进行操作！',  'warning');
	}
}

function printYwhs(){
	var row = ywhs_dg.datagrid('getSelected');
	if (row != undefined) {
		$.messager.confirm('请确认', '是否打印业务调号单？', function(r) {
			if (r) {
				var url = lnyw.bp() + '/jxc/ywhsAction!printYwhs.action?ywhslsh=' + row.ywhslsh + '&bmbh=' + did;
				jxc.print(url, PREVIEW_REPORT, HIDE_PRINT_WINDOW);
			}
		});
	}else{
		$.messager.alert('警告', '请选择一条记录进行操作！',  'warning');
	}
}

function searchYwhs(){
	ywhs_dg.datagrid('load',{
		bmbh: did,
		createTime: $('input[name=createTimeYwhs]').val(),
	});
}
// function cancelYwhs(){
// 	console.info('From ywhs.jsp');
// 	var row = ywhs_dg.datagrid('getSelected');
// 	if (row != undefined) {
// 		if(row.isCancel != '1'){
// 			if(row.isCompleted != '1'){
// 				if(row.cgjhlsh == undefined){
// 					$.messager.confirm('请确认', '您要取消选中的采购需求单？', function(r) {
// 						if (r) {
// 							$.ajax({
// 								url : '${pageContext.request.contextPath}/jxc/ywhsAction!cancel.action',
// 								data : {
// 									ywhslsh : row.ywhslsh
// 								},
// 								dataType : 'json',
// 								success : function(d) {
// 									ywhs_dg.datagrid('load');
// 									ywhs_dg.datagrid('unselectAll');
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

// function completeYwhs(){
// 	var row = ywhs_dg.datagrid('getSelected');
// 	if (row != undefined) {
// 		if(row.cgjhlsh != undefined){
// 			if(row.isCancel != '1'){
// 				if(row.isCompleted != '1'){
// 					$.messager.confirm('请确认', '您要完成选中的采购需求单？', function(r) {
// 						if (r) {
// 							$.ajax({
// 								url : '${pageContext.request.contextPath}/jxc/ywhsAction!complete.action',
// 								data : {
// 									ywhslsh : row.ywhslsh
// 								},
// 								dataType : 'json',
// 								success : function(d) {
// 									ywhs_dg.datagrid('load');
// 									ywhs_dg.datagrid('unselectAll');
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


//////////////////////////////////////////////以上为采购需求列表处理代码

</script>

<!-- tabPosition:'left', headerWidth:'35' -->
<div id="jxc_ywhs_tabs" class="easyui-tabs" data-options="fit:true, border:false," style="width:100%;height:100%;">
    <div title="新增记录" data-options="closable:false">
        <div id='jxc_ywhs_layout' style="height:100%;width=100%">
			<div data-options="region:'north',title:'单据信息',border:false,collapsible:false" style="width:100%;height:240px">		
				<table class="tinfo">
					<tr>
						<th>仓库</th><td><input id="jxc_ywhs_ckId" name="ckId" type="text" size="8"></td>
				  		<th>分户<input id="fhCheck" type="checkbox" name="isFh"></th>
				  		<td colspan="5" class="isFh"><input id="jxc_ywhs_fhId" name="fhId" size="8" style="display:none"></td>
				  		<th class="read">时间</th><td><div id="createDate" class="read"></div></td>
				  		<th class="read">单据号</th><td><div id="ywhsLsh" class="read"></div></td>
					</tr>
					<tr>
						<td colspan="6">
							<table  bgcolor="#CCCCCC" width="500px">
								<tr>
									<td colspan="2"><strong>增加商品:</strong></td>
									<td colspan="2">
										<strong>直送</strong><input class="inputText" type="checkbox" name="zIsZs">
									</td>
								<tr>
								<tr>
									<th >商品编号</th>
									<td ><input class="inputText" type="text" name="zspbh" size="4"></td>
									<th >名称</th>
									<td colspan="3" ><input class="inputText" type="text" name="zspmc" size="40"></td>
								</tr>
								<tr>
									<th >产地</th><td ><input class="inputText" type="text" name="zspcd" size="4"></td>
									<th >品牌</th><td ><input class="inputText" type="text" name="zsppp" size="20"></td>
									<th >包装</th><td ><input class="inputText" type="text" name="zspbz" size="8"></td>
								</tr>
								<tr>
									<th>单位1</th>
									<td >
										<input class="inputText" type="text" name="zspzdwId" hidden="true">
										<input class="inputText" type="text" name="zspzdw" size="4">
									</td>
									<th>数量1</th>
									<td><input class="inputText" type="text" name="zspsl" size="8"></td>
									<th>单价1</th>
									<td ><input class="inputText" type="text" name="zspdj" size="8"></td>
								</tr>
								<tr>
									<th><div class = "isMoZ">单位2</div></th>
									<td>
										<div class = "isMoZ">
										<input class="inputText" type="text" name="zspcdwId" hidden="true">
										<input class="inputText" type="text" name="zspcdw" size="4">
										<input class="inputText" type="text" name="zzhxs" hidden="true">
										</div>
									</td>
									<th><div class = "isMoZ">数量2</div></th>
									<td><div class = "isMoZ"><input class="inputText" type="text" name="zspcsl" size="8"></div></td>
									<th>金额</th>
									<td><input class="inputText" type="text" name="zspje" size="8"></td>
								</tr>
							</table>
						</td>
						<td colspan="6">
							<table  bgcolor="#ECEBE1" width="500px">
								<tr>
									<td colspan="2">
										<strong>减少商品:</strong>
									</td>
									<td colspan="2">
										<strong>直送</strong><input class="inputText" type="checkbox" name="jIsZs">
									</td>
								<tr>
								<tr>
									<th>商品编号</th><td><input class="inputText" type="text" name="jspbh" size="4"></td>
									<th>名称</th><td colspan="3"><input class="inputText" type="text" name="jspmc" size="40"></td>
								</tr>
								<tr>
									<th>产地</th><td><input class="inputText" type="text" name="jspcd" size="4"></td>
									<th>品牌</th><td><input class="inputText" type="text" name="jsppp" size="20"></td>
									<th>包装</th><td><input class="inputText" type="text" name="jspbz" size="10"></td>
								</tr>
								<tr>
									<th>单位1</th><td>
									<input class="inputText" type="text" name="jspzdwId" hidden="true">
									<input class="inputText" type="text" name="jspzdw" size="4"></td>
									<th>数量1</th><td><input class="inputText" type="text" name="jspsl" size="8"></td>
									<th>单价1</th><td><input class="inputText" type="text" name="jspdj" size="8"></td>
								</tr>
								<tr>
									<th><div class = "isMoJ">单位2</div></th>
									<td>
										<div class = "isMoJ">
										<input class="inputText" type="text" name="jspcdwId" hidden="true">
										<input class="inputText" type="text" name="jspcdw" size="4">
										<input class="inputText" type="text" name="jzhxs" hidden="true">
										</div>
									</td>
									<th><div class = "isMoJ">数量2</div></th>
									<td><div class = "isMoJ"><input class="inputText" type="text" name="jspcsl" size="8"></div></td>
									<th>金额</th><td><input class="inputText" type="text" name="jspje" size="8"></td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td colspan="12" bgcolor="#E4FAFA"><strong>备注</strong><input type="text" name="jxc_ywhs_bz" size="100"></td>
					</tr>
			  </table>
			</div>
			<div data-options="region:'center',title:'商品信息',split:true" style="width:150px">		
				<table id='jxc_ywhs_spdg'></table>
			</div>
			<div data-options="region:'east',title:'商品库存信息',split:true,collapsed:true" style="width:150px">
				<table id="show_spkc"></table>
			</div>
		</div>
    </div>
    <div title="业务调号列表" data-options="closable:false" >
    	<table id='jxc_ywhs_dg'></table>
    </div>
</div>

<div id="jxc_ywhs_spTb" style="padding:3px;height:auto">
	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="addRow();">增加</a>
	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="removeRow();">删除</a>
</div>
<div id="jxc_ywhs_tb" style="padding:3px;height:auto">
	请输入查询起始日期:<input type="text" name="createTimeYwhs" class="easyui-datebox" data-options="value: moment().date(1).format('YYYY-MM-DD')" style="width:100px">
	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchYwhs();">查询</a>
</div>



