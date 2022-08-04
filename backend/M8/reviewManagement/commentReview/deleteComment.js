async function deleteComment(client, facility_type, reportedFacilityid, reportedID){
    var MyQuery =  {$set: {"reviews.$[elem]": {}}}
    client.db("Help!Db").collection(facility_type).updateOne(  
        { _id: reportedFacilityid },    
        MyQuery,
        { arrayFilters: [{ "elem.replierID": { $eq: reportedID } }] }
    );
}
module.exports = deleteComment;
