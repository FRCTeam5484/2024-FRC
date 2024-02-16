package frc.robot.subsystems;

import com.ctre.phoenix6.configs.Pigeon2Configuration;
import com.ctre.phoenix6.hardware.Pigeon2;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.SwerveConstants;
import frc.robot.classes.swerveModule;

public class subSwerve extends SubsystemBase {
  public static final double kFrontLeftOffset = 0.1880;
  public static final double kFrontRightOffset = 0.9524;
  public static final double kRearLeftOffset = 0.3767;
  public static final double kRearRightOffset = 0.8359;

  public static final int kFrontLeftDrivingCanId = 1;
  public static final int kFrontRightDrivingCanId = 3;
  public static final int kRearLeftDrivingCanId = 5;
  public static final int kRearRightDrivingCanId = 7;

  public static final int kFrontLeftTurningCanId = 2;
  public static final int kFrontRightTurningCanId = 4;
  public static final int kRearLeftTurningCanId = 6;
  public static final int kRearRightTurningCanId = 8;

  public static final int kFrontLeftCANcoder = 2;
  public static final int kFrontRightCANcoder = 4;
  public static final int kRearLeftCANcoder = 6;
  public static final int kRearRightCANcoder = 8;

  private final swerveModule frontLeftModule = new swerveModule(kFrontLeftDrivingCanId,kFrontLeftTurningCanId,kFrontLeftCANcoder,kFrontLeftOffset);
  private final swerveModule frontRightModule = new swerveModule(kFrontRightDrivingCanId,kFrontRightTurningCanId,kFrontRightCANcoder,kFrontRightOffset);
  private final swerveModule rearLeftModule = new swerveModule(kRearLeftDrivingCanId,kRearLeftTurningCanId,kRearLeftCANcoder,kRearLeftOffset);
  private final swerveModule rearRightModule = new swerveModule(kRearRightDrivingCanId,kRearRightTurningCanId,kRearRightCANcoder,kRearRightOffset);

  private final Pigeon2 gyro;
  public SwerveDriveOdometry odometry;

  public subSwerve() {
    gyro = new Pigeon2(1);
    gyro.getConfigurator().apply(new Pigeon2Configuration());
    gyro.setYaw(0);

    odometry = new SwerveDriveOdometry(
      SwerveConstants.kDriveKinematics,
      gyro.getRotation2d(),
      new SwerveModulePosition[] {
        frontLeftModule.getPosition(),
        frontRightModule.getPosition(),
        rearLeftModule.getPosition(),
        rearRightModule.getPosition()
      });
  }

  public Pose2d getPose() { return odometry.getPoseMeters(); }
  public void resetPose(Pose2d pose) {
    odometry.resetPosition(
      getRotation2d(),
      new SwerveModulePosition[] {
        frontLeftModule.getPosition(),
        frontRightModule.getPosition(),
        rearLeftModule.getPosition(),
        rearRightModule.getPosition()
      },
      pose);
  }
  public void updateOdometry(){
    odometry.update(
    getRotation2d(),
      new SwerveModulePosition[] {
        frontLeftModule.getPosition(),
        frontRightModule.getPosition(),
        rearLeftModule.getPosition(),
        rearRightModule.getPosition()
      });
  }

  
  public void drive(double xSpeed, double ySpeed, double rot) {
    var swerveModuleStates = SwerveConstants.kDriveKinematics.toSwerveModuleStates(ChassisSpeeds.fromFieldRelativeSpeeds(xSpeed, ySpeed, rot, getRotation2d()));
    SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, Constants.DriveConstants.kMaxSpeedMetersPerSecond);
    frontLeftModule.setDesiredState(swerveModuleStates[0]);
    frontRightModule.setDesiredState(swerveModuleStates[1]);
    rearLeftModule.setDesiredState(swerveModuleStates[2]);
    rearRightModule.setDesiredState(swerveModuleStates[3]);
  }

  public void setModuleStates(SwerveModuleState[] desiredStates) {
    SwerveDriveKinematics.desaturateWheelSpeeds(desiredStates, Constants.DriveConstants.kMaxSpeedMetersPerSecond);
    frontLeftModule.setDesiredState(desiredStates[0]);
    frontRightModule.setDesiredState(desiredStates[1]);
    rearLeftModule.setDesiredState(desiredStates[2]);
    rearRightModule.setDesiredState(desiredStates[3]);
  }

  public SwerveModuleState[] getModuleStates() {
    SwerveModuleState[] states = new SwerveModuleState[4];
    states[0] = frontLeftModule.getState();
    states[1] = frontRightModule.getState();
    states[2] = rearLeftModule.getState();
    states[3] = rearRightModule.getState();
    return states;
  }

  public SwerveModulePosition[] getModulePosition(){
    SwerveModulePosition[] positions = new SwerveModulePosition[4];
    positions[0] = frontLeftModule.getPosition();
    positions[1] = frontRightModule.getPosition();
    positions[2] = rearLeftModule.getPosition();
    positions[3] = rearRightModule.getPosition();
    return positions;
  }

  public void resetEncoders() {
    frontLeftModule.resetEncoders();
    frontRightModule.resetEncoders();
    rearLeftModule.resetEncoders();
    rearRightModule.resetEncoders();
  }

  public void stopModules(){
    frontLeftModule.stopModule();
    frontRightModule.stopModule();
    rearLeftModule.stopModule();
    rearRightModule.stopModule();
  }

  public void zeroHeading() { gyro.reset(); }
  public Rotation2d getRotation2d() { return gyro.getRotation2d();}
  public ChassisSpeeds getChassisSpeeds(){ return SwerveConstants.kDriveKinematics.toChassisSpeeds(frontLeftModule.getState(), frontRightModule.getState(), rearLeftModule.getState(), rearRightModule.getState());}


  @Override
  public void periodic() {
    odometry.update(
      gyro.getRotation2d(),
      new SwerveModulePosition[] {
        frontLeftModule.getPosition(),
        frontRightModule.getPosition(),
        rearLeftModule.getPosition(),
        rearRightModule.getPosition()
      });


    SmartDashboard.putNumber("Heading", gyro.getRotation2d().getDegrees() );
    SmartDashboard.putString("Robot Location", getPose().getTranslation().toString());
    SmartDashboard.putNumber("Front Left Angle Raw", frontLeftModule.getRawAngle());    
    SmartDashboard.putNumber("Front Right Angle Raw", frontRightModule.getRawAngle());    
    SmartDashboard.putNumber("Back Left Angle Raw", rearLeftModule.getRawAngle());    
    SmartDashboard.putNumber("Back Right Angle Raw", rearRightModule.getRawAngle());    
  }
}