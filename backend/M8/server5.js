var express = require('express');
var app = express();

const bodyParser = require('body-parser');
//const { application } = require('express');



const { MongoClient } = require("mongodb");

var ObjectId = require('mongodb').ObjectID;
const uri = "mongodb://127.0.0.1:27017"
const client = new MongoClient(uri)

const { Router } = require("express");
const router = new Router();

//for accessing other apis

app.use(express.json());

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

router.use(express.json());
router.use(bodyParser.json());
router.use(bodyParser.urlencoded({ extended: true }));


//const interfacespecific = require('./interfacespecific');
//const creditCalculation = require("./creditCalculation");


const realTimeUpdate = require("./user/notification/realTimeUpdate");
const google_sign_up = require("./user/signIn/google_sign_up");


const FindAfacility = require("./facility/FacilityDisplay/findAfacility");


const TypeSelection = require("./facility/FacilityDisplay/typeSelection")
const findMany = require("./facility/FacilityDisplay/findMany")
const logicOfAddFacility = require("./facility/facilityManagement/logicOfAddFacility")
const insertFacility = require("./facility/facilityManagement/insertFacility");
const searchOne = require("./facility/FacilityDisplay/searchOne")
const reportFacility = require("./facility/facilityManagement/reportFacility")

const voteManage = require("./reviewManagement/commentReview/voteManage");

const commentManage = require("./reviewManagement/facilityReview/commentManage");
const rateManage = require("./reviewManagement/facilityReview/rateManage");

const creditCalculation = require("./user/credit/creditCalculation")
const creditHandlingNormal = require("./user/credit/creditHandlingNormal")

const displayReports = require("./Administrator/Display/displayReports");

const deleteComment = require("/home/azureuser/Test 1/reviewManagement/commentReview/deleteComment.js")

app.get('/',
    async function (req, res) {
        // res.json({result:"Connected to test"});
        res.send({ Result: "Connected to server at http://20.213.243.141:3000/" });
    });


// app.post('/t', async function (req, res) {
//     var type = req.body.name;
//     //var w = req.body.age; 
//     // const id = parseInt(req.body.facility_id);
//     // const numberOfType = parseInt(type);
//     // // type = typeSelection(numberOfType);
//     // const result =  findAfacility(client, numberOfType, id, "");
//     // res.status(200).send(result);
//     res.send({ sss: type })
// });
/**
 * purpose: get a specific detailed look of a post
 * pre: user chooses the specific facility they would like
 * post: get the speifici post a suer wants
 */
app.post('/specific', async function (req, res) {
    const a = new FindAfacility();
    var type = req.body.facilityType;
    const id = parseInt(req.body.facility_id,10);
    console.log(req.body)
    const numberOfType = parseInt(type,10);
    // type = typeSelection(numberOfType);
    const result = await a.findAfacility(client, numberOfType, id, "");
    console.log(result)
    if (result == null || result == {}) {
        res.status(404).send({ "result": "null" })
    }
    else {
        res.status(200).send(result);
    }
});

/**
 * Purpose: Get the newest facilities by page number
 * Pre: User scrolls on app with different pages and browses facility
 * Post:  User will get information for the facilities they would like to see
 */
app.post('/facility/newest', async (req, res) => {

    const a = new TypeSelection();
    var type = req.body.type;

    const numberOfType = parseInt(type,10);
    type = await a.typeSelection(numberOfType)
    const rest = await findMany(client, type);
    if(rest == null){
        res.status(404).send({"result" : "null"})
    }
    else{
        res.status(200).send(rest);
    }
    
});


/**
 * Purpose:  USer uses search functionality and get facility they want 
 * Pre:  User must enter valid input to get the result they like to see
 * Post:  User gets all relevany information about the facility they searched using the search bar
 */
