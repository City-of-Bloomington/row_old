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

public class BondAction extends TopAction{

		static final long serialVersionUID = 313L;	
		String company_contact_id="";
		String permit_id="";
		static Logger logger = Logger.getLogger(BondAction.class);
		//
		Bond bond = null; 
		public List<Type> bond_companies = null;
		public List<Type> bond_types = null;
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
						bond.setUser_id(user.getId());
						back = bond.doSave();
						if(!back.equals("")){
								addActionError(back);
						}
						else{
								id = bond.getId();
								if(bond.hasExpireDate() && !bond.isExpired()){
										// add schedular here
										ExpireScheduler shed = new ExpireScheduler();
										shed.setType("bond");
										shed.setUrl(url);
										if(activeMail)
												shed.setActiveMail();
										shed.setExpire_date(bond.getExpire_date());
										shed.setFire_date(bond.getFire_date());
										shed.setId(id);
										try{
												shed.run();
										}catch(Exception ex){
												back += ex;
												System.err.println(ex);
										}
								}
								if(!back.equals("")){
										addActionError(back);
								}
								else{
										addActionMessage("Saved Successfully");
								}
						}
				}
				else if(action.equals("Update")){
						ret = SUCCESS;			
						bond.setUser_id(user.getId());
						back = bond.doUpdate();
						if(!back.equals("")){
								addActionError(back);
						}
						else{
								addActionMessage("Updated Successfully");
								if(bond.expireDateChanged()){
										// update schedular if expire date changed
										ExpireScheduler shed = new ExpireScheduler();
										shed.setUrl(url);
										if(activeMail)
												shed.setActiveMail();
										shed.setType("bond");
										shed.setExpire_date(bond.getExpire_date());
										shed.setFire_date(bond.getFire_date());
										shed.setId(bond.getId());
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
						getBond();
				}		
				else if(!id.equals("")){
						ret = populate();
				}
				//
				return ret;
		}

		public Bond getBond(){ 
				if(bond == null){
						bond = new Bond();
						if(!company_contact_id.equals("")){
								bond.setCompany_contact_id(company_contact_id);
						}
						if(!permit_id.equals("")){
								bond.setPermit_id(permit_id);
						}
				}		
				return bond;
		}
		//
		public List<Type> getBond_companies(){
				if(bond_companies == null){
						TypeList tl = new TypeList("bond_companies");
						String back = tl.find();
						if(back.equals("")){
								bond_companies = tl.getTypes();
						}
				}
				return bond_companies;
		}
		public List<Type> getBond_types(){
				if(bond_types == null){
						TypeList tl = new TypeList("bond_types");
						String back = tl.find();
						if(back.equals("")){
								bond_types = tl.getTypes();
						}
				}
				return bond_types;
		}		
		public void setBond(Bond val){
				if(val != null)
						bond = val;
		}

		public String getId(){
				if(id.equals("") && bond != null){
						id = bond.getId();
				}
				return id;
		}
		public String getCompany_contact_id(){
				return company_contact_id;
		}
		public void setCompany_contact_id(String val){
				company_contact_id = val;
		}	

		public String getBondsTitle(){
				return "Most recent bonds";
		}

		public void setPermit_id(String val){
				if(val != null)
						permit_id = val;
		}
		public String populate(){
				String ret = SUCCESS;
				if(!id.equals("")){
						bond = new Bond(id);
						String back = bond.doSelect();
						if(!back.equals("")){
								addActionError(back);
						}
				}
				return ret;
		}

}





































