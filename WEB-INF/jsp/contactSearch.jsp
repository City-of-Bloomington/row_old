<%@  include file="header.jsp" %>
<!-- 
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<h3>Search Contacts</h3>
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
<s:form action="contactSearch" method="post" id="form_id" >
  <s:hidden name="action2" id="action_id" value=""/>  
  <table border="1" width="100%" cellpadding="0" cellspacing="0">
		<tr>
			<td>
				<table width="100%" cellpadding="0" cellspacing="0">
					<tr>
						<th>ID </th>
						<td><s:textfield name="contactList.id" size="10" maxlength="10" value="%{contactList.id}" id="contact_id" /></td>
					</tr>
					<tr>
						<th>Name </th>
						<td><s:textfield name="contactList.name" size="30" maxlength="30" value="%{contactList.name}" id="contact_name" />(Enter few name letters to select from list)</td>
					</tr>
					<tr>
						<th>Address </th>
						<td><s:textfield name="contactList.address" value="%{contactList.address}" maxlength="70" size="50" /></td>
					</tr>
					<tr>
						<th>City </th>		   
						<td><s:textfield name="contactList.city" value="%{contactList.city}" maxlength="30" size="30" /></td>
					</tr>
					<tr>
						<th>State </th>		 		 
						<td><s:textfield name="contactList.state" value="%{contactList.state}" maxlength="2" size="2" /></td>
					</tr>
					<tr>
						<th>Zip </th>			 
						<td><s:textfield name="contactList.zip" value="%{contactList.zip}" maxlength="5" size="5" /></td>
					</tr>
					<tr>
						<th>Phone </th>			 
						<td><s:textfield name="contactList.phone" value="%{contactList.phone}" maxlength="20" size="20" /></td>
					</tr>
					<tr>
						<th>Contact Type </th>			   
						<td><s:select name="contactList.type_id" list="types" listKey="id" listValue="name" headerKey="-1" headerValue="All" /></td>
					</tr>
					<tr>
						<th>Sort by</th>
						<td align="left"><s:select name="contactList.sort_by" value="contactList.sort_by" list="#{'c.id':'ID','c.lname':'Name','c.address':'Address'}" headerKey="c.id" headerValue="ID" /></td>
					</tr>		 
				</table>
			</td>
		</tr>
		<tr>
			<td align="right"><s:submit name="action" value="Submit"/></td>
		</tr>
		<tr>
			<td align="left">
				To add a new contact <a href="<s:property value='#application.url' />contact.action">here</a>
			</td>
		</tr>
  </table>
</s:form>
<s:if test="contacts != null">
  <s:set var="contactsTitle" value="contactsTitle" />	
  <s:set var="contacts" value="contacts" />
  <%@  include file="contacts.jsp" %>	  
</s:if>
<%@  include file="footer.jsp" %>
























































