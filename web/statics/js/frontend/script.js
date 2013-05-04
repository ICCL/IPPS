google.load("visualization", "1", {packages:["corechart"]});
google.setOnLoadCallback(drawChart);
function drawChart() {
  $(function() {
    var time = 5000;
    var Run, Title;

    Title = 'Humidity';
    //draw();
    $('ul li a').on('click', function() {
        destroy();
        Title = getTitle($(this).attr('href'));
        Run = setInterval(draw, 200);
        setTimeout(function() {
            destroy();
            Run = setInterval(draw, time);
        }, 300);
    });

    function getTitle(href) {
        return href.substr(1, href.lenght);
    }

    function destroy() {
        clearInterval(Run);
    }

    function draw() {
      var chartTitle = ['Date', Title];
      var chartData = new Array(chartTitle);
      $.getJSON('/ipps/'+ Title +'s/json/',{})
      .done(function( data ) {
        $.each( data, function( i, item ) {
          chartData.push([item['create_at'], parseInt(item['value'])]);
        });
        var data = google.visualization.arrayToDataTable(chartData);
        var options = {
          title: Title
        };
        var chart = new google.visualization.LineChart(document.getElementById('chart'+Title));
        chart.draw(data, options);
      })
      .fail(function( jqxhr, textStatus, error ) {
        var err = textStatus + ', ' + error;
        console.log( "Request Failed: " + err);
      });
    }

  });}
