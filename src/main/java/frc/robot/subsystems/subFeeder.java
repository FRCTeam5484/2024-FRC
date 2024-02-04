package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class subFeeder extends SubsystemBase {
  private final int kFeederMotor = 11;
  private final CANSparkMax feederMotor;
  
  public subFeeder() {
    feederMotor = new CANSparkMax(kFeederMotor, MotorType.kBrushless);
    feederMotor.restoreFactoryDefaults();
    feederMotor.setIdleMode(IdleMode.kBrake);
    feederMotor.setInverted(false);
    feederMotor.burnFlash();
  }

  public void stop(){
    feederMotor.stopMotor();
  }

  public void slowIntake(){
    feederMotor.set(.2);
  }

  public void forward(){
    feederMotor.set(1);
  }
  
  public void reverse(){
    feederMotor.set(-1);
  }

  @Override
  public void periodic() {
    
  }
}
