
const { MongoClient } = require("mongodb");
const uri = "mongodb://127.0.0.1:27017"
const client = new MongoClient(uri)

const commentManage = require("/home/azureuser/Test 1/reviewManagement/facilityReview/commentManage.js");
const rateManage = require("/home/azureuser/Test 1/reviewManagement/facilityReview/rateManage.js")
const voteManage = require("/home/azureuser/Test 1/reviewManagement/commentReview/voteManage.js")
const deleteComment = require("/home/azureuser/Test 1/reviewManagement/commentReview/deleteComment.js")


const SomeClass = require("/home/azureuser/Test 1/user/userAccount/numberOfReply.js");
jest.mock("/home/azureuser/Test 1/user/userAccount/numberOfReply.js"); // this happens automatically with automocking
const mockMethod = jest.fn(()=>"ok");
SomeClass.mockImplementation(() => {
  return {
    numberOfReply: mockMethod,
  };
});
const some = new SomeClass();
some.numberOfReply();
console.log('Calls to method: ', mockMethod.mock.calls);





const SomeClass2 = require("/home/azureuser/Test 1/user/userAccount/numberOfRate.js");
jest.mock("/home/azureuser/Test 1/user/userAccount/numberOfRate.js"); // this happens automatically with automocking
const mockMethod2 = jest.fn(x=>"wqes");
SomeClass2.mockImplementation(() => {
  return {
    numberOfRate: mockMethod2,
  };
});
const some2 = new SomeClass2();
some2.numberOfRate();
console.log('Calls to method: ', mockMethod2.mock.calls);



const SomeClass3 = require("/home/azureuser/Test 1/user/credit/creditHandlingNormal.js");
jest.mock("/home/azureuser/Test 1/user/credit/creditHandlingNormal.js"); // this happens automatically with automocking
const mockMethod3 = jest.fn(x=>"");
SomeClass2.mockImplementation(() => {
  return {
    creditHandlingNormal: mockMethod3,
  };
});
const some3 = new SomeClass3();
some3.creditHandlingNormal();
console.log('Calls to method: ', mockMethod3.mock.calls);


const SomeClass4 = require("/home/azureuser/Test 1/facility/FacilityDisplay/findAfacility.js");
jest.mock("/home/azureuser/Test 1/facility/FacilityDisplay/findAfacility.js"); // this happens automatically with automocking
const sample4 ={
    "_id" : 1,
    "facility" : {
            "facility_status" : "normal",
            "facilityType" : "studys",
            "facilityTitle" : "The Irving K. Barber Learning Centre",
            "facilityDescription" : "The Irving K. Barber Learning Centre is a facility at the Vancouver campus of the University of British Columbia. The Learning Centre is built around the refurbished core of the 1925 UBC Main Library.",
            "timeAdded" : "2022/07/06",
            "facilityImageLink" : "https://cdn.discordapp.com/attachments/984213736652935230/994316357028036738/unknown.png",
            "facilityOverallRate" : 2.5,
            "numberOfRates" : 1,
            "longitude" : -123.2527,
            "latitude" : 49.2675
    },
    "ratedUser" : [
            {
              "replierID" : "l2542293790@gmail.com"
            },
            {
              "replierID" : "test1@gmail.com"
            }
    ],
    "reviews" : [
            {
                    "replierID" : "l2542293790@gmail.com",
                    "userName" : "Linxin Li",
                    "rateScore" : 2.5,
                    "upVotes" : 0,
                    "downVotes" : 0,
                    "replyContent" : "testing ",
                    "timeOfReply" : "2022/6/18/23/41/5"
            }
    ],
    "adderID" : ""
}

const mockMethod4 = jest.fn(x=>sample4);
SomeClass4.mockImplementation(() => {
  return {
    findAfacility: mockMethod4,
  };
});
const some4 = new SomeClass4();
some4.findAfacility();
console.log('Calls to method: ', mockMethod4.mock.calls);

