var express = require('express'); 
var app = express(); 
  
const bodyParser = require('body-parser'); 
const { application } = require('express');
var server = app.listen(3000); 
  
app.use(bodyParser.json()); 
app.use(bodyParser.urlencoded({ extended: true })); 
  
app.post('/postdata', (req, res) => { 
	console.log(req.body); 
   var data = req.body.data; // your data 
	console.log(data); 

   var user_id = req.body.user_id;
   console.log(user_id); 

   var password = req.body.password;
   console.log(password); 

   // do something with that data (write to a DB, for instance) 
	res.status(200).json({ 
		message: "JSON Data received successfully" 
	}); 
}); 

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

app.get('/post/newest', (req, res) => { 
   var newestPostId = req.body.id;
   var newestPostTitle = req.body.title;
   var newestfacilityType = req.body.facilityType;
   var newestPostDescription = req.body.description;
   var newestPostRating = req.body.rating;

   var review = req.body.review;
 
   try{
      res.status(200).send({
         "facility_id": newestPostId,
         "facility_type": newestfacilityType,
         "facility_title": newestPostTitle,
         "facility_content": newestPostDescription,
         "facility_overall_rate": newestPostRating,
         //parts below need to be refined later, this is just an exaple
         "review": review,
        
      });
   }catch(err){
      console.log(err);
   }
   
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



//recieve infomation from user side


// -------------------------------------------------------------
app.post('/add_facility', (req, res) => { 

}); 

app.post('/report/facilty', (req, res) => { 

}); 

app.post('/remove/facility', (req, res) => { 

}); 

app.post('/report/comment', (req, res) => { 

}); 

app.post('/remove/comment', (req, res) => { 

}); 

app.post('/report/user', (req, res) => { 

}); 

app.post('/remove/user', (req, res) => { 

}); 

app.post('/credit/add', (req, res) => { 

}); 

app.post('/credit/remove', (req, res) => { 

}); 


async function run(){
   try{ 
       // await client.connect()
       // console.log(" successfully connect to db")
       
       var server = app.listen(8000, (req, res) =>{
           var host = server.address().address
           var port = server.address().port
           console.log("Example app is running");
       })

   }catch(err){
       console.log("error")
       //await client.close()
   }
}


run()