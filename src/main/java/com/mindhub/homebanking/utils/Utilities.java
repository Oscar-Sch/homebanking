package com.mindhub.homebanking.utils;


import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.repositories.CardRepository;

import java.util.regex.Pattern;

public class Utilities {

    static private String cardNumberGenerator(){
        String finalNum="";
        for (byte i=0; i<4;i++){
            finalNum += String.format("%04d", (int) Math.floor(Math.random() * 9999));
            if (i!=3){
                finalNum+="-";
            }
        }
        return finalNum;
    }
    static public String handleCardNumberGeneration(CardRepository cardRepository){
        String number;
        do{
            number= cardNumberGenerator();
        }while(cardRepository.existsCardByNumber(number));
        return number;
    }
    static public String cardCvvGenerator(){
        return String.format("%03d", (int) Math.floor(Math.random() * 999));
    }
    static public String accountNumberGenerator(Account account){
        return "VIN-"+String.format("%08d",68721873-account.getId());
    }
    static public Boolean emailValidation(String email){
        Pattern pattern = Pattern.compile("^[\\w!#$%&amp;'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&amp;'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
        return pattern.matcher(email).matches();
    }
    static public Boolean userNameValidation(String userName){
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$");
        return pattern.matcher(userName).matches();
    }
    static public Boolean phoneNumberValidation(String phoneNumber){
        Pattern pattern = Pattern.compile("\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}");
        return pattern.matcher(phoneNumber).matches();
    }
}
