<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>


<div style="margin-top: 20px;">
		<div class="message_line">
			<span class="field_label">主题：</span>
			<span class="field_value" id="subject" style="width: 220px;"></span>
		</div>
		<div class="send message_line">
			<span class="field_label">收件人：</span>
			<span class="field_value" id="receivers" style="width: 220px;"></span>
		</div>
		<div class="receive message_line">
			<span class="field_label">发件人：</span>
			<span class="field_value" id="sender" style="width: 220px;"></span>
		</div>
		<div class="message_line">
			<span class="field_label">时间：</span>
			<span class="field_value" id="createTime" style="width: 220px;"></span>
		</div>
		<div class="message_line">
			<span class="field_label">内容：</span>
			<div id="memo" style="margin-left:70px; width:1000px;min-width: 1000px; height: 500px; min-height: 500px; border-style:solid; border-width: thin;border-color: black;"></div>
		</div>
</div>
