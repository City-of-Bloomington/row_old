<%@  include file="header.jsp" %>
<!-- 
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->

<s:form action="reschedule" method="post">    
	<h3>Reschedule Bond/Insurance Mail Notification</h3>
	<p>
		This form is designed to run only once after building this
		mail notification app part. It may be rerun in case some of
		the data were corrupt. It will erase all planned notifications
		and will re-add them again. <br />
		Therefore it is not recommend to run this routine unless you know what
		you are doing. <br />
		This routine will look for bonds/insurances that have active expire date
		and reschedule them. <br />
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
  <p>
		You can pick which reschedules to run the bonds and/or insurances
		by marking the checkbox next to each one. 
	</p>
  <table border="1" width="50%" cellpadding="0" cellspacing="0">
	<tr>
	  <td>
			<table width="100%">
				<tr>
					<th width="20%">Bonds</th> 
					<td><s:checkbox name="reschedule.bond_flag" value="%{reschedule.bond_flag}" /> Yes</td>
				</tr>
				<tr>
					<th>Insurances </th>
					<td><s:checkbox name="reschedule.insurance_flag" value="%{reschedule.insurance_flag}" /> Yes</td>			
				</tr>		  
			</table> 
	  </td>
	</tr>
	<tr>
		<td align="right"><s:submit name="action" value="Process" /></td> 
	</tr>
  </table>
</s:form>
<s:if test="action != ''">
	<s:if test="hasBonds()" >
	  <s:set var="bondsTitle" value="'Re-schedules Bond Notifications'" />	
	  <s:set var="bonds" value="bonds" />
	  <%@  include file="bondsShort.jsp" %>	  
	</s:if>
	<s:if test="hasInsurances()" >
	  <s:set var="insurancesTitle" value="'Re-schedules Insurance Notifications'" />	
	  <s:set var="insurances" value="insurances" />
	  <%@  include file="insurancesShort.jsp" %>	  
	</s:if>	
</s:if>
<%@  include file="footer.jsp" %>























































