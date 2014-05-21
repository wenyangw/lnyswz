<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<div align="center">
	<form id="jxc_hwAdd_form" method="post">
	<table>
		<tr>
			<th><label for="id">编号</label></th>
			<td><input name="id"class="easyui-validatebox"
				data-options="required:true,missingMessage:'请输入货位编号',
				validType:['mustLength[2]','floatOrInt']"style="width: 220px;"></td>
		</tr>
		<tr>
			<th><label for="hwmc">货位名称</label></th>
			<td><input name="hwmc" class="easyui-validatebox"
				data-options="required:true,missingMessage:'请填写货位名称'"
				style="width: 220px;"></td>
		</tr>
		<tr>
			<th><label for="ckId">所属仓库</label></th>
			<td><input name="ckId" style="width: 224px;"></td>
		</tr>		
	</table>
	<input type="hidden" name="depId">
	<input name="menuId" type="hidden">	
	</form>
</div>
