define([
	"underscore", 
	"backbone",
	"jquery",
	"text!templates/permissions/permissionsedit.html"], 
	function(_, Backbone, $, editTemplate) {
	
	var view = Backbone.View.extend({
		
		tagName: "div",
		
		events: {
			"click a#save": "save",
			"click a#cancel": "cancel"
		},
		
		render: function() {

			this.$el.html(_.template(editTemplate, this.model.toJSON()));
			return this;
		},
		
		initialize: function() {
			
			this.model.on("error", this.showErrors, this);
			this.model.on("change", this.render, this);
		},
		
		autofocus: "#permissionstring",

		save: function() {
						
			var permissionString = $("#permissionstring").val();

			var result = this.model.set({
				permissionString: permissionString
			});
			
			if(result) {
				this.model.save({}, {
					success: function() {
						Backbone.history.navigate("PermissionsList", true);		
					}
				});
			}
		},
		
		cancel: function() {
			
			Backbone.history.navigate("PermissionsList", true);
		},
		
		showErrors: function(model, errors) {
			
			if(errors.permissionsString) {
				this.highlightField("#permissionstring", "error", errors.permissionString);
			}
			else {
				this.highlightField("#permissionstring", "success");
			}
		}
	});
	
	return view;
});
