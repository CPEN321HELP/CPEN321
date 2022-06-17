interface user_report {
    // Json sample for report
    // {
    //     "report_type ": 0, (0 for report_user, 1 for report_comment, 2 for report_facility)
    //     "reporter" : "user_id",
    //     "who_to_report": "user_id",
    //     "facility_id" : "facility_id",
    //     "reason" : "why reporting"
    //   }
      
    // parameter: String reporter_user_id, the user id of user who is reporting
    //            String reproted_user_id, the user id of user who is being report
    //            String reason_to_report, why user reports another user
    // purpose:   to report a user that has inappropriate behaviour 
    // return:    if report is successfuly recieved by server, return true
    //            else report is not successfuly recieved by server, return fale
    public void report_user(String reporter_user_id, String reproted_user_id, String reason_to_report);

    // parameter: String reporter_user_id, the user id of user who is reporting
    //            String reproted_user_id, the user id of user who is being report
    //            String facility_id, the facility that has the reported comment
    //            String reason_to_report, why user reports a comment    
    // purpose:   to report a user that has inappropriate behaviour 
    // return:    if report is successfuly recieved by server, return true
    //            else report is not successfuly recieved by server, return fale
    public void report_comment(String reporter_user_id, String reproted_user_id, String facility_id, String reason_to_report);

    // parameter: String reporter_user_id, the user id of user who is reporting 
    //            String facility_id, the facility is being report
    //            String reason_to_report, why user reports a facility   
    // purpose:   to report a user that has inappropriate behaviour 
    // return:    if report is successfuly recieved by server, return true
    //            else report is not successfuly recieved by server, return fale
    public void report_facility(String reporter_user_id, String facility_id, String reason_to_report);
}