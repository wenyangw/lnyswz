<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<script type="text/javascript">
/**
 * 说明
 存储过程 tree 必须两个字段 。如果只需要一个，在存储过程中加''。
 tree 第一列为选择条件
 */
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
var jxc_select_fhCombo;
var openTreeSelectDid='n';
//页面数据加载
$(function(){

	did = lnyw.tab_options().did;
	query = lnyw.tab_options().query;
	eval("var hql_"+query+"=''");
	var string;

	$('#jxc_selectTree_layout').attr('id', 'jxc_selectTree_layout' + query);//查询页面父类div
	$('#selectcommonTree').attr('id', 'sc_' + query);//查询条件内容
	$('#select2').attr('id', 'pro_' + query);//查询结果显示字段
	$('#jxc_select_addDialog').attr('id', 'jsa_' + query);//查询结果显示字段父页面div
	$('#select_treeShow_layout').attr('id', 'stl_' + query);//查询结果显示父div
	$('#select_tree').attr('id', 'str_' + query);//查询结果显示视图
	$('#jxc_select_dg').attr('id', 'jsd_' + query);//查询结果显示详细内容
// 	$('#select_tree_dep').attr('id','select_tree_dep_'+query);
//  $('#div_select_tree').attr('id','div_tree_select_'+query);
	$('#exportExcelTree_sql').attr('id', 'exportExcelTree_sql' + query);//导出报表视图语句
	$('#exportExcel_sql').attr('id', 'exportExcel_sql' + query);//导出报表语句
	//创建对象 obj类型
	dataClass=Object.create(Object.prototype);
	checkeds=Object.create(Object.prototype);	
	var isNeedDepTree= "";
	var bzTree="";
	//
	$.ajax({	
		url:'${pageContext.request.contextPath}/admin/dictAction!getDict.action',
		async: false,
		cache: false,
		context:this,	
		data : {
			selectType:query,
		},
		dataType : 'json',
		success:function(data){
				if(data.isDepName == '1'){
					isNeedDepTree="true";	
				}
				if(data.bz != undefined){
					bzTree=data.bz;	
				}
		}
	});
	//初始化页面信息
	$.ajax({
		url : '${pageContext.request.contextPath}/admin/dictAction!listFields.action',
		async: false,
		cache: false,		
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
			if(bzTree !=undefined && bzTree != "" ){
				star +='<tr ><th class="query" colspan="3"><font color="#0000FF">'+bzTree+'</font></th></tr>';
			}
			//循环data数据 拼写字符串
			if(isNeedDepTree=="true"){
				if(did>='09'){
					star += '<tr>';
					star += '<th   class="query_field" align="left">部门 </th>';
					star += '<td   class="query_ope" align="left"> </td><td  class="query_int" >&#12288;<input id="select_tree_dep_' + query  + '" class="inputval"  name="select_tree_depName" style="width:104px;" ></td>';
					star += '</tr>';
				}
			}
			$.each(data,function(){
				star += '<tr>';
				star += '<th  class="query_field"  align="left">'+this.cname+'</th>';
				if(this.specials=="time"){
					star += '<td  class="query_ope"  align="right">开始日期</td><td  class="query_int"  >&#12288;<input id="a_'+this.ename+query+'"'; 
					star += 'class="inputval'+query+' easyui-my97" readonly="readonly" value="'+moment().date(1).format('YYYY-MM-DD')+'" ';
					star += 'name='+this.ename+' size="12"></td>';
					star += '</tr><tr><th  class="query_field" ></th> <td  class="query_ope"  align="right">结束日期</td>';
					star += '<td  class="query_int" >&#12288;<input id="b_'+this.ename+query+'"';
					star += 'class="inputval'+query+' easyui-my97" readonly="readonly" value="'+moment().format('YYYY-MM-DD')+'"';
					star += 'name='+this.ename+' size="12"></td>';
					//checkeds[this.ename]="";
				}else if(this.specials=="selectBox"){
					star += '<td  class="query_ope"  align="left"> </th><td  class="query_int"  >&#12288;<input id="select_'+this.ename+'" class="inputval'+query+'"  name="select_'+this.ename+'" style="width:104px;" ></td>';				
				}else if(this.specials=="sjzsj"){
					star += '<td   class="query_ope"  align="right">起始月份</td><td  class="query_int" >&#12288;<input id="a_'+this.ename+query+'"'; 
					star += 'class="inputval'+query+'" name='+this.ename+' value='+eval(this.specialValues)+' style="width:100px;"></td>';
					star += '</tr><tr><th  class="query_field" ></th><td  class="query_ope"  align="right">结束月份</td>';
					star += '<td  class="query_int" >&#12288;<input id="b_'+this.ename+query+'"';
					star += 'class="inputval'+query+'" name='+this.ename+' value='+eval(this.specialValues)+' style="width:100px;"></td>';
				}else if(this.specials=="scope"){
					star += '<td  class="query_ope"  align="right">起始范围</td><td  class="query_int"  >&#12288;<input id="c_'+this.ename+query+'"'; 
					star += 'class="inputval'+query+'" name='+this.ename+' style="width:100px;"></td>';
					star += '</tr><tr><th  class="query_field" ></th><td  class="query_ope"  align="right">结束范围</td>';
					star += '<td  class="query_int" >&#12288;<input id="d_'+this.ename+query+'"';
					star += 'class="inputval'+query+'" name='+this.ename+' style="width:100px;"></td>';
				}else{
					if(this.specialValues != null && this.specialValues.trim("").length > 0 ){
						star += '<td   class="query_ope" >&#12288;<input id="ope_'+this.ename+query+'" name="ope_'+this.ename+query+'" style="width:70px;"value="=" ></td>';
 						star += '<td class="query_int" >&#12288;<input class="inputval'+query+'" id='+this.ename+query+' name='+this.ename+' value='+eval(this.specialValues)+' style="width:100px;"></td>';
					}else{
					//将checked属性名设置为：字典英文名，属性值设置为：“checked”。					
						star += '<td class="query_ope"  >&#12288;<input id="ope_'+this.ename+query+'" name="ope_'+this.ename+query+'" style="width:70px;" ></td>';
						star += '<td class="query_int" >&#12288;<input class="inputval'+query+'" id="'+this.ename+query+'"  name="'+this.ename+'" style="width:100px;"></td>';
					}
					if(this.show != null && this.show.trim().length > 0 ){
 						star += '<tr><th class="query_field" ></th><td class="query_ope" ></td><td class="query_int"  >&#12288;'+this.show+'</td></tr>';
					}
				}				
				checkeds[this.ename]="checked";
				star += '</tr>';
			});	
			star +='</table>';			
			//页面加载
			$('#sc_' + query).html(star);
		}
	});		
	var selectbox= $('input[id^="ope_"]').combobox({
			data:dictOpe,
			panelHeight: 'auto',
	});
	did = lnyw.tab_options().did;
	jxc_select_fhCombo = lnyw.initCombo($('input[name^="select_"]'), 'id', 'fhmc', '${pageContext.request.contextPath}/jxc/fhAction!listFhs.action?depId=' + did);
	jxc_select_fhCombo.combobox('selectedIndex', 0);		




// 		$('#select_fhId').combobox({
// 				    url:'${pageContext.request.contextPath}/jxc/fhAction!listFhs.action?depId=' + did ,
// 				    valueField:'id',
// 				    textField:'fhmc',
// 				    panelHeight: 'auto',
// 				}).combobox('selectedIndex', 0);
// 		openSelectDid='y';

	if(isNeedDepTree=="true"){
		if(did >= '09'){
			$('#select_tree_dep_' + query).combobox({
					    url:'${pageContext.request.contextPath}/admin/departmentAction!listYws.action?id=' + did ,
					    valueField:'id',
					    textField:'depName',
					    panelHeight: 'auto',
					}).combobox('selectedIndex', 0);
			openTreeSelectDid='y';
		}

	}


	
});
//查询按钮事件
function selectClickTree(){
	//取 input值
	//select where条件拼写
	var hql='';
	var string='';
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
	var dd;
	if(openTreeSelectDid=='y'){
		dd=$('#select_tree_dep_' + query).combobox('getValue');
		eval("var did_"+query+"=dd");
		eval("did=did_"+query);

	}

	var conditions=[];
	var execHql=[];	
	var treeExec=[];	
	//遍历input 进行hql拼写
	$.each(s,function(){	
		var inputVal=$(this).val().trim();
// 		if(!($(this).attr('id') == ("a_"+$(this).attr('name')+query) || $(this).attr('id') == ("b_"+$(this).attr('name')+query) || $(this).attr('id') == ("c_"+$(this).attr('name')+query) ||  $(this).attr('id') == ("s_"+$(this).attr('name')+query) ) ){		
// 			if(($('input[name=ope_'+$(this).attr('id')+']').val() != ""&&$('input[name=ope_'+$(this).attr('id')+']').val() != undefined )){	
// 				if($(this).val().trim().length <= 0 ){
// 					flag=true;
// 				}	
// 			}else{
// 				if($(this).val().trim().length > 0){
// 						if($('input[name=ope_'+$(this).attr('id')+']').val() == ""&&$('input[name=ope_'+$(this).attr('id')+']').val() != undefined){
// 								flag=true;
// 						}
// 				}	
// 			}
// 		}
		
	var hql='';

		if(inputVal != "" ){
		
			if(!($(this).attr('id') == ("a_"+$(this).attr('name')+query) || $(this).attr('id') == ("b_"+$(this).attr('name')+query) || $(this).attr('id') == ("c_"+$(this).attr('name')+query)|| $(this).attr('id') == ("d_"+$(this).attr('name')+query) ||  $(this).attr('id') == ("s_"+$(this).attr('name')+query) ) ){																	
//					if(($('input[name=ope_'+$(this).attr('name')+query+']').val() != ""&&$('input[name=ope_'+$(this).attr('name')+query+']').val() != " "&&$('input[name=ope_'+$(this).attr('name')+query+']').val() != undefined )){	
				//下拉列表条件是否选择
					if(($('input[name=ope_'+$(this).attr('name')+query+']').val() != ""&&$('input[name=ope_'+$(this).attr('name')+query+']').val() != " "&&$('input[name=ope_'+$(this).attr('name')+query+']').val() != undefined )){	
							if($(this).val().trim().length <= 0 ){
								flag=true;
							}										
						}else{
							if($(this).val().trim().length > 0){
		//								if($('input[name=ope_'+$(this).attr('name')+query+']').val() == ""&&$('input[name=ope_'+$(this).attr('name')+query+']').val() != undefined){
											flag=true;
		// 											}
							}	
						}
 				}
			
			
			
// 			if($(this).attr('id')=="spmc"){
// 				b_bh=$(this).val();
// 			}
// 			if($(this).attr('id')=="a_createTime"){
// 				a_time=$(this).val();
// 			}
// 			if($(this).attr('id')=="b_createTime"){
// 				b_time=moment($(this).val()).add('days', 1).format('YYYY-MM-DD');
// 			}

			string +=',';
			hql +=$(this).attr('name');
			switch($('input[name=ope_'+$(this).attr('name')+query+']').val()){
					case '1':
						hql +=' like  \'%'+$(this).val()+'%\'';
						execHql.push( "like");
						execHql.push("%"+$(this).val()+"%");
						break;
					case '2':
						hql +=' like  \''+$(this).val()+'%\'';
						execHql.push( "like");
						execHql.push($(this).val()+"%");
						break;
					case '3':
						hql +=' like  \'%'+$(this).val()+'\'';
						execHql.push( "like");
						execHql.push("%"+$(this).val());
						break;
					default:
						if($(this).attr('id')==("a_"+$(this).attr('name')+query)){					
							hql +=' >= ';						
							hql +=' \''+$(this).val()+'\'';
							execHql.push( ">=");
							execHql.push($(this).val());
						}else if($(this).attr('id')==("b_"+$(this).attr('name')+query)){
							hql +=' <= ';
							hql +=' \''+moment($(this).val()).add('days', 1).format('YYYY-MM-DD')+'\'';
							execHql.push( "<=");
							execHql.push(moment($(this).val()).add('days', 1).format('YYYY-MM-DD'));
						}else if($(this).attr('id')==("c_"+$(this).attr('name')+query)){
							hql +=' >= ';
							hql +=' \''+$(this).val()+'\'';
							execHql.push( ">=");
							execHql.push($(this).val());
						}else if($(this).attr('id')==("d_"+$(this).attr('name')+query)){
							hql +=' <= ';
							hql +=' \''+$(this).val()+'\'';
							execHql.push( "<=");
							execHql.push($(this).val());
						}else{
							hql +=' '+$('input[name=ope_'+$(this).attr('name')+query+']').val();
							hql +=' \''+$(this).val()+'\'';
							execHql.push( $('input[name=ope_'+$(this).attr('name')+query+']').val());
							execHql.push($(this).val());
						}
						break;
			}
			conditions.push(hql);	
		 }else{
			execHql.push("<>");
			execHql.push("");
			if(($('input[name=ope_'+$(this).attr('name')+query+']').val() != ""&&$('input[name=ope_'+$(this).attr('name')+query+']').val() != " "&&$('input[name=ope_'+$(this).attr('name')+query+']').val() != undefined )){	
// 				console.info($(this).attr('name'));
				if($(this).val().trim().length <= 0 ){
					flag=true;
				}
			}
		}
		if($(this).attr('id')!=undefined){
			if($(this).attr('id').indexOf('select_')==0){
				hql='';
	 			hql +=$(this).attr('id').replace('select_','') +' = ';
	 			hql +=('\''+jxc_select_fhCombo.combobox('getValue')+'\'').trim();
	 				conditions.push(hql);
				}
		}		
	});

// 	hql=conditions.join(" and ");
	
	if(flag){
		$.messager.alert('提示', message+'条件输入不完整', 'error');
	}else {		
		$('#stl_' + query).layout({
			fit : true,
			border : false,
		});

// 		eval("var hql_"+query+"=' '+hql");
		var title=[];
		var frozenTitle=[];
		var allTitle=[];
		var allFields=[];
		var treeFields=[];
		var treeShow=[];
		var treeSql=[];
		var i=0;
		$.ajax({
			url:'${pageContext.request.contextPath}/admin/dictAction!selectTree.action',
			async: false,
			cache: false,
			data : {
					selectType :query,
					},
			dataType : 'json',
			success : function(data) {	
						$.each(data,function(){
							allFields.push(this.ename.replace('abc','(').replace('xyz',')').replace('fgh',',').replace("mno","'").replace("mno","'"));
							var treeTitle=Object.create(Object.prototype);
							var elseTitle=Object.create(Object.prototype);
							var frozen=Object.create(Object.prototype);
							if(this.tree == '1'){							
								treeFields.push(this.ename);
								treeTitle["field"]=this.ename;
								treeTitle["title"]=this.cname;
								title.push(treeTitle);
								if(this.treeShow != '1'){
									treeShow.push(this.ename);
								}
								if(this.treeSql == '1'){
									treeSql.push(i);
									treeExec.push("");								
								}
								i++;
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
			cache: false,
			data : {
					hqls :conditions.join(" and "),
					query:query,
					exec:execHql.join(" , "),
					treeExec:treeExec.join(" , "),
					//拼写显示名称
					con  :treeFields.join(','),
					did  :did,	
					},
			dataType : 'json',
			success : function(data) {
					//判断后台查询是否成功
						if(data.success){
							var datas=[];
							$('#exportExcelTree_sql' + query).val(data.obj.obj);
							//遍历后台传回查询的数据
							$.each(data.obj.rows,function(){	
							//创建olnyData对象 ，将属性名设置为fields对象内的值  将遍历后的查询数据设置为属性值
								var onlyData=Object.create(Object.prototype);				
								for(var m=0;m<i;m++){
									onlyData[treeFields[m]]=this[m];
								}			
								datas.push(onlyData);
							});	

							$('#str_' + query).datagrid({
								data:datas,
						 		fit : true,
								loadMsg:"加载数据中，请稍后",  
						 	    border : false,
						 	    singleSelect : true,
						 	    fitColumns: false,
						 	    pagePosition : 'bottom',	
								columns:[title],
								onSelect:function(){
									var treeSelectExec=[];	
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
									if(treeSql.length > 0){
											$.each(treeSql,function(){							
												treeSelectExec.push(rows[0][treeFields[this]]);
											});									
									}			
									if(rows[0][title[1].field] == null){		
										
										 hqlTree = 'and '+title[0].field+' is '+rows[0][title[0].field];
// 										 treeHql +=hqlTree;
									}else{										
										hqlTree = 'and '+title[0].field+' = \''+rows[0][title[0].field]+'\'';
// 										treeHql +=hqlTree;
									}
// 									eval("hqlTree += hql_"+query);

// 									spbh= rows[0][title[0].field];
// 									console.info(spbh);
					                showDatagridTree(conditions.join(" and ")+ hqlTree,allFields,allTitle,execHql.join(" , "),treeSelectExec.join(" , "));
					            
				                }					
							});	
							var treeFirst=[];
							hideTree('1',treeShow);   //隐藏tree不显示的列
							if(datas.length>0){	
									var hqlNews="";
									hqlNews=" and "+treeFields[0];
									if(treeSql.length > 0){
										$.each(treeSql,function(){							
											treeFirst.push(datas[0][treeFields[this]]);
										});									
									}
									if(datas[0][treeFields[0]]==null){
										hqlNews += " is "+datas[0][treeFields[0]];
									}else{
										hqlNews += "= '"+datas[0][treeFields[0]]+"'";
									}
// 									console.info(spbh);
// 									treeHql="";
// 									treeHql +=hqlNews;
// 									eval("hqlNews += hql_"+query);
// 									hqlNews +=conditions.join(" and ");
// 									spbh= datas[0][treeFields[0]];
// 									console.info(execHql.join(" , "));
// 									console.info(treeFirst.join(" , "));
					                showDatagridTree(conditions.join(" and ")+ hqlNews,allFields,allTitle,execHql.join(" , "),treeFirst.join(" , "));
					             
					                
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
function showDatagridTree(hqls,allFields,allTitle,exec,treeExec){
	var isFilter = "00";
	query = lnyw.tab_options().query;
// 	did = lnyw.tab_options().did;
	resultDg=$('#jsd_' + query);
	$.ajax({
		url : '${pageContext.request.contextPath}/jxc/selectCommonAction!selectCommonList.action',
		async: false,
		cache: false,
		data : {
				hqls :hqls,
				query:query,
				exec:exec,
				treeExec:treeExec,
				//拼写显示名称
				con  :allFields.join(','),
				did  :did,	
				isFilter:isFilter,
				},
		dataType : 'json',
		success : function(data) {
				//判断后台查询是否成功
				if(data.success){
						$('#exportExcel_sql' + query).val(data.obj.obj);
						var datas=[];
						//遍历后台传回查询的数据
						sqls=data.obj.obj;
						$.each(data.obj.rows,function(){
	
							//创建olnyData对象 ，将属性名设置为fields对象内的值  将遍历后的查询数据设置为属性值
							var onlyData=Object.create(Object.prototype);							
							for( var i=0;i<this.length;i++){
								var allF=allFields[i].replace('(','abc').replace(')','xyz').replace(',','fgh').replace("'","mno").replace("'","mno");									
								onlyData[allF]=this[i];							
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
						   		},
							],
					   		onHeaderContextMenu: function(e, titles){
				                   	e.preventDefault();
				                    if (!cmenu){
				                        createColumnMenuTree();
				                    }
				                    cmenu.menu('show', {
				                        left:e.pageX,
				                        top:e.pageY
				                    });
				             }					
						});						
						if(treeExec.length > 0 ){
							var options = resultDg.datagrid('options');
							options.toolbar.push(
										{
											iconCls: 'icon-edit',
											text   : '导出全部报表',
											handler: function(){
												exportExcelAll();
										},			   		
									});
							options.toolbar.push(
									{
										iconCls: 'icon-edit',
										text   : '导出视图报表',
										handler: function(){
											exportExcelTree();
									},			   		
							});
							resultDg.datagrid(options);
						}	
				}else{
					$.messager.show({
						title : "提示",
						msg : data.msg
					});
				}							
		}											
	});

	var cmenu;
	function createColumnMenuTree(){
			cmenu = $('</div>').appendTo('body');
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
		         	iconCls: 'icon-ok',
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

function hideTree(isTree,data){
	$.each(data,function(){
		if(isTree == '1'){
			$('#str_' + query).datagrid('hideColumn', this);
		}else{
			$('#jsd_' + query).datagrid('hideColumn', this);
		}
	
		
});
}


function cleanClickTree(){
	query = lnyw.tab_options().query;
	var s=$('input.inputval'+query).val('');
	$.each(datas,function(){
		 $('#ope_'+this.ename+query).combobox('clear');
	});
	$('input[id^="b_"]').val(moment().format('YYYY-MM-DD'));
	$('input[id^="a_"]').val(moment().date(1).format('YYYY-MM-DD'));

}
function exportExcelTree(){
	lnyw.MaskUtil.mask('正在导出，请等待……');
	var titles=[];
	var fields=[];
	var hid=[];
	query = lnyw.tab_options().query;
	var dd;
	var isFilter = "00";
	if(openTreeSelectDid=='y'){
		dd=$('#select_tree_dep_' + query).combobox('getValue');
		eval("var did_"+query+"=dd");
		eval("did=did_"+query);
	}
	$.ajax({
			url:'${pageContext.request.contextPath}/admin/dictAction!selectTree.action',
			async: false,
			cache: false,
			data : {
					selectType :query,
					},
			dataType : 'json',
			success : function(data) {	
						var i=1;
						$.each(data,function(){
							if(this.tree == '1'){								
								if(this.treeShow == '1'){
// 									hid.push(i);	
									fields.push(this.ename);
									titles.push(this.cname);
								}else{
									hid.push(i);	
								}
							}
							i++;
						});
			}											
		});	

	$.ajax({	
		url:'${pageContext.request.contextPath}/jxc/selectCommonAction!ExportExcel.action',
		async: false,
		cache: false,
		context:this,	
		data : {
			query:query,
			did:did,
			con :fields.join(','),
			sqls:$('#exportExcelTree_sql' + query).val(),
			titles:titles.join(','),
			hid:hid.join(','),
			isFilter:isFilter,
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
		},
		complete: function(){
			lnyw.MaskUtil.unmask();
		}
	});
		
}




function exportExcelAll(){
	lnyw.MaskUtil.mask('正在导出，请等待……');
	var titles=[];
	var fields=[];
	var isFilter = "00";
	query = lnyw.tab_options().query;
	var dd;
	if(openTreeSelectDid=='y'){
		dd=$('#select_tree_dep_' + query).combobox('getValue');
		eval("var did_"+query+"=dd");
		eval("did=did_"+query);
	}
	
	$.ajax({
			url:'${pageContext.request.contextPath}/admin/dictAction!selectTree.action',
			async: false,
			cache: false,
			data : {
					selectType :query,
					},
			dataType : 'json',
			success : function(data) {	
						$.each(data,function(){
							fields.push(this.ename.replace('abc','(').replace('xyz',')').replace('fgh',',').replace("mno","'").replace("mno","'"));
							titles.push(this.cname);
						
						});
			}											
		});	

	$.ajax({	
		url:'${pageContext.request.contextPath}/jxc/selectCommonAction!ExportExcel.action',
		async: false,
		cache: false,
		context:this,	
		data : {
			query:query,
			did:did,
			con :fields.join(','),
			sqls:$('#exportExcelTree_sql' + query).val().replace(/,5,/, ",0,"),
// 			sqls:treeHql,
			titles:titles.join(','),
			isFilter:isFilter,
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
		},
		complete: function(){
			lnyw.MaskUtil.unmask();
		}
	});
		
}




function exportExcel(){
	lnyw.MaskUtil.mask('正在导出，请等待……');
	var titles=[];
	var fields=[];
	var isFilter = "00";
	query = lnyw.tab_options().query;
	var dd;
	if(openTreeSelectDid=='y'){
		dd=$('#select_tree_dep_' + query).combobox('getValue');
		eval("var did_"+query+"=dd");
		eval("did=did_"+query);
	}
	$.ajax({
			url:'${pageContext.request.contextPath}/admin/dictAction!selectTree.action',
			async: false,
			cache: false,
			data : {
					selectType :query,
					},
			dataType : 'json',
			success : function(data) {	
						$.each(data,function(){
							fields.push(this.ename.replace('abc','(').replace('xyz',')').replace('fgh',',').replace("mno","'").replace("mno","'"));
							titles.push(this.cname);
						
						});
			}											
		});	
// 	console.info($('#exportExcelTree_sql' + query).val());
// 	console.info($('#exportExcelTree_sql' + query).val().replace(/,5,/, ",0,"));
	$.ajax({	
		url:'${pageContext.request.contextPath}/jxc/selectCommonAction!ExportExcel.action',
		async: false,
		cache: false,
		context:this,	
		data : {
			query:query,
			did:did,
			con :fields.join(','),
			sqls:$('#exportExcel_sql' + query).val(),
// 			sqls:treeHql,
			titles:titles.join(','),
			isFilter:isFilter,
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
		},
		complete: function(){
			lnyw.MaskUtil.unmask();
		}
	});
		
}










</script>
<div id='jxc_selectTree_layout' class='easyui-layout'
	style="height: 100%;">
	<div data-options="region:'west',title:'查询条件',split:true,"
		style="width: 340px;">
		<div id='jxc_select' class='easyui-layout'
			data-options="split:false,border:false,fit:true"
			style="height: 100%;">
			<div align="center" data-options="region:'north',border:false"
				style="height: 48px;">
				<input type="hidden" id="exportExcelTree_sql" name="exportExcelTree_sql" />
				<input type="hidden" id="exportExcel_sql" name="exportExcel_sql" />
				<a href="#" class="easyui-linkbutton"
					data-options="iconCls:'icon-search',plain:true"
					onclick="selectClickTree();">查询</a> <a href="#"
					class="easyui-linkbutton"
					data-options="iconCls:'icon-reload',plain:true"
					onclick="cleanClickTree();">清除</a> <br>
<!-- 				<div id='div_select_tree'> -->
<!-- 					<th align="left"><b>部门</b></th> -->
<!-- 					<tr> -->
<!-- 						<th align="left"></th> -->
<!-- 						<td class="tdTitle">&#12288;<input id="select_tree_dep" -->
<!-- 							class="inputval" name="select_tree_depName" style="width: 104px;"></td> -->
<!-- 					</tr> -->
<!-- 				</div> -->
			
			</div>
			<div id='selectcommonTree'
				data-options="region:'center',border:false"></div>
		</div>
	</div>
	<div data-options="region:'center',title:'详细内容',split:true"
		style="width: 100%; height: 100%">
		<div id='select_treeShow_layout' style="height: 100%;">
			<div data-options="region:'west',title:'视图',split:true"
				style="height: 100px; width: 400px">
				<ul id="select_tree"></ul>
			</div>
			<div data-options="region:'center',title:'详细内容',split:true"
				style="height: 100px;">
				<div id='jxc_select_dg'></div>
			</div>
		</div>
	</div>
</div>
<div id='jxc_select_addDialog'>
	<div id='select2'></div>
</div>



