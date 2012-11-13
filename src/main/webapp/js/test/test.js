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
	
	parse: function(response) {
		
		this.offset = response.offset;
		this.limit = response.limit;
		this.records = response.records;
		
		return response.results;
	},

	model: Permission
});


Roles = Backbone.Collection.extend({
	
	url: "/ivncr/api/roles",
	
	parse: function(response) {
		
		this.offset = response.offset;
		this.limit = response.limit;
		this.records = response.records;
		
		return response.results;
	},

	model: Role
});

var roles = new Roles();
roles.fetch({async: false});
_.each(roles.models, function(item) {
	if(item.get("rolename") === "testrole")
		item.destroy({async: false});
}, this);

var role = new Role({
	rolename: "testrole",
	description: "Test role"
});
role.save(null, {async: false});

var permissions = new RolePermissions({roleId: role.get("id")});
permissions.fetch({async: false});	

var permission = new Permission({
	id: "1",
	permissionString: "*"
});

permission.urlRoot = "/ivncr/api/roles/" + role.id + "/permissions";
permissions.create(permission, {async: false});

permissions.fetch();