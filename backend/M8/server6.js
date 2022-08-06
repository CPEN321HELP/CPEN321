var express = require('express');
var app = express();
var express = require('express');
var app = express();

const bodyParser = require('body-parser');
//const { application } = require('express');



const { MongoClient } = require("mongodb");


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





const realTimeUpdate = require("./user/notification/realTimeUpdate");
const google_sign_up = require("./user/signIn/google_sign_up");


const FindAfacility = require("./facility/FacilityDisplay/findAfacility");


const TypeSelection = require("./facility/FacilityDisplay/typeSelection")
const findMany = require("./facility/FacilityDisplay/findMany")

const searchOne = require("./facility/FacilityDisplay/searchOne")
const reportFacility = require("./facility/facilityManagement/reportFacility")

const voteManage = require("./reviewManagement/commentReview/voteManage");

const commentManage = require("./reviewManagement/facilityReview/commentManage");
const rateManage = require("./reviewManagement/facilityReview/rateManage");



const displayReports = require("./Administrator/Display/displayReports");



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

    const s1 = {"result":"unsuccesful find with missing field"}
    // const s2 = {"result" : "typewrong"}
    // const s3 = {"result":"Invalid id"}
    if(JSON.stringify(result) == JSON.stringify(s1)){
        res.status(404).send(s1);
    }
    else if (result == null || result == {}) {
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
    // if(rest == {"result":"unsuccesful find with invalid type"}){
    //     res.status(404).send({"result":"unsuccesful find with invalid type"})
    // }
    if(JSON.stringify(rest) == JSON.stringify({"result":"unsuccesful find with invalid type"})){
        res.status(400).send({"result":"unsuccesful find with invalid type"})
    }
    else if( rest == null){
        res.status(210).send({"result":"what"})
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

        if(final == null){
            res.status(404).send(final);
        }
        else if(JSON.stringify(final) == JSON.stringify({"result":"unsuccesful find with invalid type"})){
            res.status(404).send({"result":"unsuccesful find with invalid type"})
        }
        else if(JSON.stringify(final) == JSON.stringify({"result":"unsuccesful search with missing field"})){
            res.status(404).send({"result":"unsuccesful search with missing field"})
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
        res.status(200).send(final)
    })

/**
 * Purpose: user reports inapporatiate facilities and puts this request in query
 * Pre: User must make a report dedicated to a facility
 * Post: Result gets in query
 */
 app.post('/user/Report/commentAndfacility',
 async function (req, res) {
    var reportedFacilityID =  parseInt(req.body.reportedFacilityID,10);
    var reportedFacilityType = parseInt(req.body.reportedFacilityType,10);
    var reportFacilityTitle = req.body.title;
    var reportedFacilityTypeString;
    
    var reporterID = req.body.reporterID;
    var reportReason = req.body.reportReason;
    var reportedUSer = req.body.reported_id;
    var reportType = req.body.report_type;
    var reportUserCond = req.body.reportUser;
   
     
    await reportFacility(client, reportedFacilityType, reportedFacilityID, reportReason,  
        reporterID, reportType, reportedUSer, reportFacilityTitle, reportUserCond, reportedFacilityTypeString)
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
        var reportType;
        var approveDecision = req.body.approve;
        var reportedFacilityid = parseInt(req.body.facility_id,10);
        var upUser = req.body.upUserId;
        if (parseInt(req.body.report_type,10) === 5) {reportType = "comment";}
        if (parseInt(req.body.report_type,10) === 6) {reportType = "facility";}
        if(req.body.adminEmail != "lufei8351@gmail.com" && req.body.adminEmail != "l2542293790@gmail.com" &&
        req.body.adminEmail != "wuyuheng0525@gmail.com"  && req.body.adminEmail != "simonxia13.13@gmail.com" &&
        req.body.adminEmail != "cpen321ubc@gmail.com" && req.body.adminEmail!="xyjyeducation@gmail.com"){
        res.status(404).json({result: "not admin making decision"});
        }
        else{ 
            if (reportType == "facility") {
                        if (approveDecision === 0) {
                            await realTimeUpdate(req.body.upMessage, 6, [upUser], reportedFacilityid, 25, 1);
                            res.status(404).send({ "result": "report facility unsuccessful" });}
                        }else if (reportType == "comment"){
                            if(approveDecision === 1){
                                res.status(200).send({ "result": "report facility successful" });
                            }else{
                                await realTimeUpdate(req.body.upMessage, 6, [upUser], reportedFacilityid, 25, 1);
                                res.status(404).send({ "result": "report facility unsuccessful" });
                            }
                        }
            }

        })
      
      
       
      
            
        


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
    var type2 = parseInt(req.body.type,10);  // a string of name without s whattttt
  
    if(!title && !description && !adderId && !long && !lat && !type2){res.status(404).send(JSON.stringify({result:"unsucessful add"}));}
    else if(lat<0 && long>0){res.status(404).send(JSON.stringify({result:"unsucessful add"}));}
    else if(Number.isFinite(long) !== true || Number.isFinite(lat) !== true){res.status(404).send(JSON.stringify({result:"unsucessful add"}));}
    else{res.status(200).send(JSON.stringify({result:"add succeessful"}));}
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
    if(userId == "test@gmail.com"){
        res.send(JSON.stringify({"result":"testing"}))
    }
    else{ 
        await rateManage(client, type, facilityId, userId, numberOfType, rateScore) 
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
        else if(status == 5){
            res.status(404).send(JSON.stringify({ "result": "outofrange" }));
        }
        else if(status == 6){
            res.status(404).send(JSON.stringify({ "result": "nonuser" }));
        }
        else {
            console.log("already commented");
            res.status(208).send(JSON.stringify({ "result": "already" }));
        }
    }
});





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
   
    var numberOfType = parseInt(type);
    console.log(numberOfType);
   
    if(!type || !facilityId || !userId || !vote ){
        res.status(404).send({"data" : "null"})
    }
    else{
        type = await a.typeSelection(numberOfType);
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
            res.status(404).send({"data": "what"});
        }
    }    
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
        const result = await google_sign_up(client, user_gmail, userName,  logo);
        if(result === 1){
            res.status(201).send({"data":"added"})
        }
        else if(result.username=="nullnull" && result.user_logo=="nullnull"){
            res.status(200).send(result)
        }
        else{
            console.log("user info json giving to frontend is")
            console.log(result)
            res.status(200).send(JSON.stringify(result))
        }
        
    }
    
});

async function run() {
    try {
        await client.connect();
        console.log(" successfully connect to db");
        var server = app.listen(6002, (req, res) => {
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
