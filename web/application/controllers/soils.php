<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Soils extends CI_Controller {
    public function __construct() {
        parent::__construct();
        $this->load->model('db/soil');
        $this->load->model('db/safetys');
    }

    public function json($limit='') {
        if(empty($limit)) $limit = 30;
        $soil = $this->soil->Select($limit);
        if($soil->num_rows() > 0) $soil = $soil->result();

        $safetys = $this->safetys->SWhere(3);
        $result  = array('safety'=> $safetys, 'data'=> $soil );
        echo json_encode($result);
    }

    public function chart($limit='') {
        $this->load->helper('url');
        $this->parames['ArticlePage'] = 'soil';
        $this->load->view('chart', $this->parames);
    }
}
