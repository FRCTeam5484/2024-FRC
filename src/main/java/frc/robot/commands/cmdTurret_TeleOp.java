package frc.robot.commands;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.subTurret;

public class cmdTurret_TeleOp extends Command {
  subTurret turret;
  BooleanSupplier shooterSafe;
  DoubleSupplier speed;
  public cmdTurret_TeleOp(subTurret turret, DoubleSupplier speed, BooleanSupplier shooterSafe) {
    this.turret = turret;
    this.speed = speed;
    this.shooterSafe = shooterSafe;
    addRequirements(turret);
  }

  @Override
  public void initialize() {}
  @Override
  public void execute() { 
    if(shooterSafe.getAsBoolean()) {
      turret.teleOp(speed.getAsDouble()); 
    } else {
      turret.stop();
    }
  }
    
  @Override
  public void end(boolean interrupted) { turret.stop(); }
  @Override
  public boolean isFinished() { return false; }
}