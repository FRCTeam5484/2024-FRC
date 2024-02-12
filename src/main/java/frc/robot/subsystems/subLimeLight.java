package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class subLimeLight extends SubsystemBase {
  private NetworkTable networkTable;
  private NetworkTableEntry ty,tv,tx,ta,pipeline;

  public subLimeLight() {
    
  }

  @Override
  public void periodic() { 
    getValues();
  }

  public void getValues(){
    networkTable = NetworkTableInstance.getDefault().getTable("limelight");
    ty = networkTable.getEntry("ty");
    tv = networkTable.getEntry("tv");
    tx = networkTable.getEntry("tx"); 
    ta = networkTable.getEntry("ta"); 
    pipeline = networkTable.getEntry("pipeline");
  }

  public double getTY(){
    return ty.getDouble(0.0);
  }

  public double getTV(){
    return tv.getDouble(0.0);
  }

  public double getTX(){
    return tx.getDouble(0.0);
  }

  public double getTA(){
    return ta.getDouble(0.0);
  }

  public boolean hasTarget(){
    return tv.getDouble(0.0) == 1 ? true : false;
  }

  public void setToDriverMode(){
    pipeline.setNumber(0);
  }

  public void setToSpeakerBlueSide(){
    pipeline.setNumber(1);
  }

  public void setToSpeakerRedSide(){
    pipeline.setNumber(2);
  }
}