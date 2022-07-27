async function insertUser(client, user_gmail,userName,logo){
    await client.db("Help!Db").collection("users").insertOne({
        _id : user_gmail, 
        "account_type" : 1,  // 1 is normal user
        "account_status" : 0, 
        "username": userName, 
        "user_logo": logo, 
        "number_of_credit": 0, 
        "number_of_rate": 0, 
        "number_of_reply": 0, 
        "number_of_facility":0     
    })
}

module.exports = insertUser;