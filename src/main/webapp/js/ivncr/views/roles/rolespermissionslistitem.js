define([
	"underscore", 
	"backbone",
	"jquery",
	"text!templates/roles/rolespermissionslistitem.html"], 
	function(_, Backbone, $, itemTemplate) {
	
	var view = Backbone.View.extend({
		
		tagName: "tr",
		
		events: {
			"click a#remove": "removeItem"
		},
		
		render: function() {
			
			this.$el.append(_.template(itemTemplate, this.model.toJSON()));
			return this;
		},
		
		removeItem: function() {

			application.modalDialog.show({
				title: "Attenzione",
				message: "Confermi la cancellazione del permesso selezionato dal ruolo corrente?",
				okCaption: "Sì",
				okCallback: this.doRemoveItem,
				showOk: true,
				cancelCaption: "No",
				cancelCallback: this.cancelRemoveItem,
				showCancel: true,
				context: this
			});
		},
		
		cancelRemoveItem: function() {
			
		},
		
		doRemoveItem: function() {
			
			this.model.destroy({wait: true});
			this.remove();
		}
	});
	
	return view;
});
