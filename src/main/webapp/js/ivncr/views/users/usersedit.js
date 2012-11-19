define([
	"underscore", 
	"backbone",
	"jquery",
	"ivncr/models/user",
	"ivncr/collections/roles",
	"ivncr/views/users/usersroleslist",
	"text!templates/users/usersedit.html"], 
	function(_, Backbone, $, User, Roles, RolesView, editTemplate) {
	
	var view = Backbone.View.extend({
		
		tagName: "div",
		
		events: {
			"click a#apply": "apply",
			"click a#save": "save",
			"click a#cancel": "cancel"
		},

		childViews: [],

		render: function() {

			this.cleanChildViews();

			this.$el.html(_.template(editTemplate, this.model.toJSON()));

			// The roles list is only to be shown for already created users.
			//
			if(this.model.id) {
				
				var roles = new Roles();
				roles.url = "api/users/" + this.model.id + "/roles";
				roles.fetch();
			
				// Create and render the roles list view.
				//
				var rolesView = new RolesView({
					collection: roles
				});
				$("div#roles", this.el).append(rolesView.render().el);
				
				// Store the subview in the list of child views, to avoid
				// memory leaks.
				//
				this.childViews.push(rolesView);
			}

			return this;
		},
		
		initialize: function() {
			
			this.model.on("error", this.showErrors, this);
			this.model.on("change", this.render, this);
		},

		onClose: function() {

			this.model.off("error", this.showError);
			this.model.off("change", this.render);
		},

		autofocus: "#username",
		
		apply: function() {
			this.doSave({stay: true});
		},
		
		save: function() {
			this.doSave({stay: false});
		},

		doSave: function(options) {
						
			var username = $("#username").val();
			var password = $("#password").val();
			var confirmPassword = $("#confirmPassword").val();
			var firstName = $("#firstName").val();
			var lastName = $("#lastName").val();
			var description = $("#description").val();

			var result = this.model.set({
				username: username,
				password: password,
				confirmPassword: confirmPassword,
				firstName: firstName,
				lastName: lastName,
				description: description
			});
			
			if(result) {
				var tmpModel = new User(_.pick(this.model.toJSON(),
						"id",
						"username", 
						"password", 
						"firstName", 
						"lastName", 
						"description"));
				
				if(options && options.stay) {
					var that = this;
					tmpModel.save({}, {
						success: function() {
							that.model.set("id", tmpModel.id);
						}						
					});
				}
				else {
					tmpModel.save({}, {
						success: function() {
							Backbone.history.navigate("UsersList", true);
						}
					});
				}
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
			
			if(errors.confirmPassword) {
				this.highlightField("#confirmPassword", "error", errors.confirmPassword);
			}
			else {
				this.highlightField("#confirmPassword", "success");
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
