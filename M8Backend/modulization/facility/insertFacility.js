async function insertFacility(client,type, newId, title, description, facilityImageLink , timeAdded , long, lat, adderId){
    await client.db("Help!Db").collection(type).insertOne({
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

    });
}

module.exports = insertFacility;