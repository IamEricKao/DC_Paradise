package android.member_info.model;
import java.sql.Date;

public class Member_infoVO implements java.io.Serializable{
	private String member_no;
	private String member_name;
	private String member_account;
	private String member_password;
	private Integer bonus;
	private String phone;
	private String address;
	private Integer sex;
	private Date birthday;
	private byte[] member_pic;
	private Integer member_type;
	private Integer status;
	private byte[] file_content;
	private String bank_account;
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
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public byte[] getMember_pic() {
		return member_pic;
	}
	public void setMember_pic(byte[] member_pic) {
		this.member_pic = member_pic;
	}
	public Integer getMember_type() {
		return member_type;
	}
	public void setMember_type(Integer member_type) {
		this.member_type = member_type;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public byte[] getFile_content() {
		return file_content;
	}
	public void setFile_content(byte[] file_content) {
		this.file_content = file_content;
	}
	public String getBank_account() {
		return bank_account;
	}
	public void setBank_account(String bank_account) {
		this.bank_account = bank_account;
	}

}