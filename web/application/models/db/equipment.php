<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Equipment extends CI_Model {
    private $tab;
    public function __construct() {
        parent::__construct();

        $this->tab = strtolower(get_class($this));
    }

    public function setStatus($id) {
        $this->db->Where('id', $id);
        $query = $this->db->get($this->tab);
        if($query->num_rows() > 0) {
            foreach ($query->result() as $row) {
                $status = $row->status;
            }
        }
        $data = array();
        if(isset($status)) {
            switch($status) {
            case '0':
                $data = array('status'=> 1, 'status_name'=> 'on');
                break;
            case '1':
                $data = array('status'=> 0, 'status_name'=> 'off');
                break;
            }
            $this->db->where('id', $id);
            $this->db->update($this->tab, $data);
        }
    }
}
