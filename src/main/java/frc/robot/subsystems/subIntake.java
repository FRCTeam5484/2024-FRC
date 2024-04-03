package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class subIntake extends SubsystemBase {
  private final int kIntakeMotorId = 10;
  private final CANSparkMax intakeMotor = new CANSparkMax(kIntakeMotorId, MotorType.kBrushless);
  private final DigitalInput noteSensor = new DigitalInput(0);
  private Timer timer = new Timer();
    
  public subIntake() {
    intakeMotor.restoreFactoryDefaults();
    intakeMotor.setIdleMode(IdleMode.kCoast);
    intakeMotor.setInverted(true);
    intakeMotor.burnFlash();
  }

  public boolean hasNote(){
    return !noteSensor.get();
  }

  public void stop(){
    intakeMotor.stopMotor();
  }

  public void forward(){
    intakeMotor.set(1);
  }

  public void floorIntake(){
    intakeMotor.set(0.9);
  }

  public void reverse(){
    intakeMotor.set(-1);
  }

  public void teleOp(double speed){
    intakeMotor.set(speed);
  }

  public void startFlash(){
    timer.reset();
    timer.start();
    NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(2);
  }

  @Override
  public void periodic() {
    SmartDashboard.putBoolean("Note Sensor", !noteSensor.get());
    if(timer.get() > 1){
      NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(1);
      timer.stop();
    }
  }
}