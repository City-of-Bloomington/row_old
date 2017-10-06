/**
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
package permit;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.sql.*;
import org.apache.log4j.Logger;

public class InsuranceList{

    String insurance_company_id="",company_contact_id="",date_from="",date_to="";
		static final long serialVersionUID = 215L;		
		static Logger logger = Logger.getLogger(InsuranceList.class);
		static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");	
		List<Insurance> insurances = null;
    String limit = "20", sort_by="b.id DESC", company_id="", contact_id="",
				id="", policy_num="", amount_from="", amount_to="", amount="", 
				permit_num="", permit_id="", type="";
		boolean active_only = false, aboutToExpire=false;
    public InsuranceList(){
    }
		public List<Insurance> getInsurances(){
				return insurances;
		}
		public void setId(String val){
				if(val != null)
						id = val;
    }
		public void setPolicy_num(String val){
				if(val != null)
						policy_num = val;
    }	
		public void setInsurance_company_id(String val){
				if(val != null && !val.equals("-1"))
						insurance_company_id = val;
    }
		public void setCompany_id(String val){
				if(val != null && !val.equals("-1"))
						company_id = val;
    }		

		public void setCompany_contact_id(String val){
				if(val != null)
						company_contact_id = val;
    }
		public void setDate_from(String val){
				if(val != null)
						date_from = val;
    }
		public void setDate_to(String val){
				if(val != null)
						date_to = val;
    }
		public void setAmount_from(String val){
				if(val != null)
						amount_from = val;
    }
		public void setAmount_to(String val){
				if(val != null)
						amount_to = val;
    }
		public void setAmount(String val){
				if(val != null)
						amount = val;
    }
		public void setType(String val){
				if(val != null && !val.equals("-1"))
						type = val;
    }		
		public void setPermit_num(String val){
				if(val != null)
						permit_num = val;
    }
		public void setPermit_id(String val){
				if(val != null)
						permit_id = val;
    }		
	
		public void setContact_id(String val){ 
				if(val != null){
						contact_id = val;
				}
    }
		public void setSort_by(String val){ 
				if(val != null){
						sort_by = val;
				}
    }
		public void setActiveOnly(){
				active_only = true;
		}
		public void setAboutToExpire(){
				aboutToExpire = true;
		}
		public void setNoLimit(){
				limit = "";
		}
		public String getId(){
				return id;
    }
		public String getType(){
				return type;
    }		
		public String getPolicy_num(){
				return policy_num;
    }	
		public String getInsurance_company_id(){
				if(insurance_company_id.equals(""))
						return "-1";
				return insurance_company_id;
    }	
		public String getDate_from(){
				return date_from;
    }
		public String getDate_to(){
				return date_to;
    }
		public String getAmount_from(){
				return amount_from;
    }
		public String getAmount_to(){
				return amount_to;
    }
		public String getAmount(){
				return amount;
    }
		public String getPermit_num(){
				return permit_num;
    }
		public String getPermit_id(){
				return permit_id;
    }		

		public String getCompan_contact_id(){
				return company_contact_id;
    }

		public String getSort_by(){
				return sort_by;
    }	
    /**
		 * find all insurances from the database
		 * @return errors if any
		 */
		public String find(){
		
				String msg="", qw="";
				String qq = "select b.id,b.insurance_company_id,b.policy_num,date_format(b.expire_date,'%m/%d/%Y'),b.amount,b.company_contact_id,b.notes,c.name,b.type,datediff(b.expire_date,now()) ";
				String qf = "from insurances b "+
						" left join bond_companies c on c.id=b.insurance_company_id "+
						" left join excavpermits p on p.insurance_id = b.id "+
						" left join company_contacts cc on b.company_contact_id = cc.id ";
				
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				if(!id.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += "b.id = ? ";
				}
				if(!policy_num.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += "b.policy_num = ? ";
				}		
				if(!insurance_company_id.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += "b.insurance_company_id = ? ";
				}
				if(!company_contact_id.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += "b.company_contact_id = ? ";
				}
				if(!company_id.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += "cc.company_id = ? ";
				}
				if(!contact_id.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += "cc.contact_id = ? ";
				}		
				if(!date_from.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += "b.expire_date >= ? ";
				}
				if(!date_to.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += "b.expire_date <= ? ";
				}
				if(!amount_from.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += "b.amount >= ? ";
				}
				if(!amount_to.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += "b.amount <= ? ";
				}
				if(!amount.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += "b.amount = ? ";
				}
				if(!permit_num.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += "p.permit_num like ? ";
				}
				if(!permit_id.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += "p.permit_id = ? ";
				}
				if(!type.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += "b.type = ? ";
				}				
				if(active_only){
						if(!qw.equals("")) qw += " and ";
						qw += "b.expire_date > CURRENT_DATE ";
				}
				else if(aboutToExpire){
						if(!qw.equals("")) qw += " and ";
						qw += "b.expire_date > CURRENT_DATE and b.expire_date < date_add(CURDATE(), INTERVAL 30 DAY) ";
				}
				qq += qf;
				if(!qw.equals("")){
						qq += " where "+qw;
				}
				if(!sort_by.equals("")){
						qq += " order by "+sort_by;
				}
				if(!limit.equals("")){
						qq += " limit "+limit;
				}
				logger.debug(qq);
				try{
						con = Helper.getConnection();
						if(con == null){
								msg = "Could not connect ";
								return msg;
						}
						pstmt = con.prepareStatement(qq);
						int jj = 1;
						if(!id.equals("")){
								pstmt.setString(jj++, id);
						}
						if(!policy_num.equals("")){
								pstmt.setString(jj++, policy_num);
						}			
						if(!insurance_company_id.equals("")){
								pstmt.setString(jj++, insurance_company_id);
						}
						if(!company_contact_id.equals("")){
								pstmt.setString(jj++, company_contact_id);
						}
						if(!company_id.equals("")){
								pstmt.setString(jj++, company_id);
						}
						if(!contact_id.equals("")){
								pstmt.setString(jj++, contact_id);
						}			
						if(!date_from.equals("")){
								pstmt.setDate(jj++, new java.sql.Date(dateFormat.parse(date_from).getTime()));		
						}
						if(!date_to.equals("")){
								pstmt.setDate(jj++, new java.sql.Date(dateFormat.parse(date_to).getTime()));		
						}
						if(!amount_from.equals("")){
								pstmt.setString(jj++, amount_from);
						}
						if(!amount_to.equals("")){
								pstmt.setString(jj++, amount_to);
						}
						if(!amount.equals("")){
								pstmt.setString(jj++, amount);
						}
						if(!permit_num.equals("")){
								pstmt.setString(jj++, permit_num);
						}
						if(!permit_id.equals("")){
								pstmt.setString(jj++, permit_id);
						}
						if(!type.equals("")){
								pstmt.setString(jj++, type);
						}						
						insurances = new ArrayList<Insurance>();
						rs = pstmt.executeQuery();
						while(rs.next()){
				
								Insurance one = new Insurance(rs.getString(1),
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
								if(!insurances.contains(one))
										insurances.add(one);
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
	
}
