package permit;
/**
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.*;
import java.io.*;
import java.text.*;
import com.opensymphony.xwork2.ModelDriven;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;  
import org.apache.log4j.Logger;

public class InsuranceAction extends TopAction{

		static final long serialVersionUID = 313L;	
		String company_contact_id="";
		String permit_id="";
		static Logger logger = Logger.getLogger(InsuranceAction.class);
		//
		Insurance insurance = null; 
		private List<Type> insurance_companies = null;
		public String execute(){
				String ret = INPUT;
				String back = doPrepare();
				if(!back.equals("")){
						try{
								HttpServletResponse res = ServletActionContext.getResponse();
								String str = url+"Login";
								res.sendRedirect(str);
								return super.execute();
						}catch(Exception ex){
								System.err.println(ex);
						}
				}
				if(action.equals("Save")){
						ret = SUCCESS;
						insurance.setUser_id(user.getId());
						back = insurance.doSave();
						if(!back.equals("")){
								addActionError(back);
						}
						else{
								id = insurance.getId();
								//
								addActionMessage("Saved Successfully");
								if(insurance.hasExpireDate() && !insurance.isExpired()){
										// add schedular here
										ExpireScheduler shed = new ExpireScheduler();
										shed.setType("insurance");
										shed.setExpire_date(insurance.getExpire_date());
										shed.setFire_date(insurance.getFire_date());
										shed.setId(id);
										shed.setUrl(url);
										if(activeMail)
												shed.setActiveMail();										
										try{
												shed.run();
										}catch(Exception ex){
												back += ex;
										}

								}								
						}
				}
				else if(action.equals("Update")){
						ret = SUCCESS;			
						insurance.setUser_id(user.getId());
						back = insurance.doUpdate();
						if(!back.equals("")){
								addActionError(back);
						}
						else{
								addActionMessage("Updated Successfully");
								if(insurance.expireDateChanged()){
										// update schedular if expire date changed
										ExpireScheduler shed = new ExpireScheduler();
										shed.setUrl(url);
										if(activeMail)
												shed.setActiveMail();
										shed.setType("insurance");
										shed.setExpire_date(insurance.getExpire_date());
										shed.setFire_date(insurance.getFire_date());
										shed.setId(insurance.getId());
										shed.setNeedClean();
										try{
												shed.run();
										}catch(Exception ex){
												back += ex;
										}
								}								
						}
				}
				else if(!company_contact_id.equals("") || !permit_id.equals("")){
						getInsurance();
				}		
				else if(!id.equals("")){
						ret = populate();
				}
				//
				return ret;
		}

		public Insurance getInsurance(){ 
				if(insurance == null){
						insurance = new Insurance();
						if(!company_contact_id.equals("")){
								insurance.setCompany_contact_id(company_contact_id);
						}
						if(!permit_id.equals("")){
								insurance.setPermit_id(permit_id);
						}
				}		
				return insurance;
		}
		//
		public List<Type> getInsurance_companies(){
				if(insurance_companies == null){
						TypeList tl = new TypeList("bond_companies");
						String back = tl.find();
						if(back.equals("")){
								insurance_companies = tl.getTypes();
						}
				}
				return insurance_companies;
		}
		public void setInsurance(Insurance val){
				if(val != null)
						insurance = val;
		}

		public String getId(){
				if(id.equals("") && insurance != null){
						id = insurance.getId();
				}
				return id;
		}
		public String getCompany_contact_id(){
				return company_contact_id;
		}
		public void setCompany_contact_id(String val){
				company_contact_id = val;
		}	

		public String getInsurancesTitle(){
				return "Most recent insurances";
		}

		public void setPermit_id(String val){
				if(val != null)
						permit_id = val;
		}
		public String populate(){
				String ret = SUCCESS;
				if(!id.equals("")){
						insurance = new Insurance(id);
						String back = insurance.doSelect();
						if(!back.equals("")){
								addActionError(back);
						}
				}
				return ret;
		}

}





































