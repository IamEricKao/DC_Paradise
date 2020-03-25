package com.example.dc_para.AdoptForm;

import java.io.Serializable;
import java.sql.Date;

public class Adopt_List implements Serializable {

    private String  Adopt_List_No;
    private String  Adopt_Project_No;
    private String  Adopter_No;
    private String  Real_Name;
    private String 	Phone;
    private int     Age;
    private String  ID_Card;
    private String  Address;
    private String  Email;
    private int     Sex;
    private Date    Date_Of_Application;
    private int     Status;

    public Adopt_List(){
        super();
    }

    public Adopt_List(String  Adopt_List_No, String  Adopt_Project_No, String  Adopter_No, String  Real_Name,
                      String Phone, int Age, String  ID_Card, String  Address, String  Email, int  Sex,
                      Date Date_Of_Application, int Status){

        this.Adopt_List_No = Adopt_List_No;
        this.Adopt_Project_No = Adopt_Project_No;
        this.Adopter_No = Adopter_No;
        this.Real_Name = Real_Name;
        this.Phone = Phone;
        this.Age = Age;
        this.ID_Card = ID_Card;
        this.Address = Address;
        this.Email = Email;
        this.Sex = Sex;
        this.Date_Of_Application = Date_Of_Application;
        this.Status = Status;
    }

    public String getAdopt_List_No() {
        return Adopt_List_No;
    }

    public void setAdopt_List_No(String adopt_List_No) {
        Adopt_List_No = adopt_List_No;
    }

    public String getAdopt_Project_No() {
        return Adopt_Project_No;
    }
    public void setAdopt_Project_No(String adopt_Project_No) {
        Adopt_Project_No = adopt_Project_No;
    }
    public String getAdopter_No() {
        return Adopter_No;
    }
    public void setAdopter_No(String adopter_No) {
        Adopter_No = adopter_No;
    }
    public String getReal_Name() {
        return Real_Name;
    }
    public void setReal_Name(String real_Name) {
        Real_Name = real_Name;
    }
    public String getPhone() {
        return Phone;
    }
    public void setPhone(String phone) {
        Phone = phone;
    }
    public int getAge() {
        return Age;
    }
    public void setAge(int age) {
        Age = age;
    }
    public String getID_Card() {
        return ID_Card;
    }
    public void setID_Card(String iD_Card) {
        ID_Card = iD_Card;
    }
    public String getAddress() {
        return Address;
    }
    public void setAddress(String address) {
        Address = address;
    }
    public String getEmail() {
        return Email;
    }
    public void setEmail(String email) {
        Email = email;
    }
    public int getSex() {
        return Sex;
    }
    public void setSex(int sex) {
        Sex = sex;
    }
    public Date getDate_Of_Application() {
        return Date_Of_Application;
    }
    public void setDate_Of_Application(Date date_Of_Application) {
        Date_Of_Application = date_Of_Application;
    }
    public int getStatus() {
        return Status;
    }
    public void setStatus(int status) {
        Status = status;
    }




}
