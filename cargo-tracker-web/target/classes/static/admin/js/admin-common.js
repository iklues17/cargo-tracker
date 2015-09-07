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
    } else if (location.hash.startsWith('#world-map')) {
    	$(".top-bar-section > div > ul.left > li").eq(4).addClass('active');
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

function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(location.search);
    return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

var getLocations = function(){
	return [
		{"name": "Chicago (USCHI)"	 , "unLocode": "USCHI"},
		{"name": "Dallas (USDAL)"	 , "unLocode": "USDAL"},
		{"name": "Guttenburg (SEGOT)", "unLocode": "SEGOT"},
		{"name": "Hamburg (DEHAM)"	 , "unLocode": "DEHAM"},
		{"name": "Hangzhou (CNHGH)"	 , "unLocode": "CNHGH"},
		{"name": "Helsinki (FIHEL)"	 , "unLocode": "FIHEL"},
		{"name": "Hong Kong (CNHKG)" , "unLocode": "CNHKG"},
		{"name": "Melbourne (AUMEL)" , "unLocode": "AUMEL"},
		{"name": "New York (USNYC)"	 , "unLocode": "USNYC"},
		{"name": "Rotterdam (NLRTM)" , "unLocode": "NLRTM"},
		{"name": "Shanghai (CNSHA)"	 , "unLocode": "CNSHA"},
		{"name": "Stockholm (SESTO)" , "unLocode": "SESTO"},
		{"name": "Tokyo (JNTKO)"	 , "unLocode": "JNTKO"}
	];
}

var appendSelectLocations = function($target){
	$.each(getLocations(), function(i){
		$target.append('<option value="'+this.unLocode+'">'+this.name+'</option>');
	});
}

var queryStringToJson = function(queryString){
	return (queryString || document.location.search).replace(/(^\?)/,'').split("&").map(function(n){return n = n.split("="),this[n[0]] = n[1],this}.bind({}))[0];
}