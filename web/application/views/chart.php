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
    <script src="//ajax.googleapis.com/ajax/libs/jquery/2.0.0/jquery.min.js"></script>
    <script src="https://www.google.com/jsapi" type="text/javascript" charset="utf-8"></script>
    <script src="<?php echo site_url('statics/lib/bootstrap/docs/assets/js/bootstrap.min.js');?>"></script>
    <script src="<?php echo site_url('statics/js/chart/script.js');?>" type="text/javascript" charset="utf-8"></script>
    <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
    <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
  </head>
  <body>
      <?php require_once 'chart/'.$ArticlePage.'.php'; ?>
  </body>
</html>
