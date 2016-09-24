'use strict';

angular.module('myApp.notes', ['ngRoute'])

.controller('NotesCtrl', function($rootScope) {

	getNotes = function(){	
		var  data = {
				"token" : $rootScope.token,
				};
		$http.post("http://localhost:8081/timeManagement/rest/note/getAllOfUser" ,data)
    	.then(function(response) {
//    		if (response.data.token == "null") {
	    		$log.log(response.data);
//    			$scope.successMessage = "Invalid email and password";
////				$location.path("/login");
//			} else {
//	  			$scope.successMessage = "User has been created successfully";
//	  			$log.log("user logged in");
//	  			$log.log(response.data.token);	
//			}
//        $scope.note = response.data;
    });
	};
	getNotes();
});