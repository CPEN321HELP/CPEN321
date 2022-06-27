var express = require('express'); 
var app = express(); 
  
const bodyParser = require('body-parser'); 
const { application } = require('express');
var server = app.listen(3000); 

const {MongoClient} = require("mongodb");
const { response } = require("express");
const uri ="mongodb://localhost:27017"
const client = new MongoClient(uri)

  
app.use(bodyParser.json()); 
app.use(bodyParser.urlencoded({ extended: true })); 
  
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

//create account with email and possward
app.post('/sign_up', (req, res) => { 
   var user_id = generate_new_user_id(); // write a method to generate a user id for new user
   var user_email = req.body.email;
   var user_password = req.body.password;
}); 

//login to account with email and possward
app.get('/sign_in', (req, res) => { 
   var user_email = req.body.email;
   var user_password = req.body.password;
}); 

//create account with google
app.post('/google_sign_up', (req, res) => { 
   var user_id = generate_new_user_id(); // write a method to generate a user id for new user
   var user_gmail = req.body.email;
}); 

//sign in account with google
app.get('/google_sign_in', (req, res) => { 
   var user_gmail = req.body.email;
}); 


//-------------------------------------------------------------------------------------
/*
   type could be post, entertainment, study, or resturant
 */

app.get('/facilityType', (req,res)=>{
   var facilityType = req.body.facilityType;
   console.log(facilityType);
   res.status(200).send(facilityType);
});

// get a detailed post -->
app.post('/post/id', (req, res)=>{
    res.send(req.body.text);
});


// app.post('/jj', async(req,res)=>{
//     try{
//         await client.db("facility").collection("posts").insertOne(req.body);
//         res.status(200).send("TODO item added successfully");
//     }
//     catch(err){
//         console.log(err);
       
//     }
// });

// get a list of general posts --> 
app.get('/post/newest', async(req, res) => { 
    
   // use date to find 5 newest
    try{
        // const greatestNumber = await client.db("facility").collection("posts").find({_id : '1'}) //sort({"_id": -1}).limit(1);
        // console.log(greatestNumber);
        // res.send(greatestNumber);
     
        const result = await client.db("facility").collection("posts").find({}).sort({_id: -1}).limit(1).toArray(function(err, result) {
            if (err) throw err;
            console.log(result);
            res.send(result);
          });// result is the current number --- an int
        //console.log(result);
        
       // console.log("cc is"+ cc);
// "greatestNumber"       :   greatestNumber in int
// greatestNumber in int  :   _id


        //await result.forEach(console.dir);
        // const arr = []; // is the array
        // for(let i=0; i<5; i++){
        //   var ww = await client.db("facility").collection("posts").find(greatestNumber - i);
        //   arr.push(ww);
        // }
        // res.status(200).send({

        // });
    }
    catch(err){
        console.log(err);
        res.status(400).send(err);
    }    
//    var newestPostId = req.body.id;

//    var newestPostTitle = req.body.title;
//    var newestfacilityType = req.body.facilityType;
//    var newestPostDescription = req.body.description;
//    var newestPostRating = req.body.rating;

//    var review = req.body.review;
 
//    try{
//       res.status(200).send({
//          "facility_id": newestPostId,
//          "facility_type": newestfacilityType,
//          "facility_title": newestPostTitle,
//          "facility_content": newestPostDescription,
//          "facility_overall_rate": newestPostRating,
//          //parts below need to be refined later, this is just an exaple
//          "review": review,
        
//       });
//    }catch(err){
//       console.log(err);
//    }
   
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
   
}); 


// return max 5 posts which title matched
app.get('/post/newest/search', (req, res) => { 
    var keyWordSearched = req.body.input;
    // query data base
}); 

app.get('/entertainment/newest', (req, res) => { 
   var newestEntertainmentId = req.body.id;
   var newewstEntertainmentTitle = req.body.title;
   var newewstEntertainmentDescription = req.body.description;
   var newewstEntertainmentPics = req.body.images;
   var newewstEntertainmentRating = req.body.rates;
   var review = req.body.review;
   var newestfacilityType = req.body.facilityType;
  
   try{
      res.status(200).send({
         "facility_id": newestEntertainmentId,
         "facility_type": newestfacilityType,
         "facility_title": newewstEntertainmentTitle,
         "facility_content": newewstEntertainmentDescription,
         "facility_image_link": newewstEntertainmentPics,
         "facility_overall_rate": newewstEntertainmentRating,
         //parts below need to be refined later, this is just an exaple
         "review": review
      });
   }catch(err){
      console.log(err);
   }
}); 

app.get('/entertainment/newest/search', (req, res) => { 
    var keyWordSearched = req.body.input;
    // query data base

}); 

