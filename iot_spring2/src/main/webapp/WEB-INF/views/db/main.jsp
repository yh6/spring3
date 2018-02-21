<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<style>
html, body {
	width: 100%; /*provides the correct work of a full-screen layout*/
	height: 100%; /*provides the correct work of a full-screen layout*/
	overflow: hidden; /*hides the default body's space*/
	margin: 0px; /*hides the body's scrolls*/
}

div.controls {
	margin: 0px 10px;
	font-size: 14px;
	font-family: Tahoma;
	color: #404040;
	height: 80px;
}

.my_ftr {
	background-color: white;
	padding-top: 9px;
}

.my_ftr .text {
	font-family: Roboto, Arial, Helvetica;
	font-size: 14px;
	color: #404040;
	padding: 5px 10px;
	height: 70px;
	border: 1px solid #dfdfdf;
	overflow:auto;
}
</style>
<script> 

var bodyLayout, dbTree,winF,popW; 
var aLay, bLay, cLay;
var bTabs, bTab1, bTab2, bTab3;
var tableInfoGrid;
var cTabs;

function columnListCB(res){
	if(res.cList){
		tableInfoGrid = bTabs.tabs("tableInfo").attachGrid();
		var columns = res.cList[0];
		var headerStr = "";
		var colTypeStr = "";
		for(var key in columns){
			if(key=="id") continue;
			headerStr += key + ",";
			colTypeStr += "ro,";
		}
		headerStr = headerStr.substr(0, headerStr.length-1);
		colTypeStr = colTypeStr.substr(0, colTypeStr.length-1);
        tableInfoGrid.setColumnIds(headerStr);
		tableInfoGrid.setHeader(headerStr);
		tableInfoGrid.setColTypes(colTypeStr);
        tableInfoGrid.init();
		tableInfoGrid.parse({data:res.cList},"js");
	
	}
	if(res.TDList) {
		tabledataGrid = bTabs.tabs("tableData").attachGrid();
		var columns = res.TDList[0];
		var headerStr = "";
		var colTypeStr = "";
		for(var key in columns){
			if(key=="id") continue;
			headerStr += key + ",";
			colTypeStr += "ro,";
		}
		headerStr = headerStr.substr(0, headerStr.length-1);
		colTypeStr = colTypeStr.substr(0, colTypeStr.length-1);
		tabledataGrid.setColumnIds(headerStr);
		tabledataGrid.setHeader(headerStr);
		tabledataGrid.setColTypes(colTypeStr);
		tabledataGrid.init();
		tabledataGrid.parse({data:res.TDList},"js");						
	}
}


function connectionListCB(res){
	dbTree = aLay.attachTreeView({
	    items: res.list
	});
	dbTree.attachEvent("onDblClick",function(id){
		var level = dbTree.getLevel(id);
		if(level==2){
			var text = dbTree.getItemText(id);
			var au = new AjaxUtil("${root}/connection/tables/" + text + "/" + id,null,"get");
			au.send(tableListCB); 
			var msg = "<br>" + "use" + "'" + text + "'" + ";"
			printLog(msg);
		}else if(level==3){
			var pId= dbTree.getParentId(id);
			var dbName = dbTree.getItemText(pId);
			var tableName = dbTree.getUserData(id,"orgText");
			var au = new AjaxUtil("${root}/connection/columns/" + dbName + "/" + tableName,null,"get");
			au.send(columnListCB);
		} 
	});
}

