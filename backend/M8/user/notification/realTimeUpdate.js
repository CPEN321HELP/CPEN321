const TypeSelection = require("/home/azureuser/Test 1/facility/FacilityDisplay/typeSelection.js")
require ("dotenv").config();
console.log(process.env.ONESIGNAL_ID);

const OneSignal = require('@onesignal/node-onesignal') ;

const app_key_provider = {
    getToken() {
        return process.env.ONESIGNAL_ID;
    }
};

const configuration = OneSignal.createConfiguration({
    authMethods: {app_key: {tokenProvider: app_key_provider}}
});

const client3 = new OneSignal.DefaultApi(configuration);

async function realTimeUpdate(reportMessage, notificationType,  gmails , facilityId, type, length){
    const a = new TypeSelection;
    //var gmails = ["l2542293790@gmail.com", "xyjyeducation@gmail.com"];
    console.log("gmails are :")
    console.log(gmails);
    if(length === 0 || gmails == null){
        return;
    }
    
    console.log(notificationType);
    type = a.typeSelection(type);

    switch (notificationType) {
        case 0:
            notificationType = "You have an review on "   + type + " , with facility id: " + facilityId+".";
            break;
        case 1:
            notificationType = "your reported comment is approved"+ type + "with facility number:" + facilityId;
            break;
        case 2: //
            notificationType = "your reported facility is addressed"+ type + "with facility number:" +facilityId;
            break;
        case 3: // 
            notificationType = "a new facility has been added to the app"+ type + "with facility number:" +facilityId;
            break;
        case 4: // vote
            notificationType = "you received an upvote"+ type + "with facility number:" +facilityId;
            break;
        case 5: // vote
            notificationType = "you received an downvote"+ type + "with facility number:" +facilityId;
            break;
        case 6:
            notificationType = reportMessage;
            break;
         case 7:
            notificationType = reportMessage;
            break;

    }

    const notification = new OneSignal.Notification();
    notification.app_id = 'f38cdc86-9fb7-40a5-8176-68b4115411da';
    notification.contents = {
        en: notificationType
    }
    notification.channel_for_external_user_ids = "push"
    notification.include_external_user_ids = []
    for(var i = 0 ; i < length; i++){
        notification.include_external_user_ids.push(gmails[i]);
    }
    
    const {id} = await client3.createNotification(notification);
    console.log(id)
}

module.exports = realTimeUpdate;
