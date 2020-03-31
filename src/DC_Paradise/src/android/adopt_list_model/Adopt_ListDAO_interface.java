package android.adopt_list_model;

import java.util.*;

public interface Adopt_ListDAO_interface {
	boolean add(Adopt_ListVO adopt_listVO);
	boolean update(Adopt_ListVO adopt_listVO);
	public List<Adopt_ListVO> findBy_Adopt_Project_No(String Adopt_Project_No);
}
