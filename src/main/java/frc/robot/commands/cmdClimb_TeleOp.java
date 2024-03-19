package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.subClimb;

public class cmdClimb_TeleOp extends Command {
  subClimb climb;
  DoubleSupplier speed;
  public cmdClimb_TeleOp(subClimb climb, DoubleSupplier speed) {
    this.climb = climb;
    this.speed = speed;
    addRequirements(climb);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    climb.teleOp(speed.getAsDouble());
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
