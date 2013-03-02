package com.adk;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.adk.dao.ClickDAO;
import com.adk.dao.hibernate.ClickHibernateDAO;
import com.adk.model.Click;

public class Service {

	public Integer clickIt(){
		Click click = new Click();
		Date date = new Date();
		click.setCreatedAt(date);
		
		ClickDAO clickDao = new ClickHibernateDAO();
		
		try{
			clickDao.save(click);
			

			//get # of clicks
			List<Click> clicks = new ArrayList<Click>();
			
			clicks = clickDao.findAll();
			
			return clicks.size();
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
		
		return 0;
	}
	
	public Integer getTotalClicks(){
		ClickDAO clickDao = new ClickHibernateDAO();
		
		List<Click> clicks = new ArrayList<Click>();
		
		clicks = clickDao.findAll();
		
		return clicks.size();
	}
}
