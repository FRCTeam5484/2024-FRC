package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.subShotAngle;

public class cmdShotAngle_SetPoint extends Command {
  subShotAngle angle;
  double goal;
  PIDController anglePID = new PIDController(1, 0, 0);
  
  public cmdShotAngle_SetPoint(subShotAngle angle, double goal) {
    this.angle = angle;
    this.goal = goal;
    addRequirements(angle);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    angle.teleOp(-anglePID.calculate(angle.getPosition(), goal));
  }

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return false;
  }
}