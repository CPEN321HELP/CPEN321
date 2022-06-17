interface FacilityInformation {
    /*  purpose: facility ID getter method
        return: the ID of the associated facility
    */
    public String getFacilityID();

    /*  purpose: facility ID getter method
        return: the ID of the associated facility:
                1: food and drinks
                2: entertainment
                3: study space
                4: Post (Q&A)
    */
    public int getFacilityType();

    /*  purpose: facility name getter method
        return: the name of the associated facility
    */
    public String getFacilityName();

    /*  purpose: facility description getter method
        return: the description of the associated facility
    */
    public String getFacilityDescription();

    /*  purpose: facility overall rate getter method
        return: the overall rate of the associated facility
    */
    public double getFacilityRate();

    /*  purpose: facility address getter method
        return: the address of the associated facility
    */
    public String getFacilityAddress();

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
