const express = require('express');

const util = require('util');
const app = express();
const request = require('request');
app.get('/adminApprovalFacility',
	function (req, res) {
       res.send(req.body);
        
        
	}
)

app.get('/processreportedFacility',
	function (req, res) {
        var url = 'http://localhost:8081/adminApprovalFacility';
        request(url, function(err, response, body) {
            if (err != null) {
                var s = util.format("Error: %s", err);
                console.log(s);
                res.send(s);
                return;
            }
            console.log(body);
            res.send(body);
            });          
	}
)

var server = app.listen(
    8081,
    function () {
	var host = server.address().address;
	var port = server.address().port;
	console.log("Example app listening at http://%s:%s" , host, port);
    });
