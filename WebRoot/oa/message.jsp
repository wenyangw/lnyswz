<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/js/kindeditor/themes/default/default.css" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/kindeditor/kindeditor-all.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/kindeditor/lang/zh-CN.js"></script>

<script type="text/javascript">

	var contact_source;
	var contact_from;
	var contact_to;
	

	contact_source = [ {
		id : 1,
		text : '经理室',
		children : [ {
			id : 11,
			text : '侯总',
			orderNum : 1,
		}, {
			id : 12,
			text : '翟总',
			orderNum : 2,
		}, ],
		orderNum : 1
	}, {
		id : 2,
		text : '综合办公室',
		children : [ {
			id : 21,
			text : '张跃',
			orderNum : 1,
		}, {
			id : 22,
			text : '赵青娟',
			orderNum : 2,
		} ],
		orderNum : 2,
	}

	];
	
	contact_reset();
	
	$('input[name="menuId"]').val(lnyw.tab_options().id);

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

	var K = KindEditor;
	var memo;
	window.memo = K
		.create(
				'#memo',
				{
					items : editor_items,
					uploadJson : '${pageContext.request.contextPath}/js/kindeditor/upload_json.jsp',
				//allowImageRemote : false,
				});

	memo.__proto__.html('');
	

	function showContacts() {
		var dialog = $('#message_contact_select');
		dialog
				.dialog({
					title : '选择收件人',
					href : '${pageContext.request.contextPath}/oa/selectContact.jsp',
					width : 550,
					height : 400,
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
						var source = $('#contact_from');
						var target = $('#contact_to');
						
						source.tree({
							data : contact_from,
							onDblClick : function(node) {
								contactTrans(node, source, target,
										contact_from, contact_to);
							},
						});
						target.tree({
							data : contact_to,
							onDblClick : function(node) {
								contactTrans(node, target, source, contact_to,
										contact_from);
							},
						});
					},

				});
	}

	function contactTrans(node, source, target, array_from, array_to) {
		if (source.tree('isLeaf', node.target)) {
			var par = [ {
				id : source.tree('getParent', node.target).id,
				text : source.tree('getParent', node.target).text,
				children : [ {
					id : node.id,
					text : node.text,
				} ],
			} ];
			target.tree({
				data : trans(par, array_to),
			});
			for (var i = 0, len = array_from.length; i < len; i++) {
				if (array_from[i].id == source.tree('getParent', node.target).id) {
					for (var j = 0, lenc = array_from[i].children.length; j < lenc; j++) {
						if (array_from[i].children[j].id == node.id) {
							array_from[i].children.splice(j, 1);
							if (array_from[i].children.length == 0) {
								array_from.splice(i, 1);
								source.tree('remove', source.tree('getParent',
										node.target).target);
							}
							source.tree('remove', node.target);
							break;
						}
					}
					break;
				}
			}
		} else {
			for (var i = 0, len = array_from.length; i < len; i++) {
				if (array_from[i].id == node.id) {
					target.tree({
						data : trans(array_from.splice(i, 1), array_to)
					});
					break;
				}
			}
			source.tree('remove', node.target);
		}
	}

	function trans(node, target) {
		if (target.length > 0) {
			for (var i = 0, len = target.length; i < len; i++) {
				if (node[0].id == target[i].id) {
					target[i].children.push(node[0].children[0]);
					return target;
				}
			}
		}
		target.push(node[0]);
		return target;
	}

	function message_submit() {
		var form = $('#message_send');
		form.form('submit', {
			url : '${pageContext.request.contextPath}/oa/messageAction!add.action',
			onSubmit : function() {
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
				$('#memo').val(memo.html());
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
	
	function message_reset(){
		$('input.cont').val('');
		memo.html('');
		contact_reset();
	}
	
	function contact_reset(){
		contact_from = [];
		for(var i = 0; i < contact_source.length; i++){
			//contact_from[contact_from.length] = Extend({}, obj);
			contact_from[contact_from.length] = $.extend(true, {}, contact_source[i]);
		}
		contact_to = [];
	}
	
</script>
<div id="oa_message_tabs" class="easyui-tabs"
	data-options="fit:true, border:false,"
	style="width: 100%; height: 100%;">

	<div title="新增记录" data-options="closable:false">
		<form id="message_send" method="post">
			<div>
				<span class="input_label"><a href="javascript:void(0)"
					onclick="showContacts()">收件人</a></span>
					<input class="cont" type="text" readOnly="readOnly"
					name="receiverNames" id="receiverNames"></input>
				<input type="hidden" class="cont" name="receiverIds" id="receiverIds"></input>
			</div>
			<div>
				<span class="input_label">主题</span> <input class="cont"
					type="text" name="subject" id="subject" data-options="required:true"></input>
			</div>
			<div>
				<span class="input_label">内容</span>
				<input class="cont" name="memo" id="memo" style="width: 700px; height: 300px;"></input>
			</div>
			<input type='hidden' name='menuId' />
		</form>
		<input type="button" value="提交" onclick="message_submit()"></input>
		<input type="button" value="重置" onclick="message_reset()"></input>
	</div>
	<div title="已发送列表" data-options="closable:false">
		<div id='oa_messageS_dg'></div>
	</div>
	<div title="接收列表" data-options="closable:false">
		<div id='oa_messageR_dg'></div>
	</div>
</div>
<div id="message_contact_select"></div>


