"use strict";

(function() {

    var root = this;
    var comm = root.comm = {};
    var domain = root.domain = {};
    
}).call(this);


$(document).ready(function(){
    page.MenuTop();
});

$(document).foundation({
	offcanvas : {
		// Sets method in which offcanvas opens.
		// [ move | overlap_single | overlap ]
		open_method: 'overlap', 
		// Should the menu close when a menu link is clicked?
		// [ true | false ]
		close_on_click : false
	}
});

comm.server = {};
comm.server.url = "http://localhost:9999";

comm.isLogedin = false;

// Login 하면 저장되는 유저정보
comm.I = {};

comm.getHtml = function (path) {

    var getFile = $.ajax({
        url: path,
        async: false,
        success: function (data) {
            return data;
        }
    });

    return getFile.responseText;
};

comm.logout = function(){
//	var $signInMenu = $('[data-login="false"]');
//	$signInMenu.find('label').text('Logout');
//	page.signUpPage();
};

comm.queryStringToJson = function(queryString){
	return (queryString || document.location.search).replace(/(^\?)/,'').split("&").map(function(n){return n = n.split("="),this[n[0]] = n[1],this}.bind({}))[0];
};

comm.openModalForErrorMsg = function(errorMsg, followup){
	$('body').find("#myModal").remove();
	$('body').append('<div id="myModal" class="reveal-modal" data-reveal aria-labelledby="modalTitle" aria-hidden="true" role="dialog">'
			+ '<h2 id="modalTitle">Sorry. Your request is failed.</h2>'
			+  '<p class="lead">'+errorMsg+'</p>'
			+  '<p>'+followup+'</p>'
			+  '<a class="close-reveal-modal" aria-label="Close">&#215;</a>'
			+ '</div>');
	$('#myModal').foundation('reveal', 'open');
};

comm.openModalForSuccessMsg = function(successMsg){
	$('body').find("#myModal").remove();
	$('body').append('<div id="myModal" class="reveal-modal" data-reveal aria-labelledby="modalTitle" aria-hidden="true" role="dialog">'
			+ '<h2 id="modalTitle">Success</h2>'
			+  '<p class="lead">for '+successMsg+'</p>'
			+  '<a class="close-reveal-modal" aria-label="Close">&#215;</a>'
			+ '</div>');
	$('#myModal').foundation('reveal', 'open');
};

comm.getLocations = function(){
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

comm.getCompanies = function(){
	return [
		{"name": "Samsung SDS"	 , "companyId": "23489056"},
		{"name": "A사"	 , "companyId": "128034"},
		{"name": "B사"	 , "companyId": "34905"}
	];
}

comm.appendSelectLocations = function($target){
	$.each(comm.getLocations(), function(i){
		$target.append('<option value="'+this.unLocode+'">'+this.name+'</option>');
	});
}

comm.appendSelectCompanies = function($target){
	$.each(comm.getCompanies(), function(i){
		$target.append('<option value="'+this.companyId+'">'+this.name+'</option>');
	});
}

comm.queryStringToJson = function(queryString){
	return (queryString || document.location.search).replace(/(^\?)/,'').split("&").map(function(n){return n = n.split("="),this[n[0]] = n[1],this}.bind({}))[0];
}


Handlebars.registerHelper('table-head', function () {
    var th = '<th style="width:'+this.width+'" class="'+(this.hidden == true ? 'hide' : '')+'">'+this.display+'</th>';
    return th;
});

Handlebars.registerHelper('entry-form-center', function(){
	var fieldHtml = "";
	if(this.viewonly){
		fieldHtml = '<label class="form-input-to-label">'+this.value+'</label>';
	}else{
		fieldHtml = (this.type === 'select')?'<select ' : '<input type="'+this.type+'" ';
		fieldHtml += 'id="'+this.id+'" ';
		fieldHtml +=' name="'+this.name+'" ';
		fieldHtml +=(this.value)?' value="'+this.value+'" ': '';
		fieldHtml +=(this.size)?' size="'+this.size+'" ' : '';
		fieldHtml +=(this.min)?' min="'+this.min+'" ' : '';
		fieldHtml +=(this.maxlength)?' maxlength="'+this.maxlength+'" ' : '';
		fieldHtml +=(this.placeholder)?' placeholder="'+this.placeholder+'" ' : '';
		fieldHtml +=(this.required)?' '+(this.required?'required':' ') : '';
		fieldHtml +=(this.disabled)?' '+(this.disabled?'disabled':' ') : '';
		fieldHtml +=(this.type === 'select')?' ></select>' : ' />';
	}
	
	var row = '<div class="row entry-row">'
		+'<div class="small-12">'
		+ '<div class="row">'
		+  '<div class="small-4 columns">'
		+   '<label for="'+this.name+'" class="inline right">'+this.displayName+'</label>'
		+  '</div>'
		+  '<div class="small-5 left columns">'
		+   fieldHtml
		+  '</div>'
		+ '</div>'
		+'</div>'
		+'</div>';
	return row;
});