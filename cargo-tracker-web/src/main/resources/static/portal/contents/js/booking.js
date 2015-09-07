
page.Booking = (function () {
	
	var ENV = {
		ID_FORM: "#frmNewBooking",
		ID_BTN_BOOK: "#btnBooking",
		ID_BTN_CANCEL: "#btnBack"
	};
	
	var formView = {
		
		init: function(){
			var that = this;
			$(ENV.ID_BTN_BOOK).on('click', function(e){
				that.doBook(that.getFormData());
			});
			$(ENV.ID_BTN_CANCEL).on('click', function(e){
				window.location.href = '';
			});
			
		},
		
		getFormData: function(){
			var formObj = comm.queryStringToJson($(ENV.ID_FORM).serialize());
			formObj.userId = comm.I.id;
			return formObj;
		},
		
		doBook : function(data){
			console.log(data);
			$.ajax({
				url: comm.server.url+"/booking/bookings",
				method: "POST",
				data: JSON.stringify(data),
				dataType: "text",
				contentType: "application/json",
				success: function(data, textStatus, jqXHR){
					window.location.href = "";
				},
				error:function( jqXHR,  textStatus,  errorThrown){
					comm.openModalForErrorMsg(textStatus, "Contack us");
					console.log(textStatus);
				},
				complete : function(text, xhr){
				}
			});
		}
			
	};
	
	var init = function(){
	}

	return function(){
		
		if(!comm.initPage()){
	    	return;
	    }

	    var entryFromList = [
	        {value: "", id: "selLocOrigin", name:"originUnlocode", displayName: "Origin", type: "select", size: "1"},
	        {value: "", id: "selLocDestination", name:"destinationUnlocode", displayName: "Destination", type: "select", size: "1"},
	        {value: "", id: "iptArrivalDeadline", name:"arrivalDeadline", displayName: "Arrival Deadline", type: "date", size: "8", maxlength: "8", placeholder: "yyyy-MM-dd", required: true},
	        {value: "", id: "iptCommodity", name:"commodity", displayName: "Commodity", type: "text", required: true},
	        {value: "", id: "iptQuantity", name:"quantity", displayName: "Quantity", type: "number", min: "0", required: true}
		];
	    
	    template.RenderOne({
	        target: "#body",
	        tagName: "div",
	        className: "booking",
	        id: "bodyBooking",
	        position: "new",
	        template: comm.getHtml("contents/booking.html"),
	        data: {entryForm:entryFromList},
	        events: {
	        },

	        afterRender: function() { 
	    		formView.init();
	        	comm.appendSelectLocations($('#selLocOrigin'));
	        	comm.appendSelectLocations($('#selLocDestination'));
	        }
	    });
	}
})();