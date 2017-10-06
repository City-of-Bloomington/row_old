/**
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
package permit;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import org.apache.log4j.Logger;


public class Company implements java.io.Serializable{

    String id="", name="", address="", city="", state="", zip="",
				phone="",website="",notes="", user_id="";
		static final long serialVersionUID = 2L;	
		static Logger logger = Logger.getLogger(Company.class);
    String errors = "";
		List<CompanyContact> company_contacts = null;
		List<Contact> contacts = null;	
		List<Permit> permits = null;
		List<Invoice> invoices = null;
		List<Bond> bonds = null;	
		List<Insurance> insurances = null;
		Bond latestBond = null;
		Insurance latestInsurance = null;
		String[] add_contacts = null;
		String[] sel_company_contact = null;// selected ones for further actions
    public Company(){

    }
    public Company(String val){
				setId(val);
    }
    public Company(String val,
									 String val2,
									 String val3,
									 String val4,
									 String val5,
									 String val6,
									 String val7,
									 String val8,
									 String val9
									 ){
				setValues(val, val2, val3, val4, val5, val6, val7, val8, val9);
    }
		void setValues(
									 String val,
									 String val2,
									 String val3,
									 String val4,
									 String val5,
									 String val6,
									 String val7,
									 String val8,
									 String val9
									 ){
				setId(val);
				setName(val2);
				setAddress(val3);
				setCity(val4);
				setState(val5);
				setZip(val6);
				setPhone(val7);
				setWebsite(val8);
				setNotes(val9);
		}
		//
		// setters
		//
		public void setId(String val){
				if(val != null)
						id = val;
    }
		public void setName(String val){
				if(val != null)
						name = val;
    }
		public void setAddress(String val){
				if(val != null)
						address = val;
    }
		public void setCity(String val){
				if(val != null)
						city = val;
    }
		public void setState(String val){
				if(val != null)
						state = val;
    }
		public void setZip(String val){
				if(val != null)
						zip = val;
    }
		public void setPhone(String val){
				if(val != null)
						phone = val;
    }
		public void setWebsite(String val){
				if(val != null)
						website = val;
    }
		public void setNotes(String val){
				if(val != null)
						notes = val;
    }
	
		public void setUser_id(String val){
				if(val != null)
						user_id = val;
    }
		public void setSel_company_contact(String[] vals){
				if(vals != null)
						sel_company_contact = vals;
    }	
    //
    // getters
    //
		public String getId(){
				return id;
    }	
    public String getName(){
				return name;
    }
		public String getAddress(){
				return address;
    }	
    public String getCity(){
				return city;
    }
		public String getState(){
				return state;
    }	
    public String getZip(){
				return zip;
    }
		public String getPhone(){
				return phone;
    }	
    public String getWebsite(){
				return website;
    }
		public String getNotes(){
				return notes;
    }	
		public String toString(){
				return name;
		}
		public String getCityStateZip(){
				String ret = Helper.initCap(city);
				if(!state.equals("")){
						if(!ret.equals("")) ret += ", ";
						ret += state;
				}
				if(!zip.equals("")){
						if(!ret.equals("")) ret += " ";
						ret += zip;
				}
				return ret;
		}	
		public void setAdd_contacts(String[] vals){
				if(vals != null && vals.length > 0){
						add_contacts = vals;
				}
		}
		//
		public List<CompanyContact> getCompany_contacts(){
				if(!id.equals("")){
						CompanyContactList ccl = new CompanyContactList(id);
						String back = ccl.find();
						if(back.equals("")){
								List<CompanyContact> list = ccl.getCompany_contacts();
								if(list != null && list.size() > 0){
										company_contacts = list;
								}
						}
				}
				return company_contacts;
		}
	
		public List<Contact> getContacts(){
				if(!id.equals("")){
						if(contacts == null){
								getCompany_contacts();
								if(company_contacts != null){
										contacts = new ArrayList<Contact>();
										for(int j=0;j<company_contacts.size();j++){
												Contact one = company_contacts.get(j).getContact();
												if(one != null){
														contacts.add(one);
												}
										}
								}
						}
				}
				return contacts;
		}
		public List<Invoice> getInvoices(){
				if(!id.equals("")){
						InvoiceList ccl = new InvoiceList(null, id);
						ccl.setNoLimit();
						String back = ccl.find();
						if(back.equals("")){
								List<Invoice> list = ccl.getInvoices();
								if(list != null && list.size() > 0){
										invoices = list;
								}
						}
				}
				return invoices;
		}
		public List<Permit> getPermits(){
				if(!id.equals("")){
						PermitList ccl = new PermitList();
						ccl.setCompany_id(id);
						ccl.setNoLimit();
						String back = ccl.find();
						if(back.equals("")){
								List<Permit> list = ccl.getPermits();
								if(list != null && list.size() > 0){
										permits = list;
								}
						}
				}
				return permits;
		}
		public List<Bond> getBonds(){
				if(!id.equals("")){
						BondList ccl = new BondList();
						ccl.setCompany_id(id);
						ccl.setNoLimit();
						String back = ccl.find();
						if(back.equals("")){
								List<Bond> list = ccl.getBonds();
								if(list != null && list.size() > 0){
										bonds = list;
										latestBond = bonds.get(0);
								}
						}
				}
				return bonds;
		}
		public List<Insurance> getInsurances(){
				if(!id.equals("")){
						InsuranceList ccl = new InsuranceList();
						ccl.setCompany_id(id);
						ccl.setNoLimit();
						String back = ccl.find();
						if(back.equals("")){
								List<Insurance> list = ccl.getInsurances();
								if(list != null && list.size() > 0){
										insurances = list;
										latestInsurance = insurances.get(0);
								}
								
						}
				}
				return insurances;
		}			
		public boolean hasPermits(){
				getPermits();
				return permits != null && permits.size() > 0;
		}
		public boolean hasAffiliations(){
				return hasContacts();
		}
		public boolean hasContacts(){
				getContacts();
				return (contacts != null && contacts.size() > 0);
		}
		public boolean hasInvoices(){
				getInvoices();
				return (invoices != null && invoices.size() > 0);
		}
		public boolean hasBonds(){
				getBonds();
				return (bonds != null && bonds.size() > 0);
		}
		public boolean hasInsurances(){
				getInsurances();
				return (insurances != null && insurances.size() > 0);
		}
		public Bond getLatestBond(){
				if(latestBond == null){
						getBonds();
				}
				return latestBond;
		}
		public Insurance getLatestInsurance(){
				if(latestInsurance == null){
						getInsurances();
				}
				return latestInsurance;
		}
		public boolean equals(Object gg){
				boolean match = false;
				if (gg != null && gg instanceof Company){
						match = id.equals(((Company)gg).id);
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
		public String doSelect(){
		
				String msg="";
		
				String qq ="select id,name,address,city,state,zip,phone,website,notes "+
						"from companies where id=?";
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				logger.debug(qq);
				try{
						con = Helper.getConnection();
						if(con == null){
								msg = "Could not connect ";
						}
						else{
								pstmt = con.prepareStatement(qq);
								pstmt.setString(1,id);
								rs = pstmt.executeQuery();
								if(rs.next()){
										setValues(rs.getString(1),
															rs.getString(2),
															rs.getString(3),
															rs.getString(4),
															rs.getString(5),
															rs.getString(6),
															rs.getString(7),
															rs.getString(8),
															rs.getString(9));
								}
						}
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(ex+" : "+qq);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				return msg;
		}
		public String doSave(){
		
				String msg = "";
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				if(name.equals("")){
						msg = "Company name is required";
						return msg;
				}
				String qq = "insert into companies values (0,"+
						"?,?,?,?,?,?,?,?)";
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
		public String setFields(PreparedStatement pstmt){

				String msg = "";
				String today = Helper.getToday();
				int jj = 1;
				try{
						if(name.equals(""))
								pstmt.setNull(jj++, Types.VARCHAR);
						else
								pstmt.setString(jj++, name);
						if(address.equals(""))
								pstmt.setNull(jj++, Types.VARCHAR);
						else
								pstmt.setString(jj++, address);				
						if(city.equals(""))	
								pstmt.setNull(jj++, Types.VARCHAR);
						else				
								pstmt.setString(jj++, city);
						if(state.equals(""))
								pstmt.setNull(jj++, Types.VARCHAR);
						else						
								pstmt.setString(jj++, state);
						if(zip.equals(""))
								pstmt.setNull(jj++, Types.VARCHAR);
						else
								pstmt.setString(jj++, zip);				
						if(phone.equals(""))
								pstmt.setNull(jj++, Types.VARCHAR);
						else
								pstmt.setString(jj++, phone);
						if(website.equals(""))
								pstmt.setNull(jj++, Types.VARCHAR);
						else
								pstmt.setString(jj++, website);
						if(notes.equals(""))
								pstmt.setNull(jj++, Types.VARCHAR);
						else
								pstmt.setString(jj++, notes);			
				}
				catch(Exception ex){
						msg += ex;
						logger.error(msg);
				}
				return msg;
		}
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
						qq = "update companies set ";
						qq += "name = ?, ";
						qq += "address = ?,"; 
						qq += "city =?,"; 
						qq += "state =?,";
						qq += "zip = ?,";
						qq += "phone= ?,";		
						qq += "website = ?,"; 
						qq += "notes = ? ";
						qq += "where id=? ";
						logger.debug(qq);
						pstmt = con.prepareStatement(qq);
						setFields(pstmt);
						pstmt.setString(9, id);
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
		public String deleteAffiliates(){
		
				String msg = "";
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;		
				if(sel_company_contact == null){
						return msg;
				}
				//
				String qq = "delete from company_contacts where id=?";
				con = Helper.getConnection();
				if(con == null){
						msg = "Could not connect to Database ";
						logger.error(msg);
						return msg;
				}
				try {
						logger.debug(qq);
						pstmt = con.prepareStatement(qq);
						for(String str:sel_company_contact){
								pstmt.setString(1, str);
								pstmt.executeUpdate();
						}
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
		public String doDelete(){
		
				String msg = "";
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;		
				if(id.equals("")){
						msg = "company id not set";
						return msg;
				}
				//
				String qq = "delete from companies where id=?";
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
