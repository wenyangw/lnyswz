<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<div align="center">
	<form id="jxc_khAdd_form" method="post">
	<table>
		<tr>
			<th><label for="khbh">客户编号</label></th>
			<td><input name="khbh" class="easyui-validatebox" id="khbh"
				data-options="required:true,missingMessage:'请输入客户编号',validType:['mustLength[8]','integer']"
				style="width: 220px;"></td>
		</tr>
		<tr>
			<th><label for="khmc">客户名称</label></th>
			<td><input name="khmc" class="easyui-validatebox"
				data-options="required:true,missingMessage:'请输入客户名称',validType:'chinese'"
				style="width: 220px;"></td>
		</tr>
		<tr>
			<th><label for="fr">法人</label></th>
			<td><input name="fr" style="width: 220px;"></td>
		</tr>
		<tr>
			<th><label for="dzdh">地址/电话</label></th>
			<td><input name="dzdh" style="width: 220px;"></td>
		</tr>
		<tr>
			<th><label for="address">送货地址</label></th>
			<td><input name="address" style="width: 220px;"></td>
		</tr>
		<tr>
			<th><label for="isNsr">一般纳税人</label></th>
			<td><input name="isNsr" type="checkbox" value="1"></td>
		</tr>
		<tr>
			<th><label for="sh">税号</label></th>
			<td><input name="sh" class="easyui-validatebox"
				data-options="validType:['length[15, 20]','startWith[\'khbh\',4,\'税号前4位必须与客户编号前4位一致！\']']"
				style="width: 220px;"
				disabled="disabled"></td>
		</tr>
		<tr>
			<th><label for="khh">开户行</label></th>
			<td><input name="khh" style="width: 220px;" disabled="disabled"></td>
		</tr>
		
	</table>
		<input name="depId" type="hidden">
		<input name="menuId" type="hidden">	
	</form>
</div>
