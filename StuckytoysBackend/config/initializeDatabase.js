var mongoose = require('mongoose');
var Picture = mongoose.model('Picture');
var Admin = mongoose.model('Admin');
var Story = mongoose.model('Story');
var Theme = mongoose.model('Theme');
var Scene = mongoose.model('Scene');
var Widget = mongoose.model('Widget');
var WidgetFile = mongoose.model('WidgetFile');

/* Admin */
Admin.find({}, function(err, ad)
{
  if(ad.length === 0)
  {
    var admin = new Admin();
    admin.username = "admin";
    admin.email  = "admin@gmail.com";
    admin.password = "wachtwoord";
    admin.save(function(err)
    {
      if(err){console.log(err);}
    });
  }
});
/* Story */
Story.find({},function(err, adhd)
{
  if(adhd.length === 0) {
    /*Theme*/
    var vervuiling = new Theme();
    vervuiling.name = "Vervuiling";
    vervuiling.description = "Dit onderwerp kaart de extreme vervuiling in ons land aan";
    vervuiling.save(function (err) {
      if (err) {
        console.log(err);
      }
    });

    /*Widget*/
    var widget = new Widget();
    widget.id = 'bustoeter';
    widget.widgetFiles = [];
    var gameW = new Widget();
    gameW.id = 'minigame';
    gameW.widgetFiles = [];
    var wfGame = new WidgetFile();
    wfGame.fileName = 'RecycleActivity';
    wfGame.type = 'Spel';
    var wf = new WidgetFile();
    wf.fileName = 'bushorn.mp3';
    wf.type = 'Geluid';
    var wf2 = new WidgetFile();
    wf2.fileName = 'bushorn.jpg';
    wf2.type = 'Afbeelding';
    wf.save(function (err) {
      if (err) {
        console.log(err);
      }
    });
    widget.widgetFiles.push(wf);
    wf2.save(function (err) {
      if (err) {
        console.log(err);
      }
    });
    wfGame.save(function (err) {
      if (err) {
        console.log(err);
      }
    });
      var wf3 = new WidgetFile();
      wf3.fileName = 'ArActivity';
      wf3.type='AR';
      wf3.save(function(err)
      {
          if (err) {
              console.log(err);
          }
      });
      var ARWidget = new Widget();
      ARWidget.id = 'AR';
      ARWidget.widgetFiles = [];
      ARWidget.widgetFiles.push(wf3);
      ARWidget.save(function (err)
      {
          if (err) {
              console.log(err);
          }
      });

      var Recording = new Widget();
      Recording.id = 'opname';
      Recording.widgetFiles = [];
      var wf4 = new WidgetFile();
      wf4.filename = "opname";
      wf4.type="Opname";
      wf4.save(function(err)
      {
          if(err){console.log(err);}
      });
      Recording.widgetFiles.push(wf4);
      Recording.save(function (err)
      {
          if (err) {
              console.log(err);
          }
      });
    widget.widgetFiles.push(wf2);
    gameW.widgetFiles.push(wfGame);
    widget.save(function (err) {
      if (err) {
        console.log(err);
      }
    });
    gameW.save(function (err) {
      if (err) {
        console.log(err);
      }
    });


    /*Scenes*/
      var scene1 = new Scene();
      var scene2 = new Scene();
      var scene3 = new Scene();
      var scene4 = new Scene();
      var scene5 = new Scene();
      scene1.text = "Vervuiler gooit blikje op straat. Tuut de vervuiler weg";
      scene2.text = "Vertel verhaal over hoe slecht vervuiling is voor de diertjes";
      scene3.text = "Gooi het blikje in de vuilbak";
      scene4.text = "Vertel de vervuiler dat het slecht is om dit te doen";
      scene4.text = "Vind het verloren papier zodat de bus weer verder kan";
      scene1.hints = [];
      scene2.hints = [];
      scene3.hints = [];
      scene4.hints = [];
      scene5.hints = [];
      var text = "TIP: Recyclage vermindert uitstoot";
      scene1.hints.push(text);
      var text = "TIP: Er staan boetes op vervuilen";
      scene1.hints.push(text);
      var text = "TIP: Zeedieren kunnen verstrikt raken";
      scene2.hints.push(text);
      var text = "TIP: Dankzij het goed sorteren in de juiste vuilbak betert het milieu";
      scene3.hints.push(text);
      var text = "TIP: Sluikstorten kost je lokale gemeente veel geld";
      scene4.hints.push(text);
      var text = "TIP: Vertel dat het kind best altijd alles recycleert en anderen daartoe best aanspoort";
      scene5.hints.push(text);
      scene1.layout = 3;
      scene2.layout = 2;
      scene3.layout = 1;
      scene4.layout = 2;
      scene5.layout = 2;
      scene1.widgets = [];
      scene1.widgets.push(widget);
      scene2.widgets = [];
      scene3.widgets = [];
      scene4.widgets = [];
      scene5.widgets = [];
      scene3.widgets.push(gameW);
      scene4.widgets.push(Recording);
      scene5.widgets.push(ARWidget);
      scene1.sceneNr = 1;
      scene2.sceneNr = 2;
      scene3.sceneNr = 3;
      scene4.sceneNr = 4;
      scene5.sceneNr = 5;
    scene1.save(function (err) {
      if (err) {
        console.log(err);
      }
    });
    scene2.save(function (err) {
      if (err) {
        console.log(err);
      }
    });
    scene3.save(function (err) {
      if (err) {
        console.log(err);
      }
    });
      scene4.save(function (err) {
          if (err) {
              console.log(err);
          }
      });
      scene5.save(function (err) {
          if (err) {
              console.log(err);
          }
      });
    /*Story*/
    var story = new Story();
    story.saveDate("21 11 2016");
    story.name = "Recyclage";
    story.scenes = [];
    story.price = 2.5;
    story.scenes.push(scene1);
    story.scenes.push(scene2);
    story.scenes.push(scene3);
    story.scenes.push(scene4);
    story.scenes.push(scene5);
    story.themes = [];
    story.themes.push(vervuiling);
    story.duration = 0.5;
    story.path = "recycleren.jpg";
    story.published = true;
    story.picture = recycleren;
    story.save(function (err) {
      if (err) {
        console.log(err);
      }
    });

    /*Diefstal Story*/
    var diefstal = new Theme();
    diefstal.name = "Diefstal";
    diefstal.description = "Dit thema kaart de foute daad van diefstal aan";
    diefstal.save(function (err) {
      if (err) {
        console.log(err);
      }
    });

    var widget2 = new Widget();
    widget2.id = 'Kassa';
    widget2.widgetFiles = [];
    var wf = new WidgetFile();
    wf.fileName = 'cash.ogg';
    wf.type = 'Geluid';
    var wf2 = new WidgetFile();
    wf2.fileName = 'cash.jpg';
    wf2.type = 'Afbeelding';
    wf.save(function (err) {
      if (err) {
        console.log(err);
      }

    });
    widget2.widgetFiles.push(wf);
    wf2.save(function (err) {
      if (err) {
        console.log(err);
      }

    });
    widget2.widgetFiles.push(wf2);
    widget2.save(function (err) {
      if (err) {
        console.log(err);
      }
    });

    var scene1 = new Scene();
    var scene2 = new Scene();
    var scene3 = new Scene();
    scene1.text = "De bus rijdt een winkel voorbij en hoort dat een overvaller vraagt voor het geld.";
    scene2.text = "De dief loopt weg en het kind moet hem zoeken";
    scene3.text = "Vind de verloren halsketting?";
    scene1.layout = 1;
    scene2.layout = 3;
    scene3.layout = 4;
    var text = "TIP: Stelen is slecht in ieder scenario";
    scene1.hints.push(text);
    scene1.widgets = [];scene1.widgets.push(widget2);
    scene2.widgets = [];
    scene3.widgets = [];scene3.widgets.push(ARWidget);
    scene1.sceneNr = 1;
    scene2.sceneNr = 2;
    scene3.sceneNr = 3;
    scene1.save(function(err)
    {
      if(err){console.log(err);}
    });
    scene2.save(function(err)
    {
      if(err){console.log(err);}
    });
    scene3.save(function(err)
    {
      if(err){console.log(err);}
    });

    /*Story*/
    var diefstalstory = new Story();
    diefstalstory.saveDate("19 11 2016");
    diefstalstory.name = "Diefstal";
    diefstalstory.scenes = [];
    diefstalstory.scenes.push(scene1);
    diefstalstory.scenes.push(scene2);
    diefstalstory.scenes.push(scene3);
    diefstalstory.themes = [];
    diefstalstory.price = 2.5;
    diefstalstory.themes.push(diefstal);
    diefstalstory.published = true;
    diefstalstory.path = 'bever.png';
    diefstalstory.duration = 0.5;
    diefstalstory.save(function(err)
    {
      if(err)
      {
        console.log(err);
      }
    });
  }
});