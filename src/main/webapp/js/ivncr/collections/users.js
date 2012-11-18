define([
	"underscore", 
	"backbone",
	"ivncr/collections/pagingcollection",
	"ivncr/models/user"], 
	function(_, Backbone, PagingCollection, User) {
	
	var users = PagingCollection.extend({

		model: User,
		
		url: "/ivncr/api/users",
	});
	
	return users;
});