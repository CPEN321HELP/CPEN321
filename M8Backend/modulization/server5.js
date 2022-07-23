var express = require('express');
var app = express();

const bodyParser = require('body-parser');
//const { application } = require('express');

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

const findTheUser = require("./user/findTheUser");
const insertUser = require("./user/insertUser");
const realTimeUpdate = require("./user/realTimeUpdate");
const google_sign_up = require("./user/google_sign_up");

const findAfacility = require("./facility/findAfacility");
const typeSelection = require("./facility/typeSelection")
const findMany = require("./facility/findMany") 
const returnLogic = require("./facility/returnLogic");
const logicOfAddFacility = require("./facility/logicOfAddFacility")
const insertFacility = require("./facility/insertFacility");

const voteManage = require("./reviewManagement/voteManage");
const rateManage = require("./reviewManagement/rateManage");
const commentManage = require("./reviewManagement/commentManage");


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
    var type = req.body.facilityType; 
    const id = parseInt(req.body.facility_id);
    const numberOfType = parseInt(type);
    type = typeSelection(numberOfType);
    const result =  findAfacility(client, type, id);
    res.status(200).send(result);
});

/**
 * Purpose: Get the newest facilities by page number
 * Pre: User scrolls on app with different pages and browses facility
 * Post:  User will get information for the facilities they would like to see
 */
app.post('/facility/newest', async (req, res) => {
    
    var type = req.body.type;
    const numberOfType = parseInt(type);
    type = typeSelection(numberOfType)
    const rest = findMany(client, type);
    res.send(rest);
});


/**
 * Purpose:  USer uses search functionality and get facility they want 
 * Pre:  User must enter valid input to get the result they like to see
 * Post:  User gets all relevany information about the facility they searched using the search bar
 */
app.post('/facility/search', async function searchOne(req, res){
    var type = req.body.type;
    var keyWordSearched = req.body.search;

    type = typeSelection(parseInt(type));
    var final = {};
    if (keyWordSearched.length > 20){
        keyWordSearched = keyWordSearched.slice(0,20);
    }else{
        final = searchOne(client, type, keyWordSearched);
        res.send(final);
    }
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
        var reportUserCond = req.body.reportUser;
      
       

        const reportredResultQuery = await client.db(myDb).collection(reportedFacilityTypeString).findOne({ _id: reportedFacilityID });

        

        var finalReportDecision = {
            facility_id: reportedFacilityID,
            facility_type: reportedFacilityType,
            reason: reportReason,
            reporter: reporterID,
            report_type: reportType,
            reported_user: reportedUSer,
            title: reportFacilityTitle,
            reportUserStatus: reportUserCond
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
                await creditCalculation(client, upUser, 1)
            if(downUSer != "none@gmail.com"){
                await creditCalculation(client, downUSer, -1)
            }
            res.send({"result":"report successful"});
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

                  client.db("Help!Db").collection("users").updateOne(
                    { _id: upUser },
                    {
                        $inc: {
                            "number_of_credit":1
                        }
                    }
                );
                if(downUSer != "none@gmail.com"){
                client.db("Help!Db").collection("users").updateOne(
                    { _id: downUSer },
                    {
                        $inc: {
                            "number_of_credit": -1
                        }
                    }
                );
                }
             
                res.send({"result":"report successful"});
                await realTimeUpdate(req.body.upMessage, 6, gmails, reportedFacilityid, 25, 1);
                await realTimeUpdate(req.body.downMessage, 7, gmails2, reportedFacilityid, 25, 1);  
         }
     }
    }
}
)

async function getDate(){
    var date_ob = await new Date();
    var year = await date_ob.getFullYear();
    var month = await date_ob.getMonth();
    var date = await date_ob.getDate();
    const timeAdded = year + "/" + month + "/" + date
    return timeAdded;
}

/**
 * Purpose: User adds a new facility to the app
 * Pre: User inputs relevant information 
 * Post:  New facility added to our app
 * Warning: Have to make sure if frontend call credithanlding http or not !!!!!!!!!!!
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
    const timeAdded = getDate();
    const lastOne = await client.db("Help!Db").collection(type).find({}).sort({ _id: -1 }).limit(1).toArray(); 
    console.log("last facility is : " + JSON.stringify(lastOne));
    var newId  = await logicOfAddFacility(lastOne) ; 
    await insertFacility(client, type, newId, title, description, facilityImageLink , timeAdded , long, lat, adderId)   
    await creditHandlingNormal(client, "addFacility", adderId);
});


/**
 * Purpose:  Letting user add comment and will display their comment to the approriate places
 * Pre:  User inputs a comment to a specific place they want
 * Post:  User comment will show up on the speific place of the app
 */
 app.post('/comment/add', async (req, res) => {

    var type = req.body.facilityType; //string
    const facilityId = parseInt(req.body.facility_id) //string
    const userId = req.body.user_id //string
    const replyContent = req.body.replyContent //string
    const userName = req.body.username;
    const rateScore = parseFloat(req.body.rateScore);
    let AdditionType = req.body.AdditionType;
    let goodUserId = req.body.upUserId;


    
    console.log("req body of comment add")
    console.log(req.body);

    var numberOfType = parseInt(type);
    console.log(numberOfType);
    type = typeSelection(numberOfType);
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
    // const theFacility =  await client.db("Help!Db").collection(type).findOne({_id: facilityId });
    const theFacility = findAfacility(client, type, facilityId )
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
        await rateManage(client, type, facilityId, userId);
    }
    else{
        console.log("user already rated")
    }
    if(finding == null){
        
        await commentManage(client, type, facilityId, userId, userName, rateScore, replyContent, timeAdded);
            //res.send(result);
            
            await creditHandlingNormal(client, AdditionType, goodUserId);
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
    else{
        console.log("already commented");
        res.send(JSON.stringify({"result": "already_exist"}));
    }
    
});

