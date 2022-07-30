
// const fs = jest.createMockFromModule('fs');


module.exports = {
    typeSelection : (numberOfType) =>{
        if(numberOfType===null){
            return ""
        }
        else if(numberOfType === ""){
            return ""
        }
        else if(numberOfType === 1234255342){
            return ""
        }
        else{
            return "posts";
        }
    }
};
// fs.typeSelection = typeSelection;

// module.exports = fs;

