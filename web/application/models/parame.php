<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Parame extends CI_Model {
    private $parames, $UserInfo, $page, $InterfaceStatus;
    public function __construct() {
        parent::__construct();

        /**     load Session    **/
        $this->load->library('Session');
        $this->load->helper('url');

        $this->UserInfo = $this->session->get('UserInfo');
        $this->verifyLogin();

        /**     load config                 **/
        $this->load->model('iconfig');
        $this->parames['NodejsUrl'] = $this->iconfig->getNodejsUrl();

        /**     load basic lagnuage         **/
        $this->lang->load('basic','zh-TW');

        if(preg_match('/backend/', uri_string())) {
            $this->InterfaceStatus = 0;
            $this->lang->load('backend','zh-TW');
            //$this->loadBackendNav();
        } else {
            if(strlen(uri_string()) != 0) {
                $this->InterfaceStatus = 1;
                $this->lang->load('frontend','zh-TW');
                //$this->lang->load('nav','zh-TW');
                // $this->loadFrontendNav();
            } else {
                $this->lang->load('index','zh-TW');
            }
        }

        $this->parames['lang']      = $this->lang;
        $this->parames['UserInfo']  = $this->UserInfo;
        $this->init();
    }

    public function init() {
    // public function init($nav_page) {
        /**     Under code follow $nav_page     **/

        $url = preg_split('/\//', uri_string());
        if($this->InterfaceStatus == 0 )array_shift($url);
        $url_count = count($url);

        if($url_count != 0) {
            $this->loadlang($url);
            $this->parames['js']        = $this->loadJS($url[0]);
        }
        $this->parames['ArticlePage']   = $this->loadArticle($url, $url_count);
    }

    private function loadBackendNav() {
        /**     load Access Control List        **/
        $this->load->model('db/access_control_list');
        $this->parames['nav'] = $this->access_control_list->getNav($this->UserInfo->type_id);
    }

    private function loadFrontendNav() {
        $this->load->model('db/items_category');
        $this->load->model('db/items_category_second');
        $this->parames['nav'] = $this->items_category->Select();
    }

    /**     load different page lagnuage    **/
    public function loadlang($url) {
        switch(count($url)) {
            case 0:
                //$this->lang->load('index', 'zh-TW');
                break;
            default:
                $this->lang->load($url[0], 'zh-TW');
                break;
        }
    }

    /**     load different Article  **/
    public function loadArticle($url, $count) {
        switch($count) {
        case 0:
            return 'index.php';
        case 1:
            return $url[0].'/index.php';
        default:
            return $url[0].'/'.$url[1].'.php';
        }
    }

    /**     load different JS   **/
    private function loadJS($fileName) {
        $result = '';
        $dir = "statics/js/";
        switch($this->InterfaceStatus) {
            case 0:
                $dir .= 'backend/';
                break;
            case 1:
                $dir .= 'frontend/';
                break;
        }

        // Open a known directory, and proceed to read its contents 
        if (is_dir($dir)) {
            if ($dh = opendir($dir)) {
                while (($file = readdir($dh)) !== false) { 
                    if ($file!="." && $file!=".." && $file == $fileName.'.js') {
                        $result = $fileName.'.js';
                        break;
                        // echo "<a href=file/".$file.">".$file."</a><br>";
                    }
                }
                closedir($dh);
            }
        }
        if($result != '')
            return $result;
        else
            return null;
    }

    public function verifyLogin() {
        $url = uri_string();
        switch($url) {
        case 'backend':
            if(empty($this->UserInfo))
                $this->redirect('/');
        }
    }

    private function verifyPage($nav) {
        $verifyPage = $this->access_control_list->verifyPage($nav);
        if(!$verifyPage) {
            show_404();
        }
    }

    public function redirect($url) {
        header("Location: ".base_url($url) );
        exit;
    }

    public function getParams() {
        return $this->parames;
    }

    public function getUserInfo() {
        if(isset($this->UserInfo)) return $this->UserInfo;
        return false;
    }

    public function sendEMail($Type, $EmailInfo) {
        /**     load SendEmail          **/
        $this->load->model('Mail');

        // Email Notice
        $Message = array(   'account'   => $this->UserInfo->account, 
                            'passwd'    => $newpassword
                        );


        $this->Mail->sendEMail($Type, $EmailInfo );
    }

    public function verifyEmail($email) {
        $regexp = "/^[^0-9][A-z0-9_]+([.][A-z0-9_]+)*[@][A-z0-9_]+([.][A-z0-9_]+)*[.][A-z]{2,4}$/";

        if (preg_match($regexp, $email)) {
            return true;
        } else {
            return false;
        }
    }
}