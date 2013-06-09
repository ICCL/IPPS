<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class All extends CI_Controller {
    public function __construct() {
        parent::__construct();
        $this->load->model('db/humidity');
        $this->load->model('db/light');
        $this->load->model('db/soil');
        $this->load->model('db/temperature');
        $this->load->model('db/safetys');
    }

    public function json($limit='') {
        if(empty($limit)) $limit = 100;

        /* Humidity */
        $humidity = $this->humidity->Select($limit);
        if($humidity->num_rows() > 0) $humidity = $humidity->result();

        $safetys = $this->safetys->SWhere(1);
        $result[]  = array('safety'=> $safetys, 'data'=> $humidity );

        /* Light */
        $light = $this->light->Select($limit);
        if($light->num_rows() > 0) $light = $light->result();

        $safetys = $this->safetys->SWhere(2);
        $result[]  = array('safety'=> $safetys, 'data'=> $light );

        /* Soil */
        $soil = $this->soil->Select($limit);
        if($soil->num_rows() > 0) $soil = $soil->result();

        $safetys = $this->safetys->SWhere(3);
        $result[]  = array('safety'=> $safetys, 'data'=> $soil );

        /* Temperature */
        $temperature = $this->temperature->Select($limit);
        if($temperature->num_rows() > 0) $temperature = $temperature->result();

        $safetys = $this->safetys->SWhere(4);
        $result[]  = array('safety'=> $safetys, 'data'=> $temperature ); 

        echo json_encode($result);
    }
}
