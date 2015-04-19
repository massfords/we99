function DataVis(){
    this.debug = false;

  this.log = function(d){
    if(this.debug){
      console.log(d);
    }
  }
}

/**
 * Creates a color scale for the supplied parameters. Expects,
 *
 * params = {
     *   colorOne: the first color in the scale [defaults to "#edf8b1"],
     *   colorTwo: the second color in the scale [defaults to "#2c7fb8"],
     *   data: array of numbers that make up the scale [required]
     * }
 *
 * @param params The parameters which make up the scale.
 *
 * @returns a d3 linear color scale
 */
DataVis.prototype.colorScale =  function(params){

  // Hash defaults.
  var defaults = {
    data: null,
    colorOne: "#1f78b4",
    colorTwo: "#b2df8a"
  };
  $.extend(true, defaults, params );
  params = defaults;

  this.log(params);

  // Make sure params are complete.
  if(!params.data){
    throw "params.data is required";
  }

  // Create Scale.
  var min = d3.min(params.data);
  var max = d3.max(params.data);
  return {
    scale: d3.scale.linear().range([defaults.colorOne, defaults.colorTwo]).domain([min, max]),
    min: min,
    max: max
  };

};

/**
 * Render a single heat map from the supplied parameters in the supplied
 * location. Expects,
 *
 * params = {
 *    location: the location to render in [required: this must be a SVG element],
 *    data: the data to be used to render the heatmap [required, format follows],
 *    colorScale: the color scale to use to render the heat map [defaults to a default green scale]
 *    mapClass: the class of the parent svg.
 *    title: the title to be display [default to no title]
 *    mapFormat: {
 *      margin: margin around the heatmap [defaults 0],\
        fixedsize_x: fixes the x access of the heatmaps size [defaults to null and off],
        fixedsize_y: fixes the y access of the heatmaps size [defaults to null and off]
 *    },
 *    cellFormat:{
 *      cellMargin: the amount of margin around a cell [defaults to 1 px],
 *      cellSize: the size of a cell [defaults to 18 px].
 *    },
 *    onCellClick: a call back function for when the cell is clicked.
 *  }
 *
 * dataformat = {
     *  row: rowindex for the data point
     *  col: columnindex for the data point
     *  value: value of the data point
     *  included: whether or not this well is included
     * }
 *
 * @param params
 */
