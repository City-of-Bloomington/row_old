/**
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
package permit;
import java.sql.*;
import java.text.*;
import java.util.List;
import java.util.ArrayList;
import org.apache.log4j.Logger;


public class Invoice implements java.io.Serializable{

    String id="", status="", date="",
				company_contact_id="", start_date="", end_date="",
				notes="", user_id="", invoice_num="";
		String permit_id=""; // needed when we come from a permit
		static final int page_size = 30, first_page = 20;
		static final long serialVersionUID = 223L;	
		static Logger logger = Logger.getLogger(Invoice.class);
		static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		CompanyContact companyContact = null;
		Company company = null;
		Contact contact = null;
		Permit permit = null;
		Receipt receipt = null;
		List<Permit> permits = null;
		List<Permit> newPermits = null;
		List<Page> pages = null;
		String[] add_permits = null;
    public Invoice(){

    }
    public Invoice(String val){
				setId(val);
    }
    public Invoice(String val,
									 String val2,
									 String val3,
									 String val4,
									 String val5,
									 String val6,
									 String val7,
									 String val8
									 ){
				setValues(val, val2, val3, val4, val5, val6, val7, val8);
    }
		void setValues(
									 String val,
									 String val2,
									 String val3,
									 String val4,
									 String val5,
									 String val6,
									 String val7,
									 String val8
									 ){
				setId(val);
				setStatus(val2);
				setDate(val3);
				setCompany_contact_id(val4);
				setStart_date(val5);
				setEnd_date(val6);
				setNotes(val7);
				setInvoice_num(val8);
		}
		//
		// setters
		//
		public void setId(String val){
				if(val != null)
						id = val;
    }
		public void setStatus(String val){
				if(val != null && !val.equals("-1"))
						status = val;
    }
		public void setDate(String val){
				if(val != null)
						date = val;
    }
		public void setCompany_contact_id(String val){
				if(val != null)
						company_contact_id = val;
    }
		public void setStart_date(String val){
				if(val != null)
						start_date = val;
    }
		public void setEnd_date(String val){
				if(val != null)
						end_date = val;
    }
		public void setNotes(String val){
				if(val != null)
						notes = val;
    }
		public void setUser_id(String val){
				if(val != null)
						user_id = val;
    }
		public void setPermit_id(String val){
				if(val != null){
						permit_id = val;
						setInvoiceDatesUsingPermit();
				}
    }
		public void setInvoice_num(String val){
				if(val != null)
						invoice_num = val;
    }		
    //
    // getters
    //
		public String getId(){
				return id;
    }	
    public String getStatus(){
				if(status.equals("") && id.equals("")){
						return "Billed";
				}
				return status;
    }
		public String getDate(){
				if(id.equals("") && date.equals("")){
						date = Helper.getToday();
				}
				return date;
    }	
    public String getCompany_contact_id(){
				if(company_contact_id.equals("") && hasPermit()){
						findCompanyContactFromPermit();
				}
				return company_contact_id;
    }
		public String getStart_date(){
				return start_date;
    }	
    public String getEnd_date(){
				return end_date;
    }

		public String getNotes(){
				return notes;
    }
		public String getInvoice_num(){
				if(id.equals("")){
						if(start_date.equals("")){
								setInvoiceDatesUsingPermit();
						}
						if(start_date.equals("")){
								int year = Helper.getCurrentYear() - 2000;
								int quarter = Helper.getCurrentQuarter();
								invoice_num = ""+year+""+quarter+"0";
						}
						else{
								try{
										int yy = Integer.parseInt(start_date.substring(8));
										int mm = Integer.parseInt(start_date.substring(0,2));
										int quarter = mm/3 + ((mm%3 == 0)?0:1);
										invoice_num = ""+yy+""+quarter;
										getCompany();
										if(company != null){
												String co_name = company.getName();
												if(co_name != null){
														if(co_name.contains("Vectern")){
																invoice_num += "1";
														}													 
														else if(co_name.contains("Duke")){
																invoice_num += "2";
														}
														else if(co_name.contains("Smithville")){
																invoice_num += "3";
														}
														else{
																invoice_num += "0";
														}
												}
										}
								}catch(Exception ex){}								 
						}
				}
				return invoice_num;
    }		

		public String toString(){
				return status;
		}
		void setInvoiceDatesUsingPermit(){
				getPermit();
				if(permit != null){
						start_date = permit.getStart_date();
						end_date = Helper.find3MonthFrom(start_date);
				}
		}
		public String getTotal(){
				double total = 0.0;
				if(!id.equals("")){
						findRelatedPermits();
				}
				if(id.equals("") || permits == null){
						findUnpaidPermits();
				}
				if(permits != null){
						for(Permit one:permits){
								total += one.getFeeDbl();
						}
				}
				NumberFormat formatter = NumberFormat.getCurrencyInstance();
				String str = formatter.format(total);
				return str;
		}
		public CompanyContact getCompanyContact(){
				if(companyContact == null){
						if(company_contact_id.equals("") && !permit_id.equals("")){
								getPermit();
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
				if(contact == null){
						getCompanyContact();
						if(companyContact != null){
								contact = companyContact.getContact();
						}
				}
				return contact;
		}
		public Company getCompany(){
				if(company == null){
						getCompanyContact();
						if(companyContact != null){
								company =  companyContact.getCompany();
						}
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
				return permit;
		}
		public Receipt getReceipt(){
				if(receipt == null && !id.equals("")){
						ReceiptList rl = new ReceiptList(id);
						String back = rl.find();
						if(back.equals("")){
								List<Receipt> ones = rl.getReceipts();
								if(ones != null && ones.size() > 0){
										receipt = ones.get(0);
								}
						}
				}
				return receipt;
		}	
		public boolean hasCompany(){
				getCompany();
				return company != null;
		}
		public boolean hasContact(){
				getContact();
				return contact != null;
		}
		public boolean hasReceipt(){
				getReceipt();
				return receipt != null;
		}	
		public String getPermit_id(){
				return permit_id;
    }
		public boolean hasPermit(){
				return !permit_id.equals("");
		}
		public boolean hasPermits(){
				getPermits();
				return permits != null && permits.size() > 0;
		}
		public boolean isStillOpen(){
				if(!id.equals("") && !status.equals("Billed")){
						return false;
				}
				return true;
		}
		public boolean canAddPermits(){
				if(!id.equals("") && status.equals("Paid")){
						return false;
				}
				findUnpaidPermits();
				if(id.equals("")){
						return permits != null && permits.size() > 0;
				}
				else{
						return newPermits != null && newPermits.size() > 0;
				}
		}
		public List<Permit> getPermits(){
				if(permits == null){
						getTotal(); // will cause to find permits
				}
				return permits;
		}
		public List<Permit> getNewPermits(){
				return newPermits;
		}		
		public void setAdd_permits(String[] vals){
				add_permits = vals;
		}
		//
		// we need this for printed invoice
		//
		public String getToday(){
				return Helper.getToday();
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
		public Integer getNeededLines(boolean first){
				// we assume we need at least the following number of lines
				// in the invoice details print out (in addition to header)
				int totalLines = page_size; // to start with
				if(first){
						totalLines = first_page;
				}
				getPermits();
				if(permits != null){
						int size = permits.size();
						for(Permit one:permits){
								List<Excavation> list = one.getExcavations();
								if(list != null)
										size += list.size();
						}
						if(totalLines > size){
								totalLines -= size;
						}
						else{
								totalLines = 1;
						}
				}
				return new Integer(totalLines);
		}
		public int getTotalLines(){
				int totalLines = 0;
				getPermits();
				if(permits != null){
						int size = permits.size();
						for(Permit one:permits){
								totalLines += 1;
								List<Excavation> list = one.getExcavations();
								if(list != null){
										totalLines += list.size();
								}
						}
				}
				return totalLines;		
		}

		public void createPages(){
				int total = getTotalLines();
				int page_count = 1;
				if(total > first_page){
						total = total - first_page;
				}
				if(total > page_size){
						page_count += (total / page_size) ;
				}
				if(page_count > 1 && total % page_size > 0){
						page_count++;
				}
				pages = new ArrayList<Page>(page_count);
				int jj=1;
				int size = 0;
				//
				// the first page
				//
				Page page = new Page("Page "+jj+" of "+page_count);		
				for(Permit one:permits){
						size += 1; // for permit
						List<Excavation> list = one.getExcavations();
						if(list != null){
								size += list.size(); // for excavations
						}
						page.add(one);
						if(jj == 1){
								if(size >= first_page){
										pages.add(page);
										size = 0;
										jj++;
										page = new Page("Page "+jj+" of "+page_count);	
								}
						}
						else{
								if(size >= page_size){
										pages.add(page);
										size = 0;
										jj++;
										page = new Page("Page "+jj+" of "+page_count);	
								}
						}
				}
				if(size > 0){ // last page if not even
						if(jj == 1)
								page.setNeedLines(first_page - size);
						else
								page.setNeedLines(page_size - size);
						pages.add(page);
				}
		}
		public List<Page> getPages(){
				if(pages == null){
						createPages();
				}
				return pages;
		}
		public boolean equals(Object gg){
				boolean match = false;
				if (gg != null && gg instanceof Invoice){
						match = id.equals(((Invoice)gg).id);
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
		/**
		 * Given the start date and end date for the invoice
		 * we want to find all the permits that will be included
		 */
		String doNext(){
				String back = findUnpaidPermits();
				if(!back.equals("")){
						return back;
				}
				if(permits == null || permits.size() == 0){
						back = "No match found";
				}
				return back;
		}
	
    //
		public String doSelect(){
		
				String msg="";
		
				String qq = "select id,status,date_format(date,'%m/%d/%Y'),company_contact_id,date_format(start_date,'%m/%d/%Y'),date_format(end_date,'%m/%d/%Y'),notes,invoice_num from "+
						"invoices where id=?";
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
															rs.getString(5),
															rs.getString(6),
															rs.getString(7),
															rs.getString(8));
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
				String qq = "insert into invoices values (0,"+
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
				findUnpaidPermits();
				if(hasPermits()){
						msg += addInvoiceToPermits();
				}
				return msg;
    }
		private String addInvoiceToPermits(){
				String msg = "";
				if(add_permits != null){
						for(String str:add_permits){
								Permit one = new Permit(str);
								msg = one.doSelect();
								one.setInvoice_id(id);
								msg += one.doUpdate();
						}
				}
				return msg;
		}
		public String setFields(PreparedStatement pstmt){

				String msg = "";
				String today = Helper.getToday();
				int jj = 1;
				try{
						if(status.equals(""))
								pstmt.setNull(jj++, Types.VARCHAR);
						else
								pstmt.setString(jj++, status);
						if(date.equals(""))
								pstmt.setNull(jj++, Types.VARCHAR);
						else
								pstmt.setDate(jj++, new java.sql.Date(dateFormat.parse(date).getTime()));		
						if(company_contact_id.equals(""))	
								pstmt.setNull(jj++, Types.VARCHAR);
						else				
								pstmt.setString(jj++, company_contact_id);
						if(start_date.equals(""))
								pstmt.setNull(jj++, Types.VARCHAR);
						else
								pstmt.setDate(jj++, new java.sql.Date(dateFormat.parse(start_date).getTime()));
						if(end_date.equals(""))
								pstmt.setNull(jj++, Types.VARCHAR);
						else
								pstmt.setDate(jj++, new java.sql.Date(dateFormat.parse(end_date).getTime()));			
						if(notes.equals(""))
								pstmt.setNull(jj++, Types.VARCHAR);
						else
								pstmt.setString(jj++, notes);
						if(invoice_num.equals("") && !id.equals("")){
								invoice_num = id;
						}
						if(invoice_num.equals(""))
								pstmt.setNull(jj++, Types.VARCHAR);
						else
								pstmt.setString(jj++, invoice_num);						
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
						qq = "update invoices set ";
						qq += "status = ?, ";
						qq += "date = ?,"; 
						qq += "company_contact_id =?,"; 
						qq += "start_date =?,";
						qq += "end_date = ?,";
						qq += "notes = ?, ";
						qq += "invoice_num = ? ";
						qq += "where id=? ";
						logger.debug(qq);
						pstmt = con.prepareStatement(qq);
						setFields(pstmt);
						pstmt.setString(8, id);
						pstmt.executeUpdate();
						addInvoiceToPermits();
				}
				catch (Exception ex){
						msg += ex+":"+qq;
						logger.error(ex+":"+qq);
				}
				finally{
						Helper.databaseDisconnect(con, pstmt, rs);
				}
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
				String qq = "delete from invoices where id=?";
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
		/**
		 * Find all permits that belong to this invoice
		 */
		private String findRelatedPermits(){
				String msg = "";
				if(id.equals("")){
						return msg;
				}
				PermitList pl = new PermitList();
				pl.setInvoice_id(id);
				pl.setNoLimit();
				pl.setSortBy("p.date");
				msg = pl.find();
				if(msg.equals("")){
						permits = pl.getPermits();
				}
				return msg;		

		}
		/**
		 * given the company id we need to find all the permits that belongs to
		 * the company that is not associated with any invoice yet
		 * (the permit will have invoice_id as null)
		 */
		private String findUnpaidPermits(){
				String msg = "", company_id="", contact_id="";
				if(company == null){
						getCompany();
				}
				if(company != null){
						company_id = company.getId();
				}
				else if(contact != null){
						contact_id = contact.getId();
				}
				if(company_id.equals("") && contact_id.equals("")){
						return "No company or contact available"; 
				}
				PermitList pl = new PermitList();
				if(!company_id.equals("")){
						pl.setCompany_id(company_id);
				}
				else {
						pl.setContact_id(contact_id);
				}
				pl.setNoInvoice();
				if(!start_date.equals("")){
						pl.setDate_from(start_date);
				}
				if(!end_date.equals("")){
						pl.setDate_to(end_date);
				}
				msg = pl.find();
				if(msg.equals("")){
						if(id.equals(""))
								permits = pl.getPermits();
						else
								newPermits = pl.getPermits();
				}
				return msg;
		}
	
}
