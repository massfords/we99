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
  cleanTourConfig: function(tourConfig) {
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

  //===== Main Page Tour
  mainPageTour:[
    {
      type: "title",
      heading: "Welcome to the West-East 99 tour",
      text: '<div class="row"><div id="title-text" class="col-md-12">'
      +'<span class="main-text">Welcome to <strong>West-East 99 Application Tour</strong></span>'
      +'<br>This tour will walk you through the features of our application.</div></div>'

    }

  ],

  //===== Assay Tour settings
  experimentListTour: [
    {
      type: "title",
      heading: "Welcome to the West-East 99 tour",
      text: 'This is the Manage Assays page. You can create, edit, and delete from'
            +' this screen.'

    },{
      type: "element",
      selector: "#expTable",
      heading: "Manage Assays",
      text: "You can see your current list of assays in this table",
      placement: "bottom",
      scroll: true
    },{
      type: "element",
      selector: "tr:last",
      //.glyphicon-zoom-in
      heading: "Manage Assays",
      text: "Click the zoom in icon to see assays details and its plates.",
      placement: "bottom",
      scroll: true
    }
    ,{
      type: "element",
      selector: ".glyphicon-remove-circle:last",
      heading: "Manage Assays",
      text: "You can also click the x button to delete assays.",
      placement: "top",
      scroll: true
    }
    ,{
      type: 'element',
      selector: ".publishLink:last",
      heading: "Manage Assays",
      text: "You can click this link to publish an assay and lock down its results.",
      placement: "top",
      scroll: true,
      attachToBody: true
    },
    {
      type: "element",
      selector: "#btnPanel",
      heading: "Manage Assays",
      text: "You can add new assays and edit settings on existing ones using these buttons",
      placement: "top",
      scroll: true
    }
  ],
  //====== Assay Create/Edit Tour
  experimentCreateTour:[{
    type: "element",
    selector: "#newExpForm",
    heading: "Manage Assays",
    text: "Use this form to enter information about a new assay",
    placement: "top",
    scroll: true
  },{
    type: "element",
    selector: "#btnPanel",
    heading: "Manage Assays",
    text: "You can assign and remove users to this experimental assay with these buttons",
    placement: "top",
    scroll: true
  },
    {
      type: "element",
      selector: "#saveBtn",
      heading: "Manage Assays",
      text: "When done entering information for the assay, save by clicking this button",
      placement: "top",
      scroll: true
    }
  ],

  //===== Assay Details Tour
  experimentDetailsTour:[
    {
    type: "element",
    selector: "#primary-details",
    heading: "Assay Details",
    text: "This is the Assay Details page. Details for the specific assay are listed here.",
    placement: "top",
    scroll: true
  }
    ,
    {
      type: "element",
      selector: "#plateTable",
      heading: "Assay Details",
      text: "The table shows the list of plates associated with this assay",
      placement: "top",
      scroll: true
    }
    ,
    {
      type: "element",
      selector: "#addPlateBtn",
      heading: "Assay Details",
      text: "Use to button to add a single new plate with a list of compounds and dosage concentrations.",
      placement: "top",
      scroll: true
    }
    ,{
      type: "element",
      selector: "#bulkPlateBtn",
      heading: "Assay Details",
      text: "Use this button to create a bulk set of plates or upload a bulk set of results using csv files.",
      placement: "top",
      scroll: true,
      attachToBody:true
    }

    ,{
      type: "element",
      selector: ".glyphicon-upload:first",
      heading: "Assay Details",
      text: "Click the upload button on a row to import results for a single a plate in the assay.",
      placement: "top",
      scroll: true
    }
    ,
    {
      type: "element",
      selector: ".glyphicon-remove-circle:first",
      heading: "Assay Details",
      text: "Click the delete button to remove a plate from the assay.",
      placement: "top",
      scroll: true
    }

  ],

  //==== Plate Map Tour
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

  //===== Compound Tour
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

  //===== Plate Type Tour
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
      text: "Click this button to create a new plate type by filling out a form"
    }],

  //======= Email Config Tour
  emailConfigTour:[
    {
      type: "title",
      heading: "Email Config",
      text: "This is the Email Config page. Here you can specify the server settings pertaining to user registration.",
      placement: "top",
      scroll: true
    },
    {
    type: "element",
    selector: "#emailPatternDiv",
    heading: "Email Config",
    text: "You can customize the allowable email domains for new users by entering a regex pattern here",
    placement: "top",
    scroll: true
  },
    {
      type: "element",
      selector: "#emailServerDiv",
      heading: "Email Config",
      text: "Specify the mail server settings for this server. This is used for sending new user"
            +" registration emails.",
      placement: "top"
    }
    ],

  //===== Heatmap Tour
  heatmapTour:[
    {
      type: "title",
      heading: "Heatmaps",
      text: "This is the Heatmap page. Here you can view the heatmap for each plate associated with the"
            +" selected assay",
      placement: "bottom",
      scroll: true
    },{
      type: "element",
      selector: "#controls",
      heading: "Heatmaps",
      text: "Select an assay from the drop down here. You can also select the coloring scheme used"
            +" to generate the heatmap",
      placement: "bottom",
      scroll: true
    },{
      type: "element",
      selector: "#display-box",
      heading: "Heatmaps",
      text: "The selected plate heatmap is displayed here. You can click on wells to exclude them.",
      placement: "right",
      scroll: true
    },
    {
      type: "element",
      selector: "#headingOne",
      heading: "Heatmaps",
      text: "This panel displays a summary of result values for the plate. You can"
            +" navigate to different plates by clicking a row in this table.",
      placement: "left",
      scroll: true
    },
    {
      type: "element",
      selector: "#headingTwo",
      heading: "Heatmaps",
      text: "This panel displays a legend for the well text abbreviations in the heatmap",
      placement: "left",
      scroll: true
    },
    {
      type: "element",
      selector: "#headingThree",
      heading: "Heatmaps",
      text: "This panel displays the numeric values that correspond to the color intensity"
      +" in the heatmap",
      placement: "left",
      scroll: true
    },
    {
      type: "element",
      selector: "#list-box",
      heading: "Heatmaps",
      text: "You can select a different plate heatmap by clicking on one of the thumbnails here as well.",
      placement: "left",
      scroll: true
    }

  ],

  //===== Omnimap Tour
  omnimapTour:[
    {
      type: "title",
      heading: "Omnimap",
      text: "This is the Omnimap page. Here you can see the entire assay plate set"
            +" as one continuous heatmap.",
      scroll: true
    },{
      type: "element",
      selector: "#controls",
      heading: "Omnimap",
      text: "You can select the assay in the dropdown menu.<br> You can choose the heatmap color scheme from here as well."
            +" Clicking the radio buttons will allow you to switch modes between 'zoom' and 'delete'."
            ,
      placement: "bottom",
      scroll: true
    },{
      type: "element",
      selector: "#display-wrapper",
      heading: "Omnimap",
      text: "The omnimap is displayed here. In zoom mode, you can drag your mouse over a region to zoom into a"
            +" specific area. In delete mode, you can click on wells to mark them as outliers.",
      placement: "top",
      scroll: true
    }

  ],

  //=====  Well QC Tour
  wellQCTour:[
    {
    type: "title",
    heading: "Well Quality Control",
    text: "This is the Well Quality Control page. Here you can view the positive and negative well controls for"
    +" the entire assay. You can also knockout well control points by clicking on them to mark them as outliers.",
    placement: "bottom",
    scroll: true
  },{
    type: "element",
    selector: "#controls",
    heading: "Well Quality Control",
    text: "Select an assay from the drop down here. This will populate the scatterplot graph"
          +" with the well control values.",
    placement: "bottom",
    scroll: true
  },{
    type: "element",
    selector: "#display-container",
    heading: "Well Quality Control",
    text: "The well control values will be displayed in a scatterplot here.",
    placement: "right",
    scroll: true
  }
   ],

  //==== Dose Response Tour
  doseResponseTour:[
    {
      type: "title",
      heading: "Dose Response",
      text: "This is the dose response analysis page. Here you can view the individual dose response curves"
            +" of different compounds. You can also \"knockout\" outlier points by clicking them, which will cause"
            +" the system to recalculate the curve.",
      placement: "bottom",
      scroll: true
    },{
      type: "element",
      selector: "#controls",
      heading: "Dose Response",
      text: "Select an assay from the drop down here. This will populate the table with compounds.",
      placement: "bottom",
      scroll: true
    },{
      type: "element",
      selector: "#compoundTable",
      heading: "Dose Response",
      text: "Clicking on a compound in this table will display its dose response curve to the right,"
            +" along with a scatter plot of its data points.",
      placement: "right",
      scroll: true,
      attachToBody:true
    },
    {
      type: "element",
      selector: "#display-container",
      heading: "Dose Response",
      text: "The dose response curve is displayed here. Clicking on a dot will mark it as an outlier"
            +" and color it red. The curve will then be recalculated without this value.",
      placement: "bottom",
      scroll: true
    }
  ],
  compoundResultsTour:[{
    type: "title",
    heading: "Compound Results and Comparison",
    text: "This is the compound results page. Here you can compare the dose response curves of different"
        +" compounds against each other.",
    placement: "bottom",
    scroll: true
  },{
    type: "element",
    selector: "#controls",
    heading: "Compound Results and Comparison",
    text: "Select an assay from the drop down here. This will populate the table with compounds.",
    placement: "bottom",
    scroll: true
  },{
    type: "element",
    selector: "#compoundTable",
    heading: "Compound Results and Comparison",
    text: "Clicking on a compound in this table will display its dose response to the right."
        +" You can select multiple compounds to compare curves. Clicking on a already selected compound will deselect it.",
    placement: "right",
    scroll: true,
    attachToBody:true
  }
  ]

});
