const SomeClass = require("./typeSelection");

module.exports = class FindFacility{
    async findAfacility(client, type, id , data){
        const some = new SomeClass()
        
        if(!client || !id){
            return {"result":"unsuccesful find with missing field"}
        }
        else if(!type && type!=0){
            return {"result":"unsuccesful find with missing field"}
        }
        // else if(typeof type != "number"){
        //     return {"result" : "typewrong"}
        // }
        // else if(typeof id != "number"){
        //     return {"result":"Invalid id"}
        // }
        const type2 = await some.typeSelection(type);
        if(data ==="array"){
            return await client.db("Help!Db").collection(type2).find({}).sort({ _id: -1 }).limit(1).toArray();  
        }
        else if(data.includes("@gmail.com")){
            return await client.db("Help!Db").collection(type2).findOne({_id: id, "reviews.replierID" : data}) ;
        }
        try{
            const result = await client.db("Help!Db").collection(type2).findOne({ _id: id});
            console.log(result)
            return result;
            // return JSON.stringify(result);
        }catch(err){}
    }
}

// module.exports = findAfacility;
