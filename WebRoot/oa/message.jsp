<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/plugins/kindeditor-4.1.11/themes/default/default.css" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/plugins/kindeditor-4.1.11/kindeditor-all.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/plugins/kindeditor-4.1.11/lang/zh-CN.js"></script>

<style type="text/css">
	.ke-container{
		margin-left: 70px;
		margin-top: 10px;
	}
</style>

<script type="text/javascript">

	var oa_message_menuId;
	var contact_source;
	var contact_from;
	var contact_to;
	
	var tree_source;
	var tree_target;

    var message_sendDg;
    var message_receiveDg;

	var memoEditor;
	var uploadlist = [];
    var savelist = [];
	
	$(function(){
        oa_message_menuId = lnyw.tab_options().id;


		//装载联系人
		$.ajax({
			url : '${pageContext.request.contextPath}/admin/userAction!getContacts.action',
			dataType : 'json',
			async: false,
			success : function(d) {
				contact_source = d;
				array_sort(contact_source);
				contact_reset();
			}
		});

		var editor_items = [  'undo', 'redo', '|', 'preview',
				//'print', 'template', 'code', 
				'cut', 'copy', 'paste', 'plainpaste', 'wordpaste', '|', 'justifyleft',
				'justifycenter', 'justifyright', 'justifyfull',
				'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent',
				'subscript', 'superscript', 'clearhtml', 'quickformat',
				'selectall', '|',
				//'fullscreen', 
				'/',
				'formatblock', 'fontname', 'fontsize', '|', 'forecolor',
				'hilitecolor', 'bold', 'italic', 'underline', 'strikethrough',
				'lineheight', 'removeformat', '|', 'image',
				//'multiimage', 'flash', 'media', 
				'insertfile', 'table', 'hr',
				//'emoticons', 'baidumap', 'pagebreak', 'anchor', 
				'link', 'unlink', '|', 'source', 'about' ];
	
 		//var K = KindEditor;
		$('#oa_message_tabs').tabs({
			onSelect: function(title, index){
				if(index == 0){
					memoEditor = KindEditor
	 				.create(
	 					//'textarea[name="memo"]',
	 					'#memo_editor',
	 					{
	 						items: editor_items,
                            allowImageRemote: false,
                            formatUploadUrl: false,
	 						uploadJson : "${pageContext.request.contextPath}/plugins/kindeditor-4.1.11/jsp/upload_json.jsp",
                            afterUpload: function(url, data, name){
								uploadlist.push({filepath: url, filename: data.title, type: name});
                            },
                            afterChange: function() {
                                this.sync();
                            },
	 					});
				}
                if(index == 1){
                    message_sendDg.datagrid({
                        url: '${pageContext.request.contextPath}/oa/messageAction!sendDg.action',
                    });
                }
                if(index == 2){
                    message_receiveDg.datagrid({
                        url: '${pageContext.request.contextPath}/oa/messageAction!receiveDg.action'
                    });
                }
			},
			onBeforeClose: function(title, index){
				if(index == 0){
					KindEditor.remove('#memo_editor');
				}
			}
		});

	});
	
	//数组的排序
	function array_sort(arr){
		//根排序
		arr.sort(function(a, b){
			return a.orderNum - b.orderNum;
		});
		//子结点排序
		for(var i = 0; i < arr.length; i++){
			arr[i].children.sort(function(a, b){
				return a.orderNum - b.orderNum;
			});
		}
	}
	
	function showContacts() {
		var dialog = $('#message_contact_select');
		dialog.dialog({
			title : '选择收件人',
			href : '${pageContext.request.contextPath}/oa/selectContact.jsp',
			width : 515,
			height : 471,
			closable: false,
			cache: false,
			modal: true,
			buttons : [ {
				text : '确定',
				handler : function() {
					var names = [];
					var ids = [];

					for (var i = 0, len = contact_to.length; i < len; i++) {
						for (var j = 0, lenc = contact_to[i].children.length; j < lenc; j++) {
							ids.push(contact_to[i].children[j].id);
							names.push(contact_to[i].children[j].text);
						}
					}

					$('input#receiverIds').val(ids.join(','));
					$('input#receiverNames').val(names.join(','));
					dialog.dialog('close');
				}
			} ],
			onLoad : function() {
			    tree_source = $('#contact_from');
                tree_target = $('#contact_to');

                tree_source.tree({
					data : contact_from,
					onDblClick : function(node) {
						contactTrans(node, tree_source, tree_target, contact_from, contact_to);
					},
				});
                tree_target.tree({
					data : contact_to,
					onDblClick : function(node) {
						contactTrans(node, tree_target, tree_source, contact_to, contact_from);
					},
				});
			},

		});
	}
	
	//更新后，排序、刷新树
	function reload_tree(){
		array_sort(contact_from);
		array_sort(contact_to);

        tree_source.tree('loadData', contact_from);
        tree_target.tree('loadData', contact_to);
	}
	
	//全部选中
	function trans_toR(){
		trans_all(tree_source, tree_target, contact_from, contact_to);
	}
	
	//全部移除
	function trans_toL(){
		trans_all(tree_target, tree_source, contact_to, contact_from);
	}
	
	//全部操作时将源删除、目标增加
	function trans_all(source, target, array_from, array_to){
		var nodes = source.tree('getRoots');
		for(var i = 0; i < nodes.length; i++){
			contactTrans(nodes[i], source, target, array_from, array_to);
		}
	}

	//处理选中结点，并删除对应源数组、增加目标数据（以后可能中间步骤拿出去）
	function contactTrans(node, source, target, array_from, array_to) {
		if (source.tree('isLeaf', node.target)) {
			//部门结点
			var parNode = source.tree('getParent', node.target);
		
			//获取人员包括部门
			var par = [{
				id: parNode.id,
				text: parNode.text,
				orderNum: getOrderNum(array_from, parNode.id),
				children: [{
					id: node.id,
					text: node.text,
					orderNum: getOrderNum(array_from, parNode.id, node.id),
				}],
			}];
			
			//处理源数驵的子结点
			for (var i = 0, len = array_from.length; i < len; i++) {
				if (array_from[i].id == parNode.id) {
					for (var j = 0, lenc = array_from[i].children.length; j < lenc; j++) {
						if (array_from[i].children[j].id == node.id) {
							//移除选中的子结点
							array_from[i].children.splice(j, 1);
							//移除子结点后部门下无人员
							if (array_from[i].children.length == 0) {
								//移除部门
								array_from.splice(i, 1);
							}
							break;
						}
					}
					break;
				}
			}
			//增加到目标数组
			trans(par, array_to);
		} else {
			for(var i = 0, len = array_from.length; i < len; i++) {
				if (array_from[i].id == node.id) {
					//处理目标数组，中间代码已处理源数组
					trans(array_from.splice(i, 1), array_to);
					break;
				}
			}
		}
		
		reload_tree();
	}
	
	//将选中结点（包括父结点加入目标数组）
	function trans(node, array_target) {
		if (array_target.length > 0) {
			for (var i = 0, len = array_target.length; i < len; i++) {
				if (node[0].id == array_target[i].id) {
					for(var j = 0; j < node[0].children.length; j++){
						array_target[i].children.push(node[0].children[j]);
					}
					return;
				}
			}
		}
		array_target.push(node[0]);
	}
	
	//获取根、子结点的OrderNum(以后可能会改为attributes)
	function getOrderNum(array_from, pid, id){
		for(var i = 0; i < array_from.length; i++){
			if(array_from[i].id == pid){
				if(id){
					for(var j = 0; j < array_from[i].children.length; j++){
						if(array_from[i].children[j].id == id){
							return array_from[i].children[j].orderNum;
						}
					}
				}else{
					return array_from[i].orderNum; 
				}
			}
		}
	}
	
	
	//信息提交
	function message_submit() {
	    var form = $('#message_send');
        form.form('submit', {
        	url : '${pageContext.request.contextPath}/oa/messageAction!add.action',
        	onSubmit : function(param) {
                var error = [];
                if(!$.trim($('#receiverIds').val())){
                    error.push('收件人');
                }
                if(!$.trim($('#subject').val())){
                    error.push('主题');
                }
                if(error.length > 0 ){
                    $.messager.alert('提示', '请录入' + error.join('，') + '！', 'error');
                    return false;
                }
                unusedUpload(memoEditor.html());
                param.menuId = oa_message_menuId;
                param.datagrid = JSON.stringify(savelist);
                //$('input[name="datagrid"]').val(JSON.stringify(savelist));
			},
			success : function(data) {
				var json = $.parseJSON(data);
				if(json.success){
					$.messager.show({
						title : '提示',
						msg : json.msg
					});
					message_reset();
				}
			}
		});
	}
	
	//信息重置
	function message_reset(){
	    uploadlist = [];
	    savelist = [];
		$('input.cont').val('');
        KindEditor.html('#memo_editor', '');
		contact_reset();
	}
	
	//联系人重置
	function contact_reset(){
		contact_from = [];
		for(var i = 0; i < contact_source.length; i++){
			contact_from[contact_from.length] = $.extend(true, {}, contact_source[i]);
		}
		contact_to = [];
	}

	//删除已上传但保存之前已删除的文件
	function unusedUpload(content){
	    $.each(uploadlist, function(){
	       	if(content.indexOf(this.filepath) < 0){
				$.ajax({
					url: '${pageContext.request.contextPath}/oa/paperAction!deleteFile.action',
					data: {
						filepath: this.filepath
					}
				});
            }else{
	       	    savelist.push(this);
            }
		});
	}

	//-------------------------------------发送列表管理
	
	message_sendDg = $('#oa_messageS_dg').datagrid({
		url:'${pageContext.request.contextPath}/oa/messageAction!sendDg.action',
		fit : true,
		singleSelect:true,
	    border : false,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
	    columns:[[
	        {field:'id',title:'编号',width:100,checkbox:true},
	        {field:'subject',title:'主题',width:300,
	        	styler: function(){
					return 'color:blue;';
				}
	        },
	        {field:'createTime',title:'时间'},
            {field:'opened',title:'公开',width:50,
                formatter : function(value) {
                    if (value == '1') {
                        return '是';
                    } else {
                        return '';
                    }
                }},
	        {field:'receiverNames',title:'接收人',
				formatter: function(value){
	            	return lnyw.memo(value, 50);
    			}},
	    ]],
        toolbar:'#oa_messageS_tb',
        onDblClickCell: function(index,field,value){
		    if(field == 'subject'){
                var row = $(this).datagrid('selectRow', index).datagrid('getSelected');
                getMessage(row, 'send');
            }

        }
	});
	//根据权限，动态加载功能按钮
	lnyw.toolbar(1, message_sendDg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', lnyw.tab_options().did);

    function deleteMessage(){
        var selected = message_sendDg.datagrid('getSelected');
        if (selected != undefined) {
            $.messager.confirm('请确认', '您要删除选中的信息，接收人也将不能再阅读此消息，此操作不可恢复，请确认？', function(r) {
                if (r) {
                    //MaskUtil.mask('正在取消，请稍等……');
                    $.ajax({
                        url : '${pageContext.request.contextPath}/oa/messageAction!deleteMessage.action',
                        data : {
                            id : selected.id,
                            menuId: oa_message_menuId
                        },
                        dataType : 'json',
                        success : function(d) {
                            message_sendDg.datagrid('reload');
                            message_sendDg.datagrid('unselectAll');
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
            $.messager.alert('警告', '请选择最少一条记录进行操作！',  'warning');
        }
	}

    function updateStatus(){
        var selected = message_sendDg.datagrid('getSelected');
        if (selected != undefined) {
            $.messager.confirm('请确认', '您要更改选中信息的状态，请确认？', function(r) {
                if (r) {
                    //MaskUtil.mask('正在取消，请稍等……');
                    $.ajax({
                        url : '${pageContext.request.contextPath}/oa/messageAction!updateStatus.action',
                        data : {
                            id : selected.id,
							menuId: oa_message_menuId
                        },
                        dataType : 'json',
                        success : function(d) {
                            message_sendDg.datagrid('load');
                            message_sendDg.datagrid('unselectAll');
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
            $.messager.alert('警告', '请选择最少一条记录进行操作！',  'warning');
        }
    }

    function searchSend(){
        message_sendDg.datagrid('load',{
            search: $('input[name=searchSend]').val()
        });
    }

	//-------------------------------------接收列表管理
    message_receiveDg = $('#oa_messageR_dg').datagrid({
        url:'${pageContext.request.contextPath}/oa/messageAction!receiveDg.action',
        fit : true,
        singleSelect:true,
        border : false,
        pagination : true,
        pagePosition : 'bottom',
        pageSize : pageSize,
        pageList : pageList,
        columns:[[
            {field:'id',title:'编号',width:100,checkbox:true},
            {field:'subject',title:'主题',width:300,
                styler: function(){
                    return 'color:blue;';
                }
            },
            {field:'createTime',title:'时间'},
            {field:'createId',title:'发送人Id',hidden:true},
            {field:'createName',title:'发送人'},
            {field:'readTime',title:'已读'},
            {field:'recId',title:'收件id',width:100,hidden:true},
        ]],
        toolbar:'#oa_messageR_tb',
        onDblClickCell: function(index,field){
            if(field == 'subject'){
                var row = $(this).datagrid('selectRow', index).datagrid('getSelected');
                getMessage(row, 'receive');
            }
        }
    });
    //根据权限，动态加载功能按钮
    lnyw.toolbar(2, message_receiveDg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', lnyw.tab_options().did);

    function cancelReceive(){
        var selected = message_receiveDg.datagrid('getSelected');
        if (selected != undefined) {
			$.messager.confirm('请确认', '您要删除选中的信息，此操作不可恢复，请确认？', function(r) {
				if (r) {
					//MaskUtil.mask('正在取消，请稍等……');
					$.ajax({
						url : '${pageContext.request.contextPath}/oa/messageAction!cancelReceive.action',
						data : {
							recId : selected.recId,
                            menuId: oa_message_menuId
						},
						dataType : 'json',
						success : function(d) {
                            message_receiveDg.datagrid('load');
                            message_receiveDg.datagrid('unselectAll');
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
            $.messager.alert('警告', '请选择最少一条记录进行操作！',  'warning');
        }
	}

    function searchReceive(){
        message_receiveDg.datagrid('load',{
            search: $('input[name=searchReceive]').val()
        });
    }

    //-------------------------------------信息显示
    function getMessage(row, source){
		$.ajax({
			url : '${pageContext.request.contextPath}/oa/messageAction!getMessage.action',
			data : {
				id : row.id,
				source: source
			},
			dataType : 'json',
			success : function(d) {
				row.memo = d.obj.memo;
				addTab(row, source);
			}
		});
    }

	function addTab(row, source){
		$('#oa_message_tabs').tabs('add',{
			title: source + ':' + row.subject,
			selected: true,
			closable: true,
			href: '${pageContext.request.contextPath}/oa/message_show.jsp',
			onLoad: function(){
                $('span#subject').text(row.subject);
                $('span#createTime').text(row.createTime);
                if(source == 'send'){
                    $('span#receivers').text(row.receiverNames);
                    $('div.receive').css("display", "none");
                }else if(source == 'receive'){
                    $('span#sender').text(row.createName);
                    $('div.send').css("display", "none");
                }

                $.ajax({
                    url : '${pageContext.request.contextPath}/oa/paperAction!getPapers.action',
                    data : {
                        messageId : row.id,
						type: 'insertfile'
                    },
                    dataType : 'json',
                    success : function(d) {
                        if(d.obj.rows){
                            var papers = [];
                            for(var i = 0; i < d.obj.rows.length; i++){
                                papers.push("<a href='${pageContext.request.contextPath}/oa/paperAction!downloadFile.action?filename=" + d.obj.rows[i].filename + "&filepath=" + d.obj.rows[i].filepath + "'>" + d.obj.rows[i].filename + "</a>");
                            }
                            $('div#attached').html(papers.join("<br/>"));
                        }else{
                            $('div.attached').css("display", "none");
                        }
                    }
                });

                $('div#memo').html(row.memo);
			}
		});
	}

</script>

<div id="oa_message_tabs" class="easyui-tabs"
	data-options="fit:true, border:false,"
	style="width: 100%; height: 100%;">

	<div title="新增记录" data-options="closable:false">
 		<form id="message_send" method="post">
			<div class="message_line">
				<span class="field_label">收件人</span>
				<input class="cont field_value" type="text" readOnly="readOnly" name="receiverNames" id="receiverNames" style="width:600px;"></input>
				<input type="button" value="添加收件人" onclick="showContacts()"></input>
				<input type="hidden" class="cont" name="receiverIds" id="receiverIds"></input>
			</div>
			<div class="message_line">
				<span class="field_label">主题</span>
				<input class="cont field_value"	type="text" name="subject" id="subject" data-options="required:true" style="width:600px;"></input>
				<span style="color: red;">(必填)</span>
			</div>
			<div class="message_line">
				<span class="field_label">内容</span>
				<textarea name="memo" id="memo_editor" class="cont" style="width:800px;height:500px;margin-top: 10px; margin-left: 70px;"></textarea>
			</div>
			<%--<input type='hidden' name='datagrid' />--%>

	 	</form>
		<div class="message_line" style="margin-left: 70px;">
			<input type="button" value="提交" onclick="message_submit()"></input>
			<input type="button" value="重置" onclick="message_reset()"></input>
		</div>
	</div>
	<div title="发送列表" data-options="closable:false">
		<div id='oa_messageS_dg'></div>
	</div>
	<div title="接收列表" data-options="closable:false">
		<div id='oa_messageR_dg'></div>
	</div>
</div>
<div id="message_contact_select"></div>
<div id="oa_messageS_tb" style="padding:3px;height:auto">
	输入主题、内容关键字：<input type="text" name="searchSend" style="width:100px">
	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchSend();">查询</a>
</div>
<div id="oa_messageR_tb" style="padding:3px;height:auto">
	输入主题、内容关键字、发送人：<input type="text" name="searchReceive" style="width:100px">
	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchReceive();">查询</a>
</div>
