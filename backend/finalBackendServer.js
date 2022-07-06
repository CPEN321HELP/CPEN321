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


app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

/**
 * purpose: get a specific detailed look of a post
 */


 app.post('/specific', async(req, res)=>{
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
    
    try{
        const result = await client.db("facility").collection(type).findOne({_id: req.body.facility_id});
        console.log(type);
        res.status(200).send(result);
        
    }
    catch(err){
        console.log(err);
        res.status(400).send(err);
    }  
});


//Part1: Create and Manage Account Use Case
app.post('/google_sign_up', (req, res) => { 
    //get request from frontend with these information
    var user_gmail = req.body.email;
    var userName= req.body.userName;
     client.db("Help!Db").collection("User").insertOne({
             "email" : user_gmail, 
             "account_type" : 0,  
             "account_status" : 0, 
             "username": userName, 
             "user_logo": "https://xxx.xxx.xxx", 
             "number_of_credit": 0, 
             "number_of_rate": 0, 
             "number_of_reply": 0, 
             "number_of_facility": 0 
             
     })
     res.send("added user with key " + user_gmail);
 }); 

app.post('/entertainment/newest', async(req, res) => { 
    try{
        //const pageNumber = req.body.pageNumber;
        //const type = req.body.facility_title;
        var length2;
        
        const bigArr = []
        await client.db("facility").collection("entertainments").find({}).sort({_id: -1}).toArray(function(err, result) {
            if (err) throw err;

            try{
                console.log(result[0]._id)
                var len = result.length;
                var o = {} // empty Object
              
                for(var i = 0; i < len; i+=1){
                    // finalResult = {
                    //     [
                    //     facility_id:result[i]._id,
                    //     facility:{facility_title:result[i].facility.facilityTitle,
                    //               facility_Description:result[i].facility.facilityDescription,
                    //               facility_timeAdded:result[i].facility.timeAdded}
                    //     ]
                        
                    // };
                    
                    var arr = [];
                    arr.push(result[i]._id)
                    arr.push(result[i].facility.facilityOverallRate)
                    arr.push(result[i].facility.facilityTitle)
                    arr.push(result[i].facility.facilityDescription)
                    arr.push(result[i].facility.timeAdded)
                    bigArr.push(arr);
                    //o[i].push(finalResult);
                }
                length2 = bigArr.length;
                // bigArr.push(bigArr.length);
                var final = {};
                final["result"]=bigArr;
                final["length"]=length2;
                res.send(final);
            }catch(err){
                console.log(err)
                res.status(400).send(err);
            }
          });
    }
    catch(err){
        console.log(err);
        res.status(400).send(err);
    }    
}); 

app.get('/user/RateFacility',
 async function (req, res) {
    //when connecting with frontend use below two lines
    //var facility_id = req.body.facility_id
    //
    // var rateScores = req.body.userRates;
    //userRates.push(rateScores)
    var myDb = "facility";

    //replace collection name by type when connecting to frontend
    // var type = req.body.facility_type;
    // console.log(type);
    // var numberOfType = parseInt(type);
    // console.log(numberOfType);
    // switch (numberOfType) {
    //     case 0:
    //         type = "posts";
    //         break;
    //     case 1:
    //         type = "studys";
    //         break;
    //     case 2:
    //         type = "entertainments";
    //         break;
    //     case 3:
    //         type = "restaurants";
    //         break;
    //     case 4:
    //         type = "report_user";
    //         break;
    //     case 5:
    //         type = "report_comment";
    //         break;
    //     case 6:
    //         type = "report_facility";
    // }

    
    const userRates = [1.1, 2.2, 3.3, 4.4, 5.5,6.6,7.7,9.9,98.7];//empty the array when connecting with frontend
    const overAllRateScore = userRates.reduce((a, b) => a + b, 0) / userRates.length;
    console.log(overAllRateScore);

   
  
    try{
      
        const result = await client.db(myDb).collection("entertainments").findOne({_id: "2"});
        console.log("facility type is: " + result.facility.facilityType);
      

        var myquery = {_id: "2"}; //replace 1 with facility_id from frontend
        var newvalues = { $set: {"facility" : {
            "facility_status": result.facility.facility_status,
            "facilityType" : result.facility.facilityType,
                "facilityTitle" : result.facility.facilityTitle,
                "facilityDescription" : result.facility.facilityDescription,
                "timeAdded" : result.facility.timeAdded,
                "facilityImageLink" : result.facility.facilityImageLink,
                "facilityOverallRate" : overAllRateScore,
                "numberOfRates" : userRates.length,
                "longtitude" : result.facility.longtitude,
                "latitude" : result.facility.latitude
        }} };
    
        await client.db(myDb).collection("entertainments").updateOne(myquery, newvalues, function(err, res) {
        if (err) throw err;
        console.log("The status for updating field is:" + JSON.stringify(res)); 
          
        });
        res.status(200).send(result);  
        
    }
    catch(err){
        console.log(err);
        res.status(400).send(err);
    } 
    
 }
)
