async function deleteComment(client, facility_type, reportedFacilityid, reportedID){
    client.db("Help!Db").collection(facility_type).updateOne(  
        { _id: reportedFacilityid },    
        {
            $set: {
                "reviews.$[elem]": {}
            }
        },
        { arrayFilters: [{ "elem.replierID": { $eq: reportedID } }] }
    );
}
module.exports = deleteComment;