define([
	"underscore", 
	"backbone",
	"jquery",
	"text!templates/roles/permissionspicker.html",
	"text!templates/roles/permissionspickeritem.html"], 
	function(_, Backbone, $, listTemplate, itemTemplate) {
	
	var view = Backbone.View.extend({
		
		tagName: "div",

		events: {
			"click a#closeModal": "doClose",
			"click a#saveModalSelection": "doSave",
			"keyup input#permissionFilter": "renderItems"
		},

		childViews: [],

		render: function(e) {

			this.cleanChildViews();
			
			// Show main part of the view.
			//
			this.$el.html(_.template(listTemplate, {
				collection: this.collection
			}));

			// Render the item views.
			//
			this.renderItems();
			
			return this;
		},
		
		renderItems: function(e) {
			
			// Clean up previously added items.
			//
			$("div.modal-body>p", this.el).remove();
			
			// Get applied filter from search input textbox.
			//
			var filter = $("input#permissionFilter", this.el).val();
			filter = filter && filter.trim().length > 0 ? filter.trim().toLowerCase() : null;
			
			// Iterate on collection's models, adding only items matching
			// applied filter.
			//
			var that = this;
			_.each(this.collection.models, function(item) {
				
				if(!filter || item.get("permissionString").indexOf(filter) >= 0) {
					$("div.modal-body", that.el).append(_.template(itemTemplate, item.toJSON()));
				}
			});
		},

		doClose: function(e) {
			
			e.preventDefault();
			$("div#permissionPicker", this.el).modal("hide");
			this.close();
		},
		
		doSave: function(e) {
			
			e.preventDefault();
			
			// Extract selected permissions list.
			//
			var selectedPermissions = [];
			$("input:checkbox[name=permissions]:checked").each(function()
			{
			    selectedPermissions.push($(this).val());
			});

			// Hide the modal.
			//
			$("div#permissionPicker", this.el).modal("hide");

			// Trigger save event.
			//
			this.trigger("picker:save", selectedPermissions);
			
			// Close the view.
			//
			this.close();
		},
		
		setFocus: function() {
			
			// Set focus to permissions filter field.
			//
			$("input#permissionFilter", this.el).focus();
		}
	});
	
	return view;
});
