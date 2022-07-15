// function forEach(items, callback) {
//   var y = 0
//   for (let index = 0; index < items.length; index++) {
//       y+= callback(items[index]);
//   }
//   return y
// }

// const mockCallback = jest.fn(x => 42 + x);
// test('should first', ()=>{
//   expect(forEach([0, 1], mockCallback)).toEqual(85);
//   expect(mockCallback.mock.calls.length).toBe(2);
//   expect(mockCallback.mock.calls[0][0]).toBe(0);
//   expect(mockCallback.mock.calls[1][0]).toBe(1);
//   expect(mockCallback.mock.results[0].value).toBe(42);
// } )


// The mock function is called twice


// // The first argument of the first call to the function was 0


// // The first argument of the second call to the function was 1


// // The return value of the first call to the function was 42


//Test Set 1 --> Testing the interface specific for module facility
const interfacespecific = require('./interfacespecific');

test("interfacespecific invalid input", ()=>{
  expect(interfacespecific("ll@@@@}}")).not.toEqual('posts');
})

test("interfacespecific number out of bound", ()=>{
  expect(interfacespecific(100000)).not.toEqual('studys');
})

test("interfacespecific inbound wrong type", ()=>{
  expect(interfacespecific(1)).not.toEqual('posts');
})

test("interfacespecific posts success", ()=>{
  expect(interfacespecific(0)).toEqual('posts');
})

test("interfacespecific studys success", ()=>{
  expect(interfacespecific(1)).toEqual('studys');
})

test("interfacespecific entertainments success", ()=>{
  expect(interfacespecific(2)).toEqual('entertainments');
})

test("interfacespecific restaurants success", ()=>{
  expect(interfacespecific(3)).toEqual('restaurants');
})



//Test 2--> Test set for rate interface
const interfaceRate = require('./interfaceRate');

test("interfacesRate invalid input", ()=>{
  expect(interfaceRate("ll@@@@}}")).not.toEqual(0);
})

test("empty rate", ()=>{
  expect(interfaceRate([])).toEqual(0);
})

test("interfacesRate valid array invalid result", ()=>{
  expect(interfaceRate([1,2,3])).not.toEqual(3);
})

test("interfacesRate valid array and valid result", ()=>{
  expect(interfaceRate([1,2,3,4,5,6,7,8])).toEqual(4.5);
})


//Test set 3 -->   Test set for newest
const interfaceNewest = require('./interfaceNewestFacility');

test("interfaceNewest invalid input", ()=>{
  expect(interfaceNewest("ll@@@@}}")).not.toEqual({result:"",length:0});
})

test("interfaceNewest empty input", ()=>{
  expect(interfaceNewest([])).toEqual({result:"",length:0});
})

test("interfaceNewest valid input invalid result", ()=>{
  expect(interfaceNewest(["3","3","3"])).not.toEqual({result:"",length:5});
})

test("interfaceNewest valid input and valid result", ()=>{
  expect(interfaceNewest(["item one","item 2",{item3:"item three"}])).
  toEqual({result:["item one","item 2",{item3:"item three"}],length:3});
})

//Test set 4 -->   Test set for addFacility
const interfaceAddFacility = require('./interfaceAddFacility');

test("interfaceNewest invalid input", ()=>{
  expect(interfaceAddFacility("ll@@@@}}")).not.toEqual({result:"",length:0});
})

test("interfaceNewest invalid due to missing field", ()=>{
  expect(interfaceAddFacility({id:1})).toEqual("Error:Invalid add, missing field");
})

test("interfaceNewest valid input", ()=>{
  expect(interfaceAddFacility({id:2,title:"facility title",description:"facility description",
                               long:"23.346778",lat:"-123.34456", type:"studys",
                               facilityImageLink:"https://example.png"})).
                              toEqual({
                                id: 2,
                                facility:
                                {
                                    "facilityType": "studys",
                                    "facility_status": "normal",
                                    "facilityTitle": "facility title",
                                    "facilityDescription": "facility description",
                                    "facilityImageLink": "https://example.png",
                                    "facilityOverallRate": 0,
                                    "numberOfRates": 0,
                                    "timeAdded": "timeAdded",
                                    "longitude": 23.346778,
                                    "latitude": -123.34456
                                },
                                "rated_user": [{}],
                                "reviews": [{}]
                            });
                            })

//Test set 5 -->   Test set for reportFacility


const interfaceReportFacility = require('./interfaceReportFacility');

test("interfaceReportFacility invalid input", ()=>{
  expect( interfaceReportFacility("ll@@@@}}")).not.toEqual({result:"",length:0});
})

test("interfaceReportFacility invalid due to missing field", ()=>{
  expect( interfaceReportFacility({approve:3})).toEqual("Error: Invalid report, missing field.");
})

test("interfaceReportFacility invalid due to report type", ()=>{
  expect( interfaceReportFacility({reportType:7,approve:0})).toEqual("Error: Invalid report, wrong report type.");
})

test("interfaceReportFacility valid report", ()=>{
  expect( interfaceReportFacility({reportType:5,approve:1})).toEqual("Report succeeded");
})




// Test set 6 --> test for search logic return


const example2 = jest.fn( x =>'posts');

