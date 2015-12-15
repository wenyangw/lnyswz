package lnyswz.jxc.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public interface Constant {
	
	public static final String NEED_AUDIT = "1";
	public static final String AUDIT_REFUSE = "9";
	//月结客户时间分隔点，20日前后
	public static final String KHLX_YJ_SEP = "21";
	//月结客户时间分隔点，20日后销售加30天
	public static final int KHLX_YJ_INC = 30;
	
	//发票税率
	public static final BigDecimal SHUILV = new BigDecimal("0.17");
	
	//金穗接口文本存储路径
	public static final String JS_FILEPATH = "C:/Lnyswz/";
	//金穗接口文本增值税文件
	public static final String JS_ZZS_FILENAME = "bill.txt";
	//金穗接口文本普通文件
	public static final String JS_PT_FILENAME = "billpt.txt";
	//金穗接口文本通用标识文件
	public static final String FP_ONE = "10590";
	
	public static final String KHLX_XK = "01";
	public static final String KHLX_XK_NAME = "现款";
	public static final String KHLX_SX = "02";
	public static final String KHLX_YJ = "03";
	public static final String KHLX_HT = "04";

	public static final String BM_DL = "08";
	
	//操作人员为业务员
	public static final String USER_POSTID = "12";
	

	//库房入库单
	public static final Map<String, String> REPORT_KFRK = new HashMap<String, String>(){{
		put("01","kfrk_report_s_ys");
		put("04","kfrk_report_s");
		put("05","kfrk_report_s");
		put("07","kfrk_report_s");
		put("08","kfrk_report_s");
	}};

	//库房出库单
	public static final Map<String, String> REPORT_KFCK = new HashMap<String, String>(){{
		put("04","kfck_report_s_ns");
		put("05","kfck_report_s_ns");
	}};	
	
	//业务盘点单
	public static final Map<String, String> REPORT_YWPD = new HashMap<String, String>(){{
		put("01","ywpd_report_s");
		put("04","ywpd_report_s");
		put("05","ywpd_report_b");
		put("07","ywpd_report_s");
		put("08","ywpd_report_s");
	}};
	
	//业务调号单
	public static final Map<String, String> REPORT_YWHS = new HashMap<String, String>(){{
		put("01","ywhs_report_b");
		put("04","ywhs_report_s");
		put("05","ywhs_report_b");
		put("07","ywhs_report_s");
		put("08","ywhs_report_s");
	}};
	
	//业务补调单
	public static final Map<String, String> REPORT_YWBT = new HashMap<String, String>(){{
		put("01","ywbt_report_b");
		put("04","ywbt_report_s");
		put("05","ywbt_report_b");
		put("07","ywbt_report_s");
		put("08","ywbt_report_s");
	}};
	
	//销售提货单
	public static final Map<String, String> REPORT_XSTH = new HashMap<String, String>(){{
		put("01","xsth_report_ys");
		put("04","xsth_report_b_ns");
		put("05","xsth_report_b_zy");
		put("07","xsth_report_jy");
		put("08","xsth_report_s_dl");
	}};
	
	//提货单-库房
	public static final Map<String, String> REPORT_XSTH_KF = new HashMap<String, String>(){{
		put("01","xsth_report_ys_kf");
		put("04","xsth_report_s_ns");
		put("05","xsth_report_s_ns");
		//put("07","xsth_report_jy");
		put("08","xsth_report_s_ns_dl");
	}};
	
	//库房调号单
	public static final Map<String, String> REPORT_KFHS = new HashMap<String, String>(){{
		put("01","kfhs_report_ys");
	}};
	
	//提货单-库房按保管员打印
	public static final Map<String, String> REPORT_XSTH_BGY = new HashMap<String, String>(){{
		put("04","xsth_report_s_ns_bgy");
		put("05","xsth_report_s_ns_bgy");
	}};
	
	//销售欠款单
	public static final Map<String, String> REPORT_XSQK = new HashMap<String, String>(){{
		put("01","qkdj_report_ys");
		put("04","qkdj_report_jc");
		put("05","qkdj_report_zy");
		put("08","qkdj_report_dl");
	}};
	
	//业务入库单
	public static final Map<String, String> REPORT_YWRK = new HashMap<String, String>(){{
		put("01","ywrk_report_ys_s_c");
		put("04","ywrk_report_s_jc");
		put("05","ywrk_report_b");
		put("07","ywrk_report_s");
		put("08","ywrk_report_s");
		
	}};
	
	//采购计划单
	public static final Map<String, String> REPORT_CGJH = new HashMap<String, String>(){{
		put("01","cgjh_report_c");
		put("04","cgjh_report");
		put("05","cgjh_report");
		put("07","cgjh_report_c");
		put("08","cgjh_report");
	}};
	
	//采购需求单
	public static final Map<String, String> REPORT_CGXQ = new HashMap<String, String>(){{
		put("04","cgxq_report_b");
	}};
	
	//销售未回款
	public static final Map<String, String> REPORT_XSHK = new HashMap<String, String>(){{
		put("01","xshk_report");
		put("04","xshk_report");
		put("05","xshk_report");
		put("07","xshk_report");
		put("08","xshk_report");
		
	}};
		
	
	public static final Map<String, String> BMMCS = new HashMap<String, String>(){{
		put("01","辽宁文达印刷物资有限公司");
		put("04","辽宁印刷物资有限责任公司");
		put("05","辽宁文达纸业有限公司");
		put("07","辽宁印刷物资有限责任公司");
		put("08","辽宁印刷物资有限责任公司大连分公司");
		
	}};
	
	
	public static final Map<String, String> XSKP_FH = new HashMap<String, String>(){{
		put("01","白燕");
		put("04","");
		put("05","");
		put("07","");
		put("08","");
	}};
	
	public static final Map<String, String> XSKP_SKR = new HashMap<String, String>(){{
		put("01","胡秀颖");
		put("04","");
		put("05","");
		put("07","");
		put("08","");
	}};
	
	public static final Map<String, String> YWPD_TITLE = new HashMap<String, String>(){{
		put("01","商  品  盘  点  单");
		put("02","商  品  客  诉  单");
		put("03","商  品  自  用  单"); 
	}};

	
	public static final String XSKP_SPMC_WITHCD = "1";
	public static final String XSKP_SPMC_WITHPP = "0";
	
	//是否打印临时提货标志
	public static final Map<String, String> XSTH_PRINT_LSBZ = new HashMap<String, String>(){{
		put("05", "1");
	}};
	
	public static final Map<String, String> XSKP_SPMC = new HashMap<String, String>(){{
		put("01",XSKP_SPMC_WITHPP);
		put("04",XSKP_SPMC_WITHCD);
		put("05",XSKP_SPMC_WITHCD);
		put("07",XSKP_SPMC_WITHCD);
		put("08",XSKP_SPMC_WITHCD);
	}};
	
	public static final Map<String, String> XSTH_HEAD = new HashMap<String, String>(){{
		put("04","白：总账  粉：运输  蓝：客户  黄：业务");
		put("05","白：总账  粉：运输  蓝：客户  黄：业务");
	}};
	
	public static final Map<String, String> XSTH_FOOT = new HashMap<String, String>(){{
		put("05","此单据为辽宁文达纸业有限公司送货收条，具有法律效力双方签字生效。");
	}};
	
	public static final String UPDATE_CK = "0";
	public static final String UPDATE_RK = "1";
	public static final String UPDATE_BT = "2";
	public static final String UPDATE_DB = "3";
	

	public static final String UPDATE_YS_LS = "0";
	public static final String UPDATE_YS_TH = "1";
	public static final String UPDATE_YS_KP = "2";
	public static final String UPDATE_YS_KP_TH = "3";
	public static final String UPDATE_HK = "4";
	public static final String UPDATE_HK_LS = "5";
	
	
	
	
	public static final BigDecimal BD_ZERO = new BigDecimal(0);
	
	public static final String IS_BGY = "1";
	public static final int REPORT_NUMBER = 8;
	
	public static final String XSTH_LX = "05";
	
	//入库类型编号
	public static final String RKLX_ZS = "01";  //正式
	public static final String RKLX_ZG = "02";  //暂估
	
	//FromOther
	public static final String YWRK_FROM_YWBT = "fromYwbt";
	public static final String XSKP_FROM_YWRK = "fromYwrk";
	
	public static final Map<String, String> XSKP_NB = new HashMap<String, String>(){{
		put("04","21010012");
		put("05","21010103");
	}};
	
	public static final String XSKP_JSFS_QK = "06";
}
