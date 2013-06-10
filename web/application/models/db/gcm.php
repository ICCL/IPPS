<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class gcm extends CI_Model {
    private $tab;
    public function __construct() {
        parent::__construct();
        $this->tab = strtolower(get_class($this));
    }

    public function SWhere($uid) {
        $this->db->where('u_id', $uid);
        return $this->db->get($this->tab);
    }

    public function Add() {

    }
}
