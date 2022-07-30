
// const { MongoClient } = require("mongodb");
// const uri = "mongodb://127.0.0.1:27017"
// const client = new MongoClient(uri)

// const commentManage = require("/home/azureuser/Test 1/reviewManagement/facilityReview/commentManage.js");
// const rateManage = require("/home/azureuser/Test 1/reviewManagement/facilityReview/rateManage.js")
// const voteManage = require("/home/azureuser/Test 1/reviewManagement/commentReview/voteManage.js")
// const deleteComment = require("/home/azureuser/Test 1/reviewManagement/commentReview/deleteComment.js")


// const SomeClass = require("/home/azureuser/Test 1/user/userAccount/numberOfReply.js");
// jest.mock("/home/azureuser/Test 1/user/userAccount/numberOfReply.js"); // this happens automatically with automocking
// const mockMethod = jest.fn(()=>"ok");
// SomeClass.mockImplementation(() => {
//   return {
//     numberOfReply: mockMethod,
//   };
// });
// const some = new SomeClass();
// some.numberOfReply();
// console.log('Calls to method: ', mockMethod.mock.calls);





// const SomeClass2 = require("");
// jest.mock(""); // this happens automatically with automocking
// const mockMethod2 = jest.fn(x=>"entertainments");
// SomeClass2.mockImplementation(() => {
//   return {
//     c: mockMethod,
//   };
// });
// const some2 = new SomeClass2();
// some.c(6);
// console.log('Calls to method: ', mockMethod.mock.calls);



// const SomeClass3 = require("");
// jest.mock(""); // this happens automatically with automocking
// const mockMethod3 = jest.fn(x=>"entertainments");
// SomeClass2.mockImplementation(() => {
//   return {
//     c: mockMethod,
//   };
// });
// const some3 = new SomeClass2();
// some.c(6);
// console.log('Calls to method: ', mockMethod.mock.calls);





// describe('testing comment on a facility', () => {
//     it('empty type', async () => {
//         //client, type, facilityId, userId, userName, rateScore, replyContent, timeAdded
//         expect(await commentManage( client, "", 1, "xsz", "sx", 3, "testing", "2/2/2" )).toEqual(2);  
//     })

//     it('empty facility id', async () => { 
//       expect(await commentManage( client, "posts", "", "xsz", "sx", 3, "testing", "2/2/2" )).toEqual(2); 
//     })

//     // it('facility id is not integer', async () => {
//     //     expect(await commentManage( client, "posts", "asdfsc", "xsz", "sx", 3, "testing", "2/2/2" )).toEqual(3); 
//     // })

//     it('empty user id', async () => {
//       //client, type, facilityId, userId, userName, rateScore, replyContent, timeAdded
//       expect(await commentManage( client, "posts", 1, "", "sx", 3, "testing", "2/2/2" )).toEqual(2);  
       
//     //   expect(await commentManage( client, "posts", 1, "xsz", "sx", "", "", "" )).toEqual(0);  
//     //   expect(await commentManage( client, "posts", 1, "xsz", "sx", "", "", "" )).toEqual(0);  
//     //   expect(await commentManage( client, "posts", 1, "xsz", "sx", "", "", "" )).toEqual(0);  
//     //   expect(await commentManage( client, "posts", 1, "xsz", "sx", "", "", "" )).toEqual(0);  
//     //   expect(await commentManage( client, "posts", 1, "xsz", "sx", "", "", "" )).toEqual(0);  
//     })

//     it('one or more field is null', async () => {
//       //client, type, facilityId, userId, userName, rateScore, replyContent, timeAdded
//       expect(await commentManage( client, 1, "x", "x", "x", "x", "x", null)).toEqual(2);
//       expect(await commentManage( client, 2, "x", "x", "x", "x", null, "x")).toEqual(2);
//       expect(await commentManage( client, 3, "x", "x", "x", null, "x", "x")).toEqual(2);
//       expect(await commentManage( client, 1, "x", "x", null, "x", "x", "x")).toEqual(2);
//       expect(await commentManage( client, 2, "x", null, "x", "x", "x", "x")).toEqual(2);
//       expect(await commentManage( client, 3, null, "x", "x", "x", "x", "x")).toEqual(2);
//       expect(await commentManage( client, null, "x", "x", "x", "x", "x", "x")).toEqual(2);
//       expect(await commentManage( null, 1, "x", "x", "x", "x", "x", "x")).toEqual(2);
//     })
  
