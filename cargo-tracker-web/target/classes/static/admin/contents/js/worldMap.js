
adminPage.WorldMap = (function () {
	
	return function(){
	    if(!comm.initPage()){
	    	return;
	    }
	    
	    template.RenderOne({
	        target: "#body",
	        tagName: "div",
	        className: "about",
	        id: "bodyAbout",
	        position: "new",
	        template: comm.getHtml("contents/map.html"),
	        data: undefined,
	        events: {
	        }
	    });
	};
})();