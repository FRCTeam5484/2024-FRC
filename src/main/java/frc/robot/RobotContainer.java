package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.cmdIntake_Stop;
import frc.robot.commands.cmdShooter_Shoot;
import frc.robot.commands.cmdShooter_Stop;
import frc.robot.commands.cmdShotAngle_Lower;
import frc.robot.commands.cmdShotAngle_Raise;
import frc.robot.commands.cmdShotAngle_SetPoint;
import frc.robot.commands.cmdShotAngle_HoldPosition;
import frc.robot.commands.cmdShotAngle_Stop;
import frc.robot.commands.cmdSwerve_TeleOp;
import frc.robot.commands.cmdTurret_SetPosition;
import frc.robot.commands.cmdTurret_TeleOp;
import frc.robot.subsystems.subIntake;
import frc.robot.subsystems.subLimeLight;
import frc.robot.subsystems.subShooter;
import frc.robot.subsystems.subShotAngle;
import frc.robot.subsystems.subSwerve;
import frc.robot.subsystems.subTurret;

public class RobotContainer {
  // Driver Controllers
  private final CommandXboxController driverOne = new CommandXboxController(OperatorConstants.DriverOne);
  private final CommandXboxController driverTwo = new CommandXboxController(OperatorConstants.DriverTwo);

  // Subsystems
  private final subSwerve swerve = new subSwerve();
  private final subIntake intake = new subIntake();
  private final subShooter shooter = new subShooter();
  private final subShotAngle shotAngle = new subShotAngle();
  //private final subLimeLight limeLight = new subLimeLight();
  private final subTurret turret = new subTurret();

  private SendableChooser<Command> chooser = new SendableChooser<>();

  private final SlewRateLimiter xspeedLimiter = new SlewRateLimiter(3);
  private final SlewRateLimiter yspeedLimiter = new SlewRateLimiter(3);
  private final SlewRateLimiter rotLimiter = new SlewRateLimiter(3);

  public RobotContainer() {
    configureDriverOne();
    configureDriverTwo();
    addAutoOptions();
  }

  private void configureDriverOne() {
    swerve.setDefaultCommand(
      new cmdSwerve_TeleOp(
          swerve,
          () -> xspeedLimiter.calculate(MathUtil.applyDeadband(driverOne.getLeftY(), 0.005)),
          () -> yspeedLimiter.calculate(MathUtil.applyDeadband(driverOne.getLeftX(), 0.005)),
          () -> rotLimiter.calculate(MathUtil.applyDeadband(driverOne.getRightX(), 0.005))));

    // Intake
    driverOne.leftTrigger().whileTrue(new RunCommand(() -> intake.teleOp(-1)));
    driverOne.leftTrigger().onFalse(new RunCommand(() -> intake.stop()));

    driverOne.rightTrigger().whileTrue(new RunCommand(() -> intake.teleOp(1)));
    driverOne.rightTrigger().onFalse(new RunCommand(() -> intake.stop()));
    
    // Reset Sensors
    driverOne.start().onTrue(new InstantCommand(() -> swerve.zeroHeading()));
    driverOne.back().onTrue(new InstantCommand(() -> turret.resetEncoder()));

    // Turret
    //driverOne.y().onTrue(new cmdTurret_SetPosition(turret, shotAngle, Constants.Turret.Front));
    //driverOne.a().onTrue(new cmdTurret_SetPosition(turret, shotAngle, Constants.Turret.Rear));
    //driverOne.x().onTrue(new cmdTurret_SetPosition(turret, shotAngle, Constants.Turret.Left));
    //driverOne.b().onTrue(new cmdTurret_SetPosition(turret, shotAngle, Constants.Turret.Right));

    // Shot Angle
    driverOne.povUp().onTrue(new cmdShotAngle_SetPoint(shotAngle, Constants.ShotAngleConstants.LowerLimit));
    driverOne.povRight().onTrue(new cmdShotAngle_SetPoint(shotAngle, Constants.ShotAngleConstants.SafeZoneShot));
    driverOne.povDown().onTrue(new cmdShotAngle_SetPoint(shotAngle, Constants.ShotAngleConstants.HigherLimit));

    // Shooter
    driverOne.rightBumper().onTrue(new cmdShooter_Shoot(shooter, 0.8));
    driverOne.rightBumper().onFalse(new cmdShooter_Stop(shooter));

    driverOne.leftBumper().onTrue(new cmdShooter_Shoot(shooter, 0.5));
    driverOne.leftBumper().onFalse(new cmdShooter_Stop(shooter));
  }

  private void configureDriverTwo(){
    // Shot Angle
    driverTwo.povUp().onTrue(new cmdShotAngle_SetPoint(shotAngle, Constants.ShotAngleConstants.LowerLimit));
    driverTwo.povRight().onTrue(new cmdShotAngle_SetPoint(shotAngle, Constants.ShotAngleConstants.SafeZoneShot));
    driverTwo.povDown().onTrue(new cmdShotAngle_SetPoint(shotAngle, Constants.ShotAngleConstants.HigherLimit));
    //driverTwo.povUp().whileTrue(new cmdShotAngle_Raise(shotAngle));
    //driverTwo.povUp().onFalse(new cmdShotAngle_HoldPosition(shotAngle));

    //driverTwo.povDown().whileTrue(new cmdShotAngle_Lower(shotAngle));
    //driverTwo.povDown().onFalse(new cmdShotAngle_HoldPosition(shotAngle));

    // Turret
    //driverTwo.y().onTrue(new cmdTurret_SetPosition(turret, shotAngle, Constants.Turret.Front));
    //driverTwo.a().onTrue(new cmdTurret_SetPosition(turret, shotAngle, Constants.Turret.Rear));
    //driverTwo.x().onTrue(new cmdTurret_SetPosition(turret, shotAngle, Constants.Turret.Left));
    //driverTwo.b().onTrue(new cmdTurret_SetPosition(turret, shotAngle, Constants.Turret.Right));

    driverTwo.b().whileTrue(new cmdTurret_TeleOp(turret, () -> shotAngle.getPosition(), () -> .2));
    driverTwo.b().onFalse(new cmdTurret_TeleOp(turret, () -> shotAngle.getPosition(), () -> 0));

    driverTwo.x().whileTrue(new cmdTurret_TeleOp(turret, () -> shotAngle.getPosition(), () -> -.2));
    driverTwo.x().onFalse(new cmdTurret_TeleOp(turret, () -> shotAngle.getPosition(), () -> 0));

    // Shooter
    driverTwo.rightBumper().onTrue(new cmdShooter_Shoot(shooter, 0.8));
    driverTwo.rightBumper().onFalse(new cmdShooter_Stop(shooter));

    driverTwo.leftBumper().onTrue(new cmdShooter_Shoot(shooter, 0.5));
    driverTwo.leftBumper().onFalse(new cmdShooter_Stop(shooter));
  }

  private void addAutoOptions(){
    chooser = AutoBuilder.buildAutoChooser();
    //chooser.setDefaultOption("Do Nothing", new InstantCommand());

    SmartDashboard.putData("Auto Options", chooser);
  }

  public Command getAutonomousCommand() {
    return chooser.getSelected();
    //return new PathPlannerAuto("Test Auto 1");
    //try { return chooser.getSelected(); } 
    //catch (NullPointerException ex) { 
    //  DriverStation.reportError("auto choose NULL somewhere in getAutonomousCommand in RobotContainer.java", null);
    //  return new InstantCommand();
    //}
  }
}