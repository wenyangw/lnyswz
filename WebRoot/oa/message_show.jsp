<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<div align="center">
	<form id="oa_message_show" method="post">
		<table>
	<tr>
		<th><label for="subject">主题</label></th>
		<td><input name="subject" style="width: 220px;"><span id="subject" style="width: 220px;">Hello</span>
		</td>
	</tr>
	<tr>
		<th><label for="receivers">收件人</label></th>
		<td><input name="receivers" style="width: 224px;"></td>
	</tr>
	<tr>
		<th><label for="createTime">时间</label></th>
		<td><input name="createTime" style="width: 224px;"><span id="createTime" style="width: 220px;">Hello</span></td>
	</tr>
	<tr>
		<th><label for="memo">内容</label></th>
		<td><input class="easyui-textbox" name="memo" multiline="true" style="width: 224px;"></td>
	</tr>

	</table>
	</form>
</div>
