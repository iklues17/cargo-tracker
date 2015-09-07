"use strict";

comm.doNetwork = function (option) {
    $.ajax({

        url: option.url,
        headers: option.header || {},
        method: option.method || 'GET',
        //type (default: 'GET') //An alias for method. You should use type if you're using versions of jQuery prior to 1.9.0.
        mimeType: option.mimeType,

        async: option.async || true,
        //accepts (default: depends on DataType)
        beforeSend: option.beforeSend, //function( jqXHR jqXHR, PlainObject settings )
        //cache: (default: true, false for dataType 'script' and 'jsonp')
        complete: option.complete || function(jqXHR, textStatus){
			
		},
        contents: option.contents,
        contentType: option.contentType || 'application/x-www-form-urlencoded; charset=UTF-8',
        context: option.context,
        //converters: //(default: {"* text": window.String, "text html": true, "text json": jQuery.parseJSON, "text xml": jQuery.parseXML}),
        //crossDomain: //(default: false for same-domain requests, true for cross-domain requests),
        data: option.data,
        dataFilter: option.dataFilter,
        dataType: option.dataType, // (default: Intelligent Guess (xml, json, script, or html))
        error: option.error || function( jqXHR, textStatus, errorThrown ){

        },
//        xhrFields: {
//        	withCredentials: true
//        },
        global: option.global || true,

        //ifModified (default: false)
        //isLocal (default: depends on current location protocol)
        //jsonp
        //jsonpCallback
        //password
        //processData (default: true)
        //scriptCharset
        //statusCode (default: {})
        //timeout: Number
        //xhr (default: ActiveXObject when available (IE), the XMLHttpRequest otherwise)
        //xhrFields
        success: option.success || function(data, textStatus, jqXHR){
        	console.log(data);
		},

    });

//http://api.jquery.com/jquery.ajax/
};

