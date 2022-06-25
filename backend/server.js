var express = require('express'); 
var app = express(); 
  
const bodyParser = require('body-parser'); 
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

app.get('/post/newest', (req, res) => { 

}); 

app.get('/post/newest/search', (req, res) => { 

}); 

app.get('/entertainment/newest', (req, res) => { 

}); 

app.get('/entertainment/newest/search', (req, res) => { 

}); 

app.get('/restaurant/newest', (req, res) => { 

}); 

app.get('/restaurant/newest/search', (req, res) => { 

}); 

app.get('/study/newest', (req, res) => { 

}); 

app.get('/study/newest/search', (req, res) => { 
    

}); 

//APIS below recieve infomation from user side and make changes accordingly

/**
 * Purpose:  API used for adding facility if approved by admin or else user will get message saying add was unsuccessful
 * Pre:  Place must exist and is new to system
 * Post: Adds place with all facility info needed as JSON; if place is inavlid prints "Add of facility is unsuccessful, please make sure the place actual exists and is new to our system."
 */
app.post('/add_facility', (req, res) => { 
    var newFacilityName = req.body.newFacilityName;
    var newFacilityType = req.body.newFacilityType;
    var newFacilityTitle = req.body.newFacilityTitle;
    var newFacilityConetnt = req.body.newFacilityConetnt;
    var newFacilityImage = req.body.newFacilityImage;
    var newFacilityOverallRate = req.body.newFacilityOverallRate;

    var approveByAdmin = req.body.approveByAdmin; // how should approve by admin be achieved exactly

    if(approveByAdmin == true)
    {
        res.send(
          {
          "facility_id": newFacilityName,
          "facility_type": newFacilityType,
          "facility_title": newFacilityTitle,
          "facility_content": newFacilityConetnt,
          "facility_image_link": newFacilityImage,
          "facility_overall_rate": newFacilityOverallRate,
          //parts below need to be refined later, this is just an exaple
          "rates": {
            "user_id1": 0,
            "user_id2": 0,
            "etc": 0
          },
          "reviews": {
            "user_id1": {
              "replier_id": "content",
              "number_of_upvote": "content",
              "number_of_downvote": "content",
              "reply_content": "content"
            },
            "user_id2": {
              "replier_id": "content",
              "number_of_upvote": "content",
              "number_of_downvote": "content",
              "reply_content": "content"
            }
          },
        }
        )
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
    var reportFacilityName = req.body.reportFacilityName;
    var reportFacilityType = req.body.reportFacilityType;
    var reportFacilityTitle = req.body.reportFacilityTitle;
    var reportFacilityConetnt = req.body.reportFacilityConetnt;
    var reportFacilityImage = req.body.reportFacilityImage;
    var reportFacilityOverallRate = req.body.reportFacilityOverallRate;

    var reportApproveByAdmin = req.body.reportApproveByAdmin; // how should approve by admin be achieved exactly

    if(reportApproveByAdmin == true){
        //remove the facility
    }else{
        res.send("Not valid report, please provide concrete reasons for report.");
    }
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


