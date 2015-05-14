<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<div align="center">
	<form id="jxc_splbAdd_form" method="post">
	<table>
		<tr>
			<th><label for="splbmc">商品类别名称</label></th>
			<td><input name="splbmc" class="easyui-validatebox"
				data-options="required:true,missingMessage:'请填写商品类别名称'"
				style="width: 156px;"></td>
		</tr>
		<tr>
			<th><label for="idFrom">编号范围从</label></th>
			<td><input name="idFrom" class="easyui-validatebox"
				data-options="required:true,missingMessage:'请填写范围起始编号'" style="width: 156px;"></td>
		</tr>
		<tr>
			<th><label for="idTo">到</label></th>
			<td><input name="idTo" class="easyui-validatebox"
				data-options="required:true,missingMessage:'请填写范围结束编号'" style="width: 156px;"></td>
		</tr>
		<tr>
			<th><label for="spdlId">商品大类</label></th>
			<td><input name="spdlId" class="easyui-validatebox"
				data-options="required:true,missingMessage:'请选择商品大类'"
				style="width:160px;"></td>
		</tr>
	</table>
	</form>
</div>
