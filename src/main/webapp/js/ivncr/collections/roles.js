define([
	"underscore", 
	"backbone",
	"ivncr/collections/pagingcollection",
	"ivncr/models/role"], 
	function(_, Backbone, PagingCollection, Role) {
	
	var roles = PagingCollection.extend({

		model: Role,
		
		url: "/ivncr/api/roles",
	});
	
	return roles;
});