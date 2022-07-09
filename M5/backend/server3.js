var express = require('express');
var app = express();

const bodyParser = require('body-parser');
const { application } = require('express');

var util = require('util');
var encoder = new util.TextEncoder('utf-8');
const { MongoClient } = require("mongodb");
const { response } = require("express");
const uri = "mongodb://127.0.0.1:27017"
const client = new MongoClient(uri)

//for accessing other apis
const request = require('request');
app.use(express.json());

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

const OneSignal = require('@onesignal/node-onesignal') ;

const app_key_provider = {
    getToken() {
        return 'MjJiMGQ3ODAtNjIzNC00ZjBkLTgxNGYtMDY0M2YxYWJlNjE0';
    }
};

const configuration = OneSignal.createConfiguration({
    authMethods: {
        app_key: {
        	tokenProvider: app_key_provider
        }
    }
});
const client3 = new OneSignal.DefaultApi(configuration);

app.post('/sendToDevice3', async function(req, res){
    const notification = new OneSignal.Notification();
    notification.app_id = 'f38cdc86-9fb7-40a5-8176-68b4115411da';
    //notification.included_segments = ["test1"];
   
    //notification.email = "test223344@gmail.com";
    //console.log(notification.included_segments)
    notification.contents = {
        en: "Help is the best!"
    };
    
    //notification.channel_for_external_user_ids= "push"
    notification.include_player_ids= ["a01b0e9a-3594-4f7a-9aaa-ce76c924e808"]
    
    const {id} = await client3.createNotification(notification);
    console.log(id)
    res.send("successfully notified");
      
    // const response2 =  await client2.getNotification(ONESIGNAL_APP_ID, id);
})
async function run() {
    try {
        await client.connect();
        console.log(" successfully connect to db");
        var server = app.listen(3000, (req, res) => {
            var host = server.address().address;
            var port = server.address().port;
            console.log("Example app is running" + "with address" + host + "port: " + port);            
        })
    } catch (err) {
        console.log("error")
        await client.close()
    }
}
run();
