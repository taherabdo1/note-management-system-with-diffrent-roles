'use strict';

var usersApp = angular.module('myApp.users', [ 'ngRoute' ]);

usersApp.controller('UsersCtrl', function($scope, $rootScope, $http, $log,
		$location, $routeParams) {

	$scope.getUsers = function() {
		var req = {
			method : 'GET',
			url : 'http://localhost:8081/timeManagement/rest/user/getAllUsers',
			headers : {
				'Authorization' : $rootScope.token
			},
			data : {}
		};

		$http(req).then(function(response) {
			$log.log("users came back");
			$scope.users = response.data;
			$log.log(response.data);
		}, function(response) {
			// update to show unauthorized*****//////
			$location.path("/notes");
		});
	};
	$scope.deleteUSer = function(index) {
		var userToBeDeleted = $scope.users[index];

		var req = {
			method : 'POST',
			url : 'http://localhost:8081/timeManagement/rest/user/delete',
			headers : {
				'Authorization' : $rootScope.token
			},
			data : userToBeDeleted.email

		};

		// $http.post("http://localhost:8081/timeManagement/rest/note/delete",data)
		$http(req).then(function(response) {
			if (response.data.deleted == "true") {
				$scope.users.splice(index, 1);
			} else {
				$log.log("can't be deleted");
			}
		});
	};

	$scope.updateUser = function(index) {
		var userToBeUpdated = $scope.users[index];
		$rootScope.userToBeUpdated = userToBeUpdated;
		$location.path("/addEditUser");
		$log.log("from the usersCtrl userToBeUpdated.email: "
				+ userToBeUpdated.email);
	};

	$scope.addUser = function() {
		$log.log("add user method");
		$rootScope.userToBeUpdated = undefined;
		$location.path("/addEditUser");
	};
	// to get the user on page startup
	$scope.getUsers();
});

usersApp.controller('AddEditUserCtrl', function($scope, $rootScope, $http,
		$log, $location, $routeParams) {
	$scope.initializeForEdit = function() {
		$scope.userUpdate = $rootScope.userToBeUpdated;
		if ($scope.userUpdate == null) {
			$location.path("/users");
		}
	};
	$scope.cancel = function() {
		$rootScope.userToBeUpdated = "newUser";
		$location.path("/users");
	};

	$scope.saveUser = function() {
		$log.log("inside save user function");

		$log.log($scope.userUpdate + " from saveUser function");

		var updateUserReq = {
			method : 'POST',
			url : 'http://localhost:8081/timeManagement/rest/user/update',
			headers : {
				'Authorization' : $rootScope.token
			},
			data : $scope.userUpdate

		};
		// this for update note
		if (typeof ($rootScope.userToBeUpdated) != 'undefined') {
			$log.log($scope.userUpdate.email + " from if statement");

			$http(updateUserReq).then(function(response) {
				if (response.data.updated == "true") {
					$rootScope.userToBeUpdated = null;
					$location.path("/notes");
				} else {
					$log.log("can't be update the user");
				}
			});
		} else {
			$log.log($scope.userUpdate.email + "from else");
			var newUserReq = {
				method : 'POST',
				url : 'http://localhost:8081/timeManagement/rest/user/signup',
				headers : {
					'Authorization' : $rootScope.token
				},
				data :  $scope.userUpdate	
			};

			$http(newUserReq).then(function(response) {
				if (response.data.response == "DONE") {
					$rootScope.userToBeUpdated = null;
					$location.path("/users");
				} else {
					$log.log("can't be create the note");
				}
			});
		}
	};
	if (typeof ($rootScope.userToBeUpdated) != 'undefined') {
		$log.log("pre-initialize");
		$scope.initializeForEdit();

	}
});
//		