DataVis.prototype.renderSingleHeatMap = function(params){

  console.log(params);
  // Merge in defaults.
  var defaults = {
    mapClass: "heatmap",
    location: null,
    data: null,
    colorScale: null,
    title: null,
    mapFormat: {
      margin_x: 2,
      margin_y: 2,
      fixedsize_x: null,
      fixedsize_y: null
    },
    cellFormat:{
      cellMargin: 3,
      cellSize: 30
    },
    onCellClick: null,
    onMouseDown: null,
    onMouseUp: null,
    onMouseMove: null,
    rowLabels: null,
    colLabels: null
  };
  $.extend(true, defaults, params );
  params = defaults;

  this.log(params);


  // Validate input
  if(!params.data){ throw "params.data is required"; }
  var displaySvg =
    d3.select(params.location)
      .append('g')
      .attr('class', params.mapClass);

  // Compute color scale if missing.
  if(!params.colorScale){
    params.colorScale = this.colorScale({
      data: params.data.map(function(d) {return d.value; })
    });
  };

  // Determine number of rows and columns.
  var rows = d3.max(params.data.map(function(d) {return d.row; })) + 1;
  var cols = d3.max(params.data.map(function(d) {return d.col; })) + 1;

  // If fixed size is enabled then derive the cell size to fit the space.
  if(params.mapFormat.fixedsize_x & params.mapFormat.fixedsize_y) {

    if(  params.mapFormat.fixedsize_x / cols <
         params.mapFormat.fixedsize_y / rows ) {
      // Cols are the limiting factor.
      params.cellFormat.cellSize = ( (params.mapFormat.fixedsize_x  + (params.mapFormat.margin_x * 2) ) / cols ) -  params.cellFormat.cellMargin;
    }else{
      // Rows are the limiting factor.
      params.cellFormat.cellSize = ( (params.mapFormat.fixedsize_y + (params.mapFormat.margin_y * 2) ) / rows) -  params.cellFormat.cellMargin;
    }

  }else if(params.mapFormat.fixedsize_x){
    params.cellFormat.cellSize = ( (params.mapFormat.fixedsize_x  + (params.mapFormat.margin_x * 2) ) / cols ) -  params.cellFormat.cellMargin;
  }
  else if(params.mapFormat.fixedsize_y){
    params.cellFormat.cellSize = ( (params.mapFormat.fixedsize_y + (params.mapFormat.margin_y * 2) ) / rows) -  params.cellFormat.cellMargin;
  }

  params.cellFormat.cellMargin = params.cellFormat.cellMargin / 10;
  params.cellFormat.itemSize = params.cellFormat.cellMargin + params.cellFormat.cellSize;

  // Write row labels.
  var rowTextWidth = 0;
  if(params.rowLabels){

    displaySvg.selectAll("text.rowlabels")
      .data(params.rowLabels).enter()
      .append("text")
      .attr("class", "rowlabels")
      .text(function(d) {
        rowTextWidth = Math.max(rowTextWidth, d.length);
        return d;
      })
      .attr("x", function(d){
        return params.mapFormat.margin_x;
      })
      .attr("y", function(d, i){
        return (i * params.cellFormat.itemSize) + 16 + params.mapFormat.margin_y;
      })
      .attr("font-family", "Arial Black")
      .attr("font-size", function(){ return params.cellFormat.itemSize / 2.5;})
      .style("text-anchor", "right");

    rowTextWidth = rowTextWidth * 4;
  }
  params.mapFormat.margin_y = params.mapFormat.margin_y + rowTextWidth;

  // Col labels
  var colHeight = 0;
  if(params.colLabels){

    displaySvg.selectAll("text.collabels")
      .data(params.colLabels).enter()
      .append("text")
      .attr("class", "collabels")
      .text(function(d) {return d;})
      .attr("x", function(d, i){

        return (i * params.cellFormat.itemSize) + 16 + params.mapFormat.margin_x;

      })
      .attr("y", function(d, i){
        return params.mapFormat.margin_y;

      })
      .attr("font-family", "Arial Black")
      .attr("font-size", function(){ return params.cellFormat.itemSize / 2.5;})
      .style("text-anchor", "middle")

    colHeight = 16;
  }
  params.mapFormat.margin_x = params.mapFormat.margin_x + colHeight;


  // Draw rectangles
  var rect = displaySvg.selectAll('rect')
    .data(params.data).enter()
    .append('rect')
    .attr('width', params.cellFormat.cellSize )
    .attr('height', params.cellFormat.cellSize )
    .attr('x',function(d) {
      return params.mapFormat.margin_x + (params.cellFormat.itemSize * d.col);
    })
    .attr('y',function(d) {
      return params.mapFormat.margin_y + (params.cellFormat.itemSize * d.row);
    })
    .attr('fill',function(d) { return params.colorScale(d.value); })
    .attr("style", function(d) {
      if(!d.included) {
        return "stroke-width:3;stroke:rgb(203,24,29)";
      }else if (d.wellType === "NEG_CONTROL" || d.wellType === "POS_CONTROL"){
        return "stroke-width:3;stroke:rgb(82,82,82)";
      }else {
        return "stroke-width:2;stroke:rgb(192,192,192)";
      }
    })
    .on('click', function(d) { if(params.onCellClick) {params.onCellClick(d);} } )
    .on('mouseup', function(d) { if(params.onMouseUp) {params.onMouseUp(d); } } )
    .on('mousedown', function(d) {if(params.onMouseDown) { params.onMouseDown(d);} })
    .on('mouvemove', function() { if(params.onMouseMove) { params.onMouseMove();}})
    .append("title")
    .text(function(d) {
      return "Well " + d.col + "x" + d.row + " in plate " + d.plate;
    });

  // Add Text

  displaySvg.selectAll('text.well-label')
    .data(params.data).enter()
    .append('text')
    .attr("x", function(d){
      return (params.mapFormat.margin_x + (params.cellFormat.itemSize * d.col) + (params.cellFormat.itemSize / 2));
    })
    .attr("y", function(d){
      return (params.mapFormat.margin_y + (params.cellFormat.itemSize * d.row)) + (params.cellFormat.itemSize / 2);
    })
    .attr("font-family", "Arial Black")
    .attr("font-size", function(){ return params.cellFormat.itemSize / 2.5;})
    .style("text-anchor", "middle")
    .text(function(d){
      if(!d.included & d.wellType === "NEG_CONTROL"){
        return "E/NC";
      }else if(!d.included & d.wellType === "POS_CONTROL"){
        return "E/PC";
      }else if(d.wellType === "NEG_CONTROL"){
        return "NC";
      }else if(d.wellType === "POS_CONTROL"){
        return "PC";
      }else if(!d.included){
        return "E";
      }else{
        return null;
      }
    })
    .attr("class","well-label")
    .on('click', function(d) { if(params.onCellClick) {params.onCellClick(d);} } )
    .on('mouseup', function(d) { if(params.onMouseUp) {params.onMouseUp(d); } } )
    .on('mousedown', function(d) {if(params.onMouseDown) { params.onMouseDown(d);} })
    .on('mouvemove', function() { if(params.onMouseMove) { params.onMouseMove();}});

  // If there is a title render it.

  if(params.title) {

    var maxRow = d3.max(params.data.map(function(d) {return d.row; })) + 1;
    var maxCol = d3.max(params.data.map(function(d) {return d.col; })) + 1;

    var y = ( maxRow * ( params.cellFormat.cellSize + params.cellFormat.cellMargin ) )
      + (params.mapFormat.margin_y * 2) + 12;

    var x = params.mapFormat.margin_x +
      ( maxCol * ( params.cellFormat.cellSize + params.cellFormat.cellMargin ) ) / 2;

    displaySvg.selectAll('text.heatmap-title')
      .data([params]).enter()
      .append('text')
      .attr('x', x  )
      .attr('y', y )
      .text(function (d) { return params.title; })
      .attr("class","heatmap-title")
      .style("text-anchor", "middle");
  }

  return {
    x: params.mapFormat.margin_x,
    y: params.mapFormat.margin_y,
    width: params.cellFormat.itemSize * cols,
    height: params.cellFormat.itemSize * rows
  };

};

