<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/plugins/kindeditor-4.1.11/themes/default/default.css" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/plugins/kindeditor-4.1.11/kindeditor-all-min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/plugins/kindeditor-4.1.11/lang/zh-CN.js"></script>


<script type="text/javascript">

	var contact_source;
	var contact_from;
	var contact_to;
	
	var source;
	var target;

	var memoEditor;
	
	$(function(){
		//装载联系人
		$.ajax({
			url : '${pageContext.request.contextPath}/admin/userAction!getContacts.action',
			dataType : 'json',
			success : function(d) {
				contact_source = d;
				array_sort(contact_source);
				contact_reset();
			}
		});
		
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
	
 		//var K = KindEditor;
		$('#oa_message_tabs').tabs({
			onSelect: function(title, index){
				if(index == 0){
					memoEditor = KindEditor
	 				.create(
	 					//'textarea[name="memo"]',
	 					'#memo_editor',
	 					{
	 						items : editor_items,
	 						uploadJson : '${pageContext.request.contextPath}/js/kindeditor/upload_json.jsp',
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
		dialog
				.dialog({
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
						source = $('#contact_from');
						target = $('#contact_to');
						
						source.tree({
							data : contact_from,
							onDblClick : function(node) {
								contactTrans(node, source, target, contact_from, contact_to);
							},
						});
						target.tree({
							data : contact_to,
							onDblClick : function(node) {
								contactTrans(node, target, source, contact_to, contact_from);
							},
						});
					},

				});
	}
	
	//更新后，排序、刷新树
	function reload_tree(){
		array_sort(contact_from);
		array_sort(contact_to);
		
		source.tree('loadData', contact_from);
		target.tree('loadData', contact_to);
	}
	
	//全部命中
	function trans_toR(){
		trans_all(source, target, contact_from, contact_to);
	}
	
	//全部移除
	function trans_toL(){
		trans_all(target, source, contact_to, contact_from);
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
				
				$('#memo_editor').val(memoEditor.html());
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
		$('input.cont').val('');
		memoEditor.html('');
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

	//-------------------------------------发送列表管理
	
	var message_sendDg = $('#oa_messageS_dg');
	message_sendDg.datagrid({
		url:'${pageContext.request.contextPath}/oa/messageAction!sendDg.action',
		fit : true,
		singleSelect:true,
	    border : false,
	    pagination : true,
		pagePosition : 'bottom',
		pageSize : pageSize,
		pageList : pageList,
	    columns:[[    	
	        {field:'id',title:'编号',width:100},	       
	        {field:'createTime',title:'时间',width:100},
	        {field:'subject',title:'主题',width:100,},
	    ]],
	});
	//根据权限，动态加载功能按钮
	lnyw.toolbar(1, message_sendDg, '${pageContext.request.contextPath}/admin/buttonAction!buttons.action', lnyw.tab_options().did);
	
	//-------------------------------------接收列表管理
	
	
	//-------------------------------------信息显示
	function addTab(){
		$('#oa_message_tabs').tabs('add',{
			title: 'new tab',
			selected: true,
			closable: true,
			href: '${pageContext.request.contextPath}/oa/message_show.jsp',
		});
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
				<textarea name="memo" id="memo_editor" class="cont" style="width:800px;height:400px;"></textarea>
			</div>
			<input type='hidden' name='menuId' />
	 	</form>
		<input type="button" value="提交" onclick="message_submit()"></input>
		<input type="button" value="重置" onclick="message_reset()"></input>
		<input type="button" value="add" onclick="addTab()"></input>
	</div>
	<div title="已发送列表" data-options="closable:false">
		<div id='oa_messageS_dg'></div>
	</div>
	<div title="接收列表" data-options="closable:false">
		<div id='oa_messageR_dg'></div>
	</div>
</div>
<div id="message_contact_select"></div>

