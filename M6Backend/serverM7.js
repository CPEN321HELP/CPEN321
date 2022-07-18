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


const interfacespecific = require('./interfacespecific');
const creditCalculation = require("./creditCalculation");
// iohafuoghqw9[ugeh]
const findTheUser = require("./findTheUser");

app.get('/',
    async function  (req, res) {
        res.send("Connected to server at http://20.213.243.141:8080/");
    });
    
/**
 * purpose: get a specific detailed look of a post
 * pre: user chooses the specific facility they would like
 * post: get the speifici post a suer wants
 */


app.post('/specific', async function (req, res) {
    var type = req.body.facilityType; // changed to facilityType
    console.log("request body is:" + JSON.stringify(req.body));
    console.log("specific 1 is: " + type);
    var id = parseInt(req.body.facility_id);
    var numberOfType = parseInt(type);
    console.log( "specific 2 is: " )
    console.log( numberOfType );
    type = interfacespecific(numberOfType);
    

    try {
        const result = await client.db("Help!Db").collection(type).findOne({ _id: id});
        console.log("specific 2 is: " + type);
        console.log(result);
        res.status(200).send(result);

    }
    catch (err) {
        console.log(err);
        res.status(400).send(err);
    }
});


app.post('/rate' , async (req, res)=>{
    const user_id = req.body._id;
    const rateScore = parseInt(req.body.rateScore);
    const facilityType = req.body.facilityType;
    const facilitId= parseInt(req.body.facility_id);



})
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
            facility_id:parseInt(req.body.facility_id)};

        
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
            // console.log("user rate array is :  "+ userRates);
            ratedUsers.push(myDoc.user_id);
            // console.log("rated user array is: " + ratedUsers);   
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


        // try {

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
            var finalResult1 ={  };
            res.send(finalResult1);
            
            // res.status(200).send(userRates);

        // }
        // catch (err) {
        //     console.log(err);
        //     res.status(400).send(err);
        // }

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
        //const pageNumber = req.body.pageNumber;
        //const type = req.body.facility_title;
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




function xx(type, interfacespecific){
    var numberOfType = parseInt(type);
    console.log(numberOfType);
    type = interfacespecific(numberOfType);
    return type;
}
const returnLogic = require("./returnLogic");
function yy(result, returnLogic){
    var final = returnLogic(result);
    return final;
}
/**
 * Purpose:  USer uses search functionality and get facility they want 
 * Pre:  User must enter valid input to get the result they like to see
 * Post:  User gets all relevany information about the facility they searched using the search bar
 */
app.post('/facility/search', async function searchOne(req, res){
    var type = req.body.type;
    var keyWordSearched = req.body.search;
    var final={};
    type = xx(type, interfacespecific)
    var final = {};
    //var bigArr = [];
    //const result = await client.db("Help!Db").collection(type).find({   $text: { $search: keyWordSearched } }).forEach((result)=>{
    await client.db("Help!Db").collection(type).find({ "facility.facilityTitle": { $regex: new RegExp(keyWordSearched, "i") } }).forEach((result) => {
        //db.store.find( { $text: { $search: "coffee" } } )    
        console.log("facility containing the keyword:");
        console.log(JSON.stringify(result));
        //bigArr.push(result);
        final = yy(result, returnLogic);
        // console.log("bigArr is ")
        // console.log(bigArr);
    });
    
    // var arr = [];
    // for (var i = 0; i < bigArr.length; i++) {
    //     arr.push(bigArr[i])
    // }
    // var theOne = [];
    // for (var i = 0; i < arr.length; i++) {
    //     var zz = []
    //     zz.push(arr[i]._id)
    //     zz.push(arr[i].facility.facilityOverallRate)
    //     zz.push(arr[i].facility.facilityTitle)
    //     zz.push(arr[i].facility.facilityDescription)
    //     zz.push(arr[i].facility.timeAdded)
    //     theOne.push(zz)
    // }
    // var length2 = theOne.length;
    // final["result"] = theOne;
    // final["length"] = length2;
    // console.log("return is ")
    // console.log(final);
    res.send(final);
});

