
(function() {

    var root = this;
    var template = root.template = {};
    var page = root.page = {};
    var adminPage = root.adminPage = {};

    template.RenderOne = function (opt) {

        var tmpl = Handlebars.compile(opt.template);

        var elem = new (Backbone.View.extend(opt));

        elem.$el.html(tmpl(opt.data));

        $(elem.el).addClass(opt.className).attr('id', opt.id);

        if (opt.position == "new") {
            $(opt.target).html(elem.el);
        } else if (opt.position == "prepend") {
            $(opt.target).prepend(elem.el);
        } else if (opt.position == "before") {
            $(opt.target).before(elem.el);
        } else if (opt.position == "after") {
            $(opt.target).after(elem.el);
        } else {
            $(opt.target).append(elem.el);
        }
        
        if(opt.afterRender){
        	opt.afterRender();
        }
    };

    template.RenderList = function (opt) {

    };

}).call(this);


