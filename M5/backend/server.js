var express = require('express');
var app = express();

const bodyParser = require('body-parser');
const { application } = require('express');

var util = require('util');
var encoder = new util.TextEncoder('utf-8');
const { MongoClient } = require("mongodb");
const { response } = require("express");
var ObjectId = require('mongodb').ObjectID;
const uri = "mongodb://127.0.0.1:27017"
const client = new MongoClient(uri)

//for accessing other apis
const request = require('request');
const { title } = require('process');
app.use(express.json());

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));



app.get('/',
    async function (req, res) {
        res.send("Connected to server at http://20.213.243.141:8000/");
    });
    
/**
 * purpose: get a specific detailed look of a post
 * pre: user chooses the specific facility they would like
 * post: get the speifici post a suer wants
 */
app.post('/specific', async (req, res) => {
    var type = req.body.facility_type;
    console.log(type);
    var numberOfType = parseInt(type);
    console.log(numberOfType);

    switch (numberOfType) {
        case 0:
            type = "posts";
            break;
        case 1:
            type = "studys";
            break;
        case 2:
            type = "entertainments";
            break;
        case 3:
            type = "restaurants";
            break;
        case 4:
            type = "report_user";
            break;
        case 5:
            type = "report_comment";
            break;
        case 6:
            type = "report_facility";
    }

    try {
        const result = await client.db("Help!Db").collection(type).findOne({ _id: req.body.facility_id });
        console.log(type);
        console.log(result);
        res.status(200).send(result);

    }
    catch (err) {
        console.log(err);
        res.status(400).send(err);
    }
});


/**
 * Purpose: Caculates overallrate of a facility and records each users' rate of a particular facility
 * Pre: Each user rates a faiclity with a score
 * Post: Sets overall score based on all users' ratings for a particular facility
 */
app.post('/user/RateFacility',
    async function (req, res) {
        var myDb = "Help!Db";
        var facilityType;
        var requestJson = {user_id:req.body._id, 
            rateScore:parseInt(req.body.rateScore),
            facility_type:facilityType, 
            facility_id:req.body.facility_id};

        
        if(req.body.facility_type == 0){
            facilityType ="posts";
        }else if (req.body.facility_type == 1){
            facilityType ="studys";
        }else if(req.body.facility_type == 2){
            facilityType ="entertainments";
        }else if(req.body.facility_type == 3){
            facilityType ="restaurants";
        }
        console.log(facilityType);
     
         await client.db(myDb).collection("userRateInfo").insertOne(requestJson, function (err, res) {
            var finalResult = JSON.stringify(res);
            if (err) throw err;
            console.log(finalResult);
        });
      
        const userRates = [];
        const ratedUsers = []; 
        await client.db(myDb).collection("userRateInfo").find().forEach(function (myDoc) {
            userRates.push(myDoc.rateScore);  
            ratedUsers.push(myDoc.user_id);
            
        });
        // userRates.push(requestJson.rateScore);
        const overAllRateScore = userRates.reduce((a, b) => a + b, 0) / userRates.length;
        console.log(overAllRateScore);

       
        const ratedUserField =  ratedUsers ;
        console.log(ratedUserField);
        // console.log("here is result:" +JSON.stringify(ratedUsers[0]));
        // console.log("this is parseINn:" + parseIn);
        for (i = 0; i < ratedUsers.length; i++) {
        const resultUserRates = await client.db(myDb).collection("users").findOne({_id:ratedUsers[i]});
        console.log("resultRtae is" + JSON.stringify(resultUserRates.number_of_rate));
            var myquery= {_id:ratedUsers[i]}
            var newvalues ={$inc: { "number_of_rate": 1 }}
             await client.db(myDb).collection("users").updateOne(myquery, newvalues, function (err, res) {
                if (err) throw err;
                // console.log("The status for updating field user rate is:" + JSON.stringify(res));
            });

        }


       

            const result = await client.db(myDb).collection(facilityType).findOne({ _id: requestJson.facility_id});
            console.log("facility type is: " + result.facility.facilityType);


            var myquery = { _id: requestJson.facility_id}; //replace 1 with facility_id from frontend
            var newvalues = {
                $set: {
                    "facility": {
                        "facility_status": result.facility.facility_status,
                        "facilityType": result.facility.facilityType,
                        "facilityTitle": result.facility.facilityTitle,
                        "facilityDescription": result.facility.facilityDescription,
                        "timeAdded": result.facility.timeAdded,
                        "facilityImageLink": result.facility.facilityImageLink,
                        "facilityOverallRate": overAllRateScore,
                        "numberOfRates": userRates.length,
                        "longitude": result.facility.longitude,
                        "latitude": result.facility.latitude
                    },
                    "ratedUser": ratedUserField
                }
            };

            await client.db(myDb).collection(facilityType).updateOne(myquery, newvalues, function (err, res) {
                if (err) throw err;
                // console.log("The status for updating facility updated field is:" + JSON.stringify(res));

            });
            var finalResult1 ={userRates};
            res.send(finalResult1);
  

    }
)