describe('testing comment on a facility', () => {
    it('emptyt fields ', async () => {
        //client, type, facilityId, userId, userName, rateScore, replyContent, timeAdded
        expect(await commentManage( client, "", 1, "xsz", "sx", 3, "testing", "2/2/2", "comment", "x" )).toEqual(2); 
        expect(await commentManage( client, "posts", "", "xsz", "sx", 3, "testing", "2/2/2", "comment", "x" )).toEqual(2);  
        expect(await commentManage( client, "posts", 1, "", "sx", 3, "testing", "2/2/2", "comment", "x" )).toEqual(2); 
    })

    it('one or more field is null', async () => {
      //client, type, facilityId, userId, userName, rateScore, replyContent, timeAdded
      //expect(await commentManage( client, 1, "x", "x", "x", "x", "x", null)).toEqual(2);
      expect(await commentManage( client, 2, "x", "x", "x", "x", null, "x", "comment", "x")).toEqual(2);
      //expect(await commentManage( client, 3, "x", "x", "x", null, "x", "x")).toEqual(2);
      expect(await commentManage( client, 1, "x", "x", null, "x", "x", "x", "comment", "x")).toEqual(2);
      expect(await commentManage( client, 2, "x", null, "x", "x", "x", "x", "comment", "x")).toEqual(2);
      expect(await commentManage( client, 3, null, "x", "x", "x", "x", "x", "comment", "x")).toEqual(2);
      expect(await commentManage( client, null, "x", "x", "x", "x", "x", "x", "comment", "x")).toEqual(2);
      expect(await commentManage( null, 1, "x", "x", "x", "x", "x", "x", "comment", "x")).toEqual(2);
    })
  
    // it('making a comment under posts', async () => {
    //   expect(await commentManage(client, "posts", 1, "simonxia@gmail.com", "Simon Xia", 3, "testing for M8", "2022/07/28")).toEqual(1);  
    // })
    it('making a comment under studys', async () => {
        expect(await commentManage(client, "studys", 2, "simon@gmail.com", "Simon Xia", 3, "testing for M8", "2022/07/28", "comment", "simon@gmail.com")).toEqual(1);  
    })

    // it('making a comment under posts with a rate', async () => {
    //   expect(await commentManage(client, "posts", 1, "simonxia@gmail.com", "Simon Xia", 3, "testing for M8", "2022/07/28")).toEqual(1);  
    // }) 

    // it('user making an empty comment under studys', async () => {
    //   expect(await commentManage(client, "posts", 1, "simonxia@gmail.com", "Simon Xia", 3, "testing for M8", "2022/07/28")).toEqual(1);  
    // })

    it('user making a comment with rate that is not an integer', async () => {
      expect(await commentManage(client, "studys", 1, "simonxia@gmail.com", "Simon Xia", "g", "testing for M8", "2022/07/28")).toEqual(5);  
    })
    it('user making a comment with out of range rate', async () => {
      expect(await commentManage(client, "studys", 1, "simonxia@gmail.com", "Simon Xia", 7, "testing for M8", "2022/07/28")).toEqual(5);  
    })
    //l2542293790@gmail.com
    it('user try to comment but already commented once before', async () => {
      expect(await commentManage(client, "studys", 1, "l2542293790@gmail.com", "Hizan Li", 0, "testing for M8", "2022/07/28", "comment", "l2542293790@gmail.com")).toEqual(-1);  // To be finsihed 
    })
    it('user try to comment but the collection does not exist', async () => {
      expect(await commentManage(client, "www", 100, "l2542293790@gmail.com", "Hizan Li", 0, "testing for M8", "2022/07/28", "comment", "x")).toEqual(3);  // To be finsihed 
    })
    it('user try to comment but the facility does not exist', async () => {
      expect(await commentManage(client, "studys", 120, "l2542293790@gmail.com", "Hizan Li", 0, "testing for M8", "2022/07/28", "comment", "x")).toEqual(4);  // To be finsihed 
    })
    // it('user try to comment but he or she does not exist in the database', async () => {
    //     expect(await commentManage(client, "studys", 1, "0000@gmail.com", "Hizan Li", 0, "testing for M8", "2022/07/28", "comment", "x")).toEqual();  // To be finsihed 
    // })
    // it('users try to comment but already rated before', async () => {
    //   expect(await commentManage(client, "posts", 100, "test1@gmail.com", "Hizan Li", 0, "testing for M8", "2022/07/28")).toEqual();  // To be finsihed 
    // })
})


