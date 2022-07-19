async function creditCalculation(client , goodUserId, currentadderCredits){
    await client.db("Help!Db").collection("users").updateOne({ _id: goodUserId },
        {
            $set:
            {
                number_of_credit: currentadderCredits,
            }
        }
    );
}

module.exports = creditCalculation;
