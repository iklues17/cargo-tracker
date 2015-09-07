		
page.Dashboard = (function(){
	var bookedCargos = [];
	
	var getTrackings = {
		init : function(){
			var userId = comm.I.id;
			$.ajax({
				async: false,
				url: comm.server.url + "/booking/bookings/of/"+userId,
				method: "GET",
				dataType: "json",
				contentType: "application/json",
				success: function(data, textStatus, jqXHR){
					bookedCargos = data;
				},
				complete : function(text, xhr){
				}
			});
		}
	};
	
	var routedView = {
		
		ID: {
			grid: "#gridRouted"
		},
		
		init: function(){
			getTrackings.init();
			page.bookedCargosGrid(bookedCargos);
		}
	};
	
	return function(){

		if(!comm.initPage()){
	    	return;
	    }

	    template.RenderOne({
	        target: "#body",
	        tagName: "div",
	        className: "dashboard",
	        id: "bodyDashboard",
	        position: "new",
	        template: comm.getHtml("contents/dashboard.html"),
	        data: {},
	        events: {
	            "click .dashboard": function () {
	                alert("dashboard!");
	            }
	        },

	        afterRender: function(Dashboard) { 
	            routedView.init();
	        } 
	    });

	};
})();