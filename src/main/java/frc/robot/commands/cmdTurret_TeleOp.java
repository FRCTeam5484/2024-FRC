package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.Constants.Turret;
import frc.robot.subsystems.subTurret;

public class cmdTurret_TeleOp extends Command {
  private subTurret turret;
  private final DoubleSupplier XSupplier;
  private final DoubleSupplier shotAngleSupplier;
  public cmdTurret_TeleOp(subTurret turret, DoubleSupplier shotAnDoubleSupplier, DoubleSupplier XSupplier) {
    this.turret = turret;
    this.XSupplier = XSupplier;
    this.shotAngleSupplier = shotAnDoubleSupplier;
    addRequirements(turret);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    if(shotAngleSupplier.getAsDouble() > Constants.ShotAngleConstants.LowestTurretLimit){
      turret.stop();
      System.out.println("Shooter too low to move Turret");
    }
    else if(turret.getPosition() > Constants.Turret.Left || turret.getPosition() < Constants.Turret.Rear){
      turret.stop();
      System.out.println("Turret at limit");
    }
    else {
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
