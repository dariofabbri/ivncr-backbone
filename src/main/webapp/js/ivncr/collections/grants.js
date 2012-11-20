define([
	"underscore", 
	"backbone",
	"ivncr/models/grant"], 
	function(_, Backbone, Grant) {
	
	var grants = Backbone.Collection.extend({

		model: Grant,
		
		url: "/ivncr/api/security/grants",
	});
	
	return grants;
});