// function routes(app) {
//     app.get('/',(req, res) => {
//       console.log('server is being accessed')
//       res.send('server is active')
//     })
//     app.route('/s')
//     .get(async (req,res,next) => {
//         //   await createAccount(req.query, (err,status,returnData) => {
//         //     if (err) console.log(err);
//         //     res.status(status).json(returnData);
//         //   })
//         res.status(200).send("gooood");
//         return "xxx";
//     });
// }

// module.exports = routes;

// const { Router } = require("express");
// const router2 = new Router();

// router2.get("/", (req, res) => {
//   // res.json({
//   //   "sda": "ddddd"
//   // })
//   res.send({"sda":"ddddd"})
// });

// module.exports = router2;

module.exports = function(app) {
  app.get('/',(req, res) => {
    console.log('server is being accessed')
    res.send('server is active')
  })
}