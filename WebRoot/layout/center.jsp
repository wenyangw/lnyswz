<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<script type="text/javascript">

$(function(){
	var centerTabs = $('#layout_center_tabs').tabs({
		fit:true,
		border:false,
		tools:'#tab-tools',
	});
	var allTabs = centerTabs.tabs('tabs');
	var closeTabsTitle = [];

	closeAll = function(){
		$.each(allTabs, function() {
	
			var opt = $(this).panel('options');
			if (opt.closable) {
				closeTabsTitle.push(opt.title);
			}
		});

		for (var i = 0; i < closeTabsTitle.length; i++) {
			centerTabs.tabs('close', closeTabsTitle[i]);
		}
	};
	
});
	
</script>    


<div id='layout_center_tabs' >
	<div title="首页">
		<iframe src="${pageContext.request.contextPath}/admin/updateLog.jsp" frameborder="0" style="border: 0; width: 100%; height: 98%;"></iframe>
	</div>
</div>
<div id="tab-tools">
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-cancel',border:false" onclick="closeAll()"></a>
</div>
<div id="jxc_spQuery"></div>	
<div id="jxc_query_dialog"></div>
<div id="jxc_queryAddr_dialog"></div>
<div id="printDialog"></div>
<div id="fileDialog"></div>
