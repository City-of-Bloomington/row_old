/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 */
package permit;
import java.sql.*;
import javax.naming.*;
import javax.naming.directory.*;
import org.apache.log4j.Logger;


public class MailUser extends User implements java.io.Serializable{

    String top_user="", inactive="";
 
		static final long serialVersionUID = 133L;		
		static Logger logger = Logger.getLogger(MailUser.class);

    public MailUser(){
				super();
    }		
    public MailUser(String val){
				super(val);
    }	
    public MailUser(String val, String val2){
				super(val, val2);
    }
		public MailUser(String val, String val2,
										String val3, String val4, String val5,
										String val6,  boolean val7){
				super(val, val2, val3, val4, val5);
				setTop_user(val6);
				setInactive(val7);
		}
    //
		public boolean isTop_user(){
				return !top_user.equals("");
    }
		public void setTop_user(String val){
				if(val != null && !val.equals("")){
						top_user="y";
				}
		}
		public void setInactive(boolean val){
				if(!val)
						inactive = "y";
		}
	
    //
    // getters
    //
    public String getTop_user(){
				return top_user;
    }
		public boolean isInactive(){
				return !inactive.equals("");
		}
		public boolean isActive(){
				return !active.equals("") && inactive.equals("");
		}
		//
		// this is the same as user
		//
		@Override
		public int hashCode() {
				int hash = 7, id_int = 0;
				if(!id.equals("")){
						try{
								id_int = Integer.parseInt(id);
						}catch(Exception ex){}
				}
				hash = 67 * hash + id_int;
				return hash;
		}
		@Override
		public boolean equals(Object obj) {
				if (obj == null) {
						return false;
				}
				if (getClass() != obj.getClass()) {
						return false;
				}
				final User other = (User) obj;
				return this.id.equals(other.id);
		}		
    //
		@Override  	
		public String doSelect(){
				String msg="";
				PreparedStatement pstmt = null;
				Connection con = null;
				ResultSet rs = null;		
				String qq = "select u.id,u.empid,u.fullname,u.role,u.active,m.top_user,m.inactive from users u,mail_receivers m where u.id=m.id and ";
				if(!empid.equals("")){
						qq += " u.empid = ?";
				}
				else if(!id.equals("")){
						qq += " u.id = ?";
				}
				logger.debug(qq);
				con = Helper.getConnection();
				if(con == null){
						msg += " could not connect to database";
						return msg;
				}		
				try{
						pstmt = con.prepareStatement(qq);
						if(!empid.equals(""))
								pstmt.setString(1, empid);
						else
								pstmt.setString(1, id);				
						rs = pstmt.executeQuery();
						if(rs.next()){
								setValues(rs.getString(1),
													rs.getString(2),
													rs.getString(3),
													rs.getString(4),
													rs.getString(5));
								setTop_user(rs.getString(6));
								setInactive(rs.getString(7) != null);
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
		public String doSave(){
				String msg="";
				PreparedStatement pstmt = null;
				Connection con = null;
				ResultSet rs = null;		
				String qq = "insert into mail_notificatios values(?,?,null)";
				logger.debug(qq);
				con = Helper.getConnection();
				if(con == null){
						msg += " could not connect to database";
						return msg;
				}		
				try{
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, id);
						if(top_user.equals(""))
								pstmt.setNull(2, Types.CHAR);
						else
								pstmt.setString(2, "y");
						pstmt.executeUpdate();
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
		public String doUpdate(){
				String msg="";
				PreparedStatement pstmt = null;
				Connection con = null;
				ResultSet rs = null;		
				String qq = "update mail_notificatios set top_user=?,inactive=? where id=?";
				logger.debug(qq);
				con = Helper.getConnection();
				if(con == null){
						msg += " could not connect to database";
						return msg;
				}		
				try{
						pstmt = con.prepareStatement(qq);
						if(top_user.equals(""))
								pstmt.setNull(1, Types.CHAR);
						else
								pstmt.setString(1, "y");
						if(inactive.equals(""))
								pstmt.setNull(2, Types.CHAR);
						else
								pstmt.setString(2, "y");						
						pstmt.setString(3, id);
						pstmt.executeUpdate();
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
		public String doDelete(){
				String msg="";
				PreparedStatement pstmt = null;
				Connection con = null;
				ResultSet rs = null;		
				String qq = "delete from mail_notificatios where id=?";
				logger.debug(qq);
				con = Helper.getConnection();
				if(con == null){
						msg += " could not connect to database";
						return msg;
				}		
				try{
						pstmt = con.prepareStatement(qq);
						pstmt.setString(1, id);
						pstmt.executeUpdate();
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
