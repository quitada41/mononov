package quitada;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.io.FileReader;
import java.io.BufferedReader;

public class JavaObjectDiff {
	private final boolean debug = false;
	private final String version = "0.2";
	private final String SPACES = "    ";
	private final String OPS_HEADER = "--";
	private final String SU_OPS_HEADER = "-";
	private final String SUBOPS_DELI = ":";
	private final String HLP_OPS = OPS_HEADER + "help";
	private final String HLP_SU_OPS = SU_OPS_HEADER + "h";
	private final String VAR_OPS = OPS_HEADER + "version";
	private final String VAR_SU_OPS = SU_OPS_HEADER + "v";
	private final String SORT_OPS = OPS_HEADER + "sort";
	private final String SORT_SU_OPS = SU_OPS_HEADER + "s";
	private final String CNT_SUBOPS = "count";
	private final String CNT_SU_SUBOPS = "c";
	private final String BYT_SUBOPS = "byte";
	private final String BYT_SU_SUBOPS = "b";

	public static void main(String[] args) {
		JavaObjectDiff mononov = new JavaObjectDiff();
		// "MONONOV" stands for "Merge Objects Numerically Ordered Nonetheless On Variety."
		// I'll name "MONONOV" to this product when I release version 1.0.
		mononov.runMerge(args);
	}

	public void runMerge(String[] argz) {
		int numOps = 0;
		boolean isCount = true;
		int argi;
		for (argi = 0 ; argi < argz.length ; argi++) {
			//System.out.println("Argument[" + argi + "] =\"" + argz[argi] +"\"");
			boolean validOps = false;
			if (argz[argi].equals(HLP_OPS) || argz[argi].equals(HLP_SU_OPS)) {
				this.showVersion();
				this.showUsage();
				this.showOptions();
				System.exit(0);
			}

			if (argz[argi].equals(VAR_OPS) || argz[argi].equals(VAR_SU_OPS)) {
				this.showVersion();
				System.exit(0);
			} 

			String ostr = "";
			int dindex = -1;
			try {
				dindex = argz[argi].indexOf(SUBOPS_DELI);
				ostr = argz[argi].substring(0, dindex);
			} catch (StringIndexOutOfBoundsException ex) {
				;
			}

			//System.out.println(ostr);
			if (ostr.equals(SORT_OPS) || ostr.equals(SORT_SU_OPS)) {
				String subops = argz[argi].substring(dindex + 1);
				//System.out.println(subops);

				if (subops.equals(CNT_SUBOPS) || subops.equals(CNT_SU_SUBOPS)) {
					numOps++;
					validOps = true;
				} else if (subops.equals(BYT_SUBOPS) || subops.equals(BYT_SU_SUBOPS)) {
					isCount = false;
					numOps++;
					validOps = true;
				}
			}

			if (!validOps && ( argz[argi].startsWith(OPS_HEADER) || argz[argi].startsWith(SU_OPS_HEADER))) {
				System.out.println("You specify an invalid option: " + argz[argi]);
				this.showUsage();
				this.showOptions();
				System.exit(1);				
			}
		}

		if (argi == 0) {
			System.out.println("You have to specify one or more arguments.");
			this.showUsage();
			System.exit(1);
		}

		//System.out.println(numOps);
		//System.out.println(argz.length);
		if (argz.length < (2 + numOps)) {
			System.out.println("You have to specify two file names for objects histgrams.");
			this.showUsage();
			System.exit(1);
		}


		ArrayList<ObjectInfo> listOfObjectInfo = new ArrayList<ObjectInfo>();
		HashMap<String, Long[]> mapOfObjectInfo = new HashMap<String, Long[]>();

		for (int n=0; n < 2; n++) {
			try {
				FileReader f = new FileReader(argz[(n + numOps)]);
				BufferedReader b = new BufferedReader(f);
				String s;
				while ((s = b.readLine()) != null){
					if (s.contains(":")) {
						//System.out.println(s);
						String columbs[] = s.split(" ");
						int u = 0;
						Long noi = (long) 0;
						Long nob = (long) 0;
						String cn = null;
						for (int i =0; i < columbs.length; i++) {
							if (!columbs[i].equals("")) {
								switch (u) {
								case 1:
									noi = new Long(columbs[i]);
									//System.out.print("[#instances]=" + noi + "/");
									break;
								case 2:
									nob = new Long(columbs[i]);
									//System.out.print("[#bytes]=" + noi + "/");
									break;
								case 3:
									cn = new String(columbs[i]);
									//System.out.print("[class name]=" + cn);
									break;
								}
								u++;
								//System.out.print("["+i+"]" + columbs[i] + "/");
							}
						}
						if (n==0) {
							listOfObjectInfo.add(new ObjectInfo(noi.longValue(), nob.longValue(), cn));
							//System.out.print("["+nor+"] noi=" + nois[nor] + "/cn=" + cns[nor]);
						} else {
							Long[] nfc = new Long[2];
							nfc[0] = noi;
							nfc[1] = nob;
							mapOfObjectInfo.put(cn, nfc);
							//System.out.println("nor=" + noi + "/cn=" + cn);
						}

						//System.out.print("\n");
					}
				}
				b.close();
				f.close();
			} catch (Exception e){
				e.printStackTrace();
				System.exit(0);
			}
			if (debug) {
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
			}
		}

		ArrayList<ObjectInfo> newListOfObjectInfo = new ArrayList<ObjectInfo>();
		for (int i = 0; i < listOfObjectInfo.size(); i++){
			ObjectInfo boc = listOfObjectInfo.get(i);
			String cn = boc.getClassName();
			long bnoi = boc.getNumOfInstance();
			long bnob = boc.getNumOfByte();
			Long[] anfc = mapOfObjectInfo.get(cn);
			if (anfc != null) {
				//System.out.println("Diff of noi =" + (anoi.longValue() - bnoi) + "/cn=" +cn);
				newListOfObjectInfo.add(new ObjectInfo((anfc[0].longValue() - bnoi), (anfc[1].longValue() - bnob), cn));
				mapOfObjectInfo.remove(cn);
			} else {
				//System.out.println("Diff of noi =" + (-bnoi) + "/cn=" +cn);
				newListOfObjectInfo.add(new ObjectInfo((-bnoi), (-bnob), cn));
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
			}			
		}

		//System.out.println("Size of newListOfObjectCounts=" + newListOfObjectCounts.size());
		Object[] oa = newListOfObjectInfo.toArray();
		System.out.print("Sorted by objects ");
		if (isCount) {
			Arrays.sort(oa, new InstanceComparator());
			System.out.println("count");
		} else {
			Arrays.sort(oa, new ByteComparator());
			System.out.println("byte");
		}
		System.out.println("diff of #instances\tdiff of #bytes\t\tclass name");
		System.out.println("------------------\t------------------\t----------");
		for (int i = 0; i < oa.length ; i++) {
			System.out.println(((ObjectInfo)oa[i]).getNumOfInstance()
					+ "\t\t\t" + ((ObjectInfo)oa[i]).getNumOfByte()
					+ "\t\t\t" + ((ObjectInfo)oa[i]).getClassName());
		}
	}


