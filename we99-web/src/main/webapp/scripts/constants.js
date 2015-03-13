var app = angular.module('we99App');

// API
var restBase = 'services/rest/';
app.constant('RestURLs', {
    plateType: restBase + 'plateType/',
    experiment: restBase + 'experiment/',
    result: restBase + 'results/'
});
