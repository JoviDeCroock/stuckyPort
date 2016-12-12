(function(){
  'use strict';

  angular
    .module('stuckyToys')
    .controller('storyController', storyController);

  storyController.$inject = ['$location', 'authService', 'storyService','themeFactory', 'widgetService'];

  function storyController ($location, authService, storyService, themeFactory, widgetService) {
    var vm = this;
    vm.username = authService.currentUser();

    vm.activeStory = {
      themes: [],
      scenes: [],
    };

    // scenes
    vm.addScene = addScene;
    vm.removeScene = removeScene;
    vm.selectScene = selectScene;
    // themes
    vm.themes = themeFactory.themes;
    vm.selectTheme = selectTheme;
    vm.themeChooserChanged = themeChooserChanged;
    vm.removeTheme = removeTheme;
    vm.newTheme = newTheme;
    vm.addTheme = addTheme;
    vm.hideThemeSelection = hideThemeSelection;
    // widgets
    vm.types = widgetService.types;
    vm.widgets = widgetService.widgets;
    vm.selectWidget = selectWidget;
    vm.widgetChooserChanged = widgetChooserChanged;
    vm.newWidget = newWidget;
    vm.removeWidget = removeWidget;
    vm.hideWidgetSelection = hideWidgetSelection;
    // scenes
    function addScene() {
      var temp = {
        sceneNr: vm.activeStory.scenes.length + 1,
        text: '',
        widgets: [],
        hints: []
      };
      vm.activeStory.scenes.push(temp);
      vm.activeScene = temp;
      vm.newWidgetClicked = false;
      vm.selectWidgetClicked = false;
    };
    function removeScene(scene) {
      vm.activeStory.scenes.splice(vm.activeStory.scenes.indexOf(scene),1);
      vm.activeScene = null;
    };
    function selectScene (scene) {
      vm.activeScene = scene;
      vm.newWidgetClicked = false;
      vm.selectWidgetClicked = false;
    };
    // themes
    function selectTheme() {
      vm.selectThemeClicked = true;
      vm.newThemeClicked = false;
    };
    function themeChooserChanged() {
      vm.activeStory.themes.push(vm.selectedTheme);
      vm.selectThemeClicked = false;
      vm.newThemeClicked = false;
    };
    function removeTheme(theme) {
      vm.activeStory.themes.splice(vm.activeStory.themes.indexOf(theme),1);
    };
    function newTheme() {
      vm.newThemeClicked = true;
      vm.selectThemeClicked = false;
      vm.addThemeError = false;
    };
    function addTheme() {
      // vm.themes.push(temp);
      themeFactory.addTheme(vm.themeToAdd)
        .success(function(data) {
          vm.themes = themeFactory.themes;
          vm.addThemeError = null;
          vm.themeToAdd = {};
          vm.hideThemeSelection();
          alert('Het thema '+data.name+' is succesvol aangemaakt');
        })
        .error(function(err) {
          vm.addThemeError = err.message;
        });
    };
    function hideThemeSelection() {
      vm.selectThemeClicked = false;
      vm.newThemeClicked = false;
    };
    //widgets
    function selectWidget() {
      vm.selectWidgetClicked = true;
      vm.newWidgetClicked = false;
    };
    function widgetChooserChanged() {
      // console.log(vm.selectedWidget);
      var temp = vm.selectedWidget;
      vm.activeScene.widgets.push(temp);
      vm.hideWidgetSelection();
    };
    function newWidget() {
      vm.newWidgetClicked = true;
      vm.selectWidgetClicked = false;
    };
    function removeWidget(widget) {
      vm.activeScene.widgets.splice(vm.activeScene.widgets.indexOf(widget),1);
    };
    function hideWidgetSelection() {
      vm.selectWidgetClicked = false;
      vm.newWidgetClicked = false;
    };

    vm.logOut = logOut;

    function logOut () {
      authService.logOut();
      $location.path('/auth');
    }
  };
})();
