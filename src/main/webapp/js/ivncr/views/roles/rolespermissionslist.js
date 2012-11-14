define([
	"underscore", 
	"backbone",
	"jquery",
	"ivncr/collections/permissions",
	"ivncr/views/roles/rolespermissionslistitem",
	"ivncr/views/roles/permissionspicker",
	"text!templates/roles/rolespermissionslist.html"], 
	function(_, Backbone, $, 
			Permissions, 
			ItemView, 
			PermissionsPickerView, 
			listTemplate) {
	
	var view = Backbone.View.extend({
		
		tagName: "div",

		events: {
			"click a#add": "openPicker"
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
			item.urlRoot = item.collection.url;
			var itemView = new ItemView({
				model : item
			});
			$("tbody", this.el).append(itemView.render().el);
			
			// Store the item view in the list of child views, to avoid
			// memory leaks.
			//
			this.childViews.push(itemView);
		},
		
		openPicker: function() {
			
			var permissions = new Permissions();
			var pickerView = new PermissionsPickerView({
				collection: permissions
			});
			pickerView.on("picker:save", this.addSelectedPermissions, this);
			
			this.childViews.push(pickerView);
			
			$("div#pickerContainer", this.el).html(pickerView.render().el);
			
			permissions.fetch({success: function() {
				$("div#permissionPicker", this.el).modal("show");	
			}});
		},
		
		addSelectedPermissions: function(selected) {
			
			var that = this;
			_.each(selected, function(item) {
				
				var model = new (that.collection.model)();
				model.urlRoot = that.collection.url;
				model.set("id", item);
				model.save(null, {async: false});
			});
			
			this.collection.fetch();
		}
	});
	
	return view;
});
