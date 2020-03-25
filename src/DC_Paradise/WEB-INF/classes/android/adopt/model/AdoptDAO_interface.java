package android.adopt.model;

import java.util.*;

public interface AdoptDAO_interface {
//          public void insert(AdoptVO adoptVO);
//          public void update(AdoptVO adoptVO);
//          public void delete(String Adopt_Project_No);
//          public AdoptVO findByPrimaryKey(String  Adopt_Project_No);
          AdoptVO findByAdoptProjectNo(String Adopt_Project_No);
          byte[] getImage(String Adopt_Project_No);
          public List<AdoptVO> getAll();          
//          萬用複合查詢(傳入參數型態Map)(回傳 List)
//        public List<EmpVO> getAll(Map<String, String[]> map); 
}
