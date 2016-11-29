(function(){
  'use strict';

  angular
    .module('stuckyToys')
    .controller('storyController', storyController);

  storyController.$inject = ['$location', 'authService', 'storyService', 'widgetService'];

  function storyController ($location, authService, storyService, widgetService) {
    var vm = this;
    vm.username = authService.currentUser();
    //maak verhaal
    vm.themeChooserClicked = false;
    vm.widgetChooserClicked = false;
    vm.widgets = widgetService.widgets;
    //Wordt uit service gehaald
    vm.themes = [{
      name: 'Recyclage',
      description: 'Het kind leren omgaan met afval'
    }, {
      name: 'Pesten',
      description: 'Moeilijk thema om over te praten'
    }]; // test
    vm.types = [
      'music',
      'game',
      'image'
    ]; //test
    vm.activeStory = {
      name: '',
      date: '',
      themes: [],
      scenes: []
    };
    vm.addTheme = addTheme;
    vm.selectTheme = selectTheme;
    vm.addScene = addScene;
    vm.addText = addText;
    vm.addWidget = addWidget;
    vm.selectScene = selectScene;
    vm.selectType = selectType; //mag weg
    vm.selectWidget = selectWidget;
    vm.addHint = addHint;

    function addTheme () {
      vm.themeChooserClicked = true;
    };
    function selectTheme () {
      vm.activeStory.themes.push(vm.selectedTheme);
      vm.themeChooserClicked = false;
      vm.selectedTheme = {};
    };
    function addScene () {
      var temp = {
        sceneNr: vm.activeStory.scenes.length + 1,
        widgets: [],
        hints: []
      };
      vm.activeStory.scenes.push(temp);
      vm.activeScene = temp;
    };
    function addText () {
      //alert('addText correct');
      vm.activeScene.text = {};
    };
    function addWidget () {
      //alert('addWidget correct');
      vm.widgetChooserClicked = true;
    };
    function selectScene (scene) {
      vm.activeScene = scene;
    };
    function selectType () {
      vm.widgetChooserClicked = false;
      switch(vm.selectedType) {
        case 'music': console.log('music');
          vm.activeScene.activeWidget = {
            type: 'music',
            soundFile: {},
            imageFile: {}
          };
        break;
        case 'game': console.log('game');
          vm.activeScene.activeWidget = {
            type: 'game',
            gameFile: {},
            imageFile: {}
          };
        break;
        case 'image': console.log('image');
          vm.activeScene.activeWidget = {
            type: 'image',
            imageFile: {}
          };
        break;
        default: console.log('niks');
      }
    };
    function selectWidget() {
      console.log(vm.selectedWidget);
    };
    function addHint() {
      vm.activeScene.hints.push('');
    }

    //alle verhalen
    vm.logOut = logOut;

    function logOut () {
      authService.logOut();
      $location.path('/auth');
    }
  };
})();