/**
 * Purpose: Get the newest facilities by page number
 * Pre: User scrolls on app with different pages and browses facility
 * Post:  User will get information for the facilities they would like to see
 */
app.post('/facility/newest', async (req, res) => {
    
    var type = req.body.type;
    var numberOfType = parseInt(type);
    console.log(numberOfType);  
    switch (numberOfType) {
        case 0:
            type = "posts";
            break;
        case 1:
            type = "studys";
            break;
        case 2:
            type = "entertainments";
            break;
        case 3:
            type = "restaurants";
            break;
    }
    try {
      
        var length2;
        var bigArr = []
        await client.db("Help!Db").collection(type).find({}).sort({ _id: -1 }).toArray(function (err, result) {
            if (err) throw err;
            try {
                console.log(result[0]._id)
                var len = result.length;
                for (var i = 0; i < len; i += 1) {
                    if(result[i].facility.facility_status == "normal"){
                    var arr = [];
                    arr.push(result[i]._id)
                    arr.push(result[i].facility.facilityOverallRate)
                    arr.push(result[i].facility.facilityTitle)
                    arr.push(result[i].facility.facilityDescription)
                    arr.push(result[i].facility.timeAdded)
                    bigArr.push(arr);
                    }
                }
                length2 = bigArr.length;
                var final = {};
                final["result"] = bigArr;
                final["length"] = length2;
                res.send(final);
            } catch (err) {
                console.log(err)
                res.status(400).send(err);
            }
        });
    }
    catch (err) {
        console.log(err);
        res.status(400).send(err);
    }
});

/**
 * Purpose:  USer uses search functionality and get facility they want 
 * Pre:  User must enter valid input to get the result they like to see
 * Post:  User gets all relevany information about the facility they searched using the search bar
 */
app.post('/facility/search', async (req, res) => {
    var type = req.body.type;
    var keyWordSearched = req.body.search;
 
    console.log(req.body);
    var numberOfType = parseInt(type);
    console.log(numberOfType);
    switch (numberOfType) {
        case 0:
            type = "posts";
            break;
        case 1:
            type = "studys";
            break;
        case 2:
            type = "entertainments";
            break;
        case 3:
            type = "restaurants";
            break;
    }
    try {
        
        var final = {};
        var bigArr = [];
        
        const result = await client.db("Help!Db").collection(type).find({ "facility.facilityTitle": {$regex: keyWordSearched }}).forEach((result)=>{
            
            console.log("facility containing the keyword:");
            console.log(JSON.stringify(result));
            bigArr.push(result);
            console.log("bigArr is ")
            console.log(bigArr);
          
            
           
        });
    
        var arr = [];
        for(var i=0; i < bigArr.length; i++){
                arr.push(bigArr[i])   
        }

        var theOne = [];
        for(var i =0; i < arr.length; i++){
            var zz = []
            zz.push(arr[i]._id)
            zz.push(arr[i].facility.facilityOverallRate)
            zz.push(arr[i].facility.facilityTitle)
            zz.push(arr[i].facility.facilityDescription)
            zz.push(arr[i].facility.timeAdded)
            // zz[ x, y, z]
            theOne.push(zz)
        }
        

        var length2 = theOne.length;
        final["result"] = theOne;
        final["length"] = length2; 
        console.log("return is ")
     
        console.log(final)
        res.send(final);
       
    }
    catch (err) {
        console.log(err);
        res.status(400).send(err);
    }
});

