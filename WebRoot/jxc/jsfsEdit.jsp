<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<div align="center">
	<form id="jxc_jsfsEdit_form" method="post">
	<table>
		<tr>
			<th><label for="id">编号</label></th>
			<td><input name="id"readonly="true" style="width: 220px;"></td>
		</tr>
		<tr>
			<th><label for="jsmc">结算方式名称</label></th>
			<td><input name="jsmc" class="easyui-validatebox"
				data-options="required:true,missingMessage:'请填写计量单位名称'"
				style="width: 220px;"></td>
		</tr>
	</table>
	<input name="depId" type="hidden">
	<input name="menuId" type="hidden">		
	</form>
</div>
