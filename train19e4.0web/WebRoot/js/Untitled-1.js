// JavaScript Document
//以下是banner轮播jquery
   //var mywt=$('#idx_ban_fl .Slides li:eq(0)').width() 这部是调取图片的宽度;
         $('#idx_ban_fl ol  li').mouseover(function(e){
                   $(this).addClass('on').siblings().removeClass();
                   var myindex=$('#idx_ban_fl ol li').index(this);
                   //var num=-(myindex*mywt);
                   $('#idx_ban_fl .Slides li').eq(myindex).fadeIn().siblings().hide(); 
                   mynum=myindex+1;
         });
		 //以上实现跟随ol 的li；banner 图发生变化
		 
		 
		 //以下是定时器启动，图片自动播放
         var mynum=1;
         function autoplay(){
                   if(mynum==3)
                   {
                    mynum=0;
                   }
                   $('#idx_ban_fl ol  li').eq(mynum).addClass('on').siblings().removeClass();
                   //$('#idx_ban_fl .Slides').animate({left:-(mynum*mywt)});
				   $('#idx_ban_fl .Slides li').eq(mynum).fadeIn().siblings().hide();
                   mynum++;
         }
         var timer=setInterval(autoplay,2000);
         
		 //这个事件是当鼠标移上。定时器来回开关
         $('#idx_ban_fl').hover(function(e){
                   clearInterval(timer);
         },function(){
               timer=setInterval(autoplay,2000);
         });
