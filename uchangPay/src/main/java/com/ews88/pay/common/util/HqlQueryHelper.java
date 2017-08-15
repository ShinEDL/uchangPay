package com.ews88.pay.common.util;

import java.util.ArrayList;
import java.util.List;

public class HqlQueryHelper {
	
	private String fromClause = "";
	private String whereClause = "";
	private String orderByClause = "";
	private List<Object> parameters;
	
	public static String ORDER_BY_ASC = "ASC";
	public static String ORDER_BY_DESC = "DESC";
	
	public HqlQueryHelper(Class<?> clazz, String alias){
		fromClause = "FROM " + clazz.getSimpleName() + " " + alias;
	}
	
	public void addCondition(String condition, Object... params){
		if(whereClause.length() > 0){
			whereClause += " AND " + condition ;
		} else {
			whereClause += " WHERE " + condition ;
		}
		if(params != null){
			if(parameters == null){
				parameters = new ArrayList<Object>();
			}
			for(Object obj: params){
				parameters.add(obj);
			}
		}
	}
	
	public void addOrderByProperty(String property, String order){
		if(orderByClause.length() > 0){
			orderByClause += "," + property + " " + order ;
		} else {
			orderByClause = " ORDER BY " + property + " " + order ;
		}
	}
	
	public String getListQueryHql(){
		return fromClause + whereClause + orderByClause;
	}
	
	public String getCountQueryHql(){
		return "SELECT COUNT(*) " + fromClause + whereClause;
	}
	
    public List<Object> getParameters(){
    	return parameters;
    }
	
}
