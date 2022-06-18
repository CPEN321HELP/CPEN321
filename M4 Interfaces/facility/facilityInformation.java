interface FacilityInformation {
//             Example Json for facilities
//         {
//           "facility_id": "yyyymmdd + type_of_ facility + order",
//           "facility_type": 0,
//           "facility_title": "This is a title",
//           "facility_content": "This is the content of this facility",
//           "facility_image_link": "https://xxx.xxx.xxx",
//           "facility_overall_rate": 0,
//           "rates": {
//             "user_id1": 0,
//             "user_id2": 0,
//             "etc": 0
//           },
//           "reviews": {
//             "user_id1": {
//               "replier_id": "content",
//               "number_of_upvote": "content",
//               "number_of_downvote": "content",
//               "reply_content": "content"
//             },
//             "user_id2": {
//               "replier_id": "content",
//               "number_of_upvote": "content",
//               "number_of_downvote": "content",
//               "reply_content": "content"
//             }
//           },
//         }

    /*  purpose: facility ID getter method
        parameter: facilityProfile is a JSON file that contains the information with the  Example Json for facilities  
        return: the ID of the associated facility
    */
    public String getFacilityID(String facilityProfile);

    /*  purpose: facility ID getter method
        return: the ID of the associated facility:
                1: food and drinks
                2: entertainment
                3: study space
                4: Post (Q&A)
    */
    public int getFacilityType(String facilityProfile);

    /*  purpose: facility name getter method
        parameter:  facilityProfile is a JSON file that contains the information with the  Example Json for facilities  
        return: the name of the associated facility
    */
    public String getFacilityName(String facilityProfile);

    /*  purpose: facility description getter method
        parameter:  facilityProfile is a JSON file that contains the information with the  Example Json for facilities  
        return: the description of the associated facility
    */
    public String getFacilityContent(String facilityProfile);
    
    /*  purpose: facility image link getter method
        parameter:  facilityProfile is a JSON file that contains the information with the Example Json for facilities  
        return: the image link of a facility
    */
    public String getFacilityImageLink(String facilityProfile);

    /*  purpose: facility overall rate getter method
        parameter:  facilityProfile is a JSON file that contains the information with the  Example Json for facilities  
        return: the overall rate of the associated facility
    */
    public double getFacilityRate(String facilityProfile);

    /*  purpose: facility address getter method    
        parameter:  facilityProfile is a JSON file that contains the information with the  Example Json for facilities  
        return: the address of the associated facility
    */
    public String getFacilityAddress(String facilityProfile);
    
    /*  purpose: facility reviews getter method 
        parameter:  facilityProfile is a JSON file that contains the information with the  Example Json for facilities  
        return: the reviews of a facility made by users 
     */
      public String getFacilityReviews(String facilityProfile);

    /*  parameter: the ID of the associated facility
        purpose: facility ID setter method
    */
    public String setFacilityID(String newID);

    /*  parameter: the new type of the associated facility
        purpose: facility type setter method
    */
    public int setFacilityType(int newType);

    /*  parameter: the new name of the associated facility
        purpose: facility name setter method
    */
    public String setFacilityName(String newName);

    /*  parameter: newDescription: the new description of the associated facility
        purpose: facility description setter method
    */
    public String setFacilityDescription(String newDescription);

    /*  parameter: newRate: the new overall rate of the associated facility
        purpose: facility rate setter method
    */
    public void setFacilityRate(double newRate);

    /*  parameter: newAddress: the new address of the associated facility
        purpose: facility address setter method
    */
    public void setFacilityAddress(String newAddress);

    /*  parameter:  ID: facility ID of the facility that wants to be added
                    type: facility type (food and drinks, entertainment, study space, Q&A)
                    name: name or title of the facility
                    description: facility description
                    rate: overall rate of the facility
                    address: address of the facility
        purpose: Add a new facility into the application
    */
    public void addFacility(String ID, int type, String name, String description, double rate, String address);

    /*  parameter: ID: facility ID of the facility that needs to be removed from the application
        purpose: removes a facility from the application and database
    */
    public void removeFacility(String ID);

}
