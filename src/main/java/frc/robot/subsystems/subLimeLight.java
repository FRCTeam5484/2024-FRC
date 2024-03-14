package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.classes.LimelightHelpers;

public class subLimeLight extends SubsystemBase {

  public subLimeLight() {}

  public double getY(){
    return LimelightHelpers.getTY("limelight")+15.819;
  }
  public double getX(){
    return LimelightHelpers.getTX("limelight")-8.99;
  }
  public double getA(){
    return LimelightHelpers.getTA("limelight")-3.66;
  }
  public boolean hasTarget(){
    return LimelightHelpers.getTV("limelight");
  }

  @Override
  public void periodic() {
    SmartDashboard.putBoolean("Limelight Has Target", hasTarget());
    SmartDashboard.putNumber("LimeLight X", getX());
    SmartDashboard.putNumber("LimeLight Y", getY());
    SmartDashboard.putNumber("LimeLight A", getA());
  }
}
