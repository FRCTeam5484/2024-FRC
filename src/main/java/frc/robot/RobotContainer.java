package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.cmdAuto_CorrectHeading;
import frc.robot.commands.cmdAuto_IntakeNote;
import frc.robot.commands.cmdAuto_LimelightTargeting;
import frc.robot.commands.cmdAuto_TurretPosition;
import frc.robot.commands.cmdCancelCommands;
import frc.robot.commands.cmdClimb_Stop;
import frc.robot.commands.cmdClimb_TeleOp;
import frc.robot.commands.cmdIntake_Stop;
import frc.robot.commands.cmdIntake_TeleOp;
import frc.robot.commands.cmdShooter_Stop;
import frc.robot.commands.cmdShooter_TeleOp;
import frc.robot.commands.cmdSwerve_TeleOp;
import frc.robot.commands.cmdTurret_Stop;
import frc.robot.commands.cmdTurret_TeleOp;
import frc.robot.subsystems.subClimb;
import frc.robot.subsystems.subIntake;
import frc.robot.subsystems.subLimeLight;
import frc.robot.subsystems.subShooter;
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
  private final subLimeLight limeLight = new subLimeLight();
  public final subTurret turret = new subTurret();
  //public final subClimb climb = new subClimb();

  private SendableChooser<Command> chooser = new SendableChooser<>();

  public RobotContainer() {
    configureDriverOne();
    configureDriverTwo();

    // Named Commands
    NamedCommands.registerCommand("Shooter 60%", new cmdShooter_TeleOp(shooter, ()->0.6).withTimeout(2));
    NamedCommands.registerCommand("Shooter 80%", new cmdShooter_TeleOp(shooter, ()->0.8).withTimeout(2));
    NamedCommands.registerCommand("Shooter Reverse", new cmdShooter_TeleOp(shooter, ()->-0.1).withTimeout(0.5));
    NamedCommands.registerCommand("Stop Shooter", new cmdShooter_Stop(shooter));
    NamedCommands.registerCommand("Auto Intake", new cmdAuto_IntakeNote(intake));
    NamedCommands.registerCommand("Feed Shooter", new cmdIntake_TeleOp(intake, ()->1).withTimeout(1));
    NamedCommands.registerCommand("Stop Intake", new cmdIntake_Stop(intake));
    NamedCommands.registerCommand("Gryo Amp Side", new cmdAuto_CorrectHeading(swerve, 59));
    NamedCommands.registerCommand("Gryo Amp Source", new cmdAuto_CorrectHeading(swerve, 55));
   
    addAutoOptions();
  }
  private void configureDriverOne() {
    swerve.setDefaultCommand(
      new cmdSwerve_TeleOp(
          swerve,
          () -> MathUtil.applyDeadband(-driverOne.getLeftY(), 0.05),
          () -> MathUtil.applyDeadband(-driverOne.getLeftX(), 0.05),
          () -> MathUtil.applyDeadband(-driverOne.getRightX(), 0.05)));
    
    // Intake
    driverOne.rightBumper().onTrue(new cmdAuto_IntakeNote(intake));
    driverOne.rightBumper().onFalse(new cmdIntake_Stop(intake));

    driverOne.rightTrigger().onTrue(new cmdIntake_TeleOp(intake, ()->1));
    driverOne.rightTrigger().onFalse(new cmdIntake_Stop(intake));
    driverOne.leftTrigger().onTrue(new cmdIntake_TeleOp(intake, ()->-1));
    driverOne.leftTrigger().onFalse(new cmdIntake_Stop(intake));

    // LimeLight
    driverOne.b().whileTrue(new cmdAuto_LimelightTargeting(turret, limeLight));
    driverOne.b().onFalse(new cmdTurret_Stop(turret));
    
    // Reset Sensors
    driverOne.start().onTrue(new InstantCommand(() -> swerve.zeroHeading()));
  }

  private void configureDriverTwo(){
    // Climb
    //new Trigger(() -> Math.abs(MathUtil.applyDeadband(driverTwo.getLeftY(), 0.01)) > 0.1 || Math.abs(MathUtil.applyDeadband(driverTwo.getRightY(), 0.01)) > 0.1)
    //  .whileTrue(new cmdClimb_TeleOp(climb, ()->-driverTwo.getLeftY(), ()->-driverTwo.getRightY()))
    //  .onFalse(new cmdClimb_Stop(climb));
    
    // Turret
    new Trigger(() -> Math.abs(MathUtil.applyDeadband(driverTwo.getRightTriggerAxis(), 0.02)) > 0.1)
      .whileTrue(new cmdTurret_TeleOp(turret, () -> MathUtil.applyDeadband(driverTwo.getRightTriggerAxis()*.5, 0.01)))
      .onFalse(new cmdTurret_Stop(turret));

    new Trigger(() -> Math.abs(MathUtil.applyDeadband(driverTwo.getLeftTriggerAxis(), 0.02)) > 0.1)
      .whileTrue(new cmdTurret_TeleOp(turret, () -> MathUtil.applyDeadband(-driverTwo.getLeftTriggerAxis()*.5, 0.01)))
      .onFalse(new cmdTurret_Stop(turret));

    driverTwo.y().whileTrue(new cmdAuto_TurretPosition(turret, Constants.Turret.Front));
    driverTwo.y().onFalse(new cmdTurret_Stop(turret));

    driverTwo.a().whileTrue(new cmdAuto_TurretPosition(turret, Constants.Turret.Rear));
    driverTwo.a().onFalse(new cmdTurret_Stop(turret));

    driverTwo.x().whileTrue(new cmdAuto_TurretPosition(turret, Constants.Turret.Left));
    driverTwo.x().onFalse(new cmdTurret_Stop(turret));

    driverTwo.b().whileTrue(new cmdAuto_TurretPosition(turret, Constants.Turret.Right));
    driverTwo.b().onFalse(new cmdTurret_Stop(turret));
    
    // Shooter
    driverTwo.rightBumper().onTrue(new cmdShooter_TeleOp(shooter, ()->0.8));
    driverTwo.rightBumper().onFalse(new cmdShooter_Stop(shooter));

    driverTwo.leftBumper().onTrue(new cmdShooter_TeleOp(shooter, ()->0.6));
    driverTwo.leftBumper().onFalse(new cmdShooter_Stop(shooter));

    driverTwo.start().whileTrue(new cmdShooter_TeleOp(shooter, ()->0.45));
    driverTwo.back().onTrue(new cmdCancelCommands(intake, shooter, turret));
  }

  private void addAutoOptions(){
    chooser = AutoBuilder.buildAutoChooser();
    SmartDashboard.putData("Auto Options", chooser);
  }

  public Command getAutonomousCommand() {
    return chooser.getSelected();
  }
}