var express = require('express');
var app = express();

const bodyParser = require('body-parser');
const { application } = require('express');

var util = require('util');
var encoder = new util.TextEncoder('utf-8');
const { MongoClient } = require("mongodb");
const { response } = require("express");
const uri = "mongodb://127.0.0.1:27017"
const client = new MongoClient(uri)

//for accessing other apis
const request = require('request');
app.use(express.json());

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));
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
        //when connecting with frontend use below two lines
        //var facility_id = req.body.facility_id
        //
        // var rateScores = req.body.userRates;
        //userRates.push(rateScores)
        var myDb = "Help!Db";
        var facilityType;
        //confirm this with frontend
        var requestJson = {_id:req.body._id, 
                            rateScore:parseInt(req.body.rateScore),
                            facility_type:req.body.facility_type, 
                            facility_id:req.body.facility_id};
        if(requestJson.facility_type == "0"){
            facilityType ="posts";
        }else if (requestJson.facility_type == "1"){
            facilityType ="studys";
        }else if(requestJson.facility_type == "2"){
            facilityType ="entertainments";
        }else if(requestJson.facility_type == "3"){
            facilityType ="restaurants";
        }

         await client.db(myDb).collection("userRateInfo").insertOne(requestJson, function (err, res) {
            var finalResult = JSON.stringify(res);
            if (err) throw err;
            console.log(finalResult);
        });
      
        const userRates = [];
        const ratedUsers = []; //replace by frontend stuff later
        await client.db(myDb).collection("userRateInfo").find().forEach(function (myDoc) {
            userRates.push(myDoc.rateScore);
            console.log("user rate array is :  "+ userRates);
            ratedUsers.push(myDoc._id);
            console.log("rated user array is: " + ratedUsers);   
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
            var newvalues ={$set: { "number_of_rate": resultUserRates.number_of_rate+=1 }}
             await client.db(myDb).collection("users").updateOne(myquery, newvalues, function (err, res) {
                if (err) throw err;
                console.log("The status for updating field is:" + JSON.stringify(res));
            });

        }


        try {

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
                        "longtitude": result.facility.longtitude,
                        "latitude": result.facility.latitude
                    },
                    "ratedUser": ratedUserField
                }
            };

            await client.db(myDb).collection(requestJson.facility_type).updateOne(myquery, newvalues, function (err, res) {
                if (err) throw err;
                console.log("The status for updating field is:" + JSON.stringify(res));

            });
            // res.status(200).send(userRates);

        }
        catch (err) {
            console.log(err);
            res.status(400).send(err);
        }

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
        const bigArr = []
        await client.db("Help!Db").collection(type).find({}).sort({ _id: -1 }).toArray(function (err, result) {
            if (err) throw err;
            try {
                console.log(result[0]._id)
                var len = result.length;
                for (var i = 0; i < len; i += 1) {
                    var arr = [];
                    arr.push(result[i]._id)
                    arr.push(result[i].facility.facilityOverallRate)
                    arr.push(result[i].facility.facilityTitle)
                    arr.push(result[i].facility.facilityDescription)
                    arr.push(result[i].facility.timeAdded)
                    bigArr.push(arr);
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
    var keyWordSearched = req.body.title;
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
        await client.db("Help!Db").collection(type).find({ "facility.facilityTitle": keyWordSearched }).toArray(function (err, result) {
            if (err) throw err;
            console.log(result);
            res.send(result);
        });
    }
    catch (err) {
        console.log(err);
        res.status(400).send(err);
    }
});

/**
 * Purpose: user reports inapporatiate facilities and puts this request in query
 * Pre: User must make a report dedicated to a facility
 * Post: Request puts in query
 */
app.post('/user/Report/facility',
    async function (req, res) {
        var myDb = "facility";  //change this to Help!Db at the end
        var myCollection;
        myCollection = "reportedFacility";

        //following fouyr should all come from frontend
        var reportedFacilityID = "1";
        var reportedFacilityType = "entertainments"; //need to chaange this to something frontend sends later 
        var reporterID = "jiajungao0124@gmail.com";
        var reportReason = "idk caonima";

        const reportredResultQuery = await client.db("facility").collection(reportedFacilityType).findOne({ _id: reportedFacilityID });

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
 * Purpose: user reports inapporatiate facilities and puts this request in query
 * Pre: User must make a report dedicated to a facility
 * Post: Result gets in query
 */
app.get('/user/Report/comment',
    async function (req, res) {
        var myDb = "facility";
        var myCollection;
        myCollection = "reportedComment";

        //follwing four needs to match with frontend
        var reportedFacilityID = "7";
        var reportedFacilityType = "entertainments"; //need to chaange this to something frontend sends later 
        var reporterID = "jiajungao0124@gmail.com";
        var reportReason = "idk caonima";

        var commentQuery = [];


        const reportredResultQuery = await client.db("facility").collection(reportedFacilityType).findOne({ _id: reportedFacilityID });

        for(i=0;i<reportredResultQuery.reviews.length;i++){
            commentQuery.push(reportredResultQuery.reviews[i].replyContent);
            console.log(commentQuery);
        }

        var finalReportDecision = {
            old_id: reportedFacilityID,
            facility_type: reportedFacilityType,
            reason: reportReason,
            reporter: reporterID,
            comments: commentQuery
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
 * Purpose:  Admin handles all kinds of report request and make decision
 * Pre: Admin reveiws all information from user query
 * Post:  Admin approves report if report is valid or else admin denies report
 */
app.get('/admin/reportApproval',
    async function (req, res) {
        //need things from frontend and change it later
        var responseJson = {
            report_type: "comment",
            temp_id: 1,
            approve: 1
        }
        var myDb = "facility"; //change myDb to Help!Db at the end
        var myCollection ;
        var oldFacilityID;
        var facility_type;
        var finalResult = JSON.stringify(responseJson);
        if (responseJson.report_type == "facility") {
            myCollection = "reportedFacility";
            var finalDecisionArray = [];
            await client.db(myDb).collection(myCollection).find().forEach(function (myDoc) {
                var innerArray = [];
                oldFacilityID = myDoc.old_id;
                facility_type = myDoc.facility_type;
                var reportReason = myDoc.reason;
                var reporterID = myDoc.reporter;
                var facilityTitle = myDoc.facility_title;
                var descriptionFacility = myDoc.facility_description;
                var timeAddedFacility = myDoc.facility_timeAdded;
                var imageLink = myDoc.facility_imageLink;
                var overallrateFacility = myDoc.facility_overallRate;
                var numOfRates = myDoc.facility_numberOfRates;
                var reviewsFacility = myDoc.facility_reviews;
                innerArray = [oldFacilityID, facility_type, reportReason, reporterID, facilityTitle, descriptionFacility, timeAddedFacility,
                imageLink, overallrateFacility, numOfRates, reviewsFacility];
                // console.log("inner array is: " + innerArray);

                finalDecisionArray.push(innerArray);
                // console.log("outer array is: " + finalDecisionArray);
            });

            res.send(finalDecisionArray);

            //change facility status if approved
            if (responseJson.approve == 1) {
                const reportredResultQuery = await client.db(myDb).collection(facility_type).findOne({ _id: oldFacilityID });
                var finalReportDecisionJSON = {
                    "facility_status": finalResult,
                    "facilityType": reportredResultQuery.facility.facilityType,
                    "facilityTitle": reportredResultQuery.facility.facilityTitle,
                    "facilityDescription": reportredResultQuery.facility.facilityDescription,
                    "timeAdded": reportredResultQuery.facility.timeAdded,
                    "facilityImageLink": reportredResultQuery.facility.facilityImageLink,
                    "facilityOverallRate": reportredResultQuery.facility.facilityOverallRate,
                    "numberOfRates": reportredResultQuery.facility.numberOfRates,
                    "longtitude": reportredResultQuery.facility.longtitude,
                    "latitude": reportredResultQuery.facility.latitude
                }
                var myquery = { _id: oldFacilityID };
                var newvalues = { $set: { "facility": finalReportDecisionJSON } };
                console.log(oldFacilityID);

                await client.db(myDb).collection(facility_type).updateOne(myquery, newvalues, function (err, res) {
                    if (err) throw err;
                    console.log("The status for updating field is:" + JSON.stringify(res));
                });
            }
        }
        else {
            myCollection = "reportedComment";
            var finalDecisionArrayB = [];
            await client.db(myDb).collection(myCollection).find().forEach(function (myDoc) {
                var innerArrayB = [];
                var oldFacilityIDB = myDoc.old_id;
                var facility_typeB = myDoc.facility_type;
                var reportReasonB = myDoc.reason;
                var reporterIDB = myDoc.reporter;
                var comments =myDoc.comments;
              
                innerArrayB = [oldFacilityIDB, facility_typeB, reportReasonB, reporterIDB, comments];
               

                finalDecisionArrayB.push(innerArrayB);
               

            });
            res.send({result:finalDecisionArrayB});
            if (responseJson.approve == 1) {
                var reportedComment = "fuck this big big big huge shit"; //need to change with actual comment
                facility_type="entertainments"; //need to change
                const reportredResultQueryB = await client.db(myDb).collection("entertainments").findOne({ _id: "7"});
               
                var updateObj ={};
            
                for(i=0;i<reportredResultQueryB.reviews.length;i++){
                    console.log("comment is: " + reportredResultQueryB.reviews[i].replyContent);
                    if(reportredResultQueryB.reviews[i].replyContent ==reportedComment){
                       

                          updateObj = {
                                            replierID: reportredResultQueryB.reviews[i].replierID,
                                            numberOfUpvote: reportredResultQueryB.reviews[i].numberOfUpvote,
                                            numberOfDownvote: reportredResultQueryB.reviews[i].numberOfDownvote,
                                            replyContent: reportredResultQueryB.reviews[i].replyContent,
                                            timeOfReply: reportredResultQueryB.reviews[i].timeOfReply
                                        };
                  
                    }
                    }

                    // console.log("This is update obj: "+ JSON.stringify(updateObj));
                    
                      await client.db(myDb).collection("entertainments").updateOne(
                      
                        { _id: "7" },
                        {
                            $pull: {"reviews": updateObj}
                        }
                    );

             
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
    console.log(req.body);
    var date_ob = new Date();
    var year = date_ob.getFullYear();
    var month = date_ob.getMonth();
    var date = date_ob.getDate();
    const timeAdded = year + "/" + month + "/" + date
    const lastOne = await client.db("Help!Db").collection(type).find({}).sort({ _id: -1 }).limit(1).toArray();
    console.log(lastOne);
    var newId = "1";
    if (lastOne.length == 0) {
    }
    else {
        var d = lastOne[lastOne.length - 1]._id;
        var dd = parseInt(d);
        dd += 1;
        newId = dd.toString();
    }
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
                "timeAdded": timeAdded
            },
            "location": {
                "lontitdue": long,
                "latitude": lat
            },
            "rated_user": [{}],
            "reviews": [{}]
        });
        res.send({ "recieved": 1 });
    } catch (err) {
        console.log(err);
        res.send({ "recieved": 0 });
    }

    //await client.db("facility").collection("facilities").find(facility_title: addedFacilityTitle{$exists: true}))
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
    //const timeAdded = year + "/" + month + "/" +  date 
    const timeAdded = year + "/" + month + "/" + date + "/" + hours + "/" + minutes + "/" + seconds;
    try {
        const result = await client.db("Help!Db").collection(type).updateOne(
            { _id: facilityId },
            {
                $push: {
                    "reviews": {
                        replierID: userId,
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
        res.send(result);
    }
    catch (err) {
        console.log(err);
        res.status(400).send(err);
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
    try {
        const result = await client.db("Help!Db").collection(type).updateOne(
            { _id: facilityId },
            {
                $pull: {
                    "reviews": {
                        replierID: userId,
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
app.put('/Votes', async (req, res) => {
    const type = req.body.facilityType; //string
    const facilityId = req.body.facility_id //string
    const userId = req.body.user_id //string
    const vote = req.body.vote; // string    
    try {
        if (vote === "up") {
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
        } else if (vote === "down") {
            const result = await client.db("Help!Db").collection(type).updateOne(
                { _id: facilityId },
                {

                    $dec: {
                        "reviews.$[elem].downVotes": 1
                    }
                },
                { arrayFilters: [{ "elem.replierID": { $eq: userId } }] }
            );
            res.send(result);
        }

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

    const result = await client.db("Help!Db").collection("users").findOne({ _id: goodUserId });
    console.log(result);
    var currentadderCredits = result.number_of_credit;
    const result2 = await client.db("Help!Db").collection("users").findOne({ _id: badUserId });
    var currentSubtractorCredits = result2.number_of_credit;

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
app.put('/creditHandling/normal', async (req, res) => {
    const additionCredit_addFacility = 5;
    const additionCredit_comment = 1;
    let AdditionType = req.body.AdditionType;
    let goodUserId = req.body.upUserId;

    const result = await client.db("Help!Db").collection("users").findOne({ _id: goodUserId });
    console.log(result);
    var currentadderCredits = result.number_of_credit;
    // const result2 = await client.db("user").collection("users").findOne({_id : badUserId});
    // var currentSubtractorCredits = result2.number_of_credit;

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
    res.send({"status" : "success"});

});

/**
 * Purpose:  Let user sign up tp the app with google authentication
 * Pre: User has never signed up with a particualr email
 * Post:  User sucessfully sign up and they get a specific profile dedicated to them
 */
app.post('/google_sign_up', async(req, res) => { 
    //var user_id = generate_new_user_id(); // write a method to generate a user id for new user
     const user_gmail = req.body._id;
     const userName= req.body.username;
     const logo = req.body.user_logo;
     console.log(req.body);
     const result= await client.db("Help!Db").collection("users").find({_id : user_gmail}).toArray();
     console.log(result);
     if(result.length === 0){
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
            res.send("Added to the db");
        }catch(err){
            console.log(err);
            res.send(err);
        }
    }else{
         console.log("already exists");
         res.send(result);
    }
 }); 


//running the server and connecting to database
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
