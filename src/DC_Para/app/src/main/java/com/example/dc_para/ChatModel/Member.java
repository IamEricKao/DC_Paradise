package com.example.dc_para.ChatModel;

public class Member implements java.io.Serializable{
    private String member_no;
    private String member_name;
    private String member_account;
    private String member_password;
    private Integer bonus;
    private String phone;
    private String address;
    private String email;
    private Integer sex;
    private String birthday;
    private Integer member_type;
    private Integer review;
    private Integer status;
    private String bank_account;

    public Member(){

    }

    public Member(String member_no, String member_name, String member_account, String member_password,
                  Integer bonus, String phone, String address, String email, Integer sex, String birthday,
                  Integer member_type, Integer review, Integer status, String bank_account){

            this.member_no= member_no;
            this.member_name = member_name;
            this.member_account = member_account;
            this.member_password = member_password;
            this.bonus = bonus;
            this.phone = phone;
            this.address = address;
            this.email = email;
            this.sex = sex;
            this.birthday = birthday;
            this.member_type = member_type;
            this.review = review;
            this.status = status;
            this.bank_account = bank_account;
    }
    public String getMember_no() {
        return member_no;
    }
    public void setMember_no(String member_no) {
        this.member_no = member_no;
    }
    public String getMember_name() {
        return member_name;
    }
    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }
    public String getMember_account() {
        return member_account;
    }
    public void setMember_account(String member_account) {
        this.member_account = member_account;
    }
    public String getMember_password() {
        return member_password;
    }
    public void setMember_password(String member_password) {
        this.member_password = member_password;
    }
    public Integer getBonus() {
        return bonus;
    }
    public void setBonus(Integer bonus) {
        this.bonus = bonus;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Integer getSex() {
        return sex;
    }
    public void setSex(Integer sex) {
        this.sex = sex;
    }
    public String getBirthday() {
        return birthday;
    }
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    public Integer getMember_type() {
        return member_type;
    }
    public void setMember_type(Integer member_type) {
        this.member_type = member_type;
    }
    public Integer getReview() {
        return review;
    }
    public void setReview(Integer review) {
        this.review = review;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getBank_account() {
        return bank_account;
    }
    public void setBank_account(String bank_account) {
        this.bank_account = bank_account;
    }

}