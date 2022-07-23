const facilityJson = {
    "_id" : 1,
    "facility" : {
            "facility_status" : "normal",
            "facilityType" : "entertainments",
            "facilityTitle" : "AMS Student Nest",
            "facilityDescription" : "The AMS Student Nest is a campus hub for students to eat, shop, socialize and study.",
            "timeAdded" : "2022/07/06",
            "facilityImageLink" : "https://cdn.discordapp.com/attachments/984213736652935230/994306235526557746/unknown.png",
            "facilityOverallRate" : 4,
            "numberOfRates" : 2,
            "longitude" : -123.25,
            "latitude" : 49.2664
    },
    "ratedUser" : [
            {
                    "replierID" : "l2542293790@gmail.com"
            },
            {
                    "replierID" : "xyjyeducation@gmail.com"
            }
    ],
    "reviews" : [
            {
                    "replierID" : "l2542293790@gmail.com",
                    "userName" : "Linxin Li",
                    "rateScore" : 3,
                    "upVotes" : 0,
                    "downVotes" : 0,
                    "replyContent" : "a",
                    "timeOfReply" : "2022/6/18/23/56/38"
            },
            {
                    "replierID" : "xyjyeducation@gmail.com",
                    "userName" : "Wilson Wang",
                    "rateScore" : 5,
                    "upVotes" : 0,
                    "downVotes" : 0,
                    "replyContent" : "Best!",
                    "timeOfReply" : "2022/6/19/6/52/58"
            }
    ],
    "adderID" : "wuyuheng0525@gmail.com"
};
  
  module.exports = {
    addFacility: jest.fn(async (fields,callback) => {
        const {_id,adderID,facility,ratedUser,reviews} =fields;
        if(!adderID && !facility && !ratedUser && !reviews && _id!="ll@@@@}}"){
            return callback(null,404,'missing field, unsucessful add');
        }
        if(_id == "ll@@@@}}"){
            return callback(null,404,'invalid input');
        }
        if(_id == 1){
            return callback(null,404,'add unsucsseful, facility already existed');
        }else{
            return callback(null,200,'add sucessful');
        }
    }),
  }