/**
 * Purpose: Let admin access two queries at a time
 * Pre: NO precondition
 * Post:  Give admin access up to two queries from 0 query (query is in a queue first in first handled)
 */
app.get('/report/admin',
    async function (req, res) {
        await client.db("Help!Db").collection("reportedComment").find({}).sort({}).limit(2).toArray(function (err, result) {
            var resultArray = [];
            for(i=0;i<2;i++){
                resultArray[i] = result[i];
                if(resultArray[i] == null){resultArray.length -= 1;}
            }
            res.send(JSON.stringify({report_content:resultArray,length:resultArray.length}));
        });
    })

/**
 * Purpose: user reports inapporatiate facilities and puts this request in query
 * Pre: User must make a report dedicated to a facility
 * Post: Result gets in query
 */
app.post('/user/Report/commentAndfacility',
    async function (req, res) {
        var myDb = "Help!Db";
        var myCollection;
        myCollection = "reportedComment";

        //follwing four needs to match with frontend
        var reportedFacilityID = req.body.reportedFacilityID;
        var reportedFacilityType = parseInt(req.body.reportedFacilityType);
        var reportFacilityTitle = req.body.title;
        var reportedFacilityTypeString;
        if (parseInt(req.body.reportedFacilityType) == 0){
            reportedFacilityTypeString = "posts";
        }
        if (parseInt(req.body.reportedFacilityType) == 1){
            reportedFacilityTypeString = "studys";
        }
        if (parseInt(req.body.reportedFacilityType) == 2){
            reportedFacilityTypeString = "entertainments";
        }
        if (parseInt(req.body.reportedFacilityType) == 3){
            reportedFacilityTypeString = "restaurants";
        }
        var reporterID = req.body.reporterID;
        var reportReason = req.body.reportReason;
        var reportedUSer = req.body.reported_id;
        var reportType = req.body.report_type;
      
       

        const reportredResultQuery = await client.db(myDb).collection(reportedFacilityTypeString).findOne({ _id: reportedFacilityID });

     

        var finalReportDecision = {
            facility_id: reportedFacilityID,
            facility_type: reportedFacilityType,
            reason: reportReason,
            reporter: reporterID,
            report_type: reportType,
            reported_user: reportedUSer,
            title: reportFacilityTitle
        }
        //sending request to admin and wait for approval or denial
        await client.db(myDb).collection(myCollection).insertOne(finalReportDecision, function (err, res) {
            var finalResult = JSON.stringify(res);
            if (err) throw err;
            console.log(finalResult);
        });

        var adminHandleQuery = []
        res.send(JSON.stringify({result: "query added"}));

    }
)

/**
 * Purpose: let admin approve or deny the report request being querired
 * Pre: Request is in query
 * Post: if accepted update field for facility or comment else remove element in query and nothing happens
 */
