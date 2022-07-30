const express = require("express"); // import express
const app = express(); //an instance of an express app, a 'fake' express app
const request = require('supertest');
const server  = require('../server5.js');

app.use("/", server); //routes
describe("testing-basic-get", () => {
  it("GET / server ip - success", async () => {
    const { body } = await request(app).get("/"); //uses the request function that calls on express app instance
    expect(body).toEqual({ "Result": "Connected to server at http://20.213.243.141:3000/"});
  });
});

app.use("/specific", server); //routes

//
describe("testing specific", () => {
  it("POST / specific type", async () => {
    //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
    const res = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
    expect(res.body).toEqual({
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
    })    
        expect(res.statusCode).toEqual(200)
    });

});

 
describe("testing specific that the facility has invalid facility type", () => {
  it("POST / specific type", async () => {
    //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
    const res = await request(app).post("/specific").send({"facilityType":"jio" , "facility_id" : "1"})
    expect(res.body).toEqual({"result" : "null"})    
        expect(res.statusCode).toEqual(404)
    });
});
 
describe("testing specific that the facility has invalid facility type", () => {
  it("POST / specific type", async () => {
    //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
    const res = await request(app).post("/specific").send({"facilityType":"@@@" , "facility_id" : "1"})
    expect(res.body).toEqual({"result" : "null"})    
        expect(res.statusCode).toEqual(404)
    });
});
describe("testing specific that facility has invalid facility id", () => {
    it("POST / specific type", async () => {
      //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
      const res = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "@@@"})
      expect(res.body).toEqual({"result" : "null"})    
          expect(res.statusCode).toEqual(404)
      });
});
describe("testing specific that the facility does not exist", () => {
    it("POST / specific type", async () => {
      //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
      const res = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "200"})
      expect(res.body).toEqual({"result" : "null"})    
          expect(res.statusCode).toEqual(404)
      });
});





// ---------------------------------
app.use("/facility/newest", server); //routes

describe("testing find many", () => {
    it("POST / many newest facilities ", async () => {
        //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
        const res = await request(app).post("/facility/newest").send({'type':'1'})
        //expect(res.body).toEqual({"result" : "null"})    
        expect(res.statusCode).toEqual(200)
      });
  });


describe("testing find many", () => {
    it("POST / many newest facilities ", async () => {
        //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
        const res = await request(app).post("/facility/newest").send({'type':'8'})
        expect(res.body).toEqual({"result" : "null"})    
        expect(res.statusCode).toEqual(404)
        });
});


describe("testing find many", () => {
    it("POST / many newest facilities ", async () => {
        //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
        const res = await request(app).post("/facility/newest").send({'type':'*&^%'})
        expect(res.body).toEqual({"result" : "null"})    
        expect(res.statusCode).toEqual(404)
        });
    });

    




//-----------------------------------------------------

app.use("/facility/search", server); //routes

describe("testing search", () => {
    it("POST / search facilities ", async () => {
        //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
        const res = await request(app).post("/facility/search").send({'type':'1', "search": "UBC"})
        //expect(res.body).toEqual({"result" : "null"})    
        expect(res.statusCode).toEqual(200)
        });
    });

    
describe("testing search", () => {
    it("POST / search facilities ", async () => {
        //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
        const res = await request(app).post("/facility/search").send({'type':'1', "search": "$$"})
        //expect(res.body).toEqual({"result" : "null"})    
        expect(res.statusCode).toEqual(404)
        });
    });
    
describe("testing search", () => {
    it("POST / search facilities ", async () => {
        //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
        const res = await request(app).post("/facility/search").send({'type':'1', "search": ""})
        //expect(res.body).toEqual({"result" : "null"})    
        expect(res.statusCode).toEqual(404)
        });
    });

describe("testing search", () => {
    it("POST / search facilities ", async () => {
        //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
        const res = await request(app).post("/facility/search").send({'type':'**', "search": ""})
        //expect(res.body).toEqual({"result" : "null"})    
        expect(res.statusCode).toEqual(404)
        });
    });


describe("testing search", () => {
    it("POST / search facilities ", async () => {
        //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
        const res = await request(app).post("/facility/search").send({'type':'1', "search": "1234567891011123456789ijhnbfdstyukjmnbvdsetyujhgbvcsaewrtyujhnbvdfswetyujhnbvcdsewtyhgbvcx"})
        expect(res.body).toEqual({"result" : "null"})    
        expect(res.statusCode).toEqual(404)
        });
    });






// app.use("/specific", server); //routes
// describe("testing specific", () => {
//   it("POST / specific type", async () => {
//     const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
//     expect(body).toEqual({
//       "_id" : 1,
//       "facility" : {
//               "facility_status" : "normal",
//               "facilityType" : "studys",
//               "facilityTitle" : "The Irving K. Barber Learning Centre",
//               "facilityDescription" : "The Irving K. Barber Learning Centre is a facility at the Vancouver campus of the University of British Columbia. The Learning Centre is built around the refurbished core of the 1925 UBC Main Library.",
//               "timeAdded" : "2022/07/06",
//               "facilityImageLink" : "https://cdn.discordapp.com/attachments/984213736652935230/994316357028036738/unknown.png",
//               "facilityOverallRate" : 2.5,
//               "numberOfRates" : 1,
//               "longitude" : -123.2527,
//               "latitude" : 49.2675
//       },
//       "ratedUser" : [
//               {
//                       "replierID" : "l2542293790@gmail.com"
//               }
//       ],
//       "reviews" : [
//               {
//                       "replierID" : "l2542293790@gmail.com",
//                       "userName" : "Linxin Li",
//                       "rateScore" : 2.5,
//                       "upVotes" : 0,
//                       "downVotes" : 0,
//                       "replyContent" : "testing ",
//                       "timeOfReply" : "2022/6/18/23/41/5"
//               }
//       ],
//       "adderID" : ""
// })    
//   expect(body.statusCode).toEqual(200)
//    });

// });
