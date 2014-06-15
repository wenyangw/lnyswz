<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>


<script type="text/javascript">
var ywsh_did;
var ywsh_lx;
var ywsh_menuId;

var ywsh_toDg;
var ywsh_dg;
var ywsh_tabs;


$(function(){
	ywsh_did = lnyw.tab_options().did;
	ywsh_lx = lnyw.tab_options().lx;
	ywsh_menuId = lnyw.tab_options().id;
	
	$('#jxc_ywsh_layout').layout({
		fit : true,
		border : false,
	});
	
	
	var cardView = $.extend({}, $.fn.datagrid.defaults.view, {
	    renderRow: function(target, fields, frozen, rowIndex, rowData){
	        var cc = [];
	        cc.push('<td colspan=' + fields.length + ' style="padding:10px 5px;border:0;">');
	        if (!frozen){
	            cc.push('<div style="float:left;margin-left:20px;">');
	            for(var i=0; i<fields.length; i++){
	                var copts = $(target).datagrid('getColumnOption', fields[i]);
	                cc.push('<p><span class="c-label">' + copts.title + ':</span> ' + rowData[fields[i]] + '</p>');
	            }
	            cc.push('</div>');
	        }
	        cc.push('</td>');
	        return cc.join('');
	    }
	});
	
	
	ywsh_toDg = $('#jxc_ywsh_toDg').datagrid({
		pagination : true,
		pagePosition : 'bottom',
		pageSize : 1,
		pageList : [1, 2],
		columns:[[
			{field:'bmmc',title:'部门',align:'center'},
			{field:'lsh',title:'流水号',align:'center'},
	    ]],
	    view: cardView,
	});
	lnyw.toolbar(1, ywsh_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', ywsh_did);
	
	ywsh_dg = $('#jxc_ywsh_dg').datagrid({
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
			{field:'ywshlsh',title:'流水号',align:'center'},
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
        	{field:'cjYwshlsh',title:'原业务入库流水号',align:'center'},
	    ]],
	    toolbar:'#jxc_ywsh_tb',
	});
	lnyw.toolbar(1, ywsh_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', ywsh_did);
	
	
	ywsh_dg.datagrid({
        view: detailview,
        detailFormatter:function(index,row){
            return '<div style="padding:2px"><table id="ywsh-ddv-' + index + '"></table></div>';
        },
        onExpandRow: function(index,row){
            $('#ywsh-ddv-'+index).datagrid({
                url:'${pageContext.request.contextPath}/jxc/ywshAction!detDatagrid.action',
                fitColumns:true,
                singleSelect:true,
                rownumbers:true,
                loadMsg:'',
                height:'auto',
                queryParams: {
        			ywshlsh: row.ywshlsh,
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
                	ywsh_dg.datagrid('fixDetailRowHeight',index);
                },
                onLoadSuccess:function(){
                    setTimeout(function(){
                    	ywsh_dg.datagrid('fixDetailRowHeight',index);
                    },0);
                }
            });
            ywsh_dg.datagrid('fixDetailRowHeight',index);
        }
    });
	
	
	
	//选中列表标签后，装载数据
	ywsh_tabs = $('#jxc_ywsh_tabs').tabs({
		onSelect: function(title, index){
			if(index == 0){
				ywsh_toDg.datagrid({
					url: '${pageContext.request.contextPath}/jxc/ywshAction!listAudits.action',
					queryParams: {
						bmbh: ywsh_did,
						},
				});
			}
			if(index == 1){
				ywsh_dg.datagrid({
					url: '${pageContext.request.contextPath}/jxc/ywshAction!datagrid.action',
					queryParams:{
						bmbh: ywsh_did,
					}
				});
			}
		},
	});
	
	//初始化创建时间
	$('#createDate').html(moment().format('YYYY年MM月DD日'));
	
	//初始化信息
	//init();
});

//以下为商品列表处理代码
function init(){

	
	//清空全部字段
	$('input').val('');
	//收回商品库存信息
	jxc.hideKc('#jxc_ywsh_layout');
	jxc.spInfo($('#jxc_ywsh_layout'), '');

	jxc_ywsh_ckCombo.combobox('selectedIndex', 0);
	jxc_ywsh_pdlxCombo.combobox('selectedIndex', 0);
	
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
				$('#ywshLsh').html(d.obj);
			}  
		},
	});
	
	//根据权限，动态加载功能按钮
	lnyw.toolbar(0, ywsh_spdg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', did);
	
	//清空合计内容
	ywsh_spdg.datagrid('reloadFooter',[{}]);
}


//////////////////////////////////////////////以下为业务审核处理代码
function audit(){
	
}
//////////////////////////////////////////////以上为业务审核处理代码

//////////////////////////////////////////////以下为业务审核列表处理代码

function cjYwsh(){
	var row = ywsh_dg.datagrid('getSelected');
	if (row != undefined) {
		if(row.isCj != '1'){
			if(row.kfpdlsh == undefined){
				$.messager.prompt('请确认', '是否要冲减选中的业务盘点？请填写备注', function(bz){
					if (bz != undefined){
						$.ajax({
							url : '${pageContext.request.contextPath}/jxc/ywshAction!cjYwsh.action',
							data : {
								ywshlsh : row.ywshlsh,
								bmbh: did,
								lxbh: lx,
								menuId : menuId,
								bz : bz
							},
							method: 'post',
							dataType : 'json',
							success : function(d) {
						 		ywsh_dg.datagrid('load');
								ywsh_dg.datagrid('unselectAll');
								$.messager.show({
									title : '提示',
									msg : d.msg
								});
								$.messager.confirm('请确认', '是否打印业务盘点单？', function(r) {
									if (r) {
										var url = lnyw.bp() + '/jxc/ywshAction!printYwsh.action?ywshlsh=' + d.obj.ywshlsh + '&bmbh=' + did;
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

function searchYwsh(){
	ywsh_dg.datagrid('load',{
		bmbh: ywsh_did,
		createTime: $('input[name=createTimeYwsh]').val(),
	});
}

//////////////////////////////////////////////以上为业务审核列表处理代码


</script>

<!-- tabPosition:'left', headerWidth:'35' -->
<div id="jxc_ywsh_tabs" class="easyui-tabs" data-options="fit:true, border:false," style="width:100%;height:100%;">
    <div title="审核" data-options="closable:false">
        <div id='jxc_ywsh_layout' style="height:100%;width=100%">
			<div data-options="region:'center',title:'商品信息',split:true" style="width:150px">		
				<table id='jxc_ywsh_toDg'></table>
			</div>
		</div>
    </div>
    <div title="业务审核列表" data-options="closable:false" >
    	<table id='jxc_ywsh_dg'></table>
    </div>
</div>
<div id="jxc_ywsh_tb" style="padding:3px;height:auto">
	请输入查询起始日期:<input type="text" name="createTimeYwsh" class="easyui-datebox" data-options="value: moment().date(1).format('YYYY-MM-DD')" style="width:100px">
	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchYwsh();">查询</a>
</div>
