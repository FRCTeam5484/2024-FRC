package frc.robot.commands;

import java.util.function.DoubleSupplier;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.subShotAngle;

public class cmdShotAngle_TeleOp extends Command {
  subShotAngle angle;
  DoubleSupplier speed;
  public cmdShotAngle_TeleOp(subShotAngle angle, DoubleSupplier speed) {
    this.angle = angle;
    this.speed = speed;
    addRequirements(angle);
  }

  @Override
  public void initialize() {
  }

  @Override
  public void execute() {
    angle.teleOp(speed.getAsDouble());
  }

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return false;
  }
}
