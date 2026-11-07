package org.firstinspires.ftc.teamcode.examples;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.panels.PanelsUtil;

@TeleOp(group = "Examples", name = "Example Robot-Centric TeleOp")
public class ExampleRobotCentricTeleOp extends OpMode {

    private Follower follower;

    @Override
    public void init() {
        PanelsUtil.init();
        follower = Constants.create(hardwareMap);
    }

    @Override
    public void loop() {
        double forward = -gamepad1.left_stick_y;
        double lateral = gamepad1.left_stick_x;
        double turn = gamepad1.right_stick_x;

        follower.manual(forward, lateral, turn);
        follower.update();

        PanelsUtil.drawRobot(follower.pose());

        telemetry.addData("X", follower.pose().x());
        telemetry.addData("Y", follower.pose().y());
        telemetry.addData("Heading", Math.toDegrees(follower.pose().heading()));
        telemetry.addData("Follower Mode", follower.mode());
        PanelsUtil.update(telemetry);
    }
}
