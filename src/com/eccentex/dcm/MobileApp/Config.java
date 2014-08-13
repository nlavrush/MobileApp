package com.eccentex.dcm.MobileApp;

public class Config {


	public static final String MOBILE_GET_STATES_RULE = "root_getListOfStates";
	private Config() {
    }

    public static final int SOURCE_MOBILE = 2;
    public static final int SOURCE_WEB = 1;


    public static final String STATUS_UPLOADED = "Відправлено";
    public static final String STATUS_NOT_UPLOADED = "Не відправлено";
    public static final String STATUS_UPDATED = "Оновлено";

    public static final String CONTENT_TYPE_IMAGE_JPG = "image/jpg";


    public static final String DEFAULT_HOST = "dcm.eccentex.com";

    public static final String DEFAULT_DOMAIN = "Eccentex_Example_Production.tenant41";

    public static final String DEFAULT_PORT = "8086";


    public static final String DEFAULT_URL_BODY = "BDS.WebService/DataServiceRest.svc/get.json";
    public static final String DEFAULT_CSM_URL_BODY = "CMS.WebService/CMSServiceRest.svc/createResource";
    public static final String DEFAULT_CALL_DAYS_HISTORY = "5";
    public static final String DEFAULT_UPDATE_FREQ = "30";
    public static final String DEFAULT_SYNC_PERIOD = "3";

    public static final String URL_PREFIX = "http://";

    /*URL configuration*/
    // Action Name to IntentFilter
    public static final String MAP_FRAGMENT_BROADCAST_ACTION = "com.appbase.maprouts.BROADCAST";

    public static final String LOGIN_SUFFIX = "/Security.WebService/AuthenticationServiceRest.svc/login";



    /*Post data params*/
    public static final String USER = "u";
    public static final String PASS = "p";
    public static final String ID = "id";



}
