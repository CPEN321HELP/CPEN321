var express = require('express'); 
var app = express(); 
  
const bodyParser = require('body-parser'); 
const { application } = require('express');
//var server = app.listen(3000); 

const {MongoClient} = require("mongodb");
const { response } = require("express");
const e = require('express');
const uri ="mongodb://localhost:27017"
const client = new MongoClient(uri)

  
app.use(bodyParser.json()); 
app.use(bodyParser.urlencoded({ extended: true })); 
  

app.get('/', function (req, res) {  
    res.send("adjioubihe");
}) 


// app.post('/postdata', (req, res) => { 
// 	console.log(req.body); 
//    var data = req.body.data; // your data 
// 	console.log(data); 

//    var user_id = req.body.user_id;
//    console.log(user_id); 

//    var password = req.body.password;
//    console.log(password); 

//    // do something with that data (write to a DB, for instance) 
// 	res.status(200).json({ 
// 		message: "JSON Data received successfully" 
// 	}); 
// }); 


//create account with google
app.post('/google_sign_up', (req, res) => { 
   //var user_id = generate_new_user_id(); // write a method to generate a user id for new user
    var user_gmail = req.body.email;
    var userName= req.body.userName;
    client.db("user").collection("users").insertOne({
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
    res.send("added");
   // store userJSON
}); 

//sign in account with google
app.get('/google_sign_in', (req, res) => { 
   var userGmail = req.body.email;
   // send back a userJson
   const result = client.db("user").collection("users").findOne({"email" : userGmail});
   res.send(result);
}); 



//-------------------------------------------------------------------------------------

   //JSON object for review 
   // {
   //    reviews: [
   //       {  "user_id": "content",
   //          "replier_id": "content",
   //          "number_of_upvote": "content",
   //          "number_of_downvote": "content",
   //          "reply_content": "content"
   //       } 
   //    ]
   // }

   //JSON object for rates 
   // {
   //    rates: [
   //       {  "user_id": "content",
   //          "facility" : "content"
   //       } 
   //    ]
   // }
/*
   type could be post, entertainment, study, or resturant
 */

// app.get('/facilityType', (req,res)=>{
//    var facilityType = req.body.facilityType;
//    console.log(facilityType);
//    res.status(200).send(facilityType);
// });

// { id : "5", facilityOverallRate: 5.0 , facilityTitle : "IKB", facilityDescription: "everyone goes there", timeAdded: "2022/06/27"}

// app.post('/jj', async(req,res)=>{
//     try{
//         await client.db("facility").collection("entertainment").insertOne(req.body);
//         res.status(200).send("TODO item added successfully");
//     }
//     catch(err){
//         console.log(err);
//     }
// });
// app.put('/kk', async(req,res)=>{
//     try{
//         await client.db("facility").collection("posts").replaceOne(req.body);
//         res.status(200).send("TODO item added successfully");
//     }
//     catch(err){
//         console.log(err);
//     }
// });


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


/**
 * purpose: get 5 most recent added posts 
 * explanation for teammate: sort(-1) means descending order and limits means the number of records
 * return: an array consists of id, date, description, title, and the length of the array
 */

app.post('/posts/newest', async(req, res) => { 
    try{
        var length2;
        const bigArr = []
        await client.db("facility").collection("posts").find({}).sort({_id: -1}).toArray(function(err, result) {
            if (err) throw err;
            try{
                console.log(result[0]._id)
                var len = result.length;
                for(var i = 0; i < len; i+=1){
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


// return all the posts which title matched
app.post('/posts/search', async(req, res) => { 
    var keyWordSearched = req.body.input;
    try{
        await client.db("facility").collection("posts").find({"facility.facilityTitle": keyWordSearched}).toArray(function(err, result) {
            if (err) throw err;
            console.log(result);
            res.send(result);
        });
    }
    catch(err){
        console.log(err);
        res.status(400).send(err);
    }  
}); 

app.post('/entertainment/newest', async(req, res) => { 
    try{
        var length2;
        const bigArr = []
        await client.db("facility").collection("entertainment").find({}).sort({_id: -1}).toArray(function(err, result) {
            if (err) throw err;

            try{
                console.log(result[0]._id)
                var len = result.length;
                for(var i = 0; i < len; i+=1){
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

app.post('/entertainment/search', async(req, res) => { 
    var keyWordSearched = req.body.input;
    try{
        await client.db("facility").collection("entertainment").find({"facility.facilityTitle": keyWordSearched}).toArray(function(err, result) {
            if (err) throw err;
            console.log(result);
            res.send(result);
        });
    }
    catch(err){
        console.log(err);
        res.status(400).send(err);
    }  

}); 

app.post('/restaurant/newest', async(req, res) => { 
    try{
        var length2;
        const bigArr = []
        await client.db("facility").collection("restaurant").find({}).sort({_id: -1}).toArray(function(err, result) {
            if (err) throw err;
            try{
                console.log(result[0]._id)
                var len = result.length;        
                for(var i = 0; i < len; i+=1){
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

app.post('/restaurant/search', async(req, res) => { 
    var keyWordSearched = req.body.input;
    try{
        await client.db("facility").collection("restaurant").find({"facility.facilityTitle": keyWordSearched}).toArray(function(err, result) {
            if (err) throw err;
            console.log(result);
            res.send(result);
        });
    }
    catch(err){
        console.log(err);
        res.status(400).send(err);
    }  
}); 



app.post('/study/newest', async(req, res) => { 
    try{
        var length2;
        const bigArr = []
        await client.db("facility").collection("study").find({}).sort({_id: -1}).toArray(function(err, result) {
            if (err) throw err;
            try{
                console.log(result[0]._id)
                var len = result.length;        
                for(var i = 0; i < len; i+=1){
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

app.post('/study/search', async(req, res) => { 
    var keyWordSearched = req.body.input;
    try{
        await client.db("facility").collection("study").find({"facility.facilityTitle": keyWordSearched}).toArray(function(err, result) {
            if (err) throw err;
            console.log(result);
            res.send(result);
        });
    }
    catch(err){
        console.log(err);
        res.status(400).send(err);
    }  
}); 


// -------------------------------------------------------------
/**
 *  can we store this in a temporary database
 */
app.post('/requestFacility/user', async (req, res) =>{
    const title = req.body.title             
    const description = req.body.description
    const location = req.body.location
    var approved = false;
    const type = req.body.facilityType; 

    var date_ob = new Date();
    var year = date_ob.getFullYear();
    var month = date_ob.getMonth();
    var date = date_ob.getDate();
    var hours = date_ob.getHours();
    var minutes = date_ob.getMinutes();
    var seconds = date_ob.getSeconds();
    const timeAdded = year + "/" + month + "/" +  date 
    //const timeAdded = year + "/" + month + "/" +  date + "/" +  hours + "/" +  minutes + "/" + seconds;
    const lastOne = await client.db("facility").collection(type).find({}).sort({_id: -1}).limit(1).toArray();
    console.log(lastOne);
    var newId = "1";
    if(lastOne.length ==0){
       
    }
    else{
        var d = lastOne[lastOne.length-1]._id;
        var dd = parseInt(d);
        dd+=1;
        newId = dd.toString();
    }
    // await client.db("facility").collection(type).insertOne(req.body); // in this case, frontend send us a JSON file with complete information
    await client.db("facility").collection(type).insertOne({
        _id: newId,  
        "facility": 
                {
                    "facilityType": type,
                    "facilityTitle":title,
                    "facilityDescription": description,
                    "facilityImageLink": "",
                    "facilityOverallRate": 0,
                    "numberOfRates" : 1,
                    
                    "location" : location,
                    
                    "timeAdded": timeAdded
                },
        "rated_user": {},
        "reviews": {}
    });
    res.send("Requested item added successfully");
    //await client.db("facility").collection("facilities").find(facility_title: addedFacilityTitle{$exists: true}))
});

// figure out what is sent from the frontend; i want a boolean and id? if id is not maybe which post u are dealing with
// display to 5 items
app.post('/RequestFacility/Admin', async (req, res) =>{
    const pageNumber = req.body.pageNumber;
    const bigArr = [];
    await client.db("facility").collection("test4").find({}).sort({_id: -1}).toArray(function(err, result) {
        if (err) throw err;
        try{
            var len = result.length;
            for(var i = (pageNumber-1)*5; i < len; i=i+pageNumber){
                var arr = [];
                arr.push(result[i]._id)
                arr.push(result[i].facility.facilityTitle)
                arr.push(result[i].facility.facilityDescription)
                arr.push(result[i].facility.timeAdded)
                //arr.push(result[i].facility.facilityOverallRate)
                bigArr.push(arr);
            }
            res.send(bigArr)
        }catch(err){
            console.log(err)
            res.status(400).send(err);
        }
    });
});


// use id to fetch a specific 
app.post('/RequestFacility/Admin/Approval', async (req, res) =>{
    const approval = req.body.approval;
    const id = req.body._id;
    const temp = await client.db("facility").collection("test4").find({_id : id});
    const type = temp.facility.facilityType;
    const name = temp.facility.facilityTitle;
    // firstly ensure if the facility is already existed in the db 
    client.db("facility").collection(type).find({"facility.facilityTitle":name}).toArray(function(err, result) {
        if(result.length !== 0){
            // oops
            res.send("This facility already exists");
        }
        else{
            if(approval == true){
                client.db("facility").collection(type).insertOne(temp);
                res.send(" Admin approved the request!");
            }
            else{
                res.send(" Admin disapproved the request");
            }
        }
    })
});



// /**
//  * Purpose:  API used for repoting facility by user
//  * Pre:  Place must have a reason to be reported
//  * Post: Place will get removed if report is true or else prints "Not valid report, please provide concrete reasons for report."
//  */
//  app.post('/report/facilty',async (req, res) => { 
//     var reportFacilityType = req.body.reportFacilityType;
//     var reportFacilityTitle = req.body.reportFacilityTitle;
//     // var reportFacilityConetnt = req.body.reportFacilityConetnt;
//     // var reportFacilityImage = req.body.reportFacilityImage;
//     // var reportFacilityOverallRate = req.body.reportFacilityOverallRate;

//     // var reportApproveByAdmin = req.body.reportApproveByAdmin; // how should approve by admin be achieved exactly

//     if(reportApproveByAdmin == true){//conditon check needs to be modified later
//         try{
//             const result = await client.db("facility").collection("posts").remove(
//                 {facility_title: reportedFacility
//                 }
//              ).toArray(function(err, result) {
//                 if (err) throw err;
//                 console.log(result);
//                 res.send("Place reported: " + reportedFacility +" is removed");
//               });
//         }
//         catch(err){
//             console.log(err);
//             res.status(400).send(err);
//         }    
              
//     }else{
//         res.send("Not valid report, please provide concrete reasons for report.");
//     }
// }); 

// //following two report method are similar to this one
// app.post('/remove/comment', async (req, res) => { 
//     var repotedCommentContent = req.body.reportFacilityType;
  
//     // var reportApproveByAdmin = req.body.reportApproveByAdmin; // how should approve by admin be achieved exactly

//     if(reportApproveByAdmin == true){//conditon check needs to be modified later
//         try{
//             const result = await client.db("facility").collection("comment").remove(
//                 {comment_content: repotedCommentContent
//                 }
//              ).toArray(function(err, result) {
//                 if (err) throw err;
//                 console.log(result);
//                 res.send("Comment: " + repotedCommentContent +" is removed");
//               });
//         }
//         catch(err){
//             console.log(err);
//             res.status(400).send(err);
//         }    
              
//     }else{
//         res.send("Not valid report, please provide concrete reasons for report.");
//     }

// }); 

// app.post('/report/user', async (req, res) => {
//     var repotedUserID = req.body.reportedUserID;
  
//     // var reportApproveByAdmin = req.body.reportApproveByAdmin; // how should approve by admin be achieved exactly

//     if(reportApproveByAdmin == true){//conditon check needs to be modified later
//         try{
//             const result = await client.db("facility").collection("users").remove(
//                 {user_id: repotedUserID
//                 }
//              ).toArray(function(err, result) {
//                 if (err) throw err;
//                 console.log(result);
//                 res.send("Reported: " + repotedUserID +" is removed");
//               });
//         }
//         catch(err){
//             console.log(err);
//             res.status(400).send(err);
//         }    
              
//     }else{
//         res.send("Not valid report, please provide concrete reasons for report.");
//     }
// }); 

//do the follwing locally? or do it with one more filed in DB
/**
 * Purpose: This is the credit calculator API which grants and removes credits when condions are met
 * Pre:  If user report others or make a review or add a facility
 * Post: User credit increase by xx amount if add place successfully; if report successful add by xx amount; if receive upvote add by xx amount 
 */
app.put('/creditHandling/report', async(req, res) => {
   
    const additionCredit_makeReport = 3;
    
    let AdditionType = req.body.AdditionType;
    let goodUserId = req.body.upUserId;
    let badUserId = req.body.downUserId;

    const result = await client.db("user").collection("users").findOne({_id : goodUserId});
    console.log(result);
    var currentadderCredits = result.number_of_credit; 
    const result2 = await client.db("user").collection("users").findOne({_id : badUserId});
    var currentSubtractorCredits = result2.number_of_credit;

    if (AdditionType == "report"){
        currentadderCredits += additionCredit_makeReport;
        currentSubtractorCredits -= additionCredit_makeReport;
    }else{
        console.log("No credits granted since no contributions made, please make contribution before any credit is granted");
        return;
    }
    await client.db("user").collection("users").updateOne( { _id: goodUserId },
        { $set:
           {
            number_of_credit: currentadderCredits,
           }
        }
    );
    await client.db("user").collection("users").updateOne( { _id: badUserId },
        { $set:
           {
            number_of_credit: currentSubtractorCredits,
           }
        }
    );
    res.send("success");
    
}); 

app.put('/creditHandling/normal', async(req, res) => {
    const additionCredit_addFacility = 5;
    const additionCredit_comment = 1;
    let AdditionType = req.body.AdditionType;
    let goodUserId = req.body.upUserId;

    const result = await client.db("user").collection("users").findOne({_id : goodUserId});
    console.log(result);
    var currentadderCredits = result.number_of_credit; 
    // const result2 = await client.db("user").collection("users").findOne({_id : badUserId});
    // var currentSubtractorCredits = result2.number_of_credit;

    if(AdditionType == "addFacility"){
        currentadderCredits += additionCredit_addFacility;
    }else if (AdditionType == "comment"){
        currentadderCredits += additionCredit_comment;
    }else{
        res.send("No credits granted since no contributions made, please make contribution before any credit is granted; AdditionType is not matched in this case!");
    }
    await client.db("user").collection("users").updateOne( { _id: goodUserId },
        { $set:
           {
            number_of_credit: currentadderCredits,
           }
        }
    );
    res.send("success");
    
}); 


//running the actual server at port xxxx
async function run(){
   try{ 
       await client.connect();
       console.log(" successfully connect to db");
       var server = app.listen(8000, (req, res) =>{
           var host = server.address().address;
           var port = server.address().port;
           console.log("Example app is running" + "with address" + host + "port: " + port);
       })
   }catch(err){
       console.log("error")
       await client.close()
   }
}
run();

    
