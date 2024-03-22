package frc.robot;

import java.util.Optional;

import javax.lang.model.util.ElementScanner14;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class Robot extends TimedRobot {
  private Command m_autonomousCommand;
  private RobotContainer m_robotContainer;

  @Override
  public void robotInit() {
    m_robotContainer = new RobotContainer();
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {
    m_robotContainer.blinkin.teamColorsWaves();
  }

  @Override
  public void disabledExit() {}

  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  @Override
  public void autonomousPeriodic() {
    if(DriverStation.getAlliance().isPresent()){
      Optional<Alliance> ally = DriverStation.getAlliance();
      if(ally.get() == Alliance.Blue){
        m_robotContainer.blinkin.breathBlue();
      }
      else{
        m_robotContainer.blinkin.breathRed();
      }
    }
    else{
      m_robotContainer.blinkin.breathGray();
    }
  }

  @Override
  public void autonomousExit() {}

  @Override
  public void teleopInit() {
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  @Override
  public void teleopPeriodic() {
    if(m_robotContainer.limeLight.hasTarget() && m_robotContainer.intake.hasNote() && m_robotContainer.limeLight.readyToFire()){
      m_robotContainer.blinkin.green();
    }
    else if(m_robotContainer.limeLight.hasTarget() && m_robotContainer.intake.hasNote()){
      m_robotContainer.blinkin.orange();
    }
    else if(m_robotContainer.intake.hasNote()){
      m_robotContainer.blinkin.blue();
    }
    else{
      m_robotContainer.blinkin.off();
    }
  }

  @Override
  public void teleopExit() {}

  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void testPeriodic() {}

  @Override
  public void testExit() {}
}
