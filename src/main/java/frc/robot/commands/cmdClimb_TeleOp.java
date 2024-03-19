package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.subClimb;

public class cmdClimb_TeleOp extends Command {
  subClimb climb;
  DoubleSupplier leftSpeed;
  DoubleSupplier rightSpeed;
  public cmdClimb_TeleOp(subClimb climb, DoubleSupplier leftSpeed, DoubleSupplier rightSpeed) {
    this.climb = climb;
    this.leftSpeed = leftSpeed;
    this.rightSpeed = rightSpeed;
    addRequirements(climb);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    climb.teleOp(leftSpeed.getAsDouble(), rightSpeed.getAsDouble());
  }

  @Override
  public void end(boolean interrupted) {
    climb.stop();
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
