$(function(){
	var num=3;
	$('nbanner_ol li').mouseover(function(e) {
		num++
        $(this).addClass('currentimg').siblings().removeClass();
		
		
		$('.banner_bg li').eq($(this).index()).css('z-index',num).hide().show(500)
		
    });
})
