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

public class RescheduleAction extends TopAction{

		static final long serialVersionUID = 313L;	
		static Logger logger = Logger.getLogger(RescheduleAction.class);
		Reschedule reschedule = null;
		List<Bond> bonds = null;
		List<Insurance> insurances = null;
		//
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
				if(action.equals("Process")){
						ret = SUCCESS;
						reschedule.setUrl(url);
						if(activeMail)
								reschedule.setActiveMail();
						back = reschedule.doProcess();
						if(!back.equals("")){
								addActionError(back);
						}
						else{
								addActionMessage("Saved Successfully");
								bonds = reschedule.getBonds();
								insurances = reschedule.getInsurances();
						}
				}
				return ret;
		}
		
		public Reschedule getReschedule(){ 
				if(reschedule == null){
						reschedule = new Reschedule();
				}		
				return reschedule;
		}
		//
		public void setReschedule(Reschedule val){
				if(val != null)
						reschedule = val;
		}
		public String populate(){
				String ret = SUCCESS;
				return ret;
		}
		public List<Bond> getBonds(){
				return bonds;
		}
		public List<Insurance> getInsurances(){
				return insurances;
		}
		public boolean hasBonds(){
				return bonds != null && bonds.size() > 0;
		}
		public boolean hasInsurances(){
				return insurances != null && insurances.size() > 0;
		}		

}





































