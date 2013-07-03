<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Iconfig extends CI_Model {
    private $ServerUrl;
    private $NodejsUrl;
    private $SensorUrl;
    public function __construct() {
        parent::__construct();

        $this->ServerUrl = "http://192.168.0.131";
        $this->NodejsUrl = "http://192.168.0.131:8808";
        $this->SensorUrl = "http://192.168.0.30";
    }

    public function getServerUrl() {
        return $this->ServerUrl;
    }

    public function getNodejsUrl() {
        return $this->NodejsUrl;
    }

    public function getSensorUrl() {
        return $this->SensorUrl;
    }
}


