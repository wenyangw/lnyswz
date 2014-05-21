<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<div align="center">
	<form id="jxc_ckAdd_form" method="post">
	<table>
		<tr>
			<th><label for="id">编号</label></th>
			<td><input name="id"readonly="readonly"style="width: 220px;"></td>
		</tr>
		<tr>
			<th><label for="ckmc">仓库名称</label></th>
			<td><input name="ckmc" class="easyui-validatebox"
				data-options="required:true,missingMessage:'请填写仓库名称'"
				style="width: 220px;"></td>
		</tr>
		<tr>
			<th><label for="did">所属部门</label></th>
			<td><input name="did" style="width: 224px;"></td>	
		</tr>	
	</table>
	<input type="hidden" name="depId">
	<input name="menuId" type="hidden">
	</form>
</div>
