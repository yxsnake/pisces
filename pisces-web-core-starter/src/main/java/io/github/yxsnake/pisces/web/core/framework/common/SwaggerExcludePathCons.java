package io.github.yxsnake.pisces.web.core.framework.common;

import com.google.common.collect.Lists;

import java.util.List;

public class SwaggerExcludePathCons {

    public static final List<String> EXCLUDE_PATHS;

    static {
        EXCLUDE_PATHS = Lists.newArrayList();
        EXCLUDE_PATHS.add("/user/permissions");
        EXCLUDE_PATHS.add("/user/roles");
        EXCLUDE_PATHS.add("/doc.html");
        EXCLUDE_PATHS.add("/webjars/**");
        EXCLUDE_PATHS.add("/v3/api-docs/*");
        EXCLUDE_PATHS.add("/favicon.ico");
        EXCLUDE_PATHS.add("/swagger-ui/**");
    }
}
