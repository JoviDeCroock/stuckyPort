(function(){
  'use strict';

  angular
    .module('stuckyToys')
    .controller('storyController', storyController);

  storyController.$inject = [];

  function storyController(){
    var vm = this;
    vm.title = "Maak verhaal";
  };
})();
