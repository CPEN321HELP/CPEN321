module.exports = class UserReply{
    async numberOfReply(client, userId){
        await client.db("Help!Db").collection("users").updateOne(
            { _id: userId },
            {
                $inc: {
                    "number_of_reply": 1 
                }
            }
        );
    }
}
 