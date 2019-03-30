<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<script type="text/javascript">
    var ywrkmx_did;
    var ywrkmx_bmbh;
    var ywrkmx_dg;

    $(function(){
        ywrkmx_did = lnyw.tab_options().did;


        if(ywrkmx_did >= '10'){
            $('.bm').css('display','inline');
            $('#jxc_ywrkmx_dep').combobox({
                data: ywbms,
                width: 100,
                valueField: 'id',
                textField: 'depName',
                panelHeight: 'auto',
                onSelect: function(rec){
                    ywrkmx_bmbh = $(this).combobox('getValue');
                }
            }).combobox('selectedIndex', 0);
            ywrkmx_bmbh = $('#jxc_ywrkmx_dep').combobox('getValue');
        }else{
            ywrkmx_bmbh = ywrkmx_did;
        }

        ywrkmx_dg = $('#ywrkmx_dg').datagrid({
            //url: '${pageContext.request.contextPath}/jxc/ywrkAction!ywrkmx.action',
//            queryParams: {x
//                bmbh:  '05'
//            },
            fit : true,
            border : false,
            singleSelect : true,
            remoteSort: false,
            //fitColumns: true,
            pagination: true,
            pagePosition : 'bottom',
            pageSize : pageSize,
            pageList : pageList,
            columns:[[
                {field:'spbh',title:'商品编号',align:'center', sortable:true,
                    sorter: function(a, b){
                        return a.localeCompare(b);
                    }},
                {field:'spmc',title:'名称',align:'center'},
                {field:'spcd',title:'产地',align:'center',sortable:true,
                    sorter: function(a, b){
                        if(typeof(a) == "string"){
                            return a.localeCompare(b);
                        }
                    }},
                {field:'sppp',title:'品牌',align:'center'},
                {field:'zjldwmc',title:'单位',align:'center',
                    formatter: function(value, row){
                    	if (row.spbh.substr(0, 1) === '4' && row.cjldwId === '07') {
                    	    return row.cjldwmc;
						}else {
                    	    return value;
						}
                    }},
                {field:'zdwsl',title:'数量',align:'right',width: 80,
                    formatter: function(value, row){
                        if (row.spbh.substr(0, 1) === '4' && row.cjldwId === '07') {
                            return row.cdwsl;
                        }else {
                            return value;
                        }
                    }},
                {field:'zdwdj',title:'单价',align:'right',halign:'center',width: 80,
                    formatter: function(value, row){
                        if (row.spbh.substr(0, 1) === '4' && row.cjldwId === '07') {
                            return row.cdwdj;
                        }else {
                            return (value * (1 + SL)).toFixed(2);
                        }
                    }},
                {field:'spje',title:'金额',align:'right',width: 80,
                    formatter: function(value){
                        return (value * (1 + SL)).toFixed(2);
                    }},
                {field:'createTime',title:'入库时间',align:'center',
                    formatter: function(value){
                        return moment(value).format('YYYY-MM-DD');
                    }},
                {field:'gysmc',title:'供应商名称',align:'center'},
//                {field:'bz',title:'备注',align:'center'},
                {field:'ywrklsh',title:'流水号',align:'center'}
            ]],
            toolbar:'#ywrkmx_tb',
        });
    });

    function ywrkmx_getData(){
        ywrkmx_dg.datagrid('load',{
            url: '${pageContext.request.contextPath}/jxc/ywrkAction!ywrkmx.action',
            bmbh: ywrkmx_bmbh
//	 		createTime: $('input[name=createTimeCgxqInCgjh]').val(),
//	 		fromOther: 'fromCgjh'
 		});
    }
</script>

<div id='ywrkmc_layout' style="height:100%;width:100%">
	<div data-options="region:'center',title:'商品信息',split:true" style="width:100%;height:90%;">
		<table id='ywrkmx_dg'></table>
	</div>
	<div data-options="region:'south',title:'说明',split:true" style="width:100%;height:10%;">
		<div style="margin:10px;">注：<br>
			1.表中数据已将纸张的单位统一转换为吨。<br>
			2.单价与金额均为包括税额。
		</div>
	</div>
</div>
<div id="ywrkmx_tb" style="padding:3px;height:auto">
	<span class="bm" style="display:none">部门：<input id="jxc_ywrkmx_dep" name="jxc_ywrkmx_dep"></span>
	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="ywrkmx_getData();">刷新</a>
</div>

