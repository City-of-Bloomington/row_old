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
import javax.naming.*;
import javax.sql.*;
import javax.naming.directory.*;
import org.apache.log4j.Logger;

public class Permit implements java.io.Serializable{

		static Logger logger = Logger.getLogger(Permit.class);
		static final long serialVersionUID = 101L;		
		static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");	
		String permit_num = "", id="";
		String status = "";
		String project = "";
		String reviewer_id = "", start_date="", date="";
		String bond_id = "", company_contact_id="", insurance_id=""; 
		String fee = "", notes="";
		String invoice_id = "";
		String permit_type = "", user_id="";
		Type violation = null;
		User reviewer = null;
		Bond bond = null;
		Invoice invoice = null;
		CompanyContact companyContact = null;
		Company company = null;
		Contact contact = null;
		Insurance insurance = null;
		List<Excavation> excavations = null;
		List<Inspection> inspections = null;

		// for permit that does not have an invoice yet, the user can pick one to add to the permit
		List<Invoice> invoices = null;
		List<Bond> bonds = null;
		List<Insurance> insurances = null;
		//
		String message="";

		public Permit(){
		}	
		public Permit(String value){
				id = value;
		}
		public Permit(String val, String val2, String val3){
				setId(val);
				setPermit_num(val2);
				setProject(val3);
		}	
		public Permit(
									String val,
									String val2,
									String val3,
									String val4,
									String val5,
									String val6,
									String val7,
									String val8,
									String val9,
									String val10,
				  
									String val11,
									String val12,
									String val13,
									String val14,
				  
									String val15,
									String val16,
									String val17,
									String val18
									){
				setValues(val, val2, val3, val4, val5,
									val6, val7, val8, val9, val10,
									val11, val12, val13, val14, val15,
									val16, val17, val18);
		}

		void setValues(String val,
									 String val2,
									 String val3,
									 String val4,
									 String val5,
									 String val6,
									 String val7,
									 String val8,
									 String val9,
									 String val10,
				   
									 String val11,
									 String val12,
									 String val13,
									 String val14,
									 String val15,
									 String val16,
									 String val17,
									 String val18
									 ){
				setId(val);
				setPermit_num(val2);
				setStatus(val3);
				setProject(val4);
				setReviewer_id(val5);
				setStart_date(val6);
				setDate(val7);
			
				setFee(val8);
				setBond_id(val9);
				setCompany_contact_id(val10);
				setNotes(val11);		
				setInvoice_id(val12);
				setPermit_type(val13);
				if(!reviewer_id.equals("")){
						reviewer = new User(reviewer_id, val14, val15, val16, val17);
				}
				setInsurance_id(val18);
		}
    //
		// setters
		//
		public void setId(String value){
				if(value != null)
						id = value;
		}
		public void setUser_id(String value){
				if(value != null)
						user_id = value;
		}	
		public void setPermit_num(String value){
				if(value != null)
						permit_num = value;
		}
		public void setStatus(String value){
				if(value != null && !value.equals("-1"))
						status = value;
		}
		public void setProject(String value){
				if(value != null)
						project = value;
		}
		public void setReviewer_id(String value){
				if(value != null && !value.equals("-1"))
						reviewer_id = value;
		}
		public void setStart_date(String value){
				if(value != null)
						start_date = value;
		}
	
		public void setDate(String value){
				if(value != null)
						date = value;
		}
		public void setFee(String value){
				if(value != null)
						fee = value;
		}
		public void setBond_id(String value){ 
				if(value != null && !value.equals("-1"))   // when picked from list
						bond_id = value;
		}
		public void setInsurance_id(String value){ 
				if(value != null && !value.equals("-1"))   // when picked from list
						insurance_id = value;
		}		
		public void setCompany_contact_id(String value){
				if(value != null)
						company_contact_id = value;
		}
		public void setNotes(String value){
				if(value != null)
						notes = value;
		}	
		public void setInvoice_id(String value){
				if(value != null && !value.equals("-1"))
						invoice_id = value;
		}
	
