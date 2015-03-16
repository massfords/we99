var app = angular.module('we99App');

// API
var restBase = 'services/rest/';
app.constant('RestURLs', {
    plateType: restBase + 'plateType/',
    experiment: restBase + 'experiment',
    protocol: restBase + 'protocol',
    user: restBase + 'user',
    result: restBase + 'results/'
});
