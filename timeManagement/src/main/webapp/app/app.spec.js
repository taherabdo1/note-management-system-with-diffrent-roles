describe('LoginCtrl', function() {

  beforeEach(module('myApp'));

  it('should create a `phones` model with 1 note', inject(function($controller) {
    var scope = {};
    var ctrl = $controller('LoginCtrl', {$scope: scope});

    expect(scope.note.length).toBe(1);
  }));

});