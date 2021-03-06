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

/**
 * Mononov CUI and logic (JavaObjectDiff)
 * 
 * @author quitada <a href="http://d.hatena.ne.jp/quitada/"
 *         target="_top">http://d.hatena.ne.jp/quitada/</a>
 * @version 0.5
 * 
 */
public class JavaObjectDiff extends MononovCommons {
	private static String charSet = "ISO-8859-1";

	//--- private parameter for CUI from here
	private static int maxClLength[] = {0,0};
	private static boolean isCUI = false;
	//--- to here

	public JavaObjectDiff() {
	}


	public static void main(String[] args) {
		// need to calculate the length of column name according to language -
		// needless for CSV
		if (System.getProperty("user.language").equals("ja")) {
			charSet = "Shift_JIS";
		}

		// "MONONOV" stands for
		// "Merge Objects Numerically Ordered Nonetheless On Variety."
		// I'll name "MONONOV" to this product when I release version 1.0.
		JavaObjectDiff mononov = new JavaObjectDiff();
		int numOps = 0;

		// -----default configuration
		// st = 0 by instance/1: by byte/2: by class name
		int st = 0;
		// ido = true: Descending order/false: Ascending order
		boolean ido = true;
		// tabl = n: TAB length=n
		int tabl = 8;
		// fmt = 0: stand format/ 1: CSV format
		int fmt = 0;
		// quote: quiote string to wrap each csv
		String quote = "";
		// -----

		boolean foundFirstFile = false;
		int argi;
		for (argi = 0; argi < args.length; argi++) {
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

			boolean validOps = false;
			boolean haveSecondSubParams = true;
			String ostr = "";
			String subops = "";
			String subparam = "";
			//String quote = "";
			int dindex = -1;
			int sindex = -1;
			int eindex = -1;
			try {
				dindex = args[argi].indexOf(SUBOPS_DELI);
				sindex = args[argi].indexOf(SUBPARAMS_SEPRATOR);
				eindex = args[argi].indexOf(EQUAL);
				ostr = args[argi].substring(0, dindex);
				if (sindex == -1) {
					sindex = args[argi].length();
					haveSecondSubParams = false;
				}
				subops = args[argi].substring(dindex + 1,sindex);
				subparam = args[argi].substring(sindex + 1, eindex);
				quote = args[argi].substring(eindex + 1);
			} catch (StringIndexOutOfBoundsException ex) {
				//ex.printStackTrace();
				;
			} /*finally {
				System.out.println("dindex=" + dindex);
				System.out.println("sindex=" + sindex);
				System.out.println("haveSecondSubParams=" + haveSecondSubParams);
				System.out.println("eindex=" + eindex);
				System.out.println("ostr=" + ostr);
				System.out.println("subops=" + subops);
				System.out.println("subparam=" + subparam);
				System.out.println("quote=" + quote);
			}*/

			// logic for options required with sub option
			if (ostr.equals(SORT_OPS) || ostr.equals(SORT_SU_OPS)) { // set sort type
				if (!haveSecondSubParams) {
					if (subops.equals(CNT_SUBOPS) || subops.equals(CNT_SU_SUBOPS)) {
						numOps++;
						validOps = true;
					} else if (subops.equals(BYT_SUBOPS)
							|| subops.equals(BYT_SU_SUBOPS)) {
						st = 1;
						numOps++;
						validOps = true;
					} else if (subops.equals(CLS_SUBOPS)
							|| subops.equals(CLS_SU_SUBOPS)) {
						st = 2;
						numOps++;
						validOps = true;
					}
				}
			} else if (ostr.equals(ORD_OPS) || ostr.equals(ORD_SU_OPS)) { // set order
				if (!haveSecondSubParams) {
					if (subops.equals(DES_SUBOPS) || subops.equals(DES_SU_SUBOPS)) {
						numOps++;
						validOps = true;
					} else if (subops.equals(ASC_SUBOPS)
							|| subops.equals(ASC_SU_SUBOPS)) {
						ido = false;
						numOps++;
						validOps = true;
					}
				}
			} else if (ostr.equals(TAB_OPS) || ostr.equals(TAB_SU_OPS)) { // set TAB
				if (!haveSecondSubParams) {
					try {
						tabl = Integer.parseInt(subops);

						if (tabl > 0) {
							numOps++;
							validOps = true;
						}

					} catch (NumberFormatException ex) {
						;
					}
				}
			} else if (ostr.equals(FORMAT_OPS) || ostr.equals(FORMAT_SU_OPS)) { // set result format
				if (subops.equals(STD_SUBOPS) || subops.equals(STD_SU_SUBOPS)) {
					if (!haveSecondSubParams) {
						numOps++;
						validOps = true;
					}
				} else if (subops.equals(CSV_SUBOPS) || subops.equals(CSV_SU_SUBOPS)) {
					if (!haveSecondSubParams || subparam.equals(QUT_SUBPARAMS) || subparam.equals(QUT_SU_SUBPARAMS)) {
						fmt = 1;
						numOps++;
						validOps = true;
					}
				}
			}

			if (!validOps) {
				if (args[argi].startsWith(OPS_HEADER)
						|| args[argi].startsWith(SU_OPS_HEADER)) {
					System.out.println(getI18nMessages("invalid_ops_mes")
							+ ": " + args[argi]);
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

		if (args.length < (1 + numOps)) {
			System.out.println(getI18nMessages("need_file_mes"));
			showUsage(mononov.getClass().getName());
			System.exit(1);
		}

		String secondFile = "";
		if (args.length == (1 + numOps)) {
			secondFile = null;
		} else {
			secondFile = args[numOps + 1];
		}

		ObjectInfo[] oa = null;

		try {
			isCUI = true;
			//@param resultFormat
			//Result format: 0 - standard format suitable for console delimited by TAB/ 1 - comma separated value 
			//@param tabLength
			//            Desired TAB length with integer value larger than 0
			oa = mononov.runMerge(st, ido, args[numOps], secondFile);	
		} catch (MononovException mex) {
			System.out.println(mex.getLocalizedMessage());
			//System.out.println(mex.getMessage());
			//System.out.println(mex.getCause().toString());
			System.exit(1);
		}

		String columnName[] = {"","",""};
		boolean[] isLongerThanVal = { true, true };

		if (fmt == 0) { // determine sort type and order to show as message - needless for CSV
			//System.out.println(getResultPatternMessage(ido, st));
			String sot = getI18nMessages("num_instances"); // "#instances";
			String soo = getI18nMessages("descending"); // "descending";
			if (!ido) {
				soo = getI18nMessages("ascending"); // "ascending";
			}
			if (st == 1) {
				sot = getI18nMessages("num_bytes"); // "#bytes";
			} else if (st == 2) {
				sot = getI18nMessages("class_name"); // "class name";
			}

			String rpm ="";
			if (System.getProperty("user.language").equals("ja")) {
				rpm = sot + getI18nMessages("de") + soo + getI18nMessages("ni_sort");
			} else {
				rpm = "Sorted by " + sot + " in " + soo + " order.";
			}
			System.out.println(rpm);

			columnName[2] = getI18nMessages("class_name");
			if (secondFile == null) {
				columnName[0] = getI18nMessages("num_instances");
				columnName[1] = getI18nMessages("num_bytes");
			} else {
				columnName[0] = getI18nMessages("diff_num_instances");
				columnName[1] = getI18nMessages("diff_num_bytes");
			}

			String clTitles = "";
			String clUnder = "";
			try {
				for (int i = 0; i < columnName.length; i++) {
					clTitles = clTitles + columnName[i];
					clUnder = clUnder
							+ fillMonoChar(columnName[i].getBytes(charSet).length,
									'-');
					if (i < (columnName.length - 1)) {
						int ic = columnName[i].getBytes(charSet).length / tabl;
						int iv = maxClLength[i] / tabl;
						String tabs = "";
						if (ic < iv) {
							tabs = fillMonoChar(iv - ic, '\t');
							isLongerThanVal[i] = false;
						}
						clTitles = clTitles + tabs + "\t";
						clUnder = clUnder + tabs + "\t";
					}
				}
				System.out.println(clTitles + "\n" + clUnder);
			} catch (UnsupportedEncodingException ex) {
				ex.printStackTrace(); // this should be never called
				System.exit(1);
			}				
		}
		//--- to here

		// show result - need for CSV partially
		//if (isCUI) {
		for (int i = 0; i < oa.length; i++) {
			for (int u = 0; u < columnName.length; u++) {
				String ts = "";
				switch (u) {
				case 0:
					ts = new Long(oa[i].getNumOfInstance())
					.toString();
					break;
				case 1:
					ts = new Long(oa[i].getNumOfByte())
					.toString();
					break;
				case 2:
					ts = oa[i].getClassName();
					break;
				}

				// determine the number of TAB - needless for CSV, instead
				// need to add ","
				if (fmt == 0) { // standard format
					try {
						if (u < (columnName.length - 1)) {
							int im;
							if (isLongerThanVal[u]) {
								im = columnName[u].getBytes(charSet).length / tabl;
							} else {
								im = maxClLength[u] / tabl;
							}
							int ic = ts.length() / tabl;
							ts = ts + fillMonoChar(im - ic, '\t') + "\t";
						}

					} catch (UnsupportedEncodingException ex) {
						ex.printStackTrace(); // this should be never called
						System.exit(1);
					}
				} else if (i < (oa.length -1) || u < (columnName.length - 1)) { // csv format
					ts = quote + ts + quote + ",";					
				} else {
					ts = quote + ts + quote;
				}
				// print one line for results - need for CSV
				System.out.print(ts);
			}
			System.out.print("\n");
		}
		//}
		//--- to here
	}

	/**
	 * Merge two java histogram files extracted with the following jmap command
	 * and sort result based on type of sorting in descending or ascending
	 * order.
	 * <table border=1 align=center>
	 * <tr>
	 * <th align=left>jmap -histo:live <i>[target java pid]</i></th>
	 * </tr>
	 * </table>
	 * 
	 * @since 0.4.5
	 * @param sortType
	 *            Type of sorting: 0 - by instance /1 - by byte /2 - by class
	 *            name
	 * @param isDesOrd
	 *            Sort in descending order or not: true - descending order
	 *            /false - ascending order
	 * @param before
	 *            Path name to histogram file before memory leak
	 * @param after
	 *            Path name to histogram file after memory leak. If this
	 *            parameter is null, this method just sort histogram set as
	 *            "before" parameter.
	 * @throws quitada.MononovException Any exceptions while happening merging jmap histograms
	 * 
	 * @return Array of ObjectInfo objects merged and sorted according to condition
	 *            
	 */
	public ObjectInfo[] runMerge(int sortType, boolean isDesOrd, 
			String before, String after) throws MononovException {

		//--- reading data from files from here
		String[] files = { before, after };
		ArrayList<ObjectInfo> listOfObjectInfo = new ArrayList<ObjectInfo>();
		HashMap<String, Long[]> mapOfObjectInfo = new HashMap<String, Long[]>();

		int nn = 2;
		if (after == null) {
			nn = 1;
		}
		for (int n = 0; n < nn; n++) {
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
				while ((s = b.readLine()) != null) {
					if (s.contains(":")) {
						String columns[] = s.split(" ");
						u = 0;
						Long noi = (long) 0;
						Long nob = (long) 0;
						String cn = null;
						for (int i = 0; i < columns.length; i++) {
							if (!columns[i].equals("")) {
								tmpStr = columns[i];
								switch (u) {
								case 1:
									noi = new Long(columns[i]);
									break;
								case 2:
									nob = new Long(columns[i]);
									break;
								case 3:
									cn = new String(columns[i]);
									break;
								}
								u++;
							}
						}
						if (cn == null) {
							throw new NullPointerException(NULL_CLN);
						}
						if (n == 0) {
							listOfObjectInfo.add(new ObjectInfo(
									noi.longValue(), nob.longValue(), cn));
						} else {
							Long[] nfc = new Long[2];
							nfc[0] = noi;
							nfc[1] = nob;
							mapOfObjectInfo.put(cn, nfc);
						}
					}
					line++;
				}
				b.close();
				f.close();
			} catch (FileNotFoundException ex) {
				String message = getNonLocalizedExMeg("file_not_found") + ": " + files[n];
				String localizedMessage = getI18nMessages("file_not_found") + ": " + files[n];
				throw new MononovException(message, localizedMessage, ex);
			} catch (NumberFormatException ex) {
				String message = getNonLocalizedExMeg("illegal_num_format") + ": " + files[n] + "\n" +
						getNonLocalizedExMeg("line_num") + " " + line + ": ";
				String localizedMessage = getI18nMessages("illegal_num_format") + ": " + files[n] + "\n" +
						getI18nMessages("line_num") + " " + line + ": ";			
				/*System.out.println(getI18nMessages("illegal_num_format") + ": "
						+ files[n]);
				System.out.print(getI18nMessages("line_num") + " " + line
						+ ": ");*/
				switch (u) {
				case 1:
					message = message + getNonLocalizedExMeg("illegal_num_instances") +	" -> \"" + tmpStr + "\"";
					localizedMessage = localizedMessage + getI18nMessages("illegal_num_instances") +
							" -> \"" + tmpStr + "\"";
					break;
				case 2:
					message = message + getNonLocalizedExMeg("illegal_num_bytes") + " -> \"" + tmpStr + "\"";	
					localizedMessage = localizedMessage + getI18nMessages("illegal_num_bytes") +
							" -> \"" + tmpStr + "\"";	
					break;
				default:
					// should not be called
				}
				throw new MononovException(message, localizedMessage, ex);
			} catch (NullPointerException ex) {
				if (ex.getMessage().equals(NULL_CLN)) {
					String localizedMessage = getI18nMessages("no_class_name")
							+ ": " + files[n] + "\n" + getI18nMessages("line_num")
							+ " " + line + ": "	+ getI18nMessages("specify_class_name");
					String message = getNonLocalizedExMeg("no_class_name")
							+ ": " + files[n] + "\n" + getNonLocalizedExMeg("line_num")
							+ " " + line + ": "	+ getNonLocalizedExMeg("specify_class_name");
					throw new MononovException(message, localizedMessage, ex);
				} else {
					throw new MononovException(ex);
				}
			} catch (IOException ex) {
				String localizedMessage = getI18nMessages("IO_error");
				String message = getNonLocalizedExMeg("IO_error");
				throw new MononovException(message, localizedMessage, ex);
			} catch (Exception ex) { // unknown runtime exception
				throw new MononovException(ex);
			}
		}
		//--- to here

		//--- comparing data from here
		//int maxClLength[] = { 0, 0 }; // need to move to main()
		if (isCUI) {
			maxClLength[0] = 0;
			maxClLength[1] = 0;
		}
		int justSort = 1;
		if (after == null) {
			justSort = -1;
		}
		ArrayList<ObjectInfo> newListOfObjectInfo = new ArrayList<ObjectInfo>();
		for (int i = 0; i < listOfObjectInfo.size(); i++) {
			ObjectInfo boc = listOfObjectInfo.get(i);
			String cn = boc.getClassName();
			long bnoi = boc.getNumOfInstance();
			long bnob = boc.getNumOfByte();
			Long[] anfc = new Long[2];
			if (after == null) {
				anfc[0] = new Long(0);
				anfc[1] = new Long(0);
			} else {
				anfc = mapOfObjectInfo.get(cn);
			}
			long tlv0 = 0;
			long tlv1 = 0;
			if (anfc != null) {
				tlv0 = anfc[0].longValue() - (bnoi * justSort);
				tlv1 = anfc[1].longValue() - (bnob * justSort);
				mapOfObjectInfo.remove(cn);
			} else {
				tlv0 = -(bnoi * justSort);
				tlv1 = -(bnob * justSort);
			}
			newListOfObjectInfo.add(new ObjectInfo(tlv0, tlv1, cn));
			if (isCUI) {
				maxClLength[0] = compareLength(new Long(tlv0).toString().length(),
						maxClLength[0]);
				maxClLength[1] = compareLength(new Long(tlv1).toString().length(),
						maxClLength[1]);
			}
		}
		if (mapOfObjectInfo.size() > 0) {
			Iterator<Entry<String, Long[]>> it = mapOfObjectInfo.entrySet()
					.iterator();
			while (it.hasNext()) {
				Entry<String, Long[]> en = it.next();
				Long[] tv = en.getValue();
				newListOfObjectInfo.add(new ObjectInfo(tv[0].longValue(), tv[1]
						.longValue(), en.getKey()));
				if (isCUI) {
					maxClLength[0] = compareLength(tv[0].toString().length(),
							maxClLength[0]);
					maxClLength[1] = compareLength(tv[1].toString().length(),
							maxClLength[1]);
				}
			}
		}
		//--- to here

		//--- sorting result - need for CSV - from here
		ObjectInfo[] oa = (ObjectInfo[])newListOfObjectInfo.toArray(new ObjectInfo[]{});
		Arrays.sort(oa, new CommonDataComparator(sortType, isDesOrd));

		return oa;
	}

	private int compareLength(int i1, int i2) {
		if (i1 > i2) {
			return i1;
		} else {
			return i2;
		}
	}

	private static String fillMonoChar(int length, char chr) {
		if (length > 0) {
			char[] cbuf = new char[length];
			Arrays.fill(cbuf, chr);
			return new String(cbuf);
		} else {
			return "";
		}
	}

	private static void showVersion(String productName) {
		System.out.println(productName + " " + getI18nMessages("version") + " "
				+ version);
	}

	private static void showUsage(String className) {
		System.out.println(getI18nMessages("usage") + ": java " + className
				+ " [" + HLP_OPS + "]" + " [" + VAR_OPS + "]" + " [" + SORT_OPS
				+ SUBOPS_DELI + "{" + CNT_SUBOPS + "|" + BYT_SUBOPS + "|"
				+ CLS_SUBOPS + "}]" + " [" + ORD_OPS + SUBOPS_DELI + "{"
				+ DES_SUBOPS + "|" + ASC_SUBOPS + "}]" + " [" + TAB_OPS
				+ SUBOPS_DELI + "<" + getI18nMessages("tab_number") + ">]"
				+ " [" + FORMAT_OPS + SUBOPS_DELI + "{" + STD_SUBOPS + "|"
				+ CSV_SUBOPS + "[," + QUT_SUBPARAMS + EQUAL + "<" + getI18nMessages("quote_str") +">" +"]"
				+ "}] <jmap " + getI18nMessages("histogram") + " 1> [<jmap "
				+ getI18nMessages("histogram") + " 2>]");
	}

	private static void showOptions() {
		System.out.println(getI18nMessages("options") + ":\n" + SPACES
				+ HLP_OPS + " | " + HLP_SU_OPS + "\t:"
				+ getI18nMessages("show_help") + "\n" + SPACES + VAR_OPS
				+ " | " + VAR_SU_OPS + "\t:" + getI18nMessages("show_version")
				+ "\n" + SPACES + SORT_OPS + SUBOPS_DELI + "<"
				+ getI18nMessages("sub_ops") + "> | " + SORT_SU_OPS
				+ SUBOPS_DELI + "<" + getI18nMessages("sub_ops") + ">\t:"
				+ getI18nMessages("sort_target") + "\n" + SPACES + "\t"
				+ getI18nMessages("usub_ops") + ":\n" + SPACES + "\t" + SPACES
				+ CNT_SUBOPS + " | " + CNT_SU_SUBOPS + "\t:"
				+ getI18nMessages("sort_by_instances") + "\n" + SPACES + "\t"
				+ SPACES + BYT_SUBOPS + " | " + BYT_SU_SUBOPS + "\t:"
				+ getI18nMessages("sort_by_bytes") + "\n" + SPACES + "\t"
				+ SPACES + CLS_SUBOPS + " | " + CLS_SU_SUBOPS + "\t:"
				+ getI18nMessages("sort_by_class") + "\n" + SPACES + ORD_OPS
				+ SUBOPS_DELI + "<" + getI18nMessages("sub_ops") + "> | "
				+ ORD_SU_OPS + SUBOPS_DELI + "<" + getI18nMessages("sub_ops")
				+ ">\t:" + getI18nMessages("sort_order") + "\n" + SPACES + "\t"
				+ getI18nMessages("usub_ops") + ":\n" + SPACES + "\t" + SPACES
				+ DES_SUBOPS + " | " + DES_SU_SUBOPS + "\t:"
				+ getI18nMessages("sort_in_des") + "\n" + SPACES + "\t"
				+ SPACES + ASC_SUBOPS + " | " + ASC_SU_SUBOPS + "\t:"
				+ getI18nMessages("sort_in_asc") + "\n" + SPACES + TAB_OPS
				+ SUBOPS_DELI + "<" + getI18nMessages("tab_number") + "> | "
				+ TAB_SU_OPS + SUBOPS_DELI + "<"
				+ getI18nMessages("tab_number") + ">\t:"
				+ getI18nMessages("tab_size") + "\n"
				+ SPACES + FORMAT_OPS + SUBOPS_DELI + "<" + getI18nMessages("sub_ops") + "> | "
				+ FORMAT_SU_OPS + SUBOPS_DELI + "<" + getI18nMessages("sub_ops")
				+ ">\t:" + getI18nMessages("result_format") + "\n" + SPACES + "\t"
				+ getI18nMessages("usub_ops") + ":\n" + SPACES + "\t" + SPACES
				+ STD_SUBOPS + " | " + STD_SU_SUBOPS + "\t:"
				+ getI18nMessages("stand_format") + "\n" + SPACES + "\t"
				+ SPACES + CSV_SUBOPS + "[," + QUT_SUBPARAMS + EQUAL + "<" + getI18nMessages("quote_str") +">" +"]" + " | "
				+ CSV_SU_SUBOPS + "[," + QUT_SU_SUBPARAMS + EQUAL + "<" + getI18nMessages("quote_str") +">" +"]\t:"
				+ getI18nMessages("csv_format"));
	}
}