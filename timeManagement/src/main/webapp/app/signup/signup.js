

angular.module('myApp.signup', ['ngRoute'])

.controller('SignupCtrl', function($scope, $rootScope, $location, $location , $http , $log) {


	$scope.signup = function(){
		$log.log("test");

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
	
	if ($rootScope.token != 'undefined' && typeof($rootScope.token) != 'undefined'){
		$log.log("from signout: " + $rootScope.token);
		$location.path("/notes");
	}
});

