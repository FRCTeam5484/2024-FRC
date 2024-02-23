package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.subTurret;

public class cmdTurret_TeleOp extends Command {
  private subTurret turret;
  private final DoubleSupplier XSupplier;
  private final DoubleSupplier shotAnglePositionSupplier;
  private final Boolean safetyEnabled;
  public cmdTurret_TeleOp(subTurret turret, DoubleSupplier shotAnglePositionSupplier, DoubleSupplier XSupplier, Boolean safetyEnabled) {
    this.turret = turret;
    this.XSupplier = XSupplier;
    this.shotAnglePositionSupplier = shotAnglePositionSupplier;
    this.safetyEnabled = safetyEnabled;
    addRequirements(turret);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    if(safetyEnabled)
    {
      if(shotAnglePositionSupplier.getAsDouble() >= Constants.ShotAngleConstants.MinimumForTurret || 
        (turret.getPosition() < 0 && XSupplier.getAsDouble() < 0) || 
        (turret.getPosition() > 120 && XSupplier.getAsDouble() > 0) )
      {
        turret.teleOp(0);
      }
      else 
      {
        turret.teleOp(XSupplier.getAsDouble());
      }
      SmartDashboard.putNumber("Turret Command", XSupplier.getAsDouble());
    }
    else{
      turret.teleOp(XSupplier.getAsDouble());
    }
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