app.post('/admin/reportApproval',
 async function (req, res) {
     var reportType;
     if( parseInt(req.body.report_type) == 5){
         reportType = "comment";
     };
     if(parseInt(req.body.report_type) == 6){
         reportType = "facility";
     }

     var reportID = req.body.report_id;
     var approveDecision =  req.body.approve;
     var facility_type = req.body.facility_type;
     var reportedFacilityid = req.body.facility_id;
     var reportedID = req.body.reported_user;
    
     var myDb = "Help!Db"; //change myDb to Help!Db at the end
     var myCollection ;

     console.log("admin apporve body is :" + JSON.stringify(req.body));

   

     
     if (reportType == "facility") {
         myCollection = "reportedComment";
       
    
         if(approveDecision == "0"){
            await client.db(myDb).collection(myCollection).deleteOne({_id:ObjectId(reportID)}, function(err, obj) {
                if (err) throw err;
                console.log("1 document deleted in reportedComment due to reported facility");
                res.send({"result":"report unsuccessful"});
              });
         }else{
            const reportredResultQuery = await client.db(myDb).collection(facility_type).findOne({ _id: reportedFacilityid});
             var finalReportDecisionJSON = {
                 "facility_status": "blocked",
                 "facilityType": reportredResultQuery.facility.facilityType,
                 "facilityTitle": reportredResultQuery.facility.facilityTitle,
                 "facilityDescription": reportredResultQuery.facility.facilityDescription,
                 "timeAdded": reportredResultQuery.facility.timeAdded,
                 "facilityImageLink": reportredResultQuery.facility.facilityImageLink,
                 "facilityOverallRate": reportredResultQuery.facility.facilityOverallRate,
                 "numberOfRates": reportredResultQuery.facility.numberOfRates,
                 "longitude": reportredResultQuery.facility.longitude,
                 "latitude": reportredResultQuery.facility.latitude
             }
             var myquery = { _id: reportedFacilityid};
             var newvalues = { $set: { "facility": finalReportDecisionJSON } };

              client.db(myDb).collection(facility_type).updateOne(myquery, newvalues, function (err, res) {
                 if (err) throw err;
                 console.log("The status for updating reported facility field is:" + JSON.stringify(res));
             });

             client.db(myDb).collection(myCollection).deleteOne({_id:ObjectId(reportID)}, function(err, obj) {
                if (err) throw err;
                console.log("1 document deleted in reportedComment due to reported facility");
              });
              res.send({"result":"report successful"});

         }
     }else{
        if(approveDecision == "0"){
            await client.db(myDb).collection(myCollection).deleteOne({_id:ObjectId(reportID)}, function(err, obj) {
                if (err) throw err;
                console.log("1 document deleted in reportedComment due to reported comment");
              });
            res.send({"result":"report unsuccessful"});
         }else{
            myCollection = "reportedComment";
         
            // const reportredResultQueryB = await client.db(myDb).collection(myCollection).findOne({ _id: reportID});
            // console.log("Request body is: " + JSON.stringify(reportredResultQueryB));
            
          
            client.db(myDb).collection(facility_type).updateOne(  
                { _id: reportedFacilityid },
                {
                    $set: {
                        "reviews.$[elem]": []
                    }
                },
                { arrayFilters: [{ "elem.replierID": { $eq: reportedID } }] }
                 );

                client.db(myDb).collection(myCollection).deleteOne({_id:ObjectId(reportID)}, function(err, obj) {
                    if (err) throw err;
                    console.log("1 document deleted in reportedComment due to reported comment");
                  });
                  res.send({"result":"report successful"});
         }

     }
 }

)






/**
 * Purpose: User adds a new facility to the app
 * Pre: User inputs relevant information 
 * Post:  New facility added to our app
 */
