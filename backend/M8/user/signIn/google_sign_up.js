const insertUser = require("./insertUser")

async function google_sign_up (client, user_gmail, userName, logo){
    const result = await findTheUser(client, user_gmail)
    if(result == null){
       try{ 
           await insertUser(client, user_gmail, userName, logo );
           console.log("Added to the db");
        //    res.send({"result":"Added to the db"});
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
module.exports = google_sign_up;