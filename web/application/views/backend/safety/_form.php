<form action="" method="post" accept-charset="utf-8" class="form-horizontal">
    <fieldset>
        <!-- Address form --> 
        <h2>Edit</h2>
        <!-- sonser-en-name input-->
        <div class="control-group">
            <label class="control-label"></label>
            <div class="controls">
                <h4><?php echo $safetys->en_name; ?></h4>
                <p class="help-block"></p>
            </div>
        </div>

        <!-- sonser-name input-->
        <div class="control-group">
        <label class="control-label"><?php echo $lang->line('sonser'); ?></label>
            <div class="controls">
                <input id="name" name="name" type="text" placeholder="<?php echo $lang->line('sonser'); ?>"
                value="<?php echo $safetys->name; ?>" class="input-xlarge">
                <p class="help-block"></p>
            </div>
        </div>

        <!-- value input-->
        <div class="control-group">
            <label class="control-label"><?php echo $lang->line('value'); ?></label>
            <div class="controls">
            <input id="value" name="value" type="text" placeholder="<?php echo $lang->line('value');?>"
                value="<?php echo $safetys->value ?>" class="input-xlarge">
                <p class="help-block"></p>
            </div>
        </div>

     <!-- value input-->
    <div class="control-group">
      <label class="control-label"></label>
      <div class="controls">
      <button class="btn btn-large" type="submit" name="submit" value="submit"><?php echo $lang->line('cancel'); ?></button>
      <button class="btn btn-large btn-primary offset1" type="submit" name="submit" value="submit"><?php echo $lang->line('submit');?></button>
      <p class="help-block"></p>
      </div>
    </div>

      </fieldset>
</form>