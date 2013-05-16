package quitada;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.io.FileReader;
import java.io.BufferedReader;

public class JavaObjectDiff {
	private static boolean debug = false;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length < 2) {
			System.out.println("JavaObjectDiff version 0.1");
			System.out.println("Usage: java quitada.JavaObjectDiff <jmap histo file 1> <jmap histo file 2>");
			System.exit(0);
		}

		ArrayList<ObjectCounts> listOfObjectCounts = new ArrayList<ObjectCounts>();
		HashMap<String, Long> mapOfObjectCounts = new HashMap<String, Long>();

		for (int n=0; n < 2; n++) {
			try {
				FileReader f = new FileReader(args[n]);
				BufferedReader b = new BufferedReader(f);
				String s;
				while ((s = b.readLine()) != null){
					if (s.contains(":")) {
						//System.out.println(s);
						String columbs[] = s.split(" ");
						int u = 0;
						Long noi = (long) 0;
						String cn = null;
						for (int i =0; i < columbs.length; i++) {

							if (!columbs[i].equals("")) {
								switch (u) {
								case 1:
									noi = new Long(columbs[i]);
									//System.out.print("[#instances]=" + noi + "/");
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
							listOfObjectCounts.add(new ObjectCounts(noi.longValue(), cn));
							//System.out.print("["+nor+"] noi=" + nois[nor] + "/cn=" + cns[nor]);
						} else {
							mapOfObjectCounts.put(cn, noi);
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
					for (int i = 0; i < listOfObjectCounts.size() ; i++) {
						ObjectCounts oc = listOfObjectCounts.get(i);
						System.out.println("["+i+"] noi=" + oc.getNumOfInstance() +
								"/cn=" + oc.getClassName());
					}
				} else {
					System.out.println("---- after");
					System.out.println("map size=" + mapOfObjectCounts.size());
				}
			}
		}

		ArrayList<ObjectCounts> newListOfObjectCounts = new ArrayList<ObjectCounts>();
		for (int i = 0; i < listOfObjectCounts.size(); i++){
			ObjectCounts boc = listOfObjectCounts.get(i);
			String cn = boc.getClassName();
			long bnoi = boc.getNumOfInstance();
			Long anoi = mapOfObjectCounts.get(cn);
			if (anoi != null) {
				//System.out.println("Diff of noi =" + (anoi.longValue() - bnoi) + "/cn=" +cn);
				newListOfObjectCounts.add(new ObjectCounts((anoi.longValue() - bnoi), cn));
				mapOfObjectCounts.remove(cn);
			} else {
				//System.out.println("Diff of noi =" + (-bnoi) + "/cn=" +cn);
				newListOfObjectCounts.add(new ObjectCounts((-bnoi), cn));
			}
		}
		//System.out.println("remaind map size=" + mapOfObjectCounts.size());
		if (mapOfObjectCounts.size() > 0) {
			Iterator<Entry<String,Long>> it = mapOfObjectCounts.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, Long> en = it.next();
				//System.out.println("noi=" + en.getValue().longValue() + "/cn=" + en.getKey());
				newListOfObjectCounts.add(new ObjectCounts(en.getValue().longValue(), en.getKey()));
			}			
		}

		//System.out.println("Size of newListOfObjectCounts=" + newListOfObjectCounts.size());
		Object[] oa = newListOfObjectCounts.toArray();
		Arrays.sort(oa, new DataComparator());
		System.out.println("diff\t\tclass name");
		System.out.println("----------");
		for (int i = 0; i < oa.length ; i++) {
			System.out.println(((ObjectCounts)oa[i]).getNumOfInstance()
					+ "\t\t" + ((ObjectCounts)oa[i]).getClassName());
		}
	}
}



