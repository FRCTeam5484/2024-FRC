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
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.cmdAuto_AlignToShoot;
import frc.robot.commands.cmdAuto_HoldShotAngle;
import frc.robot.commands.cmdAuto_IntakeNote;
import frc.robot.commands.cmdAuto_StaticShotAngle;
import frc.robot.commands.cmdAuto_TurretPosition;
import frc.robot.commands.cmdCancelCommands;
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
    NamedCommands.registerCommand("Shooter Speaker", new cmdAuto_StaticShotAngle(shotAngle, Constants.ShotAngleConstants.SpeakerBaseShot));
    NamedCommands.registerCommand("Shooter 60%", new cmdShooter_TeleOp(shooter, ()->0.6).withTimeout(2));
    NamedCommands.registerCommand("Shooter 80%", new cmdShooter_TeleOp(shooter, ()->0.8).withTimeout(2));
    NamedCommands.registerCommand("Shooter Reverse", new cmdShooter_TeleOp(shooter, ()->-0.1).withTimeout(0.5));
    NamedCommands.registerCommand("Stop Shooter", new cmdShooter_Stop(shooter));
    NamedCommands.registerCommand("Auto Intake", new cmdAuto_IntakeNote(intake));
    NamedCommands.registerCommand("Feed Shooter", new RunCommand(() -> intake.forward()).withTimeout(1));
    NamedCommands.registerCommand("Stop Intake", new cmdIntake_Stop(intake));
   
    addAutoOptions();
  }

  private void configureDriverOne() {
    /*
    swerve.setDefaultCommand(
      new cmdSwerve_TeleOp(
          swerve,
          () -> xspeedLimiter.calculate(MathUtil.applyDeadband(-driverOne.getLeftY(), 0.05)),
          () -> yspeedLimiter.calculate(MathUtil.applyDeadband(-driverOne.getLeftX(), 0.05)),
          () -> rotLimiter.calculate(MathUtil.applyDeadband(-driverOne.getRightX(), 0.05)),
          () -> driverOne.leftBumper().getAsBoolean()));
    */
    swerve.setDefaultCommand(
      new cmdSwerve_TeleOp(
          swerve,
          () -> MathUtil.applyDeadband(-driverOne.getLeftY(), 0.05),
          () -> MathUtil.applyDeadband(-driverOne.getLeftX(), 0.05),
          () -> rotLimiter.calculate(MathUtil.applyDeadband(-driverOne.getRightX(), 0.05))));
    
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

    new Trigger(() -> MathUtil.applyDeadband(driverTwo.getLeftY(), 0.02) < 0)
      .onTrue(new cmdShotAngle_TeleOp(shotAngle, ()->0.3))
      .onFalse(new cmdAuto_HoldShotAngle(shotAngle));

    new Trigger(() -> MathUtil.applyDeadband(driverTwo.getLeftY(), 0.02) > 0)
      .onTrue(new cmdShotAngle_TeleOp(shotAngle, ()->-0.3))
      .onFalse(new cmdAuto_HoldShotAngle(shotAngle));

    new Trigger(() -> Math.abs(MathUtil.applyDeadband(driverTwo.getRightX(), 0.02)) > 0.5)
      .onTrue(new cmdTurret_TeleOp(turret, () -> MathUtil.applyDeadband(driverTwo.getRightX()*.3, 0.02), ()->shotAngle.safeToTurret()))
      .onFalse(new cmdTurret_Stop(turret));
    
    //driverTwo.leftTrigger().onTrue(new cmdShotAngle_TeleOp(shotAngle, ()->-0.5));
    //driverTwo.leftTrigger().onFalse(new cmdAuto_HoldShotAngle(shotAngle));

    //driverTwo.rightTrigger().onTrue(new cmdShotAngle_TeleOp(shotAngle, ()->0.5));
    //driverTwo.rightTrigger().onFalse(new cmdAuto_HoldShotAngle(shotAngle));

    driverTwo.povLeft().whileTrue(new cmdAuto_StaticShotAngle(shotAngle, Constants.ShotAngleConstants.Amp));
    driverTwo.povLeft().onFalse(new cmdAuto_StaticShotAngle(shotAngle, Constants.ShotAngleConstants.SpeakerBaseShot));
    driverTwo.povDown().whileTrue(new cmdAuto_StaticShotAngle(shotAngle, Constants.ShotAngleConstants.SafeZoneShot));
    driverTwo.povDown().onFalse(new cmdAuto_StaticShotAngle(shotAngle, Constants.ShotAngleConstants.SpeakerBaseShot));
    driverTwo.povUp().whileTrue(new cmdAuto_StaticShotAngle(shotAngle, Constants.ShotAngleConstants.MaxPostition));
    driverTwo.povUp().onFalse(new cmdAuto_StaticShotAngle(shotAngle, Constants.ShotAngleConstants.SpeakerBaseShot));
    driverTwo.povRight().whileTrue(new cmdAuto_StaticShotAngle(shotAngle, Constants.ShotAngleConstants.SpeakerBaseShot));
    driverTwo.povRight().onFalse(new cmdAuto_StaticShotAngle(shotAngle, Constants.ShotAngleConstants.SpeakerBaseShot));

    // Turret
    //turret.setDefaultCommand(new cmdTurret_TeleOp(turret, () -> MathUtil.applyDeadband(driverTwo.getLeftX()*.3, 0.02), ()->shotAngle.safeToTurret()));
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
    driverTwo.rightBumper().onFalse(new cmdAuto_StaticShotAngle(shotAngle, Constants.ShotAngleConstants.SpeakerBaseShot));

    driverTwo.leftBumper().onTrue(new cmdShooter_TeleOp(shooter, ()->0.6));
    driverTwo.leftBumper().onFalse(new cmdShooter_Stop(shooter));
    driverTwo.leftBumper().onFalse(new cmdAuto_StaticShotAngle(shotAngle, Constants.ShotAngleConstants.SpeakerBaseShot));

    driverTwo.start().whileTrue(new cmdShooter_TeleOp(shooter, ()->0.35));
    driverTwo.leftBumper().onFalse(new cmdAuto_StaticShotAngle(shotAngle, Constants.ShotAngleConstants.SpeakerBaseShot));
    
    //driverTwo.start().whileTrue(new cmdAuto_AlignToShoot(shotAngle, turret, shooter));
    //driverTwo.start().onFalse(new cmdCancelCommands(intake, shooter, shotAngle, turret));

    driverTwo.back().onTrue(new cmdCancelCommands(intake, shooter, shotAngle, turret));
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