package io.github.yxsnake.pisces.mybatis.plus.service;

import cn.hutool.extra.spring.SpringUtil;
import org.springframework.stereotype.Service;

/**
 * @author snake
 * @description
 * @since 2024/9/24 1:39
 */

@Service("sdss")
public class SysDataScopeService {

    private RemoteDataScopeService remoteDataScopeService = SpringUtil.getBean(RemoteDataScopeService.class);

    /**
     * 获取角色自定义权限语句
     *
     * @param roleId 角色ID
     * @return 返回角色的自定义权限语句，如果没有找到则返回 null
     */
    public String getRoleCustom(Long roleId) {
        return remoteDataScopeService.getRoleCustom(roleId);
    }

    /**
     * 获取部门和下级权限语句
     *
     * @param deptId 部门ID
     * @return 返回部门及其下级的权限语句，如果没有找到则返回 null
     */
    public String getDeptAndChild(Long deptId) {
        return remoteDataScopeService.getDeptAndChild(deptId);
    }

}

