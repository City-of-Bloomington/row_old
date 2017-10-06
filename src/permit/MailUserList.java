/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 */
package permit;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.naming.*;
import javax.naming.directory.*;
import org.apache.log4j.Logger;

public class MailUserList{

    boolean debug = false;
		static final long serialVersionUID = 1123L;		
		static Logger logger = Logger.getLogger(MailUserList.class);
		List<MailUser> mailUsers = null;
		String name = "", next_fire_time="";

    public MailUserList(){
    }	
    public MailUserList(boolean deb){
				debug = deb;
    }	
    //
    // setters
    //
		public List<MailUser> getMailUsers(){
				return mailUsers;
		}
		String find(){
				String msg = "";
				String qq = " select u.id,u.empid,u.fullname,u.role,u.active,m.top_user,m.inactive from users u, mail_receivers m where u.id=m.id ";
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String qo = " order by u.fullname ";
				qq += qo;
				// System.err.println(qq);
				logger.debug(qq);
				try{
						mailUsers = new ArrayList<MailUser>();
						con = Helper.getConnection();
						if(con == null){
								msg = "Could not connect ";
								return msg;
						}
						pstmt = con.prepareStatement(qq);
						rs = pstmt.executeQuery();	
						while(rs.next()){
								MailUser one = new MailUser(
																						rs.getString(1),
																						rs.getString(2),
																						rs.getString(3),
																						rs.getString(4),
																						rs.getString(5),
																						rs.getString(6),
																						rs.getString(7) != null
																						);
								mailUsers.add(one);
						}
				}catch(Exception e){
						msg += e+":"+qq;
						logger.error(msg);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				return msg;
			
		}
		/*
		String findNextFireTime(){
				String msg = "";
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				//		
				String qq = " select date_format(from_unixtime(next_fire_time/1000),'%W %m/%d/%Y %H:%i') from QRTZ_TRIGGERS"; // we will have only one record
				logger.debug(qq);
				try{
						con = Helper.getConnection();
						if(con == null){
								msg = "Could not connect to DB";
								return msg;
						}
						pstmt = con.prepareStatement(qq);
						rs = pstmt.executeQuery();
						if(rs.next()){ //  all what we need is one record
								next_fire_time = rs.getString(1);
						}
				}catch(Exception ex){
						msg += ex;
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				return msg;
		
		
		}
		*/	
}
