const {returnLogic} = require("./returnLofic");

async function findMany(client, type){
    const result = await client.db("Help!Db").collection(type).find({}).sort({ _id: -1 }).toArray();
    const final =  returnLogic(result);
    return final;
}

module.exports = findMany;