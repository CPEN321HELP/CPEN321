async function displayReports(client){
    try{
        const result = await client.db("Help!Db").collection("reportedComment").find({}).sort({}).limit(2).toArray()
        var resultArray = [];
        resultArray.length = 0;
        if(result.length === 0){resultArray.length = 0;}else{
            for(var i=0; i<2; i++){
                resultArray[i] = result[i];
                if( resultArray[i] == result[i]){resultArray.length += 1;}
                
            }
        }
        var displayResult = JSON.stringify({report_content:resultArray, length:resultArray.length, status:200})
    }catch(err){return err}
    return displayResult;
}

module.exports = displayReports;
