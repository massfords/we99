'use strict';

describe('Controller: DoseResponseCntrl', function () {

  // load the controller's module
  beforeEach(module('we99App'));

  var DoseResponseCntrl,
    scope,
    httpBackend,expResp,location;

  expResp ={
    "totalCount": 3,
    "page": 0,
    "pageSize": 100,
    "values": [
      {
        "id": 1,
        "name": "experiment uno",
        "description": "Experiment using the Alpha protocol",
        "labels": [],
        "status": "UNPUBLISHED",
        "created": "2015-04-06T23:08:44.134-04:00",
        "protocol": {
          "id": 1,
          "name": "Alpha"
        }
      }
      ,{
        "id": 2,
        "name": "experiment dos",
        "description": "Experiment using the Beta protocol",
        "labels": [],
        "status": "UNPUBLISHED",
        "created": "2015-04-06T23:08:44.380-04:00",
        "protocol": {
          "id": 2,
          "name": "Beta"
        }
      }
      ,
      {
        "id": 3,
        "name": "experiment tres",
        "description": "Experiment using the Gamma protocol",
        "labels": [],
        "status": "UNPUBLISHED",
        "created": "2015-04-06T23:08:44.551-04:00",
        "protocol": {
          "id": 3,
          "name": "Gamma"
        }
      }
    ]
  };

  var doseResponse={
    "totalCount" : 24,
    "page" : 0,
    "pageSize" : 100,
    "values" : [ {
      "id" : 49,
      "compound" : {
        "id" : 192,
        "name" : "C1255-528"
      },
      "experiment" : {
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
      },
      "experimentPoints" : [ {
        "x" : 16.666666666666668,
        "logx" : 1.2218487496163564,
        "y" : -20.58799983498501,
        "plateId" : 2,
        "doseId" : 182,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 33.333333333333336,
        "logx" : 1.5228787452803376,
        "y" : -54.85188350423936,
        "plateId" : 2,
        "doseId" : 183,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 50.0,
        "logx" : 1.6989700043360187,
        "y" : -39.584244567691755,
        "plateId" : 2,
        "doseId" : 184,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 66.66666666666667,
        "logx" : 1.8239087409443189,
        "y" : -55.11483661454996,
        "plateId" : 2,
        "doseId" : 185,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 83.33333333333334,
        "logx" : 1.9208187539523751,
        "y" : -38.17035606015611,
        "plateId" : 2,
        "doseId" : 186,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 100.0,
        "logx" : 2.0,
        "y" : 38.7595283285483,
        "plateId" : 2,
        "doseId" : 187,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 116.66666666666667,
        "logx" : 2.066946789630613,
        "y" : 10.405182788823529,
        "plateId" : 2,
        "doseId" : 188,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 133.33333333333334,
        "logx" : 2.1249387366083,
        "y" : 61.308457004565895,
        "plateId" : 2,
        "doseId" : 189,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 150.0,
        "logx" : 2.1760912590556813,
        "y" : 49.19315523223376,
        "plateId" : 2,
        "doseId" : 190,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 166.66666666666669,
        "logx" : 2.2218487496163566,
        "y" : 26.70891202008219,
        "plateId" : 2,
        "doseId" : 191,
        "resultStatus" : "INCLUDED"
      } ],
      "curveFitPoints" : [ {
        "sequenceNumber" : 0,
        "x" : 1.2218487496163564,
        "y" : -49.85028758354957
      }, {
        "sequenceNumber" : 1,
        "x" : 1.247489775257382,
        "y" : -49.85028758354957
      }, {
        "sequenceNumber" : 2,
        "x" : 1.2731308008984075,
        "y" : -49.85028758354957
      }, {
        "sequenceNumber" : 3,
        "x" : 1.298771826539433,
        "y" : -49.85028758354957
      }, {
        "sequenceNumber" : 4,
        "x" : 1.3244128521804586,
        "y" : -49.85028758354957
      }, {
        "sequenceNumber" : 5,
        "x" : 1.3500538778214841,
        "y" : -49.85028758354957
      }, {
        "sequenceNumber" : 6,
        "x" : 1.3756949034625097,
        "y" : -49.85028758354957
      }, {
        "sequenceNumber" : 7,
        "x" : 1.4013359291035352,
        "y" : -49.85028758354957
      }, {
        "sequenceNumber" : 8,
        "x" : 1.4269769547445608,
        "y" : -49.85028758354957
      }, {
        "sequenceNumber" : 9,
        "x" : 1.4526179803855863,
        "y" : -49.85028758354957
      }, {
        "sequenceNumber" : 10,
        "x" : 1.4782590060266119,
        "y" : -49.85028758354957
      }, {
        "sequenceNumber" : 11,
        "x" : 1.5039000316676374,
        "y" : -49.85028758354957
      }, {
        "sequenceNumber" : 12,
        "x" : 1.529541057308663,
        "y" : -49.85028758354957
      }, {
        "sequenceNumber" : 13,
        "x" : 1.5551820829496885,
        "y" : -49.85028758354957
      }, {
        "sequenceNumber" : 14,
        "x" : 1.580823108590714,
        "y" : -49.85028758354957
      }, {
        "sequenceNumber" : 15,
        "x" : 1.6064641342317396,
        "y" : -49.85028758354957
      }, {
        "sequenceNumber" : 16,
        "x" : 1.6321051598727652,
        "y" : -49.85028758354957
      }, {
        "sequenceNumber" : 17,
        "x" : 1.6577461855137907,
        "y" : -49.85028758354957
      }, {
        "sequenceNumber" : 18,
        "x" : 1.6833872111548163,
        "y" : -49.85028758354957
      }, {
        "sequenceNumber" : 19,
        "x" : 1.7090282367958418,
        "y" : -49.85028758354957
      }, {
        "sequenceNumber" : 20,
        "x" : 1.7346692624368674,
        "y" : -49.85028758354956
      }, {
        "sequenceNumber" : 21,
        "x" : 1.760310288077893,
        "y" : -49.850287583547995
      }, {
        "sequenceNumber" : 22,
        "x" : 1.7859513137189185,
        "y" : -49.85028758336572
      }, {
        "sequenceNumber" : 23,
        "x" : 1.811592339359944,
        "y" : -49.85028756215846
      }, {
        "sequenceNumber" : 24,
        "x" : 1.8372333650009696,
        "y" : -49.85028509475722
      }, {
        "sequenceNumber" : 25,
        "x" : 1.8628743906419951,
        "y" : -49.849998020973175
      }, {
        "sequenceNumber" : 26,
        "x" : 1.8885154162830207,
        "y" : -49.8166107560151
      }, {
        "sequenceNumber" : 27,
        "x" : 1.9141564419240462,
        "y" : -46.09932773599496
      }, {
        "sequenceNumber" : 28,
        "x" : 1.9397974675650718,
        "y" : 23.30009529421227
      }, {
        "sequenceNumber" : 29,
        "x" : 1.9654384932060973,
        "y" : 37.1322682352166
      }, {
        "sequenceNumber" : 30,
        "x" : 1.9910795188471229,
        "y" : 37.273866255339954
      }, {
        "sequenceNumber" : 31,
        "x" : 2.0167205444881486,
        "y" : 37.27508528554698
      }, {
        "sequenceNumber" : 32,
        "x" : 2.042361570129174,
        "y" : 37.27509576323344
      }, {
        "sequenceNumber" : 33,
        "x" : 2.0680025957701997,
        "y" : 37.27509585328893
      }, {
        "sequenceNumber" : 34,
        "x" : 2.0936436214112253,
        "y" : 37.275095854062954
      }, {
        "sequenceNumber" : 35,
        "x" : 2.119284647052251,
        "y" : 37.275095854069605
      }, {
        "sequenceNumber" : 36,
        "x" : 2.1449256726932764,
        "y" : 37.27509585406966
      }, {
        "sequenceNumber" : 37,
        "x" : 2.170566698334302,
        "y" : 37.27509585406966
      }, {
        "sequenceNumber" : 38,
        "x" : 2.1962077239753275,
        "y" : 37.27509585406966
      }, {
        "sequenceNumber" : 39,
        "x" : 2.221848749616353,
        "y" : 37.27509585406966
      } ],
      "fitEquation" : "HILLEQUATION",
      "fitParameterMap" : {
        "Min" : {
          "value" : -49.85028758354957,
          "name" : "Min",
          "status" : "FLOAT"
        },
        "Max" : {
          "value" : 37.27509585406967,
          "name" : "Max",
          "status" : "FLOAT"
        },
        "EC50" : {
          "value" : 1.930874614556427,
          "name" : "EC50",
          "status" : "FLOAT"
        },
        "Slope" : {
          "value" : 80.56445275548734,
          "name" : "Slope",
          "status" : "FLOAT"
        }
      }
    }, {
      "id" : 50,
      "compound" : {
        "id" : 193,
        "name" : "C1010-1047"
      },
      "experiment" : {
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
      },
      "experimentPoints" : [ {
        "x" : 16.666666666666668,
        "logx" : 1.2218487496163564,
        "y" : -7.3277242552973405,
        "plateId" : 3,
        "doseId" : 194,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 33.333333333333336,
        "logx" : 1.5228787452803376,
        "y" : -7.204866018375848,
        "plateId" : 3,
        "doseId" : 195,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 50.0,
        "logx" : 1.6989700043360187,
        "y" : -5.1215895213304625,
        "plateId" : 3,
        "doseId" : 196,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 66.66666666666667,
        "logx" : 1.8239087409443189,
        "y" : 1.7115678872485824,
        "plateId" : 3,
        "doseId" : 197,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 83.33333333333334,
        "logx" : 1.9208187539523751,
        "y" : 16.304478638885065,
        "plateId" : 3,
        "doseId" : 198,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 100.0,
        "logx" : 2.0,
        "y" : 45.89621022745083,
        "plateId" : 3,
        "doseId" : 199,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 116.66666666666667,
        "logx" : 2.066946789630613,
        "y" : 78.51652096363199,
        "plateId" : 3,
        "doseId" : 200,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 133.33333333333334,
        "logx" : 2.1249387366083,
        "y" : 91.66652040708514,
        "plateId" : 3,
        "doseId" : 201,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 150.0,
        "logx" : 2.1760912590556813,
        "y" : 101.66847179675077,
        "plateId" : 3,
        "doseId" : 202,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 166.66666666666669,
        "logx" : 2.2218487496163566,
        "y" : 100.36828990355117,
        "plateId" : 3,
        "doseId" : 203,
        "resultStatus" : "INCLUDED"
      } ],
      "curveFitPoints" : [ {
        "sequenceNumber" : 0,
        "x" : 1.2218487496163564,
        "y" : -6.1005827122393566
      }, {
        "sequenceNumber" : 1,
        "x" : 1.247489775257382,
        "y" : -6.100460254799032
      }, {
        "sequenceNumber" : 2,
        "x" : 1.2731308008984075,
        "y" : -6.100272092327935
      }, {
        "sequenceNumber" : 3,
        "x" : 1.298771826539433,
        "y" : -6.099982970960744
      }, {
        "sequenceNumber" : 4,
        "x" : 1.3244128521804586,
        "y" : -6.099538722049943
      }, {
        "sequenceNumber" : 5,
        "x" : 1.3500538778214841,
        "y" : -6.0988561146049385
      }, {
        "sequenceNumber" : 6,
        "x" : 1.3756949034625097,
        "y" : -6.09780726477288
      }, {
        "sequenceNumber" : 7,
        "x" : 1.4013359291035352,
        "y" : -6.096195684345837
      }, {
        "sequenceNumber" : 8,
        "x" : 1.4269769547445608,
        "y" : -6.093719487925966
      }, {
        "sequenceNumber" : 9,
        "x" : 1.4526179803855863,
        "y" : -6.089914882267059
      }, {
        "sequenceNumber" : 10,
        "x" : 1.4782590060266119,
        "y" : -6.084069390240282
      }, {
        "sequenceNumber" : 11,
        "x" : 1.5039000316676374,
        "y" : -6.075088646293123
      }, {
        "sequenceNumber" : 12,
        "x" : 1.529541057308663,
        "y" : -6.061292031111052
      }, {
        "sequenceNumber" : 13,
        "x" : 1.5551820829496885,
        "y" : -6.040099386395
      }, {
        "sequenceNumber" : 14,
        "x" : 1.580823108590714,
        "y" : -6.007551364294183
      }, {
        "sequenceNumber" : 15,
        "x" : 1.6064641342317396,
        "y" : -5.957576492446272
      }, {
        "sequenceNumber" : 16,
        "x" : 1.6321051598727652,
        "y" : -5.880874566058433
      }, {
        "sequenceNumber" : 17,
        "x" : 1.6577461855137907,
        "y" : -5.76322343129694
      }, {
        "sequenceNumber" : 18,
        "x" : 1.6833872111548163,
        "y" : -5.582929973815421
      }, {
        "sequenceNumber" : 19,
        "x" : 1.7090282367958418,
        "y" : -5.307036266706058
      }, {
        "sequenceNumber" : 20,
        "x" : 1.7346692624368674,
        "y" : -4.885774408366931
      }, {
        "sequenceNumber" : 21,
        "x" : 1.760310288077893,
        "y" : -4.244696955631856
      }, {
        "sequenceNumber" : 22,
        "x" : 1.7859513137189185,
        "y" : -3.2740503053684544
      }, {
        "sequenceNumber" : 23,
        "x" : 1.811592339359944,
        "y" : -1.8156608589254413
      }, {
        "sequenceNumber" : 24,
        "x" : 1.8372333650009696,
        "y" : 0.3504452380112024
      }, {
        "sequenceNumber" : 25,
        "x" : 1.8628743906419951,
        "y" : 3.5132405899463954
      }, {
        "sequenceNumber" : 26,
        "x" : 1.8885154162830207,
        "y" : 8.018065260174943
      }, {
        "sequenceNumber" : 27,
        "x" : 1.9141564419240462,
        "y" : 14.212529556517838
      }, {
        "sequenceNumber" : 28,
        "x" : 1.9397974675650718,
        "y" : 22.33060469871222
      }, {
        "sequenceNumber" : 29,
        "x" : 1.9654384932060973,
        "y" : 32.32466397684103
      }, {
        "sequenceNumber" : 30,
        "x" : 1.9910795188471229,
        "y" : 43.72265569621524
      }, {
        "sequenceNumber" : 31,
        "x" : 2.0167205444881486,
        "y" : 55.64179880714586
      }, {
        "sequenceNumber" : 32,
        "x" : 2.042361570129174,
        "y" : 67.02712154997148
      }, {
        "sequenceNumber" : 33,
        "x" : 2.0680025957701997,
        "y" : 76.99989714617267
      }, {
        "sequenceNumber" : 34,
        "x" : 2.0936436214112253,
        "y" : 85.09369564999724
      }, {
        "sequenceNumber" : 35,
        "x" : 2.119284647052251,
        "y" : 91.26542698909536
      }, {
        "sequenceNumber" : 36,
        "x" : 2.1449256726932764,
        "y" : 95.75143347183476
      }, {
        "sequenceNumber" : 37,
        "x" : 2.170566698334302,
        "y" : 98.8998672995537
      }, {
        "sequenceNumber" : 38,
        "x" : 2.1962077239753275,
        "y" : 101.05559075267145
      }, {
        "sequenceNumber" : 39,
        "x" : 2.221848749616353,
        "y" : 102.50673946032978
      } ],
      "fitEquation" : "HILLEQUATION",
      "fitParameterMap" : {
        "Min" : {
          "value" : -6.100810940610783,
          "name" : "Min",
          "status" : "FLOAT"
        },
        "Max" : {
          "value" : 105.31889204413098,
          "name" : "Max",
          "status" : "FLOAT"
        },
        "EC50" : {
          "value" : 2.003741375962335,
          "name" : "EC50",
          "status" : "FLOAT"
        },
        "Slope" : {
          "value" : 7.27541261277126,
          "name" : "Slope",
          "status" : "FLOAT"
        }
      }
    }, {
      "id" : 51,
      "compound" : {
        "id" : 194,
        "name" : "C616-1326"
      },
      "experiment" : {
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
      },
      "experimentPoints" : [ {
        "x" : 16.666666666666668,
        "logx" : 1.2218487496163564,
        "y" : -6.240188344722317,
        "plateId" : 3,
        "doseId" : 206,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 33.333333333333336,
        "logx" : 1.5228787452803376,
        "y" : -6.488813877878615,
        "plateId" : 3,
        "doseId" : 207,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 50.0,
        "logx" : 1.6989700043360187,
        "y" : -3.6043297351922825,
        "plateId" : 3,
        "doseId" : 208,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 66.66666666666667,
        "logx" : 1.8239087409443189,
        "y" : 0.41738509243762323,
        "plateId" : 3,
        "doseId" : 209,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 83.33333333333334,
        "logx" : 1.9208187539523751,
        "y" : 16.80271678252801,
        "plateId" : 3,
        "doseId" : 210,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 100.0,
        "logx" : 2.0,
        "y" : 48.00928208353313,
        "plateId" : 3,
        "doseId" : 211,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 116.66666666666667,
        "logx" : 2.066946789630613,
        "y" : 77.30619802366165,
        "plateId" : 3,
        "doseId" : 212,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 133.33333333333334,
        "logx" : 2.1249387366083,
        "y" : 93.27078799394938,
        "plateId" : 3,
        "doseId" : 213,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 150.0,
        "logx" : 2.1760912590556813,
        "y" : 100.86862010036266,
        "plateId" : 3,
        "doseId" : 214,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 166.66666666666669,
        "logx" : 2.2218487496163566,
        "y" : 101.8432466123282,
        "plateId" : 3,
        "doseId" : 215,
        "resultStatus" : "INCLUDED"
      } ],
      "curveFitPoints" : [ {
        "sequenceNumber" : 0,
        "x" : 1.2218487496163564,
        "y" : -5.503933326081441
      }, {
        "sequenceNumber" : 1,
        "x" : 1.247489775257382,
        "y" : -5.5038110351578196
      }, {
        "sequenceNumber" : 2,
        "x" : 1.2731308008984075,
        "y" : -5.503623102541637
      }, {
        "sequenceNumber" : 3,
        "x" : 1.298771826539433,
        "y" : -5.503334294391965
      }, {
        "sequenceNumber" : 4,
        "x" : 1.3244128521804586,
        "y" : -5.50289046533666
      }, {
        "sequenceNumber" : 5,
        "x" : 1.3500538778214841,
        "y" : -5.502208408634295
      }, {
        "sequenceNumber" : 6,
        "x" : 1.3756949034625097,
        "y" : -5.501160259998697
      }, {
        "sequenceNumber" : 7,
        "x" : 1.4013359291035352,
        "y" : -5.49954953410233
      }, {
        "sequenceNumber" : 8,
        "x" : 1.4269769547445608,
        "y" : -5.497074308213372
      }, {
        "sequenceNumber" : 9,
        "x" : 1.4526179803855863,
        "y" : -5.493270667591845
      }, {
        "sequenceNumber" : 10,
        "x" : 1.4782590060266119,
        "y" : -5.487425850004912
      }, {
        "sequenceNumber" : 11,
        "x" : 1.5039000316676374,
        "y" : -5.4784449009103895
      }, {
        "sequenceNumber" : 12,
        "x" : 1.529541057308663,
        "y" : -5.464646064956495
      }, {
        "sequenceNumber" : 13,
        "x" : 1.5551820829496885,
        "y" : -5.443447085555474
      }, {
        "sequenceNumber" : 14,
        "x" : 1.580823108590714,
        "y" : -5.410884854672382
      }, {
        "sequenceNumber" : 15,
        "x" : 1.6064641342317396,
        "y" : -5.36088131368931
      }, {
        "sequenceNumber" : 16,
        "x" : 1.6321051598727652,
        "y" : -5.284124933783478
      }, {
        "sequenceNumber" : 17,
        "x" : 1.6577461855137907,
        "y" : -5.166374408074145
      }, {
        "sequenceNumber" : 18,
        "x" : 1.6833872111548163,
        "y" : -4.985904740104973
      }, {
        "sequenceNumber" : 19,
        "x" : 1.7090282367958418,
        "y" : -4.709705840921984
      }, {
        "sequenceNumber" : 20,
        "x" : 1.7346692624368674,
        "y" : -4.287926228951937
      }, {
        "sequenceNumber" : 21,
        "x" : 1.760310288077893,
        "y" : -3.6459882133204427
      }, {
        "sequenceNumber" : 22,
        "x" : 1.7859513137189185,
        "y" : -2.6739432514051096
      }, {
        "sequenceNumber" : 23,
        "x" : 1.811592339359944,
        "y" : -1.2133439413513099
      }, {
        "sequenceNumber" : 24,
        "x" : 1.8372333650009696,
        "y" : 0.9561279357437114
      }, {
        "sequenceNumber" : 25,
        "x" : 1.8628743906419951,
        "y" : 4.123788914396817
      }, {
        "sequenceNumber" : 26,
        "x" : 1.8885154162830207,
        "y" : 8.635123451118004
      }, {
        "sequenceNumber" : 27,
        "x" : 1.9141564419240462,
        "y" : 14.837301640691434
      }, {
        "sequenceNumber" : 28,
        "x" : 1.9397974675650718,
        "y" : 22.962767523300748
      }, {
        "sequenceNumber" : 29,
        "x" : 1.9654384932060973,
        "y" : 32.96103953370799
      }, {
        "sequenceNumber" : 30,
        "x" : 1.9910795188471229,
        "y" : 44.356545698881064
      }, {
        "sequenceNumber" : 31,
        "x" : 2.0167205444881486,
        "y" : 56.26407545968668
      }, {
        "sequenceNumber" : 32,
        "x" : 2.042361570129174,
        "y" : 67.62905852697207
      }, {
        "sequenceNumber" : 33,
        "x" : 2.0680025957701997,
        "y" : 77.5760575638076
      }, {
        "sequenceNumber" : 34,
        "x" : 2.0936436214112253,
        "y" : 85.6430494363295
      }, {
        "sequenceNumber" : 35,
        "x" : 2.119284647052251,
        "y" : 91.79048198842261
      }, {
        "sequenceNumber" : 36,
        "x" : 2.1449256726932764,
        "y" : 96.25650747132909
      }, {
        "sequenceNumber" : 37,
        "x" : 2.170566698334302,
        "y" : 99.38959672224078
      }, {
        "sequenceNumber" : 38,
        "x" : 2.1962077239753275,
        "y" : 101.53407984705862
      }, {
        "sequenceNumber" : 39,
        "x" : 2.221848749616353,
        "y" : 102.97725549263906
      } ],
      "fitEquation" : "HILLEQUATION",
      "fitParameterMap" : {
        "Min" : {
          "value" : -5.504161153809596,
          "name" : "Min",
          "status" : "FLOAT"
        },
        "Max" : {
          "value" : 105.77233824682716,
          "name" : "Max",
          "status" : "FLOAT"
        },
        "EC50" : {
          "value" : 2.00351764144519,
          "name" : "EC50",
          "status" : "FLOAT"
        },
        "Slope" : {
          "value" : 7.277756663608603,
          "name" : "Slope",
          "status" : "FLOAT"
        }
      }
    }, {
      "id" : 52,
      "compound" : {
        "id" : 195,
        "name" : "C1264-1498"
      },
      "experiment" : {
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
      },
      "experimentPoints" : [ {
        "x" : 16.666666666666668,
        "logx" : 1.2218487496163564,
        "y" : -8.74247171894255,
        "plateId" : 3,
        "doseId" : 218,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 33.333333333333336,
        "logx" : 1.5228787452803376,
        "y" : -40.246693120684085,
        "plateId" : 3,
        "doseId" : 219,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 50.0,
        "logx" : 1.6989700043360187,
        "y" : -7.19530032778993,
        "plateId" : 3,
        "doseId" : 220,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 66.66666666666667,
        "logx" : 1.8239087409443189,
        "y" : -15.355245690220265,
        "plateId" : 3,
        "doseId" : 221,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 83.33333333333334,
        "logx" : 1.9208187539523751,
        "y" : 8.069310613213517,
        "plateId" : 3,
        "doseId" : 222,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 100.0,
        "logx" : 2.0,
        "y" : 42.252875950028525,
        "plateId" : 3,
        "doseId" : 223,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 116.66666666666667,
        "logx" : 2.066946789630613,
        "y" : 30.758363097735547,
        "plateId" : 3,
        "doseId" : 224,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 133.33333333333334,
        "logx" : 2.1249387366083,
        "y" : 59.71390443721464,
        "plateId" : 3,
        "doseId" : 225,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 150.0,
        "logx" : 2.1760912590556813,
        "y" : 59.88465774827988,
        "plateId" : 3,
        "doseId" : 226,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 166.66666666666669,
        "logx" : 2.2218487496163566,
        "y" : 64.71088326974042,
        "plateId" : 3,
        "doseId" : 227,
        "resultStatus" : "INCLUDED"
      } ],
      "curveFitPoints" : [ {
        "sequenceNumber" : 0,
        "x" : 1.2218487496163564,
        "y" : -14.689367196780085
      }, {
        "sequenceNumber" : 1,
        "x" : 1.247489775257382,
        "y" : -14.688438115535298
      }, {
        "sequenceNumber" : 2,
        "x" : 1.2731308008984075,
        "y" : -14.687115282061868
      }, {
        "sequenceNumber" : 3,
        "x" : 1.298771826539433,
        "y" : -14.685231843370296
      }, {
        "sequenceNumber" : 4,
        "x" : 1.3244128521804586,
        "y" : -14.682550265346674
      }, {
        "sequenceNumber" : 5,
        "x" : 1.3500538778214841,
        "y" : -14.67873241495615
      }, {
        "sequenceNumber" : 6,
        "x" : 1.3756949034625097,
        "y" : -14.673297003187098
      }, {
        "sequenceNumber" : 7,
        "x" : 1.4013359291035352,
        "y" : -14.665559074034405
      }, {
        "sequenceNumber" : 8,
        "x" : 1.4269769547445608,
        "y" : -14.654544015686655
      }, {
        "sequenceNumber" : 9,
        "x" : 1.4526179803855863,
        "y" : -14.638865469118487
      }, {
        "sequenceNumber" : 10,
        "x" : 1.4782590060266119,
        "y" : -14.616552183891647
      }, {
        "sequenceNumber" : 11,
        "x" : 1.5039000316676374,
        "y" : -14.584802893578182
      }, {
        "sequenceNumber" : 12,
        "x" : 1.529541057308663,
        "y" : -14.53964013720133
      }, {
        "sequenceNumber" : 13,
        "x" : 1.5551820829496885,
        "y" : -14.475423086326565
      }, {
        "sequenceNumber" : 14,
        "x" : 1.580823108590714,
        "y" : -14.3841654263435
      }, {
        "sequenceNumber" : 15,
        "x" : 1.6064641342317396,
        "y" : -14.254587290079929
      }, {
        "sequenceNumber" : 16,
        "x" : 1.6321051598727652,
        "y" : -14.070811661512796
      }, {
        "sequenceNumber" : 17,
        "x" : 1.6577461855137907,
        "y" : -13.810600250790426
      }, {
        "sequenceNumber" : 18,
        "x" : 1.6833872111548163,
        "y" : -13.443022846902474
      }, {
        "sequenceNumber" : 19,
        "x" : 1.7090282367958418,
        "y" : -12.925491306159875
      }, {
        "sequenceNumber" : 20,
        "x" : 1.7346692624368674,
        "y" : -12.200209878004584
      }, {
        "sequenceNumber" : 21,
        "x" : 1.760310288077893,
        "y" : -11.190374803464932
      }, {
        "sequenceNumber" : 22,
        "x" : 1.7859513137189185,
        "y" : -9.797010018084325
      }, {
        "sequenceNumber" : 23,
        "x" : 1.811592339359944,
        "y" : -7.898265407018376
      }, {
        "sequenceNumber" : 24,
        "x" : 1.8372333650009696,
        "y" : -5.354313013258171
      }, {
        "sequenceNumber" : 25,
        "x" : 1.8628743906419951,
        "y" : -2.0222018382044507
      }, {
        "sequenceNumber" : 26,
        "x" : 1.8885154162830207,
        "y" : 2.2151701161210084
      }, {
        "sequenceNumber" : 27,
        "x" : 1.9141564419240462,
        "y" : 7.405778590801383
      }, {
        "sequenceNumber" : 28,
        "x" : 1.9397974675650718,
        "y" : 13.480272003490366
      }, {
        "sequenceNumber" : 29,
        "x" : 1.9654384932060973,
        "y" : 20.220640500711994
      }, {
        "sequenceNumber" : 30,
        "x" : 1.9910795188471229,
        "y" : 27.272084668799337
      }, {
        "sequenceNumber" : 31,
        "x" : 2.0167205444881486,
        "y" : 34.20871348842196
      }, {
        "sequenceNumber" : 32,
        "x" : 2.042361570129174,
        "y" : 40.631404305779974
      }, {
        "sequenceNumber" : 33,
        "x" : 2.0680025957701997,
        "y" : 46.253247894184334
      }, {
        "sequenceNumber" : 34,
        "x" : 2.0936436214112253,
        "y" : 50.93709996695629
      }, {
        "sequenceNumber" : 35,
        "x" : 2.119284647052251,
        "y" : 54.68160377529964
      }, {
        "sequenceNumber" : 36,
        "x" : 2.1449256726932764,
        "y" : 57.57754493201019
      }, {
        "sequenceNumber" : 37,
        "x" : 2.170566698334302,
        "y" : 59.760326386065735
      }, {
        "sequenceNumber" : 38,
        "x" : 2.1962077239753275,
        "y" : 61.373871944046336
      }, {
        "sequenceNumber" : 39,
        "x" : 2.221848749616353,
        "y" : 62.54955532529363
      } ],
      "fitEquation" : "HILLEQUATION",
      "fitParameterMap" : {
        "Min" : {
          "value" : -14.691559296387886,
          "name" : "Min",
          "status" : "FLOAT"
        },
        "Max" : {
          "value" : 65.47286208998865,
          "name" : "Max",
          "status" : "FLOAT"
        },
        "EC50" : {
          "value" : 1.9842624057037836,
          "name" : "EC50",
          "status" : "FLOAT"
        },
        "Slope" : {
          "value" : 5.985083642572277,
          "name" : "Slope",
          "status" : "FLOAT"
        }
      }
    }, {
      "id" : 53,
      "compound" : {
        "id" : 196,
        "name" : "C1210-772"
      },
      "experiment" : {
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
      },
      "experimentPoints" : [ {
        "x" : 16.666666666666668,
        "logx" : 1.2218487496163564,
        "y" : -12.138670884102295,
        "plateId" : 3,
        "doseId" : 230,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 33.333333333333336,
        "logx" : 1.5228787452803376,
        "y" : -35.415481203486756,
        "plateId" : 3,
        "doseId" : 231,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 50.0,
        "logx" : 1.6989700043360187,
        "y" : -37.59217262117846,
        "plateId" : 3,
        "doseId" : 232,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 66.66666666666667,
        "logx" : 1.8239087409443189,
        "y" : 2.140318167202806,
        "plateId" : 3,
        "doseId" : 233,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 83.33333333333334,
        "logx" : 1.9208187539523751,
        "y" : 10.301263286067803,
        "plateId" : 3,
        "doseId" : 234,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 100.0,
        "logx" : 2.0,
        "y" : 39.14148711608717,
        "plateId" : 3,
        "doseId" : 235,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 116.66666666666667,
        "logx" : 2.066946789630613,
        "y" : 38.484237365950854,
        "plateId" : 3,
        "doseId" : 236,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 133.33333333333334,
        "logx" : 2.1249387366083,
        "y" : 58.34731973120925,
        "plateId" : 3,
        "doseId" : 237,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 150.0,
        "logx" : 2.1760912590556813,
        "y" : 94.25979167504948,
        "plateId" : 3,
        "doseId" : 238,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 166.66666666666669,
        "logx" : 2.2218487496163566,
        "y" : 95.35962197710889,
        "plateId" : 3,
        "doseId" : 239,
        "resultStatus" : "INCLUDED"
      } ],
      "curveFitPoints" : [ {
        "sequenceNumber" : 0,
        "x" : 1.2218487496163564,
        "y" : 0.0
      }, {
        "sequenceNumber" : 1,
        "x" : 1.247489775257382,
        "y" : 0.0
      }, {
        "sequenceNumber" : 2,
        "x" : 1.2731308008984075,
        "y" : 0.0
      }, {
        "sequenceNumber" : 3,
        "x" : 1.298771826539433,
        "y" : 0.0
      }, {
        "sequenceNumber" : 4,
        "x" : 1.3244128521804586,
        "y" : 0.0
      }, {
        "sequenceNumber" : 5,
        "x" : 1.3500538778214841,
        "y" : 0.0
      }, {
        "sequenceNumber" : 6,
        "x" : 1.3756949034625097,
        "y" : 0.0
      }, {
        "sequenceNumber" : 7,
        "x" : 1.4013359291035352,
        "y" : 0.0
      }, {
        "sequenceNumber" : 8,
        "x" : 1.4269769547445608,
        "y" : 0.0
      }, {
        "sequenceNumber" : 9,
        "x" : 1.4526179803855863,
        "y" : 0.0
      }, {
        "sequenceNumber" : 10,
        "x" : 1.4782590060266119,
        "y" : 0.0
      }, {
        "sequenceNumber" : 11,
        "x" : 1.5039000316676374,
        "y" : 0.0
      }, {
        "sequenceNumber" : 12,
        "x" : 1.529541057308663,
        "y" : 0.0
      }, {
        "sequenceNumber" : 13,
        "x" : 1.5551820829496885,
        "y" : 0.0
      }, {
        "sequenceNumber" : 14,
        "x" : 1.580823108590714,
        "y" : 0.0
      }, {
        "sequenceNumber" : 15,
        "x" : 1.6064641342317396,
        "y" : 0.0
      }, {
        "sequenceNumber" : 16,
        "x" : 1.6321051598727652,
        "y" : 0.0
      }, {
        "sequenceNumber" : 17,
        "x" : 1.6577461855137907,
        "y" : 0.0
      }, {
        "sequenceNumber" : 18,
        "x" : 1.6833872111548163,
        "y" : 0.0
      }, {
        "sequenceNumber" : 19,
        "x" : 1.7090282367958418,
        "y" : 0.0
      }, {
        "sequenceNumber" : 20,
        "x" : 1.7346692624368674,
        "y" : 0.0
      }, {
        "sequenceNumber" : 21,
        "x" : 1.760310288077893,
        "y" : 0.0
      }, {
        "sequenceNumber" : 22,
        "x" : 1.7859513137189185,
        "y" : 0.0
      }, {
        "sequenceNumber" : 23,
        "x" : 1.811592339359944,
        "y" : 0.0
      }, {
        "sequenceNumber" : 24,
        "x" : 1.8372333650009696,
        "y" : 0.0
      }, {
        "sequenceNumber" : 25,
        "x" : 1.8628743906419951,
        "y" : 0.0
      }, {
        "sequenceNumber" : 26,
        "x" : 1.8885154162830207,
        "y" : 0.0
      }, {
        "sequenceNumber" : 27,
        "x" : 1.9141564419240462,
        "y" : 0.0
      }, {
        "sequenceNumber" : 28,
        "x" : 1.9397974675650718,
        "y" : 0.0
      }, {
        "sequenceNumber" : 29,
        "x" : 1.9654384932060973,
        "y" : 0.0
      }, {
        "sequenceNumber" : 30,
        "x" : 1.9910795188471229,
        "y" : 0.0
      }, {
        "sequenceNumber" : 31,
        "x" : 2.0167205444881486,
        "y" : 0.0
      }, {
        "sequenceNumber" : 32,
        "x" : 2.042361570129174,
        "y" : 0.0
      }, {
        "sequenceNumber" : 33,
        "x" : 2.0680025957701997,
        "y" : 0.0
      }, {
        "sequenceNumber" : 34,
        "x" : 2.0936436214112253,
        "y" : 0.0
      }, {
        "sequenceNumber" : 35,
        "x" : 2.119284647052251,
        "y" : 0.0
      }, {
        "sequenceNumber" : 36,
        "x" : 2.1449256726932764,
        "y" : 0.0
      }, {
        "sequenceNumber" : 37,
        "x" : 2.170566698334302,
        "y" : 0.0
      }, {
        "sequenceNumber" : 38,
        "x" : 2.1962077239753275,
        "y" : 0.0
      }, {
        "sequenceNumber" : 39,
        "x" : 2.221848749616353,
        "y" : 0.0
      } ],
      "fitEquation" : "HILLEQUATION",
      "fitParameterMap" : { }
    }, {
      "id" : 54,
      "compound" : {
        "id" : 197,
        "name" : "C1304-1284"
      },
      "experiment" : {
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
      },
      "experimentPoints" : [ {
        "x" : 16.666666666666668,
        "logx" : 1.2218487496163564,
        "y" : -37.25408689312377,
        "plateId" : 3,
        "doseId" : 242,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 33.333333333333336,
        "logx" : 1.5228787452803376,
        "y" : -30.772192463750343,
        "plateId" : 3,
        "doseId" : 243,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 50.0,
        "logx" : 1.6989700043360187,
        "y" : -18.88923683463341,
        "plateId" : 3,
        "doseId" : 244,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 66.66666666666667,
        "logx" : 1.8239087409443189,
        "y" : -11.437704196262356,
        "plateId" : 3,
        "doseId" : 245,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 83.33333333333334,
        "logx" : 1.9208187539523751,
        "y" : -17.51435100034539,
        "plateId" : 3,
        "doseId" : 246,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 100.0,
        "logx" : 2.0,
        "y" : 24.208035682284823,
        "plateId" : 3,
        "doseId" : 247,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 116.66666666666667,
        "logx" : 2.066946789630613,
        "y" : 73.89396366330676,
        "plateId" : 3,
        "doseId" : 248,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 133.33333333333334,
        "logx" : 2.1249387366083,
        "y" : 57.09296129746719,
        "plateId" : 3,
        "doseId" : 249,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 150.0,
        "logx" : 2.1760912590556813,
        "y" : 70.03023774799902,
        "plateId" : 3,
        "doseId" : 250,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 166.66666666666669,
        "logx" : 2.2218487496163566,
        "y" : 100.53526451067121,
        "plateId" : 3,
        "doseId" : 251,
        "resultStatus" : "INCLUDED"
      } ],
      "curveFitPoints" : [ {
        "sequenceNumber" : 0,
        "x" : 1.2218487496163564,
        "y" : 0.0
      }, {
        "sequenceNumber" : 1,
        "x" : 1.247489775257382,
        "y" : 0.0
      }, {
        "sequenceNumber" : 2,
        "x" : 1.2731308008984075,
        "y" : 0.0
      }, {
        "sequenceNumber" : 3,
        "x" : 1.298771826539433,
        "y" : 0.0
      }, {
        "sequenceNumber" : 4,
        "x" : 1.3244128521804586,
        "y" : 0.0
      }, {
        "sequenceNumber" : 5,
        "x" : 1.3500538778214841,
        "y" : 0.0
      }, {
        "sequenceNumber" : 6,
        "x" : 1.3756949034625097,
        "y" : 0.0
      }, {
        "sequenceNumber" : 7,
        "x" : 1.4013359291035352,
        "y" : 0.0
      }, {
        "sequenceNumber" : 8,
        "x" : 1.4269769547445608,
        "y" : 0.0
      }, {
        "sequenceNumber" : 9,
        "x" : 1.4526179803855863,
        "y" : 0.0
      }, {
        "sequenceNumber" : 10,
        "x" : 1.4782590060266119,
        "y" : 0.0
      }, {
        "sequenceNumber" : 11,
        "x" : 1.5039000316676374,
        "y" : 0.0
      }, {
        "sequenceNumber" : 12,
        "x" : 1.529541057308663,
        "y" : 0.0
      }, {
        "sequenceNumber" : 13,
        "x" : 1.5551820829496885,
        "y" : 0.0
      }, {
        "sequenceNumber" : 14,
        "x" : 1.580823108590714,
        "y" : 0.0
      }, {
        "sequenceNumber" : 15,
        "x" : 1.6064641342317396,
        "y" : 0.0
      }, {
        "sequenceNumber" : 16,
        "x" : 1.6321051598727652,
        "y" : 0.0
      }, {
        "sequenceNumber" : 17,
        "x" : 1.6577461855137907,
        "y" : 0.0
      }, {
        "sequenceNumber" : 18,
        "x" : 1.6833872111548163,
        "y" : 0.0
      }, {
        "sequenceNumber" : 19,
        "x" : 1.7090282367958418,
        "y" : 0.0
      }, {
        "sequenceNumber" : 20,
        "x" : 1.7346692624368674,
        "y" : 0.0
      }, {
        "sequenceNumber" : 21,
        "x" : 1.760310288077893,
        "y" : 0.0
      }, {
        "sequenceNumber" : 22,
        "x" : 1.7859513137189185,
        "y" : 0.0
      }, {
        "sequenceNumber" : 23,
        "x" : 1.811592339359944,
        "y" : 0.0
      }, {
        "sequenceNumber" : 24,
        "x" : 1.8372333650009696,
        "y" : 0.0
      }, {
        "sequenceNumber" : 25,
        "x" : 1.8628743906419951,
        "y" : 0.0
      }, {
        "sequenceNumber" : 26,
        "x" : 1.8885154162830207,
        "y" : 0.0
      }, {
        "sequenceNumber" : 27,
        "x" : 1.9141564419240462,
        "y" : 0.0
      }, {
        "sequenceNumber" : 28,
        "x" : 1.9397974675650718,
        "y" : 0.0
      }, {
        "sequenceNumber" : 29,
        "x" : 1.9654384932060973,
        "y" : 0.0
      }, {
        "sequenceNumber" : 30,
        "x" : 1.9910795188471229,
        "y" : 0.0
      }, {
        "sequenceNumber" : 31,
        "x" : 2.0167205444881486,
        "y" : 0.0
      }, {
        "sequenceNumber" : 32,
        "x" : 2.042361570129174,
        "y" : 0.0
      }, {
        "sequenceNumber" : 33,
        "x" : 2.0680025957701997,
        "y" : 0.0
      }, {
        "sequenceNumber" : 34,
        "x" : 2.0936436214112253,
        "y" : 0.0
      }, {
        "sequenceNumber" : 35,
        "x" : 2.119284647052251,
        "y" : 0.0
      }, {
        "sequenceNumber" : 36,
        "x" : 2.1449256726932764,
        "y" : 0.0
      }, {
        "sequenceNumber" : 37,
        "x" : 2.170566698334302,
        "y" : 0.0
      }, {
        "sequenceNumber" : 38,
        "x" : 2.1962077239753275,
        "y" : 0.0
      }, {
        "sequenceNumber" : 39,
        "x" : 2.221848749616353,
        "y" : 0.0
      } ],
      "fitEquation" : "HILLEQUATION",
      "fitParameterMap" : { }
    }, {
      "id" : 55,
      "compound" : {
        "id" : 198,
        "name" : "C1813-1511"
      },
      "experiment" : {
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
      },
      "experimentPoints" : [ {
        "x" : 16.666666666666668,
        "logx" : 1.2218487496163564,
        "y" : -6.117547733245703,
        "plateId" : 3,
        "doseId" : 254,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 33.333333333333336,
        "logx" : 1.5228787452803376,
        "y" : -7.584844044538645,
        "plateId" : 3,
        "doseId" : 255,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 50.0,
        "logx" : 1.6989700043360187,
        "y" : -3.7070565725751847,
        "plateId" : 3,
        "doseId" : 256,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 66.66666666666667,
        "logx" : 1.8239087409443189,
        "y" : -0.5382595365226248,
        "plateId" : 3,
        "doseId" : 257,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 83.33333333333334,
        "logx" : 1.9208187539523751,
        "y" : 17.502376681196402,
        "plateId" : 3,
        "doseId" : 258,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 100.0,
        "logx" : 2.0,
        "y" : 47.51733448608951,
        "plateId" : 3,
        "doseId" : 259,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 116.66666666666667,
        "logx" : 2.066946789630613,
        "y" : 75.12127299908809,
        "plateId" : 3,
        "doseId" : 260,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 133.33333333333334,
        "logx" : 2.1249387366083,
        "y" : 91.6543179223993,
        "plateId" : 3,
        "doseId" : 261,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 150.0,
        "logx" : 2.1760912590556813,
        "y" : 101.4341692334232,
        "plateId" : 3,
        "doseId" : 262,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 166.66666666666669,
        "logx" : 2.2218487496163566,
        "y" : 100.65859205301484,
        "plateId" : 3,
        "doseId" : 263,
        "resultStatus" : "INCLUDED"
      } ],
      "curveFitPoints" : [ {
        "sequenceNumber" : 0,
        "x" : 1.2218487496163564,
        "y" : -6.150390400789074
      }, {
        "sequenceNumber" : 1,
        "x" : 1.247489775257382,
        "y" : -6.1501756039241835
      }, {
        "sequenceNumber" : 2,
        "x" : 1.2731308008984075,
        "y" : -6.149852361173313
      }, {
        "sequenceNumber" : 3,
        "x" : 1.298771826539433,
        "y" : -6.149365921936959
      }, {
        "sequenceNumber" : 4,
        "x" : 1.3244128521804586,
        "y" : -6.148633895410173
      }, {
        "sequenceNumber" : 5,
        "x" : 1.3500538778214841,
        "y" : -6.1475322986294625
      }, {
        "sequenceNumber" : 6,
        "x" : 1.3756949034625097,
        "y" : -6.145874564371779
      }, {
        "sequenceNumber" : 7,
        "x" : 1.4013359291035352,
        "y" : -6.143379959464011
      }, {
        "sequenceNumber" : 8,
        "x" : 1.4269769547445608,
        "y" : -6.13962607834524
      }, {
        "sequenceNumber" : 9,
        "x" : 1.4526179803855863,
        "y" : -6.133977397439243
      }, {
        "sequenceNumber" : 10,
        "x" : 1.4782590060266119,
        "y" : -6.125477862009026
      }, {
        "sequenceNumber" : 11,
        "x" : 1.5039000316676374,
        "y" : -6.112689477732527
      }, {
        "sequenceNumber" : 12,
        "x" : 1.529541057308663,
        "y" : -6.093449943464379
      }, {
        "sequenceNumber" : 13,
        "x" : 1.5551820829496885,
        "y" : -6.064509123630942
      }, {
        "sequenceNumber" : 14,
        "x" : 1.580823108590714,
        "y" : -6.020984708969439
      }, {
        "sequenceNumber" : 15,
        "x" : 1.6064641342317396,
        "y" : -5.955549206669786
      }, {
        "sequenceNumber" : 16,
        "x" : 1.6321051598727652,
        "y" : -5.857220333454335
      }, {
        "sequenceNumber" : 17,
        "x" : 1.6577461855137907,
        "y" : -5.709571903416435
      }, {
        "sequenceNumber" : 18,
        "x" : 1.6833872111548163,
        "y" : -5.4881113341187335
      }, {
        "sequenceNumber" : 19,
        "x" : 1.7090282367958418,
        "y" : -5.156488871390316
      }, {
        "sequenceNumber" : 20,
        "x" : 1.7346692624368674,
        "y" : -4.661137140410698
      }, {
        "sequenceNumber" : 21,
        "x" : 1.760310288077893,
        "y" : -3.923955508173622
      }, {
        "sequenceNumber" : 22,
        "x" : 1.7859513137189185,
        "y" : -2.8329094403444
      }, {
        "sequenceNumber" : 23,
        "x" : 1.811592339359944,
        "y" : -1.231229387664583
      }, {
        "sequenceNumber" : 24,
        "x" : 1.8372333650009696,
        "y" : 1.0921894003725985
      }, {
        "sequenceNumber" : 25,
        "x" : 1.8628743906419951,
        "y" : 4.404901711412739
      }, {
        "sequenceNumber" : 26,
        "x" : 1.8885154162830207,
        "y" : 9.013743162873977
      }, {
        "sequenceNumber" : 27,
        "x" : 1.9141564419240462,
        "y" : 15.211798881491356
      }, {
        "sequenceNumber" : 28,
        "x" : 1.9397974675650718,
        "y" : 23.177144180188492
      }, {
        "sequenceNumber" : 29,
        "x" : 1.9654384932060973,
        "y" : 32.837163273529654
      }, {
        "sequenceNumber" : 30,
        "x" : 1.9910795188471229,
        "y" : 43.76168782777626
      }, {
        "sequenceNumber" : 31,
        "x" : 2.0167205444881486,
        "y" : 55.18147685843511
      }, {
        "sequenceNumber" : 32,
        "x" : 2.042361570129174,
        "y" : 66.17805222367272
      }, {
        "sequenceNumber" : 33,
        "x" : 2.0680025957701997,
        "y" : 75.96103164337931
      }, {
        "sequenceNumber" : 34,
        "x" : 2.0936436214112253,
        "y" : 84.06995233240364
      }, {
        "sequenceNumber" : 35,
        "x" : 2.119284647052251,
        "y" : 90.40629338446857
      }, {
        "sequenceNumber" : 36,
        "x" : 2.1449256726932764,
        "y" : 95.13311980012095
      }, {
        "sequenceNumber" : 37,
        "x" : 2.170566698334302,
        "y" : 98.53865573472646
      }, {
        "sequenceNumber" : 38,
        "x" : 2.1962077239753275,
        "y" : 100.9311916387374
      }, {
        "sequenceNumber" : 39,
        "x" : 2.221848749616353,
        "y" : 102.58244968973634
      } ],
      "fitEquation" : "HILLEQUATION",
      "fitParameterMap" : {
        "Min" : {
          "value" : -6.150815841518435,
          "name" : "Min",
          "status" : "FLOAT"
        },
        "Max" : {
          "value" : 106.00808263194605,
          "name" : "Max",
          "status" : "FLOAT"
        },
        "EC50" : {
          "value" : 2.004933366415429,
          "name" : "EC50",
          "status" : "FLOAT"
        },
        "Slope" : {
          "value" : 6.922614628348823,
          "name" : "Slope",
          "status" : "FLOAT"
        }
      }
    }, {
      "id" : 56,
      "compound" : {
        "id" : 199,
        "name" : "C1531-701"
      },
      "experiment" : {
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
      },
      "experimentPoints" : [ {
        "x" : 16.666666666666668,
        "logx" : 1.2218487496163564,
        "y" : -33.02729759357268,
        "plateId" : 3,
        "doseId" : 266,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 33.333333333333336,
        "logx" : 1.5228787452803376,
        "y" : -26.21093880153022,
        "plateId" : 3,
        "doseId" : 267,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 50.0,
        "logx" : 1.6989700043360187,
        "y" : -16.508446740993797,
        "plateId" : 3,
        "doseId" : 268,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 66.66666666666667,
        "logx" : 1.8239087409443189,
        "y" : -15.150606069157355,
        "plateId" : 3,
        "doseId" : 269,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 83.33333333333334,
        "logx" : 1.9208187539523751,
        "y" : -10.665235416319437,
        "plateId" : 3,
        "doseId" : 270,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 100.0,
        "logx" : 2.0,
        "y" : 35.51171669936222,
        "plateId" : 3,
        "doseId" : 271,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 116.66666666666667,
        "logx" : 2.066946789630613,
        "y" : 78.47297473889351,
        "plateId" : 3,
        "doseId" : 272,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 133.33333333333334,
        "logx" : 2.1249387366083,
        "y" : 95.59547190123516,
        "plateId" : 3,
        "doseId" : 273,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 150.0,
        "logx" : 2.1760912590556813,
        "y" : 72.61411134779087,
        "plateId" : 3,
        "doseId" : 274,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 166.66666666666669,
        "logx" : 2.2218487496163566,
        "y" : 80.52459786459882,
        "plateId" : 3,
        "doseId" : 275,
        "resultStatus" : "INCLUDED"
      } ],
      "curveFitPoints" : [ {
        "sequenceNumber" : 0,
        "x" : 1.2218487496163564,
        "y" : -22.192913712997257
      }, {
        "sequenceNumber" : 1,
        "x" : 1.247489775257382,
        "y" : -22.192913712608924
      }, {
        "sequenceNumber" : 2,
        "x" : 1.2731308008984075,
        "y" : -22.19291371166514
      }, {
        "sequenceNumber" : 3,
        "x" : 1.298771826539433,
        "y" : -22.192913709371417
      }, {
        "sequenceNumber" : 4,
        "x" : 1.3244128521804586,
        "y" : -22.192913703796904
      }, {
        "sequenceNumber" : 5,
        "x" : 1.3500538778214841,
        "y" : -22.192913690248936
      }, {
        "sequenceNumber" : 6,
        "x" : 1.3756949034625097,
        "y" : -22.192913657322777
      }, {
        "sequenceNumber" : 7,
        "x" : 1.4013359291035352,
        "y" : -22.19291357730102
      }, {
        "sequenceNumber" : 8,
        "x" : 1.4269769547445608,
        "y" : -22.19291338282096
      }, {
        "sequenceNumber" : 9,
        "x" : 1.4526179803855863,
        "y" : -22.19291291016835
      }, {
        "sequenceNumber" : 10,
        "x" : 1.4782590060266119,
        "y" : -22.192911761461932
      }, {
        "sequenceNumber" : 11,
        "x" : 1.5039000316676374,
        "y" : -22.192908969715155
      }, {
        "sequenceNumber" : 12,
        "x" : 1.529541057308663,
        "y" : -22.19290218482259
      }, {
        "sequenceNumber" : 13,
        "x" : 1.5551820829496885,
        "y" : -22.192885695232057
      }, {
        "sequenceNumber" : 14,
        "x" : 1.580823108590714,
        "y" : -22.192845619946002
      }, {
        "sequenceNumber" : 15,
        "x" : 1.6064641342317396,
        "y" : -22.192748223511444
      }, {
        "sequenceNumber" : 16,
        "x" : 1.6321051598727652,
        "y" : -22.19251151783112
      }, {
        "sequenceNumber" : 17,
        "x" : 1.6577461855137907,
        "y" : -22.191936247016642
      }, {
        "sequenceNumber" : 18,
        "x" : 1.6833872111548163,
        "y" : -22.190538169539717
      }, {
        "sequenceNumber" : 19,
        "x" : 1.7090282367958418,
        "y" : -22.187140520150475
      }, {
        "sequenceNumber" : 20,
        "x" : 1.7346692624368674,
        "y" : -22.17888398658851
      }, {
        "sequenceNumber" : 21,
        "x" : 1.760310288077893,
        "y" : -22.158823171264835
      }, {
        "sequenceNumber" : 22,
        "x" : 1.7859513137189185,
        "y" : -22.110100248380327
      }, {
        "sequenceNumber" : 23,
        "x" : 1.811592339359944,
        "y" : -21.99187374643898
      }, {
        "sequenceNumber" : 24,
        "x" : 1.8372333650009696,
        "y" : -21.70564153192363
      }, {
        "sequenceNumber" : 25,
        "x" : 1.8628743906419951,
        "y" : -21.01642042452358
      }, {
        "sequenceNumber" : 26,
        "x" : 1.8885154162830207,
        "y" : -19.378363762410046
      }, {
        "sequenceNumber" : 27,
        "x" : 1.9141564419240462,
        "y" : -15.603134334009681
      }, {
        "sequenceNumber" : 28,
        "x" : 1.9397974675650718,
        "y" : -7.486575247572169
      }, {
        "sequenceNumber" : 29,
        "x" : 1.9654384932060973,
        "y" : 7.62511313638532
      }, {
        "sequenceNumber" : 30,
        "x" : 1.9910795188471229,
        "y" : 29.467432544115123
      }, {
        "sequenceNumber" : 31,
        "x" : 2.0167205444881486,
        "y" : 51.75618389661972
      }, {
        "sequenceNumber" : 32,
        "x" : 2.042361570129174,
        "y" : 67.71762210285709
      }, {
        "sequenceNumber" : 33,
        "x" : 2.0680025957701997,
        "y" : 76.48106565523456
      }, {
        "sequenceNumber" : 34,
        "x" : 2.0936436214112253,
        "y" : 80.60371194446387
      }, {
        "sequenceNumber" : 35,
        "x" : 2.119284647052251,
        "y" : 82.40182199099677
      }, {
        "sequenceNumber" : 36,
        "x" : 2.1449256726932764,
        "y" : 83.160080513297
      }, {
        "sequenceNumber" : 37,
        "x" : 2.170566698334302,
        "y" : 83.4752794641388
      }, {
        "sequenceNumber" : 38,
        "x" : 2.1962077239753275,
        "y" : 83.60552118674833
      }, {
        "sequenceNumber" : 39,
        "x" : 2.221848749616353,
        "y" : 83.65920438867023
      } ],
      "fitEquation" : "HILLEQUATION",
      "fitParameterMap" : {
        "Min" : {
          "value" : -22.192913713268755,
          "name" : "Min",
          "status" : "FLOAT"
        },
        "Max" : {
          "value" : 83.69676854184814,
          "name" : "Max",
          "status" : "FLOAT"
        },
        "EC50" : {
          "value" : 1.9924808190483267,
          "name" : "EC50",
          "status" : "FLOAT"
        },
        "Slope" : {
          "value" : 15.041012670361576,
          "name" : "Slope",
          "status" : "FLOAT"
        }
      }
    }, {
      "id" : 57,
      "compound" : {
        "id" : 200,
        "name" : "C1961-1510"
      },
      "experiment" : {
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
      },
      "experimentPoints" : [ {
        "x" : 16.666666666666668,
        "logx" : 1.2218487496163564,
        "y" : -16.325022405627728,
        "plateId" : 3,
        "doseId" : 278,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 33.333333333333336,
        "logx" : 1.5228787452803376,
        "y" : -13.545807167430373,
        "plateId" : 3,
        "doseId" : 279,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 50.0,
        "logx" : 1.6989700043360187,
        "y" : -8.781555842569846,
        "plateId" : 3,
        "doseId" : 280,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 66.66666666666667,
        "logx" : 1.8239087409443189,
        "y" : -9.174043574647747,
        "plateId" : 3,
        "doseId" : 281,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 83.33333333333334,
        "logx" : 1.9208187539523751,
        "y" : 15.674387791024271,
        "plateId" : 3,
        "doseId" : 282,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 100.0,
        "logx" : 2.0,
        "y" : 39.41602679697649,
        "plateId" : 3,
        "doseId" : 283,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 116.66666666666667,
        "logx" : 2.066946789630613,
        "y" : 66.79192568852449,
        "plateId" : 3,
        "doseId" : 284,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 133.33333333333334,
        "logx" : 2.1249387366083,
        "y" : 85.95481599739647,
        "plateId" : 3,
        "doseId" : 285,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 150.0,
        "logx" : 2.1760912590556813,
        "y" : 101.53977273538982,
        "plateId" : 3,
        "doseId" : 286,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 166.66666666666669,
        "logx" : 2.2218487496163566,
        "y" : 93.62108455271625,
        "plateId" : 3,
        "doseId" : 287,
        "resultStatus" : "INCLUDED"
      } ],
      "curveFitPoints" : [ {
        "sequenceNumber" : 0,
        "x" : 1.2218487496163564,
        "y" : -13.8389388117367
      }, {
        "sequenceNumber" : 1,
        "x" : 1.247489775257382,
        "y" : -13.838244631032683
      }, {
        "sequenceNumber" : 2,
        "x" : 1.2731308008984075,
        "y" : -13.837244246145701
      }, {
        "sequenceNumber" : 3,
        "x" : 1.298771826539433,
        "y" : -13.835802599023062
      }, {
        "sequenceNumber" : 4,
        "x" : 1.3244128521804586,
        "y" : -13.833725071087438
      }, {
        "sequenceNumber" : 5,
        "x" : 1.3500538778214841,
        "y" : -13.830731227613482
      }, {
        "sequenceNumber" : 6,
        "x" : 1.3756949034625097,
        "y" : -13.826416999375036
      }, {
        "sequenceNumber" : 7,
        "x" : 1.4013359291035352,
        "y" : -13.820200221657695
      }, {
        "sequenceNumber" : 8,
        "x" : 1.4269769547445608,
        "y" : -13.811242232107109
      }, {
        "sequenceNumber" : 9,
        "x" : 1.4526179803855863,
        "y" : -13.798335055468144
      }, {
        "sequenceNumber" : 10,
        "x" : 1.4782590060266119,
        "y" : -13.77973917951606
      }, {
        "sequenceNumber" : 11,
        "x" : 1.5039000316676374,
        "y" : -13.752950509762297
      }, {
        "sequenceNumber" : 12,
        "x" : 1.529541057308663,
        "y" : -13.714366055377159
      }, {
        "sequenceNumber" : 13,
        "x" : 1.5551820829496885,
        "y" : -13.658805315998714
      }, {
        "sequenceNumber" : 14,
        "x" : 1.580823108590714,
        "y" : -13.578827107620471
      }, {
        "sequenceNumber" : 15,
        "x" : 1.6064641342317396,
        "y" : -13.463758583010392
      }, {
        "sequenceNumber" : 16,
        "x" : 1.6321051598727652,
        "y" : -13.298323854860396
      }, {
        "sequenceNumber" : 17,
        "x" : 1.6577461855137907,
        "y" : -13.060724972599647
      }, {
        "sequenceNumber" : 18,
        "x" : 1.6833872111548163,
        "y" : -12.719993325156391
      }, {
        "sequenceNumber" : 19,
        "x" : 1.7090282367958418,
        "y" : -12.23240955273674
      }, {
        "sequenceNumber" : 20,
        "x" : 1.7346692624368674,
        "y" : -11.536818586951311
      }, {
        "sequenceNumber" : 21,
        "x" : 1.760310288077893,
        "y" : -10.548812758191803
      }, {
        "sequenceNumber" : 22,
        "x" : 1.7859513137189185,
        "y" : -9.154147184069476
      }, {
        "sequenceNumber" : 23,
        "x" : 1.811592339359944,
        "y" : -7.2025901723954995
      }, {
        "sequenceNumber" : 24,
        "x" : 1.8372333650009696,
        "y" : -4.504943161063398
      }, {
        "sequenceNumber" : 25,
        "x" : 1.8628743906419951,
        "y" : -0.8382908518062457
      }, {
        "sequenceNumber" : 26,
        "x" : 1.8885154162830207,
        "y" : 4.032907150958623
      }, {
        "sequenceNumber" : 27,
        "x" : 1.9141564419240462,
        "y" : 10.311664713622564
      }, {
        "sequenceNumber" : 28,
        "x" : 1.9397974675650718,
        "y" : 18.096740164480117
      }, {
        "sequenceNumber" : 29,
        "x" : 1.9654384932060973,
        "y" : 27.298192091114203
      }, {
        "sequenceNumber" : 30,
        "x" : 1.9910795188471229,
        "y" : 37.57777890248005
      }, {
        "sequenceNumber" : 31,
        "x" : 2.0167205444881486,
        "y" : 48.36330382363119
      }, {
        "sequenceNumber" : 32,
        "x" : 2.042361570129174,
        "y" : 58.95962333355621
      }, {
        "sequenceNumber" : 33,
        "x" : 2.0680025957701997,
        "y" : 68.71854803615285
      }, {
        "sequenceNumber" : 34,
        "x" : 2.0936436214112253,
        "y" : 77.18565928155752
      }, {
        "sequenceNumber" : 35,
        "x" : 2.119284647052251,
        "y" : 84.1599121724818
      }, {
        "sequenceNumber" : 36,
        "x" : 2.1449256726932764,
        "y" : 89.66272254074455
      }, {
        "sequenceNumber" : 37,
        "x" : 2.170566698334302,
        "y" : 93.8590536945151
      }, {
        "sequenceNumber" : 38,
        "x" : 2.1962077239753275,
        "y" : 96.97666615265582
      }, {
        "sequenceNumber" : 39,
        "x" : 2.221848749616353,
        "y" : 99.24823966690941
      } ],
      "fitEquation" : "HILLEQUATION",
      "fitParameterMap" : {
        "Min" : {
          "value" : -13.840512511891932,
          "name" : "Min",
          "status" : "FLOAT"
        },
        "Max" : {
          "value" : 104.75942895131577,
          "name" : "Max",
          "status" : "FLOAT"
        },
        "EC50" : {
          "value" : 2.009842940711834,
          "name" : "EC50",
          "status" : "FLOAT"
        },
        "Slope" : {
          "value" : 6.189330809093426,
          "name" : "Slope",
          "status" : "FLOAT"
        }
      }
    }, {
      "id" : 58,
      "compound" : {
        "id" : 177,
        "name" : "C1397-1103"
      },
      "experiment" : {
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
      },
      "experimentPoints" : [ {
        "x" : 16.666666666666668,
        "logx" : 1.2218487496163564,
        "y" : -83.14615683830608,
        "plateId" : 1,
        "doseId" : 2,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 33.333333333333336,
        "logx" : 1.5228787452803376,
        "y" : -53.922784038372264,
        "plateId" : 1,
        "doseId" : 3,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 50.0,
        "logx" : 1.6989700043360187,
        "y" : -16.439176060312853,
        "plateId" : 1,
        "doseId" : 4,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 66.66666666666667,
        "logx" : 1.8239087409443189,
        "y" : -13.491331529425151,
        "plateId" : 1,
        "doseId" : 5,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 83.33333333333334,
        "logx" : 1.9208187539523751,
        "y" : -5.673453085391212,
        "plateId" : 1,
        "doseId" : 6,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 100.0,
        "logx" : 2.0,
        "y" : 19.426561683296104,
        "plateId" : 1,
        "doseId" : 7,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 116.66666666666667,
        "logx" : 2.066946789630613,
        "y" : 53.80635556965739,
        "plateId" : 1,
        "doseId" : 8,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 133.33333333333334,
        "logx" : 2.1249387366083,
        "y" : 14.369716546540204,
        "plateId" : 1,
        "doseId" : 9,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 150.0,
        "logx" : 2.1760912590556813,
        "y" : 58.20948161977043,
        "plateId" : 1,
        "doseId" : 10,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 166.66666666666669,
        "logx" : 2.2218487496163566,
        "y" : 57.72583335747113,
        "plateId" : 1,
        "doseId" : 11,
        "resultStatus" : "INCLUDED"
      } ],
      "curveFitPoints" : [ {
        "sequenceNumber" : 0,
        "x" : 1.2218487496163564,
        "y" : 0.0
      }, {
        "sequenceNumber" : 1,
        "x" : 1.247489775257382,
        "y" : 0.0
      }, {
        "sequenceNumber" : 2,
        "x" : 1.2731308008984075,
        "y" : 0.0
      }, {
        "sequenceNumber" : 3,
        "x" : 1.298771826539433,
        "y" : 0.0
      }, {
        "sequenceNumber" : 4,
        "x" : 1.3244128521804586,
        "y" : 0.0
      }, {
        "sequenceNumber" : 5,
        "x" : 1.3500538778214841,
        "y" : 0.0
      }, {
        "sequenceNumber" : 6,
        "x" : 1.3756949034625097,
        "y" : 0.0
      }, {
        "sequenceNumber" : 7,
        "x" : 1.4013359291035352,
        "y" : 0.0
      }, {
        "sequenceNumber" : 8,
        "x" : 1.4269769547445608,
        "y" : 0.0
      }, {
        "sequenceNumber" : 9,
        "x" : 1.4526179803855863,
        "y" : 0.0
      }, {
        "sequenceNumber" : 10,
        "x" : 1.4782590060266119,
        "y" : 0.0
      }, {
        "sequenceNumber" : 11,
        "x" : 1.5039000316676374,
        "y" : 0.0
      }, {
        "sequenceNumber" : 12,
        "x" : 1.529541057308663,
        "y" : 0.0
      }, {
        "sequenceNumber" : 13,
        "x" : 1.5551820829496885,
        "y" : 0.0
      }, {
        "sequenceNumber" : 14,
        "x" : 1.580823108590714,
        "y" : 0.0
      }, {
        "sequenceNumber" : 15,
        "x" : 1.6064641342317396,
        "y" : 0.0
      }, {
        "sequenceNumber" : 16,
        "x" : 1.6321051598727652,
        "y" : 0.0
      }, {
        "sequenceNumber" : 17,
        "x" : 1.6577461855137907,
        "y" : 0.0
      }, {
        "sequenceNumber" : 18,
        "x" : 1.6833872111548163,
        "y" : 0.0
      }, {
        "sequenceNumber" : 19,
        "x" : 1.7090282367958418,
        "y" : 0.0
      }, {
        "sequenceNumber" : 20,
        "x" : 1.7346692624368674,
        "y" : 0.0
      }, {
        "sequenceNumber" : 21,
        "x" : 1.760310288077893,
        "y" : 0.0
      }, {
        "sequenceNumber" : 22,
        "x" : 1.7859513137189185,
        "y" : 0.0
      }, {
        "sequenceNumber" : 23,
        "x" : 1.811592339359944,
        "y" : 0.0
      }, {
        "sequenceNumber" : 24,
        "x" : 1.8372333650009696,
        "y" : 0.0
      }, {
        "sequenceNumber" : 25,
        "x" : 1.8628743906419951,
        "y" : 0.0
      }, {
        "sequenceNumber" : 26,
        "x" : 1.8885154162830207,
        "y" : 0.0
      }, {
        "sequenceNumber" : 27,
        "x" : 1.9141564419240462,
        "y" : 0.0
      }, {
        "sequenceNumber" : 28,
        "x" : 1.9397974675650718,
        "y" : 0.0
      }, {
        "sequenceNumber" : 29,
        "x" : 1.9654384932060973,
        "y" : 0.0
      }, {
        "sequenceNumber" : 30,
        "x" : 1.9910795188471229,
        "y" : 0.0
      }, {
        "sequenceNumber" : 31,
        "x" : 2.0167205444881486,
        "y" : 0.0
      }, {
        "sequenceNumber" : 32,
        "x" : 2.042361570129174,
        "y" : 0.0
      }, {
        "sequenceNumber" : 33,
        "x" : 2.0680025957701997,
        "y" : 0.0
      }, {
        "sequenceNumber" : 34,
        "x" : 2.0936436214112253,
        "y" : 0.0
      }, {
        "sequenceNumber" : 35,
        "x" : 2.119284647052251,
        "y" : 0.0
      }, {
        "sequenceNumber" : 36,
        "x" : 2.1449256726932764,
        "y" : 0.0
      }, {
        "sequenceNumber" : 37,
        "x" : 2.170566698334302,
        "y" : 0.0
      }, {
        "sequenceNumber" : 38,
        "x" : 2.1962077239753275,
        "y" : 0.0
      }, {
        "sequenceNumber" : 39,
        "x" : 2.221848749616353,
        "y" : 0.0
      } ],
      "fitEquation" : "HILLEQUATION",
      "fitParameterMap" : { }
    }, {
      "id" : 59,
      "compound" : {
        "id" : 178,
        "name" : "C1085-5"
      },
      "experiment" : {
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
      },
      "experimentPoints" : [ {
        "x" : 16.666666666666668,
        "logx" : 1.2218487496163564,
        "y" : -69.54182234891537,
        "plateId" : 1,
        "doseId" : 14,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 33.333333333333336,
        "logx" : 1.5228787452803376,
        "y" : -57.53780268275723,
        "plateId" : 1,
        "doseId" : 15,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 50.0,
        "logx" : 1.6989700043360187,
        "y" : -30.43130278006194,
        "plateId" : 1,
        "doseId" : 16,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 66.66666666666667,
        "logx" : 1.8239087409443189,
        "y" : 4.939681430849988,
        "plateId" : 1,
        "doseId" : 17,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 83.33333333333334,
        "logx" : 1.9208187539523751,
        "y" : -30.574733462928787,
        "plateId" : 1,
        "doseId" : 18,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 100.0,
        "logx" : 2.0,
        "y" : 2.6346980851317947,
        "plateId" : 1,
        "doseId" : 19,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 116.66666666666667,
        "logx" : 2.066946789630613,
        "y" : 61.48406542872398,
        "plateId" : 1,
        "doseId" : 20,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 133.33333333333334,
        "logx" : 2.1249387366083,
        "y" : 72.2243678299332,
        "plateId" : 1,
        "doseId" : 21,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 150.0,
        "logx" : 2.1760912590556813,
        "y" : 92.4743447967696,
        "plateId" : 1,
        "doseId" : 22,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 166.66666666666669,
        "logx" : 2.2218487496163566,
        "y" : 52.64189484918632,
        "plateId" : 1,
        "doseId" : 23,
        "resultStatus" : "INCLUDED"
      } ],
      "curveFitPoints" : [ {
        "sequenceNumber" : 0,
        "x" : 1.2218487496163564,
        "y" : 0.0
      }, {
        "sequenceNumber" : 1,
        "x" : 1.247489775257382,
        "y" : 0.0
      }, {
        "sequenceNumber" : 2,
        "x" : 1.2731308008984075,
        "y" : 0.0
      }, {
        "sequenceNumber" : 3,
        "x" : 1.298771826539433,
        "y" : 0.0
      }, {
        "sequenceNumber" : 4,
        "x" : 1.3244128521804586,
        "y" : 0.0
      }, {
        "sequenceNumber" : 5,
        "x" : 1.3500538778214841,
        "y" : 0.0
      }, {
        "sequenceNumber" : 6,
        "x" : 1.3756949034625097,
        "y" : 0.0
      }, {
        "sequenceNumber" : 7,
        "x" : 1.4013359291035352,
        "y" : 0.0
      }, {
        "sequenceNumber" : 8,
        "x" : 1.4269769547445608,
        "y" : 0.0
      }, {
        "sequenceNumber" : 9,
        "x" : 1.4526179803855863,
        "y" : 0.0
      }, {
        "sequenceNumber" : 10,
        "x" : 1.4782590060266119,
        "y" : 0.0
      }, {
        "sequenceNumber" : 11,
        "x" : 1.5039000316676374,
        "y" : 0.0
      }, {
        "sequenceNumber" : 12,
        "x" : 1.529541057308663,
        "y" : 0.0
      }, {
        "sequenceNumber" : 13,
        "x" : 1.5551820829496885,
        "y" : 0.0
      }, {
        "sequenceNumber" : 14,
        "x" : 1.580823108590714,
        "y" : 0.0
      }, {
        "sequenceNumber" : 15,
        "x" : 1.6064641342317396,
        "y" : 0.0
      }, {
        "sequenceNumber" : 16,
        "x" : 1.6321051598727652,
        "y" : 0.0
      }, {
        "sequenceNumber" : 17,
        "x" : 1.6577461855137907,
        "y" : 0.0
      }, {
        "sequenceNumber" : 18,
        "x" : 1.6833872111548163,
        "y" : 0.0
      }, {
        "sequenceNumber" : 19,
        "x" : 1.7090282367958418,
        "y" : 0.0
      }, {
        "sequenceNumber" : 20,
        "x" : 1.7346692624368674,
        "y" : 0.0
      }, {
        "sequenceNumber" : 21,
        "x" : 1.760310288077893,
        "y" : 0.0
      }, {
        "sequenceNumber" : 22,
        "x" : 1.7859513137189185,
        "y" : 0.0
      }, {
        "sequenceNumber" : 23,
        "x" : 1.811592339359944,
        "y" : 0.0
      }, {
        "sequenceNumber" : 24,
        "x" : 1.8372333650009696,
        "y" : 0.0
      }, {
        "sequenceNumber" : 25,
        "x" : 1.8628743906419951,
        "y" : 0.0
      }, {
        "sequenceNumber" : 26,
        "x" : 1.8885154162830207,
        "y" : 0.0
      }, {
        "sequenceNumber" : 27,
        "x" : 1.9141564419240462,
        "y" : 0.0
      }, {
        "sequenceNumber" : 28,
        "x" : 1.9397974675650718,
        "y" : 0.0
      }, {
        "sequenceNumber" : 29,
        "x" : 1.9654384932060973,
        "y" : 0.0
      }, {
        "sequenceNumber" : 30,
        "x" : 1.9910795188471229,
        "y" : 0.0
      }, {
        "sequenceNumber" : 31,
        "x" : 2.0167205444881486,
        "y" : 0.0
      }, {
        "sequenceNumber" : 32,
        "x" : 2.042361570129174,
        "y" : 0.0
      }, {
        "sequenceNumber" : 33,
        "x" : 2.0680025957701997,
        "y" : 0.0
      }, {
        "sequenceNumber" : 34,
        "x" : 2.0936436214112253,
        "y" : 0.0
      }, {
        "sequenceNumber" : 35,
        "x" : 2.119284647052251,
        "y" : 0.0
      }, {
        "sequenceNumber" : 36,
        "x" : 2.1449256726932764,
        "y" : 0.0
      }, {
        "sequenceNumber" : 37,
        "x" : 2.170566698334302,
        "y" : 0.0
      }, {
        "sequenceNumber" : 38,
        "x" : 2.1962077239753275,
        "y" : 0.0
      }, {
        "sequenceNumber" : 39,
        "x" : 2.221848749616353,
        "y" : 0.0
      } ],
      "fitEquation" : "HILLEQUATION",
      "fitParameterMap" : { }
    }, {
      "id" : 60,
      "compound" : {
        "id" : 179,
        "name" : "C657-1357"
      },
      "experiment" : {
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
      },
      "experimentPoints" : [ {
        "x" : 16.666666666666668,
        "logx" : 1.2218487496163564,
        "y" : -30.547818778926068,
        "plateId" : 1,
        "doseId" : 26,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 33.333333333333336,
        "logx" : 1.5228787452803376,
        "y" : -32.918857677822565,
        "plateId" : 1,
        "doseId" : 27,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 50.0,
        "logx" : 1.6989700043360187,
        "y" : -4.055151505652808,
        "plateId" : 1,
        "doseId" : 28,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 66.66666666666667,
        "logx" : 1.8239087409443189,
        "y" : 2.450032846193708,
        "plateId" : 1,
        "doseId" : 29,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 83.33333333333334,
        "logx" : 1.9208187539523751,
        "y" : -5.308737637501102,
        "plateId" : 1,
        "doseId" : 30,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 100.0,
        "logx" : 2.0,
        "y" : 48.872604918703026,
        "plateId" : 1,
        "doseId" : 31,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 116.66666666666667,
        "logx" : 2.066946789630613,
        "y" : 65.98475535226417,
        "plateId" : 1,
        "doseId" : 32,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 133.33333333333334,
        "logx" : 2.1249387366083,
        "y" : 84.48234204609255,
        "plateId" : 1,
        "doseId" : 33,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 150.0,
        "logx" : 2.1760912590556813,
        "y" : 91.9167538840209,
        "plateId" : 1,
        "doseId" : 34,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 166.66666666666669,
        "logx" : 2.2218487496163566,
        "y" : 96.71348904985355,
        "plateId" : 1,
        "doseId" : 35,
        "resultStatus" : "INCLUDED"
      } ],
      "curveFitPoints" : [ {
        "sequenceNumber" : 0,
        "x" : 1.2218487496163564,
        "y" : -24.732393426989002
      }, {
        "sequenceNumber" : 1,
        "x" : 1.247489775257382,
        "y" : -24.722261394999034
      }, {
        "sequenceNumber" : 2,
        "x" : 1.2731308008984075,
        "y" : -24.708954368249344
      }, {
        "sequenceNumber" : 3,
        "x" : 1.298771826539433,
        "y" : -24.691478363474623
      }, {
        "sequenceNumber" : 4,
        "x" : 1.3244128521804586,
        "y" : -24.668528896093026
      }, {
        "sequenceNumber" : 5,
        "x" : 1.3500538778214841,
        "y" : -24.638394479138015
      }, {
        "sequenceNumber" : 6,
        "x" : 1.3756949034625097,
        "y" : -24.598830488199088
      }, {
        "sequenceNumber" : 7,
        "x" : 1.4013359291035352,
        "y" : -24.546894551791393
      }, {
        "sequenceNumber" : 8,
        "x" : 1.4269769547445608,
        "y" : -24.47873217860614
      }, {
        "sequenceNumber" : 9,
        "x" : 1.4526179803855863,
        "y" : -24.389298348003596
      }, {
        "sequenceNumber" : 10,
        "x" : 1.4782590060266119,
        "y" : -24.271997260742374
      }, {
        "sequenceNumber" : 11,
        "x" : 1.5039000316676374,
        "y" : -24.118218473481505
      }, {
        "sequenceNumber" : 12,
        "x" : 1.529541057308663,
        "y" : -23.916743534625958
      }, {
        "sequenceNumber" : 13,
        "x" : 1.5551820829496885,
        "y" : -23.652993700310663
      }, {
        "sequenceNumber" : 14,
        "x" : 1.580823108590714,
        "y" : -23.30808771388047
      }, {
        "sequenceNumber" : 15,
        "x" : 1.6064641342317396,
        "y" : -22.85768148106634
      }, {
        "sequenceNumber" : 16,
        "x" : 1.6321051598727652,
        "y" : -22.270573044423386
      }, {
        "sequenceNumber" : 17,
        "x" : 1.6577461855137907,
        "y" : -21.507083463993844
      }, {
        "sequenceNumber" : 18,
        "x" : 1.6833872111548163,
        "y" : -20.517277489513873
      }, {
        "sequenceNumber" : 19,
        "x" : 1.7090282367958418,
        "y" : -19.239181669821576
      }, {
        "sequenceNumber" : 20,
        "x" : 1.7346692624368674,
        "y" : -17.59730885057046
      }, {
        "sequenceNumber" : 21,
        "x" : 1.760310288077893,
        "y" : -15.502020998936485
      }, {
        "sequenceNumber" : 22,
        "x" : 1.7859513137189185,
        "y" : -12.850554196960616
      }, {
        "sequenceNumber" : 23,
        "x" : 1.811592339359944,
        "y" : -9.530843745853163
      }, {
        "sequenceNumber" : 24,
        "x" : 1.8372333650009696,
        "y" : -5.429492506096764
      }, {
        "sequenceNumber" : 25,
        "x" : 1.8628743906419951,
        "y" : -0.4450685205240781
      }, {
        "sequenceNumber" : 26,
        "x" : 1.8885154162830207,
        "y" : 5.492961886020584
      }, {
        "sequenceNumber" : 27,
        "x" : 1.9141564419240462,
        "y" : 12.40127269538102
      }, {
        "sequenceNumber" : 28,
        "x" : 1.9397974675650718,
        "y" : 20.220135596993703
      }, {
        "sequenceNumber" : 29,
        "x" : 1.9654384932060973,
        "y" : 28.79852567108941
      }, {
        "sequenceNumber" : 30,
        "x" : 1.9910795188471229,
        "y" : 37.894898318550815
      }, {
        "sequenceNumber" : 31,
        "x" : 2.0167205444881486,
        "y" : 47.1985460580576
      }, {
        "sequenceNumber" : 32,
        "x" : 2.042361570129174,
        "y" : 56.36940709506773
      }, {
        "sequenceNumber" : 33,
        "x" : 2.0680025957701997,
        "y" : 65.08633506246318
      }, {
        "sequenceNumber" : 34,
        "x" : 2.0936436214112253,
        "y" : 73.08987547433455
      }, {
        "sequenceNumber" : 35,
        "x" : 2.119284647052251,
        "y" : 80.20819087463236
      }, {
        "sequenceNumber" : 36,
        "x" : 2.1449256726932764,
        "y" : 86.36217250749306
      }, {
        "sequenceNumber" : 37,
        "x" : 2.170566698334302,
        "y" : 91.55336612257786
      }, {
        "sequenceNumber" : 38,
        "x" : 2.1962077239753275,
        "y" : 95.84242611616185
      }, {
        "sequenceNumber" : 39,
        "x" : 2.221848749616353,
        "y" : 99.3257538604433
      } ],
      "fitEquation" : "HILLEQUATION",
      "fitParameterMap" : {
        "Min" : {
          "value" : -24.764713537701542,
          "name" : "Min",
          "status" : "FLOAT"
        },
        "Max" : {
          "value" : 111.91394741373396,
          "name" : "Max",
          "status" : "FLOAT"
        },
        "EC50" : {
          "value" : 2.0067412860822675,
          "name" : "EC50",
          "status" : "FLOAT"
        },
        "Slope" : {
          "value" : 4.619900175194147,
          "name" : "Slope",
          "status" : "FLOAT"
        }
      }
    }, {
      "id" : 61,
      "compound" : {
        "id" : 180,
        "name" : "C1735-704"
      },
      "experiment" : {
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
      },
      "experimentPoints" : [ {
        "x" : 16.666666666666668,
        "logx" : 1.2218487496163564,
        "y" : -4.116690592724017,
        "plateId" : 1,
        "doseId" : 38,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 33.333333333333336,
        "logx" : 1.5228787452803376,
        "y" : -3.5858381408094426,
        "plateId" : 1,
        "doseId" : 39,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 50.0,
        "logx" : 1.6989700043360187,
        "y" : -1.1691625074050893,
        "plateId" : 1,
        "doseId" : 40,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 66.66666666666667,
        "logx" : 1.8239087409443189,
        "y" : 5.0400629500457645,
        "plateId" : 1,
        "doseId" : 41,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 83.33333333333334,
        "logx" : 1.9208187539523751,
        "y" : 21.05110707632417,
        "plateId" : 1,
        "doseId" : 42,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 100.0,
        "logx" : 2.0,
        "y" : 49.71175475354197,
        "plateId" : 1,
        "doseId" : 43,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 116.66666666666667,
        "logx" : 2.066946789630613,
        "y" : 77.95190392110636,
        "plateId" : 1,
        "doseId" : 44,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 133.33333333333334,
        "logx" : 2.1249387366083,
        "y" : 94.24428499595902,
        "plateId" : 1,
        "doseId" : 45,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 150.0,
        "logx" : 2.1760912590556813,
        "y" : 100.35582997164722,
        "plateId" : 1,
        "doseId" : 46,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 166.66666666666669,
        "logx" : 2.2218487496163566,
        "y" : 102.63137415482446,
        "plateId" : 1,
        "doseId" : 47,
        "resultStatus" : "INCLUDED"
      } ],
      "curveFitPoints" : [ {
        "sequenceNumber" : 0,
        "x" : 1.2218487496163564,
        "y" : -2.9107080893356416
      }, {
        "sequenceNumber" : 1,
        "x" : 1.247489775257382,
        "y" : -2.9104307770439424
      }, {
        "sequenceNumber" : 2,
        "x" : 1.2731308008984075,
        "y" : -2.9100171178853667
      }, {
        "sequenceNumber" : 3,
        "x" : 1.298771826539433,
        "y" : -2.9094000757393954
      }, {
        "sequenceNumber" : 4,
        "x" : 1.3244128521804586,
        "y" : -2.908479657927097
      }, {
        "sequenceNumber" : 5,
        "x" : 1.3500538778214841,
        "y" : -2.9071067158365596
      }, {
        "sequenceNumber" : 6,
        "x" : 1.3756949034625097,
        "y" : -2.905058786452825
      }, {
        "sequenceNumber" : 7,
        "x" : 1.4013359291035352,
        "y" : -2.9020040686076487
      }, {
        "sequenceNumber" : 8,
        "x" : 1.4269769547445608,
        "y" : -2.8974477159300753
      }, {
        "sequenceNumber" : 9,
        "x" : 1.4526179803855863,
        "y" : -2.890651787359158
      }, {
        "sequenceNumber" : 10,
        "x" : 1.4782590060266119,
        "y" : -2.8805159818819734
      }, {
        "sequenceNumber" : 11,
        "x" : 1.5039000316676374,
        "y" : -2.8654000509011786
      }, {
        "sequenceNumber" : 12,
        "x" : 1.529541057308663,
        "y" : -2.8428596012214413
      }, {
        "sequenceNumber" : 13,
        "x" : 1.5551820829496885,
        "y" : -2.809253568214028
      }, {
        "sequenceNumber" : 14,
        "x" : 1.580823108590714,
        "y" : -2.7591621777098445
      }, {
        "sequenceNumber" : 15,
        "x" : 1.6064641342317396,
        "y" : -2.6845264604619685
      }, {
        "sequenceNumber" : 16,
        "x" : 1.6321051598727652,
        "y" : -2.57338178619874
      }, {
        "sequenceNumber" : 17,
        "x" : 1.6577461855137907,
        "y" : -2.408006443367665
      }, {
        "sequenceNumber" : 18,
        "x" : 1.6833872111548163,
        "y" : -2.162242854767327
      }, {
        "sequenceNumber" : 19,
        "x" : 1.7090282367958418,
        "y" : -1.7976828451705218
      }, {
        "sequenceNumber" : 20,
        "x" : 1.7346692624368674,
        "y" : -1.2583693185410352
      }, {
        "sequenceNumber" : 21,
        "x" : 1.760310288077893,
        "y" : -0.4637295868734759
      }, {
        "sequenceNumber" : 22,
        "x" : 1.7859513137189185,
        "y" : 0.7002168934677617
      }, {
        "sequenceNumber" : 23,
        "x" : 1.811592339359944,
        "y" : 2.3904321367697716
      }, {
        "sequenceNumber" : 24,
        "x" : 1.8372333650009696,
        "y" : 4.8143095833001155
      }, {
        "sequenceNumber" : 25,
        "x" : 1.8628743906419951,
        "y" : 8.228584780982873
      }, {
        "sequenceNumber" : 26,
        "x" : 1.8885154162830207,
        "y" : 12.918511594242194
      }, {
        "sequenceNumber" : 27,
        "x" : 1.9141564419240462,
        "y" : 19.142985443475588
      }, {
        "sequenceNumber" : 28,
        "x" : 1.9397974675650718,
        "y" : 27.0376236202454
      }, {
        "sequenceNumber" : 29,
        "x" : 1.9654384932060973,
        "y" : 36.493802905851204
      }, {
        "sequenceNumber" : 30,
        "x" : 1.9910795188471229,
        "y" : 47.07421437970072
      }, {
        "sequenceNumber" : 31,
        "x" : 2.0167205444881486,
        "y" : 58.0466889186972
      }, {
        "sequenceNumber" : 32,
        "x" : 2.042361570129174,
        "y" : 68.56500828077893
      }, {
        "sequenceNumber" : 33,
        "x" : 2.0680025957701997,
        "y" : 77.91453730280799
      }, {
        "sequenceNumber" : 34,
        "x" : 2.0936436214112253,
        "y" : 85.68343333241238
      }, {
        "sequenceNumber" : 35,
        "x" : 2.119284647052251,
        "y" : 91.7853682157086
      }, {
        "sequenceNumber" : 36,
        "x" : 2.1449256726932764,
        "y" : 96.3694070730364
      }, {
        "sequenceNumber" : 37,
        "x" : 2.170566698334302,
        "y" : 99.69929453033748
      }, {
        "sequenceNumber" : 38,
        "x" : 2.1962077239753275,
        "y" : 102.05953972807657
      }, {
        "sequenceNumber" : 39,
        "x" : 2.221848749616353,
        "y" : 103.70355710228984
      } ],
      "fitEquation" : "HILLEQUATION",
      "fitParameterMap" : {
        "Min" : {
          "value" : -2.9112721010543576,
          "name" : "Min",
          "status" : "FLOAT"
        },
        "Max" : {
          "value" : 107.21131776379755,
          "name" : "Max",
          "status" : "FLOAT"
        },
        "EC50" : {
          "value" : 2.0029345538254693,
          "name" : "EC50",
          "status" : "FLOAT"
        },
        "Slope" : {
          "value" : 6.773373726874846,
          "name" : "Slope",
          "status" : "FLOAT"
        }
      }
    }, {
      "id" : 62,
      "compound" : {
        "id" : 181,
        "name" : "C1942-1599"
      },
      "experiment" : {
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
      },
      "experimentPoints" : [ {
        "x" : 16.666666666666668,
        "logx" : 1.2218487496163564,
        "y" : -38.73376843740996,
        "plateId" : 1,
        "doseId" : 50,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 33.333333333333336,
        "logx" : 1.5228787452803376,
        "y" : -34.11583307170866,
        "plateId" : 1,
        "doseId" : 51,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 50.0,
        "logx" : 1.6989700043360187,
        "y" : -30.773245897124195,
        "plateId" : 1,
        "doseId" : 52,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 66.66666666666667,
        "logx" : 1.8239087409443189,
        "y" : -37.352067804670966,
        "plateId" : 1,
        "doseId" : 53,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 83.33333333333334,
        "logx" : 1.9208187539523751,
        "y" : -16.153603141349258,
        "plateId" : 1,
        "doseId" : 54,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 100.0,
        "logx" : 2.0,
        "y" : 8.536329381714335,
        "plateId" : 1,
        "doseId" : 55,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 116.66666666666667,
        "logx" : 2.066946789630613,
        "y" : 55.77347838360806,
        "plateId" : 1,
        "doseId" : 56,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 133.33333333333334,
        "logx" : 2.1249387366083,
        "y" : 69.81539557941899,
        "plateId" : 1,
        "doseId" : 57,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 150.0,
        "logx" : 2.1760912590556813,
        "y" : 73.94090437236271,
        "plateId" : 1,
        "doseId" : 58,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 166.66666666666669,
        "logx" : 2.2218487496163566,
        "y" : 72.69707726067048,
        "plateId" : 1,
        "doseId" : 59,
        "resultStatus" : "INCLUDED"
      } ],
      "curveFitPoints" : [ {
        "sequenceNumber" : 0,
        "x" : 1.2218487496163564,
        "y" : -34.592595634034616
      }, {
        "sequenceNumber" : 1,
        "x" : 1.247489775257382,
        "y" : -34.59259484382503
      }, {
        "sequenceNumber" : 2,
        "x" : 1.2731308008984075,
        "y" : -34.59259339948784
      }, {
        "sequenceNumber" : 3,
        "x" : 1.298771826539433,
        "y" : -34.59259075954279
      }, {
        "sequenceNumber" : 4,
        "x" : 1.3244128521804586,
        "y" : -34.592585934277814
      }, {
        "sequenceNumber" : 5,
        "x" : 1.3500538778214841,
        "y" : -34.592577114707595
      }, {
        "sequenceNumber" : 6,
        "x" : 1.3756949034625097,
        "y" : -34.59256099438851
      }, {
        "sequenceNumber" : 7,
        "x" : 1.4013359291035352,
        "y" : -34.59253152984183
      }, {
        "sequenceNumber" : 8,
        "x" : 1.4269769547445608,
        "y" : -34.59247767487713
      }, {
        "sequenceNumber" : 9,
        "x" : 1.4526179803855863,
        "y" : -34.5923792394441
      }, {
        "sequenceNumber" : 10,
        "x" : 1.4782590060266119,
        "y" : -34.59219932059212
      }, {
        "sequenceNumber" : 11,
        "x" : 1.5039000316676374,
        "y" : -34.59187046821857
      }, {
        "sequenceNumber" : 12,
        "x" : 1.529541057308663,
        "y" : -34.59126940018033
      }, {
        "sequenceNumber" : 13,
        "x" : 1.5551820829496885,
        "y" : -34.59017079089915
      }, {
        "sequenceNumber" : 14,
        "x" : 1.580823108590714,
        "y" : -34.58816282024051
      }, {
        "sequenceNumber" : 15,
        "x" : 1.6064641342317396,
        "y" : -34.58449286004458
      }, {
        "sequenceNumber" : 16,
        "x" : 1.6321051598727652,
        "y" : -34.577785573308816
      }, {
        "sequenceNumber" : 17,
        "x" : 1.6577461855137907,
        "y" : -34.56552816562356
      }, {
        "sequenceNumber" : 18,
        "x" : 1.6833872111548163,
        "y" : -34.54313122519152
      }, {
        "sequenceNumber" : 19,
        "x" : 1.7090282367958418,
        "y" : -34.502217781457134
      }, {
        "sequenceNumber" : 20,
        "x" : 1.7346692624368674,
        "y" : -34.42751487757487
      }, {
        "sequenceNumber" : 21,
        "x" : 1.760310288077893,
        "y" : -34.29123458087032
      }, {
        "sequenceNumber" : 22,
        "x" : 1.7859513137189185,
        "y" : -34.04301089665396
      }, {
        "sequenceNumber" : 23,
        "x" : 1.811592339359944,
        "y" : -33.59218827610062
      }, {
        "sequenceNumber" : 24,
        "x" : 1.8372333650009696,
        "y" : -32.77766250783916
      }, {
        "sequenceNumber" : 25,
        "x" : 1.8628743906419951,
        "y" : -31.319777761044215
      }, {
        "sequenceNumber" : 26,
        "x" : 1.8885154162830207,
        "y" : -28.75373164649858
      }, {
        "sequenceNumber" : 27,
        "x" : 1.9141564419240462,
        "y" : -24.367640819307805
      }, {
        "sequenceNumber" : 28,
        "x" : 1.9397974675650718,
        "y" : -17.233267853219814
      }, {
        "sequenceNumber" : 29,
        "x" : 1.9654384932060973,
        "y" : -6.514864835238054
      }, {
        "sequenceNumber" : 30,
        "x" : 1.9910795188471229,
        "y" : 7.808643464179816
      }, {
        "sequenceNumber" : 31,
        "x" : 2.0167205444881486,
        "y" : 24.224558133189767
      }, {
        "sequenceNumber" : 32,
        "x" : 2.042361570129174,
        "y" : 40.031105143086165
      }, {
        "sequenceNumber" : 33,
        "x" : 2.0680025957701997,
        "y" : 52.89432948005741
      }, {
        "sequenceNumber" : 34,
        "x" : 2.0936436214112253,
        "y" : 62.00413668158218
      }, {
        "sequenceNumber" : 35,
        "x" : 2.119284647052251,
        "y" : 67.83961227630586
      }, {
        "sequenceNumber" : 36,
        "x" : 2.1449256726932764,
        "y" : 71.34084175992244
      }, {
        "sequenceNumber" : 37,
        "x" : 2.170566698334302,
        "y" : 73.35962321637332
      }, {
        "sequenceNumber" : 38,
        "x" : 2.1962077239753275,
        "y" : 74.49702356900205
      }, {
        "sequenceNumber" : 39,
        "x" : 2.221848749616353,
        "y" : 75.12950765134747
      } ],
      "fitEquation" : "HILLEQUATION",
      "fitParameterMap" : {
        "Min" : {
          "value" : -34.592596588636084,
          "name" : "Min",
          "status" : "FLOAT"
        },
        "Max" : {
          "value" : 75.90342145958493,
          "name" : "Max",
          "status" : "FLOAT"
        },
        "EC50" : {
          "value" : 2.0112197744962517,
          "name" : "EC50",
          "status" : "FLOAT"
        },
        "Slope" : {
          "value" : 10.2151260584858,
          "name" : "Slope",
          "status" : "FLOAT"
        }
      }
    }, {
      "id" : 63,
      "compound" : {
        "id" : 182,
        "name" : "C1181-755"
      },
      "experiment" : {
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
      },
      "experimentPoints" : [ {
        "x" : 16.666666666666668,
        "logx" : 1.2218487496163564,
        "y" : -21.972576895326217,
        "plateId" : 1,
        "doseId" : 62,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 33.333333333333336,
        "logx" : 1.5228787452803376,
        "y" : -8.332043826467572,
        "plateId" : 1,
        "doseId" : 63,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 50.0,
        "logx" : 1.6989700043360187,
        "y" : -9.766281997871271,
        "plateId" : 1,
        "doseId" : 64,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 66.66666666666667,
        "logx" : 1.8239087409443189,
        "y" : -7.9428395599977275,
        "plateId" : 1,
        "doseId" : 65,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 83.33333333333334,
        "logx" : 1.9208187539523751,
        "y" : 9.576462440987262,
        "plateId" : 1,
        "doseId" : 66,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 100.0,
        "logx" : 2.0,
        "y" : 39.34439335466496,
        "plateId" : 1,
        "doseId" : 67,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 116.66666666666667,
        "logx" : 2.066946789630613,
        "y" : 73.06429962911125,
        "plateId" : 1,
        "doseId" : 68,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 133.33333333333334,
        "logx" : 2.1249387366083,
        "y" : 92.59534687483495,
        "plateId" : 1,
        "doseId" : 69,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 150.0,
        "logx" : 2.1760912590556813,
        "y" : 81.96318146214495,
        "plateId" : 1,
        "doseId" : 70,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 166.66666666666669,
        "logx" : 2.2218487496163566,
        "y" : 84.62269813241903,
        "plateId" : 1,
        "doseId" : 71,
        "resultStatus" : "INCLUDED"
      } ],
      "curveFitPoints" : [ {
        "sequenceNumber" : 0,
        "x" : 1.2218487496163564,
        "y" : -12.486350313965028
      }, {
        "sequenceNumber" : 1,
        "x" : 1.247489775257382,
        "y" : -12.486344216549002
      }, {
        "sequenceNumber" : 2,
        "x" : 1.2731308008984075,
        "y" : -12.486333710127008
      }, {
        "sequenceNumber" : 3,
        "x" : 1.298771826539433,
        "y" : -12.48631560657469
      }, {
        "sequenceNumber" : 4,
        "x" : 1.3244128521804586,
        "y" : -12.486284412459355
      }, {
        "sequenceNumber" : 5,
        "x" : 1.3500538778214841,
        "y" : -12.486230662097459
      }, {
        "sequenceNumber" : 6,
        "x" : 1.3756949034625097,
        "y" : -12.486138045281152
      }, {
        "sequenceNumber" : 7,
        "x" : 1.4013359291035352,
        "y" : -12.485978458156792
      }, {
        "sequenceNumber" : 8,
        "x" : 1.4269769547445608,
        "y" : -12.485703475649686
      }, {
        "sequenceNumber" : 9,
        "x" : 1.4526179803855863,
        "y" : -12.485229658324924
      }, {
        "sequenceNumber" : 10,
        "x" : 1.4782590060266119,
        "y" : -12.484413236735138
      }, {
        "sequenceNumber" : 11,
        "x" : 1.5039000316676374,
        "y" : -12.483006496140822
      }, {
        "sequenceNumber" : 12,
        "x" : 1.529541057308663,
        "y" : -12.480582641017776
      }, {
        "sequenceNumber" : 13,
        "x" : 1.5551820829496885,
        "y" : -12.476406382209904
      }, {
        "sequenceNumber" : 14,
        "x" : 1.580823108590714,
        "y" : -12.469211102802431
      }, {
        "sequenceNumber" : 15,
        "x" : 1.6064641342317396,
        "y" : -12.45681535809362
      }, {
        "sequenceNumber" : 16,
        "x" : 1.6321051598727652,
        "y" : -12.435463445907208
      }, {
        "sequenceNumber" : 17,
        "x" : 1.6577461855137907,
        "y" : -12.398693229416361
      }, {
        "sequenceNumber" : 18,
        "x" : 1.6833872111548163,
        "y" : -12.335397378446219
      }, {
        "sequenceNumber" : 19,
        "x" : 1.7090282367958418,
        "y" : -12.226518438766202
      }, {
        "sequenceNumber" : 20,
        "x" : 1.7346692624368674,
        "y" : -12.039459401702869
      }, {
        "sequenceNumber" : 21,
        "x" : 1.760310288077893,
        "y" : -11.71876101979047
      }, {
        "sequenceNumber" : 22,
        "x" : 1.7859513137189185,
        "y" : -11.170932862463502
      }, {
        "sequenceNumber" : 23,
        "x" : 1.811592339359944,
        "y" : -10.240869391713597
      }, {
        "sequenceNumber" : 24,
        "x" : 1.8372333650009696,
        "y" : -8.678291171646514
      }, {
        "sequenceNumber" : 25,
        "x" : 1.8628743906419951,
        "y" : -6.098575387188931
      }, {
        "sequenceNumber" : 26,
        "x" : 1.8885154162830207,
        "y" : -1.9602288339731722
      }, {
        "sequenceNumber" : 27,
        "x" : 1.9141564419240462,
        "y" : 4.38196524871492
      }, {
        "sequenceNumber" : 28,
        "x" : 1.9397974675650718,
        "y" : 13.451847784258852
      }, {
        "sequenceNumber" : 29,
        "x" : 1.9654384932060973,
        "y" : 25.217123438565405
      }, {
        "sequenceNumber" : 30,
        "x" : 1.9910795188471229,
        "y" : 38.688405512749114
      }, {
        "sequenceNumber" : 31,
        "x" : 2.0167205444881486,
        "y" : 52.07581424233942
      }, {
        "sequenceNumber" : 32,
        "x" : 2.042361570129174,
        "y" : 63.632203598956906
      }, {
        "sequenceNumber" : 33,
        "x" : 2.0680025957701997,
        "y" : 72.45608571796166
      }, {
        "sequenceNumber" : 34,
        "x" : 2.0936436214112253,
        "y" : 78.58285647412444
      }, {
        "sequenceNumber" : 35,
        "x" : 2.119284647052251,
        "y" : 82.56154876385233
      }, {
        "sequenceNumber" : 36,
        "x" : 2.1449256726932764,
        "y" : 85.034159057964
      }, {
        "sequenceNumber" : 37,
        "x" : 2.170566698334302,
        "y" : 86.5290405749234
      }, {
        "sequenceNumber" : 38,
        "x" : 2.1962077239753275,
        "y" : 87.41780214247788
      }, {
        "sequenceNumber" : 39,
        "x" : 2.221848749616353,
        "y" : 87.94095098414462
      } ],
      "fitEquation" : "HILLEQUATION",
      "fitParameterMap" : {
        "Min" : {
          "value" : -12.486358746358466,
          "name" : "Min",
          "status" : "FLOAT"
        },
        "Max" : {
          "value" : 88.67353090308625,
          "name" : "Max",
          "status" : "FLOAT"
        },
        "EC50" : {
          "value" : 1.9899711199020622,
          "name" : "EC50",
          "status" : "FLOAT"
        },
        "Slope" : {
          "value" : 9.216054268110662,
          "name" : "Slope",
          "status" : "FLOAT"
        }
      }
    }, {
      "id" : 64,
      "compound" : {
        "id" : 183,
        "name" : "C1042-30"
      },
      "experiment" : {
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
      },
      "experimentPoints" : [ {
        "x" : 16.666666666666668,
        "logx" : 1.2218487496163564,
        "y" : -30.311064399946403,
        "plateId" : 1,
        "doseId" : 74,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 33.333333333333336,
        "logx" : 1.5228787452803376,
        "y" : -4.959191840694093,
        "plateId" : 1,
        "doseId" : 75,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 50.0,
        "logx" : 1.6989700043360187,
        "y" : -32.016754531969,
        "plateId" : 1,
        "doseId" : 76,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 66.66666666666667,
        "logx" : 1.8239087409443189,
        "y" : -28.58831422931159,
        "plateId" : 1,
        "doseId" : 77,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 83.33333333333334,
        "logx" : 1.9208187539523751,
        "y" : -1.081238271684381,
        "plateId" : 1,
        "doseId" : 78,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 100.0,
        "logx" : 2.0,
        "y" : 46.289414282802376,
        "plateId" : 1,
        "doseId" : 79,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 116.66666666666667,
        "logx" : 2.066946789630613,
        "y" : 43.204224009331945,
        "plateId" : 1,
        "doseId" : 80,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 133.33333333333334,
        "logx" : 2.1249387366083,
        "y" : 83.3453158443986,
        "plateId" : 1,
        "doseId" : 81,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 150.0,
        "logx" : 2.1760912590556813,
        "y" : 63.77619436327648,
        "plateId" : 1,
        "doseId" : 82,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 166.66666666666669,
        "logx" : 2.2218487496163566,
        "y" : 94.20868672491547,
        "plateId" : 1,
        "doseId" : 83,
        "resultStatus" : "INCLUDED"
      } ],
      "curveFitPoints" : [ {
        "sequenceNumber" : 0,
        "x" : 1.2218487496163564,
        "y" : -25.232635122793862
      }, {
        "sequenceNumber" : 1,
        "x" : 1.247489775257382,
        "y" : -25.23246515052244
      }, {
        "sequenceNumber" : 2,
        "x" : 1.2731308008984075,
        "y" : -25.23220533691003
      }, {
        "sequenceNumber" : 3,
        "x" : 1.298771826539433,
        "y" : -25.231808195822108
      }, {
        "sequenceNumber" : 4,
        "x" : 1.3244128521804586,
        "y" : -25.231201143170743
      }, {
        "sequenceNumber" : 5,
        "x" : 1.3500538778214841,
        "y" : -25.230273233312367
      }, {
        "sequenceNumber" : 6,
        "x" : 1.3756949034625097,
        "y" : -25.22885488795567
      }, {
        "sequenceNumber" : 7,
        "x" : 1.4013359291035352,
        "y" : -25.22668691798437
      }, {
        "sequenceNumber" : 8,
        "x" : 1.4269769547445608,
        "y" : -25.2233731889253
      }, {
        "sequenceNumber" : 9,
        "x" : 1.4526179803855863,
        "y" : -25.218308308355095
      }, {
        "sequenceNumber" : 10,
        "x" : 1.4782590060266119,
        "y" : -25.21056718811809
      }, {
        "sequenceNumber" : 11,
        "x" : 1.5039000316676374,
        "y" : -25.19873645918565
      }, {
        "sequenceNumber" : 12,
        "x" : 1.529541057308663,
        "y" : -25.18065730685599
      }, {
        "sequenceNumber" : 13,
        "x" : 1.5551820829496885,
        "y" : -25.153033611746185
      }, {
        "sequenceNumber" : 14,
        "x" : 1.580823108590714,
        "y" : -25.110835846703054
      }, {
        "sequenceNumber" : 15,
        "x" : 1.6064641342317396,
        "y" : -25.046396602080737
      }, {
        "sequenceNumber" : 16,
        "x" : 1.6321051598727652,
        "y" : -24.948043638581357
      }, {
        "sequenceNumber" : 17,
        "x" : 1.6577461855137907,
        "y" : -24.798046613703782
      }, {
        "sequenceNumber" : 18,
        "x" : 1.6833872111548163,
        "y" : -24.569562163044996
      }, {
        "sequenceNumber" : 19,
        "x" : 1.7090282367958418,
        "y" : -24.22215641837601
      }, {
        "sequenceNumber" : 20,
        "x" : 1.7346692624368674,
        "y" : -23.695398378263462
      }, {
        "sequenceNumber" : 21,
        "x" : 1.760310288077893,
        "y" : -22.900049280994402
      }, {
        "sequenceNumber" : 22,
        "x" : 1.7859513137189185,
        "y" : -21.706753943405133
      }, {
        "sequenceNumber" : 23,
        "x" : 1.811592339359944,
        "y" : -19.933344607654732
      }, {
        "sequenceNumber" : 24,
        "x" : 1.8372333650009696,
        "y" : -17.334695242795554
      }, {
        "sequenceNumber" : 25,
        "x" : 1.8628743906419951,
        "y" : -13.604391421453256
      }, {
        "sequenceNumber" : 26,
        "x" : 1.8885154162830207,
        "y" : -8.404895362417658
      }, {
        "sequenceNumber" : 27,
        "x" : 1.9141564419240462,
        "y" : -1.447173984917395
      }, {
        "sequenceNumber" : 28,
        "x" : 1.9397974675650718,
        "y" : 7.372087192712819
      }, {
        "sequenceNumber" : 29,
        "x" : 1.9654384932060973,
        "y" : 17.813733863979294
      }, {
        "sequenceNumber" : 30,
        "x" : 1.9910795188471229,
        "y" : 29.22254648765839
      }, {
        "sequenceNumber" : 31,
        "x" : 2.0167205444881486,
        "y" : 40.644868530593115
      }, {
        "sequenceNumber" : 32,
        "x" : 2.042361570129174,
        "y" : 51.12261333990962
      }, {
        "sequenceNumber" : 33,
        "x" : 2.0680025957701997,
        "y" : 59.99009075001585
      }, {
        "sequenceNumber" : 34,
        "x" : 2.0936436214112253,
        "y" : 66.99733781486907
      }, {
        "sequenceNumber" : 35,
        "x" : 2.119284647052251,
        "y" : 72.2404733871694
      }, {
        "sequenceNumber" : 36,
        "x" : 2.1449256726932764,
        "y" : 76.00558487049905
      }, {
        "sequenceNumber" : 37,
        "x" : 2.170566698334302,
        "y" : 78.6302120818292
      }, {
        "sequenceNumber" : 38,
        "x" : 2.1962077239753275,
        "y" : 80.42216573308741
      }, {
        "sequenceNumber" : 39,
        "x" : 2.221848749616353,
        "y" : 81.62831205865933
      } ],
      "fitEquation" : "HILLEQUATION",
      "fitParameterMap" : {
        "Min" : {
          "value" : -25.23295669384332,
          "name" : "Min",
          "status" : "FLOAT"
        },
        "Max" : {
          "value" : 83.98721019153209,
          "name" : "Max",
          "status" : "FLOAT"
        },
        "EC50" : {
          "value" : 1.991421609190875,
          "name" : "EC50",
          "status" : "FLOAT"
        },
        "Slope" : {
          "value" : 7.187135753717086,
          "name" : "Slope",
          "status" : "FLOAT"
        }
      }
    }, {
      "id" : 65,
      "compound" : {
        "id" : 184,
        "name" : "C500-1759"
      },
      "experiment" : {
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
      },
      "experimentPoints" : [ {
        "x" : 16.666666666666668,
        "logx" : 1.2218487496163564,
        "y" : -49.93662182691112,
        "plateId" : 1,
        "doseId" : 86,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 33.333333333333336,
        "logx" : 1.5228787452803376,
        "y" : -94.73727151368152,
        "plateId" : 1,
        "doseId" : 87,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 50.0,
        "logx" : 1.6989700043360187,
        "y" : -32.780369671798454,
        "plateId" : 1,
        "doseId" : 88,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 66.66666666666667,
        "logx" : 1.8239087409443189,
        "y" : -14.243787495592983,
        "plateId" : 1,
        "doseId" : 89,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 83.33333333333334,
        "logx" : 1.9208187539523751,
        "y" : -42.90990212661016,
        "plateId" : 1,
        "doseId" : 90,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 100.0,
        "logx" : 2.0,
        "y" : 36.870438885792666,
        "plateId" : 1,
        "doseId" : 91,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 116.66666666666667,
        "logx" : 2.066946789630613,
        "y" : -17.169675325710255,
        "plateId" : 1,
        "doseId" : 92,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 133.33333333333334,
        "logx" : 2.1249387366083,
        "y" : 71.5850359227387,
        "plateId" : 1,
        "doseId" : 93,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 150.0,
        "logx" : 2.1760912590556813,
        "y" : 72.9216884519027,
        "plateId" : 1,
        "doseId" : 94,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 166.66666666666669,
        "logx" : 2.2218487496163566,
        "y" : 22.797120525876625,
        "plateId" : 1,
        "doseId" : 95,
        "resultStatus" : "INCLUDED"
      } ],
      "curveFitPoints" : [ {
        "sequenceNumber" : 0,
        "x" : 1.2218487496163564,
        "y" : -29.97801996932323
      }, {
        "sequenceNumber" : 1,
        "x" : 1.247489775257382,
        "y" : -29.97801996932323
      }, {
        "sequenceNumber" : 2,
        "x" : 1.2731308008984075,
        "y" : -29.97801996932323
      }, {
        "sequenceNumber" : 3,
        "x" : 1.298771826539433,
        "y" : -29.97801996932323
      }, {
        "sequenceNumber" : 4,
        "x" : 1.3244128521804586,
        "y" : -29.97801996932323
      }, {
        "sequenceNumber" : 5,
        "x" : 1.3500538778214841,
        "y" : -29.97801996932323
      }, {
        "sequenceNumber" : 6,
        "x" : 1.3756949034625097,
        "y" : -29.97801996932323
      }, {
        "sequenceNumber" : 7,
        "x" : 1.4013359291035352,
        "y" : -29.97801996932323
      }, {
        "sequenceNumber" : 8,
        "x" : 1.4269769547445608,
        "y" : -29.97801996932323
      }, {
        "sequenceNumber" : 9,
        "x" : 1.4526179803855863,
        "y" : -29.97801996932323
      }, {
        "sequenceNumber" : 10,
        "x" : 1.4782590060266119,
        "y" : -29.97801996932323
      }, {
        "sequenceNumber" : 11,
        "x" : 1.5039000316676374,
        "y" : -29.97801996932323
      }, {
        "sequenceNumber" : 12,
        "x" : 1.529541057308663,
        "y" : -29.97801996932323
      }, {
        "sequenceNumber" : 13,
        "x" : 1.5551820829496885,
        "y" : -29.97801996932323
      }, {
        "sequenceNumber" : 14,
        "x" : 1.580823108590714,
        "y" : -29.97801996932323
      }, {
        "sequenceNumber" : 15,
        "x" : 1.6064641342317396,
        "y" : -29.97801996932323
      }, {
        "sequenceNumber" : 16,
        "x" : 1.6321051598727652,
        "y" : -29.97801996932323
      }, {
        "sequenceNumber" : 17,
        "x" : 1.6577461855137907,
        "y" : -29.97801996932323
      }, {
        "sequenceNumber" : 18,
        "x" : 1.6833872111548163,
        "y" : -29.97801996932323
      }, {
        "sequenceNumber" : 19,
        "x" : 1.7090282367958418,
        "y" : -29.97801996932323
      }, {
        "sequenceNumber" : 20,
        "x" : 1.7346692624368674,
        "y" : -29.97801996932323
      }, {
        "sequenceNumber" : 21,
        "x" : 1.760310288077893,
        "y" : -29.978019969323228
      }, {
        "sequenceNumber" : 22,
        "x" : 1.7859513137189185,
        "y" : -29.978019969323178
      }, {
        "sequenceNumber" : 23,
        "x" : 1.811592339359944,
        "y" : -29.978019969322172
      }, {
        "sequenceNumber" : 24,
        "x" : 1.8372333650009696,
        "y" : -29.97801996930219
      }, {
        "sequenceNumber" : 25,
        "x" : 1.8628743906419951,
        "y" : -29.978019968904896
      }, {
        "sequenceNumber" : 26,
        "x" : 1.8885154162830207,
        "y" : -29.978019961006726
      }, {
        "sequenceNumber" : 27,
        "x" : 1.9141564419240462,
        "y" : -29.978019803990637
      }, {
        "sequenceNumber" : 28,
        "x" : 1.9397974675650718,
        "y" : -29.978016682502176
      }, {
        "sequenceNumber" : 29,
        "x" : 1.9654384932060973,
        "y" : -29.977954627176775
      }, {
        "sequenceNumber" : 30,
        "x" : 1.9910795188471229,
        "y" : -29.976720979381266
      }, {
        "sequenceNumber" : 31,
        "x" : 2.0167205444881486,
        "y" : -29.952202130032376
      }, {
        "sequenceNumber" : 32,
        "x" : 2.042361570129174,
        "y" : -29.467179684586597
      }, {
        "sequenceNumber" : 33,
        "x" : 2.0680025957701997,
        "y" : -20.69275623281235
      }, {
        "sequenceNumber" : 34,
        "x" : 2.0936436214112253,
        "y" : 38.298086048816856
      }, {
        "sequenceNumber" : 35,
        "x" : 2.119284647052251,
        "y" : 70.36529910566371
      }, {
        "sequenceNumber" : 36,
        "x" : 2.1449256726932764,
        "y" : 72.79328836015108
      }, {
        "sequenceNumber" : 37,
        "x" : 2.170566698334302,
        "y" : 72.91852791194196
      }, {
        "sequenceNumber" : 38,
        "x" : 2.1962077239753275,
        "y" : 72.92483573421633
      }, {
        "sequenceNumber" : 39,
        "x" : 2.221848749616353,
        "y" : 72.92515304863879
      } ],
      "fitEquation" : "HILLEQUATION",
      "fitParameterMap" : {
        "Min" : {
          "value" : -29.97801996932323,
          "name" : "Min",
          "status" : "FLOAT"
        },
        "Max" : {
          "value" : 72.9251698555517,
          "name" : "Max",
          "status" : "FLOAT"
        },
        "EC50" : {
          "value" : 2.087820896208484,
          "name" : "EC50",
          "status" : "FLOAT"
        },
        "Slope" : {
          "value" : 50.638286329060136,
          "name" : "Slope",
          "status" : "FLOAT"
        }
      }
    }, {
      "id" : 66,
      "compound" : {
        "id" : 185,
        "name" : "C1818-1865"
      },
      "experiment" : {
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
      },
      "experimentPoints" : [ {
        "x" : 16.666666666666668,
        "logx" : 1.2218487496163564,
        "y" : -46.639706872305005,
        "plateId" : 2,
        "doseId" : 98,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 33.333333333333336,
        "logx" : 1.5228787452803376,
        "y" : -113.93123266922167,
        "plateId" : 2,
        "doseId" : 99,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 50.0,
        "logx" : 1.6989700043360187,
        "y" : -95.20938465577623,
        "plateId" : 2,
        "doseId" : 100,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 66.66666666666667,
        "logx" : 1.8239087409443189,
        "y" : -66.34488250556187,
        "plateId" : 2,
        "doseId" : 101,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 83.33333333333334,
        "logx" : 1.9208187539523751,
        "y" : -85.10629192947493,
        "plateId" : 2,
        "doseId" : 102,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 100.0,
        "logx" : 2.0,
        "y" : 38.521504020200716,
        "plateId" : 2,
        "doseId" : 103,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 116.66666666666667,
        "logx" : 2.066946789630613,
        "y" : 49.03002626439499,
        "plateId" : 2,
        "doseId" : 104,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 133.33333333333334,
        "logx" : 2.1249387366083,
        "y" : 37.442826139812766,
        "plateId" : 2,
        "doseId" : 105,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 150.0,
        "logx" : 2.1760912590556813,
        "y" : 87.57256529770132,
        "plateId" : 2,
        "doseId" : 106,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 166.66666666666669,
        "logx" : 2.2218487496163566,
        "y" : 10.634360620703017,
        "plateId" : 2,
        "doseId" : 107,
        "resultStatus" : "INCLUDED"
      } ],
      "curveFitPoints" : [ {
        "sequenceNumber" : 0,
        "x" : 1.2218487496163564,
        "y" : -91.83328886904958
      }, {
        "sequenceNumber" : 1,
        "x" : 1.247489775257382,
        "y" : -91.83328886904958
      }, {
        "sequenceNumber" : 2,
        "x" : 1.2731308008984075,
        "y" : -91.83328886904958
      }, {
        "sequenceNumber" : 3,
        "x" : 1.298771826539433,
        "y" : -91.83328886904958
      }, {
        "sequenceNumber" : 4,
        "x" : 1.3244128521804586,
        "y" : -91.83328886904958
      }, {
        "sequenceNumber" : 5,
        "x" : 1.3500538778214841,
        "y" : -91.83328886904958
      }, {
        "sequenceNumber" : 6,
        "x" : 1.3756949034625097,
        "y" : -91.83328886904958
      }, {
        "sequenceNumber" : 7,
        "x" : 1.4013359291035352,
        "y" : -91.83328886904958
      }, {
        "sequenceNumber" : 8,
        "x" : 1.4269769547445608,
        "y" : -91.83328886904958
      }, {
        "sequenceNumber" : 9,
        "x" : 1.4526179803855863,
        "y" : -91.83328886904958
      }, {
        "sequenceNumber" : 10,
        "x" : 1.4782590060266119,
        "y" : -91.83328886904958
      }, {
        "sequenceNumber" : 11,
        "x" : 1.5039000316676374,
        "y" : -91.83328886904955
      }, {
        "sequenceNumber" : 12,
        "x" : 1.529541057308663,
        "y" : -91.83328886904934
      }, {
        "sequenceNumber" : 13,
        "x" : 1.5551820829496885,
        "y" : -91.83328886904773
      }, {
        "sequenceNumber" : 14,
        "x" : 1.580823108590714,
        "y" : -91.83328886903547
      }, {
        "sequenceNumber" : 15,
        "x" : 1.6064641342317396,
        "y" : -91.83328886894196
      }, {
        "sequenceNumber" : 16,
        "x" : 1.6321051598727652,
        "y" : -91.8332888682286
      }, {
        "sequenceNumber" : 17,
        "x" : 1.6577461855137907,
        "y" : -91.83328886278636
      }, {
        "sequenceNumber" : 18,
        "x" : 1.6833872111548163,
        "y" : -91.8332888212677
      }, {
        "sequenceNumber" : 19,
        "x" : 1.7090282367958418,
        "y" : -91.8332885045231
      }, {
        "sequenceNumber" : 20,
        "x" : 1.7346692624368674,
        "y" : -91.83328608808873
      }, {
        "sequenceNumber" : 21,
        "x" : 1.760310288077893,
        "y" : -91.8332676531905
      }, {
        "sequenceNumber" : 22,
        "x" : 1.7859513137189185,
        "y" : -91.83312701410391
      }, {
        "sequenceNumber" : 23,
        "x" : 1.811592339359944,
        "y" : -91.83205409262523
      }, {
        "sequenceNumber" : 24,
        "x" : 1.8372333650009696,
        "y" : -91.82386936982293
      }, {
        "sequenceNumber" : 25,
        "x" : 1.8628743906419951,
        "y" : -91.7614610155465
      }, {
        "sequenceNumber" : 26,
        "x" : 1.8885154162830207,
        "y" : -91.28724143850395
      }, {
        "sequenceNumber" : 27,
        "x" : 1.9141564419240462,
        "y" : -87.7762389335345
      }, {
        "sequenceNumber" : 28,
        "x" : 1.9397974675650718,
        "y" : -66.02175650820313
      }, {
        "sequenceNumber" : 29,
        "x" : 1.9654384932060973,
        "y" : -4.964936294858944
      }, {
        "sequenceNumber" : 30,
        "x" : 1.9910795188471229,
        "y" : 34.07497545907633
      }, {
        "sequenceNumber" : 31,
        "x" : 2.0167205444881486,
        "y" : 41.95638764681057
      }, {
        "sequenceNumber" : 32,
        "x" : 2.042361570129174,
        "y" : 43.06322736081107
      }, {
        "sequenceNumber" : 33,
        "x" : 2.0680025957701997,
        "y" : 43.20967024412364
      }, {
        "sequenceNumber" : 34,
        "x" : 2.0936436214112253,
        "y" : 43.22888945192834
      }, {
        "sequenceNumber" : 35,
        "x" : 2.119284647052251,
        "y" : 43.23140909825052
      }, {
        "sequenceNumber" : 36,
        "x" : 2.1449256726932764,
        "y" : 43.23173937875812
      }, {
        "sequenceNumber" : 37,
        "x" : 2.170566698334302,
        "y" : 43.23178267182381
      }, {
        "sequenceNumber" : 38,
        "x" : 2.1962077239753275,
        "y" : 43.23178834665167
      }, {
        "sequenceNumber" : 39,
        "x" : 2.221848749616353,
        "y" : 43.23178909050431
      } ],
      "fitEquation" : "HILLEQUATION",
      "fitParameterMap" : {
        "Min" : {
          "value" : -91.83328886904958,
          "name" : "Min",
          "status" : "FLOAT"
        },
        "Max" : {
          "value" : 43.231789202716726,
          "name" : "Max",
          "status" : "FLOAT"
        },
        "EC50" : {
          "value" : 1.9580046596568086,
          "name" : "EC50",
          "status" : "FLOAT"
        },
        "Slope" : {
          "value" : 34.416166375070446,
          "name" : "Slope",
          "status" : "FLOAT"
        }
      }
    }, {
      "id" : 67,
      "compound" : {
        "id" : 186,
        "name" : "C256-782"
      },
      "experiment" : {
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
      },
      "experimentPoints" : [ {
        "x" : 16.666666666666668,
        "logx" : 1.2218487496163564,
        "y" : -17.01121088725127,
        "plateId" : 2,
        "doseId" : 110,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 33.333333333333336,
        "logx" : 1.5228787452803376,
        "y" : -8.499420455690432,
        "plateId" : 2,
        "doseId" : 111,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 50.0,
        "logx" : 1.6989700043360187,
        "y" : -11.444095187269593,
        "plateId" : 2,
        "doseId" : 112,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 66.66666666666667,
        "logx" : 1.8239087409443189,
        "y" : -0.4908653474980087,
        "plateId" : 2,
        "doseId" : 113,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 83.33333333333334,
        "logx" : 1.9208187539523751,
        "y" : 11.608120265801146,
        "plateId" : 2,
        "doseId" : 114,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 100.0,
        "logx" : 2.0,
        "y" : 42.876102510794055,
        "plateId" : 2,
        "doseId" : 115,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 116.66666666666667,
        "logx" : 2.066946789630613,
        "y" : 72.87894256362485,
        "plateId" : 2,
        "doseId" : 116,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 133.33333333333334,
        "logx" : 2.1249387366083,
        "y" : 90.62550428409372,
        "plateId" : 2,
        "doseId" : 117,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 150.0,
        "logx" : 2.1760912590556813,
        "y" : 99.11846237742644,
        "plateId" : 2,
        "doseId" : 118,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 166.66666666666669,
        "logx" : 2.2218487496163566,
        "y" : 95.22856299662773,
        "plateId" : 2,
        "doseId" : 119,
        "resultStatus" : "INCLUDED"
      } ],
      "curveFitPoints" : [ {
        "sequenceNumber" : 0,
        "x" : 1.2218487496163564,
        "y" : -11.413417226296506
      }, {
        "sequenceNumber" : 1,
        "x" : 1.247489775257382,
        "y" : -11.413230647796173
      }, {
        "sequenceNumber" : 2,
        "x" : 1.2731308008984075,
        "y" : -11.412947887948743
      }, {
        "sequenceNumber" : 3,
        "x" : 1.298771826539433,
        "y" : -11.412519366112901
      }, {
        "sequenceNumber" : 4,
        "x" : 1.3244128521804586,
        "y" : -11.411869944441658
      }, {
        "sequenceNumber" : 5,
        "x" : 1.3500538778214841,
        "y" : -11.410885755505797
      }, {
        "sequenceNumber" : 6,
        "x" : 1.3756949034625097,
        "y" : -11.4093942427597
      }, {
        "sequenceNumber" : 7,
        "x" : 1.4013359291035352,
        "y" : -11.407133919293384
      }, {
        "sequenceNumber" : 8,
        "x" : 1.4269769547445608,
        "y" : -11.40370855438265
      }, {
        "sequenceNumber" : 9,
        "x" : 1.4526179803855863,
        "y" : -11.398517782775953
      }, {
        "sequenceNumber" : 10,
        "x" : 1.4782590060266119,
        "y" : -11.390652034167015
      }, {
        "sequenceNumber" : 11,
        "x" : 1.5039000316676374,
        "y" : -11.378733508301444
      }, {
        "sequenceNumber" : 12,
        "x" : 1.529541057308663,
        "y" : -11.360675654065377
      }, {
        "sequenceNumber" : 13,
        "x" : 1.5551820829496885,
        "y" : -11.333319764957924
      }, {
        "sequenceNumber" : 14,
        "x" : 1.580823108590714,
        "y" : -11.291886773640371
      }, {
        "sequenceNumber" : 15,
        "x" : 1.6064641342317396,
        "y" : -11.229152251708424
      }, {
        "sequenceNumber" : 16,
        "x" : 1.6321051598727652,
        "y" : -11.134209398532448
      }, {
        "sequenceNumber" : 17,
        "x" : 1.6577461855137907,
        "y" : -10.990624643304931
      }, {
        "sequenceNumber" : 18,
        "x" : 1.6833872111548163,
        "y" : -10.773711331624071
      }, {
        "sequenceNumber" : 19,
        "x" : 1.7090282367958418,
        "y" : -10.446553747660191
      }, {
        "sequenceNumber" : 20,
        "x" : 1.7346692624368674,
        "y" : -9.954330903861374
      }, {
        "sequenceNumber" : 21,
        "x" : 1.760310288077893,
        "y" : -9.216488408502803
      }, {
        "sequenceNumber" : 22,
        "x" : 1.7859513137189185,
        "y" : -8.116558372656987
      }, {
        "sequenceNumber" : 23,
        "x" : 1.811592339359944,
        "y" : -6.490287910464455
      }, {
        "sequenceNumber" : 24,
        "x" : 1.8372333650009696,
        "y" : -4.114831511815647
      }, {
        "sequenceNumber" : 25,
        "x" : 1.8628743906419951,
        "y" : -0.7058867763744061
      }, {
        "sequenceNumber" : 26,
        "x" : 1.8885154162830207,
        "y" : 4.064037425109147
      }, {
        "sequenceNumber" : 27,
        "x" : 1.9141564419240462,
        "y" : 10.507418725011084
      }, {
        "sequenceNumber" : 28,
        "x" : 1.9397974675650718,
        "y" : 18.80952130165737
      }, {
        "sequenceNumber" : 29,
        "x" : 1.9654384932060973,
        "y" : 28.878570921842083
      }, {
        "sequenceNumber" : 30,
        "x" : 1.9910795188471229,
        "y" : 40.23188086961977
      }, {
        "sequenceNumber" : 31,
        "x" : 2.0167205444881486,
        "y" : 52.02727164270904
      }, {
        "sequenceNumber" : 32,
        "x" : 2.042361570129174,
        "y" : 63.28449060403941
      }, {
        "sequenceNumber" : 33,
        "x" : 2.0680025957701997,
        "y" : 73.19040724548627
      }, {
        "sequenceNumber" : 34,
        "x" : 2.0936436214112253,
        "y" : 81.3035088839345
      }, {
        "sequenceNumber" : 35,
        "x" : 2.119284647052251,
        "y" : 87.56656176350334
      }, {
        "sequenceNumber" : 36,
        "x" : 2.1449256726932764,
        "y" : 92.18418671848022
      }, {
        "sequenceNumber" : 37,
        "x" : 2.170566698334302,
        "y" : 95.47453608065985
      }, {
        "sequenceNumber" : 38,
        "x" : 2.1962077239753275,
        "y" : 97.76256442264936
      }, {
        "sequenceNumber" : 39,
        "x" : 2.221848749616353,
        "y" : 99.32671914883669
      } ],
      "fitEquation" : "HILLEQUATION",
      "fitParameterMap" : {
        "Min" : {
          "value" : -11.413779160120333,
          "name" : "Min",
          "status" : "FLOAT"
        },
        "Max" : {
          "value" : 102.49237602854674,
          "name" : "Max",
          "status" : "FLOAT"
        },
        "EC50" : {
          "value" : 2.00260770876157,
          "name" : "EC50",
          "status" : "FLOAT"
        },
        "Slope" : {
          "value" : 7.041759274518866,
          "name" : "Slope",
          "status" : "FLOAT"
        }
      }
    }, {
      "id" : 68,
      "compound" : {
        "id" : 187,
        "name" : "C1451-130"
      },
      "experiment" : {
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
      },
      "experimentPoints" : [ {
        "x" : 16.666666666666668,
        "logx" : 1.2218487496163564,
        "y" : -84.32935753561948,
        "plateId" : 2,
        "doseId" : 122,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 33.333333333333336,
        "logx" : 1.5228787452803376,
        "y" : -53.275597193856484,
        "plateId" : 2,
        "doseId" : 123,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 50.0,
        "logx" : 1.6989700043360187,
        "y" : -6.124881273616115,
        "plateId" : 2,
        "doseId" : 124,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 66.66666666666667,
        "logx" : 1.8239087409443189,
        "y" : -33.92475540996609,
        "plateId" : 2,
        "doseId" : 125,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 83.33333333333334,
        "logx" : 1.9208187539523751,
        "y" : -2.5990405133244483,
        "plateId" : 2,
        "doseId" : 126,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 100.0,
        "logx" : 2.0,
        "y" : 44.02175513148914,
        "plateId" : 2,
        "doseId" : 127,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 116.66666666666667,
        "logx" : 2.066946789630613,
        "y" : 31.975133283439668,
        "plateId" : 2,
        "doseId" : 128,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 133.33333333333334,
        "logx" : 2.1249387366083,
        "y" : 85.41896351480865,
        "plateId" : 2,
        "doseId" : 129,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 150.0,
        "logx" : 2.1760912590556813,
        "y" : 79.88630349050872,
        "plateId" : 2,
        "doseId" : 130,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 166.66666666666669,
        "logx" : 2.2218487496163566,
        "y" : 51.160741201696084,
        "plateId" : 2,
        "doseId" : 131,
        "resultStatus" : "INCLUDED"
      } ],
      "curveFitPoints" : [ {
        "sequenceNumber" : 0,
        "x" : 1.2218487496163564,
        "y" : -79.91178673278112
      }, {
        "sequenceNumber" : 1,
        "x" : 1.247489775257382,
        "y" : -78.59090362189843
      }, {
        "sequenceNumber" : 2,
        "x" : 1.2731308008984075,
        "y" : -77.15968355973423
      }, {
        "sequenceNumber" : 3,
        "x" : 1.298771826539433,
        "y" : -75.61060668843871
      }, {
        "sequenceNumber" : 4,
        "x" : 1.3244128521804586,
        "y" : -73.93595334979416
      }, {
        "sequenceNumber" : 5,
        "x" : 1.3500538778214841,
        "y" : -72.12786113925027
      }, {
        "sequenceNumber" : 6,
        "x" : 1.3756949034625097,
        "y" : -70.17839524904869
      }, {
        "sequenceNumber" : 7,
        "x" : 1.4013359291035352,
        "y" : -68.07963325325525
      }, {
        "sequenceNumber" : 8,
        "x" : 1.4269769547445608,
        "y" : -65.82376528109279
      }, {
        "sequenceNumber" : 9,
        "x" : 1.4526179803855863,
        "y" : -63.40321021637364
      }, {
        "sequenceNumber" : 10,
        "x" : 1.4782590060266119,
        "y" : -60.810748135366055
      }, {
        "sequenceNumber" : 11,
        "x" : 1.5039000316676374,
        "y" : -58.03966864173902
      }, {
        "sequenceNumber" : 12,
        "x" : 1.529541057308663,
        "y" : -55.08393406961366
      }, {
        "sequenceNumber" : 13,
        "x" : 1.5551820829496885,
        "y" : -51.938355706836404
      }, {
        "sequenceNumber" : 14,
        "x" : 1.580823108590714,
        "y" : -48.598780254184504
      }, {
        "sequenceNumber" : 15,
        "x" : 1.6064641342317396,
        "y" : -45.06228271001905
      }, {
        "sequenceNumber" : 16,
        "x" : 1.6321051598727652,
        "y" : -41.32736079767473
      }, {
        "sequenceNumber" : 17,
        "x" : 1.6577461855137907,
        "y" : -37.39412499553279
      }, {
        "sequenceNumber" : 18,
        "x" : 1.6833872111548163,
        "y" : -33.26447726474212
      }, {
        "sequenceNumber" : 19,
        "x" : 1.7090282367958418,
        "y" : -28.942270788148107
      }, {
        "sequenceNumber" : 20,
        "x" : 1.7346692624368674,
        "y" : -24.433442535629595
      }, {
        "sequenceNumber" : 21,
        "x" : 1.760310288077893,
        "y" : -19.746110355142406
      }, {
        "sequenceNumber" : 22,
        "x" : 1.7859513137189185,
        "y" : -14.89062664387292
      }, {
        "sequenceNumber" : 23,
        "x" : 1.811592339359944,
        "y" : -9.879581545203663
      }, {
        "sequenceNumber" : 24,
        "x" : 1.8372333650009696,
        "y" : -4.727750073747728
      }, {
        "sequenceNumber" : 25,
        "x" : 1.8628743906419951,
        "y" : 0.5480204256741956
      }, {
        "sequenceNumber" : 26,
        "x" : 1.8885154162830207,
        "y" : 5.928983601365189
      }, {
        "sequenceNumber" : 27,
        "x" : 1.9141564419240462,
        "y" : 11.39472649346618
      }, {
        "sequenceNumber" : 28,
        "x" : 1.9397974675650718,
        "y" : 16.92345631201846
      }, {
        "sequenceNumber" : 29,
        "x" : 1.9654384932060973,
        "y" : 22.492333959616843
      }, {
        "sequenceNumber" : 30,
        "x" : 1.9910795188471229,
        "y" : 28.077844008486892
      }, {
        "sequenceNumber" : 31,
        "x" : 2.0167205444881486,
        "y" : 33.65618869432291
      }, {
        "sequenceNumber" : 32,
        "x" : 2.042361570129174,
        "y" : 39.20369203972837
      }, {
        "sequenceNumber" : 33,
        "x" : 2.0680025957701997,
        "y" : 44.69719960076509
      }, {
        "sequenceNumber" : 34,
        "x" : 2.0936436214112253,
        "y" : 50.11445959829723
      }, {
        "sequenceNumber" : 35,
        "x" : 2.119284647052251,
        "y" : 55.43447232661478
      }, {
        "sequenceNumber" : 36,
        "x" : 2.1449256726932764,
        "y" : 60.637796621323815
      }, {
        "sequenceNumber" : 37,
        "x" : 2.170566698334302,
        "y" : 65.7068046471642
      }, {
        "sequenceNumber" : 38,
        "x" : 2.1962077239753275,
        "y" : 70.62587912033297
      }, {
        "sequenceNumber" : 39,
        "x" : 2.221848749616353,
        "y" : 75.38155007629945
      } ],
      "fitEquation" : "HILLEQUATION",
      "fitParameterMap" : {
        "Min" : {
          "value" : -94.51665315247695,
          "name" : "Min",
          "status" : "FLOAT"
        },
        "Max" : {
          "value" : 147.3108085162074,
          "name" : "Max",
          "status" : "FLOAT"
        },
        "EC50" : {
          "value" : 1.983365266222439,
          "name" : "EC50",
          "status" : "FLOAT"
        },
        "Slope" : {
          "value" : 1.5652371297786078,
          "name" : "Slope",
          "status" : "FLOAT"
        }
      }
    }, {
      "id" : 69,
      "compound" : {
        "id" : 188,
        "name" : "C153-1016"
      },
      "experiment" : {
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
      },
      "experimentPoints" : [ {
        "x" : 16.666666666666668,
        "logx" : 1.2218487496163564,
        "y" : -21.058460370680315,
        "plateId" : 2,
        "doseId" : 134,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 33.333333333333336,
        "logx" : 1.5228787452803376,
        "y" : -20.888040606812364,
        "plateId" : 2,
        "doseId" : 135,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 50.0,
        "logx" : 1.6989700043360187,
        "y" : -23.819243799819198,
        "plateId" : 2,
        "doseId" : 136,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 66.66666666666667,
        "logx" : 1.8239087409443189,
        "y" : -27.171859356524607,
        "plateId" : 2,
        "doseId" : 137,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 83.33333333333334,
        "logx" : 1.9208187539523751,
        "y" : -4.20093536186355,
        "plateId" : 2,
        "doseId" : 138,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 100.0,
        "logx" : 2.0,
        "y" : 13.96734351048757,
        "plateId" : 2,
        "doseId" : 139,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 116.66666666666667,
        "logx" : 2.066946789630613,
        "y" : 59.13055379916673,
        "plateId" : 2,
        "doseId" : 140,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 133.33333333333334,
        "logx" : 2.1249387366083,
        "y" : 58.58843713980657,
        "plateId" : 2,
        "doseId" : 141,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 150.0,
        "logx" : 2.1760912590556813,
        "y" : 75.62356116354502,
        "plateId" : 2,
        "doseId" : 142,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 166.66666666666669,
        "logx" : 2.2218487496163566,
        "y" : 98.87176829630174,
        "plateId" : 2,
        "doseId" : 143,
        "resultStatus" : "INCLUDED"
      } ],
      "curveFitPoints" : [ {
        "sequenceNumber" : 0,
        "x" : 1.2218487496163564,
        "y" : -24.356639210997134
      }, {
        "sequenceNumber" : 1,
        "x" : 1.247489775257382,
        "y" : -24.356053967393688
      }, {
        "sequenceNumber" : 2,
        "x" : 1.2731308008984075,
        "y" : -24.355220061760818
      }, {
        "sequenceNumber" : 3,
        "x" : 1.298771826539433,
        "y" : -24.354031846703506
      }, {
        "sequenceNumber" : 4,
        "x" : 1.3244128521804586,
        "y" : -24.352338795000033
      }, {
        "sequenceNumber" : 5,
        "x" : 1.3500538778214841,
        "y" : -24.3499264403688
      }, {
        "sequenceNumber" : 6,
        "x" : 1.3756949034625097,
        "y" : -24.346489230546773
      }, {
        "sequenceNumber" : 7,
        "x" : 1.4013359291035352,
        "y" : -24.341591867145294
      }, {
        "sequenceNumber" : 8,
        "x" : 1.4269769547445608,
        "y" : -24.33461426249813
      }, {
        "sequenceNumber" : 9,
        "x" : 1.4526179803855863,
        "y" : -24.324673197390553
      }, {
        "sequenceNumber" : 10,
        "x" : 1.4782590060266119,
        "y" : -24.31051087124206
      }, {
        "sequenceNumber" : 11,
        "x" : 1.5039000316676374,
        "y" : -24.290336462656914
      }, {
        "sequenceNumber" : 12,
        "x" : 1.529541057308663,
        "y" : -24.26160111006801
      }, {
        "sequenceNumber" : 13,
        "x" : 1.5551820829496885,
        "y" : -24.220678783431957
      }, {
        "sequenceNumber" : 14,
        "x" : 1.580823108590714,
        "y" : -24.162414599164048
      }, {
        "sequenceNumber" : 15,
        "x" : 1.6064641342317396,
        "y" : -24.079487363731662
      }, {
        "sequenceNumber" : 16,
        "x" : 1.6321051598727652,
        "y" : -23.961513677070627
      }, {
        "sequenceNumber" : 17,
        "x" : 1.6577461855137907,
        "y" : -23.793796371651183
      }, {
        "sequenceNumber" : 18,
        "x" : 1.6833872111548163,
        "y" : -23.555591337628297
      }, {
        "sequenceNumber" : 19,
        "x" : 1.7090282367958418,
        "y" : -23.217738109414686
      }, {
        "sequenceNumber" : 20,
        "x" : 1.7346692624368674,
        "y" : -22.739482315006466
      }, {
        "sequenceNumber" : 21,
        "x" : 1.760310288077893,
        "y" : -22.064337752517236
      }, {
        "sequenceNumber" : 22,
        "x" : 1.7859513137189185,
        "y" : -21.11494391244076
      }, {
        "sequenceNumber" : 23,
        "x" : 1.811592339359944,
        "y" : -19.787164955902856
      }, {
        "sequenceNumber" : 24,
        "x" : 1.8372333650009696,
        "y" : -17.944298757528003
      }, {
        "sequenceNumber" : 25,
        "x" : 1.8628743906419951,
        "y" : -15.413412262305036
      }, {
        "sequenceNumber" : 26,
        "x" : 1.8885154162830207,
        "y" : -11.987617023211282
      }, {
        "sequenceNumber" : 27,
        "x" : 1.9141564419240462,
        "y" : -7.44027658819747
      }, {
        "sequenceNumber" : 28,
        "x" : 1.9397974675650718,
        "y" : -1.5583957711116128
      }, {
        "sequenceNumber" : 29,
        "x" : 1.9654384932060973,
        "y" : 5.800187410825423
      }, {
        "sequenceNumber" : 30,
        "x" : 1.9910795188471229,
        "y" : 14.631605886872212
      }, {
        "sequenceNumber" : 31,
        "x" : 2.0167205444881486,
        "y" : 24.71721704914481
      }, {
        "sequenceNumber" : 32,
        "x" : 2.042361570129174,
        "y" : 35.60230672565672
      }, {
        "sequenceNumber" : 33,
        "x" : 2.0680025957701997,
        "y" : 46.656637821909
      }, {
        "sequenceNumber" : 34,
        "x" : 2.0936436214112253,
        "y" : 57.21034656799485
      }, {
        "sequenceNumber" : 35,
        "x" : 2.119284647052251,
        "y" : 66.7082930787828
      }, {
        "sequenceNumber" : 36,
        "x" : 2.1449256726932764,
        "y" : 74.81239598488514
      }, {
        "sequenceNumber" : 37,
        "x" : 2.170566698334302,
        "y" : 81.41859600965213
      }, {
        "sequenceNumber" : 38,
        "x" : 2.1962077239753275,
        "y" : 86.60620739236494
      }, {
        "sequenceNumber" : 39,
        "x" : 2.221848749616353,
        "y" : 90.56157363963987
      } ],
      "fitEquation" : "HILLEQUATION",
      "fitParameterMap" : {
        "Min" : {
          "value" : -24.35801659233851,
          "name" : "Min",
          "status" : "FLOAT"
        },
        "Max" : {
          "value" : 101.08509973694129,
          "name" : "Max",
          "status" : "FLOAT"
        },
        "EC50" : {
          "value" : 2.048741304882146,
          "name" : "EC50",
          "status" : "FLOAT"
        },
        "Slope" : {
          "value" : 5.997620656644867,
          "name" : "Slope",
          "status" : "FLOAT"
        }
      }
    }, {
      "id" : 70,
      "compound" : {
        "id" : 189,
        "name" : "C597-876"
      },
      "experiment" : {
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
      },
      "experimentPoints" : [ {
        "x" : 16.666666666666668,
        "logx" : 1.2218487496163564,
        "y" : -12.182010436436236,
        "plateId" : 2,
        "doseId" : 146,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 33.333333333333336,
        "logx" : 1.5228787452803376,
        "y" : -13.410464095016211,
        "plateId" : 2,
        "doseId" : 147,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 50.0,
        "logx" : 1.6989700043360187,
        "y" : -4.12503520871685,
        "plateId" : 2,
        "doseId" : 148,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 66.66666666666667,
        "logx" : 1.8239087409443189,
        "y" : -6.692486696513041,
        "plateId" : 2,
        "doseId" : 149,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 83.33333333333334,
        "logx" : 1.9208187539523751,
        "y" : 15.710753997851878,
        "plateId" : 2,
        "doseId" : 150,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 100.0,
        "logx" : 2.0,
        "y" : 48.72594047728346,
        "plateId" : 2,
        "doseId" : 151,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 116.66666666666667,
        "logx" : 2.066946789630613,
        "y" : 71.97599383824328,
        "plateId" : 2,
        "doseId" : 152,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 133.33333333333334,
        "logx" : 2.1249387366083,
        "y" : 91.63718798296637,
        "plateId" : 2,
        "doseId" : 153,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 150.0,
        "logx" : 2.1760912590556813,
        "y" : 102.0835927231113,
        "plateId" : 2,
        "doseId" : 154,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 166.66666666666669,
        "logx" : 2.2218487496163566,
        "y" : 97.38512940246079,
        "plateId" : 2,
        "doseId" : 155,
        "resultStatus" : "INCLUDED"
      } ],
      "curveFitPoints" : [ {
        "sequenceNumber" : 0,
        "x" : 1.2218487496163564,
        "y" : -10.92725977250739
      }, {
        "sequenceNumber" : 1,
        "x" : 1.247489775257382,
        "y" : -10.92698508857847
      }, {
        "sequenceNumber" : 2,
        "x" : 1.2731308008984075,
        "y" : -10.92657349375652
      }, {
        "sequenceNumber" : 3,
        "x" : 1.298771826539433,
        "y" : -10.925956749284914
      }, {
        "sequenceNumber" : 4,
        "x" : 1.3244128521804586,
        "y" : -10.925032607310392
      }, {
        "sequenceNumber" : 5,
        "x" : 1.3500538778214841,
        "y" : -10.923647864136164
      }, {
        "sequenceNumber" : 6,
        "x" : 1.3756949034625097,
        "y" : -10.921572972439009
      }, {
        "sequenceNumber" : 7,
        "x" : 1.4013359291035352,
        "y" : -10.918464012441225
      }, {
        "sequenceNumber" : 8,
        "x" : 1.4269769547445608,
        "y" : -10.913805737710337
      }, {
        "sequenceNumber" : 9,
        "x" : 1.4526179803855863,
        "y" : -10.906826299397165
      }, {
        "sequenceNumber" : 10,
        "x" : 1.4782590060266119,
        "y" : -10.896369615271981
      }, {
        "sequenceNumber" : 11,
        "x" : 1.5039000316676374,
        "y" : -10.880704458117586
      }, {
        "sequenceNumber" : 12,
        "x" : 1.529541057308663,
        "y" : -10.857239139097008
      }, {
        "sequenceNumber" : 13,
        "x" : 1.5551820829496885,
        "y" : -10.822095665656963
      }, {
        "sequenceNumber" : 14,
        "x" : 1.580823108590714,
        "y" : -10.769475422727691
      }, {
        "sequenceNumber" : 15,
        "x" : 1.6064641342317396,
        "y" : -10.690717139641333
      }, {
        "sequenceNumber" : 16,
        "x" : 1.6321051598727652,
        "y" : -10.572904189272611
      }, {
        "sequenceNumber" : 17,
        "x" : 1.6577461855137907,
        "y" : -10.39681974415797
      }, {
        "sequenceNumber" : 18,
        "x" : 1.6833872111548163,
        "y" : -10.13397589940158
      }, {
        "sequenceNumber" : 19,
        "x" : 1.7090282367958418,
        "y" : -9.742367329578247
      }, {
        "sequenceNumber" : 20,
        "x" : 1.7346692624368674,
        "y" : -9.160556525115021
      }, {
        "sequenceNumber" : 21,
        "x" : 1.760310288077893,
        "y" : -8.299774935306374
      }, {
        "sequenceNumber" : 22,
        "x" : 1.7859513137189185,
        "y" : -7.034116432424921
      }, {
        "sequenceNumber" : 23,
        "x" : 1.811592339359944,
        "y" : -5.189975625136131
      }, {
        "sequenceNumber" : 24,
        "x" : 1.8372333650009696,
        "y" : -2.5382177046508225
      }, {
        "sequenceNumber" : 25,
        "x" : 1.8628743906419951,
        "y" : 1.2032916565687657
      }, {
        "sequenceNumber" : 26,
        "x" : 1.8885154162830207,
        "y" : 6.34369001386405
      }, {
        "sequenceNumber" : 27,
        "x" : 1.9141564419240462,
        "y" : 13.153552361492652
      }, {
        "sequenceNumber" : 28,
        "x" : 1.9397974675650718,
        "y" : 21.752765768694196
      }, {
        "sequenceNumber" : 29,
        "x" : 1.9654384932060973,
        "y" : 31.977421031067273
      }, {
        "sequenceNumber" : 30,
        "x" : 1.9910795188471229,
        "y" : 43.2998826755471
      }, {
        "sequenceNumber" : 31,
        "x" : 2.0167205444881486,
        "y" : 54.89152144615159
      }, {
        "sequenceNumber" : 32,
        "x" : 2.042361570129174,
        "y" : 65.84323054615557
      }, {
        "sequenceNumber" : 33,
        "x" : 2.0680025957701997,
        "y" : 75.43294371114466
      }, {
        "sequenceNumber" : 34,
        "x" : 2.0936436214112253,
        "y" : 83.2868839608149
      }, {
        "sequenceNumber" : 35,
        "x" : 2.119284647052251,
        "y" : 89.37443452933209
      }, {
        "sequenceNumber" : 36,
        "x" : 2.1449256726932764,
        "y" : 93.89443150191414
      }, {
        "sequenceNumber" : 37,
        "x" : 2.170566698334302,
        "y" : 97.14457850015471
      }, {
        "sequenceNumber" : 38,
        "x" : 2.1962077239753275,
        "y" : 99.42810437304601
      }, {
        "sequenceNumber" : 39,
        "x" : 2.221848749616353,
        "y" : 101.00649744145377
      } ],
      "fitEquation" : "HILLEQUATION",
      "fitParameterMap" : {
        "Min" : {
          "value" : -10.927810864162671,
          "name" : "Min",
          "status" : "FLOAT"
        },
        "Max" : {
          "value" : 104.31334853525675,
          "name" : "Max",
          "status" : "FLOAT"
        },
        "EC50" : {
          "value" : 1.9985547135624326,
          "name" : "EC50",
          "status" : "FLOAT"
        },
        "Slope" : {
          "value" : 6.84993028781164,
          "name" : "Slope",
          "status" : "FLOAT"
        }
      }
    }, {
      "id" : 71,
      "compound" : {
        "id" : 190,
        "name" : "C269-1328"
      },
      "experiment" : {
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
      },
      "experimentPoints" : [ {
        "x" : 16.666666666666668,
        "logx" : 1.2218487496163564,
        "y" : -37.09623752295039,
        "plateId" : 2,
        "doseId" : 158,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 33.333333333333336,
        "logx" : 1.5228787452803376,
        "y" : -40.330171072320425,
        "plateId" : 2,
        "doseId" : 159,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 50.0,
        "logx" : 1.6989700043360187,
        "y" : -25.972410963915816,
        "plateId" : 2,
        "doseId" : 160,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 66.66666666666667,
        "logx" : 1.8239087409443189,
        "y" : -2.201063416525893,
        "plateId" : 2,
        "doseId" : 161,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 83.33333333333334,
        "logx" : 1.9208187539523751,
        "y" : -22.1561776599478,
        "plateId" : 2,
        "doseId" : 162,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 100.0,
        "logx" : 2.0,
        "y" : 42.87142907440395,
        "plateId" : 2,
        "doseId" : 163,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 116.66666666666667,
        "logx" : 2.066946789630613,
        "y" : 37.70870885938145,
        "plateId" : 2,
        "doseId" : 164,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 133.33333333333334,
        "logx" : 2.1249387366083,
        "y" : 65.35375504796082,
        "plateId" : 2,
        "doseId" : 165,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 150.0,
        "logx" : 2.1760912590556813,
        "y" : 101.05039359753016,
        "plateId" : 2,
        "doseId" : 166,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 166.66666666666669,
        "logx" : 2.2218487496163566,
        "y" : 94.19591187116822,
        "plateId" : 2,
        "doseId" : 167,
        "resultStatus" : "INCLUDED"
      } ],
      "curveFitPoints" : [ {
        "sequenceNumber" : 0,
        "x" : 1.2218487496163564,
        "y" : -36.92966500305455
      }, {
        "sequenceNumber" : 1,
        "x" : 1.247489775257382,
        "y" : -36.88541680653376
      }, {
        "sequenceNumber" : 2,
        "x" : 1.2731308008984075,
        "y" : -36.83129886031113
      }, {
        "sequenceNumber" : 3,
        "x" : 1.298771826539433,
        "y" : -36.76511737724982
      }, {
        "sequenceNumber" : 4,
        "x" : 1.3244128521804586,
        "y" : -36.68419476884461
      }, {
        "sequenceNumber" : 5,
        "x" : 1.3500538778214841,
        "y" : -36.58526484503948
      }, {
        "sequenceNumber" : 6,
        "x" : 1.3756949034625097,
        "y" : -36.4643462524426
      }, {
        "sequenceNumber" : 7,
        "x" : 1.4013359291035352,
        "y" : -36.31659010211216
      }, {
        "sequenceNumber" : 8,
        "x" : 1.4269769547445608,
        "y" : -36.136097225647724
      }, {
        "sequenceNumber" : 9,
        "x" : 1.4526179803855863,
        "y" : -35.915700055321246
      }, {
        "sequenceNumber" : 10,
        "x" : 1.4782590060266119,
        "y" : -35.646703848206315
      }, {
        "sequenceNumber" : 11,
        "x" : 1.5039000316676374,
        "y" : -35.318582018914334
      }, {
        "sequenceNumber" : 12,
        "x" : 1.529541057308663,
        "y" : -34.918620942619604
      }, {
        "sequenceNumber" : 13,
        "x" : 1.5551820829496885,
        "y" : -34.43151108020609
      }, {
        "sequenceNumber" : 14,
        "x" : 1.580823108590714,
        "y" : -33.83888414540837
      }, {
        "sequenceNumber" : 15,
        "x" : 1.6064641342317396,
        "y" : -33.11880094594471
      }, {
        "sequenceNumber" : 16,
        "x" : 1.6321051598727652,
        "y" : -32.24520236356034
      }, {
        "sequenceNumber" : 17,
        "x" : 1.6577461855137907,
        "y" : -31.187347779611258
      }, {
        "sequenceNumber" : 18,
        "x" : 1.6833872111548163,
        "y" : -29.909282338105506
      }, {
        "sequenceNumber" : 19,
        "x" : 1.7090282367958418,
        "y" : -28.369397957809973
      }, {
        "sequenceNumber" : 20,
        "x" : 1.7346692624368674,
        "y" : -26.520183698921414
      }, {
        "sequenceNumber" : 21,
        "x" : 1.760310288077893,
        "y" : -24.30829850887178
      }, {
        "sequenceNumber" : 22,
        "x" : 1.7859513137189185,
        "y" : -21.67514070226599
      }, {
        "sequenceNumber" : 23,
        "x" : 1.811592339359944,
        "y" : -18.558126960640713
      }, {
        "sequenceNumber" : 24,
        "x" : 1.8372333650009696,
        "y" : -14.892916505632193
      }, {
        "sequenceNumber" : 25,
        "x" : 1.8628743906419951,
        "y" : -10.61680354978326
      }, {
        "sequenceNumber" : 26,
        "x" : 1.8885154162830207,
        "y" : -5.673426796015775
      }, {
        "sequenceNumber" : 27,
        "x" : 1.9141564419240462,
        "y" : -0.01878075055789452
      }, {
        "sequenceNumber" : 28,
        "x" : 1.9397974675650718,
        "y" : 6.371757807853868
      }, {
        "sequenceNumber" : 29,
        "x" : 1.9654384932060973,
        "y" : 13.496042506438705
      }, {
        "sequenceNumber" : 30,
        "x" : 1.9910795188471229,
        "y" : 21.31846611928323
      }, {
        "sequenceNumber" : 31,
        "x" : 2.0167205444881486,
        "y" : 29.765337751368307
      }, {
        "sequenceNumber" : 32,
        "x" : 2.042361570129174,
        "y" : 38.723764343357985
      }, {
        "sequenceNumber" : 33,
        "x" : 2.0680025957701997,
        "y" : 48.04513640957262
      }, {
        "sequenceNumber" : 34,
        "x" : 2.0936436214112253,
        "y" : 57.55354299501418
      }, {
        "sequenceNumber" : 35,
        "x" : 2.119284647052251,
        "y" : 67.0583699667453
      }, {
        "sequenceNumber" : 36,
        "x" : 2.1449256726932764,
        "y" : 76.36928863612135
      }, {
        "sequenceNumber" : 37,
        "x" : 2.170566698334302,
        "y" : 85.31119709581091
      }, {
        "sequenceNumber" : 38,
        "x" : 2.1962077239753275,
        "y" : 93.73668970323128
      }, {
        "sequenceNumber" : 39,
        "x" : 2.221848749616353,
        "y" : 101.53429554588404
      } ],
      "fitEquation" : "HILLEQUATION",
      "fitParameterMap" : {
        "Min" : {
          "value" : -37.12772767827211,
          "name" : "Min",
          "status" : "FLOAT"
        },
        "Max" : {
          "value" : 151.88107157858101,
          "name" : "Max",
          "status" : "FLOAT"
        },
        "EC50" : {
          "value" : 2.0931681835494302,
          "name" : "EC50",
          "status" : "FLOAT"
        },
        "Slope" : {
          "value" : 3.4192099128463047,
          "name" : "Slope",
          "status" : "FLOAT"
        }
      }
    }, {
      "id" : 72,
      "compound" : {
        "id" : 191,
        "name" : "C118-1380"
      },
      "experiment" : {
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
      },
      "experimentPoints" : [ {
        "x" : 16.666666666666668,
        "logx" : 1.2218487496163564,
        "y" : -12.75946175526724,
        "plateId" : 2,
        "doseId" : 170,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 33.333333333333336,
        "logx" : 1.5228787452803376,
        "y" : -39.16314534048514,
        "plateId" : 2,
        "doseId" : 171,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 50.0,
        "logx" : 1.6989700043360187,
        "y" : -32.973676083733764,
        "plateId" : 2,
        "doseId" : 172,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 66.66666666666667,
        "logx" : 1.8239087409443189,
        "y" : -27.838834547773807,
        "plateId" : 2,
        "doseId" : 173,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 83.33333333333334,
        "logx" : 1.9208187539523751,
        "y" : 11.226944687747281,
        "plateId" : 2,
        "doseId" : 174,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 100.0,
        "logx" : 2.0,
        "y" : 27.521805519309744,
        "plateId" : 2,
        "doseId" : 175,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 116.66666666666667,
        "logx" : 2.066946789630613,
        "y" : 74.82780756475059,
        "plateId" : 2,
        "doseId" : 176,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 133.33333333333334,
        "logx" : 2.1249387366083,
        "y" : 77.39222257304573,
        "plateId" : 2,
        "doseId" : 177,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 150.0,
        "logx" : 2.1760912590556813,
        "y" : 74.54335657455601,
        "plateId" : 2,
        "doseId" : 178,
        "resultStatus" : "INCLUDED"
      }, {
        "x" : 166.66666666666669,
        "logx" : 2.2218487496163566,
        "y" : 89.00960779165925,
        "plateId" : 2,
        "doseId" : 179,
        "resultStatus" : "INCLUDED"
      } ],
      "curveFitPoints" : [ {
        "sequenceNumber" : 0,
        "x" : 1.2218487496163564,
        "y" : -29.258896096264337
      }, {
        "sequenceNumber" : 1,
        "x" : 1.247489775257382,
        "y" : -29.258738985889682
      }, {
        "sequenceNumber" : 2,
        "x" : 1.2731308008984075,
        "y" : -29.258495827343612
      }, {
        "sequenceNumber" : 3,
        "x" : 1.298771826539433,
        "y" : -29.258119493402226
      }, {
        "sequenceNumber" : 4,
        "x" : 1.3244128521804586,
        "y" : -29.257537047000415
      }, {
        "sequenceNumber" : 5,
        "x" : 1.3500538778214841,
        "y" : -29.25663560752233
      }, {
        "sequenceNumber" : 6,
        "x" : 1.3756949034625097,
        "y" : -29.255240479277404
      }, {
        "sequenceNumber" : 7,
        "x" : 1.4013359291035352,
        "y" : -29.253081308624985
      }, {
        "sequenceNumber" : 8,
        "x" : 1.4269769547445608,
        "y" : -29.2497397237152
      }, {
        "sequenceNumber" : 9,
        "x" : 1.4526179803855863,
        "y" : -29.244568340420713
      }, {
        "sequenceNumber" : 10,
        "x" : 1.4782590060266119,
        "y" : -29.23656551285223
      }, {
        "sequenceNumber" : 11,
        "x" : 1.5039000316676374,
        "y" : -29.22418173604323
      }, {
        "sequenceNumber" : 12,
        "x" : 1.529541057308663,
        "y" : -29.205020616631735
      }, {
        "sequenceNumber" : 13,
        "x" : 1.5551820829496885,
        "y" : -29.17537750315515
      }, {
        "sequenceNumber" : 14,
        "x" : 1.580823108590714,
        "y" : -29.129528861116093
      }, {
        "sequenceNumber" : 15,
        "x" : 1.6064641342317396,
        "y" : -29.05864064495738
      }, {
        "sequenceNumber" : 16,
        "x" : 1.6321051598727652,
        "y" : -28.949098345793722
      }, {
        "sequenceNumber" : 17,
        "x" : 1.6577461855137907,
        "y" : -28.779968908104742
      }, {
        "sequenceNumber" : 18,
        "x" : 1.6833872111548163,
        "y" : -28.51918263559799
      }, {
        "sequenceNumber" : 19,
        "x" : 1.7090282367958418,
        "y" : -28.117882932892588
      }, {
        "sequenceNumber" : 20,
        "x" : 1.7346692624368674,
        "y" : -27.50228469416903
      }, {
        "sequenceNumber" : 21,
        "x" : 1.760310288077893,
        "y" : -26.56245718489182
      }, {
        "sequenceNumber" : 22,
        "x" : 1.7859513137189185,
        "y" : -25.13806085270878
      }, {
        "sequenceNumber" : 23,
        "x" : 1.811592339359944,
        "y" : -23.002947243276054
      }, {
        "sequenceNumber" : 24,
        "x" : 1.8372333650009696,
        "y" : -19.854858840813474
      }, {
        "sequenceNumber" : 25,
        "x" : 1.8628743906419951,
        "y" : -15.324312507351276
      }, {
        "sequenceNumber" : 26,
        "x" : 1.8885154162830207,
        "y" : -9.026488181390857
      }, {
        "sequenceNumber" : 27,
        "x" : 1.9141564419240462,
        "y" : -0.6814262099781239
      }, {
        "sequenceNumber" : 28,
        "x" : 1.9397974675650718,
        "y" : 9.701384429306614
      }, {
        "sequenceNumber" : 29,
        "x" : 1.9654384932060973,
        "y" : 21.652846294388574
      }, {
        "sequenceNumber" : 30,
        "x" : 1.9910795188471229,
        "y" : 34.23819934841578
      }, {
        "sequenceNumber" : 31,
        "x" : 2.0167205444881486,
        "y" : 46.30773393297432
      }, {
        "sequenceNumber" : 32,
        "x" : 2.042361570129174,
        "y" : 56.8878060701384
      }, {
        "sequenceNumber" : 33,
        "x" : 2.0680025957701997,
        "y" : 65.45602309089696
      }, {
        "sequenceNumber" : 34,
        "x" : 2.0936436214112253,
        "y" : 71.96076851561378
      }, {
        "sequenceNumber" : 35,
        "x" : 2.119284647052251,
        "y" : 76.66081761289493
      }, {
        "sequenceNumber" : 36,
        "x" : 2.1449256726932764,
        "y" : 79.9369181906286
      }, {
        "sequenceNumber" : 37,
        "x" : 2.170566698334302,
        "y" : 82.16364664825454
      }, {
        "sequenceNumber" : 38,
        "x" : 2.1962077239753275,
        "y" : 83.65132246413705
      }, {
        "sequenceNumber" : 39,
        "x" : 2.221848749616353,
        "y" : 84.63385084854374
      } ],
      "fitEquation" : "HILLEQUATION",
      "fitParameterMap" : {
        "Min" : {
          "value" : -29.259182953298783,
          "name" : "Min",
          "status" : "FLOAT"
        },
        "Max" : {
          "value" : 86.47259951292038,
          "name" : "Max",
          "status" : "FLOAT"
        },
        "EC50" : {
          "value" : 1.9796166976504543,
          "name" : "EC50",
          "status" : "FLOAT"
        },
        "Slope" : {
          "value" : 7.397760852520452,
          "name" : "Slope",
          "status" : "FLOAT"
        }
      }
    } ]
  };



  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope,$httpBackend) {
    scope = $rootScope.$new();

    // Setup unit test Rest Calls
    httpBackend = $httpBackend;
    httpBackend.whenGET("services/rest/experiment").respond(expResp);
    httpBackend.whenGET("services/rest/experiment/1/doseresponse/results").respond(doseResponse);
    //httpBackend.whenDELETE("services/rest/experiment/2").respond("");

    //basic mock of location service
    location={
      saved: "",
      path: function(loc){this.saved=loc;}}

    DoseResponseCntrl = $controller('DoseResponseCntrl', {
      $scope: scope
    });
  }));

  it('should retrieve all experiments on init', function () {
    httpBackend.flush();
    expect(scope.experiments.length).toBe(3);
    expect(scope.experiments[0].name).toBe("experiment uno");
    expect(scope.experiments[2].protocol.name).toBe("Gamma");

  });

  it('should start tour', function () {
    httpBackend.flush();
    scope.startTour();

    expect(scope.startJoyRide).toBe(true);
  });

  it('should call knockout service on point click', function () {
    httpBackend.flush();

    expect(scope.compounds).toBeDefined();
    expect(scope.compounds.length).toBe(24);

    scope.doClick(scope.selectedCompound.wells[0]);

    httpBackend.whenPOST("services/rest/experiment/1/doseresponse/kopoint").respond(403,"");

    httpBackend.flush();


  });


});
