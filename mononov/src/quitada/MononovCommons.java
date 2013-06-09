package quitada;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class MononovCommons {
	protected final static String version = "0.5";
	protected final static String SPACES = "    ";
	protected final static String OPS_HEADER = "--";
	protected final static String SU_OPS_HEADER = "-";
	protected final static String SUBOPS_DELI = ":";
	protected final static String SUBPARAMS_SEPRATOR = ",";
	protected final static String EQUAL = "=";
	protected final static String HLP_OPS = OPS_HEADER + "help";
	protected final static String HLP_SU_OPS = SU_OPS_HEADER + "h";
	protected final static String VAR_OPS = OPS_HEADER + "version";
	protected final static String VAR_SU_OPS = SU_OPS_HEADER + "v";
	protected final static String SORT_OPS = OPS_HEADER + "sort";
	protected final static String SORT_SU_OPS = SU_OPS_HEADER + "s";
	protected final static String CNT_SUBOPS = "instance";
	protected final static String CNT_SU_SUBOPS = "i";
	protected final static String BYT_SUBOPS = "byte";
	protected final static String BYT_SU_SUBOPS = "b";
	protected final static String CLS_SUBOPS = "class";
	protected final static String CLS_SU_SUBOPS = "c";
	protected final static String ORD_OPS = OPS_HEADER + "order";
	protected final static String ORD_SU_OPS = SU_OPS_HEADER + "o";
	protected final static String DES_SUBOPS = "des";
	protected final static String DES_SU_SUBOPS = "d";
	protected final static String ASC_SUBOPS = "asc";
	protected final static String ASC_SU_SUBOPS = "a";
	protected final static String TAB_OPS = OPS_HEADER + "tab";
	protected final static String TAB_SU_OPS = SU_OPS_HEADER + "t";
	protected final static String FORMAT_OPS = OPS_HEADER + "format";
	protected final static String FORMAT_SU_OPS = SU_OPS_HEADER + "f";
	protected final static String STD_SUBOPS = "standard";
	protected final static String STD_SU_SUBOPS = "s";
	protected final static String CSV_SUBOPS = "csv";
	protected final static String CSV_SU_SUBOPS = "c";
	protected final static String QUT_SUBPARAMS = "quote";
	protected final static String QUT_SU_SUBPARAMS = "q";

	protected final static String RES_BNDL = "quitada.props.messages";
	protected final static String EX_BNDL = "quitada.props.exceptions";
	protected final static String NULL_CLN = "NullClass";
	
	protected static String getI18nMessages(String key) {
		return commonMessageGenerator(key, RES_BNDL);
	}

	protected static String getNonLocalizedExMeg(String key) {
		return commonMessageGenerator(key, EX_BNDL);
	}

	protected static String commonMessageGenerator(String key, String resourceBundle) {
		try {
			return ResourceBundle.getBundle(resourceBundle).getString(key);
		} catch (MissingResourceException ex) {
			//System.out.println("Failed to get a resource bundle for i18n messages: " + ex.toString());
			return new String("NO RESOURCE BUNDLES!!");
		}
	}
}
