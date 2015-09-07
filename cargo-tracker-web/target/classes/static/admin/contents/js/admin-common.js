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