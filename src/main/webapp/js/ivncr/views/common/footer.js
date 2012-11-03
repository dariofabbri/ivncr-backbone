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

		initialize: function() {
			this.model.on("change:loggedOn", this.evaluateLogon, this);
		},

		onClose: function() {
			this.model.off("change:loggedOn", this.evaluateLogon);
		},

		render: function() {

			this.$el.html(_.template(footerTemplate, this.model.toJSON()));
			return this;
		},
		
		logout: function() {
			
			this.model.doLogout();
		},
		
		evaluateLogon: function() {
			
			if(!this.model.get("loggedOn")) {
				
				Backbone.history.navigate("login", true);
			}
		}
	});
	
	return view;
});
