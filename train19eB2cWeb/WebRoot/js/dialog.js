/*
openID=显示按钮,conID=需要显示的div,closeID=关闭按钮
Downloads By http://www.veryhuo.com
解决了:
1.可以遮挡ie6下的select元素 但是在ie6下div没有透明度
2.弹出的div可以一直在浏览器屏幕中间显示
问题:
1.目前不支持.class 只支持#id
2.需要显示的div需要自己设置css
3.在ie6下需要设置css
例如div {_position: absolute;_top: expression(documentElement.scrollTop + 340 + "px"); }
4.ie6下背景div没有透明度 这里我上网搜到的结果都不能解决 如果您有方法请给我留言
 */
var _CalF = { // 便捷方法
	$ : function(id) {
		return document.getElementById(id)
	},
	create : function(id) {
		return document.createElement(id)
	},
	append : function(id) {
		return document.body.appendChild(id)
	},
	remove : function(id) {
		return document.body.removeChild(id)
	}
}
function popup(openID, conID, closeID) {
	this.onclick(openID,conID,closeID);
}
popup.prototype = {
	cssStyle : "z-index:9999;width:100%;position:absolute;left:0;top:0;filter:alpha(opacity = 50);opacity:0.5;border:0;overflow:auto;",
	createShadowDiv : function() { // 背景遮罩
		var shadowDiv = _CalF.create("div");
		shadowDiv.id = "shadowDiv";
		shadowDiv.style.cssText = this.cssStyle;
		shadowDiv.style.height = document.body.scrollHeight + "px";
		shadowDiv.style.backgroundColor = "#000"
		shadowDiv.style.zindex = 100;
		_CalF.append(shadowDiv);
	},
	createIframe : function() { // iframe
		var iframe = _CalF.create("iframe");
		iframe.id = "shadowIframe";
		iframe.style.cssText = this.cssStyle;
		iframe.style.height = document.body.scrollHeight + "px";
		iframe.style.zindex = 99;
		_CalF.append(iframe);
	},
	removeDiv : function() {
		_CalF.remove(_CalF.$("shadowDiv"));
		_CalF.remove(_CalF.$("shadowIframe"));
	},
	onclick : function(openID, conID, closeID) {
		var that = this;
		_CalF.$(openID).onclick = function() {
			if (window.ActiveXObject) { // ie6
				if (!window.XMLHttpRequest) {
					document.body.style.cssText = "_background-image: url(about:blank);_background-attachment: fixed;";
				}
			}
			that.createIframe();
			that.createShadowDiv();
			_CalF.$(conID).style.display = "block";
		}
		document.getElementById(closeID).onclick = function() {
			_CalF.$(conID).style.display = "none";
			that.removeDiv();
		}
	}
}