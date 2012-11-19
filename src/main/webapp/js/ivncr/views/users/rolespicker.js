define([
	"underscore", 
	"backbone",
	"jquery",
	"text!templates/users/rolespicker.html",
	"text!templates/users/rolespickeritem.html"], 
	function(_, Backbone, $, listTemplate, itemTemplate) {
	
	var view = Backbone.View.extend({
		
		tagName: "div",

		events: {
			"click input#selectAll": "toggleAll",
			"click a#closeModal": "doClose",
			"click a#saveModalSelection": "doSave",
			"keyup input#roleFilter": "renderItems"
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
			$("div.modal-body>p#item", this.el).remove();
			
			// Get applied filter from search input textbox.
			//
			var filter = $("input#roleFilter", this.el).val();
			filter = filter && filter.trim().length > 0 ? filter.trim().toLowerCase() : null;
			
			// Iterate on collection's models, adding only items matching
			// applied filter.
			//
			var that = this;
			_.each(this.collection.models, function(item) {
				
				if(!filter || item.get("rolename").indexOf(filter) >= 0) {
					$("div.modal-body", that.el).append(_.template(itemTemplate, item.toJSON()));
				}
			});
		},

		doClose: function(e) {
			
			e.preventDefault();
			$("div#rolePicker", this.el).modal("hide");
			this.close();
		},
		
		doSave: function(e) {
			
			e.preventDefault();
			
			// Extract selected roles list.
			//
			var selectedRoles = [];
			$("input:checkbox[name=roles]:checked").each(function()
			{
			    selectedRoles.push($(this).val());
			});

			// Hide the modal.
			//
			$("div#rolePicker", this.el).modal("hide");

			// Trigger save event.
			//
			this.trigger("picker:save", selectedRoles);
			
			// Close the view.
			//
			this.close();
		},
		
		setFocus: function() {
			
			// Set focus to role filter field.
			//
			$("input#roleFilter", this.el).focus();
		},
		
		toggleAll: function() {
			
			var checked = $("input:checkbox[id=selectAll]").prop("checked");
			$("input:checkbox[name=roles]:not(:disabled)").prop("checked", checked);
		}
	});
	
	return view;
});
