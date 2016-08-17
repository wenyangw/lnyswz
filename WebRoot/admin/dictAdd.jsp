<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<div align="center">
	<form id="admin_dictAdd_form" method="post">
	<table>
		<tr>
			<th><label for="ename">英文名称</label></th>
			<td><input name="ename" class="easyui-validatebox"
				data-options="required:true,missingMessage:'请填写名称'"
				style="width: 156px;"></td>
		</tr>
		<tr>
			<th><label for="cname">中文名称</label></th>
			<td><input name="cname" class="easyui-validatebox"
				data-options="required:true,missingMessage:'请填写中文名称'"
				style="width: 156px;"></td>
		</tr>
		<tr>
			<th><label for="tname">所属表名</label></th>
			<td><input name="tname" class="easyui-validatebox"
				style="width: 156px;"></td>
		</tr>
		<tr>
			<th><label for="genre">类型</label></th>
			<td><input name="genre" style="width: 160px;"/>					 
			</td>
		</tr>
		<tr>
			<th><label for=specials>特殊种类</label></th>
			<td><input name="specials" class="easyui-validatebox"
				style="width: 156px;"></td>
		</tr>
		<tr>
			<th><label for="show">提示信息</label></th>
			<td><input name="show" class="easyui-validatebox"
				style="width: 156px;"></td>
		</tr>
		<tr>
			<th><label for="specialValues">特殊值</label></th>
			<td><input name="specialValues" class="easyui-validatebox"
				style="width: 156px;"></td>
		</tr>
		<tr>
			<th><label for="orderNum">排序</label></th>
			<td><input name="orderNum" class="easyui-validatebox"
				style="width: 156px;"></td>
		</tr>
		<tr>
			<th><label for="isDepName">是否需要部门</label></th>
			<td><input type="checkbox" value="1" checked=true name="isDepName"></td>
		</tr>
		<tr>
			<th><label for="isShow">是否显示</label></th>
			<td><input type="checkbox" value="1" checked=true name="isShow"></td>
		</tr>
		<tr>
			<th><label for="frozen">是否固定</label></th>
			<td><input type="checkbox" value="1"  name="frozen"></td>
		</tr>
		<tr>
			<th><label for="display">是否查询</label></th>
			<td><input type="checkbox" checked=true value="1"  name="display"/> 
			</td>
		</tr>
		<tr>
			<th><label for="tree">分类</label></th>
			<td><input type="checkbox" value="1"  name="tree"></td>
		</tr>
		<tr>
			<th><label for="treeShow">分类显示</label></th>
			<td><input type="checkbox" value="1"  name="treeShow"></td>
		</tr>
		<tr>
			<th><label for="treeSql">是否分类查询</label></th>
			<td><input type="checkbox" value="1"  name="treeSql"></td>
		</tr>	
		<tr>
			<th><label for="orderByTree">分类排序条件</label></th>
			<td><input name="orderByTree" class="easyui-validatebox"
				style="width: 156px;"></td>
		</tr>	
		<tr>
			<th><label for="orderBy">排序条件</label></th>
			<td><input name="orderBy" class="easyui-validatebox"
				style="width: 156px;"></td>
		</tr>
		<tr>
			<th><label for="isHj">是否合计</label></th>
			<td><input type="checkbox" checked=true value="01"  name="isHj"/> 
			</td>
		</tr>
		<tr>
			<th><label for="sqlWhere">合计筛选</label></th>
			<td><input name="sqlWhere" class="easyui-validatebox"
				style="width: 156px;"></td>
		</tr>
		<tr>
			<th><label for="inGroupBy">求和</label></th>
			<td><input name="inGroupBy" class="easyui-validatebox"
				style="width: 156px;"></td>
		</tr>
		<tr>
			<th><label for="outGroupBy">合计求和</label></th>
			<td><input name="outGroupBy" class="easyui-validatebox"
				style="width: 156px;"></td>
		</tr>
	</table>
		<input name="depId" type="hidden">
		<input name="menuId" type="hidden">
	</form>
</div>
