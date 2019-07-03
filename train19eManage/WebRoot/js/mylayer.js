function showlayer(title,mysrc,areax,areay){
	$.layer({
		type : 2,
		
	     fix: false,
	    moveOut : false,
	    move : ['.xubox_title' , false],
		title : [title,true],
		iframe : {src : mysrc},
		area : [areax , areay],
		offset : ['40px', ''],
		close : function(index){
			//layer.msg('您获得了子窗口标记：' + layer.getChildFrame('#name', index).val(),3,1);
			layer.close(index);
			window.location.reload(); 
		}
	});
}
function reloadPage(){
		window.location.reload(); 
	 	//var index = parent.layer.getFrameIndex(window.name);
		//parent.layer.close(index);
		//parent.reloadPage();
}

function backPage()
{
	var index = parent.layer.getFrameIndex(window.name);
	parent.layer.close(index);	
}