package frc.robot.commands;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.subSwerve;

public class cmdSwerve_TeleOp extends Command {
  private final subSwerve swerve;
  private final DoubleSupplier XSupplier, YSupplier, rotationSupplier;
  private final BooleanSupplier boost;

  public cmdSwerve_TeleOp(subSwerve swerve, DoubleSupplier XSupplier, DoubleSupplier YSupplier, DoubleSupplier rotationSupplier, BooleanSupplier boost) {
    this.swerve = swerve;
    this.XSupplier = XSupplier;
    this.YSupplier = YSupplier;
    this.rotationSupplier = rotationSupplier;
    this.boost = boost;
    addRequirements(swerve);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    swerve.drive(XSupplier.getAsDouble()*Constants.DriveConstants.kMaxSpeedMetersPerSecond, YSupplier.getAsDouble()*Constants.DriveConstants.kMaxSpeedMetersPerSecond, rotationSupplier.getAsDouble()*Constants.DriveConstants.kMaxAngularSpeed, boost.getAsBoolean());
  }

  @Override
  public void end(boolean interrupted) { swerve.drive(0, 0, 0, false); }

  @Override
  public boolean isFinished() { return false; }
}
