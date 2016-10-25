<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<script type="text/javascript">
//部门
var did;
//人员
var userId;
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
var resultDg;
var hql='';
//页面数据加载
var total;
var openSelectDid='n';
$(function(){	
	did = lnyw.tab_options().did;
	userId = lnyw.tab_options().userId;
	query = lnyw.tab_options().query;
	$('#selectcommon').attr('id', 'sc_' + query);
	$('#total').attr('id', 'total_' + query);
	$('#exportExcel_sql').attr('id', 'exportExcel_sql' + query);
	$('#select2').attr('id', 'pro_' + query);
	$('#bzShow').attr('id', 'bzShow' + query);
	$('#result_dg').attr('id', 'result_' + query);
	resultDg = $('#result_' + query);
	$('#jxc_select_addDialog').attr('id', 'dialog_' + query);
	$('#jxc_select_layout').attr('id', 'jxc_select_layout' + query);
	//$('#jxc_select_layout').attr('id', 'layout_' + query);
// 	$('#select_dep').attr('id','select_dep_'+query);
// 	$('#div_select').attr('id','div_select_'+query);
// 	$("input[name=depName]").attr('name','dep_'+query);
	//创建对象 obj类型
	dataClass=Object.create(Object.prototype);
	checkeds=Object.create(Object.prototype);
// 	checkeds[query]=Object.create(Object.prototype);	
// 	p = $('#dialog_' + query);
	var isNeedDep="";

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
				isNeedDep="true";	
			}
			if(data.bz != undefined){
				$('#bzShow' + query).html("<font color='#0000FF'> "+data.bz+"</font>");	
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
			//判断参数（根据参数进行查询条件筛选）sqlSelecte d(值可以是任意不等于空值) 当sqlSelected有值时为查询条件字段
			sqlSelected : 1,
		},
		dataType : 'json',
		success : function(data) {		
			//字符串拼写
			datas=data;
			var star='<table ">';
			//循环data数据 拼写字符串
			if(isNeedDep=="true"){
				if(did>='09'){
					star += '<tr>';
					star += '<th align="left">部门 </th>';
					star += '<th align="left"> </th><td class="tdTitle">&#12288;<input id="select_dep_' + query  + '" class="inputval"  name="select_depName" style="width:104px;" ></td>';
					star += '</tr>';
				}
			}
			$.each(data,function(){
				star += '<tr>';
				star += '<th align="left">'+this.cname+'</th>';
				
				if(this.specials=="time"){
					star += '<td align="right">开始日期</td><td>&#12288;<input id="a_'+this.ename+query+'"'; 
					
					star += 'class="inputval'+query+' easyui-my97" readonly="readonly" value="'+moment().date(1).format('YYYY-MM-DD')+'" ';
					star += '  name='+this.ename+' size="12"></td>';
					star += '</tr><tr><td></td><td align="right">结束日期</td>';
					star += '<td>&#12288;<input id="b_'+this.ename+query+'"';
					star += 'class="inputval'+query+' easyui-my97" readonly="readonly" value="'+moment().format('YYYY-MM-DD')+'"';
					star += 'name='+this.ename+' size="12"></td>';
					//checkeds[this.ename]="";
				}
				else if(this.specials=="scope"){
					star += '<td align="right">起始范围</td><td>&#12288;<input id="a_'+this.ename+query+'"'; 
					star += 'class="inputval'+query+'" name='+this.ename+' style="width:100px;"></td>';
					star += '</tr><tr><td></td><td align="right">结束范围</td>';
					star += '<td>&#12288;<input id="c_'+this.ename+query+'"';
					star += 'class="inputval'+query+'" name='+this.ename+' style="width:100px;"></td>';
				}
				else if(this.specials=="stime"){
				
					star += '<td align="right">查询日期</td>';
					star += '<td>&#12288;<input id="s_'+this.ename+query+'"';
					star += 'class="inputval'+query+' easyui-my97" readonly="readonly" value="'+moment().format('YYYY-MM-DD')+'"';
					star += '  name='+this.ename+' size="12"></td>';	
				}else{
					if(this.specialValues != null && this.specialValues.trim("").length > 0 ){
						star += '<td class="tdTitle'+query+'">&#12288;<input id="ope_'+this.ename+query+'" name="ope_'+this.ename+query+'" style="width:70px;"value="=" ></td>';
 						star += '<td>&#12288;<input class="inputval'+query+'" id='+this.ename+query+' name='+this.ename+' value='+eval(this.specialValues)+' style="width:100px;"></td>';
					}else{
					//将checked属性名设置为：字典英文名，属性值设置为：“checked”。					
						star += '<td class="tdTitle'+query+'">&#12288;<input id="ope_'+this.ename+query+'" name="ope_'+this.ename+query+'" style="width:70px;" ></td>';
						star += '<td>&#12288;<input class="inputval'+query+'" id="'+this.ename+query+'"  name="'+this.ename+'" style="width:100px;"></td>';
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

	var selectbox= $('input[id^="ope_"]').combobox({
			data:dictOpe,
			panelHeight: 'auto',
	});
	if(isNeedDep=="true"){
		if(did >='09'){	
			$('#select_dep_' + query).combobox({
					    url:'${pageContext.request.contextPath}/admin/departmentAction!listYws.action?id=' + did ,
					    valueField:'id',
					    textField:'depName',
					    panelHeight: 'auto',
					}).combobox('selectedIndex', 0);
			openSelectDid='y';
		}
	}
});

//查询按钮事件
function selectClick(){
	//创建对象 obj类型
// 	total='';
	dataClass=Object.create(Object.prototype);
	checkeds=Object.create(Object.prototype);
	query = lnyw.tab_options().query;
	//查询试图全部字段并设置dataClass，为选择显示字段用
	p = $('#dialog_' + query);
	$.ajax({
		url : '${pageContext.request.contextPath}/admin/dictAction!listFields.action',
		async: false,
		data : { 
			selectType :query,
			isShow:'1',
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
	var s=$('input.inputval'+query);
	//select where条件拼写
	var flag=false;
	var message='';
	//遍历input 进行hql拼写
	
	var dd;
	if(openSelectDid=='y'){
		dd=$('#select_dep_' + query).combobox('getValue');
		eval("var did_"+query+"=dd");
		eval("did=did_"+query);
	}
	
	var conditions=[];
	var execHql=[];
	
	$.each(s,function(){
			var inputVal=$(this).val().trim();		
			//当输入框有值
			
			if(inputVal != "" ){
				//排除特殊值情况（商品编号范围，时间范围）	
				if(!($(this).attr('id') == ("a_"+$(this).attr('name')+query) || $(this).attr('id') == ("b_"+$(this).attr('name')+query) || $(this).attr('id') == ("c_"+$(this).attr('name')+query) ||  $(this).attr('id') == ("s_"+$(this).attr('name')+query)  ) ){																	
									if(($('input[name=ope_'+$(this).attr('name')+query+']').val() != ""&&$('input[name=ope_'+$(this).attr('name')+query+']').val() != " "&&$('input[name=ope_'+$(this).attr('name')+query+']').val() != undefined )){	
									//下拉列表条件是否选择
// 									if(($('input[name=ope_'+$(this).attr('name')+query+']').val().trim().length > 0 &&$('input[name=ope_'+$(this).attr('name')+query+']').val() != undefined )){	
										if($(this).val().trim().length <= 0 ){
											flag=true;
										}										
									}else{
										if($(this).val().trim().length > 0){
// 												if($('input[name=ope_'+$(this).attr('name')+query+']').val() == ""&&$('input[name=ope_'+$(this).attr('name')+query+']').val() != undefined){
														flag=true;
	// 											}
										}	
									}
					 }
					
		
			hql='';
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
						}else if($(this).attr('id')==("s_"+$(this).attr('name')+query)){
							hql +=' = ';
							hql +=' \''+moment($(this).val()).format('YYYY-MM-DD')+'\'';
							execHql.push( "=");
							execHql.push(moment($(this).val()).format('YYYY-MM-DD'));	
						}else if($(this).attr('id')==("c_"+$(this).attr('name')+query)){
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
// 			console.info("ddd"+$(this).attr('name'));
			if(($('input[name=ope_'+$(this).attr('name')+query+']').val() != ""&&$('input[name=ope_'+$(this).attr('name')+query+']').val() != " "&&$('input[name=ope_'+$(this).attr('name')+query+']').val() != undefined )){	
// 				console.info($(this).attr('name'));
				if($(this).val().trim().length <= 0 ){
					flag=true;
				}
			}
			execHql.push("");
		}
		
	});
	if(flag){
		$.messager.alert('提示', message+'条件输入不完整', 'error');
	}else {
		$('#dialog_' + query).dialog({
			title : '选择显示',
			width : 500,
			height : 350,
			modal : true,
			buttons: [{
	            text:'查询',
	            iconCls:'icon-ok',
	            handler:function(){
	            	
					//数组fields 用于存放 查询所需要查询内容
	      			var fields=[];
					var frozens=[];
					var allFields=[];
					//遍历 checkbox值
	      			$.each(dictData,function(){ 
	      				var s=$('input:checkbox[name='+this.ename+']');
	      			//判断是否被选中 将选中值放入数组中  
	      				if(s.is(':checked')){ 
	          			
	          				var value=s.val().replace('abc','(').replace('xyz',')');
	          		
	      					allFields.push(value);
	      					if(this.frozen=="1"){      				
	      						frozens.push(value);
	      					}else{
	      						fields.push(value);
	      					}
	      				}else{
	      					checkeds[this.name]="";
	      				}				
					});						
	      			var dgDatas=[];
					var titles=[];
					var frozenTitles=[];
					//遍历feilds 设置datagrid将显示的title
					$.each(fields,function(){
						var values=this.replace('(','abc').replace(')','xyz');
						var onlyTitle=Object.create(Object.prototype);
						onlyTitle["field"]=values;
						
						onlyTitle["title"]=dataClass[values];
						onlyTitle["formatter"]=function(value) {
		   					if (value == 0) {
		   						return '';
		   					} else {
		   						return value;
		   					}
		   				}
						titles.push(onlyTitle);
					});	
					//遍历frozens 设置datagrid的固定项
					$.each(frozens,function(){
						var onlyFrozenTitle=Object.create(Object.prototype);
						onlyFrozenTitle["field"]=this;
						onlyFrozenTitle["title"]=dataClass[this];					
						frozenTitles.push(onlyFrozenTitle);
					});	
					query = lnyw.tab_options().query;
					resultDg = $('#result_' + query);
	      			resultDg.datagrid({	
	      				data:dgDatas,
	      				fit : true,
	      				fitColumns: false, 
	      			    border : false,  
	      			    pagination : true,	      			    
	      				pagePosition : 'bottom',
// 	      				pageSize : pageSize,
	      				pageList : pageList,
	      				frozenColumns:[frozenTitles],
						columns:[titles],
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
		                        top :e.pageX,
// 		                        top :809,
		                        
		                    });
		                },
	      			});	
	      			
	      		
					var m = step1Ok(conditions.join(" and "),allFields,	execHql.join(" , "));	
					$('#dialog_' + query).dialog('close');	      			
	      			var cmenu;
	      			function createColumnMenu(){
	      						cmenu = $('<div/>').appendTo('body');
	      						cmenu.menu({
	      					     	onClick: function(item){
	      				                if (item.iconCls == 'icon-ok'){
	      				                	resultDg.datagrid('hideColumn', item.name);
	      				                    cmenu.menu('setIcon', {
	      				                    	target: item.target,
	      				                   		iconCls: 'icon-empty'
	      				                    });
	      				                 }else {
	      				                    resultDg.datagrid('showColumn', item.name);
	      				                    cmenu.menu('setIcon', {
	      				                    	target: item.target,
	      				                    	iconCls: 'icon-ok'
	      				                    });
	      				                 }
	      					        }
	      					    });
	      					    var fields = resultDg.datagrid('getColumnFields');
	      					    for(var i=0; i<fields.length; i++){
	      					    	var field = fields[i];
	      					    	var col = resultDg.datagrid('getColumnOption', field);
	      					        cmenu.menu('appendItem', {
	      					        	text: col.title,
	      					            name: field,
	      					         	iconCls: 'icon-ok'
	      					         });
	      					   }
	      			};	
	            },
			
	        }],  
	        onBeforeOpen : function() {
				
	        	$('#select2').attr('id', 'pro_' + query);
	        	var stars="<table width='100%'>";			
				var i=0;
				stars += '<tr>';
				//遍历字典数据 拼写checkbox < name="字典英文名" checked value="字典英文名" > 字典中文名
				$.each(dictData,function(){							
					stars += '<td align="left">';
					stars += '<input type="checkbox"  name="'+this.ename+'"';
					//去checkeds对象属性值 实现默认是否被选中
					stars += checkeds[this.ename] == '' ? ' ' : ' checked="checked" ';
					stars += 'value="'+this.ename+'"><b>'+this.cname;
					//每四条数据进行换行
					if((++i)%4==0){
						stars += '</tr>';
						stars += '<tr>';
					}						
				});	
				stars += '</tr>';
				stars +='</table>';
				//初始化显示页面
//  				$('#pro_' + query).html('');
//				console.info(stars);
 				$('#pro_' + query).html(stars);
 				stars='';
				//绑定button点击事件			
			},	
		});
	}	
}
function step1Ok(cons,allFields,exec) {
  
   	query = lnyw.tab_options().query;
	resultDg = $('#result_' + query);
	var p = resultDg.datagrid('getPager'); 
    $(p).pagination({ 
    		onSelectPage: function (pageNumber, pageSize) { 
	              getData(pageNumber, pageSize,cons,allFields,exec); 
	              var p = resultDg.datagrid('getPager'); 
	              $(p).pagination({ 
	            		    total:$('#total_' + query).val(),
	            		    pageSize:pageSize
           		  });
         	 } 
     });
     getData(1,pageSize,cons,allFields,exec);
     var p = resultDg.datagrid('getPager'); 
     $(p).pagination({ 
    		    total:$('#total_' + query).val(),
    		    pageSize:pageSize
	 });
};

function getData(page, rows,cons,allFields,exec) { 
	query = lnyw.tab_options().query;

	resultDg = $('#result_' + query);
	$.ajax({ 
   		async: false,
   		cache: false,
   		url : '${pageContext.request.contextPath}/jxc/selectCommonAction!selectCommonList.action',
		dataType : 'json',
   		data : {
			hqls :cons,
			exec:exec,
			query:query,
			//拼写显示名称
			con  :allFields.join(','),
			did  :did,	
			userId  :userId,	
			page :page,
			rows :rows,
			total :$('#total_' + query).val(),
		},
        error: function (XMLHttpRequest, textStatus, errorThrown) { 
            alert(textStatus); 
            $.messager.progress('close'); 
        }, 
        success: function (data) { 
          	  $('#exportExcel_sql' + query).val(data.obj.obj);
				var dgDatas=[];
				//遍历后台传回查询的数据
				$.each(data.obj.rows,function(){
				//创建olnyData对象 ，将属性名设置为fields对象内的值  将遍历后的查询数据设置为属性值
					var onlyData=Object.create(Object.prototype);
					for( var i=0;i<this.length;i++){	
						if(allFields[i] != undefined){
							var allF=allFields[i].replace('(','abc').replace(')','xyz');
							onlyData[allF]=this[i];	
						}
											
					}	
					dgDatas.push(onlyData);						
				});	
	            $.messager.progress('close');          	          
	           $('#total_' + query).val(data.obj.total);
	         
	           
// 	            eval("var total_=to");
// 	            console.info(eval("total_"));
// 	            total=eval("total_");
// 	    		eval("total=total"+query);
// 	            resultDg.pagination('refresh');	// 刷新页面右栏的信息
// 	            resultDg.pagination('refresh',{	// 改变选项并刷新页面右栏的信息
// 	              total: 114,
// 	              pageNumber: 6
// 	            });
// 				if(data.obj.obj.indexOf("execute") >=0 ){
// 					if(data.obj.total >38 && data.obj.total < 100){
// 						pageSize=100;
// 					}else{
// 						pageSize=data.obj.total;
// 					}
					
// 				}
	            resultDg.datagrid('loadData', dgDatas);
       } 
    }); 
};
function cleanClick(){
	query = lnyw.tab_options().query;
	var s=$('input.inputval'+query).val('');
	$.each(datas,function(){
		 $('#ope_'+this.ename+query).combobox('clear');
	});
	$('input[id^="b_"]').val(moment().format('YYYY-MM-DD'));
	$('input[id^="a_"]').val(moment().date(1).format('YYYY-MM-DD'));
}

function exportExcel(){
    var titles=[];
	var fields=[];
	query = lnyw.tab_options().query;
	resultDg = $('#result_' + query);
	var hh =resultDg.datagrid('options').columns[0];
	var ss =resultDg.datagrid('options').frozenColumns[0];
	var dd;
	if(openSelectDid=='y'){
		dd=$('#select_dep_' + query).combobox('getValue');
		eval("var did_"+query+"=dd");
		eval("did=did_"+query);
	}
	$.each(ss,function(){			
		var s="";
		for(var i=0;i<this.field.length;i++){
			s +=this.field[i];
		}
		if(!this.hidden==true){
			titles.push(this.title);
			fields.push(s);		
		}	
	});
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
	lnyw.MaskUtil.mask('正在导出，请等待……');
	$.ajax({	
		url:'${pageContext.request.contextPath}/jxc/selectCommonAction!ExportExcel.action',
		async: false,
		cache: false,
		context:this,	
		data : {
			query:query,
			did:did,
			userId:userId,
			sqls: $('#exportExcel_sql' + query).val(),
			con :fields.join(','),
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
		},
		complete: function(){
			lnyw.MaskUtil.unmask();
		}
	});
}



</script>
<div id='jxc_select_layout' class='easyui-layout' style="height: 100%;">
	<div data-options="region:'west',title:'查询条件',split:true,"
		style="width: 400px;">
		<input type="hidden" id="total" name="total" >
		<input type="hidden" id="exportExcel_sql" name="exportExcel_sql" >
		<div id='jxc_select' class='easyui-layout'
			data-options="split:false,border:false,fit:true"
			style="height: 100%;">
			<div align="center" data-options="region:'north',border:false"
				style="height: 48px;">
				<a href="#" class="easyui-linkbutton"
					data-options="iconCls:'icon-search',plain:true"
					onclick="selectClick();">查询</a> <a href="#"
					class="easyui-linkbutton"
					data-options="iconCls:'icon-reload',plain:true"
					onclick="cleanClick();">清除</a>
					<div id="bzShow"  name="bzShow"  align="center"></div>
			</div>
			<div id='selectcommon' data-options="region:'center',border:false"></div>

		</div>
	</div>
	<div data-options="region:'center',title:'详细内容',split:true"
		style="width: 100%; height: 100%">
		<div id='result_dg'></div>
	</div>
</div>
<div id='jxc_select_addDialog'>
	<div id='select2'></div>
</div>



