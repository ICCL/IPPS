$(function() {
    var time = 5000;
    var Run, Title;
    var getNodeUrl = $.getJSON('/ipps/index/ajaxNodeUrl');

    var TitleUnit = {humidity: {unit: '%', title: 'Humidity'}, light: {unit: 'lm', title: 'Light'},
                    soil: {unit: '%', title: 'Soil'}, temperature: {unit: '℃', title: 'Temperature'}};

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

    //Title = getTitle($(this).attr('href'));
    Title = $('input[type="hidden"]').val();
    //Original_fun();
    updateChart();

    function Original_fun () {
        Run = setInterval(draw, 200);
        setTimeout(function() {
            destroy();
            Run = setInterval(draw, time);
        }, 300);
    }

    function updateChart() {
        draw();
    }

    function getTitle(href) {
        return href.substr(1, href.lenght);
    }

    function destroy() {
        clearInterval(Run);
    }

    function draw() {
        $.getJSON('/ipps/'+ Title +'s/json/',{}).done(function(response) {
            var data = new Array();
            var safety = new Array()
            $.each(response.data, function(key, val) {
                datetime = val.create_at.split(' ');
                date = datetime[0].split('-');
                time = datetime[1].split(':');
                data.push([Date.UTC(date[0], date[1]-1, date[2], time[0], time[1]), parseInt(val.value)]);
                safety.push([Date.UTC(date[0], date[1]-1, date[2], time[0], time[1]), parseInt(response.safety.value)]);
            });
            $('#chart_'+Title).highcharts({
                chart: {
                    type: 'spline'
                },
                title: {
                    text: ' '//TitleUnit[Title].title
                },
                subtitle: {
                    //text: 'Source: WorldClimate.com'
                },
                xAxis: {
                    type: 'datetime'
                },
                yAxis: {
                    title: {
                        text: TitleUnit[Title].title + '(' + TitleUnit[Title].unit + ')'
                    }
                },
                tooltip: {
                    xDateFormat: 'Date: %Y-%m %H:%M',
                    shared: true,
                    crosshairs: true,
                    valueSuffix: TitleUnit[Title].unit,
                },
                series: [{
                    name: TitleUnit[Title].title,
                    data: data
                }, {
                    name: 'Safety',
                    data: safety,
                    color: '#DC3912'
                }]
            });
        });
    }
});
