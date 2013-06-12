<!DOCTYPE html>
<html lang="zh-TW">
  <head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8">
    <title>IPPS</title>
    <!-- css styles -->
    <link href="<?php echo site_url('statics/css/normalize.css');?>" rel="stylesheet" />
    <link href="<?php echo site_url('statics/lib/bootstrap/docs/assets/css/bootstrap.css');?>" rel="stylesheet" />
    <link href="<?php echo site_url('statics/css/frontend.css');?>" rel="stylesheet" />

    <!-- js -->
    <script src="http://192.168.10.103:8808/socket.io/socket.io.js" type="text/javascript" charset="utf-8"></script>
    <script src="//ajax.googleapis.com/ajax/libs/jquery/2.0.0/jquery.min.js"></script>
    <script src="//www.google.com/jsapi" type="text/javascript" charset="utf-8"></script>
    <script src="<?php echo site_url('statics/lib/bootstrap/docs/assets/js/bootstrap.min.js');?>"></script>
    <script src="<?php echo site_url('statics/js/frontend/script.js');?>" type="text/javascript" charset="utf-8"></script>
    <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
    <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
  </head>
  <body>
    <div class="container">
      <?php require_once 'frontend/_header.php';?>
      <?php require_once 'frontend/'.$ArticlePage; ?>
      <hr>
      <?php require_once 'frontend/_footer.php';?>
    </div> <!-- /container -->
  </body>
</html>
