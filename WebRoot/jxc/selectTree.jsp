<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<script type="text/javascript">
//部门
var did;
//查询试图
var query;
//字典查询数据
var dictData;  
//选择指定<div>
var p;
//存储 字典字段英文名和中文名对象  
var dataClass; 
// checked 的对象
var checkeds;
var sqls;
//字典数据
var datas;
var a_bh;
var b_bh;
var spmc;
var a_time;
var b_time;
var treeHql="";
//页面数据加载
$(function(){

	did = lnyw.tab_options().did;
	query = lnyw.tab_options().query;
	eval("var hql_"+query+"=''");
	var string;

	$('#selectcommonTree').attr('id', 'sc_' + query);
	$('#select_treeShow_layout').attr('id', 'stl_' + query);
	$('#select_tree').attr('id', 'str_' + query);
	$('#select2').attr('id', 'pro_' + query);
	$('#jxc_select_dg').attr('id', 'jsd_' + query);
	$('#jxc_select_addDialog').attr('id', 'jsa_' + query);
	//创建对象 obj类型
	dataClass=Object.create(Object.prototype);
	checkeds=Object.create(Object.prototype);	
	p = $('#jsa_' + query);
	//初始化页面信息
	$.ajax({
		url : '${pageContext.request.contextPath}/admin/dictAction!listFields.action',
		async: false,
		data : {
			selectType :query,
			//判断参数（根据参数进行查询条件筛选）sqlSelected(值可以是任意不等于空值) 当sqlSelected有值时为查询条件字段
			sqlSelected : 1,
		},
		dataType : 'json',
		success : function(data) {			
			//字符串拼写
			datas=data;
			var star='<table>';
			//循环data数据 拼写字符串
			$.each(data,function(){
				star += '<tr>';
				star += '<th align="left">'+this.cname+'</th>';
				if(this.specials=="time"){
					star += '<td align="right">开始日期</td><td>&#12288;<input id="a_'+this.ename+'"'; 
					star += 'class="inputval'+query+' easyui-my97" readonly="readonly" value="'+moment().date(1).format('YYYY-MM-DD')+'" ';
					star += 'name='+this.ename+'></td>';
					star += '</tr><tr><th></th><td align="right">结束日期</td>';
					star += '<td>&#12288;<input id="b_'+this.ename+'"';
					star += 'class="inputval'+query+' easyui-my97" readonly="readonly" value="'+moment().format('YYYY-MM-DD')+'"';
					star += 'name='+this.ename+'></td>';
					//checkeds[this.ename]="";
				}else if(this.specials=="scope"){
					star += '<td align="right">起始范围</td><td>&#12288;<input id="a_'+this.ename+'"'; 
					star += 'class="inputval'+query+'" name='+this.ename+'></td>';
					star += '</tr><tr><th></th><td align="right">结束范围</td>';
					star += '<td>&#12288;<input id="c_'+this.ename+'"';
					star += 'class="inputval'+query+'" name='+this.ename+'></td>';
				}else{
					if(this.specialValues != null && this.specialValues.trim("").length > 0 ){
						star += '<td class="tdTitle'+query+'">&#12288;<input id="ope_'+this.ename+'" name="ope_'+this.ename+'" style="width:70px;"value="=" ></td>';
 						star += '<td>&#12288;<input class="inputval'+query+'" name='+this.ename+' value='+eval(this.specialValues)+'></td>';
					}else{
					//将checked属性名设置为：字典英文名，属性值设置为：“checked”。					
						star += '<td class="tdTitle'+query+'">&#12288;<input id="ope_'+this.ename+'" name="ope_'+this.ename+'" style="width:70px;" ></td>';
						star += '<td>&#12288;<input class="inputval'+query+'" name='+this.ename+'></td>';
					}
					if(this.show != null && this.show.trim().length > 0 ){
 						star += '<tr><td></td><td></td><td class="show">&#12288;'+this.show+'</td></tr>';
					}
				}				
				checkeds[this.ename]="checked";
				star += '</tr>';
// 				star += '<tr>';
// 				star += '<td colspan="3" align="center">';
// 				star += '<input name="rdo_'+this.ename+'" type="radio" checked="true" value="and" >并且';
// 				star += '</td>';
// 				star += '</tr>';
			});	
			star +='</table>';			
			//页面加载
			$('#sc_' + query).html(star);
		}
	});		
	var selectbox= $('input[name^="ope_"]').combobox({
			data:dictOpe,
			panelHeight: 'auto',
	});
	
	
	
	
});
//查询按钮事件
function selectClick(){
	//取 input值
	//select where条件拼写
	var hql='';
	var string='';
// 	var abc = [];
// 	var mm=[];
	did = lnyw.tab_options().did;
	query = lnyw.tab_options().query;
	var flag=false;
	var message='';
	var s=$('input.inputval'+query);
	dataClass=Object.create(Object.prototype);
	checkeds=Object.create(Object.prototype);	
	p = $('#jsa_' + query);
	//查询试图全部字段并设置dataClass，为选择显示字段用
	$.ajax({
		url : '${pageContext.request.contextPath}/admin/dictAction!listFields.action',
		async: false,
		data : {
			selectType :query,
		},
		dataType : 'json',
		success : function(data) {
			dictData=data;
			$.each(data,function(){		
			//将dataClass属性名设置为：字典英文名， 属性值设置为：中文名。	
				dataClass[this.ename]=this.cname;	
			});
		}
	});
	//遍历input 进行hql拼写
	$.each(s,function(){	
		var inputVal=$(this).val().trim();
		if(this.id.trim().length <= 0 || this.id == null){
			var opeVal=$('input[name=ope_'+$(this).attr('name')+']').val().trim();
			//判断input 里面是否有值，将有值的数据进行拼写;
			if((inputVal.length >0 && opeVal.length <=0)||(inputVal.length <=0 && opeVal.length >0)){
				flag=true;
				message +=dataClass[$(this).attr('name')]+"，";			
			}
		}
		if(inputVal != "" ){
			hql +=' and ';
			if($(this).attr('id')=="spmc"){
				b_bh=$(this).val();
			}
			if($(this).attr('id')=="a_createTime"){
				a_time=$(this).val();
			}
			if($(this).attr('id')=="b_createTime"){
				b_time=moment($(this).val()).add('days', 1).format('YYYY-MM-DD');
			}
			string +=',';
			hql +=$(this).attr('name');
			switch($('input[name=ope_'+$(this).attr('name')+']').val()){
					case '1':
						hql +=' like  \'%'+$(this).val()+'%\'';					
						break;
					case '2':
						hql +=' like  \''+$(this).val()+'%\'';
						break;
					case '3':
						hql +=' like  \'%'+$(this).val()+'\'';		
						break;
					default:
						if($(this).attr('id')==("a_"+$(this).attr('name'))){					
							hql +=' >= ';						
							hql +=' \''+$(this).val()+'\'';
						}else if($(this).attr('id')==("b_"+$(this).attr('name'))){
							hql +=' <= ';
							hql +=' \''+moment($(this).val()).add('days', 1).format('YYYY-MM-DD')+'\'';
						}else if($(this).attr('id')==("c_"+$(this).attr('name'))){
							hql +=' <= ';
							hql +=' \''+$(this).val()+'\'';
						}else{
							hql +=' '+$('input[name=ope_'+$(this).attr('name')+']').val();
							hql +=' \''+$(this).val()+'\'';
						}
						break;
			}		
		 }
		
	});

	
	if(flag){
		$.messager.alert('提示', message+'条件输入不完整', 'error');
	}else {		
		$('#stl_' + query).layout({
			fit : true,
			border : false,
		});

		eval("var hql_"+query+"=' '+hql");
		var title=[];
		var frozenTitle=[];
		var allTitle=[];
		var allFields=[];
		var treeFields=[];
		$.ajax({
			url:'${pageContext.request.contextPath}/admin/dictAction!selectTree.action',
			async: false,
			data : {
					selectType :query,
					},
			dataType : 'json',
			success : function(data) {	
						$.each(data,function(){
							allFields.push(this.ename);
							var treeTitle=Object.create(Object.prototype);
							var elseTitle=Object.create(Object.prototype);
							var frozen=Object.create(Object.prototype);
							if(this.tree == '1'){
								
								treeFields.push(this.ename);
								treeTitle["field"]=this.ename;
								treeTitle["title"]=this.cname;
								title.push(treeTitle);
							}else{
								if(this.frozen=="1"){      				
									frozen["field"]=this.ename;
									frozen["title"]=this.cname;
									frozenTitle.push(frozen);
		      					}else{
		      						elseTitle["field"]=this.ename;
									elseTitle["title"]=this.cname;
									allTitle.push(elseTitle);
		      					}
								
							}
						
						});
			}											
		});	
		$.ajax({
			url : '${pageContext.request.contextPath}/jxc/selectCommonAction!selectCommonTree.action',
			async: false,
			data : {
					hqls :hql,
					query:query,
					//拼写显示名称
					con  :treeFields.join(','),
					did  :did,	
					},
			dataType : 'json',
			success : function(data) {
					//判断后台查询是否成功
						if(data.success){
							var datas=[];
							//遍历后台传回查询的数据
							$.each(data.obj.rows,function(){
							//创建olnyData对象 ，将属性名设置为fields对象内的值  将遍历后的查询数据设置为属性值
								var onlyData=Object.create(Object.prototype);				
// 								for( var i=0;i<this.length;i++){									
// 									onlyData[treeFields[i]]=this[i];	
// 								}
								onlyData[treeFields[0]]=this[0];
								onlyData[treeFields[1]]=this[1];
								onlyData[treeFields[2]]=this[2];
								datas.push(onlyData);						
							});	
							
							$('#str_' + query).datagrid({
								data:datas,
								loadMsg:'',
						 		fit : true,
						 	    border : false,
						 	    singleSelect : true,
						 	    fitColumns: false,
						 	    pagePosition : 'bottom',	
								columns:[title],
								onSelect:function(){
									//根据tree进行筛选
									query = lnyw.tab_options().query;
									var rows = $('#str_' + query).datagrid('getSelections');
									var title=[];	
									$.ajax({
										url:'${pageContext.request.contextPath}/admin/dictAction!selectTree.action',
										async: false,
										data : {
												selectType :query,
												},
										dataType : 'json',
										success : function(data) {												
													$.each(data,function(){
														var treeTitle=Object.create(Object.prototype);
														if(this.tree == '1'){
															treeTitle["field"]=this.ename;
															treeTitle["title"]=this.cname;
															title.push(treeTitle);
														}	
												});
										}											
									});
									var hqlTree="";
									treeHql="";
									if(rows[0][title[1].field] == null){							
										 hqlTree = 'and '+title[0].field+' is '+rows[0][title[0].field];
// 										 var treeHql="";
										 treeHql +=hqlTree;
									}else{										
										hqlTree = 'and '+title[0].field+' = '+rows[0][title[0].field];
// 										var treeHql="";
										treeHql +=hqlTree;
									}
									eval("hqlTree += hql_"+query);			 
									spbh= rows[0][title[0].field];
					                showDatagrid(hqlTree,allFields,allTitle);
					            
				                }					
							});	
							if(datas.length>0){
									var hqlNews="";
									hqlNews=" and "+treeFields[0];
									if(datas[0][treeFields[0]]==null){
										hqlNews += " is "+datas[0][treeFields[0]];
									}else{
										hqlNews += "= "+datas[0][treeFields[0]];
									}
										
									
									treeHql="";
									treeHql +=hqlNews;
									eval("hqlNews += hql_"+query);
									console.info(hqlNews);
									spbh= datas[0][treeFields[0]];
					                showDatagrid(hqlNews,allFields,allTitle);
					             
					                
							}else{
								resultDg=$('#jsd_' + query);
								resultDg.datagrid('loadData', { total: 0, rows: [] });
							}
					}else{
						$.messager.show({
							title : "提示",
							msg : data.msg,
						});
					}							
			}											
		});
	}	
}
//显示datagrid数据
function showDatagrid(hql,allFields,allTitle){
	query = lnyw.tab_options().query;
	did = lnyw.tab_options().did;
	resultDg=$('#jsd_' + query);
	treeHql="";
	treeHql +=hql;
	$.ajax({
		url : '${pageContext.request.contextPath}/jxc/selectCommonAction!selectCommonList.action',
		async: false,
		data : {
				hqls :hql,
				query:query,
				spbh:spbh,
				spmc:spmc,
				a_time:a_time,
				b_time:b_time,
				//拼写显示名称
				con  :allFields.join(','),
				did  :did,	
				},
		dataType : 'json',
		success : function(data) {
				//判断后台查询是否成功
				if(data.success){
					var datas=[];
					
					//遍历后台传回查询的数据
					sqls=data.obj.obj;
					$.each(data.obj.rows,function(){
					//创建olnyData对象 ，将属性名设置为fields对象内的值  将遍历后的查询数据设置为属性值
						var onlyData=Object.create(Object.prototype);
						for( var i=0;i<this.length;i++){									
							onlyData[allFields[i]]=this[i];	
						}	
						datas.push(onlyData);						
					});	
					resultDg.datagrid({
						data:datas,
						loadMsg:'加载中',
				 		fit : true,
				 	    border : false,
				 	    singleSelect : true,
				 	    fitColumns: false,	  
						columns:[allTitle],
						toolbar: [{
				   			iconCls: 'icon-edit',
				   			text   : '导出报表',
				   			handler: function(){
				   				exportExcel();
				   			},			   		
				   		}],
				   		onHeaderContextMenu: function(e, titles){
			                   	e.preventDefault();
			                    if (!cmenu){
			                        createColumnMenu();
			                    }
			                    cmenu.menu('show', {
			                        left:e.pageX,
			                        top:e.pageY
			                    });
			                }					
					});						
				}else{
					$.messager.show({
						title : "提示",
						msg : data.msg
					});
				}							
		}											
	});
	p.dialog('destroy');
	var cmenu;
	function createColumnMenu(){
			cmenu = $('<div/>').appendTo('body');
			cmenu.menu({
		     	onClick: function(item){
	                if (item.iconCls == 'icon-ok'){
	                	$('#jsd_' + query).datagrid('hideColumn', item.name);
	                    cmenu.menu('setIcon', {
	                    	target: item.target,
	                   		iconCls: 'icon-empty'
	                    });
	                 }else {
	                    $('#jsd_' + query).datagrid('showColumn', item.name);
	                    cmenu.menu('setIcon', {
	                    	target: item.target,
	                    	iconCls: 'icon-ok'
	                    });
	                 }
		        }
		    });
		    var fields = $('#jsd_' + query).datagrid('getColumnFields');
		    for(var i=0; i<fields.length; i++){
		    	var field = fields[i];
		    	var col = $('#jsd_' + query).datagrid('getColumnOption', field);
		        cmenu.menu('appendItem', {
		        	text: col.title,
		            name: field,
		         	iconCls: 'icon-ok'
		         });
		   }
	}
}

