'use strict';

var app = angular.module('myApp.notes', [ 'ngRoute' ]);
app.controller('NotesCtrl', function($scope, $rootScope, $http, $log,
		$location, $routeParams) {

	$log.log("token from the root scope: " + $rootScope.token);
	$scope.getNotes = function() {
		var data = {
			"token" : $rootScope.token
		};
		$http.post(
				"http://localhost:8081/timeManagement/rest/note/getAllOfUser",
				data).then(function(response) {

			if (response.data.token == "null") {
				$log.log(response.data);
				$scope.successMessage = "you need to login before that";
				$location.path("/login");
			} else {
				$log.log("notes came back");
				$scope.notes = response.data;
				$log.log(response.data);
			}
			$scope.note = response.data;
		});
	};
	$scope.deleteNote = function(index) {
		$log.log(index);
		var noteToBeDeleted = $scope.notes[index];

		var data = {
			id : noteToBeDeleted.id
		};

		$http.post("http://localhost:8081/timeManagement/rest/note/delete",
				data).then(function(response) {
			if (response.data.deleted == "true") {
				$scope.notes.splice(index, 1);
			} else {
				$log.log("can't be deleted");
			}
		});
	};

	$scope.updateNote = function(index) {
		$log.log(index);
		// $log.log(note.description);

		var noteToBeUpdated = $scope.notes[index];
		var params = "{'index' : '" + index + "'}";
		$rootScope.noteToBeUpdated = noteToBeUpdated;
		$location.path("/addEditNote").search(noteToBeUpdated);
		$log.log(noteToBeUpdated.startDate);
		// $scope.initializeForEdit();
	};
	$scope.addNote = function() {
		$log.log("add note method");
		$rootScope.noteToBeUpdated = undefined;
		$location.path("/addEditNote");
	};

	// $scope.initializeForEdit = function () {
	// var index = $routeParams.index;
	// $scope.noteUpdate = $scope.notes[index];
	// $log.log(index);
	// $log.log("*********************&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
	// $log.log($routeParams.startDate);
	// // $scope.noteUpdate = $rootScope.notes[index].startDate;
	// };
	$scope.getNotes();
});

app
		.controller(
				'addEditNoteCtrl',
				function($scope, $rootScope, $http, $log, $location,
						$routeParams) {

					$scope.initializeForEdit = function() {
						var note = $routeParams.noteToBeUpdated;
						$scope.noteUpdate = $rootScope.noteToBeUpdated;
						$log.log("noteUpdate start date"
								+ $scope.noteUpdate.startDate);

						if ($scope.noteUpdate == null) {
							$location.path("/notes");
						} else {
							$log.log($rootScope.noteToBeUpdated.startDate
									+ " from root scope");
							$log
									.log("*********************&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
							$log.log($scope.noteUpdate);

							// $rootScope.noteToBeUpdated = null;
						}
					};

					$scope.cancel = function() {
						$rootScope.noteToBeUpdated = "newNote";
						$location.path("/notes");
					};

					$scope.saveNote = function() {
						$log.log("inside save function");

						$log.log($scope.noteUpdate + " from saveNote function");

						// this for update note
						if (typeof ($rootScope.noteToBeUpdated) != 'undefined') {
							$log.log($scope.noteUpdate.description
									+ "from if statement");

							$http
									.post(
											"http://localhost:8081/timeManagement/rest/note/update",
											$scope.noteUpdate)
									.then(
											function(response) {
												if (response.data.updated == "true") {
													$rootScope.noteToBeUpdated = null;
													$location.path("/notes");
												} else {
													$log
															.log("can't be update the note");
												}
											});
						} 
						else{
						// means you
						// gonna
						// save a
						// new note
						
							$log.log($scope.noteUpdate.description
									+ "from else");
							var data = {
								token : "sjkdfnlkjsnbdfkjsbdkf",
								note : $scope.noteUpdate
							};
							$log.log("data: " + data.token);
							$log.log("data: " + data.note.description);

							$http
									.post(
											"http://localhost:8081/timeManagement/rest/note/add",
											data)
									.then(
											function(response) {
												if (response.data.added == "true") {
													$rootScope.noteToBeUpdated = null;
													$location.path("/notes");
												} else {
													$log
															.log("can't be update the note");
												}
											});
						}
					};

					if (typeof ($rootScope.noteToBeUpdated) != 'undefined') {
						$log.log("pre-initialize");
						$scope.initializeForEdit();

					}
				});