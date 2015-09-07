adminPage.Registration = (function() {
	
	var formView = {
			
		ID: {
			FORM: "#frmRegistration",
			SEL_ORIGIN: "#selLocOrigin",
			SEL_DESTIN: "#selLocDestination",
			BTN_SUBMIT: "#btnBooking"
		},
		
		init: function(){
			_this = this;
		
			$(this.ID.FORM).submit(function(e){
				e.preventDefault();
				_this.doRegister(queryStringToJson($(this).serialize()));
			});
			
			comm.appendSelectLocations($(this.ID.SEL_ORIGIN));
			comm.appendSelectLocations($(this.ID.SEL_DESTIN));
			
		},
		
		doRegister : function(data){
			$.ajax({
				url: "http://128.0.0.1:9999/tracker/cargos/registration",
				method: "POST",
				data: JSON.stringify(data),
				dataType: "text",
				contentType: "application/json",
				success: function(data, textStatus, jqXHR){
					location.href = "/admin/cargoDetail.html?trackingId="+data;
				},
				error: function(jqXHR, textStatus, errorThrown){
					console.log(jqXHR);
				},
				complete : function(text, xhr){
					
				}
			});
		}
	};
	
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
	        template: comm.getHtml("contents/registration.html"),
	        data: undefined,
	        events: {
	        },

	        afterRender: function(Dashboard) { 
	        	formView.init();
	        } 
	    });
	};
})();