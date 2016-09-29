<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<div id="message_contact" class="easyui-layout" style="width:500px;height:400px;">
    <div data-options="region:'west',title:'通讯录',border:false, split:false, collapsible:false" style="width:200px;">
    	<ul id="contact_from"></ul>
    </div>
    <div data-options="region:'center'," style="padding:5px;background:#eee;">
    	<input type="button" value="全部>>" onclick="trans_toR()"></input>
    	<input type="button" value="<<全部" onclick="trans_toL()"></input>
    </div>
    <div data-options="region:'east',title:'收件人',border:false,split:false, collapsible:false" style="width:200px;">
    	<ul id="contact_to"></ul>
    </div>
</div>


