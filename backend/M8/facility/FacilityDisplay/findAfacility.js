const SomeClass = require("./typeSelection");

module.exports = class FindFacility{
    async findAfacility(client, type, id , data){
        const some = new SomeClass()
        if(!client && !type && !id){
            return {"result":"unsuccesful find with missing field"}
        }
        if(typeof id != "number"){
            return {"result":"in valid id"}
        }
        if(data ==="array"){
            return await client.db("Help!Db").collection(type2).find({}).sort({ _id: -1 }).limit(1).toArray();  
        }
        const type2 = await some.typeSelection(type);
        try{
            const result = await client.db("Help!Db").collection(type2).findOne({ _id: id});
            return result;
        }catch(err){
            // console.log(err);
            // return JSON.stringify({"h":err})
        }
    }
}

//module.exports = findAfacility;