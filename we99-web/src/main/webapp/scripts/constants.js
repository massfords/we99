var app = angular.module('we99App');

// API
var restBase = 'services/rest/';
app.constant('RestURLs', {
    base: '/we99/',
    plateType: restBase + 'plateType/',
    experiment: restBase + 'experiment/'
});