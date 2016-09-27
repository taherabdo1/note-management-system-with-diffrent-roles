

angular.module('myApp.signup', ['ngRoute'])

.controller('SignupCtrl', function($scope, $rootScope, $location, $location , $http , $log) {

//	$scope.showSuccessMessage = false;

	$scope.signup = function(){
		$log.log("test");

//		if(!$scope.signupFormValid) return;

		signupRequest = {
			"email" : $scope.email,
			"password" : $scope.password,
			"firstName" : $scope.firstName,
			"lastName" : $scope.lastName,
			"role" : "user"
		};
		$http.post("http://localhost:8081/timeManagement/rest/user/signup" ,signupRequest)
    	.then(function(response) {
    		$log.log(response);
    		if (response.data.response == "DONE") {
				$scope.successMessage = "User has been created successfully";
				$location.path("/login");
			} else {
	    		$log.log("error");
				$scope.errorMessage = "Email is already exists";
			}
    });
	};
	
	//to prevent from log in if he is already loged in
	$log.log("from login token from rootScope: "+$rootScope.token);
	if (typeof ($rootScope.token) != 'undefined'){
		$location.path("/notes");
	}
});

