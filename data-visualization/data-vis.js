function DataVis(){
    this.debug = true;
}

DataVis.prototype.heatMap = function(location, dataSet, configParams){

        var self = this;
        // Validate and report input if debug is enabled.
        if(self.debug){

            console.log("dvis.heatmap:")
            console.log(location);
            console.log(dataSet);
            console.log(configParams);

            if(!location){ throw "location can't be null."; }
            if(!dataSet) { throw "dataSet can't be null."; }
            if(!dataSet.colName) { throw "dataSet.colName can't be null."; }
            if(!dataSet.rowName) { throw "dataSet.rowName can't be null."; }
            if(!dataSet.data) { throw "dataSet.data can't be null."; }
            if(!dataSet.data.length === dataSet.colName.length) {throw "dataSet.data.legnth must equal dataSet.colName.length"};
            if(!dataSet.data[0].length === dataSet.rowName.length) {throw "dataSet.data[0].length must equal dataSet.rowName.length"};

        }

        // Mash in defaults.
        var config = {
            color: {
                light: 'lightgreen',
                dark: 'darkgreen',
                scaleMin: null,
                scaleMax: null
            },
            classes: {
                tableClass: "heatmap-table",
                dataCellClass: "heatmap-data-cell",
                rowClass: "heatmap-row",
                rowNameCellClass: "heatmap-row-cell",
                colHeaderClass: "heatmap-col-header",
                colCellClass: "heatmap-col-cell"
            }
        };
        $.extend(true, config, configParams );

        // Create Color Scale
        var min = config.color.scaleMin;
        if(min === null){
            min = d3.min(dataSet.data, function(row){ return d3.min(row) });
        }

        var max = config.color.scaleMax;
        if(max === null){
            max = d3.max(dataSet.data, function(row){ return d3.max(row) });
        }

        var colorScale = d3.scale.linear()
            .range([config.color.light, config.color.dark])
            .domain([min, max]) ;

        // Create table structure.
        var table = d3.select(location).append("table").attr("class", config.classes.tableClass),
            thead = table.append("thead"),
            tbody = table.append("tbody");

        // Create table header.
        var headerRow = thead.append("tr").
            attr("class", config.classes.colHeaderClass);

        headerRow.selectAll("th")
            .data(function(){ return [""].concat(dataSet.colName)})
            .enter()
            .append("th")
            .attr("class", config.classes.colCellClass)
            .text(function (c) { return c; });


        // Create table body.
        var rows = tbody.selectAll("tr")
            .data(dataSet.rowName)
            .enter()
            .append("tr")
            .attr("class", config.classes.rowClass);

        var rowCount = -1;
        rows.selectAll("td")
            .data(function(){
                rowCount++;

                var rowColors = dataSet.data[rowCount].map(
                    function(d){ return {
                        fill: colorScale(d),
                        text: null,
                        class: config.classes.dataCellClass
                    } ; }
                );

                return [ {
                    fill: null,
                    text: dataSet.rowName[rowCount],
                    class: config.classes.rowNameCellClass
                } ].concat(rowColors);
            })
            .enter()
            .append("td")
            .style("background-color", function(c) { return c.fill; } )
            .attr("class", function(c) {return c.class; })
            .text(function (c) { return c.text; } );

    }