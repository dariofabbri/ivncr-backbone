define([
	"underscore", 
	"backbone",
	"ivncr/collections/pagingcollection",
	"ivncr/models/permission"], 
	function(_, Backbone, PagingCollection, Permission) {
	
	var permissions = PagingCollection.extend({

		model: Permission,
		
		url: "/ivncr/api/permissions",
	});
	
	return permissions;
});