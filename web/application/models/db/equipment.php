<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Equipment extends CI_Model {
    private $tab;
    public function __construct() {
        parent::__construct();

        $this->load->model('iconfig');
        $this->tab = strtolower(get_class($this));
    }

    public function Select() {
        return $this->db->get($this->tab);
    }

    public function SWhere($name) {
        $this->db->where('name', $name);
        return $this->db->get($this->tab);
    }

    public function setStatus($sensor, $status) {
        $this->db->where('name', $sensor);
        switch($status) {
            case 'Auto':
                $data = array('status'=> 0, 'status_name'=> 'Auto');
                break;
            case 'Manually':
                $data = array('status'=> 1, 'status_name'=> 'Manually');
                break;
            case 'on':
                $data = array('status'=> 1, 'status_name'=> 'on');
                break;
            case 'off':
                $data = array('status'=> 0, 'status_name'=> 'off');
                break;
        }
        $this->db->update($this->tab, $data);
    }
}
