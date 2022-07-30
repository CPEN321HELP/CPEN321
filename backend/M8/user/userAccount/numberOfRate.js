module.exports = class UserRate{
    
    async numberOfRate(client, userId){
        await client.db("Help!Db").collection("users").updateOne(
            { _id: userId },
            {
                $inc: {
                    "number_of_rate" : 1
                }
            }
        );
    }

}

 