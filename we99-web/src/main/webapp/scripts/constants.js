var app = angular.module('we99App');

// API
var restBase = 'services/rest/';
app.constant('RestURLs', {
    plateType: restBase + 'plateType/:id',
    experiment: restBase + 'experiment',
    protocol: restBase + 'protocol',
    user: restBase + 'user',
    plateMap: restBase + 'plateMap',
    result: restBase + 'results/',
    serverSettings: restBase + 'settings/email/config',
    emailFilter: restBase + 'settings/email/filter',
    resultsUpload: function(experimentId,plateId){
      return restBase+'experiment/'+experimentId+'/plates/'+plateId+'/results';
    }
});