app.post('/addFacility', async (req, res) => {
    const title = req.body.title
    const description = req.body.description
    var long = parseFloat(req.body.long)
    var lat = parseFloat(req.body.lat)
    const type = req.body.type;  // a string of name without s
    const facilityImageLink = req.body.facilityImageLink;
    console.log("req body is " + req.body);
    var date_ob = new Date();
    var year = date_ob.getFullYear();
    var month = date_ob.getMonth();
    var date = date_ob.getDate();
    const timeAdded = year + "/" + month + "/" + date
    const lastOne = await client.db("Help!Db").collection(type).find({}).sort({ _id: -1 }).limit(1).toArray();
    console.log("last facility" + lastOne);
    var newId = "1";
    if (lastOne.length == 0) {
    }
    else {
        var d = lastOne[lastOne.length - 1]._id;
        var dd = parseInt(d);
        dd += 1;
        newId = dd.toString();
    }
   
    try {
        await client.db("Help!Db").collection(type).insertOne({
            _id: newId,
            "facility":
            {
                "facilityType": type,
                "facility_status": "normal",
                "facilityTitle": title,
                "facilityDescription": description,
                "facilityImageLink": facilityImageLink,
                "facilityOverallRate": 0,
                "numberOfRates": 0,
                "timeAdded": timeAdded,
                "longitude": long,
                "latitude": lat
            },
            "rated_user": [{}],
            "reviews": [{}]
        });
        res.send(JSON.stringify({ "recieved": 1 }));
    } catch (err) {
        console.log(err);
        res.send(SON.stringify({ "recieved": 0 }));
    }

 
});




/**
 * Purpose:  Letting user add comment and will display their comment to the approriate places
 * Pre:  User inputs a comment to a specific place they want
 * Post:  User comment will show up on the speific place of the app
 */
 app.put('/comment/add', async (req, res) => {
    var type = req.body.facilityType; //string
    const facilityId = req.body.facility_id //string
    const userId = req.body.user_id //string
    const replyContent = req.body.replyContent //string
    const userName = req.body.username;
    const rateScore = parseFloat(req.body.rateScore);
    
    

    var numberOfType = parseInt(type);
    console.log(numberOfType);
    switch (numberOfType) {
        case 0:
            type = "posts";
            break;
        case 1:
            type = "studys";
            break;
        case 2:
            type = "entertainments";
            break;
        case 3:
            type = "restaurants";
            break;
        case 4:
            type = "report_user";
            break;
        case 5:
            type = "report_comment";
            break;
        case 6:
            type = "report_facility";
    }
    var date_ob = new Date();
    var year = date_ob.getFullYear();
    var month = date_ob.getMonth();
    var date = date_ob.getDate();
    var hours = date_ob.getHours();
    var minutes = date_ob.getMinutes();
    var seconds = date_ob.getSeconds();
  
    const timeAdded = year + "/" + month + "/" + date + "/" + hours + "/" + minutes + "/" + seconds;
    const finding = await client.db("Help!Db").collection(type).findOne({_id: facilityId,"reviews.replierID" : userId});
  
    if(finding == null){
        try {
            const result = await client.db("Help!Db").collection(type).updateOne(
                { _id: facilityId },
                {
                    $push: {
                        "reviews": {
                            replierID: userId,
                            userName: userName,
                            rateScore:rateScore,
                            upVotes: 0,
                            downVotes: 0,
                            replyContent: replyContent,
                            timeOfReply: timeAdded
                        }
                    }
                }
            );
            await client.db("Help!Db").collection(type).updateOne(
                { _id: facilityId },
                {
                    $inc: {
                        "facility.numberOfRates":1
                    }
                }
            );
            await client.db("Help!Db").collection("users").updateOne(
                { _id: userId },
                {
                    $inc: {
                        "number_of_reply": 1
                    }
                }
            );
            console.log(result);
            res.send(result);
        }
        catch (err) {
            console.log(err);
            res.status(400).send(err);
        }
    }
    else{
        console.log("already exist");
        res.send(JSON.stringify({"result": "already_exist"}));
    }
    
});



//following two report method are similar to this one
/**
 * Purpose: Let user chooses either to upvote or downvote something
 * Pre:  User chooses to upvote or downvote
 * Post:  If user upvotes the number of votes increase by one or else decrease by one
 */
