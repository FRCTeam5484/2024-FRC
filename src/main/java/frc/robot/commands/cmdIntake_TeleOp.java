package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.subIntake;

public class cmdIntake_TeleOp extends Command {
  private subIntake intake;
  private DoubleSupplier speed;
  public cmdIntake_TeleOp(subIntake intake, DoubleSupplier speed) {
    this.intake = intake;
    this.speed = speed;
    addRequirements(intake);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() { intake.teleOp(speed.getAsDouble()); }
  @Override
  public void end(boolean interrupted) { intake.stop(); }
  @Override
  public boolean isFinished() { return false; }
}
