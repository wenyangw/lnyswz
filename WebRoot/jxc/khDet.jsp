<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<div align="center">
	<form id="jxc_khDet_form" method="post">
	<table>
		<tr>
			<th><label for="khbh">客户编号</label></th>
			<td><input id="khbh" name="khbh" readonly="readonly" style="width:156px;"></td>
		</tr>
		<tr>
			<th><label for="khmc">客户名称</label></th>
			<td><input name="khmc" readonly="readonly" style="width:156px;"></td>
		</tr>
		<tr>
			<th><label for="lxr">联系人</label></th>
			<td><input name="lxr" style="width:156px;"></td>
		</tr>
		<tr>
			<th><label for="ywyId">业务员</label></th>
			<td><input name="ywyId" style="width:160px;"></td>
		</tr>
		<tr>
			<th><label for="khlxId">客户类型</label></th>
			<td><input name="khlxId" style="width:160px;"></td>
		</tr>
<!-- 		<tr> -->
<!-- 			<th><label for="isSx">授信客户</label></th> -->
<!-- 			<td><input name="isSx" type="checkbox" value="1"></td> -->
<!-- 		</tr> -->
		<tr>
			<th><label for="sxzq">授信账期</label></th>
			<td><input name="sxzq" id="sxzq" class="easyui-validatebox"
				data-options="validType:'integer'" 
				style="width:156px;"
				disabled="disabled"></td>
		</tr>
		<tr>
			<th><label for="sxje">授信金额</label></th>
			<td><input name="sxje" class="easyui-validatebox"
				data-options="validType:'floatOrInt'"
				style="width:156px;"
				disabled="disabled"></td>
		</tr>
		<tr>
			<th><label for="lsje">历史金额</label></th>
			<td><input name="lsje" class="easyui-validatebox"
				data-options="validType:'floatOrInt'"
				style="width:156px;"
				disabled="disabled"></td>
		</tr>
	</table>
<!-- 	<input type="hidden" name="id"> -->
	<input type="hidden" name="detId">
	<input type="hidden" name="depId" >
	<input type="hidden" name="menuId" >	
	</form>
</div>
