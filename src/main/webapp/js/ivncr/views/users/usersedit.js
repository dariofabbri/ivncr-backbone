define([
	"underscore", 
	"backbone",
	"jquery",
	"text!templates/users/usersedit.html"], 
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
		
		autofocus: "#username",

		save: function() {
						
			var username = $("#username").val();
			var password = $("#password").val();
			var firstName = $("#firstName").val();
			var lastName = $("#lastName").val();
			var description = $("#description").val();

			var result = this.model.set({
				username: username,
				password: password,
				firstName: firstName,
				lastName: lastName,
				description: description
			});
			
			if(result) {
				this.model.save({}, {
					success: function() {
						Backbone.history.navigate("UsersList", true);		
					}
				});
			}
		},
		
		cancel: function() {
			
			Backbone.history.navigate("UsersList", true);
		},
		
		showErrors: function(model, errors) {
			
			if(errors.username) {
				this.highlightField("#username", "error", errors.username);
			}
			else {
				this.highlightField("#username", "success");
			}
			
			if(errors.password) {
				this.highlightField("#password", "error", errors.password);
			}
			else {
				this.highlightField("#password", "success");
			}
			
			if(errors.firstName) {
				this.highlightField("#firstName", "error", errors.firstName);
			}
			else {
				this.highlightField("#firstName", "success");
			}
			
			if(errors.lastName) {
				this.highlightField("#lastName", "error", errors.lastName);
			}
			else {
				this.highlightField("#lastName", "success");
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
