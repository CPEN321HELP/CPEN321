async function commentManage(client, type, facilityId, userId, userName, rateScore, replyContent, timeAdded){
    await client.db("Help!Db").collection(type).updateOne(
        { _id: facilityId },
        {
            $push: {
                "reviews": {
                    replierID: userId,
                    userName: userName,
                    rateScore:rateScore,
                    upVotes: 0,
                    downVotes: 0,
                    replyContent: replyContent,
                    timeOfReply: timeAdded
                }
            }
        }
    );
    await client.db("Help!Db").collection("users").updateOne(
        { _id: userId },
        {
            $inc: {
                "number_of_reply": 1 
            }
        }
    );
}

module.exports = commentManage;