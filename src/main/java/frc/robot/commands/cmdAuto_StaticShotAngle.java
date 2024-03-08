package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.subShotAngle;

public class cmdAuto_StaticShotAngle extends Command {
  subShotAngle angle;
  double goal;
  PIDController anglePID = new PIDController(0.007, 0, 0);
  public cmdAuto_StaticShotAngle(subShotAngle angle, double goal) {
    this.angle = angle;
    this.goal = goal;
    anglePID.setIntegratorRange(-0.8, 0.8);
    anglePID.setTolerance(2);
    addRequirements(angle);
  }

  @Override
  public void initialize() {}
  @Override
  public void execute() {
    angle.teleOp(-anglePID.calculate(angle.getPosition(), goal));
    //System.out.println("ShotAngle: " + -anglePID.calculate(angle.getPosition(), goal));
  }
  @Override
  public void end(boolean interrupted) {  }
  @Override
  public boolean isFinished() { return false; }
}
