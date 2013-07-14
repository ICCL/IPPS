<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Api extends CI_Controller {	
    public function __construct() {
        parent::__construct();
        $this->load->model('db/humidity');
        $this->load->model('db/light');
        $this->load->model('db/soil');
        $this->load->model('db/temperature');
        $this->load->model('db/equipment');
        $this->load->model('db/safetys');
        $this->load->model('iconfig');
    }

    public function index($hum='', $light='', $soil='', $temp='') {
        if(strlen($hum) > 0 && strlen($light) > 0 && strlen($soil) > 0 && strlen($temp) > 0) {
            $this->temperature->Add($temp);
            $this->humidity->Add($hum);
            $this->soil->Add($soil);
            $this->light->Add($light);

            $this->checkVal($hum, $light, $soil, $temp);

            /* call nodejs update front-end chart update */
            $ch = curl_init($this->iconfig->getNodejsUrl().'/chart_update');
            curl_exec($ch);

            /* Send Data to mobile use GCM */
            $this->load->model('mgcm');
            $this->mgcm->activity('UpdateData');
        } else {
            show_404();
        }
    }

    private function checkVal($hum, $light, $soil, $temp) {
        $safe_hum;$safe_light;$safe_soil;$safe_temp;
        $equipmentStatus = $this->equipment->SWhere('Status');
        foreach($equipmentStatus->result() as $row) {
            $equipmentStatus = $row->status;
            break;
        }

        if($equipmentStatus == '1') {
            $data = array();
            $query = $this->safetys->Select();
            foreach($query->result() as $row) {
                if(strlen($row->value) > 0) {
                    switch($row->en_name) {
                    case 'Humidity':
                        $safe_hum = $row->value;
                        break;
                    case 'Light':
                        $safe_light = $row->value;
                        break;
                    case 'Soil':
                        $safe_soil = $row->value;
                        break;
                    case 'Temperature':
                        $safe_temp = $row->value;
                        break;
                    }
                }
            }


            $i = 0;
            if(!empty($safe_light)) {
                if($light < $safe_light) {
                    $data[] = array('Lamp', 'Error');
                    $i++;
                } else {
                    $data[] = array('Lamp', 'Normal');
                }
            }

            if(!empty($safe_temp) && !empty($safe_hum)) {
                if($temp > $safe_temp || $hum > $safe_hum) {
                    $data[] = array('Fan', 'Error');
                    $i++;
                } else {
                    $data[] = array('Fan', 'Normal');
                }
            }

            if(!empty($safe_soil)) {
                if($soil < $safe_soil) {
                    $data[] = array('Sprinkler', 'Error');
                    $i++;
                } else {
                    $data[] = array('Sprinkler', 'Normal');
                }
            }

            $result = array('ErrorCount'=> $i, 'data'=> $data);
            if($i > 0) {
                /* Send Data to mobile use GCM */
                $this->load->model('mgcm');
                $this->mgcm->activity('Notification', $result);
            }
        }
    }

    public function setSafetys($Item='' , $Value='') {
        if(!empty($Item)) {
            $data = array('value'=> $Value);
            switch($Item) {
                case 'humidity':
                    $this->safetys->Update(1, $data);
                    break;
                case 'light':
                    $this->safetys->Update(2, $data);
                    break;
                case 'soil':
                    $this->safetys->Update(3, $data);
                    break;
                case 'temperature':
                    $this->safetys->Update(4, $data);
                    break;
            }

            /* call nodejs update front-end chart update */
            $ch = curl_init($this->iconfig->getNodejsUrl().'/chart_update');
            curl_exec($ch);

            /* call api/safetys update Sensor safetys */
            $ch = curl_init($this->iconfig->getServerUrl().'/api/safetys');
            curl_exec($ch);

            /* Send Data to mobile use GCM */
            $this->load->model('mgcm');
            $this->mgcm->activity('UpdateData');
        }
    }

    public function safetys() {
        $result = '';

        $Query = $this->safetys->Select();
        foreach($Query->result() as $row) {
            $result .= $row->value.",";
        }
        $result = substr($result, 0, -1);

        $Url = $this->iconfig->getSensorUrl()."/safetys[$result]";
        $ch = curl_init($Url);
        curl_exec($ch);
    }

    public function equipment($sensor, $status) {
        if(!empty($sensor) && !empty($status)) {
            $this->equipment->setStatus($sensor, $status);

            /*  Send activity Status to Sensor*/
            $Url = $this->iconfig->getSensorUrl()."/$sensor=$status";
            $ch = curl_init($Url);
            curl_exec($ch);

            /* Send Sensor Status to mobile use GCM */
            $this->load->model('mgcm');
            $this->mgcm->activity('UpdateSensorStatus');
        }
    }

    public function equipmentStatus() {
        /* Send Sensor Status to mobile use GCM */
        $this->load->model('mgcm');
        $this->mgcm->activity('UpdateSensorStatus');
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

    public function UpdateData() {
        /* Send Data to mobile use GCM */
        $this->load->model('mgcm');
    }
}
