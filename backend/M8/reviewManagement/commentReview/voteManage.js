const findTheUser = require("/home/azureuser/Test 1/user/userAccount/findTheUser.js")
const Find = require("/home/azureuser/Test 1/facility/FacilityDisplay/findAfacility.js")
const Type = require("/home/azureuser/Test 1/facility/FacilityDisplay/typeSelection.js")

async function voteManage(client, vote, type, facilityId, isCancelled, userId){
    const a = new Find()
    const b = new Type()
    if(!client || !vote || !type || !facilityId || !userId){
        return 0;
    }
    if(typeof type != "number" || typeof facilityId !="number"){
        return -1;
    }
    if(await findTheUser(client, userId) == null){
        return 10;
    }
    if(await a.findAfacility(client, type, facilityId , "") == null){
        return 11;
    }
    const type2 = b.typeSelection(type)
    if( await client.db("Help!Db").collection(type2).findOne({_id: facilityId, "reviews.replierID" : userId}) == null ){
        return 20;
    }
     
    if (vote === "up") {
        if(isCancelled === "pend"){
            const result = await client.db("Help!Db").collection(type2).updateOne(
                { _id: facilityId },
                {

                    $inc: {
                        "reviews.$[elem].upVotes": 1
                    }
                },
                { arrayFilters: [{ "elem.replierID": { $eq: userId } }] }
            );
            console.log(result)
            return 1;
        }
        else{
            const result = await client.db("Help!Db").collection(type2).updateOne(
                { _id: facilityId },
                {

                    $inc: {
                        "reviews.$[elem].upVotes": -1
                    }
                },
                { arrayFilters: [{ "elem.replierID": { $eq: userId } }] }
            );
            console.log(result)
            return 2;
        }
    } 
    else if (vote === "down") {
        if(isCancelled === "pend"){
            const result = await client.db("Help!Db").collection(type2).updateOne(
                { _id: facilityId },
                {
                    $inc: {
                        "reviews.$[elem].downVotes": 1
                    }
                },
                { arrayFilters: [{ "elem.replierID": { $eq: userId } }] }
            );
            console.log(result)
            return 3;
        }
        else{
            const result = await client.db("Help!Db").collection(type2).updateOne(
                { _id: facilityId },
                {
                    $inc: {
                        "reviews.$[elem].downVotes": -1
                    }
                },
                { arrayFilters: [{ "elem.replierID": { $eq: userId } }] }
            );
            console.log(result)
            return 4;
        }   
    }
}

module.exports = voteManage;
