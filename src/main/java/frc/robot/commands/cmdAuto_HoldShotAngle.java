package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.subShotAngle;

public class cmdAuto_HoldShotAngle extends Command {
  subShotAngle angle;
  double goal;
  PIDController anglePID = new PIDController(0.005, 0, 0);
  public cmdAuto_HoldShotAngle(subShotAngle angle) {
    this.angle = angle;
    anglePID.setIntegratorRange(-0.1, 0.6);
    anglePID.setTolerance(1);
    addRequirements(angle);
  }

  @Override
  public void initialize() {
    goal = angle.getPosition();
   }

  @Override
  public void execute() {
    angle.teleOp(-anglePID.calculate(angle.getPosition(), goal));
    //System.out.println("Position: " + angle.getPosition() + " Goal: " + goal + " Correction: " + -anglePID.calculate(angle.getPosition(), goal));
  }

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return false;
  }
}
