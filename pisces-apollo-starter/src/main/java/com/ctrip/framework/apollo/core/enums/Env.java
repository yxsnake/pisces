package com.ctrip.framework.apollo.core.enums;

import com.google.common.base.Preconditions;

public enum Env {

  LOCAL, DEV, TEST,FWS, FAT, UAT, LPT, PRO, TOOLS, KA, UNKNOWN;

  public static Env fromString(String env) {
    Env environment = EnvUtils.transformEnv(env);
    Preconditions.checkArgument(environment != UNKNOWN, String.format("Env %s is invalid", env));
    return environment;
  }

}