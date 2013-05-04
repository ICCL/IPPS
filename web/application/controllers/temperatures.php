<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Temperatures extends CI_Controller {
    public function __construct() {
        parent::__construct();
    }

    public function json($limit='') {
        if(empty($limit)) $limit = 100;
        $this->load->model('db/temperature');
        $result = $this->temperature->Select($limit);
        if($result->num_rows() > 0)
            echo json_encode($result->result());
    }
}
