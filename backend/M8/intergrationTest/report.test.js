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

app.use("/user/Report/commentAndfacility", server); //routes
describe("testing userside report comment or facility", () => {
  it("POST/user/Report/commentAndfacility", async () => {
    const res = await request(app).post("/user/Report/commentAndfacility")
    .send({"reportedFacilityType":"entertainments",
            "reporterID":"wuyuheng0525@gmail.com",
            "reportReason":"reason",
            "reported_id":"12345667@gmail.com",
            "report_type":"facility",
            "reportUserCond":1})
    expect(res.statusCode).toEqual(200)
   });
   
});

describe("user report their own comment", () => {
  it("POST/user/Report/commentAndfacility", async () => {
    const res = await request(app).post("/user/Report/commentAndfacility")
    .send({"reportedFacilityType":"entertainments",
            "reporterID":"wuyuheng0525@gmail.com",
            "reportReason":"reason",
            "reported_id":"wuyuheng0525@gmail.com",
            "report_type":"facility",
            "reportUserCond":1})
    expect(res.statusCode).toEqual(200)
   });
});


app.use("/report/admin", server); //routes
describe("testing displaying any number of report for admin", () => {
  it("get//report/admin", async () => {
    const res = await request(app).get("/report/admin")
     expect(res.statusCode).toEqual(200)
    });
});



// report_type
// var reportID = req.body.report_id;
// var approveDecision = req.body.approve;
// var facility_type = req.body.facility_type;
// var reportedFacilityid = parseInt(req.body.facility_id);
// var reportedID = req.body.reported_user;
// var upUser = req.body.upUserId;
// var downUSer = req.body.downUserId;

app.use("/admin/reportApprovalnonAdmin", server); //routes
describe("not admin making decision", () => {
  it("/report/adminapprove0", async () => {
    const res = await request(app).post("/admin/reportApproval").send({report_type:"facility",report_id:"id", approveDecision:1,
          faciliy_type:"posts", facility_id:1, reported_user:"none@gmail.com", upUserId:"wuyuheng0525@gmail.com",
          downUserId:"none@gmail.com"})
     expect(res.statusCode).toEqual(404)
    });

})
describe("testing report facility and approve with credit addition", () => {
  it("/report/adminapprove1", async () => {
    const res = await request(app).post("/admin/reportApproval").send({report_type:"facility",report_id:"id", approveDecision:0,
          faciliy_type:"posts", facility_id:1, reported_user:"none@gmail.com", upUserId:"wuyuheng0525@gmail.com",
          downUserId:"none@gmail.com"})
     expect(res.statusCode).toEqual(404)
    });
});

describe("testing report facility and reject without credit addition", () => {
  it("get//report/admin", async () => {
    const res = await request(app).post("/admin/reportApproval").send({report_type:"facility",report_id:"id", approveDecision:0,
          faciliy_type:"posts", facility_id:1, reported_user:"none@gmail.com", upUserId:"wuyuheng0525@gmail.com",
          downUserId:"none@gmail.com"})
     expect(res.statusCode).toEqual(404)
    });
});


describe("report facility with approve", () => {
  it("get//report/admin", async () => {
    const res = await request(app).post("/admin/reportApproval").send({report_type:6,report_id:"id", approveDecision:1,
    facility_type:"posts", facility_id:1, reported_user:"none@gmail.com", upUserId:"wuyuheng0525@gmail.com",
          downUserId:"none@gmail.com",adminEmail:"wuyuheng0525@gmail.com"})
     expect(res.statusCode).toEqual(200)
    });
});




describe("report reviews with approve", () => {
  it("get//report/admin", async () => {
    const res = await request(app).post("/admin/reportApproval").send({report_type:5,report_id:"id", approveDecision:1,
    facility_type:"posts", facility_id:1, reported_user:"none@gmail.com", upUserId:"wuyuheng0525@gmail.com",
          downUserId:"none@gmail.com",adminEmail:"wuyuheng0525@gmail.com"})
     expect(res.statusCode).toEqual(200)
    });
});






