<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Safety extends CI_Controller {
    public function __construct() {
        parent::__construct();
        $this->load->model('parame');
        $this->UserInfo = $this->parame->getUserInfo();
        $this->parames = $this->parame->getParams();

        $this->load->model('iconfig');
        $this->load->model('db/safetys');
    }

    public function index() {
        $this->parames['safetys'] = $this->safetys->Select();
        //print_r($this->parames);
        $this->load->view('backend', $this->parames);
    }

    public function edit($id) {
        $this->parames['safetys'] = $this->safetys->SWhere($id);
        $this->onEdit($id);
        $this->load->view('backend', $this->parames);
    }

    private function onEdit($id) {
        $name   = $this->input->post("name", TRUE);
        $value  = $this->input->post("value", TRUE);
        $submit = $this->input->post("submit", TRUE);

        if(!empty($submit)) {
            $data = array('name'=> $name, 'value'=> $value);
            $this->safetys->Update($id, $data);

            $this->sendSafetys();
            $this->parames-redirect(site_url('backend/safety').'/');
        }
    }

    private function sendSafetys() {
        $Query = $this->safetys->Select();

        $result = '';
        foreach($Query->result() as $row) {
            $result .= $row->value.",";
        }
        $result = substr($result, 0, -1);

        $Url = $this->iconfig->getSensorUrl()."/safetys[$result]";
        $ch = curl_init($Url);
        curl_exec($ch);
    }
}