define([
	"underscore", 
	"backbone",
	"ivncr/views/users/userslist",
	"ivncr/views/users/usersedit",
	"ivncr/models/userwithpassword",
	"ivncr/collections/users"], 
	function(
			_, 
			Backbone, 
			UsersListView, 
			UsersEditView,
			UserWithPassword,
			Users) {

	var users = Backbone.Router.extend({
		
		collection: new Users(),
		
		routes: {
			"UsersList": "list",
			"UsersList/page/:page": "page",
			"UsersNew": "create",
			"UsersEdit/:id": "edit"
		},
		
		list: function() {
			
			this.collection.fetchPage();
			var view = new UsersListView({collection: this.collection});
			
			this.show(view, "#container");
		},
		
		page: function(page) {
			
			this.collection.fetchPage(page);
		},
		
		create: function() {

			var model = new UserWithPassword();
			
			var view = new UsersEditView({
				model: model
			});
			
			this.show(view, "#container");
		},
		
		edit: function(id) {
			
			// Fetch the existing user from the server. It could have been
			// possible to use data stored in the list (that is in the
			// existing collection) but this could have been changed in the
			// meanwhile by another user.
			//
			var model = new UserWithPassword({id: id});
			model.fetch();

			var view = new UsersEditView({
				model: model
			});
			
			this.show(view, "#container");
		}
	});

	return users;
});
