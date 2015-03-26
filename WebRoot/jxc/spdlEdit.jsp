<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<div align="center">
	<form id="jxc_spdlEdit_form" method="post">
	<table>
		<tr>
			<th><label for="id">编号</label></th>
			<td><input name="id" class="easyui-validatebox"
				data-options="required:true,missingMessage:'请填写商品大类编号'"
				style="width: 156px;"></td>
		</tr>
		<tr>
			<th><label for="spdlmc">商品大类名称</label></th>
			<td><input name="spdlmc" class="easyui-validatebox"
				data-options="required:true,missingMessage:'请填写中文名称'"
				style="width: 156px;"></td>
		</tr>
		<tr>
			<th><label for="depIds">所属部门</label></th>
			<td><input name="depIds" style="width: 160px;"></td>
		</tr>
		
	</table>
	</form>
</div>
