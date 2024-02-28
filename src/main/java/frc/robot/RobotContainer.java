package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.cmdAuto_HoldShotAngle;
import frc.robot.commands.cmdAuto_IntakeNote;
import frc.robot.commands.cmdAuto_TurretPosition;
import frc.robot.commands.cmdIntake_Stop;
import frc.robot.commands.cmdIntake_TeleOp;
import frc.robot.commands.cmdShooter_Stop;
import frc.robot.commands.cmdShooter_TeleOp;
import frc.robot.commands.cmdShotAngle_TeleOp;
import frc.robot.commands.cmdSwerve_TeleOp;
import frc.robot.commands.cmdTurret_Stop;
import frc.robot.commands.cmdTurret_TeleOp;
import frc.robot.subsystems.subIntake;
import frc.robot.subsystems.subShooter;
import frc.robot.subsystems.subShotAngle;
import frc.robot.subsystems.subSwerve;
import frc.robot.subsystems.subTurret;

public class RobotContainer {
  // Driver Controllers
  private final CommandXboxController driverOne = new CommandXboxController(OperatorConstants.DriverOne);
  private final CommandXboxController driverTwo = new CommandXboxController(OperatorConstants.DriverTwo);

  // Subsystems
  public final subSwerve swerve = new subSwerve();
  public final subIntake intake = new subIntake();
  public final subShooter shooter = new subShooter();
  public final subShotAngle shotAngle = new subShotAngle();
  //private final subLimeLight limeLight = new subLimeLight();
  public final subTurret turret = new subTurret();

  private SendableChooser<Command> chooser = new SendableChooser<>();

  private final SlewRateLimiter xspeedLimiter = new SlewRateLimiter(2);
  private final SlewRateLimiter yspeedLimiter = new SlewRateLimiter(2);
  private final SlewRateLimiter rotLimiter = new SlewRateLimiter(2);

  public RobotContainer() {
    configureDriverOne();
    configureDriverTwo();

    // Named Commands
    //NamedCommands.registerCommand("Shooter Speaker", new cmdAuto_StaticShotAngle(shotAngle, Constants.ShotAngleConstants.SpeakerBaseShot).withTimeout(1));
    //NamedCommands.registerCommand("Shooter 50%", new cmdShooter_TeleOp(shooter, ()->0.5).withTimeout(1));
    //NamedCommands.registerCommand("Shooter 80%", new cmdShooter_TeleOp(shooter, ()->0.8).withTimeout(1));
    //NamedCommands.registerCommand("Stop Shooter", new InstantCommand(()->shooter.stop()));
    //NamedCommands.registerCommand("Run Intake", new cmdAuto_IntakeNote(intake));
    //NamedCommands.registerCommand("Feed Shooter", new RunCommand(() -> intake.forward()).withTimeout(2));
    //NamedCommands.registerCommand("Stop Intake", new RunCommand(() -> intake.stop()));
   
    //addAutoOptions();
  }

  private void configureDriverOne() {
    swerve.setDefaultCommand(
      new cmdSwerve_TeleOp(
          swerve,
          () -> xspeedLimiter.calculate(MathUtil.applyDeadband(-driverOne.getLeftY(), 0.005)),
          () -> yspeedLimiter.calculate(MathUtil.applyDeadband(-driverOne.getLeftX(), 0.005)),
          () -> rotLimiter.calculate(MathUtil.applyDeadband(-driverOne.getRightX(), 0.005)),
          () -> driverOne.leftBumper().getAsBoolean()));
    
    // Intake
    driverOne.rightBumper().onTrue(new cmdAuto_IntakeNote(intake));
    driverOne.rightBumper().onFalse(new cmdIntake_Stop(intake));

    driverOne.rightTrigger().onTrue(new cmdIntake_TeleOp(intake, ()->1));
    driverOne.rightTrigger().onFalse(new cmdIntake_Stop(intake));
    driverOne.leftTrigger().onTrue(new cmdIntake_TeleOp(intake, ()->-1));
    driverOne.leftTrigger().onFalse(new cmdIntake_Stop(intake));
    
    // Reset Sensors
    driverOne.start().onTrue(new InstantCommand(() -> swerve.zeroHeading()));
  }

