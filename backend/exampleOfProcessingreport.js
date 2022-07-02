app.post('/user/Report',
    async function (req, res) {
        var myDb = db.db("myDB");
      
        var myCollection
        var report_Type = req.body.report_Type;
        if(report_Type == "user"){
            myCollection = "reportedUser";
            var reportedUserID = req.body.user_gmail;
            var reportReasonUser = req.body.reason;
            var accountStatus = req.body.accountStatus;
            var userObj = {
                report_Type: report_Type,
                reportedUserid: reportedUserID,
                accountStatus: accountStatus,
                reason: reportReasonUser
            };
            await client.db(myDb).collection(myCollection).insertOne(userObj, function(err, res) {
                if (err) throw err;
                console.log("1 document inserted"+res);
                // db.close();
                res.send(userObj);
              });
          
        }else if (report_Type == "facility"){
            myCollection = "reportedFacility";
            var reportedFacilityID = req.body.facility_id;
            var reportedFacilityType = req.body.facility_type;
            var reporterID = req.body.user_id;
            var reportReason = req.body.reason;
            var facilityObj = { 
                          report_Type: report_Type,
                          facility_id: reportedFacilityID,
                          facility_type: reportedFacilityType,
                          reporter_id: reporterID,
                          reason: reportReason
                          }; //replace with real facility name or id or equivalent
            await client.db(myDb).collection(myCollection).insertOne(facilityObj, function(err, res) {
                if (err) throw err;
                console.log("1 document inserted"+res);
                // db.close();
                res.send(facilityObj);
              });
        }else{
            myCollection = "reportedComment";
            //TBD
        }
       
    }
)
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
