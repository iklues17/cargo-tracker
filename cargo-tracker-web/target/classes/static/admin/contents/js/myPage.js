
adminPage.MyPage = (function(){
	
	var ENV = {
		FORM_ID: "#formMyInfo",
		BTN_GROUP_TOP: "#btnTop",
		BTN_GROUP_BOTTOM: "#btnBottom",
		SEL_COMPANY: "[name=company]",
		SEL_ROLE: "[name=role]",
		BTN_MOD: "#btnModify",
		BTN_PWD_RE: "#btnPwdReset",
		BTN_WITHDROW: "#btnWithdrowal",
		BTN_SAVE: "#btnSave",
		BTN_BACK: "#btnBack"
	};
	
	var formView = {
		// VIEW, MODIFY, PWD, WITHDROWAL
		mode : "VIEW",
		
		init: function(){
			this.resetView();
		},
		
		resetView: function(){

			$(ENV.FORM_ID).find("[name=password]").attr('disabled', true);
			$(ENV.FORM_ID).find("[name=password]").parent().hide();
			$(ENV.FORM_ID).find("[name=firstName]").attr('disabled', true);
			$(ENV.FORM_ID).find("[name=firstName]").parent().show();
			$(ENV.FORM_ID).find("[name=lastName]").attr('disabled', true);
			$(ENV.FORM_ID).find("[name=lastName]").parent().show();
			$(ENV.FORM_ID).find("[name=company]").attr('disabled', true);
			$(ENV.FORM_ID).find("[name=company]").parent().show();
			$(ENV.FORM_ID).find("[name=role]").attr('disabled', true);
			$(ENV.FORM_ID).find("[name=role]").parent().show();
			
			$(ENV.FORM_ID).find("[name=emailAddress]").val(comm.I.emailAddress);
			$(ENV.FORM_ID).find("[name=password]").val('1234567890');
			$(ENV.FORM_ID).find("[name=firstName]").val(comm.I.firstName);
			$(ENV.FORM_ID).find("[name=lastName]").val(comm.I.lastName);
			$(ENV.FORM_ID).find("[name=company]").val(comm.I.company.id);
			$(ENV.FORM_ID).find("[name=role]").val(comm.I.role);
			
			
			$(ENV.BTN_GROUP_BOTTOM).hide();
			$("[data-mode]").attr("data-mode", "view");
		},
		
		toModifyMode: function(){
			
			this.resetView();
			
			$(ENV.FORM_ID).find("[name=firstName]").attr('disabled', false);
			$(ENV.FORM_ID).find("[name=lastName]").attr('disabled', false);
			$(ENV.FORM_ID).find("[name=company]").attr('disabled', false);
			$(ENV.FORM_ID).find("[name=role]").attr('disabled', false);
			$(ENV.BTN_GROUP_BOTTOM).show();
			
			$("[data-mode]").attr("data-mode", "modify");
		},
		
		toPwdResetMode: function(){
			
			this.resetView();
			
			$(ENV.FORM_ID).find("[name=password]").attr('disabled', false);
			$(ENV.FORM_ID).find("[name=password]").val('');
			$(ENV.FORM_ID).find("[name=password]").parent().show();
			
			$(ENV.FORM_ID).find("[name=firstName]").parent().hide();
			$(ENV.FORM_ID).find("[name=lastName]").parent().hide();
			$(ENV.FORM_ID).find("[name=company]").parent().hide();
			$(ENV.FORM_ID).find("[name=role]").parent().hide();
			$(ENV.BTN_GROUP_BOTTOM).show();
			
			$("[data-mode]").attr("data-mode", "pwdreset");
		},
		
		toWithdrowalMode: function(){
			
			this.resetView();
			
			$(ENV.FORM_ID).find("[name=password]").attr('disabled', false);
			$(ENV.FORM_ID).find("[name=password]").val('');
			$(ENV.FORM_ID).find("[name=password]").parent().show();
			
			$(ENV.FORM_ID).find("[name=firstName]").parent().hide();
			$(ENV.FORM_ID).find("[name=lastName]").parent().hide();
			$(ENV.FORM_ID).find("[name=company]").parent().hide();
			$(ENV.FORM_ID).find("[name=role]").parent().hide();
			$(ENV.BTN_GROUP_BOTTOM).show();
			
			$("[data-mode]").attr("data-mode", "withdrowal");
		}
	};
	
	var btnFn = {
		
		cancel: function(){
			formView.resetView();
		},
		
		save: function(){
			//정보수정
			if($("[data-mode=modify]").size() > 0){
				this.doModify();
			}
			//패스워드 변경
			else if($("[data-mode=pwdreset]").size() > 0){
				this.doPwdReset();
			}
			//탈퇴
			else if($("[data-mode=withdrowal]").size() > 0){
				this.doWithdrowal();
			}
			else{
				alert("There is no change")
			}
		},
		
		doModify: function(){
			
			var formdata = {
				emailAddress: $(ENV.FORM_ID).find("[name=emailAddress]").val(),
				firstName: $(ENV.FORM_ID).find("[name=firstName]").val(),
				lastName: $(ENV.FORM_ID).find("[name=lastName]").val(),
				company: {
					id: $(ENV.FORM_ID).find("[name=company] option:selected").val(),
					name: $(ENV.FORM_ID).find("[name=company] option:selected").val()
				},
				role: $(ENV.FORM_ID).find("[name=role] option:selected").val()
			}
			
			$.ajax({
				url: comm.server.url+"/user/users",
				method: "PUT",
				data: JSON.stringify(formdata),
				dataType: "json",
				contentType: "application/json",
				success: function(data, textStatus, jqXHR){
					// 쿠키저장
					$.cookie('I', JSON.stringify(data));
					// Menu bar 다시 로딩
					page.MenuTop();
					// 사용자정보 재설정
					formView.resetView();
					comm.openModalForSuccessMsg("Modify your infomation");
				},
				error:function( jqXHR,  textStatus,  errorThrown){
					var errorObj = jqXHR.responseJSON;
					comm.openModalForErrorMsg(textStatus, "You have a problem, Please Contact us");
				},
				complete : function(text, xhr){
				}
			});
		},
		
		doPwdReset: function(){
			
			var formdata = {
				emailAddress: $(ENV.FORM_ID).find("[name=emailAddress]").val(),
				password: $(ENV.FORM_ID).find("[name=password]").val()
			}
			
			formdata.password = sha256_digest(formdata.password);
			
			$.ajax({
				url: comm.server.url+"/user/users/password",
				method: "PUT",
				data: JSON.stringify(formdata),
				dataType: "json",
				contentType: "application/json",
				success: function(data, textStatus, jqXHR){
					comm.openModalForSuccessMsg("Modify your Password");
				},
				error:function( jqXHR,  textStatus,  errorThrown){
					var errorObj = jqXHR.responseJSON;
					comm.openModalForErrorMsg(textStatus, "You have a problem, Please Contact us");
				},
				complete : function(text, xhr){
				}
			});
		},
		
		doWithdrowal: function(){
			if(confirm("정말 탈퇴하시겠습니까?")){
				
				var formdata = {
					emailAddress: $(ENV.FORM_ID).find("[name=emailAddress]").val(),
					password: $(ENV.FORM_ID).find("[name=password]").val()
				}
				
				formdata.password = sha256_digest(formdata.password);
				
				$.ajax({
					url: comm.server.url+"/user/withdrawal",
					method: "POST",
					data: JSON.stringify(formdata),
//					dataType: "json",
					contentType: "application/json",
					success: function(data, textStatus, jqXHR){
						comm.openModalForSuccessMsg("Goodbye my friend~!");
						logout();
					},
					error:function( jqXHR,  textStatus,  errorThrown){
						comm.openModalForErrorMsg(textStatus, "You have a problem, Please Contact us");
					},
					complete : function(text, xhr){
					}
				});
			}
		},
		
		init: function(){
			var that = this;

			$(ENV.BTN_MOD).on('click', function(e){
				$(ENV.BTN_GROUP_TOP).find(".active").removeClass("active");
				$(this).addClass('active');
				formView.toModifyMode();
			});
			$(ENV.BTN_PWD_RE).on('click', function(e){
				$(ENV.BTN_GROUP_TOP).find(".active").removeClass("active");
				$(this).addClass('active');
				formView.toPwdResetMode();
			});
			$(ENV.BTN_WITHDROW).on('click', function(){
				$(ENV.BTN_GROUP_TOP).find(".active").removeClass("active");
				$(this).addClass('active');
				formView.toWithdrowalMode();
			});
			$(ENV.BTN_SAVE).on('click', function(){
				$(ENV.BTN_GROUP_TOP).find(".active").removeClass("active");
				that.save();
			});
			$(ENV.BTN_BACK).on('click', function(){
				$(ENV.BTN_GROUP_TOP).find(".active").removeClass("active");
				that.cancel();
			});
		}
	};
	
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
	
	var init = function(){
		comm.appendSelectCompanies($(ENV.SEL_COMPANY));
		$(ENV.SEL_COMPANY).val(comm.I.company.id);
		formView.init();
		btnFn.init();
	};
	
	return function(){

		if(!comm.initPage()){
	    	return;
	    }
		
		var datas = comm.I;
		
	    template.RenderOne({
	        target: "#body",
	        tagName: "div",
	        className: "my-page",
	        id: "bodyMyPage",
	        position: "new",
	        template: comm.getHtml("contents/my-page.html"),
	        data: {},
	        events: {},

	        afterRender: function(Dashboard) { 
	            init();
	        }
	    });
	};
})();