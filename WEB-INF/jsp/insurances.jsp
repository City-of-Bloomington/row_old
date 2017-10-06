<!-- 
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<h3><s:property value="#insurancesTitle" /></h3>
<table border="1" width="100%">
  <tr>
		<td>ID</td>
		<td>Insurance Company</td>
		<td>Company </td>
		<td>Contact </td>
		<td>Policy Number</td>
		<td>Expire Date</td>
		<td>Amount</td>
		<td>Notes</td>
  </tr>
  <s:iterator var="one" value="#insurances" status="conStatus">
		<tr <s:if test="conStatus.even">style="background:lightgray"</s:if>>
			<td>
				<a href="<s:property value='#application.url' />insurance.action?id=<s:property value='id' />"> <s:property value="id" /></a>
			</td>
			<td>
				<s:property value="insuranceCompany" />
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
				<s:property value="policy_num" />
			</td>
			<td>
				<s:property value="expire_date" />
			</td>		
			<td align="right">
				<s:property value="amountStr" />
			</td>
			<td>			
				<s:property value="notes" />
			</td>
		</tr>
	</s:iterator>
</table>






















































