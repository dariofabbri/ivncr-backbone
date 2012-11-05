define([
	"underscore", 
	"backbone",
	"jquery",
	"text!templates/common/footer.html"], 
	function(_, Backbone, $, footerTemplate) {
	
	var view = Backbone.View.extend({

		events: {
			"click a#logout": "logout"
		},

		render: function() {

			this.$el.html(_.template(footerTemplate, this.model.toJSON()));
			return this;
		},
		
		logout: function() {
			
			// Try to destroy the logininfo model. Either case, success or
			// error, the flow is redirected to login page, which is what the
			// user requires even if it is not possible, for some reason, to
			// destroy the session (already expired?)...
			//
			this.model.destroy({
				success: function() {
					Backbone.history.navigate("login", true);
				},
				error: function() {
					Backbone.history.navigate("login", true);
				}
			});
		}
	});
	
	return view;
});
