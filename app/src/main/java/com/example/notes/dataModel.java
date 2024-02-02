package com.example.notes;

public class dataModel {
    private String Name,Email,Pass,Number;

    public dataModel(String name, String email, String pass, String number) {
        Name = name;
        Email = email;
        Pass = pass;
        Number = number;
    }

    public dataModel() {
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPass() {
        return Pass;
    }

    public void setPass(String pass) {
        Pass = pass;
    }


    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }



}