DataVis.prototype.addDarkOverlay = function(params){
  var defaults = {
    color: 'rgba(0, 0, 0, 0.2)',
    offset_x: 0,
    offset_y: 0,
    width: 100,
    height: 100
  };
  $.extend(true, defaults, params );
  params = defaults;

  d3.select(params.location)
    .append("rect")
    .attr("fill", params.color)
    .attr("x", params.offset_x)
    .attr("y", params.offset_y)
    .attr("width", params.width)
    .attr("height", params.height);

};

DataVis.prototype.heatMapColorGuide = function(params){

  var iterations = 5;
  var values = [];
  for(var i = 0; i < iterations; i++){
    values.push(params.min + (i * ((params.max - params.min) / iterations)) );
  }
  values.push(params.max);

  var loc = d3.select(params.location).html("");

  loc.selectAll("rect")
    .data(values).enter()
    .append("rect")
    .attr("width","20")
    .attr("height","20")
    .attr("x", "5")
    .attr("y", function(d,i) {return i * 24; })
    .attr("fill", function(d) { return params.colorScale(d); });

  loc.selectAll("text")
    .data(values).enter()
    .append("text")
    .attr("x","30")
    .attr("y", function(d,i) {return (i * 24) + 16; })
    .text(function(d) {return Math.round(d * 1000) / 1000;})
    .style("text-anchor", "left");
};

