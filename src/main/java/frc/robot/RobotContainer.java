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
import frc.robot.commands.cmdShooter_Shoot;
import frc.robot.commands.cmdShooter_Stop;
import frc.robot.commands.cmdShotAngle_SetPoint;
import frc.robot.commands.cmdShotAngle_TeleOp;
import frc.robot.commands.cmdSwerve_TeleOp;
import frc.robot.commands.cmdTurret_SetPosition;
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

    // Named Commands
    NamedCommands.registerCommand("Shooter Speaker", new cmdShotAngle_SetPoint(shotAngle, Constants.ShotAngleConstants.HigherLimit).withTimeout(1));
    NamedCommands.registerCommand("Shooter 50%", new RunCommand(() -> shooter.set(0.5)).withTimeout(1));
    NamedCommands.registerCommand("Shooter 80%", new RunCommand(() -> shooter.set(0.8)).withTimeout(1));
    NamedCommands.registerCommand("Stop Shooter", new RunCommand(() -> shooter.set(0)).withTimeout(0.5));
    NamedCommands.registerCommand("Run Intake", new RunCommand(() -> intake.forward()).withTimeout(1.5));
    NamedCommands.registerCommand("Feed Shooter", new RunCommand(() -> intake.forward()).withTimeout(2));
    NamedCommands.registerCommand("Stop Intake", new RunCommand(() -> intake.stop()).withTimeout(0.5));

    addAutoOptions();

    //NamedCommands.registerCommand("Shooter 50%", shooter.teleOp(0.5));
  }

  private void configureDriverOne() {
    swerve.setDefaultCommand(
      new cmdSwerve_TeleOp(
          swerve,
          () -> xspeedLimiter.calculate(MathUtil.applyDeadband(-driverOne.getLeftY(), 0.005)),
          () -> yspeedLimiter.calculate(MathUtil.applyDeadband(-driverOne.getLeftX(), 0.005)),
          () -> rotLimiter.calculate(MathUtil.applyDeadband(-driverOne.getRightX(), 0.005))));

    // Intake
    driverOne.leftTrigger().whileTrue(new RunCommand(() -> intake.teleOp(-1)));
    driverOne.leftTrigger().onFalse(new RunCommand(() -> intake.stop()));

    driverOne.rightTrigger().whileTrue(new RunCommand(() -> intake.teleOp(1)));
    driverOne.rightTrigger().onFalse(new RunCommand(() -> intake.stop()));
    
    // Reset Sensors
    driverOne.start().onTrue(new InstantCommand(() -> swerve.zeroHeading()));
    driverOne.back().onTrue(new InstantCommand(() -> turret.resetEncoder()));

    // Turret
    driverOne.y().onTrue(new cmdTurret_SetPosition(turret, shotAngle, Constants.Turret.Front));
    driverOne.a().onTrue(new cmdTurret_SetPosition(turret, shotAngle, Constants.Turret.Rear));
    driverOne.x().onTrue(new cmdTurret_SetPosition(turret, shotAngle, Constants.Turret.Left));
    driverOne.b().onTrue(new cmdTurret_SetPosition(turret, shotAngle, Constants.Turret.Right));

    // Shooter
    driverOne.rightBumper().onTrue(new cmdShooter_Shoot(shooter, 0.8));
    driverOne.rightBumper().onFalse(new cmdShooter_Stop(shooter));

    driverOne.leftBumper().onTrue(new cmdShooter_Shoot(shooter, 0.5));
    driverOne.leftBumper().onFalse(new cmdShooter_Stop(shooter));
  }

  private void configureDriverTwo(){
    // Shot Angle
    shotAngle.setDefaultCommand(new cmdShotAngle_TeleOp(shotAngle, () -> -MathUtil.applyDeadband(driverTwo.getRightY(), 0.02)*0.1));
    driverTwo.povUp().whileTrue(new cmdShotAngle_SetPoint(shotAngle, Constants.ShotAngleConstants.LowerLimit));
    driverTwo.povUp().onFalse(new cmdShotAngle_SetPoint(shotAngle, Constants.ShotAngleConstants.HigherLimit));
    driverTwo.povRight().whileTrue(new cmdShotAngle_SetPoint(shotAngle, Constants.ShotAngleConstants.SafeZoneShot));
    driverTwo.povRight().onFalse(new cmdShotAngle_SetPoint(shotAngle, Constants.ShotAngleConstants.HigherLimit));
    driverTwo.povDown().whileTrue(new cmdShotAngle_SetPoint(shotAngle, Constants.ShotAngleConstants.HigherLimit));
    driverTwo.povDown().onFalse(new cmdShotAngle_SetPoint(shotAngle, Constants.ShotAngleConstants.HigherLimit));

    // Turret
    turret.setDefaultCommand(new cmdTurret_TeleOp(turret, () -> shotAngle.getPosition(), () -> MathUtil.applyDeadband(driverTwo.getLeftX()*.3, 0.02), true));
    driverTwo.y().whileTrue(new cmdTurret_SetPosition(turret, shotAngle, Constants.Turret.Front));
    driverTwo.a().whileTrue(new cmdTurret_SetPosition(turret, shotAngle, Constants.Turret.Rear));
    driverTwo.x().whileTrue(new cmdTurret_SetPosition(turret, shotAngle, Constants.Turret.Left));
    driverTwo.b().whileTrue(new cmdTurret_SetPosition(turret, shotAngle, Constants.Turret.Right));
    // Shooter
    driverTwo.rightBumper().onTrue(new cmdShooter_Shoot(shooter, 0.8));
    driverTwo.rightBumper().onFalse(new cmdShooter_Stop(shooter));
    driverTwo.rightBumper().onFalse(new cmdShotAngle_SetPoint(shotAngle, Constants.ShotAngleConstants.HigherLimit));

    driverTwo.leftBumper().onTrue(new cmdShooter_Shoot(shooter, 0.5));
    driverTwo.leftBumper().onFalse(new cmdShooter_Stop(shooter));
    driverTwo.leftBumper().onFalse(new cmdShotAngle_SetPoint(shotAngle, Constants.ShotAngleConstants.HigherLimit));
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