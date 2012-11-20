define([
	"underscore", 
	"backbone"], 
	function(_, Backbone) {
	
	var grant = Backbone.Model.extend({
	
		defaults: {
			action: null,
			allowed: false
		},

		urlRoot: "/ivncr/api/security/grants",
		
		idAttribute: "action"
	});
	
	return grant;
});