DataVis.prototype.renderScatterPlot = function(params) {
  var defaults = {
      width: 600,
      height: 600,
      xScaleIsDate: true,
      scaleX: null,
      scaleY: null,
      axisTitle: {
        x: "",
        y: ""
      },
      lineFunction: null
  };


  $.extend(true, defaults, params);
  params = defaults;

  if(!params.scaleY){
    params.scaleY = {min: null, max: null};
    params.scaleY.min = d3.min(params.data.map(function (d) { return d.value; }));
    params.scaleY.max = d3.max(params.data.map(function (d) { return d.value; }));
  }

  var yScale = d3.scale.linear()
    .domain([params.scaleY.max, params.scaleY.min])
    .range([50, (params.height - 50 )]);

  var yAxis = d3.svg.axis()
    .scale(yScale).orient("left");

  if(!params.scaleX){
    params.scaleX = {min: null, max: null};
    if(params.xScaleIsDate){
      params.scaleX.min = new Date(d3.min(params.data.map(function (d) { return d.date; })));
      params.scaleX.max = new Date(d3.max(params.data.map(function (d) { return d.date; })));
    }else{
      params.scaleX.min = d3.min(params.data.map(function (d) { return d.amount; }));
      params.scaleX.max = d3.max(params.data.map(function (d) { return d.amount; }));
    }
  }

  var xScale = null;
  if (params.xScaleIsDate) {
    xScale = d3.time.scale()
      .domain([params.scaleX.min, params.scaleX.max])
      .range([50, (params.width - 50 )]);
  }else{
    xScale = d3.svg.axis().scale()
      .domain([ params.scaleX.min, params.scaleX.max ])
      .range([50, (params.width - 50 )]);
  }

  var xAxis = d3.svg.axis()
    .scale(xScale)
    .orient("bottom")
    .ticks(5);

  if(params.xScaleIsDate){
    xAxis.tickFormat(d3.time.format('%m-%d-%y %H:%M'));
  }

  var svg = d3.select(params.location);

  // x-axis
  svg.append("g")
    .attr("class", "x axis")
    .attr("transform", "translate(0," + (params.height - 30) + ")")
    .call(xAxis)
    .append("text")
    .attr("class", "label")
    .attr("x", params.width - 10)
    .attr("y", -6)
    .style("text-anchor", "end")
    .text(params.axisTitle.x);

  // y-axis
  svg.append("g")
    .attr("class", "y axis")
    .attr("transform", "translate(30,0)")
    .call(yAxis)
    .append("text")
    .attr("class", "label")
    .attr("transform", "rotate(-90)")
    .attr("y", 6)
    .attr("dy", ".71em")
    .style("text-anchor", "end")
    .text(params.axisTitle.y);


  var scatterPlot = svg.append("g");

  scatterPlot.selectAll(".dot")
    .data(params.data)
    .enter().append("circle")
    .attr("class","dot")
    .attr("r", 4.5)
    .attr("cx", function(d) {
      if(params.xScaleIsDate) {
        return xScale(d.date);
      } else {
        return xScale(d.amount);
      }
    })
    .attr("cy", function(d) {return yScale(d.value);} )
    .style("fill", function(d) {
      if (d.wellType === "NEG_CONTROL"){
        return "rgb(239,237,245)";
      }else if (d.wellType === "POS_CONTROL"){
        return "rgb(117,107,177)";
      }else{
        return "rgb(255,255,255)";
      }
    })
    .attr("stroke-width", 2)
    .attr("stroke", function(d) {
      if(d.included){
        return "black";
        var xs = d3.scale.linear()
          .domain([params.scaleX.min, params.scaleX.max])
          .range([50, params.width]);

        var ys = d3.scale.linear()
          .domain([params.scaleY.min, params.scaleY.max])
          .range([params.height - 50, 0]);

        var points = [];
        var diff = ( params.scaleX.max - params.scaleX.min ) / 200;
        var x = params.scaleX.min;
        var y = params.lineFunction(x);
        while(x < params.scaleX.max & y < params.scaleY.max){
          points.push({
            x: x,
            y: params.lineFunction(x)
          });
          x += diff;
          y = params.lineFunction(x);
        }


        var lineFunction = d3.svg.line()
          .x(function(d) { return  xs(d.x); })
          .y(function(d) { return ys(d.y); })
          .interpolate("basis");

        svg.append("path").attr("d", lineFunction(points))
          .attr("stroke", "blue")
          .attr("stroke-width", 2)
          .attr("fill", "none");

      }else{
        return "red";
      }
    })
    .on('click', function(d) { if(params.onCellClick) {params.onCellClick(d);} } );

    if(params.lineFunction != null){
      this.renderLine({
        hasAxis: false,
        width: params.width,
        height: params.height,
        location: params.location,
        scaleX: params.scaleX,
        scaleY: params.scaleY,
        lineFunction: params.lineFunction,
        color: "blue"
      });
    }
};

