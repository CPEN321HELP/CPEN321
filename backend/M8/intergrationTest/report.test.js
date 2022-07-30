const express = require("express"); // import express
const app = express(); //an instance of an express app, a 'fake' express app
const request = require('supertest');
const server  = require('../server5.js');

// app.use("/user/Report/commentAndfacility", server); //routes
// describe("testing userside report comment or facility", () => {
//   it("POST/user/Report/commentAndfacility", async () => {
//     const res = await request(app).post("/user/Report/commentAndfacility")
//     .send({"reportedFacilityType":"entertainments",
//             "reporterID":"wuyuheng0525@gmail.com",
//             "reportReason":"reason",
//             "reported_id":"12345667@gmail.com",
//             "report_type":"facility",
//             "reportUserCond":1})
  
//     expect(res.body).toEqual({result:{"reportedFacilityType":"entertainments",
//     "reporterID":"wuyuheng0525@gmail.com",
//     "reportReason":"reason",
//     "reported_id":"12345667@gmail.com",
//     "report_type":"facility",
//     "reportUserCond":1}})

//     expect(res.statusCode).toEqual(200)
//    });
   

// });

// describe("user report their own comment", () => {
//   it("POST/user/Report/commentAndfacility", async () => {
//     const res = await request(app).post("/user/Report/commentAndfacility")
//     .send({"reportedFacilityType":"entertainments",
//             "reporterID":"wuyuheng0525@gmail.com",
//             "reportReason":"reason",
//             "reported_id":"wuyuheng0525@gmail.com",
//             "report_type":"facility",
//             "reportUserCond":1})
  
//     expect(res.body).toEqual({result:"user is reporting themselevs"})
//     expect(res.statusCode).toEqual(200)
//    });
// });


// app.use("/report/admin", server); //routes
// describe("testing displaying zero report for admin", () => {
//   it("get//report/admin", async () => {
//     //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
//     const res = await request(app).post("/report/admin").send([])
//      expect(res.statusCode).toEqual(200)
//     });
// });

// app.use("/report/admin", server); //routes
// describe("testing displaying one report for admin", () => {
//   it("get//report/admin", async () => {
//     //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
//     const res = await request(app).post("/report/admin").send([{
//       reporterID: "id1",
//       reportReason: "reason1",
//       reportedUSer: "reporteduser1",
//       reportType: "type1",
//       reportUserCond: 1
//      }])
//      expect(res.statusCode).toEqual(200)
//     });
// });

// app.use("/report/admin", server); //routes
// describe("testing displaying two or more reports for admin", () => {
//   it("get//report/admin", async () => {
//     //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
//     const res = await request(app).post("/report/admin").send([{
//       reporterID: "id1",
//       reportReason: "reason1",
//       reportedUSer: "reporteduser1",
//       reportType: "type1",
//       reportUserCond: 1
//      },
//      {
//       reporterID: "id2",
//       reportReason: "reason2",
//       reportedUSer: "reporteduser2",
//       reportType: "type2",
//       reportUserCond: 1
//      }])
//      expect(res.statusCode).toEqual(200)
//     });
// });


// report_type
// var reportID = req.body.report_id;
// var approveDecision = req.body.approve;
// var facility_type = req.body.facility_type;
// var reportedFacilityid = parseInt(req.body.facility_id);
// var reportedID = req.body.reported_user;
// var upUser = req.body.upUserId;
// var downUSer = req.body.downUserId;

// app.use("/admin/reportApproval", server); //routes
// describe("testing report facility and approve with credit addition", () => {
//   it("get//report/adminapprove1", async () => {
//     //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
//     const res = await request(app).post("/admin/reportApproval").send({report_type:"facility",report_id:"id", approveDecision:1,
//           faciliy_type:"posts", facility_id:1, reported_user:"none@gmail.com", upUserId:"wuyuheng0525@gmail.com",
//           downUserId:"none@gmail.com"})
//      expect(res.statusCode).toEqual(200)
//     });
// });

// describe("testing report facility and reject without credit addition", () => {
//   it("get//report/admin", async () => {
//     //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
//     const res = await request(app).post("/admin/reportApproval").send({report_type:"facility",report_id:"id", approveDecision:0,
//           faciliy_type:"posts", facility_id:1, reported_user:"none@gmail.com", upUserId:"wuyuheng0525@gmail.com",
//           downUserId:"none@gmail.com"})
//      expect(res.statusCode).toEqual(404)
//     });
// });



// describe("testing report review and reject without credit adjustment", () => {
//   it("get//report/adminapprove2", async () => {
//     //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
//     const res = await request(app).post("/report/admin").send({report_type:"comment",report_id:"id", approveDecision:0,
//           faciliy_type:"", facility_id:"", reported_user:"wuyuheng0525@gmail.com", upUserId:"wuyuheng0525@gmail.com",
//           downUserId:"wuyuheng0525@gmail.com"})
//      expect(res.statusCode).toEqual(404)
//     });
// });


// describe("testing report review and approve with credit adjustment", () => {
//   it("get//report/adminapprove2", async () => {
//     //const { body } = await request(app).post("/specific").send({"facilityType":"1" , "facility_id" : "1"})
//     const res = await request(app).post("/report/admin").send({report_type:"comment",report_id:"id", approveDecision:1,
//           faciliy_type:"", facility_id:"", reported_user:"wuyuheng0525@gmail.com", upUserId:"wuyuheng0525@gmail.com",
//           downUserId:"wuyuheng0525@gmail.com"})
//      expect(res.statusCode).toEqual(404)
//     });
// });


