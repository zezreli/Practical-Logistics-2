package sonar.logistics.helpers;

import sonar.logistics.PL2ASMLoader;
import sonar.logistics.info.comparators.ILogicComparator;
import sonar.logistics.info.comparators.ObjectComparator;

public class ComparatorHelper {

	public static ILogicComparator DEFAULT = new ObjectComparator();
	
	public static ILogicComparator getComparator(String name) {
		ILogicComparator comparator = PL2ASMLoader.comparatorClasses.get(name);
		return comparator == null ? DEFAULT : comparator;
	}

}