app.post('/facility/search', async function (req, res) {
    const a = new TypeSelection();
    var type = req.body.type;
    var keyWordSearched = req.body.search;
     
    if(isNaN(type) ){
        res.status(404).send(null);
    }
    else{
        var type2 = await a.typeSelection(parseInt(type,10));
        if (keyWordSearched.length > 20) {
            keyWordSearched = keyWordSearched.slice(0, 20);
        }
        const final = await searchOne(client, type2, keyWordSearched);

        // var final = {};
        //try{
            // await client.db("Help!Db").collection(type).find({ "facility.facilityTitle": { $regex: new RegExp(keyWordSearched, "i") } }).forEach((result) => {
            //     console.log(result)
            //     //final = returnLogic(result);
            // });

        //     var arr = [];

        //     const ff = await client.db("Help!Db").collection(type).find(
        //         {
        //             // $and: [
        //             //     {
        //             //         "facility.facilityTitle" : { $regex: keyWordSearched } 
        //             //     }
        //             // ]
        //         }
        //     ).toArray();

        //     console.log(ff)
        //     for(var i = 0 ; i < ff.length; i++){
        //         arr.push(ff);
        //     }
        //     console.log("arr is")
        //     console.log(arr)
        //         //final = returnLogic(result);
        //     //});
        //     return final;
        // }catch(err){}



        if(final == null){
            res.status(404).send(final);
        }
        else{
            res.status(200).send(final);
        }
    }
});

/**
 * Purpose: Let admin access two queries at a time
 * Pre: NO precondition
 * Post:  Give admin access up to two queries from 0 query (query is in a queue first in first handled)
 */
app.get('/report/admin',
    async function (req, res) {
        const final = await displayReports(client);
        console.log("sad")
        console.log(final)
        res.status(200).send(final)
    })

/**
 * Purpose: user reports inapporatiate facilities and puts this request in query
 * Pre: User must make a report dedicated to a facility
 * Post: Result gets in query
 */
 app.post('/user/Report/commentAndfacility',
 async function (req, res) {
    // var myDb = "Help!Db";
    // var myCollection;
    // myCollection = "reportedComment";

     //follwing four needs to match with frontend
    // console.log("Testing report comment and facility is: " + JSON.stringify(req.body));
    var reportedFacilityID =  parseInt(req.body.reportedFacilityID,10);
    var reportedFacilityType = parseInt(req.body.reportedFacilityType,10);
    var reportFacilityTitle = req.body.title;
    var reportedFacilityTypeString;
    if (parseInt(req.body.reportedFacilityType,10) === 0){
        reportedFacilityTypeString = "posts";
    }
    if (parseInt(req.body.reportedFacilityType,10) === 1){
        reportedFacilityTypeString = "studys";
    }
    if (parseInt(req.body.reportedFacilityType,10) === 2){
        reportedFacilityTypeString = "entertainments";
    }
    if (parseInt(req.body.reportedFacilityType,10) === 3){
        reportedFacilityTypeString = "restaurants";
    }
    var reporterID = req.body.reporterID;
    var reportReason = req.body.reportReason;
    var reportedUSer = req.body.reported_id;
    var reportType = req.body.report_type;
    var reportUserCond = req.body.reportUser;
   
     
    await reportFacility(client, reportedFacilityType, reportedFacilityID, reportReason,  
        reporterID, reportType, reportedUSer, reportFacilityTitle, reportUserCond, reportedFacilityTypeString)
    //  var finalReportDecision = {
    //      facility_id: reportedFacilityID,
    //      facility_type: reportedFacilityType,
    //      reason: reportReason,
    //      reporter: reporterID,
    //      report_type: reportType,
    //      reported_user: reportedUSer,
    //      title: reportFacilityTitle,
    //      reportUserStatus: reportUserCond
    //  }
    //  //sending request to admin and wait for approval or denial
    //  await client.db(myDb).collection(myCollection).insertOne(finalReportDecision, function (err, res) {
    //      var finalResult = JSON.stringify(res);
    //      if (err) throw err;
    //      console.log("Added one query from user report which is : "+ finalResult);
    //  });

    res.status(200).send(JSON.stringify({result: "query added"}));
 }
)


/**
 * Purpose: let admin approve or deny the report request being querired
 * Pre: Request is in query
 * Post: if accepted update field for facility or comment else remove element in query and nothing happens
 */
