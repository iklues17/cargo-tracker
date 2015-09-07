$(function(){

    var AppRouter = new (Backbone.Router.extend({
        routes: {
        	// Menu View
            "": "showDashboard",
            "booking": "showBooking",
            "track": "showTrack",
            "track/:trackingId": "showTrack",
            "about": "showAbout",
            // Right Menu view
            "my-page": "showMyPage",
            "my-page/:tabName": "showMyPage",
            "log-in": "showLogIn",
            "log-out": "showLogOut",
            "sign-up": "showSignUp",
            // Subview
            "detail/:bookingId": "showBookingDetail",
            "detail/:bookingId/change-destination": "showChangeDestination",
            // Util View
            "success": "showSuccess"
            
        },
        showDashboard: page.Dashboard,
        showBooking: page.Booking,
        showTrack: page.Track,
        showAbout: page.About,
        
        showMyPage: page.MyPage,
        showLogIn: page.LogIn,
        showLogOut: page.LogOut,
        showSignUp: page.SignUp,
        
        showChangeDestination: page.ChangeDestination,
        showBookingDetail: page.bookingDetailSection,
        
        showSuccess: page.Success

    }));

    Backbone.history.start();

});