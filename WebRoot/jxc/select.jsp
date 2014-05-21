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
var resultDg;
var hql='';
//页面数据加载
var total;
$(function(){	
	did = lnyw.tab_options().did;
	query = lnyw.tab_options().query;
	$('#selectcommon').attr('id', 'sc_' + query);
	$('#select2').attr('id', 'pro_' + query);
	$('#result_dg').attr('id', 'result_' + query);
	resultDg = $('#result_' + query);
	$('#jxc_select_addDialog').attr('id', 'dialog_' + query);	
	//创建对象 obj类型
	dataClass=Object.create(Object.prototype);
	checkeds=Object.create(Object.prototype);
// 	checkeds[query]=Object.create(Object.prototype);	
	p = $('#dialog_' + query);
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
					star += 'name='+this.ename+' size="12"></td>';
					star += '</tr><tr><th></th><td align="right">结束日期</td>';
					star += '<td>&#12288;<input id="b_'+this.ename+'"';
					star += 'class="inputval'+query+' easyui-my97" readonly="readonly" value="'+moment().format('YYYY-MM-DD')+'"';
					star += 'name='+this.ename+' size="12"></td>';
					//checkeds[this.ename]="";
				}
				else if(this.specials=="scope"){
					star += '<td align="right">起始范围</td><td>&#12288;<input id="a_'+this.ename+'"'; 
					star += 'class="inputval'+query+'" name='+this.ename+' style="width:100px;"></td>';
					star += '</tr><tr><th></th><td align="right">结束范围</td>';
					star += '<td>&#12288;<input id="c_'+this.ename+'"';
					star += 'class="inputval'+query+'" name='+this.ename+' style="width:100px;"></td>';
				}
				else if(this.specials=="stime"){
				
					star += '<td align="right">查询日期</td>';
					star += '<td>&#12288;<input id="s_'+this.ename+'"';
					star += 'class="inputval'+query+' easyui-my97" readonly="readonly" value="'+moment().format('YYYY-MM-DD')+'"';
					star += 'name='+this.ename+' size="12"></td>';
				}else{
					if(this.specialValues != null && this.specialValues.trim("").length > 0 ){
						star += '<td class="tdTitle'+query+'">&#12288;<input id="ope_'+this.ename+'" name="ope_'+this.ename+'" style="width:70px;"value="=" ></td>';
 						star += '<td>&#12288;<input class="inputval'+query+'" name='+this.ename+' value='+eval(this.specialValues)+' style="width:100px;"></td>';
					}else{
					//将checked属性名设置为：字典英文名，属性值设置为：“checked”。					
						star += '<td class="tdTitle'+query+'">&#12288;<input id="ope_'+this.ename+'" name="ope_'+this.ename+'" style="width:70px;" ></td>';
						star += '<td>&#12288;<input class="inputval'+query+'" name='+this.ename+' style="width:100px;"></td>';
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
	//创建对象 obj类型
	total='';
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
	hql ='';
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
			//hql +=' '+$('input:radio[name=rdo_'+$(this).attr('name')+']:checked').val()+' ';	
			hql +=' and ';
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
						}else if($(this).attr('id')==("s_"+$(this).attr('name'))){
							hql +=' = ';
							hql +=' \''+moment($(this).val()).format('YYYY-MM-DD')+'\'';
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
		p.dialog({
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
	      					allFields.push(s.val());
	      					if(this.frozen=="1"){      				
	      						frozens.push(s.val());
	      					}else{
	      						fields.push(s.val());
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
						var onlyTitle=Object.create(Object.prototype);
						onlyTitle["field"]=this;
						onlyTitle["title"]=dataClass[this];
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
		                }
	      			});	
					var m = step1Ok(hql,allFields);	
	      			p.dialog('close');	      			
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
	      			}	
	            }
			
	        }],  
	        onBeforeOpen : function() {
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
 				$('#pro_' + query).html('');
 				$('#pro_' + query).html(stars);
 				stars='';
				//绑定button点击事件			
			},	
		});
	}	
}
function step1Ok(hql,allFields) {
  
   	query = lnyw.tab_options().query;
	resultDg = $('#result_' + query);
	var p = resultDg.datagrid('getPager'); 
    $(p).pagination({ 
    		onSelectPage: function (pageNumber, pageSize) { 
	              getData(pageNumber, pageSize,hql,allFields); 
	              var p = resultDg.datagrid('getPager'); 
	              $(p).pagination({ 
	            		    total:total,
	            		    pageSize:pageSize
           		  });
         	 } 
     });
     getData(1,pageSize,hql,allFields);
     var p = resultDg.datagrid('getPager'); 
     $(p).pagination({ 
    		    total:total,
    		    pageSize:pageSize
	 });
};
function getData(page, rows,hql,allFields) { 
	query = lnyw.tab_options().query;
	resultDg = $('#result_' + query);
	$.ajax({ 
		dataType : 'json',
   		url : '${pageContext.request.contextPath}/jxc/selectCommonAction!selectCommonList.action',
   		async: false,
   		data : {
			hqls :hql,
			query:query,
			//拼写显示名称
			con  :allFields.join(','),
			did  :did,	
			page :page,
			rows :rows,
			},
        error: function (XMLHttpRequest, textStatus, errorThrown) { 
            alert(textStatus); 
            $.messager.progress('close'); 
        }, 
        success: function (data) { 
            sqls=data.obj.obj;
					var dgDatas=[];
					//遍历后台传回查询的数据
					$.each(data.obj.rows,function(){
					//创建olnyData对象 ，将属性名设置为fields对象内的值  将遍历后的查询数据设置为属性值
						var onlyData=Object.create(Object.prototype);
						for( var i=0;i<this.length;i++){									
							onlyData[allFields[i]]=this[i];						
						}	
						dgDatas.push(onlyData);						
					});	
            $.messager.progress('close'); 
            total=data.obj.total;
            resultDg.datagrid('loadData', dgDatas);
       } 
    }); 
};
function cleanClick(){
	query = lnyw.tab_options().query;
	var s=$('input.inputval'+query).val('');
	$.each(datas,function(){
		 $('#ope_'+this.ename).combobox('clear');
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
	
	$.ajax({	
		url:'${pageContext.request.contextPath}/jxc/selectCommonAction!ExportExcel.action',
		async: false,
		context:this,	
		data : {
			query:query,
			did:did,
			sqls:sqls,
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
		}
	});
		
}



</script>
<div id='jxc_select_layout' class='easyui-layout' style="height:100%;width=100%">
	<div data-options="region:'west',title:'查询条件',split:true,"  style="width:310px;">
		<div id='jxc_select'  class='easyui-layout' data-options="split:false,border:false,fit:true" style="height:100%;width=100%">
			<div  align="center" data-options="region:'north',border:false"  style="height:26px;"  >
				<a href="#" class="easyui-linkbutton"
					data-options="iconCls:'icon-search',plain:true" onclick="selectClick();">查询</a>
				<a href="#" class="easyui-linkbutton"
					data-options="iconCls:'icon-reload',plain:true" onclick="cleanClick();">清除</a>		
			</div>
			<div id='selectcommon' data-options="region:'center',border:false"></div>
		</div>
	</div>
    <div data-options="region:'center',title:'详细内容',split:true, fit:true" style="width:100%;height:100%">	
    	<div id='result_dg'>   	
    	</div>
    </div>
</div>
<div id='jxc_select_addDialog'>
<div id='select2'></div>
</div>


	
