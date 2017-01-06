<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>


<script type="text/javascript">
var jhsh_did;
var jhsh_lx;
var jhsh_menuId;

var jhsh_toDg;
var jhsh_dg;
var jhsh_tabs;

var cgjhlsh;

$(function(){
	jhsh_did = lnyw.tab_options().did;
	jhsh_lx = lnyw.tab_options().lx;
	jhsh_menuId = lnyw.tab_options().id;
	
	$('#jxc_jhsh_layout').layout({
		fit : true,
		border : false,
	});
	
	
	var cardView = $.extend({}, $.fn.datagrid.defaults.view, {
	    renderRow: function(target, fields, frozen, rowIndex, rowData){
	        var cc = [];
	        cc.push('<td colspan=' + fields.length + ' style="width:1400px; padding:10px 5px; border:0;">');
	        if (!frozen){
	            cc.push('<table border= "0" width = 95%>');
	            var j = 0;
	            for(var i=0; i<fields.length; i++){
	                var copts = $(target).datagrid('getColumnOption', fields[i]);
	                //赋值，供显示单据明细调用
	                if(fields[i] == 'lsh'){
	                	cgjhlsh = rowData.lsh;
	                }
	                if( j == 0){
	                	cc.push('<tr><th class="read">单据信息:</th><td colspan="3"></td></tr>');
	                }
// 	                if( fields[i] == 'gysmc'){
// 	                	cc.push('<tr><td colspan="4">&nbsp;</td></tr>');
// 	                	cc.push('<tr><th class="read">供应商信息:</th><td colspan="3"></td></tr>');
// 	                }
	                if( fields[i] == 'timeLatest'){
	                	cc.push('<tr><td colspan="4">&nbsp;</td></tr>');
	                	cc.push('<tr><th class="read">最早一笔未付款提货:</th><td colspan="3"></td></tr>');
	                }
	                if( fields[i] == 'levels'){
	                	cc.push('<tr><td colspan="4">&nbsp;</td></tr>');
	                	cc.push('<tr><th class="read">审批进度</th><th class="read" align="center">审批人</th><th class="read" colspan="2">审批时间</th></tr>');
	                	
	                	var levels = rowData[fields[i]].split(',');
	                	var names = rowData[fields[i + 1]].split(',');
	                	var times = rowData[fields[i + 2]].split(',');
	                	for(var m = 0; m < levels.length; m++){
	                		cc.push('<tr style="color:blue;">');
	                		cc.push('<td align="right">' + levels[m] + '</td>');
	                		cc.push('<td align="right">' + names[m] + '</td>');
	                		cc.push('<td colspan="2" align="right">' + (times[m] == '1900-01-01 00:00:00.0' ? '' : times[m]) + '</td>');
	                		cc.push('</tr>');
	                	}
	                	break;
	                }
	                
	                if(j % 2 == 0){
	                	cc.push('<tr>');
	                }
	                if(!copts.hidden){
		                if(fields[i] == 'bz'){
		                	if(j % 2 == 1){
		                		cc.push('</tr><tr>');
		                	}
		                	cc.push('<th width=20%>' + copts.title + ':</th><td colspan="3">' + rowData[fields[i]] + '</td>');
		                	if(j % 2 == 1){
		                		j++;
		                	}
		                }else{
			                cc.push('<th width=20%>' + copts.title + ':</th><td width=30%>' + rowData[fields[i]] + '</td>');
		                }
	                }else{
	                	j--;
	                }
	                if(j % 2 == 1 || (fields.length - 1  == i && j % 2 == 0)){
	                	cc.push('</tr>');
	                }
	                j++;
	            }
	            cc.push('</table>');
// 	            cc.push('</div>');
	        }
	        cc.push('<br><div id="cardDg"></div>');
	        
	        cc.push('</td>');
	        return cc.join('');
	    }
	});
	
	
	jhsh_toDg = $('#jxc_jhsh_toDg').datagrid({
		url: '${pageContext.request.contextPath}/jxc/ywshAction!listCgjhAudits.action',
		queryParams: {
			bmbh: jhsh_did,
		},
		fit: true,
		showHeader: false,
		pagination : true,
		pagePosition : 'bottom',
		pageSize : 1,
		pageList : [1, 2],
		columns:[[
			{field:'bmmc',title:'部门',align:'center'},
			{field:'bmbh',title:'部门编号',align:'center', hidden:true},
			{field:'auditName',title:'业务名称',align:'center'},
			{field:'lsh',title:'流水号',align:'center'},
			{field:'createTime',title:'创建时间',align:'center'},
			{field:'createName',title:'创建人',align:'center'},
			{field:'gysbh',title:'供应商编号',align:'center', hidden:true},
			{field:'gysmc',title:'供应商名称',align:'center'},
			{field:'jsfsmc',title:'结算方式',align:'center'},
			{field:'hjje',title:'采购金额(元)',align:'center'},
			{field:'bz',title:'备注',align:'center'},
			{field:'levels',title:'进度',align:'center'},
			{field:'names',title:'审批人',align:'center'},
			{field:'times',title:'审批时间',align:'center'},
	    ]],
	    view: cardView,
	    onLoadSuccess:function(){
	    	$('#cardDg').datagrid({
	    		url:'${pageContext.request.contextPath}/jxc/cgjhAction!detDatagrid.action',
	    		queryParams: {
	        		cgjhlsh: cgjhlsh,
	        		fromOther: 'fromJhsh'
	        	},
	    		fit : true,
	    	    border : false,
	    	    singleSelect : true,
	    	    remoteSort: false,
//	     	    fitColumns: true,
	    		columns:[[
					{field:'spbh',title:'商品编号',width:60,align:'center'},
					{field:'spmc',title:'名称',width:200,align:'center'},
					{field:'spcd',title:'产地',width:50,align:'center'},
					{field:'sppp',title:'品牌',width:60,align:'center'},
					{field:'spbz',title:'包装',width:60,align:'center'},
					{field:'zjldwmc',title:'单位1',width:50,align:'center'},
					{field:'zdwsl',title:'数量1',width:90,align:'center'},
					{field:'zdwdj',title:'单价1',width:90,align:'center'},
					{field:'cjldwmc',title:'单位2',width:50,align:'center'},
					{field:'cdwsl',title:'数量2',width:90,align:'center'},
					{field:'cdwdj',title:'单价2',width:90,align:'center'},
					{field:'spje',title:'金额',width:90,align:'center',
						formatter: function(value){
							return lnyw.formatNumberRgx(value);
						}},
					{field:'kcsl',title:'当前库存数量',width:90,align:'center'},
					{field:'zzl',title:'周转率',width:90,align:'center'},
					{field:'xqs',title:'需求',width:200,align:'center'},
	    	    ]],
	    	});
	    },
	});
	lnyw.toolbar(0, jhsh_toDg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', jhsh_did);
	
	jhsh_dg = $('#jxc_jhsh_dg').datagrid({
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
			{field:'lsh',title:'流水号',align:'center'},
	        {field:'createTime',title:'时间',align:'center',width:100},
	        {field:'createName',title:'审批人',align:'center',width:100},
	        {field:'auditLevel',title:'等级',align:'center',width:100},
	        {field:'isAudit',title:'结果',align:'center',width:100,
	        	formatter: function(value){
        			if(value == '0'){
        				return '拒绝';
        			}else{
        				return '通过';
        			}
        		},
	        	styler: function(value){
					if(value == '0'){
						return 'color:red;';
					}
				}},
	        {field:'bz',title:'备注',align:'center',width:100,
        		formatter: function(value){
        			return lnyw.memo(value, 15);
        		}},
	    ]],
	    toolbar:'#jxc_jhsh_tb',
	});
	lnyw.toolbar(1, jhsh_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', jhsh_did);
	
	
	jhsh_dg.datagrid({
        view: detailview,
        detailFormatter:function(index,row){
            return '<div style="padding:2px"><table id="jhsh-ddv-' + index + '"></table></div>';
        },
        onExpandRow: function(index,row){
            $('#jhsh-ddv-'+index).datagrid({
                url:'${pageContext.request.contextPath}/jxc/cgjhAction!detDatagrid.action',
                fitColumns:true,
                singleSelect:true,
                rownumbers:true,
                loadMsg:'',
                height:'auto',
                queryParams: {
        			cgjhlsh: row.lsh,
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
                	jhsh_dg.datagrid('fixDetailRowHeight',index);
                },
                onLoadSuccess:function(){
                    setTimeout(function(){
                    	jhsh_dg.datagrid('fixDetailRowHeight',index);
                    },0);
                }
            });
            jhsh_dg.datagrid('fixDetailRowHeight',index);
        }
    });
	
	
	
	//选中列表标签后，装载数据
	jhsh_tabs = $('#jxc_jhsh_tabs').tabs({
		onSelect: function(title, index){
			if(index == 0){
 				jhsh_toDg.datagrid('reload');
// 				jhsh_toDg.datagrid({
// 					url: '${pageContext.request.contextPath}/jxc/jhshAction!listAudits.action',
// 					queryParams: {
// 						bmbh: jhsh_did,
// 					},
// 				});
			}
			if(index == 1){
				jhsh_dg.datagrid({
					url: '${pageContext.request.contextPath}/jxc/ywshAction!jhshDatagrid.action',
					queryParams:{
						bmbh: jhsh_did,
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
				$('#jhshLsh').html(d.obj);
			}  
		},
	});
	
	//根据权限，动态加载功能按钮
	//lnyw.toolbar(0, jhsh_spdg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', did);
	
	//清空合计内容
	//jhsh_spdg.datagrid('reloadFooter',[{}]);
}


//////////////////////////////////////////////以下为业务审核处理代码
function jhsh_audit(){
	var selected = jhsh_toDg.datagrid('getRows');
	if(selected.length > 0){
		$.ajax({
			type: "POST",
			async: false,
			url: '${pageContext.request.contextPath}/jxc/ywshAction!refreshJhsh.action',
			data: {
				bmbh: jhsh_did,
				lsh: selected[0].lsh,
			},
			dataType: 'json',
			success: function(d){
				if(d.obj != undefined){
					var row = d.obj;
					$.messager.prompt('请确认', '是否将该笔采购计划审核通过？', function(bz){
						if (bz != undefined){
							$.ajax({
								type: "POST",
								url: '${pageContext.request.contextPath}/jxc/ywshAction!jhshAudit.action',
								data: {
									lsh: row.lsh,
									auditLevel: row.auditLevel,
									bmbh: jhsh_did,
									menuId: jhsh_menuId,
									bz: bz,
								},
								dataType: 'json',
								success: function(d){
									if(d.success){
										jhsh_toDg.datagrid('reload');
										$.messager.show({
											title : '提示',
											msg : d.msg
										});
									}  
								},
							});
							
						}
					});
				}else{
					$.messager.alert('警告', '该单据已审批结束！',  'warning');
					jhsh_toDg.datagrid('reload');
				}
			}
		});
	}else{
		$.messager.alert('警告', '没有需要进行审批的业务！',  'warning');
	}
}

function jhsh_refuse(){
	var selected = jhsh_toDg.datagrid('getRows');
	if(selected.length > 0){
		$.ajax({
			type: "POST",
			async: false,
			url: '${pageContext.request.contextPath}/jxc/ywshAction!refreshJhsh.action',
			data: {
				bmbh: jhsh_did,
				lsh: selected[0].lsh,
			},
			dataType: 'json',
			success: function(d){
				if(d.obj != undefined){
					var row = d.obj;
					$.messager.prompt('请确认', '<font color="red">是否拒绝将该笔业务审核通过？</font>', function(bz){
						if (bz != undefined){
							$.ajax({
								type: "POST",
								url: '${pageContext.request.contextPath}/jxc/ywshAction!jhshRefuse.action',
								data: {
									lsh: row.lsh,
									auditLevel: row.auditLevel,
									bmbh: jhsh_did,
									menuId: jhsh_menuId,
									bz: bz,
								},
								dataType: 'json',
								success: function(d){
									if(d.success){
										jhsh_toDg.datagrid('reload');
										$.messager.show({
											title : '提示',
											msg : d.msg
										});
									}  
								},
							});
							
						}
					});
				}else{
					$.messager.alert('警告', '该单据已审批结束！',  'warning');
					jhsh_toDg.datagrid('reload');
				}
			}
		});
	}else{
		$.messager.alert('警告', '没有需要进行审批的业务！',  'warning');
	}
	
}
//////////////////////////////////////////////以上为业务审核处理代码


function searchjhsh(){
	jhsh_dg.datagrid('load',{
		bmbh: jhsh_did,
		createTime: $('input[name=createTimejhsh]').val(),
	});
}

//////////////////////////////////////////////以上为业务审核列表处理代码


</script>

<!-- tabPosition:'left', headerWidth:'35' -->
<div id="jxc_jhsh_tabs" class="easyui-tabs" data-options="fit:true, border:false," style="width:100%;height:100%;">
    <div title="审核" data-options="closable:false">
<!--         <div id='jxc_jhsh_layout' style="height:100%;width=100%;"> -->
<!-- 			<div data-options="region:'center',title:'商品信息',split:true" >		 -->
				<table id='jxc_jhsh_toDg'></table>
<!-- 			</div> -->
<!-- 		</div> -->
    </div>
    <div title="计划审核列表" data-options="closable:false" >
    	<table id='jxc_jhsh_dg'></table>
    </div>
</div>
<div id="jxc_jhsh_tb" style="padding:3px;height:auto">
	请输入查询起始日期:<input type="text" name="createTimejhsh" class="easyui-datebox" data-options="value: moment().date(1).format('YYYY-MM-DD')" style="width:100px">
	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchjhsh();">查询</a>
</div>