  private void configureDriverTwo(){
    // Shot Angle
    //shotAngle.setDefaultCommand(new cmdShotAngle_TeleOp(shotAngle, () -> -MathUtil.applyDeadband(driverTwo.getRightY(), 0.02)*0.1));
    driverTwo.leftTrigger().onTrue(new cmdShotAngle_TeleOp(shotAngle, ()->-0.5));
    driverTwo.leftTrigger().onFalse(new cmdAuto_HoldShotAngle(shotAngle));

    driverTwo.rightTrigger().onTrue(new cmdShotAngle_TeleOp(shotAngle, ()->0.5));
    driverTwo.rightTrigger().onFalse(new cmdAuto_HoldShotAngle(shotAngle));

    //driverTwo.povUp().onTrue(new cmdAuto_StaticShotAngle(shotAngle, Constants.ShotAngleConstants.LowerLimit));
    //driverTwo.povUp().onFalse(new cmdAuto_StaticShotAngle(shotAngle, Constants.ShotAngleConstants.SpeakerBaseShot));
    //driverTwo.povRight().onTrue(new cmdAuto_StaticShotAngle(shotAngle, Constants.ShotAngleConstants.SafeZoneShot));
    //driverTwo.povRight().onFalse(new cmdAuto_StaticShotAngle(shotAngle, Constants.ShotAngleConstants.SpeakerBaseShot));
    //driverTwo.povDown().onTrue(new cmdAuto_StaticShotAngle(shotAngle, Constants.ShotAngleConstants.SpeakerBaseShot));
    //driverTwo.povDown().onFalse(new cmdAuto_StaticShotAngle(shotAngle, Constants.ShotAngleConstants.SpeakerBaseShot));

    // Turret
    turret.setDefaultCommand(new cmdTurret_TeleOp(turret, () -> MathUtil.applyDeadband(driverTwo.getLeftX()*.3, 0.02), ()->shotAngle.safeToTurret()));
    driverTwo.y().whileTrue(new cmdAuto_TurretPosition(turret, shotAngle, Constants.Turret.Front));
    driverTwo.y().onFalse(new cmdTurret_Stop(turret));
    driverTwo.y().onFalse(new cmdAuto_HoldShotAngle(shotAngle));

    driverTwo.a().whileTrue(new cmdAuto_TurretPosition(turret, shotAngle, Constants.Turret.Rear));
    driverTwo.a().onFalse(new cmdTurret_Stop(turret));
    driverTwo.a().onFalse(new cmdAuto_HoldShotAngle(shotAngle));

    driverTwo.x().whileTrue(new cmdAuto_TurretPosition(turret, shotAngle, Constants.Turret.Left));
    driverTwo.x().onFalse(new cmdTurret_Stop(turret));
    driverTwo.x().onFalse(new cmdAuto_HoldShotAngle(shotAngle));

    driverTwo.b().whileTrue(new cmdAuto_TurretPosition(turret, shotAngle, Constants.Turret.Right));
    driverTwo.b().onFalse(new cmdTurret_Stop(turret));
    driverTwo.b().onFalse(new cmdAuto_HoldShotAngle(shotAngle));
    
    // Shooter
    driverTwo.rightBumper().onTrue(new cmdShooter_TeleOp(shooter, ()->0.8));
    driverTwo.rightBumper().onFalse(new cmdShooter_Stop(shooter));
    //driverTwo.rightBumper().onFalse(new cmdAuto_StaticShotAngle(shotAngle, Constants.ShotAngleConstants.HigherLimit));

    driverTwo.leftBumper().onTrue(new cmdShooter_TeleOp(shooter, ()->0.5));
    driverTwo.leftBumper().onFalse(new cmdShooter_Stop(shooter));
    //driverTwo.leftBumper().onFalse(new cmdAuto_StaticShotAngle(shotAngle, Constants.ShotAngleConstants.HigherLimit));
  }

  private void addAutoOptions(){
    //chooser = AutoBuilder.buildAutoChooser();
    chooser.setDefaultOption("Do Nothing", new InstantCommand());

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