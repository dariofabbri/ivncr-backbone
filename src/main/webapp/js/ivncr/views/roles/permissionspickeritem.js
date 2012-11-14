define([
	"underscore", 
	"backbone",
	"jquery",
	"text!templates/roles/permissionspickeritem.html"], 
	function(_, Backbone, $, itemTemplate) {
	
	var view = Backbone.View.extend({
		
		tagName: "div",
		
		events: {
		},
		
		render: function() {
			
			this.$el.append(_.template(itemTemplate, this.model.toJSON()));
			return this;
		}
	});
	
	return view;
});
