package org.firstinspires.ftc.teamcode.subsystems;

import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class ElevatorSubsystem {
    // Variables
    private final DcMotor elevatorMotor;
    private final Servo boxRotatorServo;
    private static final double ELEVATOR_UP_SPEED = 1.0;
    private static final double ELEVATOR_DOWN_SPEED = -0.5;
    private static final double ROTATOR_HOLD_POSITION = 1.0;
    private static final double ROTATOR_GIVE_POSITION = 0.5;
    private static final double ELEVATOR_HOLD_POWER = 0.2;

    // Functions/Actions
    public ElevatorSubsystem(HardwareMap hardwareMap) {
        this.elevatorMotor = hardwareMap.get(DcMotor.class, "elevator");
        this.boxRotatorServo = hardwareMap.get(Servo.class, "rotatorBox");

        this.elevatorMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    // Low-level hardware actions
    public void lift() {
        elevatorMotor.setPower(ELEVATOR_UP_SPEED);
    }

    public void lower() {
        elevatorMotor.setPower(ELEVATOR_DOWN_SPEED);
    }

    public void stop() {
        elevatorMotor.setPower(ELEVATOR_HOLD_POWER);
    }

    public void hold() {
        boxRotatorServo.setPosition(ROTATOR_HOLD_POSITION);
    }

    public void dump() {
        boxRotatorServo.setPosition(ROTATOR_GIVE_POSITION);
    }

    // High-level Ivy command factories
    public Command liftBox() {
        return Command.build()
                .setStart(() -> lift())
                .requiring(elevatorMotor);
    }

    public Command lowerBox() {
        return Command.build()
                .setStart(() -> lower())
                .requiring(elevatorMotor);
    }

    public Command stopBox() {
        return Command.build()
                .setStart(() -> stop())
                .requiring(elevatorMotor);
    }

    public Command boxHoldPosition() {
        return Command.build()
                .setStart(() -> hold())
                .requiring(boxRotatorServo);
    }

    public Command boxDumpPosition() {
        return Command.build()
                .setStart(() -> dump())
                .requiring(boxRotatorServo);
    }
}
