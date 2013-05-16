package quitada;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * JavaObjectDiff (code name: Mononov)
 * 
 * @author quitada <a href="http://d.hatena.ne.jp/quitada/" target="_top">http://d.hatena.ne.jp/quitada/</a>
 * @version 0.3.5
 *
 */
public class JavaObjectDiff {
	//private final boolean debug = false;

	//private final static String PRODUCT_NAME = "JavaObjectDiff";
	private final static String version = "0.3.5";
	private final static String SPACES = "    ";
	private final static String OPS_HEADER = "--";
	private final static String SU_OPS_HEADER = "-";
	private final static String SUBOPS_DELI = ":";
	private final static String HLP_OPS = OPS_HEADER + "help";
	private final static String HLP_SU_OPS = SU_OPS_HEADER + "h";
	private final static String VAR_OPS = OPS_HEADER + "version";
	private final static String VAR_SU_OPS = SU_OPS_HEADER + "v";
	private final static String SORT_OPS = OPS_HEADER + "sort";
	private final static String SORT_SU_OPS = SU_OPS_HEADER + "s";
	private final static String CNT_SUBOPS = "instance";
	private final static String CNT_SU_SUBOPS = "i";
	private final static String BYT_SUBOPS = "byte";
	private final static String BYT_SU_SUBOPS = "b";
	private final static String CLS_SUBOPS = "class";
	private final static String CLS_SU_SUBOPS = "c";
	private final static String ORD_OPS = OPS_HEADER + "order";
	private final static String ORD_SU_OPS = SU_OPS_HEADER + "o";
	private final static String DES_SUBOPS = "des";
	private final static String DES_SU_SUBOPS = "d";
	private final static String ASC_SUBOPS = "asc";
	private final static String ASC_SU_SUBOPS = "a";
	//private final static String ASCENDING = "ascending";
	//private final static String CLB_NAME[] = { "diff of #instances", "diff of #bytes", "class name"};
	//private final static String CLB_NAME[] = { "55555", "7777777", "class name"};
	private final static int TAB_LENGTH = 8;
	private final static String RES_BNDL = "quitada.props.messages";

	public static void main(String[] args) {
		// "MONONOV" stands for "Merge Objects Numerically Ordered Nonetheless On Variety."
		// I'll name "MONONOV" to this product when I release version 1.0.
		JavaObjectDiff mononov = new JavaObjectDiff();
		int numOps = 0;

		// st =0: by instance/1: by byte/2: by class name
		int st = 0;
		// ido =true: Descending order/false: Ascending order
		boolean ido = true;
		// foundFirstFile = true: found first histogram file/ false: not found yet 
		boolean foundFirstFile = false;
		int argi;
		for (argi = 0 ; argi < args.length ; argi++) {
			//System.out.println("Argument[" + argi + "] =\"" + argz[argi] +"\"");
			boolean validOps = false;
			if (args[argi].equals(HLP_OPS) || args[argi].equals(HLP_SU_OPS)) {
				showVersion(mononov.getClass().getSimpleName());
				showUsage(mononov.getClass().getName());
				showOptions();
				System.exit(0);
			}

			if (args[argi].equals(VAR_OPS) || args[argi].equals(VAR_SU_OPS)) {
				showVersion(mononov.getClass().getSimpleName());
				System.exit(0);
			} 

			String ostr = "";
			String subops = "";
			int dindex = -1;
			try {
				dindex = args[argi].indexOf(SUBOPS_DELI);
				ostr = args[argi].substring(0, dindex);
				subops = args[argi].substring(dindex + 1);
			} catch (StringIndexOutOfBoundsException ex) {
				;
			}

			//System.out.println(ostr);
			// set sort type
			if (ostr.equals(SORT_OPS) || ostr.equals(SORT_SU_OPS)) {
				//String subops = argz[argi].substring(dindex + 1);
				//System.out.println(subops);

				if (subops.equals(CNT_SUBOPS) || subops.equals(CNT_SU_SUBOPS)) {
					numOps++;
					validOps = true;
				} else if (subops.equals(BYT_SUBOPS) || subops.equals(BYT_SU_SUBOPS)) {
					st = 1;
					numOps++;
					validOps = true;
				} else if (subops.equals(CLS_SUBOPS) || subops.equals(CLS_SU_SUBOPS)) {
					st = 2;
					numOps++;
					validOps = true;
				}
			} else if (ostr.equals(ORD_OPS) || ostr.equals(ORD_SU_OPS)) { // set order
				//String subops = argz[argi].substring(dindex + 1);
				//System.out.println(subops);

				if (subops.equals(DES_SUBOPS) || subops.equals(DES_SU_SUBOPS)) {
					numOps++;
					validOps = true;
				} else if (subops.equals(ASC_SUBOPS) || subops.equals(ASC_SU_SUBOPS)) {
					ido = false;
					numOps++;
					validOps = true;
				}
			} 

			if (!validOps) {
				if ( args[argi].startsWith(OPS_HEADER) || args[argi].startsWith(SU_OPS_HEADER)) {
					System.out.println(getI18nMessages("invalid_ops_mes") + ": " + args[argi]);
					showUsage(mononov.getClass().getName());
					showOptions();
					System.exit(1);				
				} else if (!foundFirstFile) {
					foundFirstFile = true;
				}
			} else if (foundFirstFile) {
				System.out.println(getI18nMessages("disorder_file_path_mes"));
				showUsage(mononov.getClass().getName());
				System.exit(1);
			}
		}

		if (argi == 0) {
			System.out.println(getI18nMessages("more_args_mes"));
			showUsage(mononov.getClass().getName());
			System.exit(1);
		}

		//System.out.println(numOps);
		//System.out.println(argz.length);
		if (args.length < (2 + numOps)) {
			System.out.println(getI18nMessages("need_two_files_mes"));
			showUsage(mononov.getClass().getName());
			System.exit(1);
		}

		//String[] fs = { args[numOps], args[numOps + 1] };
		//try {
		mononov.runMerge(st, ido, args[numOps], args[numOps + 1]);
		//} catch (UnsupportedEncodingException e) {
		// Auto-generated catch block
		//e.printStackTrace();
		//}
	}
	
