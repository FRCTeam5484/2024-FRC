package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class subIntake extends SubsystemBase {
  private final int kIntakeMotorId = 10;
  private final CANSparkMax intakeMotor = new CANSparkMax(kIntakeMotorId, MotorType.kBrushless);;
    
  public subIntake() {
    intakeMotor.restoreFactoryDefaults();
    intakeMotor.setIdleMode(IdleMode.kCoast);
    intakeMotor.setInverted(true);
    intakeMotor.burnFlash();
  }

  public void stop(){
    intakeMotor.stopMotor();
  }

  public void forward(){
    intakeMotor.set(1);
  }

  public void reverse(){
    intakeMotor.set(-1);
  }

  public void teleOp(double speed){
    intakeMotor.set(speed);
  }

  @Override
  public void periodic() {
    
  }
}
