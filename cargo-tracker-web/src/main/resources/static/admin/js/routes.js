$(function(){

    var AppRouter = new (Backbone.Router.extend({
        routes: {
        	// Menu View
            "": "showDashboard",
            "booking": "showBooking",
            "track": "showTrack",
            "track/:trackingId": "showTrack",
            "about": "showAbout",
            "world-map": "showWorldMap",
            // Right Menu view
            "my-page": "showMyPage",
            "my-page/:tabName": "showMyPage",
            "log-in": "showLogIn",
            "log-out": "showLogOut",
            "sign-up": "showSignUp",
            // Subview
            "detail/:status/:id": "showCargoDetail",
            "detail/:status/:id/change-destination": "showChangeDestination",
            "detail/:status/:id/select-itinerary": "showSelectItinerary"
            // Util View
//            "success": "showSuccess"
            
        },
        showDashboard: adminPage.Dashboard,
        showBooking: adminPage.Registration,
        showTrack: adminPage.Track,
        showAbout: adminPage.About,
        showWorldMap: adminPage.WorldMap,
//        
        showMyPage: adminPage.MyPage,
        showLogIn: page.LogIn,
        showLogOut: page.LogOut,
        showSignUp: page.SignUp,
        
        showCargoDetail: adminPage.cargoDetailSection,
        showChangeDestination: adminPage.ChangeDestination,
        showSelectItinerary: adminPage.SelectItinerary
//        
//        showSuccess: page.Success

    }));

    Backbone.history.start();

});