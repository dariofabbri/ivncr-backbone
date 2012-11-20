define([
	"underscore", 
	"backbone",
	"jquery",
	"text!templates/common/navbar.html"], 
	function(_, Backbone, $, navbarTemplate) {
	
	var navbarView = Backbone.View.extend({

		events: {
			"click a": "checkDisabled"
		},
		
		render: function() {

			this.$el.html(_.template(navbarTemplate, this.model.toJSON()));
			
			var that = this;
			_.each($("ul.dropdown-menu>li[action]", this.$el), function(item) {
				
				var action = $(item).attr("action");
				$(item).removeAttr("action");
				$(item).removeClass("disabled");
				if(!that.model.checkGrant(action)) {
					$(item).addClass("disabled");
				}
			});
			
			return this;
		},
		
		checkDisabled: function(e) {
			
			if($(e.target).parent().hasClass("disabled")) {
				e.preventDefault();
			}
		}
	});
	
	return navbarView;
});
