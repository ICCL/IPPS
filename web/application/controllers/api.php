<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Api extends CI_Controller {	
    public function __construct() {
        parent::__construct();
        $this->load->model('db/humidity');
        $this->load->model('db/light');
        $this->load->model('db/soil');
        $this->load->model('db/temperature');
        $this->load->model('db/equipment');
    }

    public function index($hum='', $light='', $soil='', $temp='') {
        if(!empty($hum) && !empty($light) && !empty($soil) && !empty($temp)) {
            $this->temperature->Add($temp);
            $this->humidity->Add($hum);
            $this->soil->Add($soil);
            $this->light->Add($light);
        } else {
            show_404();
        }
    }

    public function equipment($id='') {
        if(!empty($id)) {
            $this->equipment->setStatus($id);
        }
    }

    public function Humidity($data) {
        $this->humidity->Add($data);
    }

    public function Light($data) {
        $this->light->Add($data);
    }

    public function Soil($data) {
        $this->soil->Add($data);
    }

    public function Temperature($data) {
        $this->temperature->Add($data);
    }
}