DataVis.prototype.renderLine = function(params) {

  var svg = d3.select(params.location);

  var xs = d3.scale.linear()
    .domain([params.scaleX.min, params.scaleX.max])
    .range([50, params.width]);

  var ys = d3.scale.linear()
    .domain([params.scaleY.min, params.scaleY.max])
    .range([params.height - 50, 0]);

  var points = [];
  var diff = ( params.scaleX.max - params.scaleX.min ) / 200;
  var x = params.scaleX.min;
  var y = params.lineFunction(x);
  while(x < params.scaleX.max & y < params.scaleY.max){
    points.push({
      x: x,
      y: params.lineFunction(x)
    });
    x += diff;
    y = params.lineFunction(x);
  }

  if(params.hasAxis){

    var yScale = d3.scale.linear()
      .domain([params.scaleY.max, params.scaleY.min])
      .range([50, (params.height - 50 )]);

    var yAxis = d3.svg.axis()
      .scale(yScale).orient("left");

    var xScale = d3.svg.axis().scale()
      .domain([ params.scaleX.min, params.scaleX.max ])
      .range([50, (params.width - 50 )]);

    var xAxis = d3.svg.axis()
      .scale(xScale)
      .orient("bottom")
      .ticks(5);


    // x-axis
    svg.append("g")
      .attr("class", "x axis")
      .attr("transform", "translate(0," + (params.height - 30) + ")")
      .call(xAxis)
      .append("text")
      .attr("class", "label")
      .attr("x", params.width - 10)
      .attr("y", -6)
      .style("text-anchor", "end")
      .text(params.axisTitle.x);

    // y-axis
    svg.append("g")
      .attr("class", "y axis")
      .attr("transform", "translate(30,0)")
      .call(yAxis)
      .append("text")
      .attr("class", "label")
      .attr("transform", "rotate(-90)")
      .attr("y", 6)
      .attr("dy", ".71em")
      .style("text-anchor", "end")
      .text(params.axisTitle.y);
  }

  var line = d3.svg.line()
    .x(function(d) { return  xs(d.x); })
    .y(function(d) { return ys(d.y); })
    .interpolate("basis");

  svg.append("path").attr("d", line(points))
    .attr("stroke", params.color)
    .attr("stroke-width", 2)
    .attr("fill", "none");
};

DataVis.prototype.convertPlateResultData = function(plateResults){


  var plateResults = plateResults.map(function(plateResult){

    // Create well array.
    var wd = [];
    var row = 0;
    var col = 0;

    for(row = 0; row < plateResult.plate.plateType.dim.rows; row++){
      wd.push([]);
      for(col = 0; col < plateResult.plate.plateType.dim.cols; col++){
        wd[row].push({ row: row, col: col });
      }
    }

    // Build array.
    plateResult.wellResults.forEach(function(wr){

      row = wr.coordinate.row;
      col = wr.coordinate.col;

      if(wr.resultStatus === "INCLUDED"){
        wd[row][col].included = true;
      }else{
        wd[row][col].included = false;
      }

      wd[row][col].value = wr.samples[0].value;

      wd[row][col].date = Date.parse(wr.samples[0].measuredAt);

    });
    plateResult.plate.wells.forEach(function(well){

      row = well.coordinate.row;
      col = well.coordinate.col;

      var wellType;
      switch(well.type){
        case "COMP": wellType = "NORMAL"; break;
        case "NEGATIVE": wellType = "NEG_CONTROL"; break;
        case "POSITIVE": wellType = "POS_CONTROL"; break;
      }

      wd[row][col].wellType = wellType;
      wd[row][col].wellIndex = well.id;

      wd[row][col].compound = well.contents[0].compound.name;
      wd[row][col].amount = well.contents[0].amount.number;

    });

    var dataSet = [];
    wd.forEach(function(row) {row.forEach(function(d) { dataSet.push(d); })} );

    return {
      data: dataSet,
      name: plateResult.plate.barcode,
      z: 0,
      z_prime: 0,
      pos_avg: 0,
      neg_avg: 0
    };
  });

  console.log(plateResults);

  return plateResults;

};

