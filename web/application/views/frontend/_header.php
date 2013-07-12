<div class="masthead">
  <ul class="nav nav-pills pull-right">
    <?php if(empty($UserInfo)) { ?>
    <li><a href="<?php echo site_url('/login'); ?>"><?php echo $lang->line('login'); ?></a></li>
    <?php } else { ?>
    <li><a href="<?php echo site_url('/backend'); ?>"><?php echo $lang->line('back-end'); ?></a></li>
    <?php } ?>
  </ul>
  <h3 class="muted"><a href="<?php echo site_url('/'); ?>"><?php echo $lang->line('web_name'); ?></a></h3>
  <div class="navbar">
    <div class="navbar-inner">
      <div class="container">
        <ul class="nav">
          <li><a href="#humidity" data-toggle="tab">Humidity</a></li>
          <li><a href="#light" data-toggle="tab">Light</a></li>
          <li><a href="#soil" data-toggle="tab">Soil</a></li>
          <li><a href="#temperature" data-toggle="tab">Temperature</a></li>
        </ul>
      </div>
    </div>
  </div><!-- /.navbar -->
</div>