const SomeClass5 = require("/home/azureuser/Test 1/facility/FacilityDisplay/findAfacility.js");
jest.mock("/home/azureuser/Test 1/facility/FacilityDisplay/findAfacility.js"); // this happens automatically with automocking
const sample5 ={
    "_id" : 1,
    "facility" : {
            "facility_status" : "normal",
            "facilityType" : "studys",
            "facilityTitle" : "The Irving K. Barber Learning Centre",
            "facilityDescription" : "The Irving K. Barber Learning Centre is a facility at the Vancouver campus of the University of British Columbia. The Learning Centre is built around the refurbished core of the 1925 UBC Main Library.",
            "timeAdded" : "2022/07/06",
            "facilityImageLink" : "https://cdn.discordapp.com/attachments/984213736652935230/994316357028036738/unknown.png",
            "facilityOverallRate" : 2.5,
            "numberOfRates" : 1,
            "longitude" : -123.2527,
            "latitude" : 49.2675
    },
    "ratedUser" : [
            {
              "replierID" : "l2542293790@gmail.com"
            },
            {
              "replierID" : "test1@gmail.com"
            }
    ],
    "reviews" : [
            {
                    "replierID" : "l2542293790@gmail.com",
                    "userName" : "Linxin Li",
                    "rateScore" : 2.5,
                    "upVotes" : 0,
                    "downVotes" : 0,
                    "replyContent" : "testing ",
                    "timeOfReply" : "2022/6/18/23/41/5"
            }
    ],
    "adderID" : ""
}
const mockMethod5 = jest.fn(x=>sample5);
SomeClass5.mockImplementation(() => {
  return {
    findAfacility: mockMethod5,
  };
});
const some5 = new SomeClass5();
some5.findAfacility();
console.log('Calls to method: ', mockMethod5.mock.calls);


describe('testing rating', () => {
  it('one or more fields is empty string', async () => {
    //(client, type, facilityId, userId)numberOfType, rateScore
    expect(await rateManage( "",     "posts", 1,  "xx", "1", 5 )).toEqual(-1);  
    expect(await rateManage( client, "",      1,  "xx", "1", 5  )).toEqual(-1);  
    expect(await rateManage( client, "posts", "", "xx", "1", 5  )).toEqual(-1);  
    expect(await rateManage( client, "posts", 1,   "", "1", 5   )).toEqual(-1);  
  })

  it('one or more fields is null', async () => {
    expect(await rateManage( client, "s", "s", null, "1", 5  )).toEqual(-1); 
    expect(await rateManage( client, "s", null, "s", "1", 5  )).toEqual(-1);  
    expect(await rateManage( client, null, "s", "s", "1", 5  )).toEqual(-1);  
    expect(await rateManage( null, "s", "s", "s", "1", 5  )).toEqual(-1);   
  })
  
   
  it('try to rate posts', async () => {
    expect(await rateManage(client, "posts", 1, "simon@gmail.copm", "0", 5  )).toEqual(null);  
  })
  it('try to rate facility that is not posts', async () => {
    expect(await rateManage(client, "studys", 5, "simonx@gmail.copm", "1", 5  )).toEqual(1);  
  })
  it('the user rated for a score out of range', async () => {
    expect(await rateManage(client, "studys", 1, "l2542293790@gmail.com", "1", 9 )).toEqual(5);  
  })
  //l2542293790@gmail.com
  it('the user attempts to rate but already rated before', async () => {
      expect(await rateManage(client, "studys", 1, "l2542293790@gmail.com", "1", 5 )).toEqual("repeatedly rating");  
  })
  // it('user already commented but not yet rated before', async () => {
  //   expect(await rateManage(client, "posts", 1, "l2542293790@gmail.com")).toEqual("repeatedly rating");  
  // })
})


// const SomeClass6 = require("/home/azureuser/Test 1/facility/FacilityDisplay/findAfacility.js");
// jest.mock("/home/azureuser/Test 1/facility/FacilityDisplay/findAfacility.js"); // this happens automatically with automocking
 
// var mockMethod6 = jest.fn(x=>null);
// SomeClass6.mockImplementation(() => {
//   return {
//     findAfacility: mockMethod6,
//   };
// });
// var some6 = new SomeClass6();
// some6.findAfacility();
// console.log('Calls to method: ', mockMethod6.mock.calls);

describe('testing rating 2', () => {
   
  it('try to rate facility that does not exist', async () => {
    expect(await rateManage(client, "studys", 100, "simon@gmail.copm", "0", 5  )).toEqual(-2);  
  })
 
})


