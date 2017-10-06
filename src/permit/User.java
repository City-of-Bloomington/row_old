/**
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
package permit;
import java.sql.*;
import javax.naming.*;
import javax.naming.directory.*;
import org.apache.log4j.Logger;

public class User implements java.io.Serializable{

    String id="", empid="", fullName="", role="",
				active="";
    boolean userExists = false;
	
		static final long serialVersionUID = 281L;	
    String errors = "";
		static Logger logger = Logger.getLogger(User.class);
    public User(){
    }	
    public User(String val){
				setId(val);
    }
    public User(String val, String val2){
				setId(val);		
				setEmpid(val2);
    }
    public User(String val, String val2, String val3){
				setId(val);		
				setEmpid(val2);
				setFullName(val3);
    }	
	
    public User(
								String val,
								String val2,
								String val3,
								String val4,
								String val5
								){
				setValues(val, val2, val3, val4, val5);
		}
		void setValues(
									 String val,
									 String val2,
									 String val3,
									 String val4,
									 String val5){
				setId(val);
				setEmpid(val2);
				setFullName(val3);
				setRole(val4);
				setActive(val5);
    }	
    //
    public boolean hasRole(String val){
				return role.indexOf(val) > -1;
    }
    //
    // getters
    //
    public String getId(){
				return id;
    }
    public String getEmpid(){
				return empid;
    }	
    public String getFullName(){
				return fullName;
    }
    public String getRole(){
				return role;
    }
    //
    // setters
    //
    public void setId(String val){
				if(val != null)
						id = val;
    }
    public void setEmpid(String val){
				if(val != null)
						empid = val;
    }
		public void setUsername(String val){
				setEmpid(val);
		}
    public void setFullName (String val){
				if(val != null)
						fullName = val;
    }
    public void setRole (String val){
				if(val != null)
						role = val;
    }
    public void setActive(String val){
				if(val != null)
						active = val;
    }	
		public boolean userExists(){
				return userExists;
		}
		public boolean canEdit(){
				return role.indexOf("Edit") > -1;
		}
		public boolean canDelete(){
				return role.indexOf("Delete") > -1;
		}
		public boolean isAdmin(){
				return role.indexOf("Admin") > -1;
		}
		public boolean isActive(){
				return !active.equals("");
		}
		public String getActive(){
				return active;
		}
    public String toString(){
				if(fullName.equals("")) return empid;
				else return fullName;
    }
		public String doSelect(){
		
				String msg="", qq="";
				qq = "select id,empid,fullname,role,active from users ";
				if(!empid.equals(""))
						qq += " where empid = ?";
				else
						qq += " where id=?";
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				con = Helper.getConnection();
				if(con == null){
						msg = "Could not connect ";
						return msg;
				}		
				logger.debug(qq);
				try{
						pstmt = con.prepareStatement(qq);
						if(!empid.equals(""))
								pstmt.setString(1, empid);
						else
								pstmt.setString(1, id);
						rs = pstmt.executeQuery();
						if(rs.next()){
								setId(rs.getString(1));
								setEmpid(rs.getString(2));
								setFullName(rs.getString(3));
								setRole(rs.getString(4));
								setActive(rs.getString(5));
								userExists = true;
						}
						else{
								msg = "No match found";
						}
				}
				catch(Exception ex){
						msg += " "+ex;
						logger.error(ex+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				return msg;
		}
	
}
