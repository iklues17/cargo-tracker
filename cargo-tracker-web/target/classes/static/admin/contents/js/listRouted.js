/*
 * Javascript file attached to the listRouted.xhtml page. This invokes the Web Sockets functionality
 * to populate the live Cargo Tracker widget on the dashboard
 */

(function() {
	// defined a connection to a new socket endpoint
	var socket = new SockJS("http://localhost:28001/tracking");
	var client = Stomp.over(socket);

	client.connect({ }, function(frame) {
		// subscribe to the /transportstatus endpoint
		client.subscribe("/transportstatus", function(data) {
			var message = JSON.parse(data.body);
			var table = document.getElementById("listRoutedTab");
			
	        for(var count = 1, row; row = table.rows[count]; count++) {
	            if(row.id === message.trackingId) {
	                row.cells[1].innerHTML = message.origin;
	                row.cells[2].innerHTML = message.destination;
	                row.cells[3].innerHTML = message.lastKnownLocation;
	                row.cells[4].innerHTML = message.transportStatus;
	                
	                break;
	            }
	        }
		});
	});

})();