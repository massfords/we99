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
            query: {method: "GET",
                    isArray: true,
                    // Get an array back to exhibit expected query behavior
                    transformResponse: valuesToArray
            },
            put: {method: "PUT", isArray: false}
        }),
        experiment: $resource(RestURLs.experiment,{},{}),
        results: $resource(RestURLs.result,{}, {})
    };

  /** For lists made into objects, turn them back into arrays.
   * Assumes the call object attribute is 'values'
   */
  function valuesToArray(data) {
    return JSON.parse(data).values;
  }
}]);

