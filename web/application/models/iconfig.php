<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Iconfig extends CI_Model {
    private $ServerUrl;
    private $NodejsUrl;
    private $SensorUrl;
    private $GoogleKey;
    public function __construct() {
        parent::__construct();

        $this->ServerUrl = "your ServerUrl/ipps";
        $this->NodejsUrl = "your NodejsUrl/:8808";
        $this->SensorUrl = "your SensorUrl";

        $this->GoogleKey = "your Google Key";
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

    public function getGoogleKey() {
        return $this->GoogleKey;
    }
}


