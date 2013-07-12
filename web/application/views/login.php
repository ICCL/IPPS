<!DOCTYPE html>
<html lang="zh-TW">
  <head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8">
    <title>Login | IPPS</title>
    <!-- css styles -->
    <link href="/ipps/statics/css/normalize.css" rel="stylesheet" />
    <link href="/ipps/statics/lib/bootstrap/docs/assets/css/bootstrap.css" rel="stylesheet" />
    <link rel="stylesheet" href="/ipps/statics/css/login.css" type="text/css" media="screen" charset="utf-8">

    <!-- js -->
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.0/jquery.min.js"></script>
    <script src="/ipps/statics/lib/bootstrap/docs/assets/js/bootstrap.min.js"></script>
    <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
    <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
  </head>
  <body>
    <div id="container">
      <form action="" method="post" accept-charset="utf-8" class="form-signin">
        <h2 class="form-signin-heading"><?php echo $lang->line('login'); ?></h2>
        <input type="text" name="account" class="input-block-level" placeholder="account" value="<?php echo $ua;?>">
        <input type="password" name="password" class="input-block-level" placeholder="password">
        <button class="btn btn-large btn-block btn-primary" type="submit" name="submit" value="submit"><?php echo $lang->line('login'); ?></button>
        <?php if(!empty($error)) { ?>
        <div class="alert alert-error">
          <button type="button" class="close" data-dismiss="alert">&times;</button>
          <h4><?php echo $lang->line('error'); ?></h4>
          <?php echo $error; ?>
        </div>
        <?php } ?>
        <div id="blackHome"><a href="<?php echo base_url('/'); ?>"><i class="icon-home"></i> Home</a></div>
      </form>
    </div>
  </body>
</html
