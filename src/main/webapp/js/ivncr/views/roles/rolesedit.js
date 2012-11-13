define([
	"underscore", 
	"backbone",
	"jquery",
	"ivncr/collections/permissions",
	"ivncr/views/roles/rolespermissionslist",
	"text!templates/roles/rolesedit.html"], 
	function(_, Backbone, $, Permissions, PermissionsView, editTemplate) {
	
	var view = Backbone.View.extend({
		
		tagName: "div",
		
		events: {
			"click a#save": "save",
			"click a#cancel": "cancel"
		},

		childViews: [],

		render: function() {

			this.cleanChildViews();

			this.$el.html(_.template(editTemplate, this.model.toJSON()));
			
			var permissions = new Permissions();
			permissions.url = "api/roles/" + this.model.id + "/permissions";
			permissions.fetch();
			
			// Create and render the permissions list view.
			//
			var permissionsView = new PermissionsView({
				collection: permissions
			});
			$("div#permissions", this.el).append(permissionsView.render().el);
			
			// Store the subview in the list of child views, to avoid
			// memory leaks.
			//
			this.childViews.push(permissionsView);
				
			
			return this;
		},
		
		initialize: function() {
			
			this.model.on("error", this.showErrors, this);
			this.model.on("change", this.render, this);
		},
		
		autofocus: "#rolename",

		save: function() {
						
			var rolename = $("#rolename").val();
			var description = $("#description").val();

			var result = this.model.set({
				rolename: rolename,
				description: description
			});
			
			if(result) {
				this.model.save({}, {
					success: function() {
						Backbone.history.navigate("RolesList", true);		
					}
				});
			}
		},
		
		cancel: function() {
			
			Backbone.history.navigate("RolesList", true);
		},
		
		showErrors: function(model, errors) {
			
			if(errors.rolename) {
				this.highlightField("#rolename", "error", errors.rolename);
			}
			else {
				this.highlightField("#rolename", "success");
			}
			
			if(errors.description) {
				this.highlightField("#description", "error", errors.description);
			}
			else {
				this.highlightField("#description", "success");
			}
		}
	});
	
	return view;
});
