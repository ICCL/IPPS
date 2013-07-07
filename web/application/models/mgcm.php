<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Mgcm extends CI_Model {
    private $K_HUM, $K_LIGHT, $K_SOIL, $K_TEMP;
    private $GoogleKey, $RegId;
    public function __construct() {
        parent::__construct();

        $this->load->model('db/gcm');
        $this->load->model('db/iconfig');

        $this->load->model('db/safetys');
        $this->load->model('db/humidity');
        $this->load->model('db/light');
        $this->load->model('db/soil');
        $this->load->model('db/temperature');
        $this->load->model('db/equipment');

        $this->GoogleKey = $this->iconfig->getGoogleKey();

    }

    public function activity($activity, $Data='') {
        $this->getRegId(1);
        switch($activity) {
            case 'UpdateData':
                $data[] = $this->getHum();
                $data[] = $this->getLight();
                $data[] = $this->getSoil();
                $data[] = $this->getTemp();

                $result = array('Kind'=> 'UpdateData', 'data'=> $data);
                $this->send(json_encode($result));
                break;
            case 'UpdateSensorStatus':
                $Query = $this->equipment->Select();
                foreach($Query->result() as $row) {
                    $data[] = array($row->name, $row->status);
                }
                $result = array('Kind'=> 'UpdateSensorStatus', 'data'=> $data);
                $this->send(json_encode($result));
                break;
            case 'Notification':
                $result = array('Kind'=> 'Notification', 'data'=> $Data);
                $this->send(json_encode($result));
                break;
        }
    }

    public function getRegId($uid) {
        $query = $this->gcm->SWhere($uid);
        if($query->num_rows > 0) {
            foreach($query->result() as $row) {
                $this->RegId[] = $row->reg_id;
            }
        }
    }
    public function getHum($limit = 1) {
        $humidity = $this->humidity->Select($limit);
        if($humidity->num_rows() > 0) $humidity = $humidity->result();

        $safetys = $this->safetys->SWhere(1);
        return array('safety'=> $safetys, 'data'=> $humidity );
    }
    public function getLight($limit = 1) {
        $light = $this->light->Select($limit);
        if($light->num_rows() > 0) $light = $light->result();

        $safetys = $this->safetys->SWhere(2);
        return array('safety'=> $safetys, 'data'=> $light );
    }

    public function getSoil($limit = 1) {
        $soil = $this->soil->Select($limit);
        if($soil->num_rows() > 0) $soil = $soil->result();

        $safetys = $this->safetys->SWhere(3);
        return array('safety'=> $safetys, 'data'=> $soil );
    }

    public function getTemp($limit = 1) {
        $temperature = $this->temperature->Select($limit);
        if($temperature->num_rows() > 0) $temperature = $temperature->result();

        $safetys = $this->safetys->SWhere(4);
        return array('safety'=> $safetys, 'data'=> $temperature );
    }

    public function send($message) {
        // Set POST variables
        $url = 'https://android.googleapis.com/gcm/send';

        $fields = array(
            'registration_ids' => $this->RegId,
            'data' => array("Message"=> $message),
        );

        $headers = array(
            'Authorization: key=' . $this->GoogleKey,
            'Content-Type: application/json'
        );
        // Open connection
        $ch = curl_init();

        // Set the url, number of POST vars, POST data
        curl_setopt($ch, CURLOPT_URL, $url);

        curl_setopt($ch, CURLOPT_POST, true);
        curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

        // Disabling SSL Certificate support temporarly
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);

        curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));

        // Execute post
        $result = curl_exec($ch);
        if ($result === FALSE) {
            die('Curl failed: ' . curl_error($ch));
        }

        // Close connection
        curl_close($ch);
        echo $result;
    }
}