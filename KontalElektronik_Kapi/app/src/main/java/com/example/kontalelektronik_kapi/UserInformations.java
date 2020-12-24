package com.example.kontalelektronik_kapi;

import java.io.Serializable;

public class UserInformations implements Serializable {

    private String message;
    private String pNum;
    private String password;
    private String name;
    private String surname;
    private String role;
    private String permission;

    public UserInformations(String message, String pNum, String password) {
        this.message = message;
        this.pNum = pNum;
        this.password = password;
    }

    public UserInformations(String message, String pNum, String password, String name, String surname, String role, String permission) {
        this.message = message;
        this.pNum = pNum;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.role = role;
        this.permission = permission;
    }

    public UserInformations(String message, String pNum) {
        this.message = message;
        this.pNum = pNum;
    }

    public String getpNum() { return pNum; }
    public String getPassword() { return password; }
    public String getMessage() { return message; }
    public String getName() { return name; }
    public String getSurname() { return surname; }
    public String getRole() { return role; }
    public String getPermission() { return permission; }
}
