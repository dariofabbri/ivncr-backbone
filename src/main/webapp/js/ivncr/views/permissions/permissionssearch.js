define([
	"underscore", 
	"backbone",
	"jquery",
	"text!templates/permissions/permissionssearch.html"], 
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
						
			var permissionString = $("#permissionstring").val();

			var that = this;
			this.collection.fetchPage(1, {
				permissionString: permissionString
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
			$("input#permissionstring", this.el).focus();
		}
	});
	
	return view;
});
