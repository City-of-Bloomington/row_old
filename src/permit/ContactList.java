/**
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
package permit;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import javax.naming.*;
import org.apache.log4j.Logger;

public class ContactList{

		static Logger logger = Logger.getLogger(ContactList.class);
		static final long serialVersionUID = 201L;
		List<Contact> contacts = null;
    String type_id="", limit="20", sort_by="c.id desc";
		String name = "", address="", city="", state="", zip="", phone="",id="";
	
    public ContactList(){
    }
    public ContactList(String val){
				setType_id(val);
    }
		public void setType_id(String val){
				if(val != null && !val.equals("-1"))
						type_id = val;
		}
		public List<Contact> getContacts(){
				return contacts;
		}
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
		public void setSort_by(String val){
				if(val != null && !val.equals("-1"))
						sort_by = val;
		}
		public void setNo_limit(){
				limit = "";
		}
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
		public String getSort_by(){
				return sort_by;
		}
		public String getType_id(){
				if(type_id.equals(""))
						return "-1";
				return type_id;
		}		
    /**
		 * find all contacts in the database
		 * @return errors if any
		 */
		public String find(){
		
				String msg="", qw="";
				String qq = "";
				qq = "select c.id,c.fname,c.lname,c.title,c.type_id,c.address,"+
						"c.city,c.state,c.zip,c.work_phone,c.cell_phone,c.fax,c.email,"+
						"c.notes,t.name "+
						"from contacts c left join contact_types t on c.type_id=t.id ";		
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				if(!id.equals("")){
						qw += "c.id = ? ";
				}
				else{
						if(!type_id.equals("")){
								qw += " c.type_id = ? ";
						}
						if(!name.equals("")){
								if(!qw.equals("")) qw += " and ";
								qw += "(c.lname like ? or c.fname like ?)";
						}
						if(!address.equals("")){
								if(!qw.equals("")) qw += " and ";			
								qw += "c.address like ? ";
						}
						if(!city.equals("")){
								if(!qw.equals("")) qw += " and ";
								qw += "c.city like ? ";
						}
						if(!state.equals("")){
								if(!qw.equals("")) qw += " and ";
								qw += "c.state = ? ";
						}
						if(!zip.equals("")){
								if(!qw.equals("")) qw += " and ";
								qw += "c.zip like ? ";
						}
						if(!phone.equals("")){
								if(!qw.equals("")) qw += " and ";
								qw += "(c.work_phone = ? or c.cell_phone = ?)";
						}
				}
				if(!qw.equals("")){
						qq += " where "+qw;
				}		
				if(sort_by.equals("")){
						qq += " order by c.id DESC ";
				}
				else{
						qq += " order by "+sort_by;
				}	
				if(!limit.equals("")){
						qq += " limit "+limit;
				}
				con = Helper.getConnection();
				if(con == null){
						msg = "Could not connect ";
						return msg;
				}		
				try{
						logger.debug(qq);
						pstmt = con.prepareStatement(qq);
						int jj=1;
						if(!id.equals("")){
								pstmt.setString(jj++, id);
						}
						else{
								if(!type_id.equals("")){
										pstmt.setString(jj++, type_id);
								}
			
								if(!name.equals("")){
										pstmt.setString(jj++, "%"+name+"%");
										pstmt.setString(jj++, "%"+name+"%");				
								}
								if(!address.equals("")){
										pstmt.setString(jj++, "%"+address+"%");
								}
								if(!city.equals("")){
										pstmt.setString(jj++, city);
								}
								if(!state.equals("")){
										pstmt.setString(jj++, state);
								}
								if(!zip.equals("")){
										pstmt.setString(jj++, zip);
								}
								if(!phone.equals("")){
										pstmt.setString(jj++, phone);
										pstmt.setString(jj++, phone);				
								}
						}
						contacts = new ArrayList<Contact>();
				
						rs = pstmt.executeQuery();
						while(rs.next()){
								Contact one =
										new Contact(rs.getString(1),
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
																rs.getString(15));
								contacts.add(one);
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
