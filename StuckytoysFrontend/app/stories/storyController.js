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
    vm.types = ['Afbeelding', 'Geluid'];
    vm.widgets = widgetService.widgets;
    vm.selectWidget = selectWidget;
    vm.widgetChooserChanged = widgetChooserChanged;
    vm.newWidget = newWidget;
    vm.removeWidget = removeWidget;
    vm.hideWidgetSelection = hideWidgetSelection;
    vm.getImageFileName = getImageFileName;
    vm.saveImage = saveImage;
    vm.saveSound = saveSound;
    // hints
    vm.newHint = newHint;
    vm.addHint = addHint;
    vm.removeHint = removeHint;
    // story
    vm.saveStory = saveStory;

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
      themeFactory.getTheme(vm.selectedTheme)
        .success(function(data) {
          vm.activeStory.themes.push(themeFactory.theme);
        })
        .error(function(err) {
          console.log(err); // todo error handling
        });
      vm.selectThemeClicked = false;
      vm.newThemeClicked = false;
    };
    function removeTheme(theme) {
      vm.activeStory.themes.splice(vm.activeStory.themes.indexOf(theme),1);
    };
    function newTheme() {
      vm.newThemeClicked = true;
      vm.selectThemeClicked = false;
      vm.addThemeError = null;
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
    // widgets
    function selectWidget() {
      vm.selectWidgetClicked = true;
      vm.newWidgetClicked = false;
    };
    function widgetChooserChanged() {
      // console.log(vm.selectedWidget);
      widgetService.getWidget(vm.selectedWidget)
        .success(function(data) {
          vm.activeScene.widgets.push(widgetService.widget);
        })
        .error(function(err) {
          console.log(err); // todo error handling
        });
      vm.hideWidgetSelection();
    };
    function newWidget() {
      vm.newWidgetClicked = true;
      vm.selectWidgetClicked = false;
      vm.addImageError = null;
    };
    function removeWidget(widget) {
      vm.activeScene.widgets.splice(vm.activeScene.widgets.indexOf(widget), 1);
    };
    function hideWidgetSelection() {
      vm.selectWidgetClicked = false;
      vm.newWidgetClicked = false;
    };
    function getImageFileName(widget) {
      return widgetService.getImageFileName(widget);
    };
    function saveImage() {
      // console.log(vm.image);
      widgetService.addImage(vm.image)
        .success(function(data) {
          vm.widgets = widgetService.widgets;
          vm.addImageError = null;
          vm.image = {};
          vm.hideWidgetSelection();
          alert('De widget '+ data.id +' is succesvol aangemaakt');
        })
        .error(function(err) {
          vm.addImageError = err.message;
        });
    };
    function saveSound() {
      widgetService.addSound(vm.sound)
        .success(function(data) {
          vm.widgets = widgetService.widgets;
          vm.addSoundError = null;
          vm.sound = {};
          vm.hideWidgetSelection();
          alert('De widget '+ data.id +' is succesvol aangemaakt');
        })
        .error(function(err) {
          vm.addSoundError = err.message;
        });
    };
    // hints
    function newHint() {
      vm.newHintClicked = true;
    };
    function addHint() {
      var temp = vm.hint;
      vm.activeScene.hints.push(temp);
      vm.hint = '';
      vm.newHintClicked = false;
    };
    function removeHint(hint) {
      vm.activeScene.hints.splice(vm.activeScene.hints.indexOf(hint), 1);
    };
    function saveStory() {
      storyService.createStory(vm.activeStory)
        .success(function(data) {
          alert('Het verhaal ' + data.name + ' werd toegevoegd');
          $location.path('/stories');
        })
        .error(function(err) {
          vm.saveError = err.message;
        });
    };

    vm.logOut = logOut;

    function logOut () {
      authService.logOut();
      $location.path('/auth');
    }
  };
})();
