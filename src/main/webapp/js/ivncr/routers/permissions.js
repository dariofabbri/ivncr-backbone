define([
	"underscore", 
	"backbone",
	"ivncr/views/permissions/permissionslist",
	"ivncr/views/permissions/permissionsedit",
	"ivncr/models/permission",
	"ivncr/collections/permissions"], 
	function(
			_, 
			Backbone, 
			PermissionsListView, 
			PermissionsEditView,
			Permission,
			Permissions) {

	var permissions = Backbone.Router.extend({
		
		collection: new Permissions(),
		
		routes: {
			"PermissionsList": "list",
			"PermissionsList/page/:page": "page",
			"PermissionsNew": "create",
			"PermissionsEdit/:id": "edit"
		},
		
		list: function() {
			
			this.collection.fetchPage();
			var view = new PermissionsListView({collection: this.collection});
			
			this.show(view, "#container");
		},
		
		page: function(page) {
			
			this.collection.fetchPage(page);
		},
		
		create: function() {

			var model = new Permission();
			
			var view = new PermissionsEditView({
				model: model
			});
			
			this.show(view, "#container");
		},
		
		edit: function(id) {
			
			var model = new Permission({id: id});
			model.fetch();
			
			var view = new PermissionsEditView({
				model: model
			});
			
			this.show(view, "#container");
		}
	});

	return permissions;
});
