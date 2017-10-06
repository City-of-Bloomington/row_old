<%@  include file="header.jsp" %>
<!-- 
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->

<s:form action="bond" method="post">    
	<s:if test="bond.id == ''">
		<h3>New Bond</h3>
	</s:if>
  <s:else>
		<s:hidden name="bond.id" value="%{bond.id}" />
		<s:hidden name="bond.old_expire_date" value="%{bond.expire_date}" />		
		<h3>Edit Bond <s:property value="bond.id" /></h3>
  </s:else>
  <s:if test="bond.company_contact_id != ''">
		<s:hidden name="bond.company_contact_id" value="%{bond.company_contact_id}" />
  </s:if>
  <s:if test="bond.permit_id != ''">
		<s:hidden name="bond.permit_id" value="%{bond.permit_id}" />
  </s:if>
	<p>Note: if the bond company is not listed in the bond company pick up list
		you need to add it to the list by click on 'Admin' link and pick 'Edit Categories' then pick bond companies.
	</p>
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
  <p>*indicate a required field</p>

  <table border="1" width="100%" cellpadding="0" cellspacing="0">
	<caption>Bond Info</caption>
	<tr>
	  <td>
		<table width="100%">
		  <s:if test="bond.hasCompany()" >
			<tr>
			  <th>Company</th> 
			  <td><a href="<s:property value='#application.url' />company.action?id=<s:property value='%{bond.company.id}' />"><s:property value="%{bond.company.name}"/></a>
			  </td>
			</tr>						  
			<s:if test="bond.hasContact()" >
			<tr>
			  <th>Contact</th>
			  <td><a href="<s:property value='#application.url' />contact.action?id=<s:property value='%{bond.contact.id}' />"><s:property value="%{bond.contact.fullName}"/></a>
			</td>
			</tr>						
			</s:if>	
		  </s:if>
		  <s:elseif test="bond.hasContact()" >
			<tr>
			  <th>Contact</th> 
			  <td><s:property value="%{bond.contact.fullName}"/></td>
			</tr>
		  </s:elseif>
		  <tr>
			<th>Bond Company</th> 
			<td><s:select name="bond.bond_company_id" value="%{bond.bond_company_id}" list="bond_companies" listKey="id" listValue="name" headerKey="-1" headerValue="" /></td>
		  </tr>
		  <tr>
			<th>Bond Number </th> 
			<td><s:textfield name="bond.bond_num" size="12" value="%{bond.bond_num}" maxlength="12" required="true" /> *</td>
		  </tr>		  
		  <tr>
			<th>Expire Date </th> 
			<td><s:textfield name="bond.expire_date" size="10" value="%{bond.expire_date}" maxlength="10" required="true" cssClass="date" /> *</td>
		  </tr>
			<s:if test="bond.hasExpireDate()">
				<tr>
					<th>Days to Expire </th> 
					<td><s:property value="%{bond.days_to_expire}" /></td>
				</tr>
			</s:if>
		  <tr>
			<th>Amount </th> 
			<td>$<s:textfield name="bond.amount" size="12" value="%{bond.amount}" maxlength="12" required="true" /> *</td>
		  </tr>
		  <tr>
			<th>Bond Type </th> 
			<td><s:radio name="bond.type" value="%{bond.type}" required="true"  list="{'Excavation','Development','Grading'}" /></td>
		  </tr>
		  <tr>
			<th>Bond Description </th> 
			<td><s:textfield name="bond.description" size="50" value="%{bond.description}" maxlength="70" required="true" /> *</td>
		  </tr>		  
		  <tr>
			<th valign="top">Notes </th> 
			<td align="left"><s:textarea name="bond.notes" rows="10" cols="80" value="%{bond.notes}" /></td>
		  </tr> 
		</table> 
	  </td>
	</tr>
	<tr>
	  <s:if test="bond.id == ''">
		<td align="right"><s:submit name="action" value="Save" /></td> 
	  </s:if>
	  <s:else>
		<td><table width="100%">
		  <tr>
			<td align="left"><s:submit name="action" value="Update" /></td>
			<!--
			<td align="right"><s:submit name="action" value="Delete" onclick="validateDelete()" /></td>
			-->
		  </tr>
		</table></td>
	  </s:else>
	</tr>
  </table>
  </s:form>
  <s:if test="bond.id == ''">
	<s:if test="bonds != null ">
	  <s:set var="bondsTitle" value="bondsTitle" />	
	  <s:set var="bonds" value="bonds" />
	  <%@  include file="bonds.jsp" %>	  
	</s:if>
  </s:if>
  <s:else>
	<s:if test="bond.hasPermits()">
	  <s:set var="permitsTitle" value="'Related Permits'" />
	  <s:set var="permits" value="bond.permits" />
	  <%@  include file="permits.jsp" %>	  	  
	</s:if>
  </s:else>
<%@  include file="footer.jsp" %>























































