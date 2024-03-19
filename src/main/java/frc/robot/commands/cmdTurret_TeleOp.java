package frc.robot.commands;

import java.util.function.DoubleSupplier;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.subTurret;

public class cmdTurret_TeleOp extends Command {
  subTurret turret;
  DoubleSupplier speed;
  public cmdTurret_TeleOp(subTurret turret, DoubleSupplier speed) {
    this.turret = turret;
    this.speed = speed;
    addRequirements(turret);
  }

  @Override
  public void initialize() {}
  @Override
  public void execute() { 
    turret.teleOp(speed.getAsDouble()); 
  }
    
  @Override
  public void end(boolean interrupted) { turret.stop(); }
  @Override
  public boolean isFinished() { return false; }
}