adminPage.Track = (function() {
	var tracks = {}
	
	var formView = {
		
		ID: {
			FORM: "#trackingForm",
			INPUT_TRACKING_ID : "#inputTrackingId",
			BTN_SUBMIT : "#btnTracking"
		},
		
		init: function(){
			_this = this;
		
			this.focus();
			$(this.ID.INPUT_TRACKING_ID).on('keydown', function(e){
				if(e.keyCode == 13) {
					_this.getTrack();
				}
			});
			
			$(this.ID.FORM).submit(function(e){
				e.preventDefault();
				_this.getTrack();
			});
		},
		
		focus: function(){
			$(this.ID.INPUT_TRACKING_ID).trigger('focus');
		},
		
		getTrack: function(){
			trackingView.getTrackingById($(this.ID.INPUT_TRACKING_ID).val());
		}	
	
	};
	
	var trackingView = {
		
		ID: {
			FORM: "#trackingForm",
			VIEW_PANEL: "#result",
			TRACKING_ID: "#spanTrackingId",
			STATUS: "#spanStatus",
			DESTINATION: "#spanDestination",
			ETA: "#spanEta",
			NEXT_ACT: "#pNextAct",
			HISTORY: "#ulHandlingEvents"
		},
		
		init: function(){
			$(this.ID.VIEW_PANEL).hide();
		},
		
		getTrackingById: function(trackingId){
			$.ajax({
				url: "http://localhost:9999/tracker/cargos/"+trackingId+"/tracks",
				method: "GET",
				dataType: "json",
				contentType: "application/json",
				success: function(data, textStatus, jqXHR){
					//console.log(data);
					tracks.cargo = data;
					tracks.events = data.handlingEventViewDto;
					trackingView.printTracking(tracks);
				},
				error: function(jqXHR, textStatus, errorThrown){
		        	console.log("error : " + textStatus + ", " + errorThrown);
		        }
			});
			
			return tracks;
		},
		
		clear: function(){
			$(this.ID.VIEW_PANEL).hide();
		},
		
		printTracking: function(track){
			
			if(track !== undefined){
				$(this.ID.VIEW_PANEL).show();
			}
			
			$(this.ID.TRACKING_ID).text(track.cargo.trackingId);
			$(this.ID.STATUS).text(track.cargo.statusText);
			$(this.ID.DESTINATION).text(track.cargo.destination);
			$(this.ID.ETA).text(track.cargo.eta);
			$(this.ID.NEXT_ACT).text(track.cargo.nextExpectedActivity);

			$("[aria-cargo-misdirected]").attr('aria-cargo-misdirected', track.cargo.misdirected);

			if(track.events !== undefined && track.events.length > 0){
				$("[aria-cargo-events-empty='false']").attr('aria-cargo-events-empty', true);
				this.printHandlingHistory(track.events);
			}
		},
		
		printHandlingHistory: function(handlingHistory){
			this.removeHandlingHistory();
			
			var rootUl = this.ID.HISTORY;
			$.each(handlingHistory, function(event){
				$(rootUl).append('<li><p><img style="vertical-align: top;" src="/images/'+ (this.expected ? 'tick' : 'cross')+'.png" alt="" /><span style="padding-left:5px;">' + this.description+'</span></p></li>')
			})
		},
		
		removeHandlingHistory: function(){
			$(this.ID.HISTORY).children().remove();
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
	        template: comm.getHtml("contents/track.html"),
	        data: undefined,
	        events: {
	        },

	        afterRender: function(Dashboard) { 
				formView.init();
				trackingView.init();
	        } 
	    });
	};
})();