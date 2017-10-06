/**
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
package permit;
import java.io.*;
import java.sql.*;
import java.util.*;
import javax.sql.*;
import javax.naming.*;
import javax.naming.directory.*;
import java.security.MessageDigest;
import org.apache.log4j.Logger;

public class Helper{

		static boolean debug = false;
		static final long serialVersionUID = 131L;	
		static int c_con = 0;
    //
    // Non static variables
    //
		static Logger logger = Logger.getLogger(Helper.class);

    //
    // basic constructor
    public Helper(boolean deb){
				//
				// initialize
				//
    }
		//
		final static Connection getConnection(){

				Connection con = null;
				int trials = 0;
				boolean pass = false;
				while(trials < 3 && !pass){
						try{
								trials++;
								Context initCtx = new InitialContext();
								Context envCtx = (Context) initCtx.lookup("java:comp/env");
								DataSource ds = (DataSource)envCtx.lookup("jdbc/MySQL_excav");
								con = ds.getConnection();
								if(con == null){
										String str = " Could not connect to DB ";
										logger.error(str);
								}
								else{
										pass = testCon(con);
										if(pass){
												c_con++;
										}
								}
						}
						catch(Exception ex){
								logger.error(ex);
						}
				}
				return con;
		
		}
		final static boolean testCon(Connection con){
				boolean pass = false;
				Statement stmt  = null;
				ResultSet rs = null;
				String qq = "select 1+1";		
				try{
						if(con != null){
								stmt = con.createStatement();
								rs = stmt.executeQuery(qq);
								if(rs.next()){
										pass = true;
								}
						}
						rs.close();
						stmt.close();
				}
				catch(Exception ex){
						logger.error(ex+":"+qq);
				}
				return pass;
		}
		/**
     * Disconnect the database and related statements and result sets
     * 
     * @param con
     * @param stmt
     * @param rs
     */
	
    public final static void databaseDisconnect(Connection con,Statement stmt,
																								ResultSet rs) {
				try {
						if(rs != null) rs.close();
						rs = null;
						if(stmt != null) stmt.close();
						stmt = null;
						if(con != null) con.close();
						con = null;
			
						// logger.debug("Closed Connection "+c_con);
						c_con--;
						if(c_con < 0) c_con = 0;
				}
				catch (Exception e) {
						e.printStackTrace();
				}
				finally{
						if (rs != null) {
								try { rs.close(); } catch (SQLException e) { ; }
								rs = null;
						}
						if (stmt != null) {
								try { stmt.close(); } catch (SQLException e) { ; }
								stmt = null;
						}
						if (con != null) {
								try { con.close(); } catch (SQLException e) { ; }
								con = null;
						}
				}
    }
    public final static void databaseDisconnect(Connection con,
																								ResultSet rs,
																								Statement ... stmt) {
				try {
						if(rs != null) rs.close();
						rs = null;
						if(stmt != null){
								for(Statement stm:stmt){
										if(stm != null) stm.close();
										stm = null;
								}
						}
						if(con != null) con.close();
						con = null;
			
						// logger.debug("Closed Connection "+c_con);
						c_con--;
						if(c_con < 0) c_con = 0;
				}
				catch (Exception e) {
						e.printStackTrace();
				}
				finally{
						if (rs != null) {
								try { rs.close(); } catch (SQLException e) { ; }
								rs = null;
						}
						if (stmt != null) {
								for(Statement stm:stmt){
										try {
												if(stm != null)
														stm.close();
										}
										catch (SQLException e){}
										stm = null;
								}
						}
						if (con != null) {
								try { con.close(); } catch (SQLException e) { ; }
								con = null;
						}
				}
    }
    public final static int getCurrentYear(){

				int year = 2015;
				Calendar current_cal = Calendar.getInstance();
				year = current_cal.get(Calendar.YEAR);
				return year;
    }
    public final static int getCurrentQuarter(){

				int month = 1;
				Calendar current_cal = Calendar.getInstance();
				month = current_cal.get(Calendar.MONTH)+1;
				return month/3 + ((month%3 == 0)?0:1);
    }		
    //	
    public final static String getToday(){

				String day="",month="",year="";
				Calendar current_cal = Calendar.getInstance();
				int mm =  (current_cal.get(Calendar.MONTH)+1);
				int dd =   current_cal.get(Calendar.DATE);
				year = ""+ current_cal.get(Calendar.YEAR);
				if(mm < 10) month = "0";
				month += mm;
				if(dd < 10) day = "0";
				day += dd;
				return month+"/"+day+"/"+year;
    }
		public final static String getDateAfter(int days){
				String day="",month="",year="";
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, days);
				int mm =  (cal.get(Calendar.MONTH)+1);
				int dd =   cal.get(Calendar.DATE);
				year = ""+ cal.get(Calendar.YEAR);
				if(mm < 10) month = "0";
				month += mm;
				if(dd < 10) day = "0";
				day += dd;
				return month+"/"+day+"/"+year;

		}
		public final static String find3MonthFrom(String date){
				String ret = "";
				int mm=0,dd=0,yy = 0;
				if(date != null){
						int i1 = date.indexOf("/");
						int i2 = date.lastIndexOf("/");
						String month = date.substring(0,i1);
						String day = date.substring(i1+1,i2);
						String year = date.substring(i2+1);
						System.err.println(" mm dd yy "+month+" "+day+" "+year);
						try{
								mm = Integer.parseInt(month);
								dd = Integer.parseInt(day);
								yy = Integer.parseInt(year);
						}catch(Exception ex){}
				}
				if(yy > 0 && mm > 0){
						Calendar cal = Calendar.getInstance();
						cal.set(Calendar.DATE, dd);
						cal.set(Calendar.YEAR, yy);			
						cal.set(Calendar.MONTH, (mm-1+3));
						yy = cal.get(Calendar.YEAR);
						dd = cal.get(Calendar.DATE);
						mm = cal.get(Calendar.MONTH)+1;
						ret = mm > 9 ? ""+mm:"0"+mm;
						ret += "/";
						ret += dd > 9 ? ""+dd:"0"+dd;
						ret += "/";
						ret += ""+yy;
				}
				return ret;
		}

    public final static String getCurrentYearYY(){

				String year="";
				Calendar current_cal = Calendar.getInstance();
				year = ""+ current_cal.get(Calendar.YEAR);
				return year.substring(2);
    }	
    //
    // initial cap a word
    //
    final static String initCapWord(String str_in){
				String ret = "";
				if(str_in !=  null){
						if(str_in.length() == 0) return ret;
						else if(str_in.length() > 1){
								ret = str_in.substring(0,1).toUpperCase()+
										str_in.substring(1).toLowerCase();
						}
						else{
								ret = str_in.toUpperCase();   
						}
				}
				// System.err.println("initcap "+str_in+" "+ret);
				return ret;
    }
    //
    // init cap a phrase
    //
    final static String initCap(String str_in){
				String ret = "";
				if(str_in != null){
						if(str_in.indexOf(" ") > -1){
								String[] str = str_in.split("\\s"); // any space character
								for(int i=0;i<str.length;i++){
										if(i > 0) ret += " ";
										ret += initCapWord(str[i]);
								}
						}
						else
								ret = initCapWord(str_in);// it is only one word
				}
				return ret;
    }
	
}






















































