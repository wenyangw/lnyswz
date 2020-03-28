<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<div align="center">
	<form id="jxc_fhAdd_form" method="post">
	<table>
	<tr>
		<th><label for="id">编号</label></th>
		<td><input name="id"class="easyui-validatebox"
				data-options="required:true, missingMessage:'请输入分户编号',validType:['mustLength[2]','floatOrInt']" style="width: 220px;"></td>
	</tr>
	<tr>
		<th><label for="fhmc">分户名称</label></th>
		<td><input name="fhmc" class="easyui-validatebox"
				data-options="required:true,missingMessage:'请填写分库名称'"
				style="width: 220px;"></td>
	</tr>
	<tr>
		<th><label for="depId">部门</label></th>
		<td><input name="depId" style="width: 224px;"></td>
	</tr>
	</table>
	</form>
</div>
