
const numberOfReply = require("/home/azureuser/Test 1/user/userAccount/numberOfReply.js");
const Credit = require("/home/azureuser/Test 1/user/credit/creditHandlingNormal.js")
async function commentManage(client, type, facilityId, userId, userName, rateScore, replyContent, timeAdded, AdditionType, goodUserId){
    const a = new numberOfReply();
    const b = new Credit();
    if( !client || !type || !facilityId || !userId || !userName || !replyContent  || !timeAdded || !rateScore){
        return 2; // null input
    }
    var type2 = type.toString() 
    const finding = await client.db("Help!Db").collection(type2).findOne({_id: facilityId, "reviews.replierID" : userId});
    if(finding ==null){
        
        // if( client.length === 0 || type.length === 0 ||  facilityId.length === 0  || userId.length === ""  || userName.length === ""  || replyContent.length === ""  || timeAdded.length === "" ){
        //     return 0; // empty input
        // }
        
        await client.db("Help!Db").collection(type2).updateOne(
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
        // await client.db("Help!Db").collection("users").updateOne(
        //     { _id: userId },
        //     {
        //         $inc: {
        //             "number_of_reply": 1 
        //         }
        //     }
        // );
        await a.numberOfReply(client, userId); // belongs to user module
        await b.creditHandlingNormal(client, AdditionType, goodUserId); // user credit 
        return 1; // good
    }
    return -1;// existing comment 
    
}

module.exports = commentManage;