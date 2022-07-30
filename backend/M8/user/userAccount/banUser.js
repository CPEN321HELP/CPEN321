async function banUser(client, id){
    await client.db("Help!Db").collection("users").updateOne({ _id: id },
        {
            $set:
            {
                account_status: 1,
            }
        }
    );
}
module.exports = banUser;