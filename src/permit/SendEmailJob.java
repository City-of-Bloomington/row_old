/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 */
package permit;

import java.util.*;
import java.io.*;
import java.text.*;
import javax.mail.*;
import javax.mail.Address;
import javax.mail.internet.*;
import javax.activation.*;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobDataMap;
import org.apache.log4j.Logger;


public class SendEmailJob implements Job{

    boolean debug = true;
		Logger logger = Logger.getLogger(SendEmailJob.class);
		long serialVersionUID = 2227L;
		String emailStr = "@bloomington.in.gov";
		String url = "";
		JobDataMap dataMap = null;
		boolean activeMail = false;
		String id="", type="", expire_date="";
		String from = "", to="", msg="", cc="";
		//
		// will send emails
		public SendEmailJob(){

		}
		public void execute(JobExecutionContext context)
        throws JobExecutionException {
				try{
						JobDataMap dataMap = context.getJobDetail().getJobDataMap();
						logger.debug("map = " + dataMap.entrySet());
						logger.debug("dataMap = " + context.getJobDetail().getJobDataMap());
						id = (String)dataMap.get("id");
						type = (String)dataMap.get("type");
						expire_date = (String)dataMap.get("expire_date");
						String str = (String)dataMap.get("activeMail");
						if(str != null && str.equals("true")){
								activeMail = true;
						}
						str = (String)dataMap.get("url");
						if(str != null){
							 url = str;
						}						
						doInit();
						doWork();
						doDestroy();
				}
				catch(Exception ex){
						logger.error(ex);
						System.err.println(ex);
				}
		}
		public void doInit(){

				//
				// good idea to clear old data from quartz tables
				//

		}
		public void doDestroy() {
				// inventory = null;
		}	    
    /**
     * 
     */
    public void doWork(){

				String msg = "";
				String from = "";
				String to = "";
				String cc = "";
				String bcc = "";
		
				List<MailUser> users = null;
				MailUserList ml = new MailUserList();
		
				String back = ml.find();
				if(back.equals("")){
						users = ml.getMailUsers();
				}
				if(users != null){
						MailUser temp = null;
						for(MailUser one:users){
								if(one.isTop_user()){
										from = one.getEmpid()+emailStr;
										to = one.getEmpid()+emailStr;
								}
								else{
										if(!cc.equals("")){
												cc +=",";
										}
										cc += one.getEmpid()+emailStr;
										temp = one;
								}
						}
						// if no top user 
						if(temp != null && from.equals("")){
								from = temp.getEmpid()+emailStr;
								to = temp.getEmpid()+emailStr;
						}
				}
				// back = ml.doClean(); // delete old record from quartz tables;
				if(!back.equals("")){
						logger.error(back);
				}
				msg = " Our record indicate that "+type+" with ID "+id+" will expire on "+expire_date+"\n";
				msg += " for more info click "+
						"<a href=\""+url+"?"+type+".action?id="+id+"\"> here </a>\n";
				msg += " inform the related contractor/developer to renew their "+type+" otherwise their records will be flagged and they may not be able to obtain future permits\n";
				msg += "\n\n";
				msg += "thanks\n";
				msg += "\n\n";
				/*
				System.err.println(" from "+from);
				System.err.println(" to "+to);
				System.err.println(" cc "+cc);
				System.err.println(" msg "+msg);
				*/
				if(activeMail){
						sendEmail(from, to, cc, bcc, msg);
				}
		}
		String sendEmail(String from, String to, String cc, String bcc,
										 String msgText){
				String message= "";
				String host = "localhost"; // whoville.city. ...
				String subject = "Bond/Insurance expiration date notification";
				try {
						//
						// create some properties and get the default Session
						//
						Properties props = new Properties();
						props.put("mail.smtp.host", host);
						if (debug) props.put("mail.debug", "true");
			
						Session session = Session.getDefaultInstance(props, null);
						session.setDebug(debug);
						//
						// create a message
						//
						Message msg = new MimeMessage(session);
						msg.setFrom(new InternetAddress(from));
						if(to != null && !to.equals("")){
								InternetAddress[] address = {new InternetAddress(to)};
								msg.setRecipients(Message.RecipientType.TO, address);
						}
						if(cc != null && !cc.equals("")){
								InternetAddress[] address2 = javax.mail.internet.InternetAddress.parse(cc);
								msg.addRecipients(Message.RecipientType.CC, address2);
						}
						if(bcc != null && !bcc.equals("")){
								InternetAddress[] address3 = javax.mail.internet.InternetAddress.parse(bcc);
								msg.setRecipients(Message.RecipientType.BCC, address3);
						}
						msg.setSubject(subject);
						msg.setSentDate(new Date());

						// If the desired charset is known, you can use
						// setText(text, charset)
						msg.setText(msgText);
						//
						Transport.send(msg);
				} catch (MessagingException mex){

						logger.debug("\n--Exception handling in MailHandle.java");
						//   mex.printStackTrace();
						message += " Exception in MailHandle "+mex;
						Exception ex = mex;
						do {
								if (ex instanceof SendFailedException) {
										SendFailedException sfex = (SendFailedException)ex;
										Address[] invalid = sfex.getInvalidAddresses();
										if (invalid != null) {
												System.out.println("    ** Invalid Addresses");
												if (invalid != null) {
														for (int i = 0; i < invalid.length; i++) 
																message += "         " + invalid[i];
												}
										}
										Address[] validUnsent = sfex.getValidUnsentAddresses();
										if (validUnsent != null) {
												System.out.println("    ** ValidUnsent Addresses");
												if (validUnsent != null) {
														for (int i = 0; i < validUnsent.length; i++) 
																message += "         "+validUnsent[i];
												}
										}
										Address[] validSent = sfex.getValidSentAddresses();
										if (validSent != null) {
												System.out.println("    ** ValidSent Addresses");
												if (validSent != null) {
														for (int i = 0; i < validSent.length; i++) 
																message += "         "+validSent[i];
												}
										}
								}
								System.out.println();
								if (ex instanceof MessagingException){
										ex = ((MessagingException)ex).getNextException();
								}
								else{
										ex = null;
								}
						} while (ex != null);
						logger.error(message);
				}
				return message;
		}
}






















































