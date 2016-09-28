'use strict';

// Declare app level module which depends on views, and components
angular.module('myApp', [
  'ngRoute',
  'myApp.view1',
  'myApp.view2',
  'myApp.notes',
  'myApp.login',
  'myApp.signup',
  'myApp.users',
  'myApp.version'
])
.config(['$locationProvider', '$routeProvider', function($locationProvider, $routeProvider) {
  $locationProvider.hashPrefix('!');

  $routeProvider.otherwise({redirectTo: '/view1'});

}])
.config(['$routeProvider', function($routeProvider) {
  $routeProvider
  .when('/login', {
    templateUrl: 'login/login.html',
    controller: 'LoginCtrl'
  })
  .when('/notes', {
    templateUrl: 'note/showNotes.html',
    controller: 'NotesCtrl'
  })
  .when('/filterNotes', {
    templateUrl: 'note/filterNotes.html',
    controller: 'filterNoteCtrl'
  })
    .when('/addEditNote', {
    templateUrl: 'note/addEditNote.html',
    controller: 'addEditNoteCtrl'
  })
  .when('/users', {
    templateUrl: 'users/showAllUsers.html',
    controller: 'UsersCtrl'
  })
     .when('/addEditUser', {
    templateUrl: 'users/addEditUser.html',
    controller: 'AddEditUserCtrl'
  })
  .when('/signup', {
    templateUrl: 'signup/signup.html',
    controller: 'SignupCtrl'
  })
    .when('/view2', {
    templateUrl: 'view2/view2.html',
    controller: 'View2Ctrl'
  });
}]);
