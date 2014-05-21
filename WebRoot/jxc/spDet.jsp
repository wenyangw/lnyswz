<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<div align="center">
	<form id="jxc_spDet_form" method="post">
	<table>
		<tr>
			<th><label for="spbh">商品编号</label></th>
			<td><input id="spbh" name="spbh" readonly="readonly" style="width:160px;"></td>
		</tr>
		<tr>
			<th><label for="spmc">商品名称</label></th>
			<td><input name="spmc" readonly="readonly" style="width:156px;"></td>
		</tr>
		<tr>
			<th><label for="xsdj">销售单价</label></th>
			<td><input name="xsdj" style="width:160px;"></td>
		</tr>
		<tr>
			<th><label for="limitXsdj">最低销价</label></th>
			<td><input name="limitXsdj" style="width:160px;"></td>
		</tr>
		<tr>
			<th><label for="maxKc">最大库存</label></th>
			<td><input name="maxKc" style="width:160px;"></td>
		</tr>
		<tr>
			<th><label for="minKc">最小库存</label></th>
			<td><input name="minKc" style="width:160px;"></td>
		</tr>
	</table>
	<input type="hidden" name="detId">
	<input type="hidden" name="depId">
	<input type="hidden" name="menuId">
	</form>
</div>
