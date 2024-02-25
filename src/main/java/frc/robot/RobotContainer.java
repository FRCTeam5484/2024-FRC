package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.cmdAuto_IntakeNote;
import frc.robot.commands.cmdAuto_StaticShotAngle;
import frc.robot.commands.cmdAuto_TurretPosition;
import frc.robot.commands.cmdShooter_TeleOp;
import frc.robot.commands.cmdShotAngle_TeleOp;
import frc.robot.commands.cmdSwerve_TeleOp;
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
    NamedCommands.registerCommand("Shooter Speaker", new cmdAuto_StaticShotAngle(shotAngle, Constants.ShotAngleConstants.SpeakerBaseShot).withTimeout(1));
    NamedCommands.registerCommand("Shooter 50%", new cmdShooter_TeleOp(shooter, ()->0.5).withTimeout(1));
    NamedCommands.registerCommand("Shooter 80%", new cmdShooter_TeleOp(shooter, ()->0.8).withTimeout(1));
    NamedCommands.registerCommand("Stop Shooter", new InstantCommand(()->shooter.stop()));
    NamedCommands.registerCommand("Run Intake", new cmdAuto_IntakeNote(intake));
    NamedCommands.registerCommand("Feed Shooter", new RunCommand(() -> intake.forward()).withTimeout(2));
    NamedCommands.registerCommand("Stop Intake", new RunCommand(() -> intake.stop()));

    addAutoOptions();
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
    driverOne.rightBumper().whileTrue(new cmdAuto_IntakeNote(intake));
    driverOne.rightBumper().onFalse(new InstantCommand(()->intake.stop()));

    driverOne.rightTrigger().onTrue(new InstantCommand(()->intake.forward()));
    driverOne.rightTrigger().onFalse(new InstantCommand(()->intake.stop()));
    driverOne.leftTrigger().onTrue(new InstantCommand(()->intake.reverse()));
    driverOne.leftTrigger().onFalse(new InstantCommand(()->intake.stop()));
    
    // Reset Sensors
    driverOne.start().onTrue(new InstantCommand(() -> swerve.zeroHeading()));

    // Turret
    driverOne.y().onTrue(new cmdAuto_TurretPosition(turret, shotAngle, Constants.Turret.Front));
    driverOne.a().onTrue(new cmdAuto_TurretPosition(turret, shotAngle, Constants.Turret.Rear));
    driverOne.x().onTrue(new cmdAuto_TurretPosition(turret, shotAngle, Constants.Turret.Left));
    driverOne.b().onTrue(new cmdAuto_TurretPosition(turret, shotAngle, Constants.Turret.Right));

    // Shooter
    driverOne.rightBumper().onTrue(new cmdShooter_TeleOp(shooter, ()->0.8));
    driverOne.rightBumper().onFalse(new InstantCommand(()->shooter.stop()));

    driverOne.leftBumper().onTrue(new cmdShooter_TeleOp(shooter, ()->0.5));
    driverOne.leftBumper().onFalse(new InstantCommand(()->shooter.stop()));
  }

  private void configureDriverTwo(){
    // Shot Angle
    shotAngle.setDefaultCommand(new cmdShotAngle_TeleOp(shotAngle, () -> -MathUtil.applyDeadband(driverTwo.getRightY(), 0.02)*0.1));
    driverTwo.povUp().whileTrue(new cmdAuto_StaticShotAngle(shotAngle, Constants.ShotAngleConstants.LowerLimit));
    driverTwo.povUp().onFalse(new cmdAuto_StaticShotAngle(shotAngle, Constants.ShotAngleConstants.SpeakerBaseShot));
    driverTwo.povRight().whileTrue(new cmdAuto_StaticShotAngle(shotAngle, Constants.ShotAngleConstants.SafeZoneShot));
    driverTwo.povRight().onFalse(new cmdAuto_StaticShotAngle(shotAngle, Constants.ShotAngleConstants.SpeakerBaseShot));
    driverTwo.povDown().whileTrue(new cmdAuto_StaticShotAngle(shotAngle, Constants.ShotAngleConstants.SpeakerBaseShot));
    driverTwo.povDown().onFalse(new cmdAuto_StaticShotAngle(shotAngle, Constants.ShotAngleConstants.SpeakerBaseShot));

    // Turret
    turret.setDefaultCommand(new cmdTurret_TeleOp(turret, () -> MathUtil.applyDeadband(driverTwo.getLeftX()*.3, 0.02), ()->shotAngle.safeToTurret()));
    driverTwo.y().whileTrue(new cmdAuto_TurretPosition(turret, shotAngle, Constants.Turret.Front));
    driverTwo.a().whileTrue(new cmdAuto_TurretPosition(turret, shotAngle, Constants.Turret.Rear));
    driverTwo.x().whileTrue(new cmdAuto_TurretPosition(turret, shotAngle, Constants.Turret.Left));
    driverTwo.b().whileTrue(new cmdAuto_TurretPosition(turret, shotAngle, Constants.Turret.Right));
    
    // Shooter
    driverTwo.rightBumper().onTrue(new cmdShooter_TeleOp(shooter, ()->0.8));
    driverTwo.rightBumper().onFalse(new InstantCommand(()-> shooter.stop()));
    driverTwo.rightBumper().onFalse(new cmdAuto_StaticShotAngle(shotAngle, Constants.ShotAngleConstants.HigherLimit));

    driverTwo.leftBumper().onTrue(new cmdShooter_TeleOp(shooter, ()->0.5));
    driverTwo.leftBumper().onFalse(new InstantCommand(()-> shooter.stop()));
    driverTwo.leftBumper().onFalse(new cmdAuto_StaticShotAngle(shotAngle, Constants.ShotAngleConstants.HigherLimit));
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