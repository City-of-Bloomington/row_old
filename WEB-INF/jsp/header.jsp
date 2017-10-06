<?xml version="1.0" encoding="UTF-8" ?>
<!-- 
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
  <meta http-equiv="X-UA-Compatible" content="IE=edge" />
  <s:head />
  <meta http-equiv="Content-Type" content="application/xhtml+xml; charset=utf-8" />
  <link rel="SHORTCUT ICON" href="https://apps.bloomington.in.gov/favicon.ico" />
  <link rel="stylesheet" href="<s:property value='#application.url' />js/jquery-ui2.css" type="text/css" media="all" />
  <link rel="stylesheet" href="<s:property value='#application.url' />js/jquery.ui.theme.css" type="text/css" media="all" />
  <link rel="stylesheet" href="<s:property value='#application.url' />css/open-sans/open-sans.css" type="text/css" />
  <link rel="stylesheet" href="<s:property value='#application.url' />css/screen.css" type="text/css" />

  <title>ROW Permits</title>
    <script type="text/javascript">
    var APPLICATION_URL = '<s:property value='#application.url' />';
  </script>
</head>
<body>
  <header>
    <div class="container">
      <div class="site-title">
        <h1 id="application_name"><a href="<s:property value='#application.url'/>">ROW Permits</a></h1>
        <div class="site-location" id="location_name"><a href="<s:property value='#application.url'/>">City of Bloomington, IN</a></div>
      </div>
	  <div class="site-utilityBar">
		<nav id="user_menu">
		  <div class="menuLauncher">		  
			<s:if test="#session != null && #session.user != null">		  
			  <s:property value='#session.user.fullName' />
			</s:if>
			<s:else>
			  <a href="<s:property value='#application.url'/>Login">Login</a>
			</s:else>
		  </div>
		  <s:if test="#session != null && #session.user != null">				
			<div class="menuLinks closed">
			  <a href="<s:property value='#application.url'/>logout.action">Logout</a>
			</div>
		  </s:if>
        </nav>
		<s:if test="#session != null && #session.user != null">
		  <s:if test="#session.user.isAdmin()">
			<nav id="admin_menu">
			  <div class="menuLauncher">Admin</div>
			  <div class="menuLinks closed">
				<a href="<s:property value='#application.url'/>type.action">Edit Categories</a>
				<a href="<s:property value='#application.url'/>statsReport.action">Stats Reports</a>
				<a href="<s:property value='#application.url'/>report.action">Detailed Reports</a>
				<a href="<s:property value='#application.url'/>reschedule.action">Reschedule Notifications</a>				
			  </div>
			</nav>
		  </s:if>
		</s:if>
	  </div>
	</div>
	<div class="nav1">
	  <nav class="container">
		<a href="<s:property value='#application.url'/>permitSearch.action" class="nav1-link">Permits</a>
		<a href="<s:property value='#application.url'/>excavationSearch.action" class="nav1-link">Excavations</a>
		<s:if test="#session != null && #session.user != null">			
		  <a href="<s:property value='#application.url'/>companySearch.action" class="nav1-link">Companies</a>
		  <a href="<s:property value='#application.url'/>contactSearch.action" class="nav1-link">Contacts</a>
		  <a href="<s:property value='#application.url'/>bondSearch.action" class="nav1-link">Bonds</a>		  
		  <a href="<s:property value='#application.url'/>insuranceSearch.action" class="nav1-link">Insurances</a>
		  <a href="<s:property value='#application.url'/>invoiceSearch.action" class="nav1-link">Invoices</a>
		</s:if>
	  </nav>
	</div>
  </header>
  <main>
    <div class="container">
