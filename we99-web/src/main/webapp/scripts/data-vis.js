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

    if(  params.mapFormat.fixedsize_x / cols >
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
      .style("text-anchor", "right")

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
      }else if (d.wellType === "CONTROL"){
        return "stroke-width:3;stroke:rgb(82,82,82)";
      }
      else {
        return "stroke-width:2;stroke:rgb(192,192,192)";
      }
    })
    .on('click', function(d) { if(params.onCellClick) {params.onCellClick(d);} } )
    .on('mouseup', function(d) { if(params.onMouseUp) {params.onMouseUp(d); } } )
    .on('mousedown', function(d) {if(params.onMouseDown) { params.onMouseDown(d);} })
    .on('mouvemove', function() { if(params.onMouseMove) { params.onMouseMove();}});

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
      if(!d.included & d.wellType === "CONTROL"){
        return "E/C";
      }else if(d.wellType === "CONTROL"){
        return "C";
      }else if(!d.included){
        return "E";
      }else{
        return null;
      }
    })
    .attr("class","well-label")
    .on('click', function(d) { if(params.onCellClick) {params.onCellClick(d);} } );;

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
  console.log(params);

  var iterations = 5;
  var values = [];
  for(var i = 0; i < iterations; i++){
    values.push(params.min + (i * ((params.max - params.min) / iterations)) );
  }
  values.push(params.max);

  console.log(values);
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
}


DataVis.prototype.getDummmyPlateData = function (){

  var dataSet = [];
  for (var num = 0; num < 110; num++) {
    var array = [];
    var index = 0;
    for (var i = 0; i < 9; i++) {
      for (var j = 0; j < 9; j++) {

        array.push({
          wellIndex: index++,
          row: i,
          col: j,
          value: Math.random(),
          included: true,
          wellType: "NORMAL"
        });

        if(Math.random() <= 0.05){
          array[array.length - 1].wellType = "CONTROL";
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
      return result;
    })
  };
}
