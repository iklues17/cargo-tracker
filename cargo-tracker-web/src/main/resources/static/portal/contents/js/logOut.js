
page.LogOut = (function(){
	
	var logout = function(){
		//ajax
		comm.I = {};
		comm.isLogedin = false;
		$.removeCookie('I');
		// Menu bar 다시 로딩
		page.MenuTop();
		// 로그인 화면으로 전환
		window.location.hash = "#log-in";
	}
	
	return function(){
		
		if(!comm.initPage()){
	    	return;
	    }
	
	    template.RenderOne({
	        target: "#body",
	        tagName: "div",
	        className: "log-out",
	        id: "bodyLogOut",
	        position: "new",
	        template: comm.getHtml("contents/log-out.html"),
	        data: undefined,
	        events: {
	            "click #btnLogOut": function(){
	            	logout();
	            }
	        }
	    });
	};
})();