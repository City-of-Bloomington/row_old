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


public class Insurance implements java.io.Serializable{

    String id="", policy_num="", expire_date="", company_contact_id="", notes="", insurance_company_id="";
		static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		NumberFormat numFormat = NumberFormat.getCurrencyInstance();
		static final long serialVersionUID = 223L;	
		static Logger logger = Logger.getLogger(Insurance.class);
		String permit_id="", user_id="", type="", old_expire_date="",fire_date="";
    String errors = "";
		double amount = 0;
		int days_to_expire = 0;
		CompanyContact companyContact = null;
		Company company = null;
		Contact contact = null;
		Permit permit = null;
		List<Permit> permits = null;
		Type insuranceCompany = null;
	
    public Insurance(){

    }
    public Insurance(String val){
				setId(val);
    }
    public Insurance(String val,
				
										 String val2,
										 String val3,
										 String val4,
										 double val5,
										 String val6,
										 String val7,
										 String val8,
										 String val9,
										 int val10

								){
				setValues(val, val2, val3, val4, val5, val6, val7, val8, val9, val10);
    }
		void setValues(
									 String val,
									 String val2,
									 String val3,
									 String val4,
									 double val5,
									 String val6,
									 String val7,
									 String val8,
									 String val9,
									 int val10
									 ){
				setId(val);
				setInsurance_company_id(val2);
				setPolicy_num(val3);
				setExpire_date(val4);
				setAmount(val5);
				setCompany_contact_id(val6);
				setNotes(val7);
				if(val8 != null){
						insuranceCompany = new Type(insurance_company_id, val8);
				}
				setType(val9);
				setDays_to_expire(val10);
		}
		//
		// setters
		//
		public void setId(String val){
				if(val != null)
						id = val;
    }
		public void setInsurance_company_id(String val){
				if(val != null)
						insurance_company_id = val;
    }	
		public void setPolicy_num(String val){
				if(val != null)
						policy_num = val;
    }
		public void setAmount(double val){
				amount = val;
    }
		public void setType(String val){
				if(val != null)
						type = val;
    }		
		public void setCompany_contact_id(String val){
				if(val != null)
						company_contact_id = val;
    }
		public void setNotes(String val){
				if(val != null)
						notes = val;
    }
		public void setExpire_date(String val){
				if(val != null)
						expire_date = val;
    }
		public void setOld_expire_date(String val){
				if(val != null)
						old_expire_date = val;
    }		
		public void setUser_id(String val){
				if(val != null)
						user_id = val;
    }
		public void setPermit_id(String val){
				if(val != null)
						permit_id = val;
    }
		public void setDays_to_expire(int val){
				days_to_expire = val;
		}
    //
    // getters
    //
		public String getId(){
				return id;
    }
		public String getInsurance_company_id(){
				return insurance_company_id;
    }	
    public String getPolicy_num(){
				return policy_num;
    }
		public String getExpire_date(){
				return expire_date;
    }
		public double getAmount(){
				return amount;
    }
		public String getAmountStr(){
				return numFormat.format(amount);
		}
		public String getCompany_contact_id(){
				return company_contact_id;
    }
		public String getNotes(){
				return notes;
    }
		public String getType(){
				return type;
    }
		public int getDays_to_expire(){
				return days_to_expire;
		}
		public boolean equals(Object gg){
				boolean match = false;
				if (gg != null && gg instanceof Insurance){
						match = id.equals(((Insurance)gg).id);
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
		public String getExpire_status(){
				String ret = "";
				if(isExpired()){
						ret = "Expired";
				}
				else if(isAboutToExpire()){
						ret = "About to expire in "+days_to_expire;
				}
				else{
						ret = "Active (days to expire "+days_to_expire+")";
				}
				return ret;
		}
		public String getFire_date(){
				if(fire_date.equals("")){
						int days_to_fire = -1;
						if(days_to_expire > 30){
								days_to_fire = days_to_expire-30;
						}
						else if(days_to_expire > 1){
								days_to_fire = 1;
						}
						if(days_to_fire > 0){
								fire_date = Helper.getDateAfter(days_to_fire);
						}
				}
				return fire_date;
		}		
		public boolean isExpired(){
				return hasExpireDate() && days_to_expire < 0;
		}
		public boolean isAboutToExpire(){
				return days_to_expire > 0 && days_to_expire <= 30;
		}		
		public CompanyContact getCompanyContact(){
				if(companyContact == null){
						if(company_contact_id.equals("") && !permit_id.equals("")){
								getPermit();
								findCompanyContactFromPermit();
						}
						if(!company_contact_id.equals("")){
								CompanyContact one = new CompanyContact(company_contact_id);
								String back = one.doSelect();
								if(back.equals("")){
										companyContact = one;
								}
						}
				}
				return companyContact;
		}	
		public Contact getContact(){
				getCompanyContact();
				if(companyContact != null){
						contact = companyContact.getContact();
				}
				return contact;
		}
		public Company getCompany(){
				getCompanyContact();
				if(companyContact != null){
						company =  companyContact.getCompany();
				}
				return company;
		}
		public Permit getPermit(){
				if(permit == null && !permit_id.equals("")){
						Permit one = new Permit(permit_id);
						String back = one.doSelect();
						if(back.equals("")){
								permit = one;
						}
				}
				else if(!company_contact_id.equals("")){
						PermitList  pl = new PermitList();
						pl.setCompany_contact_id(company_contact_id);
						String back = pl.find();
						if(back.equals("")){
								List<Permit> list = pl.getPermits();
								if(list != null && list.size() > 0){
										permit = list.get(0);
										permit_id = permit.getId();
								}
						}
				}
				return permit;
		}
		public List<Permit> getPermits(){
				if(permits == null && !id.equals("")){
						PermitList  pl = new PermitList();
						pl.setInsurance_id(id);
						pl.setNoLimit();
						String back = pl.find();
						if(back.equals("")){
								List<Permit> list = pl.getPermits();
								if(list != null && list.size() > 0){
										permits = list;
										if(list.size() == 1){
												permit = list.get(0);
										}
								}
						}
				}
				return permits;
		}
		public boolean hasPermits(){
				return getPermits() != null;
		}
		public Type getInsuranceCompany(){
				if(insuranceCompany == null && !insurance_company_id.equals("")){
						Type one = new Type(insurance_company_id, null, "bond_companies");
						String back = one.doSelect();
						if(back.equals("")){
								insuranceCompany = one;
						}
				}
				return insuranceCompany;
		}
		public boolean hasCompany(){
				getCompany();
				return company != null;
		}
		public boolean hasContact(){
				getContact();
				return contact != null;
		}
		public String getPermit_id(){
				return permit_id;
    }
		public boolean hasPermit(){
				getPermit();
				return !permit_id.equals("");
		}
		public boolean hasExpireDate(){
				return !expire_date.equals("");
		}
		// we need this to check if there is a change in
		// expire date, for qurtz schedular for update
		public boolean expireDateChanged(){
				return !id.equals("") && !expire_date.equals(old_expire_date);
		}		
		private void findCompanyContactFromPermit(){
				if(permit == null){
						getPermit();
				}
				if(permit != null){
						if(permit.hasCompanyContact()){
								companyContact = permit.getCompanyContact();
								company_contact_id = companyContact.getId();
						}
				}
		}	
		public String toString(){
				return policy_num;
		}
		public String getInfo(){
				String ret = "";
				getInsuranceCompany();
				if(insuranceCompany != null){
						ret = insuranceCompany.getName();
				}
				if(!policy_num.equals("")){
						ret += " #:"+policy_num;
				}
				if(!expire_date.equals("")){
						ret += " Exp:"+expire_date;
				}
				return ret;
		}
		
		private String addInsuranceToPermit(){
				String msg = "";
				getPermit();
				if(permit != null){
						permit.setInsurance_id(id);
						msg = permit.doUpdate();
				}
				return msg;
		}
		public String doSelect(){
		
				String msg="";
		
				String qq = "select b.id,b.insurance_company_id,b.policy_num,"+
						" date_format(b.expire_date,'%m/%d/%Y'),b.amount,"+
						" b.company_contact_id,b.notes,c.name,b.type,datediff(b.expire_date,now()) "+
						" from insurances b "+
						" left join bond_companies c on c.id=b.insurance_company_id "+
						" where b.id=?";
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
															rs.getDouble(5),
															rs.getString(6),
															rs.getString(7),
															rs.getString(8),
															rs.getString(9),
															rs.getInt(10)
															);
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
				if(hasPermit() && company_contact_id.equals("")){
						findCompanyContactFromPermit();
				}		
				String qq = "insert into insurances values (0,"+
						"?,?,?,?,?,?,?)";
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
				if(msg.equals(""))
						msg = doSelect();
				return msg;
    }	
		public String setFields(PreparedStatement pstmt){

				String msg = "";
				String today = Helper.getToday();
				int jj = 1;
				try{
						if(insurance_company_id.equals(""))	
								pstmt.setNull(jj++, Types.VARCHAR);
						else				
								pstmt.setString(jj++, insurance_company_id);			
						if(policy_num.equals(""))	
								pstmt.setNull(jj++, Types.VARCHAR);
						else				
								pstmt.setString(jj++, policy_num);
						if(expire_date.equals(""))
								pstmt.setNull(jj++, Types.DATE);
						else
								pstmt.setDate(jj++, new java.sql.Date(dateFormat.parse(expire_date).getTime()));				
						pstmt.setDouble(jj++, amount);
						if(company_contact_id.equals(""))
								pstmt.setNull(jj++, Types.INTEGER);
						else
								pstmt.setString(jj++, company_contact_id);
						if(notes.equals(""))
								pstmt.setNull(jj++, Types.VARCHAR);
						else
								pstmt.setString(jj++, notes);
						if(type.equals(""))
								pstmt.setNull(jj++, Types.VARCHAR);
						else
								pstmt.setString(jj++, type);						
			
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
						qq = "update insurances set ";
						qq += "insurance_company_id =?, ";
						qq += "policy_num =?,"; 
						qq += "expire_date =?, ";
						qq += "amount = ?,";
						qq += "company_contact_id = ?,";
						qq += "notes = ?,type=? ";
						qq += "where id=? ";
						logger.debug(qq);
						pstmt = con.prepareStatement(qq);
						setFields(pstmt);
						pstmt.setString(8, id);
						pstmt.executeUpdate();
				}
				catch (Exception ex){
						msg += ex+":"+qq;
						logger.error(ex+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				if(msg.equals(""))
						msg = doSelect();
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
				String qq = "delete from insurances where id=?";
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
