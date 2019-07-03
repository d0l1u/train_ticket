$().ready(function(){
		var iframeSetHeightPath='http://dl4.19e.cn';
		var windowHeight = document.body.scrollHeight+100;
	    var urlRecordHeight=iframeSetHeightPath+"/40/pages/common/setiframeheight.jsp?documentHeight="+windowHeight+"&iframeName=portal30_right";
	    $("#iframeSetHeight").attr("src",urlRecordHeight);
	});
	
	function changeHeight(len){
		try{
	    	var path = $("#iframeSetHeight").attr("src");
	    	var begin = path.indexOf("documentHeight=") + ("documentHeight=".length);
	    	var end = path.indexOf("&iframeName=");
	    	var height = parseInt(path.substring(begin, end)) + len;
	    	var prePath = path.substring(0, begin);
	    	var sufPath = path.substring(end);
	    	$("#iframeSetHeight").attr("src",prePath+height+sufPath);
    	}catch(e){}
}

function toHref(id, url){
	var index = id.substring(12);
	var oldId = $(".current_onclick").attr("id");
	$("#"+oldId).removeClass("current_onclick");
	$("#"+id).addClass("current_onclick");
	// alert(oldId+" "+id);
	window.location = url;
}

Date.prototype.format = function(fmt)   
		{ // author: meizz
		  var o = {   
		    "M+" : this.getMonth()+1,                 // 月份
		    "d+" : this.getDate(),                    // 日
		    "h+" : this.getHours(),                   // 小时
		    "m+" : this.getMinutes(),                 // 分
		    "s+" : this.getSeconds(),                 // 秒
		    "q+" : Math.floor((this.getMonth()+3)/3), // 季度
		    "S"  : this.getMilliseconds()             // 毫秒
		  };   
		  if(/(y+)/.test(fmt))   
		    fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));   
		  for(var k in o)   
		    if(new RegExp("("+ k +")").test(fmt))   
		  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
		  return fmt;   
		}

	$().ready(function(){
		var day = 24*1000*60*60
		var startTime = $("#from_time").val();
		startTime = startTime+":00";
		var dateTime_start= new Date(Date.parse(startTime.replace(/-/g,   "/")));
		var startMillis = dateTime_start.getTime();
		var nowMillis = new Date().getTime();
		var tommorrow = nowMillis+day;
		var tommorrow_str = new Date(tommorrow).format("yyyy-MM-dd")+" 23:00:00";
		var tommorrow_time = new Date(Date.parse(tommorrow_str.replace(/-/g,   "/"))).getTime();
		
		var bt = startMillis-tommorrow_time;
		if(bt<=60*60*1000){
			// 抢票 前一小时
			var children = $("#yun_qiangpiao").children();
			children.each(function(index,element){
				if(index!=4){
					$(element).remove();
				}
				
			});
		}
		
		if(bt>60*60*1000&&bt<=1*day){
			// 抢票 一天
			var children = $("#yun_qiangpiao").children();
			children.each(function(index,element){
				if(index==4||index==3){
					if (index==3) {
						$(element).text("抢至:"+tommorrow_str);
					}
				}else{
					$(element).remove();
				}
				
			});
		}
		if (bt>1*day&&bt<=2*day) {
			// 抢票两天
			var children = $("#yun_qiangpiao").children();
			children.each(function(index,element){
				if(index==4||index==3||index==2){
					if (index==3) {
						$(element).text("抢至:"+tommorrow_str);
					}else if (index==2) {
						var a = new Date(Date.parse(tommorrow_str.replace(/-/g,   "/")));
						a = a.getTime()+day;
						var str = new Date(a).format("yyyy-MM-dd hh:mm:ss");
						$(element).text("抢至:"+str);
					}
				}else{
					$(element).remove();
				}
				
			});
		}
		if(bt>2*day&&bt<=4*day){
			// 抢票三天
			var children = $("#yun_qiangpiao").children();
			children.each(function(index,element){
				if(index==4||index==3||index==2||index ==1){
					if (index==3) {
						$(element).text("抢至:"+tommorrow_str);
					}else if (index==2) {
						var a = new Date(Date.parse(tommorrow_str.replace(/-/g,   "/")));
						a = a.getTime()+day;
						var str = new Date(a).format("yyyy-MM-dd hh:mm:ss");
						$(element).text("抢至:"+str);
					}else if(index==1){
						var a = new Date(Date.parse(tommorrow_str.replace(/-/g,   "/")));
						a = a.getTime()+2*day;
						var str = new Date(a).format("yyyy-MM-dd hh:mm:ss");
						$(element).text("抢至:"+str);
					}
				}else{
					$(element).remove();
				}
				
			});
			
		
		}
		
		if(bt>=4*day){
			// 抢票 5 天
			var children = $("#yun_qiangpiao").children();
			children.each(function(index,element){
				
					if (index==3) {
						$(element).text("抢至:"+tommorrow_str);
					}else if (index==2) {
						var a = new Date(Date.parse(tommorrow_str.replace(/-/g,   "/")));
						a = a.getTime()+day;
						var str = new Date(a).format("yyyy-MM-dd hh:mm:ss");
						$(element).text("抢至:"+str);
					}else if(index==1){
						var a = new Date(Date.parse(tommorrow_str.replace(/-/g,   "/")));
						a = a.getTime()+2*day;
						var str = new Date(a).format("yyyy-MM-dd hh:mm:ss");
						$(element).text("抢至:"+str);
					}else if(index==0){
						var a = new Date(Date.parse(tommorrow_str.replace(/-/g,   "/")));
						a = a.getTime()+4*day;
						var str = new Date(a).format("yyyy-MM-dd hh:mm:ss");
						$(element).text("抢至:"+str);
					}
			});
		}

		
	});
	var jsonLinkList = JSON.stringify([])+"";
	var linkJsonInfo = "";	// jsonLinkList解析后的json集合
	var linkList = [];
	var param_product_id = "";	// 传递给后台的保险ID值
	 $("#check_class_11").click(function(){
		document.getElementById("check_type_11").className='checked_type';
		document.getElementById("check_type_22").className='unchecked_type';
		document.getElementById("table_type_11").style.display='block';
		document.getElementById("table_type_22").style.display='none';
		document.getElementById("choose_type_value").value='choose_type_11';
		hideErrMsg("choose_seat_num");
		hideErrMsg("link_name_ps");	
		hideErrMsg("link_phone_ps");
		hideErrMsg("province");
		hideErrMsg("city");
		hideErrMsg("district");
		hideErrMsg("ps_address");
	});
	
	$("#check_class_22").click(function(){
		showPsCity();
		document.getElementById("check_type_22").className='checked_type';
		document.getElementById("check_type_11").className='unchecked_type';
		document.getElementById("table_type_22").style.display='block';
		document.getElementById("table_type_11").style.display='none';
		document.getElementById("choose_type_value").value='choose_type_22';
		var count = $(".adult:visible,.child:visible").length;
		document.getElementById("choose_seat_num").value = count;
		hideErrMsg("link_name");
		hideErrMsg("link_phone");
	});
	
    $(document).ready(function(){
    	changeHeight(330);// 增加130px的高度（普通购票过长的高度）
    	$("#bx_confirm").hide();
    
    	$("#seat_type").focus();
        $("#seat_type").click(function(){
			var seatType = $(this).val();
			if(seatType=='4' || seatType=='5' || seatType=='6'){
				$("#sleeper_alter").show();
				$("#choose_type").show();
				$("#choose_type_alert").show();
			}else{
				$("#sleeper_alter").hide();
				$("#choose_type").hide();
				$("#choose_type_alert").hide();
			}
        }).trigger("click");
    	
    	var travelTime = $.trim($("#travelTime").val());// 出发时间
				
    	$("#product_0").attr("class","train_product_checked");
    	param_product_id = $("#train_product_0").val();
    	$("#gepf").html($("#product_0").text());
    
   		passengerShow();
   		// 更多
		$("#ser-more").click(function(){
			if($(this).attr("class")=="down-arrow"){
				$(".pass_hidden").show();
				$(this).attr("class","up-arrow");
			}else{
				$(".pass_hidden").hide();
				$(this).attr("class","down-arrow");
			}
		});
    	// 交通意外险选框触发
    	$(".info-select span").click(function(){
			$(this).siblings("span").attr("class","train_product");
			$(this).attr("class","train_product_checked");
			var str = $.trim($(this).text());
			// param_product_id = $(this).val();
			var product_id = $(this).attr("id");
			var train_product_id ="train_"+product_id;
			param_product_id = $("#"+train_product_id).val();
			
			$("#gepf").html(str);
			if(str=='普通购票'){// 不购买保险
				$("#visitant_service").hide();
				$("#free_service").hide();
				$("#normal_service").show();
				// 保险发票隐藏
				$("#fp_content").hide();
				$("#bx_confirm").hide();
			}else if(str=='20元/份，保额65万'){
				$("#visitant_service").show();
				$("#normal_service").hide();
				$("#free_service").hide();
				// 保险发票显示
				$("#fp_content").show();
				$("#bx_confirm").show();
			}else{// 赠送保险
				$("#free_service").show();
				$("#visitant_service").hide();
				$("#normal_service").hide();
				// 保险发票隐藏
				$("#fp_content").hide();
				$("#bx_confirm").hide();
			}
        });
		// 升级VIP服务
        $("#upgrade_service").click(function(){
        	// $("#train_product0").siblings("span").attr("class","train_product");
        	// $("#train_product0").attr("class","train_product_checked");
        	// $("#gepf").html($("#train_product0").text());
        	// param_product_id = $("#train_product0").val();
        	$("#product_0").siblings("span").attr("class","train_product");
			$("#product_0").attr("class","train_product_checked");
			$("#gepf").html($("#product_0").text());
			param_product_id = $("#train_product_0").val();
        	
        	$("#free_service").show();
        	$("#visitant_service").hide();
			$("#normal_service").hide();
			// 保险发票显示
			$("#fp_content").hide();
			$("#bx_confirm").hide();
        });

		
     	 
     	 imagePreview();
     	 
     	 // 异常情况加载坐席价格
     	 if($("#seat_type").val()!=""){
     	 	var key = "seatType" + $("#seat_type").val();
     	 	var price = mapper[key];
			$("#danjia_show").text(price);
			$("#danjia").val(price);
     	 }
     
         $("#tickets-way h5 span").click(function(){
         	 var index = $("#tickets-way h5 span").index($(this)[0]);
             $(this).addClass('current').siblings().removeClass('current');
             // $("#tickets-way
				// .pub_table:eq("+index+")").addClass('current').siblings().removeClass('current');
             if($(this).attr("id")=="outtickettype_11"){// 车站自提 则隐藏地址
             	$("#addressInput").hide();
             }else{// 送票上门 则显示
             	$("#addressInput").show();
             }
             
             // 设置出票方式
             var type_id = $(this).attr("id").split("_")[1];
             $("#out_ticket_type").val(type_id);
         }); 
         
		// 行变色
		$(".table_list tr.adult,.table_list tr.child").live("mouseover", function(){
			$(this).css('background-color','#daeff8').siblings().css("background-color","");
		});	
		
		
		// 添加通行成人
		$(".add_adult").click(function(){
			var count = $(".adult:visible,.child:visible").length;
			// for(var i=1;i<=count;i++){
			// $("#addChildren_"+count).hide();
			// }
			if(count>=5){
				// dialogAlter("一个订单最多可代购5张票！");
				$("#addChildren_"+count).hide();
				$("#addChildren_5").hide();
				return;
			}
			var isValid=true;
			$(".adult:visible :input:text").each(function(){
				if($.trim($(this).val())==""){
					$(this).focus();
					isValid=false;
					showErrMsg($(this).attr("id"), "110px", "请输入"+$(this).attr("title")+"！");
					return false;
				}else if($(this).attr("title")=="姓名" && !checkName($.trim($(this).val()))){
					$(this).focus();
					isValid=false;
					showErrMsg($(this).attr("id"), "110px", "请填写姓名！");
					return false;
				}else if($(this).attr("title")=="证件号码" 
					&& $(this).parents("tr").find(".ids_type_select").val()=="2"
						&& !valiIdCard($.trim($(this).val()))){
						$(this).focus();
						isValid=false;
						showErrMsg($(this).attr("id"), "180px", "请填写正确的二代身份证号！");
						return false;
				}else{
					hideErrMsg($(this).attr("id"));
				}
			});
			if(!isValid){
				return;
			}
			var replaceStr = "bookDetailInfoList["+count+"]";
			var html = $(".adult:hidden").html().replace(/index_source/g, count+1);
			html = html.replace(/bookDetailInfoList_source/g, replaceStr);
			$("<tr class=\"adult\">" + html + "</tr>").css("background-color","")
				.appendTo("#train_ticket_list").show();
			// 每添加一行增加30px整体高度
			changeHeight(35);
			if(count==4){
				$("#add_person").attr("class","unableAdd");
			}
			// 自动补全姓名
			$(".user_name_text").autocomplete(linkList, {
	            max: 12,    // 列表里的条目数
	            minChars: 1,    // 自动完成激活之前填入的最小字符
	            width: 90,     // 提示的宽度，溢出隐藏
	            scrollHeight: 300,   // 提示的高度，溢出显示滚动条
	            matchContains: true,    // 包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
	            autoFill: false,    // 自动填充
	            formatItem: function(row, i, max) {
	                return row.link_name;
	            },
	            formatMatch: function(row, i, max) {
	                return row.link_name;
	            },
	            formatResult: function(row) {
	                return row.link_name;
	            }
	        }).result(function(event, row, formatted) {
	            var id = $(this).attr("id");
	            var app = id.substring(id.length-1,id.length);
	            $("#ticket_type_"+app).val(row.ticket_type);
	            $("#ids_type_"+app).val(row.ids_type);
	            $("#user_ids_"+app).val(row.user_ids);
	        });
			if(count<4){
				addChildrenDisplay();
			}
			var newCount = $(".adult:visible,.child:visible").length;
			for(var i=1;i<=newCount;i++){
				if($("#ticket_type_"+i).val()=="1"){// 儿童票
					$("#addChildren_"+i).hide();
					$("#user_name_"+i).prop("disabled", true);// 设置为只读属性
					$("#ticket_type_"+i).prop("disabled", true);// 设置为只读属性
					$("#ids_type_"+i).prop("disabled", true);// 设置为只读属性
					$("#user_ids_"+i).prop("disabled", true);// 设置为只读属性
				}else{
					$("#addChildren_"+i).show();
					$("#user_name_"+i).prop("disabled", false);// 取消只读属性
					$("#ticket_type_"+i).prop("disabled", false);// 取消只读属性
					$("#ids_type_"+i).prop("disabled", false);// 取消只读属性
					$("#user_ids_"+i).prop("disabled", false);// 取消只读属性
				}
				if(newCount == 5 && $("#ticket_type_"+i).val()=="0"){
					$("#addChildren_"+i).hide();
				}
			}
			// 兼容IE
			$("#add_person").attr("style","");
			$(".savePaxDiv").attr("style","padding-top:4px;*+padding-top:0px;_padding-top:0px;");
		}).trigger("click");
		
		// 添加随行儿童显示
		function addChildrenDisplay(){
			var count = $(".adult:visible,.child:visible").length;
			var replaceStr = "bookDetailInfoList["+count+"]";
			var num = count+1;
			// var html = $(".adult:hidden").html().replace(/index_source/g,
			// num);
			// html = html.replace(/bookDetailInfoList_source/g, replaceStr);
			for(var i=0;i<=count;i++){
				var name = $("#user_name_"+i).val();
				var ticket_type = $("#ticket_type_"+i).val();
				var user_ids = $("#user_ids_"+i).val();
				if(name != "" && user_ids!="" && ticket_type=="0"){// 有儿童票
					$("#addChildren_"+i).show();// “添加随行儿童”显示
					$("#user_name_"+(i)).prop("disabled", false);// 取消只读属性
					$("#ticket_type_"+(i)).prop("disabled", false);// 取消只读属性
					$("#ids_type_"+(i)).prop("disabled", false);// 取消只读属性
					$("#user_ids_"+(i)).prop("disabled", false);// 取消只读属性
				}else{
					$("#addChildren_"+i).hide();// “添加随行儿童”不显示
					$("#user_name_"+(i)).prop("disabled", true);// 设置为只读属性
					$("#ticket_type_"+(i)).prop("disabled", true);// 设置为只读属性
					$("#ids_type_"+(i)).prop("disabled", true);// 设置为只读属性
					$("#user_ids_"+(i)).prop("disabled", true);// 设置为只读属性
				}
			}
			// 兼容IE
			$("#add_person").attr("style","");
			$(".savePaxDiv").attr("style","padding-top:4px;*+padding-top:0px;_padding-top:0px;");
		}
		
		// 添加随行儿童
		$(".addChildren").live("click", function(){
			var id = $(this).attr("id").split('_')[1];// addChildren_1
			var name = $("#user_name_"+id).val(); 
			var ticket_type = "1";
			var ids_type = $("#ids_type_"+id).val();
			var user_ids = $("#user_ids_"+id).val();
			if(name.length==0){
				$("#user_name_"+id).focus();
				showErrMsg("user_name_"+id, "120px", "请输入乘客姓名！");
				return;
			}else if(user_ids.length==0){
				$("#user_ids_"+id).focus();
				showErrMsg("user_ids_"+id, "150px", "请输入乘客身份证号码！");
				return;
			}
			var count = $(".adult:visible,.child:visible").length;
			var replaceStr = "bookDetailInfoList["+count+"]";
			if(count==4){
				for(var i=1;i<=count;i++){
					$("#addChildren_"+i).hide();
				}
			}
			if(count>=5){
				return;
			}
			var num = parseInt(id)+1;
		
			if(parseInt(id) <= count){
				num = parseInt(count)+1;
			}
			var html = $(".adult:hidden").html().replace(/index_source/g, num);
			html = html.replace(/bookDetailInfoList_source/g, replaceStr);
			$("<tr class=\"adult\">" + html + "</tr>").css("background-color","")
				.appendTo("#train_ticket_list").show();
			if(num==5){
				$("#addChildren_"+num).hide();
			}
			// alert("表格为："+$("#train_ticket_list").html());
			// 每添加一行增加30px整体高度
			changeHeight(35);
			if(count==4){
				$("#add_person").attr("class","unableAdd");
				for(var i=1;i<=count;i++){
					$("#addChildren_"+i).hide();
				}
			}
			// 将后续的乘客index_source加1（往下移一行）
			var next = parseInt(id)+2;
			var count1 = parseInt(count)+1;
			if(parseInt(id) < count){
				if(count1>5){
					return;
				}
				if(count1==4){
					for(var i=1;i<=count1;i++){
						$("#addChildren_"+i).hide();
					}
				}
				for(var i=count1;i>=next;i--){
					// 得到原本第parseInt(id)+1行的数据，将此行数据往下移一行
					if(i==3){
						$("#index_"+(i-1)).parent().html().replace(/2/g,"3");
					}else if(i==4){
						$("#index_"+(i-1)).parent().html().replace(/3/g,"4");
					}else if(i==5){
						$("#index_"+(i-1)).parent().html().replace(/4/g,"5");
					}
					var userName = $("#user_name_"+(i-1)).val();
					$("#user_name_"+(i)).val(userName);
					$("#ticket_type_"+(i)).val($("#ticket_type_"+(i-1)).val());
					$("#ids_type_"+(i)).val($("#ids_type_"+(i-1)).val());
					$("#user_ids_"+(i)).val($("#user_ids_"+(i-1)).val());
					toggleTip(i);
				}
			}
			
			$("#user_name_"+(parseInt(id)+1)).val(name);
			$("#ticket_type_"+(parseInt(id)+1)).val("1");
			$("#ids_type_"+(parseInt(id)+1)).val(ids_type);
			$("#user_ids_"+(parseInt(id)+1)).val(user_ids);
			$("#user_name_"+(parseInt(id)+1)).prop("disabled", true);// 设置为只读属性
			$("#ticket_type_"+(parseInt(id)+1)).prop("disabled", true);// 设置为只读属性
			$("#ids_type_"+(parseInt(id)+1)).prop("disabled", true);// 设置为只读属性
			$("#user_ids_"+(parseInt(id)+1)).prop("disabled", true);// 设置为只读属性
			$("#child_tip").show();// 添加随行儿童温馨提示--显示
			changeHeight(130);// 增加100px整体高度
			// 若为儿童票，则不显示“添加随行儿童”；若为成人票，显示“添加随行儿童按钮”
			if(count<4){
				addChildrenDisplay();
			}
			var newCount = $(".adult:visible,.child:visible").length;
			for(var i=1;i<=newCount;i++){
				if($("#ticket_type_"+i).val()=="1"){// 儿童票
					$("#addChildren_"+i).hide();
					$("#user_name_"+i).prop("disabled", true);// 设置为只读属性
					$("#ticket_type_"+i).prop("disabled", true);// 设置为只读属性
					$("#ids_type_"+i).prop("disabled", true);// 设置为只读属性
					$("#user_ids_"+i).prop("disabled", true);// 设置为只读属性
				}else{
					$("#addChildren_"+i).show();
					$("#user_name_"+i).prop("disabled", false);// 取消只读属性
					$("#ticket_type_"+i).prop("disabled", false);// 取消只读属性
					$("#ids_type_"+i).prop("disabled", false);// 取消只读属性
					$("#user_ids_"+i).prop("disabled", false);// 取消只读属性
				}
				if(newCount == 5 && $("#ticket_type_"+i).val()=="0"){
					$("#addChildren_"+i).hide();
				}
			}
			// 兼容IE
			$("#add_person").attr("style","");
			$(".savePaxDiv").attr("style","padding-top:4px;*+padding-top:0px;_padding-top:0px;");
		});
		
		// 删除乘客
		$(".delPerson").live("click", function(){
			var id = $(this).attr("id").split('_')[1];// delPerson_1
			var count = $(".adult:visible,.child:visible").length;
			if($(".adult:visible,.child:visible").length==1){
				removePaxAttr($("#user_name_1").val(), $("#user_ids_1").val());// 删除时，乘客信息变为不选中，背景颜色去掉
				$("#user_name_1").val("");
				$("#user_ids_1").val("");
				$("#ticket_type_1").val("0");
				$("#ids_type_1").val("2");
				return false;
			}
			var name = "";
			var card_num = "";
			$(this).parents("tr").find(":text").each(function(){
				if($(this).attr("id").indexOf("name")>0){
					name = $("#"+$(this).attr("id")).val();
				};
				if($(this).attr("id").indexOf("ids")>0){
					card_num = $("#"+$(this).attr("id")).val();
				};
			});
			if($("#ticket_type_"+id).val()=="0"){// 成人票
				for(var i=parseInt(id)+1;i<=count;i++){// 删除乘客的同事删除该乘客的随行儿童
					if($("#user_name_"+i).val()==name && $("#user_ids_"+i).val()==card_num && $("#ticket_type_"+i).val()=="1"){
						removePaxAttr($("#user_name_"+i).val(), $("#user_ids_"+i).val());
						$("#delPerson_"+i).parents("tr").remove();
						$("#delPerson_"+i).parents("tr").find(":text").each(function(){
							hideErrMsg($("#delPerson_"+i).attr("id"));
						});
					}
				}
				removePaxAttr(name,card_num);// 若该乘客为常用联系人，删除乘客时同时“常用联系人”不选中，清除背景色
			}
			
			// 删除同行乘客的对应选框清空
			var countAfter = $(".adult:visible,.child:visible").length;
			if(countAfter<=1){// 一行数据时，清空文本框内容
				$("#user_name_1").val("");
				$("#user_ids_1").val("");
				$("#ticket_type_1").val("0");
				$("#ids_type_1").val("2");
			}else{// 多行数据时，删除该行数据
				$(this).parents("tr").remove();
			}
			$(this).parents("tr").find(":text").each(function(){
				hideErrMsg($(this).attr("id"));
			});
			
			// 重新设置index
			$(".adult:visible,.child:visible").each(function(index){
				var newIndex = index+1;
				$(this).html($(this).html().replace(/bookDetailInfoList\[\d+\]/g, "bookDetailInfoList["+index+"]"))
					.find(".indexTr").html(newIndex);
				$(this).html($(this).html().replace(/\_\d/g, "_"+newIndex));
				toggleTip(newIndex);// 判断是否显示“儿童票温馨提示”
			});	
			$("#add_person").attr("class","ableAdd");
			// 兼容IE
			$("#add_person").attr("style","");
			if($(".adult:visible,.child:visible").length>1){
				addChildrenDisplay();
			}
			
		});
		
		$("#seat_type").change(function(){
			if($(this).val()==""){
				$("#danjia_show").text("0.00");
				$("#danjia").val("0.00");
				return;
			}
			var key = "seatType" + $(this).val();
			var price = mapper[key];
			$("#danjia_show").text(price);
			$("#danjia").val(price);
			
			if($(this).val()=='8'){// 硬座显示无座备选
				$("#wz_agree").show();
			}else{
				$("#wz_agree").hide();
			}
			if($(this).val()=='2'){// 一等座显示无座备选
				$("#wz_agree1").show();
			}else{
				$("#wz_agree1").hide();
			}
			if($(this).val()=='3'){// 二等座显示无座备选
				$("#wz_agree2").show();
			}else{
				$("#wz_agree2").hide();
			}
		}).trigger("change");
		
		// 无座选择
		function selectWz(){
			if($("#wz_agree:visible").length>0 && $("#wz_chk").attr("checked")){
				$("#wz_ext").val("1");
			}else if($("#wz_agree1:visible").length>0 && $("#wz_chk1").attr("checked")){
				$("#wz_ext").val("1");
			}else if($("#wz_agree2:visible").length>0 && $("#wz_chk2").attr("checked")){
				$("#wz_ext").val("1");
			}else{
				$("#wz_ext").val("0");
			}
		}

		/**
		 * 获取n天后的日期
		 */        
	     function adddays(n) {   
	    	 var newdate=new Date();   
	    	 var newtimems=newdate.getTime()+(n*24*60*60*1000);   
	    	 newdate.setTime(newtimems);   
	    	 var month = newdate.getMonth()+1;
	    	 month<10 ? month='0'+month : month=month;
	    	 // 只得到年月日的
	    	 var newDay = newdate.getFullYear()+"-"+month+"-"+newdate.getDate();
	    	 return newDay;
	    }  
	   // 出发日期大于20天则隐藏“普通购票”功能-----没用
	    function serverSVIP(){
				// 在【提交订单】页面如果代理商选择出发日期＞20天，隐藏“普通购票”功能
				// param_product_id
				var travelTime = $.trim($("#travelTime").val());// 出发时间
				// 30天后的日期是
				if( adddays(1)<travelTime ){
					$("#free_service").show();
				}
			}
	    
		// 提交订单
		$("#btnSubmit").click(function(){
			//var leakCutstr = $("#yun_qiangpiao option:selected").text();
			var se=document.getElementById("yun_qiangpiao");
			var index = se.selectedIndex;
			var text = se.options[index].text;
			text = $.trim(text.replace("抢至:",""));
			$("#leakcutStr").val(text);
			if(checkForm()){
				//SVIP 服务费
				changeSVIPPrice();
				//提交表单
				submitForm();
			}
			
			function submitForm(){
				// 消息框
				var dialog = art.dialog({
					lock: true,
					fixed: true,
					left: '50%',
					top: '40%',
				    title: 'Loading...',
				    icon: "/images/loading.gif",
				    content: '正在验证乘客和余票信息，请稍候！'
				});
				$(".aui_titleBar").hide();
				
				var jsonstr="["; 
				$(".adult:visible").each(function(){
					var userName=$.trim($(this).find(".user_name_text").val());
					var userIds=$.trim($(this).find(".idcard_text").val());
					var cert_type=$.trim($(this).find(".ids_type_select").val());
					jsonstr += "{\"user_name\""+ ":" + "\"" + userName + "\","; 
					jsonstr += "\"cert_no\""+ ":" + "\"" + userIds + "\","; 
					jsonstr += "\"cert_type\""+ ":" + "\"" + cert_type + "\","; 
					jsonstr = jsonstr.substring(0,jsonstr.lastIndexOf(',')); 
					jsonstr += "},";
				});
				jsonstr = jsonstr.substring(0,jsonstr.lastIndexOf(',')); 
				jsonstr += "]";
				$.ajax({
					url:"/userIdsCardInfo/checkUserIdsCardInfo.jhtml",
					type: "POST",
					cache: false,
					data: {'data':jsonstr}, 
					success: function(res){
						if(res=='SUCCESS'){
							dialog.content('验证成功，可以订购！');
							// 保存常用乘客信息
							savePaxInfo();
							var newCount = $(".adult:visible,.child:visible").length;
							for(var i=1;i<=newCount;i++){
								$("#user_name_"+i).prop("disabled", false);// 取消只读属性
								$("#ticket_type_"+i).prop("disabled", false);// 取消只读属性
								$("#ids_type_"+i).prop("disabled", false);// 取消只读属性
								$("#user_ids_"+i).prop("disabled", false);// 取消只读属性
							}
							
							var choose_type_value =$("#choose_type_value").val();
							if($("#chk_fp:visible").length>0 && $("#chk_fp").attr("checked")){
								$("form:first").attr("action", "/rob/createRobOrder.jhtml?fpNeed=1&product_id="+param_product_id+"&choose_type_value="+choose_type_value);
							}else{
								$("form:first").attr("action", "/rob/createRobOrder.jhtml?product_id="+param_product_id+"&choose_type_value="+choose_type_value);
							}
							$("form:first").submit();
						}else{
							var dataObj=eval("("+res+")");
							var strAll = "<p style='width:370px;color:#555;font: 12px/18px Simsun;padding-bottom:6px;'>";
							var errStr = "";
							$.each(dataObj.errorData,function(idx,item){
								if(item.status=='1'){
									errStr+=item.userName;
									strAll+=idx+1+"."+item.userName+"("+item.ids_card+") &nbsp;&nbsp;身份信息待核验<br />";
								}else{
									errStr+=item.userName;
									strAll+=idx+1+"."+item.userName+"("+item.ids_card+") &nbsp;&nbsp;身份信息未通过审核<br />";
								}
							});
							strAll+="<br />&nbsp;&nbsp;&nbsp;&nbsp;自2014年3月1日起，铁道部门规定未通过身份信息核验的用户无法网上购票，必须携带二代身份证到火车站核验之后方可网上购票!</p>";
							if(strAll!=''){
								dialog.content(strAll);
								dialog.title("提示");
								dialog.button({name: '确认',callback: function(){}});
								$(".aui_titleBar").show();
							}else{
								dialog.content('验证失败，请重试！');
								dialog.title("提示");
								dialog.button({name:'确认',focus: true,callback: function(){}});
								$(".aui_titleBar").show();
							}
						}
					},
					error: function(res){
							dialog.content('验证失败，请重试！');
							dialog.title("提示");
							dialog.button({name: '确认',callback: function(){}});
							$(".aui_titleBar").show();
	           		}
				});
				if($(".adult:visible,.child:visible").length==5){
					$("#addChildren_5").hide();
				}
			}

		});
		
		
		// 验证数据
		function checkForm(){
			var isValid=true;
			$(".adult:visible :input:text").each(function(){
				if($.trim($(this).val())==""){
					$(this).focus();
					isValid=false;
					showErrMsg($(this).attr("id"), "110px", "请输入"+$(this).attr("title")+"！");
					return false;
				}else if($(this).attr("title")=="姓名" 
					&& !checkName($.trim($(this).val()))){
					$(this).focus();
					isValid=false;
					showErrMsg($(this).attr("id"), "130px", "请填写正确的姓名！");
					return false;
				}else if($(this).attr("title")=="证件号码" 
					&& $(this).parents("tr").find(".ids_type_select").val()=="2"
					&& !valiIdCard($.trim($(this).val()))){
					$(this).focus();
					isValid=false;
					showErrMsg($(this).attr("id"), "180px", "请填写正确的二代身份证号！");
					return false;
				}else{
					hideErrMsg($(this).attr("id"));
				}
			});
			if(!isValid){
				return false;
			}
			// 根据取票方式核验信息
			if($("#choose_type_value").val()=="choose_type_11"){
				if($.trim($("#link_name").val())==""){
					showErrMsg("link_name", "110px", "请填写联系人！");
					return false;
				}else if(!checkName($.trim($("#link_name").val()))){
					showErrMsg("link_name", "150px", "请填写正确的联系人！");
					return false;
				}else{
					hideErrMsg("link_name");
				}
				if($.trim($("#link_phone").val())==""){
					showErrMsg("link_phone", "90px", "请填写手机！");
					return false;
				}else if(!/^1(3[\d]|4[57]|5[\d]|8[\d]|7[0678])\d{8}$/g.test($.trim($("#link_phone").val()))){
					showErrMsg("link_phone", "150px", "请填写正确的手机号！");
					return false;
				}else{
					hideErrMsg("link_phone");
				}
			// submitInsurance();
			}else if($("#choose_type_value").val()=="choose_type_22"){
				if($("#seat_type").val()=='4' || $("#seat_type").val()=='5' || $("#seat_type").val()=='6'){
					if($(".adult:visible,.child:visible").length<$("#choose_seat_num").val()){
					$("#choose_seat_num").focus();
					showErrMsg("choose_seat_num", "160px", "下铺张数不能大于购票数！");
					return false;
					}else{
					hideErrMsg("choose_seat_num");
					}
					if($("#choose_type:visible").length>0 && $("#choose_agree").attr("checked")){
						$("#choose_ext").val("11");
					}else{
						$("#choose_ext").val("00");
					}
				}
				// alert($("#choose_ext").val());
				if($.trim($("#link_name_ps").val())==""){
					showErrMsg("link_name_ps", "110px", "请填写联系人！");
					return false;
				}else if(!checkName($.trim($("#link_name_ps").val()))){
					showErrMsg("link_name_ps", "150px", "请填写正确的联系人！");
					return false;
				}else{
					hideErrMsg("link_name_ps");
				}
				if($.trim($("#link_phone_ps").val())==""){
					showErrMsg("link_phone_ps", "90px", "请填写手机！");
					return false;
				}else if(!/^1(3[\d]|4[57]|5[\d]|8[\d]|7[0678])\d{8}$/g.test($.trim($("#link_phone_ps").val()))){
					showErrMsg("link_phone_ps", "150px", "请填写正确的手机号！");
					return false;
				}else{
					hideErrMsg("link_phone_ps");
				}
				if($("#province").val()=="0"){
					showErrMsg("province", "90px", "请选择省份！");
					return false;
				}else{
					hideErrMsg("province");
				}
				if($("#city").val()=="0"){
					showErrMsg("city", "90px", "请选择城市！");
					return false;
				}else{
					hideErrMsg("city");
				}
				if($("#district").val()=="0"){
					showErrMsg("district", "90px", "请选择区县！");
					return false;
				}else{
					hideErrMsg("district");
				}
				if($.trim($("#ps_address").val())=="" || $.trim($("#ps_address").val())=="请填写可以收件的真实地址（包括省市区）"){
					showErrMsg("ps_address", "110px", "请填写地址！");
					return false;
				}else{
					hideErrMsg("ps_address");
				}
			}
			
			// 儿童不能单独乘车
			var child=0,adult=0,count=0;
			$(".ticket_type_select:visible").each(function(){
				if($(this).val()=="0"){
					adult++;
				}else if($(this).val()=="1"){
					child++;
				}
				count++;
			});
			if(child>0 && adult==0){
				// 消息框
				var dialog = art.dialog({
					lock: true,
					fixed: true,
					left: '50%',
					top: '50%',
				    title: '提示',
				    okVal: '确认',
				    icon: "/images/warning.png",
				    content: '儿童乘车需要成人陪同，请添加成人乘客！',
				    ok: function(){}
				});
				return false;
			}

			// 成人姓名和身份证号码不能重复
			var isNameDup=false;
			var idNameArray=new Array('1');
			$(".adult:visible").each(function(){
				if($(this).find(".ticket_type_select").val()=='0'){// 成人
					var user_name = $.trim($(this).find(".user_name_text").val());
					var idcard = $.trim($(this).find(".idcard_text").val());
					var nameAndIdCard="";
					nameAndIdCard=user_name+idcard;
					if($.inArray(nameAndIdCard, idNameArray)==-1){
						idNameArray.push(nameAndIdCard);
					}else{
						isNameDup=true;
						return false;
					}
				}
			});
			// 成人 姓名及身份证重复
			if(isNameDup){
				// 消息框
				var dialog = art.dialog({
					lock: true,
					fixed: true,
					left: '50%',
					top: '50%',
				    title: '提示',
				    okVal: '确认',
				    icon: "/images/warning.png",
				    content: '成人乘客姓名和身份证号不能重复，请修改！',
				    ok: function(){}
				});
				return false;
			}

			// 成人证件号不能重复
			var isDup=false;
			var idCardArray=new Array('1');
			$(".adult:visible").each(function(){
				if($(this).find(".ticket_type_select").val()=='0'){// 成人
					var idcard = $.trim($(this).find(".idcard_text").val());
					if($.inArray(idcard, idCardArray)==-1){
						idCardArray.push(idcard);
					}else{
						isDup=true;
						return false;
					}
				}
			});
			// 证件号重复
			if(isDup){
				// 消息框
				var dialog = art.dialog({
					lock: true,
					fixed: true,
					left: '50%',
					top: '50%',
				    title: '提示',
				    okVal: '确认',
				    icon: "/images/warning.png",
				    content: '成人乘客证件号不能重复，请修改！',
				    ok: function(){}
				});
				return false;
			}
			// 勾选我已阅读并同意《保险说明》和《火车票线下代购服务协议》
			if(!$("#chk_bxconfirm").attr("checked")){
				showErrMsg("chk_bxconfirm", "150px", "请认真阅读后勾选！");
				// 一秒后消失
				setTimeout(function(){
					hideErrMsg("chk_bxconfirm")},
					1000
				); 
				return false;
			}
			// 学生票
			if(!checkStu()){
				return false;
			}
			
			return true;
			
		}
		
		artDialog.confirm = function (content, yes, no) {
		    return artDialog({
		        id: 'Confirm',
		        icon: "/images/warning.png",
		        lock: true,
				fixed: true,
				left: '50%',
				top: '50%',
				title: '提示',
		        content: content,
		        ok: function (here) {
		            return yes.call(this, here);
		        },
		        cancel: function (here) {
		            return no && no.call(this, here);
		        }
		    });
		};
		
		$("#seat_type").change(function(){
			if(($("#seat_type").val()!="")){
				hideErrMsg("seat_type");
			}
		});
		
		$("#link_name").blur(function(){
			if($.trim($("#link_name").val())!=""){
				hideErrMsg("link_name");
			}
		});
		
		$("#link_phone").blur(function(){
			if($.trim($("#link_name").val())!=""){
				hideErrMsg("link_name");
			}
		});
		
		// 保险发票
		$("#chk_fp").click(function(){
			if($("#chk_fp").attr("checked")){
				$(".fp_item").show();
				// 增加100px整体高度
				changeHeight(100);
			}else{
				// 减少100px整体高度
				changeHeight(-100);
				$(".fp_item").hide();
			}
		});
     });
     
    // 显示购票说明
	function showGpDesc(){
		// 消息框
		var dialog = art.dialog({
			lock: true,
			fixed: true,
			left: '50%',
			top: '25%',
		    title: '购票说明',
		    okVal: '确认',
		    content: '<p style="width:250px;height:60px;line-height:20px;">温馨提示：乘车人进站乘车时须凭本人有效身份证件原件，票、证、人一致的方可进站乘车。如遇填写错误，只能在网站退票，并按规定收取退票费。</p>',
		    ok: function(){}
		});
	}
     
    // 显示取票说明
	function showQpDesc(){
		// 消息框
		var dialog = art.dialog({
			lock: true,
			fixed: true,
			left: '50%',
			top: '70%',
		    title: '取票说明',
		    okVal: '确认',
		    content: '<p style="width:250px;height:40px;line-height:20px;">1、凭购票时的有效证件和电子订单号，发车前可在全国任意火车站或代售点取票。</p>'
		    		+'<p style="width:250px;height:40px;line-height:20px;margin-top:15px;">2、代售点收取代售费5元/张，另外车站售票窗口取异地票，火车站将收取代售费5元/张。</p>',
		    ok: function(){}
		});
	}
     
   	function showErrMsg(id, _width, msg){
		$("#"+id+"_errMsg").remove();
		var offset = $("#"+id).offset();
		$obj=$("#tip").clone().attr("id", id+"_errMsg")
			.css({'position':'absolute', 'top':offset.top-30, 'left':offset.left, 'width':_width,'z-index':'9999'}).appendTo("body");
		$obj.find(".errMsg").text(msg).end().show();
	}
	
	function hideErrMsg(id){
		$("#"+id+"_errMsg").remove();
	}
     
    function hideMsg(obj){
    	$(".adult:visible :input:text,.child:visible :input:text").each(function(){
			if($.trim($(this).val())!=""){
				hideErrMsg($(this).attr("id"));
			}
		});
    }
    
    // 验证身份证是否有效
	function valiIdCard(idCard){
		if(idCard.length!=18){
			return false;
		}
		var checkFlag = new clsIDCard(idCard);
		if(!checkFlag.IsValid()){
			return false;
		}else{
			return true;
		}
	}
	
	// 验证姓名是否有效
	function checkName(val){
	    var pat=new RegExp("[^a-zA-Z\_\u4e00-\u9fa5]","i"); 
	    // var forbidArr = new Array('成人','成人票','学生票','一张');
	    if(pat.test(val)==true){
	        return false; 
	    }else{
	        var check = /^[A-Za-z]+/;
	        if(check.test(val)){
	            if(val.length<3){
	                return false;
	            }
	        }
	        var checkCName = /^[\u4e00-\u9fa5]+/;
	        if(checkCName.test(val)){
	            if(val.length<2){
	                return false;
	            }
	        }
	        /*******************************************************************
			 * if($.inArray(val.trim(),forbidArr)>=0){ return false; }
			 ******************************************************************/
	        return true;
	    }
	}   
	
	// 显示数字
	function getNumbers(obj,type){
		var offset=$(obj).offset();
		var content="";
		if(type=="idcard"){
			content=$(obj).val().replace(/(^[\w][\w][\w])([\w]{0,3})([\w]{0,4})([\w]{0,4})/g,"$1 $2 $3 $4 ");
		}else{// phone
			content=$(obj).val().replace(/(^[\w][\w][\w])([\w]{0,4})([\w]{0,4})/g,"$1 $2 $3 ");
		}
		var count=content.length;
		var _width=0;
		if(count>3){
			_width=count*12;
		}else{
			_width=36;
		}
		$('#num_tip .numMsg').text(content);
		$('#num_tip').css({'position':'absolute', 'top':offset.top-38, 'left':offset.left, 'width':_width,'z-index':'9998'}).appendTo("body").show();
		if($.trim($(obj).val())==""){
			$('#num_tip').hide();
		}
	}
	
	function hideNumMsg(obj){
		$('#num_tip').hide();
	}
	
	// 显示儿童票购买提示
	function toggleTip(id){
		if($("#ticket_type_"+id).val()=="1"){// 儿童票
			$("#addChildren_"+id).hide();
		}else{
			$("#addChildren_"+id).show();
		}
		var haveChild = false;
		var preName = "";
		if(id > 1){
			preName = $("#user_name_"+(parseInt(id)-1)).val();
			preIds = $("#user_ids_"+(parseInt(id)-1)).val();
		}
		if(id >= 5){
			$("#addChildren_5").hide();
		}
		$(".ticket_type_select").each(function(){
			if($(this).val()=="1"){
				haveChild = true;
				if( ($("#user_name_"+id).val().length>0) && (preName==$("#user_name_"+id).val()) && ($("#user_ids_"+id).val().length>0) && (preIds==$("#user_ids_"+id).val()) ){
					$("#user_name_"+(id)).prop("disabled", true);// 设置为只读属性
					$("#ticket_type_"+(id)).prop("disabled", true);// 设置为只读属性
					$("#ids_type_"+(id)).prop("disabled", true);// 设置为只读属性
					$("#user_ids_"+(id)).prop("disabled", true);// 设置为只读属性
				}
				return false;
			}else{
				$("#user_name_"+(id)).prop("disabled", false);// 取消只读属性
				$("#ticket_type_"+(id)).prop("disabled", false);// 取消只读属性
				$("#ids_type_"+(id)).prop("disabled", false);// 取消只读属性
				$("#user_ids_"+(id)).prop("disabled", false);// 取消只读属性
			}
		});
		if(haveChild){// 儿童票
			$("#child_tip").show();
			// 增加130px整体高度
			// changeHeight(130);
		}else{
			// 减少130px整体高度
			// changeHeight(-130);
			$("#child_tip").hide();
		}
	}
	
	// 验证邮编
	function checkZipCode(code){
		var re = /^[0-9]{6}$/
		if(re.test(code)){
			return true;
		}else{
			return false;
		}
	}
	
	this.imagePreview = function(){ 
		/* CONFIG */
		   xOffset = -10;
		   yOffset = -30;   
		   // var msg = "购买火车票意外险，出行更安全！<br />每份保险最高赔付65万！";
		   var msg = "购买保险，出票速度更快哟！";
		/* END CONFIG */
		
		$("a.preview").hover(function(e){
		   $("body").append("<div id='preview'>" + msg + "</div>");         
		   $("#preview")
		    .css("top",(e.pageY - xOffset) + "px")
		    .css("left",(e.pageX + yOffset) + "px")
		    .fadeIn("slow");      
		    },
		function(){
		   $("#preview").fadeOut("fast");
		    }); 
			$("a.preview").mousemove(function(e){
			   $("#preview")
			    .css("top",(e.pageY - xOffset) + "px")
			    .css("left",(e.pageX + yOffset) + "px");
			});  
	 };
		
    	// 通用弹出信息为str的消息弹框
		function dialogAlter(str){
			// 消息框
			var dialog = art.dialog({
				lock: true,
				fixed: true,
				left: '50%',
				top: '50%',
			    title: '提示',
			    okVal: '确认',
			    icon: "/images/warning.png",
			    content: str,
			    ok: function(){}
			});
			return;
		}

		function passengerShow(){
			$(".usual-passenger ul.oz li").remove(); 
	       	if(jsonLinkList!="" && jsonLinkList!=undefined && jsonLinkList!=null){
	           	linkJsonInfo = JSON.parse(jsonLinkList);
	       		var size = linkJsonInfo.length;
	       		if(size >= 6){
	       			$("#ser-more").show();
			    }else{
			    	$("#ser-more").hide();
				}
	   	       	for(var i=0;i<size;i++){  
	   		       	var newCard = "cookie_"+i;
	   		       	if(i<=6){
	   		       		$(".usual-passenger ul.oz").append("<li id=\""+ newCard +"\"><input type=\"checkbox\" id=\""+newCard+"_for\" class=\""+newCard+"\" onclick='addCommonManager(\""+ newCard +"\");'/><label for=\""+ newCard +"_for\">"+linkJsonInfo[i].link_name+"</label><a title=\"删除乘客\" href='javascript:delHtml("+i+")'></a></li>");
		   		    }else{
		   		    	$(".usual-passenger ul.oz").append("<li class=\"pass_hidden\" style=\"display:none;\" id=\""+ newCard +"\"><input type=\"checkbox\" id=\""+newCard+"_for\" class=\""+newCard+"\" onclick='addCommonManager(\""+ newCard +"\");'/><label for=\""+ newCard +"_for\">"+linkJsonInfo[i].link_name+"</label><a title=\"删除乘客\" href='javascript:delHtml("+i+")'></a></li>");
			   		}
	           	}
	   	       	$("#pax_passenger").show();
	        }else{
	           	$("#pax_passenger").hide();
	        }
		}

    	// 将常用乘客添加到同行乘客中
		function addCommonManager(personId){
			var count = $(".adult:visible,.child:visible").length;
			var id = personId.split('_')[1];// cookie_1
			// 新增乘车人信息赋值
			var name = linkJsonInfo[id].link_name;
			var ticket_type = linkJsonInfo[id].ticket_type;
			var card_type = linkJsonInfo[id].ids_type;
			var card_num = linkJsonInfo[id].user_ids;
		 	if($('.'+personId).attr('checked')=='checked'){
					if(checkPaxInfo(name,ticket_type,card_num,count)){
						dialogAlter("该乘客已经添加到同行乘客中！");
						$("."+personId).removeAttr("checked");
						return;
					}else{
						var replaceStr = "bookDetailInfoList["+count+"]";
						var result = true;
						// 同行乘客信息存在空行则填充最近空行，若不存在空行，则添加一行并填充选中乘客信息
						for(var i=1; i<=5; i++){
							if($("#user_name_"+i).val()=='' && $("#user_ids_"+i).val()==''){
								$("#user_name_"+i).val(name);
								$("#ticket_type_"+i).val(ticket_type);
								$("#ids_type_"+i).val(card_type);
								$("#user_ids_"+i).val(card_num);
								result = false;
								if($("#user_name_"+i).val() != "" && $("#user_ids_"+i).val()!="" && $("#ticket_type_"+i).val()=="0"){
									$("#addChildren_"+i).show();
								}else{
									$("#addChildren_"+i).hide();
								}
								break;
							}
						}
						// $("#"+personId).children("label").attr("class","current");
						var old_class = $("#"+personId).attr("class");
						if(old_class=='pass_hidden'){
							$("#"+personId).attr("class","current_hidden");
						}else{
							$("#"+personId).attr("class","current");
						}
						if(result){
							if(count>=5){
								// dialogAlter("一个订单最多可代购5张票！");
								$("."+personId).removeAttr("checked");
								return;
							}	
							var num = count+1;
							var html = $(".adult:hidden").html().replace(/index_source/g, num);
							html = html.replace(/bookDetailInfoList_source/g, replaceStr);
							$("<tr class=\"adult\">" + html + "</tr>").css("background-color","")
								.appendTo("#train_ticket_list").show();
							// 每添加一行增加30px整体高度
							changeHeight(35);
							if(count==4){
								$("#add_person").attr("class","unableAdd");
								for(var i=1;i<=count;i++){
									$("#addChildren_"+i).hide();
								}
							}
							// 兼容IE
							$("#add_person").attr("style","");
							$(".savePaxDiv").attr("style","padding-top:4px;*+padding-top:3px;_padding-top:0px;");
							
							$("#user_name_"+num).val(name);
							$("#ticket_type_"+num).val(ticket_type);
							$("#ids_type_"+num).val(card_type);
							$("#user_ids_"+num).val(card_num);
							if($("#user_name_"+num).val() != "" && $("#user_ids_"+num).val()!="" && $("#ticket_type_"+num).val()=="0"){
								$("#addChildren_"+num).show();
							}else{
								$("#addChildren_"+num).hide();
							}
						}
					}
					var newCount = $(".adult:visible,.child:visible").length;		
					if(newCount>=5){
						$("#addChildren_5").hide();
					}		
				}else{
					// 选框未选中，删除同行乘客中的该乘客信息
					delPaxInfo(count,name,card_num,id);
				}
		 	
		}
		// 选框未选中，删除同行乘客中的该乘客信息
		function delPaxInfo(count,name,card_num,id){
			if(count==1){
				if($("#user_name_"+count).val()==name && $("#user_ids_"+count).val()==card_num){
					$("#user_name_"+count).val("");
					$("#ticket_type_"+count).val(0);
					$("#ids_type_"+count).val(2);
					$("#user_ids_"+count).val("");
				}
			}else{
				for(var i=1; i<=count; i++){
					if($("#user_name_"+i).val()==name && $("#user_ids_"+i).val()==card_num){
						$("#user_name_"+i).val("");
						$("#ticket_type_"+i).val(0);
						$("#ids_type_"+i).val(2);
						$("#user_ids_"+i).val("");
						$("#user_name_"+i).parents("tr").remove();
						// 重新设置index
						$(".adult:visible,.child:visible").each(function(index){
							var newIndex = index+1;
							$(this).html($(this).html().replace(/bookDetailInfoList\[\d+\]/g, "bookDetailInfoList["+index+"]"))
								.find(".indexTr").html(newIndex);
							$(this).html($(this).html().replace(/\_\d/g, "_"+newIndex));
						});	
						$("#add_person").attr("class","ableAdd");
						// 兼容IE
						$("#add_person").attr("style","");
					}
				}
			}
			if($("#cookie_"+id).attr("class")=='current_hidden'){
				$("#cookie_"+id).attr("class","pass_hidden");
			}else{
				$("#cookie_"+id).removeAttr("class");
			}
			// $("#cookie_"+id).children("label").removeAttr("class");
		}
		// 从通行乘客中删除常用乘客的同时清空该常用乘客的选中按钮
	    function removePaxAttr(name,card_num){
		    if(linkJsonInfo!=null && linkJsonInfo!=undefined){
		    	var size = linkJsonInfo.length;
				for(var i=0; i<size; i++){
					if(linkJsonInfo[i].link_name==name && linkJsonInfo[i].user_ids==card_num){
						if($("#cookie_"+i).attr("class")=='current_hidden'){
							$("#cookie_"+i).attr("class","pass_hidden");
						}else{
							$("#cookie_"+i).removeAttr("class");
						}
						$(".cookie_"+i).removeAttr("checked");
					}
				}
			}
	    }
    
		// 判断通行乘客是否已经存在身份证号为card_num的乘客
		function checkPaxInfo(name,ticket_type,card_num,count){
			var result = false;
			for(var i=1; i<=count; i++){
				var existCard = $("#user_ids_"+i).val();
				if(ticket_type==1){	// 儿童乘客 需要姓名和证件号都匹配
					if(existCard==card_num && name==$("#user_name_"+i).val()){
						result = true;
					}else{
						continue;
					}
				}else{
					if(existCard==card_num){
						result = true;
					}else{
						continue;
					}
				}
				
				return result;
			}
		}
		// 验证json中是否已经存在身份证号为num乘客
		function checkCookiePax(name,ticket_type,num){
			var result = false;
			if(linkJsonInfo==null||linkJsonInfo==undefined){
				return result;
			}
			var size = linkJsonInfo.length;
			for(var i=0; i<size; i++){
				if(ticket_type==1){	// 儿童乘客若num和name都存在则存在
					if(num == linkJsonInfo[i].user_ids && name == linkJsonInfo[i].link_name){
						result = true;
					}else{
						continue;
					}
				}else{
					if(num == linkJsonInfo[i].user_ids){
						result = true;
					}else{
						continue;
					}
				}
			}
			return result;
		}


    	// 新增选中需要保存为常用乘客的信息
		function savePaxInfo(){
			var count = $(".adult:visible,.child:visible").length;
			var jsonstr="[";
			for(var i=1; i<=count;i++){
				if($(".savePax_"+i).attr("checked")=='checked'||$(".savePax_"+i).prop("checked")==true){
					var name = $.trim($("#user_name_"+i).val());
					var card_type = $("#ids_type_"+i).val();
					var card_num = $.trim($("#user_ids_"+i).val());
					
					jsonstr += "{\"user_name\""+ ":" + "\"" + name + "\",";
					jsonstr += "\"card_type\""+ ":" + "\"" + card_type + "\",";  
					jsonstr += "\"card_num\""+ ":" + "\"" + card_num + "\","; 
					jsonstr = jsonstr.substring(0,jsonstr.lastIndexOf(',')); 
					jsonstr += "},";
				}
			}
			jsonstr = jsonstr.substring(0,jsonstr.lastIndexOf(',')); 
			jsonstr += "]";
			$.ajax({
				url:"/userIdsCardInfo/saveUserIdsCardInfo.jhtml",
				type: "POST",
				cache: false,
				data: {'data':jsonstr},
				success: function(res){
				}
			});
		};

		function delHtml(id){
        	// 删除提示框
        	// var id = $(this).parents("li").attr("id").split("_")[1];
    		art.dialog.confirm('您确认删除该常用乘客信息？',function(){
	            delCookiePaxInfo(id);
    		},function(){});
        };
       
      // Json转换为字符串
    	function json2str(obj)
    	{
    	  var S = [];
    	  for(var i in obj){
    	  obj[i] = typeof obj[i] == 'string'?'"'+obj[i]+'"':(typeof obj[i] == 'object'?json2str(obj[i]):obj[i]);
    	  S.push(i+':'+obj[i]); 
    	  }
    	  var result =  '{'+S.join(',')+'}';
    	  return result.replace(/\"/g,"'");
    	}
     	// 删除Cookie中某个乘车人
    	function delCookiePaxInfo(id){
        	var name = linkJsonInfo[id].link_name;
        	var card_num = linkJsonInfo[id].user_ids;
        	var count = $(".adult:visible,.child:visible").length;
        	// 选框未选中，删除同行乘客中的该乘客信息
			delPaxInfo(count,name,card_num,id);
			var arr = new Array();
			// 获取选中状态的index
			$(".usual-passenger ul.oz li").each(function(index){
				if($(".cookie_"+index).attr('checked')=='checked'){
					if(id!=index){
						index = id<index ? index-1 : index;
						arr.push(index);
					}
				}				
			});	
			// 删除常用乘客信息的html
			$("li").remove("#cookie_"+id);
			var obj = JSON.stringify(linkJsonInfo[id])+"";
    		var cookieStr = json2str(linkJsonInfo[id]);// 要删除的乘客信息（字符串）
    		var linkListStr = json2str(linkList);// 常用乘客的信息（字符串）
    		var size = linkJsonInfo.length;
    		if(linkListStr.indexOf(cookieStr)>0){
    			if(id==size-1){
    				jsonLinkList = jsonLinkList.replace(","+obj,"");
	        	}else{
	        		jsonLinkList = jsonLinkList.replace(obj+",","");	
	            }
				// 删除该乘客信息
    			$.ajax({
    				url:"/userIdsCardInfo/deleteUserIdsCardInfo.jhtml",
    				type: "POST",
    				cache: false,
    				data: {'data':cookieStr},
    				success: function(res){
    				}
    			});
        	}
            $(".adult:visible,.child:visible").each(function(index){
				$(this).html($(this).html().replace(/cookie\_\d/g, "cookie_"+index));
			});	
			// 排列常用乘客信息展示
    		passengerShow();	
			for(var i=0; i<arr.length; i++){
				$(".cookie_"+arr[i]).attr('checked','checked');
				$("#cookie_"+arr[i]).attr('class','current');
			}
        }
        
    // 统计获取验证码数
	function tjInsurance(){
		$.ajax({
			url:"/tjInsurance/clickNumAdd.jhtml?type=22&version="+new Date(),
			type: "POST",
			cache: false,
			dataType: "text",
			async: true,
			success: function(data){
			if(data != "success"){
			}
		 }
		});
	}
// 购买保险
function submitInsurance(){
	tjInsurance();
	var names= "";var idCards= "";
	$(".adult:visible :input:text").each(function(){
			if($(this).attr("title")=="姓名" && $(this).parents("tr").find(".ids_type_select").val()=="2"){
					names+=$(this).val()+"@@";
				}else if($(this).attr("title")=="证件号码" 
					&& $(this).parents("tr").find(".ids_type_select").val()=="2"){
					idCards+=$(this).val()+"&&";
				}
			});
		// alert(names);alert(idCards);
		var name = $("#link_name").val();
		var phone = $("#link_phone").val();
		var idCard = "";
		var arr = new Array();arr = names.split("@@");
		var arr2 = new Array();arr2 = idCards.split("&&");
		if(names.indexOf(name) > -1) {
			if(arr.length > 1){	
				for(var i=0;i<arr.length;i++){
					if(arr[i]==name){
					idCard = arr2[i];
					}
				}
			}
		}else{
		name = arr[0];
		idCard = arr2[0];
		}
		// alert(name+"###"+idCard);
		$.ajax({
			url:"/tjInsurance/submitInsurance.jhtml",
			type: "POST",
			cache: false,
			dataType: "text",
			async: true,
			data:{name:name,phone:phone,idCard:idCard},
			success: function(data1){
			if(data1 != "success"){
			}
		 }
		});
	}
	
	
	  // 显示取票说明
	function showPsCity(){
		// 消息框
		var dialog = art.dialog({
			lock: true,
			fixed: true,
			left: '50%',
			top: '70%',
		    title: '配送说明',
		    okVal: '确认',
		    content: '<p style="width:250px;height:40px;line-height:20px;margin:5px;">1、目前该功能仅支持<font color="red">北京地区</font>配送。</p>'
		    		+'<p style="width:250px;height:40px;line-height:20px;margin:5px;">2、配送费<font color="red">￥20</font> + 纸质票费<font color="red">￥5</font>，今天16点前完成支付，明天17点前送到！</p>',
		    ok: function(){}
		});
	
}
	function changeSVIPPrice(){
		var selects = new Array();
		var svip = 0;
		// 所有选票最大票面值
		var max = 0;
		var defaultPrice = $("#defaultSeat").val().split("_")[1];
		max = parseFloat(defaultPrice);
		$("input[name='bkseattype']:checked").each(function(){
			var selectSeat = $(this).val();
			selects.push(selectSeat);
			var str = $(this).val().split("_")[1];
			var price = parseFloat(str);
			if(price>max){
				max = price;
			}
		});
		// 所有成人累计
		if(max<=50){
			svip = 10;
		}else if(max<=200&&max>50){
			svip = 20;
		}else{
			svip = 30;
		}
		$("#max12306Price").val(max);
		$("#selectSeats").val(selects.toString());
		$("#svip_price_span").text("SVIP服务费合计："+$(".adult:visible").length*svip+"元");
		$("#svip_price").val($(".adult:visible").length*svip);
	}

function showStu(){
	hideErrMsg("showStuGrid");
	var stu_arr = new Array();	
	$(".ticket_type_select").each(function(){
		if(this.value=="2"){
			stu_arr.push(this);
		}
	});
	var stu_len = $("#stu_info_div_1 .insurance_wrap").length;
	if(stu_len==stu_arr.length){
		return ;
	}
	if(stu_arr.length>stu_len){
		for(var i= stu_len;i<stu_arr.length;i++){
			
			var obj = stu_arr[i];
			var index = obj.id.replace("ticket_type_","");
			if($("#user_name_"+index).val()==""){
				showErrMsg("user_name_"+index, "110px", "请填写姓名！");
				return false;
			}
			var html = $("#stu_div_0")[0].outerHTML.replace(/_0/g,"_"+index).replace("@@",$("#user_name_"+index).val());
			$("#stu_info_div_1").append(html);
			$("#stu_info_div_1 .insurance_wrap").show();
		}
		return ;
	}
	$("#stu_info_div_1 .insurance_wrap").remove();
	
}
var pro_array =["北京", "天津", "河北", "山西", "内蒙古", "辽宁", "吉林", "黑龙江", "上海", "江苏", "浙江", "安徽", "福建", "江西", "山东", "河南", "湖北", "湖南", "广东", "广西", "海南", "重庆", "四川", "贵州", "云南", "西藏", "陕西", "甘肃", "青海", "宁夏", "新疆"]
var arr_entry_year =["2008","2009","2010","2011","2012","2013","2014","2015","2016","2017"];
var arr_school_sys = ["1年","2年","3年","4年","5年","6年","7年","8年","9年"];
var school_arr = [];
var city_arr = [];
$(function(){
	$.ajax({
		type : "post",
		url :  "/js/rob/school.json",
		success : function(data){
			var arr = data.data;
			for (var i = 0; i < arr.length; i++) {
				var obj = arr[i]
				var name = obj.chineseName;
				school_arr.push(name);
			}
		}
	});
	$.ajax({
		type : "post",
		url :  "/js/rob/city.json",
		success : function(data){
			var arr = data.data;
			for (var i = 0; i < arr.length; i++) {
				var obj = arr[i]
				var name = obj.chineseName;
				city_arr.push(name);
			}
		}
	});
	$("#stu_info_div_1").on("focus",".auto",function(){
		
		var op ={
			minChars: 0,
			matchContains: true,
			width:120
		}
		var id = this.id;
		hideErrMsg(id);
		if(id.indexOf("school_system")!=-1){
			$(this).autocomplete(arr_school_sys);
		}
		if(id.indexOf("enter_year")!=-1){
			$(this).autocomplete(arr_entry_year);
		}
		if(id.indexOf("province_name")!=-1){
			op.max = 40;
			$(this).autocomplete(pro_array,op);
		}
		if(id.indexOf("school_name")!=-1){
			op.width = 130;
			$(this).autocomplete(school_arr,op);
		}
		if(id.indexOf("preference_from_station_name")!=-1){
			op.width = 130;
			$(this).autocomplete(city_arr,op);
		}
		if(id.indexOf("preference_to_station_name")!=-1){
			op.width = 100;
			$(this).autocomplete(city_arr,op);
		}
		
		
	});
})
var stuinfos = [];
function checkStu(){
	var stu_arr = new Array();	
	$(".ticket_type_select").each(function(){
		if(this.value=="2"){
			stu_arr.push(this);
		}
	});
	// 没有学生票
	if(stu_arr.length == 0){
		return true;
	}
	var stu_len = $("#stu_info_div_1 .insurance_wrap").length;
	if(stu_arr.length!=stu_len){
		showErrMsg("showStuGrid", 110, "点我完善学生票信息!");
		return false;
	}
	
	for (var i = 0; i < stu_arr.length; i++) {
		var obj = stu_arr[i];
		var index = obj.id.replace("ticket_type_","");
		if($("#user_name_"+index).val()!=$("#stu_name_"+index).val()){
			showErrMsg("stu_name_"+index, "110px", "请与乘客信息名字一致!");
			return false;
		}
	}
	$("#stu_info_div_1 input[type=text]").each(function(){
		var value = $(this).val();
		if($.trim(value)==""){
			showErrMsg(this.id, 110, "请输入相关信息!");
			return false;
		}
	});
	
	for (var i = 0; i < stu_arr.length; i++) {
		var obj = stu_arr[i];
		var index = obj.id.replace("ticket_type_","");
		var DBStudentInfo = {
				"stu_name":$("#stu_name_"+index).val(),
				"province_name":$("#province_name_"+index).val(),
				"school_name":$("#school_name_"+index).val(),
				"student_no":$("#student_no_"+index).val(),
				"enter_year":$("#enter_year_"+index).val(),
				"preference_from_station_name":$("#preference_from_station_name_"+index).val(),
				"preference_to_station_name":$("#preference_to_station_name_"+index).val(),
				"school_system":$("#school_system_"+index).val().replace("年","")
		};
		stuinfos.push(DBStudentInfo);
	}
	$("#stu_infos").val(JSON.stringify(stuinfos));
	return true;
}

	

