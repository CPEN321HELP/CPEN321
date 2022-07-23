
const {addFacility,searchFacility,getFacilityByType} = require("./Facility.mock")


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
  

beforeAll(done => {
  done()
})

//testSet for interface addFacility
describe('testing addFacility', () => {
  test('invalid input', async () => {
    const facilityFields0 = {
        "_id" : "ll@@@@}}"
      }
    await addFacility(facilityFields0, (err,status,returnData) => {
      expect(err).toBeNull()
      expect(status).toStrictEqual(404);
      expect(returnData).toEqual('invalid input')
    })
  })
  
  test('missing adder invalid add', async () => {
    const facilityFields1 = {
        "_id" : 1
      }
    await addFacility(facilityFields1, (err,status,returnData) => {
      expect(err).toBeNull()
      expect(status).toStrictEqual(404);
      expect(returnData).toEqual('missing field, unsucessful add')
    })
  })

  test('pre-existed facility', async () => {
    const facilityFields2 = {
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
        }
   await addFacility(facilityFields2, (err,status,returnData) => {
      expect(err).toBeNull()
      expect(status).toStrictEqual(404);
      expect(returnData).toEqual('add unsucsseful, facility already existed')
    })
  })

  test('add facility success', async () => {
    const facilityFields3 = {
        "_id" : 10,
        "facility" : {
                "facilityType" : "entertainments",
                "facility_status" : "normal",
                "facilityTitle" : "Tower Beach",
                "facilityDescription" : "Discover this 6.4-km out-and-back trail near Greater Vancouver A, British Columbia. Generally considered an easy route, it takes an average of 1 h 38 min to complete. This is a popular trail for birding, hiking, and trail running, but you can still enjoy some solitude during quieter times of day. The trail is open year-round and is beautiful to visit anytime. Dogs are welcome, but must be on a leash.",
                "facilityImageLink" : "https://s3.bmp.ovh/imgs/2022/07/11/4ee674f9796be12c.jpg",
                "facilityOverallRate" : 0,
                "numberOfRates" : 0,
                "timeAdded" : "2022/6/11",
                "longitude" : -123.2567102,
                "latitude" : 49.2727073
        },
        "ratedUser" : [ ],
        "reviews" : [ ],
        "adderID" : "123@gmail.com"
    }
  
     
   await addFacility(facilityFields3, (err,status,returnData) => {
      expect(err).toBeNull()
      expect(status).toStrictEqual(200);
      expect(returnData).toEqual('add sucessful')
    })
  })

  
})


//testSet for interface search facility
describe('testing search Facility', () => {
    test('invalid input', async () => {
        const facilitysearchFields0 = {
            "_id" : "ll@@@@}}"
          }
        await searchFacility(facilitysearchFields0, (err,status,returnData) => {
          expect(err).toBeNull()
          expect(status).toStrictEqual(404);
          expect(returnData).toEqual('invalid input')
        })
      })

      test('empty input', async () => {
        const facilitysearchFields0 = {
            "_id" : ""
          }
        await searchFacility(facilitysearchFields0, (err,status,returnData) => {
          expect(err).toBeNull()
          expect(status).toStrictEqual(404);
          expect(returnData).toEqual('empty input')
        })
      })
      
      test('missing adder invalid add', async () => {
        const facilitysearchFields1 = {
           "facility":{}
          }
        await searchFacility(facilitysearchFields1, (err,status,returnData) => {
          expect(err).toBeNull()
          expect(status).toStrictEqual(404);
          expect(returnData).toEqual('missing field, unsucessful search')
        })
      })

      test('successful search', async () => {
        const facilitysearchFields2 = {
           "_id":1
          }
        await searchFacility(facilitysearchFields2, (err,status,returnData) => {
          expect(err).toBeNull()
          expect(status).toStrictEqual(200);
          expect(returnData).toEqual([1])
        })
      })
   
  })

//testSet for interface  getFacilityByTYpe
describe('testing getFacilityByTYpe', () => {
  test('invalid input', async () => {
    const facilitysearchFields0 = {
        "numberOfType" : "ll@@@@}}"
      }
    await getFacilityByType(facilitysearchFields0, (err,status,returnData) => {
      expect(err).toBeNull()
      expect(status).toStrictEqual(404);
      expect(returnData).toEqual('invalid input')
    })
  })
  
  test('missing field', async () => {
    const facilitysearchFields1 = {
       "facility":{}
      }
    await getFacilityByType(facilitysearchFields1, (err,status,returnData) => {
      expect(err).toBeNull()
      expect(status).toStrictEqual(404);
      expect(returnData).toEqual('missing field, unsucessful get')
    })
  })

  test('get one success', async () => {
    const facilitysearchFields2 = {
      "numberOfType":2
      }
    await getFacilityByType(facilitysearchFields2, (err,status,returnData) => {
      expect(err).toBeNull()
      expect(status).toStrictEqual(200);
      expect(returnData).toEqual(facilityJson)
    })
  })

  test('get many same type success', async () => {
    const facilitysearchFields3 = {
      "numberOfType":3,
      "newestID":11
      }
    await getFacilityByType(facilitysearchFields3, (err,status,returnData) => {
      expect(err).toBeNull()
      expect(status).toStrictEqual(200);
      expect(returnData).toEqual([
        11, 10, 9, 8, 7,
         6,  5, 4, 3, 2,
         1
      ])
    })
  })
})


afterAll((done) => {
  done();
});
