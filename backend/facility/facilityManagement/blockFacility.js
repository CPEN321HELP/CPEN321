async function blockFacility(client, myDb, facility_type, reportedFacilityid){
    
    if(facility_type!="entertainments"&& facility_type!="studys" && facility_type!="restaurants" && facility_type!="posts" &&  facility_type!="" ){
        return {"result":"unsuccesful block with invalid type"}
    }
    if(!facility_type || !reportedFacilityid){
        return {"result":"unsuccesful block with missing field"}
    }else{
        try{
            const reportredResultQuery = await client.db(myDb).collection(facility_type).findOne({ _id: reportedFacilityid});
            return {report_content: reportredResultQuery};
        }catch(err){}

    }
    // try{
    // const reportredResultQuery = await client.db(myDb).collection(facility_type).findOne({ _id: reportedFacilityid});
    //         var finalReportDecisionJSON = {
    //             "facility_status": "blocked",
    //             "facilityType": reportredResultQuery.facility.facilityType,
    //             "facilityTitle": reportredResultQuery.facility.facilityTitle,
    //             "facilityDescription": reportredResultQuery.facility.facilityDescription,
    //             "timeAdded": reportredResultQuery.facility.timeAdded,
    //             "facilityImageLink": reportredResultQuery.facility.facilityImageLink,
    //             "facilityOverallRate": reportredResultQuery.facility.facilityOverallRate,
    //             "numberOfRates": reportredResultQuery.facility.numberOfRates,
    //             "longitude": reportredResultQuery.facility.longitude,
    //             "latitude": reportredResultQuery.facility.latitude
    //         }
    //         // var myquery = { _id: reportedFacilityid};
    //         // var newvalues = { $set: { "facility": finalReportDecisionJSON } };
    //     }catch(err){}

            // client.db(myDb).collection(facility_type).updateOne(myquery, newvalues, function (err, res) {
            //     if (err) throw err;
            // });
}

module.exports = blockFacility;