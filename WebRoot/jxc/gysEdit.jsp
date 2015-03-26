<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<div align="center">
	<form id="jxc_gysAdd_form" method="post">
	<table>
		<tr>
			<th><label for="gysbh">供应商编号</label></th>
			<td><input name="gysbh" class="easyui-validatebox" id="gysbh"
				data-options="validType:['mustLength[8]']"readonly="true"
				style="width: 220px;"></td>
		</tr>		
		<tr>
			<th><label for="gysmc">供应商名称</label></th>
			<td><input name="gysmc" class="easyui-validatebox"
				data-options="required:true,missingMessage:'请输入供应商名称',validType:'chinese'"
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
				data-options="validType:['mustLength[15]','startWith[\'gysbh\',4,\'税号前4位必须与供应商编号前4位一致！\']']"
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
