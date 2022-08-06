const express = require("express"); // import express
const app = express(); //an instance of an express app, a 'fake' express app
const request = require('supertest');
const server  = require('../server6.js');
 

app.use("/", server);
// describe("testing-basic-get", () => {
//       it("GET / server ip - success", async () => {
//         const { body } = await request(app).get("/"); //uses the request function that calls on express app instance
//         expect(body).toEqual({ "Result": "Connected to server at http://20.213.243.141:3000/"});
//       });
// });

app.use("/Votes", server);
describe("success", () => {
  it("POST / upvote existed", async () => {
    
    const res = await request(app).put('/Votes').send({"facilityType":"1", "facility_id":11,
    "user_id":"simon@gmail.com" , "vote":"up", "isCancelled":"pend"})
    expect(res.body).toEqual({"data" : "upvote"})  
    expect(res.statusCode).toEqual(200)
    });

  it("POST / cancel upvote", async () => {
    
    const res = await request(app).put('/Votes').send({"facilityType":"1", "facility_id":9,
    "user_id":"simon@gmail.com" , "vote":"down", "isCancelled":"pend"})
    expect(res.body).toEqual({"data" : "downvote"})  
    expect(res.statusCode).toEqual(200)
    });

  it("POST / down vote  ", async () => {
     
    const res = await request(app).put('/Votes').send({"facilityType":"1", "facility_id": 11,
    "user_id":"simon@gmail.com" , "vote":"up", "isCancelled":"x"})
    expect(res.body).toEqual({"data" : "cancel upvote"})  
    expect(res.statusCode).toEqual(200)
    });
  it("POST / cancel downvote  ", async () => {
     
    const res = await request(app).put('/Votes').send({"facilityType":"1", "facility_id": 9,
    "user_id":"simon@gmail.com" , "vote":"down", "isCancelled":"x"})
    expect(res.body).toEqual({"data" : "cancel downvote"})  
    expect(res.statusCode).toEqual(200)
    });
});

describe(" null fields ", () => {
    it("POST / facility type ", async () => {
      //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
      const res = await request(app).put('/Votes').send({"facilityType":"", "facility_id":1,
      "user_id":"s" , "vote":"up", "isCancelled":"s"})
      expect(res.body).toEqual({"data" : "null"})  
      expect(res.statusCode).toEqual(404)
      });
    it("POST / facility id ", async () => {
      //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
      const res = await request(app).put('/Votes').send({"facilityType":"1", "facility_id":"",
      "user_id":"s" , "vote":"up", "isCancelled":"s"})
      expect(res.body).toEqual({"data" : "null"})  
      expect(res.statusCode).toEqual(404)
    });
    it("POST / user id", async () => {
        //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
        const res = await request(app).put('/Votes').send({"facilityType":"1", "facility_id":2,
        "user_id":"" , "vote":"up", "isCancelled":"s"})
        expect(res.body).toEqual({"data" : "null"})  
        expect(res.statusCode).toEqual(404)
    });
    it("POST / vote", async () => {
        //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
        const res = await request(app).put('/Votes').send({"facilityType":"1", "facility_id":2,
        "user_id":"simon" , "vote":"", "isCancelled":""})
        expect(res.body).toEqual({"data" : "null"})  
        expect(res.statusCode).toEqual(404)
    });
});

describe("non existent situations", () => {
    it("POST / non existent user ", async () => {
      //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
      const res = await request(app).put('/Votes').send({"facilityType":"1", "facility_id":200,
      "user_id":"simon" , "vote":"up", "isCancelled":"pend"})
      expect(res.body).toEqual({"data" : "nonexistent"})  
      expect(res.statusCode).toEqual(404)
    });
    it("POST / non existent facility", async () => {
      //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
      const res = await request(app).put('/Votes').send({"facilityType":"1", "facility_id": 9,
      "user_id":"xxx" , "vote":"up", "isCancelled":"pend"})
      expect(res.body).toEqual({"data" : "nonexistent"})  
      expect(res.statusCode).toEqual(404)
    });
     
});

describe("user never  reviewed", () => {
    it("POST / repated user ", async () => {
      //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
        const res = await request(app).put('/Votes').send({"facilityType":"1", "facility_id": 11 ,
        "user_id":"lufei8351@gmail.com" , "vote":"up", "isCancelled":"pend"})
        expect(res.body).toEqual({"data" : "neverCommented"})  
        expect(res.statusCode).toEqual(404)
    }); 
});
 
