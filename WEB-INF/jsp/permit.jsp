<%@  include file="header.jsp" %>
<!-- 
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<s:form action="permit" method="post">    
  <s:if test="permit.id == ''">
		<h3>New Permit</h3>
  </s:if>
  <s:else>
		<h3>Edit Permit <s:property value="permit.id" /></h3>
		<s:hidden name="permit.id" value="%{permit.id}" />
		<s:if test="permit.hasBond()">
			<s:hidden name="permit.bond_id" value="%{permit.bond_id}" />
		</s:if>
		<s:if test="permit.hasInvoice() && !permit.canPickInvoice()">
			<s:hidden name="permit.invoice_id" value="%{permit.invoice_id}" />
		</s:if>		
  </s:else>
  <s:if test="permit.hasCompanyContact()">
		<s:hidden name="permit.company_contact_id" value="%{permit.company_contact_id}" />
  </s:if>
  <s:if test="hasActionErrors()">
		<div class="errors">
      <s:actionerror/>
		</div>
  </s:if>
  <s:elseif test="hasActionMessages()">
		<div class="welcome">
			<s:actionmessage/>
		</div>
  </s:elseif>
  <p>* Indicate a required field</p>
  <s:if test="permit.id == ''">
		<p>** If there is a list of available bonds, you can pick one. If you want to use a new bond, ignore the bond field now. You will be able to add a new bond after you Save by clicking on 'Add Bond'.The same applies to insurance.</p> 
  </s:if>
  <table border="1" width="100%" cellpadding="0" cellspacing="0">
		<caption>Permit Info</caption>
		<tr>
			<td>
				<table width="100%">
					<s:if test="permit.hasCompany()" >
						<tr>
							<th>Company</th> 
							<td><a href="<s:property value='#application.url' />company.action?id=<s:property value='%{permit.company.id}' />"><s:property value="%{permit.company.name}"/></a>
							</td>
						</tr>						  
						<s:if test="permit.hasContact()" >
							<tr>
								<th>Company Contact</th>
								<td><a href="<s:property value='#application.url' />contact.action?id=<s:property value='%{permit.contact.id}' />"><s:property value="%{permit.contact.fullName}"/></a>
								</td>
							</tr>						
						</s:if>	
					</s:if>
					<s:elseif test="permit.hasContact()" >
						<tr>
							<th>Contact</th>
							<td><a href="<s:property value='#application.url' />contact.action?id=<s:property value='%{permit.contact.id}' />"><s:property value="%{permit.contact.fullName}"/></a></td>
						</tr>
					</s:elseif>
					<tr>
						<th>Permit Number </th> 
						<td><s:textfield name="permit.permit_num" size="12" value="%{permit.permit_num}" maxlength="12" required="true" /> *</td>
					</tr>		  
					<tr>
						<th>Status</th> 
						<td><s:select name="permit.status" value="%{permit.status}" list="{'Not Started','Active','On Hold','Work Complete','Closed','Violation'}"  headerKey="-1" headerValue="" /></td>
					</tr>
					<tr>
						<th>Type</th> 
						<td><s:select name="permit.permit_type" value="%{permit.permit_type}" list="{'ROW','Grading'}"  headerKey="-1" headerValue="" /></td>
					</tr>		  
					<tr>
						<th>Issue Date </th> 
						<td><s:textfield name="permit.date" size="10" value="%{permit.date}" maxlength="10" cssClass="date" /> (before printing make sure you added the date)</td>
					</tr>
					<tr>
						<th>Start Date </th> 
						<td><s:textfield name="permit.start_date" size="10" value="%{permit.start_date}" maxlength="10" required="true" cssClass="date" /> *</td>
					</tr>
					<tr>
						<th>Project </th> 
						<td><s:textfield name="permit.project" size="50" value="%{permit.project}" maxlength="70" required="true" /> *</td>
					</tr>
					<tr>
						<th>Reviewer</th> 
						<td><select name="permit.reviewer_id" value="%{permit.reviewer_id}" >
							<option value="-1"></option>
							  <s:iterator var="one" value="inspectors">
									<s:if test="id == permit.reviewer_id">
										<option selected="selected" value="<s:property value="id" />"><s:property value="fullName" /></option>
									</s:if>
									<s:elseif test="isActive()">
										<option value="<s:property value="id" />""><s:property value="fullName"/></option>
									</s:elseif>
								</s:iterator>
						</select></td>
					</tr>
					<tr>
						<th>Fee </th> 
						<td><s:textfield name="permit.fee" size="10" value="%{permit.fee}" maxlength="10" /></td>
					</tr>
					<s:if test="permit.hasBond()" >
						<tr>
							<th>Bond </th> 
							<td>
								<a href="<s:property value='#application.url' />bond.action?id=<s:property value='%{permit.bond_id}' />"><s:property value="%{permit.bond.info}" /></a>
							</td>
						</tr>
					</s:if>
					<s:elseif test="permit.canPickBond()">
						<tr>
							<th>Bond </th> 
							<td><s:select name="permit.bond_id" list="permit.bondsForSelection" value="%{permit.bond_id}" listKey="id" listValue="info" headerKey="-1" headerValue="Pick One" /> ** </td>
						</tr>			
					</s:elseif>
					<s:if test="permit.hasInsurance()" >
						<tr>
							<th>Insurance </th> 
							<td>
								<a href="<s:property value='#application.url' />insurance.action?id=<s:property value='%{permit.insurance_id}' />"><s:property value="%{permit.insurance.info}" /></a>
							</td>
						</tr>
					</s:if>					
					<s:elseif test="permit.canPickInsurance()">
						<tr>
							<th>Insurance </th> 
							<td><s:select name="permit.insurance_id" list="permit.insurancesForSelection" value="%{permit.insurance_id}" listKey="id" listValue="info" headerKey="-1" headerValue="Pick One" /> ** </td>
						</tr>			
					</s:elseif>
					<s:if test="permit.hasInvoice()">
						<tr>
							<th>Invoice No.</th>
							<td>
								<a href="<s:property value='#application.url' />invoice.action?id=<s:property value='%{permit.invoice_id}' />"><s:property value="%{permit.invoice_num}" /></a>
							</td>
						</tr>
					</s:if>
					<s:if test="permit.canPickInvoice()">
						<tr>
							<th>Invoice No.</th>
							<td><s:select name="permit.invoice_id" value="%{permit.invoice_id}" list="%{permit.invoices}" listKey="id" listValue="invoice_num" headerKey="-1" headerValue="Pick Invoice" /></td>
						</tr>
					</s:if>
					<tr>
						<th valign="top">Notes </th> 
						<td align="left"><s:textarea name="permit.notes" rows="10" cols="80" value="%{permit.notes}" /></td>
					</tr> 
				</table> 
			</td>
		</tr>
		<tr>
			<s:if test="permit.id == ''">
				<td align="right"><s:submit name="action" value="Save" /></td> 
			</s:if>
			<s:else>
				<td><table width="100%">
					<tr>
						<td align="left"><s:submit name="action" value="Update" /></td>
						<s:if test="!permit.hasBond()">
							<td><button onclick="document.location='<s:property value='#application.url' />bond.action?permit_id=<s:property value='permit.id' />';return false;">Add Bond</button></td>
						</s:if>
						<s:if test="!permit.hasInvoice()">
							<td><button onclick="document.location='<s:property value='#application.url' />invoice.action?permit_id=<s:property value='permit.id' />';return false;">New Invoice</button></td>
						</s:if>
						<td><button onclick="document.location='<s:property value='#application.url' />PermitPdf?action=Print&id=<s:property value='permit.id' />';return false;">Printable </button></td>			
						<td><button onclick="document.location='<s:property value='#application.url' />excavation.action?permit_id=<s:property value='%{permit.id}' />';return false;">New Excavation</button></td>
						<td><button onclick="document.location='<s:property value='#application.url' />inspection.action?permit_id=<s:property value='%{permit.id}' />';return false;">New Inspection</button></td>
						<!--
						<td align="right"><s:submit name="action" value="Delete" onclick="validateDelete()" /></td>
						-->
					</tr>
				</table></td>
			</s:else>
		</tr>	
		<s:if test="%{permit.hasBond()}" >
			<tr><td>
				<s:set var="bondsTitle" value="bondTitle" />	
				<s:set var="bonds" value="permit.bonds" />
				<%@  include file="bondsShort.jsp" %>	  		
			</td></tr>
		</s:if>	
		<s:if test="%{permit.hasExcavations()}" >
			<tr><td>
				<s:set var="excavationsTitle" value="excavationsTitle" />	
				<s:set var="excavations" value="permit.excavations" />
				<%@  include file="excavations.jsp" %>	  		
			</td></tr>
		</s:if>
		<s:if test="%{permit.hasInspections()}" >
			<tr><td>
				<s:set var="inspectionsTitle" value="inspectionsTitle" />	
				<s:set var="inspections" value="permit.inspections" />
				<%@  include file="inspections.jsp" %>	  		
			</td></tr>
		</s:if>	
  </table>
</s:form>
<s:if test="permit.id == ''">
	<s:if test="permits != null ">
	  <s:set var="permitsTitle" value="permitsTitle" />	
	  <s:set var="permits" value="permits" />
	  <%@  include file="permits.jsp" %>	  
	</s:if>
</s:if>
<%@  include file="footer.jsp" %>























































