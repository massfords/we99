__author__ = 'orchie'
import sys
import json
import logging
import numpy as np
from lmfit import minimize, Parameters
from lmfit import Model



def parsePoint(point):
    if point['y'] and point['x']:
        return point

class Point:
    def __init__(self,xvalue,yvalue,sequence=0):
        self.sequenceNumber=sequence
        self.x=xvalue
        self.y=yvalue


def buildResponse(id, fitParameters,curveFitPoints,):
        return json.dumps(dict(id.items() + fitParameters.items() + curveFitPoints.items()))

def main():
    """Parse a json string from cmd line"""
    #print sys.argv[1]
    #print len(sys.argv)
    logging.debug("Call=>" + sys.argv[1])
    logging.debug(len(sys.argv))
    data = json.loads(sys.argv[1])

    logging.debug("Parsed")
    logging.debug(data)
    #print json.dumps(data)

    id = None
    points = None
    validPoints = None
    if data['id']:
        logging.debug("ID")
        id = data['id']
        logging.debug(id)



    if data['experimentPoints']:
        points = data['experimentPoints']
        validPoints = [ p for p in points if parsePoint(p)]

    logging.debug("Mark1")
    #if data["fitEquation"]:
        #print data['fitEquation']

    #if data['fitParameterMap']:
        #print data['fitParameterMap']


    #y data is response data
    yarray = np.array([0.0,2.381,28.571,45.283,66.667,76.190,83.333,92.857,92.857,92.857])

    #x data is log concentration
    xarray = np.array([-10.0,-8.000,-7.523,-7.000,-6.523,-6.000,-5.523,-5.000,-4.523,-4.000])

    #initial parameters for fit
    params = Parameters()
    params.add('top',value=100,vary=True)
    params.add('bottom',value=0,vary=True)
    params.add('logec50',value=-6.912,vary=True)
    params.add('slope',value=1,vary=True)

    # This is the Hill Equation - the hill curve
    def hill(x, top=100, bottom=0, logec50=-6.912, slope=1):
        return bottom + ((top-bottom)/(1 + 10**((logec50-x)*slope)))

    gmod = Model(hill)
    #print gmod.param_names
    #print gmod.independent_vars

    #Change Fit parameters here for hill equation or float them all
    gmod.make_params()
    gmod.set_param_hint('top',value=100,vary=True)
    gmod.set_param_hint('bottom',value=0,vary=True)
    gmod.set_param_hint('logec50',value=-6.912,vary=True)
    gmod.set_param_hint('slope',value=-1,vary=True)
    result = gmod.fit(yarray, x=xarray)

    numFitPoints = 30
    lowest = np.nanmin(xarray)
    lowestFloor = np.floor(lowest)
    highest = np.nanmax(xarray)
    highestCeil = np.ceil(highest)
    step = (highest - lowest) / numFitPoints
    xfitted  = np.arange(lowestFloor,highestCeil,step).tolist()

    #Best parameters for fit
    fitarray1 = hill(xfitted,result.best_values['top'],result.best_values['bottom'],result.best_values['logec50'],result.best_values['slope'])
    yfitted = fitarray1.tolist()

    pointsArray = []
    for idx, val in enumerate(xfitted):
        pointsArray.append(Point(val,yfitted[idx],idx))

    logging.debug("Mark2")
    responseId = dict({"id" : id})
    logging.debug("Mark3")
    experimentPoints = dict({"experimentPoints" : points})
    logging.debug("Mark4")
    curveFitPoints = dict({"curveFitPoints" : [ pt.__dict__ for pt in pointsArray]})
    fitParameters = dict({"fitParameterMap" : {  "top" :{ "value" : result.best_values['top'], "name" : "top", "status" : "FLOAT"} ,
                                             "bottom" :{ "value" : result.best_values['bottom'], "name" : "bottom", "status" : "FLOAT"},
                                             "logec50" :{ "value" : result.best_values['logec50'], "name" : "logec50", "status" : "FLOAT"},
                                             "slope" :{ "value" : result.best_values['slope'], "name" : "slope", "staus" : "FLOAT"}}})



    #print json.dumps(curveFitPoints)
    #print json.dumps(fitParameters)
    logging.debug("Mark")
    return buildResponse(responseId, fitParameters,curveFitPoints)



if __name__ == "__main__":

    logging.basicConfig(filename='example.log',level=logging.DEBUG)

    try:
        result = main()
        logging.debug("Result =>" + result)
        print result

    except:
        print "Error"
        e = sys.exc_info()[0]
        print( "<p>Error: %s</p>" % e )