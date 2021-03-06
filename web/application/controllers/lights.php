<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Lights extends CI_Controller {
    private $parames;
    public function __construct() {
        parent::__construct();
        $this->load->model('db/light');
        $this->load->model('db/safetys');

        $this->load->model('parame');
        $this->parames = $this->parame->getParams();
    }

    public function json($limit='') {
        if(empty($limit)) $limit = 30;
        $light = $this->light->Select($limit);
        if($light->num_rows() > 0) $light = $light->result();

        $safetys = $this->safetys->SWhere(2);
        $result  = array('safety'=> $safetys, 'data'=> $light );
        echo json_encode($result);
    }

    public function chart($limit='') {
        $this->load->helper('url');
        $this->parames['ArticlePage'] = 'light';
        $this->load->view('chart', $this->parames);
    }
}
