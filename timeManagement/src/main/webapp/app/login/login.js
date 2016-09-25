'use strict';

angular.module('myApp.login', ['ngRoute'])



.controller('LoginCtrl', function($scope , $http , $log , $rootScope , $location) {
	
	$scope.login = function(){
		var  loginData = {
				"email" : $scope.email,
				"password" : $scope.password
				};
		$http.post("http://localhost:8081/timeManagement/rest/user/signin" ,loginData)
    	.then(function(response) {
    		if (response.data.token == "null") {
	    		$log.log("error");
    			$scope.successMessage = "Invalid email and password";
//				$location.path("/login");
			} else {
				$rootScope.token = response.data.token;
				$rootScope.userName = $scope.email;
	  			$scope.successMessage = "User has been created successfully";
	  			$log.log("user logged in");
	  			$log.log(response.data.token);
	  			$rootScope.token = response.data.token;
				$location.path("/notes");
			}
        $scope.note = response.data;
    });
	};
	//to prevent from log in if he is already loged in
	$log.log("from login token from rootScope: "+$rootScope.token);
	if (typeof ($rootScope.token) != 'undefined'){
		$location.path("/notes");
	}
});



