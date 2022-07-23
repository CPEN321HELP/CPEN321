function logicOfAddFacility(lastOne){
    var newId = "1";
    if (lastOne.length === 0) {
    // implying that the collection is empty
    }
    else {
        var theidOfTheLastOne = lastOne[lastOne.length - 1]._id;
        var theidOfTheLastOneInt = parseInt(theidOfTheLastOne);
        theidOfTheLastOneInt += 1;
        console.log("dd now is : " + theidOfTheLastOneInt);
        newId = theidOfTheLastOneInt;
        console.log("new id  now is : " + newId);
    }
    return newId;
}
module.exports = logicOfAddFacility;