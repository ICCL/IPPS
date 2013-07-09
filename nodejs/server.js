var port = 9669,
    express = require("express"),
    app = express(),
    http = require("http"),
    server = http.createServer(app),
    io = require('socket.io').listen(server);

server.listen(port);
console.log('Url: localhost:' + port);
console.log('Server running');

//.on()為socket的接收端，預設的key值是connection
io.sockets.on('connection', function (socket) {
    console.log('socket start');
    app.get('/chart_update', function(req, res){
        res.send('');
        socket.broadcast.emit('update');
    });

    console.log('start');
});