app.get('/restaurant/newest', (req, res) => { 
   var newestRestaurantId = req.body.id;
   var newestRestaurantTitle = req.body.title;
   var newestRestaurantDescription = req.body.description;
   var newestRestaurantPics = req.body.images;
   var newestRestaurantRating = req.body.ratings;
   var review = req.body.review;
   var newestfacilityType = req.body.facilityType;
   try{
      res.status(200).send({
         "facility_id": newestRestaurantId,
         "facility_type": newestfacilityType,
         "facility_title": newestRestaurantTitle,
         "facility_content": newestRestaurantDescription,
         "facility_image_link": newestRestaurantPics,
         "facility_overall_rate": newestRestaurantRating,
         //parts below need to be refined later, this is just an exaple
         "review": review
      });
   }catch(err){
      console.log(err);
   }
}); 

app.get('/restaurant/newest/search', (req, res) => { 
    var keyWordSearched = req.body.input;
    // query data base
}); 

app.get('/study/newest', (req, res) => { 
   var newestStudyId = req.body.id;
   var newewstStudyTitle = req.body.title;
   var newewstStudyDescription = req.body.description;
   var newewstStudyPics = req.body.images;
   var newewstStudyRating = req.body.ratings;
   var review = req.body.review;
   var newestfacilityType = req.body.facilityType;
   try{
      res.status(200).send({
         "facility_id": newestStudyId,
         "facility_type": newestfacilityType,
         "facility_title": newewstStudyTitle,
         "facility_content": newewstStudyDescription,
         "facility_image_link": newewstStudyPics,
         "facility_overall_rate": newewstStudyRating,
         //parts below need to be refined later, this is just an exaple
         "review": review
      });
   }catch(err){
      console.log(err);
   }
}); 

app.get('/study/newest/search', (req, res) => { 
    var keyWordSearched = req.body.input;
    // query data base
}); 





// -------------------------------------------------------------
app.post('/add_facility', async (req, res) => { 
    // var newFacilityName = req.body.newFacilityName;
    // var newFacilityType = req.body.newFacilityType;
    // var newFacilityTitle = req.body.newFacilityTitle;
    // var newFacilityConetnt = req.body.newFacilityConetnt;
    // var newFacilityImage = req.body.newFacilityImage;
    // var newFacilityOverallRate = req.body.newFacilityOverallRate;

    var approveByAdmin = req.body.approveByAdmin; // how should approve by admin be achieved exactly
    //this process needs to be modified later after front end sets how we should approve

    if(approveByAdmin == true && db("mydb").collection("facilities").find(facility_title: addedFacilityTitle{$exists: true})) // this condtion check needs to be modified later
    {
        // res.send(
        //   {
        //   "facility_id": newFacilityName,
        //   "facility_type": newFacilityType,
        //   "facility_title": newFacilityTitle,
        //   "facility_content": newFacilityConetnt,
        //   "facility_image_link": newFacilityImage,
        //   "facility_overall_rate": newFacilityOverallRate,
          //parts below need to be refined later, this is just an exaple
        //   "rates": {
        //     "user_id1": 0,
        //     "user_id2": 0,
        //     "etc": 0
        //   },
        //   "reviews": {
        //     "user_id1": {
        //       "replier_id": "content",
        //       "number_of_upvote": "content",
        //       "number_of_downvote": "content",
        //       "reply_content": "content"
        //     },
        //     "user_id2": {
        //       "replier_id": "content",
        //       "number_of_upvote": "content",
        //       "number_of_downvote": "content",
        //       "reply_content": "content"
        //     }
        //   },

        // }
        // )
        try{
            const result = await client.db("facility").collection("facilities").insert
            (
                {facility_id: 1234, 
                facility_type: 00, 
                facility_title:"The title" ,
                facility_content: "The content",
                facility_image_link: "http:// the link",
                }).toArray(function(err, result) {
            if (err) throw err;
            console.log(result);
            res.send(result);
              });
        }
        catch(err){
            console.log(err);
            res.status(400).send(err);
        }    
    } else{
        res.send("Add of facility is unsuccessful, please make sure the place actual exists and is new to our system.");
    }
}); 



/**
 * Purpose:  API used for repoting facility by user
 * Pre:  Place must have a reason to be reported
 * Post: Place will get removed if report is true or else prints "Not valid report, please provide concrete reasons for report."
 */
 app.post('/report/facilty', (req, res) => { 
    var reportFacilityType = req.body.reportFacilityType;
    var reportFacilityTitle = req.body.reportFacilityTitle;
    // var reportFacilityConetnt = req.body.reportFacilityConetnt;
    // var reportFacilityImage = req.body.reportFacilityImage;
    // var reportFacilityOverallRate = req.body.reportFacilityOverallRate;

    // var reportApproveByAdmin = req.body.reportApproveByAdmin; // how should approve by admin be achieved exactly

    if(reportApproveByAdmin == true){//conditon check needs to be modified later
        try{
            const result = await client.db("facility").collection("posts").remove(
                {facility_title: reportedFacility
                }
             ).toArray(function(err, result) {
                if (err) throw err;
                console.log(result);
                res.send("Place reported: " + reportedFacility +" is removed");
              });
        }
        catch(err){
            console.log(err);
            res.status(400).send(err);
        }    
              
    }else{
        res.send("Not valid report, please provide concrete reasons for report.");
    }
}); 

