
interface userInteraction {
    // parameter: A string of keyword input by user
    // purpose: To allow users to search a specific facility by names
    // return: if the facility exists, return the facility JSON file
    // else return null
    public string searchFacilityByName(String keyword);

    // parameter: integer of the id of the post, text is the user input
    // purpose: To allow users to repsond to a post
    // return: nothing
    //
    public void commentOnPost(int postID, String text);

    // parameter: integer of the id of the facility, text is the user input
    // purpose: To allow users to repsond to a facility
    // return: nothing
    //
    public void commentOnFacility(int facilityID, String text);

    // parameter: facilityID is an integer of the id of the comment
    // purpose: Like a comment in facility or post page
    // return: nothing
    //
    public void upVote(int commentID);

    // parameter: integer of the id of the comment
    // purpose: Dislike a comment in facility or post page
    // return: nothing
    //
    public void downVote(int commentID);

    // parameter: integer of the id of the facility
    // purpose: To allow users to rate the facility from star 1 to 5
    // return: nothing
    //
    public void rateTheFacility(int facilityID, int star);

}
