package quitada.test;

import quitada.Mononov;
import quitada.MononovFactory;
import quitada.ObjectInfo;
import quitada.MononovException;

public class MononovFactoryTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Mononov mnv = new MononovFactory()
			.setSortType("instance")
			.setSortOrder("asc")
			.setPathBefore("before.txt")
			.setPathAfter("after.txt")
			.create();

			ObjectInfo[] oi = mnv.runMerge();
			for (int i=0; i < oi.length ; i++) {
				System.out.println(
						"Instance:" + oi[i].getNumOfInstance() + ","
								+ "Byte:" + oi[i].getNumOfByte() + ","
								+ "Class:" + oi[i].getClassName()
						);
			}
		} catch(MononovException me) {
			me.printStackTrace();
		}

	}

}
