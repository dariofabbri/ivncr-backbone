define([
	"underscore", 
	"backbone",
	"jquery",
	"text!templates/roles/rolessearch.html"], 
	function(_, Backbone, $, searchTemplate) {
	
	var view = Backbone.View.extend({
		
		tagName: "div",
		
		events: {
			"click a#search": "search",
			"click a#cancel": "cancel"
		},
		
		render: function() {

			this.$el.html(_.template(searchTemplate, {
				collection: this.collection
			}));
			return this;
		},
		
		initialize: function() {
		},
		
		autofocus: "#rolename",

		search: function() {
						
			var rolename = $("#rolename").val();
			var description = $("#description").val();

			this.collection.fetchPage(1, {
				rolename: rolename,
				description: description
			}, {
				success: function() {
					Backbone.history.navigate("RolesList", true);		
				}
			});
		},
		
		cancel: function() {
			
			Backbone.history.navigate("RolesList", true);
		}
	});
	
	return view;
});