app.put('/Votes', async (req, res) => {
    var type = req.body.facilityType; //string
    const facilityId = req.body.facility_id //string
    const userId = req.body.user_id //string
    const vote = req.body.vote; // string   
    const isCancelled = req.body.isCancelled; // string "cancel" or "pend"   

    console.log(req.body)

    var numberOfType = parseInt(type);
    console.log(numberOfType);
    switch (numberOfType) {
        case 0:
            type = "posts";
            break;
        case 1:
            type = "studys";
            break;
        case 2:
            type = "entertainments";
            break;
        case 3:
            type = "restaurants";
            break;
        case 4:
            type = "report_user";
            break;
        case 5:
            type = "report_comment";
            break;
        case 6:
            type = "report_facility";
    }

    try {
        if (vote === "up") {
            if(isCancelled === "pend"){
                const result = await client.db("Help!Db").collection(type).updateOne(
                    { _id: facilityId },
                    {
    
                        $inc: {
                            "reviews.$[elem].upVotes": 1
                        }
                    },
                    { arrayFilters: [{ "elem.replierID": { $eq: userId } }] }
                );
                res.send(result);
            }
            else{
                const result = await client.db("Help!Db").collection(type).updateOne(
                    { _id: facilityId },
                    {
    
                        $inc: {
                            "reviews.$[elem].upVotes": -1
                        }
                    },
                    { arrayFilters: [{ "elem.replierID": { $eq: userId } }] }
                );
                res.send(result);
            }
            
        } else if (vote === "down") {
            if(isCancelled === "pend"){
                const result = await client.db("Help!Db").collection(type).updateOne(
                    { _id: facilityId },
                    {
    
                        $inc: {
                            "reviews.$[elem].downVotes": -1
                        }
                    },
                    { arrayFilters: [{ "elem.replierID": { $eq: userId } }] }
                );
                res.send(result);
            }
            else{
                const result = await client.db("Help!Db").collection(type).updateOne(
                    { _id: facilityId },
                    {
    
                        $inc: {
                            "reviews.$[elem].downVotes": 1
                        }
                    },
                    { arrayFilters: [{ "elem.replierID": { $eq: userId } }] }
                );
                res.send(result);
            }
            
        }

    }
    catch (err) {
        console.log(err);
        res.status(400).send(err);
    }
});




/**
 * Purpose: Adds credit to the user porfile if user mets the contriubution criteria (adding facility or commenting)
 * Pre: User eitrher adds facility or comments
 * Post:  Added credit show up on user profile
 */
app.put('/creditHandling/normal', async (req, res) => {
    const additionCredit_addFacility = 5;
    const additionCredit_comment = 1;
    let AdditionType = req.body.AdditionType;
    let goodUserId = req.body.upUserId;
    console.log("credit input is " + req.body);


    const result = await client.db("Help!Db").collection("users").findOne({ _id: goodUserId });
    console.log("the user profile(handled by credit ) is "+result);
    var currentadderCredits = result.number_of_credit;
   

    if (AdditionType == "addFacility") {
        currentadderCredits += additionCredit_addFacility;
    } else if (AdditionType == "comment") {
        currentadderCredits += additionCredit_comment;
    } else {
        res.send("No credits granted since no contributions made, please make contribution before any credit is granted; AdditionType is not matched in this case!");
    }
    await client.db("Help!Db").collection("users").updateOne({ _id: goodUserId },
        {
            $set:
            {
                number_of_credit: currentadderCredits,
            }
        }
    );

});

/**
 * Purpose:  Let user sign up tp the app with google authentication
 * Pre: User has never signed up with a particualr email
 * Post:  User sucessfully sign up and they get a specific profile dedicated to them
 */
