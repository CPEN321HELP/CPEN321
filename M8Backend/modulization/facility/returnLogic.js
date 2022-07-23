function returnLogic(result) {
    var bigArr = [];
    bigArr.push(result);
    var arr = [];
    for (var i = 0; i < bigArr.length; i++) {
        arr.push(bigArr[i])
    }
    var theOne = [];
    for (var i = 0; i < arr.length; i++) {
        var zz = []
        zz.push(arr[i]._id)
        zz.push(arr[i].facility.facilityOverallRate)
        zz.push(arr[i].facility.facilityTitle)
        zz.push(arr[i].facility.facilityDescription)
        zz.push(arr[i].facility.timeAdded)
        // zz[ x, y, z]
        theOne.push(zz)
    }
    var length2 = theOne.length;
    final["result"] = theOne;
    final["length"] = length2;
    console.log("return is ")
    console.log(final)
    return final;
}

module.exports = returnLogic;