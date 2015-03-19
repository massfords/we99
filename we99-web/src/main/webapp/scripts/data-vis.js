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
    colorOne: "#edf8b1",
    colorTwo: "#2c7fb8"
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
  return d3.scale.linear()
    .range([defaults.colorOne, defaults.colorTwo])
    .domain([min, max]);

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
      margin: 0,
      fixedsize_x: null,
      fixedsize_y: null
    },
    cellFormat:{
      cellMargin: 1,
      cellSize: 18
    },
    onCellClick: null
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

  // Compute missing input
  if(!params.colorScale){
    params.colorScale = this.colorScale({
      data: params.data.map(function(d) {return d.value; })
    });
  };

  // If fixed size is enabled then derive the cell size to fit the space.
  if(params.mapFormat.fixedsize_x & params.mapFormat.fixedsize_y) {

    // Determine number of rows and columns.
    var rows = d3.max(params.data.map(function(d) {return d.row; })) + 1;
    var cols = d3.max(params.data.map(function(d) {return d.col; })) + 1;

    if( ( params.mapFormat.fixedsize_x * params.cellFormat.cellMargin ) / rows >
      ( params.mapFormat.fixedsize_y * params.cellFormat.cellMargin) / cols ) {
      // Rows are the limiting factor... so fit to rows.
      params.cellFormat.cellSize = ( (params.mapFormat.fixedsize_x + (params.mapFormat.margin * 2) ) / rows ) ;
    }else{
      // They are equally spaced or cols are the limiting factor.
      params.cellFormat.cellSize = ( (params.mapFormat.fixedsize_y + (params.mapFormat.margin * 2) ) / cols) ;
    }

  }
  params.cellFormat.itemSize = params.cellFormat.cellMargin + params.cellFormat.cellSize;

  // Draw rectangles
  var rect = displaySvg.selectAll('rect')
    .data(params.data).enter()
    .append('rect')
    .attr('width', params.cellFormat.cellSize )
    .attr('height', params.cellFormat.cellSize )
    .attr('x',function(d) {
      return params.mapFormat.margin + (params.cellFormat.itemSize * d.col);
    })
    .attr('y',function(d) { return params.mapFormat.margin + (params.cellFormat.itemSize * d.row); })
    .attr('fill',function(d) { return params.colorScale(d.value); })
    .attr("style", function(d) {
      if (d.wellType === "CONTROL"){
        return "stroke-width:1;stroke:rgb(0,255,0)";
      }
      else if(!d.included) {
        return "stroke-width:1;stroke:rgb(255,0,0)";
      }
      else {
        return "stroke-width:1;stroke:rgb(192,192,192)";
      }
    })
    .on('click', function(d) { if(params.onCellClick) {params.onCellClick(d);} } );

  // If there is a title render it.
  if(params.title) {

    var maxRow = d3.max(params.data.map(function(d) {return d.row; })) + 1;

    var height = ( maxRow * ( params.cellFormat.cellSize + params.cellFormat.cellMargin ) )
      + (params.mapFormat.margin * 2)
      + 16;

    displaySvg.selectAll('text')
      .data([params]).enter()
      .append('text')
      .attr('x', params.mapFormat.margin )
      .attr('y', height )
      .text(function (d) { return params.title; })
      .attr("class","heatmap-title");
  }
};

DataVis.prototype.addDarkOverlay = function(params){

  var defaults = {
    color: 'rgba(255, 211, 155, 0.4)',
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
