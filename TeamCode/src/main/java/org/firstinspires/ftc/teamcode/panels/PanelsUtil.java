package org.firstinspires.ftc.teamcode.panels;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.field.Canvas;
import com.bylazar.field.FieldManager;
import com.bylazar.field.Line;
import com.bylazar.field.PanelsField;
import com.bylazar.field.Style;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.math.Pose;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Reusable utility class for FTC Panels integration.
 * Manages field drawing (robot tracking on field map) and synchronized telemetry.
 */
@Configurable
public class PanelsUtil {

    public static double ROBOT_WIDTH = 18.0;
    public static double ROBOT_LENGTH = 18.0;
    public static double ROBOT_RADIUS = 9.0;
    public static boolean DRAW_ROBOT_BOX = true;
    public static boolean DRAW_ROBOT_CIRCLE = true;
    public static boolean DRAW_HEADING_LINE = true;
    public static double HEADING_LINE_LENGTH = 12.0;
    public static String DEFAULT_ROBOT_COLOR = "#00E5FF";
    public static String DEFAULT_HEADING_COLOR = "#FFD600";

    private static boolean initialized = false;

    /**
     * Initializes Panels field preset for Pedro Pathing coordinate system.
     */
    public static void init() {
        FieldManager field = PanelsField.INSTANCE.getField();
        field.setOffsets(PanelsField.INSTANCE.getPresets().getPEDRO_PATHING());
        initialized = true;
    }

    /**
     * Returns the Panels TelemetryManager instance.
     */
    public static TelemetryManager getTelemetry() {
        return PanelsTelemetry.INSTANCE.getTelemetry();
    }

    /**
     * Returns the Panels FieldManager instance.
     */
    public static FieldManager getField() {
        return PanelsField.INSTANCE.getField();
    }

    /**
     * Draws the robot's current pose on the field map using the default color.
     *
     * @param pose The current robot pose (x, y, heading).
     */
    public static void drawRobot(Pose pose) {
        drawRobot(pose, DEFAULT_ROBOT_COLOR, DEFAULT_HEADING_COLOR);
    }

    /**
     * Draws the robot's current pose on the field map with specified colors.
     *
     * @param pose The current robot pose (x, y, heading).
     * @param robotColor Color for the robot body/outline (hex or color name).
     * @param headingColor Color for the heading indicator line.
     */
    public static void drawRobot(Pose pose, String robotColor, String headingColor) {
        if (!initialized) {
            init();
        }

        if (pose == null) return;

        FieldManager field = PanelsField.INSTANCE.getField();
        double x = pose.x();
        double y = pose.y();
        double heading = pose.heading();

        // 1. Draw robot circular footprint if enabled
        if (DRAW_ROBOT_CIRCLE) {
            field.moveCursor(x, y);
            field.setStyle(PanelsField.INSTANCE.getTRANSPARENT(), robotColor, 0.2);
            field.circle(ROBOT_RADIUS);
        }

        // 2. Draw robot rotated bounding box if enabled
        if (DRAW_ROBOT_BOX) {
            double halfW = ROBOT_WIDTH / 2.0;
            double halfL = ROBOT_LENGTH / 2.0;

            double cos = Math.cos(heading);
            double sin = Math.sin(heading);

            // Calculate 4 corners relative to center (x, y)
            // Front-Left
            double flX = x + (halfL * cos - halfW * sin);
            double flY = y + (halfL * sin + halfW * cos);

            // Front-Right
            double frX = x + (halfL * cos + halfW * sin);
            double frY = y + (halfL * sin - halfW * cos);

            // Back-Right
            double brX = x + (-halfL * cos + halfW * sin);
            double brY = y + (-halfL * sin - halfW * cos);

            // Back-Left
            double blX = x + (-halfL * cos - halfW * sin);
            double blY = y + (-halfL * sin + halfW * cos);

            field.setStyle(PanelsField.INSTANCE.getTRANSPARENT(), robotColor, 0.25);
            // Draw rectangle edges
            field.moveCursor(flX, flY);
            field.line(frX, frY);

            field.moveCursor(frX, frY);
            field.line(brX, brY);

            field.moveCursor(brX, brY);
            field.line(blX, blY);

            field.moveCursor(blX, blY);
            field.line(flX, flY);
        }

        // 3. Draw heading indicator line
        if (DRAW_HEADING_LINE) {
            double targetX = x + Math.cos(heading) * HEADING_LINE_LENGTH;
            double targetY = y + Math.sin(heading) * HEADING_LINE_LENGTH;

            field.moveCursor(x, y);
            field.setStyle(PanelsField.INSTANCE.getTRANSPARENT(), headingColor, 0.3);
            field.line(targetX, targetY);
        }
    }

    /**
     * Draws a line between two points on the field.
     */
    public static void drawLine(double x1, double y1, double x2, double y2, String color, double width) {
        if (!initialized) init();
        FieldManager field = PanelsField.INSTANCE.getField();
        field.moveCursor(x1, y1);
        field.setStyle(PanelsField.INSTANCE.getTRANSPARENT(), color, width);
        field.line(x2, y2);
    }

    /**
     * Draws a point/circle at specified coordinates.
     */
    public static void drawPoint(double x, double y, double radius, String fillColor) {
        if (!initialized) init();
        FieldManager field = PanelsField.INSTANCE.getField();
        field.moveCursor(x, y);
        field.setStyle(fillColor, PanelsField.INSTANCE.getTRANSPARENT(), 0.0);
        field.circle(radius);
    }

    /**
     * Updates Panels field canvas and telemetry (both Driver Station and Panels Dashboard).
     *
     * @param telemetry FTC SDK Telemetry instance.
     */
    public static void update(Telemetry telemetry) {
        PanelsField.INSTANCE.getField().update();
        if (telemetry != null) {
            PanelsTelemetry.INSTANCE.getTelemetry().update(telemetry);
        } else {
            PanelsTelemetry.INSTANCE.getTelemetry().update();
        }
    }
}