	/**
	 * Merge two java histogram files extracted with the following jmap command and sort result based on type of sorting in descending or ascending order.
	 * <table border=1 align=center>
	 * <tr>
	 * <th align=left>jmap -histo:live <i>[target java pid]</i></th>
	 * </tr>
	 * </table>
	 * 
	 * @since 0.3.5
	 * @param sortType Type of sorting: 0 - by instance /1 - by byte /2 - by class name
	 * @param isDesOrd Sort in descending order or not: true - descending order /false - ascending order
	 * @param before Path name to histogram file before memory leak
	 * @param after Path name to histogram file after memory leak
	 */
	public void runMerge(int sortType, boolean isDesOrd, String before, String after) {
		String[] files = { before, after };
		ArrayList<ObjectInfo> listOfObjectInfo = new ArrayList<ObjectInfo>();
		HashMap<String, Long[]> mapOfObjectInfo = new HashMap<String, Long[]>();

		//int maxClLength[] = {0,0};
		for (int n=0; n < 2; n++) {
			// line number to read
			int line = 1;

			// (column number -1) to read
			int u = 0;

			// tmp String for exception handling
			String tmpStr = "";
			try {
				FileReader f = new FileReader(files[n]);
				BufferedReader b = new BufferedReader(f);
				String s;
				while ((s = b.readLine()) != null){
					if (s.contains(":")) {
						//System.out.println(s);
						String columns[] = s.split(" ");
						u = 0;
						Long noi = (long) 0;
						Long nob = (long) 0;
						String cn = null;
						//boolean debug2 = false;
						for (int i =0; i < columns.length; i++) {
							if (!columns[i].equals("")) {
								tmpStr = columns[i];
								switch (u) {
								case 1:
									//maxClLength[0] = compareLength(columbs[i].length(), maxClLength[0]);
									noi = new Long(columns[i]);
									//System.out.print("[#instances]=" + noi + "/");
									break;
								case 2:
									//maxClLength[1] = compareLength(columbs[i].length(), maxClLength[1]);
									/*if (maxClLength[1] == 7) {
										debug2 = true;
									}*/
									nob = new Long(columns[i]);
									//System.out.print("[#bytes]=" + noi + "/");
									break;
								case 3:
									//maxClLength[2] = compareLength(columbs[i].length(), maxClLength[2]);
									cn = new String(columns[i]);
									//System.out.print("[class name]=" + cn);
									break;
								}
								u++;
								//System.out.print("["+i+"]" + columbs[i] + "/");
							}
						}
						if (cn == null) {
							System.out.println(getI18nMessages("no_class_name") + ": " + files[n]);
							System.out.println(getI18nMessages("line_num") + " " + line + ": "
									+ getI18nMessages("specify_class_name"));
							System.exit(0);
						}
						if (n==0) {
							listOfObjectInfo.add(new ObjectInfo(noi.longValue(), nob.longValue(), cn));
							//System.out.print("["+nor+"] noi=" + nois[nor] + "/cn=" + cns[nor]);
						} else {
							Long[] nfc = new Long[2];
							nfc[0] = noi;
							nfc[1] = nob;
							mapOfObjectInfo.put(cn, nfc);
							/*if (debug2) {
								System.out.println("nor=[" + noi + "]/nob=[" + nob + "]/cn=[" + cn + "]");
							}*/
						}

						//System.out.print("\n");
					}
					line++;
				}
				b.close();
				f.close();
			} catch (FileNotFoundException ex){
				//ex.printStackTrace();
				System.out.println(getI18nMessages("file_not_found") + ": " + files[n]);
				System.exit(0);
			} catch (NumberFormatException ex){
				System.out.println(getI18nMessages("illegal_num_format") + ": " + files[n]);
				System.out.print(getI18nMessages("line_num") + " " + line + ": ");
				switch(u) {
				case 1:
					System.out.println(getI18nMessages("illegal_num_instances") + " -> \"" + tmpStr + "\"" );
					break;
				case 2:
					System.out.println(getI18nMessages("illegal_num_bytes") + " -> \"" + tmpStr + "\"" );
					break;
				default:
					// should not be called
					System.out.print("\n");
				}
				System.exit(0);
			} catch (IOException ex) {
				System.out.println(getI18nMessages("IO_error"));
				ex.printStackTrace();
				System.exit(0);
			} catch (Exception ex){ // unknown runtime exception
				ex.printStackTrace();
				System.exit(0);
			}
			/*if (debug) {
				if (n==0) {
					System.out.println("---- before");
					for (int i = 0; i < listOfObjectInfo.size() ; i++) {
						ObjectInfo oc = listOfObjectInfo.get(i);
						System.out.println("["+i+"] noi=" + oc.getNumOfInstance() +
								"/nob=" + oc.getNumOfByte() +
								"/cn=" + oc.getClassName());
					}
				} else {
					System.out.println("---- after");
					System.out.println("map size=" + mapOfObjectInfo.size());
				}
			}*/
		}

		/*System.out.print("---maxClLength[] = { ");
		for (int i = 0; i < (CLB_NAME.length -1) ;i++) {
			System.out.print(maxClLength[i]);
			if (i < (CLB_NAME.length -2)) {
				System.out.print(", ");
			} else {
				System.out.println(" }");
			}
		}*/
		int maxClLength[] = {0,0};
		ArrayList<ObjectInfo> newListOfObjectInfo = new ArrayList<ObjectInfo>();
		for (int i = 0; i < listOfObjectInfo.size(); i++){
			ObjectInfo boc = listOfObjectInfo.get(i);
			String cn = boc.getClassName();
			long bnoi = boc.getNumOfInstance();
			long bnob = boc.getNumOfByte();
			Long[] anfc = mapOfObjectInfo.get(cn);
			if (anfc != null) {
				//System.out.println("Diff of noi =" + (anoi.longValue() - bnoi) + "/cn=" +cn);
				long tlv0 = anfc[0].longValue() - bnoi;
				long tlv1 = anfc[1].longValue() - bnob;
				newListOfObjectInfo.add(new ObjectInfo(tlv0, tlv1, cn));
				mapOfObjectInfo.remove(cn);
				maxClLength[0] = compareLength(new Long(tlv0).toString().length(), maxClLength[0]);
				maxClLength[1] = compareLength(new Long(tlv1).toString().length(), maxClLength[1]);
			} else {
				//System.out.println("Diff of noi =" + (-bnoi) + "/cn=" +cn);
				newListOfObjectInfo.add(new ObjectInfo((-bnoi), (-bnob), cn));
				maxClLength[0] = compareLength(new Long(-bnoi).toString().length(), maxClLength[0]);
				maxClLength[1] = compareLength(new Long(-bnob).toString().length(), maxClLength[1]);
			}
		}
		//System.out.println("remained map size=" + mapOfObjectCounts.size());
		if (mapOfObjectInfo.size() > 0) {
			Iterator<Entry<String,Long[]>> it = mapOfObjectInfo.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, Long[]> en = it.next();
				//System.out.println("noi=" + en.getValue().longValue() + "/cn=" + en.getKey());
				Long[] tv = en.getValue();
				newListOfObjectInfo.add(new ObjectInfo(tv[0].longValue(), tv[1].longValue(), en.getKey()));
				maxClLength[0] = compareLength(tv[0].toString().length(), maxClLength[0]);
				maxClLength[1] = compareLength(tv[1].toString().length(), maxClLength[1]);
			}			
		}
		/*System.out.print("---maxClLength[] = { ");
		for (int i = 0; i < (CLB_NAME.length -1) ;i++) {
			System.out.print(maxClLength[i]);
			if (i < (CLB_NAME.length -2)) {
				System.out.print(", ");
			} else {
				System.out.println(" }");
			}
		}*/

