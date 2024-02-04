package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.subTurret;

public class cmdTurret_TeleOp extends Command {
  private subTurret turret;
  private final DoubleSupplier XSupplier;
  public cmdTurret_TeleOp(subTurret turret, DoubleSupplier XSupplier) {
    this.turret = turret;
    this.XSupplier = XSupplier;
    addRequirements(turret);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    turret.teleOp(XSupplier.getAsDouble());
  }

  @Override
  public void end(boolean interrupted) {
    turret.stop();
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
