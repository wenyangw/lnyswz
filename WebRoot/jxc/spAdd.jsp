<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<div align="center">
	<form id="jxc_spAdd_form" method="post">
	<table>
		<tr>
			<th><label for="spdwId">商品段位</label></th>
			<td><input id="spdwId" name="spdwId" readonly="readonly" style="width:156px;"></td>
		</tr>
		<tr>
			<th><label for="spdwmc">商品段位名称</label></th>
			<td><input id="spdwmc" name="spdwmc" readonly="readonly" style="width:156px;"></td>
		</tr>
		<tr>
			<th><label for="spbh">商品编号</label></th>
			<td><input id="spbh" name="spbh" class="easyui-validatebox"
				data-options="validType:['mustLength[7]','startWith[\'spdwId\',5,\'商品编码前5位必须与商品段位一致！\']']"
				style="width:156px;"></td>
		</tr>
		<tr>
			<th><label for="spmc">商品名称</label></th>
			<td><input name="spmc" style="width:156px;"></td>
		</tr>
		<tr>
			<th><label for="spcd">商品产地</label></th>
			<td><input name="spcd" class="easyui-validatebox"
				data-options="required:true,missingMessage:'请选择商品产地'"
				style="width:156px;"></td>
		</tr>
		<tr>
			<th><label for="sppp">商品品牌</label></th>
			<td><input name="sppp" class="easyui-validatebox"
				style="width:156px;"></td>
		</tr>
		<tr>
			<th><label for="spbz">商品包装</label></th>
			<td><input name="spbz" class="easyui-validatebox"
				style="width:156px;"></td>
		</tr>
		<tr>
			<th><label for="zjldwId">主计量单位</label></th>
			<td><input name="zjldwId" 
				style="width: 160px;"></td>
		</tr>
		<tr>
			<th><label for="cjldwId">次计量单位</label></th>
			<td><input name="cjldwId" style="width: 160px;"></td>
		</tr>
		<tr>
			<th><label for="zhxs">系数</label></th>
			<td><input name="zhxs" style="width: 156px;"></td>
		</tr>
		<tr>
			<th><label for="yxq">有效期</label></th>
			<td><input name="yxq" style="width: 156px;"></td>
		</tr>
	</table>
	<input name="depId" type="hidden">
	<input name="menuId" type="hidden">
	</form>
</div>