//const xxx = require("./xxx.js")
function xx(type, example1) {
  var numberOfType = parseInt(type);
  console.log(numberOfType);
  type = example1(numberOfType);
  return type;
}

//const foo = require("./foo")
test("guaguagua", ()=>{
  expect(xx("0", example2)).toEqual("posts")
  expect(example2.mock.calls.length).toBe(1);
  expect(example2.mock.calls[0][0]).toBe(0);
  expect(example2.mock.results[0].value).toBe("posts");
})


function yy(result, returnLogic){
  var final = returnLogic(result);
  return final;
}
const example3 = jest.fn( x =>final={"result":[[8,0,"Ice cream","Ice cream is a sweetened frozen food typically eaten as a snack or dessert. It may be made from milk or cream and is flavoured with a sweetener, either sugar or an alternative, and a spice, such as cocoa or vanilla, or with fruit such as strawberries or peaches. Wikipedia","2022/6/11"]],"length":1});

test("lalalala", ()=>{
  expect(yy({"_id":8,"facility":{"facilityType":"posts","facility_status":"normal","facilityTitle":"Ice cream","facilityDescription":"Ice cream is a sweetened frozen food typically eaten as a snack or dessert. It may be made from milk or cream and is flavoured with a sweetener, either sugar or an alternative, and a spice, such as cocoa or vanilla, or with fruit such as strawberries or peaches. Wikipedia","facilityImageLink":"https://imgtu.com/i/jyXa9K","facilityOverallRate":0,"numberOfRates":0,"timeAdded":"2022/6/11","longitude":null,"latitude":null},"rated_user":[{}],"reviews":[{}]}, example3)).toEqual({"result":[[8,0,"Ice cream","Ice cream is a sweetened frozen food typically eaten as a snack or dessert. It may be made from milk or cream and is flavoured with a sweetener, either sugar or an alternative, and a spice, such as cocoa or vanilla, or with fruit such as strawberries or peaches. Wikipedia","2022/6/11"]],"length":1})
  // expect(example3.mock.calls.length).toBe(1);
  // expect(example3.mock.calls[0][0]).toBe(0);
  // expect(example3.mock.results[0].value).toBe("posts");
})


//test 7 
async function creditHandlingNormal (client, AdditionType, goodUserId, findTheUser, creditCalculation){
  const additionCredit_addFacility = 5;
  const additionCredit_comment = 1;
  const result = await findTheUser(client, goodUserId)
  
  console.log("the user profile(handled by credit ) is ");
  console.log(result);
  var currentadderCredits = result.number_of_credit;
  // const result2 = await client.db("user").collection("users").findOne({_id : badUserId});
  // var currentSubtractorCredits = result2.number_of_credit;

  if (AdditionType === "addFacility") {
      currentadderCredits += additionCredit_addFacility;
  } else if (AdditionType === "comment") {
      currentadderCredits += additionCredit_comment;
  } else {
      res.send("No credits granted since no contributions made, please make contribution before any credit is granted; AdditionType is not matched in this case!");
  }
  await creditCalculation(client, goodUserId , currentadderCredits)   
  return 
}

const findTheUserMock = jest.fn();

const creditCalculationMock = jest.fn();


test("This is a creditHandling interface that is exposed to the rate", ()=>{
  expect(creditHandlingNormal("", "comment" , "l2542293790@gmail.com", findTheUserMock , creditCalculationMock)).toEqual()
  // expect(example3.mock.calls[0][0]).toBe(0);
  // expect(example3.mock.results[0].value).toBe("posts");
})



// return logic test 8 
var ttt = {
  "_id" : 11,
  "facility" : {
          "facility_status" : "normal",
          "facilityType" : "posts",
          "facilityTitle" : "engineering",
          "facilityDescription" : "Engineering is the use of scientific principles to design and build machines, structures, and other items, including bridges, tunnels, roads, vehicles, ...",
          "timeAdded" : "2022/6/11",
          "facilityImageLink" : "https://imgtu.com/i/jyXa9K",
          "facilityOverallRate" : 4.113636363636363,
          "numberOfRates" : 89,
          
  },
  "ratedUser" : [
          {

          }
  ],
  "reviews" : [
         
  ]
 
}
function returnLogic(ttt , callback) {
  var final = {}
  var bigArr = [];
  bigArr.push(ttt); //result is a big JSON of two elements 
  var arr = [];
  for (var i = 0; i < bigArr.length; i++) {
      arr.push(bigArr[i])
  }
  // var theOne = [];

  // for (var i = 0; i < arr.length; i++) {
  //     var zz = []
  //     zz.push(arr[i]._id)
  //     zz.push(arr[i].facility.facilityOverallRate)
  //     zz.push(arr[i].facility.facilityTitle)
  //     zz.push(arr[i].facility.facilityDescription)
  //     zz.push(arr[i].facility.timeAdded)
  //     // zz[ x, y, z]
  //     theOne.push(zz)
  // }

  var theOne = callback(); //return [ ["1" , 4.5, "aa", "aacontent", "xxx"], ["2" , 3.5, "b", "bbcontent", "yyy"] ]
  var length2 = theOne.length;
  final["result"] = theOne;
  final["length"] = length2;
  console.log("return is ")
  console.log(final)
  return final; // {"result" :  , "length" : 2}
}
