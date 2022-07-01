app.get('/adminApprovalFacility',
	async function (req, res) {
        if(req.body.approve == 0){
            res.send ("report is unsuccessful");
        }
        else{
            res.send(req.body.temp_id); //would it be better to just get name of faility?
        }
        
        
	}
)

app.get('/processreportedFacility',
	async function (req, res) {
        var url = 'http://localhost:8081/adminApprovalFacility';
        request(url, function(err, response, body) {
            if (err != null) {
                var s = util.format("Error: %s", err);
                console.log(s);
                res.send(s);
                return;
            }
            else if (err == null && body == "report is unsuccessful")
            {
                res.send("Nothing happens");
            }else{
                var myDb = db.db("myDB");
                var myCollection = "facility"
                var myquery = { facility: 'Facility To Be Deleted'}; //replace with real facility name or id or equivalent
                var newvalues = { $set: {facility_status: "removed status"} }; // this ststaus needs to be confirmed later
                await myDb.collection(myCollection).updateOne(myquery, newvalues, function(err, res) {
                    if (err) throw err;
                    console.log("1 document updated" + res);
                    // db.close();
                  });
                
            }
            });          
	}
)