function tableListCB(res){
	var parentId = res.parentId;
	var i=1;
	for(var table of res.list){
		var id = parentId + "_" + i++;
		var text = table.tableName;
		if(table.tableComment!=""){
			text += "[" + table.tableComment + "]";
		}
		text += ":"+ table.tableSize + "KB"; 
		dbTree.addItem(id, text, parentId);
		dbTree.setUserData(id,"orgText",table.tableName);
	}
	dbTree.openItem(parentId);
}
function addConnectionCB(res){
	alert(res.msg);
	popW.hide();
	console.log(res);
}
function dbListCB(res){
	console.log(res);
	if(res.error){
		alert(res.error);
		return;
	}
	var parentId = res.parentId;
	for(var db of res.list){
		var id = db.id;
		var text = db.text;
		dbTree.addItem(id, text, parentId);
	}
	dbTree.openItem(parentId);
}
dhtmlxEvent(window,"load",function(){
	bodyLayout = new dhtmlXLayoutObject(document.body,"3L");
	bodyLayout.attachFooter("footDiv");
	
	aLay = bodyLayout.cells("a");
	aLay.setWidth(300);
	aLay.setText("Connection Info List");
	var aToolbar = aLay.attachToolbar();
	aToolbar.addButton("addcon",1,"add Connector");
	aToolbar.addButton("condb",2,"Connection");
	aToolbar.attachEvent("onClick",function(id){
		if(id=="condb"){
			var rowId =dbTree.getSelectedId();
			if(!rowId){
				alert("접속할 커넥션을 선택해주세요.");
				return;
			}
			var au = new AjaxUtil("${root}/connection/db_list/" + rowId,null,"get");
			au.send(dbListCB); 
		}else if(id=="addcon"){
			popW.show();
		}
	})
	
	var au = new AjaxUtil("${root}/connection/list",null,"get");
	au.send(connectionListCB); 
	

	bLay = bodyLayout.cells("b");
	bTabs = bLay.attachTabbar({
		align:"left",
		tabs:[
			{id:"tableInfo", text:"Table Info"},
			{id:"tableData", text:"Table Datas"},
			{id:"sql", text:"Run Sql", active:true}
		]
	});
	
	
	var sqlFormObj = [
		{type: "block", blockOffset: 10, list: [
			{type: "button", name:"runBtn",value: "실행"},
			{type: "newcolumn"},
			{type: "button", name:"cancelBtn",value: "취소"} 
		]},
		{type:"input",name:"sqlTa",label:"sql",required:true,rows:10,style:"background-color:#ecf3f9;border:1px solid #39c;width:800"},
	];
	var sqlForm = bTabs.tabs("sql").attachForm(sqlFormObj);
	
	sqlForm.attachEvent("onButtonClick", function(name){      
	      if(name=="runBtn"){	    
	         var sql = sqlForm.getItemValue("sqlTa").trim();			
			var sqlLength = sql.length;
			var semiColNum = sql.lastIndexOf(";");				
			if(semiColNum != -1 && sqlLength == (semiColNum+1)){
				sql = sql.substr(0, (sqlLength-1));		
			}
	         
	         var sqlArr = [];
	         sqlArr = sql.split(";");
	         
	     	 if(sqlArr.length==1){
	     	      if(sql.indexOf("select") == 0){
	  	            var au = new AjaxUtil("${root}/sql/query/"+sql,null,"post");         
	  	            function sqlCB(res){
	  	            	if(res.errorMsg){
	  	            		alert(res.errorMsg);
	  	            	}else {	
	  	            	cTabs = cLay.attachTabbar();	
	  	               //var cLayGrid = cLay.attachGrid();
	  	               var headerStr = "";
	  	               var colTypeStr = ""; 
	  	               var headerStyle =[];
	  	               if(res.list[0] != null){
	  	                  for(var key in res.list[0]){ 
	  	                	  if(key == "tName"){
	  	                		cTabs.addTab("list",(res.list[0])[key], null, null, true, false);
	  	                	  }else{	  	                	   	                	  
	  	                         headerStr += key + ",";
	  	                         colTypeStr += "ro,";
	  	                         headerStyle.push("color : grren;");
	  	                	  }
	  	                  }                
	  	               }
	  	              var cTGrid = cTabs.tabs("list").attachGrid();
	  	               headerStr = headerStr.substr(0, headerStr.length-1);
	  	              	 colTypeStr = colTypeStr.substr(0, colTypeStr.length-1);            
	  	            	 cTGrid.setColumnIds(headerStr);
	  	          		 cTGrid.setHeader(headerStr,null,headerStyle);
	  	        		 cTGrid.setColTypes(colTypeStr);
	  	      			 cTGrid.init();  
	  	               
	  	      			cTGrid.parse({data:res.list},"js");  
	  	               
	  	    		 var dRows = 0;
	  	 			 var aRows = 0;
	  	     			if(res.dRows){
	  	     				dRows = res.dRows;
	  	     			}
	  	     			 if(res.result){
	  	     				aRows = res.result;	  	     			
	  	     			}
	  	     			var msg = "<br>" + "Affected rows: " + aRows + "    찾은 행: " + dRows ;
	  	     			printLog(msg);
	  	     	//	$("div.text").append("<br>" + "Affected rows: " + aRows + "    찾은 행: " + dRows);
	  	     			}
	  	           	}  	  	            		                    
	  	            au.send(sqlCB); 
	  	  	  	            
	  	         }
	     	      else {
	  	            var au = new AjaxUtil("${root}/sql/update/"+sql,null,"post"); 
	  	            function updateCB(res){
	  	            	if(res.result!=0){
	  	            		var dRows = 0;
	  	   	 			 	var aRows = 0;
	  	   	 			if(res.dRows){
	  	     				dRows = res.dRows;
	  	     			}
	  	   	 			if(res.result){
	  	     				aRows = res.result;	     			
	  	     			}
	  	     			var msg = "<br>" + "Affected rows: " + aRows + "    찾은 행: " + dRows ;
	  	     			printLog(msg);	  	     			
	//  	     		$("div.text").append("<br>" + "Affected rows: " + aRows + "    찾은 행: " + dRows);	         
	  	            		cLayGrid.detachObject();
	  	            	}else {
	  	            		alert("실패");
	  	            	}
	  	            }
	  	           
	  	            au.send(updateCB);
	  	         }
	     	      	     		 
	     	 }
	     	 else{
	     		$.ajax({ 
	     			url      : "${root}/sql/multi",  
	    	        type     : "POST",
	    	        data     : sql,
	    	        dataType : "json",
	    	        beforeSend: function(xhr) {	    		    	
	    		    	xhr.setRequestHeader("Accept", "application/json");
	    		        xhr.setRequestHeader("Content-Type", "application/json");
	    		    },
	    	        success : function(res) {	    	        	
	    	        	cTabs = cLay.attachTabbar();	
	    	        	
	    	        	var dRows = 0;
	   	 			 	var aRows = 0;
	   	 				    	        	
				   		console.log(res);
				   		for(var key in res){
				   			if(key!="dRows" && key!="result"){
				   				cTabs.addTab(key, ((res[key])[0])["tName"], null, null, true, true);
				   				var cTGrid = cTabs.tabs(key).attachGrid();
					   			var headerStr = "";
								var colTypeStr = "";
								var headerStyle =[];
								for(var listCol in (res[key])[0]){
									if(listCol == "tName") continue;
									headerStr += listCol + ",";
									colTypeStr += "ro,";
									headerStyle.push("color : grren;");
								}
								headerStr = headerStr.substr(0, headerStr.length-1);
								colTypeStr = colTypeStr.substr(0, colTypeStr.length-1);				
								cTGrid.setColumnIds(headerStr);
								cTGrid.setHeader(headerStr,null,headerStyle);
								cTGrid.setColTypes(colTypeStr);
								cTGrid.init();  					
								cTGrid.parse({data:res[key]},"js"); 
							}
				   			else{
				   				if(res.dRows){
			  	     				dRows = res.dRows;
			  	     			}
			  	     			if(res.result){
			  	     				aRows = res.result;	     			
			  	     			}				   								   				
				   			}				   			
				   		}
				   		var msg = "<br>" + "Affected rows: " + aRows + "    찾은 행: " + dRows ;
				   		printLog(msg);
				   	//	$("div.text").append("<br>" + "Affected rows: " + aRows + "    찾은 행: " + dRows);	
	    	        	
	    	        },
	    	        error : function(xhr, status, e) {
	    			    	alert("에러 : "+e);
	    	        }
	     		});
	     		
	     	 }
	     	 
	         
	      }else if(name=="cancelBtn"){         
	         sqlForm.clear();
	      }
	      
	   });

	
	sqlForm.attachEvent("onKeyDown",function(inp, ev, name, value){
	   if(ev.which==120){		    
	         var sql = sqlForm.getItemValue("sqlTa").trim();			
			var sqlLength = sql.length;
			var semiColNum = sql.lastIndexOf(";");				
			if(semiColNum != -1 && sqlLength == (semiColNum+1)){
				sql = sql.substr(0, (sqlLength-1));		
			}
	         
	         var sqlArr = [];
	         sqlArr = sql.split(";");
	         
	     	 if(sqlArr.length==1){
	     	      if(sql.indexOf("select") == 0){
	  	            var au = new AjaxUtil("${root}/sql/query/"+sql,null,"post");         
	  	            function sqlCB(res){
	  	            	if(res.errorMsg){
	  	            		alert(res.errorMsg);
	  	            	}else {	
	  	            	cTabs = cLay.attachTabbar();	
	  	               //var cLayGrid = cLay.attachGrid();
	  	               var headerStr = "";
	  	               var colTypeStr = ""; 
	  	               var headerStyle =[];
	  	               if(res.list[0] != null){
	  	                  for(var key in res.list[0]){ 
	  	                	  if(key == "tName"){
	  	                		cTabs.addTab("list",(res.list[0])[key], null, null, true, false);
	  	                	  }else{	  	                	   	                	  
	  	                         headerStr += key + ",";
	  	                         colTypeStr += "ro,";
	  	                         headerStyle.push("color : grren;");
	  	                	  }
	  	                  }                
	  	               }
	  	              var cTGrid = cTabs.tabs("list").attachGrid();
	  	               headerStr = headerStr.substr(0, headerStr.length-1);
	  	              	 colTypeStr = colTypeStr.substr(0, colTypeStr.length-1);            
	  	            	 cTGrid.setColumnIds(headerStr);
	  	          		 cTGrid.setHeader(headerStr,null,headerStyle);
	  	        		 cTGrid.setColTypes(colTypeStr);
	  	      			 cTGrid.init();  
	  	               
	  	      			cTGrid.parse({data:res.list},"js");  
	  	               
	  	    		 var dRows = 0;
	  	 			 var aRows = 0;
	  	     			if(res.dRows){
	  	     				dRows = res.dRows;
	  	     			}
	  	     			if(res.result){
	  	     				aRows = res.result;
	  	     			
	  	     			}
	  	     			var msg = "<br>" + "Affected rows: " + aRows + "    찾은 행: " + dRows ;
	  	     			printLog(msg);
	  	     	//	$("div.text").append("<br>" + "Affected rows: " + aRows + "    찾은 행: " + dRows);
	  	     			}
	  	           	}  	  	            		                    
	  	            au.send(sqlCB); 
	  	  	  	            
	  	         }
	     	      else {
	  	            var au = new AjaxUtil("${root}/sql/update/"+sql,null,"post"); 
	  	            function updateCB(res){
	  	            	if(res.result!=0){
	  	            		var dRows = 0;
	  	   	 			 	var aRows = 0;
	  	   	 			if(res.dRows){
	  	     				dRows = res.dRows;
	  	     			}
	  	     			if(res.result){
	  	     				aRows = res.result;	     			
	  	     			}
	  	     			var msg = "<br>" + "Affected rows: " + aRows + "    찾은 행: " + dRows ;
	  	     			printLog(msg);	  	     			
	//  	     		$("div.text").append("<br>" + "Affected rows: " + aRows + "    찾은 행: " + dRows);	         
	  	            		cLayGrid.detachObject();
	  	            	}else {
	  	            		alert("실패");
	  	            	}
	  	            }
	  	           
	  	            au.send(updateCB);
	  	         }
	     	      	     		 
	     	 }
	     	 else{
	     		$.ajax({ 
	     			url      : "${root}/sql/multi",  
	    	        type     : "POST",
	    	        data     : sql,
	    	        dataType : "json",
	    	        beforeSend: function(xhr) {	    		    	
	    		    	xhr.setRequestHeader("Accept", "application/json");
	    		        xhr.setRequestHeader("Content-Type", "application/json");
	    		    },
	    	        success : function(res) {	    	        	
	    	        	cTabs = cLay.attachTabbar();	
	    	        	
	    	        	var dRows = 0;
	   	 			 	var aRows = 0;
	   	 				    	        	
				   		console.log(res);
				   		for(var key in res){
				   			if(key!="dRows" && key!="result"){
				   				cTabs.addTab(key, ((res[key])[0])["tName"], null, null, true, true);
				   				var cTGrid = cTabs.tabs(key).attachGrid();
					   			var headerStr = "";
								var colTypeStr = "";
								var headerStyle =[];
								for(var listCol in (res[key])[0]){
									if(listCol == "tName") continue;
									headerStr += listCol + ",";
									colTypeStr += "ro,";
									headerStyle.push("color : grren;");
								}
								headerStr = headerStr.substr(0, headerStr.length-1);
								colTypeStr = colTypeStr.substr(0, colTypeStr.length-1);				
								cTGrid.setColumnIds(headerStr);
								cTGrid.setHeader(headerStr,null,headerStyle);
								cTGrid.setColTypes(colTypeStr);
								cTGrid.init();  					
								cTGrid.parse({data:res[key]},"js"); 
							}
				   			else{
				   				if(res.dRows){
			  	     				dRows = res.dRows;
			  	     			}
			  	     			if(res.result){
			  	     				aRows = res.result;	     			
			  	     			}
				   				
				   				
				   			}
				   			
				   		}
				   		var msg = "<br>" + "Affected rows: " + aRows + "    찾은 행: " + dRows ;
				   		printLog(msg);
				   	//	$("div.text").append("<br>" + "Affected rows: " + aRows + "    찾은 행: " + dRows);	
	    	        	
	    	        },
	    	        error : function(xhr, status, e) {
	    			    	alert("에러 : "+e);
	    	        }
	     		});
	     		
	     	 }		   
	   }
	});

	

	 
	cLay = bodyLayout.cells("c");
	cTabs = cLay.attachTabbar({
		align:"left",
		tabs:[
			{id:"tableInfo", text:"Table Info"},
	
		]
	});

	winF = new dhtmlXWindows();
	popW = winF.createWindow("win1",20,30,350,450);
	//popW.hide(); 
	popW.setText("Add Connection Info"); 
	var formObj = [
		        {type:"settings", offsetTop:12,name:"connectionInfo",labelAlign:"left"},
				{type:"input",name:"ciName", label:"커넥션이름",required:true},
				{type:"input",name:"ciUrl", label:"접속URL",required:true},
				{type:"input",name:"ciPort", label:"PORT번호",validate:"ValidInteger",required:true},
				{type:"input",name:"ciDatabase", label:"데이터베이스",required:true},
				{type:"input",name:"ciUser", label:"유저ID",required:true},
				{type:"password",name:"ciPwd", label:"비밀번호",required:true},
				{type:"input",name:"ciEtc", label:"설명"},
				{type: "block", blockOffset: 0, list: [
					{type: "button", name:"saveBtn",value: "저장"},
					{type: "newcolumn"},
					{type: "button", name:"cancelBtn",value: "취소"}
				]}
		];
	var form = popW.attachForm(formObj,true);
	popW.hide();
	
	form.attachEvent("onButtonClick",function(id){
		if(id=="saveBtn"){
			if(form.validate()){
				form.send("${root}/connection/insert", "post",addConnectionCB);
			}
		}else if(id=="cancelBtn"){
			form.clear();
		}
	});
	
})

function printLog(msg){
	$("div.text").append("<br><em><b>" + msg + "</em></b>");
	$("div.text").scrollTop($("div.text")[0].scrollHeight);
		}
</script>


<body>
	<div id="footDiv" class="my_ftr">
		<div class="text">log</div>
	</div>
</body>
</html>