page.ChangeDestination = (function(){
	
	var ENV = {
		BOOKING_ID: "",
		ID_FORM: "#frmChangeDestination",
		ID_BTN_SUBMIT: "#btnChangeDestination",
		ID_BTN_CANCEL: "#btnBack"
	};
	
	var booking = {};
	
	var getCargoDetail = function(bookingId){

		console.log("API Call(bookingId:"+bookingId+") GET /booking/bookings/{bookingId}");
//		$.ajax({
//			url: "http://localhost:9999/tracker/cargos/" + trackingId,
//			method: "GET",
//			dataType: "json",
//			contentType: "application/json",
//			success: function(data, textStatus, jqXHR){
//				cargoDetails = data;
//				gridView.init();
//				formView.init();
//			},
//			complete : function(text, xhr){
//			}
//		});
		
	    booking = {
	     	bookingId: '1',
	     	origin: 'BUSAN',
	     	destination: 'SEOUL',
	        arrDate: '2015-09-09',
	        comodity: 'Phone',
	        quantity: 3,
	        status: 'Not Accepted',
	        misrouted: false,
	    	legs: []
		};
		
	};
	var formView = {
		
		init: function(){
			var that = this;
			$(ENV.ID_BTN_SUBMIT).on('click', function(e){
				that.doChangeDestination(that.getFormData());
			});
			$(ENV.ID_BTN_CANCEL).on('click', function(e){
				history.back();
			});
			
		},
		
		getFormData: function(){
			var formObj = comm.queryStringToJson($(ENV.ID_FORM).serialize());
			formObj.bookingId = ENV.BOOKING_ID;
			return formObj;
		},
		
		doChangeDestination : function(data){
			console.log("API Call(bookingId:"+data.bookingId+") PUT /booking/bookings/{bookingId}/change-destination");
//			$.ajax({
//				url: "http://localhost:9999/tracker/cargos/"+data.trackingId+"/change-destination",
//				method: "POST",
//				data: JSON.stringify(data),
//				dataType: "json",
//				contentType: "application/json",
//				error:function( jqXHR,  textStatus,  errorThrown){
//					console.log(textStatus);
//				},
//				complete : function(text, xhr){
//					location.href = "/admin/cargoDetail.html?trackingId="+data.trackingId;
//				}
//			});
		}
			
	};
	
	var init = function(bookingId){
		ENV.BOOKING_ID = bookingId;
		getCargoDetail(bookingId);
	}

	return function(bookingId){
		
		if(!comm.initPage()){
	    	return;
	    }
	    
		init(bookingId);
		
	    template.RenderOne({
	        target: "#body",
	        tagName: "div",
	        className: "chage-destination",
	        id: "bodyChangeDestination",
	        position: "new",
	        template: comm.getHtml("contents/change-destination.html"),
	        data: booking,
	        events: {
	        },

	        afterRender: function() { 
	    		formView.init();
	        	comm.appendSelectLocations($('#locations'));
	        } 
	    });

	};
})();