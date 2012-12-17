define([
	"underscore", 
	"backbone",
	"jquery",
	"ivncr/views/common/pager",
	"ivncr/views/permissions/permissionslistitem",
	"ivncr/views/permissions/permissionssearch",
	"text!templates/permissions/permissionslist.html"], 
	function(_, Backbone, $, Pager, ItemView, SearchView, listTemplate) {
	
	var view = Backbone.View.extend({
		
		tagName: "div",

		events: {
			"click a#add": "addItem",
			"click a#search": "search",
			"click a#reset-filters": "resetFilters"
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
			
			// If the collection has filters applied, 
			// show the special button to clean up filters.
			//
			$("a#reset-filters", this.el).tooltip();

			// Render an item view for each model in the collection.
			//
			var that = this;
			_.each(this.collection.models, function(item) {
				that.renderItem(item);
			}, this);
			that = null;
			
			// Add a pager at the bottom of the view.
			//
			var pager = new Pager({
				collection: this.collection,
				baseUrl: "PermissionsList/page/"
			});
			this.$el.append(pager.render().el);
			
			// Store the pager view in the list of child views, to avoid
			// memory leaks.
			//
			this.childViews.push(pager);
			
			return this;
		},
		
		renderItem: function(item) {
			
			// Create and render the item view.
			//
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
			
			Backbone.history.navigate("PermissionsNew", true);
		},
		
		search: function() {
			
			// Create the search view object by passing the
			// current collection, that will be used for reissuing
			// the query on the backend.
			// 
			var searchView = new SearchView({
				collection: this.collection
			});
			
			// Keep a reference to search view for later clean-up.
			//
			this.childViews.push(searchView);
				
			// Render the view and show modal.
			//
			$("div#modalContainer", this.el).html(searchView.render().el);
			$("div#searchModal", this.el).modal("show");
			$("div#searchModal", this.el).on("shown", searchView.setFocus);
		},

		resetFilters: function() {

			$("a#reset-filters", this.el).tooltip("destroy");

			this.collection.resetFilters();
			this.collection.fetchPage();
		}
	});
	
	return view;
});
