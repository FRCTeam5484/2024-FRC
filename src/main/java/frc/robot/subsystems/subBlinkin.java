package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class subBlinkin extends SubsystemBase {
  private Spark blinkin = new Spark(0);
  public subBlinkin() {

  }

  public void off(){
    blinkin.set(0.99);
  }

  public void white(){
    blinkin.set(0.93);
  }

  public void red(){
    blinkin.set(0.61);
  }

  public void green(){
    blinkin.set(0.77);
  }

  public void blue(){
    blinkin.set(0.87);
  }

  public void orange(){
    blinkin.set(0.65);
  }

  public void lime(){
    blinkin.set(0.73);
  }

  public void teamColorsWaves(){
    blinkin.set(0.53);
  }

  public void teamColorsSparkle(){
    blinkin.set(0.37);
  }

  public void strobeRed(){
    blinkin.set(-0.17);
  }

  public void strobeBlue(){
    blinkin.set(-0.09);
  }

  public void strobeGold(){
    blinkin.set(-0.07);
  }

  public void strobeWhite(){
    blinkin.set(-0.05);
  }

  public void breathRed(){
    blinkin.set(-0.25);
  }

  public void breathBlue(){
    blinkin.set(-0.25);
  }

  public void breathGray(){
    blinkin.set(-0.25);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
