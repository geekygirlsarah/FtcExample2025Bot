package org.firstinspires.ftc.teamcode;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {

    // Insert drivetrain config here
    public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {

        c.frontLeftName.set("FL");
        c.frontRightName.set("FR");
        c.backLeftName.set("BL");
        c.backRightName.set("BR");
        c.frontLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);
    });

    // Insert localization config here
    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("Pinpoint");
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_SWINGARM_POD);
        c.xPodOffset.set(-1.2383162881445697);
        c.yPodOffset.set(5.184031208669107);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });

    // Insert Foresignt config here
    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.27134792355640636);
                Controller secondaryTranslationalForward = Controller.proportional(0.10025580720403943);
                Controller primaryTranslationalLateral = Controller.proportional(0.3716731848778171);
                Controller secondaryTranslationalLateral = Controller.proportional(0.13732331052194627);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.01840003596080439));
                c.brake.set(Controller.proportionalFeedforward(0.01564003056668373));

                c.headingFeedback.set(Controller.proportional(4.985764198776721));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.04930055539685405, 0.007504503636486141));

                c.linearBrakeCoefficients.set(Matrix.diag(0.07206668097375384, 0.022204709298704463));
                c.quadraticBrakeCoefficients.set(Matrix.diag(0.002140207253790586, 0.003226533166542073));

                c.maxAchievableForwardVelocity.set(52.96131521327267);
                c.maxAchievableStrafeVelocity.set(41.2799720260029);
                c.naturalForwardDeceleration.set(40.831232528359166);
                c.naturalStrafeDeceleration.set(63.77811274657652);
            }
    );

    // Insert Follower create() here
    public static Follower create(HardwareMap h) {
        return new Follower(
                new PinpointLocalizer(h, localizerConfig),
                new Mecanum(h, drivetrainConfig),
                new Foresight(foresightConfig)
        );
    }
}