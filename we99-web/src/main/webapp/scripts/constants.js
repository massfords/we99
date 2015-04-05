var app = angular.module('we99App');

/** Can Dependency Inject Lodash */
app.constant('_', window._);

/** Accepted Units of measures for compounds */
// MILLIS, MICRO, NANO, PICO
app.constant('kCompoundUOM', {
  'uM': 'MICROMOLAR',
  'mM': 'MILLIMOLAR',
  'nM': 'NANOMOLAR',
  'pM': 'PICOMOLAR'});


/** API */
var restBase = 'services/rest/';
app.constant('RestURLs', {
    plateMap: restBase + 'plateMap/:id',
    mergeInfoTemplate: restBase + 'plateMap/:id/merge',
    mergeInfoSubmit: restBase + 'experiment/:id/plates/merge',
    plateType: restBase + 'plateType/:id',
    compound: restBase + 'compound/:id',
    experiment: restBase + 'experiment',
    protocol: restBase + 'protocol',
    user: restBase + 'user',
    result: restBase + 'results/',
    serverSettings: restBase + 'settings/email/config',
    emailFilter: restBase + 'settings/email/filter',
    resultsUpload: function(experimentId,plateId){
      return restBase+'experiment/'+experimentId+'/plates/'+plateId+'/results';
    }
});
