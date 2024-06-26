package frc.robot;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.util.Units;

public class Constants {
    public static final class OperatorConstants {
      public static final int DriverOne = 0;
      public static final int DriverTwo = 1;
    }
    public static final class DriveConstants {
      public static final double kMaxSpeedMetersPerSecond = 12;//9;
      public static final double kMaxAngularSpeed = 4 * Math.PI;
    }
    public static final class SwerveConstants {

        public static final double kTrackWidth = Units.inchesToMeters(26.5);
        public static final double kWheelBase = Units.inchesToMeters(26.5);

        public static final SwerveDriveKinematics kDriveKinematics = new SwerveDriveKinematics(
            new Translation2d(kWheelBase / 2, kTrackWidth / 2),
            new Translation2d(kWheelBase / 2, -kTrackWidth / 2),
            new Translation2d(-kWheelBase / 2, kTrackWidth / 2),
            new Translation2d(-kWheelBase / 2, -kTrackWidth / 2));

        public static final int kDrivingMotorCurrentLimit = 60;
        public static final int kTurningMotorCurrentLimit = 40;

        public static final double kDrivingMotorFreeSpeedRps = 5676 / 60;
        public static final double kWheelDiameterMeters = Units.inchesToMeters(4);
        public static final double kWheelCircumferenceMeters = kWheelDiameterMeters * Math.PI;
        public static final double kDrivingMotorReduction = 5.14;//6.75; //4.714285714;
        public static final double kDriveWheelFreeSpeedRps = kDrivingMotorFreeSpeedRps / kDrivingMotorReduction;

        public static final double kDrivingEncoderPositionFactor = (kWheelDiameterMeters * Math.PI) / kDrivingMotorReduction; // meters
        public static final double kDrivingEncoderVelocityFactor = kDrivingEncoderPositionFactor / 60.0; // meters per second

        public static final double kTurningEncoderPositionFactor = (2 * Math.PI); // radians
        public static final double kTurningEncoderVelocityFactor = (2 * Math.PI) / 60.0; // radians per second

        public static final double kTurningEncoderPositionPIDMinInput = 0; // radians
        public static final double kTurningEncoderPositionPIDMaxInput = kTurningEncoderPositionFactor; // radians

        public static final double kDrivingP = 0.05;//0.04;
        public static final double kDrivingI = 0;
        public static final double kDrivingD = 0;
        public static final double kDrivingFF = 1 / kDriveWheelFreeSpeedRps;
        public static final double kDrivingMinOutput = -1;
        public static final double kDrivingMaxOutput = 1;

        public static final double kTurningP = 0.01;
        public static final double kTurningI = 0;
        public static final double kTurningD = 0;
        public static final double kTurningFF = 0;
        public static final double kTurningMinOutput = -1;
        public static final double kTurningMaxOutput = 1;
    }
    public static final class Turret{
      public static final double Rear = 0;
      public static final double Right = -40;
      public static final double Front = 80;
      public static final double Left = 40;
    }
    public static final class ShotAngleConstants {
      public static final double MaxPostition = 461;
      public static final double TurretSafe = 550;      
      public static final double LowestPosition = 600;
      
      public static final double Amp = 500;
      public static final double SpeakerBaseShot = 490;
      public static final double SafeZoneShot = 580;
    }
    public static final class Shoot{
      public static final double Amp = 1600;
      public static final double SafeZoneShot = 5000;
      public static final double SpeakerBase = 4000;
    }
}