define([
	"underscore", 
	"backbone",
	"jquery",
	"text!templates/users/userssearch.html"], 
	function(_, Backbone, $, searchTemplate) {
	
	var view = Backbone.View.extend({
		
		tagName: "div",
		
		events: {
			"click a#saveModalSelection": "search",
			"click a#closeModal": "cancel",
			"keypress": "manageEnter"
		},
		
		render: function() {

			this.$el.html(_.template(searchTemplate, {
				collection: this.collection
			}));
			return this;
		},
		
		initialize: function() {
		},
		
		manageEnter: function(e) {
			if (e.keyCode == 13) {
				e.preventDefault();
				this.search();
			}
		},

		search: function() {
						
			var username = $("#username").val();
			var firstName = $("#firstName").val();
			var lastName = $("#lastName").val();
			var description = $("#description").val();
			

			var that = this;
			this.collection.fetchPage(1, {
				username: username,
				firstName: firstName,
				lastName: lastName,
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
			
			// Set focus to first field.
			//
			$("input#permissionstring", this.el).focus();
		}
	});
	
	return view;
});
