module.exports = class SomeClass{
    typeSelection(numberOfType) {
        if(!numberOfType && numberOfType!=0){
            return "invalid input type";
        }
        switch (numberOfType) {
            case 0:
                return "posts";
            case 1:
                return "studys";
    
            case 2:
                return "entertainments";
    
            case 3:
                return "restaurants";
    
            case 4:
                return "report_user";
    
            case 5:
                return "report_comment";
    
            case 6:
                return "report_facility";
        }
        return "invalid input type"
    }
};