app.post('/admin/reportApproval',
    async function (req, res) {
        console.log("admin apporve body is :" + JSON.stringify(req.body));
        var reportType;
        if (parseInt(req.body.report_type,10) === 5) {
            // console.log("sssssss")
            reportType = "comment";
        }
        if (parseInt(req.body.report_type,10) === 6) {
            // console.log("ttttt")
            reportType = "facility";
        }
        if(req.body.adminEmail != "lufei8351@gmail.com" && req.body.adminEmail != "l2542293790@gmail.com" &&
        req.body.adminEmail != "wuyuheng0525@gmail.com"  && req.body.adminEmail != "simonxia13.13@gmail.com" &&
        req.body.adminEmail != "cpen321ubc@gmail.com" && req.body.adminEmail!="xyjyeducation@gmail.com"){
            // console.log("xxxxxxxxxxxxxxxxxxxxxxxxxxx")
            res.status(404).json({result: "not admin making decision"});
        }
        var reportID = req.body.report_id;
        var approveDecision = req.body.approve;
        var facility_type = req.body.facility_type;
        var reportedFacilityid = parseInt(req.body.facility_id,10);
        var reportedID = req.body.reported_user;
        var myDb = "Help!Db"; //change myDb to Help!Db at the end
        var upUser = req.body.upUserId;
        var downUSer = req.body.downUserId;
        // console.log("admin apporve body is :" + JSON.stringify(req.body));
        if (req.body.adminEmail != null) {
            if (reportType == "facility") {
                if (parseInt(req.body.approve,10) === 0) {
                    await realTimeUpdate(req.body.upMessage, 6, [upUser], reportedFacilityid, 25, 1);
                    client.db(myDb).collection("reportedComment").deleteOne({ _id: ObjectId(reportID) }, function (err, obj) {
                        if (err) throw err;
                        console.log("1 document deleted in reportedComment due to reported facility");
                       
                       
                        // res.json({status:200} , {"result": "report unsuccessful"})
                    });
                    res.status(200).send(JSON.stringify({ "result": "report unsuccessful" }));
                } else {
                    // blockFacility(client, myDb, req.body.facility_type, reportedFacilityid);
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
                    client.db(myDb).collection("reportedComment").deleteOne({ _id: ObjectId(reportID) }, function (err, obj) {
                        if (err) throw err;
                        console.log("1 document deleted in reportedComment due to reported facility");
                    });
                    await creditCalculation(client, upUser, 1)
                    if (downUSer != "none@gmail.com") {
                        await creditCalculation(client, downUSer, -1)
                    }

                    await realTimeUpdate(req.body.upMessage, 6, [upUser], reportedFacilityid, 25, 1);
                    await realTimeUpdate(req.body.downMessage, 7, [downUSer], reportedFacilityid, 25, 1);
                    res.status(200).send({ "result": "report successful" });
                }
            }
            else {
                if (approveDecision == "0") {
                    await realTimeUpdate(req.body.upMessage, 6, [upUser], reportedFacilityid, 25, 1);
                    client.db(myDb).collection("reportedComment").deleteOne({ _id: ObjectId(reportID) }, function (err, obj) {
                        if (err) throw err;
                        console.log("1 document deleted in reportedComment due to reported comment");
                    });
                    res.status(200).send(JSON.stringify({ "result": "report unsuccessful" }));
                }
                else {
                    var gmails = [upUser];
                    var gmails2 = [downUSer];
                    myCollection = "reportedComment";
                    console.log("down gmail is :" + gmails2);
                    // remove the comment !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    await deleteComment(client, facility_type, reportedFacilityid, reportedID);
                    // client.db(myDb).collection(facility_type).updateOne(  
                    //     { _id: reportedFacilityid },
                    //     {
                    //         $set: {
                    //             "reviews.$[elem]": []
                    //         }
                    //     },
                    //     { arrayFilters: [{ "elem.replierID": { $eq: reportedID } }] }
                    //      );
                    // remove in the reportComment collection 
                    client.db(myDb).collection("reportedComment").deleteOne({ _id: ObjectId(reportID) }, function (err, obj) {
                        if (err) throw err;
                        console.log("1 document deleted in reportedComment due to reported comment");
                    });
                    // increase credit 
                    await creditCalculation(client, upUser, 1)
                    if (downUSer != "none@gmail.com") {
                        // decrease credit
                        await creditCalculation(client, downUSer, -1)
                    }
                    res.status(200).send({ "result": "report successful" });
                    //real time update
                    await realTimeUpdate(req.body.upMessage, 6, gmails, reportedFacilityid, 25, 1);
                    await realTimeUpdate(req.body.downMessage, 7, gmails2, reportedFacilityid, 25, 1);
                }
            }
        }
    }
)

