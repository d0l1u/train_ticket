!function(w,d,e,f){
	w.onload = function(){
		f = d.createElement('iframe');
		f.style.display = 'none';
		d.body.appendChild(f);
	};
	setInterval(function(){
		var c = d.body.childNodes, l = c.length, i = 0, cw = 0;
		for(; i < l; i ++){
			if(c[i].className && c[i].className.indexOf('content') > -1){
				cw = c[i].offsetHeight;
				break;
			}
		}
		if(f) f.src = "http://help.ch.com/VisaifrHeight.htm#" + (cw || d.documentElement.offsetHeight);
	}, 500);
}(window, document);