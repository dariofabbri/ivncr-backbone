define([
	"underscore", 
	"backbone",
	"jquery",
	"ivncr/views/roles/rolespermissionslistitem",
	"text!templates/roles/rolespermissionslist.html"], 
	function(_, Backbone, $, ItemView, listTemplate) {
	
	var view = Backbone.View.extend({
		
		tagName: "div",

		events: {
			"click a#add": "addItem"
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
		
		addItem: function() {
			
			$("div#permission-picker", this.el).modal("show");
		}
	});
	
	return view;
});
