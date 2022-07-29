package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by hug.
 */
public class TestBuggyAList {
    // YOUR TESTS HERE
    @Test
    public void testThreeAddThreeRemove() {
        AListNoResizing<Integer> list1 = new AListNoResizing<>();
        BuggyAList<Integer> list2 = new BuggyAList<>();
        list1.addLast(4);
        list2.addLast(4);
        list1.addLast(5);
        list2.addLast(5);
        list1.addLast(6);
        list2.addLast(6);
        assertEquals(list1.removeLast(), list2.removeLast());
        assertEquals(list1.removeLast(), list2.removeLast());
        assertEquals(list1.removeLast(), list2.removeLast());
    }

    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> B = new BuggyAList<>();

        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                B.addLast(randVal);
                //System.out.println("addLast(" + randVal + ")");
            } else if (operationNumber == 1) {
                // size
                int size1 = L.size();
                int size2 = B.size();
                //System.out.println("size: " + size1);
                assertEquals(size1, size2);
            } else if (operationNumber == 2) {
                if (L.size() > 0) {
                    int lastVal1 = L.getLast();
                    int lastVal2 = B.getLast();
                    //System.out.println("getLast(" + lastVal1 + ")");
                    assertEquals(lastVal1, lastVal2);
                }
            } else if (operationNumber == 3) {
                if (L.size() > 0) {
                    int lastVal1 = L.removeLast();
                    int lastVal2 = B.removeLast();
                    //System.out.println("removeLast(" + lastVal1 + ")");
                    assertEquals(lastVal1, lastVal2);
                }
            }
        }
    }

}
