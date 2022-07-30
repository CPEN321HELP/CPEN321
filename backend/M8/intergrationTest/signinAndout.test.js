const express = require("express"); // import express
const app = express(); //an instance of an express app, a 'fake' express app
const request = require('supertest');
const server  = require('../server5.js');

app.use("/google_sign_up", server);

describe("non-registered sign up", () => {
    it("POST / specific type", async () => {
      //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
      const res = await request(app).post("/google_sign_up").send({"_id":"333333@gmail.com", "username":"name","logo":"new logo"})
      expect(res.statusCode).toEqual(200)
      });
  });

  describe("existing user sign up", () => {
    it("POST / specific type", async () => {
      //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
      const res = await request(app).post("/google_sign_up").send({"_id":"wuyuheng0525@gmail.com", "username":"Yuheng Wu","logo":"old logo"})
      expect(res.statusCode).toEqual(404)
      });
  });