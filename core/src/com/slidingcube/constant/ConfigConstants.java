package com.slidingcube.constant;

/**
 * @author bgamard.
 */

public class ConfigConstants {
    private ConfigConstants() {
        // private class
    }

    public static float GRAVITY = -20f;
    public static float PPM = 25;
    public static float CAMERA_MARGIN = 0.02f;

    public static float GROUND_FRICTION = 0.1f;
    public static int GROUND_WIDTH = 1000;

    public static float HELP_FORCE_MULTIPLIER = 100f;
    public static float JUMP_VERTICAL = 4000f;
    public static float JUMP_HORIZONTAL = 4000f;
    public static int JUMP_INTERVAL = 400;
    public static final float PLAYER_DENSITY = 20f;
    public static final float HELP_FORCE = 0.5f;
    public static float JUMP_PUSH = -500f;

    public static Integer FIXTURE_FOOT = 0;
}
