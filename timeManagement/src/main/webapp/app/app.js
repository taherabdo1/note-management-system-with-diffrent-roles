'use strict';

// Declare app level module which depends on views, and components
angular.module('myApp', [
  'ngRoute',
  'myApp.view1',
  'myApp.view2',
  'myApp.login',
  'myApp.signup',
  'myApp.version'
])
.config(['$locationProvider', '$routeProvider', function($locationProvider, $routeProvider) {
  $locationProvider.hashPrefix('!');

  $routeProvider.otherwise({redirectTo: '/view1'});
}])
.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/login', {
    templateUrl: 'login/login.html',
    controller: 'LoginCtrl'
  })
  .when('/view1', {
    templateUrl: 'view1/view1.html',
    controller: 'View1Ctrl'
  })
  .when('/view2', {
    templateUrl: 'view2/view2.html',
    controller: 'View2Ctrl'
  })
  .when('/signup', {
    templateUrl: 'signup/signup.html',
    controller: 'SignupCtrl'
  });
}]);
