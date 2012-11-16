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
			
			// Create the picker view object by passing a new
			// collection of permissions.
			// 
			var permissions = new Permissions();
			var pickerView = new PermissionsPickerView({
				collection: permissions
			});
			
			// Subscribe to save event of picker view.
			//
			pickerView.on("picker:save", this.addSelectedPermissions, this);
			
			// Keep a reference to picker view for later clean-up.
			//
			this.childViews.push(pickerView);
			
			// Fetch the permission list from the server.
			//
			var that = this;
			permissions.fetch({success: function() {
				
				// Mark already added permissions.
				//
				_.each(permissions.models, function(permission) {
					
					if(that.collection.get(permission.id)) {
						permission.set("alreadyPresent", true);
					} else {
						permission.set("alreadyPresent", false);
					}
				});
				
				// Render the view and show modal.
				//
				$("div#pickerContainer", this.el).html(pickerView.render().el);
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
