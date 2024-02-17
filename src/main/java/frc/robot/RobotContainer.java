package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.commands.PathPlannerAuto;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.cmdIntake_Run;
import frc.robot.commands.cmdIntake_Stop;
import frc.robot.commands.cmdShooter_Shoot;
import frc.robot.commands.cmdShooter_Stop;
import frc.robot.commands.cmdShotAngle_Lower;
import frc.robot.commands.cmdShotAngle_Raise;
import frc.robot.commands.cmdShotAngle_SetPoint;
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
  private final subLimeLight limeLight = new subLimeLight();
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
          () -> MathUtil.applyDeadband(driverOne.getLeftY(), 0.005),
          () -> MathUtil.applyDeadband(driverOne.getLeftX(), 0.005),
          () -> MathUtil.applyDeadband(driverOne.getRightX(), 0.005)));
    
    // Shot Angle
    driverOne.povUp().whileTrue(new cmdShotAngle_Raise(shotAngle));
    driverOne.povUp().onFalse(new cmdIntake_Stop(intake));

    driverOne.povDown().whileTrue(new cmdShotAngle_Lower(shotAngle));
    driverOne.povDown().onFalse(new cmdShotAngle_Stop(shotAngle));

    // Turret
    driverOne.povLeft().whileTrue(new cmdTurret_TeleOp(turret, () -> .2));
    driverOne.povLeft().onFalse(new cmdTurret_TeleOp(turret, () -> 0));

    driverOne.povRight().whileTrue(new cmdTurret_TeleOp(turret, () -> -.2));
    driverOne.povRight().onFalse(new cmdTurret_TeleOp(turret, () -> 0));

    // Intake
    driverOne.leftTrigger().whileTrue(new RunCommand(() -> intake.teleOp(-driverOne.getLeftTriggerAxis())));
    driverOne.leftTrigger().onFalse(new RunCommand(() -> intake.stop()));

    driverOne.rightTrigger().whileTrue(new RunCommand(() -> intake.teleOp(driverOne.getRightTriggerAxis())));
    driverOne.rightTrigger().onFalse(new RunCommand(() -> intake.stop()));

    driverOne.rightBumper().onTrue(new cmdShooter_Shoot(shooter));
    driverOne.rightBumper().onFalse(new cmdShooter_Stop(shooter));

    driverOne.leftBumper().onTrue(new cmdIntake_Run(intake));
    driverOne.leftBumper().onFalse(new cmdIntake_Stop(intake));
    
    driverOne.a().onTrue(new InstantCommand(() -> swerve.zeroHeading()));
    driverOne.b().onTrue(new cmdShotAngle_SetPoint(shotAngle, 0.65));
    driverOne.y().onTrue(new cmdShotAngle_SetPoint(shotAngle, 0.61));
    driverOne.x().onTrue(new InstantCommand(() -> turret.resetEncoder()));
  }

  private void configureDriverTwo(){
    //turret.setDefaultCommand(new cmdTurret_TeleOp(turret, () -> MathUtil.applyDeadband(driverTwo.getLeftX(), 0.01)));
    driverTwo.y().onTrue(new cmdTurret_SetPosition(turret, Constants.Turret.Front));
    driverTwo.a().onTrue(new cmdTurret_SetPosition(turret, Constants.Turret.Rear));
    driverTwo.x().onTrue(new cmdTurret_SetPosition(turret, Constants.Turret.Left));
    driverTwo.b().onTrue(new cmdTurret_SetPosition(turret, Constants.Turret.Right));
    //shotAngle.setDefaultCommand(new cmdShotAngle_TeleOp(shotAngle, () -> MathUtil.applyDeadband(driverTwo.getRightY(), 0.01)));
    //driverTwo.leftBumper().onTrue(new InstantCommand(() -> shooter.set(0.5)));
    //driverTwo.leftBumper().onFalse(new InstantCommand(() -> shooter.stop()));
    //driverTwo.leftBumper().onTrue(new cmdShooter_Shoot(feeder, shooter));
    //driverTwo.leftBumper().onFalse(new cmdShooter_Stop(feeder, shooter));
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