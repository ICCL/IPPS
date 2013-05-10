<!DOCTYPE html>
<html lang="zh-TW">
    <head> 
        <meta http-equiv="content-type" content="text/html; charset=utf-8">
        <title>Backend | IPPS</title>
        <!-- css styles -->
        <link href="<?php echo site_url('statics/css/normalize.css');?>" rel="stylesheet" />
        <link href="<?php echo site_url('statics/lib/bootstrap/docs/assets/css/bootstrap.css');?>" rel="stylesheet" />
        <link rel="stylesheet" href="<?php echo site_url('statics/css/backend.css');?>" type="text/css" media="screen" charset="utf-8">

        <!-- js -->
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/2.0.0/jquery.min.js"></script>
        <script src="<?php echo site_url('statics/lib/bootstrap/docs/assets/js/bootstrap.min.js');?>"></script>
        <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
        <!--[if lt IE 9]>
        <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
        <![endif]-->
    </head>
  <body>
    <div class="container-narrow">
      <?php require_once 'backend/_header.php';?>
      <hr>
      <?php require_once 'backend/'.$ArticlePage; ?>
    <hr>
    <?php require_once 'backend/_footer.php';?>
    </div> <!-- /container -->
    </body>
</html>
