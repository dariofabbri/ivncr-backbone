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
