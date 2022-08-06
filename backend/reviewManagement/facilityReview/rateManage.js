
const FindFacility = require("/home/azureuser/Test 1/facility/FacilityDisplay/findAfacility.js")
const UserRate = require("/home/azureuser/Test 1/user/userAccount/numberOfRate.js")


async function rateManage(client, type, facilityId, userId, numberOfType, rateScore){

    const a = new FindFacility();
    const b = new UserRate();
   

    if( !client || !type || !facilityId || !userId ){
        return -1; // null input
    }
    if( type == "posts"){
        return null;
    }
    if(rateScore>5 || rateScore<0 || !Number.isInteger(rateScore)  ){
        return 5; 
    }
    const finding2 = await client.db("Help!Db").collection(type).findOne({_id: facilityId,"ratedUser.replierID" : userId});
    const theFacility = await a.findAfacility(client, numberOfType, facilityId , "") // look for a facility
    // if(theFacility == null){
    //     return -2;
    // }
    if(await client.db("Help!Db").collection(type).findOne({_id: facilityId})==null){
        return -2;
    }
    console.log("the facility is")
    console.log(theFacility)
    var NumberOfRates = theFacility.facility.numberOfRates
    var total=0;
    for(var i = 0 ; i < theFacility.reviews.length; i++){
        if( theFacility.reviews[i].rateScore == null){
            //console.log("skip")
        }
        else{
            total += theFacility.reviews[i].rateScore;  //total is the total rates in a facility
        }
        
    }
    total+=rateScore;    
    const newScore = total / (NumberOfRates + 1);

    if(finding2 == null){ // meaning the user hasn't rated this facility yet
        var PushForRate =  {$push: {"ratedUser": {replierID: userId,}}}
        await client.db("Help!Db").collection(type).updateOne(
            { _id: facilityId },
             PushForRate
        );
        var UpdateRates =  {$inc: {"facility.numberOfRates":1}}
        await client.db("Help!Db").collection(type).updateOne(
            { _id: facilityId },
             UpdateRates
        );
        var setScore =   {$set: {"facility.facilityOverallRate" : newScore}}
        await client.db("Help!Db").collection(type).updateOne(
            { _id: facilityId},
            setScore
        );
         
        await b.numberOfRate(client, userId); //belongs to user
        return 1;
    }
    else{return "repeatedly rating"}

     
    // await client.db("Help!Db").collection("users").updateOne(
    //     { _id: userId },
    //     {
    //         $inc: {
    //             "number_of_rate" : 1
    //         }
    //     }
    // );
}

module.exports = rateManage;
