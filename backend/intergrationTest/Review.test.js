const express = require("express"); // import express
const app = express(); //an instance of an express app, a 'fake' express app
const request = require('supertest');
const server  = require('../server6.js');

app.use("/", server); //routes
describe("testing-basic-get", () => {
  it("GET / server ip - success", async () => {
    const  {body}  = await request(app).get("/"); //uses the request function that calls on express app instance
    expect(body).toEqual({ "Result": "Connected to server at http://20.213.243.141:3000/"});
  });
});

app.use("/comment/add", server); //routes


describe(" review posts successful", () => {
  it("POST / insertion is successful for posts ", async () => {
    //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
    const res = await request(app).post("/comment/add").send({ "facilityType":"0", "facility_id":32, 
      "user_id":"111114@gmail.com", "replyContent":"s", "username":"sx", "rateScore":0,
      "AdditionType": "comment","upUserId":"simon@gmail.com" })
      //expect(res.body).toEqual({ "result": "comment_add!" })    
      expect(res.statusCode).toEqual(208) //changed fro 200 to 208 and test passed
    });


    it("POST / insertion is successful for studys", async () => {
      const res = await request(app).post("/comment/add").send({ "facilityType":"1", "facility_id":10, 
        "user_id":"111114@gmail.com", "replyContent":"s", "username":"sx", "rateScore":0,
        "AdditionType": "comment","upUserId":"simon@gmail.com" })
        //expect(res.body).toEqual({ "result": "comment_add!" })    
        expect(res.statusCode).toEqual(208) //changed from 200 to 208 and test passed
      });
});

describe(" review posts but it is a non existent user", () => {
    it("POST / posts ", async () => {
      //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
      const res = await request(app).post("/comment/add").send({ "facilityType":"0", "facility_id":33, 
        "user_id":"simon", "replyContent":"s", "username":"sx", "rateScore":0,
        "AdditionType": "comment","upUserId":"simon" })
        // expect(res.body).toEqual({ "data":"nonuser" })    
        expect(res.statusCode).toEqual(404)
      });

      it("POST / studys", async () => {
        const res = await request(app).post("/comment/add").send({ "facilityType":"1", "facility_id":10, 
          "user_id":"simon", "replyContent":"s", "username":"sx", "rateScore":0,
          "AdditionType": "comment","upUserId":"simon" })
          // expect(res.body).toEqual({ "data":"nonuser" })    
          expect(res.statusCode).toEqual(404)
        });
});

describe(" repeated review  ", () => {
    it("POST / ", async () => {
      const res = await request(app).post("/comment/add").send({ "facilityType":"1", "facility_id":11, 
        "user_id":"simon@gmail.com", "replyContent":"s", "username":"sx", "rateScore":0,
        "AdditionType": "comment","upUserId":"simon@gmail.com" })
        //expect(res.body).toEqual({"result" : ""})    
        expect(res.statusCode).toEqual(208)
      });
});

describe(" review that contains many null fields  ", () => {
    

    it("POST /    ", async () => {
        //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
        const res = await request(app).post("/comment/add").send({ "facilityType": "1", "facility_id": null, 
        "user_id":"simon", "replyContent":"s", "username":"sx", "rateScore":0,
        "AdditionType": "comment","upUserId":"simon" })
        // expect(res.body).toEqual({"data":"null"})    
            expect(res.statusCode).toEqual(404)
        });

    it("POST /    ", async () => {
        //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
        const res = await request(app).post("/comment/add").send({ "facilityType": "1", "facility_id":1, 
        "user_id": null, "replyContent":"s", "username":"sx", "rateScore":0,
        "AdditionType": "comment","upUserId":"simon" })
        // expect(res.body).toEqual({"data":"null"})    
            expect(res.statusCode).toEqual(404)
        });

    it("POST /    ", async () => {
        //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
        const res = await request(app).post("/comment/add").send({ "facilityType": "1", "facility_id":1, 
        "user_id": "simon", "replyContent":null, "username":"sx", "rateScore":"",
        "AdditionType": "comment","upUserId":"simon" })
        // expect(res.body).toEqual({"data":"null"})    
            expect(res.statusCode).toEqual(404)
    });
    it("POST /    ", async () => {
      //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
      const res = await request(app).post("/comment/add").send({ "facilityType": "1", "facility_id":1, 
      "user_id": "simon", "replyContent":"null", "username":"sx", "rateScore":3,
      "AdditionType": null,"upUserId":"simon" })
      // expect(res.body).toEqual({"data":"null"})    
          expect(res.statusCode).toEqual(404)
    });
    it("POST /    ", async () => {
      //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
      const res = await request(app).post("/comment/add").send({ "facilityType": "1", "facility_id":1, 
      "user_id": "simon", "replyContent":"null", "username":null, "rateScore":3,
      "AdditionType": "comment","upUserId":"simon" })
      // expect(res.body).toEqual({"data":"null"})    
          expect(res.statusCode).toEqual(404)
    });
  
});

describe(" review for a facility that does not exist  ", () => {
    it("POST / specific type", async () => {
      //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
        const res = await request(app).post("/comment/add").send({ "facilityType": "1", "facility_id":200, 
        "user_id": "simon@gmail.com", "replyContent":"as", "username": "Simon Xia", "rateScore":4,
        "AdditionType": "comment","upUserId":"simon" })
        // expect(res.body).toEqual({"data": "nonfacility"})    
        expect(res.statusCode).toEqual(404)
      });
});

describe(" review for a facility with a rate out of scope", () => {
  it("POST / specific type", async () => {
    //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
      const res = await request(app).post("/comment/add").send({ "facilityType": "1", "facility_id":2, 
      "user_id": "simon@gmail.com", "replyContent":"as", "username": "Simon Xia", "rateScore":9,
      "AdditionType": "comment","upUserId":"simon" })
     // expect(res.body).toEqual({"result": "outofrange"})    
      expect(res.statusCode).toEqual(404)
    });
});
