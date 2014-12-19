<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<div align="center">
	<form id="jxc_spdwAdd_form" method="post">
	<table>
		<tr>
			<th><label for="splbId">商品类别</label></th>
			<td><input name="splbId" class="easyui-validatebox"
				data-options="required:true,missingMessage:'请选择商品大类'"
				style="width:160px;"></td>
		</tr>
		<tr id="fromTo_tr" style="display:none">
			<th>编号范围</th>
			<td><div id="fromTo" style="padding:5px"></div></td>
		</tr>
		<tr>
			<th><label for="id">商品段位编号</label></th>
			<td><input name="id" class="easyui-validatebox"
				data-options="required:true,missingMessage:'请填写商品段位编号'"
				style="width: 156px;"></td>
		</tr>
		<tr>
			<th><label for="spdwmc">商品段位名称</label></th>
			<td><input name="spdwmc" class="easyui-validatebox"
				data-options="required:true,missingMessage:'请填写商品类别名称'"
				style="width: 156px;"></td>
		</tr>
		
	</table>
	</form>
</div>
