function returnLogic(result) {
    // console.log("result is" + JSON.stringify(result))
    var bigArr = [];
    bigArr.push(result);
  
    var arr = [];
    for (var i = 0; i < bigArr.length; i++) {
        arr.push(bigArr[i])
    }
    // console.log("arr id is" + JSON.stringify(arr[0]))
    // console.log("/n")
    var theOne = [];
    for (var j = 0; j < arr.length; j++) {
        var zz = []
        zz.push(arr[j]._id)
        zz.push(arr[j].facility.facilityOverallRate)
        zz.push(arr[j].facility.facilityTitle)
        zz.push(arr[j].facility.facilityDescription)
        zz.push(arr[j].facility.timeAdded)
        // zz[ x, y, z]
        theOne.push(zz)
        // console.log("zz is" + JSON.stringify(zz))
        // console.log("/n")
    }
    var length2 = theOne.length;
    final["result"] = theOne;
    final["length"] = length2;
    // console.log("return is ")
    // console.log(final)
    return final;
}

module.exports = returnLogic;
