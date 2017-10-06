<%@  include file="header.jsp" %>
<!-- 
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->

<s:if test="report.report_type == 'active_permits'">
  <s:set var="excavations" value="report.list" />
  <s:set var="excavationsTitle" value="report.title" />
  <%@  include file="excavations2.jsp" %>
</s:if>
<s:elseif test="report.report_type == 'vectren'">
  <s:set var="excavations" value="report.list" />
  <s:set var="excavationsTitle" value="report.title" />
  <%@  include file="excavations2.jsp" %>
</s:elseif>
<s:elseif test="report.report_type == 'cbu'">
  <s:set var="excavations" value="report.list" />
  <s:set var="excavationsTitle" value="report.title" />
  <%@  include file="excavations2.jsp" %>
</s:elseif>
<s:elseif test="report.report_type == 'patch'">
  <s:set var="excavations" value="report.list" />
  <s:set var="excavationsTitle" value="report.title" />
  <%@  include file="excavations2.jsp" %>
</s:elseif>
<s:elseif test="report.report_type == 'bond'">
  <s:set var="bonds" value="report.list" />
  <s:set var="bondsTitle" value="report.title" />
  <%@  include file="bonds.jsp" %>
</s:elseif>
<s:elseif test="report.report_type == 'bond0'">
  <s:set var="bonds" value="report.list" />
  <s:set var="bondsTitle" value="report.title" />
  <%@  include file="bonds.jsp" %>
</s:elseif>
<s:elseif test="report.report_type == 'bondAbout'">
  <s:set var="bonds" value="report.list" />
  <s:set var="bondsTitle" value="report.title" />
  <%@  include file="bonds.jsp" %>
</s:elseif>
<s:elseif test="report.report_type == 'insurAbout'">
  <s:set var="insurances" value="report.list" />
  <s:set var="insurancesTitle" value="report.title" />
  <%@  include file="insurances.jsp" %>
</s:elseif>
<%@  include file="footer.jsp" %>























