// function sqlString(){
// 	var hql;
// 	$.each(s,function(){	
// 		var inputVal=$(this).val().trim();
// 		if(this.id.trim().length <= 0 || this.id == null){
// 			var opeVal=$('input[name=ope_'+$(this).attr('name')+']').val().trim();
// 			//判断input 里面是否有值，将有值的数据进行拼写;
// 			if((inputVal.length >0 && opeVal.length <=0)||(inputVal.length <=0 && opeVal.length >0)){
// 				flag=true;
// 				message +=dataClass[$(this).attr('name')]+"，";			
// 			}
// 		}
// 		if(inputVal != "" ){
// 			hql +=' and ';
// 			hql +=$(this).attr('name');
// 			switch($('input[name=ope_'+$(this).attr('name')+']').val()){
// 					case '1':
// 						hql +=' like  \'%'+$(this).val()+'%\'';
// 						break;
// 					case '2':
// 						hql +=' like  \''+$(this).val()+'%\'';
// 						break;
// 					case '3':
// 						hql +=' like  \'%'+$(this).val()+'\'';
// 						break;
// 					default:
// 						if($(this).attr('id')==("a_"+$(this).attr('name'))){
// 							hql +=' >= ';						
// 							hql +=' \''+$(this).val()+'\'';
// 						}else if($(this).attr('id')==("b_"+$(this).attr('name'))){
// 							hql +=' <= ';
// 							hql +=' \''+moment($(this).val()).add('days', 1).format('YYYY-MM-DD')+'\'';
// 						}else{
// 							hql +=' '+$('input[name=ope_'+$(this).attr('name')+']').val();
// 							hql +=' \''+$(this).val()+'\'';
// 						}
// 						break;
// 			}		
// 		 }
// 	});
// }