async function getDate() {
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
    // const a = new FindAfacility()
    const b = new TypeSelection()
    const title = req.body.title
    const description = req.body.description
    const adderId = req.body.adderID
    var long = parseFloat(req.body.long)
    var lat = parseFloat(req.body.lat)
    var type2 = parseInt(req.body.type,10);  // a string of name without s whattttt
    const facilityImageLink = req.body.facilityImageLink;
    console.log("req body is " + req.body);

    const timeAdded = await getDate();
    const type = b.typeSelection(type2)
    const lastOne = await client.db("Help!Db").collection(type).find({}).sort({ _id: -1 }).limit(1).toArray(); 

    //const lastOne = await a.findAfacility(client, type2, "", "array");
    
    console.log("last facility is : " + JSON.stringify(lastOne));
    var newId = await logicOfAddFacility(lastOne);
    const message = await insertFacility(client, type, newId, title, description, facilityImageLink, timeAdded, long, lat, adderId)
    console.log("blablabla")
    res.json({
        status: 200,
        data: message
    })
});


/**
 * Purpose:  Letting user add comment and will display their comment to the approriate places
 * Pre:  User inputs a comment to a specific place they want
 * Post:  User comment will show up on the speific place of the app
 */
app.post('/comment/add', async (req, res) => {
    const a = new TypeSelection();
    var type = req.body.facilityType; //string
    const facilityId = parseInt(req.body.facility_id,10) //string
    const userId = req.body.user_id //string
    const replyContent = req.body.replyContent //string
    const userName = req.body.username;
    const rateScore = parseFloat(req.body.rateScore);
    let AdditionType = req.body.AdditionType;
    let goodUserId = req.body.upUserId;

    console.log("req body of comment add")
    console.log(req.body);

    var numberOfType = parseInt(type,10);
    console.log(numberOfType);
    type = await a.typeSelection(numberOfType);
    var date_ob = new Date();
    var year = date_ob.getFullYear();
    var month = date_ob.getMonth();
    var date = date_ob.getDate();
    var hours = date_ob.getHours();
    var minutes = date_ob.getMinutes();
    var seconds = date_ob.getSeconds();
    //const timeAdded = year + "/" + month + "/" +  date 
    const timeAdded = year + "/" + month + "/" + date + "/" + hours + "/" + minutes + "/" + seconds;
 

        //const finding2 = await client.db("Help!Db").collection(type).findOne({_id: facilityId,"ratedUser.replierID" : userId});

        // const theFacility =  await client.db("Help!Db").collection(type).findOne({_id: facilityId });
        // //const theFacility = await findAfacility(client, numberOfType, facilityId , "")
       
        await rateManage(client, type, facilityId, userId, numberOfType, rateScore)

        // if(finding2 == null){ // meaning the user hasn't rated this facility yet
        //     await rateManage(client, type, facilityId, userId, newScore);
        // }
    
        //const finding = await client.db("Help!Db").collection(type).findOne({_id: facilityId, "reviews.replierID" : userId});
        const status = await commentManage(client, type, facilityId, userId, userName, rateScore, replyContent, timeAdded, AdditionType, goodUserId);
        if (status === 1) { // meaning the user hasn't commented on this facility yet
            res.status(200).send({ "result": "comment_add!" });
            var gmails = req.body.reviewers; // gmail is a array ["string" , "string"]
            var facilityId2 = parseInt(req.body.facility_id,10); // 
            var type2 = parseInt(req.body.facilityType,10); // "int"
            var length2 = req.body.length; // "int"
            await realTimeUpdate("None", 0, gmails, facilityId2, type2, length2);
        }
        else if(status === 2){
            res.status(404).send(JSON.stringify({ "result": "null" }));
        }
        else if(status === 4){
            res.status(404).send(JSON.stringify({ "result": "nonfacility" }));
        }
        else {
            console.log("already commented");
            res.status(208).send(JSON.stringify({ "result": "outofrange" }));
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
app.post('/Votes', async (req, res) => {
    const a = new TypeSelection()
    var type = req.body.facilityType;                   //string 
    const facilityId = parseInt(req.body.facility_id, 10) //string
    const userId = req.body.user_id                      //string
    const vote = req.body.vote;                          // string   
    const isCancelled = req.body.isCancelled;            // string "cancel" or "pend"   
    // var type = "1"                //string 
    // const facilityId = parseInt("11") //string
    // const userId = "simon@gmail.com"                      //string
    // const vote = "down";                          // string   
    // const isCancelled = "cancel";            // string "cancel" or "pend"   
    var numberOfType = parseInt(type);
    console.log(numberOfType);
    //console.log(req.body)
    if(!type || !facilityId || !userId || !vote ){
        res.status(404).send({"data" : "null"})
    }
    else{
        type = a.typeSelection(numberOfType);
        const result = await voteManage(client, vote, numberOfType, facilityId, isCancelled, userId)
        if(result === 1){
            res.send({"data": "upvote"});
        } 
        else if(result === 2){
            res.send({"data": "cancel upvote"});
        }
        else  if(result === 3){
            res.send({"data": "downvote"});
        }
        else if(result === 4){
            res.send({"data": "cancel downvote"});
        }
        else if( result === 10){
            res.status(404).send({"data": "nonexistent"})
        }
        else if(result === 20){
            res.status(404).send({"data": "neverCommented"})
        }
        else{
            res.send({"data": "what"});
        }
    }    
});


/**
 * Purpose: This is the credit calculator API which grants and removes credits when condions are met (contribution for add and report for removal)
 * Pre:  If user report others or make a review or add a facility
 * Post: User credit increase by xx amount if add place successfully; if report successful add by xx amount; if receive upvote add by xx amount 
//  */
// app.put('/creditHandli ng/report', async (req, res) => {

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

// X----------------------------------------------------------------------------------------------------------------------------------
app.post('/creditHandling/normal', async function (req, res) {
    let AdditionType = req.body.AdditionType;
    let goodUserId = req.body.upUserId;
    console.log("credit input is ")
    console.log(req.body);
    creditHandlingNormal(client, AdditionType, goodUserId)
    res.send(JSON.stringify({ "creditHanldingNormal": "1" }))
});

/**
 * 
 * Purpose:  Let user sign up tp the app with google authentication
 * Pre: User has never signed up with a particualr email
 * Post:  User sucessfully sign up and they get a specific profile dedicated to them
 */
app.post('/google_sign_up', async (req, res) => {
    const user_gmail = req.body._id;
    console.log("user gamil is :")
    console.log(user_gmail);
    const userName = req.body.username;
    const logo = req.body.user_logo;
    console.log(req.body);
    if(!user_gmail || !userName){
        res.status(404).send({"data" : "null"})
    }
    else
    {
        const result = await google_sign_up(client, user_gmail, userName, logo);
        if(result === 1){
            res.status(201).send({"data":"added"})
        }
        else{
            console.log("user info json giving to frontend is")
            console.log(result)
            res.status(200).send(JSON.stringify(result))
        }
        
    }
    
});


// X-------------------------------------------------------------------------------------------------------------------------------------------
// app.post('/sendToDevice3', async (req, res) => {
//     realTimeUpdate("reportMessage", 7, "gmails", 1, 2, 2);
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
//require('./sroutes')(app);
module.exports = app;

