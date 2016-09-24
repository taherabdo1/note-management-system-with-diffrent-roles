'use strict';

angular.module('myApp.login', ['ngRoute'])



.controller('LoginCtrl', function($scope , $http) {
	$scope.login = function(){
//		$scope.userInfo = $scope.userName + $scope.password;	
		$http.post("http://localhost:8081/timeManagement/rest/note/getNote" ,{ id: '2' }
)
    	.then(function(response) {
        $scope.userInfo = response.data;
    });
	};
})



