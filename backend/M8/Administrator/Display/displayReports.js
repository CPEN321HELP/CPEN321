async function displayReports(client){
    // var mycollection;
    // if(scenario == 0){
    //     return {report_content:[], length:0, status:200}
    // }else if(scenario == 1){
    //     return {report_content:["oldest 1"], length:1, status:200}
    // }else if(scenario == 2){
    //     return {report_content:["oldest 1", "oldest 2"], length:2, status:200}
    // }
    await client.db("Help!Db").collection(mycollection).find({}).sort({}).limit(2).toArray(function (err, result) {
        var resultArray = [];
        resultArray.length = 0;
        if(result.length == 0){resultArray.length = 0;}else{
            for(var i=0; i<2; i++){
                resultArray[i] = result[i];
                if( resultArray[i] = result[i]){resultArray.length += 1;}
                
            }
        }
        return JSON.stringify({report_content:resultArray, length:resultArray.length, status:200});
    });
}

module.exports = displayReports;