interface CreditCalculator {
    /*  
        parameter:  userID: the user ID of the user who will be granted credit
                    amount: the amount of credit to add to the user
        purpose:    To add credit for an registered user of the application for reviewing and commenting
        return:     True: if credits are successfully added to the user's account
                    False: if credits were unsuccessfully added due to an error
    */
    public boolean addCredit(String userID, int amount);

    /* 
        parameter:  userID: the user ID of the user who will be granted credit
                    amount: the amount of credit to add to the user
        purpose:    To remove credit for an registered user of the application for inappropriate behaviour 
        return:     True: if credits were successfully removed from the user's account
                    False: if credits were unsuccessfully removed due to an error
    */
    public boolean removeCredit(String userID, int amount);

    /* 
        parameter:  userID: ID of an registered user
        purpose:    To show the history of an user's credit transactions
        return:     True: if the history is successfully obtained
                    False: if the history is unsuccessfully obtained due to an error
    */
    public boolean requestCreditHistory(String userID);
    
}
