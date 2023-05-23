(function(doc, win) {
	
	//rem布局
	var reCreateStyle = function() {
		var style;
		if(style = document.getElementById("forhtml")) {
			style.parentNode.removeChild(style);
		}
		style = document.createElement("style");
		style.id = "forhtml";
		var head=document.head||document.getElementsByTagName("head")[0];
	    head.appendChild(style);

		var fontSize = 100.00 * (parseFloat(document.documentElement.clientWidth, 10)) / 375.00;
		style.appendChild(document.createTextNode("html{font-size:" + fontSize + "px !important;}"));
	};

	var docEl = doc.documentElement,
		resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
		recalc = function() {
			reCreateStyle();
		},
		loadcalc = function() {
			reCreateStyle();

			var bodyEl = document.body;
			if(bodyEl.classList.contains("loading")) {
				bodyEl.classList.remove("loading");
			}
		}
	recalc();
	if(!doc.addEventListener) return;
	win.addEventListener(resizeEvt, loadcalc, false);
	doc.addEventListener('DOMContentLoaded', loadcalc, false);
})(document, window);
