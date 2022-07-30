module.exports = class SomeClass{
    newestFacilityReturnLogic(result){
        var length2;
        var bigArr = []
        var len = result.length;
        //console.log(len)
        for (var i = 0; i < len; i += 1) {
            if(result[i].facility.facility_status === "normal"){
                var arr = [];
                arr.push(result[i]._id)
                arr.push(result[i].facility.facilityOverallRate)
                arr.push(result[i].facility.facilityTitle)
                arr.push(result[i].facility.facilityDescription)
                arr.push(result[i].facility.timeAdded)
                //console.log("arr is")
                //console.log(arr)
                bigArr.push(arr);
                //console.log(bigArr)
            }
        }
        length2 = bigArr.length;
        var final = {};
        final["result"] = bigArr;
        final["length"] = length2;
        return final;
    }
}

 