define([
	"underscore", 
	"backbone",
	"jquery",
	"ivncr/views/roles/permissionspickeritem",
	"text!templates/roles/permissionspicker.html"], 
	function(_, Backbone, $, ItemView, listTemplate) {
	
	var view = Backbone.View.extend({
		
		tagName: "div",

		events: {
			"click a#closePicker": "doClose",
			"click a#savePickerSelection": "doSave"
		},

		initialize: function() {
			this.collection.on("reset", this.render, this);
		},

		onClose: function() {
			this.collection.off("reset", this.render);
		},

		childViews: [],

		render: function(e) {

			this.cleanChildViews();
			
			// Show main part of the view.
			//
			this.$el.html(_.template(listTemplate, {
				collection: this.collection
			}));

			// Render an item view for each model in the collection.
			//
			var that = this;
			_.each(this.collection.models, function(item) {
				that.renderItem(item);
			}, this);
			that = null;
			
			return this;
		},
		
		renderItem: function(item) {
			
			// Create and render the item view.
			//
			var itemView = new ItemView({
				model : item
			});
			$("div.modal-body", this.el).append(itemView.render().el);
			
			// Store the item view in the list of child views, to avoid
			// memory leaks.
			//
			this.childViews.push(itemView);
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
		}
	});
	
	return view;
});