//Purpose: similiar to comment/add bewsides this is removing comment from a speificed place
//Pre:  User comments exists in a speific place
//Post: User comments get removed at that speifici place
// app.put('/comment/remove', async (req, res) => {
//     const type = req.body.facilityType; //string
//     const facilityId = req.body.facility_id //string
//     const userId = req.body.user_id //string
//     const replyContent = req.body.replyContent //string
//     const upVotes = req.body.upVotes;
//     const downVotes = req.body.downVotes;
//     const timeAdded = req.body.timeAdded;
//     const userName = req.body.username;
//     const rateScore = parseFloat(req.body.rateScore);
//     try {
//         const result = await client.db("Help!Db").collection(type).updateOne(
//             { _id: facilityId },
//             {
//                 $pull: {
//                     "reviews": {
//                         replierID: userId,
//                         userName: userName,
//                         rateScore: rateScore,
//                         upVotes: upVotes,
//                         downVotes: downVotes,
//                         replyContent: replyContent,
//                         timeOfReply: timeAdded
//                     }
//                 }
//             }
//         );
//         res.send(result);
//     }
//     catch (err) {
//         console.log(err);
//         res.status(400).send(err);
//     }
// });



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
    voteManage(client, vote, type, facilityId, isCancelled, userId)
    res.send({"result":"voteIsCompleted"});
});


/**
 * Purpose: This is the credit calculator API which grants and removes credits when condions are met (contribution for add and report for removal)
 * Pre:  If user report others or make a review or add a facility
 * Post: User credit increase by xx amount if add place successfully; if report successful add by xx amount; if receive upvote add by xx amount 
//  */
// app.put('/creditHandling/report', async (req, res) => {

//     const additionCredit_makeReport = 3;
//     let AdditionType = req.body.AdditionType;
//     let goodUserId = req.body.upUserId;
//     let badUserId = req.body.downUserId;
//     console.log("req body is")
//     console.log(req.body)

//     const result = await client.db("Help!Db").collection("users").findOne({ _id: goodUserId });
//     console.log(result);
//     var currentadderCredits = result.number_of_credit;
//     if(badUserId != ""){
//         const result2 = await client.db("Help!Db").collection("users").findOne({ _id: badUserId });
//         var currentSubtractorCredits = result2.number_of_credit;
//     }
    
//     if (AdditionType == "report") {
//         currentadderCredits += additionCredit_makeReport;
//         currentSubtractorCredits -= additionCredit_makeReport;
//     } else {
//         console.log("No credits granted since no contributions made, please make contribution before any credit is granted");
//         return;
//     }
//     await client.db("Help!Db").collection("users").updateOne({ _id: goodUserId },
//         {
//             $set:
//             {
//                 number_of_credit: currentadderCredits,
//             }
//         }
//     );
//     await client.db("Help!Db").collection("users").updateOne({ _id: badUserId },
//         {
//             $set:
//             {
//                 number_of_credit: currentSubtractorCredits,
//             }
//         }
//     );
//     res.send("success");
// });

/**
 * Purpose: Adds credit to the user porfile if user mets the contriubution criteria (adding facility or commenting)
 * Pre: User eitrher adds facility or comments
 * Post:  Added credit show up on user profile
 */
app.post('/creditHandling/normal', async function (req, res ) { 
    let AdditionType = req.body.AdditionType;
    let goodUserId = req.body.upUserId;
    console.log("credit input is ")
    console.log( req.body);
    creditHandlingNormal(client, AdditionType, goodUserId)
    res.send(JSON.stringify({"creditHanldingNormal":"1"}))
});

/**
 * 
 * Purpose:  Let user sign up tp the app with google authentication
 * Pre: User has never signed up with a particualr email
 * Post:  User sucessfully sign up and they get a specific profile dedicated to them
 */
app.post('/google_sign_up', async(req, res) => { 
    
    const user_gmail = req.body._id;
    console.log("user gamil is :")
    console.log(user_gmail);
    const userName= req.body.username;
    const logo = req.body.user_logo;

    console.log(req.body);
    const result = await google_sign_up (client, user_gmail, userName, logo); 
    console.log("user info json giving to frontend is")
    console.log(result)
    res.status(200).send(JSON.stringify(result))
}); 
 
app.post('/sendToDevice3', async (req, res)=>{
    realTimeUpdate("reportMessage", 7,  "gmails" , 1, 2, 2);
})

async function run() {
    try {
        await client.connect();
        console.log(" successfully connect to db");
        var server = app.listen(3000, (req, res) => {
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


