
page.SignUp = (function(){
	
	var ENV = {
		FORM_ID : "#formSignUp",
		SEL_COMPANY : "[name=company]",
		SEL_ROLE : "[name=role]"
	}
	
	var signup = function(){
		
		var formdata = {
			emailAddress: $(ENV.FORM_ID).find("[name=emailAddress]").val(),
			password: $(ENV.FORM_ID).find("[name=password]").val(),
			firstName: $(ENV.FORM_ID).find("[name=firstName]").val(),
			lastName: $(ENV.FORM_ID).find("[name=lastName]").val(),
			company: {
				id: $(ENV.SEL_COMPANY + " option:selected").val(),
				name: $(ENV.SEL_COMPANY + " option:selected").text()
			},
			role: $(ENV.SEL_ROLE + " option:selected").val()
		};
		
		formdata.password = sha256_digest(formdata.password);
		
		$.ajax({
			url: comm.server.url+"/user/sign-up",
			method: "POST",
			data: JSON.stringify(formdata),
			dataType: "json",
			contentType: "application/json",
			success: function(data, textStatus, jqXHR){
				window.location.hash = "#log-in";
			},
			error:function( jqXHR,  textStatus,  errorThrown){
				var errorObj = jqXHR.responseJSON;
//				if(){
//					comm.openModalForErrorMsg(textStatus, "You have a problem, Please Contact us");
//				}
			},
			complete : function(text, xhr){
			}
		});
		
	};
	
	var init = function(){
		comm.appendSelectCompanies($(ENV.SEL_COMPANY));
	};
	
	return function(){
		
		if(!comm.initPage()){
	    	return;
	    }
	
	    template.RenderOne({
	        target: "#body",
	        tagName: "div",
	        className: "sign-up",
	        id: "bodySignUp",
	        position: "new",
	        template: comm.getHtml("contents/sign-up.html"),
	        data: undefined,
	        events: {
	            "click #btnSignUp": function() {
	            	signup();
	            },
			    "click #btnBack": function() {
					window.location.hash = "#log-in";
			    }
	        },

	        afterRender: function(Dashboard) { 
	            init();
	        }
	    });
	};
})();