function cleanClick(){
	var s=$('input.inputval').val('');
	$.each(datas,function(){
		 $('#ope_'+this.ename).combobox('clear');
	});
	$('input[id^="b_"]').val(moment().format('YYYY-MM-DD'));
	$('input[id^="a_"]').val(moment().date(1).format('YYYY-MM-DD'));

}
function exportExcel(){
	var titles=[];
	var fields=[];
// 	eval("hqlNews += hql_"+query);
	var hh =$('#jsd_' + query).datagrid('options').columns[0];
	$.each(hh,function(){			
		var s="";
		for(var i=0;i<this.field.length;i++){
			s +=this.field[i];
		}
		if(!this.hidden==true){
			titles.push(this.title);
			fields.push(s);		
		}	
	});	
	$.ajax({	
		url:'${pageContext.request.contextPath}/jxc/selectCommonAction!ExportExcel.action',
		async: false,
		context:this,	
		data : {
			query:query,
			did:did,
			con :fields.join(','),
			sqls:treeHql,
			titles:titles.join(','),
		},
		success:function(data){	
			var json = $.parseJSON(data);
			if (json.success) {
				var dd="${pageContext.request.contextPath}/"+json.obj;
				if (json.success) {
					window.open(dd);						
				}						
			}
			$.messager.show({
				title : "提示",
				msg : json.msg
			});
		}
	});
		
}



