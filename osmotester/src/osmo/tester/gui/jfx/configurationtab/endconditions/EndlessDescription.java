package osmo.tester.gui.jfx.configurationtab.endconditions;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import osmo.tester.generator.endcondition.EndCondition;
import osmo.tester.generator.endcondition.Endless;
import osmo.tester.gui.jfx.configurationtab.BasicsPane;

/**
 * @author Teemu Kanstren
 */
public class EndlessDescription implements ECDescription {
  private Endless endless = new Endless();

  @Override
  public EndCondition getEndCondition() {
    return endless;
  }

  @Override
  public Pane createEditor(BasicsPane parent, Stage stage) {
    return null;
  }

  @Override
  public boolean supportsEditing() {
    return false;
  }

  @Override
  public String toString() {
    return "Endless";
  }
}
