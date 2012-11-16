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
			"click a#closePicker": "doClose",
			"click a#savePickerSelection": "doSave"
		},

		initialize: function() {
			//this.collection.on("reset", this.render, this);
		},

		onClose: function() {
			//this.collection.off("reset", this.render);
		},

		childViews: [],

		render: function(e) {

			this.cleanChildViews();
			
			// Show main part of the view.
			//
			this.$el.html(_.template(listTemplate, {
				collection: this.collection
			}));

			// Render a line for each model in the collection.
			//
			var that = this;
			_.each(this.collection.models, function(item) {
				$("div.modal-body", that.el).append(_.template(itemTemplate, item.toJSON()));
			});
			that = null;
			
			return this;
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
