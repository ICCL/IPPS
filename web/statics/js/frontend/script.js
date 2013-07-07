google.load("visualization", "1", {packages:["corechart"]});
google.setOnLoadCallback(drawChart);
function drawChart() {
  $(function() {
    var time = 5000, Run, RunStatus = false, Title;
    var NodeJsUrl = '';

    var getNodeUrl = $.getJSON('/ipps/index/ajaxNodeUrl');

    var UnitArr = {Humidity: '%', Light: 'lm', Soil: '%', Temperature: '℃'};
    $.when(getNodeUrl)
    .done(function(response) {
        var socket = io.connect(response.url);
        //.on()為socket的接收端，client端預設的key值是connect
        socket.on('connect', function () {
            socket.on('update', function() {
                if(RunStatus) {
                    updateChart();
                }
            });
        });
    });

    $('ul li a').on('click', function() {
        RunStatus = true;
        destroy();
        Title = getTitle($(this).attr('href'));

        //Original_fun();
        updateChart();
    });

    function Original_fun () {
        Run = setInterval(draw, 200);
        setTimeout(function() {
            destroy();
            Run = setInterval(draw, time);
        }, 300);
    }

    function updateChart() {
        Run = setInterval(draw, 200);
        setTimeout(function() {
            destroy();
            clearTimeout();
        }, 300);
    }

    function getTitle(href) {
        return href.substr(1, href.lenght);
    }

    function destroy() {
        clearInterval(Run);
    }

    function draw() {
      var chartTitle = ['Date', Title, 'Safety'];
      var chartData = new Array(chartTitle);
      $.getJSON('/ipps/'+ Title +'s/json/',{})
      .done(function( response ) {
        var safety = response['safety']['value'];
        $.each( response['data'], function( i, item ) {
          chartData.push([item['create_at'], parseInt(item['value']), parseInt(safety)]);
        });
        var data = google.visualization.arrayToDataTable(chartData);
        var options = {
          title: Title,
          vAxis: {title: Title + '(' + UnitArr[Title] + ')'},
          hAxis: {title: ''}
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
