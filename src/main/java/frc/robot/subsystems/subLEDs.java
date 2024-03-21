package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class subLEDs extends SubsystemBase {
  private final AddressableLED m_led = new AddressableLED(0);
  private AddressableLEDBuffer m_ledBuffer = new AddressableLEDBuffer(20);
  private int m_rainbowFirstPixelHue;

  public subLEDs() {
    m_led.setLength(m_ledBuffer.getLength());
  }

  public void yellow() {
    for (var i = 0; i < m_ledBuffer.getLength(); i++) {
      m_ledBuffer.setRGB(i, 255, 255, 0);
    }
  }
  public void orange() {
    for (var i = 0; i < m_ledBuffer.getLength(); i++) {
      m_ledBuffer.setRGB(i, 255, 128, 0);
    }
  }
  public void red() {
    for (var i = 0; i < m_ledBuffer.getLength(); i++) {
      m_ledBuffer.setRGB(i, 255, 0, 0);
    }
  }
  public void blue() {
    for (var i = 0; i < m_ledBuffer.getLength(); i++) {
      m_ledBuffer.setRGB(i, 0, 0, 255);
    }
  }
  public void green() {
    for (var i = 0; i < m_ledBuffer.getLength(); i++) {
      m_ledBuffer.setRGB(i, 0, 255, 0);
    }
  }
  public void off() {
    for (var i = 0; i < m_ledBuffer.getLength(); i++) {
      m_ledBuffer.setRGB(i, 0, 0, 0);
    }
  }
  public void stop(){
    m_led.stop();
  }

  public void setRainbow(){
    for (var i = 0; i < m_ledBuffer.getLength(); i++) {
      final var hue = (m_rainbowFirstPixelHue + (i * 180 / m_ledBuffer.getLength())) % 180;
      m_ledBuffer.setHSV(i, hue, 255, 128);
    }
    m_rainbowFirstPixelHue += 3;
    m_rainbowFirstPixelHue %= 180;
  }


  @Override
  public void periodic() {
    m_led.setData(m_ledBuffer);
  }
}