//exports.searchOne = searchOne;

/**
 * Purpose: user reports inapporatiate facilities and puts this request in query
 * Pre: User must make a report dedicated to a facility
 * Post: Request puts in query
 */
app.post('/user/Report/facility',
    async function (req, res) {
        var myDb = "Help!Db";  //change this to Help!Db at the end
        var myCollection;
        myCollection = "reportedFacility";

        //following fouyr should all come from frontend
        var reportedFacilityID = req.body.reportedFacilityID;
        var reportedFacilityType = req.body.reportedFacilityType; //need to chaange this to something frontend sends later 
        var reporterID = req.body.reporterID;
        var reportReason = req.body.reportReason;

        const reportredResultQuery = await client.db(myDb).collection(reportedFacilityType).findOne({ _id: reportedFacilityID });

        var finalReportDecision = {
            old_id: reportedFacilityID,
            facility_type: reportedFacilityType,
            reason: reportReason,
            reporter: reporterID,
            facility_title: reportredResultQuery.facility.facilityTitle,
            facility_description: reportredResultQuery.facility.facilityDescription,
            facility_timeAdded: reportredResultQuery.facility.timeAdded,
            facility_imagelink: reportredResultQuery.facility.facilityImageLink,
            facility_overallRate: reportredResultQuery.facility.facilityOverallRate,
            facility_numberOfRates: reportredResultQuery.facility.numberOfRates,
            facility_reviews: reportredResultQuery.facility.reviews
        }
        //sending request to admin and wait for approval or denial
        await client.db(myDb).collection(myCollection).insertOne(finalReportDecision, function (err, res) {
            var finalResult = JSON.stringify(res);
            if (err) throw err;
            console.log(finalResult);
        });
        console.log("final decision is: " + JSON.stringify(finalReportDecision));
    }
)


/**
 * Purpose: Let admin access two queries at a time
 * Pre: NO precondition
 * Post:  Give admin access up to two queries from 0 query (query is in a queue first in first handled)
 */
