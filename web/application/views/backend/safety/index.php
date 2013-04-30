<div class="jumbotron">
  <h3><?php echo $lang->line('title'); ?></h3>
  <table class="table table-hover">
    <tr>
      <th></th>
      <th class="center"><?php echo $lang->line('sonser');?></th>
      <th class="center"><?php echo $lang->line('value');?></th>
      <th class="date center"><?php echo $lang->line('update_at');?></th>
    </tr>
    <?php
        if($safetys->num_rows() > 0) {
            foreach($safetys->result() as $row) {
    ?>
    <tr>
      <td class="item center">
        <a href="<?php echo site_url('backend/safety/edit/').'/'.$row->id.'/'; ?>"><i class="iconic-pen-alt"></i></a>
      </td>
      <td class="center"><?php echo $row->name;?></td>
      <td class="center"><?php echo $row->value;?></td>
      <td class="center"><?php echo $row->update_at;?></td>
    </tr>
    <?php
        }
    }
    ?>
  </table>
</div>
