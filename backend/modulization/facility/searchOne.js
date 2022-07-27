const returnLogic = require("./returnLogic");

async function searchOne(client, type, keyWordSearched){
    var final = {};
    await client.db("Help!Db").collection(type).find({ "facility.facilityTitle": { $regex: new RegExp(keyWordSearched, "i") } }).forEach((result) => {
        final = await returnLogic(result);
    });
    return final;
}

module.exports = searchOne;