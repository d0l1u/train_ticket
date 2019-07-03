//弹窗js
function openLayer(objId,alerthtml,owidth,oheight){
var arrayPageSize   = getPageSize();
var arrayPageScroll = getPageScroll();
if (!document.getElementById("popupAddr")){
var popupDiv = document.createElement("div");
popupDiv.setAttribute("id","popupAddr")
popupDiv.style.position = "absolute";
popupDiv.style.background = "#fff";
popupDiv.style.zIndex = 99;
var bodyBack = document.createElement("div");
bodyBack.setAttribute("id","bodybg")
bodyBack.style.position = "absolute";
bodyBack.style.width = "100%";
bodyBack.style.height = (arrayPageSize[1] + 1000 + 'px');
bodyBack.style.zIndex = 98;
bodyBack.style.top = 0;
bodyBack.style.left = 0;
bodyBack.style.filter = "alpha(opacity=50)";
bodyBack.style.opacity = 0.5;
bodyBack.style.background = "#ddf";
var mybody = document.getElementById(objId);
insertAfter(popupDiv,mybody);
insertAfter(bodyBack,mybody);
}
document.getElementById("bodybg").style.display = "";
var popObj=document.getElementById("popupAddr")
popObj.innerHTML = alerthtml;
popObj.style.display = "";
popObj.style.width = owidth;
popObj.style.height = oheight;
popObj.style.top = arrayPageScroll[1] + (arrayPageSize[3] - 250 - oheight) / 2 + 'px';
popObj.style.left = (arrayPageSize[0] - owidth + 70) / 2 + 'px';

}
function getConSize(conId){
var conObj=document.getElementById(conId)
conObj.style.position = "absolute";
conObj.style.left=-1000+"px";
conObj.style.display="";
var arrayConSize=[conObj.offsetWidth,conObj.offsetHeight]
conObj.style.display="none";
return arrayConSize;
}
function insertAfter(newElement,targetElement){
var parent = targetElement.parentNode;
if(parent.lastChild == targetElement){
parent.appendChild(newElement);
}
else{
parent.insertBefore(newElement,targetElement.nextSibling);
}
}
function getPageScroll(){
var yScroll;
if (self.pageYOffset) {
yScroll = self.pageYOffset;
} else if (document.documentElement && document.documentElement.scrollTop){
yScroll = document.documentElement.scrollTop;
} else if (document.body) {
yScroll = document.body.scrollTop;
}
arrayPageScroll = new Array('',yScroll)
return arrayPageScroll;
}
function getPageSize(){
var xScroll,yScroll;
if (window.innerHeight && window.scrollMaxY){
xScroll = document.body.scrollWidth;
yScroll = window.innerHeight + window.scrollMaxY;
} else if (document.body.scrollHeight > document.body.offsetHeight){
sScroll = document.body.scrollWidth;
yScroll = document.body.scrollHeight;
} else {
xScroll = document.body.offsetWidth;
yScroll = document.body.offsetHeight;
}
var windowWidth,windowHeight;
if (self.innerHeight) {
windowWidth = self.innerWidth;
windowHeight = self.innerHeight;
} else if (document.documentElement && document.documentElement.clientHeight) {
windowWidth = document.documentElement.clientWidth;
windowHeight = document.documentElement.clientHeight;
} else if (document.body) {
windowWidth = document.body.clientWidth;
windowHeight = document.body.clientHeight;
}
var pageWidth,pageHeight
if(yScroll < windowHeight){
pageHeight = windowHeight;
} else {
pageHeight = yScroll;
}
if(xScroll < windowWidth) {
pageWidth = windowWidth;
} else {
pageWidth = xScroll;
}
arrayPageSize = new Array(pageWidth,pageHeight,windowWidth,windowHeight)
return arrayPageSize;
}
function closeLayer(){
document.getElementById("popupAddr").style.display = "none";
document.getElementById("bodybg").style.display = "none";
return false;
}
var move=false,oldcolor,_X,_Y;
function StartDrag(obj){ 
obj.setCapture(); 
oldcolor=obj.style.backgroundColor;
obj.style.background="#999";
move=true;
var parentwin=document.getElementById("popupAddr");
_X=parentwin.offsetLeft-event.clientX
_Y=parentwin.offsetTop-event.clientY
}
function Drag(obj){       
if(move){
var parentwin=document.getElementById("popupAddr");
parentwin.style.left=event.clientX+_X;
parentwin.style.top=event.clientY+_Y;
}
}
function StopDrag(obj){   
obj.style.background=oldcolor;
obj.releaseCapture(); 
move=false;
}