function _printIT(obj) {
	//初始化打印方式
	$.ajax({
			url:"/print/findPrintSetup_no.jhtml?order_id="+obj,
			type: "POST",
			cache: false,
			success: function(data){
			if(data=='1'||data=='0'){
			printIT(obj);
			}
			}
		});
}
function  printIT(obj,printStyle,issmall){
		var url = "/print/orderView_no.jhtml?orderId="+obj;
		window.open(url);
}
