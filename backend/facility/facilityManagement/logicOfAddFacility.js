function logicOfAddFacility(lastOne){
    var newId = 1;
    console.log("lastOne")
    console.log(lastOne)
    // if (JSON.stringify(lastOne)  == JSON.stringify({"result":"Invalid id"})) {
    // // implying that the collection is empty
    //     console.log("empty db")
    //     return newId;
    // }
    if (lastOne == null) {
        // implying that the collection is empty
            console.log("empty db")
            return newId;
        }
    else {
        
        var theidOfTheLastOne = lastOne[lastOne.length - 1]._id;
        var theidOfTheLastOneInt = parseInt(theidOfTheLastOne,10);
        theidOfTheLastOneInt += 1;
        console.log("dd now is : " + theidOfTheLastOneInt);
        newId = theidOfTheLastOneInt;
        console.log("new id  now is : " + newId);
        return newId;
    }
    
}
module.exports = logicOfAddFacility;