//     it('making a comment under posts', async () => {
//       expect(await commentManage(client, "posts", 1, "simonxia@gmail.com", "Simon Xia", 3, "testing for M8", "2022/07/28")).toEqual(1);  
//     })

//     // it('making a comment under posts with a rate', async () => {
//     //   expect(await commentManage(client, "posts", 1, "simonxia@gmail.com", "Simon Xia", 3, "testing for M8", "2022/07/28")).toEqual(1);  
//     // }) 

//     // it('user making an empty comment under studys', async () => {
//     //   expect(await commentManage(client, "posts", 1, "simonxia@gmail.com", "Simon Xia", 3, "testing for M8", "2022/07/28")).toEqual(1);  
//     // })

//     // it('user making a comment without rating under studys', async () => {
//     //   expect(await commentManage(client, "posts", 1, "simonxia@gmail.com", "Simon Xia", 3, "testing for M8", "2022/07/28")).toEqual(1);  
//     // })
//     //l2542293790@gmail.com
//     it('user try to comment but already commented once before', async () => {
//         expect(await commentManage(client, "posts", 1, "l2542293790@gmail.com", "Hizan Li", 0, "testing for M8", "2022/07/28")).toEqual(-1);  // To be finsihed 
//     })
//     // it('users try to comment but already rated before', async () => {
//     //   expect(await commentManage(client, "posts", 1, "l2542293790@gmail.com", "Hizan Li", 0, "testing for M8", "2022/07/28")).toEqual(-1);  // To be finsihed 
//     // })
// })

// describe('testing rating', () => {
//     it('one or more fields is empty string', async () => {
//       //(client, type, facilityId, userId)
//       expect(await rateManage( "",     "posts", 1,  "xx" )).toEqual(0);  
//       expect(await rateManage( client, "",      1,  "xx" )).toEqual(0);  
//       expect(await rateManage( client, "posts", "", "xx" )).toEqual(0);  
//       expect(await rateManage( client, "posts", 1,   ""  )).toEqual(0);  
//     })
  
//     it('one or more fields is null', async () => {
//       expect(await rateManage( client, "s", "s", null )).toEqual(-1); 
//       expect(await rateManage( client, "s", null, "s" )).toEqual(-1);  
//       expect(await rateManage( client, null, "s", "s" )).toEqual(-1);  
//       expect(await rateManage( null, "s", "s", "s" )).toEqual(-1);   
//     })
//     it('facilityID is not a integer', async () => {
//       expect(await rateManage( client, "s", "s", "s" )).toEqual(-1); 
//     })
     
//     it('try to rate posts', async () => {
//       expect(await rateManage(client, "posts", 1, "simon@gmail.copm" )).toEqual(null);  
//     })
//     it('try to rate facility except posts', async () => {
//       expect(await rateManage(client, "posts", 1, "simon@gmail.copm" )).toEqual(1);  
//     })
//     it('the user rated for a score out of range', async () => {
//       expect(await rateManage(client, "posts", 1, "l2542293790@gmail.com")).toEqual("repeatedly rating");  
//     })
//     //l2542293790@gmail.com
//     it('the user attempts to rate but already rated before', async () => {
//         expect(await rateManage(client, "posts", 1, "l2542293790@gmail.com")).toEqual("repeatedly rating");  
//     })
//     // it('user already commented but not yet rated before', async () => {
//     //   expect(await rateManage(client, "posts", 1, "l2542293790@gmail.com")).toEqual("repeatedly rating");  
//     // })
// })

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