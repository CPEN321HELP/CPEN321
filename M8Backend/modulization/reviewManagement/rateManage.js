async function rateManage(client, type, facilityId, userId){
    await client.db("Help!Db").collection(type).updateOne(
        { _id: facilityId },
        {
            $push: {
                "ratedUser": {
                    replierID: userId,
                }
            }
        }
    );
    await client.db("Help!Db").collection(type).updateOne(
        { _id: facilityId },
        {
            $inc: {
                "facility.numberOfRates":1
            }
        }
    );
    await client.db("Help!Db").collection(type).updateOne(
        { _id: facilityId},
        {$set: {
            "facility.facilityOverallRate" : newScore
            }
        }
    );
    await client.db("Help!Db").collection("users").updateOne(
        { _id: userId },
        {
            $inc: {
                "number_of_rate" : 1
            }
        }
    );
}

module.exports = rateManage;