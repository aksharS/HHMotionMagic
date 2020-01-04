package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.commands.Elevate;

import java.util.Optional;

public class Elevator extends Subsystem {
    private WPI_TalonSRX MainMotor;

    private DigitalInput topHFX;
    private Boolean isAtTop;
    private DigitalInput bottomHFX;
    private Boolean isAtBottom;

    private enum ElevatorControlState {
        MANUAL_POSITIONING,
        MOTION_MAGIC,
        CALIBRATION
    };

    private ElevatorControlState currentState;

    private Optional<Double> ELEVATOR_BOTTOM_POSITION = Optional.empty();
    private Optional<Double> ELEVATOR_TOP_POSITION = Optional.empty();

    private Optional<Double> ELEVATOR_TARGET_POSITION = Optional.empty();

    public Elevator() {
        this.MainMotor = new WPI_TalonSRX(RobotMap.elevatorMotor);
        
        MainMotor.configFactoryDefault();
        MainMotor.setInverted(true);
        MainMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, RobotMap.elevatorLoopIdx, RobotMap.CtreTimeoutMs);
        MainMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, RobotMap.CtreTimeoutMs);
        MainMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, RobotMap.CtreTimeoutMs);

        /* Set the peak and nominal outputs */
        MainMotor.configNominalOutputForward(0, RobotMap.CtreTimeoutMs);
        MainMotor.configNominalOutputReverse(0, RobotMap.CtreTimeoutMs);
        MainMotor.configPeakOutputForward(1, RobotMap.CtreTimeoutMs);
        MainMotor.configPeakOutputReverse(-1, RobotMap.CtreTimeoutMs);

        /* Set Motion Magic gains in slot0 - see documentation */
        MainMotor.selectProfileSlot(RobotMap.elevatorMotorSlotIdx, RobotMap.elevatorLoopIdx);
        MainMotor.config_kF(RobotMap.elevatorMotorSlotIdx, RobotMap.kF, RobotMap.CtreTimeoutMs);
        MainMotor.config_kP(RobotMap.elevatorMotorSlotIdx, RobotMap.kP, RobotMap.CtreTimeoutMs);
        MainMotor.config_kI(RobotMap.elevatorMotorSlotIdx, RobotMap.kI, RobotMap.CtreTimeoutMs);
        MainMotor.config_kD(RobotMap.elevatorMotorSlotIdx, RobotMap.kD, RobotMap.CtreTimeoutMs);

        MainMotor.setSelectedSensorPosition(0, RobotMap.kPidLoopIdx, RobotMap.CtreTimeoutMs);

        /* Set acceleration and vcruise velocity - see documentation */
        MainMotor.configMotionCruiseVelocity(35000, RobotMap.CtreTimeoutMs);
        MainMotor.configMotionAcceleration(55000, RobotMap.CtreTimeoutMs);

        topHFX = new DigitalInput(RobotMap.topHallEffect);
        bottomHFX = new DigitalInput(RobotMap.bottomHallEffect);

        this.currentState = ElevatorControlState.CALIBRATION;
    }

    public void update(){

    }

    public WPI_TalonSRX getMainMotor() {
        return MainMotor;
    }

    public Boolean getTopHFX() {
        return topHFX.get();
    }

    public Boolean getBottomHFX() {
        return bottomHFX.get();
    }

    public int getElevatorPosition() {
        try {
            return MainMotor.getSelectedSensorPosition(0);
        } catch (NullPointerException e) {
            return -1;
        }
    }

    public ElevatorControlState getState() {
        return currentState;
    }

    public void Calibrate() {
        if(getBottomHFX()) {
            ELEVATOR_BOTTOM_POSITION = Optional.of((double) getElevatorPosition());
            if(!ELEVATOR_TOP_POSITION.isPresent()) {
                ELEVATOR_TOP_POSITION = Optional.of((double) getElevatorPosition() + RobotMap.elevatorHeight * RobotMap.elevatorTicksPerInch);
            }
        } else if(getTopHFX()) {
            ELEVATOR_TOP_POSITION = Optional.of((double) getElevatorPosition());
            if(!ELEVATOR_BOTTOM_POSITION.isPresent()) {
                ELEVATOR_BOTTOM_POSITION = Optional.of((double) getElevatorPosition() - RobotMap.elevatorHeight * RobotMap.elevatorTicksPerInch);
            }
        }
    }

    public void setBottomPosition(Optional<Double> value) {
        ELEVATOR_BOTTOM_POSITION = value;
    }

    public Optional<Double> getBottomPosition() {
        return ELEVATOR_BOTTOM_POSITION;
    }

    public void setTopPosition(Optional<Double> value) {
        ELEVATOR_TOP_POSITION = value;
    }

    public Optional<Double> getELEVATOR_TOP_POSITION() {
        return ELEVATOR_TOP_POSITION;
    }

    public void setElevatorMotor(double power) {
        if (!getBottomHFX() && power > 0) {
            MainMotor.set(power);
        }
        else if (!getBottomHFX() && power <= 0) {
            MainMotor.set(0);
        }
        else if (!getTopHFX() && power < 0) {
            MainMotor.set(power);
        }
        else if (!getTopHFX() && power >= 0) {
            MainMotor.set(0);
        }
        else{
            MainMotor.set(power);
        }
    }

    public Boolean isCalibrated() {
        return ELEVATOR_BOTTOM_POSITION.isPresent() && ELEVATOR_TOP_POSITION.isPresent();
    }

    @Override
    public void initDefaultCommand() {
        setDefaultCommand(new Elevate());
    }

    public void MagicSetMotor(double setpoint){
        MainMotor.set(ControlMode.MotionMagic, setpoint);
    }
}
