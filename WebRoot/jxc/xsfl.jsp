<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>


<script type="text/javascript">
	var xsfl_did;
	var xsfl_lx;
	var xsfl_menuId;
	var xsfl_dg;
	var xsfl_tool;
	var xsfl_tabs;

	var countXsfl = 0;

	var jxc_xsfl_ywyCombo;

	$(function() {
		xsfl_did = lnyw.tab_options().did;
		xsfl_lx = lnyw.tab_options().lx;
		xsfl_menuId = lnyw.tab_options().id;

		$('#jxc_xsfl_layout').layout({
			fit : true,
			border : false,
		});

		xsfl_dg = $('#jxc_xsfl_dg').datagrid({
			fit : true,
			border : false,
			singleSelect : false,
			remoteSort : false,
			fitColumns : false,
			pagination : true,
			pagePosition : 'bottom',
			pageSize : pageSize,
			pageList : pageList,
			columns : [ [ {
				field : 'xskplsh',
				title : '流水号',
				align : 'center'
			}, {
				field : 'createTime',
				title : '*时间',
				align : 'center',
				sortable : true,
				sorter : function(a, b) {
					return moment(a).diff(moment(b), 'days');
				}
			}, {
				field : 'khbh',
				title : '客户编号',
				align : 'center',
				hidden : true
			}, {
				field : 'khmc',
				title : '客户名称',
				align : 'center'
			}, {
				field : 'ywymc',
				title : '业务员',
				align : 'center'
			}, {
				field : 'hjje',
				title : '金额',
				align : 'center',
				formatter : function(value) {
					return lnyw.formatNumberRgx(value);
				}
			}, {
				field : 'hjse',
				title : '税额',
				align : 'center',
				formatter : function(value) {
					return lnyw.formatNumberRgx(value);
				}
			}, {
				field : 'bz',
				title : '备注',
				align : 'center',
				formatter : function(value) {
					return lnyw.memo(value, 15);
				}
			}, {
				field : 'isCj',
				title : '*状态',
				align : 'center',
				sortable : true,
				formatter : function(value) {
					if (value == '1') {
						return '已冲减';
					} else {
						return '正常';
					}
				},
				sorter : function(a, b) {
					a = a == undefined ? 0 : a;
					b = b == undefined ? 0 : b;
					return (a - b);
				}
			}, {
				field : 'cjTime',
				title : '冲减时间',
				align : 'center'
			}, {
				field : 'cjXskplsh',
				title : '原流水号',
				align : 'center'
			}, {
				field : 'jsfsmc',
				title : '结算方式',
				align : 'center'
			}, {
				field : 'fplxmc',
				title : '发票类型',
				align : 'center'
			}, ] ],
			toolbar : '#jxc_xsfl_tb',
		});
		lnyw.toolbar(1, xsfl_dg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', xsfl_did);

		xsfl_tool = $('#jxc_xsfl_tool').datagrid({
			fit: true,
			border: false,
			singleSelect: false
		});
		//根据权限，动态加载功能按钮
		<%--lnyw.toolbar(0, xsfl_tool, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', xsfl_did);--%>


		//选中列表标签后，装载数据
		xsfl_tabs = $('#jxc_xsfl_tabs').tabs({
            onSelect : function(title, index) {
                if (index == 1) {
                    xsfl_dg.datagrid({
                        url : '${pageContext.request.contextPath}/jxc/xskpAction!datagridXsfl.action',
                        queryParams : {
                            bmbh : xsfl_did,
                            createTime : countXsfl == 0 ? undefined : $('input[name=createTimeXsfl]').val(),
                            xslxId : '02',
                            search : countXsfl == 0 ? undefined : $('input[name=searchXsfl]').val(),
                        }
                    });
                    countXsfl++;
                }
            },
        });

		//初始化创建时间
		$('#createDate').html(moment().format('YYYY年MM月DD日'));

		$('input[name=jxc_xsfl_khmc]').change(
			function() {
				loadKh($('input[name=jxc_xsfl_khbh]').val().trim());
			}
		);

		//初始化业务员列表
		jxc_xsfl_ywyCombo = lnyw.initCombo($("#jxc_xsfl_ywyId"), 'id', 'realName', '${pageContext.request.contextPath}/admin/userAction!listYwys.action?did=' + xsfl_did);

		//初始化信息
		init();
	});

	//以下为商品列表处理代码
	function init() {

		//清空全部字段
		$('#info input').val('');

		//jxc_xsfl_ywyCombo.combobox('selectedIndex', 0);

		jxc.spInfo($('#jxc_xsfl_layout'), '');

		//初始化流水号
		$.ajax({
			type : "POST",
			url : '${pageContext.request.contextPath}/jxc/lshAction!getLsh.action',
			data : {
				bmbh : xsfl_did,
				lxbh : xsfl_lx,
			},
			dataType : 'json',
			success : function(d) {
				if (d.success) {
					$('#jxc_xsfl_xskpLsh').html(d.obj);
				}
			},
		});

		//根据权限，动态加载功能按钮
		lnyw.toolbar(0, xsfl_tool, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', xsfl_did);
	}

	//提交数据到后台
	function saveXsfl() {
		var effectRow = new Object();
		effectRow['fplxId'] = '1';
		effectRow['fplxmc'] = '增值税';
		effectRow['isSx'] = '0';
		effectRow['isZs'] = '0';
		effectRow['isTh'] = '0';
		effectRow['isHk'] = '0';
		effectRow['fromTh'] = '0';
		effectRow['xslxId'] = '02';
		effectRow['xslxmc'] = '返利';
		effectRow['thfs'] = '1';
		effectRow['khbh'] = $('input[name=jxc_xsfl_khbh]').val();
		effectRow['khmc'] = $('input[name=jxc_xsfl_khmc]').val();
		effectRow['sh'] = $('input[name=xsfl_sh]').val();
		effectRow['khh'] = $('input[name=xsfl_khh]').val();
		effectRow['dzdh'] = $('input[name=xsfl_dzdh]').val();
		effectRow['ckId'] = jxc.getFlCk(xsfl_did);
		effectRow['ckmc'] = jxc.getFlCk(xsfl_did, true);
		effectRow['ywyId'] = jxc_xsfl_ywyCombo.combobox('getValue');
		effectRow['ywymc'] = jxc_xsfl_ywyCombo.combobox('getText');
		effectRow['jsfsId'] = "01";
		effectRow['jsfsmc'] = "现款";
		effectRow['bz'] = $('input[name=jxc_xsfl_bz]').val();
		var hjje = ($('input[name=jxc_xsfl_je]').val() / (1 + SL)).toFixed(2);
		var hjse = ($('input[name=jxc_xsfl_je]').val() - hjje).toFixed(2);
		effectRow['hjje'] = -hjje;
		effectRow['hjse'] = -hjse;
		effectRow['bmbh'] = xsfl_did;
		effectRow['lxbh'] = xsfl_lx;
		effectRow['menuId'] = xsfl_menuId;

		$.ajax({
			type : "POST",
			url : '${pageContext.request.contextPath}/jxc/xskpAction!saveXsfl.action',
			data : effectRow,
			dataType : 'json',
			async : false,
			success : function(rsp) {
				if (rsp.success) {
					$.messager.show({
						title : '提示',
						msg : '提交成功！',
					});
					init();
				}
			},
			error : function() {
				$.messager.alert("提示", "提交错误了！");
			}
		});

	}

	function checkKh() {
		$.ajax({
			url : '${pageContext.request.contextPath}/jxc/khAction!checkKh.action',
			async : false,
			data : {
				khbh : $('input[name=jxc_xsfl_khbh]').val(),
				depId : xsfl_did,
			},
			dataType : 'json',
			success : function(data) {
				if (!data.success) {
					$.messager.alert('提示', data.msg, 'error');
					$('input[name=jxc_xsfl_khbh]').val('');
					$('input[name=jxc_xsfl_khmc]').val('');
					$('input[name=xsfl_sh]').val('');
					$('input[name=xsfl_khh]').val('');
					$('input[name=xsfl_dzdh]').val('');
					$('input[name=jxc_xsfl_khbh]').focus();
					return false;
				}
			}
		});
		return true;
	}

	function loadKh(khbh) {
		$.ajax({
			url : '${pageContext.request.contextPath}/jxc/khAction!loadKh.action',
			async : false,
			cache : false,
			context : this,
			data : {
				khbh : khbh,
				depId : xsfl_did,
			},
			dataType : 'json',
			success : function(data) {
				if (data.success) {
					//设置信息字段值
					$('input[name=jxc_xsfl_khmc]').val(data.obj.khmc);
					$('input[name=xsfl_sh]').val(data.obj.sh);
					$('input[name=xsfl_khh]').val(data.obj.khh);
					$('input[name=xsfl_dzdh]').val(data.obj.dzdh);
					jxc_xsfl_ywyCombo.combobox('setValue',
							data.obj.ywyId);
					// 				if(data.obj.isSx == '1'){
					// 					$('input[name=isSx]').prop('checked', 'ckecked');
					// 				}
					if (data.obj.isNsr == '1') {
						$('input#isNsr').attr('checked', 'checked');
						$('input#isNsr').prop('checked', 'checked');
					} else {
						$('input#isNoNsr').attr('checked', 'checked');
						$('input#isNoNsr').prop('checked', 'checked');
					}
				} else {
					$.messager.alert('提示', '客户信息不存在！', 'error');
				}
			}
		});
	}

	function khLoad() {
		switch (event.keyCode) {
		case 27:
			jxc.query('客户检索', $('input[name=jxc_xsfl_khbh]'),
                $('input[name=jxc_xsfl_khmc]'), '',
                '${pageContext.request.contextPath}/jxc/query.jsp',
                '${pageContext.request.contextPath}/jxc/khAction!khDg.action');
			break;
		case 9:
			break;
		default:
			if ($('input[name=jxc_xsfl_khbh]').val().trim().length == 0) {
				$('input[name=jxc_xsfl_khmc]').val('');
				$('input[name=xsfl_sh]').val('');
				$('input[name=xsfl_khh]').val('');
				$('input[name=xsfl_dzdh]').val('');
			}
			if ($('input[name=jxc_xsfl_khbh]').val().trim().length == 8) {
				loadKh($('input[name=jxc_xsfl_khbh]').val().trim());
			}
			break;
		}
	}
	//////////////////////////////////////////////以上为商品列表处理代码

	//////////////////////////////////////////////以下为销售返利列表处理代码
	function cjXsfl() {
		var row = xsfl_dg.datagrid('getSelected');
		if (row != undefined) {
			if (row.isCj != '1') {
                $.messager.prompt('请确认', '是否要冲减选中的销售返利单？请填写备注',
                    function(bz) {
                        if (bz != undefined) {
                            //MaskUtil.mask('正在冲减，请等待……');
                            $.ajax({
                                url : '${pageContext.request.contextPath}/jxc/xskpAction!cjXsfl.action',
                                data : {
                                    xskplsh : row.xskplsh,
                                    bmbh : xsfl_did,
                                    lxbh : xsfl_lx,
                                    bz : bz,
                                },
                                method : 'post',
                                dataType : 'json',
                                success : function(d) {
                                    xsfl_dg.datagrid('load');
                                    xsfl_dg.datagrid('unselectAll');
                                    $.messager.show({
                                        title : '提示',
                                        msg : d.msg
                                    });
                                },
                                complete : function() {
                                    //MaskUtil.unmask();
                                }
                            });
                        }
                    });

			} else {
				$.messager.alert('警告', '选中的销售记录已被冲减，请重新选择！', 'warning');
			}
		} else {
			$.messager.alert('警告', '请选择一条记录进行操作！', 'warning');
		}
	}

	function searchXsfl() {
		xsfl_dg.datagrid('load', {
			bmbh : xsfl_did,
			createTime : $('input[name=createTimeXsfl]').val(),
			search : $('input[name=searchXsfl]').val(),
		});
	}

	//////////////////////////////////////////////以上为销售返利列表处理代码
</script>

<div id="jxc_xsfl_tabs" class="easyui-tabs"
	data-options="fit:true, border:false,"
	style="width: 100%; height: 100%;">

	<div title="新增记录" data-options="closable:false">
		<div id='jxc_xsfl_layout' style="height:100%;width:100%;">
			<div class="tinfo" id="info" data-options="region:'north',title:'单据信息',border:false"	style="width: 100%; height: 200px">
				<table>
					<tr>
						<td colspan="2" style="display: none;">增值税发票<input type="radio"
							name="jxc_xsfl_fplxId" value="1" id="isNsr" checked="checked">
							&nbsp;&nbsp; 普通发票<input type="radio" name="jxc_xsfl_fplxId"
							value="0" id="isNoNsr"></td>
						<th class="read">时间</th>
						<td><div id="createDate" class="read"></div></td>
						<th class="read">单据号</th>
						<td><div id="jxc_xsfl_xskpLsh" class="read"></div></td>
					</tr>
					<tr>
						<th>客户编码</th>
						<td><input name="jxc_xsfl_khbh" class="easyui-validatebox"
							data-options="validType:['mustLength[8]','integer']"
							onkeyup="khLoad()"></td>
						<th class="read">客户名称</th>
						<td colspan="3" class="read"><input name="jxc_xsfl_khmc"
							readonly="readonly" style="width: 100%"></td>
					</tr>
					<tr>
						<th class="read">税号</th>
						<td class="read"><input name="xsfl_sh" readonly="readonly"></td>
						<th class="read">开户行账号</th>
						<td class="read"><input name="xsfl_khh" readonly="readonly"></td>
						<th class="read">地址电话</th>
						<td class="read"><input name="xsfl_dzdh" readonly="readonly"></td>
					</tr>
					<tr>
						<th>业务员</th>
						<td><input id="jxc_xsfl_ywyId" name="ywyId" type="text"></td>
					</tr>
					<tr>
						<th>备注</th>
						<td colspan="7"><input name="jxc_xsfl_bz" style="width: 90%"></td>
					</tr>
					<tr>
						<th>返利金额</th>
						<td colspan="7"><input name="jxc_xsfl_je" style="width: 100px;">元</td>
					</tr>
				</table>
			</div>
			<div data-options="region:'center',title:'商品信息',split:true, border: false, noheader: true" style="height:10%;width:100%">
				<div id='jxc_xsfl_tool'></div>
			</div>
		</div>
	</div>
	<div title="返利列表" data-options="closable:false">
		<div id='jxc_xsfl_dg'></div>
	</div>
</div>

<div id="jxc_xsfl_tb" style="padding: 3px; height: auto">
	请输入查询起始日期:<input type="text" name="createTimeXsfl"
		class="easyui-datebox"
		data-options="value: moment().date(1).format('YYYY-MM-DD')"
		style="width: 100px"> 输入流水号、客户编号、名称、业务员、备注：<input type="text"
		name="searchXsfl" style="width: 100px"> <a href="#"
		class="easyui-linkbutton"
		data-options="iconCls:'icon-search',plain:true"
		onclick="searchXsfl();">查询</a>
</div>
