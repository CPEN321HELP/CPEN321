const express = require("express"); // import express
const app = express(); //an instance of an express app, a 'fake' express app
const request = require('supertest');
const server  = require('../server6.js');

app.use("/", server);
describe("testing-basic-get", () => {
  it("GET / server ip - success", async () => {
    const { body } = await request(app).get("/"); //uses the request function that calls on express app instance
    expect(body).toEqual({ "Result": "Connected to server at http://20.213.243.141:3000/"});
  });
});

app.use("/google_sign_up", server);
describe("non-registered sign up", () => {
  it("POST / success", async () => {
    //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
    const res = await request(app).post('/google_sign_up').send({"_id":"333334@gmail.com", "username":"name","user_logo":"new logo"})
    expect(res.body).toEqual({"data" : "added"})  
    expect(res.statusCode).toEqual(201)
    });

  it("POST / missing id", async () => {
    //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
    const res = await request(app).post('/google_sign_up').send({"_id":"", "username":"name","user_logo":"new logo"})
    expect(res.body).toEqual({"data" : "null"})  
    expect(res.statusCode).toEqual(404)
    });

  it("POST / missing name ", async () => {
    //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
    const res = await request(app).post('/google_sign_up').send({"_id":"333333@gmail.com", "username":"","user_logo":"new logo"})
    expect(res.body).toEqual({"data" : "null"})  
    //{"data" : "missing_fields"}
    expect(res.statusCode).toEqual(404)
    });
  it("POST / missing logo ", async () => {
    //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
    const res = await request(app).post('/google_sign_up').send({"_id":"111112@gmail.com", "username":"name","user_logo":""})
    expect(res.body).toEqual({"data" : "added"})  
    expect(res.statusCode).toEqual(201)
    });
});

describe("existing user sign in", () => {
    it("POST / success", async () => {
      //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
      const res = await request(app).post('/google_sign_up').send({"_id":"wuyuheng0525@gmail.com",  "username":"Yuheng Wu","user_logo":"none"})
      expect(res.statusCode).toEqual(200)
      });
    it("POST / missing user name", async () => {
      //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
      const res = await request(app).post('/google_sign_up').send({"_id":"simon@gmail.com",  "username":"","user_logo":"none"})
      expect(res.body).toEqual({"data" : "null"})  
      expect(res.statusCode).toEqual(404)
      });
  });
