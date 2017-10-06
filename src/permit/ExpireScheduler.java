/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 */
package permit;
import org.quartz.TriggerBuilder;
import org.quartz.DateBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;
import java.util.List;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextListener;
import java.text.SimpleDateFormat;
import java.sql.*;
import org.apache.log4j.Logger;

public class ExpireScheduler{

		static boolean debug = false;
		final static SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		static Logger logger = Logger.getLogger(ExpireScheduler.class);
		final static long serialVersionUID = 2237L;
		final static String emailStr = "@bloomington.in.gov";
	
		boolean activeMail = false, needClean = false;
		Date fire_date = null;
		String id="", type="", expire_date="", url="";
	
		public void run() throws Exception {
				//
				if(needClean){
						String str = "row_";
						if(type.equals("bond"))
								str += "bond_";
						else
								str += "insur_";
						str += id;
						doClean(str);
				}
				try{
						String msg = "";
						logger.debug("------- Initializing ----------------------");
			
						// First we must get a reference to a scheduler
						// SchedulerFactory sf = new StdSchedulerFactory();
						// Scheduler sched = sf.getScheduler();
			
						logger.debug("------- Initialization Complete -----------");
			
						// computer a time that is on the next round minute
						//  Date runTime = evenMinuteDate(new Date());
			
						logger.debug("------- Scheduling Job  -------------------");
			
						// define the job and tie it to our Job class
			
						String jobName = "";
						String groupName = "";
						if(type.equals("bond")){
								jobName = "row_bond_"+id;
								groupName="row_bond";
						}
						else if(type.equals("insurance")){
								jobName = "row_insur_"+id;
								groupName="row_insur";
						}
						// System.err.println("ES job "+jobName);
						// System.err.println("ES group "+groupName);
						if(jobName.equals("")){
								logger.error(" bond or insurance id not set ");
								return;
						}
						if(fire_date == null){
								logger.error(" notification date not set ");
								return;
						}
						//System.err.println(" before job");
						JobDetail job = JobBuilder.newJob(SendEmailJob.class)
								.withIdentity(jobName, groupName)
								.build();
			
						// JobDetail job = new JobDetail();
						// job.setName(jobName);
						// job.setJobClass(InventoryJob.class);
						//
						// pass initialization parameters into the job
				
						// job.getJobDataMap().put("from","sibow"+emailStr);
						// job.getJobDataMap().put("subject","Bond/Insuranc expiration date notification");	   
						job.getJobDataMap().put("id",id);
						job.getJobDataMap().put("type",type);
						job.getJobDataMap().put("expire_date",expire_date);
						job.getJobDataMap().put("activeMail",""+activeMail);
						job.getJobDataMap().put("url",url);						
						//second minute hours day month (day of week) year
						// 
						// Trigger will run at 7am on the speciified date
						// cron date and time entries (year can be ignored)
						// second minute hour day-of-month month week-day year
						// you can use ? no specific value, 0/5 for incrment (every 5 seconds)
						// * for any value (in minutes mean every minute
						/*
							Trigger trigger = newTrigger()
							.withIdentity("trigger_"+month+"_"+day+"_"+year, "accrualGroup")
							.startAt(startDate)
							.withSchedule(cronSchedule("* * 8 0 0/2 * ,FRI")
							// .withMisfireHandlingInstructionFireNow())
							.withMisfireHandlingInstructionFireAndProceed())
							.endAt(endDate)						  
							// .withMisfireHandlingInstructionIgnoreMisfires())
							.build();
						*/
						Trigger trigger = TriggerBuilder.newTrigger()
								.withIdentity(jobName, groupName)
								.startAt(fire_date)
								.withSchedule(simpleSchedule()
															// .withIntervalInMinutes(5)
															// .withIntervalInHours(24*7) // 24*7 every weeks
															// .repeatForever()
															// .withRepeatCount(2) 
															// .withMisfireHandlingInstructionFireNow())
															.withMisfireHandlingInstructionIgnoreMisfires())
								.endAt(fire_date)						  
								.build();
			
						// Tell quartz to schedule the job using our trigger
						Scheduler sched = new StdSchedulerFactory().getScheduler();
						sched.start();
						sched.scheduleJob(job, trigger);
						// System.err.println(" after schedule ");
						//  logger.info(job.getKey() + " will run at: " + runTime);  
			
						// Start up the scheduler (nothing can actually run until the 
						// scheduler has been started)
						//	sched.start();

						/*
							logger.info("------- Started Scheduler -----------------");
			  
							// wait long enough so that the scheduler as an opportunity to 
							// run the job!
							logger.info("------- Waiting 65 seconds... -------------");
							try {
							// wait 65 seconds to show job
							Thread.sleep(65L * 1000L); 
							// executing...
							} catch (Exception e) {
							}
			  
							// shut down the scheduler
							logger.info("------- Shutting Down ---------------------");
							sched.shutdown(true);
							logger.info("------- Shutdown Complete -----------------");
						*/
				}catch(Exception ex){
						logger.error(ex);
						System.err.println(ex);
				}
    }
		public void setType(String val){
				if(val != null){
						type = val; // bond, insurance
				}
		}
		public void setId(String val){
				if(val != null){
						id = val;
				}
		}
		public void setExpire_date(String val){
				if(val != null){
						expire_date = val;
				}
		}
		public void setUrl(String val){
				if(val != null){
						url = val;
				}
		}
		public void setActiveMail(){
				activeMail = true;
		}
		public void setNeedClean(){
				needClean = true;
		}
		public void setFire_date(String val){
				if(val != null){
						try{
								fire_date = format.parse(val);
						}catch(Exception ex){
								logger.error(ex);
						}
				}
		}
		private String doClean(String val){
				String msg = "";
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				//
				// apps mysql is case sensetive
				
				String[] qa = {
						"delete from QRTZ_SIMPLE_TRIGGERS where trigger_name=?",
						"delete from QRTZ_FIRED_TRIGGERS where trigger_name=?",
						"delete from QRTZ_CRON_TRIGGERS where trigger_name=?",						
						"delete from QRTZ_TRIGGERS where trigger_name=?",						
						"delete from QRTZ_JOB_DETAILS where job_name=?",
						"delete from QRTZ_BLOB_TRIGGERS where trigger_name=?"
				};		
				if(val == null || val.equals("")){
						msg = " trigger name not set "+val;
						return msg;
				}
				String qq = "";
				try{
						con = Helper.getConnection();
						if(con == null){
								msg = "Could not connect to DB";
								return msg;
						}
						for(String str:qa){
								qq = str;
								logger.debug(str);
								pstmt = con.prepareStatement(str);
								pstmt.setString(1, val);
								pstmt.executeUpdate();
						}
				}catch(Exception ex){
						msg += ex+" "+qq;
						System.err.println(ex+" "+qq);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
				return msg;
				

		}
	
	
}