		//System.out.println("Size of newListOfObjectCounts=" + newListOfObjectCounts.size());
		Object[] oa = newListOfObjectInfo.toArray();
		Arrays.sort(oa, new CommonDataComparator(sortType, isDesOrd));
		String sot = getI18nMessages("num_instances"); //"#instances";
		String soo = getI18nMessages("descending"); //"descending";
		if (!isDesOrd) {
			soo = getI18nMessages("ascending"); //"ascending";
		}
		//System.out.print("Sorted by objects ");
		if (sortType == 1) {
			sot = getI18nMessages("num_bytes"); //"#bytes";
		} else if (sortType == 2) {
			sot = getI18nMessages("class_name"); //"class name";
		}
		if (System.getProperty("user.language").equals("ja")) {
			System.out.println(sot +"‚Å" + soo + "‚Éƒ\[ƒg");
		} else {
			System.out.println("Sorted by " + sot + " in " + soo + " order.");
		}
		/*if (isCount) {
			Arrays.sort(oa, new InstanceDesComparator());
			System.out.println("count");
		} else {
			Arrays.sort(oa, new ByteDesComparator());
			System.out.println("byte");
		}*/
		try {
			String columnName[] = { getI18nMessages("diff_num_instances"),
					getI18nMessages("diff_num_bytes"),
					getI18nMessages("class_name")};

			String charSet = "";
			if (System.getProperty("user.language").equals("ja")) {
				charSet = "Shift_JIS";
			} else {
				charSet = "ISO-8859-1";
			}

			boolean[] isLongerThanVal = { true, true };
			String clTitles ="";
			String clUnder = "";
			for (int i = 0; i < columnName.length; i++) {
				clTitles = clTitles + columnName[i];
				clUnder = clUnder + fillMonoChar(columnName[i].getBytes(charSet).length, '-');
				if (i < (columnName.length -1)){
					//fillTab(CLB_NAME[i].length(), maxClLength[i]);
					int ic = columnName[i].getBytes(charSet).length / TAB_LENGTH;
					int iv = maxClLength[i] / TAB_LENGTH;
					String tabs = "";
					if (ic < iv) {
						tabs = fillMonoChar(iv-ic, '\t');
						isLongerThanVal[i] = false;
					}
					clTitles = clTitles + tabs + "\t";
					clUnder = clUnder + tabs + "\t";
				}
			}
			System.out.println(clTitles +"\n" + clUnder);
			//System.out.println("divide test=" + (1/8));

			//for (int i = 0; i < CLB_NAME.length; i++) {		
			//char[] cbuf = new char[CLB_NAME[i].length()];
			//Arrays.fill(cbuf,'-');
			//System.out.print(fillMonoChar(CLB_NAME[i].length(), '-') +"\t");
			//if (i < (CLB_NAME.length -1)){
			//fillTab(CLB_NAME[i].length(), maxClLength[i]);
			//}
			//}
			//System.out.print("\n");
			//System.out.println("diff of #instances\tdiff of #bytes\t\tclass name");
			//System.out.println("------------------\t------------------\t----------");
			for (int i = 0; i < oa.length ; i++) {
				for (int u = 0; u < columnName.length ; u++) {
					String ts="";
					switch(u) {
					case 0:
						ts = new Long(((ObjectInfo)oa[i]).getNumOfInstance()).toString();
						break;
					case 1:
						ts = new Long(((ObjectInfo)oa[i]).getNumOfByte()).toString();
						break;
					case 2:
						ts = ((ObjectInfo)oa[i]).getClassName();
						break;
					}
					if (u < (columnName.length -1)){
						int im;
						if (isLongerThanVal[u]) {
							im = columnName[u].getBytes(charSet).length / TAB_LENGTH;
						} else {
							im = maxClLength[u] / TAB_LENGTH;
						}						
						int ic = ts.length() / TAB_LENGTH;
						ts = ts + fillMonoChar(im - ic, '\t') + "\t";
						//isLongerThanCl[i] = true;
					}
					System.out.print(ts);
				}
				System.out.print("\n");
				/*System.out.println(((ObjectInfo)oa[i]).getNumOfInstance()
						+ "\t\t\t" + ((ObjectInfo)oa[i]).getNumOfByte()
						+ "\t\t\t" + ((ObjectInfo)oa[i]).getClassName());*/
			}
		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace(); // this should be never called 
		}
	}

	private int compareLength(int i1, int i2) {
		if (i1 > i2) {
			return i1;
		} else {
			return i2;
		}
	}

	private String fillMonoChar(int length, char chr) {
		if (length > 0) {
			char[] cbuf = new char[length];
			Arrays.fill(cbuf, chr);
			return new String(cbuf);
		} else {
			return "";
		}
	}

	private static void showVersion(String productName) {
		System.out.println(productName + " " + getI18nMessages("version") + " " + version);
	}

	private static void showUsage(String className) {
		System.out.println(getI18nMessages("usage") + ": java " + className
				+ " [" + HLP_OPS + "]"
				+ " [" + VAR_OPS+"]"
				+ " [" + SORT_OPS + SUBOPS_DELI + "{" + CNT_SUBOPS + "/" + BYT_SUBOPS + "/" + CLS_SUBOPS + "}]"
				+ " [" + ORD_OPS + SUBOPS_DELI + "{" + DES_SUBOPS + "/" + ASC_SUBOPS + "}]"
				+ " <jmap " + getI18nMessages("histogram") + " 1> <jmap " + getI18nMessages("histogram") + " 2>");
	}

	private static void showOptions() {
		System.out.println(getI18nMessages("options") + ":\n"
				+ SPACES + HLP_OPS +" | " + HLP_SU_OPS + "\t:" + getI18nMessages("show_help") + "\n"
				+ SPACES + VAR_OPS +" | " + VAR_SU_OPS + "\t:" + getI18nMessages("show_version") + "\n"
				+ SPACES + SORT_OPS + SUBOPS_DELI + "<" + getI18nMessages("sub_ops") + "> | "
				+ SORT_SU_OPS + SUBOPS_DELI + "<" + getI18nMessages("sub_ops") + ">\t:" + getI18nMessages("sort_target") + "\n"
				+ SPACES + "\t" + getI18nMessages("usub_ops") + ":\n"
				+ SPACES + "\t" + SPACES + CNT_SUBOPS +" | " + CNT_SU_SUBOPS + "\t:" + getI18nMessages("sort_by_instances") + "\n"
				+ SPACES + "\t" + SPACES + BYT_SUBOPS +" | " + BYT_SU_SUBOPS + "\t:" + getI18nMessages("sort_by_bytes") + "\n"
				+ SPACES + "\t" + SPACES + CLS_SUBOPS +" | " + CLS_SU_SUBOPS + "\t:" + getI18nMessages("sort_by_class") + "\n"
				+ SPACES + ORD_OPS + SUBOPS_DELI + "<" + getI18nMessages("sub_ops") + "> | "
				+ ORD_SU_OPS + SUBOPS_DELI + "<" + getI18nMessages("sub_ops") + ">\t:" + getI18nMessages("sort_order") + "\n"
				+ SPACES + "\t" + getI18nMessages("usub_ops") + ":\n"
				+ SPACES + "\t" + SPACES + DES_SUBOPS +" | " + DES_SU_SUBOPS + "\t:" + getI18nMessages("sort_in_des") + "\n"
				+ SPACES + "\t" + SPACES + ASC_SUBOPS +" | " + ASC_SU_SUBOPS + "\t:" + getI18nMessages("sort_in_asc") + "\n"
				);
	}

	private static String getI18nMessages(String key) {
		//RES_BNDL
		try {
			return ResourceBundle.getBundle(RES_BNDL).getString(key);
		} catch (MissingResourceException ex) {
			System.out.println("Failed to get a resource bundle for i18n messages: "
					+ ex.toString());
			return new String("NO RESOURCE BUNDLES!!");
		}
	}

}