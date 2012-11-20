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
		
		idAttribute: "securityToken",
		
		checkGrant: function(action) {
			
			var grants = this.get("grants");
			
			if(!grants)
				return false;
			
			var found = _.find(grants, function(grant) {
				return grant.action === action;
			});
			
			if(!found)
				return false;
			
			return found.allowed;
		}
	});
	
	return loginInfo;
});