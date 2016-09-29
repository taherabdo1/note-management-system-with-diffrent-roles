'use strict';

var app = angular.module('myApp.notes', [ 'ngRoute' ]);
app
		.controller(
				'NotesCtrl',
				function($scope, $rootScope, $http, $log, $location,
						$routeParams, $filter) {

					$log.log("token from the root scope: " + $rootScope.token);
					// $log.log("user to get notes of from the root scope: " +
					// $rootScope.userToShowNotesOf);

					$scope.getNotes = function() {
						var data = {
							"token" : $rootScope.token
						};

						var req;
						if (typeof ($rootScope.userToShowNotesOf) != 'undefined') {
							req = {
								method : 'POST',
								url : 'http://localhost:8081/timeManagement/rest/note/getAllNotesOfAnotherUser',
								headers : {
									'Authorization' : $rootScope.token
								},
								data : $rootScope.userToShowNotesOf.email
							};
						} else {
							req = {
								method : 'POST',
								url : 'http://localhost:8081/timeManagement/rest/note/getAllOfUser',
								headers : {
									'Authorization' : $rootScope.token
								},
								data : $rootScope.token
							};

						}
						$http(req)
								.then(
										function(response) {
											$log.log("notes came back");
											$scope.notes = response.data;

										}, function(response) {
											$location.path("/login");
										});
						$rootScope.userToShowNotesOf = undefined;
					};

					$scope.deleteNote = function(index) {
						$log.log(index);
						var noteToBeDeleted = $scope.notes[index];
						var req = {
							method : 'POST',
							url : 'http://localhost:8081/timeManagement/rest/note/delete',
							headers : {
								'Authorization' : $rootScope.token
							},
							data : noteToBeDeleted.id

						};

						$http(req).then(function(response) {
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
					
					$scope.set_color  = function(note){
						$log.log("set_color function");
						$scope.current_date_format_ddMMyyyy = $filter(
								'date')(new Date(),
								'yyyy-MM-dd');
						$scope.current_date_format_HHMMSS = $filter(
								'date')(new Date(),
								'HH:mm:ss');

						$scope.prefared_date_format_HHMMSS = $filter(
								'date')
								(
										note.preferredWorkingHourPerDay,
										'HH:mm:ss');
						$scope.endTime = new Date(note.startDate
								+ ' '
								+ note.preferredWorkingHourPerDay);//.setHours(response.data[0].period);
						$scope.endTime.setHours($scope.endTime.getHours()+note.period);
						$log.log("end Date hours: " +$scope.endTime.getHours());
						var regExp = /(\d{1,2})\:(\d{1,2})\:(\d{1,2})/;
						if ($scope.current_date_format_ddMMyyyy == note.startDate&&(parseInt($scope.current_date_format_HHMMSS
								.replace(regExp, "$1$2$3")) > parseInt(note.preferredWorkingHourPerDay
								.replace(regExp, "$1$2$3"))) && $filter('date')($scope.endTime,'HH:mm:ss')> $scope.current_date_format_HHMMSS) 
						{
							$log
							.log("prefered after the current time and end lesa magatsh");
							return { color: "green" };
						}
					};

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
						$rootScope.noteToBeUpdated = 'undefined';
						$location.path("/notes");
					};

					$scope.saveNote = function() {
						$log.log("inside save function");

						$log.log($scope.noteUpdate + " from saveNote function");
						var updateNoteReq = {
							method : 'POST',
							url : 'http://localhost:8081/timeManagement/rest/note/update',
							headers : {
								'Authorization' : $rootScope.token
							},
							data : $scope.noteUpdate

						};
						// this for update note
						if (typeof ($rootScope.noteToBeUpdated) != 'undefined') {
							$log.log($scope.noteUpdate.description
									+ " from if statement");
							if($rootScope.noteToBeUpdated.preferredWorkingHourPerDay==null){
								$log.log("initialize the prefered time");
								$rootScope.noteToBeUpdated.preferredWorkingHourPerDay='00:00:00';
							}

							// $http
							// .post(
							// "http://localhost:8081/timeManagement/rest/note/update",
							// $scope.noteUpdate)
							$http(updateNoteReq).then(function(response) {
								if (response.data.updated == "true") {
									$rootScope.noteToBeUpdated = null;
									$location.path("/notes");
								} else {
									$log.log("can't be update the note");
								}
							});
						} else {
							// means you
							// gonna
							// save a
							// new note

							$log.log($scope.noteUpdate.description
									+ " from else");
							if($scope.noteUpdate.preferredWorkingHourPerDay==null){
								$log.log("initialize the prefered time");
								$scope.noteUpdate.preferredWorkingHourPerDay='00:00:00';
							}

							var newNoteReq;
							// check if you are gonna add the new note for
							// another user if you are admin
							if (typeof ($rootScope.addForAnotherUserAsAmdin) != 'undefined') {
								$log
										.log("email of the user to add note to is: "
												+ $rootScope.addForAnotherUserAsAmdin);
								newNoteReq = {
									method : 'POST',
									url : 'http://localhost:8081/timeManagement/rest/note/add',
									headers : {
										'Authorization' : $rootScope.token
									},
									data : {
										'note' : $scope.noteUpdate,
										'token' : $rootScope.addForAnotherUserAsAmdin
									}

								};

							} else { // add the note for the logged in user

							$log.log("add note for the logged in user and the token is:" + $rootScope.token);
								newNoteReq = {
									method : 'POST',
									url : 'http://localhost:8081/timeManagement/rest/note/add',
									headers : {
										'Authorization' : $rootScope.token
									},
									data : {
										'note' : $scope.noteUpdate,
										'token' : $rootScope.token
									}
								};
							}

							$http(newNoteReq).then(function(response) {
								if (response.data.added == "true") {
									$rootScope.noteToBeUpdated = null;
									$location.path("/notes");
								} else {
									$log.log("can't be create the note");
								}
							});

							$rootScope.addForAnotherUserAsAmdin = 'undefined';
						}
					};
					
					

					// if(typeof ($rootScope.token) == 'undefined'){
					// $location.path("/login");
					// }
					if (typeof ($rootScope.noteToBeUpdated) != 'undefined') {
						$log.log("pre-initialize");
						$scope.initializeForEdit();
					}

				});

app
		.controller(
				'filterNoteCtrl',
				function($scope, $rootScope, $http, $log, $location,
						$routeParams, $filter) {

					if (typeof ($rootScope.token) == 'undefined') {
						$log.log("no token, the user is not logged in yet");
						$location.path("/login");

					}
					$log.log("inside filter function");

					$scope.filterNotes = function() {
						var filterReq = {
							method : 'POST',
							url : 'http://localhost:8081/timeManagement/rest/note/filterByStartAndEndDateForUser',
							headers : {
								'Authorization' : $rootScope.token
							},
							data : {
								'startDate' : $scope.startDate,
								'endDate' : $scope.endDate,
								'userToken' : $rootScope.token
							}

						};

						$http(filterReq).then(function(response) {
							$scope.rows = response.data;
							$log.log("notes: " + response.data[0].notes[0]);
						}, function(response) {
							$log.log("unauthorized");
						});
					};
				});
