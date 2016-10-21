<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>


<script type="text/javascript">
	var xsfl_did;
	var xsfl_lx;
	var xsfl_menuId;
	var xsfl_spdg;
	var xsfl_dg;
	var editIndex = undefined;
	var xsfl_tabs;

	var countXsfl = 0;

	var jxc_xsfl_ckCombo;
	var jxc_xsfl_ywyCombo;
	var jxc_xsfl_jsfsCombo;

	//编辑行字段
	var spbhEditor;
	var spmcEditor;
	var spcdEditor;
	var spppEditor;
	var spbzEditor;
	var zjldwEditor;
	//var zthslEditor;
	//var zytslEditor;
	//var zslEditor;
	//var zdjEditor;
	//var cthslEditor;
	//var cytslEditor;
	//var cslEditor;
	//var cdjEditor;   
	var spjeEditor;
	var spseEditor;
	var sphjEditor;
	var zhxsEditor;
	var zjldwIdEditor;
	var cjldwIdEditor;
	var cjldwEditor;

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
		lnyw
				.toolbar(
						1,
						xsfl_dg,
						'${pageContext.request.contextPath}/admin/buttonAction!buttons.action',
						xsfl_did);

		xsfl_dg
				.datagrid({
					view : detailview,
					detailFormatter : function(index, row) {
						return '<div style="padding:2px"><table id="xsfl-ddv-' + index + '"></table></div>';
					},
					onExpandRow : function(index, row) {
						$('#xsfl-ddv-' + index)
								.datagrid(
										{
											url : '${pageContext.request.contextPath}/jxc/xskpAction!detDatagrid.action',
											fitColumns : true,
											singleSelect : true,
											rownumbers : true,
											loadMsg : '',
											height : 'auto',
											queryParams : {
												xskplsh : row.xskplsh,
											},
											columns : [ [
													{
														field : 'spbh',
														title : '商品编号',
														width : 200,
														align : 'center'
													},
													{
														field : 'spmc',
														title : '名称',
														width : 100,
														align : 'center'
													},
													{
														field : 'spcd',
														title : '产地',
														width : 100,
														align : 'center'
													},
													{
														field : 'sppp',
														title : '品牌',
														width : 100,
														align : 'center'
													},
													{
														field : 'spbz',
														title : '包装',
														width : 100,
														align : 'center'
													},
													{
														field : 'zjldwmc',
														title : '单位',
														width : 100,
														align : 'center'
													},
													{
														field : 'spje',
														title : '金额',
														width : 100,
														align : 'center',
														formatter : function(
																value) {
															return lnyw
																	.formatNumberRgx(value);
														}
													},
													{
														field : 'spse',
														title : '税额',
														width : 100,
														align : 'center',
														formatter : function(
																value) {
															return lnyw
																	.formatNumberRgx(value);
														}
													}, ] ],
											onResize : function() {
												xsfl_dg.datagrid(
														'fixDetailRowHeight',
														index);
											},
											onLoadSuccess : function() {
												setTimeout(
														function() {
															xsfl_dg
																	.datagrid(
																			'fixDetailRowHeight',
																			index);
														}, 0);
											}
										});
						xsfl_dg.datagrid('fixDetailRowHeight', index);
					}
				});

		//选中列表标签后，装载数据
		xsfl_tabs = $('#jxc_xsfl_tabs')
				.tabs(
						{
							onSelect : function(title, index) {
								if (index == 1) {
									xsfl_dg
											.datagrid({
												url : '${pageContext.request.contextPath}/jxc/xskpAction!datagridXsfl.action',
												queryParams : {
													bmbh : xsfl_did,
													createTime : countXsfl == 0 ? undefined
															: $(
																	'input[name=createTimeXsfl]')
																	.val(),
													xslxId : '02',
													search : countXsfl == 0 ? undefined
															: $(
																	'input[name=searchXsfl]')
																	.val(),
												}
											});
									countXsfl++;
								}
							},
						});

		xsfl_spdg = $('#jxc_xsfl_spdg').datagrid({
			width : '100%',
			fit : true,
			border : false,
			singleSelect : true,
			fitColumns : true,
			autoRowHeight : false,
			showFooter : true,
			columns : [ [ {
				field : 'spbh',
				title : '商品编号',
				width : 25,
				align : 'center',
				editor : 'text'
			}, {
				field : 'spmc',
				title : '商品名称',
				width : 100,
				align : 'center',
				editor : 'textRead'
			}, {
				field : 'spcd',
				title : '商品产地',
				width : 25,
				align : 'center',
				editor : 'textRead'
			}, {
				field : 'sppp',
				title : '商品品牌',
				width : 25,
				align : 'center',
				editor : 'text',
				hidden : true
			}, {
				field : 'spbz',
				title : '商品包装',
				width : 25,
				align : 'center',
				editor : 'text',
				hidden : true
			}, {
				field : 'zjldwmc',
				title : '单位',
				width : 25,
				align : 'center',
				editor : 'textRead'
			}, {
				field : 'spje',
				title : '金额',
				width : 25,
				align : 'center',
				editor : {
					type : 'numberbox',
					options : {
						precision : 2
					}
				}
			}, {
				field : 'spse',
				title : '税额',
				width : 25,
				align : 'center',
				editor : {
					type : 'numberbox',
					options : {
						precision : 2
					}
				}
			}, {
				field : 'sphj',
				title : '合计',
				width : 25,
				align : 'center',
				editor : {
					type : 'numberbox',
					options : {
						precision : 2
					}
				}
			}, 
			{field:'zhxs',title:'转换系数',width:25,align:'center',editor:'text', hidden:true},
			{
				field : 'zjldwId',
				title : '主单位id',
				width : 25,
				align : 'center',
				editor : 'text',
				hidden : true
			},
        	{field:'cjldwId',title:'次单位id',width:25,align:'center',editor:'text', hidden:true},
        	{
				field : 'cjldwmc',
				title : '次单位',
				width : 25,
				align : 'center',
				editor : 'textRead',
				hidden: true,
				
			}] ],
			onClickRow : clickRow,
			onAfterEdit : function(rowIndex, rowData, changes) {
				//endEdit该方法触发此事件
				editIndex = undefined;
			},

		});

		//$('#jxc_xsfl_tabs a.tabs-inner').css('height','100px');
		//$('#jxc_xsfl_tabs span.tabs-title').css('white-space','normal');

		//初始化创建时间
		$('#createDate').html(moment().format('YYYY年MM月DD日'));

		$('input[name=jxc_xsfl_khmc]').change(
				function() {
					// 		var isCheck = true;
					// 		if($('input#sxCheck').is(':checked')){
					// 			isCheck = checkKh();
					// 		}
					// 		if(isCheck){
					// 			loadKh($('input[name=khbh]').val().trim());
					// 		}
					loadKh($('input[name=jxc_xsfl_khbh]').val().trim());
				});

		//初始化仓库列表
		jxc_xsfl_ckCombo = lnyw.initCombo($("#jxc_xsfl_ckId"), 'id', 'ckmc', '${pageContext.request.contextPath}/jxc/ckAction!listCk.action?depId=' + xsfl_did);
		
		//初始化业务员列表
		jxc_xsfl_ywyCombo = lnyw.initCombo($("#jxc_xsfl_ywyId"), 'id',
				'realName',
				'${pageContext.request.contextPath}/admin/userAction!listYwys.action?did='
						+ xsfl_did);

		//初始化付款方式列表
		jxc_xsfl_jsfsCombo = lnyw
				.initCombo($("#jxc_xsfl_jsfsId"), 'id', 'jsmc',
						'${pageContext.request.contextPath}/jxc/jsfsAction!listJsfs.action');

		$('input').focusin(function() {
			if (editIndex != undefined) {
				xsfl_spdg.datagrid('endEdit', editIndex);
			}
		});

		//初始化信息
		init();
	});

	//以下为商品列表处理代码
	function init() {
		//清空列表，添加一个空行
		xsfl_spdg.datagrid({
			data : [ {} ],
		});

		//清空全部字段
		$('#info input').val('');
		// 	$('input').val('');
		//$('input:checkbox').removeAttr('checked');
		//$('input:checkbox').removeProp('checked');
		$('input:checkbox').prop('checked', false);

		jxc_xsfl_ckCombo.combobox('clear');
		jxc_xsfl_ckCombo.combobox('selectedIndex', 0);
		
		//jxc_xsfl_ywyCombo.combobox('selectedIndex', 0);
		//jxc_xsfl_jsfsCombo.combobox('selectedIndex', 0);
		jxc_xsfl_jsfsCombo.combobox('setValue', JSFS_QK);
		jxc_xsfl_jsfsCombo.combobox('readonly', false);

		//xsfl_spdg.datagrid('fitColumns');
		jxc.spInfo($('#jxc_xsfl_layout'), '');

		//初始化流水号
		$
				.ajax({
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
		lnyw
				.toolbar(
						0,
						xsfl_spdg,
						'${pageContext.request.contextPath}/admin/buttonAction!buttons.action',
						xsfl_did);

		//清空合计内容
		xsfl_spdg.datagrid('reloadFooter', [ {} ]);
	}

	//判断行是否编辑完成
	function rowOk() {
		if (editIndex == undefined) {
			return true;
		}
		if (keyOk()) {
			if (spjeEditor.target.val() > 0) {
				return true;
			}
		}
		return false;
	}

	//判断商品信息是否完成
	function keyOk() {
		if (spmcEditor.target.val().length > 0) {
			return true;
		}
		return false;
	}

	//根据指定行，进入编辑状态
	function enterEdit(rowIndex, isClick) {
		//如果选中行为最后一行，先增加一个空行
		if (rowIndex == xsfl_spdg.datagrid('getRows').length - 1) {
			xsfl_spdg.datagrid('appendRow', {});
		} else {
			if (!isClick) {
				return false;
			}
		}
		//将选中行标识为编辑行
		editIndex = rowIndex;
		//选择编辑行，并进行入编辑状态
		xsfl_spdg.datagrid('selectRow', editIndex).datagrid('beginEdit',
				editIndex);
		setEditing();
	}

	//单击行操作
	function clickRow(rowIndex, rowData) {
		//是否有编辑行
		if (editIndex != undefined) { //有编辑行
			//选中行与编辑行是否为同一行
			//同一行则无任何操作
			if (editIndex != rowIndex) {
				//编辑行内容验证是否通过
				if (rowOk()) { //行验证通过
					//结束编辑行
					xsfl_spdg.datagrid('endEdit', editIndex);
					//将选中行转为编辑状态
					enterEdit(rowIndex, true);
				} else { //行验证未通过
					//验证关键字段信息
					//通过，不做任何操作，将该行继续完成或删除该行
					if (!keyOk()) { //未通过，将该行删除
						if (rowIndex > editIndex) {
							rowIndex = rowIndex - 1;
						}
						removeRow();
						enterEdit(rowIndex, true);
					}
				}
			}
		} else { //无编辑行
			enterEdit(rowIndex, true);
		}
	}

	//删除行
	function removeRow() {
		if (editIndex == undefined) {
			return;
		}
		xsfl_spdg.datagrid('cancelEdit', editIndex).datagrid('deleteRow',
				editIndex);
		editIndex = undefined;
		updateFooter();
		if (xsfl_did != '04') {
			jxc.hideKc('#jxc_xsfl_layout');
		}
		//xsfl_spdg.datagrid('fitColumns');
	}

	//保存行
	function accept() {
		if (rowOk()) {
			xsfl_spdg.datagrid('acceptChanges');
			if (xsfl_did != '04') {
				jxc.hideKc('#jxc_xsfl_layout');
			}
			//xsfl_spdg.datagrid('fitColumns');
		}
	}

	//取消编辑行
	function cancelAll() {
		xsfl_spdg.datagrid('rejectChanges');
		editIndex = undefined;
		updateFooter();
		if (xsfl_did != '04') {
			jxc.hideKc('#jxc_xsfl_layout');
		}
		//xsfl_spdg.datagrid('fitColumns');
	}

	//提交数据到后台
	function saveXsfl() {
		var msg = formValid();
		if (msg == '') {
			//编辑行是否完成
			if (rowOk()) {
				xsfl_spdg.datagrid('endEdit', editIndex);
			} else { //编辑行未完成
				if (keyOk()) {
					$.messager.alert('提示', '商品数据编辑未完成,请继续操作！', 'error');
					return false;
				} else {
					removeRow();
				}
			}
		} else {
			$.messager.alert('提示', msg + '填写不完整,请继续操作！', 'error');
			return false;
		}

		var footerRows_xsfl = xsfl_spdg.datagrid('getFooterRows');

		var sxkh_xsfl = jxc.isExcess('${pageContext.request.contextPath}',
				xsfl_did, $('input[name=khbh]').val(), jxc_xsfl_ywyCombo
						.combobox('getValue'));
		if (sxkh_xsfl.isLocked == '1') {
			$.messager.alert('提示', '该客户已经被限制销售，请联系管理人员！', 'error');
			return false;
		}

		//直接填开发票时考察客户限额
		if (jxc.notInExcludeKhs(xsfl_did, $('input[name=khbh]').val())
				&& jxc_xsfl_jsfsCombo.combobox('getValue') == JSFS_QK) {
			if (!$('input[name=xsthDetIds]').val()) {
				if (sxkh_xsfl.khlxId == '02') {
					if ((Number(sxkh_xsfl.qkje) + Number(footerRows_xsfl[0].sphj)) > Number(sxkh_xsfl.sxje)
							* Number(sxkh_xsfl.limitPer)) {
						$.messager.alert('提示', '客户欠款已超出限制比例，请回款后销售！', 'error');
						return false;
					} else {
						if ((Number(sxkh_xsfl.qkje) + Number(footerRows_xsfl[0].sphj)) > Number(sxkh_xsfl.limitJe)) {
							$.messager.alert('提示', '客户欠款已超出限制金额，请回款后销售！',
									'error');
							return false;
						}
					}
				}
			}
		}

		var rows = xsfl_spdg.datagrid('getRows');
		var effectRow = new Object();
		if (rows.length == 1) {
			$.messager.alert('提示', '未添加商品数据,请继续操作！', 'error');
			return false;
		}

		doSave();


		function doSave() {
			var footerRows = xsfl_spdg.datagrid('getFooterRows');
			//将表头内容传入后台
			if ($('input#isNsr').is(':checked')) {
				effectRow['fplxId'] = '1';
				effectRow['fplxmc'] = '增值税';
			} else {
				effectRow['fplxId'] = '0';
				effectRow['fplxmc'] = '普通';
			}

			// 		effectRow['isSx'] = $('input[name=isSx]').is(':checked') ? '1' : '0';
			effectRow['isSx'] = '0';
			effectRow['isZs'] = '0';
			effectRow['isTh'] = '0';
			effectRow['isHk'] = '1';
			effectRow['fromTh'] = '0';
			effectRow['xslxId'] = '02';
			effectRow['xslxmc'] = '返利';
			effectRow['thfs'] = '1';
			

			effectRow['khbh'] = $('input[name=jxc_xsfl_khbh]').val();
			effectRow['khmc'] = $('input[name=jxc_xsfl_khmc]').val();
			effectRow['sh'] = $('input[name=xsfl_sh]').val();
			effectRow['khh'] = $('input[name=xsfl_khh]').val();
			effectRow['dzdh'] = $('input[name=xsfl_dzdh]').val();
			effectRow['ckId'] = jxc_xsfl_ckCombo.combobox('getValue');
			effectRow['ckmc'] = jxc_xsfl_ckCombo.combobox('getText');
			effectRow['ywyId'] = jxc_xsfl_ywyCombo.combobox('getValue');
			effectRow['ywymc'] = jxc_xsfl_ywyCombo.combobox('getText');
			effectRow['jsfsId'] = jxc_xsfl_jsfsCombo.combobox('getValue');
			effectRow['jsfsmc'] = jxc_xsfl_jsfsCombo.combobox('getText');
			effectRow['bz'] = $('input[name=jxc_xsfl_bz]').val();
			effectRow['hjje'] = lnyw.delcommafy(footerRows[0]['spje']);
			effectRow['hjse'] = lnyw.delcommafy(footerRows[0]['spse']);
			effectRow['bmbh'] = xsfl_did;
			effectRow['lxbh'] = xsfl_lx;
			effectRow['menuId'] = xsfl_menuId;

			//将表格中的数据去掉最后一个空行后，转换为json格式
			effectRow['datagrid'] = JSON.stringify(rows.slice(0,
					rows.length - 1));
			//提交到action
			//$.ajaxSettings.traditional=true;
			//MaskUtil.mask('正在保存，请等待……');
			$
					.ajax({
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
							$.messager
									.confirm(
											'请确认',
											'是否导出销售单据到金穗？',
											function(r) {
												if (r) {
													var url = lnyw.bp()
															+ '/jxc/xskpAction!toJs.action?xskplsh='
															+ rsp.obj.xskplsh;
													jxc.toJs(url,
															rsp.obj.fplxId);
												}
											});
						},
						error : function() {
							$.messager.alert("提示", "提交错误了！");
						},
						complete : function() {
							//MaskUtil.unmask();
						}
					});
		}
	}

	//处理编辑行
	function setEditing() {

		//加载字段
		var editors = xsfl_spdg.datagrid('getEditors', editIndex);
		spbhEditor = editors[0];
	    spmcEditor = editors[1];
	    spcdEditor = editors[2];
	    spppEditor = editors[3];
	    spbzEditor = editors[4];
	    zjldwEditor = editors[5];
	    spjeEditor = editors[6];
	    spseEditor = editors[7];
	    sphjEditor = editors[8];
	    zhxsEditor = editors[9];
	    zjldwIdEditor = editors[10];
	    cjldwIdEditor = editors[11];
	    cjldwEditor = editors[12];

		if ($(spbhEditor.target).val() != '') {
			jxc.spInfo($('#jxc_xsfl_layout'), '1', $(spppEditor.target).val(),
					$(spbzEditor.target).val());
			//     	var fhValue = '';
			//     	if($('input[name=isFh]').is(':checked')){
			//     		fhValue = jxc_xsfl_fhCombo.combobox('getValue');
			//     	}
		} else {
			jxc.spInfo($('#jxc_xsfl_layout'), '');
		}
		//xsfl_spdg.datagrid('fitColumns');

		//loadEditor();
		//处理编辑行的换行事件
		$('.datagrid-row-editing').on('keydown', 'input', function(event) {
			//监听按下向下箭头
			if (event.keyCode == 40) {
				var rowIndex = editIndex;
				if (rowOk()) {
					xsfl_spdg.datagrid('endEdit', editIndex);
					enterEdit(rowIndex + 1, false);
				} else {
					if (!keyOk()) {
						removeRow();
					}
				}
			}
		});

		//处理商品编号按键事件
		spbhEditor.target
				.bind(
						'keydown',
						function(event) {
							//按Tab键,根据商品编号获取商品信息
							if (event.which == 9) {
								if ($(this).val().trim().length == 7) {
									if (!existKey($(this).val(), editIndex)) {
										$
												.ajax({
													url : '${pageContext.request.contextPath}/jxc/spAction!loadSp.action',
													async : false,
													context : this,
													data : {
														spbh : $(this).val(),
														depId : xsfl_did,
													},
													dataType : 'json',
													success : function(data) {
														if (data.success) {
															//设置信息字段值
															setValueBySpbh(data.obj);
															zslEditor.target
																	.focus();
														} else {
															$.messager.alert(
																	'提示',
																	'商品编号不存在！',
																	'error');
														}
													}
												});
									} else {
										$.messager.alert('提示',
												'商品编号只能出现一次，请重新输入！', 'error');
									}
								} else {
									$.messager.alert('提示', '商品编号必须是7位，请重新输入！',
											'error');
								}
								return false;
							}
							//按ESC键，弹出对话框，可以按商品编号或名称查询，双击商品行返回信息
							if (event.which == 27) {
								jxc
										.spQuery(
												$(spbhEditor.target).val(),
												xsfl_did,
												jxc_xsfl_ckCombo.combobox('getValue'),
												'${pageContext.request.contextPath}/jxc/spQuery.jsp',
												'${pageContext.request.contextPath}/jxc/spAction!spDg.action',
												spjeEditor);
								// 			zslEditor.target.focus();
								return false;
							}
						});

		spjeEditor.target.bind('keyup', function(event){
			$(spseEditor.target).numberbox('setValue', $(spjeEditor.target).val() * SL);
			$(sphjEditor.target).numberbox('setValue', Number($(spjeEditor.target).val()) + Number($(spseEditor.target).val()));
	    	updateFooter();
	    }).bind('keydown', function(event){
	    	if(event.keyCode == 40){
//	      		//spjeEditor.target.focus();
	     	}
	    });
		
		//输入合计金额后，计算单价
		sphjEditor.target.bind('keyup', function(event) {
			if (event.keyCode == 9) {
				return false;
			}
		}).bind('keydown', function(event) {
			$(spjeEditor.target).numberbox('setValue',
					$(sphjEditor.target).val() / (1 + SL));
			$(spseEditor.target).numberbox('setValue',
					$(sphjEditor.target).val() - $(spjeEditor.target).val());
			updateFooter();
			if (event.keyCode == 40) {
				//spjeEditor.target.focus();
			}
		});

		//初始进入编辑状态时，使用商品编号获得焦点
		spbhEditor.target.focus();
	}
	//求和
	function updateFooter() {
		var rows = xsfl_spdg.datagrid('getRows');
		//var spmc_footer = '合计';
		var hjje = 0;
		var hjse = 0;
		$.each(rows, function() {
			var index = xsfl_spdg.datagrid('getRowIndex', this);
			if (index <= rows.length - 1) {
				if (editIndex == index) {
					hjje += Number(spjeEditor.target.val());
					hjse += Number(spseEditor.target.val());
				} else {
					hjje += Number(this.spje == undefined ? 0 : this.spje);
					hjse += Number(this.spse == undefined ? 0 : this.spse);
				}
			}
		});

		xsfl_spdg.datagrid('reloadFooter', [ {
			spmc : '合计',
			spje : lnyw.formatNumberRgx(hjje.toFixed(2)),
			spse : lnyw.formatNumberRgx(hjse.toFixed(2)),
			sphj : lnyw.formatNumberRgx((hjje + hjse).toFixed(2))
		}
		// 			,{
		// 				spmc: '总计',
		// 				sphj: (hjje + hjse).toFixed(LENGTH_JE)
		// 			}
		]);
	}

	//验证记录是否已存在
	function existKey(value, rowIndex) {
		var keys = xsfl_spdg.datagrid('getRows');
		var exist = false;
		if (keys.length > 2) {
			$.each(keys, function() {
				var index = xsfl_spdg.datagrid('getRowIndex', this);
				if (index != rowIndex) {
					if (this.spbh == value) {
						exist = true;
						return;
					}
				}
			});
		}
		return exist;
	}

	function formValid() {
		var message = '';
		if ($('input[name=jxc_xsfl_khmc]').val() == '') {
			message += '客户信息<br>';
		}
		if (jxc_xsfl_ywyCombo.combobox('getValue') == 0) {
			message += '未选择业务员<br>';
		}
		return message;
	}

	//根据获得的行数据对各字段赋值
	function setValueBySpbh(rowData) {
		spbhEditor.target.val(rowData.spbh);
		spmcEditor.target.val(rowData.spmc);
		spcdEditor.target.val(rowData.spcd);
		spppEditor.target.val(rowData.sppp);
		spbzEditor.target.val(rowData.spbz);
		zjldwEditor.target.val(rowData.zjldwmc);
		zhxsEditor.target.val(rowData.zhxs);
		zjldwIdEditor.target.val(rowData.zjldwId);
		cjldwIdEditor.target.val(rowData.cjldwId);
		cjldwEditor.target.val(rowData.cjldwmc);

		jxc.spInfo($('#jxc_xsfl_layout'), '1', $(spppEditor.target).val(), $(
				spbzEditor.target).val());
	}

	function checkKh() {
		$
				.ajax({
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
		$
				.ajax({
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
			//不再进行判断，2014-06-22
			//是否授信客户
			// 		var params = '';
			// 		if($('input#sxCheck').is(':checked') ){
			// 			params += '?isSx=1&depId=' + xsfl_did;
			// 			if($('input#isNsr').is(':checked')){
			// 				params += '&isNsr=1';
			// 			}
			// 		}else{
			// 			if($('input#isNsr').is(':checked')){
			// 				params += '?isNsr=1';
			// 			}
			// 		}
			jxc
					.query('客户检索', $('input[name=jxc_xsfl_khbh]'),
							$('input[name=jxc_xsfl_khmc]'), '',
							'${pageContext.request.contextPath}/jxc/query.jsp',
							'${pageContext.request.contextPath}/jxc/khAction!khDg.action');
			// 				'${pageContext.request.contextPath}/jxc/khAction!khDg.action' + params);
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
					$.messager
							.prompt(
									'请确认',
									'是否要冲减选中的销售返利单？请填写备注',
									function(bz) {
										if (bz != undefined) {
											//MaskUtil.mask('正在冲减，请等待……');
											$
													.ajax({
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
															xsfl_dg
																	.datagrid('load');
															xsfl_dg
																	.datagrid('unselectAll');
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

	function toJs() {
		var rows = xsfl_dg.datagrid('getSelections');
		var xskplshs = [];
		if (rows.length > 0) {
			var preRow = undefined;
			var flag = true;
			$.each(rows, function(index) {
				if (rows[index].isCj == '1') {
					$.messager.alert('提示', '选择的销售已冲减！', 'error');
					return false;
				}
				xskplshs.push(rows[index].xskplsh);
				if (index != 0) {
					if (this.khbh != preRow.khbh) {
						$.messager.alert('提示', '请选择同一客户的销售发票进行操作！', 'error');
						flag = false;
						//return false;
					} else {
						preRow = this;
					}
				}
				preRow = this;
			});
			if (flag) {
				$.messager.confirm('请确认', '是否导出数据到金穗接口？', function(r) {
					if (r) {
						var xsfllshStr = xskplshs.join(',');
						var url = lnyw.bp()
								+ '/jxc/xskpAction!toJs.action?xskplsh='
								+ xsfllshStr;
						jxc.toJs(url, rows[0].fplxId);
					}
				});
			}
		} else {
			$.messager.alert('警告', '请选择一条记录进行操作！', 'warning');
		}

		// 	var row = xsfl_dg.datagrid('getSelected');
		// 	if (row != undefined) {
		// 		$.messager.confirm('请确认', '是否导出数据到金穗接口？', function(r) {
		// 			if (r) {
		// 				var url = lnyw.bp() + '/jxc/xsflAction!toJs.action?xsfllsh=' + row.xsfllsh;
		// 				jxc.toJs(url, row.fplxId);
		// 			}
		// 		});
		// 	}else{
		// 		$.messager.alert('警告', '请选择一条记录进行操作！',  'warning');
		// 	}
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

<!-- tabPosition:'left', headerWidth:'35' -->
<div id="jxc_xsfl_tabs" class="easyui-tabs"
	data-options="fit:true, border:false,"
	style="width: 100%; height: 100%;">

	<div title="新增记录" data-options="closable:false">
		<div id='jxc_xsfl_layout' style="height: 100%;width=100%">
			<div class="tinfo" id="info"
				data-options="region:'north',title:'单据信息',border:false,collapsible:false"
				style="width: 100%; height: 200px">
				<table>
					<tr>
						<td colspan="2">增值税发票<input type="radio"
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
						<th>结算方式</th>
						<td><input id="jxc_xsfl_jsfsId" name="jsfsId" type="text"></td>
						<th>业务员</th>
						<td><input id="jxc_xsfl_ywyId" name="ywyId" type="text"></td>
						<th>仓库</th><td><input id="jxc_xsfl_ckId" name="ckId" type="text"></td>
					</tr>
					<tr>
						<th>备注</th>
						<td colspan="7"><input name="jxc_xsfl_bz" style="width: 90%"></td>
					</tr>
				</table>
			</div>
			<div data-options="region:'center',title:'商品信息',split:true"
				style="width: 150px">
				<table id='jxc_xsfl_spdg'></table>
			</div>
		</div>
	</div>
	<div title="销售列表" data-options="closable:false">
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