		public void setPermit_type(String value){
				if(value != null)
						permit_type = value;
		}

		//
		// getters
		//
		public String getId(){
		
				return id;
		}
		//
		// for new permit, we generate a new permit num
		//
		public String getPermit_num(){
				if(id.equals("")){
						genPermitNum();
				}
				return permit_num;
		}
		public String getStatus(){
				return status;
		}
		public String getProject(){
				return project;
		}
		public String getReviewer_id(){
				return reviewer_id;
		}
		public String getInsurance_id(){
				return insurance_id;
		}		
		public String getStart_date(){
				return start_date;
		}	
		public String getDate(){
				return date;
		}	
		public String getFee(){
				return fee;
		}
		public double getFeeDbl(){
				double ret = 0.0;
				if(!fee.equals("")){
						try{
								ret = Double.parseDouble(fee);
						}catch(Exception ex){}
				}
				return ret;
		}
		public String getBond_id(){
				return bond_id;
		}		
		public String getCompany_contact_id(){
				return company_contact_id;
		}
		public String getNotes(){
				return notes;
		}	
		public String getInvoice_id(){
				return invoice_id ;
		}
		public String getInvoice_num(){
				getInvoice();
				if(invoice != null){
						return invoice.getInvoice_num();
				}
				return invoice_id;
		}		
		public String getPermit_type(){
				return permit_type;
		}
		public Bond getBond(){
				if(bond == null && !bond_id.equals("")){
						Bond one = new Bond(bond_id);
						String back = one.doSelect();
						if(back.equals("")){
								bond = one;
						}
				}
				return bond;
		}
		public boolean hasBond(){
				return !bond_id.equals("");
		}
		public Insurance getInsurance(){
				if(insurance == null && !insurance_id.equals("")){
						Insurance one = new Insurance(insurance_id);
						String back = one.doSelect();
						if(back.equals("")){
								insurance = one;
						}
				}
				return insurance;
		}		
		public Invoice getInvoice(){
				if(invoice == null && !invoice_id.equals("")){
						Invoice one = new Invoice(invoice_id);
						String back = one.doSelect();
						if(back.equals("")){
								invoice = one;
						}
				}
				return invoice;
		}	
		public CompanyContact getCompanyContact(){
				if(companyContact == null && !company_contact_id.equals("")){
						CompanyContact one = new CompanyContact(company_contact_id);
						String back = one.doSelect();
						if(back.equals("")){
								companyContact = one;
								company = one.getCompany();
								contact = one.getContact();
						}
				}
				return companyContact;
		}
		public List<Invoice> getInvoices(){
				// if(invoice_id.equals("")){
						
				InvoiceList  ill = new InvoiceList();
				getCompany();
				if(company != null){
						ill.setCompany_id(company.getId());
				}
				else{
						getContact();
						if(contact != null){
								ill.setContact_id(contact.getId());
						}
				}
				ill.setUnpaidStatus();
				String back = ill.find();
				if(back.equals("")){
						List<Invoice> ones = ill.getInvoices();
						if(ones != null && ones.size() > 0){
								invoices = ones;
						}
				}
				return invoices;
		}
		public List<Bond> getBondsForSelection(){
				if(bond_id.equals("")){
						
						BondList  ill = new BondList();
						getCompany();
						if(company != null){
								ill.setCompany_id(company.getId());
						}
						else{
								getContact();
								if(contact != null){
										ill.setContact_id(contact.getId());
								}
						}
						ill.setActiveOnly();
						String back = ill.find();
						if(back.equals("")){
								List<Bond> ones = ill.getBonds();
								if(ones != null && ones.size() > 0){
										bonds = ones;
								}
						}
				}
				return bonds;
		}
		public List<Insurance> getInsurancesForSelection(){
				if(insurance_id.equals("")){
						
						InsuranceList  ill = new InsuranceList();
						getCompany();
						if(company != null){
								ill.setCompany_id(company.getId());
						}
						else{
								getContact();
								if(contact != null){
										ill.setContact_id(contact.getId());
								}
						}
						ill.setActiveOnly();
						String back = ill.find();
						if(back.equals("")){
								List<Insurance> ones = ill.getInsurances();
								if(ones != null && ones.size() > 0){
										insurances = ones;
								}
						}
				}
				return insurances;
		}				
		public boolean canPickInvoice(){
				getInvoice();
				if(invoice != null && !invoice.isStillOpen()){ // can not be changed
						return false;
				}
				getInvoices();
				return invoices != null && invoices.size() > 0;
		}
		public boolean canPickBond(){
				getBondsForSelection();
				return bonds != null && bonds.size() > 0;
		}
		public boolean canPickInsurance(){
				getInsurancesForSelection();
				return insurances != null && insurances.size() > 0;
		}		
		public Contact getContact(){
				if(contact == null) 
						getCompanyContact();
				return contact;
		}
		public Company getCompany(){
				if(company == null)
						getCompanyContact();
				return company;
		}	

