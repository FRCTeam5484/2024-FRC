package frc.robot;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.cmdIntake_Run;
import frc.robot.commands.cmdShooter_Shoot;
import frc.robot.commands.cmdShotAngle_TeleOp;
import frc.robot.commands.cmdSwerve_TeleOp;
import frc.robot.commands.cmdTurret_TeleOp;
import frc.robot.subsystems.subFeeder;
import frc.robot.subsystems.subIntake;
import frc.robot.subsystems.subShooter;
import frc.robot.subsystems.subShotAngle;
import frc.robot.subsystems.subSwerve;
import frc.robot.subsystems.subTurret;

public class RobotContainer {
  private final CommandXboxController driverOne = new CommandXboxController(OperatorConstants.DriverOne);
  private final CommandXboxController driverTwo = new CommandXboxController(OperatorConstants.DriverTwo);
  private final subSwerve swerve = new subSwerve();
  private final subFeeder feeder = new subFeeder();
  private final subIntake intake = new subIntake();
  private final subShooter shooter = new subShooter();
  private final subShotAngle shotAngle = new subShotAngle();
  private final subTurret turret = new subTurret();

  SendableChooser<Command> chooser = new SendableChooser<>();

  public RobotContainer() {
    configureDriverOne();
    configureDriverTwo();
    addAutoOptions();
  }

  private void configureDriverOne() {
    swerve.setDefaultCommand(
      new cmdSwerve_TeleOp(
          swerve,
          () -> MathUtil.applyDeadband(driverOne.getLeftY(), 0.01),
          () -> MathUtil.applyDeadband(driverOne.getLeftX(), 0.01),
          () -> MathUtil.applyDeadband(driverOne.getRightX(), 0.01)));
    
    driverOne.leftBumper().whileTrue(new RunCommand(() -> intake.forward()));
    driverOne.leftBumper().whileFalse(new RunCommand(() -> intake.stop()));
  }

  private void configureDriverTwo(){
    turret.setDefaultCommand(new cmdTurret_TeleOp(turret, () -> MathUtil.applyDeadband(driverTwo.getLeftX(), 0.01)));
    shotAngle.setDefaultCommand(new cmdShotAngle_TeleOp(shotAngle, () -> MathUtil.applyDeadband(driverTwo.getRightY(), 0.01)));
    driverTwo.leftBumper().whileTrue(new cmdShooter_Shoot(feeder, shooter));
  }

  private void addAutoOptions(){
    chooser.setDefaultOption("Do Nothing", new InstantCommand());
    SmartDashboard.putData("Auto Options", chooser);
  }

  public Command getAutonomousCommand() {
    try { return chooser.getSelected(); } 
    catch (NullPointerException ex) { 
      DriverStation.reportError("auto choose NULL somewhere in getAutonomousCommand in RobotContainer.java", null);
      return new InstantCommand();
    }
  }
}