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

public class BondList{

    String bond_company_id="",company_contact_id="",date_from="",date_to="";
		static final long serialVersionUID = 214L;		
		static Logger logger = Logger.getLogger(BondList.class);
		static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");	
		List<Bond> bonds = null;
    String limit = "20", sort_by="b.id DESC", company_id="", contact_id="",
				id="", bond_num="", amount_from="", amount_to="", amount="", type="",
				permit_num="";
		boolean active_only = false, aboutToExpire=false, expired=false;
    public BondList(){
    }
		public List<Bond> getBonds(){
				return bonds;
		}
		public void setId(String val){
				if(val != null)
						id = val;
    }
		public void setBond_num(String val){
				if(val != null)
						bond_num = val;
    }	
		public void setBond_company_id(String val){
				if(val != null && !val.equals("-1"))
						bond_company_id = val;
    }
		public void setCompany_id(String val){
				if(val != null && !val.equals("-1"))
						company_id = val;
    }		
		public void setType(String val){
				if(val != null && !val.equals("-1"))
						type = val;
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
		public void setPermit_num(String val){
				if(val != null)
						permit_num = val;
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
		public void setNoLimit(){
				limit = "";
		}
		public String getId(){
				return id;
    }
		public String getBond_num(){
				return bond_num;
    }	
		public String getBond_company_id(){
				if(bond_company_id.equals(""))
						return "-1";
				return bond_company_id;
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
		public String getType(){
				if(type.equals(""))
						return "-1";
				return type;
    }
		public String getCompan_contact_id(){
				return company_contact_id;
    }
		public void setAboutToExpire(){
				aboutToExpire = true;
		}
		public void setExpired(){
				expired = true;
		}
		public String getSort_by(){
				return sort_by;
    }	
    /**
		 * find all bonds from the database
		 * @return errors if any
		 */
		/*
			select b.id,b.bond_company_id,b.bond_num,date_format(b.expire_date,'%m/%d/%Y'),b.amount,b.type_id,b.company_contact_id,b.notes,t.name,c.name,b.description left join bond_types t on t.id=b.type_id left join bond_companies c on c.id=b.bond_company_id left join company_contacts cc on b.company_contact_id = cc.id where b.expire_date >  current_date order by c.name limit 10";

		*/
		public String find(){
		
				String msg="", qw="";
				String qq = "select b.id,b.bond_company_id,b.bond_num,date_format(b.expire_date,'%m/%d/%Y'),b.amount,b.company_contact_id,b.notes,c.name,b.description,b.type,if(b.expire_date is null, -1, datediff(b.expire_date, now())) ";
				String qf = "from bonds b "+
						" left join bond_companies c on c.id=b.bond_company_id "+
						" left join excavpermits p on p.bond_id = b.id "+
						" left join company_contacts cc on b.company_contact_id = cc.id ";
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				if(!id.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += "b.id = ? ";
				}
				if(!bond_num.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += "b.bond_num = ? ";
				}		
				if(!type.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += "b.type = ? ";
				}		
				if(!bond_company_id.equals("")){
						if(!qw.equals("")) qw += " and ";
						qw += "b.bond_company_id = ? ";
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
				if(active_only){
						if(!qw.equals("")) qw += " and ";
						qw += "b.expire_date > CURRENT_DATE ";
				}
				else if(expired){
						if(!qw.equals("")) qw += " and ";
						qw += "b.expire_date < CURRENT_DATE ";
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
						if(!bond_num.equals("")){
								pstmt.setString(jj++, bond_num);
						}			
						if(!type.equals("")){
								pstmt.setString(jj++, type);
						}		
						if(!bond_company_id.equals("")){
								pstmt.setString(jj++, bond_company_id);
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
						bonds = new ArrayList<Bond>();
						rs = pstmt.executeQuery();
						while(rs.next()){
								Bond one = new Bond(rs.getString(1),
																		rs.getString(2),
																		rs.getString(3),
																		rs.getString(4),
																		rs.getDouble(5),
																		rs.getString(6),
																		rs.getString(7),
																		rs.getString(8),
																		rs.getString(9),
																		rs.getString(10),
																		rs.getInt(11)
																		);
								if(!bonds.contains(one))
										bonds.add(one);
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