		public User getReviewer(){
				if(reviewer == null && !reviewer_id.equals("")){
						User one = new User(reviewer_id);
						String back = one.doSelect();
						if(back.equals("")){
								reviewer = one;
						}
				}
				return reviewer;
		}
		/**
		 * eventhough we have only one bond per permit
		 * we want to List all bonds in jsp
		 */
		public List<Bond> getBonds(){
				List<Bond> bonds = null;
				if(hasBond()){
						getBond();
						if(bond != null){
								bonds = new ArrayList<Bond>(1);
								bonds.add(bond);
						}
				}
				return bonds;
		}
		/**
		 * eventhough we have only one insurance per permit
		 * we want to List all insurances in jsp
		 */
		public List<Insurance> getInsurances(){
				List<Insurance> insurances = null;
				if(hasInsurance()){
						getInsurance();
						if(insurance != null){
								insurances = new ArrayList<Insurance>(1);
								insurances.add(insurance);
						}
				}
				return insurances;
		}		
		public boolean hasInsurance(){
				return !insurance_id.equals("");
		}
		public boolean hasInvoice(){
				return !invoice_id.equals("");
		}
		public boolean hasCompany(){
				getCompany();
				return company != null;
		}
		public boolean hasContact(){
				getContact();
				return contact != null;
		}
		public boolean hasCompanyContact(){
				getCompanyContact();
				return companyContact != null;
		}
		public List<Excavation> getExcavations(){
				if(excavations == null && !permit_num.equals("")){
						ExcavationList el = new ExcavationList();
						el.setPermit_num(permit_num);
						String back = el.find();
						if(back.equals("")){
								excavations = el.getExcavations();
						}
				}
				return excavations;
		}
		public List<Inspection> getInspections(){
				if(inspections == null && !permit_num.equals("")){
						InspectionList el = new InspectionList();
						el.setPermit_num(permit_num);
						String back = el.find();
						if(back.equals("")){
								inspections = el.getInspections();
						}
				}
				return inspections;
		}	
		public boolean hasExcavations(){
				getExcavations();
				return excavations != null && excavations.size() > 0;
		}
		public boolean hasInspections(){
				getInspections();
				return inspections != null && inspections.size() > 0;
		}
		public boolean equals(Object gg){
				boolean match = false;
				if (gg != null && gg instanceof Permit){
						match = id.equals(((Permit)gg).id);
				}
				return match;
		}
		public int hashCode(){
				int code = 0;
				try{
						code = Integer.parseInt(id);
				}catch(Exception ex){};
				return code;
		}		
		//	
		/**
		 * save data to database
		 * @return string if an error occurs
		 */
		public String doSave(){
		
				String msg = "";
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				if(reviewer_id.equals("") && !user_id.equals("")){
						reviewer_id = user_id;
				}

				String qq = "insert into excavpermits values (0,"+
						" ?,?,?,?,?, ?,?,?,?,?, "+
						"?,?,?)";
				con = Helper.getConnection();
				if(con == null){
						msg = "Could not connect to Database ";
						logger.error(msg);
						return msg;
				}
				try {
						logger.debug(qq);			
						pstmt = con.prepareStatement(qq);
						msg += setFields(pstmt);
						pstmt.executeUpdate();
						qq = "select LAST_INSERT_ID() ";
						logger.debug(qq);
						pstmt = con.prepareStatement(qq);			
						rs = pstmt.executeQuery();
						if(rs.next()){
								id = rs.getString(1);
						}			
				}
				catch (Exception ex){
						msg += ex+":"+qq;
						logger.error(ex+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				return msg;
    }
		/**
		 * we need it to be unique, so we need to 
		 * make it synchronized
		 */
		public synchronized String genPermitNum(){
		
				String msg = "";
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				int seq = 1; // if start from scratch
				String qq = "select permit_num from permit_seq order by id DESC ";
				String qq2 = " insert into permit_seq values(0,?)";
				con = Helper.getConnection();
				if(con == null){
						msg = "Could not connect to Database ";
						logger.error(msg);
						return msg;
				}
				try {
						logger.debug(qq);			
						pstmt = con.prepareStatement(qq);
						rs = pstmt.executeQuery();
						if(rs.next()){
								seq = rs.getInt(1);
						}
						seq++;
						qq = qq2;
						pstmt = con.prepareStatement(qq);
						pstmt.setInt(1, seq);
						pstmt.executeUpdate();

						String str = ""+seq;
						if(seq < 10){
								str = "00"+seq;
						}
						else if(seq < 100){
								str = "0"+seq;
						}
						permit_num = "C"+Helper.getCurrentYearYY()+"-ROW-"+str;			
			
				}
				catch (Exception ex){
						msg += ex+":"+qq;
						logger.error(ex+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				return msg;
    }	
		//
		public String setFields(PreparedStatement pstmt){

				String msg = "";
				String today = Helper.getToday();
				int jj = 1;
				try{
						if(permit_num.equals(""))
								pstmt.setNull(jj++, Types.VARCHAR);
						else
								pstmt.setString(jj++, permit_num);
						if(status.equals(""))
								pstmt.setNull(jj++, Types.VARCHAR);
						else
								pstmt.setString(jj++, status);				
						if(project.equals(""))	
								pstmt.setNull(jj++, Types.VARCHAR);
						else				
								pstmt.setString(jj++, project);
						if(reviewer_id.equals(""))
								pstmt.setNull(jj++, Types.VARCHAR);
						else						
								pstmt.setString(jj++, reviewer_id);
						if(start_date.equals(""))
								pstmt.setNull(jj++, Types.VARCHAR);
						else
								pstmt.setDate(jj++, new java.sql.Date(dateFormat.parse(start_date).getTime()));		
						if(date.equals(""))
								pstmt.setNull(jj++, Types.VARCHAR);
						else
								pstmt.setDate(jj++, new java.sql.Date(dateFormat.parse(date).getTime()));		

						if(fee.equals(""))
								pstmt.setNull(jj++, Types.DOUBLE);
						else				
								pstmt.setString(jj++, fee);
						if(bond_id.equals(""))
								pstmt.setNull(jj++, Types.INTEGER);
						else				
								pstmt.setString(jj++, bond_id);
						if(company_contact_id.equals(""))
								pstmt.setNull(jj++, Types.VARCHAR);
						else				
								pstmt.setString(jj++, company_contact_id);
						if(notes.equals(""))
								pstmt.setNull(jj++, Types.VARCHAR);
						else
								pstmt.setString(jj++, notes);

						if(invoice_id.equals(""))
								pstmt.setNull(jj++, Types.INTEGER);
						else				
								pstmt.setString(jj++, invoice_id);
						if(permit_type.equals(""))
								pstmt.setNull(jj++, Types.VARCHAR);
						else				
								pstmt.setString(jj++, permit_type);
						if(insurance_id.equals(""))
								pstmt.setNull(jj++, Types.INTEGER);
						else				
								pstmt.setString(jj++, insurance_id);						
				}
				catch(Exception ex){
						msg += ex;
						logger.error(msg);
				}
				return msg;
		}
	
		/**
		 * update the record in the database
		 */
		public String doUpdate(){
		
				String msg = "";
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				//
				String qq = "";
				con = Helper.getConnection();
				if(con == null){
						msg = "Could not connect to Database ";
						logger.error(msg);
						return msg;
				}		
				try {
						//
						qq = "update excavpermits set ";
			
						qq += "permit_num = ?, ";
						qq += "status = ?,"; 
						qq += "project =?,"; 
						qq += "reviewer_id =?,";
						qq += "start_date = ?,";
						qq += "date = ?,";		
						qq += "fee = ?,"; 
						qq += "bond_id = ?,"; 
						qq += "company_contact_id = ?,";
						qq += "notes = ?,";
			
						qq += "invoice_id=?,";
						qq += "permit_type=?, insurance_id=? ";			
						qq += " where id = ? ";
						logger.debug(qq);
						pstmt = con.prepareStatement(qq);
						setFields(pstmt);
						pstmt.setString(14, id);
						pstmt.executeUpdate();
				}
				catch (Exception ex){
						msg += ex+":"+qq;
						logger.error(ex+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				return msg;
		}
		/**
		 * retreive the record from the database
		 * @return string if an error occurs
		 */
		public String doSelect(){
		
				String msg = "";
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String qq = "";
				con = Helper.getConnection();
				if(con == null){
						msg = "Could not connect to Database ";
						logger.error(msg);
						return msg;
				}
				try {
						qq = "select c.id, " +                         
								"c.permit_num, " +
								"c.status, " +
								"c.project, " +
								"c.reviewer_id, " +
								"date_format(c.start_date,'%m/%d/%Y'),"+
								"date_format(c.date,'%m/%d/%Y'),"+				
								"c.fee,"+     				
								"c.bond_id, " +
								"c.company_contact_id, " + 
								"c.notes, " +  // 8
								"c.invoice_id, " + //				
								"c.permit_type, " +
								"u.empid,u.fullname,u.role,u.active,c.insurance_id "+
								" from excavpermits c "+
								" left join users u on u.id=c.reviewer_id "+
								" where c.id=?";
						logger.debug(qq);
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, id);
						rs = pstmt.executeQuery();
						//
						if(rs.next()){
								setValues(
													rs.getString(1),
													rs.getString(2),
													rs.getString(3),
													rs.getString(4),
													rs.getString(5),
													rs.getString(6),
													rs.getString(7),
													rs.getString(8),
													rs.getString(9),
													rs.getString(10),
													rs.getString(11),
													rs.getString(12),
													rs.getString(13),
													rs.getString(14),
													rs.getString(15),
													rs.getString(16),
													rs.getString(17),
													rs.getString(18)
													);
						}
						else{
								msg = "No record found for "+id;
						}
				}
				catch (Exception ex){
						msg += ex+":"+qq;
						logger.error(ex+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				return msg;

		}
		//
		public String doDelete(){
		
				String msg = "";
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;		
				if(id.equals("")){
						msg = "Need to set ticket id ";
						return msg;
				}
				//
				String qq = "delete from excavpermits where id=?";
				con = Helper.getConnection();
				if(con == null){
						msg = "Could not connect to Database ";
						logger.error(msg);
						return msg;
				}
				try {
						logger.debug(qq);
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, id);
						pstmt.executeUpdate();
				}
				catch (Exception ex){
						msg += ex+":"+qq;
						logger.error(msg);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				return msg;
		}		
	
}






















































