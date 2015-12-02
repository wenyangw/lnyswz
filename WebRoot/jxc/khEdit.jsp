<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<div align="center">
	<form id="jxc_khAdd_form" method="post">
	<table>
		<tr>
			<th><label for="khbh">客户编号</label></th>
			<td><input name="khbh" class="easyui-validatebox" id="khbh"
				data-options="validType:['mustLength[8]']" readonly="readonly"
				style="width: 220px;"></td>
		</tr>
		
		<tr>
			<th><label for="khmc">客户名称</label></th>
			<td><input name="khmc" class="easyui-validatebox"
				data-options="required:true,missingMessage:'请输入客户名称',validType:'chinese'"
				style="width: 220px;"></td>
		</tr>
		<tr>
			<th><label for="dzdh">地址/电话</label></th>
			<td><input name="dzdh"style="width: 220px;"></td>
		</tr>
		<tr>
			<th><label for="khh">开户行</label></th>
			<td><input name="khh"style="width: 220px;"></td>
		</tr>
		<tr>
			<th><label for="sh">税号</label></th>
			<td><input name="sh" class="easyui-validatebox"
				data-options="validType:['mustLength[8]']"
				style="width: 220px;" ></td>
		</tr>
		<tr>
			<th><label for="fr">法人</label></th>
			<td><input name="fr" style="width: 220px;"></td>
		</tr>
		
	</table>
		<input name="depId" type="hidden">
		<input name="menuId" type="hidden">	
	</form>
</div>
