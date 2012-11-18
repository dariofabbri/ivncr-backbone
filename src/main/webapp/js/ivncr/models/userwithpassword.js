define([
	"underscore", 
	"backbone"], 
	function(_, Backbone) {
	
	var user = Backbone.Model.extend({
	
		defaults: {
			id: null,
			username: null,
			password: null,
			confirmPassword: null,
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
			
			// The password must be validated only when the id is
			// not present. If the id is present, this means we are
			// to update the model. In this case password can be left
			// unchanged.
			//
			if(changed.id) {
				
				if(!_.isEmpty(changed.password)) {
					
					if(changed.password.length < 6) {
						errors.password = "La password introdotta è troppo corta.";
					}

					if(_.isEmpty(changed.confirmPassword)) {
						errors.confirmPassword = "Il campo è obbligatorio.";
					}
					else if(changed.password !== changed.confirmPassword) {
						errors.confirmPassword = "Le due password non coincidono.";
					}
				}
				
			} else {
				
				if(_.isEmpty(changed.password)) {
					errors.password = "Il campo è obbligatorio.";
				}
				else if(changed.password.length < 6) {
					errors.password = "La password introdotta è troppo corta.";
				}

				if(_.isEmpty(changed.confirmPassword)) {
					errors.confirmPassword = "Il campo è obbligatorio.";
				}
				else if(changed.password !== changed.confirmPassword) {
					errors.confirmPassword = "Le due password non coincidono.";
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