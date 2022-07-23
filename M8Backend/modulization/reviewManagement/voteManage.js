async function voteManage(client, vote, type, facilityId, isCancelled, userId){
    if (vote === "up") {
        if(isCancelled === "pend"){
            const result = await client.db("Help!Db").collection(type).updateOne(
                { _id: facilityId },
                {

                    $inc: {
                        "reviews.$[elem].upVotes": 1
                    }
                },
                { arrayFilters: [{ "elem.replierID": { $eq: userId } }] }
            );
            
        }
        else{
            const result = await client.db("Help!Db").collection(type).updateOne(
                { _id: facilityId },
                {

                    $inc: {
                        "reviews.$[elem].upVotes": -1
                    }
                },
                { arrayFilters: [{ "elem.replierID": { $eq: userId } }] }
            );
             
        }
        
    } else if (vote === "down") {
        if(isCancelled === "pend"){
            const result = await client.db("Help!Db").collection(type).updateOne(
                { _id: facilityId },
                {
                    $inc: {
                        "reviews.$[elem].downVotes": -1
                    }
                },
                { arrayFilters: [{ "elem.replierID": { $eq: userId } }] }
            );
             
        }
        else{
            const result = await client.db("Help!Db").collection(type).updateOne(
                { _id: facilityId },
                {
                    $inc: {
                        "reviews.$[elem].downVotes": 1
                    }
                },
                { arrayFilters: [{ "elem.replierID": { $eq: userId } }] }
            );
             
        }   
    }
}

module.exports = voteManage;