DataVis.prototype.getDummmyPlateData = function (){

  var dummyData = {
      "curveFitPoints": [
        {
          "x": -10.0,
          "y": -2.9667803082152915,
          "sequenceNumber": 0
        },
        {
          "x": -9.8,
          "y": -2.7946475022487647,
          "sequenceNumber": 1
        },
        {
          "x": -9.600000000000001,
          "y": -2.546932084721604,
          "sequenceNumber": 2
        },
        {
          "x": -9.400000000000002,
          "y": -2.191138713075702,
          "sequenceNumber": 3
        },
        {
          "x": -9.200000000000003,
          "y": -1.6815378878378766,
          "sequenceNumber": 4
        },
        {
          "x": -9.000000000000004,
          "y": -0.9545507691536557,
          "sequenceNumber": 5
        },
        {
          "x": -8.800000000000004,
          "y": 0.0766644892373165,
          "sequenceNumber": 6
        },
        {
          "x": -8.600000000000005,
          "y": 1.527662540960364,
          "sequenceNumber": 7
        },
        {
          "x": -8.400000000000006,
          "y": 3.5463097561312225,
          "sequenceNumber": 8
        },
        {
          "x": -8.200000000000006,
          "y": 6.310824560340265,
          "sequenceNumber": 9
        },
        {
          "x": -8.000000000000007,
          "y": 10.016298678373417,
          "sequenceNumber": 10
        },
        {
          "x": -7.800000000000008,
          "y": 14.842476735496533,
          "sequenceNumber": 11
        },
        {
          "x": -7.6000000000000085,
          "y": 20.89863082599834,
          "sequenceNumber": 12
        },
        {
          "x": -7.400000000000009,
          "y": 28.152988781234825,
          "sequenceNumber": 13
        },
        {
          "x": -7.20000000000001,
          "y": 36.373967919509774,
          "sequenceNumber": 14
        },
        {
          "x": -7.000000000000011,
          "y": 45.12503131882727,
          "sequenceNumber": 15
        },
        {
          "x": -6.800000000000011,
          "y": 53.84098415416858,
          "sequenceNumber": 16
        },
        {
          "x": -6.600000000000012,
          "y": 61.965505775989094,
          "sequenceNumber": 17
        },
        {
          "x": -6.400000000000013,
          "y": 69.08423980422725,
          "sequenceNumber": 18
        },
        {
          "x": -6.2000000000000135,
          "y": 74.99125851865332,
          "sequenceNumber": 19
        },
        {
          "x": -6.000000000000014,
          "y": 79.67538410303582,
          "sequenceNumber": 20
        },
        {
          "x": -5.800000000000015,
          "y": 83.25791597380483,
          "sequenceNumber": 21
        },
        {
          "x": -5.600000000000016,
          "y": 85.92289736413555,
          "sequenceNumber": 22
        },
        {
          "x": -5.400000000000016,
          "y": 87.86466500443292,
          "sequenceNumber": 23
        },
        {
          "x": -5.200000000000017,
          "y": 89.25821830795377,
          "sequenceNumber": 24
        },
        {
          "x": -5.000000000000018,
          "y": 90.24749981007409,
          "sequenceNumber": 25
        },
        {
          "x": -4.8000000000000185,
          "y": 90.94437188085135,
          "sequenceNumber": 26
        },
        {
          "x": -4.600000000000019,
          "y": 91.43259063179673,
          "sequenceNumber": 27
        },
        {
          "x": -4.40000000000002,
          "y": 91.77332258435578,
          "sequenceNumber": 28
        },
        {
          "x": -4.200000000000021,
          "y": 92.01048724895065,
          "sequenceNumber": 29
        }
      ]
    };

  var NOISE = 0.1;

  function calPoint(x){
    return  1/(1+Math.exp(-.07 * x));
  }

  var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

  var dataSet = [];
  var index = 0;
  for (var num = 0; num < 150; num++) {
    var array = [];
    for (var i = 0; i < 9; i++) {
      for (var j = 0; j < 9; j++) {


        array.push({
          wellIndex: index++,
          row: i,
          col: j,
          amount: (Math.random() * 200),
          included: true,
          wellType: "NORMAL",
          compound: possible.charAt(Math.floor(Math.random() * possible.length)),
          date: new Date() - (Math.random() * 20000000)
        });

        if(Math.random() < 0.10){
          array[array.length - 1].value = Math.random();
        }else{
          array[array.length - 1].value = Math.abs(calPoint(array[array.length - 1].amount - 100) + (NOISE * Math.random()) - NOISE);
        }

        if(Math.random() <= 0.025){
          array[array.length - 1].wellType = "NEG_CONTROL";
          array[array.length - 1].value = 0.0 + (0.1 * Math.random());
        }else  if(Math.random() <= 0.05){
          array[array.length - 1].wellType = "POS_CONTROL";
          array[array.length - 1].value = 1.0 - (0.1 * Math.random());
        }
      }
    }
    dataSet[num] = array;
  };

  return {
    dataSets: dataSet.map(function (d) {
      var result = {
        data: d,
        name: "A" + String(Math.round(Math.random() * 400)),
        z: Math.round(Math.random() * 100) / 100,
        z_prime: Math.round(Math.random() * 100) / 100,
        pos_avg: Math.round(Math.random() * 100) / 100,
        neg_avg: Math.round(Math.random() * 100) / 100
      };
      result.data.forEach(function(d) {d.plate = result.name;});
      return result;
    })
  };
};

