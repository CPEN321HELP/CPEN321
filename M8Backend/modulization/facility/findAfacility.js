async function findAfacility(client, type, id){
    const result = await client.db("Help!Db").collection(type).findOne({ _id: id});
    return result;
}
module.exports = findAfacility;