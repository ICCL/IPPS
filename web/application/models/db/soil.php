<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Soil extends CI_Model {
    private $tab;
    public function __construct() {
        parent::__construct();

        $this->tab = strtolower(get_class($this));
    }

    public function Select($limit) {
        $query = "SELECT TBL.* ".
                    "FROM (SELECT * FROM ". $this->tab ." Order by create_at DESC limit $limit) as TBL ".
                    "Order by TBL.create_at ASC ".
                    "Limit $limit";
        return $this->db->query($query);
    }

    public function Add($value) {
        $data = array('value'=> $value);
        $this->db->insert($this->tab, $data);
    }

    public function Update($id, $data) {
        $this->db->where('id', $id);
        $this->db->update($this->tab, $data);
    }

    public function Del($id) {
        $this->db->where('id', $id);
        $this->db->delete($this->tab); 
    }
}
