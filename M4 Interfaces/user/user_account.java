
interface user_account {
   
    // Example Json for User Account Info
    // {
    // "id" : "123456789",
    // "login_name" : "example",
    // "password" : "example123",
    // "email" : "example@gmail.com",
    // "account_type" : 0, 
    // "account_status" : 0
    // }
    
    // parameter: String user_id,  the user id to be searched in database
    //            String password, the password to be searched in database
    // purpose:   Login in to the app with user_id and password
    // return:    if user_id and password matched in user_db, return true
    //            else return false
    public void verify_user_normal(String user_id, String password);

    // parameter: String gmail, the email address to be search in databases
    // purpose:   Login in to the app with google authorization 
    // return:    if google authorization matched in user_db, return true
    //            else return false
    public void verify_user_google(String gmail);

    // parameter: String id, the user id to be stored in database
    //            String login_name, the login_name to be stored in database
    //            String password, the password to be stored in database
    //            String email, the email to be stored in database
    // purpose:   create a account by sending information to database
    // return:    if user id exist in db, return "user id exist"
    //            if login name exist in db, return "login name exist"
    //            if email exist in db, return "user email exist"
    //            else return "succssfully registed for account"
    public void create_account(String user_id, String login_name, String password, String email);

    //Example Json for user profile 
    // {
    //     "username": "user+user_id",
    //     "user_logo": "https://xxx.xxx.xxx",
    //     "number_of_credit": 0,
    //     "phone_number": "unknown",
    //     "student_id": "unknown",
    //     "program": "unknown",
    //     "gender": "unknown",
    //     "Biography": "unknown",
    //     "number_of_rate": 0,
    //     "number_of_reply": 0,
    //     "number_of_facility": 0
    //   } 

    // parameter: String user_id, the user_id to be search in database and update with new info
    //            String username, new information to be store in database
    //            Int type, the number repersentation wich filed in user profile to be update
    // purpose:   update username stored in database
    // return:    if successfuly change data stored in database, return true
    //            else return false
    public void update_user_profile_username(String user_id, String usernamem, int type);

    // parameter: String user_id, the user_id to be search in database and update with new information
    //            String userLogo_link, new information to be store in database
    //            Int type, the number repersentation wich filed in user profile to be update
    // purpose:   update user infomation stored in database
    // return:    if successfuly change data stored in database, return true
    //            else return false
    public void update_user_profile_userLogo(String user_id, String userLogo_link);  

    // parameter: String user_id, the user_id to be search in database and update with new information
    //            String number_of_credit, new information to be store in database
    //            Int type, the number repersentation wich filed in user profile to be update
    // purpose:   update user infomation stored in database
    // return:    if successfuly change data stored in database, return true
    //            else return false
    public void update_user_profile_credit(String user_id, int number_of_credit);  

    // parameter: String user_id, the user_id to be search in database and update with new information
    //            String phone_number, new information to be store in database
    //            Int type, the number repersentation wich filed in user profile to be update
    // purpose:   update user infomation stored in database
    // return:    if successfuly change data stored in database, return true
    //            else return false
    public void update_user_profile_phone_number(String user_id, String phone_number);  
    
    // parameter: String user_id, the user_id to be search in database and update with new information
    //            String student_id, new information to be store in database
    //            Int type, the number repersentation wich filed in user profile to be update
    // purpose:   update user infomation stored in database
    // return:    if successfuly change data stored in database, return true
    //            else return false
    public void update_user_profile_student_id(String user_id, String student_id);  

    // parameter: String user_id, the user_id to be search in database and update with new information
    //            String program, new information to be store in database
    //            Int type, the number repersentation wich filed in user profile to be update
    // purpose:   update user infomation stored in database
    // return:    if successfuly change data stored in database, return true
    //            else return false
    public void update_user_profile_program(String user_id, String program);  

    // parameter: String user_id, the user_id to be search in database and update with new information
    //            String gender, new information to be store in database
    //            Int type, the number repersentation wich filed in user profile to be update
    // purpose:   update user infomation stored in database
    // return:    if successfuly change data stored in database, return true
    //            else return false
    public void update_user_profile_gender(String user_id, String gender);  

    // parameter: String user_id, the user_id to be search in database and update with new information
    //            String Biography, new information to be store in database
    //            Int type, the number repersentation wich filed in user profile to be update
    // purpose:   update user infomation stored in database
    // return:    if successfuly change data stored in database, return true
    //            else return false
    public void update_user_profile_Biography(String user_id, String Biography);  

    // parameter: String user_id, the user_id to be search in database and update with new information
    //            String number_of_rate, new information to be store in database
    //            Int type, the number repersentation wich filed in user profile to be update
    // purpose:   update user infomation stored in database. which is how many rate the user give out
    // return:    if successfuly change data stored in database, return true
    //            else return false
    public void update_user_profile_number_of_rate(String user_id, String number_of_rate);  

    // parameter: String user_id, the user_id to be search in database and update with new information
    //            String number_of_comment, new information to be store in database
    //            Int type, the number repersentation wich filed in user profile to be update
    // purpose:   update user infomation stored in database, which is how many times a user has made a new review
    // return:    if successfuly change data stored in database, return true
    //            else return false
    public void update_user_profile_number_of_review(String user_id, String number_of_comment);  

    // parameter: String user_id, the user_id to be search in database and update with new information
    //            String number_of_facility, new information to be store in database
    //            Int type, the number repersentation wich filed in user profile to be update
    // purpose:   update user infomation stored in database, which is how many times a user has request adding new facilities
    // return:    if successfuly change data stored in database, return true
    //            else return false
    public void update_user_profile_number_of_facility(String user_id, String number_of_facility);  
}

