define([
	"underscore", 
	"backbone"], 
	function(_, Backbone) {
	
	var user = Backbone.Model.extend({
	
		defaults: {
			id: null,
			username: null,
			firstName: null,
			lastName: null,
			description: null
		},
		
		urlRoot: "/ivncr/api/users",			

		validate: function(changed) {
			
			var errors = {};
			
			if(!_.isUndefined(changed.username)) {
				if(_.isEmpty(changed.username)) {
					errors.username = "Il campo è obbligatorio.";
				}
			}
			
			if(!_.isUndefined(changed.firstName)) {
			}
			
			if(!_.isUndefined(changed.lastName)) {
			}
			
			if(!_.isUndefined(changed.description)) {
			}
			
			if(!_.isEmpty(errors)) {
				return errors;
			}
		}
	});
	
	return user;
});