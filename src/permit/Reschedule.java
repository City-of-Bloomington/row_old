package permit;
/**
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.sql.*;
import java.text.*;
import java.util.List;
import org.apache.log4j.Logger;


public class Reschedule{

		static final long serialVersionUID = 222L;	
		static Logger logger = Logger.getLogger(Reschedule.class);
		boolean bond_flag = false, insurance_flag=false;
		String url = "";
		boolean activeMail = false;
		List<Bond> bonds = null;
		List<Insurance> insurances = null;
    public Reschedule(){

    }
		public void setUrl(String val){
				if(val != null)
						url = val;
		}
		public void setActiveMail(){
				activeMail = true;
		}
		public void setBond_flag(boolean val){
				bond_flag = val;
    }
		public void setInsurance_flag(boolean val){
				insurance_flag = val;
    }
		public boolean getBond_flag(){
				return bond_flag;
		}
		public boolean getInsurance_flag(){
				return insurance_flag;
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
		
		public String doProcess(){
		
				String msg = "";
				if(bond_flag){
						BondList bl = new BondList();
						bl.setActiveOnly();
						bl.setNoLimit();
						msg = bl.find();
						if(msg.equals("")){
								bonds = bl.getBonds();
								System.err.println(" bonds "+bonds.size());
						}
						if(bonds != null){
								ExpireScheduler shed = new ExpireScheduler();
								shed.setUrl(url);
								if(activeMail)
										shed.setActiveMail();
								shed.setType("bond");
								shed.setNeedClean();								
								for(Bond one:bonds){
										shed.setExpire_date(one.getExpire_date());
										shed.setFire_date(one.getFire_date());
										shed.setId(one.getId());
										try{
												shed.run();
										}catch(Exception ex){
												msg += ex;
										}
								}
						}
				}
				if(insurance_flag){
						InsuranceList il = new InsuranceList();
						il.setActiveOnly();
						il.setNoLimit();
						msg = il.find();
						if(msg.equals("")){
								insurances = il.getInsurances();
						}
						if(insurances != null){
								ExpireScheduler shed = new ExpireScheduler();
								shed.setUrl(url);
								if(activeMail)
										shed.setActiveMail();
								shed.setType("insurance");
								shed.setNeedClean();								
								for(Insurance one:insurances){
										shed.setExpire_date(one.getExpire_date());
										shed.setFire_date(one.getFire_date());
										shed.setId(one.getId());
										try{
												shed.run();
										}catch(Exception ex){
												msg += ex;
										}
								}
						}
				}
				return msg;
    }	
	
}