</script>
<div id='jxc_selectTree_layout' class='easyui-layout' style="height:100%;width=100%">
	<div data-options="region:'west',title:'查询条件',split:true,"  style="width:340px;">
	<div id='jxc_select'  class='easyui-layout' data-options="split:false,border:false,fit:true" style="height:100%;width=100%">
		<div  align="center" data-options="region:'north',border:false"  style="height:26px;"  >
			<a href="#" class="easyui-linkbutton"
			data-options="iconCls:'icon-search',plain:true" onclick="selectClick();">查询</a>
			<a href="#" class="easyui-linkbutton"
			data-options="iconCls:'icon-reload',plain:true" onclick="cleanClick();">清除</a>		
		</div>
		<div id='selectcommonTree' data-options="region:'center',border:false"></div>
		</div>
	</div>
    <div data-options="region:'center',title:'详细内容',split:true, fit:true" style="width:100%;height:100%">	   
		<div id='select_treeShow_layout' style="height: 100%;">
			<div data-options="region:'west',title:'视图',split:true" style="height: 100px; width: 150px">
				<ul id="select_tree"></ul>
			</div>
			<div data-options="region:'center',title:'详细内容',split:true, fit:true" style="height: 100px;">
				<div id='jxc_select_dg'></div>
			</div>
		</div>	   
    </div>
</div>
</div>
<div id='jxc_select_addDialog'>
<div id='select2'></div>
</div>


	
