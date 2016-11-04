var mongoose = require('mongoose');
var Figure = mongoose.model('Figure');
var Picture = mongoose.model('Picture');

Figure.find({}, function(err, figures){
  if(figures.length === 0){
    var bever = new Picture();
    bever.pad = 'figures/bever.png';
    bever.save(function(err){
      if(err){ console.log(err); }
    });
    var geit = new Picture();
    geit.pad = 'figures/geit.png';
    geit.save(function(err){
      if(err){ console.log(err); }
    });
    var wasbeer = new Picture();
    wasbeer.pad = 'figures/wasbeer.png';
    wasbeer.save(function(err){
      if(err){ console.log(err); }
    });

    var figure1 = new Figure();
    figure1.name = 'de bever';
    figure1.type = 'bever';
    figure1.description = 'Dit is een leuke bever die graag dammen bouwt';
    figure1.picture = bever;
    figure1.save(function(err){
      if(err){ console.log(err); }
    });
    var figure2 = new Figure();
    figure2.name = 'de geit';
    figure2.type = 'geit';
    figure2.description = 'Deze geit klimt graag op rotsen';
    figure2.picture = geit;
    figure2.save(function(err){
      if(err){ console.log(err); }
    });
    var figure3 = new Figure();
    figure3.name = 'de wasbeer';
    figure3.type = 'wasbeer';
    figure3.description = 'Dit is de vriendelijkste wasbeer die je ooit zal tegenkomen';
    figure3.picture = wasbeer;
    figure3.save(function(err){
      if(err){ console.log(err); }
    });
  }
});
