<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<script type="text/javascript">
$(function(){
		
});

var contact_from = [
        {
			id: 1, text:'经理室',
			children:
				[
				 {id:11, text:'侯总'}, 
				{id:12, text:'翟总'}
				 ],
		},
		{id:2, text:'综合办公室', 
		children:[],}
];
var contact_to = [];


function showContacts(){
	var dialog = $('#message_contact_select');
	dialog.dialog({
		title : '选择收件人',
		href : '${pageContext.request.contextPath}/oa/selectContact.jsp',
		width : 350,
		height : 380,
		buttons : [ {
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
		} ],
		onLoad : function(){
			var tt = $('#contact_from');
			tt.tree({
				data:contact_from,
				onDblClick: function(node){
					if(tt.tree('isLeaf', node.target)){
						var i = 0;
						for(; i < contact_from.length; i++){
							if(contact_from[i].id == tt.tree('getParent', node.target).id){
								console.info()
							}
						}
						;
						
					}else{
						contact_from[]
						node.id;
					}
					
					
					  // alert node text property when clicked
				},
			});
		}
		
	});
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

	
