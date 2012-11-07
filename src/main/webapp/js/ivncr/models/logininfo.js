define([
	"underscore", 
	"backbone"], 
	function(_, Backbone) {
	
	var loginInfo = Backbone.Model.extend({
	
		defaults: {
			loggedOn: false,
			username: null,
			name: null,
			surname: null,
			logonTs: null,
			securityToken: null,
			roles: null,
			permissions: null
		},

		urlRoot: "/ivncr/api/security/sessions",
		
		idAttribute: "securityToken"
	});
	
	return loginInfo;
});