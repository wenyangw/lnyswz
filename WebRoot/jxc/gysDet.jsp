<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<div align="center">
	<form id="jxc_gysDet_form" method="post">
	<table>
		<tr>
			<th><label for="gysbh">供应商编号</label></th>
			<td><input id="gysbh" name="gysbh" readonly="readonly" style="width:160px;"></td>
		</tr>
		<tr>
			<th><label for="gysmc">商供应商名称</label></th>
			<td><input name="gysmc" readonly="readonly" style="width:156px;"></td>
		</tr>
		<tr>
			<th><label for="lxr">联系人</label></th>
			<td><input name="lxr" style="width:160px;"></td>
		</tr>
		<tr>
			<th><label for="sxzq">授信账期（天）</label></th>
			<td><input name="sxzq" class="easyui-validatebox"
			data-options="validType:'integer'"
				style="width:160px;"></td>
		</tr>
		<tr>
			<th><label for="sxje">授信金额（元）</label></th>
			<td><input name="sxje"class="easyui-validatebox"
			data-options="validType:'integer'"
				 style="width:160px;"></td>
		</tr>
	</table>
	<input type="hidden" name="detId">
	<input type="hidden" name="depId">
	<input name="menuId" type="hidden">
	</form>
</div>
