RolePermission = Backbone.RelationalModel.extend({
	
});


Role = Backbone.RelationalModel.extend({
	
	urlRoot: "/ivncr/api/roles",
	
	relations: [{
		type: Backbone.HasMany,
		key: "permissions",
		relatedModel: RolePermission,
		includeInJSON: true,
		reverseRelation: {
			key: "role"
		}
	}]
});


Permission = Backbone.RelationalModel.extend({
	
	urlRoot: "/ivncr/api/roles",
	
	relations: [{
		type: Backbone.HasMany,
		key: "roles",
		relatedModel: RolePermission,
		includeInJSON: false,
		reverseRelation: {
			key: "permission"
		}
	}]
});



var role = new Role({
	id: "1",
	rolename: "admin",
	description: "Administration role"
});

var permission = new RolePermission({
	role: "1",
	permission: "1"
});

var permissions = role.get("permissions");
permissions.add(permission);

role.save();