app.post('/google_sign_up', async(req, res) => { 
    //var user_id = generate_new_user_id(); // write a method to generate a user id for new user
     const user_gmail = req.body._id;
     console.log("user gamil is :" + user_gmail);
     const userName= req.body.username;
     const logo = req.body.user_logo;
     console.log(req.body);
     const result= await client.db("Help!Db").collection("users").findOne({_id : user_gmail});
     console.log(result);
     if(result == null){
        try{
            await client.db("Help!Db").collection("users").insertOne({
                _id : user_gmail, 
                "account_type" : 0,  
                "account_status" : 0, 
                "username": userName, 
                "user_logo": logo, 
                "number_of_credit": 0, 
                "number_of_rate": 0, 
                "number_of_reply": 0, 
                "number_of_facility":0     
            })

            console.log("Added to the db");
            res.send({"result":"Added to the db"});
        }catch(err){
            console.log(err);
            res.send(err);
        }
    }else{
         console.log("already exists");
         console.log("sign up JOSN is : " + JSON.stringify(result));
         res.send(result);
    }
 }); 


//intiation for using the OneSignal Service
const OneSignal = require('@onesignal/node-onesignal') ;

const app_key_provider = {
    getToken() {
        return 'MjJiMGQ3ODAtNjIzNC00ZjBkLTgxNGYtMDY0M2YxYWJlNjE0';
    }
};

const configuration = OneSignal.createConfiguration({
    authMethods: {
        app_key: {
        	tokenProvider: app_key_provider
        }
    }
});
const client3 = new OneSignal.DefaultApi(configuration);

/** purpose: push notification to users
 *  parameter: gmail array, facility's id, facility's type
 *  return message to indicate success
 */  
app.post('/sendToDevice3', async function(req, res){
    
    var gmails = req.body.reviewers; 
    var facilityId = req.body.facilityId; 
    var type = parseInt(req.body.facilityType); 
    var length = req.body.length;
    
    console.log("This is real time update body");
    console.log(req.body);
    if(length === 0){
        return;
    }
   
    var notificationType = 0
   
    var numberOfType = parseInt(notificationType);
    console.log(numberOfType);

    switch(type){
        case 0:
            type = "posts";
            break;
        case 1:
            type = "studys";
            break;
        case 2:
            type = "entertainments";
            break;
        case 3:
            type = "restaurants";
            break;
        case 4:
            type = "report_user";
            break;
        case 5:
            type = "report_comment";
            break;
        case 6:
            type = "report_facility";
    }


    switch (numberOfType) {
        case 0:
            notificationType = "You have an review on "   + type + " , with facility id: " + facilityId+".";
            break;
        case 1:
            notificationType = "your reported comment is approved"+ type + "with facility number:" + facilityId;
            break;
        case 2:
            notificationType = "your reported facility is addressed"+ type + "with facility number:" +facilityId;
            break;
        case 3:
            notificationType = "a new facility has been added to the app"+ type + "with facility number:" +facilityId;
            break;
        case 4:
            notificationType = "you received an upvote"+ type + "with facility number:" +facilityId;
            break;
        case 5:
            notificationType = "you received an downvote"+ type + "with facility number:" +facilityId;
            break;
    }


    const notification = new OneSignal.Notification();
    notification.app_id = 'f38cdc86-9fb7-40a5-8176-68b4115411da';
 
    notification.contents = {
        en: notificationType
    };
    notification.channel_for_external_user_ids = "push",
    
    
    notification.include_external_user_ids = []
    for(var i = 0 ; i < length; i++){
        notification.include_external_user_ids.push(gmails[i]);
    }
    
    const {id} = await client3.createNotification(notification);
    console.log(id)
 
    res.send(JSON.stringify({"result":"real time done"}));
})

async function run() {
    try {
        await client.connect();
        console.log(" successfully connect to db");
        var server = app.listen(8000, (req, res) => {
            var host = server.address().address;
            var port = server.address().port;
            console.log("Example app is running" + "with address" + host + "port: " + port);

            
        })
    } catch (err) {
        console.log("error")
        await client.close()
    }
}
run();
