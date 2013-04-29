<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Users extends CI_Model {
    private $tab;
    public function __construct() {
        parent::__construct();
        $this->tab = strtolower(get_class($this));
    }

    // 驗證使用者帳號密碼
    public function verifyUser($account, $password) {
        $this->db->select('id, passwd');
        $this->db->where('account', $account);
        $this->db->where('passwd', hash('sha1', $password));
        $query = $this->db->get($this->tab);

        if( $query->num_rows() > 0 ) {
            foreach( $query->result() as $row ) {
                return $row->id;
            }
        }
        return false;
    }

    // 驗證有沒有此帳號
    public function verifyAccount( $account ) {
        $this->db->where('account', $account);
        $query = $this->db->get($this->tab);

        if( $query->num_rows() > 0 ) {
            return false;
        }
        return true;
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
