const creditHandlingNormal = require("/home/azureuser/Test 1/user/credit/creditHandlingNormal");
async function insertFacility(client,type, newId, title, description, facilityImageLink , timeAdded , long, lat, adderId){
    var insertInfo ={
        _id: newId,
        "facility":
        {
            "facilityType": type,
            "facility_status": "normal",
            "facilityTitle": title,
            "facilityDescription": description,
            "facilityImageLink": facilityImageLink,
            "facilityOverallRate": 0,
            "numberOfRates": 0,
            "timeAdded": timeAdded,
            "longitude": long,
            "latitude": lat
        },
        "rated_user": [{}],
        "reviews": [{}],
        "adderID": adderId
    }
    if(!type || !newId || !title || !description ||!facilityImageLink || !timeAdded &&!long || !lat || !adderId){
        return {"result":"unsuccesful add with missing field"}
    }
    if(type !="" && type!= "entertainments" && type!="studys" && type!="posts" &&type!="restaurants"){
        return {"result":"unsuccesful add with invalid input"}
    }else{
    
        try{
            await client.db("Help!Db").collection(type).insertOne(insertInfo);
          
        }catch(err){}
    return({insertInfo: insertInfo})
    
}
    // await creditHandlingNormal(client, "addFacility", adderId);
    // // return "done"
}

module.exports = insertFacility;