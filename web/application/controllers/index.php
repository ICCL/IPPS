<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Index extends CI_Controller {
    private $parames;
    public function __construct() {
        parent::__construct();
        $this->load->model('parame');
        $this->parames = $this->parame->getParams();
    }

    public function index() {
        $this->load->view('index', $this->parames);
    }

    public function logout() {
        $this->session->_destroy();
        $this->parame->redirect('/');
    }

    public function ajaxNodeUrl() {
        $this->load->model('iconfig');
        $result = array('url'=> $this->iconfig->getNodejsUrl());
        echo json_encode($result);
    }
}