page.bookingDetailSection = function (bookingId) {

	if(!comm.initPage()){
    	return;
    }
	
	var env = {
		targetId: "#divBookingDetail",
		tagName: "div",
		className: "booking-detail",
		id: "sectionBookingDetail",
		html: "contents/booking-detail.html"
	};

	//Dashboard에 종속된 화면이므로 대쉬보드 화면이 떠잇는지 확인 후 없으면 로딩
	if($(env.targetId).length === 0){
		page.Dashboard();
	}
	var datas;
	$.ajax({
		async: false,
		url: comm.server.url + "/booking/bookings/"+bookingId,
		method: "GET",
		dataType: "json",
		contentType: "application/json",
		success: function(data, textStatus, jqXHR){
			datas = data;
		},
		complete : function(text, xhr){
		}
	});
	
//    var datas = {
//     	bookingId: '1',
//     	origin: 'BUSAN',
//     	destination: 'SEOUL',
//        arrDate: '2015-09-09',
//        commodity: 'Phone',
//        quantity: 3,
//        status: 'Not Accepted',
//        misrouted: false,
//        trackingId: "ABC123",
//    	legs: [
//    	    {
//        		voyageNumber: "0200T",
//        		fromUnLocode: "USCHI",
//        		toUnLocode: "CNHKG",
//        		loadTime: "08/24/2015 06:31 오후 KST",
//        		unloadTime: "08/25/2015 09:09 오후 KST",
//        		from: "Chicago (USCHI)",
//        		to: "CNHKG (Hong Kong)"
//    		},{
//    			voyageNumber: "0300A",
//    			fromUnLocode: "CNHKG",
//    			toUnLocode: "USDAL",
//    			loadTime: "08/28/2015 05:34 오전 KST",
//    			unloadTime: "08/29/2015 07:11 오전 KST",
//    			from: "Hong Kong (CNHKG)",
//    			to: "USDAL (Dallas)"
//    		},{
//    			voyageNumber: "0301S",
//    			fromUnLocode: "USDAL",
//    			toUnLocode: "JNTKO",
//    			loadTime: "08/31/2015 11:48 오전 KST",
//    			unloadTime: "09/01/2015 01:49 오후 KST",
//    			from: "Dallas (USDAL)",
//    			to: "JNTKO (Tokyo)"
//    		}
//    	]
//	};
    
    datas.entryForm = [
        {value: datas.bookingId, viewonly: true, displayName: "Booking ID", name:"bookingId"},
        {value: datas.origin, viewonly: true, displayName: "Origin", name:"origin"},
        {value: datas.destination, viewonly: true, displayName: "Destination", name:"destination"},
        {value: datas.status, viewonly: true, displayName: "Booking Status", name:"status"},
        {value: datas.trackingId, viewonly: true, displayName: "Tracking ID", name:"trackingId"}
	];
    
    template.RenderOne({
        target: env.targetId,
        tagName: env.tagName,
        className: env.className,
        id: env.id,
        position: "new",
        template: comm.getHtml(env.html),
        data: datas,
        events: {
            "mouseover tbody>tr": function (e) {
            	$(e.currentTarget).addClass('recode-active');
            },
		    "mouseout tbody>tr": function (e) {
		    	$(e.currentTarget).removeClass('recode-active');
		    },
		    "click tbody>tr": function (e) {
		    	$(e.currentTarget).parent().find('.recode-selected').removeClass('recode-selected');
		    	$(e.currentTarget).addClass('recode-selected');
		    }
        }
    });
};