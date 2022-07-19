async function findTheUser(client ,id){
    const result = await client.db("Help!Db").collection("users").findOne({ _id: id });
    console.log(result);
    return result;
}

module.exports = findTheUser; 
