"use strict";

page.successPage = function (options) {

	var options = {
		message: options.message || "Your Request is complete!",
		buttons: [{
				id: "btnGoHome",
				name: "Go Home"
			}
		]
	};
	
    template.RenderOne({
        target: "#content",
        tagName: "div",
        className: "success",
        id: "success-content",
        position: "new",
        template: comm.getHtml("contents/success.html"),
        data: options,
        events: options.handler || {
            "click #btnGoHome": function() {
            	page.welcome();
            }
        }
    });
};
