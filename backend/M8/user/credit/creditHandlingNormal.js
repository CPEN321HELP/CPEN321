const creditCalculation = require("./creditCalculation");
const findTheUser = require("../userAccount/findTheUser");

module.exports = class creditHandlingNormal{
    async creditHandlingNormal (client, AdditionType, goodUserId){
        const additionCredit_addFacility = 5;
        const additionCredit_comment = 1;
        const result = await findTheUser(client, goodUserId)
        
        console.log("the user profile(handled by credit ) is ");
        console.log(result);
        var currentadderCredits = result.number_of_credit;
    
        if (AdditionType === "addFacility") {
            currentadderCredits += additionCredit_addFacility;
            await creditCalculation(client, goodUserId, additionCredit_addFacility)  
        } else if (AdditionType === "comment") {
            currentadderCredits += additionCredit_comment;
            await creditCalculation(client, goodUserId, additionCredit_comment)  
        } else {
            res.send("No credits granted since no contributions made, please make contribution before any credit is granted; AdditionType is not matched in this case!");
        }
         
    }
}


// module.exports = creditHandlingNormal;