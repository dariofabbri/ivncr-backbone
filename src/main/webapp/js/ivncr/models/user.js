define([
	"underscore", 
	"backbone"], 
	function(_, Backbone) {
	
	var user = Backbone.Model.extend({
	
		defaults: {
			id: null,
			username: "",
			password: "",
			firstName: "",
			lastName: "",
			description: ""
		},
		
		urlRoot: "/ivncr/api/users",			

		validate: function(changed) {
			
			var errors = {};
			
			if(!_.isUndefined(changed.username)) {
				if(_.isEmpty(changed.username)) {
					errors.username = "Il campo è obbligatorio.";
				}
			}
			
			// The password must be validated only when the id is
			// not present. The model coming from the server obviously
			// does not have the password field.
			//
			if(_.isUndefined(changed.id) && !_.isUndefined(changed.password)) {
				if(_.isEmpty(changed.password)) {
					errors.password = "Il campo è obbligatorio.";
				}
				else if(changed.password.length < 6) {
					errors.password = "La password introdotta è troppo corta.";
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