app.get('/report/admin',
    async function (req, res) {
        await client.db("Help!Db").collection("reportedComment").find({}).sort({}).limit(2).toArray(function (err, result) {
            var resultArray = [];
            resultArray.length = 0;
            if(result.length == 0){resultArray.length = 0;}else{
            for(i=0;i<2;i++){
                resultArray[i] = result[i];
                if( resultArray[i] = result[i]){resultArray.length += 1;}
                
            }
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
       console.log("Testing report comment and facility is: " + JSON.stringify(req.body));
        var reportedFacilityID =  parseInt(req.body.reportedFacilityID);
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
            console.log("Added one query from user report which is : "+ finalResult);
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
     var reportedFacilityid =   parseInt (req.body.facility_id);
     var reportedID = req.body.reported_user;
    
     var myDb = "Help!Db"; //change myDb to Help!Db at the end
     var myCollection ;

     var upUser = req.body.upUserId;
     var downUSer = req.body.downUserId;

     console.log("admin apporve body is :" + JSON.stringify(req.body));

    const notification = new OneSignal.Notification();
    notification.app_id = 'f38cdc86-9fb7-40a5-8176-68b4115411da';
    var gmails = [];
    var gmails2= [];
     
     if(req.body.adminEmail != null){
     if (reportType == "facility") {
         myCollection = "reportedComment";
       
    
         if( parseInt(req.body.approve) == 0){
            // gmails = [upUser];
            console.log("goes in second if with gmails: " + gmails);
            client.db(myDb).collection("reportedComment").deleteOne({_id:ObjectId(reportID)}, function(err, obj) {
                if (err) throw err;
                console.log("1 document deleted in reportedComment due to reported facility");
                res.send({"result":"report unsuccessful"});
              });
            // await realTimeUpdate(gmails, reportedFacilityid, 44, 1);    
         }else{
            // gmails = [upUser,downUSer];
            console.log("goes in second if with gmails: " + gmails);
            const reportredResultQuery = await client.db(myDb).collection(req.body.facility_type).findOne({ _id: reportedFacilityid});
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

              client.db("Help!Db").collection("users").updateOne(
                { _id: upUser },
                {
                    $inc: {
                        "number_of_credit":1
                    }
                }
            );

            client.db("Help!Db").collection("users").updateOne(
                { _id: downUSer },
                {
                    $inc: {
                        "number_of_credit": -1
                    }
                }
            );
              res.send({"result":"report successful"});

            // await realTimeUpdate(gmails, reportedFacilityid, 22, 2);
         }
     }else{
        if(approveDecision == "0"){
            client.db(myDb).collection("reportedComment").deleteOne({_id:ObjectId(reportID)}, function(err, obj) {
                if (err) throw err;
                console.log("1 document deleted in reportedComment due to reported comment");
              });
            res.send({"result":"report unsuccessful"});
         }else{
               gmails = [upUser];
               gmails2 =[downUSer];
               myCollection = "reportedComment";
               console.log("down gmail is :" + gmails2);
         
            // client.db(myDb).collection(facility_type).updateOne(  
            //     { _id: reportedFacilityid },
            //     {
            //         $set: {
            //             "reviews.$[elem]": []
            //         }
            //     },
            //     { arrayFilters: [{ "elem.replierID": { $eq: reportedID } }] }
            //      );

            //     client.db(myDb).collection(myCollection).deleteOne({_id:ObjectId(reportID)}, function(err, obj) {
            //         if (err) throw err;
            //         console.log("1 document deleted in reportedComment due to reported comment");
            //       });

            //       client.db("Help!Db").collection("users").updateOne(
            //         { _id: upUser },
            //         {
            //             $inc: {
            //                 "number_of_credit":1
            //             }
            //         }
            //     );
    
            //     client.db("Help!Db").collection("users").updateOne(
            //         { _id: downUSer },
            //         {
            //             $inc: {
            //                 "number_of_credit": -1
            //             }
            //         }
            //     );
                  res.send({"result":"report successful"});

                 //await realTimeUpdate(req.body.upMessage, 6, gmails, reportedFacilityid, 25, 1);
                 await realTimeUpdate(req.body.downMessage, 7, gmails2, reportedFacilityid, 25, 1);  
         }

     }
    }
 }
 

)



function logicOfAddFacility(lastOne){
    var newId = "1";
    if (lastOne.length === 0) {
    // implying that the collection is empty
    }
    else {
        var theidOfTheLastOne = lastOne[lastOne.length - 1]._id;
        var theidOfTheLastOneInt = parseInt(theidOfTheLastOne);
        theidOfTheLastOneInt += 1;
        console.log("dd now is : " + theidOfTheLastOneInt);
        newId = theidOfTheLastOneInt;
        console.log("new id  now is : " + newId);
    }
    return newId;
}


/**
 * Purpose: User adds a new facility to the app
 * Pre: User inputs relevant information 
 * Post:  New facility added to our app
 */
app.post('/addFacility', async (req, res) => {
    const title = req.body.title
    const description = req.body.description
    const adderId = req.body.adderID
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
    console.log("last facility is : " + JSON.stringify(lastOne));
    var newId  = await logicOfAddFacility(lastOne) ; 
    // await client.db("facility").collection(type).insertOne(req.body); // in this case, frontend send us a JSON file with complete information
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
            "reviews": [{}],
            "adderID": adderId

        });
        //res.send("addfacility");
        await userProfile(adderId, res)

        //await creditHandlingNormal(res,req);
        //res.send(JSON.stringify({ "recieved": 1 }));
    } catch (err) {
        console.log(err);
        res.send(JSON.stringify({ "recieved": 0 }));
    }
    //await client.db("facility").collection("facilities").find(facility_title: addedFacilityTitle{$exists: true}))
});




