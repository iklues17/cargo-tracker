"use strict";

page.MenuTop = (function () {
	
	return function(){

		var I = $.cookie('I');
		if(I !== undefined){
			comm.isLogedin = true;
			comm.I = JSON.parse(I);
		}
		
		var datas = {
			isLogedin : comm.isLogedin,
			emailAddress: comm.I.emailAddress,
			firstName: comm.I.firstName,
			lastName: comm.I.lastName
		};
		
	    template.RenderOne({
	        target: ".top-bar-section",
	        tagName: "div",
	        className: "menu-top",
	        id: "menuTop",
	        position: "new",
	        template: comm.getHtml("menu-top.html"),
	        data: datas,
	        events: {
	        }
	    });
	};
})();