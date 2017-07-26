<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<script type="text/javascript">
$(function(){
	$.ajax({
		url : '${pageContext.request.contextPath}/oa/messageAction!receiveDg.action',
		data : {
		    page: 1,
			rows: 10
		},
		dataType : 'json',
		success : function(d) {
			console.info(d);
			//var json = $.parseJSON(d);
			var content = '';
			$.each(d.rows, function(){
				console.info(this.readTime);
			    var text = "<a href='javascript:void(0)' onclick='show_message(" + JSON.stringify(this) + ")'>" + moment(this.createTime).format('YYYY-MM-DD') + "&nbsp&nbsp" + this.subject + "&nbsp&nbsp" + this.createName + "</a>";
				content += "<li>";
				if(this.readTime){
					content += "<font color='blue'>" + text + "</font>";
				}else{
					content += "<font color='red'>" + text + "</font>";
				}
				content += "</li>";
			});
			$('div#message_list').html(content);
		}
	});
});
function show_message(data){
	$.ajax({
		url : '${pageContext.request.contextPath}/oa/messageAction!getMessage.action',
		data : {
			id : data.id,
			source: 'receive'
		},
		dataType : 'json',
		success : function(d) {
			data.memo = d.obj.memo;
			var message_dialog = $("<div></div>").dialog({
				title: '查看信息',
				width: 1050,
				height: 800,
				closed: false,
				cache: false,
				href: '${pageContext.request.contextPath}/oa/message_show.jsp',
				modal: true,
				onLoad: function(){
					$('span#subject').text(data.subject);
					$('span#createTime').text(data.createTime);
					$('span#sender').text(data.createName);
					$('div.send').css("display", "none");

					$.ajax({
						url : '${pageContext.request.contextPath}/oa/paperAction!getPapers.action',
						data : {
							messageId : data.id,
							type: 'insertfile'
						},
						dataType : 'json',
						success : function(d) {
						    console.info(d.obj.rows);
							if(d.obj.rows){
								var papers = [];
								for(var i = 0; i < d.obj.rows.length; i++){
									papers.push("<a href='${pageContext.request.contextPath}/oa/paperAction!downloadFile.action?filename=" + d.obj.rows[i].filename + "&filepath=" + d.obj.rows[i].filepath + "'>" + d.obj.rows[i].filename + "</a>");
								}

                                $('div#attached').html(papers.join("<br/>"));
							}else{
                                $('div.attached').css("display", "none");
                            }
						}
					});

					$('div#memo').html(data.memo);
				}
			});
		}
	});
}
</script>

<div id="message_list"></div>

