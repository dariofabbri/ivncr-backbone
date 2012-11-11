define([
	"underscore", 
	"backbone",
	"jquery",
	"text!templates/permissions/permissionssearch.html"], 
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
		
		autofocus: "#permissionstring",

		search: function() {
						
			var permissionString = $("#permissionstring").val();

			this.collection.fetchPage(1, {
				permissionString: permissionString
			}, {
				success: function() {
					Backbone.history.navigate("PermissionsList", true);		
				}
			});
		},
		
		cancel: function() {
			
			Backbone.history.navigate("PermissionsList", true);
		}
	});
	
	return view;
});