DataVis.prototype.linear_regression = function (){
  var linreg = {},
    data = [];

  // Assign data to the model. Data is assumed to be an array.
  linreg.data = function(x) {
    if (!arguments.length) return data;
    data = x.slice();
    return linreg;
  };

  // Calculate the slope and y-intercept of the regression line
  // by calculating the least sum of squares
  linreg.mb = function() {
    var m, b;

    // Store data length in a local variable to reduce
    // repeated object property lookups
    var data_length = data.length;

    //if there's only one point, arbitrarily choose a slope of 0
    //and a y-intercept of whatever the y of the initial point is
    if (data_length === 1) {
      m = 0;
      b = data[0][1];
    } else {
      // Initialize our sums and scope the `m` and `b`
      // variables that define the line.
      var sum_x = 0, sum_y = 0,
        sum_xx = 0, sum_xy = 0;

      // Use local variables to grab point values
      // with minimal object property lookups
      var point, x, y;

      // Gather the sum of all x values, the sum of all
      // y values, and the sum of x^2 and (x*y) for each
      // value.
      //
      // In math notation, these would be SS_x, SS_y, SS_xx, and SS_xy
      for (var i = 0; i < data_length; i++) {
        point = data[i];
        x = point[0];
        y = point[1];

        sum_x += x;
        sum_y += y;

        sum_xx += x * x;
        sum_xy += x * y;
      }

      // `m` is the slope of the regression line
      m = ((data_length * sum_xy) - (sum_x * sum_y)) /
      ((data_length * sum_xx) - (sum_x * sum_x));

      // `b` is the y-intercept of the line.
      b = (sum_y / data_length) - ((m * sum_x) / data_length);
    }

    // Return both values as an object.
    return { m: m, b: b };
  };

  // a shortcut for simply getting the slope of the regression line
  linreg.m = function() {
    return linreg.mb().m;
  };

  // a shortcut for simply getting the y-intercept of the regression
  // line.
  linreg.b = function() {
    return linreg.mb().b;
  };

  // ## Fitting The Regression Line
  //
  // This is called after `.data()` and returns the
  // equation `y = f(x)` which gives the position
  // of the regression line at each point in `x`.
  linreg.line = function() {

    // Get the slope, `m`, and y-intercept, `b`, of the line.
    var mb = linreg.mb(),
      m = mb.m,
      b = mb.b;

    // Return a function that computes a `y` value for each
    // x value it is given, based on the values of `b` and `a`
    // that we just computed.
    return function(x) {
      return b + (m * x);
    };
  };

  return linreg;
}
