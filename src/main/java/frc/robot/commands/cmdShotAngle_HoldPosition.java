package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.subShotAngle;

public class cmdShotAngle_HoldPosition extends Command {
  subShotAngle angle;
  double position;
  PIDController anglePID = new PIDController(1, 0, 0);

  public cmdShotAngle_HoldPosition(subShotAngle angle) {
    this.angle = angle;
    anglePID.setIntegratorRange(-0.5, 0.5);
    addRequirements(angle);
  }

  @Override
  public void initialize() {
    position = angle.getPosition();
  }

  @Override
  public void execute() {
    angle.teleOp(-anglePID.calculate(angle.getPosition(), position));
  }

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return false;
  }
}
