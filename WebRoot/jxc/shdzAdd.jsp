<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<div align="center">
	<form id="jxc_shdzAdd_form" method="post">
	<table>
		<tr>
			<th><label for="khmc">客户名称</label></th>
			<td><input name="khmc" class="easyui-validatebox"
					data-options="required:true,missingMessage:'请填写客户名称名称'"
					style="width: 220px;"></td>
		</tr>		
		<tr>
			<th><label for="khdz">客户地址</label></th>
			<td><input name="khdz" class="easyui-validatebox"
					data-options="required:true,missingMessage:'请填写客户地址'"
					style="width: 220px;"></td>
		</tr>		
		<tr>
			<th><label for="lxr">联系人</label></th>
			<td><input name="lxr" class="easyui-validatebox"
					data-options="required:true,missingMessage:'请填写联系人'"
					style="width: 220px;"></td>
		</tr>		
		<tr>
			<th><label for="phone">电话</label></th>
			<td><input name="phone" class="easyui-validatebox"
					data-options="required:true,missingMessage:'请填写电话号码'"
					style="width: 220px;"></td>
		</tr>
		<tr>
			<th><label for="orderNum">排序</label></th>
			<td><input name="orderNum" class="easyui-numberspinner" style="width:160px;"
        		required="required" data-options="value:1,min:1,max:99,editable:false"></td>
		</tr>		
			
	</table>
	<input name="depId" type="hidden">
	<input name="menuId" type="hidden">			
	</form>
</div>
