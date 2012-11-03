define([
	"underscore", 
	"backbone"], 
	function(_, Backbone) {
	
	var contact = Backbone.Model.extend({
	
		defaults: {
			id: null,
			firstName: "",
			lastName: "",
			phoneNumber: ""
		},
		
		urlRoot: "/ivncr/api/contacts",			

		validate: function(changed) {
			
			var errors = {};
			
			if(!_.isUndefined(changed.firstName)) {
				if(_.isEmpty(changed.firstName)) {
					errors.firstName = "Il campo è obbligatorio.";
				}
			}
			
			if(!_.isUndefined(changed.lastName)) {
				if(_.isEmpty(changed.lastName)) {
					errors.lastName = "Il campo è obbligatorio.";
				}
			}
			
			if(!_.isUndefined(changed.phoneNumber)) {
				if(_.isEmpty(changed.phoneNumber)) {
					errors.phoneNumber = "Il campo è obbligatorio.";
				}
				else if(!/^[+0123456789\. ]+$/i.test(changed.phoneNumber)) {
					errors.phoneNumber = "Il valore introdotto non rappresenta un numero di telefono valido.";
				}
			}
			
			if(!_.isEmpty(errors)) {
				return errors;
			}
		}
	});
	
	return contact;
});