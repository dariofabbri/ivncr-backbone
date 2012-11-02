define([
	"underscore", 
	"backbone",
	"ivncr/collections/pagingcollection",
	"ivncr/models/contact"], 
	function(_, Backbone, PagingCollection, Contact) {
	
	var contacts = PagingCollection.extend({

		model: Contact,
		
		url: "/ivncr/api/contacts",
	});
	
	return contacts;
});