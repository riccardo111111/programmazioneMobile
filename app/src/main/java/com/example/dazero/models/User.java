package com.example.dazero.models;

public class User {private int id;
    private String name;
    private String mail;
    private String surname;
    private String password;


    public User(int id,
                 String name,
                  String surname,
                 String mail,
                 String password) {
        this.setId(id);
        this.setName(name);
        this.setPassword(password);
        this.setSurname(surname);
        this.setMail(mail);

    }

    public  int getId() {
        return this.id;
    }


    public  String getName() {
        return this.name;
    }

    public  String getMail() {
        return this.mail;
    }

    public  String getSurname() {
        return this.surname;
    }

    public  String getPassword() {
        return this.password;
    }

    public void setId(int id) {
        this.id=id;
    }

    public void setName(String name) {
        this.name=name;
    }

    public void setMail(String mail) {
        this.mail=mail;
    }

    public void setSurname(String surname) {
        this.surname=surname;
    }
    public void setPassword(String password) {
        this.password=password;
    }
}

