
page.LogIn = (function(){
	
	var ENV = {
		FORM_ID : "#formLogin"
	}
	
	var login = function(){
		
		var formdata = {
			emailAddress: $(ENV.FORM_ID).find("[name=emailAddress]").val(),
			password: $(ENV.FORM_ID).find("[name=password]").val()
		}
		formdata.password = sha256_digest(formdata.password);
		
		$.ajax({
			url: comm.server.url+"/user/log-in",
			method: "PUT",
			data: JSON.stringify(formdata),
			dataType: "json",
			contentType: "application/json",
			success: function(data, textStatus, jqXHR){
				// 쿠키저장
				$.cookie('I', JSON.stringify(data));
				// Menu bar 다시 로딩
				page.MenuTop();
				// Main 화면으로 전환
				var role = data.role
				if(role == "MEMBER"){
					window.location.hash = "";
				}else{
					window.location.href = "../admin/dashboard.html"
				}
			},
			error:function( jqXHR,  textStatus,  errorThrown){
				var errorObj = jqXHR.responseJSON;
				// 미가입
				if(errorObj.status === 404){
					comm.openModalForErrorMsg(errorObj.message, "You have to sign up");
				}
				// Password 오류
				else if(errorObj.status === 406){
					comm.openModalForErrorMsg(errorObj.message, "Try again");
				}else{
					comm.openModalForErrorMsg(textStatus, "You have a problem, Please Contact us");
				}
			},
			complete : function(text, xhr){
			}
		});
	}
	
	return function(){
		
		if(!comm.initPage()){
	    	return;
	    }
	
	    template.RenderOne({
	        target: "#body",
	        tagName: "div",
	        className: "log-in",
	        id: "bodyLogIn",
	        position: "new",
	        template: comm.getHtml("contents/log-in.html"),
	        data: undefined,
	        events: {
	            "click #btnCreateAccount": function() {
	    			window.location.hash = "#sign-up";
	            },
	            "click #btnLogIn": function(){
	            	login();
	            }
	        }
	    });
	};
})();