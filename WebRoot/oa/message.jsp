<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<script type="text/javascript">
$(function(){
		
});


function showContacts(){
	alert('Hello');
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


	
