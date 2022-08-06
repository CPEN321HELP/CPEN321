const express = require("express"); // import express
const app = express(); //an instance of an express app, a 'fake' express app
const request = require('supertest');
const server  = require('../server6.js');

app.use("/", server); //routes
// describe("testing-basic-get", () => {
//   it("GET / server ip - success", async () => {
//     const { body } = await request(app).get("/"); //uses the request function that calls on express app instance
//     // expect(body).toEqual({ "Result": "Connected to server at http://20.213.243.141:3000/"});
//   });
// });

app.use("/specific", server); //routes

//
describe("testing specific 1", () => {
    it("POST / specific type", async () => {
    //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
        const res = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
        expect(res.statusCode).toEqual(200)
    });
    it("POST / specific type", async () => {
        //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
        const res = await request(app).post("/specific").send({"facilityType": null , "facility_id" : "1"})
            expect(res.statusCode).toEqual(404)
    });
    it("POST / specific type", async () => {
        //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
        const res = await request(app).post("/specific").send({"facilityType": "1" , "facility_id" : null})
        expect(res.statusCode).toEqual(404)
    });
    it("POST / specific type", async () => {
        //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
        const res = await request(app).post("/specific").send({"facilityType": "1" , "facility_id" : "200"})
        expect(res.body).toEqual({ "result": "null" })
    });
    it("POST / specific type", async () => {
        //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
        const res = await request(app).post("/specific").send({"facilityType": "200" , "facility_id" : "1"})
        expect(res.body).toEqual({ "result": "null" })
    });
});


 
describe("testing specific that the facility has invalid facility type", () => {
  it("POST / specific type", async () => {
        const res = await request(app).post("/specific").send({"facilityType":"jio" , "facility_id" : "1"}) 
        expect(res.body).toEqual( {"result":"unsuccesful find with missing field"})
    });
});
 

describe("testing specific that the facility has invalid facility type", () => {
  it("POST / specific type", async () => {
    //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
    const res = await request(app).post("/specific").send({"facilityType":"@@@" , "facility_id" : "1"})
        expect(res.body).toEqual( {"result":"unsuccesful find with missing field"})    
        //expect(res.body).toEqual(404)
    });
});

describe("testing specific that facility has invalid facility id", () => {
    it("POST / specific type", async () => {
      //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
        const res = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "@@@"})
        expect(res.body).toEqual( {"result":"unsuccesful find with missing field"})    
        //expect(res.body).toEqual(404)
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
        expect(res.body).toEqual( {"result":"unsuccesful find with invalid type"} )    
        expect(res.statusCode).toEqual(400)
        });
});


describe("testing find many", () => {
    it("POST / many newest facilities ", async () => {
        //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
        const res = await request(app).post("/facility/newest").send({'type':'*&^%'})
        expect(res.body).toEqual( {"result":"unsuccesful find with invalid type"})    
        expect(res.statusCode).toEqual(400)
        });
});
describe("testing find many", () => {
    it("POST / many newest facilities ", async () => {
        //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
        const res = await request(app).post("/facility/newest").send({'type': null})
        expect(res.body).toEqual( {"result":"unsuccesful find with invalid type"})    
        expect(res.statusCode).toEqual(400)
        });
});

// //-----------------------------------------------------

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
        const res = await request(app).post("/facility/search").send({'type':'1', "search": "@@"})
        //expect(res.body).toEqual({"result" : [], "length":0})    
        expect(res.statusCode).toEqual(200)
    });
});
    
describe("testing search", () => {
    it("POST / search facilities ", async () => {
        //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
        const res = await request(app).post("/facility/search").send({'type':'1', "search": ""})
      
        expect(res.body).toEqual({"result":"unsuccesful search with missing field"})    
        expect(res.statusCode).toEqual(404)
        });
    });

describe("testing search", () => {
    it("POST / search facilities ", async () => {
        //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
        const res = await request(app).post("/facility/search").send({'type':'**', "search": ""})
        //expect(res.body).toEqual({"result":"unsuccesful find with invalid type"})    
        expect(res.statusCode).toEqual(404)
        });
    });

    describe("testing search", () => {
        it("POST / search facilities ", async () => {
            //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
            const res = await request(app).post("/facility/search").send({'type':'10', "search": "j"})
            //expect(res.body).toEqual({"result":"unsuccesful find with invalid type"})    
            expect(res.statusCode).toEqual(404)
            });
        });
    
describe("testing search", () => {
    it("POST / search facilities ", async () => {
        //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
        const res = await request(app).post("/facility/search").send({'type':'1', "search": "1234567891011123456789ijhnbfdstyukjmnbvdsetyujhgbvcsaewrtyujhnbvdfswetyujhnbvcdsewtyhgbvcx"})
        expect(res.body).toEqual({"result" : [], "length":0})    
       // expect(res.statusCode).toEqual(404)
    });
});





 
