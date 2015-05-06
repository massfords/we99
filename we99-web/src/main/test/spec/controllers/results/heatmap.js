'use strict';

describe('Controller: HeatmapCtrl', function () {

  // load the controller's module
  beforeEach(module('we99App'));

  var HeatmapCtrl,
    scope,
    httpBackend,expResp,samplePlates;

  expResp ={
    "totalCount" : 1,
    "page" : 0,
    "pageSize" : 100,
    "values" : [ {
      "id" : 1,
      "name" : "experiment uno",
      "description" : "Experiment using the Alpha protocol",
      "labels" : [ ],
      "status" : "UNPUBLISHED",
      "created" : "2015-05-03T15:44:10.747-04:00",
      "protocol" : {
        "id" : 1,
        "name" : "Alpha"
      }
    }
    ]
  };

  var results={
    "id" : 1,
    "plate" : {
      "id" : 1,
      "name" : "plate 0 for exp 1",
      "barcode" : "abc0",
      "labels" : [ ],
      "wellCount" : 96,
      "experimentId" : 1,
      "wells" : [ {
        "id" : 1,
        "coordinate" : {
          "row" : 0,
          "col" : 0
        },
        "labels" : [ {
          "id" : 1,
          "name" : "loc",
          "value" : "well0,0"
        } ],
        "type" : "NEGATIVE",
        "contents" : [ {
          "id" : 1,
          "amount" : {
            "number" : 0.0,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 177,
            "name" : "C1397-1103"
          }
        } ]
      }, {
        "id" : 53,
        "coordinate" : {
          "row" : 4,
          "col" : 4
        },
        "labels" : [ {
          "id" : 53,
          "name" : "loc",
          "value" : "well4,4"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 53,
          "amount" : {
            "number" : 66.66666666666667,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 181,
            "name" : "C1942-1599"
          }
        } ]
      }, {
        "id" : 2,
        "coordinate" : {
          "row" : 0,
          "col" : 1
        },
        "labels" : [ {
          "id" : 2,
          "name" : "loc",
          "value" : "well0,1"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 2,
          "amount" : {
            "number" : 16.666666666666668,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 177,
            "name" : "C1397-1103"
          }
        } ]
      }, {
        "id" : 54,
        "coordinate" : {
          "row" : 4,
          "col" : 5
        },
        "labels" : [ {
          "id" : 54,
          "name" : "loc",
          "value" : "well4,5"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 54,
          "amount" : {
            "number" : 83.33333333333334,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 181,
            "name" : "C1942-1599"
          }
        } ]
      }, {
        "id" : 3,
        "coordinate" : {
          "row" : 0,
          "col" : 2
        },
        "labels" : [ {
          "id" : 3,
          "name" : "loc",
          "value" : "well0,2"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 3,
          "amount" : {
            "number" : 33.333333333333336,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 177,
            "name" : "C1397-1103"
          }
        } ]
      }, {
        "id" : 55,
        "coordinate" : {
          "row" : 4,
          "col" : 6
        },
        "labels" : [ {
          "id" : 55,
          "name" : "loc",
          "value" : "well4,6"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 55,
          "amount" : {
            "number" : 100.0,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 181,
            "name" : "C1942-1599"
          }
        } ]
      }, {
        "id" : 4,
        "coordinate" : {
          "row" : 0,
          "col" : 3
        },
        "labels" : [ {
          "id" : 4,
          "name" : "loc",
          "value" : "well0,3"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 4,
          "amount" : {
            "number" : 50.0,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 177,
            "name" : "C1397-1103"
          }
        } ]
      }, {
        "id" : 56,
        "coordinate" : {
          "row" : 4,
          "col" : 7
        },
        "labels" : [ {
          "id" : 56,
          "name" : "loc",
          "value" : "well4,7"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 56,
          "amount" : {
            "number" : 116.66666666666667,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 181,
            "name" : "C1942-1599"
          }
        } ]
      }, {
        "id" : 5,
        "coordinate" : {
          "row" : 0,
          "col" : 4
        },
        "labels" : [ {
          "id" : 5,
          "name" : "loc",
          "value" : "well0,4"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 5,
          "amount" : {
            "number" : 66.66666666666667,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 177,
            "name" : "C1397-1103"
          }
        } ]
      }, {
        "id" : 57,
        "coordinate" : {
          "row" : 4,
          "col" : 8
        },
        "labels" : [ {
          "id" : 57,
          "name" : "loc",
          "value" : "well4,8"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 57,
          "amount" : {
            "number" : 133.33333333333334,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 181,
            "name" : "C1942-1599"
          }
        } ]
      }, {
        "id" : 6,
        "coordinate" : {
          "row" : 0,
          "col" : 5
        },
        "labels" : [ {
          "id" : 6,
          "name" : "loc",
          "value" : "well0,5"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 6,
          "amount" : {
            "number" : 83.33333333333334,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 177,
            "name" : "C1397-1103"
          }
        } ]
      }, {
        "id" : 58,
        "coordinate" : {
          "row" : 4,
          "col" : 9
        },
        "labels" : [ {
          "id" : 58,
          "name" : "loc",
          "value" : "well4,9"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 58,
          "amount" : {
            "number" : 150.0,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 181,
            "name" : "C1942-1599"
          }
        } ]
      }, {
        "id" : 7,
        "coordinate" : {
          "row" : 0,
          "col" : 6
        },
        "labels" : [ {
          "id" : 7,
          "name" : "loc",
          "value" : "well0,6"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 7,
          "amount" : {
            "number" : 100.0,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 177,
            "name" : "C1397-1103"
          }
        } ]
      }, {
        "id" : 59,
        "coordinate" : {
          "row" : 4,
          "col" : 10
        },
        "labels" : [ {
          "id" : 59,
          "name" : "loc",
          "value" : "well4,10"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 59,
          "amount" : {
            "number" : 166.66666666666669,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 181,
            "name" : "C1942-1599"
          }
        } ]
      }, {
        "id" : 8,
        "coordinate" : {
          "row" : 0,
          "col" : 7
        },
        "labels" : [ {
          "id" : 8,
          "name" : "loc",
          "value" : "well0,7"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 8,
          "amount" : {
            "number" : 116.66666666666667,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 177,
            "name" : "C1397-1103"
          }
        } ]
      }, {
        "id" : 60,
        "coordinate" : {
          "row" : 4,
          "col" : 11
        },
        "labels" : [ {
          "id" : 60,
          "name" : "loc",
          "value" : "well4,11"
        } ],
        "type" : "POSITIVE",
        "contents" : [ {
          "id" : 60,
          "amount" : {
            "number" : 183.33333333333334,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 181,
            "name" : "C1942-1599"
          }
        } ]
      }, {
        "id" : 9,
        "coordinate" : {
          "row" : 0,
          "col" : 8
        },
        "labels" : [ {
          "id" : 9,
          "name" : "loc",
          "value" : "well0,8"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 9,
          "amount" : {
            "number" : 133.33333333333334,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 177,
            "name" : "C1397-1103"
          }
        } ]
      }, {
        "id" : 10,
        "coordinate" : {
          "row" : 0,
          "col" : 9
        },
        "labels" : [ {
          "id" : 10,
          "name" : "loc",
          "value" : "well0,9"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 10,
          "amount" : {
            "number" : 150.0,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 177,
            "name" : "C1397-1103"
          }
        } ]
      }, {
        "id" : 11,
        "coordinate" : {
          "row" : 0,
          "col" : 10
        },
        "labels" : [ {
          "id" : 11,
          "name" : "loc",
          "value" : "well0,10"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 11,
          "amount" : {
            "number" : 166.66666666666669,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 177,
            "name" : "C1397-1103"
          }
        } ]
      }, {
        "id" : 12,
        "coordinate" : {
          "row" : 0,
          "col" : 11
        },
        "labels" : [ {
          "id" : 12,
          "name" : "loc",
          "value" : "well0,11"
        } ],
        "type" : "NEGATIVE",
        "contents" : [ {
          "id" : 12,
          "amount" : {
            "number" : 183.33333333333334,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 177,
            "name" : "C1397-1103"
          }
        } ]
      }, {
        "id" : 61,
        "coordinate" : {
          "row" : 5,
          "col" : 0
        },
        "labels" : [ {
          "id" : 61,
          "name" : "loc",
          "value" : "well5,0"
        } ],
        "type" : "POSITIVE",
        "contents" : [ {
          "id" : 61,
          "amount" : {
            "number" : 0.0,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 182,
            "name" : "C1181-755"
          }
        } ]
      }, {
        "id" : 62,
        "coordinate" : {
          "row" : 5,
          "col" : 1
        },
        "labels" : [ {
          "id" : 62,
          "name" : "loc",
          "value" : "well5,1"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 62,
          "amount" : {
            "number" : 16.666666666666668,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 182,
            "name" : "C1181-755"
          }
        } ]
      }, {
        "id" : 63,
        "coordinate" : {
          "row" : 5,
          "col" : 2
        },
        "labels" : [ {
          "id" : 63,
          "name" : "loc",
          "value" : "well5,2"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 63,
          "amount" : {
            "number" : 33.333333333333336,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 182,
            "name" : "C1181-755"
          }
        } ]
      }, {
        "id" : 64,
        "coordinate" : {
          "row" : 5,
          "col" : 3
        },
        "labels" : [ {
          "id" : 64,
          "name" : "loc",
          "value" : "well5,3"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 64,
          "amount" : {
            "number" : 50.0,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 182,
            "name" : "C1181-755"
          }
        } ]
      }, {
        "id" : 13,
        "coordinate" : {
          "row" : 1,
          "col" : 0
        },
        "labels" : [ {
          "id" : 13,
          "name" : "loc",
          "value" : "well1,0"
        } ],
        "type" : "NEGATIVE",
        "contents" : [ {
          "id" : 13,
          "amount" : {
            "number" : 0.0,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 178,
            "name" : "C1085-5"
          }
        } ]
      }, {
        "id" : 65,
        "coordinate" : {
          "row" : 5,
          "col" : 4
        },
        "labels" : [ {
          "id" : 65,
          "name" : "loc",
          "value" : "well5,4"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 65,
          "amount" : {
            "number" : 66.66666666666667,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 182,
            "name" : "C1181-755"
          }
        } ]
      }, {
        "id" : 14,
        "coordinate" : {
          "row" : 1,
          "col" : 1
        },
        "labels" : [ {
          "id" : 14,
          "name" : "loc",
          "value" : "well1,1"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 14,
          "amount" : {
            "number" : 16.666666666666668,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 178,
            "name" : "C1085-5"
          }
        } ]
      }, {
        "id" : 66,
        "coordinate" : {
          "row" : 5,
          "col" : 5
        },
        "labels" : [ {
          "id" : 66,
          "name" : "loc",
          "value" : "well5,5"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 66,
          "amount" : {
            "number" : 83.33333333333334,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 182,
            "name" : "C1181-755"
          }
        } ]
      }, {
        "id" : 15,
        "coordinate" : {
          "row" : 1,
          "col" : 2
        },
        "labels" : [ {
          "id" : 15,
          "name" : "loc",
          "value" : "well1,2"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 15,
          "amount" : {
            "number" : 33.333333333333336,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 178,
            "name" : "C1085-5"
          }
        } ]
      }, {
        "id" : 67,
        "coordinate" : {
          "row" : 5,
          "col" : 6
        },
        "labels" : [ {
          "id" : 67,
          "name" : "loc",
          "value" : "well5,6"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 67,
          "amount" : {
            "number" : 100.0,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 182,
            "name" : "C1181-755"
          }
        } ]
      }, {
        "id" : 16,
        "coordinate" : {
          "row" : 1,
          "col" : 3
        },
        "labels" : [ {
          "id" : 16,
          "name" : "loc",
          "value" : "well1,3"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 16,
          "amount" : {
            "number" : 50.0,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 178,
            "name" : "C1085-5"
          }
        } ]
      }, {
        "id" : 68,
        "coordinate" : {
          "row" : 5,
          "col" : 7
        },
        "labels" : [ {
          "id" : 68,
          "name" : "loc",
          "value" : "well5,7"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 68,
          "amount" : {
            "number" : 116.66666666666667,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 182,
            "name" : "C1181-755"
          }
        } ]
      }, {
        "id" : 17,
        "coordinate" : {
          "row" : 1,
          "col" : 4
        },
        "labels" : [ {
          "id" : 17,
          "name" : "loc",
          "value" : "well1,4"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 17,
          "amount" : {
            "number" : 66.66666666666667,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 178,
            "name" : "C1085-5"
          }
        } ]
      }, {
        "id" : 69,
        "coordinate" : {
          "row" : 5,
          "col" : 8
        },
        "labels" : [ {
          "id" : 69,
          "name" : "loc",
          "value" : "well5,8"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 69,
          "amount" : {
            "number" : 133.33333333333334,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 182,
            "name" : "C1181-755"
          }
        } ]
      }, {
        "id" : 18,
        "coordinate" : {
          "row" : 1,
          "col" : 5
        },
        "labels" : [ {
          "id" : 18,
          "name" : "loc",
          "value" : "well1,5"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 18,
          "amount" : {
            "number" : 83.33333333333334,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 178,
            "name" : "C1085-5"
          }
        } ]
      }, {
        "id" : 70,
        "coordinate" : {
          "row" : 5,
          "col" : 9
        },
        "labels" : [ {
          "id" : 70,
          "name" : "loc",
          "value" : "well5,9"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 70,
          "amount" : {
            "number" : 150.0,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 182,
            "name" : "C1181-755"
          }
        } ]
      }, {
        "id" : 19,
        "coordinate" : {
          "row" : 1,
          "col" : 6
        },
        "labels" : [ {
          "id" : 19,
          "name" : "loc",
          "value" : "well1,6"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 19,
          "amount" : {
            "number" : 100.0,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 178,
            "name" : "C1085-5"
          }
        } ]
      }, {
        "id" : 71,
        "coordinate" : {
          "row" : 5,
          "col" : 10
        },
        "labels" : [ {
          "id" : 71,
          "name" : "loc",
          "value" : "well5,10"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 71,
          "amount" : {
            "number" : 166.66666666666669,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 182,
            "name" : "C1181-755"
          }
        } ]
      }, {
        "id" : 20,
        "coordinate" : {
          "row" : 1,
          "col" : 7
        },
        "labels" : [ {
          "id" : 20,
          "name" : "loc",
          "value" : "well1,7"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 20,
          "amount" : {
            "number" : 116.66666666666667,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 178,
            "name" : "C1085-5"
          }
        } ]
      }, {
        "id" : 72,
        "coordinate" : {
          "row" : 5,
          "col" : 11
        },
        "labels" : [ {
          "id" : 72,
          "name" : "loc",
          "value" : "well5,11"
        } ],
        "type" : "POSITIVE",
        "contents" : [ {
          "id" : 72,
          "amount" : {
            "number" : 183.33333333333334,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 182,
            "name" : "C1181-755"
          }
        } ]
      }, {
        "id" : 21,
        "coordinate" : {
          "row" : 1,
          "col" : 8
        },
        "labels" : [ {
          "id" : 21,
          "name" : "loc",
          "value" : "well1,8"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 21,
          "amount" : {
            "number" : 133.33333333333334,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 178,
            "name" : "C1085-5"
          }
        } ]
      }, {
        "id" : 22,
        "coordinate" : {
          "row" : 1,
          "col" : 9
        },
        "labels" : [ {
          "id" : 22,
          "name" : "loc",
          "value" : "well1,9"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 22,
          "amount" : {
            "number" : 150.0,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 178,
            "name" : "C1085-5"
          }
        } ]
      }, {
        "id" : 23,
        "coordinate" : {
          "row" : 1,
          "col" : 10
        },
        "labels" : [ {
          "id" : 23,
          "name" : "loc",
          "value" : "well1,10"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 23,
          "amount" : {
            "number" : 166.66666666666669,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 178,
            "name" : "C1085-5"
          }
        } ]
      }, {
        "id" : 24,
        "coordinate" : {
          "row" : 1,
          "col" : 11
        },
        "labels" : [ {
          "id" : 24,
          "name" : "loc",
          "value" : "well1,11"
        } ],
        "type" : "NEGATIVE",
        "contents" : [ {
          "id" : 24,
          "amount" : {
            "number" : 183.33333333333334,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 178,
            "name" : "C1085-5"
          }
        } ]
      }, {
        "id" : 73,
        "coordinate" : {
          "row" : 6,
          "col" : 0
        },
        "labels" : [ {
          "id" : 73,
          "name" : "loc",
          "value" : "well6,0"
        } ],
        "type" : "POSITIVE",
        "contents" : [ {
          "id" : 73,
          "amount" : {
            "number" : 0.0,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 183,
            "name" : "C1042-30"
          }
        } ]
      }, {
        "id" : 74,
        "coordinate" : {
          "row" : 6,
          "col" : 1
        },
        "labels" : [ {
          "id" : 74,
          "name" : "loc",
          "value" : "well6,1"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 74,
          "amount" : {
            "number" : 16.666666666666668,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 183,
            "name" : "C1042-30"
          }
        } ]
      }, {
        "id" : 75,
        "coordinate" : {
          "row" : 6,
          "col" : 2
        },
        "labels" : [ {
          "id" : 75,
          "name" : "loc",
          "value" : "well6,2"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 75,
          "amount" : {
            "number" : 33.333333333333336,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 183,
            "name" : "C1042-30"
          }
        } ]
      }, {
        "id" : 76,
        "coordinate" : {
          "row" : 6,
          "col" : 3
        },
        "labels" : [ {
          "id" : 76,
          "name" : "loc",
          "value" : "well6,3"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 76,
          "amount" : {
            "number" : 50.0,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 183,
            "name" : "C1042-30"
          }
        } ]
      }, {
        "id" : 25,
        "coordinate" : {
          "row" : 2,
          "col" : 0
        },
        "labels" : [ {
          "id" : 25,
          "name" : "loc",
          "value" : "well2,0"
        } ],
        "type" : "NEGATIVE",
        "contents" : [ {
          "id" : 25,
          "amount" : {
            "number" : 0.0,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 179,
            "name" : "C657-1357"
          }
        } ]
      }, {
        "id" : 77,
        "coordinate" : {
          "row" : 6,
          "col" : 4
        },
        "labels" : [ {
          "id" : 77,
          "name" : "loc",
          "value" : "well6,4"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 77,
          "amount" : {
            "number" : 66.66666666666667,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 183,
            "name" : "C1042-30"
          }
        } ]
      }, {
        "id" : 26,
        "coordinate" : {
          "row" : 2,
          "col" : 1
        },
        "labels" : [ {
          "id" : 26,
          "name" : "loc",
          "value" : "well2,1"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 26,
          "amount" : {
            "number" : 16.666666666666668,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 179,
            "name" : "C657-1357"
          }
        } ]
      }, {
        "id" : 78,
        "coordinate" : {
          "row" : 6,
          "col" : 5
        },
        "labels" : [ {
          "id" : 78,
          "name" : "loc",
          "value" : "well6,5"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 78,
          "amount" : {
            "number" : 83.33333333333334,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 183,
            "name" : "C1042-30"
          }
        } ]
      }, {
        "id" : 27,
        "coordinate" : {
          "row" : 2,
          "col" : 2
        },
        "labels" : [ {
          "id" : 27,
          "name" : "loc",
          "value" : "well2,2"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 27,
          "amount" : {
            "number" : 33.333333333333336,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 179,
            "name" : "C657-1357"
          }
        } ]
      }, {
        "id" : 79,
        "coordinate" : {
          "row" : 6,
          "col" : 6
        },
        "labels" : [ {
          "id" : 79,
          "name" : "loc",
          "value" : "well6,6"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 79,
          "amount" : {
            "number" : 100.0,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 183,
            "name" : "C1042-30"
          }
        } ]
      }, {
        "id" : 28,
        "coordinate" : {
          "row" : 2,
          "col" : 3
        },
        "labels" : [ {
          "id" : 28,
          "name" : "loc",
          "value" : "well2,3"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 28,
          "amount" : {
            "number" : 50.0,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 179,
            "name" : "C657-1357"
          }
        } ]
      }, {
        "id" : 80,
        "coordinate" : {
          "row" : 6,
          "col" : 7
        },
        "labels" : [ {
          "id" : 80,
          "name" : "loc",
          "value" : "well6,7"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 80,
          "amount" : {
            "number" : 116.66666666666667,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 183,
            "name" : "C1042-30"
          }
        } ]
      }, {
        "id" : 29,
        "coordinate" : {
          "row" : 2,
          "col" : 4
        },
        "labels" : [ {
          "id" : 29,
          "name" : "loc",
          "value" : "well2,4"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 29,
          "amount" : {
            "number" : 66.66666666666667,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 179,
            "name" : "C657-1357"
          }
        } ]
      }, {
        "id" : 81,
        "coordinate" : {
          "row" : 6,
          "col" : 8
        },
        "labels" : [ {
          "id" : 81,
          "name" : "loc",
          "value" : "well6,8"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 81,
          "amount" : {
            "number" : 133.33333333333334,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 183,
            "name" : "C1042-30"
          }
        } ]
      }, {
        "id" : 30,
        "coordinate" : {
          "row" : 2,
          "col" : 5
        },
        "labels" : [ {
          "id" : 30,
          "name" : "loc",
          "value" : "well2,5"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 30,
          "amount" : {
            "number" : 83.33333333333334,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 179,
            "name" : "C657-1357"
          }
        } ]
      }, {
        "id" : 82,
        "coordinate" : {
          "row" : 6,
          "col" : 9
        },
        "labels" : [ {
          "id" : 82,
          "name" : "loc",
          "value" : "well6,9"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 82,
          "amount" : {
            "number" : 150.0,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 183,
            "name" : "C1042-30"
          }
        } ]
      }, {
        "id" : 31,
        "coordinate" : {
          "row" : 2,
          "col" : 6
        },
        "labels" : [ {
          "id" : 31,
          "name" : "loc",
          "value" : "well2,6"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 31,
          "amount" : {
            "number" : 100.0,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 179,
            "name" : "C657-1357"
          }
        } ]
      }, {
        "id" : 83,
        "coordinate" : {
          "row" : 6,
          "col" : 10
        },
        "labels" : [ {
          "id" : 83,
          "name" : "loc",
          "value" : "well6,10"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 83,
          "amount" : {
            "number" : 166.66666666666669,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 183,
            "name" : "C1042-30"
          }
        } ]
      }, {
        "id" : 32,
        "coordinate" : {
          "row" : 2,
          "col" : 7
        },
        "labels" : [ {
          "id" : 32,
          "name" : "loc",
          "value" : "well2,7"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 32,
          "amount" : {
            "number" : 116.66666666666667,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 179,
            "name" : "C657-1357"
          }
        } ]
      }, {
        "id" : 84,
        "coordinate" : {
          "row" : 6,
          "col" : 11
        },
        "labels" : [ {
          "id" : 84,
          "name" : "loc",
          "value" : "well6,11"
        } ],
        "type" : "POSITIVE",
        "contents" : [ {
          "id" : 84,
          "amount" : {
            "number" : 183.33333333333334,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 183,
            "name" : "C1042-30"
          }
        } ]
      }, {
        "id" : 33,
        "coordinate" : {
          "row" : 2,
          "col" : 8
        },
        "labels" : [ {
          "id" : 33,
          "name" : "loc",
          "value" : "well2,8"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 33,
          "amount" : {
            "number" : 133.33333333333334,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 179,
            "name" : "C657-1357"
          }
        } ]
      }, {
        "id" : 34,
        "coordinate" : {
          "row" : 2,
          "col" : 9
        },
        "labels" : [ {
          "id" : 34,
          "name" : "loc",
          "value" : "well2,9"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 34,
          "amount" : {
            "number" : 150.0,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 179,
            "name" : "C657-1357"
          }
        } ]
      }, {
        "id" : 35,
        "coordinate" : {
          "row" : 2,
          "col" : 10
        },
        "labels" : [ {
          "id" : 35,
          "name" : "loc",
          "value" : "well2,10"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 35,
          "amount" : {
            "number" : 166.66666666666669,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 179,
            "name" : "C657-1357"
          }
        } ]
      }, {
        "id" : 36,
        "coordinate" : {
          "row" : 2,
          "col" : 11
        },
        "labels" : [ {
          "id" : 36,
          "name" : "loc",
          "value" : "well2,11"
        } ],
        "type" : "NEGATIVE",
        "contents" : [ {
          "id" : 36,
          "amount" : {
            "number" : 183.33333333333334,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 179,
            "name" : "C657-1357"
          }
        } ]
      }, {
        "id" : 85,
        "coordinate" : {
          "row" : 7,
          "col" : 0
        },
        "labels" : [ {
          "id" : 85,
          "name" : "loc",
          "value" : "well7,0"
        } ],
        "type" : "POSITIVE",
        "contents" : [ {
          "id" : 85,
          "amount" : {
            "number" : 0.0,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 184,
            "name" : "C500-1759"
          }
        } ]
      }, {
        "id" : 86,
        "coordinate" : {
          "row" : 7,
          "col" : 1
        },
        "labels" : [ {
          "id" : 86,
          "name" : "loc",
          "value" : "well7,1"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 86,
          "amount" : {
            "number" : 16.666666666666668,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 184,
            "name" : "C500-1759"
          }
        } ]
      }, {
        "id" : 87,
        "coordinate" : {
          "row" : 7,
          "col" : 2
        },
        "labels" : [ {
          "id" : 87,
          "name" : "loc",
          "value" : "well7,2"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 87,
          "amount" : {
            "number" : 33.333333333333336,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 184,
            "name" : "C500-1759"
          }
        } ]
      }, {
        "id" : 88,
        "coordinate" : {
          "row" : 7,
          "col" : 3
        },
        "labels" : [ {
          "id" : 88,
          "name" : "loc",
          "value" : "well7,3"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 88,
          "amount" : {
            "number" : 50.0,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 184,
            "name" : "C500-1759"
          }
        } ]
      }, {
        "id" : 37,
        "coordinate" : {
          "row" : 3,
          "col" : 0
        },
        "labels" : [ {
          "id" : 37,
          "name" : "loc",
          "value" : "well3,0"
        } ],
        "type" : "NEGATIVE",
        "contents" : [ {
          "id" : 37,
          "amount" : {
            "number" : 0.0,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 180,
            "name" : "C1735-704"
          }
        } ]
      }, {
        "id" : 89,
        "coordinate" : {
          "row" : 7,
          "col" : 4
        },
        "labels" : [ {
          "id" : 89,
          "name" : "loc",
          "value" : "well7,4"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 89,
          "amount" : {
            "number" : 66.66666666666667,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 184,
            "name" : "C500-1759"
          }
        } ]
      }, {
        "id" : 38,
        "coordinate" : {
          "row" : 3,
          "col" : 1
        },
        "labels" : [ {
          "id" : 38,
          "name" : "loc",
          "value" : "well3,1"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 38,
          "amount" : {
            "number" : 16.666666666666668,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 180,
            "name" : "C1735-704"
          }
        } ]
      }, {
        "id" : 90,
        "coordinate" : {
          "row" : 7,
          "col" : 5
        },
        "labels" : [ {
          "id" : 90,
          "name" : "loc",
          "value" : "well7,5"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 90,
          "amount" : {
            "number" : 83.33333333333334,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 184,
            "name" : "C500-1759"
          }
        } ]
      }, {
        "id" : 39,
        "coordinate" : {
          "row" : 3,
          "col" : 2
        },
        "labels" : [ {
          "id" : 39,
          "name" : "loc",
          "value" : "well3,2"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 39,
          "amount" : {
            "number" : 33.333333333333336,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 180,
            "name" : "C1735-704"
          }
        } ]
      }, {
        "id" : 91,
        "coordinate" : {
          "row" : 7,
          "col" : 6
        },
        "labels" : [ {
          "id" : 91,
          "name" : "loc",
          "value" : "well7,6"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 91,
          "amount" : {
            "number" : 100.0,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 184,
            "name" : "C500-1759"
          }
        } ]
      }, {
        "id" : 40,
        "coordinate" : {
          "row" : 3,
          "col" : 3
        },
        "labels" : [ {
          "id" : 40,
          "name" : "loc",
          "value" : "well3,3"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 40,
          "amount" : {
            "number" : 50.0,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 180,
            "name" : "C1735-704"
          }
        } ]
      }, {
        "id" : 92,
        "coordinate" : {
          "row" : 7,
          "col" : 7
        },
        "labels" : [ {
          "id" : 92,
          "name" : "loc",
          "value" : "well7,7"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 92,
          "amount" : {
            "number" : 116.66666666666667,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 184,
            "name" : "C500-1759"
          }
        } ]
      }, {
        "id" : 41,
        "coordinate" : {
          "row" : 3,
          "col" : 4
        },
        "labels" : [ {
          "id" : 41,
          "name" : "loc",
          "value" : "well3,4"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 41,
          "amount" : {
            "number" : 66.66666666666667,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 180,
            "name" : "C1735-704"
          }
        } ]
      }, {
        "id" : 93,
        "coordinate" : {
          "row" : 7,
          "col" : 8
        },
        "labels" : [ {
          "id" : 93,
          "name" : "loc",
          "value" : "well7,8"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 93,
          "amount" : {
            "number" : 133.33333333333334,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 184,
            "name" : "C500-1759"
          }
        } ]
      }, {
        "id" : 42,
        "coordinate" : {
          "row" : 3,
          "col" : 5
        },
        "labels" : [ {
          "id" : 42,
          "name" : "loc",
          "value" : "well3,5"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 42,
          "amount" : {
            "number" : 83.33333333333334,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 180,
            "name" : "C1735-704"
          }
        } ]
      }, {
        "id" : 94,
        "coordinate" : {
          "row" : 7,
          "col" : 9
        },
        "labels" : [ {
          "id" : 94,
          "name" : "loc",
          "value" : "well7,9"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 94,
          "amount" : {
            "number" : 150.0,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 184,
            "name" : "C500-1759"
          }
        } ]
      }, {
        "id" : 43,
        "coordinate" : {
          "row" : 3,
          "col" : 6
        },
        "labels" : [ {
          "id" : 43,
          "name" : "loc",
          "value" : "well3,6"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 43,
          "amount" : {
            "number" : 100.0,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 180,
            "name" : "C1735-704"
          }
        } ]
      }, {
        "id" : 95,
        "coordinate" : {
          "row" : 7,
          "col" : 10
        },
        "labels" : [ {
          "id" : 95,
          "name" : "loc",
          "value" : "well7,10"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 95,
          "amount" : {
            "number" : 166.66666666666669,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 184,
            "name" : "C500-1759"
          }
        } ]
      }, {
        "id" : 44,
        "coordinate" : {
          "row" : 3,
          "col" : 7
        },
        "labels" : [ {
          "id" : 44,
          "name" : "loc",
          "value" : "well3,7"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 44,
          "amount" : {
            "number" : 116.66666666666667,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 180,
            "name" : "C1735-704"
          }
        } ]
      }, {
        "id" : 96,
        "coordinate" : {
          "row" : 7,
          "col" : 11
        },
        "labels" : [ {
          "id" : 96,
          "name" : "loc",
          "value" : "well7,11"
        } ],
        "type" : "POSITIVE",
        "contents" : [ {
          "id" : 96,
          "amount" : {
            "number" : 183.33333333333334,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 184,
            "name" : "C500-1759"
          }
        } ]
      }, {
        "id" : 45,
        "coordinate" : {
          "row" : 3,
          "col" : 8
        },
        "labels" : [ {
          "id" : 45,
          "name" : "loc",
          "value" : "well3,8"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 45,
          "amount" : {
            "number" : 133.33333333333334,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 180,
            "name" : "C1735-704"
          }
        } ]
      }, {
        "id" : 46,
        "coordinate" : {
          "row" : 3,
          "col" : 9
        },
        "labels" : [ {
          "id" : 46,
          "name" : "loc",
          "value" : "well3,9"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 46,
          "amount" : {
            "number" : 150.0,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 180,
            "name" : "C1735-704"
          }
        } ]
      }, {
        "id" : 47,
        "coordinate" : {
          "row" : 3,
          "col" : 10
        },
        "labels" : [ {
          "id" : 47,
          "name" : "loc",
          "value" : "well3,10"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 47,
          "amount" : {
            "number" : 166.66666666666669,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 180,
            "name" : "C1735-704"
          }
        } ]
      }, {
        "id" : 48,
        "coordinate" : {
          "row" : 3,
          "col" : 11
        },
        "labels" : [ {
          "id" : 48,
          "name" : "loc",
          "value" : "well3,11"
        } ],
        "type" : "NEGATIVE",
        "contents" : [ {
          "id" : 48,
          "amount" : {
            "number" : 183.33333333333334,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 180,
            "name" : "C1735-704"
          }
        } ]
      }, {
        "id" : 49,
        "coordinate" : {
          "row" : 4,
          "col" : 0
        },
        "labels" : [ {
          "id" : 49,
          "name" : "loc",
          "value" : "well4,0"
        } ],
        "type" : "POSITIVE",
        "contents" : [ {
          "id" : 49,
          "amount" : {
            "number" : 0.0,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 181,
            "name" : "C1942-1599"
          }
        } ]
      }, {
        "id" : 50,
        "coordinate" : {
          "row" : 4,
          "col" : 1
        },
        "labels" : [ {
          "id" : 50,
          "name" : "loc",
          "value" : "well4,1"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 50,
          "amount" : {
            "number" : 16.666666666666668,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 181,
            "name" : "C1942-1599"
          }
        } ]
      }, {
        "id" : 51,
        "coordinate" : {
          "row" : 4,
          "col" : 2
        },
        "labels" : [ {
          "id" : 51,
          "name" : "loc",
          "value" : "well4,2"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 51,
          "amount" : {
            "number" : 33.333333333333336,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 181,
            "name" : "C1942-1599"
          }
        } ]
      }, {
        "id" : 52,
        "coordinate" : {
          "row" : 4,
          "col" : 3
        },
        "labels" : [ {
          "id" : 52,
          "name" : "loc",
          "value" : "well4,3"
        } ],
        "type" : "COMP",
        "contents" : [ {
          "id" : 52,
          "amount" : {
            "number" : 50.0,
            "units" : "MICROMOLAR"
          },
          "compound" : {
            "id" : 181,
            "name" : "C1942-1599"
          }
        } ]
      } ],
      "plateType" : {
        "id" : 1,
        "name" : "Corning-3788",
        "description" : "Clear high-grade polystyrene for research diagnostic assays Formulated for uniform binding\\",
        "dim" : {
          "rows" : 8,
          "cols" : 12
        },
        "manufacturer" : "Corning",
        "orderLink" : "http://www.coleparmer.com/Product/Corning_Plate_96_Well_ps_round_100_cs_Clear/UX-01728-07"
      },
      "hasResults" : true
    },
    "created" : "2015-05-03T15:44:10.750-04:00",
    "wellResults" : [ {
      "id" : 1,
      "coordinate" : {
        "row" : 0,
        "col" : 0
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 1,
        "value" : 0.06600932196591061,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 53,
      "coordinate" : {
        "row" : 4,
        "col" : 4
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 53,
        "value" : -0.3055657545084374,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 2,
      "coordinate" : {
        "row" : 0,
        "col" : 1
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 2,
        "value" : -0.7287593233670768,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 54,
      "coordinate" : {
        "row" : 4,
        "col" : 5
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 54,
        "value" : -0.10966593196571237,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 3,
      "coordinate" : {
        "row" : 0,
        "col" : 2
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 3,
        "value" : -0.45869950363342105,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 55,
      "coordinate" : {
        "row" : 4,
        "col" : 6
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 55,
        "value" : 0.11849933739121071,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 4,
      "coordinate" : {
        "row" : 0,
        "col" : 3
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 4,
        "value" : -0.11230497611520551,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 56,
      "coordinate" : {
        "row" : 4,
        "col" : 7
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 56,
        "value" : 0.5550285508915821,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 5,
      "coordinate" : {
        "row" : 0,
        "col" : 4
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 5,
        "value" : -0.08506327584793937,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 57,
      "coordinate" : {
        "row" : 4,
        "col" : 8
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 57,
        "value" : 0.6847930942445044,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 6,
      "coordinate" : {
        "row" : 0,
        "col" : 5
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 6,
        "value" : -0.012816487027580453,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 58,
      "coordinate" : {
        "row" : 4,
        "col" : 9
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 58,
        "value" : 0.7229178572059691,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 7,
      "coordinate" : {
        "row" : 0,
        "col" : 6
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 7,
        "value" : 0.2191384454215291,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 59,
      "coordinate" : {
        "row" : 4,
        "col" : 10
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 59,
        "value" : 0.7114233686038802,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 8,
      "coordinate" : {
        "row" : 0,
        "col" : 7
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 8,
        "value" : 0.5368499225666996,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 60,
      "coordinate" : {
        "row" : 4,
        "col" : 11
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 60,
        "value" : 0.9643425952781495,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 9,
      "coordinate" : {
        "row" : 0,
        "col" : 8
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 9,
        "value" : 0.17240699195780007,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 10,
      "coordinate" : {
        "row" : 0,
        "col" : 9
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 10,
        "value" : 0.5775402096016302,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 11,
      "coordinate" : {
        "row" : 0,
        "col" : 10
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 11,
        "value" : 0.5730707062549344,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 12,
      "coordinate" : {
        "row" : 0,
        "col" : 11
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 12,
        "value" : 0.04221299933809849,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 61,
      "coordinate" : {
        "row" : 5,
        "col" : 0
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 61,
        "value" : 0.9425747181748692,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 62,
      "coordinate" : {
        "row" : 5,
        "col" : 1
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 62,
        "value" : -0.1634403889317971,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 63,
      "coordinate" : {
        "row" : 5,
        "col" : 2
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 63,
        "value" : -0.03738512738254572,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 64,
      "coordinate" : {
        "row" : 5,
        "col" : 3
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 64,
        "value" : -0.05063924779760226,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 13,
      "coordinate" : {
        "row" : 1,
        "col" : 0
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 13,
        "value" : 0.013633021633833476,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 65,
      "coordinate" : {
        "row" : 5,
        "col" : 4
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 65,
        "value" : -0.03378840243358108,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 14,
      "coordinate" : {
        "row" : 1,
        "col" : 1
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 14,
        "value" : -0.6030385811052688,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 66,
      "coordinate" : {
        "row" : 5,
        "col" : 5
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 66,
        "value" : 0.12811144303039618,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 15,
      "coordinate" : {
        "row" : 1,
        "col" : 2
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 15,
        "value" : -0.49210671128601,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 67,
      "coordinate" : {
        "row" : 5,
        "col" : 6
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 67,
        "value" : 0.4032036478702812,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 16,
      "coordinate" : {
        "row" : 1,
        "col" : 3
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 16,
        "value" : -0.24160939437983736,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 68,
      "coordinate" : {
        "row" : 5,
        "col" : 7
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 68,
        "value" : 0.7148169538447837,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 17,
      "coordinate" : {
        "row" : 1,
        "col" : 4
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 17,
        "value" : 0.08526189748632582,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 69,
      "coordinate" : {
        "row" : 5,
        "col" : 8
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 69,
        "value" : 0.8953077936427691,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 18,
      "coordinate" : {
        "row" : 1,
        "col" : 5
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 18,
        "value" : -0.24293486986907945,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 70,
      "coordinate" : {
        "row" : 5,
        "col" : 9
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 70,
        "value" : 0.7970535402960789,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 19,
      "coordinate" : {
        "row" : 1,
        "col" : 6
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 19,
        "value" : 0.06396102331634829,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 71,
      "coordinate" : {
        "row" : 5,
        "col" : 10
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 71,
        "value" : 0.8216307373728824,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 20,
      "coordinate" : {
        "row" : 1,
        "col" : 7
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 20,
        "value" : 0.6078013816847768,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 72,
      "coordinate" : {
        "row" : 5,
        "col" : 11
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 72,
        "value" : 0.9726418875837125,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 21,
      "coordinate" : {
        "row" : 1,
        "col" : 8
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 21,
        "value" : 0.707054953482009,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 22,
      "coordinate" : {
        "row" : 1,
        "col" : 9
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 22,
        "value" : 0.8941895859802437,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 23,
      "coordinate" : {
        "row" : 1,
        "col" : 10
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 23,
        "value" : 0.5260888767965806,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 24,
      "coordinate" : {
        "row" : 1,
        "col" : 11
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 24,
        "value" : 0.04177939410405642,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 73,
      "coordinate" : {
        "row" : 6,
        "col" : 0
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 73,
        "value" : 0.9903411496796931,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 74,
      "coordinate" : {
        "row" : 6,
        "col" : 1
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 74,
        "value" : -0.24049824422337274,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 75,
      "coordinate" : {
        "row" : 6,
        "col" : 2
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 75,
        "value" : -0.006215836777309627,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 76,
      "coordinate" : {
        "row" : 6,
        "col" : 3
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 76,
        "value" : -0.25626091380737576,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 25,
      "coordinate" : {
        "row" : 2,
        "col" : 0
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 25,
        "value" : 0.021218146332441436,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 77,
      "coordinate" : {
        "row" : 6,
        "col" : 4
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 77,
        "value" : -0.22457791895110246,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 26,
      "coordinate" : {
        "row" : 2,
        "col" : 1
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 26,
        "value" : -0.24268614516643391,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 78,
      "coordinate" : {
        "row" : 6,
        "col" : 5
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 78,
        "value" : 0.029621212181824258,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 27,
      "coordinate" : {
        "row" : 2,
        "col" : 2
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 27,
        "value" : -0.26459745369338544,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 79,
      "coordinate" : {
        "row" : 6,
        "col" : 6
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 79,
        "value" : 0.4673841623078038,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 28,
      "coordinate" : {
        "row" : 2,
        "col" : 3
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 28,
        "value" : 0.0021386051135920195,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 80,
      "coordinate" : {
        "row" : 6,
        "col" : 7
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 80,
        "value" : 0.4388732188653227,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 29,
      "coordinate" : {
        "row" : 2,
        "col" : 4
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 29,
        "value" : 0.06225448993705249,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 81,
      "coordinate" : {
        "row" : 6,
        "col" : 8
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 81,
        "value" : 0.8098261577716346,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 30,
      "coordinate" : {
        "row" : 2,
        "col" : 5
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 30,
        "value" : -0.009446068808383473,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 82,
      "coordinate" : {
        "row" : 6,
        "col" : 9
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 82,
        "value" : 0.6289834653241078,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 31,
      "coordinate" : {
        "row" : 2,
        "col" : 6
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 31,
        "value" : 0.4912560131802184,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 83,
      "coordinate" : {
        "row" : 6,
        "col" : 10
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 83,
        "value" : 0.9102170333142028,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 32,
      "coordinate" : {
        "row" : 2,
        "col" : 7
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 32,
        "value" : 0.649393278614316,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 84,
      "coordinate" : {
        "row" : 6,
        "col" : 11
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 84,
        "value" : 0.9933495626028936,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 33,
      "coordinate" : {
        "row" : 2,
        "col" : 8
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 33,
        "value" : 0.8203336749279911,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 34,
      "coordinate" : {
        "row" : 2,
        "col" : 9
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 34,
        "value" : 0.8890367618208647,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 35,
      "coordinate" : {
        "row" : 2,
        "col" : 10
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 35,
        "value" : 0.9333644800162738,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 36,
      "coordinate" : {
        "row" : 2,
        "col" : 11
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 36,
        "value" : 0.03444563381941891,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 85,
      "coordinate" : {
        "row" : 7,
        "col" : 0
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 85,
        "value" : 0.9727845307494536,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 86,
      "coordinate" : {
        "row" : 7,
        "col" : 1
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 86,
        "value" : -0.4218624740534971,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 87,
      "coordinate" : {
        "row" : 7,
        "col" : 2
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 87,
        "value" : -0.8358754444655666,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 88,
      "coordinate" : {
        "row" : 7,
        "col" : 3
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 88,
        "value" : -0.26331765460249373,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 37,
      "coordinate" : {
        "row" : 3,
        "col" : 0
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 37,
        "value" : 0.09523723441036612,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 89,
      "coordinate" : {
        "row" : 7,
        "col" : 4
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 89,
        "value" : -0.0920168921868877,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 38,
      "coordinate" : {
        "row" : 3,
        "col" : 1
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 38,
        "value" : 0.0015699084448398837,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 90,
      "coordinate" : {
        "row" : 7,
        "col" : 5
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 90,
        "value" : -0.3569269627224264,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 39,
      "coordinate" : {
        "row" : 3,
        "col" : 2
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 39,
        "value" : 0.006475636419864846,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 91,
      "coordinate" : {
        "row" : 7,
        "col" : 6
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 91,
        "value" : 0.3803412732064492,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 40,
      "coordinate" : {
        "row" : 3,
        "col" : 3
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 40,
        "value" : 0.02880868436679369,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 92,
      "coordinate" : {
        "row" : 7,
        "col" : 7
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 92,
        "value" : -0.11905568559967805,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 41,
      "coordinate" : {
        "row" : 3,
        "col" : 4
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 41,
        "value" : 0.08618954588489339,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 93,
      "coordinate" : {
        "row" : 7,
        "col" : 8
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 93,
        "value" : 0.70114674224339,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 42,
      "coordinate" : {
        "row" : 3,
        "col" : 5
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 42,
        "value" : 0.23415123805822619,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 94,
      "coordinate" : {
        "row" : 7,
        "col" : 9
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 94,
        "value" : 0.7134990515933013,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 43,
      "coordinate" : {
        "row" : 3,
        "col" : 6
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 43,
        "value" : 0.4990107872330835,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 95,
      "coordinate" : {
        "row" : 7,
        "col" : 10
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 95,
        "value" : 0.2502865445701338,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 44,
      "coordinate" : {
        "row" : 3,
        "col" : 7
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 44,
        "value" : 0.7599844142560829,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 96,
      "coordinate" : {
        "row" : 7,
        "col" : 11
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 96,
        "value" : 0.9186795636553304,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 45,
      "coordinate" : {
        "row" : 3,
        "col" : 8
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 45,
        "value" : 0.9105460050152677,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 46,
      "coordinate" : {
        "row" : 3,
        "col" : 9
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 46,
        "value" : 0.9670241790346686,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 47,
      "coordinate" : {
        "row" : 3,
        "col" : 10
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 47,
        "value" : 0.9880529992229462,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 48,
      "coordinate" : {
        "row" : 3,
        "col" : 11
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 48,
        "value" : 0.002369691731484702,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 49,
      "coordinate" : {
        "row" : 4,
        "col" : 0
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 49,
        "value" : 0.9551729804184753,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 50,
      "coordinate" : {
        "row" : 4,
        "col" : 1
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 50,
        "value" : -0.31833436360629674,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 51,
      "coordinate" : {
        "row" : 4,
        "col" : 2
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 51,
        "value" : -0.27565897493912034,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    }, {
      "id" : 52,
      "coordinate" : {
        "row" : 4,
        "col" : 3
      },
      "resultStatus" : "INCLUDED",
      "samples" : [ {
        "id" : 52,
        "value" : -0.2447693683221061,
        "label" : "",
        "measuredAt" : "2015-04-28T06:07:10.754-04:00",
        "status" : "INCLUDED"
      } ]
    } ],
    "metrics" : [ {
      "label" : "",
      "avgPositive" : 0.9637358735178222,
      "avgNegative" : 0.03961318041695127,
      "zee" : -0.9572097314894645,
      "zeePrime" : 0.8341708558218117
    } ]
  };

  samplePlates = {
    "totalCount" : 3,
    "page" : 0,
    "pageSize" : 100,
    "values" : [ {
      "id" : 1,
      "name" : "plate 0 for exp 1",
      "barcode" : "abc0",
      "labels" : [ ],
      "wellCount" : 96,
      "experimentId" : 1,
      "wells" : [ ],
      "plateType" : {
        "id" : 1,
        "name" : "Corning-3788",
        "description" : "Clear high-grade polystyrene for research diagnostic assays Formulated for uniform binding\\",
        "dim" : {
          "rows" : 8,
          "cols" : 12
        },
        "manufacturer" : "Corning",
        "orderLink" : "http://www.coleparmer.com/Product/Corning_Plate_96_Well_ps_round_100_cs_Clear/UX-01728-07"
      },
      "hasResults" : true
    }, {
      "id" : 2,
      "name" : "plate 1 for exp 1",
      "barcode" : "abc1",
      "labels" : [ ],
      "wellCount" : 96,
      "experimentId" : 1,
      "wells" : [ ],
      "plateType" : {
        "id" : 1,
        "name" : "Corning-3788",
        "description" : "Clear high-grade polystyrene for research diagnostic assays Formulated for uniform binding\\",
        "dim" : {
          "rows" : 8,
          "cols" : 12
        },
        "manufacturer" : "Corning",
        "orderLink" : "http://www.coleparmer.com/Product/Corning_Plate_96_Well_ps_round_100_cs_Clear/UX-01728-07"
      },
      "hasResults" : true
    }, {
      "id" : 3,
      "name" : "plate 2 for exp 1",
      "barcode" : "abc2",
      "labels" : [ ],
      "wellCount" : 96,
      "experimentId" : 1,
      "wells" : [ ],
      "plateType" : {
        "id" : 1,
        "name" : "Corning-3788",
        "description" : "Clear high-grade polystyrene for research diagnostic assays Formulated for uniform binding\\",
        "dim" : {
          "rows" : 8,
          "cols" : 12
        },
        "manufacturer" : "Corning",
        "orderLink" : "http://www.coleparmer.com/Product/Corning_Plate_96_Well_ps_round_100_cs_Clear/UX-01728-07"
      },
      "hasResults" : true
    } ]
  };



  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope,$httpBackend) {
    scope = $rootScope.$new();

    // Setup unit test Rest Calls
    httpBackend = $httpBackend;
    httpBackend.whenGET("services/rest/experiment").respond(expResp);
    httpBackend.whenGET('services/rest/experiment/1/plates').respond(samplePlates);

    httpBackend.whenGET("services/rest/experiment/1/plates/1/results").respond(results);
    httpBackend.whenGET("services/rest/experiment/1/plates/2/results").respond(results);
    httpBackend.whenGET("services/rest/experiment/1/plates/3/results").respond(results);
    //httpBackend.whenDELETE("services/rest/experiment/2").respond("");

    //wipe logs
    console.log = function() {};
    HeatmapCtrl = $controller('HeatmapCtrl', {
      $scope: scope
    });
  }));

  it('should retrieve all experiments and the results data on init', function () {
    httpBackend.flush();
    expect(scope.experiments.length).toBe(1);
    expect(scope.selectedIndex).toBe(1);
    expect(scope.data).toBeDefined();

  });

  it('should start tour', function () {
    httpBackend.flush();
    scope.startTour();

    expect(scope.startJoyRide).toBe(true);
  });

  it('should re-render heatmap when different plate is clicked', function () {
    httpBackend.flush();


    expect(scope.selectedIndex).toBe(1);

    //plate with id 3 is third in overall list:
    scope.selectPlate(1);
    expect(scope.selectedIndex).toBe(3);

  });

  it('should mark well as outlier when clicked', function () {
    httpBackend.flush();

    httpBackend.whenPOST("services/rest/experiment/1/plates/1/results/update").respond(results);
    scope.doHeatMapOnClick({wellIndex: 76});
    httpBackend.flush();


  });

  it('should retrieve default page when prompted', function () {
    httpBackend.flush();

    var paging=scope.doGetDefaultPaginationInfo({length:5});
    //expect that page index starts at 0
    expect(scope.pagination.paginationIndex).toBe(0);
    //set our offset really high to allow the next page
    scope.pagination.of=9999999;
    paging.doPaging('NEXT');
    // expect page to ++
    expect(scope.pagination.paginationIndex).toBe(1);
    paging.doPaging('PREV');
    // expect page to --
    expect(scope.pagination.paginationIndex).toBe(0);

  });




});
