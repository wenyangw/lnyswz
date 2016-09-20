<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<script type="text/javascript">
$(function(){

	
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
				var f = dialog.find('form');
				f.form('submit', {
					url : '${pageContext.request.contextPath}/admin/userAction!edit.action',
					onSubmit:function(){},
					success : function(d) {
						var json = $.parseJSON(jxc.toJson(d));
						if (json.success) {
							user_dg.datagrid('unselectAll');
							user_dg.datagrid('reload');
							dialog.dialog('close');
						}
						$.messager.show({
							msg : json.msg,
							title : '提示'
						});
					}
				});
			}
		}],
		onLoad : function(){
			var contact_from = [
			    {
	          		id: 1, 
	          		text:'经理室',
	          		children: [
	          		    {
	          			id:11, 
	          			text:'侯总'
	          			}, 
	          			{
	          			id:12, 
	          			text:'翟总'
	          			},
	          			],
	          		},
	          	{
	          		id:2, 
	          		text:'综合办公室', 
	          		children: [{
	          			id: 21,
	          			text: '张跃'
	          			},
	          			{
	          				id: 22,
	          				text: '赵青娟'
	          			}],}
	        ];
	        var contact_to = [];
			var source = $('#contact_from');
			var target = $('#contact_to');
			source.tree({
				data:contact_from,
				onDblClick: function(node){
					contactTrans(node, source, target, contact_from, contact_to);
					console.info('from');
					console.info(source);
					console.info('to');
					console.info(target);
				},
			});
			target.tree({
				data:contact_to,
				onDblClick: function(node){
					contactTrans(node, target, source, contact_to, contact_from);
					console.info('from');
					console.info(target);
					console.info('to');
					console.info(source);
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
			trans(par, array_to);
			target.tree('reload');
// 			target.tree({
// 				data: trans(par, array_to),
// 			});
			for(var i = 0, len = array_from.length; i < len; i++){
				if(array_from[i].id == source.tree('getParent', node.target).id){
					for(var j = 0, lenc = array_from[i].children.length; j < lenc; j++){
						if(array_from[i].children[j].id == node.id){
							array_from[i].children.splice(j, 1);
							if(array_from[i].children.length == 0){
								array_from.splice(i, 1);
								source.tree('reload');
								//source.tree('remove', source.tree('getParent', node.target).target);
							}
							//source.tree('remove', node.target);
							source.tree('reload');
							break;
						}
					}
				break;
				}
			}
	}else{
		for(var i = 0, len = array_from.length; i < len; i++){
			if(array_from[i].id == node.id){
				trans(array_from.splice(i, 1), array_to);
				target.tree('reload');
// 				target.tree({
// 					data: trans(array_from.splice(i, 1), array_to)
// 				});
				break;
			}
		}
		source.tree('reload');
		//source.tree('remove', node.target);
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
			<span class="input_label"><a href="javascript:void(0)" class="easyui-linkbutton" onclick="showContacts()">收件人</a></span>
			<input class="easyui-textbox" type="text" name="receive_name" data-options="required:true"></input><a>...</a>
		</div>
		<div>
			<span class="input_label">主题</span>
			<input class="easyui-textbox" type="text" name="subject" data-options="required:true"></input>
		</div>
		<div>
			<span class="input_label">内容</span>
			<input class="easyui-textbox" type="text" name="memo" data-options="required:true,multiline:true" style="height:60px"></input>
		</div>
		<div>
			<span class="input_label">附件</span>
			<input class="easyui-textbox" type="text" name="name" data-options="required:true"></input>
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

	
