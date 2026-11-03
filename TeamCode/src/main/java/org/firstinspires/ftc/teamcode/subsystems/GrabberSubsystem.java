package org.firstinspires.ftc.teamcode.subsystems;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.commands.Commands;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class GrabberSubsystem {

    // Variables
    private final Servo grabberServo;
    private final DcMotor armMotor;

    // We don't actually know these but we'd probably use them in the real world
    private final int ARM_DOWN_POSITION_TICKS = 0;
    private final int ARM_UP_POSITION_TICKS = 0;
    private final double GRABBER_FORWARD_POSITION = 1.0;
    private final double GRABBER_REVERSE_POSITION = 0.0;

    private final double ARM_UP_SPEED = 0.80;
    private final double ARM_DOWN_SPEED = -0.40;
    private final double ARM_STOP_SPEED = 0.4;
    // Functions
    public GrabberSubsystem(HardwareMap hardwareMap) {
        this.grabberServo = hardwareMap.get(Servo.class, "grabber");
        this.armMotor = hardwareMap.get(DcMotor.class, "arm");

        this.armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    // Low-level hardware actions
    public void close() {
        grabberServo.setPosition(GRABBER_FORWARD_POSITION);
    }

    public void open() {
        grabberServo.setPosition(GRABBER_REVERSE_POSITION);
    }

    public void raiseUp() {
        armMotor.setPower(ARM_UP_SPEED);
    }

    public void lower() {
        armMotor.setPower(ARM_DOWN_SPEED);
    }
    public void stop() {
        armMotor.setPower(ARM_STOP_SPEED);
    }

    // High-level Ivy command factories
    public Command closeGrabber() {
        return Command.build()
                .setStart(() -> close())
                .requiring(grabberServo);
    }

    public Command openGrabber() {
        return Command.build()
                .setStart(() -> open())
                .requiring(grabberServo);
    }

    public Command raiseArmCommand() {
        return Command.build()
                .setStart(() -> raiseUp())
                .requiring(armMotor);
    }

    public Command lowerArmCommand() {
        return Command.build()
                .setStart(() -> lower())
                .requiring(armMotor);
    }

    public Command stopArmCommand() {
        return Command.build()
                .setStart(() -> stop())
                .requiring(armMotor);
    }
}
