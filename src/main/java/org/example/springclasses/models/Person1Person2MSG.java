package org.example.springclasses.models;

public class Person1Person2MSG {

    private int idOne;// id кто авторизуется
    private int idTwo;// id кому пишут
    private String email;// кто авторизовался
    private String password;// кто авторизовался
    private String nameOne, nameTwo;

    private String message;// сообщение от 1 к 2-му

    public Person1Person2MSG(){}

    public Person1Person2MSG(int idOne, int idTwo, String email, String password, String nameOne, String nameTwo, String message) {
        this.idOne = idOne;
        this.idTwo = idTwo;
        this.email = email;
        this.password = password;
        this.nameOne = nameOne;
        this.nameTwo = nameTwo;
        this.message = message;
    }

    public int getIdOne() {
        return idOne;
    }

    public void setIdOne(int idOne) {
        this.idOne = idOne;
    }

    public int getIdTwo() {
        return idTwo;
    }

    public void setIdTwo(int idTwo) {
        this.idTwo = idTwo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNameOne() {
        return nameOne;
    }

    public void setNameOne(String nameOne) {
        this.nameOne = nameOne;
    }

    public String getNameTwo() {
        return nameTwo;
    }

    public void setNameTwo(String nameTwo) {
        this.nameTwo = nameTwo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Person1Person2MSG{" +
                "idOne=" + idOne +
                ", idTwo=" + idTwo +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", nameOne='" + nameOne + '\'' +
                ", nameTwo='" + nameTwo + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
