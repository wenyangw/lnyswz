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
	
	$('#pp').portal({
		border:false,
	});
	
	$('#updateLog').panel({
		border: false,
	    href: '${pageContext.request.contextPath}/admin/updateLogAction!listUpdateLog.action',
	    onLoad: function(){
	    	var data = $(this).html();
	    	$(this).html('');
	    	var json = $.parseJSON(data);
	    	var content = '';
	    	$.each(json,function(){
	    		var text = this.updateDate + "&nbsp&nbsp" + this.contents;
	    		content += "<li>";
	    		if(moment().diff(this.updateDate, 'days') <= 20){
		    		content += "<font color='red'>" + text + "</font>";
	    		}else{
		    		content += text;
	    		}
	    		content += "</li>";
			});
	    	$(this).html(content);
	    },
	});
	
	$('#message_panel').panel({
		border: false,
 	    href: '${pageContext.request.contextPath}/oa/message_panel.jsp'
	});

});

</script>

<%--<style type="text/css">--%>
	<%--a:link, a:visited, a:hover, a:active {color: #0000FF}--%>
<%--</style>--%>

<div id='layout_center_tabs' >
	<div title="首页">
<!-- 		<div id="pp" style="position:fixed"> -->
		<div id="pp" style="width:800px;height:500px;">
			<div style="width:50%;">
				<div title="更新日志" collapsible="true" closable="true" style="width:500px;height:300px; padding:15px;">
			    	<div id="updateLog"></div>
			    </div>
			</div>
			<div style="width:50%;">
				<div title="通知" style="width:500px;height:300px; padding:15px;" data-options="columnIndex: 0, tools: [{
					iconCls:'icon-reload',
					handler:function(){
						$('#message_panel').panel('refresh');
					}
				}]">
			    	<div id="message_panel">
			    	</div>
			    </div>
			</div>
		</div>
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
<div class="datagrid-mask"></div>  
<div class="datagrid-mask-msg"></div>
