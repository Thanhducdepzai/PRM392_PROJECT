//package com.example.se1731_houserentailproject_group1.Utils;
//
//import com.twilio.Twilio;
//import com.twilio.rest.api.v2010.account.Message;
//import com.twilio.type.PhoneNumber;
//
//public class SmsSender {
//    public static final String ACCOUNT_SID = "";
//    public static final String AUTH_TOKEN = "";
//
//    public void sendSms(String to, String from, String body) {
//        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
//        Message message = Message.creator(new PhoneNumber(to), new PhoneNumber(from), body).create();
//    }
//}
