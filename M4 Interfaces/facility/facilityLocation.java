interface FacilityLocation {
//         Example Json for facilities
//         {
//           "facility_id": "yyyymmdd + type_of_ facility + order",
//           "facility_type": 0,
//           "facility_title": "This is a title",
//           "facility_content": "This is the content of this facility",
//           "facility_image_link": "https://xxx.xxx.xxx",
//           "facility_overall_rate": 0,
//           "latitude" : 123
//           "Longitude" : 123
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
    /*  purpose: facility latitude getter method
        parameter: facilityProfile is a JSON file that contains the information with the  Example Json for facilities  
        return: the latitude of the associated facility 
    */
    public double getLatitude(String facilityProfile);

    /*  purpose: facility longitude getter method
        parameter: facilityProfile is a JSON file that contains the information with the  Example Json for facilities  
        return: the longitude of the associated facility 
    */
    public double getLongitude(String facilityProfile);

    /*  parameter:  latitude: latitude of the facility
                    longitude: longitude of the facility
                    googleMap: GoogleMap object instance for displaying the location of the facility
        purose: display and locate the facility on Google Maps using Google Maps API
        return: the location of the facility on map
    */
    public void locateFacilityOnMap(double latitude, double longitude, GoogleMap googleMap);

}
