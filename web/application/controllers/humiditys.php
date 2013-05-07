<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Humiditys extends CI_Controller {
    public function __construct() {
        parent::__construct();
        $this->load->model('db/humidity');
        $this->load->model('db/safetys');
    }

    public function json($limit='') {
        if(empty($limit)) $limit = 100;
        $humidity = $this->humidity->Select($limit);
        if($humidity->num_rows() > 0) $humidity = $humidity->result();

        $safetys = $this->safetys->SWhere(1);
        $result  = array('safety'=> $safetys, 'data'=> $humidity );
        echo json_encode($result);
    }
}