//following two report method are similar to this one
app.post('/remove/comment', (req, res) => { 
    var repotedCommentContent = req.body.reportFacilityType;
  
    // var reportApproveByAdmin = req.body.reportApproveByAdmin; // how should approve by admin be achieved exactly

    if(reportApproveByAdmin == true){//conditon check needs to be modified later
        try{
            const result = await client.db("facility").collection("comment").remove(
                {comment_content: repotedCommentContent
                }
             ).toArray(function(err, result) {
                if (err) throw err;
                console.log(result);
                res.send("Comment: " + repotedCommentContent +" is removed");
              });
        }
        catch(err){
            console.log(err);
            res.status(400).send(err);
        }    
              
    }else{
        res.send("Not valid report, please provide concrete reasons for report.");
    }

}); 

app.post('/report/user', (req, res) => {
    var repotedUserID = req.body.reportedUserID;
  
    // var reportApproveByAdmin = req.body.reportApproveByAdmin; // how should approve by admin be achieved exactly

    if(reportApproveByAdmin == true){//conditon check needs to be modified later
        try{
            const result = await client.db("facility").collection("users").remove(
                {user_id: repotedUserID
                }
             ).toArray(function(err, result) {
                if (err) throw err;
                console.log(result);
                res.send("Reported: " + repotedUserID +" is removed");
              });
        }
        catch(err){
            console.log(err);
            res.status(400).send(err);
        }    
              
    }else{
        res.send("Not valid report, please provide concrete reasons for report.");
    }
}); 

//do the follwing locally? or do it with one more filed in DB
/**
 * Purpose: This is the credit calculator API which grants credit when condions are met
 * Pre:  User must either receive upvote, make a sucessful report or add a new place
 * Post: User credit increase by xx amount if add place successfully; if report successful add by xx amount; if receive upvote add by xx amount 
 */
app.post('/credit/add', (req, res) => {
    let additionCredit_addFacility = 5;
    let additionCredit_getUpvote = 1;
    let additionCredit_makeReport = 3;

    var AdditionType = req.body.AdditionType;
    let userOriginalCredit = req.body.userOriginalCredit;

    if(AdditionType == "addFacility"){
        userOriginalCredit += additionCredit_addFacility;
    }else if (AdditionType == "report"){
        userOriginalCredit += additionCredit_makeReport;
    }else if (AdditionType == "getUpvote"){
        userOriginalCredit += additionCredit_getUpvote;
    }else{
        userOriginalCredit += 0;
        res.send("No credits granted since no contributions made, please make contribution before any credit is granted");
    }


}); 

/**
 * Purpose: Credit Calculator that deducts crddit from users
 * Pre:  User gets downvote; 
 * Post: Original credit dedcuted by xx amount if user gets downvote;.....
 */
app.post('/credit/remove', (req, res) => { 
    let deduction_getDownvote = 1;

    var deductionType = req.body.deductionType;
    let userOriginalCredit = req.body.userOriginalCredit;
    if (deductionType == "downVote"){
        userOriginalCredit -= deduction_getDownvote;
    }else{
        userOriginalCredit -= 0;
        res.send("should not have any points deduction being made");
    }
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

// {
//     // (yyyymmddhhmm + type_of_facilicity+number(第几个帖子) )
//     [
//         "Facility_id" : 
//         {
//             "facility_type": 0,
//             "facility_title": "This is a title",
//             "facility_content": "This is the content of this facility",
//             "facility_image_link": "https://xxx.xxx.xxx",
//             "facility_overall_rate": 0,
//             "numberOfRates" : 1,
//             "rated_user": {
//                 "user_id1": 0,
//                 "user_id2": 0,
//                 "etc": 0
//             },
//             "longtidue" : 0.01,
//             "latitude" : 0.01,
//             "reviews": 
//             [
//                     {
//                         "replier_id": "content",
//                         "number_of_upvote": 0,
//                         "number_of_downvote": 00,
//                         "reply_content": "content",
//                         "timeOfReply" : "yyyymmddhhmmss"
//                     },
//                     {
//                         "replier_id": "content",
//                         "number_of_upvote": 00,
//                         "number_of_downvote": 01,
//                         "reply_content": "content",
//                         "timeOfReply" : "yyyymmddhhmmss"
//                     }
//             ]
//         }
//     ]
// }
    
