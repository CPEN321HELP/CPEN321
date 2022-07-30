const returnLogic = require("./returnLogic");

async function searchOne(client, type, keyWordSearched){
    if(!type && !keyWordSearched){
        //return {"result":"unsuccesful search with missing field"}
        return;
    }
    var final = {};
    try{
    await client.db("Help!Db").collection(type).find({ "facility.facilityTitle": { $regex: new RegExp(keyWordSearched, "i") } }).forEach((result) => {
        final = returnLogic(result);
    });
    return final;
    }catch(err){}
}

module.exports = searchOne;