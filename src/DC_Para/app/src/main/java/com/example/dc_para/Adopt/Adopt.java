package com.example.dc_para.Adopt;

import java.io.Serializable;

public class Adopt implements Serializable {

    private String Adopt_Project_No;
    private String Adopt_Project_Name;
    private String Breed;
    private String Founder_No;
    private String Adopter_No;
    private int Pet_Category;
    private int Adopt_Status;
    private int Sex;
    private String Age;
    private int Chip;
    private int Birth_Control;
    private String Founder_Location;
    private String Adopt_Pic_No;

    public Adopt() {

    }

    public Adopt(String Adopt_Project_No, String Adopt_Project_Name, String Breed, String Founder_No,
                 String Adopter_No, int Pet_Category, int Adopt_Status, int Sex, String Age, int Chip,
                 int Birth_Control, String Founder_Location, String Adopt_Pic_No)
    {
        this.Adopt_Project_No = Adopt_Project_No;
        this.Adopt_Project_Name = Adopt_Project_Name;
        this.Breed = Breed;
        this.Founder_No = Founder_No;
        this.Adopter_No = Adopter_No;
        this.Pet_Category= Pet_Category;
        this.Adopt_Status = Adopt_Status;
        this.Sex = Sex;
        this.Age = Age;
        this.Chip = Chip;
        this.Birth_Control = Birth_Control;
        this.Founder_Location = Founder_Location;
        this.Adopt_Pic_No = Adopt_Pic_No;
    }

    public String getFounder_No() {
        return Founder_No;
    }

    public void setFounder_No(String founder_No) {
        Founder_No = founder_No;
    }

    public String getAdopter_No() {
        return Adopter_No;
    }

    public void setAdopter_No(String adopter_No) {
        Adopter_No = adopter_No;
    }

    public Integer getPet_Category() {
        return Pet_Category;
    }

    public void setPet_Category(Integer pet_Category) {
        Pet_Category = pet_Category;
    }

    public Integer getAdopt_Status() {
        return Adopt_Status;
    }

    public void setAdopt_Status(Integer adopt_Status) {
        Adopt_Status = adopt_Status;
    }

    public Integer getSex() {
        return Sex;
    }

    public void setSex(Integer sex) {
        Sex = sex;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public Integer getChip() {
        return Chip;
    }

    public void setChip(Integer chip) {
        Chip = chip;
    }

    public Integer getBirth_Control() {
        return Birth_Control;
    }

    public void setBirth_Control(Integer birth_Control) {
        Birth_Control = birth_Control;
    }

    public String getFounder_Location() {
        return Founder_Location;
    }

    public void setFounder_Location(String founder_Location) {
        Founder_Location = founder_Location;
    }

    public String getAdopt_Pic_No() {
        return Adopt_Pic_No;
    }

    public void setAdopt_Pic_No(String adopt_Pic_No) {
        Adopt_Pic_No = adopt_Pic_No;
    }

    public String getAdopt_Project_No() {
        return Adopt_Project_No;
    }

    public void setAdopt_Project_No(String adopt_Project_No) {
        Adopt_Project_No = adopt_Project_No;
    }


    public String getAdopt_Project_Name() {
        return Adopt_Project_Name;
    }

    public void setAdopt_Project_Name(String adopt_Project_Name) {
        Adopt_Project_Name = adopt_Project_Name;
    }


    public String getBreed() {
        return Breed;
    }

    public void setBreed(String breed) {
        Breed = breed;
    }
}
