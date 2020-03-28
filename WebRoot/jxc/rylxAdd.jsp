<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<div align="center">
	<form id="jxc_rylxAdd_form" method="post">
	<table>
		<tr>
			<th><label for="id">编号</label></th>
			<td><input name="id"class="easyui-validatebox"
					data-options="required:true,missingMessage:'请输入人员类型编号',
						validType:['mustLength[2]','floatOrInt']"style="width: 220px;"></td>
		</tr>
		<tr>
			<th><label for="rylxmc">人员类型名称</label></th>
			<td><input name="rylxmc" class="easyui-validatebox"
					data-options="required:true,missingMessage:'请填写人员类型名称'"
					style="width: 220px;"></td>
		</tr>		
	</table>
	<input name="depId" type="hidden">
	<input name="menuId" type="hidden">			
	</form>
</div>
