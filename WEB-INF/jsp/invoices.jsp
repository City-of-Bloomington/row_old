<!-- 
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<s:if test="hasActionErrors()">
  <div class="errors">
    <s:actionerror/>
  </div>
</s:if>
<h3><s:property value="#invoicesTitle" /></h3>
<table border="1">
  <tr>
		<td>Invoice #</td>
		<td>Company</td>
		<td>Contact </td>
		<td>Status</td>
		<td>Invoice Date</td>
		<td>Start Date</td>
		<td>End Date</td>
		<td>Receipt</td>
  </tr>
  <s:iterator var="one" value="#invoices" status="conStatus">
		<tr <s:if test="conStatus.even">style="background:lightgray"</s:if>>
			<td>
				<a href="<s:property value='#application.url' />invoice.action?id=<s:property value='id' />"> <s:property value="invoice_num" /></a>
			</td>				  
			<td>&nbsp;
				<s:if test="hasCompany()">
					<s:property value="company.name" />
				</s:if>
			</td>
			<td>&nbsp;
				<s:if test="hasContact()">
					<s:property value="contact.fullName" />
				</s:if>
			</td>
			<td>
				<s:property value="status" />
			</td>	
			<td>
				<s:property value="date" />
			</td>
			<td>
				<s:property value="start_date" />
			</td>		
			<td>
				<s:property value="end_date" />
			</td>		
			<td>&nbsp;
				<s:if test="hasReceipt()">
					<a href="<s:property value='#application.url' />receipt.action?id=<s:property value='receipt.id' />"> <s:property value="receipt.id" /></a>
				</s:if>
			</td>
		</tr>
	</s:iterator>
</table>






















































