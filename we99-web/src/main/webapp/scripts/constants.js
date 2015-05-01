var app = angular.module('we99App');

/** Can Dependency Inject Lodash */
app.constant('_', window._);

/** Accepted Units of measures for compounds */
app.constant('kCompoundUOM', {
  'uM': 'MICROMOLAR',
  'mM': 'MILLIMOLAR',
  'nM': 'NANOMOLAR',
  'pM': 'PICOMOLAR'
});


/** API */
var restBase = 'services/rest/';
app.constant('RestURLs', {
  plateMap:  restBase + 'plateMap',
  plateMapById: restBase + 'plateMap/:id',
  mergeInfoTemplate: restBase + 'plateMap/:id/merge',
  mergeInfoSubmit: restBase + 'experiment/:id/plates/merge',
  mergeInfoSubmitWithCompound: restBase + 'experiment/:id/plates/bulkmerge',
  plateType: restBase + 'plateType/:id',
  compound: restBase + 'compound/:id',
  compoundUpload: restBase + 'compound/',
  experiment: restBase + 'experiment',
  protocol: restBase + 'protocol',
  user: restBase + 'user',
  result: restBase + 'results/',
  serverSettings: restBase + 'settings/email/config',
  emailFilter: restBase + 'settings/email/filter',
  resultsUploadPerPlate: function (experimentId, plateId) {
    return restBase + 'experiment/' + experimentId + '/plates/' + plateId + '/results';
  },
  resultsUpload: function (experimentId) {
    return restBase + 'experiment/' + experimentId + '/plates/results';
  }
});

/** Merge Types for addplate*/
app.constant('MergeType', {
  ADD: 'ADD',
  ADD_W_CMPD: 'ADD_W_CMPD',
  FULL_MONTY: 'FULL_MONTY'
});

/** ng-joyride Tour configs **/
app.constant('TourConstants', {
  // Removes any jquery selectors not found on the current page (avoid tour crashes)
  checkTourConfig: function(tourConfig) {
      var list = tourConfig;
      var newList = [];
      for (var i = 0; i < list.length; i++) {
        if (list[i].selector) {
          if ($(list[i].selector).length)
            newList.push(list[i]);
        }
        else
          newList.push(list[i]);

      }
      return newList;
},
  mainPageTour:[
    {
      type: "title",
      heading: "Welcome to the West-East 99 tour",
      text: '<div class="row"><div id="title-text" class="col-md-12">'
      +'<span class="main-text">Welcome to <strong>West-East 99 Application Tour</strong></span>'
      +'<br>This tour will walk you through the features of our application.</div></div>'

    }

  ],

  // Experiment Tour settings
  experimentTour: [
    {
      type: "title",
      heading: "Welcome to the West-East 99 tour",
      text: '<div class="row"><div id="title-text" class="col-md-12">'
      +'<span class="main-text">Welcome to <strong>West-East 99 Application Tour</strong></span>'
      +'<br>This tour will walk you through the features of our application.</div></div>'

    },{
      type: "element",
      selector: "#expTable",
      heading: "Manage Experiments",
      text: "You can see your current list of experiments in this table",
      placement: "bottom",
      scroll: true
    },{
      type: "element",
      selector: "#expTable",
      heading: "Manage Experiments",
      text: "Click the zoom in icon or double click the row to see experiment plates",
      placement: "bottom",
      scroll: true
    },{
      type: "element",
      selector: "#expTable",
      heading: "Manage Experiments",
      text: "You can also click the x button to delete experiments or publish experiments to lock them down.",
      placement: "bottom",
      scroll: true
    },{
      type: "element",
      selector: "#searchbar",
      heading: "Manage Experiments",
      text: "Use the searchbar to filter for specific experiments and click the column headers to sort the table",
      placement: "bottom",
      scroll: true
    },
    {
      type: "element",
      selector: "#btnPanel",
      heading: "Manage Experiments",
      text: "You can add new experiments and edit settings on existing ones using these buttons",
      placement: "top",
      scroll: true
    }
  ],
  //==================================
  experimentCreateTour:[{
    type: "element",
    selector: "#newExpForm",
    heading: "Manage Experiments",
    text: "Use this form to enter information about a new experiment",
    placement: "top",
    scroll: true
  },{
    type: "element",
    selector: "#btnPanel",
    heading: "Manage Experiments",
    text: "You can assign and remove users to this experiment with these buttons",
    placement: "top",
    scroll: true
  },
    {
      type: "element",
      selector: "#saveBtn",
      heading: "Manage Experiments",
      text: "When done entering information for the new experiment, save by clicking this button",
      placement: "top",
      scroll: true
    }
  ],

  experimentDetailsTour:[],
  plateMapTour: [
    {
    type: "element",
    selector: "#plateMapTable",
    heading: "Manage Plate Maps",
    text: "This is the Plate Map page. This table lists all of plate maps in the system.",
    placement: "top",
    scroll: true
    },
    {
      type: "element",
      selector: "#searchbar",
      heading: "Manage Plate Maps",
      text: "Use the searchbar to filter for specific plate maps and click the column headers to sort the table",
      placement: "bottom",
      scroll: true
    },
    {
    type: "element",
    selector: "#addBtn",
    heading: "Manage Plate Maps",
    text: "Click this button to add a new plate map by uploading a csv definition file"
   }

  ],
  compoundTour: [
    {
      type: "element",
      selector: "#searchFilter",
      heading: "Manage Compounds",
      text: "Enter a compound name to see if it is already in the system"

},{
      type: "element",
      selector: "#addBtn",
      heading: "Manage Compounds",
      text: "Click this button to import more compounds into the system via a csv file"

    }],
  plateTypeTour:[    {
    type: "element",
    selector: "#plateTypeTable",
    heading: "Manage Plate Types",
    text: "This is the Plate Type page. This table lists all of plate types in the system.",
    placement: "top",
    scroll: true
  },
    {
      type: "element",
      selector: "#searchbar",
      heading: "Manage Plate Types",
      text: "Use the searchbar to filter for specific plate types and click the column headers to sort the table",
      placement: "bottom",
      scroll: true
    },
    {
      type: "element",
      selector: "#addBtn",
      heading: "Manage Plate Types",
      text: "Click this button to get a form to create a new plate type"
    }],
  emailConfigTour:[ {
    type: "element",
    selector: "#emailPatternDiv",
    heading: "Email Config",
    text: "You can customize the allowable email domains for new users by entering a regex here",
    placement: "top",
    scroll: true
  },
    {
      type: "element",
      selector: "#emailServerDiv",
      heading: "Email Config",
      text: "Specify the mail server settings for user registration."
    },
    ],
  heatmapTour:[],
  omnimapTour:[],
  wellQCTour:[],
  doseResponse:[],
  compoundResults:[]





});
