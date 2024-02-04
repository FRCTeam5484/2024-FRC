package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.subShotAngle;

public class cmdShotAngle_TeleOp extends Command {
  private subShotAngle shotAngle;
  private final DoubleSupplier YSupplier;
  public cmdShotAngle_TeleOp(subShotAngle shotAngle, DoubleSupplier YSupplier) {
    this.shotAngle = shotAngle;
    this.YSupplier = YSupplier;
    addRequirements(shotAngle);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    shotAngle.teleOp(YSupplier.getAsDouble());
  }

  @Override
  public void end(boolean interrupted) {
    shotAngle.stop();
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
