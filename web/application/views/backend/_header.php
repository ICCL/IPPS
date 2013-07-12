<div class="masthead">
  <ul class="nav nav-pills pull-right">
    <li><a href="<?php echo site_url('/'); ?>"><?php echo $lang->line('front-end'); ?></a></li>
    <li <?php echo preg_match('/safety/', uri_string()) ? 'class="active"' : ''; ?>><a href="<?php echo site_url('backend/safety').'/'; ?>"><?php echo $lang->line('safety'); ?></a></li>
    <li><a href="<?php echo site_url('/index/logout'); ?>"><?php echo $lang->line('logout'); ?></a></li>
  </ul>
  <h3 class="muted"><a href="<?php echo site_url('backend/').'/'; ?>">Backend | <?php echo $lang->line('web_name');?></a></h3>
</div>