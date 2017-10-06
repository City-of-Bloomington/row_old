<%@  include file="header.jsp" %>
<!-- 
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->

<h3>View Permit <s:property value="permit.id" /></h3>
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
<table border="1" width="100%" cellpadding="0" cellspacing="0">
	<caption>Permit Info</caption>
	<tr>
		<td>
			<table width="100%">
				<s:if test="permit.hasCompany()" >
					<tr>
						<th width="30%">Company</th> 
						<td>
							<s:property value="%{permit.company.name}"/>
						</td>
					</tr>						  
					<s:if test="permit.hasContact()" >
						<tr>
							<th>Company Contact</th>
							<td>
								<s:property value="%{permit.contact.fullName}"/>
							</td>
						</tr>						
					</s:if>	
				</s:if>
				<s:elseif test="permit.hasContact()" >
					<tr>
						<th>Contact</th>
						<td><s:property value="%{permit.contact.fullName}"/></td>
					</tr>
				</s:elseif>
				<tr>
					<th>Permit Number</th> 
					<td><s:property value="%{permit.permit_num}" /></td>
				</tr>		  
				<tr>
					<th>Status</th> 
					<td><s:property value="%{permit.status}" /></td>
				</tr>
				<tr>
					<th>Type</th> 
					<td><s:property value="%{permit.permit_type}" /></td>
				</tr>		  
				<tr>
					<th>Permit Date </th> 
					<td><s:property value="%{permit.date}" /></td>
				</tr>
				<tr>
					<th>Start Date </th> 
					<td><s:property value="%{permit.start_date}" /></td>
				</tr>
				<tr>
					<th>Project </th> 
					<td><s:property value="%{permit.project}" /></td>
				</tr>
				<tr>
					<th>Reviewer</th> 
					<td><s:property value="%{permit.reviewer}" /></td>
				</tr>
				<tr>
					<th>Fee </th> 
					<td>$<s:property value="%{permit.fee}" /></td>
				</tr>
				<s:if test="permit.hasBond()" >
					<tr>
						<th>Bond Amount</th> 
						<td><s:property value="%{permit.bond.amountStr}" /></td>
					</tr>
					<tr>
						<th>Bond</th> 
						<td><s:property value="%{permit.bond.info}" /></td>
					</tr>					
				</s:if>
				<s:if test="permit.hasInsurance()" >
					<tr>
						<th>Insurance Amount</th> 
						<td><s:property value="%{permit.insurance.amountStr}"</td>
					</tr>
					<tr>
						<th>Insurance </th> 
						<td><s:property value="%{permit.insurance.info}"</td>
					</tr>					
				</s:if>
				<tr>
					<th valign="top">Notes </th> 
					<td align="left"><s:property value="%{permit.notes}" /></td>
				</tr> 
			</table> 
		</td>
	</tr>
	<tr>
		<s:if test="%{permit.hasExcavations()}" >
			<tr>
				<td>
					<s:set var="excavationsTitle" value="excavationsTitle" />	
					<s:set var="excavations" value="permit.excavations" />
					<%@  include file="excavations.jsp" %>	  		
				</td>
			</tr>
		</s:if>
</table>

<%@  include file="footer.jsp" %>























































