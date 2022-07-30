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

app.use("/comment/add", server); //routes

//
// describe("review non posts facility", () => {
//   it("POST / ", async () => {
//     //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
//         const res = await request(app).post("/comment/add").send({})
//         expect(res.statusCode).toEqual(200)
//     });

// });

 
// describe(" posts facility  ", () => {
//   it("POST / specific type", async () => {
//     //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
//     const res = await request(app).post("/comment/add").send({ })
//     expect(res.body).toEqual({"result" : "null"})    
//         expect(res.statusCode).toEqual(404)
//     });
// });

describe(" review posts  ", () => {
    it("POST / specific type", async () => {
      //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
      const res = await request(app).post("/comment/add").send({ "facilityType":"0", "facility_id":1, 
        "user_id":"simon", "replyContent":"s", "username":"sx", "rateScore":0,
        "AdditionType": "comment","upUserId":"simon" })
       expect(res.body).toEqual({"result" : "comment_add!"})    
          expect(res.statusCode).toEqual(200)
      });
});

describe(" repeated posts  ", () => {
    it("POST / specific type", async () => {
      //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
      const res = await request(app).post("/comment/add").send({ "facilityType":"0", "facility_id":1, 
        "user_id":"simon", "replyContent":"s", "username":"sx", "rateScore":0,
        "AdditionType": "comment","upUserId":"simon" })
      expect(res.body).toEqual({"result" : "yes"})    
          expect(res.statusCode).toEqual(208)
      });
});

describe(" review that contains many null fields  ", () => {
    it("POST / specific type", async () => {
      //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
      const res = await request(app).post("/comment/add").send({ "facilityType": null, "facility_id":1, 
      "user_id":"simon", "replyContent":"s", "username":"sx", "rateScore":0,
      "AdditionType": "comment","upUserId":"simon" })
      expect(res.body).toEqual({"result" : "null"})    
          expect(res.statusCode).toEqual(404)
    });

    it("POST / specific type", async () => {
        //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
        const res = await request(app).post("/comment/add").send({ "facilityType": "1", "facility_id": null, 
        "user_id":"simon", "replyContent":"s", "username":"sx", "rateScore":0,
        "AdditionType": "comment","upUserId":"simon" })
        expect(res.body).toEqual({"result" : "null"})    
            expect(res.statusCode).toEqual(404)
        });

    it("POST / specific type", async () => {
        //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
        const res = await request(app).post("/comment/add").send({ "facilityType": "1", "facility_id":1, 
        "user_id": null, "replyContent":"s", "username":"sx", "rateScore":0,
        "AdditionType": "comment","upUserId":"simon" })
        expect(res.body).toEqual({"result" : "null"})    
            expect(res.statusCode).toEqual(404)
        });

    it("POST / specific type", async () => {
        //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
        const res = await request(app).post("/comment/add").send({ "facilityType": "1", "facility_id":1, 
        "user_id": "simon", "replyContent":"", "username":"sx", "rateScore":null,
        "AdditionType": "comment","upUserId":"simon" })
        expect(res.body).toEqual({"result" : "null"})    
            expect(res.statusCode).toEqual(404)
        });
});


describe(" facility does not exist  ", () => {
    it("POST / specific type", async () => {
      //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
        const res = await request(app).post("/comment/add").send({ "facilityType": "1", "facility_id":200, 
        "user_id": "simon", "replyContent":"", "username":"sx", "rateScore":null,
        "AdditionType": "comment","upUserId":"simon" })
        expect(res.body).toEqual({"result" : "already_exist"})    
          expect(res.statusCode).toEqual(208)
      });
});