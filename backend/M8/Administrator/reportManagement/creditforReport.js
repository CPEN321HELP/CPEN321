async function displayReports(admindecision,usergmail, reportedGmail){
    var resultOfDecision;
    if(admindecision == 0){
        resultOfDecision = "user: " + usergmail + "has no credit adjustment" +
                          "and reported user: " + reportedGmail + "has no credit adjustment"
        return {result:resultOfDecision}
    }else{
        resultOfDecision = "user: " + usergmail + "has gained one credit by successful report"
        resultOfDecision = "and reported user: " + reportedGmail + "has lost one credit due to the report"
        return {result:resultOfDecision}
    }
}