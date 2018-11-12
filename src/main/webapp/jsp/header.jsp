<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:if test="${pageContext.request.userPrincipal.name == null}">
   <% response.sendRedirect("login");%>
</c:if>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <!-- Meta, title, CSS, favicons, etc. -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>EzSwype ERP </title>
    <!-- Bootstrap core CSS -->

    <link href="resources/css/bootstrap.min.css" rel="stylesheet">

    <link href="resources/fonts/css/font-awesome.min.css" rel="stylesheet">
    <link href="resources/css/animate.min.css" rel="stylesheet">

    <!-- Custom styling plus plugins -->
    <link href="resources/css/custom.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="resources/css/maps/jquery-jvectormap-2.0.1.css" />
    <link href="resources/css/icheck/flat/green.css" rel="stylesheet" />
    <link href="resources/css/floatexamples.css" rel="stylesheet" type="text/css" />
    <link href="resources/css/datatables/tools/css/dataTables.tableTools.css" rel="stylesheet">

    <!-- editor -->
    <link href="resources/css/font-awesome.css" rel="stylesheet">
    <link href="resources/css/editor/external/google-code-prettify/prettify.css" rel="stylesheet">
    <link href="resources/css/editor/index.css" rel="stylesheet">
    <script src="<c:url value="resources/js/ezcitystate.js" />"></script>
 <script src="<c:url value="resources/js/businesscode.js" />"></script>
    <!-- select2 -->
    <link href="resources/css/select/select2.min.css" rel="stylesheet">

    <!-- switchery -->
    <link rel="stylesheet" href="resources/css/switchery/switchery.min.css" />

    <script src="resources/js/jquery.min.js"></script>
    <script src="resources/js/nprogress.js"></script>
    <script>
        NProgress.start();
    </script>
    
    <!--[if lt IE 9]>
        <script src="../assets/js/ie8-responsive-file-warning.js"></script>
        <![endif]-->

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
          <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
          <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
        <![endif]-->

</head>


