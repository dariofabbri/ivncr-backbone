define([
	"underscore", 
	"backbone"], 
	function(_, Backbone) {
	
	var role = Backbone.Model.extend({
	
		defaults: {
			id: null,
			rolename: "",
			description: ""
		},
		
		urlRoot: "/ivncr/api/roles",			

		validate: function(changed) {
			
			var errors = {};
			
			if(!_.isUndefined(changed.rolename)) {
				if(_.isEmpty(changed.rolename)) {
					errors.rolename = "Il campo è obbligatorio.";
				}
			}
		}
	});
	
	return role;
});