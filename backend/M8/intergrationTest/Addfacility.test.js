const express = require("express"); // import express
const app = express(); //an instance of an express app, a 'fake' express app
const request = require('supertest');
const server  = require('../server6.js');


app.use("/", server); //routes
describe("testing-basic-get", () => {
  it("GET / server ip - success", async () => {
    const { body } = await request(app).get("/"); //uses the request function that calls on express app instance
    expect(body).toEqual({ "Result": "Connected to server at http://20.213.243.141:3000/"});
  });
});

describe("testing add with missing field", () => {
  it("POST /addFacility", async () => {
    const res = await request(app).post("/addFacility").send({title:""})
        expect(res.statusCode).toEqual(404)
    });

});

describe("testing add with location misplacement", () => {
    it("POST /addFacility", async () => {
      const res = await request(app).post("/addFacility").send({title:"IKB",description:"is library", adderID:"adder@gmail.com", long:"49.2675",lat:"-123.2527"})
          expect(res.statusCode).toEqual(404)
      });
  
  });

  describe("testing add with field misplacement", () => {
    it("POST /addFacility", async () => {
      const res = await request(app).post("/addFacility").send({title:"IKB",description:"-123.2527", adderID:"49.2675", long:"adder@gmail.com",lat:"is library"})
          expect(res.statusCode).toEqual(404)
      });
  
  });

  describe("testing add with all correct field", () => {
    it("POST /addFacility", async () => {
      const res = await request(app).post("/addFacility").send({title:"IKB",description:"is library", adderID:"adder@gmail.com", long:"-49.2675",lat:"123.2527"})
          expect(res.statusCode).toEqual(200)
      });
  
  });
 







  



 
