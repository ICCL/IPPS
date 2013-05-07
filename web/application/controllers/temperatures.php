<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Temperatures extends CI_Controller {
    public function __construct() {
        parent::__construct();
        $this->load->model('db/temperature');
        $this->load->model('db/safetys');
    }

    public function json($limit='') {
        if(empty($limit)) $limit = 100;
        $temperature = $this->temperature->Select($limit);
        if($temperature->num_rows() > 0) $temperature = $temperature->result();

        $safetys = $this->safetys->SWhere(4);
        $result  = array('safety'=> $safetys, 'data'=> $temperature );
        echo json_encode($result);
    }
}
