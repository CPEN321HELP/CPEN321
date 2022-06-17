interface FacilityLocation {
    /*  purpose: facility latitude getter method
        return: the latitude of the associated facility 
    */
    public double getLatitude();

    /*  purpose: facility longitude getter method
        return: the longitude of the associated facility 
    */
    public double getLongitude();

    /*  parameter: latitude: the new latitude of the associated facility
        purpose: facility latitude setter method
    */
    public void setLatitude(double latitude);

    /*  parameter: longitude: the new longitude of the associated facility
        purose: facility longitude setter method
    */
    public void setLongitude(double longitude);

    /*  parameter:  latitude: latitude of the facility
                    longitude: longitude of the facility
                    googleMap: GoogleMap object instance for displaying the location of the facility
        purose: display and locate the facility on Google Maps using Google Maps API
    */
    public void locateFacilityOnMap(double latitude, double longitude, GoogleMap googleMap);

}
