package osmo.tester.unittests.parser;

import org.junit.Before;
import org.junit.Test;
import osmo.tester.OSMOConfiguration;
import osmo.tester.generator.SingleInstanceModelFactory;
import osmo.tester.generator.testsuite.TestSuite;
import osmo.tester.model.FSM;
import osmo.tester.model.FSMTransition;
import osmo.tester.model.Requirements;
import osmo.tester.parser.MainParser;
import osmo.tester.parser.ParserResult;
import osmo.tester.unittests.testmodels.GroupModel1;
import osmo.tester.unittests.testmodels.GroupModelInvalid;
import osmo.tester.unittests.testmodels.PartialModel1;
import osmo.tester.unittests.testmodels.PartialModel2;

import static junit.framework.Assert.*;

/** @author Teemu Kanstren */
public class GroupParserTests {
  private MainParser parser = null;
  private OSMOConfiguration config = null;

  @Before
  public void setup() {
    config = new OSMOConfiguration();
    config.setMethodBasedNaming(true);
    parser = new MainParser(config);
  }

  private OSMOConfiguration conf(Object... modelObjects) {
    SingleInstanceModelFactory factory = new SingleInstanceModelFactory();
    config.setFactory(factory);
    for (Object mo : modelObjects) {
      factory.add(mo);
    }
    return config;
  }

  @Test
  public void testValidModel() throws Exception {
    GroupModel1 model = new GroupModel1();
    ParserResult result = parser.parse(1, conf(model), new TestSuite());
    FSM fsm = result.getFsm();
    assertEquals("Number of @Before methods", 0, fsm.getBeforeTests().size());
    assertEquals("Number of @BeforeSuite methods", 0, fsm.getBeforeSuites().size());
    assertEquals("Number of @After methods", 0, fsm.getAfterTests().size());
    assertEquals("Number of @AfterSuite methods", 0, fsm.getAfterSuites().size());
    assertEquals("Number of end conditions", 0, fsm.getEndConditions().size());
    assertEquals("Number of exploration enablers", 0, fsm.getExplorationEnablers().size());
    FSMTransition step1 = fsm.getTransition("step1");
    assertEquals("Number of guards", 2, step1.getGuards().size());
    assertEquals("Number of pre's", 0, step1.getPreMethods().size());
    assertEquals("Number of post's", 0, step1.getPostMethods().size());
    FSMTransition step2 = fsm.getTransition("step2");
    assertEquals("Number of guards", 2, step2.getGuards().size());
    assertEquals("Number of pre's", 0, step2.getPreMethods().size());
    assertEquals("Number of post's", 0, step2.getPostMethods().size());
    FSMTransition step3 = fsm.getTransition("step3");
    assertEquals("Number of guards", 3, step3.getGuards().size());
    assertEquals("Number of pre's", 0, step3.getPreMethods().size());
    assertEquals("Number of post's", 0, step3.getPostMethods().size());
    FSMTransition step4 = fsm.getTransition("step4");
    assertEquals("Number of guards", 3, step4.getGuards().size());
    assertEquals("Number of pre's", 1, step4.getPreMethods().size());
    assertEquals("Number of post's", 1, step4.getPostMethods().size());

    assertEquals("Group name", "group1", step1.getGroupName().toString());
    assertEquals("Group name", "group1", step2.getGroupName().toString());
    assertEquals("Group name", "group1", step3.getGroupName().toString());
    assertEquals("Group name", "big-group", step4.getGroupName().toString());
  }

  @Test
  public void testTwoModels() throws Exception {
    Requirements req = new Requirements();
    PartialModel1 model1 = new PartialModel1(req);
    PartialModel2 model2 = new PartialModel2(req);

    ParserResult result = parser.parse(1, conf(model1, model2), new TestSuite());
    FSM fsm = result.getFsm();
    FSMTransition hello = fsm.getTransition("Hello");
    FSMTransition world = fsm.getTransition("world");
    FSMTransition epixx = fsm.getTransition("epixx");
    assertEquals("Group name", "", hello.getGroupName().toString());
    assertEquals("Group name", "part2-group", world.getGroupName().toString());
    assertEquals("Group name", "part2-group", epixx.getGroupName().toString());

    //turn the order around to check that class group name is reset as should be
    model1 = new PartialModel1(req);
    model2 = new PartialModel2(req);
    result = parser.parse(1, conf(model2, model1), new TestSuite());
    fsm = result.getFsm();
    hello = fsm.getTransition("Hello");
    world = fsm.getTransition("world");
    epixx = fsm.getTransition("epixx");
    assertEquals("Group name", "", hello.getGroupName().toString());
    assertEquals("Group name", "part2-group", world.getGroupName().toString());
    assertEquals("Group name", "part2-group", epixx.getGroupName().toString());
  }

  @Test
  public void testInvalidModel() throws Exception {
    GroupModelInvalid model = new GroupModelInvalid();
    try {
      ParserResult result = parser.parse(1, conf(model), new TestSuite());
    } catch (Exception e) {
      String expected = "Invalid test model:\n" +
              "@Group must have name.\n" +
              "@Guard without matching step:group1.\n" +
              "@Pre without matching step:group1.\n" +
              "@Post without matching step:group1.\n" +
              "Group name same as a step name (step3). Must be different.\n";
      assertEquals("Errors for parsing invalid group model", expected, e.getMessage());
    }
  }
}