	private void showVersion() {
		System.out.println(this.getClass().getSimpleName() + " version " + version);
	}

	private void showUsage() {
		// to do: add --order={dsc/asc} for version 0.3
		System.out.println("Usage: java " + this.getClass().getName()
				+ " [" + HLP_OPS + "]"
				+ " [" + VAR_OPS+"]"
				+ " [" + SORT_OPS + SUBOPS_DELI + "{" + CNT_SUBOPS + "/" + BYT_SUBOPS + "}]"
				+ " <jmap histo file 1> <jmap histo file 2>");
	}

	private void showOptions() {
		// to do: add --order={dsc/asc} for version 0.3
		System.out.println("Options:\n"
				+ SPACES + HLP_OPS +" | " + HLP_SU_OPS + "\t:Show this help.\n"
				+ SPACES + VAR_OPS +" | " + VAR_SU_OPS + "\t:Show version of this tool.\n"
				+ SPACES + SORT_OPS + SUBOPS_DELI + "<sub ops> | " + SORT_SU_OPS + SUBOPS_DELI + "<sub ops>\t:Specify sort target.\n"
				+ SPACES + "\tSub options:\n"
				+ SPACES + "\t" + SPACES + CNT_SUBOPS +" | " + CNT_SU_SUBOPS + "\t:Sort by objects count(DEFAULT behavior).\n"
				+ SPACES + "\t" + SPACES + BYT_SUBOPS +" | " + BYT_SU_SUBOPS + "\t:Sort by objects byte.\n"
				);
	}
}



