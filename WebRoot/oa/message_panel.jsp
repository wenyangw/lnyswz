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
			var content = '';
			$.each(d.rows, function(){
				content += "<li>";
				var read_class;
				if(this.readTime){
                    read_class = 'readed_list';
				}else{
                    read_class = 'unread_list';
				}
                content += "<a href='javascript:void(0)' class='" + read_class + "' onclick='show_message(" + JSON.stringify(this) + ")'>" + moment(this.createTime).format('YYYY-MM-DD') + "&nbsp&nbsp" + this.subject + "&nbsp&nbsp" + this.createName + "</a>";
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
                width: $(window).width() * 0.66,
                height: $(window).height() * 0.9,
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

