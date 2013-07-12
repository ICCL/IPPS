<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Login extends CI_Controller {
    private $UserInfo, $parames;
    public function __construct() {
        parent::__construct();
        $this->load->model('parame');
        $this->UserInfo = $this->parame->getUserInfo();
        $this->parames = $this->parame->getParams();
    }

    public function index() {
        $this->login();
        $this->load->view('login', $this->parames);
    }

    private function login() {
        if(!empty($this->UserInfo)) $this->parame->redirect('backend');

        $account  = $this->input->post('account', TRUE);
        $password = $this->input->post('password', TRUE);
        $submit   = $this->input->post('submit', TRUE);

        $this->parames['ua'] = $account;
        if(!empty($submit)) {
            if(empty($account)) {
                $this->parames['error'] = $this->lang->line('login_error_account');
            } else if(empty($password)) {
                $this->parames['error'] = $this->lang->line('login_error_password');
            } else {
                $this->load->model('db/users');
                $UserInfoId = $this->users->verifyUser($account, $password);
                if($UserInfoId) {
                    $this->UserInfo = array('user'=> $account);
                    $this->session->set('UserInfo', $this->UserInfo);
                    $this->parame->redirect('/backend');
                } else {
                    $this->parames['error'] = $this->lang->line('login_error_password');
                }
            }
        }

    }
}