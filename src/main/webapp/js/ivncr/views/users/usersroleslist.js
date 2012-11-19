define([
	"underscore", 
	"backbone",
	"jquery",
	"ivncr/collections/roles",
	"ivncr/views/users/usersroleslistitem",
	"ivncr/views/users/rolespicker",
	"text!templates/users/usersroleslist.html"], 
	function(_, Backbone, $, 
			Roles, 
			ItemView, 
			RolesPickerView, 
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
			// collection of roles.
			// 
			var roles = new Roles();
			var pickerView = new RolesPickerView({
				collection: roles
			});
			
			// Subscribe to save event of picker view.
			//
			pickerView.on("picker:save", this.addSelectedRoles, this);
			
			// Keep a reference to picker view for later clean-up.
			//
			this.childViews.push(pickerView);
			
			// Fetch the roles list from the server.
			//
			var that = this;
			roles.fetch({success: function() {
				
				// Mark already added roles.
				//
				_.each(roles.models, function(role) {
					
					if(that.collection.get(role.id)) {
						role.set("alreadyPresent", true);
					} else {
						role.set("alreadyPresent", false);
					}
				});
				
				// Render the view and show modal.
				//
				$("div#modalContainer", this.el).html(pickerView.render().el);
				$("div#rolePicker", this.el).modal("show");
				$("div#rolePicker", this.el).on("shown", pickerView.setFocus);
			}});
		},
		
		addSelectedRoles: function(selected) {
			
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
