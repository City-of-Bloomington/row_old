<!-- 
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<h3><s:property value="#bondsTitle" /></h3>
<table border="1" width="100%">
  <tr>
		<td>ID</td>
		<td>Bond Company</td>
		<td>Company </td>
		<td>Contact </td>
		<td>Bond Number</td>
		<td>Expire Date</td>
		<td>Days to Expire</td>
		<td>Amount</td>
		<td>Description</td>
		<td>Type</td>
		<td>Notes</td>
  </tr>
  <s:iterator var="one" value="#bonds" status="conStatus">
		<tr <s:if test="conStatus.even">style="background:lightgray"</s:if>>
			<td>
				<a href="<s:property value='#application.url' />bond.action?id=<s:property value='id' />"> <s:property value="id" /></a>
			</td>
			<td>
				<s:property value="bond_company" />
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
				<s:property value="bond_num" />
			</td>
			<td>
				<s:property value="expire_date" />
			</td>
			<td>&nbsp;
				<s:if test="hasExpireDate()">
					<s:property value="days_to_expire" />
				</s:if>
			</td>			
			<td align="right">
				<s:property value="amountStr" />
			</td>
			<td>
				<s:property value="description" />
			</td>
			<td>
				<s:property value="type" />
			</td>
			<td>			
				<s:property value="notes" />
			</td>
		</tr>
	</s:iterator>
</table>






















