/**
 * Purpose:  Letting user add comment and will display their comment to the approriate places
 * Pre:  User inputs a comment to a specific place they want
 * Post:  User comment will show up on the speific place of the app
 */
 app.post('/comment/add', async (req, res) => {

    //-----------------------------------------------------------------------------------------------
    console.log("running the function")
    var type = req.body.facilityType; //string
    const facilityId = parseInt(req.body.facility_id) //string
    const userId = req.body.user_id //string
    const replyContent = req.body.replyContent //string
    const userName = req.body.username;
    const rateScore = parseFloat(req.body.rateScore);
    let AdditionType = req.body.AdditionType;
    let goodUserId = req.body.upUserId;


    // var type = "0"; //string
    // const facilityId = parseInt( "12" ) //string
    // const userId = "l2542293790@gmail.com"; //string
    // const replyContent = " lalalalallala " //string
    // const userName =  " Simon Xia ";
    // const rateScore = parseFloat(3);
    // let AdditionType = "comment";
    // let goodUserId = userId;
    
    console.log("req body of comment add")
    console.log(req.body);

    var numberOfType = parseInt(type);
    console.log(numberOfType);
    type = interfacespecific(numberOfType);
    var date_ob = new Date();
    var year = date_ob.getFullYear();
    var month = date_ob.getMonth();
    var date = date_ob.getDate();
    var hours = date_ob.getHours();
    var minutes = date_ob.getMinutes();
    var seconds = date_ob.getSeconds();
    //const timeAdded = year + "/" + month + "/" +  date 
    const timeAdded = year + "/" + month + "/" + date + "/" + hours + "/" + minutes + "/" + seconds;
    const finding = await client.db("Help!Db").collection(type).findOne({_id: facilityId,"reviews.replierID" : userId});
    const finding2 = await client.db("Help!Db").collection(type).findOne({_id: facilityId,"ratedUser.replierID" : userId});


    console.log("type is");
    console.log(type);
    const theFacility =  await client.db("Help!Db").collection(type).findOne({_id: facilityId });
    console.log("the facility is")
    console.log(theFacility)

    var NumberOfRates = theFacility.facility.numberOfRates
    var total=0;

    for(var i = 0 ; i < theFacility.reviews.length; i++){
        total += theFacility.reviews[i].rateScore;  //total is the total rates in a facility
    }
    total+=rateScore;    
    const newScore = total / (NumberOfRates + 1);

    if(finding2 == null){
        await client.db("Help!Db").collection(type).updateOne(
            { _id: facilityId },
            {
                $push: {
                    "ratedUser": {
                        replierID: userId,
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
        await client.db("Help!Db").collection(type).updateOne(
            { _id: facilityId},
            {$set: {
                "facility.facilityOverallRate" : newScore
                }
            }
        );
        await client.db("Help!Db").collection("users").updateOne(
            { _id: userId },
            {
                $inc: {
                    "number_of_rate" : 1
                }
            }
        );
    }
    else{
        console.log("user already rated")
    }
    if(finding == null){
        try {
            await client.db("Help!Db").collection(type).updateOne(
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
            await client.db("Help!Db").collection("users").updateOne(
                { _id: userId },
                {
                    $inc: {
                        "number_of_reply": 1 
                    }
                }
            );
            //res.send(result);
            
            await creditHandlingNormal(client, AdditionType, goodUserId, findTheUser, creditCalculation);
            res.status(200).send({"result": "comment_add!"});
            //do real time update
            var gmails = req.body.reviewers; // gmail is a array ["string" , "string"]
            var facilityId2 = parseInt(req.body.facility_id); // 
            var type2 = parseInt(req.body.facilityType); // "int"
            var length2 = req.body.length; // "int"

            // var gmails = ["xyjyeducation@gmail.com","l2542293790@gmail.com"]; // gmail is a array ["string" , "string"]
            // var facilityId = parseInt("34"); // 
            // var type = parseInt("2"); // "int"
            // var length = parseInt("2"); // "int"
            console.log("This is real time update body");
            console.log(req.body);

            await realTimeUpdate("None", 0, gmails, facilityId2, type2, length2);
        }
        catch (err) {
            console.log(err);
            res.status(400).send(err);
        }
    }
    else{
        console.log("already commented");
        res.send(JSON.stringify({"result": "already_exist"}));
    }
    
});

//Purpose: similiar to comment/add bewsides this is removing comment from a speificed place
//Pre:  User comments exists in a speific place
//Post: User comments get removed at that speifici place
app.put('/comment/remove', async (req, res) => {
    const type = req.body.facilityType; //string
    const facilityId = req.body.facility_id //string
    const userId = req.body.user_id //string
    const replyContent = req.body.replyContent //string
    const upVotes = req.body.upVotes;
    const downVotes = req.body.downVotes;
    const timeAdded = req.body.timeAdded;
    const userName = req.body.username;
    const rateScore = parseFloat(req.body.rateScore);
    try {
        const result = await client.db("Help!Db").collection(type).updateOne(
            { _id: facilityId },
            {
                $pull: {
                    "reviews": {
                        replierID: userId,
                        userName: userName,
                        rateScore: rateScore,
                        upVotes: upVotes,
                        downVotes: downVotes,
                        replyContent: replyContent,
                        timeOfReply: timeAdded
                    }
                }
            }
        );
        res.send(result);
    }
    catch (err) {
        console.log(err);
        res.status(400).send(err);
    }
});



//following two report method are similar to this one
/**
 * Purpose: Let user chooses either to upvote or downvote something
 * Pre:  User chooses to upvote or downvote
 * Post:  If user upvotes the number of votes increase by one or else decrease by one
 */
app.put('/Votes', async (req, res) => {
    var type = req.body.facilityType; //string shou
    const facilityId = parseInt(req.body.facility_id) //string
    const userId = req.body.user_id //string
    const vote = req.body.vote; // string   
    const isCancelled = req.body.isCancelled; // string "cancel" or "pend"   

    console.log(req.body)

    var numberOfType = parseInt(type);
    console.log(numberOfType);
    type = interfacespecific(numberOfType);

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
        specific(req, res); // the facility type is not consistent and should be modified 
        

    }
    catch (err) {
        console.log(err);
        res.status(400).send(err);
    }
});


/**
 * Purpose: This is the credit calculator API which grants and removes credits when condions are met (contribution for add and report for removal)
 * Pre:  If user report others or make a review or add a facility
 * Post: User credit increase by xx amount if add place successfully; if report successful add by xx amount; if receive upvote add by xx amount 
 */
app.put('/creditHandling/report', async (req, res) => {

    const additionCredit_makeReport = 3;

    let AdditionType = req.body.AdditionType;
    let goodUserId = req.body.upUserId;
    let badUserId = req.body.downUserId;
    console.log("req body is")
    console.log(req.body)

    const result = await client.db("Help!Db").collection("users").findOne({ _id: goodUserId });
    console.log(result);
    var currentadderCredits = result.number_of_credit;
    if(badUserId != ""){
        const result2 = await client.db("Help!Db").collection("users").findOne({ _id: badUserId });
        var currentSubtractorCredits = result2.number_of_credit;
    }
    
    if (AdditionType == "report") {
        currentadderCredits += additionCredit_makeReport;
        currentSubtractorCredits -= additionCredit_makeReport;
    } else {
        console.log("No credits granted since no contributions made, please make contribution before any credit is granted");
        return;
    }
    await client.db("Help!Db").collection("users").updateOne({ _id: goodUserId },
        {
            $set:
            {
                number_of_credit: currentadderCredits,
            }
        }
    );
    await client.db("Help!Db").collection("users").updateOne({ _id: badUserId },
        {
            $set:
            {
                number_of_credit: currentSubtractorCredits,
            }
        }
    );
    res.send("success");

});

/**
 * Purpose: Adds credit to the user porfile if user mets the contriubution criteria (adding facility or commenting)
 * Pre: User eitrher adds facility or comments
 * Post:  Added credit show up on user profile
 */


async function creditHandlingNormal (client, AdditionType, goodUserId, findTheUser, creditCalculation){
    const additionCredit_addFacility = 5;
    const additionCredit_comment = 1;
    const result = await findTheUser(client, goodUserId)
    
    console.log("the user profile(handled by credit ) is ");
    console.log(result);
    var currentadderCredits = result.number_of_credit;
    // const result2 = await client.db("user").collection("users").findOne({_id : badUserId});
    // var currentSubtractorCredits = result2.number_of_credit;

    if (AdditionType === "addFacility") {
        currentadderCredits += additionCredit_addFacility;
    } else if (AdditionType === "comment") {
        currentadderCredits += additionCredit_comment;
    } else {
        res.send("No credits granted since no contributions made, please make contribution before any credit is granted; AdditionType is not matched in this case!");
    }
    await creditCalculation(client, goodUserId , currentadderCredits)   
}

app.post('/creditHandling/normal', async function (req, res ) {
    
    let AdditionType = req.body.AdditionType;
    let goodUserId = req.body.upUserId;
    console.log("credit input is ")
    console.log( req.body);
    creditHandlingNormal(client, AdditionType, goodUserId, findTheUser, creditCalculation)
    res.send(JSON.stringify({"creditHanldingNormal":"1"}))
    //res.send("asdohui")
});


async function insertUser(user_gmail,userName,logo){
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
}

async function userProfile(id,res){
    const result = await findTheUser(client, id);
    res.status(200).send(result)
}
async function google_sign_up (user_gmail, userName, logo, findTheUser1, insertUser1){
    const result = await findTheUser1(client, user_gmail)
    console.log(result);
    if(result == null){
       try{ 
           await insertUser1(user_gmail, userName, logo );
           console.log("Added to the db");
           res.send({"result":"Added to the db"});
       }catch(err){
           console.log(err);
           res.send(err);
       }
   }else{
        console.log("already exists");
        console.log("sign up JOSN is : " + JSON.stringify(result));
        return result;
   }
}
/**
 * Purpose:  Let user sign up tp the app with google authentication
 * Pre: User has never signed up with a particualr email
 * Post:  User sucessfully sign up and they get a specific profile dedicated to them
 */
app.post('/google_sign_up', async(req, res) => { 
    //var user_id = generate_new_user_id(); // write a method to generate a user id for new user
     const user_gmail = req.body._id;
     console.log("user gamil is :")
     console.log(user_gmail);
     const userName= req.body.username;
     const logo = req.body.user_logo;

     console.log(req.body);
     const result = await google_sign_up (user_gmail, userName, logo, findTheUser, insertUser); 
     console.log("user info json giving to frontend is")
     console.log(result)
     res.status(200).send(JSON.stringify(result))
     //const result= await client.db("Help!Db").collection("users").findOne({_id : user_gmail});
    
 }); 

//  const OneSignal = require('@onesignal/node-onesignal') ;
// const { analytics } = require('firebase-functions/v1');

//  const cors = require('cors');
//  const dotenv = require('dotenv');
 
//  app.use(cors());
//  app.use(bodyParser.urlencoded({ limit: '10mb', extended: true }));
//  dotenv.config();
 
//  var admin = require("firebase-admin");
//  var serviceAccount = require("/home/azureuser/Test 1/cpen321help-firebase-adminsdk-a9y9e-6d5b5af891.json"); // to be adjusted

 
//  admin.initializeApp({
//    credential: admin.credential.cert(serviceAccount)
//  });
 
//  const notification = {
//      title: "A Push Notification Test",
//      body: "Test Body"
//  };
//  const data = {
//      key1: "value1",
//      key2: "value2"
//  };
 
//  const ONESIGNAL_APP_ID ='f38cdc86-9fb7-40a5-8176-68b4115411da';

//  const app_key_provider = {
//     getToken() {
//         return 'MjJiMGQ3ODAtNjIzNC00ZjBkLTgxNGYtMDY0M2YxYWJlNjE0';
//     }
// };

// const configuration = OneSignal.createConfiguration({
//     authMethods: {
//         app_key: {
//         	tokenProvider: app_key_provider
//         }
//     }
// });



// const client2 = new OneSignal.DefaultApi(configuration);
// function createSeg(){
   
    
//     //OneSignal.Filter.apply("")
//     // analytics.identify('userId123', {
//     //     firstName: 'John',
//     //     lastName: 'Doe',
//     //        country: ‘USA'
//     //   });

//     // "filters": [
//     //     {"field": "tag", "key": "level", "relation": "=", "value": "10"},
//     //     {"field": "amount_spent", "relation": ">","value": "0"}
//     //   ];
    
//     const segment = new OneSignal.Segment();
//     segment.name = "Segment2";
//     // segment.filters = [{"email":"lufei8351@gmail.com"}, {"field": "Email", "key": "", "relation": "=", "value": "10"}]
//     segment.filters = [ {"field": "email",  "relation": "=", "value": "lufei8351@gmail.com"}]
//     client2.createSegments("f38cdc86-9fb7-40a5-8176-68b4115411da", segment)
    
// }

//  app.post('/sendToDevice', async function(req, res){
//     const notification = new OneSignal.Notification();
//     notification.app_id = 'f38cdc86-9fb7-40a5-8176-68b4115411da';
//     notification.included_segments = ["Active Users"];
//     //var segment = createSeg();
    
//     //console.log(notification.included_segments)
//     notification.contents = {
//         en: "Help is the best!"
//     };
//     const {id} = await client2.createNotification(notification);
//     console.log(id)
//     res.send("successfully notified");
      
//     // const response2 =  await client2.getNotification(ONESIGNAL_APP_ID, id);
// })


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

async function realTimeUpdate(reportMessage, notificationType,  gmails , facilityId, type, length){
    //var gmails = ["l2542293790@gmail.com", "xyjyeducation@gmail.com"];
    console.log("gmails are :")
    console.log(gmails);
    if(length === 0){
        return;
    }
    //var notificationType = req.body.notificationType; // int
    console.log(notificationType);
    type = interfacespecific(type);

    // if(type = 44){
    //     notificationType = reportMessage;
    // }else if(type = 22){
    //     notificationType = reportMessage;
    // }


    switch (notificationType) {
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
        case 6:
            notificationType = reportMessage;
            break;
         case 7:
            notificationType = reportMessage;
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
}
app.post('/sendToDevice3', async (req, res)=>{
    realTimeUpdate("reportMessage", 7,  "gmails" , 1, 2, 2);
})
/** purpose: push notification to users
 *  parameter: gmail array, facility's id, facility's type
 *  return message to indicate success
 */  
// app.post('/sendToDevice3', async function(req, res){

//     // var gmails = req.body.reviewers; // gmail is a array ["string" , "string"]
//     // var facilityId = parseInt(req.body.facility_id); // 
//     // var type = parseInt(req.body.facilityType); // "int"
//     // var length = req.body.length; // "int"

//     var gmails = ["xyjyeducation@gmail.com","l2542293790@gmail.com"]; // gmail is a array ["string" , "string"]
//     var facilityId = parseInt("34"); // 
//     var type = parseInt("2"); // "int"
//     var length = parseInt("2"); // "int"
//     console.log("This is real time update body");
//     console.log(req.body);

//     realTimeUpdate(gmails, facilityId, type, length);
    
    
//     // res.send("successfully notified");
      
//     //const response2 =  await client3.getNotification(ONESIGNAL_APP_ID, id);
//     res.send(JSON.stringify({"result":"real time done"}));
// })

async function run() {
    try {
        await client.connect();
        console.log(" successfully connect to db");
        var server = app.listen(8080, (req, res) => {
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


