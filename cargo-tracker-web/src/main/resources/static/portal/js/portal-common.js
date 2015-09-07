comm.initPage = function () {
	
	var movePage = true;
	if(!comm.isLogedin){
		if(!(window.location.hash.startsWith("#log") || window.location.hash.startsWith("#sign"))){
			window.location.hash = "#log-in";
			movePage = false;
		}
	}
	
//    $("#content").html('');

//    $("#header > .icon-bar > .item").removeClass('active');

    $(".top-bar-section > div > ul.left > li").removeClass('active');
    $(".top-bar-section > div > ul.right > li").removeClass('active');
    
    if (location.hash.startsWith('#booking')) {
    	$(".top-bar-section > div > ul.left > li").eq(1).addClass('active');
    } else if (location.hash.startsWith('#track')) {
    	$(".top-bar-section > div > ul.left > li").eq(2).addClass('active');
    } else if (location.hash.startsWith('#about')) {
    	$(".top-bar-section > div > ul.left > li").eq(3).addClass('active');
    }
    // Top Right Bar - login state
    else if (location.hash.startsWith("#my-page")) {
    	$(".top-bar-section > div > ul.right > li").eq(0).addClass('active');
    } else if (location.hash.startsWith("#log-out")) {
    	$(".top-bar-section > div > ul.right > li").eq(1).addClass('active');
    }
    // Top Rigth Bar - logout state
    else if (location.hash.startsWith('#log-in')) {
    	$(".top-bar-section > div > ul.right > li").eq(0).addClass('active');
    } else if (location.hash.startsWith('#sign-up')) {
    	$(".top-bar-section > div > ul.right > li").eq(1).addClass('active');
    } else {
    	$(".top-bar-section > div > ul.left > li").eq(0).addClass('active');
    }

    return movePage;
};