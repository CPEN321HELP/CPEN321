
const NumberOfReply = require("/home/azureuser/Test 1/user/userAccount/numberOfReply.js");
const Credit = require("/home/azureuser/Test 1/user/credit/creditHandlingNormal.js");
const FindFacility = require("/home/azureuser/Test 1/facility/FacilityDisplay/findAfacility.js")
async function commentManage(client, type, facilityId, userId, userName, rateScore, replyContent, timeAdded, AdditionType, goodUserId){
    const Reply = new NumberOfReply();
    const CreditInstance = new Credit();
    const FindFacilityInstance = new FindFacility();
    if( !client || !type || !facilityId || !userId || !userName || !replyContent  || !timeAdded ){
        return 2; // null input
    }
    if(type!="posts"&&type!="studys"&&type!="entertainments"&&type!="restaurants"){
        return 3;
    }
    var type2 = type.toString() 
    const xxx= await FindFacilityInstance.findAfacility(client, type2, facilityId, "")
    if(await client.db("Help!Db").collection(type2).findOne({_id: facilityId}) == null){// facility couldn't be found 
        return 4;
    }
    if(rateScore>5 || rateScore<0 || !Number.isInteger(rateScore)  ){
        return 5; 
    }
    const finding = await client.db("Help!Db").collection(type2).findOne({_id: facilityId, "reviews.replierID" : userId}) ;
    //const finding = await c.findAfacility(client, type2, facilityId, userId)
    console.log("finding")
    console.log(finding)
    if(finding == null || finding == []){ // never comment before
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
         
        await Reply.numberOfReply(client, userId); // belongs to user module
        await CreditInstance.creditHandlingNormal(client, AdditionType, goodUserId); // user credit 
        return 1; // good
    }
    return -1;// existing comment 
    
}

module.exports = commentManage;
