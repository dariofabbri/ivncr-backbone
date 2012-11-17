define([
	"underscore", 
	"backbone",
	"jquery",
	"text!templates/roles/rolessearch.html"], 
	function(_, Backbone, $, searchTemplate) {
	
	var view = Backbone.View.extend({
		
		tagName: "div",
		
		events: {
			"click a#saveModalSelection": "search",
			"click a#closeModal": "cancel"
		},
		
		render: function() {

			this.$el.html(_.template(searchTemplate, {
				collection: this.collection
			}));
			return this;
		},
		
		initialize: function() {
		},

		search: function() {
						
			var rolename = $("#rolename").val();
			var description = $("#description").val();

			var that = this;
			this.collection.fetchPage(1, {
				rolename: rolename,
				description: description
			});

			$("div#searchModal", this.el).modal("hide");
			this.close();
		},
		
		cancel: function() {
			
			$("div#searchModal", this.el).modal("hide");
			this.close();
		},
		
		setFocus: function() {
			
			// Set focus to role name filter field.
			//
			$("input#rolename", this.el).focus();
		}
	});
	
	return view;
});
