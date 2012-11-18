define([
	"underscore", 
	"backbone",
	"ivncr/views/users/userslist",
	"ivncr/views/users/usersedit",
	"ivncr/models/user",
	"ivncr/collections/users"], 
	function(
			_, 
			Backbone, 
			UsersListView, 
			UsersEditView,
			User,
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

			var view = new UsersEditView({
				model: new User()
			});
			this.show(view, "#container");
		},
		
		edit: function(id) {
			
			var model = this.collection.get(id);
			var view = new UsersEditView({
				model: model
			});
			
			this.show(view, "#container");
		}
	});

	return users;
});
