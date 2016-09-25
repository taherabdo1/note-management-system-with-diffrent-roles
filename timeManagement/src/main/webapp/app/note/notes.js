'use strict';

angular.module('myApp.notes', ['ngRoute'])

.controller('NotesCtrl', function($scope , $rootScope , $http , $log , $location , $routeParams) {

	$log.log("token from the root scope: " + $rootScope.token);
	$scope.getNotes = function(){	
		var  data = {
				"token" : $rootScope.token
				};
		$http.post("http://localhost:8081/timeManagement/rest/note/getAllOfUser" ,data)
    	.then(function(response) {
    		
    		if (response.data.token == "null") {
	    		$log.log(response.data);
    			$scope.successMessage = "you need to login before that";
				$location.path("/login");
			} else {
	  			$log.log("notes came back");
	  			$rootScope.notes = response.data;
	  			$log.log(response.data);	
			}
        $scope.note = response.data;
    });
	};
	$scope.deleteNote = function(index){
		$log.log(index);
		var noteToBeDeleted = $rootScope.notes[index];
		
		var data = {
				id : noteToBeDeleted.id
		};
		
		$http.post("http://localhost:8081/timeManagement/rest/note/delete" ,data)
    	.then(function(response) {
    		if (response.data.deleted == "true") {
    		    $scope.notes.splice(index, 1);
    		}else{
    			$log.log("can't be deleted");
    		}
    	});
	};
	
	$scope.updateNote = function(index){
		$log.log(index);
//		$log.log(note.description);

		var noteToBeUpdated = $scope.notes[index];
		var params = "{'index' : '"+index+"'}";
		$location.path("/addEditNote").search(noteToBeUpdated);
		$log.log(noteToBeUpdated.startDate);
		$scope.initializeForEdit();
	};
	
	$scope.initializeForEdit = function () {
		var index = $routeParams.index;
		$scope.noteUpdate = $rootScope.notes[index];
		$log.log(index);
		$log.log("*********************&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
		$log.log($routeParams.startDate);
//		$scope.noteUpdate = $rootScope.notes[index].startDate;
	};
	$scope.getNotes();
});