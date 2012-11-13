Role = Backbone.Model.extend({
	
	urlRoot: "/ivncr/api/roles",
});


Permission = Backbone.Model.extend({
	
	urlRoot: "/ivncr/api/permissions",

});


RolePermissions = Backbone.Collection.extend({
	
	initialize: function(options) {
	
		this.url = "/ivncr/api/roles/" + options.roleId + "/permissions";
	},

	model: Permission
});



var role = new Role({
	rolename: "testrole",
	description: "Test role"
});
role.save(null, {success: function() {

	var permissions = new RolePermissions({roleId: role.get("id")});
	permissions.fetch();	

	var permission = new Permission({
		id: "1",
		permissionString: "*"
	});

	permission.urlRoot = "/ivncr/api/roles/" + role.id + "/permissions";
	permissions.create(permission);
}});
