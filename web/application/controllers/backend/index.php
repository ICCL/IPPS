<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Index extends CI_Controller {
    public function __construct() {
        parent::__construct();
        $this->load->model('parame');
        $this->parames = $this->parame->getParams();

    }

    public function index() {
        $this->load->view('backend', $this->parames);
    }
}