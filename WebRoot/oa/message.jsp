<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/kindeditor/themes/default/default.css"  />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/kindeditor/kindeditor-all.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/kindeditor/lang/zh-CN.js"></script>

<script type="text/javascript">

var contact_from;
var contact_to;

$(function(){
	contact_from = [
				    {
		          		id: 1, 
		          		text:'经理室',
		          		children: [
		          		    {
		          			id:11, 
		          			text:'侯总',
		          			orderNum: 1,
		          			}, 
		          			{
		          			id:12, 
		          			text:'翟总',
		          			orderNum: 2,
		          			},
		          			],
		          		orderNum: 1
		          	},
		          	{
		          		id:2, 
		          		text:'综合办公室', 
		          		children: [{
		          				id: 21,
		          				text: '张跃',
		          				orderNum: 1,
		          				
		          			},
		          			{
		          				id: 22,
		          				text: '赵青娟',
		          				orderNum: 2,
		          			}],
		          			orderNum: 2,
		          	}
		          			
		        ];

	contact_to = [];
	
	
	 
	var editor_items = [
		'source', '|', 'undo', 'redo', '|', 'preview', 
		//'print', 'template', 'code', 
		'cut', 'copy', 'paste', 'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
	    'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
	    'superscript', 'clearhtml', 'quickformat', 'selectall', '|', 
	    //'fullscreen', '/',
	    'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
	    'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|', 'image', 
	    //'multiimage', 'flash', 'media', 
	    'insertfile', 'table', 'hr', 
	    //'emoticons', 'baidumap', 'pagebreak', 'anchor', 
	    'link', 'unlink', '|', 'about'
	    ];
    
	KindEditor.create('#editorc',{
   		items: editor_items,
   		uploadJson : '${pageContext.request.contextPath}/js/kindeditor/upload_json.jsp',
   		//allowImageRemote: false,
   	});
    
   	
//    	KindEditor('input[name=readonly]').click(function(){
//    		alert('click!!!');
//    	});
   	
   	KindEditor.instances[0].html("");

});




function showContacts(){
	var dialog = $('#message_contact_select');
	dialog.dialog({
		title : '选择收件人',
		href : '${pageContext.request.contextPath}/oa/selectContact.jsp',
		width : 350,
		height : 380,
		buttons : [{
			text : '确定',
			handler : function() {
				var names = [];
				var ids = [];
				
				
				for(var i = 0, len = contact_to.length; i < len; i++){
					for(var j = 0, lenc = contact_to[i].children.length; j < lenc; j++){
						ids.push(contact_to[i].children[j].id);
						names.push(contact_to[i].children[j].text);
					}
				}
				
				$('input[name=receive_id]').val(ids.join(','));
				$('input[name=receive_name]').val(names.join(','));
				dialog.dialog('close');
			}
		}],
		onLoad : function(){
			var source = $('#contact_from');
			var target = $('#contact_to');

			source.tree({
				data:contact_from,
				onDblClick: function(node){
					contactTrans(node, source, target, contact_from, contact_to);
				},
			});
			target.tree({
				data:contact_to,
				onDblClick: function(node){
					contactTrans(node, target, source, contact_to, contact_from);
				},
			});
		},
		
	});
}


function contactTrans(node, source, target, array_from, array_to){
	if(source.tree('isLeaf', node.target)){
		var par = [{
			id: source.tree('getParent', node.target).id,
				text: source.tree('getParent', node.target).text,
				children: [{
					id: node.id,
					text: node.text,
					}],
			}];
			target.tree({
				data: trans(par, array_to),
			});
			for(var i = 0, len = array_from.length; i < len; i++){
				if(array_from[i].id == source.tree('getParent', node.target).id){
					for(var j = 0, lenc = array_from[i].children.length; j < lenc; j++){
						if(array_from[i].children[j].id == node.id){
							array_from[i].children.splice(j, 1);
							if(array_from[i].children.length == 0){
								array_from.splice(i, 1);
								source.tree('remove', source.tree('getParent', node.target).target);
							}
							source.tree('remove', node.target);
							break;
						}
					}
				break;
				}
			}
	}else{
		for(var i = 0, len = array_from.length; i < len; i++){
			if(array_from[i].id == node.id){
				target.tree({
					data: trans(array_from.splice(i, 1), array_to)
				});
				break;
			}
		}
		source.tree('remove', node.target);
	}
}

function trans(node, target){
	if(target.length > 0){
		for(var i = 0, len = target.length; i < len; i++){
			if(node[0].id == target[i].id){
				target[i].children.push(node[0].children[0]);
				return target;
			}
		}
	}
	target.push(node[0]);
	return target;
}

</script>
<div id="oa_message_tabs" class="easyui-tabs" data-options="fit:true, border:false," style="width:100%;height:100%;">
	
    <div title="新增记录" data-options="closable:false">
		
			<div>
				<span class="input_label"><a href="javascript:void(0)" onclick="showContacts()">收件人</a></span>
				<input class="easyui-textbox" type="text" name="receive_name" data-options="required:true"></input>
				<input type="hidden" name="receive_id"></input>
			</div>
			<div>
				<span class="input_label">主题</span>
				<input class="easyui-textbox" type="text" name="subject" data-options="required:true"></input>
			</div>
			<div>
				<span class="input_label">内容</span>
				<textarea name="editorc" id="editorc"  style="width:700px;height:300px;"></textarea>
			</div>
			
		
	</div>
    <div title="已发送列表" data-options="closable:false" >
    	<div id='oa_messageS_dg'></div>
    </div>
	<div title="接收列表" data-options="closable:false" >
		<div id='oa_messageR_dg'></div>
	</div>
</div>
<div id="message_contact_select"></div>

	
