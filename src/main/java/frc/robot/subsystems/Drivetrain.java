/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.RobotMap;
import frc.robot.commands.TeleopDrive;

/**
 * Add your docs here.
 */
public class Drivetrain extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  private WPI_TalonSRX frontleftM;
  private WPI_TalonSRX frontrightM;
  private WPI_TalonSRX rearleftS;
  private WPI_TalonSRX rearrightS;

  private Encoder leftEncoder;
  private Encoder rightEncoder;

  private DifferentialDrive drive;

  private PIDController leftDistanceControl;

  public Drivetrain() {
    // Initialize motor controllers
    frontleftM = new WPI_TalonSRX(RobotMap.DRIVE_LEFT_M);
    frontrightM = new WPI_TalonSRX(RobotMap.DRIVE_RIGHT_M);
    rearleftS = new WPI_TalonSRX(RobotMap.DRIVE_LEFT_S);
    rearrightS = new WPI_TalonSRX(RobotMap.DRIVE_RIGHT_S);

    // Encoders
    leftEncoder = new Encoder(ENC_L_A, ENC_L_B, true);
    leftEncoder.setDistancePerPulse(ENC_FEET_PER_PULSE);
    leftEncoder.setPIDSourceType(PIDSourceType.kDisplacement);

    rightEncoder = new Encoder(ENC_R_A, ENC_R_B, false);
    rightEncoder.setDistancePerPulse(ENC_FEET_PER_PULSE);
  
    // Motor controllers follow a 'master/slave' configuration
    // When you set the master controller speed, the slave controller automatically follows
    rearleftS.follow(frontleftM);
    rearrightS.follow(frontrightM);
  
    // Initialize driving controller
    drive = new DifferentialDrive(frontleftM, frontrightM);

    // Set up PID control
    leftDistanceControl = new PIDController(1, 0, 0, leftEncoder, frontleftM);
    leftDistanceControl.setAbsoluteTolerance(0.1);
  }  

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    setDefaultCommand(new TeleopDrive());
  }

  public void arcadeDrive(double throttle, double steer) {
    drive.arcadeDrive(throttle, steer);
  }

  public void setTargetDistance(double feet) {
    leftEncoder.reset();
    leftDistanceControl.reset();
    leftDistanceControl.setSetpoint(feet);
    leftDistanceControl.enable();
    // todo: right side


    
  }

  public boolean onTarget() {
    return leftDistanceControl.onTarget();// && rightDistanceControl.onTarget();
  }

  public void disablePID() {
    leftDistanceControl.disable();
    // todo: right side
  }

}
