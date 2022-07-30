async function reportFacility(client, reportedFacilityType, reportedFacilityID, reportReason,  
    reporterID, reportType, reportedUSer, reportFacilityTitle, reportUserCond, reportedFacilityTypeString){

        var finalReportDecision = {
            facility_id: reportedFacilityID,
            facility_type: reportedFacilityType,
            reason: reportReason,
            reporter: reporterID,
            report_type: reportType,      // reportType == 5 -->comment |||  reportType == 6 -->facility 
            reported_user: reportedUSer, //  if no user ==> "none@gmail.com"
            title: reportFacilityTitle, 
            reportUserStatus: reportUserCond // "0"==> user is not reported ||| "1" ==> user is reported
        }
        
    if(!reportedFacilityType || !reportedFacilityID || !reportReason || !reporterID && 
        !reportType || !reportedUSer || !reportFacilityTitle || !reportUserCond || !reportedFacilityTypeString){
            return {"result":"unsuccesful report with missing field"}
        }
    //!reportedFacilityTypeString == "entertainments" && !reportedFacilityTypeString == "studys"
    // && !reportedFacilityTypeString == "posts" && !reportedFacilityTypeString =="restaurants" && !reportedFacilityTypeString
    // ==""
    if(reportedFacilityTypeString != "entertainments" && reportedFacilityTypeString != "" && reportedFacilityTypeString != "studys"
        && reportedFacilityTypeString != "posts" && reportedFacilityTypeString != "restaurants" && 
        reportedFacilityID != "" && Number.isInteger(reportedFacilityID) != true){
        return {"result":"unsuccesful report with invalid input"};
       }
   
    else{   // await client.db(myDb).collection(reportedFacilityTypeString).findOne({ _id: reportedFacilityID });
    return finalReportDecision;
    }
 

   
    // try{
    //     await client.db(myDb).collection(myCollection).insertOne(finalReportDecision, function (err, res) {
    //         if (err) throw err;
    //     });
    // }catch(err){}

   
}



module.exports = reportFacility;