<body class="nav-md">

    <div class="container body">


        <div class="main_container">

            <div class="col-md-3 left_col">
                <div class="left_col scroll-view">

                    <div class="navbar nav_title" style="border: 0;">
                        <a href="<c:url value='welcome' />" class="site_title"><i class="fa fa-paw"></i> <span>Acquiro Payments</span></a>
                    </div>
                    <div class="clearfix"></div>

                    <!-- menu prile quick info -->
                    <div class="profile">
                        <div class="profile_pic">
                            <img src="resources/images/logo.jpg" alt="..." class="img-circle profile_img">
                        </div>
                        <div class="profile_info">
                            <span>Welcome,</span>
                            <h2><%= session.getAttribute("userName") %></h2>
                        </div>
                      </div>
                    <!-- /menu prile quick info -->
                    <br />

                    <!-- sidebar menu -->
                    <div id="sidebar-menu" class="main_menu_side hidden-print main_menu">

                        <div class="menu_section">
                           <br clear="all">
                              <ul class="nav side-menu">
                               <% if(session.getAttribute("empRole").equals("1")){	%>
                              
                              <li><a href="<c:url value='welcome' />"><i class="fa fa-home"></i> Home <span class="fa fa-chevron-down"></span></a>
                                </li>
                                 <li><a href="<c:url value='beneficiaryHome' />"><i class="fa fa-desktop"></i>Beneficiary File <span class="fa fa-chevron-down"></span></a>
                                </li>
                                <li><a href="<c:url value='list' />"><i class="fa fa-desktop"></i>Merchant List <span class="fa fa-chevron-down"></span></a>
                                </li>
                                <li><a href="<c:url value='addmerchant' />"><i class="fa fa-edit"></i> Add  New Merchant <span class="fa fa-chevron-down"></span></a>
                                 </li>
                                 <li><a href="<c:url value='addOrg' />"><i class="fa fa-edit"></i>Add Organization <span class="fa fa-chevron-down"></span></a>                                   
                                </li>
                                 <li><a href="<c:url value='addDeviceDetail' />"><i class="fa fa-edit"></i>Add Device Detail <span class="fa fa-chevron-down"></span></a>                                   
                                </li>
                                <li><a href="<c:url value='txnhome' />"><i class="fa fa-table"></i> Transaction List <span class="fa fa-chevron-down"></span></a>
                                </li>
                                <li><a href="<c:url value='txnreport' />"><i class="fa fa-bar-chart-o"></i> Transaction Report <span class="fa fa-chevron-down"></span></a>
                                 </li>
                                <li><a href="<c:url value='billinghome' />"><i class="fa fa-bar-chart-o"></i> MDR Report <span class="fa fa-chevron-down"></span></a>
                                 </li>
                                 <li><a href="<c:url value='commissionreport' />"><i class="fa fa-bar-chart-o"></i> Commission Report<span class="fa fa-chevron-down"></span></a>
                                 </li>
                                  <li><a href="<c:url value='settingHome' />"><i class="fa fa-edit"></i> Settings <span class="fa fa-chevron-down"></span></a>
                                 </li>
                                  <li><a><i class="fa fa-edit"></i> Wallet Management <span class="fa fa-chevron-down"></span></a>                                
                                   <ul class="nav child_menu" style="display: none">
                                     <li><a href="<c:url value='walletList' />">Wallet List</a></li> 
                                     <li><a href="<c:url value='wallettTxnReport' />">Wallet Transaction Report</a></li>
 									 <li><a href="<c:url value='walletMdrHome' />">Wallet MDR Report</a>     
 									 <li><a href="<c:url value='walletCommissionReport' />">Wallet Commission Report</a></li>                                                                      
                                   </ul>                                  
                                 </li>                                 
                                 <li><a><i class="fa fa-edit"></i> Lead-(Self Register) <span class="fa fa-chevron-down"></span></a>                                
                                   <ul class="nav child_menu" style="display: none">
                                      <li><a href="<c:url value='leadList' />">List</a>        
                                        </li>                     
                                   </ul>                                  
                                 </li>
                                 <li><a href="<c:url value='generateBill' />"><i class="fa fa-bar-chart-o"></i> Generate Bill<span class="fa fa-chevron-down"></span></a>
                                 </li>
                                  <li><a><i class="fa fa-edit"></i> Manage Employee <span class="fa fa-chevron-down"></span></a>                                
                                   <ul class="nav child_menu" style="display: none">
                                     <li><a href="<c:url value='addEmployee' />">Add Employee</a></li>
                                     <li><a href="<c:url value='emplockunlock' />">Lock/Unlock Employee</a></li>    
                                   </ul>
                                 </li>  
                                 
                                 <%}%>
                                 <% if(session.getAttribute("empRole").equals("2")){%>
                              	<li><a href="<c:url value='welcome' />"><i class="fa fa-home"></i> Home <span class="fa fa-chevron-down"></span></a>
                                </li>
                                <li><a href="<c:url value='beneficiaryHome' />"><i class="fa fa-desktop"></i>Beneficiary File <span class="fa fa-chevron-down"></span></a>
                                </li>
                                <li><a href="<c:url value='list' />"><i class="fa fa-desktop"></i>Merchant List <span class="fa fa-chevron-down"></span></a>
                                </li>
                                <li><a href="<c:url value='addmerchant' />"><i class="fa fa-edit"></i> Add  New Merchant <span class="fa fa-chevron-down"></span></a>
                                 </li>
                                 <li><a href="<c:url value='addOrg' />"><i class="fa fa-edit"></i>Add Organization <span class="fa fa-chevron-down"></span></a>                                   
                                </li>
                                 <li><a href="<c:url value='addDeviceDetail' />"><i class="fa fa-edit"></i>Add Device Detail <span class="fa fa-chevron-down"></span></a>                                   
                                </li>   
                                 <li><a><i class="fa fa-edit"></i> Lead-(Self Register) <span class="fa fa-chevron-down"></span></a>                                
                                   <ul class="nav child_menu" style="display: none">
                                      <li><a href="<c:url value='leadList' />">List</a>        
                                        </li>                     
                                   </ul>                                  
                                 </li>                             
                                 <%}%>
                                 <% if(session.getAttribute("empRole").equals("3")){	%>
                             	 <li><a href="<c:url value='welcome' />"><i class="fa fa-home"></i> Home <span class="fa fa-chevron-down"></span></a>
                                </li>                                
                                <li><a href="<c:url value='beneficiaryHome' />"><i class="fa fa-desktop"></i>Beneficiary File <span class="fa fa-chevron-down"></span></a>
                                </li>
                                <li><a href="<c:url value='txnhome' />"><i class="fa fa-table"></i> Transaction List <span class="fa fa-chevron-down"></span></a>
                                </li>
                                <li><a href="<c:url value='txnreport' />"><i class="fa fa-bar-chart-o"></i> Transaction Report <span class="fa fa-chevron-down"></span></a>
                                 </li>
                                <li><a href="<c:url value='billinghome' />"><i class="fa fa-bar-chart-o"></i> MDR Report <span class="fa fa-chevron-down"></span></a>
                                 </li>
                                 <li><a href="<c:url value='commissionreport' />"><i class="fa fa-bar-chart-o"></i> Commission Report<span class="fa fa-chevron-down"></span></a>
                                 </li> 
                                 <li><a href="<c:url value='walletList' />"><i class="fa fa-table"></i> Wallet List <span class="fa fa-chevron-down"></span></a>
                                </li> 
                                 <li><a href="<c:url value='wallettTxnReport' />"><i class="fa fa-bar-chart-o"></i>Wallet Transaction Report <span class="fa fa-chevron-down"></span></a>
                                 </li>
									<li><a href="<c:url value='walletMdrHome' />"><i class="fa fa-bar-chart-o"></i>Wallet MDR Report <span class="fa fa-chevron-down"></span></a>
                                 </li>
                                   <li><a href="<c:url value='generateBill' />"><i class="fa fa-bar-chart-o"></i> Generate Bill<span class="fa fa-chevron-down"></span></a>
                                 </li>      
                                  <li><a><i class="fa fa-edit"></i> Lead-(Self Register) <span class="fa fa-chevron-down"></span></a>                                
                                   <ul class="nav child_menu" style="display: none">
                                      <li><a href="<c:url value='leadList' />">List</a>        
                                        </li>                     
                                   </ul>                                  
                                 </li>                                                        
                                 <%}%>
                                 <% if(session.getAttribute("empRole").equals("4")){	%>
                              	<li><a href="<c:url value='welcome' />"><i class="fa fa-home"></i> Home <span class="fa fa-chevron-down"></span></a>
                                </li>
                                <li><a href="<c:url value='list' />"><i class="fa fa-desktop"></i>Merchant List <span class="fa fa-chevron-down"></span></a>
                                </li>                              
                                <li><a href="<c:url value='txnhome' />"><i class="fa fa-table"></i> Transaction List <span class="fa fa-chevron-down"></span></a>
                                </li>                                
                                 <%}%>


                                
                              </ul>
                        </div>
                     </div>
                    <!-- /sidebar menu -->

                </div>
            </div>

            <!-- top navigation -->
            <div class="top_nav">

                <div class="nav_menu">
                    <nav class="" role="navigation">
                        <div class="nav toggle">
                            <a id="menu_toggle"><i class="fa fa-bars"></i></a>
                        </div>

                        <ul class="nav navbar-nav navbar-right">
                            <li class="">
                                <a href="javascript:;" class="user-profile dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
                                    <img src="resources/images/logo.jpg" alt=""><%= session.getAttribute("userName") %>
                                    <span class=" fa fa-angle-down"></span>
                                </a>
                                <ul class="dropdown-menu dropdown-usermenu animated fadeInDown pull-right">
                                    <li>
                                        <a href="#">Help</a>
                                    </li>
                                    <li><a href="<c:url value='logout'/>"><i class="fa fa-sign-out pull-right"></i> Log Out</a>
                                    </li>
                                </ul>
                            </li>
                        </ul>
                    </nav>
                </div>

            </div>
            <!-- /top navigation -->
