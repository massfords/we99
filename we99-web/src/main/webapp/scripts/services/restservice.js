'use strict';

/**
 * @ngdoc service
 * @name we99App.restService
 * @description
 * # restService
 * Service in the we99App.
 */
var app = angular.module('we99App');
app.factory('RestService', ['$resource','RestURLs', function ($resource, RestURLs) {
    return {
        plateType: $resource(RestURLs.plateType,{}, {
            query: {method: "GET", isArray: false},
            put: {method: "PUT", isArray: false}
        }),
        experiment: $resource(RestURLs.experiment,{},{}),
        results: $resource(RestURLs.result,{}, {})
    };
}]);

