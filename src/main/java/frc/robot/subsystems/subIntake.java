package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class subIntake extends SubsystemBase {
  private final int kIntakeMotorId = 10;
  private final CANSparkMax intakeMotor;
    
  public subIntake() {
    intakeMotor = new CANSparkMax(kIntakeMotorId, MotorType.kBrushless);
    intakeMotor.restoreFactoryDefaults();
    intakeMotor.setIdleMode(IdleMode.kCoast);
    intakeMotor.setInverted(false);
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

  @Override
  public void periodic() {
    
  }
}
