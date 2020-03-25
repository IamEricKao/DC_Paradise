package android.member_info.model;

import java.util.*;

public interface Member_infoDAO_interface {
//		public void insert(Member_infoVO member_infoVO);
//		public void update(Member_infoVO member_infoVO);
//		public void delete(String member_no);
//		public Member_infoVO findByPrimaryKey(String member_no);
//		public List<Member_infoVO> getAll();
		public boolean isUserValid(String member_account, String member_password);
		public Member_infoVO findByMemberNo (String member_account);
		
}