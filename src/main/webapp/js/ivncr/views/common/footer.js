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
			
			this.model.destroy({success: function() {
				
				Backbone.history.navigate("login", true);
			}});
		}
	});
	
	return view;
});
