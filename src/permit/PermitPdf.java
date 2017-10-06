/**
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
package permit;

import java.util.*;
import java.sql.*;
import java.io.*;
import javax.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.naming.*;
import javax.naming.directory.*;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Chunk;
import com.lowagie.text.Phrase;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Font;
import com.lowagie.text.html.HtmlWriter;
import com.lowagie.text.pdf.PdfWriter;
// import com.lowagie.text.rtf.RtfWriter2;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.Image;
import org.apache.log4j.Logger;


public class PermitPdf extends HttpServlet {

    String url="",url2="";
		static Logger logger = Logger.getLogger(PermitPdf.class);
    boolean debug = false;
		static final long serialVersionUID = 1136L;
		static final String[] conditions = {
				"Permittee is required to call the Planning and Transportation Department at 812-349-3423 for inspection of any work at the City of Bloomington Right of way prior to placing any concrete, or at any point specified in the Specific Conditions of Approval.",
				"This approval only covers concerns within the jurisdictions of the City of Bloomington Planning and Transportaion Department, other entities or agencies may also need to grant approval for work done in the course of this project.",
				"Projects shall conform to all current A.D.A.,(Americans with Disabilities Act) requirements.",
				"All utility work shall conform to specifications to be obtained from the owner of the affected utility, and work on said utility shall be performed only with expressed permission of its owner. It shall be the responsibility of the permittee to obtain any necessary inspections or approvals from the owner of the utilites involved.",
				"Permittee shall be responsible for supplying and placing all required signs and barricades. All signs and barricades, and their placement, shall conform to current M.U.T.C.D. and I.N.D.O.T. standards. All required traffic control measures shall be in place before work begins.",
				"Erosion control measures complying with Bloomington Municipal Code 20.05.040 are required to be in place during the period of any earth disturbing activities, and remain in place until the site is stablized.",
				"All bonding must remain current until a written release of such bonding is given by a representitive of the City of Bloomington Planning and Transportation Department.",
				"Any work in a street shall conform to the City of Bloomington Street Cut Requirements. Copies of these requirements are available from Planning and Transportation Department. All damaged Public Improvements must be repaired to prior or improved conditions.",
				"Any brick or inlaid limestone sidewalks, or brick surfaced streets, shall remain undisturbed, unless specific permission is given by a representitive of the Planning and Transportation Department. If they are disturbed the surface material shall be taken up, saved, and re-installed to City of Bloomington specifications. Backfill methods and materials must also meet these specifications.",
				"Any damage to any underground facility or utility must be reported immediately to the City of Bloomington Planning and Transportation Department and the owner of the facility or utility (if known). If not, the permittee will be required to re-excavate the damaged facility or utility, at their expense, to demonstrate that repairs have been made to the satisfaction of the owner of the damaged facility or utility,",
				"Any above ground appurtenances (line markers, switch boxes, meters, etc,), or structures, to be placed in the City Right of Way must be approved the City of Bloomington Planning and Transportation Department prior to installation.",
				"All existing regulatory signs (STOP, YIELD, NO PARKING, etc.) that are owned by the City of Bloomington shall be removed and replaced only by the City of Bloomington Traffic Division. Any regulatory signs removed, or installed, by the permittee are subject to removal or replacement by the Traffic Division with Permittee being billed for time and materials."

		};
		//
		public void doPost(HttpServletRequest req, 
											 HttpServletResponse res) 
				throws ServletException, IOException{
				doGet(req, res);
		}
    public void doGet(HttpServletRequest req, 
											HttpServletResponse res) 
				throws ServletException, IOException{
				res.setContentType("text/html");

				String id="", date_from="",date_to="", process_date="";
	    
				String name, value, message="", action="";
				Enumeration<String> values = req.getParameterNames();
				if(url.equals("")){
						url    = getServletContext().getInitParameter("url");
				}
				User user = null;
				HttpSession session = req.getSession(false);
				if(session != null){
						user = (User)session.getAttribute("user");
						if(user == null){
								String str = url+"Login";
								res.sendRedirect(str);
								return; 
						}
				}
				else{
						String str = url+"Login";
						res.sendRedirect(str);
						return; 
				}
				String [] vals;
				Permit permit = new Permit();
				while (values.hasMoreElements()){
						name = values.nextElement().trim();
						vals = req.getParameterValues(name);
						value = vals[vals.length-1].trim();
			
						if (name.equals("id")) {
								permit.setId(value);
						}			
						else if (name.equals("action")) {
								action=value;
						}
				}
				if(!action.equals("")){ 
						String back = permit.doSelect();
						if(!back.equals("")){
								message += back;
						}
						else {
								writePage(res, permit);
								return;
						}
				}
				PrintWriter out = res.getWriter();			  
				if(!message.equals("")){
						out.println("<html><head><title>ROW Permits</title>");
						out.println("</head><body>");
						out.println("<center>");
						out.println("<p>"+message+"</p>");
						out.println("<br />");
						out.print("</center></body></html>");
						out.close();
				}
				else{
						out.println("<html><head><title>ROW Permits</title>");
						out.println("</head><body>");
						out.println("<center>");
						if(permit == null)
								out.println("<p>No permit found</p>");				
						out.println("<br />");
						out.print("</center></body></html>");
						out.close();			
				}
    }
		/**
		 * handles the letter header
		 */
		PdfPTable getHeader(){
				//
				String str = "";
				String spacer = "   ";
				//
				PdfPTable headTable = null;
				try{
						//
						// for http url use
						//
						Image image = Image.getInstance(url+"js/images/city_logo3.jpg");

						Font fnt = new Font(Font.TIMES_ROMAN, 10, 
																Font.NORMAL);
						Font fntb = new Font(Font.TIMES_ROMAN, 10, 
																 Font.BOLD);
						float[] widths = {25f, 40f, 35f}; // percentages
						headTable = new PdfPTable(widths);
						headTable.setWidthPercentage(100);
						headTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						headTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						// image.setWidthPercentage(33.0f);
						image.scalePercent(15f);
						PdfPCell cell = new PdfPCell(image);
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						headTable.addCell(cell);
						//
						float[] width ={33f}; 
						PdfPTable midTable = new PdfPTable(width);
						midTable.setWidthPercentage(33);
						midTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						midTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						Phrase phrase = new Phrase();
						Chunk ch = new Chunk("City of Bloomington ",fntb);
						phrase.add(ch);
						cell = new PdfPCell(phrase);
						cell.setBorder(Rectangle.NO_BORDER);
						midTable.addCell(cell);
						//
						phrase = new Phrase();
						ch = new Chunk("Planning and Transportation Department ",fntb);
						phrase.add(ch);
						cell = new PdfPCell(phrase);
						cell.setBorder(Rectangle.NO_BORDER);
						midTable.addCell(cell);
						//
						phrase = new Phrase();
						ch = new Chunk("bloomington.in.gov", fntb);
						phrase.add(ch);
						cell = new PdfPCell(phrase);
						cell.setBorder(Rectangle.NO_BORDER);
						midTable.addCell(cell);
						//
						headTable.addCell(midTable);
						//
						PdfPTable rightTable = new PdfPTable(width);
						rightTable.setWidthPercentage(33);
						rightTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						rightTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);			
						phrase = new Phrase();
						ch = new Chunk("401 N Morton St Suite 130 ",fntb);
						phrase.add(ch);
						cell = new PdfPCell(phrase);
						cell.setBorder(Rectangle.NO_BORDER);
						rightTable.addCell(cell);
						//
						phrase = new Phrase();
						ch = new Chunk("PO Box 100 \nBloomington, IN 47404",fntb);
						phrase.add(ch);
						cell = new PdfPCell(phrase);
						cell.setBorder(Rectangle.NO_BORDER);
						rightTable.addCell(cell);			
						//
						phrase = new Phrase();
						ch = new Chunk("\n Phone: (812) 349-3423\nFax (812) 349-3520\nEmail: planning@bloomington.in.gov",fntb);
						phrase.add(ch);
						cell = new PdfPCell(phrase);
						cell.setBorder(Rectangle.NO_BORDER);
						rightTable.addCell(cell);
						headTable.addCell(rightTable);
				}
				catch(Exception ex){
						logger.error(ex);
				}
				return headTable;
		}
		//
    void writePage(HttpServletResponse res,
									 Permit permit){
				//
				// paper size legal (A4) 8.5 x 11
				// page 1-inch = 72 points
				//
				String fileName = "row_permit_"+permit.getId()+".pdf";
				Rectangle pageSize = new Rectangle(612, 792); // 8.5" X 11"
        Document document = new Document(pageSize, 36, 36, 18, 18);
				ServletOutputStream out = null;
				Font fnt = new Font(Font.TIMES_ROMAN, 10, Font.NORMAL);
				Font fntb = new Font(Font.TIMES_ROMAN, 10, Font.BOLD);
				Font fnts = new Font(Font.TIMES_ROMAN, 8, Font.NORMAL);
				Font fntbs = new Font(Font.TIMES_ROMAN, 8, Font.BOLD);		
				String spacer = "   ";
				PdfPTable header = getHeader();
				Bond bond = permit.getBond();
				Invoice invoice = permit.getInvoice();
				if(bond == null) bond = new Bond();
				if(invoice == null) invoice = new Invoice();
				try{
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						PdfWriter writer = PdfWriter.getInstance(document, baos);
						String str = "";
						document.open();
						document.add(header);
						//
						// title
						float[] width = {100f}; // one cell
						PdfPTable table = new PdfPTable(width);
						table.setWidthPercentage(100.0f);
						PdfPCell cell = new PdfPCell(new Phrase("Right Of Way Excavation Permit", fntb));
						//			
						cell.setBorder(Rectangle.BOTTOM);
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						table.addCell(cell);
						document.add(table);
						//
						// we need these later
						Paragraph pp = new Paragraph();
						Chunk ch = new Chunk(" ", fntb);
						Phrase phrase = new Phrase();			
						//
						float[] widths = {14f, 20f, 17f, 18f, 13f, 20f}; // percentages
						table = new PdfPTable(widths);
						table.setWidthPercentage(100.0f);
						table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						//
						// first row
						cell = new PdfPCell(new Phrase("Company", fntb));
						cell.setBorder(Rectangle.NO_BORDER);
						table.addCell(cell);
						cell = new PdfPCell(new Phrase(permit.getCompany().getName(), fnt));
						cell.setBorder(Rectangle.NO_BORDER);
						table.addCell(cell);
						cell = new PdfPCell(new Phrase("Status", fntb));
						cell.setBorder(Rectangle.NO_BORDER);
						table.addCell(cell);
						cell = new PdfPCell(new Phrase(permit.getStatus(), fnt));
						cell.setBorder(Rectangle.NO_BORDER);
						table.addCell(cell);
						cell = new PdfPCell(new Phrase("Permit", fntb));
						cell.setBorder(Rectangle.NO_BORDER);
						table.addCell(cell);
						cell = new PdfPCell(new Phrase(permit.getPermit_num(), fnt));
						cell.setBorder(Rectangle.NO_BORDER);
						table.addCell(cell);
						//
						// 2nd row
						//
						cell = new PdfPCell(new Phrase("Responsible", fntb));
						cell.setBorder(Rectangle.NO_BORDER);
						table.addCell(cell);
						cell = new PdfPCell(new Phrase(permit.getContact().getFullName(), fnt));
						cell.setBorder(Rectangle.NO_BORDER);
						table.addCell(cell);
						cell = new PdfPCell(new Phrase("Inspector", fntb));
						cell.setBorder(Rectangle.NO_BORDER);
						table.addCell(cell);
						cell = new PdfPCell(new Phrase(permit.getReviewer().getFullName(), fnt));
						cell.setBorder(Rectangle.NO_BORDER);
						table.addCell(cell);			
						cell = new PdfPCell(new Phrase("Date Issued", fntb));
						cell.setBorder(Rectangle.NO_BORDER);
						table.addCell(cell);
						cell = new PdfPCell(new Phrase(permit.getDate(), fnt));
						cell.setBorder(Rectangle.NO_BORDER);
						table.addCell(cell);
						//
						// 3rd row
						cell = new PdfPCell(new Phrase("Project", fntb));
						cell.setBorder(Rectangle.NO_BORDER);
						table.addCell(cell);
						cell = new PdfPCell(new Phrase(permit.getProject(), fnt));
						cell.setBorder(Rectangle.NO_BORDER);
						table.addCell(cell);
						cell = new PdfPCell(new Phrase("Permit Fee", fntb));
						cell.setBorder(Rectangle.NO_BORDER);
						table.addCell(cell);
						cell = new PdfPCell(new Phrase("$"+permit.getFee(), fnt));
						cell.setBorder(Rectangle.NO_BORDER);
						table.addCell(cell);
						cell = new PdfPCell(new Phrase("Start Date", fntb));
						cell.setBorder(Rectangle.NO_BORDER);
						table.addCell(cell);
						cell = new PdfPCell(new Phrase(permit.getStart_date(), fnt));
						cell.setBorder(Rectangle.NO_BORDER);
						table.addCell(cell);			
						//
						// 4th row
						cell = new PdfPCell(new Phrase("Bond Amount", fntb));
						cell.setBorder(Rectangle.NO_BORDER);
						table.addCell(cell);
						cell = new PdfPCell(new Phrase(bond.getAmountStr(), fnt));
						cell.setBorder(Rectangle.NO_BORDER);
						table.addCell(cell);
						cell = new PdfPCell(new Phrase("Expiration Date", fntb));
						cell.setBorder(Rectangle.NO_BORDER);
						table.addCell(cell);
						cell = new PdfPCell(new Phrase(bond.getExpire_date(), fnt));
						cell.setBorder(Rectangle.NO_BORDER);
						table.addCell(cell);
						cell = new PdfPCell(new Phrase("Invoice", fntb));
						cell.setBorder(Rectangle.NO_BORDER);
						table.addCell(cell);
						cell = new PdfPCell(new Phrase(invoice.getStatus(), fnt));
						cell.setBorder(Rectangle.NO_BORDER);
						table.addCell(cell);			
						document.add(table);
						//
						phrase = new Phrase(new Chunk(spacer, fnt));
						document.add(phrase);
						//
						float[] widths2 = {25f, 15f, 15f, 25f, 10f, 10f}; 		
						table = new PdfPTable(widths2);
						table.setWidthPercentage(100.0f);
						cell = new PdfPCell(new Phrase("Address", fntb));
						table.addCell(cell);			
						cell = new PdfPCell(new Phrase("Cut Type", fntb));
						table.addCell(cell);
						cell = new PdfPCell(new Phrase("Utility", fntb));
						table.addCell(cell);
						cell = new PdfPCell(new Phrase("Description", fntb));
						table.addCell(cell);
						cell = new PdfPCell(new Phrase("Width", fntb));
						table.addCell(cell);
						cell = new PdfPCell(new Phrase("Length", fntb));
						table.addCell(cell);
						List<Excavation> list = permit.getExcavations();
						if(list != null && list.size() > 0){
								for(Excavation one:list){
										cell = new PdfPCell(new Phrase(one.getAddress().getAddress(), fnt));
										table.addCell(cell);
										cell = new PdfPCell(new Phrase(one.getCut_type(), fnt));
										table.addCell(cell);
										cell = new PdfPCell(new Phrase(one.getUtility_type().getName(), fnt));
										table.addCell(cell);					
										cell = new PdfPCell(new Phrase(one.getCut_description(), fnt));
										table.addCell(cell);
										cell = new PdfPCell(new Phrase(one.getWidth(), fnt));
										table.addCell(cell);
										cell = new PdfPCell(new Phrase(one.getLength(), fnt));
										table.addCell(cell);					
								}
						}
						document.add(table);
						//
						pp = new Paragraph();
						pp.setIndentationLeft(12);
						pp.setAlignment(Element.ALIGN_LEFT);
						pp.setLeading(0f, 1f);
						ch = new Chunk("\nSpecial Provisions\n", fntb);
						phrase = new Phrase();
						phrase.add(ch);
						ch = new Chunk(permit.getNotes()+"\n", fnt);
						phrase.add(ch);			
						pp.add(phrase);
						document.add(pp);
						//
						pp = new Paragraph();
						pp.setIndentationLeft(12);
						pp.setAlignment(Element.ALIGN_LEFT);
						ch = new Chunk("Standards Conditions of Approval\n", fntb);
						phrase = new Phrase();
						phrase.add(ch);
						pp.add(phrase);
						document.add(pp);
						//
						pp = new Paragraph();
						pp.setIndentationLeft(12);
						pp.setAlignment(Element.ALIGN_LEFT);
						pp.setLeading(0f, 1f);
						ch = new Chunk("1 - "+conditions[0]+"\n", fntbs);
						phrase = new Phrase();			
						phrase.add(ch);
						pp.add(phrase);
						document.add(pp);
						//
						int jj=1;
						for(int j=1;j<conditions.length;j++){
								jj = j+1;
								pp = new Paragraph();
								pp.setLeading(0f, 1f);
								pp.setIndentationLeft(12);
								pp.setAlignment(Element.ALIGN_LEFT);
								ch = new Chunk(jj+" - "+conditions[j]+"\n", fnts);
								phrase = new Phrase();
								phrase.add(ch);
								pp.add(phrase);
								document.add(pp);				
						}
			
						pp = new Paragraph();
						pp.setIndentationLeft(20);
						pp.setAlignment(Element.ALIGN_RIGHT);
						ch = new Chunk("\n\n___________________\n", fnt);
						phrase = new Phrase();
						phrase.add(ch);
						pp.add(phrase);			
						ch = new Chunk(permit.getReviewer().getFullName(), fnt);
						phrase = new Phrase();
						phrase.add(ch);
						pp.add(phrase);
						document.add(pp);			
						document.close();
						writer.close();
						res.setHeader("Expires", "0");
						res.setHeader("Cache-Control", 
													"must-revalidate, post-check=0, pre-check=0");
						res.setHeader("Content-Disposition","attachment; filename="+fileName);			
						res.setHeader("Pragma", "public");
						//
						// setting the content type
						res.setContentType("application/pdf");
						//
						// the contentlength is needed for MSIE!!!
						res.setContentLength(baos.size());
						//
						out = res.getOutputStream();
						if(out != null){
								baos.writeTo(out);
						}
				}catch(Exception ex){
						logger.error(ex);
				}

		}
}






















































