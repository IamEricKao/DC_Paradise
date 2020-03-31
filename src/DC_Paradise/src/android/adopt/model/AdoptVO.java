package android.adopt.model;
import java.sql.Clob;
import java.sql.Date;

public class AdoptVO implements java.io.Serializable{

	private String  Adopt_Project_No;
	private String  Founder_No; 
	private String  Adopter_No;
	private String  Adopt_Project_Name;
	private Integer Pet_Category;
	private String  Adopt_Content;
	private Integer Adopt_Status;
	private String  Adopt_Result;
	private Integer Sex;
	private String  Age;
	private String  Breed;
	private Integer Chip;
	private Integer Birth_Control;
	private String  Founder_Location;
	private String  Adopt_Pic_No;
	
	
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
	public String getAdopt_Project_Name() {
		return Adopt_Project_Name;
	}
	public void setAdopt_Project_Name(String adopt_Project_Name) {
		Adopt_Project_Name = adopt_Project_Name;
	}
	public Integer getPet_Category() {
		return Pet_Category;
	}
	public void setPet_Category(Integer pet_Category) {
		Pet_Category = pet_Category;
	}
	public String getAdopt_Content() {
		return Adopt_Content;
	}
	public void setAdopt_Content(String adopt_Content) {
		Adopt_Content = adopt_Content;
	}
	public Integer getAdopt_Status() {
		return Adopt_Status;
	}
	public void setAdopt_Status(Integer adopt_Status) {
		Adopt_Status = adopt_Status;
	}
	public String getAdopt_Result() {
		return Adopt_Result;
	}
	public void setAdopt_Result(String adopt_Result) {
		Adopt_Result = adopt_Result;
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
	public String getBreed() {
		return Breed;
	}
	public void setBreed(String breed) {
		Breed = breed;
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
}