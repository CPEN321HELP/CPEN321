interface userInteraction {

    // parameter: A string of keyword input by user
    // purpose: To allow users to search a specific facility by names
    // return: if facilities conatining the keyword exists, return the facilities
    // JSON file
    // else return null
    public string searchFacilityByName(String keyword);

    // parameter: integer of the id of the facility, text is the user input,
    // facilityType is the bottom catogry bar which user can select types on the
    // main page, all user who has comment on this post will get real-time
    // notification.
    // purpose: Users defined by userID can repsond comments in string to a facility
    // defined by facilityID. Call the notifyUser, and specify the notification type
    // is a post reply
    // return: if success, return: a string indicates that users have successfully
    // commented
    // else return a warning message

    public void commentOnFacility(String facilityID, String text, String userID, int facilityType);

    // parameter: facilityID is an integer of the id of the comment
    // purpose: Users defined by senderID can like a comment which is associated
    // with a receiverID in the facility page or the post page which are defined by
    // facilityID, user who comment will get noficied when recieve a up vote.
    // Call the notifyUser, and specify the notification type is a upvote.
    // return: if success, return: a string indicates that users have successfully
    // up voted the comment
    // else return a warning message

    public void upVote(String senderID, String facilityID, String receiverID);

    // parameter: integer of the id of the comment
    // purpose: Users defined by senderID can dislike a comment which is associated
    // with a receiverID in the facility page or the post page which are defined by
    // facilityID, user who comment will get noficied when recieve a down vote.
    // Call the notifyUser, and specify the notification type is a downvote.
    // return: if success, return: a string indicates that users have successfully
    // down voted the comment
    // else return a warning message
    public void downVote(String senderID, String facilityID, String receiverID);

    // parameter:
    // purpose: To allow users who are defined by userID to rate the facility which
    // is defined by facilityID from star 1 to 5
    // return: if success, return: a string indicates that users have successfully
    // rated
    // else return a warning message
    public void rateTheFacility(String facilityID, int star, String userID);

    // purpose: User can select a type of facility on the main page and the most
    // recent added facilities would be listed first and so forth
    // return: if success, return a JSON file with all the facilites
    // else return a warning message
    public void getMostRecentFacility(String facilityType);

    // purpose: notify the user with a message either saying their posts are replied
    // and their comments are downvoted or upvoted
    // return: if success, return true
    // else trun a warning message
    public void notifyUser(String userID, int meesageType);
}
