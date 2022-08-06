// const newestFacilityReturnLogic = require("./newestFacilityReturnLogic");
const SomeClass = require("./newestFacilityReturnLogic");
async function findMany(client, type){
    const a = new SomeClass();
    if(type!= "posts" && type!="entertainments" && type!= "restaurants" && type!="studys"){
        return {"result":"unsuccesful find with invalid type"}
    } 
    const result =  await client.db("Help!Db").collection(type).find({}).sort({ _id: -1 }).toArray();
    return await a.newestFacilityReturnLogic(result);
}

module.exports = findMany;
