/**
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
package permit;

import java.util.*;
import java.sql.*;
import java.io.*;
import java.text.*;
import javax.sql.*;
import org.apache.log4j.Logger;

public class Report{
	
		static Logger logger = Logger.getLogger(Report.class);
		static final long serialVersionUID = 172L;
		static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		NumberFormat currFormat = NumberFormat.getCurrencyInstance();	
		String title = "", report_type="active_permits";
		boolean debug = false;

		List<?> list = null;
		DecimalFormat decFormat = new DecimalFormat("###,###.##");
		//
		public Report(){

		}

		public void setReport_type(String val){
				if(val != null)
						report_type = val;
		}
		public String getReport_type(){
				return report_type;
		}
		public String getTitle(){
				return title;
		}	
		//
		/*
			public List<Excavation> getExcavations(){
			return excavations;
			}
			public List<Bond> getBonds(){
			return bonds;
			}
		*/
		public List<?> getList(){
				return list;
		}	

		public String find(){
				String msg = "";
				if(report_type.equals("active_permits")){
						msg = getActivePermits();
						title = "Active Permits ("+list.size()+")";
				}
				else if(report_type.equals("vectren")){
						// 72 = Vectren id
						msg = getActivePermitsByCompany("72");
						title = "Vectren Active Permits ("+list.size()+")";
				}
				else if(report_type.equals("cbu")){
						// 5 = CBU id 
						msg = getActivePermitsByCompany("5");
						title = "CBU Active Permits ("+list.size()+")";			
				}
				else if(report_type.equals("patch")){
						msg = getTempPatchPermits();
						title = "Temporary Patches ("+list.size()+")";			
				}
				else if(report_type.equals("bond")){
						//
						msg = getActiveBonds();
						title = "Active Bonds ("+list.size()+")";					
				}
				else if(report_type.equals("bond0")){
						//
						msg = getZeroBonds();
						title = "Zero Bonds ("+list.size()+")";				
				}
				else if(report_type.equals("bondAbout")){
						//
						msg = getAboutToExpireBonds();
						if(list != null && list.size() > 0)
								title = "About To Expire Bonds ("+list.size()+")";
						else
								title = "No match found ";
				}
				else if(report_type.equals("insurAbout")){
						//
						msg = getAboutToExpireInsurances();
						if(list != null && list.size() > 0)
								title = "About To Expire insurances ("+list.size()+")";
						else
								title = "No match found ";
				}				
				return msg;
		}
		/**
		 * actually these are excavations 
		 */
		public String getActivePermits(){

				ExcavationList el = new ExcavationList();
				el.setActiveOnly();
				el.setSort_by("cm.name ASC"); // company name
				el.setNoLimit();
				String back = el.find();
				if(back.equals("")){
						list = el.getExcavations();
				}
				return back;
		}
		/**
		 * actually these are excavations 
		 */
		public String getActivePermitsByCompany(String company_id){

				ExcavationList el = new ExcavationList();
				el.setActiveOnly();
				el.setSort_by("c.permit_num ASC");
				el.setNoLimit();
				el.setCompany_id(company_id); // Vectren
				String back = el.find();
				if(back.equals("")){
						list = el.getExcavations();
				}
				return back;
		}
		/**
		 * temporary patch cuts
		 */
		public String getTempPatchPermits(){

				ExcavationList el = new ExcavationList();
				el.setStatus("Temporary Patch");
				el.setSort_by("cm.name ASC");
				el.setNoLimit();
				String back = el.find();
				if(back.equals("")){
						list = el.getExcavations();
				}
				return back;
		}	
		/**
		 * actually these are excavations 
		 */
		public String getActiveBonds(){

				BondList el = new BondList();
				el.setActiveOnly();
				el.setSort_by("c.name ASC"); // company name
				el.setNoLimit();
				String back = el.find();
				if(back.equals("")){
						list =  el.getBonds();
				}
				return back;
		}
		public String getAboutToExpireBonds(){

				BondList el = new BondList();
				el.setAboutToExpire();
				el.setSort_by("c.name ASC"); // company name
				el.setNoLimit();
				String back = el.find();
				if(back.equals("")){
						list =  el.getBonds();
				}
				return back;
		}
		public String getAboutToExpireInsurances(){

				InsuranceList el = new InsuranceList();
				el.setAboutToExpire();
				el.setSort_by("c.name ASC"); // company name
				el.setNoLimit();
				String back = el.find();
				if(back.equals("")){
						list =  el.getInsurances();
				}
				return back;
		}				
		public String getZeroBonds(){

				BondList el = new BondList();
				el.setSort_by("c.name ASC"); // company name
				el.setAmount("0");
				el.setNoLimit();
				String back = el.find();
				if(back.equals("")){
						list =  el.getBonds();
				}
				return back;
		}	
}






















































