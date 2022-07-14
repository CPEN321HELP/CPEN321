//The following are all the mock functions for module --> Facility


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
