package com.android.launcher;

public class C001Common {
    public static final int AUTO_START_TEST = 4097;

    public static final int AUTO_STOP_TEST = 4098;

    public static final String ECHO_FILE_CPU0 = "sys/devices/system/cpu/cpufreq/policy0/scaling_governor";

    public static final String ECHO_FILE_CPU4 = "sys/devices/system/cpu/cpufreq/policy4/scaling_governor";

    public static final String GET_HDMI0_DET_INFO = "cat /sys/class/fyl_gpios/fyl_gpios/fyl_hdmi_0_insert";

    public static final String GET_HDMI1_DET_INFO = "cat /sys/class/fyl_gpios/fyl_gpios/fyl_hdmi_1_insert";

    public static final String GET_HDMI_0_INFO = "cat /sys/class/fyl_gpios/fyl_gpios/hdmi_i_0_6911";

    public static final String GET_HDMI_1_INFO = "cat /sys/class/fyl_gpios/fyl_gpios/hdmi_i_1_6911";

    public static final String GET_LINE_DET_INFO = "cat /sys/class/fyl_gpios/fyl_gpios/fyl_line_insert";

    public static final String GET_MIC_DET_INFO = "cat /sys/class/fyl_gpios/fyl_gpios/fyl_mic_insert";

    public static final String INTERACTIVE = "interactive";

    public static final String ONDEMAND = "ondemand";

    public static final String PATH_6911_UPDATE_FILE = "LT6911C_EVB_HDMI2MIPI.bin";

    public static final String PATH_6911_UPGRADE_0 = "/sys/class/fyl_gpios/fyl_gpios/hdmi_i_0_upgrade";

    public static final String PATH_6911_UPGRADE_1 = "/sys/class/fyl_gpios/fyl_gpios/hdmi_i_1_upgrade";

    public static final String PREF_AUTO_TEST_RESULT = "auto_test_tag_result_";

    public static final String PREF_HDMI_1_STATE = "hdmi_1_upgrade_state";

    public static final String PREF_HDMI_2_STATE = "hdmi_2_upgrade_state";

    public static final String PREF_HOT_SWAP_SIM = "hot_swap_sim";

    public static final String PREF_SINGLE_TEST_RESULT = "single_test_tag_result_";

    public static final String RESULT_6911_UPGRADE_FAILED = "failed";

    public static final String RESULT_6911_UPGRADE_SUCCESS = "success";

    public static final String RESULT_6911_UPGRADE_UNKNOWN = "unknown";

    public static final String RESULT_6911_UPGRADE_UPGRADE = "upgrade";

    public static final String SP_FILE_NAME = "C002_SP";

    public static final String SP_IS_FIRST_OPEN_APP = "is_first_open_app";

    public static final boolean TEST_AUTO = true;

    public static final String TEST_NAME = "TestName";

    public static final int TEST_STATE_FAILED = 0;

    public static final int TEST_STATE_NO_TEST = -1;

    public static final int TEST_STATE_SUCCESS = 1;

    public static final String TEST_TAG = "TestTag";
}