// describe('testing voting', () => {
//     //(client, vote, type, facilityId, isCancelled, userId)
//     it('empty string', async () => {
//       expect(await voteManage( "", "x", "x", "x", "x", "x" )).toEqual(0);  
//       expect(await voteManage( client, "", "x", "x", "x", "x" )).toEqual(0); 
//       expect(await voteManage( client, "x", "", "x", "x", "x" )).toEqual(0); 
//       expect(await voteManage( client, "x", "x", "", "x", "x" )).toEqual(0); 
//       expect(await voteManage( client, "x", "x", "x", "", "x" )).toEqual(0); 
//       expect(await voteManage( client, "x", "x", "x", "x", "" )).toEqual(0); 
//     })
  
//     it('null', async () => {
//       expect(await voteManage(client, "", "", "", "", null)).toEqual(-1);  
//       expect(await voteManage(client, "", "", "", null, "")).toEqual(-1);  
//       expect(await voteManage(client, "", "", null, "", "")).toEqual(-1);  
//       expect(await voteManage(client, "", null, "", "", "")).toEqual(-1);  
//       expect(await voteManage(client, null, "", "", "", "")).toEqual(-1);  
//       expect(await voteManage(null, "", "", "", "", "")).toEqual(-1);  
//     })

//     it('facilityID is not a integer', async () => {
//       expect(await rateManage( client, "s", "s", "s","s","s" )).toEqual(-1); 
//     })
//     it('type is not a valid string', async () => {
//       expect(await rateManage( client, "s", "@@@", 1,"s","s" )).toEqual(-1); 
//     })


//     it('upvote', async () => {
//       expect(await voteManage(client, "posts", 1, "simonxia@gmail.com", "Simon Xia", 3, "testing for M8", "2022/07/28")).toEqual(1);  
//     })
//     it('downvote', async () => {
//       expect(await voteManage(client, "posts", 1, "simonxia@gmail.com", "Simon Xia", 3, "testing for M8", "2022/07/28")).toEqual(1);  
//     })
//     it('vote for someone does not exist', async () => {
//       expect(await voteManage(client, "posts", 1, "simonxia@gmail.com", "Simon Xia", 3, "testing for M8", "2022/07/28")).toEqual(1);  
//     })
//     it('vote for someone who does not exist', async () => {
//       expect(await voteManage(client, "posts", 1, "simonxia@gmail.com", "Simon Xia", 3, "testing for M8", "2022/07/28")).toEqual(1);  
//     })
//     it('vote in facility which does not exist', async () => {
//       expect(await voteManage(client, "posts", 1, "simonxia@gmail.com", "Simon Xia", 3, "testing for M8", "2022/07/28")).toEqual(1);  
//     })
//     it('withdraw a upVote', async () => {
//       expect(await voteManage(client, "posts", 1, "simonxia@gmail.com", "Simon Xia", 3, "testing for M8", "2022/07/28")).toEqual(1);  
//     })
//     //l2542293790@gmail.com
//     it('withdraw a downVote', async () => {
//         expect(await voteManage(client, "posts", 1, "l2542293790@gmail.com", "Hizan Li", 0, "testing for M8", "2022/07/28")).toEqual(null);  
//     })
  
// })

// describe('testing deleting a comment', () => {
//     it('empty string', async () => {
//       //(client, facility_type, reportedFacilityid, reportedID)
//       expect(await deleteComment( client, "", "", "" )).toEqual(0);  
//     })
  
//     it('null', async () => {
//       expect(await deleteComment("", "", "", "")).toEqual(-1);  
//     })
//     it('facility type is not a valid string', async () => {
//       expect(await deleteComment(client, "@@@", 1, "")).toEqual(-1);  
//     })
  
//     it('delete in a non-exsitent facility', async () => {
//       expect(await deleteComment(client, "posts", 1, "simonxia@gmail.com", "Simon Xia", 3, "testing for M8", "2022/07/28")).toEqual(1);  
//     })
//     it('delete in a non-exsitent comment', async () => {
//       expect(await deleteComment(client, "posts", 1, "simonxia@gmail.com", "Simon Xia", 3, "testing for M8", "2022/07/28")).toEqual(1);  
//     })
//     //l2542293790@gmail.com
//     it('delete successfully', async () => {
//         expect(await deleteComment(client, "posts", 1, "l2542293790@gmail.com", "Hizan Li", 0, "testing for M8", "2022/07/28")).toEqual(null);  
//     })
  
// })
