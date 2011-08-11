package osmo.tester.unit;

import org.junit.Test;
import osmo.tester.model.dataflow.DataGenerationStrategy;
import osmo.tester.model.dataflow.ValueRange;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

/**
 * @author Teemu Kanstren
 */
public class ValueRangeTests {
  @Test
  public void optimizedRandomValueRange() {
    ValueRange vr = new ValueRange(5, 7);
    vr.setStrategy(DataGenerationStrategy.OPTIMIZED_RANDOM);
    boolean b5 = false;
    boolean b6 = false;
    boolean b7 = false;
    for (int a = 0 ; a < 10 ; a++) {
      for (int i = 0 ; i < 3 ; i++) {
        int n = vr.nextInt();
        if (n == 5) {
          b5 = true;
        }
        if (n == 6) {
          b6 = true;
        }
        if (n == 7) {
          b7 = true;
        }
      }
      assertTrue("Should generate value 5 (loop "+a+")", b5);
      assertTrue("Should generate value 6 (loop \"+a+\")", b6);
      assertTrue("Should generate value 7 (loop \"+a+\")", b7);
    }
  }

  @Test
  public void orderedLoopValueRange() {
    ValueRange vr = new ValueRange(5, 7);
    vr.setStrategy(DataGenerationStrategy.ORDERED_LOOP);
    assertEquals("First item", 5, vr.nextInt());
    assertEquals("Second item", 6, vr.nextInt());
    assertEquals("Third item", 7, vr.nextInt());
    assertEquals("Fourth item", 5, vr.nextInt());
  }

  @Test
  public void generics() {
    ValueRange<Integer> vr = new ValueRange<Integer>(1,5);
    Object o = vr.next();
    assertEquals("Integer value range should produce integers..", Integer.class, o.getClass());

    ValueRange<Long> vr2 = new ValueRange<Long>(1l,5l);
    Object o2 = vr2.next();
    assertEquals("Long value range should produce longs..", Long.class, o2.getClass());

    ValueRange<Double> vr3 = new ValueRange<Double>(1d,5d);
    Object o3 = vr3.next();
    assertEquals("Double value range should produce doubles..", Double.class, o3.getClass());

    ValueRange<Integer> vr4 = new ValueRange<Integer>(Integer.class, 1,5);
    Object o4 = vr4.next();
    assertEquals("Integer value range should produce integers..", Integer.class, o4.getClass());

    ValueRange<Long> vr5 = new ValueRange<Long>(Long.class, 1,5);
    Object o5 = vr5.next();
    assertEquals("Long value range should produce longs..", Long.class, o5.getClass());

    ValueRange<Double> vr6 = new ValueRange<Double>(Double.class, 1,5);
    Object o6 = vr6.next();
    assertEquals("Double value range should produce doubles..", Double.class, o6.getClass());
  }

  @Test
  public void boundaryScan() {
    fail("TBD");//add tests for boundaries, including size of boundary scan
  }
  //TODO: whine about reified generics
}
