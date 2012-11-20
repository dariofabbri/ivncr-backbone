define([
	"underscore", 
	"backbone",
	"jquery",
	"ivncr/collections/grants",
	"text!templates/login/loginpanel.html",
	"text!templates/common/alert.html"], 
	function(_, Backbone, $, Grants, loginPanelTemplate, alertTemplate) {
	
	var loginView = Backbone.View.extend({
	
		events: {
			"click #login": "processSubmit",
			"click #cancel": "cancelForm",
			"keypress": "manageEnter"
		},
		
		render: function() {
			
			this.$el.html(_.template(loginPanelTemplate));
			return this;
		},
		
		autofocus: "#username",
		
		manageEnter: function(e) {
			if (e.keyCode == 13) {
				e.preventDefault();
				this.executeLogin();
			}
		},
		
		cancelForm: function(e) {
			
			e.preventDefault();
			alert(application.loginInfo.get("loggedOn"));
		},
		
		processSubmit: function(e) {
			
			e.preventDefault();
			this.executeLogin();
		},
		
		executeLogin: function() {
			
			var username = $("#username").val();
			var password = $("#password").val();
			
			application.loginInfo.save({username: username,	password: password}, {
				success: function() {
					
					// Set up options for grants collection fetch.
					//
					var options = {
						async: false,
						data: {
							actions: application.grantActions.join(",")
						}
					};

					// Execute server call.
					//
					var grants = new Grants();
					grants.fetch(options);
					
					// Set grants property in loginInfo object.
					//
					application.loginInfo.set("grants", grants.toJSON());
					
					// Move to home page.
					//
					Backbone.history.navigate("home", true);
				},
				error: function() {
					$("#notification").remove();
				    $("form>legend").after(
				   		_.template(alertTemplate, {
								alertClass: "alert-error", 
								title: "Errore", 
								message: "Le credenziali immesse non sono valide."}));
				}
			});
		}
	});
	
	return loginView;
});
